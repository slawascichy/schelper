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

import javax.persistence.Id;

/**
 * 
 * IDCantGetValueOfIdFieldException
 * <p>
 * Klasa informaująca o wewnetrznym błędzie podczas czytania obiektu encji. Pole
 * z annotacją {@link Id} zostało odnalezione, ale jest ono takiego typu, że nie
 * mozna go odkodować (jest jakimś typem złozonym).
 * </p>
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class IDCantGetValueOfIdFieldException extends IDUtilsErrorException {

	private static final long serialVersionUID = 1602501396214379967L;

	/**
	 * 
	 */
	public IDCantGetValueOfIdFieldException() {
		super("Can't get value of field with @Id annotation.");
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IDCantGetValueOfIdFieldException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public IDCantGetValueOfIdFieldException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IDCantGetValueOfIdFieldException(Throwable cause) {
		super(cause);
	}



}
