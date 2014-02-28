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
package pl.slawas.entities;

import java.io.Serializable;

/**
 * 
 * NameValuePair - para wartości nazwa - wartość
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.3 $
 * 
 */
public interface NameValuePair extends Serializable {

	/**
	 * Pobieranie nazwy
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Ustawianie nazwy
	 * 
	 * @param name
	 *           nazwa do ustawienia
	 */
	public void setName(String name);

	/**
	 * Pobieranie wartości
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * Ustwaianie wartości
	 * 
	 * @param value
	 *           wartośc do ustawienia
	 */
	public void setValue(String value);

	/**
	 * Metoda tworzaca element XML z wartosci pol obiektu.
	 * 
	 * @return łańcuch znakowy elementu w postaci XML
	 */
	public String toXMLItem();

}