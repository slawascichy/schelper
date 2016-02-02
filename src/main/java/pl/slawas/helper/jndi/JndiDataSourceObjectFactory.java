package pl.slawas.helper.jndi;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

public class JndiDataSourceObjectFactory implements ObjectFactory {

	private static Map<String, Object> objectHashTable = new Hashtable<String, Object>();

	/**
	 * Wstawia do tablicy odpowiednie obiekty managerów połączeń, fabryk i
	 * źródeł danych
	 * 
	 * @param objName
	 *            nazwa obiektu - referencja: this.toString()
	 * @param obj
	 *            obiekt
	 */
	public static void addObject(String objName, Object obj) {
		objectHashTable.put(objName, obj);
	}

	final private static Logger log = LoggerFactory
			.getLogger(JndiDataSourceObjectFactory.class);

	@SuppressWarnings({ "rawtypes" })
	public Object getObjectInstance(Object obj, Name name, Context ctx,
			Hashtable params) throws Exception {

		Enumeration tEnum;
		Object result = null;

		log.debug(" ============= getObjectInstance : start ===============");
		if (log.isDebugEnabled()) {
			tEnum = name.getAll();
			while (tEnum.hasMoreElements()) {
				String key = (String) tEnum.nextElement();
				log.debug(" -> Element: {}", new Object[] { key });
			}
		}

		if (obj instanceof Reference) {
			Reference ref = (Reference) obj;
			RefAddr addr = null;
			if (ref.getClassName().equals(HSQLDatasourceObject.class.getName())) {
				addr = ref
						.get(HSQLDatasourceObject.DATABASE_CONNECTION_FACTORY);
				if (addr != null) {
					result = objectHashTable.get(addr.getContent());
				}
			}
		}
		log.debug(" =============  getObjectInstance : end  ===============");

		return result;
	}

}
