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
package org.eclipse.emf.compare.tests.nodes.impl;

import org.eclipse.emf.compare.tests.nodes.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class NodesFactoryImpl extends EFactoryImpl implements NodesFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static NodesFactory init() {
		try {
			NodesFactory theNodesFactory = (NodesFactory)EPackage.Registry.INSTANCE.getEFactory(NodesPackage.eNS_URI);
			if (theNodesFactory != null) {
				return theNodesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new NodesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case NodesPackage.NODE: return createNode();
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT: return createNodeMultipleContainment();
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT: return createNodeSingleValueContainment();
			case NodesPackage.NODE_SINGLE_VALUE_ATTRIBUTE: return createNodeSingleValueAttribute();
			case NodesPackage.NODE_MULTI_VALUED_ATTRIBUTE: return createNodeMultiValuedAttribute();
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE: return createNodeSingleValueReference();
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE: return createNodeMultiValueReference();
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE: return createNodeOppositeRefOneToOne();
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY: return createNodeOppositeRefOneToMany();
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY: return createNodeOppositeRefManyToMany();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT: return createNodeFeatureMapContainment();
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT: return createNodeFeatureMapNonContainment();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node createNode() {
		NodeImpl node = new NodeImpl();
		return node;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeMultipleContainment createNodeMultipleContainment() {
		NodeMultipleContainmentImpl nodeMultipleContainment = new NodeMultipleContainmentImpl();
		return nodeMultipleContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeSingleValueContainment createNodeSingleValueContainment() {
		NodeSingleValueContainmentImpl nodeSingleValueContainment = new NodeSingleValueContainmentImpl();
		return nodeSingleValueContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeSingleValueAttribute createNodeSingleValueAttribute() {
		NodeSingleValueAttributeImpl nodeSingleValueAttribute = new NodeSingleValueAttributeImpl();
		return nodeSingleValueAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeMultiValuedAttribute createNodeMultiValuedAttribute() {
		NodeMultiValuedAttributeImpl nodeMultiValuedAttribute = new NodeMultiValuedAttributeImpl();
		return nodeMultiValuedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeSingleValueReference createNodeSingleValueReference() {
		NodeSingleValueReferenceImpl nodeSingleValueReference = new NodeSingleValueReferenceImpl();
		return nodeSingleValueReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeMultiValueReference createNodeMultiValueReference() {
		NodeMultiValueReferenceImpl nodeMultiValueReference = new NodeMultiValueReferenceImpl();
		return nodeMultiValueReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToOne createNodeOppositeRefOneToOne() {
		NodeOppositeRefOneToOneImpl nodeOppositeRefOneToOne = new NodeOppositeRefOneToOneImpl();
		return nodeOppositeRefOneToOne;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToMany createNodeOppositeRefOneToMany() {
		NodeOppositeRefOneToManyImpl nodeOppositeRefOneToMany = new NodeOppositeRefOneToManyImpl();
		return nodeOppositeRefOneToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefManyToMany createNodeOppositeRefManyToMany() {
		NodeOppositeRefManyToManyImpl nodeOppositeRefManyToMany = new NodeOppositeRefManyToManyImpl();
		return nodeOppositeRefManyToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeFeatureMapContainment createNodeFeatureMapContainment() {
		NodeFeatureMapContainmentImpl nodeFeatureMapContainment = new NodeFeatureMapContainmentImpl();
		return nodeFeatureMapContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeFeatureMapNonContainment createNodeFeatureMapNonContainment() {
		NodeFeatureMapNonContainmentImpl nodeFeatureMapNonContainment = new NodeFeatureMapNonContainmentImpl();
		return nodeFeatureMapNonContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodesPackage getNodesPackage() {
		return (NodesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static NodesPackage getPackage() {
		return NodesPackage.eINSTANCE;
	}

} //NodesFactoryImpl
