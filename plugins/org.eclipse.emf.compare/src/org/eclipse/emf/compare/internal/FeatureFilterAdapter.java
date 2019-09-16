/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.diff.FeatureFilter;

/**
 * This will be used to attach the FeatureFilter to its comparison so that it can be used after the diff
 * process.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FeatureFilterAdapter extends AdapterImpl {
	/** The wrapped feature filter. */
	private final FeatureFilter featureFilter;

	/**
	 * Constructor.
	 * 
	 * @param featureFilter
	 *            The wrapped feature filter.
	 */
	public FeatureFilterAdapter(FeatureFilter featureFilter) {
		this.featureFilter = featureFilter;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == FeatureFilterAdapter.class;
	}

	public FeatureFilter getFeatureFilter() {
		return featureFilter;
	}
}
