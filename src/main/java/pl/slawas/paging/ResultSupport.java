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
 * ResultSupport klasa abstrakcyjna predefiniująca wynik stronicowanego zapytania 
 *
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 *
 * @param <Obj>
 */
@SuppressWarnings("serial")
public abstract class ResultSupport<Obj> implements Serializable, _IResultInfo {

	private final int startPosition;

	private final int endPosition;

	private final int resultSize;

	private final Collection<Obj> result;

	private final ResultMessage message;

	private boolean hasMoreResultRows = false;

	private final Integer lastRowPosition;

	private final Integer firstRowPosition;

	private final int resultMaxPages;

	private final int absoluteFirstRowPosition;

	/**
	 * @param startPosition
	 *           numer pierwszej pozycji na stronie
	 * @param endPosition
	 *           numer ostatniej pozycji na stronie
	 * @param firstRowPosition
	 *           pierwsza pozycja rezultatu
	 * @param resultSize
	 *           rozmiar rezultatu
	 * @param lastRowPosition
	 *           ostatnia pozycja rezultatu
	 * @param result
	 *           lista wyników rezultatu
	 * @param message
	 *           komunikat związany z rezultatem ({@link ResultMessage})
	 * @param absoluteFirstRowPosition
	 *           absolutny pierwszy numer wiersza w wyniku zapytania
	 * @param resultMaxPages
	 *           predefiniowana maksymalna liczba stron w rezultacie.
	 */
	protected ResultSupport(
			int startPosition,
			int endPosition,
			Integer firstRowPosition,
			int resultSize,
			Integer lastRowPosition,
			Collection<Obj> result,
			ResultMessage message,
			int absoluteFirstRowPosition,
			int resultMaxPages
			) {
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
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getStartPosition()
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getEndPosition()
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getResultSize()
	 */
	public int getResultSize() {
		return resultSize;
	}

	/**
	 * @return the result
	 */
	public Collection<Obj> getResult() {
		return result;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getMessage()
	 */
	public ResultMessage getMessage() {
		return message;
	}

	public Obj getUniqueResult() {
		if (result != null && !result.isEmpty()) {
			return result.iterator().next();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#hasMoreResultRows()
	 */
	public boolean hasMoreResultRows() {
		return hasMoreResultRows;
	}

	/**
	 * @param hasMoreResultRows
	 *           the hasMoreResultRows to set
	 */
	public void setHasMoreResultRows(boolean hasMoreResultRows) {
		this.hasMoreResultRows = hasMoreResultRows;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getLastRowPosition()
	 */
	public Integer getLastRowPosition() {
		return lastRowPosition;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getFirstRowPosition()
	 */
	public int getFirstRowPosition() {
		return firstRowPosition;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getResultMaxPages()
	 */
	public int getResultMaxPages() {
		return resultMaxPages;
	}

	/* (non-Javadoc)
	 * @see pl.wp.andro.paging._IResultInfo#getAbsoluteFirstRowPosition()
	 */
	public int getAbsoluteFirstRowPosition() {
		return absoluteFirstRowPosition;
	}

}
