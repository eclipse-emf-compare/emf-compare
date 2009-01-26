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
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage
 * @generated
 */
public interface DiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	DiffFactory eINSTANCE = org.eclipse.emf.compare.diff.metamodel.impl.DiffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Add Attribute</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Add Attribute</em>'.
	 * @generated
	 */
	AddAttribute createAddAttribute();

	/**
	 * Returns a new object of class '<em>Add Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Add Model Element</em>'.
	 * @generated
	 */
	AddModelElement createAddModelElement();

	/**
	 * Returns a new object of class '<em>Add Reference Value</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Add Reference Value</em>'.
	 * @generated
	 */
	AddReferenceValue createAddReferenceValue();

	/**
	 * Returns a new object of class '<em>Attribute Change</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Attribute Change</em>'.
	 * @generated
	 */
	AttributeChange createAttributeChange();

	/**
	 * Returns a new object of class '<em>Attribute Change Left Target</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Attribute Change Left Target</em>'.
	 * @generated
	 */
	AttributeChangeLeftTarget createAttributeChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Attribute Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Change Right Target</em>'.
	 * @generated
	 */
	AttributeChangeRightTarget createAttributeChangeRightTarget();

	/**
	 * Returns a new object of class '<em>Conflicting Diff Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Conflicting Diff Element</em>'.
	 * @generated
	 */
	ConflictingDiffElement createConflictingDiffElement();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	DiffGroup createDiffGroup();

	/**
	 * Returns a new object of class '<em>Comparison Resource Snapshot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Comparison Resource Snapshot</em>'.
	 * @generated
	 */
	ComparisonResourceSnapshot createComparisonResourceSnapshot();

	/**
	 * Returns a new object of class '<em>Comparison Resource Set Snapshot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Comparison Resource Set Snapshot</em>'.
	 * @generated
	 */
	ComparisonResourceSetSnapshot createComparisonResourceSetSnapshot();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	DiffModel createDiffModel();

	/**
	 * Returns a new object of class '<em>Resource Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Set</em>'.
	 * @generated
	 */
	DiffResourceSet createDiffResourceSet();

	/**
	 * Returns a new object of class '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Model Element Change</em>'.
	 * @generated
	 */
	ModelElementChange createModelElementChange();

	/**
	 * Returns a new object of class '<em>Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Element Change Left Target</em>'.
	 * @generated
	 */
	ModelElementChangeLeftTarget createModelElementChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Element Change Right Target</em>'.
	 * @generated
	 */
	ModelElementChangeRightTarget createModelElementChangeRightTarget();

	/**
	 * Returns a new object of class '<em>Move Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Move Model Element</em>'.
	 * @generated
	 */
	MoveModelElement createMoveModelElement();

	/**
	 * Returns a new object of class '<em>Update Containment Feature</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Containment Feature</em>'.
	 * @generated
	 */
	UpdateContainmentFeature createUpdateContainmentFeature();

	/**
	 * Returns a new object of class '<em>Reference Change</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Reference Change</em>'.
	 * @generated
	 */
	ReferenceChange createReferenceChange();

	/**
	 * Returns a new object of class '<em>Reference Change Left Target</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Reference Change Left Target</em>'.
	 * @generated
	 */
	ReferenceChangeLeftTarget createReferenceChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>Reference Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reference Change Right Target</em>'.
	 * @generated
	 */
	ReferenceChangeRightTarget createReferenceChangeRightTarget();

	/**
	 * Returns a new object of class '<em>Remote Add Attribute</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Add Attribute</em>'.
	 * @generated
	 */
	RemoteAddAttribute createRemoteAddAttribute();

	/**
	 * Returns a new object of class '<em>Remote Add Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Add Model Element</em>'.
	 * @generated
	 */
	RemoteAddModelElement createRemoteAddModelElement();

	/**
	 * Returns a new object of class '<em>Remote Add Reference Value</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Add Reference Value</em>'.
	 * @generated
	 */
	RemoteAddReferenceValue createRemoteAddReferenceValue();

	/**
	 * Returns a new object of class '<em>Remote Move Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Move Model Element</em>'.
	 * @generated
	 */
	RemoteMoveModelElement createRemoteMoveModelElement();

	/**
	 * Returns a new object of class '<em>Remote Update Containment Feature</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remote Update Containment Feature</em>'.
	 * @generated
	 */
	RemoteUpdateContainmentFeature createRemoteUpdateContainmentFeature();

	/**
	 * Returns a new object of class '<em>Remote Remove Attribute</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Remove Attribute</em>'.
	 * @generated
	 */
	RemoteRemoveAttribute createRemoteRemoveAttribute();

	/**
	 * Returns a new object of class '<em>Remote Remove Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Remove Model Element</em>'.
	 * @generated
	 */
	RemoteRemoveModelElement createRemoteRemoveModelElement();

	/**
	 * Returns a new object of class '<em>Remote Remove Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remote Remove Reference Value</em>'.
	 * @generated
	 */
	RemoteRemoveReferenceValue createRemoteRemoveReferenceValue();

	/**
	 * Returns a new object of class '<em>Remote Update Attribute</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remote Update Attribute</em>'.
	 * @generated
	 */
	RemoteUpdateAttribute createRemoteUpdateAttribute();

	/**
	 * Returns a new object of class '<em>Remote Update Unique Reference Value</em>'.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return a new object of class '<em>Remote Update Unique Reference Value</em>'.
	 * @generated
	 */
	RemoteUpdateUniqueReferenceValue createRemoteUpdateUniqueReferenceValue();

	/**
	 * Returns a new object of class '<em>Resource Diff</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Diff</em>'.
	 * @generated
	 */
	ResourceDiff createResourceDiff();

	/**
	 * Returns a new object of class '<em>Resource Dependency Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Dependency Change</em>'.
	 * @generated
	 */
	ResourceDependencyChange createResourceDependencyChange();

	/**
	 * Returns a new object of class '<em>Add Resource Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Resource Dependency</em>'.
	 * @generated
	 */
	AddResourceDependency createAddResourceDependency();

	/**
	 * Returns a new object of class '<em>Remove Resource Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Resource Dependency</em>'.
	 * @generated
	 */
	RemoveResourceDependency createRemoveResourceDependency();

	/**
	 * Returns a new object of class '<em>Remote Add Resource Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remote Add Resource Dependency</em>'.
	 * @generated
	 */
	RemoteAddResourceDependency createRemoteAddResourceDependency();

	/**
	 * Returns a new object of class '<em>Remote Remove Resource Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remote Remove Resource Dependency</em>'.
	 * @generated
	 */
	RemoteRemoveResourceDependency createRemoteRemoveResourceDependency();

	/**
	 * Returns a new object of class '<em>Remove Attribute</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remove Attribute</em>'.
	 * @generated
	 */
	RemoveAttribute createRemoveAttribute();

	/**
	 * Returns a new object of class '<em>Remove Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remove Model Element</em>'.
	 * @generated
	 */
	RemoveModelElement createRemoveModelElement();

	/**
	 * Returns a new object of class '<em>Remove Reference Value</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Remove Reference Value</em>'.
	 * @generated
	 */
	RemoveReferenceValue createRemoveReferenceValue();

	/**
	 * Returns a new object of class '<em>Update Attribute</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Update Attribute</em>'.
	 * @generated
	 */
	UpdateAttribute createUpdateAttribute();

	/**
	 * Returns a new object of class '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Update Model Element</em>'.
	 * @generated
	 */
	UpdateModelElement createUpdateModelElement();

	/**
	 * Returns a new object of class '<em>Update Reference</em>'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return a new object of class '<em>Update Reference</em>'.
	 * @generated
	 */
	UpdateReference createUpdateReference();

	/**
	 * Returns a new object of class '<em>Update Unique Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Unique Reference Value</em>'.
	 * @generated
	 */
	UpdateUniqueReferenceValue createUpdateUniqueReferenceValue();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DiffPackage getDiffPackage();

} // DiffFactory
