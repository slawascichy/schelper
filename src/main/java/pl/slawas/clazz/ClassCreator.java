/*
 * Slawas.pl Copyright &copy; 2007-2012 
 * http://slawas.pl 
 * All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL SĹ�AWOMIR CICHY BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.slawas.clazz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;
import sun.tools.java.CompilerError;

/**
 * 
 * ClassCreator - dynamiczne tworzenie klas, na podstawie źródła‚a danych
 * zawartego w {@link StringBuffer}. Przykład zastosowania można znależć w
 * implementacji testów:
 * 
 * <pre>
 * Date today = new Date();
 * String todayMillis = Long.toString(today.getTime());
 * String todayClass = &quot;z_&quot; + todayMillis;
 * 
 * StringBuffer source = new StringBuffer();
 * source.append(&quot;public class &quot; + todayClass + &quot;{&quot;);
 * source.append(&quot; public void doit() {&quot;);
 * source.append(&quot; System.out.println(\&quot;&quot; + todayMillis + &quot;\&quot;);&quot;);
 * source.append(&quot; }}\n&quot;);
 * 
 * try {
 * 	ClassCreator cc = new ClassCreator(&quot;./target/&quot;,
 * 			ClassCreatorTest.class.getClassLoader());
 * 	System.out.println(&quot;Running &quot; + todayClass + &quot;:\n\n&quot;);
 * 	Class&lt;?&gt; params[] = {};
 * 	Object paramsObj[] = {};
 * 	Class&lt;?&gt; thisClass = cc.generate(todayClass, source);
 * 	Object iClass = thisClass.newInstance();
 * 	Method thisMethod = thisClass.getDeclaredMethod(&quot;doit&quot;, params);
 * 	thisMethod.invoke(iClass, paramsObj);
 * } catch (Exception e) {
 * 	logger.error(&quot;Blad testu:&quot;, e);
 * 	result = &quot;BAD&quot;;
 * }
 * </pre>
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1.2.2 $
 * 
 */
public class ClassCreator implements Serializable {

	private static final long serialVersionUID = -214103884698278796L;

	private final File classesDir;

	final protected static Logger logger = LoggerFactory
			.getLogger(ClassCreator.class);

	private transient final URLClassLoader classLoader;

	public ClassCreator(String dynamicClassLocation, ClassLoader externalLoader)
			throws MalformedURLException {
		classesDir = new File(dynamicClassLocation);
		classLoader = new URLClassLoader(
				new URL[] { classesDir.toURI().toURL() }, externalLoader);

	}

	public Class<?> generate(String className, StringBuffer source)
			throws IOException, ClassNotFoundException {
		/* utworzenie pliku źródła */
		FileWriter aWriter = null;
		try {
			aWriter = new FileWriter(classesDir + "/" + className + ".java",
					true);
			aWriter.write(source.toString());
			aWriter.flush();
		} finally {
			if (aWriter != null) {
				aWriter.close();
			}
		}
		/* kompilacja źródła */
		String[] sourceFile = { (new StringBuffer()).append(classesDir)
				.append("/").append(className).append(".java").toString() };
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			com.sun.tools.javac.Main.compile(sourceFile);
			if (!(baos.toString().indexOf("error") == -1)) {
				logger.error("Blad kompilacji:\n{}", baos.toString());
				throw new CompilerError("Blad kompilacji zrodla danych");
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
		}
		return this.classLoader.loadClass(className);
	}

}
