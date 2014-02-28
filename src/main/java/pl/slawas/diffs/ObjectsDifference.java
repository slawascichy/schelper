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
package pl.slawas.diffs;

import java.lang.reflect.Type;

/**
 * 
 * TraceDifference - obiekt prezentujacy roznice pomiedzy poszczegolnymi
 * sledzonymi polami dwoch obiektow
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class ObjectsDifference {

	/**
	 * etykieta zmiany
	 */
	private String label;

	/**
	 * przy zachowaniu zasady ze w targecie umieszczamy nowe wartosci to pole to
	 * przezentuje zmieniona nowa wartosc
	 */
	private Object targetValue;

	/**
	 * przy zachowaniu zasady ze w source umieszczamy stare wartosci to pole to
	 * przezentuje stara wartosc
	 */
	private Object sourceValue;

	/**
	 * Typ zmienianego pola
	 */
	private Type type;

	public ObjectsDifference(
			String label,
			Object targetValue,
			Object sourceValue,
			Type type) {

		this.label = label;
		this.targetValue = sourceValue;
		this.sourceValue = targetValue;
		this.type = type;

	}

	/**
	 * @return etykieta nazywajaca to co zostalo zmienione
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the targetValue
	 */
	public Object getTargetValue() {
		return targetValue;
	}

	/**
	 * @return the sourceValue
	 */
	public Object getSourceValue() {
		return sourceValue;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

}
