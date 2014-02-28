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
package pl.slawas.paging;

import java.io.Serializable;

import pl.slawas.entities._ICopyable;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


/**
 * 
 * PagingParams - klasa służy do definiowania ile, i których z kolei dokumentów
 * zwrócić ma zwrócić wplucene. Jeśli wplucen znajdzie więcej dokumentów to
 * zwróci tyle o ile została zapytana. Jeśli znajdzie mniej to zwróci tyle ile
 * znalazła.
 * 
 * Dla ułatwienia można posłużyć się pl.wp.lucene.api.search.Page i użyć
 * konstruktora z parametrem tej klasy. Ustawienia count i offset zmieniają się
 * w momencie ustawiania paramteru klasy Page. Przy ustawianiu parametrów
 * sprawdzane są ograniczenia. Jeśli podawana wartość prowadzi do przekroczenia
 * ograniczeń to wyszukiwanie nie zostanie przeprowadzone.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @author Patryk Makuch &lt;pmakuch@slawas.pl&gt;
 * @version $Revision: 1.5.2.1 $
 * 
 */
public class PagingParams implements Serializable, _ICopyable<PagingParams> {

	private static final long serialVersionUID = -5210221106589109534L;

	private static Logger logger = LoggerFactory.getLogger(PagingParams.class);

	private enum Param {

		/** parematr {@link #offset} */
		offset,
				/** parematr {@link #cursorOfPage} */
		cursorOfPage,
				/** parematr {@link #maxPageSize} */
		maxPageSize,
				/** parematr {@link #maxCount} */
		maxCount,
				/** parematr {@link #pageSize} */
		pageSize,
				/** parematr {@link #page} */
		page;

		private String value;

		private String getMsgLabel() {
			return "Ustawianie parametru " + this.toString()
					+ (value != null ? " na wartosc " + value : "");
		}

		private Param setValue(String value) {
			this.value = value;
			return this;
		}

		private Param checkAccessibilityOperation(
				int currCalue, int newValue, boolean objectIsReadOnly) {
			setValue(Integer.toString(newValue));
			if (objectIsReadOnly && currCalue != newValue)
				throw new PaginigParamsException("[" + getMsgLabel()
						+ "] Parametr jest tylko do odczytu");
			return this;
		}

	}

	protected boolean isReadOnly = false;

	/**
	 * Domyślna liczba pierwszych wyników/dokumentów jaka ma być pominięta w
	 * odpowiedzi wyszukiwarki.
	 */
	public static int DEFAULT_PAGING_OFFSET = 0;

	/**
	 * <b>Prametr stronicowania:</b> Liczba pierwszych wyników/dokumentów jaka ma
	 * być pominięta w odpowiedzi wyszukiwarki
	 */
	private int offset = DEFAULT_PAGING_OFFSET;

	/**
	 * Pozycja kursora dla początku strony, poprostu numer wiersza w wyniku
	 * zapytania, który ma być pierwszym elementwm na stronie. Np. Jeżeli chcemy
	 * odczytać kolenją stronę, to paramer zmieniany jest na wartość wskazującą
	 * na pierwszy element tej.
	 * <p>
	 * Parametr ma analogiczne znaczenie jak
	 * {@link _IResultInfo#getStartPosition()}
	 * </p>
	 * <p>
	 * Parametr jest domyślnie ustawiony na {@link #DEFAULT_PAGING_OFFSET}
	 * wskazujący na pierwszą stronę.
	 * </p>
	 * 
	 * @see #setPage(Page)
	 * 
	 */
	private int cursorOfPage = DEFAULT_PAGING_OFFSET;

	/**
	 * Domyslna maksymalna liczba dokumentów offsetu (maksymalna liczba na
	 * stronie)
	 */
	public static final int MAX_PAGE_SIZE = 500;

