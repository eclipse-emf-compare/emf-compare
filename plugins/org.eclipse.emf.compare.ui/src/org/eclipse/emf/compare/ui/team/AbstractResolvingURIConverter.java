/*******************************************************************************
 * Copyright (c) 2010, 2011  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.team;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

/**
 * A converter, which performs automatic resolving of relative resources during normalization.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public abstract class AbstractResolvingURIConverter extends ExtensibleURIConverterImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#normalize(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public URI normalize(URI uri) {
		URI normalizedUri = null;
		if (uri.segmentCount() > 0 && uri.isRelative()) {
			try {
				normalizedUri = super.normalize(resolve(uri));
			} catch (CoreException e) {
				// cannot do anything here, let super implementation
				// return a valid value
				normalizedUri = super.normalize(uri);
			}
		} else {
			normalizedUri = super.normalize(uri);
		}
		return normalizedUri;
	}

	/**
	 * Called to delegate resvoling of a given relative uri to an absolute one.
	 * 
	 * @param relativeUri
	 *            the relative uri to resolve.
	 * @return the resolved absolute uri
	 * @throws CoreException
	 *             in case resolving could not be successfully performed.
	 */
	protected abstract URI resolve(URI relativeUri) throws CoreException;
}
