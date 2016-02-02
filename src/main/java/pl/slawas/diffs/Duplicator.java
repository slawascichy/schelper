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

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import javax.persistence.Id;

import pl.slawas.helpers.PrimitiveType;
import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


/**
 * 
 * Duplicator - wsparcie dla klas narzedziowych pozwalajacych na kopiowanie i
 * porownywanie pol obiektow na podstawie okreslonej annotacji. Przyklady
 * zastosowania znajdziecie w klasach znajdihacy sie w tym pakiecie.
 * 
 * @see pl.slawas.diffs.TraceCreator
 * @see pl.slawas.diffs.PropertyCreator
 * 
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2.2.1 $
 * 
 */
public class Duplicator {

	final protected static Logger log = LoggerFactory.getLogger(Duplicator.class);

	static final TimeZone timeZone = Calendar.getInstance().getTimeZone();

	static TimeZone customTimeZone = null;

	/**
	 * Format daty długiej (z godziną).Ustawiana jest na podstawie
	 * <code>AresConfig.getInstance().get(AresConfig.DATE_LONG_FORMAT)</code>.
	 * Jeżeli podana komenda zwróci <code>null</code>, to wtedy ustawiana jest
	 * wartość domyślna "dd-MM-yyyy HH:mm:ss".
	 * 
	 */
	static final String dateLongFormat = DuplicatorProperties.DEFAULT_DATE_LONG_FORMAT;

	static String customDateLongFormat = null;

	/**
	 * Format daty krótkiej (bez godziną). Ustawiana jest na podstawie
	 * <code>AresConfig.getInstance().get(AresConfig.DATE_SHORT_FORMAT)</code>.
	 * Jeżeli podana komenda zwróci <code>null</code>, to wtedy ustawiana jest
	 * wartość domyślna "dd-MM-yyyy".
	 * 
	 */
	static final String dateShortFormat = DuplicatorProperties.DEFAULT_DATE_SHORT_FORMAT;

	static String customDateShortFormat = null;

	/**
	 * parametr zabezpieczajacy przed zapentleniem sie operacji rekurencyjnych.
	 * Wartosc 1 jest w zupelnosci wystarczajaca.
	 */
	static final int REQURENCY_LEVEL = 1;

