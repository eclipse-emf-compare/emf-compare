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
package org.eclipse.emf.compare.ui.viewer;

import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;

/**
 * Define an action to launch any execution to filter or group difference elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public abstract class AbstractOrderingAction extends Action {
	/** Viewer linked to this action. */
	protected ParameterizedStructureMergeViewer mViewer;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of the action.
	 * @param style
	 *            The style of the action (see org.eclipse.jface.action.IAction).
	 * @param viewer
	 *            The viewer containing the action.
	 */
	public AbstractOrderingAction(String name, int style, ParameterizedStructureMergeViewer viewer) {
		super(name, style);
		mViewer = viewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final IContentProvider provider = mViewer.getContentProvider();
		if (provider instanceof ParameterizedStructureContentProvider) {
			doRun((ParameterizedStructureContentProvider)provider);
			mViewer.refresh();
		}
	}

	/**
	 * Run the action in relation to the ordering kind.
	 * 
	 * @param provider
	 *            the content provider.
	 */
	protected abstract void doRun(ParameterizedStructureContentProvider provider);
}
