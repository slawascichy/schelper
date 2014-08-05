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

/**
 * 
 * ResultSupport klasa abstrakcyjna predefiniująca wynik stronicowanego
 * zapytania
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 * @param <Obj>
 */
@SuppressWarnings("serial")
public abstract class ResultSupport<Obj> implements Serializable, _IResultInfo {

	private final Long startPosition;

	private final Long endPosition;

	private final Long resultSize;

	private final Collection<Obj> result;

	private final ResultMessage message;

	private boolean hasMoreResultRows = false;

	private final Long lastRowPosition;

	private final Long firstRowPosition;

	private final Integer resultMaxPages;

	private final Long absoluteFirstRowPosition;

	/** Czas wykonania zapytania (w milisekundach) */
	private final Long executionTime;

	/**
	 * @param startPosition
	 *            numer pierwszej pozycji na stronie
	 * @param endPosition
	 *            numer ostatniej pozycji na stronie
	 * @param firstRowPosition
	 *            pierwsza pozycja rezultatu
	 * @param resultSize
	 *            rozmiar rezultatu
	 * @param lastRowPosition
	 *            ostatnia pozycja rezultatu
	 * @param result
	 *            lista wyników rezultatu
	 * @param message
	 *            komunikat związany z rezultatem ({@link ResultMessage})
	 * @param absoluteFirstRowPosition
	 *            absolutny pierwszy numer wiersza w wyniku zapytania
	 * @param resultMaxPages
	 *            predefiniowana maksymalna liczba stron w rezultacie.
	 * @param executionTime
	 *            czas wykonania zapytania (w milisekundach)
	 */
	protected ResultSupport(Long startPosition, Long endPosition,
			Long firstRowPosition, Long resultSize, Long lastRowPosition,
			Collection<Obj> result, ResultMessage message,
			Long absoluteFirstRowPosition, Integer resultMaxPages,
			Long executionTime) {
		super();
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.resultSize = resultSize;
		this.result = result;
		this.message = message;
		this.lastRowPosition = lastRowPosition;
		this.firstRowPosition = firstRowPosition;
		this.absoluteFirstRowPosition = absoluteFirstRowPosition;
		this.resultMaxPages = resultMaxPages;
		this.executionTime = executionTime;
	}

	public Long getStartPosition() {
		return startPosition;
	}

	public Long getEndPosition() {
		return endPosition;
	}

	public Long getResultSize() {
		return resultSize;
	}

	/**
	 * @return the result
	 */
	public Collection<Obj> getResult() {
		return result;
	}

	public ResultMessage getMessage() {
		return message;
	}

	public Obj getUniqueResult() {
		if (result != null && !result.isEmpty()) {
			return result.iterator().next();
		}
		return null;
	}

	public boolean hasMoreResultRows() {
		return hasMoreResultRows;
	}

	/**
	 * @param hasMoreResultRows
	 *            the hasMoreResultRows to set
	 */
	public void setHasMoreResultRows(boolean hasMoreResultRows) {
		this.hasMoreResultRows = hasMoreResultRows;
	}

	public Long getLastRowPosition() {
		return lastRowPosition;
	}

	public Long getFirstRowPosition() {
		return firstRowPosition;
	}

	public Integer getResultMaxPages() {
		return resultMaxPages;
	}

	public Long getAbsoluteFirstRowPosition() {
		return absoluteFirstRowPosition;
	}

	/**
	 * @return the {@link #executionTime}
	 */
	public Long getExecutionTime() {
		return executionTime;
	}

}
