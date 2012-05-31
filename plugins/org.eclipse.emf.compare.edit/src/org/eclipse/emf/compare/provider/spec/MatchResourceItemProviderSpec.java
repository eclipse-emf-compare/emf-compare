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
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.provider.MatchResourceItemProvider;

/**
 * Specialized {@link MatchResourceItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchResourceItemProviderSpec extends MatchResourceItemProvider {

	/**
	 * Constructor calling super {@link #MatchResourceItemProviderSpec(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchResourceItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchResourceItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		MatchResource matchResource = (MatchResource)object;
		return matchResource.getLeftURI() + " <-> " + matchResource.getRightURI() + " ("
				+ matchResource.getOriginURI() + ")";
	}

}
