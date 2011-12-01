/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage
 * @generated
 */
public interface SysMLdiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	SysMLdiffFactory eINSTANCE = org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Property Change Left Target</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Property Change Left Target</em>'.
	 * @generated
	 */
	SysMLStereotypePropertyChangeLeftTarget createSysMLStereotypePropertyChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Property Change Right Target</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Property Change Right Target</em>'.
	 * @generated
	 */
	SysMLStereotypePropertyChangeRightTarget createSysMLStereotypePropertyChangeRightTarget();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Reference Change Left Target</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Reference Change Left Target</em>'.
	 * @generated
	 */
	SysMLStereotypeReferenceChangeLeftTarget createSysMLStereotypeReferenceChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Reference Change Right Target</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Reference Change Right Target</em>'.
	 * @generated
	 */
	SysMLStereotypeReferenceChangeRightTarget createSysMLStereotypeReferenceChangeRightTarget();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Reference Order Change</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Reference Order Change</em>'.
	 * @generated
	 */
	SysMLStereotypeReferenceOrderChange createSysMLStereotypeReferenceOrderChange();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Update Attribute</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Update Attribute</em>'.
	 * @generated
	 */
	SysMLStereotypeUpdateAttribute createSysMLStereotypeUpdateAttribute();

	/**
	 * Returns a new object of class '<em>Sys ML Stereotype Update Reference</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sys ML Stereotype Update Reference</em>'.
	 * @generated
	 */
	SysMLStereotypeUpdateReference createSysMLStereotypeUpdateReference();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	SysMLdiffPackage getSysMLdiffPackage();

} // SysMLdiffFactory
