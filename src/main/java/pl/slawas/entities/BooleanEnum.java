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

import javax.persistence.Id;

import pl.slawas.xml.XMLNameValuePairUtils;

/**
 * 
 * BooleanEnum - symulacja encji z wartościami boolean w wersji językowej
 * polskiej oraz angielskiej.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 */
public enum BooleanEnum implements DictionaryWithIntegerId {

	YES, NO, UNKNOWN;

	/**
	 * Stała reprezentująca definicję języka polskiego (wartość domyślna dla pola
	 * {@link #lang}).
	 * 
	 * @see BooleanLangEnum#POLISH
	 */
	public final static String POLISH = BooleanLangEnum.POLISH.getLangCode();

	/**
	 * Stała reprezentująca definicję języka angielskiego
	 * 
	 * @see BooleanLangEnum#ENGLISH
	 */
	public final static String ENGLISH = BooleanLangEnum.ENGLISH.getLangCode();

	private String lang = BooleanLangEnum.POLISH.getLangCode();

	/**
	 * Pobieranie kodu
	 * 
	 * @return kod reprezentacji wartości logicznych w zależności od ustawionej
	 *         wartości {@link #lang}
	 */
	public String getCode() {
		switch (this) {
		case YES:
			return BooleanLangEnum.getByLangCode(lang).getYes();
		case NO:
			return BooleanLangEnum.getByLangCode(lang).getNo();
		case UNKNOWN:
		default:
			return BooleanLangEnum.getByLangCode(lang).getUnknown();
		}
	}

	/**
	 * Pobieranie opisu
	 * 
	 * @return opis reprezentacji wartości logicznych w zależności od ustawionej
	 *         wartości {@link #lang}
	 */
	public String getDescription() {
		switch (this) {
		case YES:
		case NO:
			return this.getCode();
		case UNKNOWN:
		default:
			return BooleanLangEnum.getByLangCode(lang).getDescriptionOfUnknown();
		}
	}

	/**
	 * Metoda pomocnicza pozwalająca na pobrznie szybkiej informacji czy ma
	 * zostać pobrany nagówek polaz z formularza
	 * 
	 * @return {@code true}, jeżeli {@link BooleanEnum#YES}, w przeciwnym wypadku
	 *         {@code false}
	 */
	public boolean useLabel() {
		switch (this) {
		case YES:
			return true;
		case NO:
		case UNKNOWN:
		default:
			return false;
		}
	}

