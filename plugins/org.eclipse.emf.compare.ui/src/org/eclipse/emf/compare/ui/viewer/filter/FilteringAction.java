/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.filter;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ui.viewer.AbstractOrderingAction;
import org.eclipse.emf.compare.ui.viewer.OrderingListener;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Action to filter difference elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class FilteringAction extends AbstractOrderingAction {
	/** Descriptor for filters. */
	private DifferenceFilterDescriptor mDesc;

	/**
	 * Constructor.
	 * 
	 * @param desc
	 *            The descriptor of filters.
	 * @param viewer
	 *            The viewer.
	 */
	public FilteringAction(DifferenceFilterDescriptor desc, ParameterizedStructureMergeViewer viewer) {
		super(desc.getName(), IAction.AS_CHECK_BOX, viewer);
		mDesc = desc;
		try {
			final ImageDescriptor imgDesc = ImageDescriptor.createFromURL(FileLocator.toFileURL(Platform
					.getBundle("org.eclipse.emf.compare.diff.edit") //$NON-NLS-1$
					.getEntry("icons/full/obj16/AddModelElement.gif"))); //$NON-NLS-1$
			setImageDescriptor(imgDesc);
		} catch (IOException e) {
			// No management
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.AbstractOrderingAction#doRun(org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider)
	 */
	@Override
	protected void doRun(ParameterizedStructureContentProvider provider) {
		if (isChecked()) {
			mViewer.fireOrderingChanged(OrderingListener.ADD_FILTER_EVENT, mDesc);
		} else {
			mViewer.fireOrderingChanged(OrderingListener.REMOVE_FILTER_EVENT, mDesc);
		}
	}
}
