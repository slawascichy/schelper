package pl.slawas.helper.jndi;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;

/**
 * 
 * HSQLDatasourceObject - Źródło HSQL'a nie spełnia interfejsu
 * javax.naming.Referenceable - trzeba było zrobić fake'a
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class HSQLDatasourceObject extends ReferenceableDatasourceObject implements CommonDataSource {

	private static final long serialVersionUID = -6258326265445224076L;
	public static final String DATABASE_CONNECTION_FACTORY = HSQLDatasourceObject.class
			.getName();

	public HSQLDatasourceObject(DataSource notReferenceableDataSource) {
		super(notReferenceableDataSource);
	}

	public Reference getReference() throws NamingException {
		String objName = this.toString();
		JndiDataSourceObjectFactory.addObject(objName, this);
		return new Reference(getClass().getName(), new StringRefAddr(
				DATABASE_CONNECTION_FACTORY, objName),
				JndiDataSourceObjectFactory.class.getName(), null);
	}

}
