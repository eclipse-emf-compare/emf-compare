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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.MatchItemProvider;

/**
 * Specialized {@link MatchItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider {

	/**
	 * Constructor calling super {@link #MatchItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		Match match = (Match)object;
		Object ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = super.getImage(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		Match match = (Match)object;
		String ret = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = super.getText(object);
		}

		return ret;
	}
}
