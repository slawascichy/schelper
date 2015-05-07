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
import java.util.Enumeration;

/**
 * 
 * _IPagingInfo - informacje o stronicowaniu
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 */
public interface _IPagingInfo extends Serializable {

	/**
	 * Pobranie informacji o aktulanej stronie
	 * 
	 * @return informacje o aktualnej stronie
	 */
	Page getCurrentPageInfo();

	/**
	 * Pobranie informacji o pierwszej stronie
	 * 
	 * @return informacje o pierwszej stronie
	 */
	Page getFirstPageInfo();

	/**
	 * Pobranie informacji o poprzedniej stronie
	 * 
	 * @return informacje o poprzedniej stronie
	 * @throws PagedResultException
	 *             zwraca błąd gdy obecna strona {@link #getCurrentPageInfo()}
	 *             jest stroną pierwszą. Poprzednia nie istnieje.
	 */
	Page getPreviousPageInfo() throws PagedResultException;

	/**
	 * Pobranie informacji o następnej stronie
	 * 
	 * @return informacje o następnej stronie
	 * @throws PagedResultException
	 *             zwraca błąd gdy obecna strona {@link #getCurrentPageInfo()}
	 *             jest stroną ostatnią. Następna nie istnieje.
	 */
	Page getNextPageInfo() throws PagedResultException;

	/**
	 * Pobranie informacji o stronie o podanym numerze
	 * 
	 * @param pageNumber
	 *            numer intetersującej nas strony
	 * @return informacje o stronie
	 * @throws PagedResultException
	 *             zwraca błąd gdy strona o danym numerze nie istnieje.
	 */
	Page getPageInfo(Integer pageNumber) throws PagedResultException;

	/**
	 * Pobranie informacji o ostatniej stronie
	 * 
	 * @return informacje o ostatniej stronie
	 */
	Page getLastPageInfo();

	/**
	 * @return the pages
	 */
	Enumeration<Page> getAllPages();

	/**
	 * Komunikat wyniku
	 * 
	 * @return komunikat wyniku {@link ResultMessage}
	 */
	ResultMessage getMessage();

	/**
	 * Liczba wswzystkich wierszy jakie można otrzymać w wyniku tego zapytania.
	 * 
	 * @return liczba wierszy
	 */
	Long getResultSize();

	/**
	 * Metode można wykożystać do badania czy został przekroczony zakres liczby
	 * wyników. Przy stronicowanym rezultacie moze dojsc do takiej sytuacji, ze
	 * nie wszystkie wiersze spełniające warunki zapytania SQL mieszczą się w
	 * danym rezultacie (ograniczenie maksymalnej liczby stron wyników). Wtedy
	 * jest ustwiana odpowiednia flaga, z informacją że wyniki się nie zmieściły
	 * i ta metoda zwraca informacje o wystąpieniu takiego zdarzenia. Mozna
	 * później wykorzystać metodę {@link #getLastRowPosition()} do pobrania
	 * kolejenj paczki rezultatu poprzez wywołanie wykonania zapytania JDBC ze
	 * odpowiednią wartością argumentu 'startPosition'.
	 * <p>
	 * Przykład na podstawie implementacji:
	 * </p>
	 * 
	 * <pre>
	 * if (pagedResult.hasMoreResultRows()) {
	 * 	T lastQuery = pagedResult.getQuery();
	 * 	lastQuery.setExecuted(false);
	 * 	lastQuery.execute(em.getConnection(), pagedResult.getLastRowPosition() + 1,
	 * 			null, CacheUsage.NONE);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} oznacza, że jest więcej wierszy w bazie danych
	 *         spełniających warunek wyszukiwania zapytania SQL.
	 */
	boolean hasMoreResultRows();

	/**
	 * Implementacja biblioteki pozwala na pobranie kolejnej części wyników
	 * zapytania w przypadku gdy {@link #hasMoreResultRows()} zwraca
	 * {@code true} . Dla takiego stronicowanego wyniku istotna jest informacja
	 * czy posiada ona poprzednia część rezultatu.
	 * 
	 * @return {@code true} oznacza, że dany wynik jest wynikiem rozszerzonym
	 *         (poza granicą maksymalnej liczby stron) i posiada poprzedzający
	 *         obecny wynik obiekt wyniku stronicowanego.
	 */
	boolean hasPreviousResultRows();

	/**
	 * @return ostatnia pozycja wiersza w stronicowanym wyniku zapytania.
	 */
	Long getLastRowPosition();

	/**
	 * @see #hasPreviousResultRows()
	 * @see #hasMoreResultRows()
	 * 
	 * @return pierwsza pozycja wiersza poprzedniej paczki wyników. Jezeli
	 *         poprzenia paczka nie istnieje (zobacz
	 *         {@link #hasPreviousResultRows()}), zwraca {@code null}.
	 */
	Long getFirstRowPositionOfPreviousResultRows();

	/**
	 * Na skróty: Pobieranie rozmiaru obecnej strony.
	 * <p>
	 * Tę samą informacje można otrzymać poprzez pobranie obiektu @{@link Page}
	 * za pomocą metody {@link #getCurrentPageInfo()} i wykorzystanie informacji
	 * w {@link Page#getSize()}.
	 * </p>
	 * 
	 * @return rozmiar aktualnej strony
	 */
	int getPageSize();

	/**
	 * Na skróty: Pobieranie liczby wierszy na obecnej strony.
	 * <p>
	 * Tę samą informacje można otrzymać poprzez pobranie obiektu @{@link Page}
	 * za pomocą metody {@link #getCurrentPageInfo()} i wykorzystanie informacji
	 * w {@link Page#getNumberOfRowsOnThePage()}.
	 * </p>
	 * 
	 * @return rozmiar aktualnej strony
	 */
	int getNumberOfRowsOnThePage();

	/**
	 * Na skróty: Pobieranie numeru obecnej strony
	 * <p>
	 * Tę samą informacje można otrzymać poprzez pobranie obiektu @{@link Page}
	 * za pomocą metody {@link #getCurrentPageInfo()} i wykorzystanie informacji
	 * w {@link Page#getNumber()}.
	 * </p>
	 * 
	 * @return numer aktualnej strony
	 */
	int getPageNr();

	/**
	 * @return the pagingParams
	 */
	PagingParams getPagingParams();

}