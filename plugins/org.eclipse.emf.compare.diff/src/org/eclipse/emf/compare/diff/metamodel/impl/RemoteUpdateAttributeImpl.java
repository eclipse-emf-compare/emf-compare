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
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Remote Update Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */

public class RemoteUpdateAttributeImpl extends UpdateAttributeImpl implements RemoteUpdateAttribute {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected RemoteUpdateAttributeImpl() {
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
		try {
			return EMFCompareDiffMessages
					.getString(
							"RemoteUpdateAttributeImpl.ToString", NameSimilarity.findName(attribute), NameSimilarity.findName(leftElement), leftElement.eGet(attribute), rightElement.eGet(attribute)); //$NON-NLS-1$
		} catch (final FactoryException e) {
			return EMFCompareDiffMessages
					.getString(
							"RemoteUpdateAttributeImpl.ToString", attribute.eClass().getName(), leftElement.eClass().getName(), leftElement.eGet(attribute), rightElement.eGet(attribute)); //$NON-NLS-1$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REMOTE_UPDATE_ATTRIBUTE;
	}
} // RemoteUpdateAttributeImpl
