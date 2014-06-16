/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage
 * @generated
 */
public interface UML2CompareTestProfileFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	UML2CompareTestProfileFactory eINSTANCE = org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfileFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>ACliche</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>ACliche</em>'.
	 * @generated
	 */
	ACliche createACliche();

	/**
	 * Returns a new object of class '<em>ACliche2</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>ACliche2</em>'.
	 * @generated
	 */
	ACliche2 createACliche2();

	/**
	 * Returns a new object of class '<em>ACliche3</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>ACliche3</em>'.
	 * @generated
	 */
	ACliche3 createACliche3();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	UML2CompareTestProfilePackage getUML2CompareTestProfilePackage();

} // UML2CompareTestProfileFactory
