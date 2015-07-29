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
package org.eclipse.emf.compare.ide.ui.internal.treecontentmanager;

import org.eclipse.jface.viewers.AbstractTreeViewer;

/**
 * Provider Service for {@link EMFCompareDeferredTreeContentManager}s.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public interface EMFCompareDeferredTreeContentManagerProvider {
	/**
	 * Provides a {@link EMFCompareDeferredTreeContentManager} for the given viewer.
	 * 
	 * @param viewer
	 *            The viewer with which the {@link EMFCompareDeferredTreeContentManager} is created.
	 * @return The created {@link EMFCompareDeferredTreeContentManager}.
	 */
	public EMFCompareDeferredTreeContentManager createEMFDeferredTreeContentManager(AbstractTreeViewer viewer);
}
