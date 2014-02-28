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

/**
 * 
 * Page - definicja strony wyników. Implementację zaczerpnąłem z projektu
 * WPLucene.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @author Patryk Makuch &lt;pmakuch@slawas.pl&gt;
 * @version $Revision: 1.4 $
 * 
 */
public class Page implements Serializable {

	private static final long serialVersionUID = -5210221106589109534L;

	/**
	 * Domyślny rozmiar storny, czyli liczba wierszy przezentowana w
	 * stronicowanym wyniku zapytania
	 */
	public static final int DEFAULT_PAGE_SIZE = 60;

	/**
	 * Domyślny numer strony stronicowanego wyniku zapytania.
	 */
	public static final int DEFAULT_PAGE_NR = 1;

	/**
	 * Minimalny numer strony stronicowanego wyniku zapytania.
	 */
	public static final int MIN_PAGE_NR = 1;

	/**
	 * Domyślna maksymalna liczba stron wyników zapytania SQL
	 */
	public static int DEFAULT_MAX_PAGES = 300;

	/**
	 * Liczba wierszy prezentowanych na stronie z wynikami.
	 */
	private int size = DEFAULT_PAGE_SIZE;

	/**
	 * Nr strony jaką chcemy pobrać jako wynik zapytania
	 */
	private int number = DEFAULT_PAGE_NR;

	/**
	 * numer pierwszego wiersza na stronie
	 */
	private int firstRowNumber = DEFAULT_PAGE_NR;

	/**
	 * numer ostatniego wiersza na stronie
	 */
	private int lastRowNumber = DEFAULT_PAGE_SIZE;

	public Page() {
		super();
	}

	/**
	 * @param number
	 *           nr strony
	 */
	public Page(int number) {
		this(DEFAULT_PAGE_SIZE, number);
	}

	/**
	 * @param size
	 *           rozmiar strony, czyli maksymalna liczba wyników zapytania na
	 *           stronie
	 * @param number
	 *           nr strony
	 */
	public Page(int size, int number) {
		super();
		this.size = size;
		this.number = number;
		this.firstRowNumber = ((this.number - 1) * this.size) + 1;
		this.lastRowNumber = this.firstRowNumber + this.size - 1;
	}

	public int getSize() {
		return size;
	}

	public int getNumber() {
		return number;
	}

	/**
	 * @return the firstRowNumber
	 */
	public int getFirstRowNumber() {
		return firstRowNumber;
	}

	/**
	 * @return the endPosition
	 */
	public int getLastRowNumber() {
		return lastRowNumber;
	}

	/**
	 * @param firstRowNumber
	 *           the firstRowNumber to set
	 */
	void setFirstRowNumber(int firstRowNumber) {
		this.firstRowNumber = firstRowNumber;
	}

	/**
	 * @param lastRowNumber
	 *           the lastRowNumber to set
	 */
	void setLastRowNumber(int lastRowNumber) {
		this.lastRowNumber = lastRowNumber;
	}

	/**
	 * Liczba wierszy na stronie
	 * 
	 * @return liczba wierszy na stronie wyznaczoną poprzez różnicę pomiędzy
	 *         {@link #lastRowNumber} oraz {@link #firstRowNumber}
	 */
	public int getNumberOfRowsOnThePage() {
		return (this.lastRowNumber > this.firstRowNumber
				? (this.lastRowNumber - this.firstRowNumber + 1)
				: 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page ["
				+ "number=" + number
				+ ", size=" + size
				+ ", firstRowNumber=" + firstRowNumber
				+ ", lastRowNumber=" + lastRowNumber
				+ ", numberOfRowsOnThePage=" + getNumberOfRowsOnThePage()
				+ "]";
	}

}
