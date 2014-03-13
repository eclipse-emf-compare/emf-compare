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
package org.eclipse.emf.compare.rcp.policy;

import java.util.List;

import org.eclipse.emf.common.util.URI;

/**
 * A policy that can be asked to if it is authorizing resource to be loaded on demand.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface ILoadOnDemandPolicy {

	/**
	 * Returns true if this policy is authorizing the resource with the given URI to be loaded on demand,
	 * false either.
	 * 
	 * @param uri
	 *            the URI to be loaded on demand.
	 * @return true if this policy is authorizing the resource with the given URI to be loaded on demand,
	 *         false either.
	 */
	boolean isAuthorizing(URI uri);

	/**
	 * A registry of policy. It can be asked if it contains at least one policy authorizing an URI to be
	 * loaded on demand.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	interface Registry {

		/**
		 * Returns true if at least one of the contained policy is authorizing the given policy to be loaded
		 * on demand.
		 * 
		 * @param uri
		 *            the URI to be tested.
		 * @return true if at least one of the contained policy is authorizing the given policy to be loaded
		 *         on demand.
		 */
		boolean hasAnyAuthorizingPolicy(URI uri);

		/**
		 * Returns the list of registered policies.
		 * 
		 * @return the list of registered policies.
		 */
		List<ILoadOnDemandPolicy> getPolicies();

		/**
		 * Add the given {@code policy} to this registry.
		 * 
		 * @param policy
		 *            the policy to be added.
		 * @return the previous value associated with the class name of the given {@code policy}.
		 */
		ILoadOnDemandPolicy addPolicy(ILoadOnDemandPolicy policy);

		/**
		 * Removes the policy registered within this registry with the given class name.
		 * 
		 * @param className
		 *            the class name of a previously registered policy.
		 * @return the previously registered policy or null if none was.
		 */
		ILoadOnDemandPolicy removePolicy(String className);

		/**
		 * Removes all of the registered policies.
		 */
		void clear();
	}
}
