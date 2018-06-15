package pl.slawas.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * MathExpressionCore based on
 * https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
 * 
 *
 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
 * @version $Revision: 1.1 $
 *
 */
class MathExpressionCore {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private int pos = -1, ch, chPiervous, chNext;
	private final String str;
	private final ExprFactory factory = new ExprFactory();

	MathExpressionCore(String str) {
		super();
		this.str = str;
	}

	private void nextChar() {
		this.chPiervous = this.ch;
		this.ch = (++this.pos < this.str.length()) ? this.str.charAt(pos) : -1;
		this.chNext = (this.pos + 1) < str.length() ? this.str.charAt(pos + 1) : -1;
	}

	private boolean eat(int charToEat) {
		while (this.ch == ' ') {
			nextChar();
		}
		if (this.ch == charToEat) {
			nextChar();
			return true;
		}
		return false;
	}

	IMathExpression parse() throws MathExpressionException {
		nextChar();
		IMathExpression x = parseExpression();
		if (this.pos < this.str.length()) {
			throw new MathExpressionException("Unexpected character: '" + (char) ch + "'");
		}
		return x;
	}

	// Grammar:
	// expression = term | expression `+` term | expression `-` term
	// term = factor | term `*` factor | term `/` factor
	// factor = `+` factor | `-` factor | `(` expression `)`
	// | number | functionName factor | factor `^` factor

	private IMathExpression parseExpression() throws MathExpressionException {
		IMathExpression x = parseTerm();
		for (;;) {
			if (eat('+')) {
				/* addition */
				x = new SumExpr(x, parseTerm());
			} else if (eat('-')) {
				/* subtraction */
				x = new DiffExpr(x, parseTerm());
			} else {
				return x;
			}
		}
	}

	private IMathExpression parseTerm() throws MathExpressionException {
		IMathExpression x = parseFactor();
		for (;;) {
			if (eat('*')) {
				/* multiplication */
				x = new MultiExpr(x, parseTerm());
			} else if (eat('/')) {
				/* division */
				x = new DivExpr(x, parseTerm());
			} else {
				return x;
			}
		}
	}

