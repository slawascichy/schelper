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
package pl.slawas.support;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 
 * EnumerationSupport - anstrakcyjna implementacjia enumeracji. Nieraz potrzeba
 * zaimplementowania enumeracji elemntów jakiejś listy, ale nie znalazłem
 * nigdzie implementacji enumeratora. Może z tego powodu, że wymaga on zawsze
 * dodatkowego wsparcia ze strony obiektu, który przesyła dany obiekt
 * enumeracji, chociażby dlatego, że metoda gettera (pobrania) powinna zawsze
 * resetować licznik {@link #getElementCounter}. Moja propozycja jest taka, że
 * jeżeli juz budujemy obiekt, który bedzie miał metodę pobierającą enumerator,
 * to niech posiada on prywatną klasę, która będzie rozszeżona niniejszą
 * abstrakcją. Przykład implementacji zaprezentowałem w klasie
 * {@link EnumerationSample}.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.5.2.1 $
 * 
 * @see EnumerationSample
 * 
 * @param <Obj>
 *            klasa, której elementy będą przechowywane w kolekcji enumeratora
 */
@SuppressWarnings("serial")
public abstract class EnumerationSupport<Obj> implements Enumeration<Obj>,
		Serializable {

	/**
	 * lista elementów
	 */
	protected transient final Vector<Obj> elements;

	/**
	 * licznik elementów pobranych metodą {@link #nextElement()}, po to aby za
	 * każdym razem wywołania tej metody wydawany był następny element.
	 */
	private int getElementCounter = 0;

	/**
	 * Stała definiująca ile razy można odwołać się do metody
	 * {@link #hasMoreElements()} bez pobrania elementu za pomocą metody
	 * {@link #nextElement()}. Zabezpieczenie przed błędem pętli nieskończonej w
	 * postaci kodu:
	 * 
	 * <pre>
	 * Enumeration&lt;Page&gt; allPages = pagedResult.getAllPages();
	 * int i = 0;
	 * while (allPages.hasMoreElements()) {
	 * 	logger.info(&quot;\nProba nr {}&quot;, i);
	 * 	i++;
	 * }
	 * 
	 * </pre>
	 */
	public static int OVERFLOW = 1000;

	/**
	 * licznik zliczajacy wywolania metody {@link #hasMoreElements()}. Licznik
	 * ten jest zerowany w momencie wywołania metody {@link #nextElement()}.
	 */
	private int overFlowCounter = 0;

	/**
	 * Dodawanie elementów do enumeratora
	 * 
	 * @param element
	 * @return własnie modyfikowany enumerator
	 */
	public EnumerationSupport<Obj> add(Obj element) {
		elements.add(element);
		return this;
	}

	/**
	 * Resetowanie licznika pobrań elementów podczas wywołania getter-a w
	 * obiekcie właścicela. Przykład użycia
	 * {@link EnumerationSample#getHistory()}, ale metoda nie powina być
	 * używana, ze wzgledu na możliwość nieprawidłowego działania pobierania
	 * kolejnych elementów ({@link #nextElement()}) w przypadku rywalizacji o
	 * dostęp do obiektu. Bardziej prawidłowym rozwiązaniem jest tworzenie
	 * kolejnych instancji enumeratorów, z tą samą listą elementów. Sprowadza
	 * się to do prawidłowej implemetacji metody {@link #getNewInstance()}.
	 * 
	 * @see #getNewInstance()
	 */
	@Deprecated
	public void reset() {
		synchronized (this) {
			getElementCounter = 0;
		}
	}

	/**
	 * Sprawdzenie czy są jakieś elementy
	 */
	public boolean hasMoreElements() {
		synchronized (this) {
			if (overFlowCounter > OVERFLOW) {
				throw new EnumerationException(
						"Nieprawidlowe uzycie enumeratora. "
								+ "Przekroczono granice "
								+ OVERFLOW
								+ " wywolan metody Enumeration<Obj>.hasMoreElements() bez"
								+ " pobierania elementow co prowadzi do nieskonczonej petli. "
								+ " Pamietaj uzywaj sekwencji wywolania metod:"
								+ "\n 1) boolean Enumeration<Obj>.hasMoreElements() // sprawdzenie "
								+ "\n 2) <Obj> Enumeration<Obj>.nextElement() // pobranie");
			}
			overFlowCounter++;
			return (!this.elements.isEmpty() && this.elements.size() > getElementCounter);
		}
	}

	/**
	 * Pobranie następnego elementu.
	 */
	public Obj nextElement() {
		synchronized (this) {
			/*
			 * zerowanie licznika przekroczenia liczby wywołań metody
			 * sprawdzającej
			 */
			overFlowCounter = 0;
			if (!this.elements.isEmpty()
					&& this.elements.size() > getElementCounter) {
				Obj element = this.elements.get(getElementCounter);
				getElementCounter++;
				return element;
			} else
				return null;
		}
	}

	public EnumerationSupport() {
		elements = new Vector<Obj>();
	}

	protected EnumerationSupport(Vector<Obj> elements) {
		this.elements = elements;
	}

	/**
	 * Używanie metody resetowania licznika wiąże się z ryzykiem, że jeżeli
	 * enumerator będzie singletonem, albo stateless-em, albo innym obiektem
	 * keszowanym, dostępnym w trybie rywalizacji o dostęp do niego, to
	 * konkurencja spowoduje niewłaściwe działanie metody {@link #nextElement()}
	 * . Dlatego wywołanie enumeratora powinno się odbyć za pomocą wywołania tej
	 * metody, która wywoła konstruktor {@link #EnumerationSupport(Vector)}, tym
	 * samym przekazując listę elementów do nowej instancji enumeratora.
	 * 
	 * @return nowa instancja enumeratora
	 */
	public abstract EnumerationSupport<Obj> getNewInstance();

}
