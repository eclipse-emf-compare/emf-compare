/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: SymrefsFactory.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage
 * @generated
 */
public interface SymrefsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SymrefsFactory eINSTANCE = org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>External Element Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>External Element Reference</em>'.
	 * @generated
	 */
	ExternalElementReference createExternalElementReference();

	/**
	 * Returns a new object of class '<em>Id Emf Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Id Emf Reference</em>'.
	 * @generated
	 */
	IdEmfReference createIdEmfReference();

	/**
	 * Returns a new object of class '<em>Element Set Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Element Set Reference</em>'.
	 * @generated
	 */
	ElementSetReference createElementSetReference();

	/**
	 * Returns a new object of class '<em>Ocl Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ocl Condition</em>'.
	 * @generated
	 */
	OclCondition createOclCondition();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SymrefsPackage getSymrefsPackage();

} //SymrefsFactory
