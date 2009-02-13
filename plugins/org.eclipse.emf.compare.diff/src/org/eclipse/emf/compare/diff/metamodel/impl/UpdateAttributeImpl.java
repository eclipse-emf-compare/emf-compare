/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Update Attribute</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */

public class UpdateAttributeImpl extends AttributeChangeImpl implements UpdateAttribute {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected UpdateAttributeImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		String toString = null;
		if (isRemote()) {
			try {
				toString = EMFCompareDiffMessages.getString("RemoteUpdateAttributeImpl.ToString", //$NON-NLS-1$
						NameSimilarity.findName(attribute), NameSimilarity.findName(leftElement), leftElement
								.eGet(attribute), rightElement.eGet(attribute));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("RemoteUpdateAttributeImpl.ToString", //$NON-NLS-1$
						attribute.eClass().getName(), leftElement.eClass().getName(), leftElement
								.eGet(attribute), rightElement.eGet(attribute));
			}
		} else {
			try {
				toString = EMFCompareDiffMessages.getString("UpdateAttributeImpl.ToString", NameSimilarity //$NON-NLS-1$
						.findName(attribute), NameSimilarity.findName(leftElement), rightElement
						.eGet(attribute), leftElement.eGet(attribute));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("UpdateAttributeImpl.ToString", attribute //$NON-NLS-1$
						.eClass().getName(), leftElement.eClass().getName(), rightElement.eGet(attribute),
						leftElement.eGet(attribute));
			}
		}
		return toString;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.UPDATE_ATTRIBUTE;
	}
} // UpdateAttributeImpl
