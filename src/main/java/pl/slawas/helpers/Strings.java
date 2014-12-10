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
package pl.slawas.helpers;

import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * @author slawas
 * 
 */
public class Strings implements Serializable {

	final protected static Logger log = LoggerFactory.getLogger(Strings.class);

	private static final long serialVersionUID = -8981008883411945666L;

	/** Otwarcie nawiasu (ze spacją na początku) */
	public static final String OPEN_BRACKED = " (";
	/** Otwarcie nawiasu kwadratowego */
	public static final String OPEN_SQUARE_BRACKED = "[";
	/** Zamknięcie nawiasu */
	public static final String CLOSE_BRACKED = ")";
	/** Zamknięcie nawiasu kwadratowego (ze spacją na końcu) */
	public static final String CLOSE_SQUARE_BRACKED = "] ";
	/** Wartość reprezentująca brak dostępu do wartości */
	public static final String NOT_ALLOWED_VALUE = "n/a";
	/** Wartość reprezentująca spację */
	public static final String SPACE = " ";
	/** Reprezentacja związku pomiędzy dwoma obiektami */
	public static final String RELATIONSHIP = " -> ";
	/** Reprezentacja znaku podkreślenia */
	public static final String UNDERSCORE = "_";
	/** Reprezentacja kropki */
	public static final String DOT = ".";
	public static final char DOTChar = '.';
	/** Reprezentacja dwukropka (sam) */
	public static final String DOUBLE_DOT_ALONE = ":";
	/** Reprezentacja dwukropka (ze spacją na końcu) */
	public static final String DOUBLE_DOT = ": ";
	/** Reprezentacja przecinka (ze spacją na końcu) */
	public static final String COMMA = ", ";
	/** Reprezentacja znaku równości */
	public static final String EQUALS = "=";
	/** cudzysłów pojedynczy */
	public static final String SINGLE_QUOTATION_MARK = "'";
	/** cudzysłów podwójny */
	public static final String DOUBLE_QUOTATION_MARK = "\"";
	/** cyfra zero */
	public static final String ZERO = "0";
	/** liczba zero */
	public static final Long ZEROL = 0L;

	/**
	 * Metoda zamieniajaca wszystkie wystapienia podanego lancucha znakowego na
	 * inny w danym lancuchu.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 	
	 * 	Strings.replaceAll(&quot;Przedmiot nie jest nie niebieski&quot;, &quot;nie&quot;, &quot;&quot;)
	 *    Zwroci &quot;Przedmiot  jest  bieski&quot;
	 *    
	 * 	Strings.replaceAll(&quot;Przedmiot nie jest nie niebieski&quot;, &quot; nie&quot;, &quot; &quot;)
	 *    Zwroci &quot;Przedmiot jest niebieski&quot;
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            tekst do przetworzenia
	 * @param toReplace
	 *            tekst, ktory ma byc odnaleziony i zamieniony
	 * @param replacement
	 *            tekst, na ktory znaleziony fragment ma zostac zamieniony
	 * @return przetworzony lancuch znakowy
	 */
	public static String replaceAll(String source, String toReplace,
			String replacement) {
		int idx = source.lastIndexOf(toReplace);
		if (idx != -1) {
			StringBuffer ret = new StringBuffer(source);
			ret.replace(idx, idx + toReplace.length(), replacement);
			while ((idx = source.lastIndexOf(toReplace, idx - 1)) != -1) {
				ret.replace(idx, idx + toReplace.length(), replacement);
			}
			source = ret.toString();
		}

		return source;
	}

	/**
	 * Metoda uzupelniajaca string z lewej strony znakami do zadanej dlugosci.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 
	 *    Strings.lpad(&quot;1&quot;, &quot;0&quot;, 5)
	 *    Zwróci: &quot;00001&quot;
	 * 
	 * </pre>
	 * 
	 * @param word
	 *            slowo, ktore ma byc uzupelnione
	 * @param character
	 *            znak jakim ma byc uzupelnione slowo
	 * @param size
	 *            rozmiar do jakiego ma byc uzupelniony string
	 * @return uzupelnione slowo
	 */
	public static String lpad(String word, String character, int size) {
		int i = 1;
		StringBuilder t_znak = new StringBuilder(character);
		while (i <= size) {
			t_znak.append(character);
			i++;
		}
		word = t_znak.toString() + word;
		return (word).substring(word.length() - size);
	}