	/**
	 * <b>Prametr stronicowania:</b> Dynamicznie ustawiane ograniczenia na
	 * maksymalną liczbę dokumentów na stronie. Parametr wykorzystywany podczas
	 * ustawiania strony, do sprawdzenia poprawności jej parametrów.
	 * 
	 * @see Page
	 * 
	 */
	private int maxPageSize = MAX_PAGE_SIZE;

	/**
	 * Domyślna maksymalna liczba dokumentów w wyniku zapytania (w jednym
	 * requeście). Ta liczba wyznaczana jest na podstawie ilorazu dwóch
	 * domyślnych parametrów: {@link Page#DEFAULT_MAX_PAGES} oraz
	 * {@link #MAX_PAGE_SIZE}.
	 */
	public static final int MAX_DOC_COUNT = Page.DEFAULT_MAX_PAGES * MAX_PAGE_SIZE;

	/**
	 * <b>Prametr stronicowania:</b> Dynamiczne ustawienie ograniczenia na ilość
	 * dokumentów pobieranych. Domyślnie wartość ta przyjmuje
	 * {@link #MAX_DOC_COUNT}.
	 */
	private int maxCount = MAX_DOC_COUNT;

	/**
	 * <b>Prametr stronicowania:</b> Liczba dokumentów, jak ma występować na
	 * stronie. Na jego podstawie generowany jest między innymi obiekt strony w
	 * metodzie pobierania strony {@link #getPage()}, jeżeli oczywiście wcześniej
	 * nie był on ustawiony. Jeżeli ustwiamy obiekt strony (metoda
	 * {@link #setPage(Page)}) to parametr jest autoamtycznie przepisywany z
	 * definicji strony i przyjmuje wartość {@link Page#getSize()}.
	 */
	private int pageSize = Page.DEFAULT_PAGE_SIZE;

	/**
	 * Obiekt strony stronicowania.
	 */
	private Page page;

	/**
	 * Czy parametry sronicowania spełniają ograniczenia?
	 */
	private boolean valid = true;

	protected PagingParams() {
	}

	/**
	 * Ustawianie parametrów stronicowania na podstawie definicji strony.
	 * 
	 * @see #setPage(Page)
	 * @param page
	 *           definicja strony.
	 */
	public PagingParams(Page page) {
		super();
		if (!setPage(page))
			throw new PaginigParamsException(
					"Definicja strony wyniku nie spelnia warunków ograniczen");
	}

	/**
	 * Ustawianie parametrów stronicowania na podstawie definicji strony z
	 * możliwośćią zmiany ograniczenia na rozmiar strony.
	 * 
	 * @see #setPage(Page)
	 * @see #maxPageSize
	 * 
	 * @param page
	 *           definicja strony
	 * @param maxPageSize
	 *           ograniczenie na rozmiar strony
	 */
	public PagingParams(Page page, int maxPageSize) {
		super();
		setMaxPageSize(maxPageSize, false);
		if (maxPageSize < this.pageSize)
			this.pageSize = maxPageSize;
		if (!setPage(page))
			throw new PaginigParamsException(
					"Definicja strony wyniku nie spelnia warunków ograniczen");
	}

	/**
	 * Ustawianie parametrów stronicowania na podstawie definicji strony z
	 * możliwośćią zmiany ograniczenia na rozmiar strony oraz maksymalną liczbą
	 * zwracanych wyników zapytania.
	 * 
	 * @see #setPage(Page)
	 * @see #maxPageSize
	 * @see #maxCount
	 * 
	 * @param page
	 *           definicja strony
	 * @param maxPageSize
	 *           ograniczenie na rozmiar strony
	 * @param maxCount
	 *           ograniczenie na liczbę zwracanych wyników
	 */
	public PagingParams(Page page, int maxPageSize, int maxCount) {
		super();
		setMaxCount(maxCount, false);
		setMaxPageSize(maxPageSize, false);
		if (maxPageSize < this.pageSize)
			this.pageSize = maxPageSize;
		if (!setPage(page))
			throw new PaginigParamsException(
					"Definicja strony wyniku nie spelnia warunkow ograniczen");

	}

