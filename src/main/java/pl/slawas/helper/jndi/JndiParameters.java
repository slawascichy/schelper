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
package pl.slawas.helper.jndi;

import java.util.Hashtable;

public interface JndiParameters {

	/**
	 * Automatic Discovery of HA-JNDI Servers
	 * 
	 * Constant that holds the name of flag indicating that the discovery
	 * process should not be attempted.
	 * 
	 * The value of this constant is "andro.jnp.disableDiscovery"
	 */
	public static final String ANDRO_JNP_DISABLEDISCOVERY = "andro.jnp.disableDiscovery";

	/**
	 * Constant that holds the name of the environment property for specifying
	 * configuration information for the service provider to use. The value of
	 * the property should contain a URL string (e.g. "ldap://somehost:389").
	 * This property may be specified in the environment, an applet parameter, a
	 * system property, or a resource file. If it is not specified in any of
	 * these sources, the default configuration is determined by the service
	 * provider.
	 * 
	 * The value of this constant is "andro.naming.provider.url".
	 */
	public static final String ANDRO_PROVIDER_URL = "andro.naming.provider.url";

	/**
	 * Constant that holds the name of the environment property for specifying
	 * the list of package prefixes to use when loading in URL context
	 * factories. The value of the property should be a colon-separated list of
	 * package prefixes for the class name of the factory class that will create
	 * a URL context factory. This property may be specified in the environment,
	 * an applet parameter, a system property, or one or more resource files.
	 * The prefix com.sun.jndi.url is always appended to the possibly empty list
	 * of package prefixes.
	 * 
	 * The value of this constant is "andro.naming.factory.url.pkgs".
	 */
	public static final String ANDRO_URL_PKG_PRFIXES = "andro.naming.factory.url.pkgs";

	/**
	 * Constant that holds the name of the environment property for specifying
	 * the initial context factory to use. The value of the property should be
	 * the fully qualified class name of the factory class that will create an
	 * initial context. This property may be specified in the environment
	 * parameter passed to the initial context constructor, an applet parameter,
	 * a system property, or an application resource file. If it is not
	 * specified in any of these sources, NoInitialContextException is thrown
	 * when an initial context is required to complete an operation.
	 * 
	 * The value of this constant is "andro.naming.factory.initial".
	 */
	public static final String ANDRO_INITIAL_CONTEXT_FACTORY = "andro.naming.factory.initial";

	/**
	 * Automatic Discovery of HA-JNDI Servers
	 * 
	 * Constant that holds the name of flag indicating that the discovery
	 * process should not be attempted.
	 * 
	 * The value of this constant is "jnp.disableDiscovery"
	 */
	public static final String JNP_DISABLEDISCOVERY = "jnp.disableDiscovery";

	public static final String DEFAULT_JNP_DISABLEDISCOVERY = "true";

	public static final String DEFAULT_PROVIDER_URL = "localhost:1099";

	public static final String DEFAULT_URL_PKG_PRFIXES = "org.jboss.naming:org.jnp.interfaces";

	public static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";

	public abstract String getJnpDisableDiscovery();

	public abstract String getProviderUrl();

	public abstract String getUrlPkgPrefixes();

	public abstract String getInitialContextFactory();

	@SuppressWarnings("rawtypes")
	public abstract Hashtable getJndiEnv();

}