	/**
	 * Metoda uzupelniajaca string z lewej strony znakami do zadanej dlugosci.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 
	 *    Strings.rpad(&quot;1&quot;, &quot;0&quot;, 5)
	 *    Zwróci: &quot;10000&quot;
	 * 
	 *    Strings.rpad(&quot;Slawas&quot;, &quot; &quot;, 10)
	 *    Zwróci: &quot;Slawas    &quot;
	 * </pre>
	 * 
	 * @param word
	 *            slowo, ktore ma byc uzupelnione
	 * @param charcter
	 *            znak jakim ma byc uzupelnione slowo
	 * @param size
	 *            rozmiar do jakiego ma byc uzupelniony string
	 * @return uzupelnione slowo
	 */
	public static String rpad(String word, String character, int size) {
		int i = 1;
		StringBuilder t_znak = new StringBuilder(character);
		while (i < (size - word.length())) {
			t_znak.append(character);
			i++;
		}
		if ((size - word.length()) > 0)
			word = word + t_znak.toString();
		return word;
	}

	/**
	 * Remove leading whitespace, usuwanie spacji wystepujacych na poczatku
	 * string-a.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 
	 *    Strings.ltrim(&quot; Slawas&quot;)
	 *    Zwróci: &quot;Slawas&quot;
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            tekst do obrobki
	 * @return przerobiony tekst
	 */
	public static String ltrim(String source) {
		return source.replaceAll("^\\s+", "");
	}

	/**
	 * Remove trailing whitespace, usuwanie spacji wystepujacych na koncu
	 * string-a.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 
	 *    Strings.rtrim(&quot;Slawas &quot;)
	 *    Zwróci: &quot;Slawas&quot;
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            tekst do obrobki
	 * @return przerobiony tekst
	 */
	public static String rtrim(String source) {
		return source.replaceAll("\\s+$", "");
	}

	/**
	 * Remove trailing whitespace, usuwanie spacji wystepujacych na koncu i na
	 * poczatku string-a.
	 * 
	 * <pre>
	 * 	Przyklad:
	 * 
	 *    Strings.lrtrim(&quot;  Slawas &quot;)
	 *    Zwróci: &quot;Slawas&quot;
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            tekst do obrobki
	 * @return przerobiony tekst
	 */
	public static String lrtrim(String source) {
		return ltrim(rtrim(source));
	}

	/**
	 * Procedura zamieniajaca liste w postaci lancucha (java.lang.String) na typ
	 * java.util.List Koniecznym jest aby lancuch wejsciowy (lista_elementow)
	 * zaczynal sie i konczyl odpowiednim znakiem rozdzielajacym
	 * (znak_rozdzielajacy) np. ",10,20,30,40," - otrzymamy liste 4 String'ow o
	 * wartosciach 10,20,30,40
	 * 
	 * @param elementsList
	 * @param separator
	 * @return
	 */
	public static List<String> toList(String elementsList, char separator) {
		Vector<String> lista = new Vector<String>();
		String reszta = elementsList.substring(1);
		int separatorPosition = reszta.indexOf(separator);
		while (separatorPosition > 0) {
			if (reszta.substring(0, separatorPosition) != null) {
				String element = reszta.substring(0, separatorPosition);
				lista.add(element);
			}
			if (reszta.substring(separatorPosition + 1) != null) {
				reszta = reszta.substring(separatorPosition + 1);
			}
			separatorPosition = reszta.indexOf(separator);
		}
		return lista;
	}

	static final Comparator<String> STRING_ORDER = new Comparator<String>() {
		public int compare(String e1, String e2) {
			return Strings.compare4Sort(e1, e2);
		}
	};

	/**
	 * Procedura sortowania kolekcji typu Vector<String>.
	 * 
	 * @param a
	 *            kolekcja
	 * @throws Exception
	 */
	public static void sort(Vector<String> a) throws Exception {
		Collections.sort(a, STRING_ORDER);
	}