	/**
	 * Konstruktor za pomocą którego można wygenerować definicję strony podczas
	 * wywołania metody {@link #getPage()}.
	 * 
	 * @param pageSize
	 *           roziar strony, liczba elementów na stronie
	 * @param offset
	 *           numer pozycji elementu w wyniku zapytania dla pierwszego
	 *           dokumentu na stronie.
	 */
	public PagingParams(int pageSize, int offset) {
		super();
		if (!setOffset(offset, false))
			throw new PaginigParamsException(
					"Pierwsza pozycja wiersza w wyniku nie spelnia warunkow ograniczen");
		if (!setPageSize(pageSize, false))
			throw new PaginigParamsException(
					"Liczba wierszy na stronie nie spelnia warunkow ograniczen na maksymalny romiar strony");

	}

	/**
	 * Konstruktor za pomocą którego można wygenerować definicję strony podczas
	 * wywołania metody {@link #getPage()} z możliwośćią zmiany ograniczenia na
	 * rozmiar strony.
	 * 
	 * @see #maxPageSize
	 * 
	 * @param pageSize
	 *           roziar strony, liczba elementów na stronie
	 * @param offset
	 *           numer pozycji elementu w wyniku zapytania dla pierwszego
	 *           dokumentu na stronie.
	 * @param maxPageSize
	 *           ograniczenie na rozmiar strony
	 */
	public PagingParams(int pageSize, int offset, int maxPageSize) {
		super();
		setMaxPageSize(maxPageSize, false);
		if (maxPageSize < this.pageSize)
			this.pageSize = maxPageSize;
		if (!setOffset(offset, false))
			throw new PaginigParamsException(
					"Pierwsza pozycja wiersza w wyniku nie spelnia warunkow ograniczen");
		if (!setPageSize(pageSize, false))
			throw new PaginigParamsException(
					"Liczba wierszy na stronie nie spelnia warunkow ograniczen na maksymalny romiar strony");

	}

	/**
	 * Konstruktor za pomocą którego można wygenerować definicję strony podczas
	 * wywołania metody {@link #getPage()} z możliwośćią zmiany ograniczenia na
	 * rozmiar strony oraz maksymalną liczbą zwracanych wyników zapytania.
	 * 
	 * @see #maxPageSize
	 * @see #maxCount
	 * 
	 * @param pageSize
	 *           roziar strony, liczba elementów na stronie
	 * @param offset
	 *           numer pozycji elementu w wyniku zapytania dla pierwszego
	 *           dokumentu na stronie.
	 * @param maxPageSize
	 *           ograniczenie na rozmiar strony
	 * @param maxCount
	 *           ograniczenie na liczbę zwracanych wyników
	 */
	public PagingParams(int pageSize, int offset, int maxPageSize, int maxCount) {
		super();
		setMaxCount(maxCount, false);
		setMaxPageSize(maxPageSize, false);
		if (maxPageSize < this.pageSize)
			this.pageSize = maxPageSize;
		if (!setOffset(offset, false))
			throw new PaginigParamsException(
					"Pierwsza pozycja wiersza w wyniku nie spelnia warunkow ograniczen");
		if (!setPageSize(pageSize, false))
			throw new PaginigParamsException(
					"Liczba wierszy na stronie nie spelnia warunkow ograniczen na maksymalny romiar strony");
	}

