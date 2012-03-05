/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import org.eclipse.emf.compare.ui.viewer.OrderingListener;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;

/**
 * An {@link OrderingListener} which listens to an ordering event to update the content provider linked to the
 * structure viewer.
 * 
 * @deprecated
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
@Deprecated
public class UpdateStructureListener implements OrderingListener {
	/** The viewer. */
	private ContentViewer mViewer;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            The viewer.
	 */
	public UpdateStructureListener(ContentViewer viewer) {
		mViewer = viewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.OrderingListener#notifyChanged(int, java.lang.Object)
	 */
	public void notifyChanged(int event, Object descriptor) {
		final IContentProvider provider = mViewer.getContentProvider();
		if (provider instanceof ParameterizedStructureContentProvider
				&& descriptor instanceof DifferenceFilterDescriptor) {
			final ParameterizedStructureContentProvider structureProvider = (ParameterizedStructureContentProvider)provider;
			final DifferenceFilterDescriptor desc = (DifferenceFilterDescriptor)descriptor;
			switch (event) {
				case ADD_FILTER_EVENT:
					structureProvider.addSelectedFilter(desc.getExtension());
					break;

				case REMOVE_FILTER_EVENT:
					structureProvider.removeSelectedFilter(desc.getExtension());
					break;
				default:
					break;
			}
		}
	}
}
