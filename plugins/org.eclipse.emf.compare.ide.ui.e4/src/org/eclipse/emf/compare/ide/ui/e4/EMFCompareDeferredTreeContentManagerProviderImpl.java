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
package org.eclipse.emf.compare.ide.ui.e4;

import org.eclipse.emf.compare.ide.ui.internal.treecontentmanager.EMFCompareDeferredTreeContentManager;
import org.eclipse.emf.compare.ide.ui.internal.treecontentmanager.EMFCompareDeferredTreeContentManagerProvider;
import org.eclipse.jface.viewers.AbstractTreeViewer;

/**
 * Service implementation to return the e4 version of
 * {@link org.eclipse.ui.progress.DeferredTreeContentManager DeferredTreeContentManager} which is
 * {@link E4DeferredTreeContentManager}.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class EMFCompareDeferredTreeContentManagerProviderImpl implements EMFCompareDeferredTreeContentManagerProvider {

	/**
	 * {@inheritDoc}
	 */
	public EMFCompareDeferredTreeContentManager createEMFDeferredTreeContentManager(AbstractTreeViewer viewer) {
		return new E4DeferredTreeContentManager(viewer);
	}

}
