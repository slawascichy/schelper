package pl.slawas.validators;

/**
 * Klasa walidująca znaki w stringu z nazwą pliku. Używa StringBuilder do
 * usuwania niepotrzebnych znaków w stringu wejściowym.
 */
public class FileNameStringValidator {
	/**
	 * Zwraca true jeśli znak c jest nieprawidłowy w nazwie pliku.
	 */
	public static boolean isInValidChar(int c) {
		return (
		/* invalid filename characters */
		(c < 0x20) || ((c > 0x7E) && (c < 0xA0)) || (c == 0x5C) || (c == 0x2F)
				|| (c == 0x3A) || (c == 0x2A) || (c == 0x3F) || (c == 0x22)
				|| (c == 0x3E) || (c == 0x7C));

	}

	/**
	 * Zamiennik znaków nieprawidłowych.
	 */
	public static char charReplacement() {
		return 0x5F; /* znaczek _ */
	}

	/**
	 * Zwraca w tablicy na pierwszym polu pozycje nieprawidłowego znaku w
	 * stringu wejściowym lub -1 oraz na drugim polu kod tego znaku lub -1.
	 */
	public static int[] invalidCharOff(String input) {
		int[] ret = { -1, -1 };
		if (input == null)
			return ret;
		int i = 0, k = input.length(), c;
		while (i < k) {
			c = input.codePointAt(i);
			if (FileNameStringValidator.isInValidChar(c)) {
				ret[0] = i;
				ret[1] = c;
				return ret;
			}
			i++;
		}
		return ret;
	}

	/**
	 * Zwraca nowy string bez nieprawidłowych znaków w stringu wejściowym lub
	 * wskazanie na string wejściowy.
	 */
	public static String invalidCharsRemoved(String input) {
		int[] s = FileNameStringValidator.invalidCharOff(input);
		if (s[0] == -1)
			return input;
		int i = s[0], k = input.length(), c;

		StringBuilder StringBuilder = new StringBuilder(input);
		StringBuilder.deleteCharAt(i);
		k--;
		while (i < k) {
			c = StringBuilder.codePointAt(i);
			if (!FileNameStringValidator.isInValidChar(c))
				i++;
			else {
				StringBuilder.deleteCharAt(i);
				k--;
			}
		}
		return new String(StringBuilder);
	}

	/**
	 * Zwraca nowy string z podmienionymi błędnymi znakami na znak domyślny w
	 * stringu wejsciowym lub wskazanie na string wejściowy.
	 */
	public static String invalidCharsReplaced(String input) {
		int[] s = FileNameStringValidator.invalidCharOff(input);
		if (s[0] == -1)
			return input;
		int i = s[0], k = input.length();
		char[] chars = input.toCharArray();
		char r = FileNameStringValidator.charReplacement();
		chars[i++] = r;
		while (i < k) {
			if (FileNameStringValidator.isInValidChar(chars[i]))
				chars[i] = r;
			i++;
		}
		return new String(chars);
	}
}
