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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import pl.slawas.helpers.PrimitiveType;
import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


/**
 * PropertyCreator - klasa przetwarzajaca obiekt na {@link java.util.Properties}
 * . Aby mogla tego dokonac w obiekcie musza byc pola z annotacja
 * {@link Property}
 * 
 * @see pl.slawas.diffs.Property
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2.2.2 $
 * 
 */
public class PropertyCreator {

	public static final String PROPERTY_TYPE = "TYPE";

	public static final String ARRAY_TYPE = "Array";

	public static final String PROPERTY_COMPONENT_TYPE = "COMPONENT";

	public static final String PROPERTY_KEY_TYPE = "KEY";

	public static final String PROPERTY_ARRAY_SIZE = "SIZE";

	final protected static Logger log = LoggerFactory.getLogger(PropertyCreator.class);

	/**
	 * Kopiowanie obiektu do nowej instancji, klonowanie obiektu. Powstaly obiekt
	 * ma skopiowane tylko te pola, dla ktorych ustwiona zostala annotacja
	 * {@link Property}. Metoda nie nadaje sie do kopiowania (klonowania) calych
	 * obiektow, chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona
	 * annotacja.
	 * 
	 * @see pl.slawas.diffs.Property
	 * @see pl.slawas.diffs.Duplicator#copy(Class, Object)
	 * 
	 * @param source
	 *           obiekt do skopiowania (sklonowania)
	 * @return kopia obiektu
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object copy(Object source)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {

		return Duplicator.copy(Property.class, source);
	}

	/**
	 * Kopiowanie wartosci pol z jednego obiektu do drugiego. Powstaly obiekt ma
	 * skopiowane tylko te pola, dla ktorych ustwiona zostala annotacja
	 * {@link Property}. Metoda nie nadaje sie do kopiowania zawartosci calych
	 * obiektow, chyba ze wszystkie pola zostana oznaczone wczesniej wymieniona
	 * annotacja.
	 * 
	 * @see pl.slawas.diffs.Property
	 * @see pl.slawas.diffs.Duplicator#copy(Class, Object, Object)
	 * 
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
	public static Object copy(Object target, Object source)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			SecurityException,
			InvocationTargetException,
			NoSuchMethodException {
		return Duplicator.copy(Property.class, target, source);
	}

	/**
	 * Metoda do wykonania porownania dwoch obiektow i wygenerowania listy zmian.
	 * Metoda porownuje tylko wartosci pol, ktore dla ktorych ustwiona zostala
	 * annotacja {@link Property}. Metoda nie nadaje sie do porownywania
	 * zawartosci calych obiektow, chyba ze wszystkie pola zostana oznaczone
	 * wczesniej wymieniona annotacja. Jezeli w obiektach wystepuja listy, to aby
	 * moc bezblednie je porownac nalezy pamietac o tym, ze w obiektach, ktore
	 * beda umieszczone w takiej liscie trzeba dodac annotacje
	 * {@link javax.persistence.Id} przy polu definiujacym unikalny klucz lub
	 * przy annotacji podac atrybut 'referencedFieldName', ktore wskażą pole
	 * jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy moga
	 * zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Property
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	public static List<ObjectsDifference> diff(Object target, Object source)
			throws Exception {
		return Duplicator.diff(new PropertyAnnotationFactory(), target, source);
	}

	/**
	 * Metoda do wykonania porownania dwoch obiektow i wygenerowania listy zmian.
	 * Metoda porownuje tylko wartosci pol, ktore dla ktorych ustwiona zostala
	 * annotacja {@link Property}. Metoda nie nadaje sie do porownywania
	 * zawartosci calych obiektow, chyba ze wszystkie pola zostana oznaczone
	 * wczesniej wymieniona annotacja. Jezeli w obiektach wystepuja listy, to aby
	 * moc bezblednie je porownac nalezy pamietac o tym, ze w obiektach, ktore
	 * beda umieszczone w takiej liscie trzeba dodac annotacje
	 * {@link javax.persistence.Id} przy polu definiujacym unikalny klucz lub
	 * przy annotacji podac atrybut 'referencedFieldName', ktore wskażą pole
	 * jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy moga
	 * zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Property
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @param labelList
	 *           hashtable z etykietami zmian pol
	 * @return lista zarejestrowanych zmian
	 * @throws Exception
	 */
	public static List<ObjectsDifference> diff(
			Object target,
			Object source,
			Hashtable<String, String> labelList)
			throws Exception {
		return Duplicator.diff(new PropertyAnnotationFactory(), target, source, labelList);
	}