	/**
	 * Pobieranie rozmiaru strony {@link #pageSize}
	 * 
	 * @return rozmiar strony
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Ustawianie rozmiaru strony {@link #pageSize}, ze sprawdzeniem jego
	 * poprawności w stosunku do ograniczeń związanych z maksymalnym rozmiarem
	 * strony (zobacz {@link #maxPageSize}).
	 * 
	 * @param pageSize
	 *           rozmiar strony, liczba dokumentów na stronie
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setPageSize(int pageSize) {
		return setPageSize(pageSize, true);
	}

	/**
	 * 
	 * @param pageSize
	 *           wartość parametru rozmiaru strony
	 * @param changePage
	 *           czy ma być zmieniony obiekt strony ({@link #page})?
	 * @return
	 */
	private boolean setPageSize(int pageSize, boolean changePage) {
		Param paramInfo =
				Param.pageSize.checkAccessibilityOperation(this.pageSize, pageSize, isReadOnly);

		if (checkRestrictions(paramInfo, pageSize, changePage)) {
			this.pageSize = pageSize;
			if (changePage) {
				createPage();
			}
			return true;
		}
		return false;
	}

	/**
	 * @return {@link #cursorOfPage}
	 */
	public int getCursorOfPage() {
		return cursorOfPage;
	}

	/**
	 * Ustawianie parametru {@link #cursorOfPage}, ze sprawdzeniem jego
	 * poprawności w stosunku do ograniczeń związanych obiektem stronicowania.
	 * 
	 * @param cursorOfPage
	 *           wartość parametru
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setCursorOfPage(int cursorOfPage) {
		return setCursorOfPage(cursorOfPage, true);
	}

	private boolean setCursorOfPage(int cursorOfPage, boolean changePage) {
		Param paramInfo = Param.cursorOfPage.checkAccessibilityOperation(
				this.cursorOfPage,
				cursorOfPage,
				isReadOnly);

		if (checkRestrictions(paramInfo, cursorOfPage, changePage)) {
			this.cursorOfPage = cursorOfPage;
			if (changePage) {
				createPage();
			}
			return true;
		}
		return false;

	}

	/**
	 * Pobieranie obiektu strony. Jeżeli wcześniej nie została ona zdefiniowana,
	 * to zostanie wygenerowany obiekt strony na podstawie {@link #cursorOfPage}
	 * oraz {@link #pageSize}.
	 * 
	 * @return aktualna definicja strony
	 */
	public Page getPage() {
		if (page == null || this.isReadOnly) {
			createPage();
		}
		return page;
	}

	/**
	 * Metoda tworząca obiekt strony. Wraz z utworzeniem strony odpowiednio jest
	 * modyfikowana pozycja {@link #cursorOfPage}.
	 */
	private void createPage() {
		int pageNumber = (this.getCursorOfPage() / this.getPageSize()) + 1;
		page = new Page(this.getPageSize(), pageNumber);
		this.cursorOfPage = page.getFirstRowNumber() - 1;
	}

	/**
	 * Ustawianie definicji aktualnej strony. Metoda sprawdza od razu czy strona
	 * spełnia warunki ograniczeń związanych z maksymalną liczbą
	 * {@link #maxCount} oraz {@link #maxPageSize}.
	 * 
	 * @see #page
	 * 
	 * @param page
	 *           defnicja strony
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setPage(Page page) {

		if (checkRestrictinsOfPage(page)) {
			this.pageSize = page.getSize();
			this.cursorOfPage = calculateCursorOfPage(page);
			this.page = page;
			return true;
		}
		return false;
	}

	/**
	 * Przeliczenie pozycji kursora na podstawie obiektu strony
	 * 
	 * @param page
	 *           definicja strony
	 * @return przeliczona pozycja kursora
	 */
	private int calculateCursorOfPage(Page page) {
		return (page.getNumber() - 1) * page.getSize();
	}

	/**
	 * Sprawdzenie restrykcji dotyczących zmiany definicji strony
	 * 
	 * @param page
	 *           definicja strony
	 * @return jezeli definicja strony jest poprawna to zostanie zwrócona wartość
	 *         {@code true}
	 */
	private boolean checkRestrictinsOfPage(Page page) {
		Param paramPageSizeInfo =
				Param.pageSize.checkAccessibilityOperation(this.pageSize, page.getSize(), isReadOnly);
		int cursorOfPage = calculateCursorOfPage(page);
		Param paramCursorOfPageInfo = Param.cursorOfPage.checkAccessibilityOperation(
				this.cursorOfPage,
				cursorOfPage,
				isReadOnly);

		return checkRestrictions(paramPageSizeInfo, page.getSize(), false)
				&& checkRestrictions(paramCursorOfPageInfo, cursorOfPage, false);
	}

