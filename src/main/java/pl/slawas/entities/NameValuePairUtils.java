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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;
import pl.slawas.xml.XMLNameValuePairUtils;

/**
 * 
 * NameValuePairUtils - klasa narzędziowa obsługująca typy spełniające interface
 * {@link NameValuePair}.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.3.2.3 $
 * 
 */
public class NameValuePairUtils {

	private static Logger logger = LoggerFactory
			.getLogger(NameValuePairUtils.class);

	/**
	 * SortBy metoda sortowania wykorzystywana podczas wyszukiwania
	 * 
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.3.2.3 $
	 * 
	 */
	public enum SortBy {
		/** Sortowanie obiektów {@link NameValuePair} po nazwie */
		NAME,
		/** Sortowanie obiektów {@link NameValuePair} po wartości */
		VALUE;
	}

	static final Comparator<NameValuePair> NAME_ORDER = new Comparator<NameValuePair>() {
		public int compare(NameValuePair e1, NameValuePair e2) {
			return Strings.compare4Sort(e1.getName(), e2.getName());
		}
	};

	static final Comparator<NameValuePair> VALUE_ORDER = new Comparator<NameValuePair>() {
		public int compare(NameValuePair e1, NameValuePair e2) {
			return Strings.compare4Sort(e1.getValue(), e2.getValue());
		}
	};

	/**
	 * Procedura sortowania kolekcji typu List<?> po polu Name.
	 * 
	 * @param a
	 *            kolekcja
	 * @throws Exception
	 * @throws Exception
	 */
	public static void sortByName(List<? extends NameValuePair> a)
			throws Exception {
		Collections.sort(a, NAME_ORDER);
	}

	/**
	 * Procedura sortowania kolekcji typu List<NameValuePair> po polu Name.
	 * 
	 * @param source
	 *            kolekcja
	 * @param lo0
	 *            pozycja, od ktorej ma byc rozpoczete sortowanie, zazwyczaj 0
	 * @param hi0
	 *            pozycja, na ktorej ma byc zakonczone sortowanie, zazwyczaj
	 *            a.length -1
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static void sortByName(List<? extends NameValuePair> source,
			int lo0, int hi0) throws Exception {

		List<NameValuePair> a = (List<NameValuePair>) source;
		int lo = lo0;
		int hi = hi0;

		if (lo >= hi) {
			return;
		} else if (lo == hi - 1) {
			/* * sort a two element list by swapping if necessary */
			if (a.get(lo).getName().compareTo(a.get(hi).getName()) > 0) {
				NameValuePair T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
			return;
		}
		/* * Pick a pivot and move it out of the way */
		NameValuePair pivot = a.get((lo + hi) / 2);
		a.set(((lo + hi) / 2), a.get(hi));
		a.set(hi, pivot);
		while (lo < hi) {
			/*
			 * * Search forward from a[lo] until an element is found that * is
			 * greater than the pivot or lo >= hi
			 */
			while ((a.get(lo).getName().compareTo(pivot.getName()) <= 0)
					&& (lo < hi)) {
				lo++;
			}
			/*
			 * * Search backward from a[hi] until element is found that * is
			 * less than the pivot, or lo >= hi
			 */
			while ((a.get(hi).getName().compareTo(pivot.getName()) >= 0)
					&& (lo < hi)) {
				hi--;
			}
			/* * Swap elements a[lo] and a[hi] */
			if (lo < hi) {
				NameValuePair T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
		}
		/* * Put the median in the "center" of the list */
		a.set(hi0, a.get(hi));
		a.set(hi, pivot);
		/*
		 * * Recursive calls, elements a[lo0] to a[lo-1] are less than or *
		 * equal to pivot, elements a[hi+1] to a[hi0] are greater than * pivot.
		 */
		sortByName(a, lo0, lo - 1);
		sortByName(a, hi + 1, hi0);
	}

	/**
	 * Procedura sortowania kolekcji typu List<NameValuePair> po polu Value.
	 * 
	 * @param a
	 *            kolekcja
	 * @throws Exception
	 * @throws Exception
	 */
	public static void sortByValue(List<? extends NameValuePair> a)
			throws Exception {
		Collections.sort(a, VALUE_ORDER);
	}

