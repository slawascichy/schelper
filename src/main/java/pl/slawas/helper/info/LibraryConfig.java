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
package pl.slawas.helper.info;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import pl.slawas.helpers.Configurations;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * ClientConfig - klasa zarządająca parametrami uruchomienia klienta.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1.2.2 $
 * 
 */
public class LibraryConfig {

	final protected static Logger logger = LoggerFactory
			.getLogger(LibraryConfig.class);

	public static final String FILE_NAME = "lib.properties";

	public static final String FILE_DIRECTORY = "";

	private static Map<String, String> _Properties = null;

	private static Object lock = new Object();

	private static LibraryConfig _Instance;

	private boolean isInvalid = true;

	private Properties propertyList;

	/**
	 * Konstruktor domyślny z domyślną ścieżką położenia pliku konfoguracyjnego
	 * {@link #FILE_DIRECTORY}/{@link #FILE_NAME}
	 * 
	 */
	private LibraryConfig() {
		loadPropertiesFromFile();
		propertyList = generatePropertyList();
	}

	/**
	 * Konstruktor ze wskazaniem innej lokalizacji położenia pliku
	 * konfiguracyjnego
	 * 
	 * @param configFileName
	 *            pełna ścieżka pliku konfiguracyjnego
	 */
	private LibraryConfig(String configFileName) {
		loadPropertiesFromFile(configFileName);
		propertyList = generatePropertyList();
	}

	/**
	 * Metoda ladująca właściwości z pliku
	 * 
	 * @throws Exception
	 */
	private void loadPropertiesFromFile() {
		String filePath = (new StringBuilder(FILE_DIRECTORY)).append("/")
				.append(FILE_NAME).toString().intern();
		synchronized (filePath) {
			loadPropertiesFromFile(filePath);
		}
	}

	/**
	 * Metoda ladująca właściwości z pliku
	 * 
	 * @throws Exception
	 */
	private void loadPropertiesFromFile(String configFileName) {

		synchronized (lock) {
			logger.debug("Loading configuration....");

			if (_Properties != null) {
				_Properties.clear();
			}
			_Properties = Configurations.loadHashtable(LibraryConfig.class,
					configFileName);
		}

	}

	private Properties generatePropertyList() {

		synchronized (lock) {
			this.propertyList = new Properties();
			Set<Entry<String, String>> propEntrySet = _Properties.entrySet();
			for (Entry<String, String> propEntry : propEntrySet) {
				String key = propEntry.getKey();
				String value = propEntry.getValue();
				this.propertyList.put(key, value);
			}

			isInvalid = false;
			return this.propertyList;
		}
	}

	/**
	 * Pobranie instancji konfiguracji
	 * 
	 * @return instancja konfiguracji klienta
	 */
	public static LibraryConfig getInstance() {
		synchronized (lock) {
			if (_Instance == null) {
				logger.debug("Get new instance.");
				_Instance = new LibraryConfig();
			}
			return _Instance;
		}
	}

	/**
	 * Pobranie instancji konfiguracji ze wskazaniem nowej lokalizacji pliku
	 * konfiguracyjnego
	 * 
	 * @param configFileName
	 *            pełna ścieżka pliku konfiguracyjnego
	 * @return instancja pliku konfiguracji
	 */
	public static LibraryConfig getInstance(String configFileName) {
		synchronized (lock) {
			if (_Instance == null) {
				logger.debug("Get new instance.");
				_Instance = new LibraryConfig(configFileName);
			}
			return _Instance;
		}
	}

	public String get(String propertyCode) {
		return (String) _Properties.get(propertyCode);
	}

	public void put(String propertyCode, String value) {
		_Properties.put(propertyCode, value);
		isInvalid = true;
	}

	public Properties getPropertyList() {
		if (this.isInvalid || this.propertyList == null)
			generatePropertyList();
		return this.propertyList;
	}

}
