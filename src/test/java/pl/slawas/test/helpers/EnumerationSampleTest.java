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
package pl.slawas.test.helpers;

import java.util.Enumeration;

import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.support.EnumerationException;
import pl.slawas.support.EnumerationSample;
import pl.slawas.support.EnumerationSupport;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class EnumerationSampleTest extends TestCase {

	final private static Logger log = LoggerFactory.getLogger(EnumerationSampleTest.class);

	@Test
	public void testReplaceAll() {

		String[] expectedValues = new String[]
			{
					"krok 1",
					"krok 2",
					"krok 3",
					"krok 4",
					"krok 5",
					"krok 6"
				};

		EnumerationSample sample = new EnumerationSample();
		for (int i = 0; i < expectedValues.length; i++) {
			sample.setWord(expectedValues[i]);
		}

		Enumeration<String> e = sample.getHistoryOfWordChanging();
		int i = 0;
		while (e.hasMoreElements()) {
			String actual = e.nextElement();
			assertEquals("Blad wartości enumeratora", expectedValues[i], actual);
			log.debug(actual);
			i++;
		}
		assertEquals("Blad rozmiaru", 5, i);

		e = sample.getHistoryOfWordChanging();
		i = 0;
		while (e.hasMoreElements()) {
			String actual = e.nextElement();
			assertEquals("Blad wartości enumeratora", expectedValues[i], actual);
			log.debug(actual);
			i++;
		}
		assertEquals("Blad rozmiaru", 5, i);

		e = sample.getHistoryOfWordChanging();
		i = 0;
		try {
			while (e.hasMoreElements()) {
				if (i > (EnumerationSupport.OVERFLOW + 1000)) {
					break;
				}
				i++;
			}
			assertTrue("Zabezpieczenie nie zadzialalo", false);
		} catch (EnumerationException ex) {
			assertTrue("Zabezpieczenie zadzialalo", true);
		}
		assertEquals("Blad rozmiaru", EnumerationSupport.OVERFLOW + 1, i);

		e = sample.getHistoryOfWordChanging();
		i = 0;
		while (e.nextElement() != null) {
			if (i > 10) {
				assertTrue("Uwaga! nieskonczona petla", false);
			}
			i++;
		}
		assertEquals("Blad rozmiaru", 5, i);

	}

}
