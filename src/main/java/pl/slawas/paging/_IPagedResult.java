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

import java.util.Collection;

/**
 * 
 * _IPagedResult - stronicowany wynik zapytania
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 * @param <Obj>
 *            klasa obiektu na liscie rezultatów
 */
public interface _IPagedResult<Obj> extends _IPagingInfo {

	/**
	 * Kolekcja wierszy na obecnej stronie
	 * 
	 * @return lista wierszy na stronie
	 */
	Collection<Obj> getResult();

	/**
	 * Kolekcja wierszy na zadanej stronie
	 * 
	 * 
	 * @param page
	 *            obiekt strony, której wyniki nas integersują
	 * @return lista wierszy na zadanej stronie
	 */
	public Collection<Obj> getResult(Page page);

	/**
	 * Zwraca pierwszy wiersz na stronie
	 * 
	 * @return wiersz na stronie
	 */
	public Obj getUniqueResult();

	/**
	 * @return zwraca zapytanie na podstawie, którego została utworzona strona
	 *         wyników.
	 */
	public _IPagedQuery<Obj> getQuery();

}