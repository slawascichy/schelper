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
package pl.slawas.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import pl.slawas.helpers.PrimitiveType;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


/**
 * 
 * IdUtils
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2.2.1 $
 * 
 */
public class IDUtils {

	private static Logger logger = LoggerFactory.getLogger(IDUtils.class);

	/**
	 * W miarę uniwersalna metoda konwertująca wartośc identyfikatora podaną jako
	 * string do odpowiedniego obiektu (z odpowiednią wartością) na podstwie
	 * klasy endji.
	 * 
	 * @param singleClass
	 *           klasa encji
	 * @param str
	 *           wartość identyfikatora w postaci string-a
	 * @return odpowiedni obiekt identyfikatora z odpowiednią wartośćią
	 * @throws IDUtilsErrorException
	 */
	public static Object stringToId(Class<?> singleClass, String str) throws IDUtilsErrorException {
		logger.debug("[stringToId] Otrzymałem wartość '{}'", str);
		Field[] fields = singleClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				try {
					Class<?> type = field.getType();
					String typeName = type.getName();
					if (PrimitiveType.isPrimitiveType(typeName)) {
						return PrimitiveType.get(typeName).setFromString(str);
					} else if (typeName.equals("java.lang.String")) {
						return str;
					} else if (typeName.startsWith("java.lang")) {
						Class<?> parTypes[] = new Class[1];
						parTypes[0] = String.class;
						Method method = type.getMethod("valueOf", parTypes);
						return method.invoke(type, str);
					} else
						throw new IDCantGetValueOfIdFieldException();
				} catch (Exception e) {
					throw new IDUtilsErrorException(e);
				}
			}
		}
		throw new IDCantGetIdAnnotationException();
	}

	/**
	 * Pobranie wartości identyfikatore na podstawie obiektu encji
	 * 
	 * @param single
	 *           obiekt encji z polem identyfikatora (oznaczonym annotacją
	 *           {@link Id})
	 * @return wartość identyfikatora
	 * @throws IDUtilsErrorException
	 */
	public static Object getObjectId(Object single) throws IDUtilsErrorException {
		Field[] fields = single.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				try {
					String methodName = "get"
							+ field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
					return single.getClass().getMethod(methodName).invoke(single);
				} catch (Exception e) {
					throw new IDUtilsErrorException(e);
				}
			}
		}
		throw new IDCantGetIdAnnotationException();
	}

	/**
	 * Ustawianie wartości identyfikatora dla obiektu
	 * 
	 * @param single
	 *           obiekt encji z polem identyfikatora (oznaczonym annotacją
	 *           {@link Id})
	 * @param id
	 *           wartość identyfikatora jaka ma być ustawiona
	 * @return zmieniony obiekt encji
	 * @throws IDUtilsErrorException
	 */
	public static Object setObjectId(Object single, Object id) throws IDUtilsErrorException {
		Field[] fields = single.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				Class<?> parTypes[] = new Class[]
					{ field.getType() };
				try {
					Object idValue = single.getClass().getClassLoader().loadClass(
							field.getType().getCanonicalName()).cast(id);
					String methodName = "set"
							+ field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
					single.getClass().getMethod(methodName, parTypes).invoke(single, idValue);
					return single;
				} catch (Exception e) {
					logger.error("Blad ustawienia identyfikatora: ",e);
					throw new IDUtilsErrorException(e);
				}
			}
		}
		throw new IDCantGetIdAnnotationException();
	}

	/**
	 * Pobieranie nazwy pola z identyfikatorem dla podanej klasy encji
	 * 
	 * @param entityClass
	 *           klasa encji
	 * @return nazwa pola z identyfikatorem
	 * @throws IDUtilsErrorException
	 */
	public static String getIdFieldName(Class<?> entityClass) throws IDUtilsErrorException {
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				return field.getName();
			}
		}
		throw new IDCantGetIdAnnotationException();
	}

}
