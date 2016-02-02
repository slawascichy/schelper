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
package pl.slawas.test.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import pl.slawas.helpers.Configurations;
import pl.slawas.helpers.Strings;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

public class ConfigurationsTest extends TestCase {

	@SuppressWarnings("unused")
	final private static Logger log = LoggerFactory
			.getLogger(ConfigurationsTest.class);

	private static final String SYSTEM_TEST_PROPERTY_NEME = "SYS_TEST_VALUE";
	private static final String SYSTEM_TEST_PROPERTY_EXPECTED_VALUE = "testValue";
	private String getSysEnvVarErrorMassage = "I can't find system property '"
			+ SYSTEM_TEST_PROPERTY_NEME
			+ "="
			+ SYSTEM_TEST_PROPERTY_EXPECTED_VALUE
			+ "'. Please try run following command in yours console command line: ";

	private static final String APP_TEST_PROPERTY_NEME = "cacheParams.CacheName";
	private static final String APP_TEST_PROPERTY_EXPECTED_VALUE = "TestCacheName";

	@Before
	public void setUp() throws IOException {
		String OS = System.getProperty("os.name").toLowerCase();
		if ((OS.indexOf("windows 9") > -1) || (OS.indexOf("nt") > -1)
				|| (OS.indexOf("windows 2000") > -1)
				|| (OS.indexOf("windows xp") > -1)) {
			getSysEnvVarErrorMassage += "'set " + SYSTEM_TEST_PROPERTY_NEME
					+ "=" + SYSTEM_TEST_PROPERTY_EXPECTED_VALUE
					+ "' and tay again.";
		} else {
			getSysEnvVarErrorMassage += "'export " + SYSTEM_TEST_PROPERTY_NEME
					+ "=" + SYSTEM_TEST_PROPERTY_EXPECTED_VALUE
					+ "' and tay again.";
		}
	}

	@Test
	public void testLoadSystemProperties() throws IOException {
		Properties sysProp = Configurations.loadSystemProperties();
		/*
		 * Enumeration keys = sysProp.keys(); while (keys.hasMoreElements()) {
		 * String key = (String) keys.nextElement(); String value =
		 * sysProp.getProperty(key); log.debug("{} = {}", new Object[] {key,
		 * value}); }
		 */
		Assert.assertEquals(
				"\n" + Strings.breakText(getSysEnvVarErrorMassage, 60) + "\n",
				SYSTEM_TEST_PROPERTY_EXPECTED_VALUE,
				sysProp.get(SYSTEM_TEST_PROPERTY_NEME));
	}

	@Test
	public void testLoadProperties() throws FileNotFoundException, IOException {

		Properties appProp = Configurations.loadProperties(getClass(),
				"/test.properties");
		/*
		 * Enumeration<Object> keys = appProp.keys(); while
		 * (keys.hasMoreElements()) { String key = (String) keys.nextElement();
		 * String value = appProp.getProperty(key); log.debug("{} = {}", new
		 * Object[] {key, value}); }
		 */
		Assert.assertEquals(
				"\nCan't find property in file '/test.properties'\n",
				APP_TEST_PROPERTY_EXPECTED_VALUE,
				appProp.get(APP_TEST_PROPERTY_NEME));

	}

	@Test
	public void testLoadHashtable() throws FileNotFoundException, IOException {

		Map<String, String> appProp = Configurations.loadHashtable(getClass(),
				"/test.properties");
		/*
		 * Enumeration<String> keys = appProp.keys(); while
		 * (keys.hasMoreElements()) { String key = (String) keys.nextElement();
		 * String value = appProp.get(key); log.debug("{} = {}", new Object[]
		 * {key, value}); }
		 */
		Assert.assertEquals(
				"\nCan't find property in file '/test.properties'\n",
				APP_TEST_PROPERTY_EXPECTED_VALUE,
				appProp.get(APP_TEST_PROPERTY_NEME));

	}

}
