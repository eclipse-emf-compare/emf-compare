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
package org.eclipse.emf.compare.uml2diff.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;

import org.eclipse.emf.compare.uml2diff.*;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage
 * @generated
 */
public class UML2DiffAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UML2DiffPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = UML2DiffPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UML2DiffSwitch<Adapter> modelSwitch =
		new UML2DiffSwitch<Adapter>() {
			@Override
			public Adapter caseUMLDiffExtension(UMLDiffExtension object) {
				return createUMLDiffExtensionAdapter();
			}
			@Override
			public Adapter caseUMLAbstractionChange(UMLAbstractionChange object) {
				return createUMLAbstractionChangeAdapter();
			}
			@Override
			public Adapter caseUMLAbstractionChangeLeftTarget(UMLAbstractionChangeLeftTarget object) {
				return createUMLAbstractionChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLAbstractionChangeRightTarget(UMLAbstractionChangeRightTarget object) {
				return createUMLAbstractionChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLAssociationChange(UMLAssociationChange object) {
				return createUMLAssociationChangeAdapter();
			}
			@Override
			public Adapter caseUMLAssociationChangeLeftTarget(UMLAssociationChangeLeftTarget object) {
				return createUMLAssociationChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLAssociationChangeRightTarget(UMLAssociationChangeRightTarget object) {
				return createUMLAssociationChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLStereotypePropertyChange(UMLStereotypePropertyChange object) {
				return createUMLStereotypePropertyChangeAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeAttributeChangeLeftTarget(UMLStereotypeAttributeChangeLeftTarget object) {
				return createUMLStereotypeAttributeChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeAttributeChangeRightTarget(UMLStereotypeAttributeChangeRightTarget object) {
				return createUMLStereotypeAttributeChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeUpdateAttribute(UMLStereotypeUpdateAttribute object) {
				return createUMLStereotypeUpdateAttributeAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeApplicationChange(UMLStereotypeApplicationChange object) {
				return createUMLStereotypeApplicationChangeAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeApplicationAddition(UMLStereotypeApplicationAddition object) {
				return createUMLStereotypeApplicationAdditionAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeApplicationRemoval(UMLStereotypeApplicationRemoval object) {
				return createUMLStereotypeApplicationRemovalAdapter();
			}
			@Override
			public Adapter caseDiffElement(DiffElement object) {
				return createDiffElementAdapter();
			}
			@Override
			public Adapter caseAbstractDiffExtension(AbstractDiffExtension object) {
				return createAbstractDiffExtensionAdapter();
			}
			@Override
			public Adapter caseModelElementChange(ModelElementChange object) {
				return createModelElementChangeAdapter();
			}
			@Override
			public Adapter caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
				return createModelElementChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
				return createModelElementChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseAttributeChange(AttributeChange object) {
				return createAttributeChangeAdapter();
			}
			@Override
			public Adapter caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
				return createAttributeChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
				return createAttributeChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUpdateAttribute(UpdateAttribute object) {
				return createUpdateAttributeAdapter();
			}
			@Override
			public Adapter caseUpdateModelElement(UpdateModelElement object) {
				return createUpdateModelElementAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDiffExtension <em>UML Diff Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDiffExtension
	 * @generated
	 */
	public Adapter createUMLDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChange <em>UML Abstraction Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChange
	 * @generated
	 */
	public Adapter createUMLAbstractionChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeLeftTarget <em>UML Abstraction Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLAbstractionChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeRightTarget <em>UML Abstraction Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLAbstractionChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange <em>UML Association Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange
	 * @generated
	 */
	public Adapter createUMLAssociationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget <em>UML Association Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLAssociationChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget <em>UML Association Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLAssociationChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @generated
	 */
	public Adapter createUMLStereotypePropertyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget <em>UML Stereotype Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLStereotypeAttributeChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget <em>UML Stereotype Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLStereotypeAttributeChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute <em>UML Stereotype Update Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute
	 * @generated
	 */
	public Adapter createUMLStereotypeUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange <em>UML Stereotype Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange
	 * @generated
	 */
	public Adapter createUMLStereotypeApplicationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition <em>UML Stereotype Application Addition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition
	 * @generated
	 */
	public Adapter createUMLStereotypeApplicationAdditionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval <em>UML Stereotype Application Removal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval
	 * @generated
	 */
	public Adapter createUMLStereotypeApplicationRemovalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	public Adapter createDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension
	 * @generated
	 */
	public Adapter createAbstractDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	public Adapter createModelElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget <em>Model Element Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * @generated
	 */
	public Adapter createModelElementChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget <em>Model Element Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * @generated
	 */
	public Adapter createModelElementChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	public Adapter createAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget <em>Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	public Adapter createAttributeChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget <em>Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	public Adapter createAttributeChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	public Adapter createUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	public Adapter createUpdateModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //UML2DiffAdapterFactory
