/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Feature Map Non Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapNonContainment#getMapNC <em>Map NC</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapNonContainment#getFirstKeyNC <em>First Key NC</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapNonContainment#getSecondKeyNC <em>Second Key NC</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapNonContainment()
 * @model
 * @generated
 */
public interface NodeFeatureMapNonContainment extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Map NC</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map NC</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map NC</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapNonContainment_MapNC()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getMapNC();

	/**
	 * Returns the value of the '<em><b>First Key NC</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Key NC</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>First Key NC</em>' reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapNonContainment_FirstKeyNC()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#mapNC'"
	 * @generated
	 */
	EList<Node> getFirstKeyNC();

	/**
	 * Returns the value of the '<em><b>Second Key NC</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Second Key NC</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Second Key NC</em>' reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapNonContainment_SecondKeyNC()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#mapNC'"
	 * @generated
	 */
	EList<Node> getSecondKeyNC();

} // NodeFeatureMapNonContainment
