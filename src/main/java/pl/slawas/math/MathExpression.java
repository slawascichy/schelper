package pl.slawas.math;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 
 * MathExpression - prosta obsługa wyrażeń arytmetycznych. Jeżeli zasób funkcji
 * arytmetycznych nie jest wystarczający warto pamiętać, że można wykorzystać
 * http://mathparser.org/mxparser-math-collection//
 * 
 * <pre>
 Grammar:
 expression = term | expression `+` term | expression `-` term
 term = factor | term `*` factor | term `/` factor
 factor = `+` factor | `-` factor | `(` expression `)`
 | number | functionName factor | factor `^` factor
 * </pre>
 * <p>
 * Wspierane funkcje i operatory matematyczne. <i>Ważne są separatory spacji
 * pomiędzy elementami wyrażenia z wyjątkiem potęgowania!</i>
 * </p>
 * <ul>
 * <li><code>+</code> dodawanie np. 1 + 2, a + b</li>
 * <li><code>-</code> odejmowanie np. 2 - 1, a - b</li>
 * <li><code>*</code> mnożenie np. 1 * 2, a * b</li>
 * <li><code>/</code> dzielenie np. 1 / 2, a / b</li>
 * <li><code>sqrt</code> pierwiastek kwadratowy np. sqrt(16), sqrt(a)</li>
 * <li><code>sin</code> sinus, podstawą jest liczba stopni, np, sin(16), sin(a)
 * </li>
 * <li><code>cos</code> cosinus, podstawą jest liczba stopni, np, cos(16),
 * cos(a)</li>
 * <li><code>tan</code> tangens, podstawą jest liczba stopni, np, tan(16),
 * tan(a)</li>
 * <li><code>^</code>potegowanie np. 1^2, a^b</li>
 * </ul>
 * Przykłady: <code><pre>
	BigDecimal expressionResult;
	MathExpression q;
	//--- 1
	q = new MathExpression("((4 - 2^3 + 1) * -sqrt(3*3+4*4)) / 2.1");
	expressionResult = q.eval();
	System.out.println(expressionResult);
	assertEquals(new BigDecimal("7.5"), expressionResult);
	//--- 2
	expressionResult = new MathExpression("a + b").setParam("a", 1d).setParam("b", 3d).eval();
	System.out.println(expressionResult);
	assertEquals(new BigDecimal("4"), expressionResult);
	//--- 3
	String arytmeticQuery = "test1 + (test3 * 3.4 + Test5)/users.mrc_Case_id";
	q = new MathExpression(arytmeticQuery);
	Map<String,Double> params = new HashMap<>();
	params.put("test1", 1d);
	params.put("test3", 5d);
	params.put("Test5", 3d);
	params.put("users.mrc_Case_id", 3d);
	
	for (String paramName : q.getParamNames()) {
		System.out.println("-->testDocGroupByFunction: q.paramName:" + paramName);
	}
	for (Entry<String, Double> e : params.entrySet()) {
		q.setParam(e.getKey(), e.getValue());
	}
	expressionResult = q.eval();
	System.out.println("testDocGroupByFunction: wynik wzoru: "+ expressionResult);
	assertEquals(new BigDecimal("7.6666666667"), expressionResult);
</pre>
 </code>
 * 
 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
 * @version $Revision: 1.1 $
 *
 */
public class MathExpression implements IMathExpression {

	private final MathExpressionCore eval;
	private final IMathExpression expression;

	/**
	 * Konstruktor wyrażenia. np.:<br/>
	 * <code>
	q = new MathExpression("((4 - 2^3 + 1) * -sqrt(3*3+4*4)) / 2.1");
	 * </code>
	 * 
	 * @param str
	 *            wyrażenie matematyczne
	 * @throws L0007MathExpressionException
	 */
	public MathExpression(final String str) throws MathExpressionException {
		eval = new MathExpressionCore(str);
		expression = eval.parse();
	}

	/**
	 * Wywołanie przeliczenia wyrażenia. Za każdym razem kiedy wywołujemy te metodę
	 * wyrażenie jest ponownie przeliczane.
	 * 
	 * @return wynik przeliczonego wyrażenia
	 */
	@Override
	public BigDecimal eval() {
		return expression.eval();
	}

	/**
	 * Pobieranie listy parametrów
	 * 
	 * @return lista parametrów
	 */
	public Set<String> getParamNames() {
		return eval.getParamNames();
	}

	/**
	 * Ustawianie wartości parametrów i określonej nazwie: np.:<br/>
	 * <code>
	q = new MathExpression("a + b");
	 * </code>
	 * 
	 * @param paramName
	 *            nazwa parametru
	 * @param value
	 *            wartość parametru
	 * @return obecny obiekt wyrażenia
	 */
	public MathExpression setParam(String paramName, Double value) {
		eval.setParam(paramName, value);
		return this;
	}

}
