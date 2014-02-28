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
package pl.slawas.test.entities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.entities.NameValuePair;
import pl.slawas.entities.NameValuePairUtils;
import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;
import pl.slawas.xml.XMLNameValuePairUtils;
import pl.slawas.xml.XMLNameValuePairUtilsErrorException;

public class NameValuePairUrilsTest extends TestCase {

	private static Logger log = LoggerFactory
			.getLogger(NameValuePairUrilsTest.class);

	private Vector<NameValuePair> testList;

	private void loadMockData() {
		/* zaladowanie przykładowych par nazwa-wartość */
		testList = new Vector<NameValuePair>();
		NameValuePair val;
		val = NameValuePairUtils.createNewInstance("Sławek", "1");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Michał", "2");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Człowiek", "3");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Test", "4");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Maciej", "5");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Majka", "6");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Basia", "7");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Aleksandra", "8");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Feliks", "9");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Lalka", "10");
		testList.add(val);
		val = NameValuePairUtils.createNewInstance("Łódź", "11");
		testList.add(val);
	}




	static final Comparator<NameValuePair> NAME_ORDER = new Comparator<NameValuePair>() {
		public int compare(NameValuePair e1, NameValuePair e2) {

			return Strings.compare4Sort(e1.getName(),e2.getName());
		}
	};

	@Test
	public void testSort() throws Exception {
		loadMockData();

		int i = 0;
		Collections.sort(testList, NAME_ORDER);
		for (NameValuePair test : testList) {
			log.debug(" - {} ({})",
					new Object[] { test.getName(), test.getValue() });
			if (i == 0)
				Assert.assertTrue(test.getName().equals("Aleksandra")
						&& test.getValue().equals("8"));
			if (i == 7)
				Assert.assertTrue(test.getName().equals("Majka")
						&& test.getValue().equals("6"));
			i++;
		}

		log.debug("sortByName");
		/**
		 * <pre>
		 * 		"Aleksandra", "8");
		 * 		"Basia", "7");
		 * 		"Człowiek", "3");
		 * 		"Feliks", "9");
		 * 		"Lalka", "10");
		 * 		"Łódź", "11");
		 * 		"Maciej", "5");
		 * 		"Majka", "6");
		 * 		"Michał", "2");
		 * 		"Sławek", "1");
		 * 		"Test", "4");
		 * </pre>
		 */

		NameValuePairUtils.sortByName(testList);
		i = 0;
		for (NameValuePair test : testList) {
			log.debug(" - {} ({})",
					new Object[] { test.getName(), test.getValue() });
			if (i == 0)
				Assert.assertTrue(test.getName().equals("Aleksandra")
						&& test.getValue().equals("8"));
			if (i == 7)
				Assert.assertTrue(test.getName().equals("Majka")
						&& test.getValue().equals("6"));
			i++;
		}

		NameValuePairUtils.sortByValue(testList);
		log.debug("sortByValue");
		for (NameValuePair test : testList) {
			log.debug(" - {} ({})",
					new Object[] { test.getValue(), test.getName() });
			if (i == 0)
				Assert.assertTrue(test.getName().equals("Slawek")
						&& test.getValue().equals("1"));
			if (i == 3)
				Assert.assertTrue(test.getName().equals("Test")
						&& test.getValue().equals("4"));
			i++;
		}

	}

	@Test
	public void testSearcher() throws Exception {
		loadMockData();

		List<? extends NameValuePair> result = NameValuePairUtils.searchByName(
				testList, "sław");
		assertEquals("Oczekiwana liczba zwróconych wierszy", 1, result.size());
		for (NameValuePair test : result) {
			log.debug(" - {} ({})",
					new Object[] { test.getName(), test.getValue() });
			Assert.assertTrue(test.getName().equals("Sławek")
					&& test.getValue().equals("1"));
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateXML() throws Exception,
			XMLNameValuePairUtilsErrorException {
		loadMockData();

		InputStream is = null;
		is = new ByteArrayInputStream(XMLNameValuePairUtils
				.getXMLDocument(testList).toString().getBytes("UTF-8"));
		List<NameValuePair> rList1 = (List<NameValuePair>) XMLNameValuePairUtils
				.readDocument(is);
		is.close();
		NameValuePair source1 = testList.get(0);
		NameValuePair copy1 = rList1.get(0);
		assertEquals("Oczekiwana nazwa w kopii", source1.getName(),
				copy1.getName());

	}

	@Test
	public void testSearcherInXML() throws Exception,
			XMLNameValuePairUtilsErrorException {
		loadMockData();

		InputStream is = null;
		is = new ByteArrayInputStream(XMLNameValuePairUtils
				.getXMLDocument(testList).toString().getBytes("UTF-8"));
		NameValuePair r1 = XMLNameValuePairUtils.searchInDocumentUniqueValue(
				is, "1");
		is.close();
		assertEquals("Oczekiwana nazwa obiektu o id = 1", "Sławek",
				r1.getName());

	}

}
