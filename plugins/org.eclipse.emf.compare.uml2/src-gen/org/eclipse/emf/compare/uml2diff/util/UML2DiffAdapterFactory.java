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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.uml2diff.*;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLElementChange;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChange;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChange;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;
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
			public Adapter caseUMLAssociationBranchChange(UMLAssociationBranchChange object) {
				return createUMLAssociationBranchChangeAdapter();
			}
			@Override
			public Adapter caseUMLAssociationBranchChangeLeftTarget(UMLAssociationBranchChangeLeftTarget object) {
				return createUMLAssociationBranchChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLAssociationBranchChangeRightTarget(UMLAssociationBranchChangeRightTarget object) {
				return createUMLAssociationBranchChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLDependencyBranchChange(UMLDependencyBranchChange object) {
				return createUMLDependencyBranchChangeAdapter();
			}
			@Override
			public Adapter caseUMLDependencyBranchChangeLeftTarget(UMLDependencyBranchChangeLeftTarget object) {
				return createUMLDependencyBranchChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLDependencyBranchChangeRightTarget(UMLDependencyBranchChangeRightTarget object) {
				return createUMLDependencyBranchChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLGeneralizationSetChange(UMLGeneralizationSetChange object) {
				return createUMLGeneralizationSetChangeAdapter();
			}
			@Override
			public Adapter caseUMLGeneralizationSetChangeLeftTarget(UMLGeneralizationSetChangeLeftTarget object) {
				return createUMLGeneralizationSetChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLGeneralizationSetChangeRightTarget(UMLGeneralizationSetChangeRightTarget object) {
				return createUMLGeneralizationSetChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLDependencyChange(UMLDependencyChange object) {
				return createUMLDependencyChangeAdapter();
			}
			@Override
			public Adapter caseUMLDependencyChangeLeftTarget(UMLDependencyChangeLeftTarget object) {
				return createUMLDependencyChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLDependencyChangeRightTarget(UMLDependencyChangeRightTarget object) {
				return createUMLDependencyChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLExtendChange(UMLExtendChange object) {
				return createUMLExtendChangeAdapter();
			}
			@Override
			public Adapter caseUMLExtendChangeLeftTarget(UMLExtendChangeLeftTarget object) {
				return createUMLExtendChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLExtendChangeRightTarget(UMLExtendChangeRightTarget object) {
				return createUMLExtendChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLExecutionSpecificationChange(UMLExecutionSpecificationChange object) {
				return createUMLExecutionSpecificationChangeAdapter();
			}
			@Override
			public Adapter caseUMLExecutionSpecificationChangeLeftTarget(UMLExecutionSpecificationChangeLeftTarget object) {
				return createUMLExecutionSpecificationChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLExecutionSpecificationChangeRightTarget(UMLExecutionSpecificationChangeRightTarget object) {
				return createUMLExecutionSpecificationChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLDestructionEventChange(UMLDestructionEventChange object) {
				return createUMLDestructionEventChangeAdapter();
			}
			@Override
			public Adapter caseUMLDestructionEventChangeLeftTarget(UMLDestructionEventChangeLeftTarget object) {
				return createUMLDestructionEventChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLDestructionEventChangeRightTarget(UMLDestructionEventChangeRightTarget object) {
				return createUMLDestructionEventChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLIntervalConstraintChange(UMLIntervalConstraintChange object) {
				return createUMLIntervalConstraintChangeAdapter();
			}
			@Override
			public Adapter caseUMLIntervalConstraintChangeLeftTarget(UMLIntervalConstraintChangeLeftTarget object) {
				return createUMLIntervalConstraintChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLIntervalConstraintChangeRightTarget(UMLIntervalConstraintChangeRightTarget object) {
				return createUMLIntervalConstraintChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLMessageChange(UMLMessageChange object) {
				return createUMLMessageChangeAdapter();
			}
			@Override
			public Adapter caseUMLMessageChangeLeftTarget(UMLMessageChangeLeftTarget object) {
				return createUMLMessageChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLMessageChangeRightTarget(UMLMessageChangeRightTarget object) {
				return createUMLMessageChangeRightTargetAdapter();
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
			public Adapter caseUMLStereotypeReferenceChangeLeftTarget(UMLStereotypeReferenceChangeLeftTarget object) {
				return createUMLStereotypeReferenceChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeReferenceChangeRightTarget(UMLStereotypeReferenceChangeRightTarget object) {
				return createUMLStereotypeReferenceChangeRightTargetAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeUpdateReference(UMLStereotypeUpdateReference object) {
				return createUMLStereotypeUpdateReferenceAdapter();
			}
			@Override
			public Adapter caseUMLStereotypeReferenceOrderChange(UMLStereotypeReferenceOrderChange object) {
				return createUMLStereotypeReferenceOrderChangeAdapter();
			}
			@Override
			public Adapter caseUMLProfileApplicationChange(UMLProfileApplicationChange object) {
				return createUMLProfileApplicationChangeAdapter();
			}
			@Override
			public Adapter caseUMLProfileApplicationAddition(UMLProfileApplicationAddition object) {
				return createUMLProfileApplicationAdditionAdapter();
			}
			@Override
			public Adapter caseUMLProfileApplicationRemoval(UMLProfileApplicationRemoval object) {
				return createUMLProfileApplicationRemovalAdapter();
			}
			@Override
			public Adapter caseUMLElementChange(UMLElementChange object) {
				return createUMLElementChangeAdapter();
			}
			@Override
			public Adapter caseUMLElementChangeLeftTarget(UMLElementChangeLeftTarget object) {
				return createUMLElementChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseUMLElementChangeRightTarget(UMLElementChangeRightTarget object) {
				return createUMLElementChangeRightTargetAdapter();
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
			public Adapter caseReferenceChange(ReferenceChange object) {
				return createReferenceChangeAdapter();
			}
			@Override
			public Adapter caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
				return createReferenceChangeLeftTargetAdapter();
			}
			@Override
			public Adapter caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
				return createReferenceChangeRightTargetAdapter();
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
			public Adapter caseUpdateReference(UpdateReference object) {
				return createUpdateReferenceAdapter();
			}
			@Override
			public Adapter caseReferenceOrderChange(ReferenceOrderChange object) {
				return createReferenceOrderChangeAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange <em>UML Association Branch Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange
	 * @generated
	 */
	public Adapter createUMLAssociationBranchChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget <em>UML Association Branch Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLAssociationBranchChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget <em>UML Association Branch Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLAssociationBranchChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange <em>UML Dependency Branch Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange
	 * @generated
	 */
	public Adapter createUMLDependencyBranchChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget <em>UML Dependency Branch Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLDependencyBranchChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget <em>UML Dependency Branch Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLDependencyBranchChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange <em>UML Generalization Set Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange
	 * @generated
	 */
	public Adapter createUMLGeneralizationSetChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget <em>UML Generalization Set Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLGeneralizationSetChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget <em>UML Generalization Set Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLGeneralizationSetChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChange <em>UML Dependency Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChange
	 * @generated
	 */
	public Adapter createUMLDependencyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget <em>UML Dependency Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLDependencyChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget <em>UML Dependency Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLDependencyChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChange <em>UML Extend Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChange
	 * @generated
	 */
	public Adapter createUMLExtendChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget <em>UML Extend Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLExtendChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget <em>UML Extend Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLExtendChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange <em>UML Execution Specification Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange
	 * @generated
	 */
	public Adapter createUMLExecutionSpecificationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget <em>UML Execution Specification Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLExecutionSpecificationChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget <em>UML Execution Specification Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLExecutionSpecificationChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange <em>UML Destruction Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange
	 * @generated
	 */
	public Adapter createUMLDestructionEventChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget <em>UML Destruction Event Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLDestructionEventChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget <em>UML Destruction Event Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLDestructionEventChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange <em>UML Interval Constraint Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange
	 * @generated
	 */
	public Adapter createUMLIntervalConstraintChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget <em>UML Interval Constraint Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLIntervalConstraintChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget <em>UML Interval Constraint Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLIntervalConstraintChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChange <em>UML Message Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChange
	 * @generated
	 */
	public Adapter createUMLMessageChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget <em>UML Message Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLMessageChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget <em>UML Message Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLMessageChangeRightTargetAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget <em>UML Stereotype Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLStereotypeReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget <em>UML Stereotype Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLStereotypeReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference <em>UML Stereotype Update Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference
	 * @generated
	 */
	public Adapter createUMLStereotypeUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange <em>UML Stereotype Reference Order Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange
	 * @generated
	 */
	public Adapter createUMLStereotypeReferenceOrderChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange <em>UML Profile Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange
	 * @generated
	 */
	public Adapter createUMLProfileApplicationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition <em>UML Profile Application Addition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition
	 * @generated
	 */
	public Adapter createUMLProfileApplicationAdditionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval <em>UML Profile Application Removal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval
	 * @generated
	 */
	public Adapter createUMLProfileApplicationRemovalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLElementChange <em>UML Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLElementChange
	 * @generated
	 */
	public Adapter createUMLElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget <em>UML Element Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget
	 * @generated
	 */
	public Adapter createUMLElementChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget <em>UML Element Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget
	 * @generated
	 */
	public Adapter createUMLElementChangeRightTargetAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	public Adapter createReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget <em>Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget <em>Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	public Adapter createUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange <em>Reference Order Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange
	 * @generated
	 */
	public Adapter createReferenceOrderChangeAdapter() {
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
