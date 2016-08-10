/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProviderConfiguration;

/**
 * Implementation of {@link IMergeViewerItemProvider} which extracts the {@link IMergeViewerItem}s from
 * {@link ICompareAccessor}s.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 * @since 4.3
 */
public class CompareAccessorMergeViewerItemProvider implements IMergeViewerItemProvider {

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getMergeViewerItems(Object object,
			IMergeViewerItemProviderConfiguration configuration) {
		if (object instanceof ICompareAccessor) {
			return new ArrayList<Object>(((ICompareAccessor)object).getItems());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IMergeViewerItem getItemToSelect(Object object,
			IMergeViewerItemProviderConfiguration configuration) {
		if (object instanceof ICompareAccessor) {
			return ((ICompareAccessor)object).getInitialItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canHandle(Object object) {
		return ICompareAccessor.class.isInstance(object);
	}

}
