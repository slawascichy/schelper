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

public interface _IResultInfo {

	/**
	 * @return numer pierwszej pozycji na stronie
	 */
	int getStartPosition();

	/**
	 * @return numer ostatniej pozycji na stronie
	 */
	int getEndPosition();

	/**
	 * @return rozmiar rezultatu
	 */
	int getResultSize();

	/**
	 * @return komunikat związany z rezultatem ({@link ResultMessage})
	 */
	ResultMessage getMessage();

	/**
	 * @return the hasMoreResultRows
	 */
	boolean hasMoreResultRows();

	/**
	 * @return ostatnia pozycja rezultatu
	 */
	Integer getLastRowPosition();

	/**
	 * @return pierwsza pozycja rezultatu
	 */
	int getFirstRowPosition();

	/**
	 * @return predefiniowana maksymalna liczba stron w rezultacie
	 */
	int getResultMaxPages();

	/**
	 * @return absolutny pierwszy numer wiersza w wyniku zapytania
	 */
	int getAbsoluteFirstRowPosition();

	/**
	 * @return czas wykonania zapytania (w milisekundach)
	 */
	Long getExecutionTime();
}