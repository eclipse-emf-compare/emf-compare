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

import static com.google.common.collect.Iterables.filter;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IDEManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IDESingleStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;
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
				EObject discriminant = diff.getDiscriminant();
				EList<Diff> differences = diff.getMatch().getComparison().getDifferences(discriminant);
				for (ReferenceChange referenceChange : filter(differences, ReferenceChange.class)) {
					if (referenceChange.getKind() == diff.getKind()) {
						EReference reference = referenceChange.getReference();
						if (reference == diff.getEReference() && referenceChange.getValue() == discriminant) {
							if (reference.isMany()) {
								ret = new IDEManyStructuralFeatureAccessorImpl(referenceChange, side);
							} else {
								ret = new IDESingleStructuralFeatureAccessorImpl(referenceChange, side);
							}
							break;
						}
					}
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
