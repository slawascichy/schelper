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
package pl.slawas.helpers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Klasa przesyłająca listę tylko do odczytu. Idea polega na tym zeby, nikt inny
 * nie mogl zmodyfikowac danej listy, oprocz singletona (klasy) nim
 * zarzadajacym. Klasa spełnia integfejs java.util.List. 
 * Przykład na podstawie fragmentu zrodla testu:
 * <code>
 * public class ImmutableListTest extends TestCase {
	
	@SuppressWarnings("unused")
	final private static Logger log = LoggerFactory.getLogger(ImmutableListTest.class);
	
	private ImmutableList<String> propertyList;

	public ImmutableListTest() {

		this.propertyList = new ImmutableList<String>();
		String currVal;
		
		currVal = new String("testValue1");
		this.propertyList.pAdd(currVal);

		currVal = new String("testValue2");
		this.propertyList.pAdd(currVal);

		currVal = new String("testValue3");
		this.propertyList.pAdd(currVal);

		currVal = new String("testValue4");
		this.propertyList.pAdd(currVal);

		currVal = new String("testValue5");
		this.propertyList.pAdd(currVal);
	}
	
	private List<String> getPropertyList() {
		return this.propertyList;
	}
	
	@Test
	public void testReadImmutableList() {
		
		List<String> testList = getPropertyList();
		int i = 0;
		for (String testVal : testList) {
			log.debug("Value[{}]: {}", new Object[]{i, testVal});
			i++;
		}
		
		Assert.assertTrue(5 == i);
		
	}

	@Test
	public void testAddImmutableList() {
		try {
			List<String> testList = getPropertyList();
			testList.add("Test");
			Assert.fail("Udalo sie dodac obiekt do listy");
		} catch (UnsupportedOperationException e) {
			log.debug("{}", e.getMessage());
			Assert.assertTrue(true);
		}
	}
	
}

 * </code>
 * @author slawas
 * 
 * @param <Obj>
 */
public class ImmutableList<Obj> extends ArrayList<Obj> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6731388208456639978L;

	public ImmutableList() {
		super();
	}

	@Override
	public boolean add(Obj o) {
		throw new UnsupportedOperationException("List is read only");
	}

	@Override
	public void add(int index, Obj element) {
		throw new UnsupportedOperationException("List is read only");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("List is read only");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("List is read only");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("List is read only");
	}

	@Override
	public Obj remove(int index) {
		throw new UnsupportedOperationException("List is read only");
	}

	@Override
	public Obj set(int index, Obj element) {
		throw new UnsupportedOperationException("List is read only");
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param o
	 * @return
	 */
	public boolean pAdd(Obj o) {
		return super.add(o);
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param index
	 * @param element
	 */
	public void pAdd(int index, Obj element) {
		super.add(index, element);
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param c
	 * @return
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean pAddAll(Collection c) {
		return super.addAll(c);
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param index
	 * @param c
	 * @return
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean pAddAll(int index, Collection c) {
		return super.addAll(index, c);
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 */
	public void pClear() {
		super.clear();
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param index
	 * @return
	 */
	public Obj pRemove(int index) {
		return super.remove(index);
	}

	/**
	 * Metoda pozwalająca rodzicowi zmienic zawartość listy.
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	public Obj pSet(int index, Obj element) {
		return super.set(index, element);
	}

}
