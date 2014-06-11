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
 * A representation of the model object '<em><b>Node Feature Map Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment#getMap <em>Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment#getFirstKey <em>First Key</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment#getSecondKey <em>Second Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment()
 * @model
 * @generated
 */
public interface NodeFeatureMapContainment extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Map</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment_Map()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getMap();

	/**
	 * Returns the value of the '<em><b>First Key</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Key</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>First Key</em>' containment reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment_FirstKey()
	 * @model containment="true" resolveProxies="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#map'"
	 * @generated
	 */
	EList<Node> getFirstKey();

	/**
	 * Returns the value of the '<em><b>Second Key</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Second Key</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Second Key</em>' containment reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment_SecondKey()
	 * @model containment="true" resolveProxies="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#map'"
	 * @generated
	 */
	EList<Node> getSecondKey();

} // NodeFeatureMapContainment