	/**
	 * Porównanie dwóch obiektów. Metoda porownuje tylko wartosci pol, ktore dla
	 * ktorych ustwiona zostala annotacja {@link Property}. Metoda nie nadaje sie
	 * do porownywania zawartosci calych obiektow, chyba ze wszystkie pola
	 * zostana oznaczone wczesniej wymieniona annotacja. Dodatkowo, ze jej pomoca
	 * mozna wstrzyknac dynamiczna liste etykiet. Aby to zadzialalo, oczywiscie
	 * trzeba podac odpowiednie parametry annotacji Property. Jezeli w obiektach
	 * wystepuja listy, to aby moc bezblednie je porownac nalezy pamietac o tym,
	 * ze w obiektach, ktore beda umieszczone w takiej liscie trzeba dodac
	 * annotacje {@link javax.persistence.Id} przy polu definiujacym unikalny
	 * klucz lub przy annotacji podac atrybut 'referencedFieldName', ktore wskażą
	 * pole jednoznacznie identyfikujace dany obiekt. Jezeli tego nie zrobimy
	 * moga zostać zwrócone nieoczekiwane wyniki.
	 * 
	 * @see javax.persistence.Id
	 * @see pl.slawas.diffs.Property
	 * @see pl.slawas.diffs.ObjectsDifference
	 * 
	 * 
	 * @param target
	 *           nowa wartosc
	 * @param source
	 *           stara wartosc
	 * @return [true|false]
	 * @throws Exception
	 */
	public static boolean assertEquals(Object target, Object source)
			throws Exception {
		return Duplicator.assertEquals(new PropertyAnnotationFactory(), target, source);
	}

	/**
	 * Metoda zamieniajaca obiekt do właściwości. Aby metoda była skuteczna pola
	 * obiektu musza być opatrzone annotacja {@link Property}
	 * 
	 * @see pl.slawas.diffs.Property
	 * 
	 * @param obj
	 *           przetwarzany obiekt
	 * @return właściwości
	 * @throws Exception
	 */
	public static Properties toProperties(Object obj)
			throws Exception {

		Properties properties = new Properties();
		properties.putAll(toProperties(obj, null, 0));
		return properties;
	}