	/**
	 * Ustawaianie kodu, metoda nie wspierana
	 * 
	 * @see java.lang.UnsupportedOperationException()
	 */
	@Deprecated
	public void setCode(String code) {
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * Ustawianie opisu, metoda nie wspierana
	 * 
	 * @see java.lang.UnsupportedOperationException()
	 */
	@Deprecated
	public void setDescription(String description) {
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * Pobieranie opisu
	 * 
	 * @see #getDescription()
	 * 
	 * @return opis reprezentacji wartości logicznych w zależności od ustawionej
	 *         wartości {@link #lang}
	 */
	public String getName() {
		return getDescription();
	}

	/**
	 * Pobieranie wartości
	 * 
	 * @see #getCode()
	 * 
	 * @return wartości reprezentacji wartości logicznych w zależności od
	 *         ustawionej wartości {@link #lang}
	 */
	public String getValue() {
		return getCode();
	}

	/**
	 * Ustawianie nazwy, metoda nie wspierana
	 * 
	 * @see java.lang.UnsupportedOperationException()
	 */
	@Deprecated
	public void setName(String name) {
		setDescription(name);
	}

	/**
	 * Ustawianie wartości, metoda nie wspierana
	 * 
	 * @see java.lang.UnsupportedOperationException()
	 */
	@Deprecated
	public void setValue(String value) {
		setCode(value);
	}

	public String toXMLItem() {
		return XMLNameValuePairUtils.toXMLItem(this).toString();
	}

	/**
	 * Pobieranie identyfikatora
	 */
	@Id
	public Integer getId() {
		switch (this) {
		case YES:
			return Integer.parseInt(BooleanLangEnum.INTEGER.getYes());
		case NO:
			return Integer.parseInt(BooleanLangEnum.INTEGER.getNo());
		case UNKNOWN:
		default:
			return Integer.parseInt(BooleanLangEnum.INTEGER.getUnknown());
		}
	}

	/**
	 * Ustawianie wartości identyfikatora, metoda nie wspierana
	 * 
	 * @see java.lang.UnsupportedOperationException()
	 */
	@Deprecated
	public void setId(Integer code) {
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * Pobieranie obiektu {@link BooleanEnum} na podstawie identyfikatora.
	 * Identyfikatory pobierane są z {@link BooleanLangEnum#INTEGER}.
	 * 
	 * @param id
	 *           identyfikator
	 * @return odpowieni obiekt symulowanej encji
	 */
	public static BooleanEnum loadById(Integer id) {
		String value = Integer.toString(id);
		BooleanLangEnum booleanLang = BooleanLangEnum.INTEGER;
		return loadByValue(value, booleanLang);
	}

	/**
	 * Pobieranie obiektu {@link BooleanEnum} na podstawie wartości i kodu
	 * językowego.
	 * 
	 * @param value
	 *           wartość
	 * @param lang
	 *           wersja językowa. Lista dostępnych wartosci otrzyma się po
	 *           wywołaniu metody {@link #getLangs()}
	 * @return odpowieni obiekt symulowanej encji
	 */
	public static BooleanEnum loadByValue(String value, String lang) {
		BooleanLangEnum booleanLang = BooleanLangEnum.getByLangCode(lang);
		return loadByValue(value, booleanLang);
	}

	/**
	 * Pobieranie obiektu {@link BooleanEnum} na podstawie wartości i obiektu
	 * reprezentacji wartości logicznych
	 * 
	 * @param value
	 *           wartość
	 * @param booleanLang
	 *           obiekt reprezentacji wartości logicznyc. Lista dostępnych
	 *           wartosci otrzyma się po wywołaniu metody
	 *           {@link BooleanLangEnum#values()}
	 * @return odpowieni obiekt symulowanej encji
	 */
	public static BooleanEnum loadByValue(String value, BooleanLangEnum booleanLang) {
		if (value.equals(booleanLang.getYes())) {
			return BooleanEnum.YES;
		}
		if (value.equals(booleanLang.getNo())) {
			return BooleanEnum.NO;
		}
		return BooleanEnum.UNKNOWN;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *           do ustwienia wersji językowej. Lista dostępnych wartosci otrzyma
	 *           się po wywołaniu metody {@link #getLangs()}
	 */
	public BooleanEnum setLang(String lang) {
		this.lang = lang;
		return this;
	}

	/**
	 * Pobieranie listy dostępnych kodów językowych reprezentacji logicznych
	 * 
	 * @return lista kodów
	 */
	public static String[] getLangs() {
		return BooleanLangEnum.getLangCodes();
	}

	/**
	 * Parsowanie do wartości logicznej
	 * 
	 * @param value
	 *           wartość podstawowa
	 * @param lang
	 *           wersja językowa. Lista dostępnych wartosci otrzyma się po
	 *           wywołaniu metody {@link #getLangs()}
	 * @return jeżeli wartość odpowiada {@link #YES} to zwrócona będzie wartość
	 *         {@code true}, w przeciwnym wypadku bedzie zwracana wartość {@code
	 *         false}
	 */
	public static boolean parse(String value, String lang) {
		BooleanEnum parsedValue = loadByValue(value, lang);
		switch (parsedValue) {
		case YES:
			return true;
		case NO:
		case UNKNOWN:
		default:
			return false;
		}
	}

	/**
	 * Parsowanie do wartości logicznej
	 * 
	 * @param value
	 *           wartość podstawowa
	 * @return jeżeli wartość odpowiada {@link #YES} to zwrócona będzie wartość
	 *         {@code true}, w przeciwnym wypadku bedzie zwracana wartość {@code
	 *         false}. Informacja o kodzie językowym jest dostępna za pomoca
	 *         metody {@link #getLang()}.
	 */
	public boolean parse(String value) {
		return BooleanLangEnum.getByLangCode(lang).parse(value);
	}

	/**
	 * Pobieranie wartości logicznej
	 * 
	 * @return jeżeli wartość odpowiada {@link #YES} to zwrócona będzie wartość
	 *         {@code true}, w przeciwnym wypadku bedzie zwracana wartość {@code
	 *         false}
	 */
	public boolean getBoolean() {
		switch (this) {
		case YES:
			return true;
		case NO:
		case UNKNOWN:
		default:
			return false;
		}
	}


}
