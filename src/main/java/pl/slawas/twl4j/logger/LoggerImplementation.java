/*
 * Slawas.pl Copyright &copy; 2007-2012 
 * http://slawas.pl 
 * All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL SŁAWOMIR CICHY BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.slawas.twl4j.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.config.LoggerConfig;

/**
 * Logger - logger oparty o {@link System#out}.
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class LoggerImplementation implements Logger {

	private final String categoryName;

	private final SimpleDateFormat sdf;

	private final Boolean addDate;

	private final LoggerAppender appender;

	public LoggerImplementation(String name, LoggerAppender appender,
			String dateLoggerFormat, Boolean addDate) {
		boolean aDate = (addDate == null ? LoggerConfig.getLoggerAddDate()
				: addDate);
		SimpleDateFormat sdf;

		try {
			sdf = new SimpleDateFormat(
					StringUtils.isBlank(dateLoggerFormat) ? LoggerConfig
							.getDateLoggerFormat() : dateLoggerFormat);
		} catch (Exception e) {
			System.err.println("Issue of logger inicjalization: \n" + e);
			sdf = new SimpleDateFormat(LoggerConfig.getDateLoggerFormat());
			aDate = false;
		}
		this.appender = appender;
		this.sdf = sdf;
		this.categoryName = name;
		this.addDate = aDate;
	}

	StringBuilder buildMessage(String arg0, Object[] arg1) {

		StringBuilder message = null;
		StackTraceElement[] els = Thread.currentThread().getStackTrace();
		for (int i = 0; i < els.length; i++) {
			String className = els[i].getClassName();
			if (className.equals(categoryName)) {
				String[] nameElements = className.split("\\.");
				String classSimpleName = nameElements[nameElements.length - 1];
				message = new StringBuilder(Strings.OPEN_SQUARE_BRACKED)
						.append(classSimpleName).append(JAVA_FILE_EXTENTION)
						.append(Strings.DOUBLE_DOT_ALONE)
						.append(els[i].getLineNumber())
						.append(Strings.CLOSE_SQUARE_BRACKED);
				break;
			}
		}
		if (message == null) {
			message = new StringBuilder(Strings.OPEN_SQUARE_BRACKED).append(
					categoryName).append(Strings.CLOSE_SQUARE_BRACKED);
		}
		message.append(Strings.DOUBLE_DOT);

		String[] splitedArg0 = new StringBuilder(arg0).append(Strings.SPACE)
				.toString().split("\\{\\}");
		int arg1Lenght = arg1.length;
		int splitedArg0Length = splitedArg0.length;
		int i = 0;
		for (String x : splitedArg0) {
			if (i < arg1Lenght && i + 1 < splitedArg0Length) {

				StringBuilder element = null;

				if (arg1[i] != null) {
					try {
						element = object2String(arg1[i]);
					} catch (Exception e) {
						message.append(e.getMessage());
					}
				} else {
					element = null;
				}

				message.append(x).append(element);
			} else {
				message.append(x).append(
						(i + 1 < splitedArg0Length ? "{}" : StringUtils.EMPTY));
			}
			i++;
		}
		return message;
	}

	protected StringBuilder object2String(Object ob) throws Exception {
		StringBuilder result = null;
		if (ob != null) {
			result = new StringBuilder();
			result.append(ob.toString());
		}
		return result;
	}

	/* Overridden (non-Javadoc) */
	@Override
	public void debug(String arg0) {
		if (appender.isDebugEnabled()) {
			Object[] args = new Object[] {};
			appender.debug(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void debug(String arg0, Object arg1) {
		if (appender.isDebugEnabled()) {
			Object[] args = new Object[] { arg1 };
			appender.debug(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void debug(String arg0, Object[] arg1) {
		if (appender.isDebugEnabled()) {
			Object[] args = arg1;
			appender.debug(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void debug(String arg0, Throwable arg1) {
		if (appender.isDebugEnabled()) {
			Object[] args = new Object[] {};
			appender.debug(getCurrentDate(), buildMessage(arg0, args)
					.toString(), arg1);
		}
	}

	/* Overridden (non-Javadoc) */
	@Override
	public void error(String arg0) {
		if (appender.isErrorEnabled()) {
			Object[] args = new Object[] {};
			appender.error(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void error(String arg0, Object arg1) {
		if (appender.isErrorEnabled()) {
			Object[] args = new Object[] { arg1 };
			appender.error(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void error(String arg0, Object[] arg1) {
		if (appender.isErrorEnabled()) {
			Object[] args = arg1;
			appender.error(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void error(String arg0, Throwable arg1) {
		if (appender.isErrorEnabled()) {
			Object[] args = new Object[] {};
			appender.error(getCurrentDate(), buildMessage(arg0, args)
					.toString(), arg1);
		}
	}

	/* Overridden (non-Javadoc) */
	@Override
	public void trace(String arg0) {
		if (appender.isTraceEnabled()) {
			Object[] args = new Object[] {};
			appender.trace(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void trace(String arg0, Object arg1) {
		if (appender.isTraceEnabled()) {
			Object[] args = new Object[] { arg1 };
			appender.trace(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void trace(String arg0, Object[] arg1) {
		if (appender.isTraceEnabled()) {
			Object[] args = arg1;
			appender.trace(getCurrentDate(), buildMessage(arg0, args)
					.toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void trace(String arg0, Throwable arg1) {
		if (appender.isTraceEnabled()) {
			Object[] args = new Object[] {};
			appender.trace(getCurrentDate(), buildMessage(arg0, args)
					.toString(), arg1);
		}
	}

	/* Overridden (non-Javadoc) */
	@Override
	public void warn(String arg0) {
		if (appender.isWarnEnabled()) {
			Object[] args = new Object[] {};
			appender.warn(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void warn(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (appender.isWarnEnabled()) {
			appender.warn(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void warn(String arg0, Object[] arg1) {
		if (appender.isWarnEnabled()) {
			Object[] args = arg1;
			appender.warn(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void warn(String arg0, Throwable arg1) {
		if (appender.isWarnEnabled()) {
			Object[] args = new Object[] {};
			appender.warn(getCurrentDate(),
					buildMessage(arg0, args).toString(), arg1);
		}
	}

	/* Overridden (non-Javadoc) */
	@Override
	public void info(String arg0) {
		if (appender.isInfoEnabled()) {
			Object[] args = new Object[] {};
			appender.info(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void info(String arg0, Object arg1) {
		if (appender.isInfoEnabled()) {
			Object[] args = new Object[] { arg1 };
			appender.info(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void info(String arg0, Object[] arg1) {
		if (appender.isInfoEnabled()) {
			Object[] args = arg1;
			appender.info(getCurrentDate(), buildMessage(arg0, args).toString());
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public void info(String arg0, Throwable arg1) {
		if (appender.isInfoEnabled()) {
			Object[] args = new Object[] {};
			appender.info(getCurrentDate(),
					buildMessage(arg0, args).toString(), arg1);
		}
	}

	/**
	 * Pobieranie aktualnej daty
	 * 
	 * @return
	 */
	private String getCurrentDate() {
		if (addDate) {
			return sdf.format(Calendar.getInstance().getTime()) + Strings.SPACE;
		} else {
			return StringUtils.EMPTY;
		}

	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean isDebugEnabled() {
		return appender.isDebugEnabled();
	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean isErrorEnabled() {
		return appender.isErrorEnabled();
	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean isTraceEnabled() {
		return appender.isTraceEnabled();
	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean isWarnEnabled() {
		return appender.isWarnEnabled();
	}

	/* Overridden (non-Javadoc) */
	@Override
	public boolean isInfoEnabled() {
		return appender.isInfoEnabled();
	}

}
