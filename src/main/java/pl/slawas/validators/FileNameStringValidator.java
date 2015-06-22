package pl.slawas.validators;

/**
 * Klasa sprawdzająca znaki w łańcuchu znakowym pełniącym rolę nazwy pliku.
 * Używa StringBuilder do usuwania niepotrzebnych znaków w wejściowym łańcuchy
 * znakowym.
 */
public class FileNameStringValidator {

	/**
	 * Zamiennik znaków nieprawidłowych.
	 */
	public static final char charReplacement = 0x5F;

	/**
	 * Zwraca true jeśli znak c jest nieprawidłowy w nazwie pliku.
	 * 
	 * @see http://www.ascii-code.com/
	 * @see https://msdn.microsoft.com/en-us/library/aa365247
	 * 
	 * @param c
	 *            kod znaku
	 * @return
	 */
	public static boolean isInvalidChar(int c) {
		return (
		/* control characters */
		(c < 0x20)
		/* extended ASCII codes */
		|| ((c > 0x7E) && (c < 0xA0))
		/* Double quotes (or speech marks) */
		|| (c == 0x22)
		/* Asterisk */
		|| (c == 0x2A)
		/* Slash or divide */
		|| (c == 0x2F)
		/* Colon */
		|| (c == 0x3A)
		/* Greater than (or close angled bracket) */
		|| (c == 0x3E)
		/* Question mark */
		|| (c == 0x3F)
		/* Backslash */
		|| (c == 0x5C)
		/* Vertical bar (pipe) */
		|| (c == 0x7C));

	}

	/**
	 * Zamiennik znaków nieprawidłowych.
	 */
	public static char charReplacement() {
		return charReplacement; /* znaczek _ */
	}

	/**
	 * Zwraca w tablicy na pierwszym polu pozycje nieprawidłowego znaku w
	 * łańcuchu znakowym wejściowym lub -1 oraz na drugim polu kod tego znaku
	 * lub -1.
	 * 
	 * @param input
	 *            wejściowa wartość
	 * @return
	 */
	public static int[] invalidCharOff(String input) {
		int[] ret = { -1, -1 };
		if (input == null) {
			return ret;
		}
		int i = 0, k = input.length(), c;
		while (i < k) {
			c = input.codePointAt(i);
			if (FileNameStringValidator.isInvalidChar(c)) {
				ret[0] = i;
				ret[1] = c;
				return ret;
			}
			i++;
		}
		return ret;
	}

	/**
	 * Zwraca nowy string bez nieprawidłowych znaków w wejściowym łańcuchu
	 * znaków lub wskazanie na string wejściowy.
	 * 
	 * @param input
	 *            wejściowy łańcuch znakowy
	 * @return wynik analizy
	 */
	public static String invalidCharsRemoved(String input) {
		int[] s = FileNameStringValidator.invalidCharOff(input);
		if (s[0] == -1) {
			return input;
		}
		int i = s[0], k = input.length(), c;

		StringBuilder StringBuilder = new StringBuilder(input);
		StringBuilder.deleteCharAt(i);
		k--;
		while (i < k) {
			c = StringBuilder.codePointAt(i);
			if (!FileNameStringValidator.isInvalidChar(c)) {
				i++;
			} else {
				StringBuilder.deleteCharAt(i);
				k--;
			}
		}
		return new String(StringBuilder);
	}

	/**
	 * Zwraca nowy string z podmienionymi błędnymi znakami na znak domyślny (
	 * {@link #charReplacement()} w wejściowym łańcuchu znakowym lub wskazanie
	 * na string wejściowy.
	 * 
	 * @param input
	 *            wejściowy łańcuch znakowy
	 * @return poprawiony łańcuch znakowy
	 */
	public static String invalidCharsReplaced(String input) {
		int[] s = FileNameStringValidator.invalidCharOff(input);
		if (s[0] == -1) {
			return input;
		}
		int i = s[0], k = input.length();
		char[] chars = input.toCharArray();
		char r = FileNameStringValidator.charReplacement();
		chars[i++] = r;
		while (i < k) {
			if (FileNameStringValidator.isInvalidChar(chars[i])) {
				chars[i] = r;
			}
			i++;
		}
		return new String(chars);
	}
}
