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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage
 * @generated
 */
public interface NodesFactory extends EFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NodesFactory eINSTANCE = org.eclipse.emf.compare.tests.nodes.impl.NodesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node</em>'.
	 * @generated
	 */
	Node createNode();

	/**
	 * Returns a new object of class '<em>Node Multiple Containment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Multiple Containment</em>'.
	 * @generated
	 */
	NodeMultipleContainment createNodeMultipleContainment();

	/**
	 * Returns a new object of class '<em>Node Single Value Containment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Single Value Containment</em>'.
	 * @generated
	 */
	NodeSingleValueContainment createNodeSingleValueContainment();

	/**
	 * Returns a new object of class '<em>Node Single Value Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Single Value Attribute</em>'.
	 * @generated
	 */
	NodeSingleValueAttribute createNodeSingleValueAttribute();

	/**
	 * Returns a new object of class '<em>Node Multi Valued Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Multi Valued Attribute</em>'.
	 * @generated
	 */
	NodeMultiValuedAttribute createNodeMultiValuedAttribute();

	/**
	 * Returns a new object of class '<em>Node Single Value Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Single Value Reference</em>'.
	 * @generated
	 */
	NodeSingleValueReference createNodeSingleValueReference();

	/**
	 * Returns a new object of class '<em>Node Multi Value Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Multi Value Reference</em>'.
	 * @generated
	 */
	NodeMultiValueReference createNodeMultiValueReference();

	/**
	 * Returns a new object of class '<em>Node Opposite Ref One To One</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Opposite Ref One To One</em>'.
	 * @generated
	 */
	NodeOppositeRefOneToOne createNodeOppositeRefOneToOne();

	/**
	 * Returns a new object of class '<em>Node Opposite Ref One To Many</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Opposite Ref One To Many</em>'.
	 * @generated
	 */
	NodeOppositeRefOneToMany createNodeOppositeRefOneToMany();

	/**
	 * Returns a new object of class '<em>Node Opposite Ref Many To Many</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Opposite Ref Many To Many</em>'.
	 * @generated
	 */
	NodeOppositeRefManyToMany createNodeOppositeRefManyToMany();

	/**
	 * Returns a new object of class '<em>Node Feature Map Containment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Feature Map Containment</em>'.
	 * @generated
	 */
	NodeFeatureMapContainment createNodeFeatureMapContainment();

	/**
	 * Returns a new object of class '<em>Node Feature Map Non Containment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Feature Map Non Containment</em>'.
	 * @generated
	 */
	NodeFeatureMapNonContainment createNodeFeatureMapNonContainment();

	/**
	 * Returns a new object of class '<em>Node Feature Map Containment2</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Feature Map Containment2</em>'.
	 * @generated
	 */
	NodeFeatureMapContainment2 createNodeFeatureMapContainment2();

	/**
	 * Returns a new object of class '<em>Node Single Value EEnum Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Single Value EEnum Attribute</em>'.
	 * @generated
	 */
	NodeSingleValueEEnumAttribute createNodeSingleValueEEnumAttribute();

	/**
	 * Returns a new object of class '<em>Node Multi Value EEnum Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Multi Value EEnum Attribute</em>'.
	 * @generated
	 */
	NodeMultiValueEEnumAttribute createNodeMultiValueEEnumAttribute();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	NodesPackage getNodesPackage();

} //NodesFactory
