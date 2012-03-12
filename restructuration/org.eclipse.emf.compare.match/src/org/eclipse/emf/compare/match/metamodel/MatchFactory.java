/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.metamodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage
 * @generated
 */
public interface MatchFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	MatchFactory eINSTANCE = org.eclipse.emf.compare.match.metamodel.impl.MatchFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Match2 Elements</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Match2 Elements</em>'.
	 * @generated
	 */
	Match2Elements createMatch2Elements();

	/**
	 * Returns a new object of class '<em>Match3 Elements</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Match3 Elements</em>'.
	 * @generated
	 */
	Match3Elements createMatch3Elements();

	/**
	 * Returns a new object of class '<em>Unmatch Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unmatch Element</em>'.
	 * @generated
	 */
	UnmatchElement createUnmatchElement();

	/**
	 * Returns a new object of class '<em>Resource Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Set</em>'.
	 * @generated
	 */
	MatchResourceSet createMatchResourceSet();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MatchModel createMatchModel();

	/**
	 * Returns a new object of class '<em>Unmatch Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unmatch Model</em>'.
	 * @generated
	 */
	UnmatchModel createUnmatchModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MatchPackage getMatchPackage();

} // MatchFactory
