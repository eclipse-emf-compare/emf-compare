/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.GenericDiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage
 * @generated
 */
@SuppressWarnings("nls")
public class DiffAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static DiffPackage modelPackage;

	/**
	 * The switch the delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiffSwitch modelSwitch = new DiffSwitch() {
		public Object caseAbstractDiffExtension(AbstractDiffExtension object) {
			return createAbstractDiffExtensionAdapter();
		}

		public Object caseAddAttribute(AddAttribute object) {
			return createAddAttributeAdapter();
		}

		public Object caseAddModelElement(AddModelElement object) {
			return createAddModelElementAdapter();
		}

		public Object caseAddReferenceValue(AddReferenceValue object) {
			return createAddReferenceValueAdapter();
		}

		public Object caseAttributeChange(AttributeChange object) {
			return createAttributeChangeAdapter();
		}

		public Object caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
			return createAttributeChangeLeftTargetAdapter();
		}

		public Object caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
			return createAttributeChangeRightTargetAdapter();
		}

		public Object caseConflictingDiffElement(ConflictingDiffElement object) {
			return createConflictingDiffElementAdapter();
		}

		public Object caseDiffElement(DiffElement object) {
			return createDiffElementAdapter();
		}

		public Object caseDiffGroup(DiffGroup object) {
			return createDiffGroupAdapter();
		}

		public Object caseDiffModel(DiffModel object) {
			return createDiffModelAdapter();
		}

		public Object caseGenericDiffElement(GenericDiffElement object) {
			return createGenericDiffElementAdapter();
		}

		public Object caseModelElementChange(ModelElementChange object) {
			return createModelElementChangeAdapter();
		}

		public Object caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
			return createModelElementChangeLeftTargetAdapter();
		}

		public Object caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
			return createModelElementChangeRightTargetAdapter();
		}

		public Object caseModelInputSnapshot(ModelInputSnapshot object) {
			return createModelInputSnapshotAdapter();
		}

		public Object caseMoveModelElement(MoveModelElement object) {
			return createMoveModelElementAdapter();
		}

		public Object caseReferenceChange(ReferenceChange object) {
			return createReferenceChangeAdapter();
		}

		public Object caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
			return createReferenceChangeLeftTargetAdapter();
		}

		public Object caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
			return createReferenceChangeRightTargetAdapter();
		}

		public Object caseRemoteAddAttribute(RemoteAddAttribute object) {
			return createRemoteAddAttributeAdapter();
		}

		public Object caseRemoteAddModelElement(RemoteAddModelElement object) {
			return createRemoteAddModelElementAdapter();
		}

		public Object caseRemoteAddReferenceValue(RemoteAddReferenceValue object) {
			return createRemoteAddReferenceValueAdapter();
		}

		public Object caseRemoteMoveModelElement(RemoteMoveModelElement object) {
			return createRemoteMoveModelElementAdapter();
		}

		public Object caseRemoteRemoveAttribute(RemoteRemoveAttribute object) {
			return createRemoteRemoveAttributeAdapter();
		}

		public Object caseRemoteRemoveModelElement(RemoteRemoveModelElement object) {
			return createRemoteRemoveModelElementAdapter();
		}

		public Object caseRemoteRemoveReferenceValue(RemoteRemoveReferenceValue object) {
			return createRemoteRemoveReferenceValueAdapter();
		}

		public Object caseRemoteUpdateAttribute(RemoteUpdateAttribute object) {
			return createRemoteUpdateAttributeAdapter();
		}

		public Object caseRemoteUpdateUniqueReferenceValue(RemoteUpdateUniqueReferenceValue object) {
			return createRemoteUpdateUniqueReferenceValueAdapter();
		}

		public Object caseRemoveAttribute(RemoveAttribute object) {
			return createRemoveAttributeAdapter();
		}

		public Object caseRemoveModelElement(RemoveModelElement object) {
			return createRemoveModelElementAdapter();
		}

		public Object caseRemoveReferenceValue(RemoveReferenceValue object) {
			return createRemoveReferenceValueAdapter();
		}

		public Object caseUpdateAttribute(UpdateAttribute object) {
			return createUpdateAttributeAdapter();
		}

		public Object caseUpdateModelElement(UpdateModelElement object) {
			return createUpdateModelElementAdapter();
		}

		public Object caseUpdateReference(UpdateReference object) {
			return createUpdateReferenceAdapter();
		}

		public Object caseUpdateUniqueReferenceValue(UpdateUniqueReferenceValue object) {
			return createUpdateUniqueReferenceValueAdapter();
		}

		public Object defaultCase(EObject object) {
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
			DiffElement diff = (DiffElement)element;
			Iterator it = diff.getIsHiddenBy().iterator();
			while (it.hasNext()) {
				AbstractDiffExtension extension = (AbstractDiffExtension)it.next();
				if (!extension.isIsCollapsed())
					result = true;
			}
		}
		if (element instanceof DiffGroup) {
			DiffGroup group = (DiffGroup)element;
			if (group.getSubchanges() == 0)
				result = true;
		}
		return result;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
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
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AddAttribute <em>Add Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddAttribute
	 * @generated
	 */
	public Adapter createAddAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AddModelElement <em>Add Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddModelElement
	 * @generated
	 */
	public Adapter createAddModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue <em>Add Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddReferenceValue
	 * @generated
	 */
	public Adapter createAddReferenceValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	public Adapter createAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget <em>Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	public Adapter createAttributeChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget <em>Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	public Adapter createAttributeChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement <em>Conflicting Diff Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	public Adapter createDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup <em>Group</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * @generated
	 */
	public Adapter createDiffGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel <em>Model</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * @generated
	 */
	public Adapter createDiffModelAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.GenericDiffElement <em>Generic Diff Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.GenericDiffElement
	 * @generated
	 */
	public Adapter createGenericDiffElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	public Adapter createModelElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget <em>Model Element Change Left Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * @generated
	 */
	public Adapter createModelElementChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget <em>Model Element Change Right Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * @generated
	 */
	public Adapter createModelElementChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot <em>Model Input Snapshot</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot
	 * @generated
	 */
	public Adapter createModelInputSnapshotAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement <em>Move Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement
	 * @generated
	 */
	public Adapter createMoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	public Adapter createReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget <em>Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	public Adapter createReferenceChangeLeftTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget <em>Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	public Adapter createReferenceChangeRightTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute <em>Remote Add Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute
	 * @generated
	 */
	public Adapter createRemoteAddAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement <em>Remote Add Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement
	 * @generated
	 */
	public Adapter createRemoteAddModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue <em>Remote Add Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue
	 * @generated
	 */
	public Adapter createRemoteAddReferenceValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement <em>Remote Move Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement
	 * @generated
	 */
	public Adapter createRemoteMoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute <em>Remote Remove Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute
	 * @generated
	 */
	public Adapter createRemoteRemoveAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement <em>Remote Remove Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement
	 * @generated
	 */
	public Adapter createRemoteRemoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue <em>Remote Remove Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue
	 * @generated
	 */
	public Adapter createRemoteRemoveReferenceValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute <em>Remote Update Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute
	 * @generated
	 */
	public Adapter createRemoteUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue <em>Remote Update Unique Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue
	 * @generated
	 */
	public Adapter createRemoteUpdateUniqueReferenceValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveAttribute <em>Remove Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveAttribute
	 * @generated
	 */
	public Adapter createRemoveAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveModelElement <em>Remove Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveModelElement
	 * @generated
	 */
	public Adapter createRemoveModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue <em>Remove Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue
	 * @generated
	 */
	public Adapter createRemoveReferenceValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	public Adapter createUpdateAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	public Adapter createUpdateModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	public Adapter createUpdateReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue <em>Update Unique Reference Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue
	 * @generated
	 */
	public Adapter createUpdateUniqueReferenceValueAdapter() {
		return null;
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This
	 * implementation returns <code>true</code> if the object is either the model's package or is an
	 * instance object of the model. <!-- end-user-doc -->
	 * 
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

} // DiffAdapterFactory
