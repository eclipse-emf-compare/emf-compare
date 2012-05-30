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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Opposite Ref Many To Many</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getDestination <em>Destination</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefManyToMany()
 * @model
 * @generated
 */
public interface NodeOppositeRefManyToMany extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefManyToMany_Source()
	 * @see org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getDestination
	 * @model opposite="destination"
	 * @generated
	 */
	EList<NodeOppositeRefManyToMany> getSource();

	/**
	 * Returns the value of the '<em><b>Destination</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination</em>' reference list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefManyToMany_Destination()
	 * @see org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany#getSource
	 * @model opposite="source"
	 * @generated
	 */
	EList<NodeOppositeRefManyToMany> getDestination();

} // NodeOppositeRefManyToMany
