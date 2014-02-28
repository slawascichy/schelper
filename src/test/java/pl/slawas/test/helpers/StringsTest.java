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

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class StringsTest extends TestCase {
	
	final private static Logger log = LoggerFactory.getLogger(StringsTest.class);
	
	@Test
	public void testReplaceAll() {

		String source = "My name is Slawas. My nick is Slawas. Slawas is very happy."; 
		String toReplace = "Slawas";
		String replacement = "Basia";
		
		Assert.assertTrue(Strings.replaceAll(source, toReplace, replacement)
				.equals("My name is Basia. My nick is Basia. Basia is very happy."));
	}

	@Test
	public void testLeftRightPad() {
		
		String slowo = "1";
		String znak = "0";
		int liczba = 3;
		
		Assert.assertTrue(Strings.lpad(slowo, znak, liczba).equals("001"));
		Assert.assertTrue(Strings.rpad(slowo, znak, liczba).equals("100"));
	}

	@Test
	public void testRightTrim() {
		
		String source = " 1 ";

		Assert.assertTrue(Strings.ltrim(source).equals("1 "));
		Assert.assertTrue(Strings.rtrim(source).equals(" 1"));
		Assert.assertTrue(Strings.lrtrim(source).equals("1"));
	}

	@Test
	public void testToList() {
		
		String lista_elementow = ",Ola,Karolinka,Basia,Slawek,";
		char znak_rozdzielajacy = ',';

		List<String> testList = Strings.toList(lista_elementow, znak_rozdzielajacy);
		
		Assert.assertTrue(testList.size() == 4);
		Assert.assertTrue(testList.get(0).equals("Ola"));
		Assert.assertTrue(testList.get(1).equals("Karolinka"));
		Assert.assertTrue(testList.get(2).equals("Basia"));
		Assert.assertTrue(testList.get(3).equals("Slawek"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSort() throws Exception {
		String elementsList = ",Ola,Karolinka,Basia,Slawek,Dominika,";
		StringBuilder debugList = new StringBuilder(",");
		char separator = ',';

		List<String> testList = Strings.toList(elementsList, separator);
		Vector<String> testVector = new Vector<String>();
		for (String value : testList) {
			testVector.addElement(value);
		}
		Strings.sort(testVector, 0, testList.size() - 1);
		
		int i = 0;
		boolean isOkResult = true;
		for (String value : testVector) {
			debugList.append(value).append(",");
			switch (i) {
			case 0:	isOkResult = (!value.equals("Basia") ? false : isOkResult);	break;
			case 1: isOkResult = (!value.equals("Dominika") ? false : isOkResult); break; 
			case 2: isOkResult = (!value.equals("Karolinka") ? false : isOkResult); break; 
			case 3: isOkResult = (!value.equals("Ola") ? false : isOkResult); break; 
			case 4: isOkResult = (!value.equals("Slawek") ? false : isOkResult); break;
			default: isOkResult = false;
			}
			i++;
		}
		log.debug("Sorted list: {}", debugList.toString());
		Assert.assertEquals(true, isOkResult);
		
		String[] array_elementow = {"Ola","Karolinka","Basia","Slawek","Dominika"};
		debugList = new StringBuilder(",");
		Strings.sort(array_elementow, 0, 2);
		
		i = 0;
		isOkResult = true;
		for (String value : array_elementow) {
			debugList.append(value).append(",");
			switch (i) {
			case 0: isOkResult = (!value.equals("Basia") ? false : isOkResult); break; 
			case 1: isOkResult = (!value.equals("Karolinka") ? false : isOkResult); break; 
			case 2: isOkResult = (!value.equals("Ola") ? false : isOkResult); break; 
			case 3: isOkResult = (!value.equals("Slawek") ? false : isOkResult); break;
			case 4: isOkResult = (!value.equals("Dominika") ? false : isOkResult); break; 
			default: isOkResult = false;
			}
			i++;
		}
		log.debug("Sorted list: {}", debugList.toString());
		Assert.assertEquals(true, isOkResult);
		
	}
	
	@Test
	public void testBreakText() {
		
		int testLineSize = 20;
		String text = 
			"For example, to write several test cases that \nwant to work with different combinations. OK?";
		String expectedtext = 
			"For example, to \n" +
			"write several test \n" +
			"cases that \n" +
			"want to work with \n" +
			"different \n" +
			"combinations. OK?";

		log.debug("Sample text before breaking:\n---\n{}\n---", text);
		text = Strings.breakText(text, testLineSize);
		log.debug("Expected:\n---\n{}\n---", expectedtext);
		log.debug("Sample text after breaking:\n---\n{}\n---", text);
		Assert.assertEquals(expectedtext, text);
	}

	@Test
	public void testToDisableTags() {
		String aText = "<b>Bold</b>";
		final String expectedValue = "&lt;b&gt;Bold&lt;/b&gt;";
		
		Assert.assertEquals(expectedValue, Strings.toDisableTags(aText));
	}

	@Test
	public void testInitCap() {
		String in = "running tests";
		final String expectedValue = "Running Tests";
		
		Assert.assertEquals(expectedValue, Strings.initCap(in));
	}

	@Test
	public void testTranslate() {
		String in = "\"Śla%&d 03 wokół nas %&";
		final String expectedValue = "ŚlAd  wokół nAs ";
		
		Assert.assertEquals(expectedValue, Strings.translate(in, "a\"%&0123456789", "A"));
	}

	
	
}
