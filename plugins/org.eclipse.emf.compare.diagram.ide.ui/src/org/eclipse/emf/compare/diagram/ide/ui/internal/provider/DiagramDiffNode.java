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
package org.eclipse.emf.compare.diagram.ide.ui.internal.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramIDEManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramDiffNode extends DiffNode {

	/**
	 * @param adapterFactory
	 */
	public DiagramDiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode#getTarget()
	 */
	@Override
	public DiagramDiff getTarget() {
		return (DiagramDiff)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITypedElement getLeft() {
		return getAccessor(MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITypedElement getRight() {
		return getAccessor(MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITypedElement getAncestor() {
		return getAccessor(MergeViewerSide.ANCESTOR);
	}

	private ITypedElement getAccessor(MergeViewerSide side) {
		ITypedElement ret = null;
		DiagramDiff diff = getTarget();
		switch (diff.getKind()) {
			case ADD:
			case DELETE:
			case MOVE:
				ret = new DiagramIDEManyStructuralFeatureAccessorImpl(diff, side);
				break;
			case CHANGE:
				// TODO: what to do in change ?
				break;
			default:
				throw new IllegalStateException();
		}
		return ret;
	}

}