	/**
	 * Ustawianie definicji aktualnej strony na podstawie numeru strony. Metoda
	 * sprawdza od razu czy strona spełnia warunki ograniczeń związanych z
	 * maksymalną liczbą {@link #maxCount}.
	 * 
	 * @see #page
	 * 
	 * @param pageNumber
	 *           numer strony
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setPage(int pageNumber) {
		Page newPage = new Page(this.getPageSize(), pageNumber);
		return setPage(newPage);
	}

	/**
	 * Pobieranie definicji pierwszej strony na podstwawie parametrów
	 * stronicowania. Numer pierwszej strony zdefiniowany jest przez
	 * {@link Page#MIN_PAGE_NR}, a jej rozmiar wynika z warunków parametrów
	 * stronicowania {@link #getPageSize()}.
	 * 
	 * @return obiekt pierwszej strony
	 */
	public Page getFirstPage() {
		Page firstPage = new Page(this.getPageSize(), Page.MIN_PAGE_NR);

		if (!checkRestrictinsOfPage(firstPage)) {
			throw new PaginigParamsException(
					"Definicja pierwszej strony nie spelnia warunkow ograniczen stronicowania");
		}
		return firstPage;
	}

	/**
	 * Pobieranie definicji strony o minimlnie możliwym numerze na podstwawie
	 * parametrów stronicowania. Numer strony zdefiniowany jest przez
	 * {@link #offset} oraz {@link PagingParams#pageSize}.
	 * 
	 * @return minimalna mozliwa strona do pobrania
	 */
	public Page getMinimalPage() {
		int pageNr = (this.offset / this.pageSize) + 1;
		Page minimalPage = new Page(this.getPageSize(), pageNr);
		if (!checkRestrictinsOfPage(minimalPage)) {
			throw new PaginigParamsException(
					"Definicja minimalnej strony nie spelnia warunkow ograniczen stronicowania");
		}
		return minimalPage;
	}

