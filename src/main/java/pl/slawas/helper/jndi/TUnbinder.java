package pl.slawas.helper.jndi;

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;

/**
 * 
 * TUnbinder
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class TUnbinder {

	@SuppressWarnings("rawtypes")
	public static void unbind(Logger log, String jndiName, Hashtable env) {
		try {
			InitialContext jndiCtx = new InitialContext(env);
			jndiCtx.unbind(jndiName);
			log.info("{}: Jndi Name was unbinded", jndiName);
		} catch (NamingException e) {
			log.warn("Błąd odłączenia obiektu '" + jndiName
					+ "'z kontekstu JNDI", e);
		}
	}

}
