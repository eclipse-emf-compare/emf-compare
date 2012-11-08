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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.factory;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.AbstractAccessorFactory;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IDEStringAttributeChangeAccessor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class IDEStringAttributeChangeAccessorFactory extends AbstractAccessorFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	public boolean isFactoryFor(Object target) {
		if (target instanceof AttributeChange) {
			EAttribute attribute = ((AttributeChange)target).getAttribute();
			return attribute.getEAttributeType().getInstanceClass() == String.class && !attribute.isMany();
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createLeft(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		AttributeChange attributeChange = (AttributeChange)target;
		EObject left = attributeChange.getMatch().getLeft();
		return new IDEStringAttributeChangeAccessor(left, (AttributeChange)target);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		AttributeChange attributeChange = (AttributeChange)target;
		EObject right = attributeChange.getMatch().getRight();
		return new IDEStringAttributeChangeAccessor(right, (AttributeChange)target);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		AttributeChange attributeChange = (AttributeChange)target;
		EObject ancestor = attributeChange.getMatch().getOrigin();
		return new IDEStringAttributeChangeAccessor(ancestor, (AttributeChange)target);
	}
}
