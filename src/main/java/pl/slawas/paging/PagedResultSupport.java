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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import pl.slawas.support.EnumerationSupport;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * PagedResultSupport - abstrakcja stronicowanego wyniku zapytania
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.6.2.1 $
 * 
 * @param <Obj>
 *            klasa obiektu na liscie rezultatów
 */
@SuppressWarnings("serial")
public abstract class PagedResultSupport<Obj> implements Serializable,
		_IPagedResult<Obj> {

	private final Page pageInfo;

	final private transient Logger logger = LoggerFactory.getLogger(getClass()
			.getName());

	private Enumeration<Page> pages;

	protected _IPagedQuery<Obj> query;

	protected ResultSupport<Obj> result;

	protected final PagingParams pagingParams;

	/**
	 * 
	 * @param pagingParams
	 *            parametry stronicowania
	 * @param result
	 *            obiektu rezultatu "pierwotnego", który jest podstwą do
	 *            zbudowania stronicowanego rezultatu
	 * @param query
	 *            zapytanie, na podstwie, którego został zbudowany rezultat
	 *            "pierwotny". Obiekt ten potrzebny jest aby bez konieczności
	 *            odwoływania się do manager-ów zarządzajacych zapytaniami,
	 *            można było pobrać kolejną stronę, czy też odświeżyć wynik.
	 * @param checkPagingParamRestrictions
	 *            czy mają być przestrzegane parametry stronicowania. Niektóre
	 *            systemy dają możliwość pobrania kolejnej "paczki" danych w
	 *            systuacji, kiedy wynik ogólny (liczba zwracanych
	 *            dokumentów/wierszy) nie spełnia warunków związnych z
	 *            ograniczeniami np. ograniczenia pozwalają na pobranie
	 *            maksymalnie {@code 5} stron i całość rezultatu nie mieści sie
	 *            na nich. Gdy parametr ustawimy parametr na {@code true}, to
	 *            wtedy nie bedziemy mogli pobrać kolejnej strony np. 6 wyniku,
	 *            trzeba zmienic kryteria stronicowania. Jeżeli jednak ustawimy
	 *            na {@code false}, parametry stronicowania zostaną zignorowane
	 *            i stronicowany obiekt pozwoli na utworzenie kolejnej paczki
	 *            stron np. {@code 6,7,8,9,10}. O tym czy wynik ma kolejną
	 *            paczkę informuje nas metoda {@link #hasMoreResultRows()}, a o
	 *            tym czy jesteśmy w "paczce rozszerzonej" (w kolejnej paczce)
	 *            możemy rozpoznać po tym, że pierwsza strona paczki ma numer
	 *            różny od {@link Page#MIN_PAGE_NR}, albo używając metody
	 *            {@link #hasPreviousResultRows()}.
	 */
	public PagedResultSupport(PagingParams pagingParams,
			ResultSupport<Obj> result, _IPagedQuery<Obj> query,
			boolean checkPagingParamRestrictions) {
		super();
		this.query = query;
		this.result = result;
		this.pagingParams = pagingParams;
		Long firstRowNumber = 0L;
		Long lastRowNumber = 0L;

		switch (this.result.getMessage()) {
		case NO_DATA_FOUND:
			this.pageInfo = pagingParams.getFirstPage();
			break;
		case FRAGMENT:
		case ALL:
		default:
			int pageNumber = Page.MIN_PAGE_NR;
			Long startPosition = this.result.getStartPosition();
			firstRowNumber = startPosition;
			lastRowNumber = this.result.getEndPosition();
			if (this.result.getAbsoluteFirstRowPosition() != firstRowNumber) {
				Long pNumber = 1L + (startPosition / pagingParams.getPageSize());
				pageNumber = pNumber.intValue();
			}
			if (checkPagingParamRestrictions) {
				if (!pagingParams.setPage(pageNumber)) {
					throw new PaginigParamsException(
							"Problem z ustwieniem strony o numerze "
									+ pageNumber + ": "
									+ pagingParams.toString());
				}
				this.pageInfo = pagingParams.getPage();
			} else {
				this.pageInfo = new Page(pagingParams.getPageSize(),
						(pageNumber == 0 ? Page.MIN_PAGE_NR : pageNumber));
			}
			break;
		}
		this.pageInfo.setFirstRowNumber(firstRowNumber);
		this.pageInfo.setLastRowNumber(lastRowNumber);
	}

	public Collection<Obj> getResult() {
		return this.result.getResult();
	}

	public Collection<Obj> getResult(Page page) {
		return this.query.getPagedResult(page).getResult();
	}

	public Obj getUniqueResult() {
		if (!getMessage().equals(ResultMessage.NO_DATA_FOUND)
				&& this.result.getResult() != null
				&& !this.result.getResult().isEmpty()
				&& this.result.getResult().iterator().hasNext()) {
			return this.result.getResult().iterator().next();
		} else if (!getMessage().equals(ResultMessage.NO_DATA_FOUND)
				&& (this.result.getResult() == null
						|| this.result.getResult().isEmpty() || !this.result
						.getResult().iterator().hasNext())) {
			logger.warn(
					"Komunikat rezultatu jest niezgodny ze stanem faktycznym: "
							+ "\n getMessage(): {}"
							+ "\n (this.result.getResult() == null): {}"
							+ "\n this.result.getResult().isEmpty(): {}"
							+ "\n this.result.getResult().iterator().hasNext(): {}",
					new Object[] {
							getMessage(),
							(this.result.getResult() == null),
							(this.result.getResult() != null ? this.result
									.getResult().isEmpty() : "n/a"),
							(this.result.getResult() != null ? this.result
									.getResult().iterator().hasNext() : "n/a"), });
		}
		return null;
	}

	public ResultMessage getMessage() {
		return this.result.getMessage();
	}

	public Long getResultSize() {
		return this.result.getResultSize();
	}

	public Page getCurrentPageInfo() {
		return this.pageInfo;
	}

	public Page getFirstPageInfo() {
		Long resultSize = calculateResultSize();
		int firstPageNumber = calculateFirstPageNumber();
		Page firstPage = new Page(this.pageInfo.getSize(), firstPageNumber);
		Long firstRowNumber = 0L;
		Long lastRowNumber = 0L;

		switch (this.result.getMessage()) {
		case NO_DATA_FOUND:
			break;
		case FRAGMENT:
		case ALL:
		default:
			firstRowNumber = firstPage.getFirstRowNumber();
			lastRowNumber = firstPage.getLastRowNumber();
			if (lastRowNumber.longValue() > resultSize.longValue())
				lastRowNumber = resultSize;
			break;
		}
		firstPage.setFirstRowNumber(firstRowNumber);
		firstPage.setLastRowNumber(lastRowNumber);

		return firstPage;
	}

	public Page getPreviousPageInfo() throws PagedResultException {
		int firstPageNumber = calculateFirstPageNumber();
		if (this.pageInfo.getNumber() == firstPageNumber)
			throw new PagedResultException(
					"Obecna strona jest pierwsza strona. Strona poprzednia nie istnieje");
		return new Page(this.pageInfo.getSize(), this.pageInfo.getNumber() - 1);
	}

	public Page getNextPageInfo() throws PagedResultException {
		Page nextPage = getNextPageInfo(this.pageInfo);
		if (null == nextPage)
			throw new PagedResultException(
					"Obecna strona jest ostatnia strona. Strona nastepna nie istnieje");
		return nextPage;
	}

	public Page getPageInfo(Integer pageNumber) throws PagedResultException {
		int firstPageNumber = calculateFirstPageNumber();

		if (pageNumber < firstPageNumber) {
			throw new PagedResultException(
					"Strona o numerze "
							+ Integer.toString(pageNumber)
							+ (firstPageNumber != Page.MIN_PAGE_NR ? " jest poza zakresem obecnego rezultatu"
									: " nie istnieje"));
		}

		Long resultSize = calculateResultSize();
		Page page = new Page(this.pageInfo.getSize(), pageNumber);
		if (page.getFirstRowNumber().longValue() > resultSize.longValue())
			throw new PagedResultException("Strona o numerze "
					+ Integer.toString(pageNumber)
					+ " jest poza zakresem obecnego rezultatu");

		if (page.getLastRowNumber().longValue() > resultSize.longValue())
			page.setLastRowNumber(resultSize);

		return page;
	}

	/**
	 * Metoda pomocnicza wyznaczająca kolejna strone dla podanej w argumencie
	 * strony
	 * 
	 * @param page
	 *            strona dla której wyznaczona zostanie strona następna
	 * @return następna strona dla argumentu 'page', jeżeli następna strona nie
	 *         istnieje wynikiem jest {@code null}
	 */
	private Page getNextPageInfo(Page page) {

		Long resultSize = calculateResultSize();
		Page nextPage = new Page(page.getSize(), page.getNumber() + 1);
		if (nextPage.getFirstRowNumber().longValue() > resultSize.longValue())
			return null;

		if (nextPage.getLastRowNumber().longValue() > resultSize.longValue())
			nextPage.setLastRowNumber(resultSize);

		return nextPage;

	}

	/**
	 * Wyliczenie faktycznego rozmiaru wyniku
	 * 
	 * @return faktyczny rozmiar wyniku
	 */
	private Long calculateResultSize() {
		logger.trace(
				"calculateResultSize(): "
						+ "\n	this.result.getFirstRowPosition(): {}"
						+ "\n	this.result.getAbsoluteFirstRowPosition(): {}"
						+ "\n	this.result.getResultSize(): {}"
						+ "\n	this.pagingParams.getOffset(): {}",
				new Object[] { this.result.getFirstRowPosition(),
						this.result.getAbsoluteFirstRowPosition(),
						this.result.getResultSize(),
						this.pagingParams.getOffset() });
		return (this.result.getFirstRowPosition() - this.result
				.getAbsoluteFirstRowPosition())
				+ (this.result.getResultSize() - this.pagingParams.getOffset());
	}

	public Page getLastPageInfo() {
		Long resultSize = calculateResultSize();
		int pageNumber = calculatePageNumber(resultSize);
		Page lastPage = new Page(this.pageInfo.getSize(), pageNumber);
		Long firstRowNumber = 0L;
		Long lastRowNumber = 0L;

		switch (this.result.getMessage()) {
		case NO_DATA_FOUND:
			break;
		case FRAGMENT:
		case ALL:
		default:
			firstRowNumber = lastPage.getFirstRowNumber();
			lastRowNumber = lastPage.getLastRowNumber();
			if (lastRowNumber.longValue() > resultSize.longValue())
				lastRowNumber = resultSize;
			break;
		}
		lastPage.setFirstRowNumber(firstRowNumber);
		lastPage.setLastRowNumber(lastRowNumber);

		return lastPage;
	}

	/**
	 * @param resultSize
	 * @return
	 */
	private int calculatePageNumber(Long resultSize) {
		Long pNumber = (resultSize / this.pageInfo.getSize());
		int pageNumber = pNumber.intValue();
		if (((resultSize.longValue() % this.pageInfo.getSize()) != 0)
				|| (pageNumber == 0)) {
			pageNumber++;
		}
		return pageNumber;
	}

	public Enumeration<Page> getAllPages() {
		if (this.pages == null) {
			Vector<Page> pages = new Vector<Page>();
			int firstPageNumber = calculateFirstPageNumber();
			Page nextPage = new Page(this.pageInfo.getSize(), firstPageNumber);
			while (nextPage != null) {
				pages.add(nextPage);
				nextPage = getNextPageInfo(nextPage);
			}
			this.pages = new PagesEnumerator(pages);
		}
		return ((PagesEnumerator) this.pages).getNewInstance();
	}

	/**
	 * @return numer pierwszej strony
	 */
	private int calculateFirstPageNumber() {
		Long firstPageNumber = 0L + Page.MIN_PAGE_NR;
		if (this.result.getFirstRowPosition() != this.result
				.getAbsoluteFirstRowPosition()) {
			firstPageNumber = (this.result.getFirstRowPosition() / pageInfo
					.getSize()) + 1;
		}
		return firstPageNumber.intValue();
	}

	/**
	 * 
	 * PagesEnumerator - prywatna implementacja enumeratora stron stronicowanego
	 * wyniku zapytania.
	 * 
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.6.2.1 $
	 * 
	 */
	private class PagesEnumerator extends EnumerationSupport<Page> {

		private PagesEnumerator() {
			super();
		}

		private PagesEnumerator(Vector<Page> elements) {
			super(elements);
		}

		@Override
		public EnumerationSupport<Page> getNewInstance() {
			return new PagesEnumerator(elements);
		}

	}

	public boolean hasMoreResultRows() {
		return result.hasMoreResultRows();
	}

	public Long getLastRowPosition() {
		return result.getLastRowPosition();
	}

	public _IPagedQuery<Obj> getQuery() {
		return query;
	}

	public Long getFirstRowPositionOfPreviousResultRows() {
		Long firstRowPositionOfPreviousResultRows;
		if (!hasPreviousResultRows())
			return null;

		firstRowPositionOfPreviousResultRows = this.result
				.getFirstRowPosition()
				- (this.result.getResultMaxPages() * pageInfo.getSize());

		return firstRowPositionOfPreviousResultRows;
	}

	public boolean hasPreviousResultRows() {
		return (this.result.getFirstRowPosition() != this.result
				.getAbsoluteFirstRowPosition());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.paging._IPagingInfo#getPageNr()
	 */
	public int getPageNr() {
		return this.pageInfo.getNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.paging._IPagingInfo#getPageSize()
	 */
	public int getPageSize() {
		return this.pageInfo.getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.paging._IPagingInfo#getNumberOfRowsOnThePage()
	 */
	public int getNumberOfRowsOnThePage() {
		return this.pageInfo.getNumberOfRowsOnThePage();
	}

	/**
	 * @return the pagingParams
	 */
	public PagingParams getPagingParams() {
		return this.pagingParams;
	}

	/**
	 * Pobieranie obiektu rezultatu "pierwotnego", który jest podstwą do
	 * zbudowania stronicowanego rezultatu. Obiekt ten jest ustawiany w
	 * konstruktorze
	 * {@link #PagedResultSupport(PagingParams, ResultSupport, _IPagedQuery, boolean)}
	 * i nie raz może być potrzebny do dalszego przetwarzania.
	 * 
	 * @return obiekt rezultatu "pierwotnego"
	 */
	public ResultSupport<Obj> getPreDefinitionOfResult() {
		return this.result;
	}
}
