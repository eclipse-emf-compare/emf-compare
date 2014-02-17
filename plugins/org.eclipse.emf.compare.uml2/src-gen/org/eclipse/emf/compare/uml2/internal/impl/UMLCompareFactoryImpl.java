/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.impl;

import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2.internal.ExtendChange;
import org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.internal.MessageChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLComparePackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class UMLCompareFactoryImpl extends EFactoryImpl implements UMLCompareFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static UMLCompareFactory init() {
		try {
			UMLCompareFactory theUMLCompareFactory = (UMLCompareFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/uml2/2.0"); //$NON-NLS-1$ 
			if (theUMLCompareFactory != null) {
				return theUMLCompareFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UMLCompareFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLCompareFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UMLComparePackage.ASSOCIATION_CHANGE: return createAssociationChange();
			case UMLComparePackage.EXTEND_CHANGE: return createExtendChange();
			case UMLComparePackage.GENERALIZATION_SET_CHANGE: return createGeneralizationSetChange();
			case UMLComparePackage.EXECUTION_SPECIFICATION_CHANGE: return createExecutionSpecificationChange();
			case UMLComparePackage.INTERVAL_CONSTRAINT_CHANGE: return createIntervalConstraintChange();
			case UMLComparePackage.MESSAGE_CHANGE: return createMessageChange();
			case UMLComparePackage.STEREOTYPE_ATTRIBUTE_CHANGE: return createStereotypeAttributeChange();
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE: return createStereotypeApplicationChange();
			case UMLComparePackage.STEREOTYPE_REFERENCE_CHANGE: return createStereotypeReferenceChange();
			case UMLComparePackage.PROFILE_APPLICATION_CHANGE: return createProfileApplicationChange();
			case UMLComparePackage.DIRECTED_RELATIONSHIP_CHANGE: return createDirectedRelationshipChange();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AssociationChange createAssociationChange() {
		AssociationChangeImpl associationChange = new AssociationChangeImpl();
		return associationChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ExtendChange createExtendChange() {
		ExtendChangeImpl extendChange = new ExtendChangeImpl();
		return extendChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public GeneralizationSetChange createGeneralizationSetChange() {
		GeneralizationSetChangeImpl generalizationSetChange = new GeneralizationSetChangeImpl();
		return generalizationSetChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionSpecificationChange createExecutionSpecificationChange() {
		ExecutionSpecificationChangeImpl executionSpecificationChange = new ExecutionSpecificationChangeImpl();
		return executionSpecificationChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalConstraintChange createIntervalConstraintChange() {
		IntervalConstraintChangeImpl intervalConstraintChange = new IntervalConstraintChangeImpl();
		return intervalConstraintChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MessageChange createMessageChange() {
		MessageChangeImpl messageChange = new MessageChangeImpl();
		return messageChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereotypeAttributeChange createStereotypeAttributeChange() {
		StereotypeAttributeChangeImpl stereotypeAttributeChange = new StereotypeAttributeChangeImpl();
		return stereotypeAttributeChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StereotypeApplicationChange createStereotypeApplicationChange() {
		StereotypeApplicationChangeImpl stereotypeApplicationChange = new StereotypeApplicationChangeImpl();
		return stereotypeApplicationChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StereotypeReferenceChange createStereotypeReferenceChange() {
		StereotypeReferenceChangeImpl stereotypeReferenceChange = new StereotypeReferenceChangeImpl();
		return stereotypeReferenceChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ProfileApplicationChange createProfileApplicationChange() {
		ProfileApplicationChangeImpl profileApplicationChange = new ProfileApplicationChangeImpl();
		return profileApplicationChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DirectedRelationshipChange createDirectedRelationshipChange() {
		DirectedRelationshipChangeImpl directedRelationshipChange = new DirectedRelationshipChangeImpl();
		return directedRelationshipChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLComparePackage getUMLComparePackage() {
		return (UMLComparePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UMLComparePackage getPackage() {
		return UMLComparePackage.eINSTANCE;
	}

} // UMLCompareFactoryImpl
