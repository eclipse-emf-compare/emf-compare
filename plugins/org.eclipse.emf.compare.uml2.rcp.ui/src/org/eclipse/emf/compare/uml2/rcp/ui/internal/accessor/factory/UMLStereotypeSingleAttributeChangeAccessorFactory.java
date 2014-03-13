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
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.SingleStructuralFeatureAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLStereotypeSingleStructuralFeatureChangeAccessor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * This will be in charge of creating the accessor for stereotype single attribute changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeSingleAttributeChangeAccessorFactory extends SingleStructuralFeatureAccessorFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	@Override
	public boolean isFactoryFor(Object target) {
		if (target instanceof StereotypeAttributeChange) {
			EObject discriminant = ((StereotypeAttributeChange)target).getDiscriminant();
			if (discriminant instanceof EAttribute) {
				EAttribute attribute = (EAttribute)discriminant;
				return !(attribute.getEAttributeType().getInstanceClass() == String.class)
						&& !attribute.isMany();
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
		return new UMLStereotypeSingleStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeAttributeChange)target, MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new UMLStereotypeSingleStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeAttributeChange)target, MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new UMLStereotypeSingleStructuralFeatureChangeAccessor(adapterFactory,
				(StereotypeAttributeChange)target, MergeViewerSide.ANCESTOR);
	}
}
