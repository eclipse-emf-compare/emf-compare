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

/**
 * A wrapper interface for {@link org.eclipse.ui.progress.DeferredTreeContentManager
 * DeferredTreeContentManager} which enables EMFCompare to switch between its own and the default platform
 * implementation. This is required to detach EMFCompare from
 * {@link org.eclipse.ui.progress.DeferredTreeContentManager DeferredTreeContentManager}'s dependency to the
 * workbench and therefore enables EMFCompare to support pure E4 applications.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public interface EMFCompareDeferredTreeContentManager {

	/**
	 * @see org.eclipse.ui.progress.DeferredTreeContentManager#addUpdateCompleteListener(IJobChangeListener)
	 */
	public void addUpdateCompleteListener(IJobChangeListener listener);

	/**
	 * @see org.eclipse.ui.progress.DeferredTreeContentManager#getChildren(Object)
	 */
	public Object[] getChildren(final Object parent);

	/**
	 * @see org.eclipse.ui.progress.DeferredTreeContentManager#removeUpdateCompleteListener(IJobChangeListener)
	 */
	public void removeUpdateCompleteListener(IJobChangeListener listener);
}
