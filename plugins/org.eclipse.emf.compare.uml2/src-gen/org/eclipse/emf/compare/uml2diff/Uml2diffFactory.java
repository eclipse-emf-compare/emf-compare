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
package org.eclipse.emf.compare.uml2diff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2diff.Uml2diffPackage
 * @generated
 */
public interface Uml2diffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Uml2diffFactory eINSTANCE = org.eclipse.emf.compare.uml2diff.impl.Uml2diffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>UML Association Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Association Change</em>'.
	 * @generated
	 */
	UMLAssociationChange createUMLAssociationChange();

	/**
	 * Returns a new object of class '<em>UML Generalization Set Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Generalization Set Change</em>'.
	 * @generated
	 */
	UMLGeneralizationSetChange createUMLGeneralizationSetChange();

	/**
	 * Returns a new object of class '<em>UML Dependency Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Dependency Change</em>'.
	 * @generated
	 */
	UMLDependencyChange createUMLDependencyChange();

	/**
	 * Returns a new object of class '<em>UML Interface Realization Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Interface Realization Change</em>'.
	 * @generated
	 */
	UMLInterfaceRealizationChange createUMLInterfaceRealizationChange();

	/**
	 * Returns a new object of class '<em>UML Substitution Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Substitution Change</em>'.
	 * @generated
	 */
	UMLSubstitutionChange createUMLSubstitutionChange();

	/**
	 * Returns a new object of class '<em>UML Extend Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Extend Change</em>'.
	 * @generated
	 */
	UMLExtendChange createUMLExtendChange();

	/**
	 * Returns a new object of class '<em>UML Execution Specification Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Execution Specification Change</em>'.
	 * @generated
	 */
	UMLExecutionSpecificationChange createUMLExecutionSpecificationChange();

	/**
	 * Returns a new object of class '<em>UML Destruction Event Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Destruction Event Change</em>'.
	 * @generated
	 */
	UMLDestructionEventChange createUMLDestructionEventChange();

	/**
	 * Returns a new object of class '<em>UML Interval Constraint Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Interval Constraint Change</em>'.
	 * @generated
	 */
	UMLIntervalConstraintChange createUMLIntervalConstraintChange();

	/**
	 * Returns a new object of class '<em>UML Message Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Message Change</em>'.
	 * @generated
	 */
	UMLMessageChange createUMLMessageChange();

	/**
	 * Returns a new object of class '<em>UML Stereotype Property Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Property Change</em>'.
	 * @generated
	 */
	UMLStereotypePropertyChange createUMLStereotypePropertyChange();

	/**
	 * Returns a new object of class '<em>UML Stereotype Application Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Application Change</em>'.
	 * @generated
	 */
	UMLStereotypeApplicationChange createUMLStereotypeApplicationChange();

	/**
	 * Returns a new object of class '<em>UML Stereotype Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Reference Change</em>'.
	 * @generated
	 */
	UMLStereotypeReferenceChange createUMLStereotypeReferenceChange();

	/**
	 * Returns a new object of class '<em>UML Profile Application Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Profile Application Change</em>'.
	 * @generated
	 */
	UMLProfileApplicationChange createUMLProfileApplicationChange();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Uml2diffPackage getUml2diffPackage();

} //Uml2diffFactory
