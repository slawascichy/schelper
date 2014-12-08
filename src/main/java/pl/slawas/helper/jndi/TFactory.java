package pl.slawas.helper.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * 
 * TFactory
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class TFactory implements ObjectFactory {

	@SuppressWarnings("rawtypes")
	public Object getObjectInstance(Object obj, Name name, Context ctx,
			Hashtable env) throws Exception {

		if (obj instanceof Reference) {
			Reference ref = (Reference) obj;
			if (ref.getClassName().equals(TObjectReference.class.getName())) {
				RefAddr addr = ref.get(TObjectReference.class.getName());
				if (addr != null) {
					return TObjectReference.getInstance(name.toString());
				}
			}
		}
		return null;
	}

}
