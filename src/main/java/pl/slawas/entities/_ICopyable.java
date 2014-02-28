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


/**
 * 
 * _ICopyable - interfejs charakteryzujacy obiekt "kopiowalny".
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 * @param <Obj>
 *           obiekt spełniający dany interfejs
 */
public interface _ICopyable<Obj> {

	/**
	 * Metoda wykonująca kopiowanie wartości poszczgólnych pól, z obiektu tej
	 * samej klasy do aktualnie modyfikowanego obiektu.
	 * <p>
	 * Przykładowa implementacja:
	 * </p>
	 * <pre>
	 * public TEntity copy(TEntity source) {
	 * 	this.id = source.getId();
	 * 	this.name = source.getName();
	 * 	this.date = source.getDate();
	 * 	this.price = source.getPrice();
	 * 	this.user = source.getUser();
	 * 	return this;
	 * }
	 * </pre>
	 * <p>
	 * Przykład użycia (pseudo-kod):
	 * </p>
	 * <pre>
	 * ...
	 *  Class&lt;Obj> target = Class&lt;Obj>.newInstance();
	 *  // wstrzykiwanie już wartości istniejącego obiektu o nazwie source
	 *  target = target.copy(source);
	 *  // albo po prostu:
	 *  target.copy(source);
	 * ...
	 * </pre>
	 * 
	 * @param source
	 *           źródło, klasa, której informacje zostaną skopiowane do
	 *           aktualnego obiektu
	 * @return modyfikowany obiekt
	 */
	public Obj copy(Obj source);

	/**
	 * Metoda tworząca nową kopie aktualnego obiektu.
	 * <p>
	 * Przykładowa implementacja:
	 * </p>
	 * <pre>
	 * public TEntity copy() {
	 * 	TEntity newCopy = new TEntity();
	 * 	return newCopy.copy(this);
	 * }
	 * </pre>
	 * <p>
	 * Przykład użycia (pseudo-kod):
	 * </p>
	 * <pre>
	 * ...
	 *  // kopiowanie już istniejącego obiektu o nazwie source
	 *  Class&lt;Obj&gt; target = source.copy();
	 * ...
	 * </pre>
	 * 
	 * @return nowy obiekt, kopia aktualnego oniektu.
	 */
	public Obj copy();

	/**
	 * Metoda wykonująca kopiowanie wartości poszczgólnych pól, z aktualnego
	 * obiektu do obiektu docelowego.
	 * <p>
	 * Przykładowa implementacja:
	 * </p>
	 * <pre>
	 * public void copyTo(TEntity target) {
	 * 	target.setDate(this.date);
	 * 	target.setId(this.id);
	 * 	target.setName(this.name);
	 * 	target.setPrice(this.price);
	 * 	target.setUser(this.user);
	 * }
	 * </pre>
	 * <p>
	 * Przykład użycia (pseudo-kod):
	 * </p>
	 * <pre>
	 * ...
	 *  Class&lt;Obj> target = Class&lt;Obj>.newInstance();
	 *  // wstrzykiwanie wartości istniejącego obiektu o nazwie source do obiektu target
	 *  source.copyTo(target);
	 * ...
	 * </pre>
	 * 
	 * @param target
	 *           obiekt, do którego mają zostac skopiowane wartości aktualnego
	 *           oniektu.
	 */
	public void copyTo(Obj target);

}
