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
	 * Domyślny rozmiar strony, czyli liczba wierszy prezentowana w
	 * stronicowanym wyniku zapytania
	 */
	public static final int DEFAULT_PAGE_SIZE = 60;

	/**
	 * Domyślny numer strony stronicowanego wyniku zapytania.
	 */
	public static final int DEFAULT_PAGE_NR = 1;
	/** Domyślny numer pierwszego wiersza */
	public static final Long DEFAULT_FIRST_ROW_NUMBER = 0L + DEFAULT_PAGE_NR;
	/** Domyślny numer ostatniego wiersza */
	public static final Long DEFAULT_LAST_ROW_NUMBER = 0L + DEFAULT_PAGE_SIZE;

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
	private Integer number = DEFAULT_PAGE_NR;

	/**
	 * numer pierwszego wiersza na stronie
	 */
	private Long firstRowNumber = DEFAULT_FIRST_ROW_NUMBER;

	/**
	 * numer ostatniego wiersza na stronie
	 */
	private Long lastRowNumber = DEFAULT_LAST_ROW_NUMBER;

	public Page() {
		super();
	}

	/**
	 * @param number
	 *            nr strony
	 */
	public Page(int number) {
		this(DEFAULT_PAGE_SIZE, number);
	}

	/**
	 * @param size
	 *            rozmiar strony, czyli maksymalna liczba wyników zapytania na
	 *            stronie
	 * @param number
	 *            nr strony
	 */
	public Page(int size, int number) {
		super();
		this.size = size;
		this.number = number;
		/** Niejawna konwersja do Long'a */
		this.firstRowNumber = 0L + ((this.number - 1) * this.size) + 1;
		this.lastRowNumber = this.firstRowNumber + this.size - 1;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the firstRowNumber
	 */
	@IgnoreProperty
	public Long getFirstRowNumber() {
		return firstRowNumber;
	}

	public String getFirstRowNumberByString() {
		return (firstRowNumber == null) ? null : firstRowNumber.toString();
	}

	/**
	 * @return the endPosition
	 */
	@IgnoreProperty
	public Long getLastRowNumber() {
		return lastRowNumber;
	}

	public String getLastRowNumberByString() {
		return (lastRowNumber == null) ? null : lastRowNumber.toString();
	}

	/**
	 * @param firstRowNumber
	 *            the firstRowNumber to set
	 */
	@IgnoreProperty
	void setFirstRowNumber(Long firstRowNumber) {
		this.firstRowNumber = firstRowNumber;
	}

	void setFirstRowNumberByString(String firstRowNumber) {
		this.firstRowNumber = (firstRowNumber == null) ? null : Long
				.valueOf(firstRowNumber);
	}

	/**
	 * @param lastRowNumber
	 *            the lastRowNumber to set
	 */
	@IgnoreProperty
	void setLastRowNumber(Long lastRowNumber) {
		this.lastRowNumber = lastRowNumber;
	}

	void setLastRowNumberByString(String lastRowNumber) {
		this.lastRowNumber = (lastRowNumber == null) ? null : Long
				.valueOf(lastRowNumber);
	}

	/**
	 * Liczba wierszy na stronie
	 * 
	 * @return liczba wierszy na stronie wyznaczoną poprzez różnicę pomiędzy
	 *         {@link #lastRowNumber} oraz {@link #firstRowNumber}
	 */
	public int getNumberOfRowsOnThePage() {
		Long numberOfRowsOnThePage = this.lastRowNumber - this.firstRowNumber
				+ 1L;
		return (this.lastRowNumber > this.firstRowNumber ? numberOfRowsOnThePage
				.intValue() : 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [" + "number=" + number + ", size=" + size
				+ ", firstRowNumber=" + firstRowNumber + ", lastRowNumber="
				+ lastRowNumber + ", numberOfRowsOnThePage="
				+ getNumberOfRowsOnThePage() + "]";
	}

	/* Overridden (non-Javadoc) */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstRowNumber == null) ? 0 : firstRowNumber.hashCode());
		result = prime * result
				+ ((lastRowNumber == null) ? 0 : lastRowNumber.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + size;
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
		Page other = (Page) obj;
		if (firstRowNumber == null) {
			if (other.firstRowNumber != null)
				return false;
		} else if (!firstRowNumber.equals(other.firstRowNumber))
			return false;
		if (lastRowNumber == null) {
			if (other.lastRowNumber != null)
				return false;
		} else if (!lastRowNumber.equals(other.lastRowNumber))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

}