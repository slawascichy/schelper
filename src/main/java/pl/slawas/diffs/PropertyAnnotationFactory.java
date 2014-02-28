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
 * PropertyAnnotationFactory - fabryka obslugujaca annotacje Property.class
 * 
 * @see pl.slawas.diffs.Property
 * @see pl.slawas.diffs.AnnotationFactory
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class PropertyAnnotationFactory implements AnnotationFactory {

	/**
	 * domyslna wartosc dla atrybutu nazwy klucza w annotacji Property.class.
	 * 
	 * @see pl.slawas.diffs.Property#key()
	 */
	public static final String DEFAULT_KEY = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getAnnotation()
	 */
	public Class<? extends Annotation> getAnnotation() {
		return Property.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getLabel(Field)
	 */
	public String getLabel(Field field) {
		String label = ((Property) field.getAnnotation(Property.class)).key();
		if (label.equals(DEFAULT_KEY))
			label = field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getLabel(Hashtable<String,
	 * String>, String, Field)
	 */
	public String getLabel(Hashtable<String, String> labelList, String parentLabel, Field field) {
		String label = getLabel(field);
		if (parentLabel != null)
			label = parentLabel + "." + label;
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getReferencedFieldName(Field)
	 */
	public String getReferencedFieldName(Field field) {
		String referencedFieldName = ((Property) field.getAnnotation(Property.class))
				.referencedFieldName();
		if (referencedFieldName.equals(DEFAULT_REFERENCED_FIELD_NAME)) {
			referencedFieldName = null;
		}
		return referencedFieldName;
	}

}
