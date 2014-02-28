/*
 * Slawas.pl Copyright &copy; 2007-2012 
 * http://slawas.pl 
 * All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL SŁAWOMIR CICHY BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.slawas.test.clazz;

import java.lang.reflect.Method;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.clazz.ClassCreator;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class ClassCreatorTest extends TestCase {

	final protected static Logger logger = LoggerFactory.getLogger(ClassCreatorTest.class);
	
	@Test
	public void testOracleSearcher() {

		String result = "OK";

		Date today = new Date();
		String todayMillis = Long.toString(today.getTime());
		String todayClass = "z_" + todayMillis;

		StringBuffer source = new StringBuffer();
		source.append("public class " + todayClass + "{");
		source.append(" public void doit() {");
		source.append(" System.out.println(\"" + todayMillis + "\");");
		source.append(" }}\n");

		try {
			ClassCreator cc = new ClassCreator("./target/", ClassCreatorTest.class.getClassLoader());
			System.out.println("Running " + todayClass + ":\n\n");
			Class<?> params[] = {};
			Object paramsObj[] = {};
			Class<?> thisClass = cc.generate(todayClass, source);
			Object iClass = thisClass.newInstance();
			Method thisMethod = thisClass.getDeclaredMethod("doit", params);
			thisMethod.invoke(iClass, paramsObj);
		} catch (Exception e) {
			logger.error("Blad testu:", e);
			result = "BAD";
		}

		assert result.equals("OK") : "Test zakonczyl sie porazka";

	}

}
