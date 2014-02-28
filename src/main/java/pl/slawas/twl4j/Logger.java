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

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggerFactory;

public class Logger {

	private final org.apache.log4j.Logger logCat;

	Logger(String logCategory) {
		this.logCat = org.apache.log4j.Logger.getLogger(logCategory);
	}

	Logger(Class<?> logCategory) {
		this.logCat = org.apache.log4j.Logger.getLogger(logCategory);
	}

	Logger(String name, LoggerFactory factory) {
		this.logCat = org.apache.log4j.Logger.getLogger(name, factory);
	}

	StrBuilder buildMessage(String arg0, Object[] arg1) {
		String[] splitedArg0 = new StringBuilder(arg0).append(" ").toString()
				.split("\\{\\}");
		StrBuilder message = new StrBuilder();
		int arg1Lenght = arg1.length;
		int splitedArg0Length = splitedArg0.length;
		int i = 0;
		for (String x : splitedArg0) {
			if (i < arg1Lenght && i + 1 < splitedArg0Length) {
				message.append(x).append(arg1[i]);
			} else {
				message.append(x).append(
						(i + 1 < splitedArg0Length ? "{}" : ""));
			}
			i++;
		}
		return message;
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

	public void debug(String arg0) {
		Object[] args = new Object[] {};
		if (isDebugEnabled()) {
			this.logCat.debug(buildMessage(arg0, args));
		}

	}

	public void debug(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (isDebugEnabled()) {
			this.logCat.debug(buildMessage(arg0, args));
		}

	}

	public void debug(String arg0, Object[] arg1) {
		Object[] args = arg1;
		if (isDebugEnabled()) {
			this.logCat.debug(buildMessage(arg0, args));
		}

	}

	public void debug(String arg0, Throwable arg1) {
		if (isDebugEnabled()) {
			this.logCat.debug(arg0, arg1);
		}
	}

	public void error(String arg0) {
		Object[] args = new Object[] {};
		if (isErrorEnabled()) {
			this.logCat.error(buildMessage(arg0, args));
		}

	}

	public void error(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (isErrorEnabled()) {
			this.logCat.error(buildMessage(arg0, args));
		}

	}

	public void error(String arg0, Object[] arg1) {
		Object[] args = arg1;
		if (isErrorEnabled()) {
			this.logCat.error(buildMessage(arg0, args));
		}

	}

	public void error(String arg0, Throwable arg1) {
		if (isErrorEnabled()) {
			this.logCat.error(arg0, arg1);
		}
	}

	public void trace(String arg0) {
		Object[] args = new Object[] {};
		if (isTraceEnabled()) {
			this.logCat.trace(buildMessage(arg0, args));
		}

	}

	public void trace(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (isTraceEnabled()) {
			this.logCat.trace(buildMessage(arg0, args));
		}

	}

	public void trace(String arg0, Object[] arg1) {
		Object[] args = arg1;
		if (isTraceEnabled()) {
			this.logCat.trace(buildMessage(arg0, args));
		}

	}

	public void trace(String arg0, Throwable arg1) {
		if (isTraceEnabled()) {
			this.logCat.trace(arg0, arg1);
		}
	}

	public void warn(String arg0) {
		Object[] args = new Object[] {};
		if (isWarnEnabled()) {
			this.logCat.warn(buildMessage(arg0, args));
		}

	}

	public void warn(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (isWarnEnabled()) {
			this.logCat.warn(buildMessage(arg0, args));
		}

	}

	public void warn(String arg0, Object[] arg1) {
		Object[] args = arg1;
		if (isWarnEnabled()) {
			this.logCat.warn(buildMessage(arg0, args));
		}

	}

	public void warn(String arg0, Throwable arg1) {
		if (isWarnEnabled()) {
			this.logCat.warn(arg0, arg1);
		}
	}

	public void info(String arg0) {
		Object[] args = new Object[] {};
		if (isInfoEnabled()) {
			this.logCat.info(buildMessage(arg0, args));
		}

	}

	public void info(String arg0, Object arg1) {
		Object[] args = new Object[] { arg1 };
		if (isInfoEnabled()) {
			this.logCat.info(buildMessage(arg0, args));
		}

	}

	public void info(String arg0, Object[] arg1) {
		Object[] args = arg1;
		if (isInfoEnabled()) {
			this.logCat.info(buildMessage(arg0, args));
		}

	}

	public void info(String arg0, Throwable arg1) {
		if (isInfoEnabled()) {
			this.logCat.info(arg0, arg1);
		}
	}

}
