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

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Util Class for handling {@link EMFCompareDeferredTreeContentManagerProvider} service.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public final class EMFCompareDeferredTreeContentManagerUtil {

	/**
	 * No constructor for Util classes.
	 */
	private EMFCompareDeferredTreeContentManagerUtil() {
	}

	/**
	 * Uses the {@link EMFCompareDeferredTreeContentManagerProvider} service to create an
	 * {@link EMFCompareDeferredTreeContentManager}.
	 * 
	 * @param viewer
	 *            The viewer with which the {@link EMFCompareDeferredTreeContentManager} is created.
	 * @return The created {@link EMFCompareDeferredTreeContentManager}.
	 */
	// suppress warnings which are caused by compatibility to helios
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static EMFCompareDeferredTreeContentManager createEMFDeferredTreeContentManager(
			AbstractTreeViewer viewer) {
		BundleContext bundleContext = EMFCompareIDEUIPlugin.getDefault().getBundle().getBundleContext();
		ServiceReference reference = bundleContext
				.getServiceReference(EMFCompareDeferredTreeContentManagerProvider.class.getName());
		if (reference != null) {
			EMFCompareDeferredTreeContentManagerProvider service = (EMFCompareDeferredTreeContentManagerProvider)bundleContext
					.getService(reference);
			return service.createEMFDeferredTreeContentManager(viewer);
		} else {
			// default implementation
			return new EMFCompareDeferredTreeContentManagerImpl(viewer);
		}
	}
}
