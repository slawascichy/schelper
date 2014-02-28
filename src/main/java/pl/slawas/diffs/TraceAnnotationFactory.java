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
 * TraceAnnotationFactory - fabryka przetwarzajaca annotacje Trace.class
 * 
 * @see pl.slawas.diffs.Trace
 * @see pl.slawas.diffs.AnnotationFactory
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class TraceAnnotationFactory implements AnnotationFactory {

	/**
	 * domyslna wartosc dla atrybutu nazwy pola w annotacji Trace.class, bedacej
	 * wskaznikiem na w dynamicznej liscie etykiet.
	 * 
	 * @see pl.slawas.diffs.Trace#labelKey()
	 * 
	 */
	public static final String DEFAULT_FIELD_NAME = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getAnnotation()
	 */
	public Class<? extends Annotation> getAnnotation() {
		return Trace.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.ares.core.diffs.AnnotationFactory#getLabel(Field)
	 */
	public String getLabel(Field field) {
		String label = ((Trace) field.getAnnotation(Trace.class)).label();
		if (label.equals(DEFAULT_LABEL))
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
		if (((Trace) field.getAnnotation(Trace.class)).isDynamic() &&
				labelList != null && labelList.size() != 0)
		{
			String labelKey = ((Trace) field.getAnnotation(Trace.class)).labelKey();
			if (labelKey.equals(DEFAULT_FIELD_NAME)) {
				labelKey = field.getName();
			}
			String dynamicLabel = labelList.get(labelKey);
			if (dynamicLabel != null && !dynamicLabel.equals("")) {
				label = dynamicLabel;
			}
		}
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
		String referencedFieldName = ((Trace) field.getAnnotation(Trace.class)).referencedFieldName();
		if (referencedFieldName.equals(DEFAULT_REFERENCED_FIELD_NAME)) {
			referencedFieldName = null;
		}
		return referencedFieldName;
	}

}
