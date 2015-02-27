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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

public class MockLoader {

	private static Logger log = LoggerFactory.getLogger(MockLoader.class);

	public static String RESERVED_CHARACTER = "~";

	/**
	 * Ladowanie pliku w formacie (separatorem jest srednik ';'). Przykładowy
	 * format linni <field1>;<field2>;<field3>;<field4>;<field5>
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 */
	public static List<String[]> loadCSV(Class<?> clazz, String fileName) {

		List<String[]> rows = new ArrayList<String[]>();

		try {
			BufferedReader d = getBufferedReader(clazz, fileName);
			String inputLine;
			while ((inputLine = d.readLine()) != null) {
				log.debug("{}", inputLine);
				if (!inputLine.startsWith("#")
						&& StringUtils.isNotBlank(inputLine)) {
					inputLine = inputLine.replaceAll("([^\\\\;])([;])", "$1"
							+ RESERVED_CHARACTER);
					rows.add(inputLine.split("\\" + RESERVED_CHARACTER));
				}
			}
			d.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return rows;
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
