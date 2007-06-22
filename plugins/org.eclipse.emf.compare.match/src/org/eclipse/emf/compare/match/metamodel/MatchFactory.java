/**
 * <copyright>
 * </copyright>
 *
 * $Id: MatchFactory.java,v 1.1 2007/06/22 15:07:39 cbrun Exp $
 */
package org.eclipse.emf.compare.match.metamodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage
 * @generated
 */
public interface MatchFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MatchFactory eINSTANCE = org.eclipse.emf.compare.match.metamodel.impl.MatchFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MatchModel createMatchModel();

	/**
	 * Returns a new object of class '<em>Match2 Elements</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Match2 Elements</em>'.
	 * @generated
	 */
	Match2Elements createMatch2Elements();

	/**
	 * Returns a new object of class '<em>Match3 Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Match3 Element</em>'.
	 * @generated
	 */
	Match3Element createMatch3Element();

	/**
	 * Returns a new object of class '<em>Un Match Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Un Match Element</em>'.
	 * @generated
	 */
	UnMatchElement createUnMatchElement();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MatchPackage getMatchPackage();

} //MatchFactory
