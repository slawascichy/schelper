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

import javax.naming.Context;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * JndiParametersSupport - klasa wspierająca obsługę kontekstu JNDI.
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public abstract class JndiParametersSupport implements JndiParameters {

	protected final static Logger log = LoggerFactory
			.getLogger(JndiParametersSupport.class);

	private String jnpDisableDiscovery;

	private String providerUrl;

	private String urlPkgPrefixes;

	private String initialContextFactory;

	public String getJnpDisableDiscovery() {
		return JndiParametersUtils.getJnpDisableDiscovery(jnpDisableDiscovery);
	}

	public void setJnpDisableDiscovery(String jnpDiscovery) {
		this.jnpDisableDiscovery = jnpDiscovery;
	}

	public String getProviderUrl() {
		return JndiParametersUtils.getProviderUrl(providerUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.jndi.JndiParameters#setProviderUrl(java.lang.String)
	 */
	public void setProviderUrl(String providerUrl) {
		log.debug("\n\n=========================" + "\nI set ProviderUrl: {} "
				+ "\n=========================\n", providerUrl);
		this.providerUrl = providerUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.jndi.JndiParameters#getUrlPkgPrefixes()
	 */
	public String getUrlPkgPrefixes() {
		return JndiParametersUtils.getUrlPkgPrefixes(urlPkgPrefixes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.jndi.JndiParameters#setUrlPkgPrefixes(java.lang.String)
	 */
	public void setUrlPkgPrefixes(String urlPkgPrefixes) {
		this.urlPkgPrefixes = urlPkgPrefixes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.jndi.JndiParameters#getInitialContextFactory()
	 */
	public String getInitialContextFactory() {
		return JndiParametersUtils
				.getInitialContextFactory(initialContextFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.wp.andro.jndi.JndiParameters#setInitialContextFactory(java.lang.String
	 * )
	 */
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.wp.andro.jndi.JndiParameters#getEnv()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable getEnv() {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory());
		env.put(Context.PROVIDER_URL, getProviderUrl());
		env.put(JndiParameters.JNP_DISABLEDISCOVERY, getJnpDisableDiscovery());
		env.put(Context.URL_PKG_PREFIXES, getUrlPkgPrefixes());
		return env;
	}

}
