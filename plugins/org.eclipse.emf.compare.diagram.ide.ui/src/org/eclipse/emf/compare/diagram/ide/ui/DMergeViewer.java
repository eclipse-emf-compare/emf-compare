/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui;

import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.viewers.ISelection;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class DMergeViewer extends AbstractEditPartViewer {

	private final MergeViewerSide fSide;

	/**
	 * 
	 */
	public DMergeViewer(MergeViewerSide side) {
		fSide = side;
	}

	/**
	 * Returns the wrapped {@link AbstractEditPartViewer}.
	 * 
	 * @return
	 */
	protected abstract AbstractEditPartViewer getGraphicalViewer();

	/**
	 * {@inheritDoc}
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return getGraphicalViewer().getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		getGraphicalViewer().setSelection(selection);
	}

	public static enum MergeViewerSide {
		LEFT, RIGHT, ANCESTOR;

		public MergeViewerSide opposite() {
			switch (this) {
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;
				case ANCESTOR:
					return ANCESTOR;
				default:
					throw new IllegalStateException(); // happy compiler :)
			}
		}
	}

}
