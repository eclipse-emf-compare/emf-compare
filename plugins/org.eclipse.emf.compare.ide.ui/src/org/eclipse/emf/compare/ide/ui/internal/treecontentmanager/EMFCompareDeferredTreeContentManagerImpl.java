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

import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;

/**
 * The default implementation of {@link EMFCompareDeferredTreeContentManager} forwards to
 * {@link DeferredTreeContentManager}.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class EMFCompareDeferredTreeContentManagerImpl implements EMFCompareDeferredTreeContentManager {

	private DeferredTreeContentManager manager;

	/**
	 * Constructor.
	 */
	public EMFCompareDeferredTreeContentManagerImpl(AbstractTreeViewer viewer) {
		manager = new DeferredTreeContentManager(viewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addUpdateCompleteListener(IJobChangeListener listener) {
		manager.addUpdateCompleteListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getChildren(Object parent) {
		return manager.getChildren(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUpdateCompleteListener(IJobChangeListener listener) {
		manager.removeUpdateCompleteListener(listener);
	}

}
