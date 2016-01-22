/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.impl;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.uml2.internal.UMLComparePackage;
import org.eclipse.emf.compare.uml2.internal.spec.UMLDiffSpec;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Multiplicity Element Change</b></em>
 * '. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MultiplicityElementChangeImpl extends UMLDiffSpec implements MultiplicityElementChange {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MultiplicityElementChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UMLComparePackage.Literals.MULTIPLICITY_ELEMENT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Diff getPrimeRefining() {
		if (getRefinedBy().isEmpty()) {
			return super.getPrimeRefining();
		} else {
			return getRefinedBy().get(0);
		}
	}

} // MultiplicityElementChangeImpl