	/**
	 * Informacja o ograniczeniu związanych z maksymalną liczbą dokumentów na
	 * stronie
	 * 
	 * @return {@link PagingParams#maxPageSize}
	 */
	public int getMaxPageSize() {
		return maxPageSize;
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów na stronie
	 * {@link #maxPageSize}
	 * 
	 * @param maxPageSize
	 *           nowa wartość ograniczeń
	 */
	public void setMaxPageSize(int maxPageSize) {
		this.setMaxPageSize(maxPageSize, true);
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów na stronie
	 * {@link #maxPageSize}
	 * 
	 * @param maxPageSize
	 *           proponowana wartość maksymalnego rozmiaru strony
	 * @param checkPageSize
	 *           czy mam porównać proponowaną wartość maksymalnego rozmiaru
	 *           strony, z obcna wartoscia rozmiaru strony?
	 */
	private void setMaxPageSize(int maxPageSize, boolean checkPageSize) {
		Param.maxPageSize.checkAccessibilityOperation(this.maxPageSize, maxPageSize, isReadOnly);

		if (checkPageSize && maxPageSize < this.pageSize) {
			throw new PaginigParamsException("Obecny rozmiar strony "
					+ this.pageSize
					+ "jest wiekszy od proponowanej wartosci na maksymalny rozmiar strony "
					+ maxPageSize
					+ ". Wpierw zmniejsz rozmiar strony za pomoca metody setPageSize(int).");
		}
		if (maxPageSize > this.maxCount)
			throw new PaginigParamsException("Proponowana wartosc na maksymalny rozmiar strony "
					+ maxPageSize
					+ " jest wieksza od maksymalnej mozliwej liczby pobranych dokumentow: "
					+ this.maxCount);
		this.maxPageSize = maxPageSize;
	}

	/**
	 * Informacja o ograniczeniu związanych z maksymalną liczbą dokumentów w
	 * wyniku zapytania.
	 * 
	 * @return {@link PagingParams#maxCount}
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * Informacja o maksymalnej liczbie stron dostępnych przy aktualnych
	 * parametrach stronicowania.
	 * 
	 * @return maksymalna liczba stron
	 */
	public int getMaxPages() {
		int maxPages = this.maxCount / this.pageSize;
		if (0 != (this.maxCount % this.pageSize)) {
			maxPages++;
		}
		return maxPages;
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów w wyniku
	 * zapytania {@link #maxCount}
	 * 
	 * @param maxCount
	 *           nowa wartość ograniczeń
	 */
	public void setMaxCount(int maxCount) {
		this.setMaxCount(maxCount, true);
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów w wyniku
	 * zapytania {@link #maxCount}
	 * 
	 * @param maxCount
	 *           nowa wartość ograniczeń {@link #maxCount}
	 * @param checkPagingParams
	 *           sprawdzić czy nowa wartość ograniczeń narusza obecną
	 *           konfiguracje stronicowania?
	 */
	private void setMaxCount(int maxCount, boolean checkPagingParams) {
		Param.maxCount.checkAccessibilityOperation(this.maxCount, maxCount, isReadOnly);
		if (checkPagingParams
				&& (offset >= maxCount || cursorOfPage + pageSize > maxCount))
		{
			throw new PaginigParamsException("Obecne parametry stronicowania:"
					+ " pageSize: " + this.pageSize
					+ ", cursorOfPage: " + this.cursorOfPage
					+ ", offset: " + this.offset
					+ ", nie pozwalaja na zmiane maksymalnej "
					+ "liczby dokumentow w wyniku zapytania na wartosc: "
					+ maxCount
					+ ".");
		}
		this.maxCount = maxCount;
	}

	/**
	 * Sprawdza warunki graniczene ustawień stronicowania.
	 * 
	 * @param param
	 *           informacja o tym, podczas ustawiania którego z parametrów
	 *           wywoływana jest metoda sprawdzenia restrykcji
	 * @param value
	 *           wartość na jaką ma zostać zmieniony parametr
	 * @param pageIsChanged
	 *           informacja o tym czy obiekt strony zostal zmieniony poprzez
	 *           zmiany parametru, o którym jest mowa w argumencie 'param'
	 * @return warunki są spełnione
	 */
	private boolean checkRestrictions(Param param, int value, boolean pageIsChanged) {

		switch (param) {
		case pageSize:
			if ((offset % value) != 0) {
				logger
						.error(
						"[{}] Zle ustawione stronicowanie. Parametr 'offset' nie jest " +
						"wielokrotnoscia parametru 'pageSize' ({})! Ma warosc {}",
						new Object[]
					{ param.getMsgLabel(), pageSize, offset });
				valid = false;
				return false;
			}
			if (value <= 0 || value > maxPageSize) {
				logger
						.error(
								"[{}] Zle ustawione stronicowanie. Parametr 'pageSize' musi zawierac sie w (0,{}]! Ma warosc {}",
								new Object[]
									{ param.getMsgLabel(), maxPageSize, pageSize });
				valid = false;
				return false;
			}
			if (offset + value > maxCount) {
				logger
						.error(
								"[{}] Zle ustawione stronicowanie. Nie moge ustawic parametrow strony. "
										+
										"CursorOfPage ma wartosc: {}, pageSize: {}. Zbudowanie strony powoduje"
										+
										" przekroczenie maksymalnej liczby wierszy {} jaka moze byc wogole pobrana z wyniku.",
								new Object[]
									{ param.getMsgLabel(), cursorOfPage, pageSize, maxCount });
				valid = false;
				return false;
			}
			break;
		case offset:
			if (value < 0 || value >= maxCount) {
				logger
						.error(
								"[{}] Zle ustawione stronicowanie. Parametr 'offset' musi zawierac sie w [0,{})! Ma warosc {}",
								new Object[]
									{ param.getMsgLabel(), maxCount, cursorOfPage });
				valid = false;
				return false;
			}
			break;
		case cursorOfPage:
			if (value < offset || value >= maxCount) {
				logger
						.error(
								"[{}] Zle ustawione stronicowanie. Parametr 'cursorOfPage' musi zawierac sie w [{},{})! Ma warosc {}",
								new Object[]
									{ param.getMsgLabel(), offset, maxCount, cursorOfPage });
				valid = false;
				return false;
			}
			if (value + pageSize > maxCount) {
				logger
						.error(
								"[{}] Zle ustawione stronicowanie. Nie moge ustawic parametrow strony. "
										+
										"CursorOfPage ma wartosc: {}, pageSize: {}. Zbudowanie strony powoduje"
										+
										" przekroczenie maksymalnej liczby wierszy {} jaka moze byc wogole pobrana z wyniku.",
								new Object[]
									{ param.getMsgLabel(), cursorOfPage, pageSize, maxCount });
				valid = false;
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}

	public boolean isValid() {
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PagingParams ["
				+ "offset=" + offset
				+ ", cursorOfPage=" + cursorOfPage
				+ ", pageSize=" + pageSize
				+ ", maxCount=" + maxCount
				+ ", maxPageSize=" + maxPageSize
				+ ", page=" + page
				+ ", valid=" + valid
				+ "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.entities._ICopyable#copy()
	 */
	public PagingParams copy() {
		PagingParams target = new PagingParams();
		return target.copy(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.entities._ICopyable#copy(java.lang.Object)
	 */
	public PagingParams copy(PagingParams source) {
		if (isReadOnly)
			throw new PaginigParamsException(
					"Operacja zabroniona. Obiekt ma ustawiona flage tylko do odczytu.");
		this.cursorOfPage = source.getCursorOfPage();
		this.maxPageSize = source.getMaxPageSize();
		this.maxCount = source.getMaxCount();
		this.pageSize = source.getPageSize();
		this.page = new Page(source.getPage().getSize(), source.getPage().getNumber());
		this.offset = source.getOffset();
		this.valid = source.isValid();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.entities._ICopyable#copyTo(java.lang.Object)
	 */
	public void copyTo(PagingParams target) {
		target.copyTo(this);
	}

	/**
	 * @return the {@link #offset}
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 *           the {@link #offset} to set
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setOffset(int offset) {
		return setOffset(offset, true);
	}

	/**
	 * @param offset
	 *           the {@link #offset} to set
	 * @param changePage
	 *           czy ma być zmieniony obiekt strony ({@link #page})?
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	private boolean setOffset(int offset, boolean changePage) {
		Param paramInfo =
				Param.offset.checkAccessibilityOperation(this.offset, offset, isReadOnly);
		if (checkRestrictions(paramInfo, offset, changePage)) {
			if (this.cursorOfPage < offset) {
				this.cursorOfPage = offset;
			}
			this.offset = offset;
			if (changePage) {
				createPage();
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cursorOfPage;
		result = prime * result + maxCount;
		result = prime * result + maxPageSize;
		result = prime * result + offset;
		result = prime * result + pageSize;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagingParams other = (PagingParams) obj;
		if (cursorOfPage != other.cursorOfPage)
			return false;
		if (maxCount != other.maxCount)
			return false;
		if (maxPageSize != other.maxPageSize)
			return false;
		if (offset != other.offset)
			return false;
		if (pageSize != other.pageSize)
			return false;
		return true;
	}

	/**
	 * @return the {@link #isReadOnly}
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}

}
