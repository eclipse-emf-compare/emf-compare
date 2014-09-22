/**
 * Copyright (c) 2011, 2014 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Node Feature Map Containment2</b></em>
 * '. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment2#getMap2 <em>Map2</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment2#getMultiple <em>Multiple</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment2#getSingle <em>Single</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment2()
 * @model
 * @generated
 */
public interface NodeFeatureMapContainment2 extends Node {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Map2</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map2</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map2</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment2_Map2()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getMap2();

	/**
	 * Returns the value of the '<em><b>Multiple</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple</em>' containment reference list isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple</em>' containment reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment2_Multiple()
	 * @model containment="true" resolveProxies="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#map2'"
	 * @generated
	 */
	EList<NodeMultipleContainment> getMultiple();

	/**
	 * Returns the value of the '<em><b>Single</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.NodeSingleValueContainment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single</em>' containment reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapContainment2_Single()
	 * @model containment="true" resolveProxies="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#map2'"
	 * @generated
	 */
	EList<NodeSingleValueContainment> getSingle();

} // NodeFeatureMapContainment2
