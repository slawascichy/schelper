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
 * Property - annotacja za pomoca ktorej pole obiektu moze zostac przetworzone
 * do elementu java.util.Properties. Do przetworzenia sluzy metoda w klasie
 * pl.wp.ares.core.entities.PropertyCreator. Annotacje stawiamy przy polach
 * obiektu, a pola musza miec odpowiedenie gettery i settery poniewaz jest to
 * podstawa dzialania sledzenia.
 * 
 * <pre>
 *   Example 1:
 * 
 *   &#064;Property
 *   private String childDescription;
 *   
 *   Example 2:
 * 
 *   &#064;Property(key = &quot;nazwa&quot;)
 *   private String name;
 * 
 * </pre>
 * 
 * W pierwszym przykładzie zostanie utworzona własność z kluczem (key) o nazwie
 * pola czyli 'childDescription', w drugim z kluczem 'nazwa'.
 * 
 * <pre>
 *   Example 3:
 * 
 *   &#064;Property(referencedFieldName=&quot;id&quot;)
 *   private List&lt;AnnotationChildMock&gt; childList;
 * 
 * </pre>
 * 
 * W trzecim przykladzie pokazane zostalo jak nalezy definowac annotacje dla
 * obiektow list. Aby prawidlowo lista zostala zamieniona na properties kreator
 * powinien znac nazwe pola, ktore charakteryzuje obiekt (sprawia ze any obiekt
 * jest unikalny) na liscie. Jezeli atrybut referencedFieldName nie zostanie
 * zdefiniowany, kreator sprobuje znalezc pole z annotacja javax.persistence.Id
 * obiektu zadeklarowanego w liscie. Jezeli nadal nie bedzie wstanie okreslic
 * pola unikalnego wtedy wypadku dobrany bedzie indeks listy.
 * 
 * @see javax.persistence.Id
 * @see pl.slawas.diffs.PropertyCreator#toProperties(Object)
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
	{ FIELD })
public @interface Property  {

	/**
	 * Dostosowana nazwa właściwości generowana dla danego pola.
	 * 
	 * @return nazwa własciwości
	 */
	String key() default PropertyAnnotationFactory.DEFAULT_KEY;

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
	String referencedFieldName() default PropertyAnnotationFactory.DEFAULT_REFERENCED_FIELD_NAME;

}
