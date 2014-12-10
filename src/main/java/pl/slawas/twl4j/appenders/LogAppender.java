package pl.slawas.twl4j.appenders;

import pl.slawas.twl4j.logger.LogLevel;
import pl.slawas.twl4j.logger.LoggerAppender;

public enum LogAppender {

	LOG4J, SYSTEMOUT;

	public LoggerAppender getAppenderInstance(String logCategory,
			LogLevel logLevel) {
		switch (this) {
		case LOG4J:
			return new Log4jAppender(logCategory);
		default:
			return new SystemOutAppender(logLevel);
		}
	}

	public LoggerAppender getAppenderInstance(Class<?> logCategory,
			LogLevel logLevel) {
		switch (this) {
		case LOG4J:
			return new Log4jAppender(logCategory);
		default:
			return new SystemOutAppender(logLevel);
		}
	}

}
