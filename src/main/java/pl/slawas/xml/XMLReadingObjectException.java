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
package pl.slawas.xml;



/**
 * 
 * XMLReadingObjectException
 * <p>
 * Klasa informaująca o wewnetrznym błędzie podczas czytania obiektu XML.
 * </p>
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class XMLReadingObjectException extends XMLNameValuePairUtilsErrorException {

	private static final long serialVersionUID = 1602501396214379967L;

	/**
	 * 
	 */
	public XMLReadingObjectException() {
		super("Can't read object");
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XMLReadingObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public XMLReadingObjectException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public XMLReadingObjectException(Throwable cause) {
		super(cause);
	}


}
