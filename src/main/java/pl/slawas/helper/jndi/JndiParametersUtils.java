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

import javax.naming.Context;

/**
 * 
 * JndiParametersUtils klasa z metodami analizy parametrów ustwień kontekstu
 * JNDI
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 */
public class JndiParametersUtils {

	/**
	 * Metoda analizy ustwienia właściwości jnpDisableDiscovery
	 * 
	 * @see JndiParameters#JNP_DISABLEDISCOVERY
	 * 
	 * @param jnpDisableDiscovery
	 * @return wartość parametru
	 */
	public static String getJnpDisableDiscovery(String jnpDisableDiscovery) {
		String sysProperty = System.getProperty(JndiParameters.ANDRO_JNP_DISABLEDISCOVERY);
		String jnpProperty = System.getProperty(JndiParameters.JNP_DISABLEDISCOVERY);
		return (jnpDisableDiscovery != null ? jnpDisableDiscovery
				: (sysProperty != null ? sysProperty
				: (jnpProperty != null ? jnpProperty
				: JndiParameters.DEFAULT_JNP_DISABLEDISCOVERY)));
	}

	/**
	 * Metoda analizy ustwienia właściwości providerUrl.
	 * 
	 * @see Context#PROVIDER_URL
	 * 
	 * @param providerUrl
	 * @return wartość parametru
	 */
	public static String getProviderUrl(String providerUrl) {
		String sysProperty = System.getProperty(JndiParameters.ANDRO_PROVIDER_URL);
		String jndiProperty = System.getProperty(Context.PROVIDER_URL);
		return (providerUrl != null ? providerUrl
				: (sysProperty != null ? sysProperty
				: (jndiProperty != null ? jndiProperty
				: JndiParameters.DEFAULT_PROVIDER_URL)));
	}

	/**
	 * Metoda analizy ustwienia właściwości urlPkgPrefixes.
	 * 
	 * @see Context#URL_PKG_PREFIXES
	 * 
	 * @param urlPkgPrefixes
	 * @return wartość parametru
	 */
	public static String getUrlPkgPrefixes(String urlPkgPrefixes) {
		String sysProperty = System.getProperty(JndiParameters.ANDRO_URL_PKG_PRFIXES);
		String jndiProperty = System.getProperty(Context.URL_PKG_PREFIXES);
		return (urlPkgPrefixes != null ? urlPkgPrefixes
				: (sysProperty != null ? sysProperty
				: (jndiProperty != null ? jndiProperty
				: JndiParameters.DEFAULT_URL_PKG_PRFIXES)));
	}

	/**
	 * Metoda analizy ustwienia właściwości initialContextFactory.
	 * 
	 * @see Context#INITIAL_CONTEXT_FACTORY
	 * 
	 * @param initialContextFactory
	 * @return wartość parametru
	 */
	public static String getInitialContextFactory(String initialContextFactory) {
		String sysProperty = System.getProperty(JndiParameters.ANDRO_INITIAL_CONTEXT_FACTORY);
		String jndiProperty = System
				.getProperty(Context.INITIAL_CONTEXT_FACTORY);
		return (initialContextFactory != null ? initialContextFactory
				: (sysProperty != null ? sysProperty
				: (jndiProperty != null ? jndiProperty
				: JndiParameters.DEFAULT_INITIAL_CONTEXT_FACTORY)));
	}

}
