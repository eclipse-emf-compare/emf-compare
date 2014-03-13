/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.factory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.ManyStructuralFeatureAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLStereotypeManyStructuralFeatureChangeAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * This will be in charge of creating the accessor for stereotype many reference changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeManyReferenceChangeAccessorFactory extends ManyStructuralFeatureAccessorFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	@Override
	public boolean isFactoryFor(Object target) {
		if (target instanceof StereotypeReferenceChange) {
			EObject discriminant = ((StereotypeReferenceChange)target).getDiscriminant();
			if (discriminant instanceof EReference) {
				EReference ref = (EReference)discriminant;
				return ref.isMany();
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createLeft(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return new UMLStereotypeManyStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeReferenceChange)target, MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new UMLStereotypeManyStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeReferenceChange)target, MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new UMLStereotypeManyStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeReferenceChange)target, MergeViewerSide.ANCESTOR);
	}
}
