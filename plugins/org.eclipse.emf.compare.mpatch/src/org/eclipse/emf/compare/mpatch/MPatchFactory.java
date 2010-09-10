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
 * $Id: MPatchFactory.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage
 * @generated
 */
public interface MPatchFactory extends EFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MPatchFactory eINSTANCE = org.eclipse.emf.compare.mpatch.impl.MPatchFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MPatchModel createMPatchModel();

	/**
	 * Returns a new object of class '<em>Change Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Group</em>'.
	 * @generated
	 */
	ChangeGroup createChangeGroup();

	/**
	 * Returns a new object of class '<em>Indep Add Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Add Element Change</em>'.
	 * @generated
	 */
	IndepAddElementChange createIndepAddElementChange();

	/**
	 * Returns a new object of class '<em>Indep Remove Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Remove Element Change</em>'.
	 * @generated
	 */
	IndepRemoveElementChange createIndepRemoveElementChange();

	/**
	 * Returns a new object of class '<em>Indep Move Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Move Element Change</em>'.
	 * @generated
	 */
	IndepMoveElementChange createIndepMoveElementChange();

	/**
	 * Returns a new object of class '<em>Indep Add Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Add Attribute Change</em>'.
	 * @generated
	 */
	IndepAddAttributeChange createIndepAddAttributeChange();

	/**
	 * Returns a new object of class '<em>Indep Remove Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Remove Attribute Change</em>'.
	 * @generated
	 */
	IndepRemoveAttributeChange createIndepRemoveAttributeChange();

	/**
	 * Returns a new object of class '<em>Indep Update Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Update Attribute Change</em>'.
	 * @generated
	 */
	IndepUpdateAttributeChange createIndepUpdateAttributeChange();

	/**
	 * Returns a new object of class '<em>Indep Add Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Add Reference Change</em>'.
	 * @generated
	 */
	IndepAddReferenceChange createIndepAddReferenceChange();

	/**
	 * Returns a new object of class '<em>Indep Remove Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Remove Reference Change</em>'.
	 * @generated
	 */
	IndepRemoveReferenceChange createIndepRemoveReferenceChange();

	/**
	 * Returns a new object of class '<em>Indep Update Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indep Update Reference Change</em>'.
	 * @generated
	 */
	IndepUpdateReferenceChange createIndepUpdateReferenceChange();

	/**
	 * Returns a new object of class '<em>Unknown Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unknown Change</em>'.
	 * @generated
	 */
	UnknownChange createUnknownChange();

	/**
	 * Returns a new object of class '<em>Model Descriptor Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Descriptor Reference</em>'.
	 * @generated
	 */
	ModelDescriptorReference createModelDescriptorReference();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MPatchPackage getMPatchPackage();

} //MPatchFactory
