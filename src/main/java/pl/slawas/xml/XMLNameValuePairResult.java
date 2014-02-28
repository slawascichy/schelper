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

import java.io.Serializable;
import java.util.List;

import pl.slawas.entities.NameValuePair;

/**
 * 
 * XMLNameValuePairResult wynik z obiektem {@link NameValuePair}, moze być
 * wykorzystywany do konwersji wyniku zapytania.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 * @param <NVPObj>
 */
public class XMLNameValuePairResult<NVPObj extends NameValuePair> implements
		_IXMLNameValuePairResult<NVPObj>,
		Serializable
{

	private static final long serialVersionUID = 4424925326877539793L;

	private String id;

	private List<NVPObj> result;

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.entities._IXMLNameValuePairResult#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.wp.ares.core.entities._IXMLNameValuePairResult#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.entities._IXMLNameValuePairResult#getResult()
	 */
	public List<NVPObj> getResult() {
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.wp.ares.core.entities._IXMLNameValuePairResult#setResult(java.util.
	 * List)
	 */
	public void setResult(List<NVPObj> result) {
		this.result = result;
	}

}