	/**
	 * Kopiowanie (klonowanie) obiektu. Metoda tworzy nowy obiekt i kopiuje do
	 * niego oznaczone zadana annotacja pola. Metoda moze byc wywoływana
	 * rekurencyjnie dlatego potrzebne jest podanie aktualnego poziomu
	 * zagniezdzenia.
	 * 
	 * @param annotation
	 *           klasa annotacji wystepujaca przy polach
	 * @param source
	 *           obiekt, ktory ma zostac skopiowany do nowego obiektu (ma byc
	 *           sklonowany)
	 * @return nowy (sklonowany) obiekt
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	static Object copy(Class<? extends Annotation> annotation, Object source)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {

		Object target = source.getClass().newInstance();
		log.debug("target is new instance of {}", source.getClass());
		return copy(annotation, target, source, 0);
	}

	/**
	 * Kopiowanie (klonowanie) obiektu. Metoda tworzy nowy obiekt i kopiuje do
	 * niego oznaczone zadana annotacja pola. Metoda moze byc wywolywana
	 * rekurencyjnie dlatego potrzeby jest podanie aktualnego poziomu
	 * zagniezdzenia.
	 * 
	 * @param annotation
	 *           klasa annotacji wystepujaca przy polach
	 * @param source
	 *           obiekt, ktory ma zostac skopiowany do nowego obiektu (ma byc
	 *           sklonowany)
	 * @param level
	 *           aktualny poziom zagniezdzenia. Na poczatku (jezeli jet to
	 *           pierwsze wywolanie) level powinien miec wartosc 0
	 * @return nowy (sklonowany) obiekt
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static Object copy(Class<? extends Annotation> annotation, Object source, int level)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {

		Object target = source.getClass().newInstance();
		log.debug("target is new instance of {}", source.getClass());
		return copy(annotation, target, source, level);
	}

	/**
	 * Kopiowanie wartosci pol z jednego obiektu do drugiego. Powstaly obiekt ma
	 * skopiowane tylko te pola, dla ktorych ustwiona zostala odpowiednia
	 * annotacja. Metoda nie nadaje sie do kopiowania zawartosci calych obiektow,
	 * chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona annotacja.
	 * 
	 * @param annotation
	 *           annotacja ustawiana przy polach, ktore mają byc kopiowane
	 * @param target
	 *           obiekt, do ktorego sa kopiowane wartosci
	 * @param source
	 *           obiekt, z ktorego sa kopiowane wartosci
	 * @return obiekt, do ktorego sa kopiowane wartosci
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	static Object copy(
			Class<? extends Annotation> annotation,
			Object target,
			Object source)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {
		return copy(annotation, target, source, 0);
	}

	/**
	 * Kopiowanie wartosci pol z jednego obiektu do drugiego. Powstaly obiekt ma
	 * skopiowane tylko te pola, dla ktorych ustwiona zostala odpowiednia
	 * annotacja. Metoda nie nadaje sie do kopiowania zawartosci calych obiektow,
	 * chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona annotacja.
	 * 
	 * @param annotation
	 *           annotacja ustawiana przy polach, ktore mają byc kopiowane
	 * @param target
	 *           obiekt, do ktorego sa kopiowane wartosci
	 * @param source
	 *           obiekt, z ktorego sa kopiowane wartosci
	 * @param level
	 *           metoda wywolywana jest rekurencyjnie dlatego, dla zabezpieczenia
	 *           przed zapetleniem sie operacji podaje sie poziom, dla ktorego
	 *           jest aktualnie wykonywana operacja
	 * @return obiekt, do ktorego sa kopiowane wartosci
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private static Object copy(
			Class<? extends Annotation> annotation,
			Object target,
			Object source,
			int level)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {

		level++;

		if (!target.getClass().equals(source.getClass()))
			throw new IllegalArgumentException(
					"Expect the same classes but have source at "
					+ source.toString()
					+ " and target at "
					+ target.toString());

		log.debug("Copy objects (LEVEL={}): {}", new Object[]
			{ level, source.getClass() });

		Field[] fields = getFields(source);
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotation)) {

				String setterMethodName = "set"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				String getterMethodName = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Class<?> parTypes[] = new Class[1];
				parTypes[0] = field.getType();

				Object targetValue = (Object)
						source.getClass().getMethod(getterMethodName).invoke(source);

				log.trace("Copy field {} of type {}, {}", new Object[]
					{ field.getName(), field.getType(), targetValue });

				if (targetValue != null) {
					if (field.getType().toString().equals(List.class.toString())) {
						log.trace("Field {} is list: {}", new Object[]
							{ field.getName(), field.getType() });

						targetValue = new ArrayList<Object>();
						for (Object object : (List<Object>) source.getClass().getMethod(getterMethodName)
								.invoke(source))
						{
							((List<Object>) targetValue).add(copy(annotation, object, level));
						}
					} else if (field.getType().toString().equals(Hashtable.class.toString())) {
						log.trace("Field {} is hashtable: {}", new Object[]
							{ field.getName(), field.getType() });

						targetValue = new Hashtable<Object, Object>();
						Hashtable<Object, Object> surceValues =
								(Hashtable<Object, Object>) source.getClass().getMethod(getterMethodName)
								.invoke(source);

						Enumeration<Object> sourceKeys = surceValues.keys();
						while (sourceKeys.hasMoreElements()) {
							Object key = sourceKeys.nextElement();
							Object value = surceValues.get(key);
							((Hashtable<Object, Object>) targetValue).put(key, copy(
									annotation,
									value,
									level));
						}

					} else if (field.getType().toString().equals(Vector.class.toString())) {
						log.trace("Field {} is vector: {}", new Object[]
							{ field.getName(), field.getType() });

						targetValue = new Vector<Object>();
						for (Object object : (List<Object>) source.getClass().getMethod(getterMethodName)
								.invoke(source))
						{
							((Vector<Object>) targetValue).add(copy(annotation, object, level));
						}
					} else if (field.getType().isArray()) {
						log.trace("Field {} is array: {}", new Object[]
							{ field.getName(), field.getType() });
						Class<?> componentType = field.getType().getComponentType();

						if (!componentType.isPrimitive()) {
							log.trace("componentType {} is not primitive", new Object[]
								{ componentType.getName() });
							int arrayLen = ((Object[]) targetValue).length;
							Object copyArrayValue = Arrays.copyOf((Object[]) targetValue, arrayLen);
							targetValue = copyArrayValue;
						} else {
							String component = "" + componentType;
							targetValue = PrimitiveType.get(component).copyOf(targetValue);
						}

					} else if (!targetValue.getClass().toString().contains("java.")) {
						if (level < REQURENCY_LEVEL)
							targetValue = copy(annotation, targetValue, level);
					}
				}

				Method method = target.getClass().getMethod(setterMethodName, parTypes);
				method.invoke(target, targetValue);

			}
		}
		return target;
	}

	/**
	 * Porównanie dwóch obiektów. Metoda porownuje tylko wartosci pol, ktore dla
	 * ktorych ustwiona zostala odpowiednia annotacja. Metoda nie nadaje sie do
	 * porownywania zawartosci calych obiektow, chyba ze wszystkie pola zostana
	 * oznaczone wczesniej wymieniona annotacja. Dodatkowo, ze jej pomoca mozna
	 * wstrzyknac dynamiczna liste etykiet. Aby to zadzialalo, oczywiscie trzeba
	 * podac odpowiednie parametry annotacji (jezeli annotacja na to pozwala).
	 * Jezeli w obiektach wystepuja listy, to aby moc bezblednie je porownac
	 * nalezy pamietac o tym, ze w obiektach, ktore beda umieszczone w takiej
	 * liscie, trzeba dodac annotacje {@link javax.persistence.Id} przy polu
	 * definiujacym unikalny klucz lub przy odpowiedniej annotacji (tej którą
	 * obsługije zadana fabryka) podac atrybut 'referencedFieldName', ktore to
	 * wskażą pole jednoznacznie identyfikujace dany obiekt. Jezeli tego nie
	 * zrobimy moga zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzoenj annotacji
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	static List<ObjectsDifference> diff(
			AnnotationFactory annotationFactory,
			Object target,
			Object source)
			throws Exception {
		return diff(annotationFactory, target, source, null, null, 0);
	}

	/**
	 * Wlasciwa metoda porownania, pozwalajaca na wstrzykniecie dynamicznej listy
	 * etykiet dla porownywanych pol.
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzoenj annotacji
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @param labelList
	 *           hashtable z etykietami zmian pol
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	static List<ObjectsDifference> diff(
			AnnotationFactory annotationFactory,
			Object target,
			Object source,
			Hashtable<String, String> labelList)
			throws Exception {
		return diff(annotationFactory, target, source, labelList, null, 0);
	}

	/**
	 * Wlasciwa metoda porownania, dla list wywolywana rekurencyjnie. Metoda
	 * stara sie być niewrażliwa na różny 'characterset' porownywanych String-ów.
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzoenj annotacji
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @param labelList
	 *           hashtable z etykietami zmian pol
	 * @param parentLabel
	 *           jezeli jest metoda wywolywana rekurencyjnie to jest to etykieta
	 *           zmany zarejestrowana w poprzednim kroku
	 * @param level
	 *           poziom zagniezdzenia podczas porownywania
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static List<ObjectsDifference> diff(
			AnnotationFactory annotationFactory,
			Object target,
			Object source,
			Hashtable<String, String> labelList,
			String parentLabel,
			int level)
			throws Exception {

		level++;

		/*
		 * sprawdzam czy nieporownuje zrodla z wartoscia null. Muzsze miec klase,
		 * a moge je povrac tylko z obiektu not null.
		 */
		Object notNullObject = null;
		if (source != null) {
			notNullObject = source;
		} else if (target != null) {
			notNullObject = target;
		}

