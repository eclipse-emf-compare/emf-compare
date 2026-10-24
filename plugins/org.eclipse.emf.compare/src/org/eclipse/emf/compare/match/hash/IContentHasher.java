/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.hash;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EObject;

/**
 * Supplies content snapshots for one comparison, allowing domain-specific fingerprinting. The matcher calls
 * {@link #initialize(Comparison)} before requesting any fingerprints. Implementations must not resolve
 * proxies while capturing content. Deferred similarity computations must use captured values so later model
 * edits cannot change an existing fingerprint.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface IContentHasher {

	/**
	 * Starts a comparison and supplies its existing resource correspondence. Implementations with
	 * per-comparison caches should reset them here. The default implementation has no state to reset.
	 *
	 * @param comparison
	 *            the comparison in progress
	 */
	default void initialize(Comparison comparison) {
	}

	/**
	 * Returns a stable content snapshot for an object in the current comparison, without resolving proxies.
	 * Any deferred computation must also avoid rereading the model.
	 *
	 * @param object
	 *            the model object
	 * @return the content fingerprint
	 */
	Fingerprint fingerprint(final EObject object);
}
