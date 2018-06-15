package pl.slawas.test.math;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;
import pl.slawas.math.MathExpression;
import pl.slawas.math.MathExpressionException;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

public class MathExpressionTest extends TestCase {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void testMath() throws MathExpressionException {

		logger.info("Start testu... " + "\n**********************************************"
				+ "\n*  SCENARIUSZ MathExpressionTest.testMath() {}  *"
				+ "\n**********************************************", Calendar.getInstance().getTimeInMillis());
		String result = "OK";

		MathExpression q;
		BigDecimal expressionResult;
		q = new MathExpression("((4 - 2^3 + 1) * -sqrt(3*3+4*4)) / 2.1");
		expressionResult = q.eval();
		System.out.println(expressionResult);

		expressionResult = new MathExpression("a + b").setParam("a", 1d).setParam("b", 3d).eval();
		System.out.println(expressionResult);

		String arytmeticQuery = "test1 + (test3 * 3.4 + Test5)/users.mrc_Case_id";
		q = new MathExpression(arytmeticQuery);
		Map<String, Double> params = new HashMap<String, Double>();
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
		System.out.println("testDocGroupByFunction: wynik wzoru: " + expressionResult);
		assertEquals(new BigDecimal("7.6666666667"), expressionResult);

		assert result.equals("OK") : "Test zakonczyl sie porazka";
	}
}
