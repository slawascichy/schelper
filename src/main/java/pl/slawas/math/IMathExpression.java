package pl.slawas.math;

import java.math.BigDecimal;

/**
 * 
 * IMathExpression wewnętrzny interfejs wyrażenia arytmetycznego.
 *
 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
 * @version $Revision: 1.1 $
 *
 */
interface IMathExpression {

	/**
	 * Wywołanie przeliczenia wyrażenia. Za każdym razem kiedy wywołujemy te metodę
	 * wyrażenie jest ponownie przeliczane.
	 * 
	 * @return wynik przeliczonego wyrażenia
	 */
	BigDecimal eval();
}
