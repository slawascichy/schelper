package pl.slawas.helper.jndi;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * TObjectReference
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class TObjectReference implements Referenceable, Serializable {

	private static final long serialVersionUID = -2941400615511455043L;

	private static final Logger log = LoggerFactory
			.getLogger(TObjectReference.class);

	private final String jndiReferenceName;

	protected static Map<String, Object> ccInstances = new Hashtable<String, Object>();

	public void addInstance(Object ccInstance, String jndiName) {
		ccInstances.put(jndiName, ccInstance);
	}

	public static Object getInstance(String jndiName) {
		return ccInstances.get(jndiName);
	}

	public TObjectReference(String jndiReferenceName) {
		this.jndiReferenceName = jndiReferenceName;

	}

	public Reference getReference() throws NamingException {
		log.debug("{}: getting reference...", jndiReferenceName);
		return new Reference(TObjectReference.class.getName(),
				new StringRefAddr(TObjectReference.class.getName(),
						jndiReferenceName), TFactory.class.getName(), null);
	}

	public String toString() {
		return "Jndi Reference Name: " + jndiReferenceName + "";
	}

}