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
package pl.slawas.test.diffs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import pl.slawas.diffs.ObjectsDifference;
import pl.slawas.diffs.PropertyCreator;
import pl.slawas.diffs.TraceCreator;
import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


@Ignore
public class DuplicatorTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(DuplicatorTest.class);

	private DiffMockParent testedObject4Track;

	public void prepareMockData() throws ParseException {
		testedObject4Track = new DiffMockParent();
		testedObject4Track.setId(1.0);
		testedObject4Track.setName("slawas");
		testedObject4Track.setDescription("Boss");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(sdf.parse("1973-07-11"));
		testedObject4Track.setCreateDate(cal);
		DiffMockChild child;
		List<DiffMockChild> childList = new ArrayList<DiffMockChild>();
		Hashtable<String, DiffMockChild> childHashtable = new Hashtable<String, DiffMockChild>();
		Hashtable<Integer, DiffMockChild> childHashtableInt = new Hashtable<Integer, DiffMockChild>();
		Vector<DiffMockChild> childVector = new Vector<DiffMockChild>();

		child = new DiffMockChild();
		child.setId(1);
		child.setChildName("tomekd");
		child.setChildDescription("DBA administrator");
		childList.add(child);
		childHashtable.put(child.getChildName(), child);
		childVector.add(child);

		testedObject4Track.setChild(child);

		child = new DiffMockChild();
		child.setId(2);
		child.setChildName("cassio");
		child.setChildDescription("DBA administrator");
		childList.add(child);
		childHashtable.put(child.getChildName(), child);
		childHashtableInt.put(child.getId(), child);
		childVector.add(child);

		child = new DiffMockChild();
		child.setId(3);
		child.setChildName("kameler");
		child.setChildDescription("ASA administrator");
		childList.add(child);
		childHashtable.put(child.getChildName(), child);
		childHashtableInt.put(child.getId(), child);
		childVector.add(child);

		child = new DiffMockChild();
		child.setId(4);
		child.setParent(testedObject4Track);
		child.setChildName("rbonieck");
		child.setChildDescription("ASA administrator");
		childList.add(child);
		childHashtable.put(child.getChildName(), child);
		childHashtableInt.put(child.getId(), child);
		childVector.add(child);

		DiffMockChild[] childArray = new DiffMockChild[childVector.size()];
		for (int i = 0; i < childArray.length; i++)
			childArray[i] = childVector.get(i);

		int stringArrayLen = 10;
		String[] stringArray = new String[stringArrayLen];
		int[] intArray = new int[stringArrayLen];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = "Slowo" + i;
			intArray[i] = i;
		}

		testedObject4Track.setChildHashtable(childHashtable);
		testedObject4Track.setChildList(childList);
		testedObject4Track.setChildVector(childVector);
		testedObject4Track.setChildArray(childArray);
		testedObject4Track.setStringArray(stringArray);
		testedObject4Track.setIntArray(intArray);
		testedObject4Track.setChildHashtableInt(childHashtableInt);

	}

	private void printChanges(List<ObjectsDifference> diffList) {
		StringBuffer logOut = new StringBuffer();
		logOut.append("\n Lista zmian");
		logOut
				.append("\n*-------------------------------------------------" +
				"+-----------------------" +
				"+-----------------------" +
				"+----------------------+");
		logOut
				.append("\n| Etykieta zmiany_________________________________" +
				"| Stara wartosc_________" +
				"| Nowa wartosc__________" +
				"| Typ pola_____________|");
		logOut
				.append("\n*-------------------------------------------------" +
				"+-----------------------" +
				"+-----------------------" +
				"+----------------------+");

		for (ObjectsDifference diff : diffList) {

			String newVal = (diff.getTargetValue() != null ? diff.getTargetValue().toString() : "null");
			int maxnewValueLength = (newVal.length() > "Nowa wartosc_________".length() ? "Nowa wartosc_________"
					.length()
					: newVal.length());
			if (newVal.indexOf('.') < 0) {
				newVal = newVal.substring(0, maxnewValueLength);
			} else {
				String[] split = newVal.split("\\.");
				newVal = split[split.length - 1];
				maxnewValueLength = (newVal.length() > "Nowa wartosc_________".length() ? "Nowa wartosc_________"
						.length()
						: newVal.length());				
				newVal = newVal.substring(0, maxnewValueLength);
			}

			String oldVal = (diff.getSourceValue() != null ? diff.getSourceValue().toString() : "null");
			int maxoldValueLength = (oldVal.length() > "Stara wartosc________".length() ? "Stara wartosc________"
					.length()
					: oldVal.length());
			if (oldVal.indexOf('.') < 0) {
				oldVal = oldVal.substring(0, maxoldValueLength);
			} else {
				String[] split = oldVal.split("\\.");
				oldVal = split[split.length - 1];
				maxoldValueLength = (oldVal.length() > "Stara wartosc________".length() ? "Stara wartosc________"
						.length()
						: oldVal.length());				
				oldVal = oldVal.substring(0, maxoldValueLength);
			}

			logOut.append("\n| "
					+ Strings.rpad(
					diff.getLabel(),
					" ",
					"Etykieta zmiany________________________________".length())
					+ " | "
					+ Strings.rpad(
					oldVal,
					" ",
					"Stara wartosc________".length())
					+ " | "
					+ Strings.rpad(
					newVal,
					" ",
					"Nowa wartosc_________".length())
					+ " | "
					+ Strings
					.rpad(((Class<?>) diff.getType()).getSimpleName(), " ", "Typ pola____________"
					.length())
					+ " |");

		}
		logOut
				.append("\n*-------------------------------------------------" +
				"+-----------------------" +
				"+-----------------------" +
				"+----------------------+");
		log.info(logOut.toString());
	}

	@Test
	public void testTraceAnnotation() throws Exception {
		StringBuffer logOut = new StringBuffer();
		logOut.append("\n+------------------------------+");
		logOut.append("\n| START TRACKER TESTS SCENARIO |");
		logOut.append("\n+------------------------------+");
		log.info(logOut.toString());
		logOut.delete(0, logOut.length());
		prepareMockData();

		String result = "OK";
		DiffMockParent copyOkClass = (DiffMockParent) TraceCreator.copy(testedObject4Track);

		log.info("Test kopiowania: "
				+ "\n oryginal: {}: '{}'; '{}'"
				+ "\n    kopia: {}: '{}'; '{}'", new Object[]
				{
					testedObject4Track.toString(),
					testedObject4Track.getName(),
					testedObject4Track.getDescription(),
					copyOkClass.toString(),
					copyOkClass.getName(),
					copyOkClass.getDescription()
				});

		assertEquals("Blad utworzenia nowego obiektu",
				false,
				testedObject4Track.toString().equals(copyOkClass.toString()));

		assertEquals("Blad skopiowania wartosci pol do nowego obiektu dla pola 'name'",
				true,
				testedObject4Track.getName().equals(copyOkClass.getName()));

		assertEquals("Blad skopiowania wartosci pol do nowego obiektu dla pola 'description'",
				true,
				testedObject4Track.getDescription().equals(copyOkClass.getDescription()));


		
		DiffMockChild child = new DiffMockChild();
		child.setId(copyOkClass.getChildHashtable().get("tomekd").getId());
		child.setChildName(copyOkClass.getChildHashtable().get("tomekd").getChildName());
		child
				.setChildDescription(copyOkClass.getChildHashtable().get("tomekd")
				.getChildDescription());
		copyOkClass.setChild(child);

		log.info("Powinny byc takie same z punktu widzenia danych sledzonych");
		assertTrue("Powinny byc takie same z punktu widzenia danych sledzonych",
				TraceCreator.assertEquals(
				copyOkClass,
				testedObject4Track));

		/* teraz zmieniamy atrybuty w kopii */
		Calendar cal = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//cal.setTime(sdf.parse("2009-07-11"));
		copyOkClass.setCreateDate(cal);
		
		log.info("Sprawdzanie obiektu daty");
		assertTrue("Sprawdzanie obiektu daty: powinny byc rozne z punktu widzenia danych sledzonych",
				!TraceCreator.assertEquals(
				copyOkClass,
				testedObject4Track));


		copyOkClass.setName("Slawomir");
		copyOkClass.setDescription("The Best Boss");
		copyOkClass.setId(2);
		/* teraz zmieniamy atrybuty w kopii, ale na sledzonych listach */
		/* test Hashtable (zmieniamy jeden z obiektow) */
		copyOkClass.getChildHashtable().get("tomekd").setChildDescription("The Best DBA");
		/* test List i Vector (dodajemy nowy obiekt do list) */
		child = new DiffMockChild();
		child.setId(5);
		child.setChildName("new");
		child.setChildDescription("DBA new worker");
		copyOkClass.getChildList().add(child);
		copyOkClass.getChildVector().add(child);
		copyOkClass.getChildArray()[0] = child;
		copyOkClass.getStringArray()[6] = "zmiana slowa";
		copyOkClass.getIntArray()[6] = 21;
		copyOkClass.getChildList().get(3).setParent(copyOkClass);
		copyOkClass.setChild(copyOkClass.getChildHashtable().get("cassio"));

		log.info("Powinny byc rozne z punktu widzenia danych sledzonych");
		assertTrue("Powinny byc rozne z punktu widzenia danych sledzonych",
				!TraceCreator.assertEquals(
				copyOkClass,
				testedObject4Track));

		List<ObjectsDifference> diffList = TraceCreator.diff(testedObject4Track, copyOkClass);
		printChanges(diffList);

		Hashtable<String, String> labelList = new Hashtable<String, String>();
		labelList.put("id", "Identyfikator szefa");
		labelList.put("childId", "Identyfikator pracownika");
		diffList = TraceCreator.diff(testedObject4Track, copyOkClass, labelList);
		printChanges(diffList);

		/**
		 * <pre>
		 *  Lista zmian
		 *  Lista zmian
		 * *-------------------------------------------------+-----------------------+-----------------------+----------------------+
		 * | Etykieta zmiany_________________________________| Stara wartosc_________| Nowa wartosc__________| Typ pola_____________|
		 * *-------------------------------------------------+-----------------------+-----------------------+----------------------+
		 * | Nazwa                                           | slawas                | Slawomir              | String               |
		 * | Description                                     | Boss                  | The Best Boss         | String               |
		 * | Lista pracownikow[5].Nazwa pracownika           | null                  | new                   | String               |
		 * | Lista pracownikow[5].ChildDescription           | null                  | DBA new worker        | String               |
		 * | Lista pracownikow[5].Identyfikator pracownika   | 0                     | 5                     | int                  |
		 * | Lista pracownikow[4].Szef                       | TracerParentMock@c3c7 | TracerParentMock@f623 | TracerParentMock     |
		 * | Hashtable pracownikow[tomekd].ChildDescription  | DBA administrator     | The Best DBA          | String               |
		 * | Vector pracownikow[5].Nazwa pracownika          | null                  | new                   | String               |
		 * | Vector pracownikow[5].ChildDescription          | null                  | DBA new worker        | String               |
		 * | Vector pracownikow[5].Identyfikator pracownika  | 0                     | 5                     | int                  |
		 * | Array pracownikow[0].Nazwa pracownika           | tomekd                | new                   | String               |
		 * | Array pracownikow[0].ChildDescription           | DBA administrator     | DBA new worker        | String               |
		 * | Array pracownikow[0].Identyfikator pracownika   | 1                     | 5                     | int                  |
		 * | Array string[6]                                 | Slowo6                | zmiana slowa          | String[]             |
		 * | Array int[6]                                    | 6                     | 21                    | int[]                |
		 * | Pracownik miesiaca.Nazwa pracownika             | tomekd                | cassio                | String               |
		 * | Pracownik miesiaca.Identyfikator pracownika     | 1                     | 2                     | int                  |
		 * *-------------------------------------------------+-----------------------+-----------------------+----------------------+
		 * </pre>
		 */
		Hashtable<String, ObjectsDifference> results = new Hashtable<String, ObjectsDifference>();
		for (ObjectsDifference diff : diffList) {
			results.put(diff.getLabel(), diff);
		}

		/* sprawdzenie wyrywkowo poprawnosci wygenerowania listy zmian */
		assertEquals("Nieprawidłowa rejestracja zmiany",
				results.get("Nazwa").getSourceValue().toString(), "slawas");

		assertEquals("Nieprawidłowa rejestracja zmiany",
				results.get("Nazwa").getTargetValue().toString(), "Slawomir");

		assertNull("Nieprawidłowa rejestracja zmiany",
				results.get("Lista pracownikow[5].Nazwa pracownika").getSourceValue());

		assertEquals("Nieprawidłowa rejestracja zmiany",
				results.get("Lista pracownikow[5].Nazwa pracownika").getTargetValue().toString(), "new");

		assertEquals(
				"Nieprawidłowa rejestracja zmiany",
				results.get("Hashtable pracownikow[tomekd].ChildDescription").getSourceValue()
				.toString(),
				"DBA administrator");

		assertEquals(
				"Nieprawidłowa rejestracja zmiany",
				results.get("Vector pracownikow[5].Identyfikator pracownika").getTargetValue()
				.toString(),
				"5");

		TraceCreator.copy(copyOkClass, testedObject4Track);

		assertTrue("Powinny byc takie same z punktu widzenia danych sledzonych",
				TraceCreator.assertEquals(
				copyOkClass,
				testedObject4Track));

		logOut.append("\n+-----------------------------+");
		logOut.append("\n| END TRACKER TESTS SCENARIO  |");
		logOut.append("\n+-----------------------------+");
		log.info(logOut.toString());
		assertEquals("Blad testu", "OK", result);

	}

	private void printProperties(Properties properties) {
		StringBuffer logOut = new StringBuffer();
		logOut.append("\n Lista wlasciwosci");
		logOut
				.append("\n*-------------------------------------------------"
				+ "+------------------------------+");
		logOut
				.append("\n| Klucz__________________________________________ "
				+ "| Wartosc_____________________ |");
		logOut
				.append("\n*-------------------------------------------------"
				+ "+------------------------------+");

		Enumeration<Object> keys = properties.keys();
		Vector<String> sortedKeys = new Vector<String>();

		while (keys.hasMoreElements()) {
			sortedKeys.add((String) keys.nextElement());
		}
		try {
			Strings.sort(sortedKeys);
		} catch (Exception e) {
			StackTraceElement[] list = e.getStackTrace();
			StringBuffer stackTrace = new StringBuffer();
			stackTrace.append("\n " + e.getMessage());
			stackTrace.append("\n STACK TRACE:");
			for (StackTraceElement el : list) {
				stackTrace.append("\n	" + el.toString());
			}
			log.error(stackTrace.toString());
		}

		for (String key : sortedKeys) {

			Object value = properties.get(key);

			String keyVal = (key != null ? key.toString() : "null");
			int maxnewValueLength =
					(keyVal.length() > "Klucz__________________________________________"
					.length() ? "Klucz__________________________________________"
					.length()
					: keyVal.length());
			keyVal = keyVal.substring(0, maxnewValueLength);

			String valueVal = (value != null ? value.toString() : "null");
			int maxoldValueLength =
					(valueVal.length() > "Wartosc_____________________"
					.length() ? "Wartosc_____________________"
					.length()
					: valueVal.length());
			if (valueVal.indexOf('.') < 0) {
				valueVal = valueVal.substring(0, maxoldValueLength);
			} else {
				String[] split = valueVal.split("\\.");
				valueVal = split[split.length - 1];
				maxoldValueLength =
						(valueVal.length() > "Wartosc_____________________"
						.length() ? "Wartosc_____________________"
						.length()
						: valueVal.length());
				valueVal = valueVal.substring(0, maxoldValueLength);
			}

			logOut.append("\n| "
					+ Strings.rpad(
					keyVal,
					" ",
					"Klucz__________________________________________".length())
					+ " | "
					+ Strings.rpad(
					valueVal,
					" ",
					"Wartosc_____________________".length())
					+ " |");

		}
		logOut
				.append("\n*-------------------------------------------------"
				+ "+------------------------------+");
		log.info(logOut.toString());
	}

	@Test
	public void testPropertyAnnotation() throws Exception {
		StringBuffer logOut = new StringBuffer();
		logOut.append("\n+-------------------------------+");
		logOut.append("\n| START PROPERTY TESTS SCENARIO |");
		logOut.append("\n+-------------------------------+");
		log.info(logOut.toString());
		logOut.delete(0, logOut.length());
		prepareMockData();

		/**
		 * <pre>
		 * Lista wlasciwosci
		 * *-------------------------------------------------+------------------------------+
		 * | Klucz__________________________________________ | Wartosc_____________________ |
		 * *-------------------------------------------------+------------------------------+
		 * | child.child.description                         | DBA administrator            |
		 * | child.child.id                                  | 1                            |
		 * | child.child.name                                | tomekd                       |
		 * | childArray.0.child.description                  | DBA administrator            |
		 * | childArray.0.child.id                           | 1                            |
		 * | childArray.0.child.name                         | tomekd                       |
		 * | childArray.1.child.description                  | DBA administrator            |
		 * | childArray.1.child.id                           | 2                            |
		 * | childArray.1.child.name                         | cassio                       |
		 * | childArray.2.child.description                  | ASA administrator            |
		 * | childArray.2.child.id                           | 3                            |
		 * | childArray.2.child.name                         | kameler                      |
		 * | childArray.3.child.boss                         | AnnotationParentMock@1ccb029 |
		 * | childArray.3.child.description                  | ASA administrator            |
		 * | childArray.3.child.id                           | 4                            |
		 * | childArray.3.child.name                         | rbonieck                     |
		 * | childArray.size                                 | 4                            |
		 * | childHashtable.cassio.child.description         | DBA administrator            |
		 * | childHashtable.cassio.child.id                  | 2                            |
		 * | childHashtable.cassio.child.name                | cassio                       |
		 * | childHashtable.kameler.child.description        | ASA administrator            |
		 * | childHashtable.kameler.child.id                 | 3                            |
		 * | childHashtable.kameler.child.name               | kameler                      |
		 * | childHashtable.rbonieck.child.boss              | AnnotationParentMock@1ccb029 |
		 * | childHashtable.rbonieck.child.description       | ASA administrator            |
		 * | childHashtable.rbonieck.child.id                | 4                            |
		 * | childHashtable.rbonieck.child.name              | rbonieck                     |
		 * | childHashtable.size                             | 4                            |
		 * | childHashtable.tomekd.child.description         | DBA administrator            |
		 * | childHashtable.tomekd.child.id                  | 1                            |
		 * | childHashtable.tomekd.child.name                | tomekd                       |
		 * | childList.1.child.description                   | DBA administrator            |
		 * | childList.1.child.id                            | 1                            |
		 * | childList.1.child.name                          | tomekd                       |
		 * | childList.2.child.description                   | DBA administrator            |
		 * | childList.2.child.id                            | 2                            |
		 * | childList.2.child.name                          | cassio                       |
		 * | childList.3.child.description                   | ASA administrator            |
		 * | childList.3.child.id                            | 3                            |
		 * | childList.3.child.name                          | kameler                      |
		 * | childList.4.child.boss                          | AnnotationParentMock@1ccb029 |
		 * | childList.4.child.description                   | ASA administrator            |
		 * | childList.4.child.id                            | 4                            |
		 * | childList.4.child.name                          | rbonieck                     |
		 * | childList.size                                  | 4                            |
		 * | childVector.1.child.description                 | DBA administrator            |
		 * | childVector.1.child.id                          | 1                            |
		 * | childVector.1.child.name                        | tomekd                       |
		 * | childVector.2.child.description                 | DBA administrator            |
		 * | childVector.2.child.id                          | 2                            |
		 * | childVector.2.child.name                        | cassio                       |
		 * | childVector.3.child.description                 | ASA administrator            |
		 * | childVector.3.child.id                          | 3                            |
		 * | childVector.3.child.name                        | kameler                      |
		 * | childVector.4.child.boss                        | AnnotationParentMock@1ccb029 |
		 * | childVector.4.child.description                 | ASA administrator            |
		 * | childVector.4.child.id                          | 4                            |
		 * | childVector.4.child.name                        | rbonieck                     |
		 * | childVector.size                                | 4                            |
		 * | description                                     | Boss                         |
		 * | intArray.0                                      | 0                            |
		 * | intArray.1                                      | 1                            |
		 * | intArray.2                                      | 2                            |
		 * | intArray.3                                      | 3                            |
		 * | intArray.4                                      | 4                            |
		 * | intArray.5                                      | 5                            |
		 * | intArray.6                                      | 6                            |
		 * | intArray.7                                      | 7                            |
		 * | intArray.8                                      | 8                            |
		 * | intArray.9                                      | 9                            |
		 * | intArray.size                                   | 10                           |
		 * | name                                            | slawas                       |
		 * | stringArray.0                                   | Slowo0                       |
		 * | stringArray.1                                   | Slowo1                       |
		 * | stringArray.2                                   | Slowo2                       |
		 * | stringArray.3                                   | Slowo3                       |
		 * | stringArray.4                                   | Slowo4                       |
		 * | stringArray.5                                   | Slowo5                       |
		 * | stringArray.6                                   | Slowo6                       |
		 * | stringArray.7                                   | Slowo7                       |
		 * | stringArray.8                                   | Slowo8                       |
		 * | stringArray.9                                   | Slowo9                       |
		 * | stringArray.size                                | 10                           |
		 * *-------------------------------------------------+------------------------------+
		 * </pre>
		 */

		testedObject4Track.setValueWithoutProperty("TEST VALUE WITHOUT PROPERTY");
		Properties testedProperties = PropertyCreator.toProperties(testedObject4Track);
		printProperties(testedProperties);

		DiffMockParent testedObject = (DiffMockParent)
				PropertyCreator.createFromProperties(getClass().getClassLoader(), testedProperties);

		List<ObjectsDifference> diffList = PropertyCreator.diff(testedObject4Track, testedObject);

		assertEquals("Obiekty powinny byc takie 'same' z punktu widzenia annotacji Property",
				Integer.valueOf(0),
				Integer.valueOf(diffList.size()));

		assertTrue("Obiekty powinny byc takie 'same' z punktu widzenia annotacji Property",
				PropertyCreator.assertEquals(testedObject4Track, testedObject));

		String changedValue = "DBA super admin";
		testedProperties.put("childVector.3.child.description", changedValue);
		testedObject = (DiffMockParent)
				PropertyCreator.createFromProperties(getClass().getClassLoader(), testedProperties);

		diffList = PropertyCreator.diff(testedObject4Track, testedObject);
		printChanges(diffList);

		assertFalse("Obiekty powinny byc 'rozne' z punktu widzenia annotacji Property",
				PropertyCreator.assertEquals(testedObject4Track, testedObject));

		boolean differenceIsOK = false;
		for (DiffMockChild child : testedObject.getChildVector()) {
			if (child.getId() == 3) {
				differenceIsOK = child.getChildDescription().equals(changedValue);
				break;
			}
		}

		assertTrue("Jezeli jest roznica, to na pewno nie celowa",
				differenceIsOK);

		PropertyCreator.copy(testedObject4Track, testedObject);

		assertTrue("Obiekty powinny byc takie 'same' z punktu widzenia annotacji Property",
				PropertyCreator.assertEquals(testedObject4Track, testedObject));

		
		assertFalse("Pola powinny byc 'rozne' z punktu widzenia annotacji Property",
				testedObject4Track.getValueWithoutProperty().equals(
				(testedObject.getValueWithoutProperty() == null ? "" : testedObject
						.getValueWithoutProperty() == null)));

		String result = "OK";

		logOut.append("\n+------------------------------+");
		logOut.append("\n| END PROPERTY TESTS SCENARIO  |");
		logOut.append("\n+------------------------------+");
		log.info(logOut.toString());
		assertEquals("Blad testu", "OK", result);
	}
}
