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
package pl.slawas.helpers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * 
 * FileUtils
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 */
public class FileUtils {

	/**
	 * Add a package name prefix if the name is not absolute Remove leading "/"
	 * if name is absolute
	 * 
	 * @param c
	 *           klasa, na podstwie której nazwy (nazwy pakietu) zostanie
	 *           zbudowana ścieżka do pliku
	 * @param name
	 *           nazwa pliku
	 * @return przebudowana nazwa pliku (z dolaczoną ścieżką pakietu)
	 */
	public static String resolveName(Class<?> c, String name) {
		if (name == null) {
			return name;
		}
		if (!name.startsWith("/")) {
			while (c.isArray()) {
				c = c.getComponentType();
			}
			String baseName = c.getName();
			int index = baseName.lastIndexOf('.');
			if (index != -1) {
				name = baseName.substring(0, index).replace('.', '/')
						+ "/" + name;
			}
		} else {
			name = name.substring(1);
		}
		return "/" + name;
	}

	/**
	 * Czytanie strumienia pliku
	 * 
	 * @param clazz
	 *           klasa pomocnicza ułatwiająca lokalizację pliku
	 * @param fileName
	 *           nazwa pliku
	 * @return czytany strumień zawatrtości pliku
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferedReader(Class<?> clazz,
			String fileName) throws FileNotFoundException {
		BufferedReader d = null;
		URL resource = clazz.getResource(fileName);
		if (resource == null) {
			FileInputStream fis = new FileInputStream(fileName);
			try {
				d = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			try {
				d = new BufferedReader(new InputStreamReader(clazz
						.getResourceAsStream(fileName), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return d;
	}
}
