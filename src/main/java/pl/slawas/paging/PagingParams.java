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

import org.apache.cxf.aegis.type.java5.IgnoreProperty;

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
 * w momencie ustawiania parametru klasy Page. Przy ustawianiu parametrów
 * sprawdzane są ograniczenia. Jeśli podawana wartość prowadzi do przekroczenia
 * ograniczeń to wyszukiwanie nie zostanie przeprowadzone.
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @author Patryk Makuch &lt;pmakuch@slawas.pl&gt;
 * @version $Revision: 1.5.2.1 $
 * 
 */
public class PagingParams implements Serializable, _ICopyable<PagingParams> {

	private static final long serialVersionUID = -5210221106589109534L;

	private static Logger logger = LoggerFactory.getLogger(PagingParams.class);

	private enum Param {

		/** parametr {@link #offset} */
		offset,
		/** parametr {@link #cursorOfPage} */
		cursorOfPage,
		/** parametr {@link #maxPageSize} */
		maxPageSize,
		/** parametr {@link #maxCount} */
		maxCount,
		/** parametr {@link #pageSize} */
		pageSize,
		/** parametr {@link #page} */
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

		private Param checkAccessibilityOperation(Long currCalue,
				Long newValue, boolean objectIsReadOnly) {
			setValue(Long.toString(newValue));
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
	public static Long DEFAULT_PAGING_OFFSET = 0L;

	/**
	 * <b>Parametr stronicowania:</b> Liczba pierwszych wyników/dokumentów jaka
	 * ma być pominięta w odpowiedzi wyszukiwarki
	 */
	private Long offset = DEFAULT_PAGING_OFFSET;

	/**
	 * Pozycja kursora dla początku strony, po prostu numer wiersza w wyniku
	 * zapytania, który ma być pierwszym elementem na stronie. Np. Jeżeli chcemy
	 * odczytać kolejną stronę, to parametr zmieniany jest na wartość wskazującą
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
	private Long cursorOfPage = DEFAULT_PAGING_OFFSET;

	/**
	 * Domyślna maksymalna liczba dokumentów offsetu (maksymalna liczba na
	 * stronie)
	 */
	public static final int MAX_PAGE_SIZE = 1000;

	/**
	 * <b>Parametr stronicowania:</b> Dynamicznie ustawiane ograniczenia na
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
	public static final Long MAX_DOC_COUNT = 0L + Page.DEFAULT_MAX_PAGES
			* MAX_PAGE_SIZE;

	/**
	 * <b>Parametr stronicowania:</b> Dynamiczne ustawienie ograniczenia na
	 * ilość dokumentów pobieranych. Domyślnie wartość ta przyjmuje
	 * {@link #MAX_DOC_COUNT}.
	 */
	private Long maxCount = MAX_DOC_COUNT;

	/**
	 * <b>Parametr stronicowania:</b> Liczba dokumentów, jak ma występować na
	 * stronie. Na jego podstawie generowany jest między innymi obiekt strony w
	 * metodzie pobierania strony {@link #getPage()}, jeżeli oczywiście
	 * wcześniej nie był on ustawiony. Jeżeli ustawiamy obiekt strony (metoda
	 * {@link #setPage(Page)}) to parametr jest automatycznie przepisywany z
	 * definicji strony i przyjmuje wartość {@link Page#getSize()}.
	 */
	private int pageSize = Page.DEFAULT_PAGE_SIZE;

	/**
	 * Obiekt strony stronicowania.
	 */
	private Page page;

	/**
	 * Czy parametry stronicowania spełniają ograniczenia?
	 */
	private boolean valid = true;

	protected PagingParams() {
	}

	/**
	 * Ustawianie parametrów stronicowania na podstawie definicji strony.
	 * 
	 * @see #setPage(Page)
	 * @param page
	 *            definicja strony.
	 */
	public PagingParams(Page page) {
		super();
		if (!setPage(page))
			throw new PaginigParamsException(
					"Definicja strony wyniku nie spelnia warunków ograniczen");
	}

	/**
	 * Ustawianie parametrów stronicowania na podstawie definicji strony z
	 * możliwością zmiany ograniczenia na rozmiar strony.
	 * 
	 * @see #setPage(Page)
	 * @see #maxPageSize
	 * 
	 * @param page
	 *            definicja strony
	 * @param maxPageSize
	 *            ograniczenie na rozmiar strony
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
	 * możliwością zmiany ograniczenia na rozmiar strony oraz maksymalną liczbą
	 * zwracanych wyników zapytania.
	 * 
	 * @see #setPage(Page)
	 * @see #maxPageSize
	 * @see #maxCount
	 * 
	 * @param page
	 *            definicja strony
	 * @param maxPageSize
	 *            ograniczenie na rozmiar strony
	 * @param maxCount
	 *            ograniczenie na liczbę zwracanych wyników
	 */
	public PagingParams(Page page, int maxPageSize, Long maxCount) {
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
	 *            rozmiar strony, liczba elementów na stronie
	 * @param offset
	 *            numer pozycji elementu w wyniku zapytania dla pierwszego
	 *            dokumentu na stronie.
	 */
	public PagingParams(int pageSize, Long offset) {
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
	 * wywołania metody {@link #getPage()} z możliwością zmiany ograniczenia na
	 * rozmiar strony.
	 * 
	 * @see #maxPageSize
	 * 
	 * @param pageSize
	 *            rozmiar strony, liczba elementów na stronie
	 * @param offset
	 *            numer pozycji elementu w wyniku zapytania dla pierwszego
	 *            dokumentu na stronie.
	 * @param maxPageSize
	 *            ograniczenie na rozmiar strony
	 */
	public PagingParams(int pageSize, Long offset, int maxPageSize) {
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
	 * wywołania metody {@link #getPage()} z możliwością zmiany ograniczenia na
	 * rozmiar strony oraz maksymalną liczbą zwracanych wyników zapytania.
	 * 
	 * @see #maxPageSize
	 * @see #maxCount
	 * 
	 * @param pageSize
	 *            rozmiar strony, liczba elementów na stronie
	 * @param offset
	 *            numer pozycji elementu w wyniku zapytania dla pierwszego
	 *            dokumentu na stronie.
	 * @param maxPageSize
	 *            ograniczenie na rozmiar strony
	 * @param maxCount
	 *            ograniczenie na liczbę zwracanych wyników
	 */
	public PagingParams(int pageSize, Long offset, int maxPageSize,
			Long maxCount) {
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
	 *            rozmiar strony, liczba dokumentów na stronie
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	public boolean setPageSize(int pageSize) {
		return setPageSize(pageSize, true);
	}

	/**
	 * 
	 * @param pageSize
	 *            wartość parametru rozmiaru strony
	 * @param changePage
	 *            czy ma być zmieniony obiekt strony ({@link #page})?
	 * @return
	 */
	private boolean setPageSize(int pageSize, boolean changePage) {
		/* Niejawna konwersja do Long */
		Long pSize = 0L + pageSize;
		Param paramInfo = Param.pageSize.checkAccessibilityOperation(
				0L + this.pageSize, pSize, isReadOnly);

		if (checkRestrictions(paramInfo, pSize, changePage)) {
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
	@IgnoreProperty
	public Long getCursorOfPage() {
		return cursorOfPage;
	}

	public String getCursorOfPageByString() {
		return (cursorOfPage == null) ? null : cursorOfPage.toString();
	}

	/**
	 * Ustawianie parametru {@link #cursorOfPage}, ze sprawdzeniem jego
	 * poprawności w stosunku do ograniczeń związanych obiektem stronicowania.
	 * 
	 * @param cursorOfPage
	 *            wartość parametru
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	@IgnoreProperty
	public boolean setCursorOfPage(Long cursorOfPage) {
		return setCursorOfPage(cursorOfPage, true);
	}

	public void setCursorOfPageByString(String cursorOfPage) {
		setCursorOfPage(
				(cursorOfPage == null) ? null : Long.valueOf(cursorOfPage),
				true);
	}

	private boolean setCursorOfPage(Long cursorOfPage, boolean changePage) {
		Param paramInfo = Param.cursorOfPage.checkAccessibilityOperation(
				this.cursorOfPage, cursorOfPage, isReadOnly);

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
		Long pageNumber = 1L + (this.getCursorOfPage().longValue() / this
				.getPageSize());
		page = new Page(this.getPageSize(), pageNumber.intValue());
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
	 *            definicja strony
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	@IgnoreProperty
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
	 *            definicja strony
	 * @return przeliczona pozycja kursora
	 */
	private Long calculateCursorOfPage(Page page) {
		return 0L + (page.getNumber() - 1) * page.getSize();
	}

	/**
	 * Sprawdzenie restrykcji dotyczących zmiany definicji strony
	 * 
	 * @param page
	 *            definicja strony
	 * @return jeżeli definicja strony jest poprawna to zostanie zwrócona
	 *         wartość {@code true}
	 */
	private boolean checkRestrictinsOfPage(Page page) {
		Param paramPageSizeInfo = Param.pageSize.checkAccessibilityOperation(
				0L + this.pageSize, 0L + page.getSize(), isReadOnly);
		Long cursorOfPage = calculateCursorOfPage(page);
		Param paramCursorOfPageInfo = Param.cursorOfPage
				.checkAccessibilityOperation(this.cursorOfPage, cursorOfPage,
						isReadOnly);

		return checkRestrictions(paramPageSizeInfo, 0L + page.getSize(), false)
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
	 *            numer strony
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	@IgnoreProperty
	public boolean setPage(int pageNumber) {
		Page newPage = new Page(this.getPageSize(), pageNumber);
		return setPage(newPage);
	}

	/**
	 * Pobieranie definicji pierwszej strony na podstawie parametrów
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
					"Definicja pierwszej strony nie spełnia warunków ograniczeń stronicowania");
		}
		return firstPage;
	}

	/**
	 * Pobieranie definicji strony o minimalnie możliwym numerze na podstwawie
	 * parametrów stronicowania. Numer strony zdefiniowany jest przez
	 * {@link #offset} oraz {@link PagingParams#pageSize}.
	 * 
	 * @return minimalna możliwa strona do pobrania
	 */
	public Page getMinimalPage() {
		Long pageNr = 1L + (this.offset / this.pageSize);
		Page minimalPage = new Page(this.getPageSize(), pageNr.intValue());
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
	 *            nowa wartość ograniczeń
	 */
	public void setMaxPageSize(int maxPageSize) {
		this.setMaxPageSize(maxPageSize, true);
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów na stronie
	 * {@link #maxPageSize}
	 * 
	 * @param maxPageSize
	 *            proponowana wartość maksymalnego rozmiaru strony
	 * @param checkPageSize
	 *            czy mam porównać proponowaną wartość maksymalnego rozmiaru
	 *            strony, z obecną wartością rozmiaru strony?
	 */
	private void setMaxPageSize(int maxPageSize, boolean checkPageSize) {
		Param.maxPageSize.checkAccessibilityOperation(0L + this.maxPageSize,
				0L + maxPageSize, isReadOnly);

		if (checkPageSize && maxPageSize < this.pageSize) {
			throw new PaginigParamsException(
					"Obecny rozmiar strony "
							+ this.pageSize
							+ "jest wiekszy od proponowanej wartosci na maksymalny rozmiar strony "
							+ maxPageSize
							+ ". Wpierw zmniejsz rozmiar strony za pomoca metody setPageSize(int).");
		}
		if (maxPageSize > this.maxCount)
			throw new PaginigParamsException(
					"Proponowana wartosc na maksymalny rozmiar strony "
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
	@IgnoreProperty
	public Long getMaxCount() {
		return maxCount;
	}

	public String getMaxCountByString() {
		return (maxCount == null) ? null : maxCount.toString();
	}

	/**
	 * Informacja o maksymalnej liczbie stron dostępnych przy aktualnych
	 * parametrach stronicowania.
	 * 
	 * @return maksymalna liczba stron
	 */
	public int getMaxPages() {
		Long maxPages = this.maxCount / this.pageSize;
		if (0 != (this.maxCount % this.pageSize)) {
			maxPages++;
		}
		return maxPages.intValue();
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów w wyniku
	 * zapytania {@link #maxCount}
	 * 
	 * @param maxCount
	 *            nowa wartość ograniczeń
	 */
	@IgnoreProperty
	public void setMaxCount(Long maxCount) {
		this.setMaxCount(maxCount, true);
	}

	public void setMaxCountByString(String maxCount) {
		setMaxCount((maxCount == null) ? null : Long.valueOf(maxCount));
	}

	/**
	 * Zmiana ograniczeń związanych z maksymalną liczbą dokumentów w wyniku
	 * zapytania {@link #maxCount}
	 * 
	 * @param maxCount
	 *            nowa wartość ograniczeń {@link #maxCount}
	 * @param checkPagingParams
	 *            sprawdzić czy nowa wartość ograniczeń narusza obecną
	 *            konfiguracje stronicowania?
	 */
	private void setMaxCount(Long maxCount, boolean checkPagingParams) {
		Param.maxCount.checkAccessibilityOperation(this.maxCount, maxCount,
				isReadOnly);
		if (checkPagingParams
				&& (offset >= maxCount || cursorOfPage + pageSize > maxCount)) {
			throw new PaginigParamsException("Obecne parametry stronicowania:"
					+ " pageSize: " + this.pageSize + ", cursorOfPage: "
					+ this.cursorOfPage + ", offset: " + this.offset
					+ ", nie pozwalaja na zmiane maksymalnej "
					+ "liczby dokumentow w wyniku zapytania na wartosc: "
					+ maxCount + ".");
		}
		this.maxCount = maxCount;
	}

	/**
	 * Sprawdza warunki graniczne ustawień stronicowania.
	 * 
	 * @param param
	 *            informacja o tym, podczas ustawiania którego z parametrów
	 *            wywoływana jest metoda sprawdzenia restrykcji
	 * @param value
	 *            wartość na jaką ma zostać zmieniony parametr
	 * @param pageIsChanged
	 *            informacja o tym czy obiekt strony został zmieniony poprzez
	 *            zmiany parametru, o którym jest mowa w argumencie 'param'
	 * @return warunki są spełnione
	 */
	private boolean checkRestrictions(Param param, Long value,
			boolean pageIsChanged) {

		switch (param) {
		case pageSize:
			if ((offset % value) != 0) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Parametr 'offset' nie jest "
								+ "wielokrotnoscia parametru 'pageSize' ({})! Ma warosc {}",
						new Object[] { param.getMsgLabel(), pageSize, offset });
				valid = false;
				return false;
			}
			if (value <= 0 || value > maxPageSize) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Parametr 'pageSize' musi zawierac sie w (0,{}]! "
								+ "Obecnie parametr 'pageSize' ma ustawiona warosc {}",
						new Object[] { param.getMsgLabel(), maxPageSize,
								pageSize });
				valid = false;
				return false;
			}
			if (offset + value > maxCount) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Nie moge ustawic parametrow strony. "
								+ "CursorOfPage ma wartosc: {}, pageSize: {}. Zbudowanie strony powoduje"
								+ " przekroczenie maksymalnej liczby wierszy {} jaka moze byc wogole pobrana z wyniku.",
						new Object[] { param.getMsgLabel(), cursorOfPage,
								pageSize, maxCount });
				valid = false;
				return false;
			}
			break;
		case offset:
			if (value < 0 || value >= maxCount) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Parametr 'offset' musi zawierac sie w [0,{})! Obecna warosc to {}",
						new Object[] { param.getMsgLabel(), maxCount,
								cursorOfPage });
				valid = false;
				return false;
			}
			break;
		case cursorOfPage:
			if (value < offset || value >= maxCount) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Parametr 'cursorOfPage' musi zawierac sie w [{},{})! Obecna warosc to {}",
						new Object[] { param.getMsgLabel(), offset, maxCount,
								cursorOfPage });
				valid = false;
				return false;
			}
			if (value + pageSize > maxCount) {
				logger.error(
						"[{}] Zle ustawione stronicowanie. Nie moge ustawic parametrow strony. "
								+ "CursorOfPage ma wartosc: {}, pageSize: {}. Zbudowanie strony powoduje"
								+ " przekroczenie maksymalnej liczby wierszy {} jaka moze byc wogole pobrana z wyniku.",
						new Object[] { param.getMsgLabel(), cursorOfPage,
								pageSize, maxCount });
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

	public void setIsValid(boolean valid) {
		this.valid = valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PagingParams [" + "offset=" + offset + ", cursorOfPage="
				+ cursorOfPage + ", pageSize=" + pageSize + ", maxCount="
				+ maxCount + ", maxPageSize=" + maxPageSize + ", page=" + page
				+ ", valid=" + valid + "]";
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
		this.page = new Page(source.getPage().getSize(), source.getPage()
				.getNumber());
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
	@IgnoreProperty
	public Long getOffset() {
		return offset;
	}

	public String getOffsetByString() {
		return (offset == null) ? null : offset.toString();
	}

	/**
	 * 
	 * @param offset
	 *            the {@link #offset} to set
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	@IgnoreProperty
	public boolean setOffset(Long offset) {
		return setOffset(offset, true);
	}

	public void setOffsetByString(String offset) {
		setOffset((offset == null) ? null : Long.valueOf(offset));
	}

	/**
	 * @param offset
	 *            the {@link #offset} to set
	 * @param changePage
	 *            czy ma być zmieniony obiekt strony ({@link #page})?
	 * @return {@code true}, jeżeli wszystko jest OK.
	 */
	private boolean setOffset(Long offset, boolean changePage) {
		Param paramInfo = Param.offset.checkAccessibilityOperation(this.offset,
				offset, isReadOnly);
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

	/* Overridden (non-Javadoc) */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cursorOfPage == null) ? 0 : cursorOfPage.hashCode());
		result = prime * result + (isReadOnly ? 1231 : 1237);
		result = prime * result
				+ ((maxCount == null) ? 0 : maxCount.hashCode());
		result = prime * result + maxPageSize;
		result = prime * result + ((offset == null) ? 0 : offset.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result + pageSize;
		result = prime * result + (valid ? 1231 : 1237);
		return result;
	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagingParams other = (PagingParams) obj;
		if (cursorOfPage == null) {
			if (other.cursorOfPage != null)
				return false;
		} else if (!cursorOfPage.equals(other.cursorOfPage))
			return false;
		if (isReadOnly != other.isReadOnly)
			return false;
		if (maxCount == null) {
			if (other.maxCount != null)
				return false;
		} else if (!maxCount.equals(other.maxCount))
			return false;
		if (maxPageSize != other.maxPageSize)
			return false;
		if (offset == null) {
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (valid != other.valid)
			return false;
		return true;
	}

	/**
	 * @return the {@link #isReadOnly}
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

}
