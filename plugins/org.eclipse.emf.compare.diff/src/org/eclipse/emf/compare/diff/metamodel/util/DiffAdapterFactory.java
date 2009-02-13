/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.util;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDiff;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage
 * @generated
 */
public class DiffAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static DiffPackage modelPackage;

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiffSwitch<Adapter> modelSwitch = new DiffSwitch<Adapter>() {
		@Override
		public Adapter caseDiffModel(DiffModel object) {
			return createDiffModelAdapter();
		}

		@Override
		public Adapter caseDiffResourceSet(DiffResourceSet object) {
			return createDiffResourceSetAdapter();
		}

		@Override
		public Adapter caseDiffElement(DiffElement object) {
			return createDiffElementAdapter();
		}

		@Override
		public Adapter caseConflictingDiffElement(ConflictingDiffElement object) {
			return createConflictingDiffElementAdapter();
		}

		@Override
		public Adapter caseDiffGroup(DiffGroup object) {
			return createDiffGroupAdapter();
		}

		@Override
		public Adapter caseComparisonSnapshot(ComparisonSnapshot object) {
			return createComparisonSnapshotAdapter();
		}

		@Override
		public Adapter caseComparisonResourceSnapshot(ComparisonResourceSnapshot object) {
			return createComparisonResourceSnapshotAdapter();
		}

		@Override
		public Adapter caseComparisonResourceSetSnapshot(ComparisonResourceSetSnapshot object) {
			return createComparisonResourceSetSnapshotAdapter();
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
		public Adapter caseUpdateModelElement(UpdateModelElement object) {
			return createUpdateModelElementAdapter();
		}

		@Override
		public Adapter caseMoveModelElement(MoveModelElement object) {
			return createMoveModelElementAdapter();
		}

		@Override
		public Adapter caseUpdateContainmentFeature(UpdateContainmentFeature object) {
			return createUpdateContainmentFeatureAdapter();
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
		public Adapter caseUpdateReference(UpdateReference object) {
			return createUpdateReferenceAdapter();
		}

		@Override
		public Adapter caseAbstractDiffExtension(AbstractDiffExtension object) {
			return createAbstractDiffExtensionAdapter();
		}

		@Override
		public Adapter caseResourceDiff(ResourceDiff object) {
			return createResourceDiffAdapter();
		}

		@Override
		public Adapter caseResourceDependencyChange(ResourceDependencyChange object) {
			return createResourceDependencyChangeAdapter();
		}

		@Override
		public Adapter caseResourceDependencyChangeLeftTarget(ResourceDependencyChangeLeftTarget object) {
			return createResourceDependencyChangeLeftTargetAdapter();
		}

		@Override
		public Adapter caseResourceDependencyChangeRightTarget(ResourceDependencyChangeRightTarget object) {
			return createResourceDependencyChangeRightTargetAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiffAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = DiffPackage.eINSTANCE;
		}
	}

	/**
	 * Check whether this element should be visible or not. A diff element may not be visible when it's hidden
	 * by any diff extension and this diff extension is not collapsed.
	 * 
	 * @param element
	 *            :the diff element we want to know if it's visible or not.
	 * @return : true if the element should be hidden in the compare view, false otherwhise.
	 */
	public static boolean shouldBeHidden(EObject element) {
		boolean result = false;
		if (element instanceof DiffElement) {
			final DiffElement diff = (DiffElement)element;
			final Iterator<AbstractDiffExtension> it = diff.getIsHiddenBy().iterator();
			while (it.hasNext()) {
				final AbstractDiffExtension extension = it.next();
				if (!extension.isIsCollapsed()) {
					result = true;
				}
			}
		}
		if (element instanceof DiffGroup) {
			final DiffGroup group = (DiffGroup)element;
			if (group.getSubchanges() == 0) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension
	 * @generated
	 */
	public Adapter createAbstractDiffExtensionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ResourceDiff <em>Resource Diff</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDiff
	 * @generated
	 */
	public Adapter createResourceDiffAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange
	 * <em>Resource Dependency Change</em>}'. <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange
	 * @generated
	 */
	public Adapter createResourceDependencyChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget
	 * <em>Resource Dependency Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget
	 * @generated
	 */
	public Adapter createResourceDependencyChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget
	 * <em>Resource Dependency Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget
	 * @generated
	 */
	public Adapter createResourceDependencyChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	public Adapter createAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * <em>Attribute Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	public Adapter createAttributeChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * <em>Attribute Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	public Adapter createAttributeChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement <em>Conflicting Diff Element</em>}
	 * '. <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement
	 * @generated
	 */
	public Adapter createConflictingDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'. <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases; it's useful to ignore a
	 * case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	public Adapter createDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * <em>Group</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * @generated
	 */
	public Adapter createDiffGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot <em>Comparison Snapshot</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot
	 * @generated
	 */
	public Adapter createComparisonSnapshotAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot <em>Comparison Snapshot</em>}
	 * '. <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot
	 * @generated
	 */
	public Adapter createComparisonResourceSnapshotAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot
	 * <em>Comparison Resource Set Snapshot</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot
	 * @generated
	 */
	public Adapter createComparisonResourceSetSnapshotAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * <em>Model</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * @generated
	 */
	public Adapter createDiffModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet <em>Resource Set</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffResourceSet
	 * @generated
	 */
	public Adapter createDiffResourceSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns
	 * null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	public Adapter createModelElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * <em>Model Element Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * @generated
	 */
	public Adapter createModelElementChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * <em>Model Element Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * @generated
	 */
	public Adapter createModelElementChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement <em>Move Model Element</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement
	 * @generated
	 */
	public Adapter createMoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature
	 * <em>Update Containment Feature</em>}'. <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature
	 * @generated
	 */
	public Adapter createUpdateContainmentFeatureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	public Adapter createReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * <em>Reference Change Left Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * <em>Reference Change Right Target</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	public Adapter createUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	public Adapter createUpdateModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	public Adapter createUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This
	 * implementation returns <code>true</code> if the object is either the model's package or is an instance
	 * object of the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage)
			return true;
		if (object instanceof EObject)
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		return false;
	}

} // DiffAdapterFactory
