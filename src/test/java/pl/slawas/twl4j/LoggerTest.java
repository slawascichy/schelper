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
package pl.slawas.twl4j;

import java.util.Hashtable;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.slawas.helpers.Configurations;

public class LoggerTest {

	final private static Logger log = LoggerFactory.getLogger(LoggerTest.class
			.getName());

	final private static pl.slawas.twl4j.Logger twlog = pl.slawas.twl4j.LoggerFactory
			.getLogger(LoggerTest.class.getName());

	/**
	 * Parametry testów
	 */
	private static Properties props = new Properties();

	static {
		final String mockFileName = "/test.properties";
		System.out.println("Loading test properties from " + mockFileName
				+ "....");
		Hashtable<String, String> _Properties = Configurations.loadHashtable(
				LoggerTest.class, mockFileName);
		props.putAll(_Properties);
	}

	@Test
	public void testWSPasswordEncoder() throws Exception {
		String testMsg = "{} Slawas {} jest {} super. {}";
		Object[] arg1 = new Object[] { 1, "Cichy", "bardzo", ":)" };
		String message = twlog.buildMessage(testMsg, arg1).toString();
		log.info("message='{}'", message);
		twlog.info(testMsg, arg1);
		assert "1 Slawas Cichy jest bardzo super. :) ".equals(message) : "zle wygenerowany komunikat bledu";

		arg1 = new Object[] { 1, "Cichy", "bardzo" };
		message = twlog.buildMessage(testMsg, arg1).toString();
		twlog.info(testMsg, arg1);
		log.info("message='{}'", message);
		assert "1 Slawas Cichy jest bardzo super. {} ".equals(message) : "zle wygenerowany komunikat bledu";

		arg1 = new Object[] { 1, "Cichy", "bardzo", "Wiecej...", "Wiecej..." };
		message = twlog.buildMessage(testMsg, arg1).toString();
		twlog.info(testMsg, arg1);
		log.info("message='{}'", message);
		assert "1 Slawas Cichy jest bardzo super. Wiecej... ".equals(message) : "zle wygenerowany komunikat bledu";

		testMsg = "Slawas jest super";
		arg1 = new Object[] { 1, "Cichy", "bardzo", "Wiecej...", "Wiecej..." };
		message = twlog.buildMessage(testMsg, arg1).toString();
		twlog.info(testMsg, arg1);
		log.info("message='{}'", message);
		assert "Slawas jest super ".equals(message) : "zle wygenerowany komunikat bledu";

		twlog.info(testMsg);

		try {
			new Error().divide(null, 1);
		} catch (Exception e) {
			twlog.error("Testowa prezentacja bledu", e);
		}
	}

	static class Error {
		int divide(Integer one, Integer two) throws Exception {
			try {
				return one / two;
			} catch (NullPointerException e) {
				Exception exept = new Exception(
						"Blad dzielenia: NullPointerException");
				exept.initCause(e);
				throw exept;
			} catch (Exception e) {
				Exception exept = new Exception("Blad dzielenia: "
						+ e.getClass().getSimpleName());
				exept.initCause(e);
				throw exept;
			}
		}
	}
}
