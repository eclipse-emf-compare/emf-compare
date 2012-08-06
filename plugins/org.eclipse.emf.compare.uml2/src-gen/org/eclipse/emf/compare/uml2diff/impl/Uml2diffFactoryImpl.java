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
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.compare.uml2diff.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Uml2diffFactoryImpl extends EFactoryImpl implements Uml2diffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Uml2diffFactory init() {
		try {
			Uml2diffFactory theUml2diffFactory = (Uml2diffFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diff/uml2/2.0"); //$NON-NLS-1$ 
			if (theUml2diffFactory != null) {
				return theUml2diffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Uml2diffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Uml2diffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Uml2diffPackage.UML_ASSOCIATION_CHANGE: return createUMLAssociationChange();
			case Uml2diffPackage.UML_DEPENDENCY_CHANGE: return createUMLDependencyChange();
			case Uml2diffPackage.UML_INTERFACE_REALIZATION_CHANGE: return createUMLInterfaceRealizationChange();
			case Uml2diffPackage.UML_SUBSTITUTION_CHANGE: return createUMLSubstitutionChange();
			case Uml2diffPackage.UML_EXTEND_CHANGE: return createUMLExtendChange();
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE: return createUMLGeneralizationSetChange();
			case Uml2diffPackage.UML_EXECUTION_SPECIFICATION_CHANGE: return createUMLExecutionSpecificationChange();
			case Uml2diffPackage.UML_DESTRUCTION_EVENT_CHANGE: return createUMLDestructionEventChange();
			case Uml2diffPackage.UML_INTERVAL_CONSTRAINT_CHANGE: return createUMLIntervalConstraintChange();
			case Uml2diffPackage.UML_MESSAGE_CHANGE: return createUMLMessageChange();
			case Uml2diffPackage.UML_STEREOTYPE_PROPERTY_CHANGE: return createUMLStereotypePropertyChange();
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE: return createUMLStereotypeApplicationChange();
			case Uml2diffPackage.UML_STEREOTYPE_REFERENCE_CHANGE: return createUMLStereotypeReferenceChange();
			case Uml2diffPackage.UML_PROFILE_APPLICATION_CHANGE: return createUMLProfileApplicationChange();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationChange createUMLAssociationChange() {
		UMLAssociationChangeImpl umlAssociationChange = new UMLAssociationChangeImpl();
		return umlAssociationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyChange createUMLDependencyChange() {
		UMLDependencyChangeImpl umlDependencyChange = new UMLDependencyChangeImpl();
		return umlDependencyChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLInterfaceRealizationChange createUMLInterfaceRealizationChange() {
		UMLInterfaceRealizationChangeImpl umlInterfaceRealizationChange = new UMLInterfaceRealizationChangeImpl();
		return umlInterfaceRealizationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLSubstitutionChange createUMLSubstitutionChange() {
		UMLSubstitutionChangeImpl umlSubstitutionChange = new UMLSubstitutionChangeImpl();
		return umlSubstitutionChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExtendChange createUMLExtendChange() {
		UMLExtendChangeImpl umlExtendChange = new UMLExtendChangeImpl();
		return umlExtendChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLGeneralizationSetChange createUMLGeneralizationSetChange() {
		UMLGeneralizationSetChangeImpl umlGeneralizationSetChange = new UMLGeneralizationSetChangeImpl();
		return umlGeneralizationSetChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExecutionSpecificationChange createUMLExecutionSpecificationChange() {
		UMLExecutionSpecificationChangeImpl umlExecutionSpecificationChange = new UMLExecutionSpecificationChangeImpl();
		return umlExecutionSpecificationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDestructionEventChange createUMLDestructionEventChange() {
		UMLDestructionEventChangeImpl umlDestructionEventChange = new UMLDestructionEventChangeImpl();
		return umlDestructionEventChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLIntervalConstraintChange createUMLIntervalConstraintChange() {
		UMLIntervalConstraintChangeImpl umlIntervalConstraintChange = new UMLIntervalConstraintChangeImpl();
		return umlIntervalConstraintChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLMessageChange createUMLMessageChange() {
		UMLMessageChangeImpl umlMessageChange = new UMLMessageChangeImpl();
		return umlMessageChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypePropertyChange createUMLStereotypePropertyChange() {
		UMLStereotypePropertyChangeImpl umlStereotypePropertyChange = new UMLStereotypePropertyChangeImpl();
		return umlStereotypePropertyChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeApplicationChange createUMLStereotypeApplicationChange() {
		UMLStereotypeApplicationChangeImpl umlStereotypeApplicationChange = new UMLStereotypeApplicationChangeImpl();
		return umlStereotypeApplicationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeReferenceChange createUMLStereotypeReferenceChange() {
		UMLStereotypeReferenceChangeImpl umlStereotypeReferenceChange = new UMLStereotypeReferenceChangeImpl();
		return umlStereotypeReferenceChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLProfileApplicationChange createUMLProfileApplicationChange() {
		UMLProfileApplicationChangeImpl umlProfileApplicationChange = new UMLProfileApplicationChangeImpl();
		return umlProfileApplicationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Uml2diffPackage getUml2diffPackage() {
		return (Uml2diffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Uml2diffPackage getPackage() {
		return Uml2diffPackage.eINSTANCE;
	}

} //Uml2diffFactoryImpl
