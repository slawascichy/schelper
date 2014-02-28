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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;

import pl.slawas.entities.NameValuePair;
import pl.slawas.entities.NameValuePairUtils;
import pl.slawas.helper.info.LibraryConfig;
import pl.slawas.helper.info.LibraryProperties;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * XMLNameValuePairUtils - klasa narzędziowa obsługująca typy spełniające
 * interface {@link NameValuePair}.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2.2.1 $
 * 
 */
public class XMLNameValuePairUtils {

	private static Logger logger = LoggerFactory.getLogger(XMLNameValuePairUtils.class);

	/**
	 * Domyślna nazwa elementu dokumentu, gdy nie zostanie podane DTD (
	 * {@link XMLDocumentTypeDefinition}, albo zostanie podane jako {@code null},
	 * podczas generacji dokumentu XML
	 * 
	 * @see #getXMLDocument(List)
	 * @see #getXMLDocument(List, XMLDocumentTypeDefinition)
	 * @see #getXMLDocument(List, String, XMLDocumentTypeDefinition)
	 */
	public static String DEFAULT_DOC_ELEMENT = "nameValuePairList";

	/**
	 * Metoda tworzaca element XML z podanego w parametrze obiektu
	 * 
	 * @param source
	 *           obiekt spełnaijący interface {@link NameValuePair}
	 * @return zbudowany element XML
	 */
	public static StringBuffer toXMLItem(NameValuePair source) {
		StringBuffer out = new StringBuffer();
		out.append("\n<item>");
		out.append("\n	<name><![CDATA[" + (source.getName() != null ? source.getName() : "")
				+ "]]></name>");
		out.append("\n	<value><![CDATA[" + (source.getValue() != null ? source.getValue() : "")
				+ "]]></value>");
		out.append("\n</item>");
		return out;
	}

	/**
	 * Metoda tworząca dokument XML dla listy pogrupowanych obiekow-par
	 * nazwa-wartość
	 * 
	 * @param valuesList
	 *           lista obiektow
	 * @return dokument XML z nadanym atrybutem id="default"
	 */
	public static StringBuffer getXMLDocument(
			List<? extends NameValuePair> valuesList) {
		return getXMLDocument(valuesList, null, null);
	}

	/**
	 * Metoda tworząca dokument XML dla listy pogrupowanych obiekow-par
	 * nazwa-wartość
	 * 
	 * @param valuesList
	 *           lista obiektow
	 * @param dtd
	 *           obiekt definiujący klauzule DTD
	 * @return dokument XML z nadanym atrybutem id="default"
	 */
	public static StringBuffer getXMLDocument(
			List<? extends NameValuePair> valuesList,
			XMLDocumentTypeDefinition dtd) {
		return getXMLDocument(valuesList, null, dtd);
	}

	/**
	 * Metoda tworząca dokument XML dla listy pogrupowanych obiekow-par
	 * nazwa-wartość
	 * 
	 * @param valuesList
	 *           lista obiektow
	 * @param xmlId
	 *           identyfikator dokumentu (atrybut id)
	 * @param dtd
	 *           obiekt definiujący klauzule DTD
	 * @return dokument XML
	 */
	public static StringBuffer getXMLDocument(
			List<? extends NameValuePair> valuesList,
			String xmlId,
			XMLDocumentTypeDefinition dtd) {
		StringBuffer out = new StringBuffer();

		if (valuesList != null && !valuesList.isEmpty()) {
			out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			String docElement = DEFAULT_DOC_ELEMENT;
			if (dtd != null && StringUtils.isNotBlank(dtd.toString())) {
				out.append("\n<!--\n" + dtd.toString() + "\n-->\n");
				if (StringUtils.isNotBlank(dtd.getDocElement())) {
					docElement = dtd.getDocElement();
				}
			}
			out.append("\n<!--"
					+ "\nXML created by Andro Helper Library version: "
					+ LibraryConfig.getInstance().get(LibraryProperties.HELPER_LIB_VERSION)
					+ "\nDate generation: " + Calendar.getInstance().getTime().toString()
					+ "\n-->");
			out.append("\n<" + docElement + " id=\"" + (xmlId != null ? xmlId : "default")
					+ "\">");
			out.append("\n<items>");
			for (NameValuePair value : valuesList) {
				if (value != null)
					out.append(value.toXMLItem());
			}
			out.append("\n</items>");
			out.append("\n</" + docElement + ">");
		}
		return out;
	}

