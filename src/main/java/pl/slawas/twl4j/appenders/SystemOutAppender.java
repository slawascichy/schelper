package pl.slawas.twl4j.appenders;

import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.logger.LogLevel;
import pl.slawas.twl4j.logger.LoggerAppenderSupport;

public class SystemOutAppender extends LoggerAppenderSupport {

	SystemOutAppender(LogLevel logLevel) {
		super(logLevel);
	}

	@Override
	public void error(String currentDate, String message) {
		StringBuilder sb = new StringBuilder();
		System.err.println(sb.append(currentDate).append(LogLevel.ERROR_LABEL)
				.append(Strings.SPACE).append(message).toString());
	}

	@Override
	public void trace(String currentDate, String message) {
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.append(currentDate).append(LogLevel.TRACE)
				.append(Strings.SPACE).append(message).toString());
	}

	@Override
	public void warn(String currentDate, String message) {
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.append(currentDate)
				.append(LogLevel.WARNING_LABEL).append(Strings.SPACE)
				.append(message).toString());
	}

	@Override
	public void info(String currentDate, String message) {
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.append(currentDate).append(LogLevel.INFO)
				.append(Strings.SPACE).append(message).toString());
	}

	@Override
	public void debug(String currentDate, String message) {
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.append(currentDate).append(LogLevel.DEBUG)
				.append(Strings.SPACE).append(message).toString());

	}

	@Override
	public void error(String currentDate, String message, Throwable arg1) {
		error(currentDate, message);
		arg1.printStackTrace(System.err);
	}

	@Override
	public void trace(String currentDate, String message, Throwable arg1) {
		trace(currentDate, message);
		arg1.printStackTrace(System.out);
	}

	@Override
	public void warn(String currentDate, String message, Throwable arg1) {
		warn(currentDate, message);
		arg1.printStackTrace(System.out);
	}

	@Override
	public void info(String currentDate, String message, Throwable arg1) {
		info(currentDate, message);
		arg1.printStackTrace(System.out);
	}

	@Override
	public void debug(String currentDate, String message, Throwable arg1) {
		debug(currentDate, message);
		arg1.printStackTrace(System.out);
	}

}
