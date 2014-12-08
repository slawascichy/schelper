package pl.slawas.helper.jndi;

/**
 * 
 * TBinder
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public interface TBinder {

	void bind(Object cp, String jndiName);

}