	private IMathExpression parseFactor() throws MathExpressionException {
		if (eat('+')) {
			/* unary plus */
			return parseFactor();
		}
		if (eat('-')) {
			/* unary minus */
			return new MinusExpr(parseFactor());
		}
		logger.info("-->parseFactor: chPiervous='{}'({}), ch='{}'({}), chNext='{}'({})", new Object[] {
				(char) this.chPiervous, this.chPiervous, (char) ch, ch, (char) this.chNext, this.chNext });
		IMathExpression x;
		int startPos = this.pos;
		if (eat('(')) {
			/* parentheses */
			x = parseExpression();
			eat(')');
		} else if (!(this.chPiervous >= 'a' && this.chPiervous <= 'z') && !(this.chNext >= 'a' && this.chNext <= 'z')
				&& !(this.chPiervous >= 'A' && this.chPiervous <= 'Z') && !(this.chNext >= 'A' && this.chNext <= 'Z')
				&& ((this.ch >= '0' && this.ch <= '9') || this.ch == '.')) {
			/* liczby */
			while ((ch >= '0' && ch <= '9') || ch == '.') {
				logger.info("-->[{}..{}] parseFactor: bedzie liczba? mam '{}'...",
						new Object[] { startPos, this.pos, (char) ch });
				nextChar();

			}
			String valOrPram = str.substring(startPos, this.pos);
			x = new ValueExpr(factory, valOrPram, new BigDecimal(valOrPram));
		} else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '.'
				|| ch == '_') {
			/* funkcje i parametry */
			while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '.'
					|| ch == '_') {
				logger.info("-->[{}..{}] parseFactor: bedzie słowo? mam '{}'...",
						new Object[] { startPos, this.pos, (char) ch });
				nextChar();
			}
			String func = str.substring(startPos, this.pos);
			if (func.equalsIgnoreCase("sqrt")) {
				x = parseFactor();
				x = new SqrtExpr(x);
			} else if (func.equalsIgnoreCase("sin")) {
				x = parseFactor();
				x = new SinExpr(x);
			} else if (func.equalsIgnoreCase("cos")) {
				x = parseFactor();
				x = new CosExpr(x);
			} else if (func.equalsIgnoreCase("tan")) {
				x = parseFactor();
				x = new TanExpr(x);
			} else {
				x = new ValueExpr(factory, func, null);
			}
		} else {
			throw new MathExpressionException("Unexpected character: '" + (char) ch + "' on position " + this.pos);
		}

		if (eat('^')) {
			/* exponentiation */
			x = new PovExpr(x, parseFactor());
		}

		return x;
	}

	/**
	 * @return the {@link #factory}
	 */
	Set<String> getParamNames() {
		return factory.params.keySet();
	}

	/**
	 * @return the {@link #factory}
	 */
	MathExpressionCore setParam(String paramName, Double value) {
		factory.params.put(paramName, new BigDecimal(value));
		return this;
	}

	/**
	 * 
	 * ExprFactory prywatna fabryka do przechowywania parametrów.
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class ExprFactory {
		private Map<String, BigDecimal> params = new HashMap<String, BigDecimal>();
	}

	/**
	 * DivExpr - operacja dzielenia
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class DivExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression arg1;
		private final IMathExpression arg2;

		private DivExpr(IMathExpression arg1, IMathExpression arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = arg1.eval();
			BigDecimal divisor = arg2.eval();
			logger.info("-->DivExpr: {} + {}", new Object[] { m, divisor });
			return m.divide(divisor, 10, RoundingMode.HALF_EVEN);
		}

	}

	/**
	 * 
	 * MultiExpr - operacja mnożenia
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class MultiExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression arg1;
		private final IMathExpression arg2;

		private MultiExpr(IMathExpression arg1, IMathExpression arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = arg1.eval();
			BigDecimal multiplicand = arg2.eval();
			logger.info("-->MultiExpr: {} + {}", new Object[] { m, multiplicand });
			return m.multiply(multiplicand);
		}

	}

	/**
	 * 
	 * SumExpr - suma
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class SumExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression arg1;
		private final IMathExpression arg2;

		private SumExpr(IMathExpression arg1, IMathExpression arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = arg1.eval();
			BigDecimal augend = arg2.eval();
			logger.info("-->SumExpr: {} + {}", new Object[] { m, augend });
			return m.add(augend);
		}

	}

	/**
	 * 
	 * DiffExpr - różnica
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class DiffExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression arg1;
		private final IMathExpression arg2;

		private DiffExpr(IMathExpression arg1, IMathExpression arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = arg1.eval();
			BigDecimal subtrahend = arg2.eval();
			logger.info("-->DiffExpr: {} - {}", new Object[] { m, subtrahend });
			return m.subtract(subtrahend);
		}

	}

	/**
	 * 
	 * ValueExpr - wartość
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class ValueExpr implements IMathExpression {

		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final ExprFactory factory;
		private final String param;

		private ValueExpr(ExprFactory factory, String param, BigDecimal value) {
			logger.info("->ValueExpr: param={}, value={}", new Object[] { param, (value != null ? value : 0d) });
			this.factory = factory;
			this.factory.params.put(param, (value != null ? value : new BigDecimal(0)));
			this.param = param;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			return this.factory.params.get(param);
		}

	}

	/**
	 * 
	 * MinusExpr - wartość ujemna (minus)
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class MinusExpr implements IMathExpression {
		private final IMathExpression expression;

		private MinusExpr(IMathExpression expression) {
			this.expression = expression;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			return expression.eval().negate();
		}

	}

	/**
	 * 
	 * SqrtExpr pierwiastek kwadratowy
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class SqrtExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression expression;

		private SqrtExpr(IMathExpression expression) {
			this.expression = expression;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = expression.eval();
			logger.info("-->SqrtExpr: {}", new Object[] { m });
			return BigDecimal.valueOf(Math.sqrt(m.doubleValue()));
		}

	}

	/**
	 * 
	 * PovExpr - potęgowanie
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class PovExpr implements IMathExpression {
		protected static Logger logger = LoggerFactory.getLogger(MathExpression.class);
		private final IMathExpression expression;
		private final IMathExpression pow;

		private PovExpr(IMathExpression expression, IMathExpression pow) {
			this.expression = expression;
			this.pow = pow;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = expression.eval();
			BigDecimal power = pow.eval();
			logger.info("-->PovExpr: {}^{}", new Object[] { m, power });
			return m.pow(power.intValue());
		}

	}

	/**
	 * 
	 * SinExpr
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class SinExpr implements IMathExpression {

		private final IMathExpression expression;

		private SinExpr(IMathExpression expression) {
			this.expression = expression;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = expression.eval();
			return BigDecimal.valueOf(Math.sin(Math.toRadians(m.doubleValue())));
		}

	}

	/**
	 * 
	 * CosExpr
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class CosExpr implements IMathExpression {

		private final IMathExpression expression;

		private CosExpr(IMathExpression expression) {
			this.expression = expression;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = expression.eval();
			return BigDecimal.valueOf(Math.cos(Math.toRadians(m.doubleValue())));
		}

	}

	/**
	 * 
	 * TanExpr
	 *
	 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
	 * @version $Revision: 1.1 $
	 *
	 */
	private static class TanExpr implements IMathExpression {

		private final IMathExpression expression;

		private TanExpr(IMathExpression expression) {
			this.expression = expression;
		}

		/* Overridden (non-Javadoc) */
		@Override
		public BigDecimal eval() {
			BigDecimal m = expression.eval();
			return BigDecimal.valueOf(Math.tan(Math.toRadians(m.doubleValue())));
		}

	}

}
