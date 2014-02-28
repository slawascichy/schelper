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
package pl.slawas.test.diffs;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import pl.slawas.diffs.Property;
import pl.slawas.diffs.Trace;

/**
 * 
 * TracerParentMock - obiekt rodzicielski do testowania sledzenia zmian w
 * obiektach. Obiekt bedzie sie zmienac w trakcie rozbudowy funkcjonalnosci
 * obieku do sledzenia o kolejne obiekty, głownie kolekcje.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class DiffMockParent {

	/**
	 * atrybut do testowania sledzenia typu prostego ze statyczna etykieta zmian
	 */
	@Property
	@Trace(label = "Nazwa")
	private String name;

	/**
	 * atrybut do testowania sledzenia typu prostego z niezdefiniowana etykieta
	 * zmian
	 */
	@Property
	@Trace
	private String description;

	/**
	 * atrybut do testowania sledzenia typu Listy
	 * 
	 * @see java.util.List
	 */
	@Property(referencedFieldName = "id")
	@Trace(referencedFieldName = "id", label = "Lista pracownikow")
	private List<DiffMockChild> childList;

	/**
	 * atrybut do testowania sledzenia typu Hashtable
	 * 
	 * @see java.util.Hashtable
	 */
	@Property
	@Trace(label = "Hashtable pracownikow")
	private Hashtable<String, DiffMockChild> childHashtable;

	@Property
	@Trace(label = "Hashtable z integer")
	private Hashtable<Integer, DiffMockChild> childHashtableInt;

	/**
	 * atrybut do testowania sledzenia typu Vector
	 * 
	 * @see java.util.Vector
	 */
	@Property(referencedFieldName = "id")
	@Trace(referencedFieldName = "id", label = "Vector pracownikow")
	private Vector<DiffMockChild> childVector;

	/**
	 * atrybut do testowania sledzenia typu array obiektow
	 * 
	 */
	@Property
	@Trace(referencedFieldName = "id", label = "Array pracownikow")
	private DiffMockChild[] childArray;

	/**
	 * atrybut do testowania sledzenia array typow prostych (String)
	 * 
	 */
	@Property
	@Trace(label = "Array string")
	private String[] stringArray;

	/**
	 * atrybut do testowania sledzenia array typow prostych (int)
	 * 
	 */
	@Property
	@Trace(label = "Array int")
	private int[] intArray;

	/**
	 * atrybut do testowania sledzenia typu prostego wraz z dynamicznym
	 * etykietowaniem zmian.
	 */
	@Property
	@Trace(label = "Identyfikator", isDynamic = true, labelKey = "id")
	private double id;

	@Property
	@Trace(label = "Pracownik miesiaca")
	private DiffMockChild child;

	@Property
	@Trace(label = "Data utworzenia")
	private Calendar createDate;
	
	
	/**
	 * Atrybut sprawdzajacy poprawnosc kopiowania obiektow. Nie jest on oznaczony
	 * zadna annotacja, zatem powinien byc pomijany podczas kopiowania i powinien
	 * zachowac swoja stara wartosc.
	 */
	private String valueWithoutProperty;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the childList
	 */
	public List<DiffMockChild> getChildList() {
		return childList;
	}

	/**
	 * @param childList
	 *           the childList to set
	 */
	public void setChildList(List<DiffMockChild> childList) {
		this.childList = childList;
	}

	/**
	 * @return the childHashtable
	 */
	public Hashtable<String, DiffMockChild> getChildHashtable() {
		return childHashtable;
	}

	/**
	 * @param childHashtable
	 *           the childHashtable to set
	 */
	public void setChildHashtable(Hashtable<String, DiffMockChild> childHashtable) {
		this.childHashtable = childHashtable;
	}

	/**
	 * @return the childVector
	 */
	public Vector<DiffMockChild> getChildVector() {
		return childVector;
	}

	/**
	 * @param childVector
	 *           the childVector to set
	 */
	public void setChildVector(Vector<DiffMockChild> childVector) {
		this.childVector = childVector;
	}

	/**
	 * @return the id
	 */
	public double getId() {
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(double id) {
		this.id = id;
	}

	/**
	 * @return the childArray
	 */
	public DiffMockChild[] getChildArray() {
		return childArray;
	}

	/**
	 * @param childArray
	 *           the childArray to set
	 */
	public void setChildArray(DiffMockChild[] childArray) {
		this.childArray = childArray;
	}

	/**
	 * @return the stringArray
	 */
	public String[] getStringArray() {
		return stringArray;
	}

	/**
	 * @param stringArray
	 *           the stringArray to set
	 */
	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}

	/**
	 * @return the intArray
	 */
	public int[] getIntArray() {
		return intArray;
	}

	/**
	 * @param intArray
	 *           the intArray to set
	 */
	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	/**
	 * @return the child
	 */
	public DiffMockChild getChild() {
		return child;
	}

	/**
	 * @param child
	 *           the child to set
	 */
	public void setChild(DiffMockChild child) {
		this.child = child;
	}

	/**
	 * @return the childHashtableInt
	 */
	public Hashtable<Integer, DiffMockChild> getChildHashtableInt() {
		return childHashtableInt;
	}

	/**
	 * @param childHashtableInt
	 *           the childHashtableInt to set
	 */
	public void setChildHashtableInt(Hashtable<Integer, DiffMockChild> childHashtableInt) {
		this.childHashtableInt = childHashtableInt;
	}

	/**
	 * @return the valueWithoutProperty
	 */
	public String getValueWithoutProperty() {
		return valueWithoutProperty;
	}

	/**
	 * @param valueWithoutProperty the valueWithoutProperty to set
	 */
	public void setValueWithoutProperty(String valueWithoutProperty) {
		this.valueWithoutProperty = valueWithoutProperty;
	}

	/**
	 * @return the createDate
	 */
	public Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

}
