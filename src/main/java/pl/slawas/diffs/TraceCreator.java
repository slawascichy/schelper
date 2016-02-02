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
package pl.slawas.diffs;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * Tracer - klasa narzedziowa do sledzenia zmian osnaczonych annotacjami
 * {@link Trace} pol. Klasa wykorzystuje metody do kopiowania oparte o
 * predefiniowane metody z klasy {@link Duplicator}
 * 
 * <pre>
 * Example: {
 * 	// ...
 * 	// budowanie historii z uzyciem dynamicznych etykiet zmian
 * 	TracerParentMock copyNewObject = (TracerParentMock) Tracer
 * 			.copy(testedObject4Track);
 * 	getEntityManager().refresh(testedObject4Track);
 * 
 * 	// przygotowanie dynamicznej listy etykiet
 * 	Hashtable&lt;String, String&gt; labelList = new Hashtable&lt;String, String&gt;();
 * 	labelList.put(&quot;id&quot;, &quot;Identyfikator szefa&quot;);
 * 	labelList.put(&quot;childId&quot;, &quot;Identyfikator pracownika&quot;);
 * 	List&lt;TraceDifference&gt; diffList = Tracer.diff(copyNewObject,
 * 			testedObject4Track, labelList);
 * 
 * 	// teraz mozna zapisac zmiany w historii
 * 	List&lt;History&gt; historyList = new ArrayList&lt;History&gt;();
 * 	for (TraceDifference diff : diffList) {
 * 		log.debug(&quot;Nowa wartosc: {}&quot;, diff.getTargetValue());
 * 		log.debug(&quot;Stara wartosc: {}&quot;, diff.getSourceValue());
 * 		History newHistRow = new History();
 * 		newHistRow.setNewVal(diff.getTargetValue());
 * 		newHistRow.setOldVal(diff.getSourceValue());
 * 		historyList.add(newHistRow);
 * 	}
 * 	getEntityManager().getTransaction().begin();
 * 	((AresDictionaryDAO) historyDAO).insertList(newHistRow);
 * 	getEntityManager().getTransaction().commit();
 * 
 * 	// teraz przywracamy obiekt do nowej wartosci
 * 	Tracer.copy(testedObject4Track, copyOkClass);
 * 	// ...
 * }
 * </pre>
 * 
 * @see pl.slawas.diffs.Trace
 * @see pl.wp.ares.core.entities.TraceId
 * @see pl.slawas.diffs.Duplicator
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2.2.1 $
 * 
 */
public class TraceCreator {

	final protected static Logger log = LoggerFactory
			.getLogger(TraceCreator.class);

