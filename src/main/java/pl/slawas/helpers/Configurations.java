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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;
import pl.slawas.twl4j.logger.LogLevel;

public class Configurations {

	public static LogLevel logLevel = LogLevel.INFO;

	private static Logger log = LoggerFactory.getSystemLogger(
			Configurations.class, logLevel);

	public static final int MAX_DEBUG_LINE_PRESENTATION = 3;

	/**
	 * Metoda ladujaca czytajaca zmienne srodowiskowe konsoli systemu (shell-a,
	 * konsoli)
	 * 
	 * @return wlasciwosci konsoli systemu
	 * @throws IOException
	 */
	public static Properties loadSystemProperties() throws IOException {
		Process p = null;
		Properties envVars = new Properties();
		Runtime r = Runtime.getRuntime();
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("windows 9") > -1) {
			p = r.exec("command.com /c set");
		} else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows") > -1)) {
			// thanks to Juan Fran for the xp fix!
			p = r.exec("cmd.exe /c set");
		} else {
			// our last hope, we assume Unix
			p = r.exec("env");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			StringBuilder debugLine = (new StringBuilder("\nFirst ")).append(
					MAX_DEBUG_LINE_PRESENTATION).append(" system properties [");
			int i = 0;
			while ((line = br.readLine()) != null) {
				int idx = line.indexOf('=');
				if (idx > 0) {
					String key = line.substring(0, idx);
					String value = "";
					if (idx < line.length()) {
						value = line.substring(idx + 1);
					}
					envVars.setProperty(key, value);
					if (i++ < MAX_DEBUG_LINE_PRESENTATION)
						debugLine.append("\n	").append(key).append(" = ")
								.append(value);
				}
			}
			debugLine.append("\n	...]");
			log.debug(debugLine.toString());
			return envVars;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	/**
	 * Metoda czytajaca wlasciwosci z pliku. Zawartosc pliku powinna byc w
	 * UTF-8.
	 * 
	 * @param clazz
	 *            dowolna klasa zaladowana przez classloader-a, podobnie jak
	 *            podczas deklaracji obiketu logowania dla "log4j"
	 * @param fileName
	 *            pelna (z ewentyalna sciezka) nazwa pliku
	 * @return wlasciwosci przeczyczate z pliku
	 * @throws IOException
	 */
	public static Properties loadProperties(Class<?> clazz, String fileName) {

		Properties envVars = new Properties();

		try {
			BufferedReader d = getBufferedReader(clazz, fileName);
			String inputLine;
			StringBuilder debugLine = (new StringBuilder("\nFirst ")).append(
					MAX_DEBUG_LINE_PRESENTATION).append(" properties [");
			int i = 0;
			while ((inputLine = d.readLine()) != null) {
				if (!inputLine.startsWith("#") && inputLine.length() > 0) {
					int comaPos = inputLine.indexOf('=');
					if (comaPos > 0) {
						String key = inputLine.substring(0, comaPos);
						String value = "";
						if (comaPos < inputLine.length()) {
							value = inputLine.substring(comaPos + 1);
						}
						envVars.setProperty(key, value);
						if (i++ < MAX_DEBUG_LINE_PRESENTATION)
							debugLine.append("\n	").append(key).append(" = ")
									.append(value);
					}
				}
			}
			debugLine.append("\n	...]");
			log.debug(debugLine.toString());
			d.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return envVars;
	}

	/**
	 * Metoda czytajaca wlasciwosci z pliku. Zawartosc pliku powinna byc w
	 * UTF-8.
	 * 
	 * @param clazz
	 *            dowolna klasa zaladowana przez classloader-a, podobnie jak
	 *            podczas deklaracji obiketu logowania dla "log4j"
	 * @param fileName
	 *            pelna (z ewentyalna sciezka) nazwa pliku
	 * @return wlasciwosci przeczyczate z pliku w postaci Map
	 */
	public static Map<String, String> loadHashtable(Class<?> clazz,
			String fileName) {

		Map<String, String> envVars = new Hashtable<String, String>();

		try {
			BufferedReader d = getBufferedReader(clazz, fileName);
			String inputLine;
			StringBuilder debugLine = (new StringBuilder("\nFirst ")).append(
					MAX_DEBUG_LINE_PRESENTATION).append(
					" hashtable elemenets [");
			int i = 0;
			while ((inputLine = d.readLine()) != null) {
				if (!inputLine.startsWith("#") && inputLine.length() > 0) {
					int comaPos = inputLine.indexOf('=');
					if (comaPos > 0) {
						String key = inputLine.substring(0, comaPos);
						String value = "";
						if (comaPos < inputLine.length()) {
							value = inputLine.substring(comaPos + 1);
						}
						envVars.put(key, value);
						if (i++ < MAX_DEBUG_LINE_PRESENTATION)
							debugLine.append("\n	").append(key).append(" = ")
									.append(value);
					}
				}
			}
			debugLine.append("\n	...]");
			log.debug(debugLine.toString());
			d.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return envVars;
	}

	private static BufferedReader getBufferedReader(Class<?> clazz,
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
				d = new BufferedReader(new InputStreamReader(
						clazz.getResourceAsStream(fileName), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return d;
	}
}
