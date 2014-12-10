package pl.slawas.twl4j;

public interface Logger {

	/** rozszerzenie plików Java */
	public final static String JAVA_FILE_EXTENTION = ".java";

	boolean isDebugEnabled();

	boolean isErrorEnabled();

	boolean isTraceEnabled();

	boolean isWarnEnabled();

	boolean isInfoEnabled();

	public abstract void debug(String arg0);

	public abstract void debug(String arg0, Object arg1);

	public abstract void debug(String arg0, Object[] arg1);

	public abstract void debug(String arg0, Throwable arg1);

	public abstract void error(String arg0);

	public abstract void error(String arg0, Object arg1);

	public abstract void error(String arg0, Object[] arg1);

	public abstract void error(String arg0, Throwable arg1);

	public abstract void trace(String arg0);

	public abstract void trace(String arg0, Object arg1);

	public abstract void trace(String arg0, Object[] arg1);

	public abstract void trace(String arg0, Throwable arg1);

	public abstract void warn(String arg0);

	public abstract void warn(String arg0, Object arg1);

	public abstract void warn(String arg0, Object[] arg1);

	public abstract void warn(String arg0, Throwable arg1);

	public abstract void info(String arg0);

	public abstract void info(String arg0, Object arg1);

	public abstract void info(String arg0, Object[] arg1);

	public abstract void info(String arg0, Throwable arg1);

}