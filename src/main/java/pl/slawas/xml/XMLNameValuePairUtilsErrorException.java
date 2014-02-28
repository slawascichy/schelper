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

import pl.slawas.entities.NameValuePair;

/**
 * 
 * XMLNameValuePairUtilsErrorException - błąd operacji na obiecie {@link NameValuePair} 
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class XMLNameValuePairUtilsErrorException extends Throwable {

	private static final long serialVersionUID = -6818199532152800874L;

	/**
	 * 
	 */
	protected XMLNameValuePairUtilsErrorException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	protected XMLNameValuePairUtilsErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	protected XMLNameValuePairUtilsErrorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public XMLNameValuePairUtilsErrorException(Throwable cause) {
		super(cause);
	}

}
