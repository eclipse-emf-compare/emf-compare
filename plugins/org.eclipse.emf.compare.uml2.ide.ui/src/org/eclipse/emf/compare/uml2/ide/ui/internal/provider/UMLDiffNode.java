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
package org.eclipse.emf.compare.uml2.ide.ui.internal.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.ide.ui.internal.accessor.UMLIDEManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.uml2.ide.ui.internal.accessor.UMLIDESingleStructuralFeatureAccessorImpl;
import org.eclipse.emf.ecore.EReference;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLDiffNode extends DiffNode {

	/**
	 * @param adapterFactory
	 */
	public UMLDiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode#getTarget()
	 */
	@Override
	public UMLDiff getTarget() {
		return (UMLDiff)super.getTarget();
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
		UMLDiff diff = getTarget();
		switch (diff.getKind()) {
			case ADD:
			case DELETE:
			case MOVE:
				EReference eReference = diff.getEReference();
				if (eReference.isMany()) {
					ret = new UMLIDEManyStructuralFeatureAccessorImpl(diff, side);
				} else {
					ret = new UMLIDESingleStructuralFeatureAccessorImpl(diff, side);
				}
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
