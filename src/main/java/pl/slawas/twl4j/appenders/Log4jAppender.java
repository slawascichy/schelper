package pl.slawas.twl4j.appenders;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggerFactory;

import pl.slawas.twl4j.logger.LogLevel;
import pl.slawas.twl4j.logger.LoggerAppender;

public class Log4jAppender implements LoggerAppender {

	private final org.apache.log4j.Logger logCat;

	Log4jAppender(String logCategory) {
		this.logCat = org.apache.log4j.Logger.getLogger(logCategory);
	}

	Log4jAppender(Class<?> logCategory) {
		this.logCat = org.apache.log4j.Logger.getLogger(logCategory);
	}

	Log4jAppender(String name, LoggerFactory factory) {
		this.logCat = org.apache.log4j.Logger.getLogger(name, factory);
	}

	public boolean isDebugEnabled() {
		return this.logCat.isDebugEnabled();
	}

	public boolean isErrorEnabled() {
		return this.logCat.isEnabledFor(Level.ERROR);
	}

	public boolean isTraceEnabled() {
		return this.logCat.isTraceEnabled();
	}

	public boolean isWarnEnabled() {
		return this.logCat.isTraceEnabled();
	}

	public boolean isInfoEnabled() {
		return this.logCat.isInfoEnabled();
	}

	@Override
	public void error(String currentDate, String message) {
		this.logCat.error(message);
	}

	@Override
	public void trace(String currentDate, String message) {
		this.logCat.trace(message);
	}

	@Override
	public void warn(String currentDate, String message) {
		this.logCat.warn(message);
	}

	@Override
	public void info(String currentDate, String message) {
		this.logCat.info(message);
	}

	@Override
	public void debug(String currentDate, String message) {
		this.logCat.debug(message);
	}

	@Override
	public void error(String currentDate, String message, Throwable arg1) {
		this.logCat.error(message, arg1);
	}

	@Override
	public void trace(String currentDate, String message, Throwable arg1) {
		this.logCat.trace(message, arg1);
	}

	@Override
	public void warn(String currentDate, String message, Throwable arg1) {
		this.logCat.warn(message, arg1);
	}

	@Override
	public void info(String currentDate, String message, Throwable arg1) {
		this.logCat.info(message, arg1);
	}

	@Override
	public void debug(String currentDate, String message, Throwable arg1) {
		this.logCat.debug(message, arg1);
	}

	/* Overridden (non-Javadoc) */
	@Override
	public LogLevel level() {
		Level l = this.logCat.getLevel();
		return LogLevel.valueOf(l.toString());
	}

}
