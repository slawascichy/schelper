/*
 * Copyright (c) 2007-2008 the original author or authors. All rights reserved.
 * 
 * Redistribution and use in source forms is strictly forbidden.
 * 
 * Redistribution and use in binary forms are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions in binary form must reproduce this license file unchanged
 * in the documentation and other materials provided with the distribution.
 * 
 * 2. Web distribution in binary form is strictly forbidden without specific
 * prior written permission from the original author or authors! The original
 * distribution files must not be renamed or modified.
 * 
 * THIS PRODUCT IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.slawas.helper.jndi;

import java.util.Hashtable;

/**
 * 
 * JndiParameters - parametry systemowe - ustawianie poprzez argument maszyny
 * wirtualnej Java np. {@code -Dschelper.jnp.disableDiscovery=true}.
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public interface JndiParameters {

	/**
	 * Automatic Discovery of HA-JNDI Servers
	 * 
	 * Constant that holds the name of flag indicating that the discovery
	 * process should not be attempted.
	 * 
	 * The value of this constant is "schelper.jnp.disableDiscovery"
	 */
	public static final String SCHELPER_JNP_DISABLEDISCOVERY = "schelper.jnp.disableDiscovery";
	/** Domyślna nazwa parametru Automatic Discovery of HA-JNDI Servers */
	public static final String JNP_DISABLEDISCOVERY = "jnp.disableDiscovery";

	/**
	 * Constant that holds the name of the environment property for specifying
	 * configuration information for the service provider to use. The value of
	 * the property should contain a URL string (e.g. "ldap://somehost:389").
	 * This property may be specified in the environment, an applet parameter, a
	 * system property, or a resource file. If it is not specified in any of
	 * these sources, the default configuration is determined by the service
	 * provider.
	 * 
	 * The value of this constant is "schelper.naming.provider.url".
	 */
	public static final String SCHELPER_PROVIDER_URL = "schelper.naming.provider.url";

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
	 * The value of this constant is "schelper.naming.factory.url.pkgs".
	 */
	public static final String SCHELPER_URL_PKG_PRFIXES = "schelper.naming.factory.url.pkgs";

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
	 * The value of this constant is "schelper.naming.factory.initial".
	 */
	public static final String SCHELPER_INITIAL_CONTEXT_FACTORY = "schelper.naming.factory.initial";

	public static final String DEFAULT_JNP_DISABLEDISCOVERY = "true";

	public static final String DEFAULT_PROVIDER_URL = "localhost:1099";

	public static final String DEFAULT_URL_PKG_PRFIXES = "org.jboss.naming:org.jnp.interfaces";

	public static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";

	public abstract String getJnpDisableDiscovery();

	public abstract void setJnpDisableDiscovery(String jnpDiscovery);

	public abstract String getProviderUrl();

	public abstract void setProviderUrl(String providerUrl);

	public abstract String getUrlPkgPrefixes();

	public abstract void setUrlPkgPrefixes(String urlPkgPrefixes);

	public abstract String getInitialContextFactory();

	public abstract void setInitialContextFactory(String initialContextFactory);

	@SuppressWarnings("rawtypes")
	public abstract Hashtable getEnv();

}