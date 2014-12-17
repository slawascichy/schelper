/**
 */
package pl.slawas.test.validators;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;
import pl.slawas.validators.FileNameStringValidator;

public class FileNameStringValidatorTest extends TestCase {

	@SuppressWarnings("unused")
	final private static Logger log = LoggerFactory
			.getLogger(FileNameStringValidatorTest.class);

	@Test
	public void testFileNameStringValidator() {

		/* czy przepuszcza poprawny tekst */
		String input = "ala ma kota";
		assertEquals(-1, FileNameStringValidator.invalidCharOff(input)[0]);
		assertEquals(-1, FileNameStringValidator.invalidCharOff(null)[0]);

		/* czy wykrywa niepoprawny tekst */
		final int badFileNameCharCode = 47;
		char c = (char) badFileNameCharCode;
		char r = FileNameStringValidator.charReplacement();
		input = "ala " + c + "ma kot" + c + "a";
		int ret[] = FileNameStringValidator.invalidCharOff(input);
		assertEquals(4, ret[0]);
		assertEquals(badFileNameCharCode, ret[1]);

		/* czy czyści niepoprawny tekst */
		input = "ala " + c + "ma kot" + c + "a";
		input = FileNameStringValidator.invalidCharsRemoved(input);
		assertEquals("ala ma kota", input);

		/* czy zamienia niepoprawny tekst */
		input = "ala " + c + "ma kot" + c + "a";
		input = FileNameStringValidator.invalidCharsReplaced(input);
		assertEquals("ala " + r + "ma kot" + r + "a", input);

		/* czy zwraca poprawny tekst */
		input = FileNameStringValidator.invalidCharsRemoved(null);
		assertEquals(null, input);
		input = FileNameStringValidator.invalidCharsReplaced(null);
		assertEquals(null, input);

		/* czy nie zmienia poprawny tekst */
		c = '_';
		input = "ala " + c + "ma kota";
		String output = FileNameStringValidator.invalidCharsRemoved(input);
		assertTrue((output == input));
		assertEquals("ala " + c + "ma kota", output);
		output = FileNameStringValidator.invalidCharsReplaced(input);
		assertTrue((output == input));
		assertEquals("ala " + c + "ma kota", output);

		/* czy zmienia niepoprawną nazwę pliku i zapisuje go */
		Enumeration<URL> urls = null;
		try {
			urls = ClassLoader.getSystemClassLoader().getResources("");
		} catch (IOException e) {
			fail("Nie można pobrać ścieżek do zasobów");
		}
		if (!urls.hasMoreElements()) {
			fail("Nie znaleziono ścieżki do zasobów");
		}

		final String targetPath = urls.nextElement().getFile() + "../";
		final String messageNumber = "&lt;!~!UENERkVCMDkAAQACAAAAAAAAAAAAAAAAABgAAAAAAAAAFq7jy/Zqqk63E727tg26RcKAAAAQAAAAG2AZm3K51kqwtZcHROx4mQEAAAAA@gmail.com&gt;.eml";
		File f = new File(targetPath + messageNumber);
		try {
			f.createNewFile();
		} catch (IOException ex) {
			final String newFileName = targetPath
					+ FileNameStringValidator
							.invalidCharsReplaced(messageNumber);
			f = new File(newFileName);
			try {
				f.createNewFile();
			} catch (IOException ex2) {
				fail("Nie można utworzyć pliku o teoretycznie poprawnej nazwie"
						+ newFileName + " " + ex2);
			}
			return;
		}
		assertEquals(f.canWrite(), false);
	}

}
