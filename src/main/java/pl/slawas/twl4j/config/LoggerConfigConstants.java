package pl.slawas.twl4j.config;

public interface LoggerConfigConstants {

	/**
	 * Parametr systemowy wskazujący/zmieniający położenie pliku konfiguracji
	 * systemu dodawany jako argument wirtualnej maszyny Java podczas
	 * uruchamiania np. {@code -Dmercury.config.file=/etc/mercury/config.prop}
	 */
	String PROP_CONFIG_LOCATION_FILE = "sclogger.config.file";

	String PROP_CONFIG_PRINT_PROPERTIES = "config.printProperties";

	/** Nazwa parametru definiującego format daty */
	String PROP_DATE_LOGGER_FORMAT = "logger.date.format";
	/** Ustawienie poziomu logowania */
	String PROP_LOGGER_LEVEL = "logger.level";
	/** Ustawienie czy ma być dodana data zdarzenia podczas jego logowania */
	String PROP_LOGGER_ADDDATE = "logger.addDate";
	/**
	 * Ustawienie co ma implementować logowanie (implementacja wyjścia
	 * logowania)
	 */
	String PROP_LOGGER_APPENDER = "logger.appender";

	/** Nazwa pliku z konfiguracją */
	String CONFIG_FILE_NAME_PATH = "sclogger.properties";

	/** Domyślny format daty */
	public static final String DEFAULT_DATE_LOGGER_FORMAT = "yyyy-MM-dddd HH:mm:ss,SSS";
	/**
	 * Domyślne ustawienie, czy ma być dodawana data do rejestrowanego
	 * komunikatu
	 */
	public static final boolean DEFAULT_LOGGER_ADD_DATE = false;

}
