package pl.slawas.twl4j.logger;

public interface LoggerAppender {

	boolean isDebugEnabled();

	boolean isErrorEnabled();

	boolean isTraceEnabled();

	boolean isWarnEnabled();

	boolean isInfoEnabled();

	void error(String currentDate, String message);

	void debug(String currentDate, String message);

	void trace(String currentDate, String message);

	void warn(String currentDate, String message);

	void info(String currentDate, String message);

	void error(String currentDate, String message, Throwable arg1);

	void debug(String currentDate, String message, Throwable arg1);

	void trace(String currentDate, String message, Throwable arg1);

	void warn(String currentDate, String message, Throwable arg1);

	void info(String currentDate, String message, Throwable arg1);
}