	/**
	 * Procedura sortowania kolekcji typu Vector<String>. Metoda nie sortuje
	 * prawidłowo polskich znaków UTF-8. Lepiej używać {@link #sort(Vector)}
	 * 
	 * @param a
	 *            kolekcja
	 * @param lo0
	 *            pozycja, od ktorej ma byc rozpoczete sortowanie, zazwyczaj 0
	 * @param hi0
	 *            pozycja, na ktorej ma byc zakonczone sortowanie, zazwyczaj
	 *            a.size() -1
	 * @throws Exception
	 */
	@Deprecated
	public static void sort(Vector<String> a, int lo0, int hi0)
			throws Exception {
		int lo = lo0;
		int hi = hi0;
		if (lo >= hi) {
			return;
		} else if (lo == hi - 1) {
			/* sort a two element list by swapping if necessary */
			if (a.get(lo).toLowerCase().compareTo(a.get(hi).toLowerCase()) > 0) {
				String T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
			return;
		}
		/* Pick a pivot and move it out of the way */
		String pivot = a.get((lo + hi) / 2);
		a.set(((lo + hi) / 2), a.get(hi));
		a.set(hi, pivot);
		while (lo < hi) {
			/*
			 * Search forward from a[lo] until an element is found that is
			 * greater than the pivot or lo >= hi
			 */
			while ((a.get(lo).toLowerCase().compareTo(pivot.toLowerCase()) <= 0)
					&& (lo < hi)) {
				lo++;
			}
			/*
			 * Search backward from a[hi] until element is found that is less
			 * than the pivot, or lo >= hi
			 */
			while ((a.get(hi).toLowerCase().compareTo(pivot.toLowerCase()) >= 0)
					&& (lo < hi)) {
				hi--;
			}
			/* Swap elements a[lo] and a[hi] */
			if (lo < hi) {
				String T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
		}
		/* Put the median in the "center" of the list */
		a.set(hi0, a.get(hi));
		a.set(hi, pivot);
		/*
		 * Recursive calls, elements a[lo0] to a[lo-1] are less than or equal to
		 * pivot, elements a[hi+1] to a[hi0] are greater than * pivot.
		 */
		sort(a, lo0, lo - 1);
		sort(a, hi + 1, hi0);
	}

	/**
	 * Procedura sortowania kolekcji typu String[].
	 * 
	 * @param a
	 *            kolekcja
	 * @throws Exception
	 */
	public static void sort(String a[]) throws Exception {
		Arrays.sort(a, STRING_ORDER);
	}

	/**
	 * Procedura sortowania kolekcji typu String[]. Metoda nie sortuje
	 * prawidłowo polskich znaków UTF-8. Lepiej używać {@link #sort(String[])}
	 * 
	 * @param a
	 *            kolekcja
	 * @param lo0
	 *            pozycja, od ktorej ma byc rozpoczete sortowanie, zazwyczaj 0
	 * @param hi0
	 *            pozycja, na ktorej ma byc zakonczone sortowanie, zazwyczaj
	 *            a.length -1
	 * @throws Exception
	 */
	@Deprecated
	public static void sort(String a[], int lo0, int hi0) throws Exception {
		int lo = lo0;

		int hi = hi0;
		if (lo >= hi) {
			return;
		} else if (lo == hi - 1) {
			/* sort a two element list by swapping if necessary */
			if (a[lo].toLowerCase().compareTo(a[hi].toLowerCase()) > 0) {
				String T = a[lo];
				a[lo] = a[hi];
				a[hi] = T;
			}
			return;
		}
		/* Pick a pivot and move it out of the way */
		String pivot = a[(lo + hi) / 2];
		a[(lo + hi) / 2] = a[hi];
		a[hi] = pivot;
		while (lo < hi) {
			/*
			 * Search forward from a[lo] until an element is found that is
			 * greater than the pivot or lo >= hi
			 */
			while ((a[lo].toLowerCase().compareTo(pivot.toLowerCase()) <= 0)
					&& (lo < hi)) {
				lo++;
			}
			/*
			 * Search backward from a[hi] until element is found that is less
			 * than the pivot, or lo >= hi
			 */
			while ((a[hi].toLowerCase().compareTo(pivot.toLowerCase()) >= 0)
					&& (lo < hi)) {
				hi--;
			}
			/* Swap elements a[lo] and a[hi] */
			if (lo < hi) {
				String T = a[lo];
				a[lo] = a[hi];
				a[hi] = T;
			}
		}
		/* Put the median in the "center" of the list */
		a[hi0] = a[hi];
		a[hi] = pivot;
		/*
		 * Recursive calls, elements a[lo0] to a[lo-1] are less than or equal to
		 * pivot, elements a[hi+1] to a[hi0] are greater than pivot.
		 */
		sort(a, lo0, lo - 1);
		sort(a, hi + 1, hi0);
	}

	/**
	 * Procedura lamaca tekst, dostosujaca go romiaru linii.
	 * 
	 * @param text
	 * @param lineSize
	 * @return
	 */
	public static String breakText(String text, int lineSize) {
		text = replaceAll(text, "\r", "");
		StringBuffer odp = new StringBuffer(text.length());
		boolean wstaw_enter = false;
		int j = 0;
		int letterInWordCounter = 0;
		for (int i = 0; i < text.length(); i++) {
			int znak = text.charAt(i);
			if (znak == '\n') {
				j = 0;
			}
			if ((j != 0) && (j % lineSize) == 0) {
				wstaw_enter = true;
			}
			if (znak == 32 || znak == '\n' || znak == '\t') {
				letterInWordCounter = 0;
			}

			if (wstaw_enter) {
				if (letterInWordCounter != 0) {
					String x = odp.substring(0, odp.length()
							- letterInWordCounter + 1);
					String rest = odp.substring(odp.length()
							- letterInWordCounter + 1);
					odp = new StringBuffer();
					odp.append(x).append("\n").append(rest);
					odp.append(text.charAt(i));
					j = letterInWordCounter;
				} else {
					odp.append("\n");
				}
				wstaw_enter = false;
			} else {
				odp.append(text.charAt(i));
			}
			j++;
			letterInWordCounter++;
		}
		return odp.toString();
	}

/**
	 * Metoda "kodujaca" elementy HTML, tak aby mozna je bylo zaprezentowac jako
	 * kod np. znak '<' zamienia na "&lt;"
	 * 
	 * @param aTagFragment
	 * @return skonwertowany tekst
	 */
	public static String forHTMLTag(String aTagFragment) {
		final StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(
				aTagFragment);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '\\') {
				result.append("&#092;");
			} else if (character == '&') {
				result.append("&amp;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

/**
	 * Metoda "kodujaca" znaki otwarcia i zamkniecia tagow HTML, tak aby mozna je
	 * bylo zaprezentowac jako kod np. znak '<' zamienia na "&lt;"
	 * 
	 * @param aText
	 * @return skonwertowany tekst
	 */
	public static String toDisableTags(String aText) {
		final StringBuffer result = new StringBuffer();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * 
	 * @param aRegexFragment
	 * @return
	 */
	public static String forRegex(String aRegexFragment) {
		final StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(
				aRegexFragment);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			/*
			 * All literals need to have backslashes doubled.
			 */
			if (character == '.') {
				result.append("\\.");
			} else if (character == '"') {
				result.append("\\\"");
			} else if (character == '\\') {
				result.append("\\\\");
			} else if (character == '?') {
				result.append("\\?");
			} else if (character == '*') {
				result.append("\\*");
			} else if (character == '+') {
				result.append("\\+");
			} else if (character == '&') {
				result.append("\\&");
			} else if (character == ':') {
				result.append("\\:");
			} else if (character == '{') {
				result.append("\\{");
			} else if (character == '}') {
				result.append("\\}");
			} else if (character == '[') {
				result.append("\\[");
			} else if (character == ']') {
				result.append("\\]");
			} else if (character == '(') {
				result.append("\\(");
			} else if (character == ')') {
				result.append("\\)");
			} else if (character == '^') {
				result.append("\\^");
			} else if (character == '$') {
				result.append("\\$");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Init Cap Words With Spaces
	 * 
	 * @param in
	 *            string
	 * @return init cap
	 */
	public static String initCap(String in) {
		if (in == null || in.length() == 0)
			return in;
		//
		boolean capitalize = true;
		char[] data = in.toCharArray();
		for (int i = 0; i < data.length; i++) {
			if (data[i] == ' ' || Character.isWhitespace(data[i]))
				capitalize = true;
			else if (capitalize) {
				data[i] = Character.toUpperCase(data[i]);
				capitalize = false;
			} else
				data[i] = Character.toLowerCase(data[i]);
		}
		return new String(data);
	} // initCap

	/**
	 * Konwersja tekstu ze znakami specjalnymi do postaci tekstu umozliwiajacego
	 * jego wprowadzenia do pliku o chatacter-set-cie iso-8859-p1: np. litera
	 * '\A' w UTF-8 do slowa "\u0104"
	 * 
	 * <pre>
	 * 		u0104, // A"); // Ą
	 * 		u0106, // C"); // Ć
	 * 		u0118, // E"); // Ę
	 * 		u0141, // L"); // Ł
	 * 		u0143, // N"); // Ń
	 * 		u00d3, // O"); // Ó
	 * 		u015a, // S"); // Ś
	 * 		u0179, // Z"); // Ź
	 * 		u017b, // Z"); // Ż
	 * 		u0105, // a"); // ą
	 * 		u0107, // c"); // ć
	 * 		u0119, // e"); // ę
	 * 		u0142, // l"); // ł
	 * 		u0144, // n"); // ń
	 * 		u00f3, // o"); // ó
	 * 		u015b, // s"); // ś
	 * 		u017a, // z"); // ź
	 * 		u017c; // z"); // ż
	 * </pre>
	 * 
	 * @param text
	 *            konwertowny tekst
	 * @param endTag
	 *            znak dostawiany na koncu, najczesciej null
	 * @return tekst skonwertowany
	 */
	public static String codeUTF8Char(String text, String endTag) {

		StringBuffer odp = new StringBuffer();
		if (endTag == null)
			endTag = "";

		int tLength = text.length();
		for (int i = 0; i < tLength; i++) {
			int znak = (int) text.charAt(i);
			if (znak > 255) {
				odp.append("\\u" + lpad(Integer.toHexString(znak), "0", 4)
						+ endTag);
			} else if (211 == znak) {
				odp.append("\\u00d3" + endTag);
			} else if (243 == znak) {
				odp.append("\\u00f3" + endTag);
			} else {
				odp.append(text.charAt(i));
			}
		}
		return odp.toString();
	}

	/**
	 * Metoda próbujaca ocenic czy dany string jest w UTF-8. Metoda ta realizuje
	 * to w bardzo prosty sposob sprawdzajac czy wystepuja znaki o wartości int
	 * większej od 255. Dlatego też podkreślam, że metoda <u>próbuje</u>
	 * zidentyfikować czy string jest w UTF-8. Metoda ma zastosowanie w
	 * przypadku obrony przed podwojnym kodowaniem w UTF stringow.
	 * 
	 * <pre>
	 *  Przyklad:
	 * 
	 * 		 String tcompareValue = (String) targetValue;
	 * 		 if (!Strings.isInUTF8(tcompareValue))
	 * 			 tcompareValue = new String((tcompareValue).getBytes(&quot;UTF-8&quot;));
	 * 
	 * </pre>
	 * 
	 * @param text
	 *            konwertowny tekst
	 * @param endTag
	 *            znak dostawiany na koncu, najczesciej null
	 * @return tekst skonwertowany
	 */
	public static boolean isInUTF8(String text) {

		int tLength = text.length();
		for (int i = 0; i < tLength; i++) {
			int znak = (int) text.charAt(i);
			if (znak > 255) {
				log.trace("Found char {} ({})", new Object[] { (char) znak,
						znak });
				return true;
			}
		}
		return false;
	}

	/**
	 * Konwersja tekstu ze polskimi znakami specjalnymi do kodowania w
	 * 'ascii-p1', tak aby mozna bylo swobodnie zaprezentowac dany tekst w shell
	 * systemu operacyjnego: np. slowo w UTF-8 "\u0104" do litery 'A' (po
	 * polskawemu)
	 * 
	 * @param text
	 * @return skonwertowany tekst
	 */
	public static String codeUTF82ASCIIpl(String text) {

		String odp = codeUTF8Char(text, null);

		odp = replaceAll(odp, "u0104", "A"); // Ą
		odp = replaceAll(odp, "u0106", "C"); // Ć
		odp = replaceAll(odp, "u0118", "E"); // Ę
		odp = replaceAll(odp, "u0141", "L"); // Ł
		odp = replaceAll(odp, "u0143", "N"); // Ń
		odp = replaceAll(odp, "u00d3", "O"); // Ó
		odp = replaceAll(odp, "u015a", "S"); // Ś
		odp = replaceAll(odp, "u0179", "Z"); // Ź
		odp = replaceAll(odp, "u017b", "Z"); // Ż

		odp = replaceAll(odp, "u00D3", "O"); // Ó
		odp = replaceAll(odp, "u015A", "S"); // Ś
		odp = replaceAll(odp, "u017B", "Z"); // Ż

		odp = replaceAll(odp, "u0105", "a"); // ą
		odp = replaceAll(odp, "u0107", "c"); // ć
		odp = replaceAll(odp, "u0119", "e"); // ę
		odp = replaceAll(odp, "u0142", "l"); // ł
		odp = replaceAll(odp, "u0144", "n"); // ń
		odp = replaceAll(odp, "u00f3", "o"); // ó
		odp = replaceAll(odp, "u015b", "s"); // ś
		odp = replaceAll(odp, "u017a", "z"); // ź
		odp = replaceAll(odp, "u017c", "z"); // ż

		odp = replaceAll(odp, "u00F3", "o"); // ó
		odp = replaceAll(odp, "u015B", "s"); // ś
		odp = replaceAll(odp, "u017A", "z"); // ź
		odp = replaceAll(odp, "u017C", "z"); // ż

		odp = replaceAll(odp, "\\", "");

		return odp;
	}

	public static String translate(String text, String sourceMask,
			String targetMask) {

		Hashtable<Integer, String> tranMap = new Hashtable<Integer, String>();
		int tLength = 0;

		tLength = sourceMask.length();
		for (int i = 0; i < tLength; i++) {
			int znak = (int) sourceMask.charAt(i);
			String toReplace = "";
			try {
				toReplace = "" + targetMask.charAt(i);
			} catch (Exception e) {
			}
			tranMap.put(znak, toReplace);
		}

		StringBuffer odp = new StringBuffer();
		tLength = text.length();
		for (int i = 0; i < tLength; i++) {
			int znak = (int) text.charAt(i);
			if (tranMap.get(Integer.valueOf(znak)) != null) {
				odp.append(tranMap.get(Integer.valueOf(znak)));
			} else {
				odp.append(text.charAt(i));
			}
		}
		return odp.toString();
	}

	/**
	 * Porownywanie dwoch stringów "source LIKE searchCredentials"
	 * 
	 * @param source
	 *            string źródłowy, przeszukiwany
	 * @param searchCredentials
	 *            wartosc porownywana, wyszukiwana
	 * @return prawda lub falsz [true|false]
	 */
	public static boolean containsStrings(String source,
			String searchCredentials) {
		return source.toLowerCase().contains(searchCredentials.toLowerCase());
	}

	static String encodeUTF8_pl(String text) {

		StringBuffer odp = new StringBuffer();
		int tLength = text.length();
		for (int i = 0; i < tLength; i++) {
			int znak = (int) text.charAt(i);
			switch (znak) {
			case 260: // Ą
				odp.append("A").append(Character.toChars(255)[0]);
				break;
			case 261: // ą
				odp.append("a").append(Character.toChars(255)[0]);
				break;
			case 262: // Ć
				odp.append("C").append(Character.toChars(255)[0]);
				break;
			case 263: // ć
				odp.append("c").append(Character.toChars(255)[0]);
				break;
			case 280: // Ę
				odp.append("E").append(Character.toChars(255)[0]);
				break;
			case 281: // ę
				odp.append("e").append(Character.toChars(255)[0]);
				break;
			case 321: // Ł
				odp.append("L").append(Character.toChars(255)[0]);
				break;
			case 322: // ł
				odp.append("l").append(Character.toChars(255)[0]);
				break;
			case 323: // Ń
				odp.append("N").append(Character.toChars(255)[0]);
				break;
			case 324: // ń
				odp.append("n").append(Character.toChars(255)[0]);
				break;
			case 211: // Ó
				odp.append("O").append(Character.toChars(255)[0]);
				break;
			case 243: // ó
				odp.append("o").append(Character.toChars(255)[0]);
				break;
			case 346: // Ś
				odp.append("S").append(Character.toChars(255)[0]);
				break;
			case 347: // ś
				odp.append("s").append(Character.toChars(255)[0]);
				break;
			case 377: // Ź
				odp.append("Z").append(Character.toChars(254)[0]);
				break;
			case 378: // ź
				odp.append("z").append(Character.toChars(254)[0]);
				break;
			case 379: // Ż
				odp.append("Z").append(Character.toChars(255)[0]);
				break;
			case 380: // ż
				odp.append("z").append(Character.toChars(255)[0]);
				break;
			default:
				odp.append(text.charAt(i));
				break;
			}
		}
		return odp.toString();
	}

	public static int compare4Sort(String e1, String e2) {
		return encodeUTF8_pl(e1).toLowerCase().compareTo(
				encodeUTF8_pl(e2).toLowerCase());

	}

}
