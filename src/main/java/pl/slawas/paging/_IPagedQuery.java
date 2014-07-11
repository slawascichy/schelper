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
 * _IPagedQuery stronicowane zapytania
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 * @param <Obj>
 *            klasa obiektu na liscie rezultatów
 */
public interface _IPagedQuery<Obj> extends Serializable {

	/**
	 * @param page
	 *            the page to set
	 */
	void setPage(Page page);

	/**
	 * Zwraca wynik zapytania na stronie zdefiniowanej metodą
	 * {@link #setPage(Page)}. Jeżeli strona nie jest ustawiona zostanie
	 * zwrócony wynik strony o numerze {@link Page#DEFAULT_PAGE_NR}. Mozna tez
	 * uzywać metody {@link #setFirstResult(int)} do ustwiwania numeru strony
	 * ale takie rozwiązanie nie jest zalecane.
	 * 
	 * @return zwraca stronicowany wynik
	 */
	_IPagedResult<Obj> getPagedResult();

	/**
	 * Zwraca stronicowany wynik zapytania na zadanej stronie
	 * 
	 * @param page
	 *            strona wyników
	 * @return winik zapytania na danej stronie
	 */
	_IPagedResult<Obj> getPagedResult(Page page);

}