	/**
	 * Procedura sortowania kolekcji typu List<NameValuePair> po polu Name.
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
	@SuppressWarnings("unchecked")
	public static void sortByValue(List<? extends NameValuePair> source,
			int lo0, int hi0) throws Exception {

		List<NameValuePair> a = (List<NameValuePair>) source;
		int lo = lo0;
		int hi = hi0;

		if (lo >= hi) {
			return;
		} else if (lo == hi - 1) {
			/* * sort a two element list by swapping if necessary */
			if (a.get(lo).getValue().compareTo(a.get(hi).getValue()) > 0) {
				NameValuePair T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
			return;
		}
		/* * Pick a pivot and move it out of the way */
		NameValuePair pivot = a.get((lo + hi) / 2);
		a.set(((lo + hi) / 2), a.get(hi));
		a.set(hi, pivot);
		while (lo < hi) {
			/*
			 * * Search forward from a[lo] until an element is found that * is
			 * greater than the pivot or lo >= hi
			 */
			while ((a.get(lo).getValue().compareTo(pivot.getValue()) <= 0)
					&& (lo < hi)) {
				lo++;
			}
			/*
			 * * Search backward from a[hi] until element is found that * is
			 * less than the pivot, or lo >= hi
			 */
			while ((a.get(hi).getValue().compareTo(pivot.getValue()) >= 0)
					&& (lo < hi)) {
				hi--;
			}
			/* * Swap elements a[lo] and a[hi] */
			if (lo < hi) {
				NameValuePair T = a.get(lo);
				a.set(lo, a.get(hi));
				a.set(hi, T);
			}
		}
		/* * Put the median in the "center" of the list */
		a.set(hi0, a.get(hi));
		a.set(hi, pivot);
		/*
		 * * Recursive calls, elements a[lo0] to a[lo-1] are less than or *
		 * equal to pivot, elements a[hi+1] to a[hi0] are greater than * pivot.
		 */
		sortByValue(a, lo0, lo - 1);
		sortByValue(a, hi + 1, hi0);
	}

	/**
	 * Metoda przeszukiwania listy obiektów spełniających interface
	 * {@link NameValuePair} po nazwie. Jest ustawiane domyślne sortowanie
	 * wyniku po nazwie.
	 * 
	 * @see #searchByName(List, String, SortBy)
	 * 
	 * @param source
	 *            lista obiektów spełniających interface {@link NameValuePair}
	 * @param searchCredentials
	 *            słowo kluczowe jakie ma być wyszukane
	 * @return przefiltrowana lista obiektów spełniających interface
	 *         {@link NameValuePair}
	 */
	public static List<? extends NameValuePair> searchByName(
			List<? extends NameValuePair> source, String searchCredentials) {
		return searchByName(source, searchCredentials, SortBy.NAME);
	}

	/**
	 * Metoda przeszukiwania listy obiektów spełniających interface
	 * {@link NameValuePair} po nazwie.
	 * 
	 * @param source
	 *            lista obiektów spełniających interface {@link NameValuePair}
	 * @param searchCredentials
	 *            słowo kluczowe jakie ma być wyszukane
	 * @param sortBy
	 *            kryterium sortowania wyniku.
	 * @return przefiltrowana lista obiektów spełniających interface
	 *         {@link NameValuePair}
	 */
	public static List<? extends NameValuePair> searchByName(
			List<? extends NameValuePair> source, String searchCredentials,
			SortBy sortBy) {

		if (StringUtils.isBlank(searchCredentials)) {
			logger.warn("Kryterium wyszukiwania nie moze byc puste");
			return source;
		}
		if (source == null || source.isEmpty()) {
			logger.warn("Przeszukiwana lista nie moze byc pusta");
			return source;
		}
		try {
			Vector<NameValuePair> target = new Vector<NameValuePair>();
			logger.trace("NameValuePair : szukam slowa '{}'", searchCredentials);
			for (NameValuePair item : source) {
				if (Strings.containsStrings(item.getName(), searchCredentials)) {
					target.add(item);
				}
				logger.trace(
						"[I] Nazwa: '{}' : {}",
						new Object[] {
								item.getName(),
								item.getName().toLowerCase()
										.indexOf(searchCredentials) >= 0 });
			}
			if (sortBy != null) {
				switch (sortBy) {
				case VALUE:
					NameValuePairUtils.sortByValue(target);
					break;
				default:
					NameValuePairUtils.sortByName(target);
					break;
				}
			}
			return target;
		} catch (Exception e) {
			logger.error("\n " + e.getMessage(), e);
			return source;
		}
	}

	/**
	 * Tworzenie nowej implemplementacji i instancje {@link NameValuePair}
	 * 
	 * @param name
	 *            wartość odpowiadająca nazwie
	 * @param value
	 *            wartość odpowiadająca wartości
	 * @return nowa instancja obiektu {@link NameValuePair}
	 */
	public static NameValuePair createNewInstance(String name, String value) {

		NameValuePair nameValuePair = new NameValuePair() {

			private static final long serialVersionUID = 1L;

			private String name;

			private String value;

			public String toXMLItem() {
				return XMLNameValuePairUtils.toXMLItem(this).toString();
			}

			/**
			 * @return the name
			 */
			public String getName() {
				return name;
			}

			/**
			 * @param name
			 *            the name to set
			 */
			public void setName(String name) {
				this.name = name;
			}

			/**
			 * @return the value
			 */
			public String getValue() {
				return value;
			}

			/**
			 * @param value
			 *            the value to set
			 */
			public void setValue(String value) {
				this.value = value;
			}

			@Override
			public String toString() {
				return "NameValuePair ['" + getName() + "','" + getValue()
						+ "']";
			}

		};

		nameValuePair.setName(name);
		nameValuePair.setValue(value);
		return nameValuePair;
	}

	/**
	 * Pobieranie nowej instancji obiektu {@link NameValuePair}
	 * 
	 * @param name
	 *            nazwa
	 * @param value
	 *            wartość
	 * @return nowa instancja obiektu implementująca {@link NameValuePair} z
	 *         ustawionymi wartościami.
	 */
	public static NameValuePair getNewInstanceNVP(String name, String value) {
		NameValuePair nvp = getNewInstanceNVP();
		nvp.setName(name);
		nvp.setValue(value);
		return nvp;
	}

	/**
	 * Pobieranie nowej instancji obiektu {@link NameValuePair}
	 * 
	 * @return nowa instancja obiektu implementująca {@link NameValuePair}
	 */
	public static NameValuePair getNewInstanceNVP() {
		return new NameValuePair() {

			private static final long serialVersionUID = 2338163915296338261L;
			private String name;
			private String value;

			@Override
			public String toXMLItem() {
				return XMLNameValuePairUtils.toXMLItem(this).toString();
			}

			@Override
			public void setValue(String value) {
				this.value = value;
			}

			@Override
			public void setName(String name) {
				this.name = name;
			}

			@Override
			public String getValue() {
				return this.value;
			}

			@Override
			public String getName() {
				return this.name;
			}
		};
	}

}