	/**
	 * Metoda właściwa przetwarzania, wywoływana rekurencyjnie. Zabezpieczeniem
	 * przed nieskonczona petla jest pole level. Metoda wykorzystuje metody
	 * transformacji list i wektorow z klasy {@link Duplicator}.
	 * 
	 * @see pl.slawas.diffs.Duplicator#array2Hashtable(Object[])
	 * @see pl.slawas.diffs.Duplicator#hashtable2TmpCopy(Hashtable)
	 * @see pl.slawas.diffs.Duplicator#list2Hashtable(List, String)
	 * 
	 * @param obj
	 *           przetwarzany obiekt
	 * @param parentKey
	 *           klucz pola nadrzednego
	 * @param level
	 *           poziom zagniezdzenia
	 * @return mapa właściwości właściwości
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> toProperties(Object obj, String parentKey, int level)
			throws Exception {

		Map<String, Object> properties = new Hashtable<String, Object>();
		if (level == 0) {
			properties.put((parentKey == null ? "" : parentKey + ".") + PROPERTY_TYPE, ""
					+ obj.getClass().getCanonicalName());
			log
					.debug(
					"--> MAIN: toProperties LEVEL={}: {}",
					new Object[]
				{
						level,
						properties.get((parentKey == null ? "" : parentKey + ".")
						+ PROPERTY_TYPE) });
		}
		level++;

		Field[] fields;
		fields = Duplicator.getFields(obj);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Property.class)) {
				String getterMethodName = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);

				String key = ((Property) field.getAnnotation(Property.class)).key();
				if (key.equals(PropertyAnnotationFactory.DEFAULT_KEY)) {
					key = field.getName();
				}
				if (parentKey != null) {
					key = parentKey + "." + key;
				}

				String referencedFieldName = (new PropertyAnnotationFactory())
						.getReferencedFieldName(field);

				Object value = (Object)
						obj.getClass().getMethod(getterMethodName).invoke(obj);

				Hashtable<Object, Object> valueHashtable = null;

				boolean componentTypeIsPrimitive = false;
				Class<?> componentType = null;
				if (field.getType().toString().equals(List.class.toString())) {
					if (value != null && !((List<Object>) value).isEmpty()) {
						properties
								.put(key + "." + PROPERTY_TYPE, "" + field.getType().getCanonicalName());
						log.trace("--> toProperties LEVEL={}: {}",
								new Object[]
							{ level, properties.get(key + "." + PROPERTY_TYPE) });
						properties.put(key + "." + PROPERTY_COMPONENT_TYPE, ((List<Object>) value).get(0)
								.getClass().getCanonicalName());
						valueHashtable = Duplicator.list2Hashtable(
								(List<Object>) value,
								referencedFieldName);
					}
				} else if (field.getType().toString().equals(Hashtable.class.toString())) {
					if (value != null && !((Hashtable<Object, Object>) value).isEmpty()) {
						properties
								.put(key + "." + PROPERTY_TYPE, "" + field.getType().getCanonicalName());
						log.trace("--> toProperties LEVEL={}: {}",
								new Object[]
							{ level, properties.get(key + "." + PROPERTY_TYPE) });
						properties.put(
								key + "." + PROPERTY_COMPONENT_TYPE,
								((Hashtable<Object, Object>) value).elements().nextElement()
								.getClass().getCanonicalName());

						properties.put(
								key + "." + PROPERTY_KEY_TYPE,
								((Hashtable<Object, Object>) value).keys().nextElement()
								.getClass().getCanonicalName());
						valueHashtable = Duplicator.hashtable2TmpCopy((Hashtable<Object, Object>) value);
					}
				} else if (field.getType().toString().equals(Vector.class.toString())) {
					if (value != null && !((Vector<Object>) value).isEmpty()) {
						properties
								.put(key + "." + PROPERTY_TYPE, "" + field.getType().getCanonicalName());
						log.trace("--> toProperties LEVEL={}: {}",
								new Object[]
							{ level, properties.get(key + "." + PROPERTY_TYPE) });
						properties.put(key + "." + PROPERTY_COMPONENT_TYPE, ((Vector<Object>) value).get(
								0)
								.getClass().getCanonicalName());
						valueHashtable = Duplicator.list2Hashtable(
								(Vector<Object>) value,
								referencedFieldName);
					}
				} else if (field.getType().isArray()) {
					componentType = field.getType().getComponentType();
					componentTypeIsPrimitive = componentType.isPrimitive();
					if (!componentTypeIsPrimitive) {
						if (value != null && ((Object[]) value).length != 0) {
							properties.put(key + "." + PROPERTY_TYPE, ARRAY_TYPE);
							log.trace("--> toProperties LEVEL={}: {}",
									new Object[]
								{ level, properties.get(key + "." + PROPERTY_TYPE) });
							properties.put(key + "." + PROPERTY_COMPONENT_TYPE, ((Object[]) value)[0]
									.getClass().getCanonicalName());
							valueHashtable = Duplicator.array2Hashtable((Object[]) value);
						}
					} else {
						componentTypeIsPrimitive = true;
						if (value != null) {
							properties.put(key + "." + PROPERTY_TYPE, ARRAY_TYPE);
							log.trace("--> toProperties LEVEL={}: {}",
									new Object[]
								{ level, properties.get(key + "." + PROPERTY_TYPE) });
							String component = "" + componentType;
							properties.put(key + "." + PROPERTY_COMPONENT_TYPE, component);
							valueHashtable = PrimitiveType.get(component).primitiveArray2Hashtable(value);
						}
					}

				}

				if (valueHashtable != null) {
					properties.putAll(hashtable2PropertyMap(
							key,
							valueHashtable,
							level));
				} else if (value != null) {
					log.trace("--> [BEFORE] Key: {}: Value: {}", new Object[]
						{ key, value });
					if (!field.getType().getCanonicalName().startsWith("java.")
							&& level <= Duplicator.REQURENCY_LEVEL)
					{
						if (field.getType().isPrimitive()) {
							String component = "" + field.getType();
							properties.put(key + "." + PROPERTY_TYPE, component);
							log.trace("--> [Prim] toProperties LEVEL={}: {}",
									new Object[]
								{ level, properties.get(key + "." + PROPERTY_TYPE) });
							properties.put(key, value);
						} else {
							properties.put(key + "." + PROPERTY_TYPE, value.getClass().getCanonicalName());
							log.trace("--> [Over] toProperties LEVEL={}: {}",
									new Object[]
								{ level, properties.get(key + "." + PROPERTY_TYPE) });
							properties.putAll(toProperties(value, key, level));
						}
					} else {
						properties.put(key + "." + PROPERTY_TYPE, value.getClass().getCanonicalName());
						log.trace("--> [Java] toProperties LEVEL={}: {}",
								new Object[]
							{ level, properties.get(key + "." + PROPERTY_TYPE) });
						properties.put(key, value);
					}
					log.trace("--> [AFTER] Key: {}: Value: {}", new Object[]
						{ key, properties.get(key) });
				}
			}
		}

		return properties;

	}

	/**
	 * Metoda tworzaca nowy obiek na podstawie podanych wlaściwości
	 * 
	 * @param extendedClassLoader
	 *           classLoader przestrzeni (pakietu), który użyje tej funkcji.
	 *           Jeżeli nie zostanie ustwaiony metoda nie bedzie mogła utworzyć
	 *           instancji klasy, która jest zdefiniowana poza biblioteką.
	 * @param properties
	 *           wlasciwosci
	 * @return nowy obiekt na podstawie podanych właściwości
	 * @throws Exception
	 */
	public static Object createFromProperties(ClassLoader extendedClassLoader, Properties properties)
			throws Exception {
		return createNewInstance(extendedClassLoader, properties, null, 0);
	}

