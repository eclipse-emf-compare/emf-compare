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
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Remote Remove Reference Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */

public class RemoteRemoveReferenceValueImpl extends ReferenceChangeLeftTargetImpl implements RemoteRemoveReferenceValue {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected RemoteRemoveReferenceValueImpl() {
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
							"RemoteRemoveReferenceValueImpl.ToString", NameSimilarity.findName(leftTarget), NameSimilarity.findName(reference), NameSimilarity.findName(leftElement)); //$NON-NLS-1$
		} catch (final FactoryException e) {
			return EMFCompareDiffMessages
					.getString(
							"RemoteRemoveReferenceValueImpl.ToString", leftTarget.eClass().getName(), reference.eClass().getName(), leftElement.eClass().getName()); //$NON-NLS-1$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REMOTE_REMOVE_REFERENCE_VALUE;
	}
} // RemoteRemoveReferenceValueImpl
