/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.StringAttributeChangeAccessorFactory;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLStereotypeStringAttributeChangeAccessor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * This will be in charge of creating the accessor for stereotype property changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeStringAttributeChangeAccessorFactory extends StringAttributeChangeAccessorFactory {

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
				return attribute.getEAttributeType().getInstanceClass() == String.class
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
		StereotypeAttributeChange change = (StereotypeAttributeChange)target;
		Diff refined = change.getRefinedBy().get(0);
		EObject left = refined.getMatch().getLeft();
		if (left != null) {
			return new UMLStereotypeStringAttributeChangeAccessor(left, (StereotypeAttributeChange)target);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		StereotypeAttributeChange change = (StereotypeAttributeChange)target;
		Diff refined = change.getRefinedBy().get(0);
		EObject right = refined.getMatch().getRight();
		if (right != null) {
			return new UMLStereotypeStringAttributeChangeAccessor(right, (StereotypeAttributeChange)target);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	@Override
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		StereotypeAttributeChange change = (StereotypeAttributeChange)target;
		Diff refined = change.getRefinedBy().get(0);
		EObject origin = refined.getMatch().getOrigin();
		if (origin != null) {
			return new UMLStereotypeStringAttributeChangeAccessor(origin, (StereotypeAttributeChange)target);
		} else {
			return null;
		}
	}

}
