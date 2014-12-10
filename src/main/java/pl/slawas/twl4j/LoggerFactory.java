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
package pl.slawas.twl4j;

import pl.slawas.twl4j.appenders.LogAppender;
import pl.slawas.twl4j.config.LoggerConfig;
import pl.slawas.twl4j.logger.LogLevel;
import pl.slawas.twl4j.logger.LoggerAppender;
import pl.slawas.twl4j.logger.LoggerImplementation;

/**
 * LoggerFactory
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public class LoggerFactory {

	public static Logger getLogger(String name) {
		LogLevel ll;
		LogAppender logAppender;
		LoggerAppender appender;
		try {
			ll = LoggerConfig.getLogLevel();
			logAppender = LoggerConfig.getLogAppender();
		} catch (Exception e) {
			System.err.println("Issue of logger inicjalization: \n" + e);
			ll = LogLevel.NONE;
			logAppender = LogAppender.SYSTEMOUT;
		}
		appender = logAppender.getAppenderInstance(name, ll);
		return new LoggerImplementation(name, appender,
				LoggerConfig.getDateLoggerFormat(),
				LoggerConfig.getLoggerAddDate());
	}

	public static Logger getLogger(Class<?> clazz) {
		LogLevel ll;
		LogAppender logAppender;
		LoggerAppender appender;
		try {
			ll = LoggerConfig.getLogLevel();
			logAppender = LoggerConfig.getLogAppender();
		} catch (Exception e) {
			System.err.println("Issue of logger inicjalization: \n" + e);
			ll = LogLevel.NONE;
			logAppender = LogAppender.SYSTEMOUT;
		}
		appender = logAppender.getAppenderInstance(clazz, ll);
		return new LoggerImplementation(clazz.getName(), appender,
				LoggerConfig.getDateLoggerFormat(),
				LoggerConfig.getLoggerAddDate());
	}

	public static Logger getSystemLogger(String name) {
		LogLevel ll = LogLevel.INFO;
		LogAppender logAppender = LogAppender.SYSTEMOUT;
		LoggerAppender appender = logAppender.getAppenderInstance(name, ll);
		return new LoggerImplementation(name, appender,
				LoggerConfig.DEFAULT_DATE_LOGGER_FORMAT,
				LoggerConfig.DEFAULT_LOGGER_ADD_DATE);
	}

	public static Logger getSystemLogger(Class<?> clazz) {
		return getSystemLogger(clazz.getName());
	}
}
