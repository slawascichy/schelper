package pl.slawas.twl4j;

import pl.slawas.twl4j.logger.LogLevel;

public interface Logger {

	/** rozszerzenie plików Java */
	final static String JAVA_FILE_EXTENTION = ".java";

	LogLevel level();

	boolean isDebugEnabled();

	boolean isErrorEnabled();

	boolean isTraceEnabled();

	boolean isWarnEnabled();

	boolean isInfoEnabled();

	void debug(String arg0);

	void debug(String arg0, Object arg1);

	void debug(String arg0, Object[] arg1);

	void debug(String arg0, Throwable arg1);

	void error(String arg0);

	void error(String arg0, Object arg1);

	void error(String arg0, Object[] arg1);

	void error(String arg0, Throwable arg1);

	void trace(String arg0);

	void trace(String arg0, Object arg1);

	void trace(String arg0, Object[] arg1);

	void trace(String arg0, Throwable arg1);

	void warn(String arg0);

	void warn(String arg0, Object arg1);

	void warn(String arg0, Object[] arg1);

	void warn(String arg0, Throwable arg1);

	void info(String arg0);

	void info(String arg0, Object arg1);

	void info(String arg0, Object[] arg1);

	void info(String arg0, Throwable arg1);

}