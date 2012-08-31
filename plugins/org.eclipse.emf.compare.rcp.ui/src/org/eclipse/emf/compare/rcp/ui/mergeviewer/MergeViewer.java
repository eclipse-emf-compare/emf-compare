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
package org.eclipse.emf.compare.rcp.ui.mergeviewer;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class MergeViewer extends ContentViewer {

	private final MergeViewerSide fSide;

	/**
	 * 
	 */
	public MergeViewer(MergeViewerSide side) {
		fSide = side;
	}

	/**
	 * Returns the wrapped {@link StructuredViewer}.
	 * 
	 * @return
	 */
	protected abstract StructuredViewer getStructuredViewer();

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
		return getStructuredViewer().getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		getStructuredViewer().setSelection(selection, reveal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		getStructuredViewer().setContentProvider(contentProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
		getStructuredViewer().setLabelProvider(labelProvider);
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
