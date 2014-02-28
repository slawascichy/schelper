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

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Trace - annotacja do sledzenia zmian w obiektach dla wybranych pol. Annotacje
 * stawiamy przy polach obiektu, a pola musza miec odpowiedenie gettery i
 * settery poniewaz jest to podstawa dzialania sledzenia. Obiekt z takimi
 * annotacjami moze byc przetworzony przez metody statyczne znajdujace sie w
 * klasie {@link TraceCreator}
 * 
 * <pre>
 *   Examples:
 * 
 *   &#064;Trace
 *   private String childDescription;
 *   
 *   &#064;Trace(label = &quot;Nazwa&quot;)
 *   private String name;
 *   
 *   &#064;Trace(label = &quot;Identyfikator&quot;, isDynamic = true, fieldKey = &quot;id&quot;)
 *   private int id;
 * 
 *   &#064;Trace(referencedFieldName=&quot;id&quot;)
 *   private List&lt;AnnotationChildMock&gt; childList;
 * 
 * </pre>
 * 
 * 
 * @see pl.slawas.diffs.TraceCreator
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
	{ FIELD })
public @interface Trace {

	/**
	 * Etykieta zmian
	 * 
	 * @return wartosc etykiety
	 */
	String label() default TraceAnnotationFactory.DEFAULT_LABEL;

	/**
	 * Czy etykieta ma być budowana dynamicznie?
	 * 
	 * @return [true|false]
	 */
	boolean isDynamic() default false;

	/**
	 * Klucz w/g którego bedzie wybierana dynamiczna etykieta z tablicy haszowej
	 * 
	 * @see pl.slawas.diffs.TraceCreator#Tracker(java.util.Hashtable)
	 * @return nazwa klucza
	 */
	String labelKey() default TraceAnnotationFactory.DEFAULT_FIELD_NAME;

	/**
	 * Nazwa pola w obiekcie definiujacym pole, ktora wskazuje na pole z unikalna
	 * wartosa charakteryzujaca obiekt znajdujacy sie w liscie (java.util.List)
	 * lub wektorze (java.util.Vector). Jezeli taki atrybut nie bedzie
	 * zdefiniowany, klasa sledzaca nie bedzie mogla poprawnie porownac list
	 * (wektorow) ze wzgledu na mozliwa ROZNA kolejnosc takich samych elementow.
	 * </br> <font color="#dd0000">UWAGA! nie trzeba definiowac atrybutu jezeli
	 * obiekt pola ma uzyta annotacje javax.persistence.Id dla zdefiniowania
	 * unikalnego pola</font>
	 * 
	 * @return nazwa pola jednoznacznie identyfikujaca obiekt
	 */
	String referencedFieldName() default TraceAnnotationFactory.DEFAULT_FIELD_NAME;

}