		/* sprawdzam czy nieporownuje dwoch wartosci null-owych */
		if (notNullObject == null)
			return new ArrayList<ObjectsDifference>();

		if ((target != null) && source != null && !target.getClass().equals(source.getClass()))
			throw new IllegalArgumentException(
					"Expect the same classes but have source at "
					+ source.toString()
					+ " and target at "
					+ target.toString());

		List<ObjectsDifference> result = new ArrayList<ObjectsDifference>();
		log.trace("Diff '{}' START", parentLabel);

		Field[] fields = getFields(notNullObject);
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotationFactory.getAnnotation())) {

				String label = annotationFactory.getLabel(labelList, parentLabel, field);

				String getterMethodName = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);

				Object sourceValue = null;
				Object targetValue = null;
				if (source != null)
					sourceValue = source.getClass().getMethod(getterMethodName)
							.invoke(source);
				if (target != null)
					targetValue = target.getClass().getMethod(getterMethodName)
							.invoke(target);

				Hashtable<Object, Object> targetHashtable = null;
				Hashtable<Object, Object> sourceHashtable = null;

				String referencedFieldName = annotationFactory.getReferencedFieldName(field);

				boolean componentTypeIsPrimitive = false;
				Class<?> componentType = null;

				if (field.getType().toString().equals(List.class.toString())
						|| field.getType().toString().equals(Vector.class.toString()))
				{
					if (referencedFieldName == null)
						log.warn("{}: Brak zdefiniowanej referencji do pola z unikalna "
								+ "wartoscia charakteryzujaca obiekt z {} dla pola '{}'. "
								+ "Porownanie obiektow moze przebiec nieprawidlowo.",
								new Object[]
							{ source.getClass(), field.getType().getCanonicalName(), field.getName() });

					if (sourceValue != null) {
						sourceHashtable = list2Hashtable(
								(List<Object>) sourceValue,
								referencedFieldName);
					}
					if (targetValue != null) {
						targetHashtable = list2Hashtable(
								(List<Object>) targetValue,
								referencedFieldName);
					}
				} else if (field.getType().toString().equals(Hashtable.class.toString())) {
					if (sourceValue != null) {
						sourceHashtable = hashtable2TmpCopy((Hashtable<Object, Object>) sourceValue);
					}
					if (targetValue != null) {
						targetHashtable = hashtable2TmpCopy((Hashtable<Object, Object>) targetValue);
					}
				} else if (field.getType().isArray()) {
					componentType = field.getType().getComponentType();
					componentTypeIsPrimitive = componentType.isPrimitive();
					if (!componentTypeIsPrimitive) {
						if (sourceValue != null) {
							sourceHashtable = array2Hashtable((Object[]) sourceValue);
						}
						if (targetValue != null) {
							targetHashtable = array2Hashtable((Object[]) targetValue);
						}
					} else {
						componentTypeIsPrimitive = true;
						if (sourceValue != null) {
							log.trace("Diff array source of primitive component {}", componentType);
						}
						if (targetValue != null) {
							log.trace("Diff array target of primitive component {}", componentType);
						}
					}
				}

				if (isDifferent(
						annotationFactory,
						level,
						field,
						targetValue,
						sourceValue,
						label,
						targetHashtable,
						sourceHashtable,
						componentTypeIsPrimitive,
						componentType))
				{
					if (targetHashtable != null && sourceHashtable != null) {
						/* sa rozne listy */
						result.addAll(diffHashtable(
								annotationFactory,
								label,
								targetHashtable,
								sourceHashtable,
								field,
								labelList,
								level));
					} else if (!componentTypeIsPrimitive) {
						/* pole nie jest macierza */
						if (field.getType().getCanonicalName().startsWith("java.")) {
							/* jest typem wbudowanym w java */
							Object sVal = sourceValue;
							Object tVal = targetValue;
							if (sVal != null) {
								sVal = tryConvertCalendarToString(sourceValue, sVal);
							}
							if (sVal != null) {
								tVal = tryConvertCalendarToString(targetValue, tVal);
							}

							result
									.add(new ObjectsDifference(label, tVal, sVal, field
									.getType()));
							log.trace("Diff (LEVEL={}): {}, {}, {}, {}",
									new Object[]
								{
										level, label, tVal, sVal,
										field.getType().getSimpleName() });
						} else if (level <= Duplicator.REQURENCY_LEVEL) {
							/*
							 * nie jest typem wbudowanym w java, ale moge dokonac
							 * kolejnego zagniezdzenia
							 */
							result.addAll(
									diff(
									annotationFactory,
									targetValue,
									sourceValue,
									labelList,
									label,
									level));
						} else {
							result
									.add(new ObjectsDifference(label, targetValue, sourceValue, field
									.getType()));
							log.trace("Diff (LEVEL={}): {}, {}, {}, {}",
									new Object[]
								{
										level, label, targetValue, sourceValue,
										field.getType().getSimpleName() });
						}

					} else if (componentTypeIsPrimitive
							&& ((sourceValue == null || targetValue == null)))
					{
						/* pole jest macierza i macierz zostala dodana lub usunieta */
						result
								.add(new ObjectsDifference(label, targetValue, sourceValue, field
								.getType()));
						log.trace("Diff (LEVEL={}): {}, {}, {}, {}",
								new Object[]
							{
									level, label, targetValue, sourceValue,
									field.getType().getSimpleName() });
					} else if (componentTypeIsPrimitive
							&& (sourceValue != null && targetValue != null)
							&& componentType != null)
					{
						/*
						 * pole jest macierza i w tej macierzy zostaly zmienione
						 * elementy
						 */
						String component = "" + componentType;
						Hashtable<Object, Object> sArrayHashtable =
								PrimitiveType.get(component).primitiveArray2Hashtable(sourceValue);
						Hashtable<Object, Object> tArrayHashtable =
								PrimitiveType.get(component).primitiveArray2Hashtable(targetValue);

						result.addAll(diffHashtable(
								annotationFactory,
								label,
								tArrayHashtable,
								sArrayHashtable,
								field,
								labelList,
								level));

					}

				}

			}
		}
		log.debug("Diff '{}' END", parentLabel);
		return result;
	}

	/**
	 * Metoda sprawdzajaca czy dany typ jest data, a jezeli tak, to sprobuje
	 * przekonwertowac dana date do string-a, tak aby w prosty sposob
	 * zaprezentowac roznice pol.
	 * 
	 * @param originalValue
	 *           obiekt wartosci do analizy
	 * @param convertedValue
	 *           obiekt do konwersji
	 * @return obiekt skonwertowany lub nie (w zaleznosci od tego czy obiekt
	 *         oryginalny byl faktycznie data
	 */
	private static Object tryConvertCalendarToString(Object originalValue, Object convertedValue) {
		if (originalValue instanceof java.util.Date) {
			convertedValue = Calendar.getInstance(timeZone);
			((Calendar) convertedValue).setTime((java.util.Date) originalValue);
		}
		if (convertedValue instanceof java.util.Calendar) {
			SimpleDateFormat sdf;
			((Calendar) convertedValue).setTimeZone(timeZone);
			if (((Calendar) convertedValue).getTimeInMillis() % 100000 == 0) {
				sdf = new SimpleDateFormat(getDateShortFormat());
			} else {
				sdf = new SimpleDateFormat(getDateLongFormat());
			}
			convertedValue = (String) sdf.format(((Calendar) convertedValue).getTime());
		}
		return convertedValue;
	}

	/**
	 * Porównanie dwóch obiektów. Metoda porownuje tylko wartosci pol, ktore dla
	 * ktorych ustwiona zostala odpowiednia annotacja. Metoda nie nadaje sie do
	 * porownywania zawartosci calych obiektow, chyba ze wszystkie pola zostana
	 * oznaczone wczesniej wymieniona annotacja. Dodatkowo, ze jej pomoca mozna
	 * wstrzyknac dynamiczna liste etykiet. Aby to zadzialalo, oczywiscie trzeba
	 * podac odpowiednie parametry annotacji (jezeli annotacja na to pozwala).
	 * Jezeli w obiektach wystepuja listy, to aby moc bezblednie je porownac
	 * nalezy pamietac o tym, ze w obiektach, ktore beda umieszczone w takiej
	 * liscie trzeba dodac annotacje Id.class przy polu definiujacym unikalny
	 * klucz lub przy annotacji podac atrybut 'referencedFieldName', ktore wskażą
	 * pole jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy
	 * moga zostać zwrócone nieoczekiwane wyniki. Metoda stara sie byc
	 * niewrażliwa na różny 'characterset' porownywanych String-ów.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.Property
	 * 
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzonej annotacji
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @return [true|false]
	 * @throws Exception
	 */
	static boolean assertEquals(AnnotationFactory annotationFactory, Object target, Object source)
			throws Exception {
		return assertEquals(annotationFactory, target, source, 0);
	}

	/**
	 * Porównanie dwóch obiektów. Metoda wywolywana rekurencyjnie. Porownuje
	 * tylko wartosci pol, ktore dla ktorych ustwiona zostala odpowiednia
	 * annotacja. Metoda nie nadaje sie do porownywania zawartosci calych
	 * obiektow, chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona
	 * annotacja. Dodatkowo, ze jej pomoca mozna wstrzyknac dynamiczna liste
	 * etykiet. Aby to zadzialalo, oczywiscie trzeba podac odpowiednie parametry
	 * annotacji (jezeli annotacja na to pozwala). Jezeli w obiektach wystepuja
	 * listy, to aby moc bezblednie je porownac nalezy pamietac o tym, ze w
	 * obiektach, ktore beda umieszczone w takiej liscie trzeba dodac annotacje
	 * Id.class przy polu definiujacym unikalny klucz lub przy annotacji podac
	 * atrybut 'referencedFieldName', ktore wskażą pole jednoznacznie
	 * identyfikujace dany obiekt. Jezeli tego nie zrobimy moga zostać zwrócone
	 * nieoczekiwane wyniki. Metoda stara sie byc niewrażliwa na różny
	 * 'characterset' porownywanych String-ów.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Trace
	 * @see pl.slawas.diffs.Property
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzonej annotacji
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @param level
	 *           aktualny poziom zagniezdzenia podczas porownywania rekurencji.
	 *           dla pierwszego wykonania level = 0
	 * @return [true|false]
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static boolean assertEquals(
			AnnotationFactory annotationFactory,
			Object target,
			Object source,
			int level)
			throws Exception {

		level++;

		/* sprawdzam czy nieporownuje wartosci null */
		if ((target == null && source != null)
				|| (target != null && source == null))
			return false;

		/* sprawdzam czy nieporownuje dwoch wartosci null-owych */
		if (target == null && source == null)
			return true;

		if (target != null && target.getClass() == null)
			throw new Exception("This is not possible");

		/* sprawdzam czy klasy obu obiektow sa takie same */
		if (target != null && target.getClass() != null
				&& !target.getClass().equals(source.getClass()))
			return false;

		log.trace("Equals(LEVEL={}) START", level);
		Field[] fields = getFields(source);
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotationFactory.getAnnotation())) {
				String getterMethodName = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Object sourceValue = source.getClass().getMethod(getterMethodName).invoke(source);
				Object targetValue = target.getClass().getMethod(getterMethodName).invoke(target);

				String label = annotationFactory.getLabel(field);

				String referencedFieldName = annotationFactory.getReferencedFieldName(field);

				Hashtable<Object, Object> targetHashtable = null;
				Hashtable<Object, Object> sourceHashtable = null;

				boolean componentTypeIsPrimitive = false;
				Class<?> componentType = null;

				if (field.getType().toString().equals(List.class.toString())
						|| field.getType().toString().equals(Vector.class.toString()))
				{
					if (referencedFieldName == null)
						log.warn("{}: Brak zdefiniowanej referencji do pola z unikalna "
								+ "wartoscia charakteryzujaca obiekt z {} dla pola '{}'. "
								+ "Porownanie obiektow moze przebiec nieprawidlowo.",
								new Object[]
							{ source.getClass(), field.getType().getCanonicalName(), field.getName() });

					if (sourceValue != null) {
						sourceHashtable = list2Hashtable(
								(List<Object>) sourceValue,
								referencedFieldName);
					}
					if (targetValue != null) {
						targetHashtable = list2Hashtable(
								(List<Object>) targetValue,
								referencedFieldName);
					}
				} else if (field.getType().toString().equals(Hashtable.class.toString())) {
					if (sourceValue != null) {
						sourceHashtable = hashtable2TmpCopy((Hashtable<Object, Object>) sourceValue);
					}
					if (targetValue != null) {
						targetHashtable = hashtable2TmpCopy((Hashtable<Object, Object>) targetValue);
					}
				} else if (field.getType().isArray()) {
					componentType = field.getType().getComponentType();
					componentTypeIsPrimitive = componentType.isPrimitive();
					if (!componentTypeIsPrimitive) {
						if (sourceValue != null) {
							sourceHashtable = array2Hashtable((Object[]) sourceValue);
						}
						if (targetValue != null) {
							targetHashtable = array2Hashtable((Object[]) targetValue);
						}
					} else {
						componentTypeIsPrimitive = true;
						if (sourceValue != null) {
							log.trace("Diff array source of primitive component {}", componentType);
						}
						if (targetValue != null) {
							log.trace("Diff array target of primitive component {}", componentType);
						}
					}
				}

				if (isDifferent(
						annotationFactory,
						level,
						field,
						targetValue,
						sourceValue,
						label,
						targetHashtable,
						sourceHashtable,
						componentTypeIsPrimitive,
						componentType))
					return false;

			}
		}
		log.trace("Equals(LEVEL={}) RETURN TRUE", level);
		return true;
	}

	/**
	 * Metoda sprawdzajaca czy wartosci pol są rózne. Metoda wykorzystywana przez
	 * prawie wszystkie glowne metody porownywania, mozliwa do wykorzytywania
	 * przez podobne fabryki korzystajace z dobrodziejstw tej klasy.
	 * 
	 * @see #assertEquals(AnnotationFactory, Object, Object, int)
	 * @see #equalsHashtable(AnnotationFactory, Field, String, Hashtable,
	 *      Hashtable, int)
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzonej annotacji
	 * @param level
	 *           poziom zagniezdzenia
	 * @param field
	 *           pole, ktorego wartosci sa porownywane
	 * @param targetValue
	 *           wartosc "nowa"
	 * @param sourceValue
	 *           wartosc "stara"
	 * @param label
	 *           pozim zagniezdzenia
	 * @param targetHashtable
	 *           ewentulana hashtablica wartosci pola, jezeli mamy do czynienia z
	 *           listami, macierzami, wartosc nowa
	 * @param sourceHashtable
	 *           ewentulana hashtablica wartosci pola, jezeli mamy do czynienia z
	 *           listami, macierzami, wartosc nowa
	 * @param componentTypeIsPrimitive
	 *           czy pole jest macierza typow prymitywnych?
	 * @param componentType
	 *           typ prymitywny (skladnik) pola, ktory jest macierza typow
	 *           prymitywnych
	 * @return zwraca dokladnie odwrotny wynik niz metoda "equals" tzn. jezeli
	 *         "equals" zwraca 'true' to metoda zwraca wartosc 'false'.
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	static boolean isDifferent(
			AnnotationFactory annotationFactory,
			int level,
			Field field,
			Object targetValue,
			Object sourceValue,
			String label,
			Hashtable<Object, Object> targetHashtable,
			Hashtable<Object, Object> sourceHashtable,
			boolean componentTypeIsPrimitive,
			Class<?> componentType) throws Exception, UnsupportedEncodingException {

		log.trace("'{}' (LEVEL={}) Check difference: {} == {}", new Object[]
			{ label, level, sourceValue, targetValue });

		if (targetHashtable != null && sourceHashtable != null) {

			if (!equalsHashtable(
					annotationFactory,
					field,
					label,
					targetHashtable,
					sourceHashtable,
					level))
			{
				log.trace("'{}' (LEVEL={}) Lists are different: return FALSE",
						new Object[]
					{ label, level });
				return true;
			}

		} else {

			/* dodano nowa liste */
			if (targetHashtable != null
					|| sourceHashtable != null)
			{
				log.trace("'{}' (LEVEL={}) New list was added: return FALSE",
						new Object[]
					{ label, level });
				return true;
			}

			/* zmieniono warosc pola na NULL */
			if ((sourceValue == null && targetValue != null)
					|| (sourceValue != null && targetValue == null))
			{
				log.trace("'{}' (LEVEL={}) Value of field was changed "
						+ "(before/after is NULL): return FALSE",
						new Object[]
					{ label, level });
				return true;
			}

			if (!componentTypeIsPrimitive && (sourceValue != null && targetValue != null)) {

				/* porownuje stringi */
				if (sourceValue.getClass().equals(String.class)) {
					String scompareValue = (String) sourceValue;
					if (!Strings.isInUTF8(scompareValue)) {
						log.trace("'{}': String is not in UTF-8 ", new Object[]
							{ scompareValue });
						scompareValue = new String((scompareValue).getBytes("UTF-8"));
					}

					String tcompareValue = (String) targetValue;
					if (!Strings.isInUTF8(tcompareValue)) {
						log.trace("'{}': String is not in UTF-8 ", new Object[]
							{ tcompareValue });
						tcompareValue = new String((tcompareValue).getBytes("UTF-8"));
					}

					if (!scompareValue.equals(tcompareValue)) {
						log.trace("'{}' (LEVEL={}) Strings are different: {}, {}, return FALSE",
								new Object[]
							{ label, level, scompareValue, tcompareValue });
						return true;
					}
				} else if (level <= Duplicator.REQURENCY_LEVEL
						&& !sourceValue.getClass().getCanonicalName().startsWith("java."))
				{
					if (!assertEquals(annotationFactory, targetValue, sourceValue, level)) {
						log.trace("'{}' (LEVEL={}) Custom equals - objects are different: return FALSE",
								new Object[]
							{ label, level });
						return true;
					}
				} else if (!sourceValue.equals(targetValue)) {
					if ((sourceValue instanceof java.util.Date
							&& targetValue instanceof java.util.Date)
							|| (sourceValue instanceof java.util.Calendar
							&& targetValue instanceof java.util.Calendar))
					{
						/*
						 * jezeli to daty, to sprawdzam czy czasy sa sobie rowne, bo
						 * obiekty moga byc rozne ze wzgledu np. na strefe czasowa
						 */
						Object sConvertedValue = sourceValue;
						Object tConvertedValue = targetValue;
						if (sourceValue instanceof java.util.Date
								&& targetValue instanceof java.util.Date)
						{
							sConvertedValue = Calendar.getInstance();
							tConvertedValue = Calendar.getInstance();
							((Calendar) sConvertedValue).setTime((java.util.Date) sourceValue);
							((Calendar) tConvertedValue).setTime((java.util.Date) targetValue);
						}
						if (((Calendar) sConvertedValue).getTimeInMillis() != ((Calendar) tConvertedValue)
								.getTimeInMillis())
						{
							log.trace("'{}' (LEVEL={}) Date equals - dates are different: return FALSE",
									new Object[]
								{ label, level });
							return true;
						}
					} else {
						log.trace(
								"'{}' (LEVEL={}) Standard equals - objects are different: return FALSE",
								new Object[]
							{ label, level });
						return true;
					}
				}
			} else if (componentTypeIsPrimitive
					&& (sourceValue != null && targetValue != null)
					&& componentType != null)
			{

				String component = "" + componentType;
				Hashtable<Object, Object> sArrayHashtable =
						PrimitiveType.get(component).primitiveArray2Hashtable(sourceValue);
				Hashtable<Object, Object> tArrayHashtable =
						PrimitiveType.get(component).primitiveArray2Hashtable(targetValue);

				if (!equalsHashtable(
						annotationFactory,
						field,
						label,
						tArrayHashtable,
						sArrayHashtable,
						level))
				{
					log.trace("'{}' (LEVEL={}) Arrays are different: return FALSE",
							new Object[]
						{ label, level });
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Metoda pomocnicza zwracajaca wartosc pola charakteryzujaca unikalny obiekt
	 * znajdujacy sie na liscie
	 * 
	 * @see javax.persistence.Id
	 * 
	 * @param obj
	 *           analizowany obiekt
	 * @param idFieldName
	 *           nazwa pola z identyfikatorem, jezeli jest null, wtedy metoda
	 *           sprobuje znalez pole z annotacja javax.persistence.Id
	 * @return wartosc pola o podanej nazwie albo (jezeli nazwa pola nie zostala
	 *         podana) pola oznaczonego annotacja Id.class
	 * @throws Exception
	 */
	private static Object getIdValue(Object obj, String referencedFieldName) throws Exception {

		if (referencedFieldName != null) {
			String methodName = "get"
					+ referencedFieldName.substring(0, 1).toUpperCase()
					+ referencedFieldName.substring(1);
			return obj.getClass().getMethod(methodName).invoke(obj);
		}

		Field[] fields = getFields(obj);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				String methodName = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				return obj.getClass().getMethod(methodName).invoke(obj);
			}
		}
		return null;
	}

	/**
	 * Metoda zamienia liste na hashtable. Metoda niezbedna do poprawnego
	 * porownywania list.
	 * 
	 * @param list
	 *           lista do porownania
	 * @param idFieldName
	 *           nazwa pola z identyfikatorem, jezeli jest null, wtedy metoda
	 *           sprobuje znalez pole z annotacja javax.persistence.Id
	 * @return przetworzona lista do porownania jako hashtable
	 * @throws Exception
	 */
	static Hashtable<Object, Object> list2Hashtable(
			List<Object> list,
			String referencedFieldName)
			throws Exception {
		Hashtable<Object, Object> result = new Hashtable<Object, Object>();
		Integer indx = Integer.valueOf(0);

		for (Object obj : list) {
			Object key = null;
			if (!obj.getClass().toString().contains("java.lang.")) {
				key = getIdValue(obj, referencedFieldName);
			}
			if (key == null)
				key = indx;
			log.trace("--> list2Hashtable: {}", key);
			result.put(key, obj);
			indx++;
		}
		return result;
	}

	/**
	 * Metoda zmieniająca tablice (array) do hashtable. Metoda niezbedna do
	 * poprawnego porownywania tablic.
	 * 
	 * @param array
	 * @return
	 * @throws Exception
	 */
	static Hashtable<Object, Object> array2Hashtable(Object[] array)
			throws Exception {
		Hashtable<Object, Object> result = new Hashtable<Object, Object>();
		Integer indx = Integer.valueOf(0);
		for (Object obj : array) {
			Object key = indx;
			log.trace("--> array2Hashtable: {}", key);
			result.put(key, obj);
			indx++;
		}
		return result;
	}

	/**
	 * Metoda potrzebna do zrobienia kopii tymczasowej hashtable, jezeli typ
	 * kolekcji to hashtable. Metoda niezbedna do porownywania wrtosci w
	 * hashtable.
	 * 
	 * @param hashtable
	 * @return
	 */
	static Hashtable<Object, Object> hashtable2TmpCopy(
			Hashtable<Object, Object> hashtable) {
		Hashtable<Object, Object> result = new Hashtable<Object, Object>();
		Enumeration<Object> hashtableKeys = hashtable.keys();
		while (hashtableKeys.hasMoreElements()) {
			Object key = hashtableKeys.nextElement();
			log.trace("--> hashtable2TmpCopy: {}", key);
			result.put(key, hashtable.get(key));
		}
		return result;
	}

	/**
	 * Porownanie obiektow znajdujacych sie w dwich hashtable
	 * 
	 * @param annotationFactory
	 *           bean zarzadzajacy argumentami sledzonej annotacji
	 * @param field
	 *           pole, ktorego wartosci sa porownywane
	 * @param label
	 *           etykieta porownania
	 * @param target
	 *           nowa wartosc, tablica musi byc tymczasowa, bo jej elementy sa
	 *           usuwane i nie nadaje sie do ponownego uzytku
	 * @param source
	 *           stara wartosc, tablica musi byc tymczasowa, bo jej elementy sa
	 *           usuwane i nie nadaje sie do ponownego uzytku
	 * @param level
	 *           poziom zagniezdzenia podczas porownywania
	 * @return wynik porownania [true|false]
	 * @throws Exception
	 */
	private static boolean equalsHashtable(
			AnnotationFactory annotationFactory,
			Field field,
			String label,
			Hashtable<Object, Object> target,
			Hashtable<Object, Object> source,
			int level) throws Exception {

		log.debug("--> target.size(): {}; source.size(): {}", new Object[]
			{ target.size(), source.size() });
		if (target.size() != source.size())
			return false;

		Enumeration<Object> sourceKeys = source.keys();
		Hashtable<Object, Object> testTarget = new Hashtable<Object, Object>();
		testTarget.putAll(target);

		while (sourceKeys.hasMoreElements()) {
			Object key = sourceKeys.nextElement();
			log.trace("--> equalHashtable: {}", key);
			Object sourceValue = source.get(key);
			Object targetValue = testTarget.get(key);
			if (targetValue == null) {
				return false;
			}

			if (isDifferent(
					annotationFactory,
					level,
					field,
					targetValue,
					sourceValue,
					label + "[" + key + "]",
					null,
					null,
					false,
					null))
				return false;

			testTarget.remove(key);
		}
		log.debug("--> testTarget.isEmpty(): {}; testTarget.size: {}", new Object[]
			{ testTarget.isEmpty(), testTarget.size() });
		return testTarget.isEmpty();
	}

	/**
	 * Metoda wyciagajaca zmiany obiektow umieszczonych w listach.
	 * 
	 * @param label
	 *           etykieta zmian
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @param field
	 *           pole listy
	 * @param labelList
	 *           dynamiczne etykiety zmian
	 * @param level
	 *           poziom zagniezdzenia podczas porownywania
	 * @return lista znalezionych zmian pomiedzy obiektami umieszczonymi w
	 *         listach
	 * @throws Exception
	 */
	private static List<ObjectsDifference> diffHashtable(
			AnnotationFactory annotationFactory,
			String label,
			Hashtable<Object, Object> target,
			Hashtable<Object, Object> source,
			Field field,
			Hashtable<String, String> labelList,
			int level)
			throws Exception {
		List<ObjectsDifference> result = new ArrayList<ObjectsDifference>();
		Enumeration<Object> sourceKeys = source.keys();
		while (sourceKeys.hasMoreElements()) {
			Object key = sourceKeys.nextElement();
			Object sourceValue = source.get(key);
			Object targetValue = target.get(key);
			if (log.isTraceEnabled()) {
				if (sourceValue.getClass().toString().contains("java.lang"))
					log
							.trace(
									"\n[Source's hastable] --> diffHashtable (LEVEL={}):\n key: {}:\n source: {}\n target: {}",
									new Object[]
										{ level, key, sourceValue, targetValue });
				else
					log.trace("\n[Source's hastable] --> diffHashtable (LEVEL={}): {}",
							new Object[]
						{ level, key });
			}
			boolean targetWasNull = false;
			if (targetValue == null) {
				targetWasNull = true;
				targetValue = sourceValue.getClass().newInstance();
			}

			if (sourceValue.getClass().toString().contains("java.lang")) {
				if (!sourceValue.equals(targetValue)) {
					result
							.add(new ObjectsDifference(
							label + "[" + key.toString() + "]",
							targetValue,
							sourceValue,
							field
							.getType()));
					log.trace("Diff: {}, {}, {}, {}",
							new Object[]
						{ label, targetValue, sourceValue, field.getType().getSimpleName() });
				}
			} else {
				result.addAll(
						diff(
						annotationFactory,
						targetValue,
						sourceValue,
						labelList,
						label + "[" + key.toString() + "]",
						level));
			}

			if (!targetWasNull) {
				target.remove(key);
			}
			source.remove(key);
		}
		Enumeration<Object> targetKeys = target.keys();
		while (targetKeys.hasMoreElements()) {
			Object key = targetKeys.nextElement();
			Object targetValue = target.get(key);
			Object sourceValue = targetValue.getClass().newInstance();
			if (log.isTraceEnabled()) {
				if (sourceValue.getClass().toString().contains("java.lang"))
					log
							.trace(
									"\n[Target's hashtable] --> diffHashtable (LEVEL={}): \n key: {}:\n source: {}\n target: {}",
									new Object[]
										{ level, key, "null", targetValue });
				else
					log.trace("\n[Target's hashtable] --> diffHashtable (LEVEL={}): {}",
							new Object[]
						{ level, key });
			}
			if (sourceValue.getClass().toString().contains("java.lang")) {
				result
						.add(new ObjectsDifference(
						label + "[" + key.toString() + "]",
						targetValue,
						sourceValue,
						field
						.getType()));
				log.trace("Diff: {}, {}, {}, {}",
						new Object[]
					{ label, targetValue, sourceValue, field.getType().getSimpleName() });
			} else {
				result.addAll(
						diff(
						annotationFactory,
						targetValue,
						sourceValue,
						labelList,
						label + "[" + key.toString() + "]",
						level));
			}
		}

		return result;
	}

	/**
	 * Metoda wyciagajaca pola danego obiektu. Implementacja zawiera mozliwosc
	 * siegniecia do pol "superklasy", ale tylko i wylacznie do "superklasy",
	 * ktora jest bezposrednio zwiazana z badanym obiektem.
	 * 
	 * @param obj
	 *           badany obiekt
	 * @return lista pol badanego obiektu
	 */
	public static Field[] getFields(Object obj) {
		Field[] fields;
		Field[] localfields = obj.getClass().getDeclaredFields();
		Class<?> superclass = obj.getClass().getSuperclass();
		if (superclass != null
				&& !superclass.getCanonicalName().startsWith("java")
				&& !superclass.getCanonicalName().startsWith("com.sun"))
		{

			Field[] superfields = superclass.getDeclaredFields();
			if (superfields.length != 0) {
				log.debug("superclass = {}, declaredFields is {}",
						new Object[]
					{ superclass.getCanonicalName(),
							superfields.length
						}
						);
				int superfieldsSize = superfields.length;
				int localfieldsSize = localfields.length;
				fields = new Field[superfieldsSize + localfieldsSize];
				int i = 0;
				for (Field f : localfields) {
					fields[i] = f;
					i++;
				}
				for (Field f : superfields) {
					fields[i] = f;
					i++;
				}
			} else {
				fields = localfields;
			}
		} else {
			fields = localfields;
		}
		return fields;
	}

	/**
	 * Pobranie aktualnego formatu daty dlugiej (z godzina)
	 * 
	 * @return the dateLongFormat
	 */
	static String getDateLongFormat() {
		return (customDateLongFormat != null ? customDateLongFormat : dateLongFormat);
	}

	/**
	 * Przywrócenie oryginalnych ustawień formatów daty zdefiniowanych w
	 * {@link #dateLongFormat} oraz {@link #dateShortFormat} oraz ustwienie
	 * domytślnej zony ustawionej w {@link #timeZone}.
	 */
	static void resetDateFormats() {
		customDateLongFormat = null;
		customDateShortFormat = null;
		customTimeZone = null;
	}

	/**
	 * Ustawienie formatu daty dlugiej (z godzina)
	 * 
	 * @see java.text.SimpleDateFormat
	 * 
	 * @param dateLongFormat
	 *           the dateLongFormat to set
	 */
	static void setDateLongFormat(String dateLongFormat) {
		customDateLongFormat = dateLongFormat;
	}

	/**
	 * Pobranie aktualnego formatu daty krotkiej (bez godziny)
	 * 
	 * @return the dateShortFormat
	 */
	static String getDateShortFormat() {
		return (customDateShortFormat != null ? customDateShortFormat : dateShortFormat);
	}

	/**
	 * Ustawienie formatu daty krotkiej (bez godziny)
	 * 
	 * @see java.text.SimpleDateFormat
	 * 
	 * @param dateShortFormat
	 *           the dateShortFormat to set
	 */
	static void setDateShortFormat(String dateShortFormat) {
		Duplicator.customDateShortFormat = dateShortFormat;
	}

	/**
	 * Metoda probujaca konwertowac dany typ do string-a. Implementacja obejmuje:
	 * <ul>
	 * <li>rozpoznanie i konwersja {@link java.util.Date}
	 * <li>rozpoznanie i konwersja {@link java.util.Calendar}
	 * </ul>
	 * 
	 * @param originalValue
	 *           obiekt wartosci do analizy
	 * @return obiekt skonwertowany lub nie (w zaleznosci od tego czy obiekt
	 *         oryginalny byl faktycznie data) przekształcony do string-a
	 */
	static String tryConvertToString(Object originalValue) {
		if (originalValue instanceof java.util.Date || originalValue instanceof java.util.Calendar) {
			return (String) tryConvertCalendarToString(originalValue, originalValue);
		}
		return originalValue.toString();
	}

	/**
	 * @param timeZone
	 *           the timeZone to set
	 */
	static void setTimeZone(TimeZone timeZone) {
		customTimeZone = timeZone;
	}
	
	/**
	 * @return the timeZone
	 */
	static TimeZone getTimeZone() {
		return (customTimeZone != null ? customTimeZone : timeZone);
	}
}
