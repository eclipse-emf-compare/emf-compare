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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Opposite Ref One To One</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getDestination <em>Destination</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefOneToOne()
 * @model
 * @generated
 */
public interface NodeOppositeRefOneToOne extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(NodeOppositeRefOneToOne)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefOneToOne_Source()
	 * @see org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getDestination
	 * @model opposite="destination"
	 * @generated
	 */
	NodeOppositeRefOneToOne getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(NodeOppositeRefOneToOne value);

	/**
	 * Returns the value of the '<em><b>Destination</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination</em>' reference.
	 * @see #setDestination(NodeOppositeRefOneToOne)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeOppositeRefOneToOne_Destination()
	 * @see org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getSource
	 * @model opposite="source"
	 * @generated
	 */
	NodeOppositeRefOneToOne getDestination();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne#getDestination <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination</em>' reference.
	 * @see #getDestination()
	 * @generated
	 */
	void setDestination(NodeOppositeRefOneToOne value);

} // NodeOppositeRefOneToOne
