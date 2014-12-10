package pl.slawas.twl4j.logger;

public abstract class LoggerAppenderSupport implements LoggerAppender {

	private final LogLevel logLevel;

	public LoggerAppenderSupport(LogLevel logLevel) {
		this.logLevel = (logLevel == null ? LogLevel.NONE : logLevel);
	}

	public boolean isDebugEnabled() {
		return logLevel.equals(LogLevel.DEBUG)
				|| logLevel.equals(LogLevel.TRACE);
	}

	public boolean isErrorEnabled() {
		return !logLevel.equals(LogLevel.NONE);
	}

	public boolean isTraceEnabled() {
		return logLevel.equals(LogLevel.TRACE);
	}

	public boolean isWarnEnabled() {
		return !logLevel.equals(LogLevel.NONE);
	}

	public boolean isInfoEnabled() {
		return logLevel.equals(LogLevel.INFO)
				|| logLevel.equals(LogLevel.DEBUG)
				|| logLevel.equals(LogLevel.TRACE);
	}
	
	
	
}
