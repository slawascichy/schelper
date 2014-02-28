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

/**
 * 
 * BooleanLangEnum - reprezentacje wartosci logicznych w zalezności od kodu
 * jeżyka, albo użycia.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public enum BooleanLangEnum {

	/**
	 * Reprezentacja wartości logicznych po angielsku
	 */
	ENGLISH("no", "yes", "?", "no available information", "en"),
		/**
	 * Reprezentacja wartości logicznych po polsku
	 */
	POLISH("nie", "tak", "?", "brak informacji", "pl"),
		/**
	 * Reprezentacja wartości logicznych
	 */
	LOGIC("false", "true", "null", "n/a", "LOGIC"),
		/**
	 * Reprezentacja wartości logicznych jako liczby
	 */
	INTEGER("0", "1", "-1", "n/a", "01");

	private final String no;

	private final String unknown;

	private final String descriptionOfUnknown;

	private final String yes;

	private final String langCode;

	private BooleanLangEnum(
			String no,
			String yes,
			String unknown,
			String descriptionOfUnknown,
			String langCode) {
		this.no = no;
		this.yes = yes;
		this.unknown = unknown;
		this.descriptionOfUnknown = descriptionOfUnknown;
		this.langCode = langCode;
	}

	/**
	 * Pobieranie reprezentacji wartosci logicznych na podstawie ich kodu
	 * językowego. Lista dostępnych kodów dostępna jest za pomocą metody
	 * {@link #getLangCodes()}
	 * 
	 * @param code
	 *           kod reprezentacji wartości logicznych
	 * @return obiekt z reprezentacjami wartości logicznych
	 */
	public static BooleanLangEnum getByLangCode(String code) {
		for (BooleanLangEnum bool : BooleanLangEnum.values()) {
			if (bool.getLangCode().equals(code)) {
				return bool;
			}
		}
		return null;
	}

	/**
	 * Pobieranie listy dostępnych kodów językowych reprezentacji logicznych
	 * 
	 * @return lista kodów
	 */
	public static String[] getLangCodes() {
		BooleanLangEnum[] bools = BooleanLangEnum.values();
		String[] codes = new String[bools.length];
		for (int i = 0; i < bools.length; i++) {
			codes[i] = bools[i].getLangCode();
		}
		return codes;
	}

	/**
	 * Parsowanie do wartości logicznej
	 * 
	 * @param value
	 *           wartość podstawowa
	 * @return jeżeli wartość odpowiada {@link #yes} to zwrócona będzie wartość
	 *         {@code true}, w przeciwnym wypadku bedzie zwracana wartość {@code
	 *         false}
	 */
	public boolean parse(String value) {
		if (value.equals(yes)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @return the unknown
	 */
	public String getUnknown() {
		return unknown;
	}

	/**
	 * @return the descriptionOfUnknown
	 */
	public String getDescriptionOfUnknown() {
		return descriptionOfUnknown;
	}

	/**
	 * @return the yes
	 */
	public String getYes() {
		return yes;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

}