	/**
	 * Metoda właściwa tworząca nową instancje obiektu.
	 * 
	 * @param extendedClassLoader
	 *           classLoader przestrzeni (pakietu), który użyje tej funkcji.
	 *           Jeżeli nie zostanie ustwaiony metoda nie bedzie mogła utworzyć
	 *           instancji klasy, która jest zdefiniowana poza biblioteką.
	 * @param properties
	 *           właściwości
	 * @param parentKey
	 *           klucz właściwości nadrzędnej
	 * @param level
	 *           poziom zagnieżdzenia
	 * @return nowa instancja obiektu, pod warunkiem, że wartość właściwości jest
	 *         różna od null
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object createNewInstance(
			ClassLoader extendedClassLoader,
			Properties properties,
			String parentKey,
			int level)
			throws Exception {

		Object newObject = null;
		level++;
		parentKey = ((parentKey == null) ? "" : parentKey);
		String propTypeKey = (parentKey.equals("") ? "" : parentKey + ".") + PROPERTY_TYPE;
		String type = (String) properties.get(propTypeKey);

		if (StringUtils.isBlank(type)) {
			log.trace("Nie moge zidentyfikowac typu dla {}", propTypeKey);
			return newObject;
		}

		log.trace("Bede tworzyc nowy obiekt {} dla key '{}'", new Object[]
			{ type, propTypeKey });

		if (type.equals(List.class.getCanonicalName())
				|| type.equals(Hashtable.class.getCanonicalName())
				|| type.equals(Vector.class.getCanonicalName())
				|| type.equals(ARRAY_TYPE))
		{

			parentKey = (parentKey.equals("") ? "" : parentKey + ".");
			String componentType = (String) properties.get(parentKey + PROPERTY_COMPONENT_TYPE);
			if (componentType == null)
				throw new Exception("Nie moge zidentyfikowac typu komponentu");

			if (type.equals(List.class.getCanonicalName())) {
				String[] groupedKeys = propertyGroupByKey(
						properties,
						parentKey);
				log.trace("Bede tworzyc nowa liste z komponentem {}", componentType);
				Class<?> clazz = extendedClassLoader.loadClass(componentType);
				newObject = new ArrayList();
				for (String key : groupedKeys) {
					Object value = properties.get(key);
					log.trace("{} = {}", new Object[]
						{ key, value });
					if (value != null) {
						((List) newObject).add(value);
					} else if (level <= Duplicator.REQURENCY_LEVEL + 1) {
						value = clazz.newInstance();
						updateInstance(
								extendedClassLoader,
								value,
								properties,
								key,
								level);
						((List) newObject).add(value);
					}
				}

			} else if (type.equals(Hashtable.class.getCanonicalName())) {
				String[] groupedKeys = propertyGroupByKey(
						properties,
						parentKey);
				log.trace("Bede tworzyc nowa hastable z komponentem {}", componentType);
				String keyType = (String) properties.get(parentKey + PROPERTY_KEY_TYPE);
				if (keyType == null)
					throw new Exception("Nie moge zidentyfikowac typu klucza");

				Class<?> clazz = extendedClassLoader.loadClass(componentType);
				Class<?> keyClazz = extendedClassLoader.loadClass(keyType);

				newObject = new Hashtable();
				for (String key : groupedKeys) {

					Object hashKey = null;
					if (!keyClazz.getCanonicalName().equals(String.class.getCanonicalName())) {
						Class<?> parTypes[] = new Class[1];
						parTypes[0] = String.class;
						Method method = keyClazz.getMethod("valueOf", parTypes);
						String hashKeyValue = Strings.replaceAll(key, parentKey, "");
						hashKey = method.invoke(keyClazz, hashKeyValue);
					} else {
						hashKey = Strings.replaceAll(key, parentKey, "");
					}

					Object value = properties.get(key);
					log.trace("{} = {}", new Object[]
						{ key, value });
					if (value != null) {
						((Hashtable) newObject).put(hashKey, value);
					} else if (level <= Duplicator.REQURENCY_LEVEL + 1) {
						value = clazz.newInstance();
						updateInstance(
								extendedClassLoader,
								value,
								properties,
								key,
								level);
						((Hashtable) newObject).put(hashKey, value);
					}
				}

			} else if (type.equals(Vector.class.getCanonicalName())) {
				String[] groupedKeys = propertyGroupByKey(
						properties,
						parentKey);
				log.trace("Bede tworzyc nowy wektor z komponentem {}", componentType);
				Class<?> clazz = extendedClassLoader.loadClass(componentType);
				newObject = new Vector();
				for (String key : groupedKeys) {
					Object value = properties.get(key);
					log.trace("{} = {}", new Object[]
						{ key, value });
					if (value != null) {
						((Vector) newObject).add(value);
					} else if (level <= Duplicator.REQURENCY_LEVEL + 1) {
						value = clazz.newInstance();
						updateInstance(
								extendedClassLoader,
								value,
								properties,
								key,
								level);
						((Vector) newObject).add(value);
					}
				}
			} else if (type.equals(ARRAY_TYPE)) {
				int size = Integer.valueOf((String) properties.get(parentKey + PROPERTY_ARRAY_SIZE));
				log.trace("Bede tworzyc nowa macierz z komponentem {}", type);
				boolean componentTypeIsPrimitive = PrimitiveType.isPrimitiveType(componentType);
				if (componentTypeIsPrimitive) {
					PrimitiveType pType = PrimitiveType.get(componentType);
					log.trace("Komponent jest typem prymitywnym {}", componentType);
					newObject = pType.createArray(size);
					for (int i = 0; i < size; i++) {
						Object value = properties.get("" + parentKey + i);
						log.trace("{} = {}", new Object[]
							{ parentKey + i, value });
						if (value != null)
							pType.set(newObject, i, value.toString());
					}
				} else if (componentType.contains("java.lang")) {
					log.trace("Komponent jest typem prostym {}", componentType);
					Class<?> clazz = extendedClassLoader.loadClass(componentType);
					newObject = Array.newInstance(clazz, size);
					for (int i = 0; i < size; i++) {
						Object value = properties.get("" + parentKey + i);
						log.trace("{} = {}", new Object[]
							{ parentKey + i, value });
						if (value != null) {
							((Object[]) newObject)[i] = value;
						}
					}
				} else {
					Class<?> clazz = extendedClassLoader.loadClass(componentType);
					newObject = Array.newInstance(clazz, size);
					for (int i = 0; i < size; i++) {
						String key = "" + parentKey + i;
						Object value = properties.get(key);
						log.trace("{} = {}", new Object[]
							{ key, value });
						if (value != null) {
							((Object[]) newObject)[i] = value;
						} else if (level <= Duplicator.REQURENCY_LEVEL + 1) {
							value = clazz.newInstance();
							updateInstance(
									extendedClassLoader,
									value,
									properties,
									key,
									level);
							((Object[]) newObject)[i] = value;
						}
					}

				}
			}
		} else if (!type.startsWith("java.")) {
			Class<?> clazz = extendedClassLoader.loadClass(type);
			try {
				log.trace("Tworze nowy obiekt {}", clazz.getName());
				newObject = clazz.newInstance();
				updateInstance(
						extendedClassLoader,
						newObject,
						properties,
						parentKey,
						level);
			} catch (InstantiationException e) {
				log.trace("InstantiationException dla {}", clazz.getName());
			}
		}

		return newObject;

	}

	/**
	 * Metoda pomocnicza aktualizująca obiekty podstawowe (nie kolekcje!!) na
	 * podstawie podanych właściwości. Gdy wartość właściwości (property value)
	 * jest typu {@link java.lang.String}, a oryginalny typ jest inny (systuacja
	 * powszechna gdy właściwości wcześniej były składowane w pliku tekstowym),
	 * to implementacja obejmuje obsługę nestępujących wyjątków (typy):
	 * 
	 * <ul>
	 * <li>typy prymitywne (zobacz {@link PrimitiveType})
	 * <li>obiekty Java (np. {@link java.lang.Integer}) z metoda statyczna
	 * "valueOf(String)"
	 * <li>typy daty: {@link java.util.Date} oraz {@link java.util.Calendar}
	 * </ul>
	 * 
	 * 
	 * @param extendedClassLoader
	 *           classLoader przestrzeni (pakietu), który użyje tej funkcji.
	 *           Jeżeli nie zostanie ustwaiony metoda nie bedzie mogła utworzyć
	 *           instancji klasy, która jest zdefiniowana poza biblioteką.
	 * @param obj
	 *           swiezo utworzony obiekt
	 * @param properties
	 *           właścowości
	 * @param parentKey
	 *           klucz właściwości nadrzędnej
	 * @param level
	 *           poziom zagnieżdżenia
	 * @throws Exception
	 */
	private static void updateInstance(
			ClassLoader extendedClassLoader,
			Object obj,
			Properties properties,
			String parentKey,
			int level)
			throws Exception {

		parentKey = ((parentKey == null || parentKey.equals("")) ? "" : parentKey + ".");

		Field[] fields;
		fields = Duplicator.getFields(obj);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Property.class)) {
				String key = ((Property) field.getAnnotation(Property.class)).key();
				if (key.equals(PropertyAnnotationFactory.DEFAULT_KEY)) {
					key = parentKey + field.getName();
				} else {
					key = parentKey + key;
				}

				String setterMethodName = "set"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Class<?> parTypes[] = new Class[1];
				parTypes[0] = field.getType();

				Object value = properties.get(key);
				log.trace("{}: value = '{}', type = '{}', valueType = '{}'", new Object[]
					{ key, value, field.getType(), (value != null ? value.getClass() : "NULL") });
				if (value != null) {
					Method method = obj.getClass().getMethod(setterMethodName, parTypes);

					if (!value.getClass().equals(field.getType())) {
						/* obsluga wyjątków */
						if (field.getType().isPrimitive()) {
							/* typy prymitywne (zobacz {@link PrimitiveType}) */
							PrimitiveType p = PrimitiveType.get(field.getType().getName());
							switch (p) {
							case INT:
								int valInt = Integer.valueOf(value.toString()).intValue();
								method.invoke(obj, valInt);
								break;
							case BOOLEAN:
								boolean booleanValue = Boolean.valueOf(value.toString()).booleanValue();
								method.invoke(obj, booleanValue);
								break;
							case BYTE:
								byte byteValue = Byte.valueOf(value.toString()).byteValue();
								method.invoke(obj, byteValue);
								break;
							case CHAR:
								char valChar = (char) Integer.valueOf(value.toString()).intValue();
								method.invoke(obj, valChar);
								break;
							case DOUBLE:
								double doubleValue = Double.valueOf(value.toString()).doubleValue();
								method.invoke(obj, doubleValue);
								break;
							case FLOAT:
								float floatValue = Float.valueOf(value.toString()).floatValue();
								method.invoke(obj, floatValue);
								break;
							case LONG:
								long longValue = Long.valueOf(value.toString()).longValue();
								method.invoke(obj, longValue);
								break;
							case SHORT:
								short shortValue = Short.valueOf(value.toString()).shortValue();
								method.invoke(obj, shortValue);
								break;
							default:
								return;
							}
						} else {
							boolean isSuccess = false;
							try {
								/*
								 * nie raz roznica typow moze wynikac z tego, ze mamy do
								 * czynienia z interfejsem, a za obiektem kryje sie
								 * gotowa implementacja, zatem wpierw sprobujemy rzucic
								 * obiekt do oczekiwanego typu...
								 */
								Object val = extendedClassLoader.loadClass(
										field.getType().getCanonicalName()).cast(value);
								method.invoke(obj, val);
								isSuccess = true;
							} catch (ClassCastException e) {
								log.trace("Can't cast {} to {}", new Object[]
									{ value.getClass(), field.getType() });
								/*
								 * nie udalo sie, zatem obsluguje wyjatki gdzie wartości
								 * właściwości są typu {@link java.lang.String} (np.
								 * zwiazane z przechowywaniem danych w pliku tekstowym)
								 */
								if (value.getClass().equals(String.class)) {
									if (field.getType().toString().contains("Date")) {
										try {
											SimpleDateFormat sdf = new SimpleDateFormat(
													Duplicator.getDateLongFormat());
											method.invoke(obj, sdf.parse((String) value));
											isSuccess = true;
										} catch (ParseException pELong) {
											try {
												SimpleDateFormat sdf = new SimpleDateFormat(
														Duplicator.getDateShortFormat());
												method.invoke(obj, sdf.parse((String) value));
												isSuccess = true;
											} catch (ParseException pEShort) {
											}
										}
										if (isSuccess)
											log.trace("New object of date {}", new Object[]
												{ field.getType() });

									}
									if (!isSuccess
											&& field.getType().toString().contains("Calendar"))
									{
										Calendar calendar = Calendar.getInstance(Duplicator.getTimeZone());
										try {
											SimpleDateFormat sdf = new SimpleDateFormat(
													Duplicator.getDateLongFormat());
											calendar.setTime(sdf.parse((String) value));
											method.invoke(obj, calendar);
											isSuccess = true;
										} catch (ParseException pELong) {
											try {
												SimpleDateFormat sdf = new SimpleDateFormat(
														Duplicator.getDateShortFormat());
												calendar.setTime(sdf.parse((String) value));
												method.invoke(obj, calendar);
												isSuccess = true;
											} catch (ParseException pEShort) {
											}
										}
										if (isSuccess)
											log.trace("New object of date {}", new Object[]
												{ field.getType() });
									}
								}
							}

							if (!isSuccess && field.getType().toString().contains("String")) {
								try {
									/* sprobuje teraz przepisac dany obiekt do stringa */
									log.trace("Konwertuje {} do string-a.", value.getClass());
									Class<?> valueClazz = value.getClass();
									Class<?> primitiveArgument = PrimitiveType.getPrimitiveClass(value);
									if (primitiveArgument == null) {
										primitiveArgument = value.getClass();
									}
									Class<?> fieldParTypes[] = new Class[1];
									fieldParTypes[0] = primitiveArgument;
									Method valueMethod = valueClazz.getMethod("toString", fieldParTypes);
									Object objValue = valueMethod.invoke(valueClazz, value);
									method.invoke(obj, objValue);
									isSuccess = true;
								} catch (Exception e) {
									log.warn(
											"Nie udalo sie skonwerowac wartosci typu '{}' do string-a: {}",
											new Object[]
										{ value.getClass().getName(), e.getMessage() });
								}
							}

							if (!isSuccess) {
								Class<?> fieldClazz = field.getType();
								Class<?> fieldParTypes[] = new Class[1];
								fieldParTypes[0] = String.class;
								Method fieldMethod = fieldClazz.getMethod("valueOf", fieldParTypes);
								Object objValue = fieldMethod.invoke(fieldClazz, value.toString());
								method.invoke(obj, objValue);
							}
						}
					} else {
						method.invoke(obj, value);
					}
				} else if (level <= Duplicator.REQURENCY_LEVEL) {
					value = createNewInstance(
							extendedClassLoader,
							properties,
							key,
							level);
					if (value != null) {
						log.trace("{}: {} ", new Object[]
							{ key, value });
						Method method = obj.getClass().getMethod(setterMethodName, parTypes);
						method.invoke(obj, value);
					}
				}
			}
		}

	}

	/**
	 * Metoda pomocnicza grupujaca klucze o zadanym prefixsie. Niezbedna do
	 * konwersji własciwosci do list i hashtable.
	 * 
	 * @param properties
	 *           lista wlasciwosci
	 * @param parentKey
	 *           prefix grupowanych kluczy właściwości
	 * @return tablica pogrupowanych kluczy wlasciwosci
	 */
	private static String[] propertyGroupByKey(
			Properties properties,
			String parentKey) {
		Hashtable<String, Object> keys = new Hashtable<String, Object>();
		Enumeration<Object> keysForGroup = properties.keys();
		String empty = "";
		while (keysForGroup.hasMoreElements()) {
			String key = (String) keysForGroup.nextElement();
			if (key.startsWith(parentKey)) {
				key = Strings.replaceAll(key, parentKey, "");
				if (key.indexOf('.') >= 0) {
					key = key.substring(0, key.indexOf('.'));
				}
				if (!key.equals(PROPERTY_ARRAY_SIZE)
						&& !key.equals(PROPERTY_TYPE)
						&& !key.equals(PROPERTY_COMPONENT_TYPE)
						&& !key.equals(PROPERTY_KEY_TYPE))
				{
					keys.put(parentKey + key, empty);
				}
			}
		}
		String[] result = new String[keys.size()];
		int indx = 0;
		Enumeration<String> keysForResult = keys.keys();
		while (keysForResult.hasMoreElements()) {
			result[indx] = keysForResult.nextElement();
			indx++;
		}
		log.debug("Result (first 4): {}, {}, {}, {}", result);
		return result;
	}

	private static Map<String, Object> hashtable2PropertyMap(
			String key,
			Hashtable<Object, Object> source,
			int level)
			throws Exception {
		Map<String, Object> result = new Hashtable<String, Object>();
		result.put(key + "." + PROPERTY_ARRAY_SIZE, Integer.toString(source.size()));
		Enumeration<Object> sourceKeys = source.keys();
		while (sourceKeys.hasMoreElements()) {
			Object sourceKey = sourceKeys.nextElement();
			Object sourceValue = source.get(sourceKey);
			log.trace(
					"\n --> hashtable2PropertyMap (LEVEL={}):\n key: {}:\n value: {}",
					new Object[]
				{ level, sourceKey, sourceValue });
			if (sourceValue.getClass().toString().contains("java.lang")) {
				result.put(key + "." + sourceKey.toString(), sourceValue);
			} else {
				result.putAll(
						toProperties(
						sourceValue,
						key + "." + sourceKey.toString(),
						level));
			}
		}

		return result;
	}

	/**
	 * PropertyCreator korzysta z formatu daty umieszczonego w klasie
	 * {@link Duplicator}. Niniejsza metoda pozwala na ustawienie formatu daty
	 * dlugiej (z godzina) wlasnie w klasie {@link Duplicator}.
	 * 
	 * @see java.text.SimpleDateFormat
	 * @see Duplicator#dateLongFormat
	 * 
	 * @param dateLongFormat
	 *           the Duplicator.dateLongFormat to set
	 */
	public static void setDateLongFormat(String dateLongFormat) {
		Duplicator.setDateLongFormat(dateLongFormat);
	}

	/**
	 * PropertyCreator korzysta z formatu daty umieszczonego w klasie
	 * {@link Duplicator}. Niniejsza metoda pozwala na ustawienie formatu daty
	 * krótkiej (bez godziny) wlasnie w klasie {@link Duplicator}.
	 * 
	 * @see java.text.SimpleDateFormat
	 * @see Duplicator#dateShortFormat
	 * 
	 * @param dateShortFormat
	 *           the Duplicator.dateShortFormat to set
	 */
	public static void setDateShortFormat(String dateShortFormat) {
		Duplicator.setDateShortFormat(dateShortFormat);
	}

	/**
	 * @return the timeZone
	 */
	public static TimeZone getTimeZone() {
		return Duplicator.getTimeZone();
	}

	/**
	 * @param timeZone
	 *           the timeZone to set
	 */
	public static void setTimeZone(TimeZone timeZone) {
		Duplicator.setTimeZone(timeZone);
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
	public static String tryConvertToString(Object originalValue) {
		return Duplicator.tryConvertToString(originalValue);
	}

	/**
	 * Przywrócenie oryginalnych ustawień formatów daty zdefiniowanych w
	 * {@link Duplicator#dateLongFormat} oraz {@link Duplicator#dateShortFormat}
	 * oraz ustwienie domytślnej zony ustawionej w {@link Duplicator#timeZone}.
	 */
	public static void resetDateFormats() {
		Duplicator.resetDateFormats();
	}

}
