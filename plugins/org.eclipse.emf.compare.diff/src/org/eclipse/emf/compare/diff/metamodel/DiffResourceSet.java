/*******************************************************************************
 * Copyright (c) 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Set</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Root of the differences model for ResourceSet-wide differencing.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getDiffModels <em>Diff Models</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getResourceDiffs <em>Resource Diffs</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffResourceSet()
 * @model
 * @generated
 */
public interface DiffResourceSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Diff Models</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.DiffModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff Models</em>' containment reference list isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diff Models</em>' containment reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffResourceSet_DiffModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<DiffModel> getDiffModels();

	/**
	 * Returns the value of the '<em><b>Resource Diffs</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.ResourceDiff}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Diffs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Diffs</em>' containment reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffResourceSet_ResourceDiffs()
	 * @model containment="true"
	 * @generated
	 */
	EList<ResourceDiff> getResourceDiffs();

} // DiffResourceSet
