/**
 * 
 */
package pl.slawas.context;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * 
 * Context podstawowe metody obsługi wejścia do metod
 * 
 * @author Karol Kowalczyk
 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
 * @version $Revision: 1.1 $
 *
 */
public class Context implements Serializable {

	private static final long serialVersionUID = -2755058896817320793L;

	/** Nazwa aplikacji */
	private String appName;
	/** Wersja aplikacji */
	private String appVersion;
	/** Nazwa użytkownika */
	private String userName;
	/** Komentarz użytkownika (dlaczego wykonuje daną akcję) */
	private String comment;
	/** Maksymalna liczba wyników uzyskana w wyniku wyszukiwania danych */
	private Integer maxResults;
	/** Definicja ewentualnego timeout'u operacji - liczba milisekund */
	private int queryTimeout;
	/** Definicja wersji językowej użytkownika */
	private String locale;
	/** Definicja strefy czasowej użytkownika */
	private String timeZone;
	/** Dodatkowa informacja na temat użytkownika - pełna nazwa */
	private String userFullName;

	/**
	 * Domyślny konstruktor
	 */
	public Context() {
	}

	/**
	 * Konstruktor z bezpośrednim ustawianiem pól
	 * 
	 * @param appName
	 * @param appVersion
	 * @param userName
	 * @param comment
	 * @param maxResults
	 * @param queryTimeout
	 * @param locale
	 * @param timeZone
	 * @param userFullName
	 */
	public Context(String appName, String appVersion, String userName,
			String comment, Integer maxResults, int queryTimeout,
			String locale, String timeZone, String userFullName) {
		super();
		this.appName = appName;
		this.appVersion = appVersion;
		this.userName = userName;
		this.comment = comment;
		this.maxResults = maxResults;
		this.queryTimeout = queryTimeout;
		this.locale = locale;
		this.timeZone = timeZone;
		this.userFullName = userFullName;
	}

	/**
	 * Konstruktor do szybkiego ustawienie parametrów wg kolejności za pomocą
	 * listy stringów
	 * 
	 * @param properties
	 *            parametry w kolejności:
	 *            <ol>
	 *            <li>appName</li>
	 *            <li>appVersion</li>
	 *            <li>userName</li>
	 *            <li>comment</li>
	 *            <li>maxResults</li>
	 *            <li>queryTimeout</li>
	 *            <li>locale</li>
	 *            <li>timeZone</li>
	 *            <li>userFullName</li>
	 *            </ol>
	 */
	public Context(final String... properties) {
		this();
		if (properties != null) {
			int i = 0;
			String property;
			while (i < properties.length) {
				property = properties[i++];
				switch (i) {
				case 1:
					appName = property;
					break;
				case 2:
					appVersion = property;
					break;
				case 3:
					userName = property;
					break;
				case 4:
					comment = property;
					break;
				case 5:
					maxResults = Integer.getInteger(property);
					break;
				case 6:
					queryTimeout = Integer.parseInt(property);
					break;
				case 7:
					locale = property;
					break;
				case 8:
					timeZone = property;
					break;
				case 9:
					userFullName = property;
					break;
				}
			}
		}
	}

	/**
	 * Tworzy nowy obiekt będący kopią bieżącego i ustawia nowe parametry
	 * 
	 * @param newProperties
	 *            parametry w kolejności:
	 *            <ol>
	 *            <li>userName</li>
	 *            <li>comment</li>
	 *            <li>maxResults</li>
	 *            <li>queryTimeout</li>
	 *            <li>locale</li>
	 *            <li>timeZone</li>
	 *            <li>userFullName</li>
	 *            </ol>
	 * @return nowy kontekst
	 */
	public Context copy(final String... newProperties) {
		Context newContext = new Context(this.appName, this.appVersion);
		if (newProperties != null) {
			int i = 0;
			String newProperty;
			while (i < newProperties.length) {
				newProperty = newProperties[i++];
				switch (i) {
				case 1:
					newContext.setUserName(newProperty);
					break;
				case 2:
					newContext.setComment(newProperty);
					break;
				case 3:
					newContext.setMaxResults(Integer.getInteger(newProperty));
					break;
				case 4:
					newContext.setQueryTimeout(Integer.parseInt(newProperty));
					break;
				case 5:
					newContext.setLocale(newProperty);
					break;
				case 6:
					newContext.setTimeZone(newProperty);
					break;
				case 7:
					newContext.setUserFullName(newProperty);
					break;
				}
			}
		}
		return newContext;
	}

	/**
	 * Nazwa użytkownika usługi
	 * 
	 * @return Nazwa użytkownika usługi
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            Nazwa użytkownika usługi
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Komentarz do wykonania usługi
	 * 
	 * @return Komentarz do wykonania usługi
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            Komentarz do wykonania usługi
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Parametry językowe użytkownika w formacie 'język_terytorium' np.
	 * {@code pl_PL}
	 * 
	 * @return Parametry językowe użytkownika
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            definicja lokalizacji użytkownika w postaci string'a np.
	 *            {@code 'pl_PL'}
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Strefa czasowa użytkownika
	 * 
	 * @return Strefa czasowa użytkownika
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * Ustawienie strefy czasowej użytkownika.
	 * 
	 * @param timeZone
	 *            definicja strefy czasowej użytkownika w postaci string'a np.
	 *            {@code 'Europe/Warsaw'}. Zobacz
	 *            {@link TimeZone#getAvailableIDs()}.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * Nazwa aplikacji użytkownika usługi
	 * 
	 * @return Nazwa aplikacji użytkownika usługi
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Ustawienie nazwy aplikacji użytkownika usługi.
	 * 
	 * @param appName
	 *            Nazwa aplikacji użytkownika usługi
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * Wersja aplikacji użytkownika usługi
	 * 
	 * @return Wersja aplikacji użytkownika usługi
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * Ustawienie wersji aplikacji użytkownika usługi.
	 * 
	 * @param appVersion
	 *            Wersja aplikacji użytkownika usługi
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * Timeout zapytania w bazie danych. Wartość 0 oznacza brak ustawienia.
	 * 
	 * @return Timeout zapytania w bazie danych
	 */
	public int getQueryTimeout() {
		return queryTimeout;
	}

	/**
	 * Ustawienie timeout zapytania w bazie danych. Wartość 0 oznacza brak
	 * ustawienia.
	 * 
	 * @param queryTimeout
	 *            Timeout zapytania w bazie danych
	 */
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	/**
	 * Maksymalna liczba zwracanych wierszy
	 * 
	 * @return Maksymalna liczba zwracanych wierszy
	 */
	public Integer getMaxResults() {
		return maxResults;
	}

	/**
	 * Ustawia maksymalną liczbę zwracanych wierszy
	 * 
	 * @return Maksymalna liczba zwracanych wierszy
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	/**
	 * Tekst do logowania parametrów
	 */
	@Override
	public String toString() {
		return "Context[" + appName + ' ' + appVersion + ' ' + userName + ' '
				+ comment + ' ' + maxResults + ' ' + queryTimeout + ' '
				+ locale + ' ' + timeZone + ']';
	}
}