	/**
	 * Metoda dekodująca elementy dokumentu XML do listy obiektów
	 * {@link NameValuePair}
	 * 
	 * @param items
	 *           element dokumentu XML
	 * @return lista obiektów {@link NameValuePair}
	 */
	public static List<? extends NameValuePair> readItems(Element items) {
		logger.trace("Czytam elementy node-a '<{}>'", items.getNodeName());
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		NodeList nodeLst = items.getElementsByTagName("item");
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node fstNode = nodeLst.item(s);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;
				/* odczytuję nazwę */
				NodeList nameElmntLst = fstElmnt.getElementsByTagName("name");
				Element nameNmElmnt = (Element) nameElmntLst.item(0);
				NodeList nameNm = nameNmElmnt.getChildNodes();
				String name = ((Node) nameNm.item(0)).getNodeValue();
				/* odczytuję wartość */
				NodeList valueElmntLst = fstElmnt.getElementsByTagName("value");
				Element valueNmElmnt = (Element) valueElmntLst.item(0);
				NodeList valueNm = valueNmElmnt.getChildNodes();
				String value = ((Node) valueNm.item(0)).getNodeValue();
				result.add(NameValuePairUtils.createNewInstance(
						name,
						value));
				logger.trace("[I] nazwa = '{}'; wartość = '{}'", new Object[]
					{ name, value });
			}
		}
		return result;
	}

	/**
	 * Transformacja dokumentu XML do listy obiektów {@link NameValuePair}
	 * 
	 * @param stream
	 *           dokument XML jako strumień danych wejściowych
	 * @return lista obiektów {@link NameValuePair}
	 * @throws XMLNameValuePairUtilsErrorException
	 */
	public static List<? extends NameValuePair> readDocument(InputStream stream)
			throws XMLNameValuePairUtilsErrorException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();
			String rootElement = doc.getDocumentElement().getNodeName();
			logger.debug("Root element {}", rootElement);
			NodeList itemsTag = doc.getElementsByTagName("items");
			Element itemsElement = (Element) itemsTag.item(0);
			return readItems(itemsElement);
		} catch (ParserConfigurationException e) {
			throw new XMLReadingObjectException(e);
		} catch (SAXException e) {
			throw new XMLReadingObjectException(e);
		} catch (IOException e) {
			throw new XMLReadingObjectException(e);
		}
	}

	/**
	 * Wyszukiwanie pojedynczego obiektu pary nazwa-wartosc w elemencie XML na
	 * podstwie unikalnej wartości
	 * 
	 * @param items
	 *           element {@code <items>}, w którym poszukiwany jest obiekt o
	 *           zadanej wartości
	 * @param searchValue
	 *           kryterium poszukiwanej wartości
	 * @return znaleziony obiekt. Jeżeli obiekt nie zostanie znaleziony zwracana
	 *         jest wartość {@code null}
	 */
	public static NameValuePair searchItemByUniqueValue(
			Element items,
			String searchValue) {
		logger.trace("Czytam elementy node-a '<{}>'", items.getNodeName());
		NameValuePair result = null;
		NodeList nodeLst = items.getElementsByTagName("item");
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node fstNode = nodeLst.item(s);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				/* odczytuję wartość */
				Element fstElmnt = (Element) fstNode;
				NodeList valueElmntLst = fstElmnt.getElementsByTagName("value");
				Element valueNmElmnt = (Element) valueElmntLst.item(0);
				NodeList valueNm = valueNmElmnt.getChildNodes();
				String value = ((Node) valueNm.item(0)).getNodeValue();
				if (searchValue.equals(value)) {
					/* odczytuję nazwę */
					NodeList nameElmntLst = fstElmnt.getElementsByTagName("name");
					Element nameNmElmnt = (Element) nameElmntLst.item(0);
					NodeList nameNm = nameNmElmnt.getChildNodes();
					String name = ((Node) nameNm.item(0)).getNodeValue();
					result = NameValuePairUtils.createNewInstance(
							name,
							value);
					logger.trace("[I] Znalazlem! nazwa = '{}'; wartosc = '{}'", new Object[]
						{ name, value });
					/* znalazlem wartość zatem przerywam pętlę wyszukiwania */
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Wyszukiwanie pojedynczego obiektu pary nazwa-wartosc w dokumencie XML
	 * (występującego jako strymień danych) na podstwie unikalnej wartości
	 * 
	 * @param stream
	 *           dokument XML jako strumień danych wejściowych
	 * @param searchValue
	 *           wyszukiwana wartość
	 * @return znaleziony obiekt {@link NameValuePair}, albo {@code null}, gdy
	 *         nie zostanie znaleziony.
	 * @throws XMLNameValuePairUtilsErrorException
	 */
	public static NameValuePair searchInDocumentUniqueValue(
			InputStream stream,
			String searchValue)
			throws XMLNameValuePairUtilsErrorException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();
			String rootElement = doc.getDocumentElement().getNodeName();
			logger.debug("Root element {}", rootElement);
			NodeList itemsTag = doc.getElementsByTagName("items");
			Element itemsElement = (Element) itemsTag.item(0);
			return searchItemByUniqueValue(itemsElement, searchValue);
		} catch (ParserConfigurationException e) {
			throw new XMLReadingObjectException(e);
		} catch (SAXException e) {
			throw new XMLReadingObjectException(e);
		} catch (IOException e) {
			throw new XMLReadingObjectException(e);
		}
	}

}
