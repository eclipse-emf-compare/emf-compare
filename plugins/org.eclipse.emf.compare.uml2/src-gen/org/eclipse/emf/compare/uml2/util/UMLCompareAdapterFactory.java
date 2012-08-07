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
package org.eclipse.emf.compare.uml2.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;

import org.eclipse.emf.compare.uml2.*;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2.UMLComparePackage
 * @generated
 */
public class UMLCompareAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UMLComparePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLCompareAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = UMLComparePackage.eINSTANCE;
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
	protected UMLCompareSwitch<Adapter> modelSwitch =
		new UMLCompareSwitch<Adapter>() {
			@Override
			public Adapter caseAssociationChange(AssociationChange object) {
				return createAssociationChangeAdapter();
			}
			@Override
			public Adapter caseDependencyChange(DependencyChange object) {
				return createDependencyChangeAdapter();
			}
			@Override
			public Adapter caseInterfaceRealizationChange(InterfaceRealizationChange object) {
				return createInterfaceRealizationChangeAdapter();
			}
			@Override
			public Adapter caseSubstitutionChange(SubstitutionChange object) {
				return createSubstitutionChangeAdapter();
			}
			@Override
			public Adapter caseExtendChange(ExtendChange object) {
				return createExtendChangeAdapter();
			}
			@Override
			public Adapter caseGeneralizationSetChange(GeneralizationSetChange object) {
				return createGeneralizationSetChangeAdapter();
			}
			@Override
			public Adapter caseExecutionSpecificationChange(ExecutionSpecificationChange object) {
				return createExecutionSpecificationChangeAdapter();
			}
			@Override
			public Adapter caseDestructionEventChange(DestructionEventChange object) {
				return createDestructionEventChangeAdapter();
			}
			@Override
			public Adapter caseIntervalConstraintChange(IntervalConstraintChange object) {
				return createIntervalConstraintChangeAdapter();
			}
			@Override
			public Adapter caseMessageChange(MessageChange object) {
				return createMessageChangeAdapter();
			}
			@Override
			public Adapter caseStereotypePropertyChange(StereotypePropertyChange object) {
				return createStereotypePropertyChangeAdapter();
			}
			@Override
			public Adapter caseStereotypeApplicationChange(StereotypeApplicationChange object) {
				return createStereotypeApplicationChangeAdapter();
			}
			@Override
			public Adapter caseStereotypeReferenceChange(StereotypeReferenceChange object) {
				return createStereotypeReferenceChangeAdapter();
			}
			@Override
			public Adapter caseProfileApplicationChange(ProfileApplicationChange object) {
				return createProfileApplicationChangeAdapter();
			}
			@Override
			public Adapter caseUMLDiff(UMLDiff object) {
				return createUMLDiffAdapter();
			}
			@Override
			public Adapter caseDiff(Diff object) {
				return createDiffAdapter();
			}
			@Override
			public Adapter caseReferenceChange(ReferenceChange object) {
				return createReferenceChangeAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.AssociationChange <em>Association Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.AssociationChange
	 * @generated
	 */
	public Adapter createAssociationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.DependencyChange <em>Dependency Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.DependencyChange
	 * @generated
	 */
	public Adapter createDependencyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.InterfaceRealizationChange <em>Interface Realization Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.InterfaceRealizationChange
	 * @generated
	 */
	public Adapter createInterfaceRealizationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.SubstitutionChange <em>Substitution Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.SubstitutionChange
	 * @generated
	 */
	public Adapter createSubstitutionChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.ExtendChange <em>Extend Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.ExtendChange
	 * @generated
	 */
	public Adapter createExtendChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.GeneralizationSetChange <em>Generalization Set Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.GeneralizationSetChange
	 * @generated
	 */
	public Adapter createGeneralizationSetChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.ExecutionSpecificationChange <em>Execution Specification Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.ExecutionSpecificationChange
	 * @generated
	 */
	public Adapter createExecutionSpecificationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.DestructionEventChange <em>Destruction Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.DestructionEventChange
	 * @generated
	 */
	public Adapter createDestructionEventChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.IntervalConstraintChange <em>Interval Constraint Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.IntervalConstraintChange
	 * @generated
	 */
	public Adapter createIntervalConstraintChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.MessageChange <em>Message Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.MessageChange
	 * @generated
	 */
	public Adapter createMessageChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.StereotypePropertyChange <em>Stereotype Property Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.StereotypePropertyChange
	 * @generated
	 */
	public Adapter createStereotypePropertyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.StereotypeApplicationChange <em>Stereotype Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.StereotypeApplicationChange
	 * @generated
	 */
	public Adapter createStereotypeApplicationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.StereotypeReferenceChange <em>Stereotype Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.StereotypeReferenceChange
	 * @generated
	 */
	public Adapter createStereotypeReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.ProfileApplicationChange <em>Profile Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.ProfileApplicationChange
	 * @generated
	 */
	public Adapter createProfileApplicationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.UMLDiff <em>UML Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2.UMLDiff
	 * @generated
	 */
	public Adapter createUMLDiffAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.Diff <em>Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.Diff
	 * @generated
	 */
	public Adapter createDiffAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.ReferenceChange
	 * @generated
	 */
	public Adapter createReferenceChangeAdapter() {
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

} //UMLCompareAdapterFactory
