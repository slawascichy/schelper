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

import javax.xml.transform.OutputKeys;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * XMLDocumentTypeDefinition - wparcie definicji DTD podczas używania DOM API.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class XMLDocumentTypeDefinition {

	/**
	 * 
	 * DTDType - typ dokumentu DTD
	 * 
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1 $
	 * 
	 */
	public enum DTDType {
		/**
		 * dokument systemowy
		 */
		SYSTEM,
		/**
		 * dokument publiczny
		 */
		PUBLIC;

		/**
		 * DTD podczas używania DOM API zwraca odpowiednią wartość klucza.
		 * 
		 * @see OutputKeys
		 * 
		 * @return możliwe wartośi: {@link OutputKeys#DOCTYPE_PUBLIC} albo
		 *         {@link OutputKeys#DOCTYPE_SYSTEM}
		 */
		public String getDocTypeKey() {

			switch (this) {
			case SYSTEM:
				return OutputKeys.DOCTYPE_SYSTEM;
			case PUBLIC:
				return OutputKeys.DOCTYPE_PUBLIC;
			}
			return null;
		}
	}

	private final DTDType docType;

	private final String docName;

	private final String docElement;

	private String comment = "-//Sci Software Sławomir Cichy//DTD document//PL";

	/**
	 * Konstruktor który domyślnie ustawi wartość pola {@link #docType} na
	 * {@link DTDType#PUBLIC}
	 * 
	 * @param docName
	 *            nazwa dokumentu np.
	 *            {@code  "http://scisoftware.pl/dhtmlselect/dtd/dhtmlselect_grouped_list_1_0.dtd"}
	 * @param docElement
	 *            nazwa elementu którego dotuczy DTD
	 * 
	 */
	public XMLDocumentTypeDefinition(String docName, String docElement) {
		this.docType = DTDType.PUBLIC;
		this.docName = docName;
		this.docElement = docElement;
	}

	/**
	 * Konstruktor, który ustawi zarówno typ dokumentu jaki i jego nazwę
	 * 
	 * @param docType
	 *            możliwe wartośi: {@link DTDType#PUBLIC} albo
	 *            {@link DTDType#SYSTEM}
	 * @param docName
	 *            nazwa dokumentu np.
	 *            {@code  "http://scisoftware.pl/dhtmlselect/dtd/dhtmlselect_grouped_list_1_0.dtd"}
	 * @param docElement
	 *            nazwa elementu którego dotuczy DTD
	 */
	public XMLDocumentTypeDefinition(DTDType docType, String docName,
			String docElement) {
		this.docType = docType;
		this.docName = docName;
		this.docElement = docElement;
	}

	/**
	 * @return the docType
	 */
	public DTDType getDocType() {
		return docType;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	public String toString() {
		return "<!DOCTYPE "
				+ this.docElement
				+ " "
				+ this.docType.toString()
				+ " "
				+ (StringUtils.isNotBlank(this.comment) ? "\"" + this.comment
						+ "\" " : "") + "\"" + this.docName + "\">";
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Ustwienie komentarza
	 * 
	 * @param comment
	 * @return zwraca zmieniony dokument
	 */
	public XMLDocumentTypeDefinition setComment(String comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * @return the docElement
	 */
	public String getDocElement() {
		return docElement;
	}
}
