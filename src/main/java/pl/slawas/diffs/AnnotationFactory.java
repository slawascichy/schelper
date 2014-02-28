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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * 
 * AnnotationFactory - interfejs dla fabryki obslugujacej annotacje. Jest in
 * potrzebny do wstrzykniecia odpowiedniej annotacji do duplikatora
 * (Duplicator.class), ktory uniwersalnie przetwarza obiekty z polami
 * oznaczonymi roznymi annotacjami.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public interface AnnotationFactory {

	public static final String DEFAULT_REFERENCED_FIELD_NAME = "";

	public static final String DEFAULT_LABEL = "";

	/**
	 * Zwraca etykiete pola na potrzeby porownania pol roznych obiektow.
	 * 
	 * @param field
	 *           pole obiektu z annotacja
	 * @return etykieta
	 */
	String getLabel(Field field);

	/**
	 * Zwraca etykiete pola na potrzeby porownania pol roznych obiektow.
	 * 
	 * @param labelList
	 *           dynamiczna lista etykiet dla pol
	 * @param parentLabel
	 *           etykieta pola nadrzednego
	 * @param field
	 *           pole obiektu z annotacja
	 * @return etykieta pola
	 */
	String getLabel(
			Hashtable<String, String> labelList,
			String parentLabel,
			Field field);

	/**
	 * Zwraca nazwe pola w obiekcie definiujacym pole, ktora wskazuje na pole z
	 * unikalna wartosa charakteryzujaca obiekt znajdujacy sie w liscie
	 * (java.util.List) lub wektorze (java.util.Vector). Jezeli taki atrybut nie
	 * bedzie zdefiniowany, klasa sledzaca nie bedzie mogla poprawnie porownac
	 * list (wektorow) ze wzgledu na mozliwa ROZNA kolejnosc takich samych
	 * elementow.
	 * 
	 * @param field
	 *           pole obiektu z annotacja
	 * @return nazw pola charakteryzujacego unikalnosc obiektu
	 */
	String getReferencedFieldName(Field field);

	/**
	 * Zwraca klase annotacji, dla ktorej zdefiniowano bean-a spelniajacego ten
	 * interfejs.
	 * 
	 * @return klasa annotacji
	 */
	Class<? extends Annotation> getAnnotation();

}
