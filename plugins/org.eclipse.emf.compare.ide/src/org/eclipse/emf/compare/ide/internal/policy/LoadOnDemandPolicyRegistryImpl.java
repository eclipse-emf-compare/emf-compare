/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.policy;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy;

/**
 * A default implementation that uses a map internally.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class LoadOnDemandPolicyRegistryImpl implements ILoadOnDemandPolicy.Registry {

	/** The store of registered policy. */
	private final Map<String, ILoadOnDemandPolicy> map;

	/**
	 * Creates a new implementation.
	 */
	public LoadOnDemandPolicyRegistryImpl() {
		map = new ConcurrentHashMap<String, ILoadOnDemandPolicy>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry#hasAnyAuthorizingPolicy(org.eclipse.emf.common.util.URI)
	 */
	public boolean hasAnyAuthorizingPolicy(URI uri) {
		for (ILoadOnDemandPolicy policy : getPolicies()) {
			if (policy.isAuthorizing(uri)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry#getPolicies()
	 */
	public List<ILoadOnDemandPolicy> getPolicies() {
		return newArrayList(map.values());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry#addPolicy(org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy)
	 */
	public ILoadOnDemandPolicy addPolicy(ILoadOnDemandPolicy policy) {
		return map.put(policy.getClass().getName(), policy);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry#removePolicy(java.lang.String)
	 */
	public ILoadOnDemandPolicy removePolicy(String className) {
		return map.remove(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry#clear()
	 */
	public void clear() {
		map.clear();
	}

}