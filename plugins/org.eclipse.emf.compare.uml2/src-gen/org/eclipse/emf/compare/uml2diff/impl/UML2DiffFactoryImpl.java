/**
 * Copyright (c) 2011 Obeo.
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
public class UML2DiffFactoryImpl extends EFactoryImpl implements UML2DiffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static UML2DiffFactory init() {
		try {
			UML2DiffFactory theUML2DiffFactory = (UML2DiffFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diff/uml2/1.0"); 
			if (theUML2DiffFactory != null) {
				return theUML2DiffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UML2DiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffFactoryImpl() {
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
			case UML2DiffPackage.UML_ABSTRACTION_CHANGE_LEFT_TARGET: return createUMLAbstractionChangeLeftTarget();
			case UML2DiffPackage.UML_ABSTRACTION_CHANGE_RIGHT_TARGET: return createUMLAbstractionChangeRightTarget();
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_LEFT_TARGET: return createUMLAssociationChangeLeftTarget();
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_RIGHT_TARGET: return createUMLAssociationChangeRightTarget();
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET: return createUMLStereotypeAttributeChangeLeftTarget();
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET: return createUMLStereotypeAttributeChangeRightTarget();
			case UML2DiffPackage.UML_STEREOTYPE_UPDATE_ATTRIBUTE: return createUMLStereotypeUpdateAttribute();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_ADDITION: return createUMLStereotypeApplicationAddition();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL: return createUMLStereotypeApplicationRemoval();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAbstractionChangeLeftTarget createUMLAbstractionChangeLeftTarget() {
		UMLAbstractionChangeLeftTargetImpl umlAbstractionChangeLeftTarget = new UMLAbstractionChangeLeftTargetImpl();
		return umlAbstractionChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAbstractionChangeRightTarget createUMLAbstractionChangeRightTarget() {
		UMLAbstractionChangeRightTargetImpl umlAbstractionChangeRightTarget = new UMLAbstractionChangeRightTargetImpl();
		return umlAbstractionChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationChangeLeftTarget createUMLAssociationChangeLeftTarget() {
		UMLAssociationChangeLeftTargetImpl umlAssociationChangeLeftTarget = new UMLAssociationChangeLeftTargetImpl();
		return umlAssociationChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationChangeRightTarget createUMLAssociationChangeRightTarget() {
		UMLAssociationChangeRightTargetImpl umlAssociationChangeRightTarget = new UMLAssociationChangeRightTargetImpl();
		return umlAssociationChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeAttributeChangeLeftTarget createUMLStereotypeAttributeChangeLeftTarget() {
		UMLStereotypeAttributeChangeLeftTargetImpl umlStereotypeAttributeChangeLeftTarget = new UMLStereotypeAttributeChangeLeftTargetImpl();
		return umlStereotypeAttributeChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeAttributeChangeRightTarget createUMLStereotypeAttributeChangeRightTarget() {
		UMLStereotypeAttributeChangeRightTargetImpl umlStereotypeAttributeChangeRightTarget = new UMLStereotypeAttributeChangeRightTargetImpl();
		return umlStereotypeAttributeChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeUpdateAttribute createUMLStereotypeUpdateAttribute() {
		UMLStereotypeUpdateAttributeImpl umlStereotypeUpdateAttribute = new UMLStereotypeUpdateAttributeImpl();
		return umlStereotypeUpdateAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeApplicationAddition createUMLStereotypeApplicationAddition() {
		UMLStereotypeApplicationAdditionImpl umlStereotypeApplicationAddition = new UMLStereotypeApplicationAdditionImpl();
		return umlStereotypeApplicationAddition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeApplicationRemoval createUMLStereotypeApplicationRemoval() {
		UMLStereotypeApplicationRemovalImpl umlStereotypeApplicationRemoval = new UMLStereotypeApplicationRemovalImpl();
		return umlStereotypeApplicationRemoval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffPackage getUML2DiffPackage() {
		return (UML2DiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UML2DiffPackage getPackage() {
		return UML2DiffPackage.eINSTANCE;
	}

} //UML2DiffFactoryImpl