	/**
	 * Kopiowanie obiektu do nowej instancji, klonowanie obiektu. Powstaly
	 * obiekt ma skopiowane tylko te pola, dla ktorych ustwiona zostala
	 * annotacja {@link Trace}. Metoda nie nadaje sie do kopiowania (klonowania)
	 * calych obiektow, chyba ze wszystkie pola zostana oznaczone wczesniej
	 * wymieniona annotacja.
	 * 
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.Duplicator#copy(Class, Object)
	 * 
	 * @param source
	 *            obiekt do skopiowania (sklonowania)
	 * @return kopia obiektu
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object copy(Object source) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {

		return Duplicator.copy(Trace.class, source);
	}

	/**
	 * Kopiowanie wartosci pol z jednego obiektu do drugiego. Powstaly obiekt ma
	 * skopiowane tylko te pola, dla ktorych ustwiona zostala annotacja
	 * {@link Trace}. Metoda nie nadaje sie do kopiowania zawartosci calych
	 * obiektow, chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona
	 * annotacja.
	 * 
	 * @see Trace
	 * @see Duplicator#copy(Class, Object, Object, int)
	 * 
	 * @param target
	 *            obiekt, do ktorego sa kopiowane wartosci
	 * @param source
	 *            obiekt, z ktorego sa kopiowane wartosci
	 * @return obiekt, do ktorego sa kopiowane wartosci
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object copy(Object target, Object source)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		return Duplicator.copy(Trace.class, target, source);
	}

	/**
	 * Metoda do wykonania porownania dwoch obiektow i wygenerowania listy
	 * zmian. Metoda porownuje tylko wartosci pol, ktore dla ktorych ustwiona
	 * zostala annotacja {@link Trace}. Metoda nie nadaje sie do porownywania
	 * zawartosci calych obiektow, chyba ze wszystkie pola zostana oznaczone
	 * wczesniej wymieniona annotacja. Jezeli w obiektach wystepuja listy, to
	 * aby moc bezblednie je porownac nalezy pamietac o tym, ze w obiektach,
	 * ktore beda umieszczone w takiej liscie trzeba dodac annotacje
	 * {@link javax.persistence.Id} przy polu definiujacym unikalny klucz lub
	 * przy annotacji podac atrybut 'referencedFieldName', ktore wskażą pole
	 * jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy moga
	 * zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * @param target
	 *            nowa wartosc
	 * @param source
	 *            stara wartosc
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	public static List<ObjectsDifference> diff(Object target, Object source)
			throws Exception {
		return Duplicator.diff(new TraceAnnotationFactory(), target, source);
	}

	/**
	 * Metoda do wykonania porownania dwoch obiektow i wygenerowania listy
	 * zmian. Metoda porownuje tylko wartosci pol, ktore dla ktorych ustwiona
	 * zostala annotacja {@link Trace}. Metoda nie nadaje sie do porownywania
	 * zawartosci calych obiektow, chyba ze wszystkie pola zostana oznaczone
	 * wczesniej wymieniona annotacja. Jezeli w obiektach wystepuja listy, to
	 * aby moc bezblednie je porownac nalezy pamietac o tym, ze w obiektach,
	 * ktore beda umieszczone w takiej liscie trzeba dodac annotacje
	 * {@link javax.persistence.Id} przy polu definiujacym unikalny klucz lub
	 * przy annotacji podac atrybut 'referencedFieldName', ktore wskażą pole
	 * jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy moga
	 * zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * @param target
	 *            nowa wartosc
	 * @param source
	 *            stara wartosc
	 * @param labelList
	 *            hashtable z etykietami zmian pol
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	public static List<ObjectsDifference> diff(Object target, Object source,
			Map<String, String> labelList) throws Exception {
		return Duplicator.diff(new TraceAnnotationFactory(), target, source,
				labelList);
	}

	/**
	 * Porównanie dwóch obiektów. Metoda porownuje tylko wartosci pol, ktore dla
	 * ktorych ustwiona zostala annotacja {@link Trace}. Metoda nie nadaje sie
	 * do porownywania zawartosci calych obiektow, chyba ze wszystkie pola
	 * zostana oznaczone wczesniej wymieniona annotacja. Dodatkowo, ze jej
	 * pomoca mozna wstrzyknac dynamiczna liste etykiet. Aby to zadzialalo,
	 * oczywiscie trzeba podac odpowiednie parametry annotacji Track. Jezeli w
	 * obiektach wystepuja listy, to aby moc bezblednie je porownac nalezy
	 * pamietac o tym, ze w obiektach, ktore beda umieszczone w takiej liscie
	 * trzeba dodac annotacje {@link javax.persistence.Id} przy polu
	 * definiujacym unikalny klucz lub przy annotacji podac atrybut
	 * 'referencedFieldName', ktore wskażą pole jednoznacznie identyfikujace
	 * dany obiekt. Jezeli tego nie zrobimy moga zostać zwrócone nieoczekiwane
	 * wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * 
	 * @param target
	 *            nowa wartosc
	 * @param source
	 *            stara wartosc
	 * @return [true|false]
	 * @throws Exception
	 */
	public static boolean assertEquals(Object target, Object source)
			throws Exception {
		return Duplicator.assertEquals(new TraceAnnotationFactory(), target,
				source);
	}

	/**
	 * @return the timeZone
	 */
	public static TimeZone getTimeZone() {
		return Duplicator.getTimeZone();
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public static void setTimeZone(TimeZone timeZone) {
		Duplicator.setTimeZone(timeZone);
	}

}
