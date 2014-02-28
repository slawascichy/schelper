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
 * EnumerationSample przykładowy obiekt z użyciem enumaretora rozszerzonego
 * klasą abstrakcyjneą {@link EnumerationSupport}. Szczególną uwagę należy
 * zwrócić na implementację metody pobrania enumeratora
 * {@link #getHistoryOfWordChanging()}, w implementacji której, przed zwróceniem
 * enumeratora, następuje resetowanie licznika pobrań.
 * 
 * <p>Przykład kodu z użyciem klasy:</p>
 * <code>
 * <pre>
	String[] expectedValues = new String[]
		{
				"krok 1",
				"krok 2",
				"krok 3",
				"krok 4",
				"krok 5",
				"krok 6"
			};

	EnumerationSample sample = new EnumerationSample();
	for (int i = 0; i < expectedValues.length; i++) {
		sample.setWord(expectedValues[i]);
	}

	Enumeration<String> e = sample.getHistoryOfWordChanging();
	int i = 0;
	while (e.hasMoreElements()) {
		String actual = e.nextElement();
		assertEquals("Blad wartości enumeratora", expectedValues[i], actual);
		log.debug(actual);
		i++;
	}
 * </pre>
 * </code>
 * 
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
@SuppressWarnings("serial")
public class EnumerationSample implements Serializable {

	private String word;

	private final Enumeration<String> historyOfWordChanging = new HistoryEnumeration();

	/**
	 * Przykładowa metoda pobrania enumeratora, z metodą użyciem metody
	 * {@link HistoryEnumeration#reset()} - metoda nie zalecana.
	 * 
	 * @return enumerator
	 */
	@SuppressWarnings("deprecation")
	public Enumeration<String> getHistoryOfWordChangingBadImplementation() {
		((HistoryEnumeration) historyOfWordChanging).reset();
		return historyOfWordChanging;
	}

	/**
	 * Przykładowa metoda pobrania enumeratora, z metodą użyciem metody
	 * {@link HistoryEnumeration#getNewInstance()} - metoda zalecana.
	 * 
	 * @return enumerator
	 */
	public Enumeration<String> getHistoryOfWordChanging() {
		return ((HistoryEnumeration) historyOfWordChanging).getNewInstance();
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Ustawianie wartości pola {@link #word}. Metoda zawiera przykładową
	 * implementację dodawania elementów do enumeratora.
	 * 
	 * @param word
	 *           the word to set
	 */
	public void setWord(String word) {
		if ((this.word != null && word != null && !word.equals(this.word))) {
			((HistoryEnumeration) historyOfWordChanging).add(this.word);
		}
		this.word = word;
	}

	/**
	 * 
	 * HistoryEnumeration prywatna implementacja klasy enumeratora.
	 * 
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1 $
	 * 
	 */
	private class HistoryEnumeration extends EnumerationSupport<String> {

		public HistoryEnumeration() {
			super();
		}

		private HistoryEnumeration(Vector<String> elements) {
			super(elements);
		}

		public HistoryEnumeration getNewInstance() {
			return new HistoryEnumeration(this.elements);
		}
	}

}
