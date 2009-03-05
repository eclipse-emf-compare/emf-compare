/*******************************************************************************
 * Copyright (c) 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Reference Order Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class ReferenceOrderChangeImpl extends ReferenceChangeImpl implements ReferenceOrderChange {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ReferenceOrderChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REFERENCE_ORDER_CHANGE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		return EMFCompareDiffMessages.getString("ReferenceOrderChange.ToString", getReference().getName()); //$NON-NLS-1$
	}
} // ReferenceOrderChangeImpl
