/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.tests.merge.MergeFactory
 * @model kind="package"
 * @generated
 */
public interface MergePackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "merge"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/tests/merge"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "merge"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MergePackage eINSTANCE = org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.merge.impl.NodeImpl
	 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNode()
	 * @generated
	 */
	int NODE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Containment Ref1</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__CONTAINMENT_REF1 = 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeMultipleContainmentImpl <em>Node Multiple Containment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.merge.impl.NodeMultipleContainmentImpl
	 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNodeMultipleContainment()
	 * @generated
	 */
	int NODE_MULTIPLE_CONTAINMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_MULTIPLE_CONTAINMENT__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Containment Ref1</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF1 = NODE__CONTAINMENT_REF1;

	/**
	 * The feature id for the '<em><b>Containment Ref2</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2 = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Node Multiple Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_MULTIPLE_CONTAINMENT_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeSingleValueContainmentImpl <em>Node Single Value Containment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.merge.impl.NodeSingleValueContainmentImpl
	 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNodeSingleValueContainment()
	 * @generated
	 */
	int NODE_SINGLE_VALUE_CONTAINMENT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_SINGLE_VALUE_CONTAINMENT__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Containment Ref1</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_SINGLE_VALUE_CONTAINMENT__CONTAINMENT_REF1 = NODE__CONTAINMENT_REF1;

	/**
	 * The feature id for the '<em><b>Single Value Containment</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Node Single Value Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_SINGLE_VALUE_CONTAINMENT_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.merge.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.merge.Node#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.Node#getName()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.tests.merge.Node#getContainmentRef1 <em>Containment Ref1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Containment Ref1</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.Node#getContainmentRef1()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_ContainmentRef1();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.merge.NodeMultipleContainment <em>Node Multiple Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Multiple Containment</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.NodeMultipleContainment
	 * @generated
	 */
	EClass getNodeMultipleContainment();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.tests.merge.NodeMultipleContainment#getContainmentRef2 <em>Containment Ref2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Containment Ref2</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.NodeMultipleContainment#getContainmentRef2()
	 * @see #getNodeMultipleContainment()
	 * @generated
	 */
	EReference getNodeMultipleContainment_ContainmentRef2();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.merge.NodeSingleValueContainment <em>Node Single Value Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Single Value Containment</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.NodeSingleValueContainment
	 * @generated
	 */
	EClass getNodeSingleValueContainment();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.tests.merge.NodeSingleValueContainment#getSingleValueContainment <em>Single Value Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Single Value Containment</em>'.
	 * @see org.eclipse.emf.compare.tests.merge.NodeSingleValueContainment#getSingleValueContainment()
	 * @see #getNodeSingleValueContainment()
	 * @generated
	 */
	EReference getNodeSingleValueContainment_SingleValueContainment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MergeFactory getMergeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.merge.impl.NodeImpl
		 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__NAME = eINSTANCE.getNode_Name();

		/**
		 * The meta object literal for the '<em><b>Containment Ref1</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__CONTAINMENT_REF1 = eINSTANCE.getNode_ContainmentRef1();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeMultipleContainmentImpl <em>Node Multiple Containment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.merge.impl.NodeMultipleContainmentImpl
		 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNodeMultipleContainment()
		 * @generated
		 */
		EClass NODE_MULTIPLE_CONTAINMENT = eINSTANCE.getNodeMultipleContainment();

		/**
		 * The meta object literal for the '<em><b>Containment Ref2</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2 = eINSTANCE.getNodeMultipleContainment_ContainmentRef2();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.merge.impl.NodeSingleValueContainmentImpl <em>Node Single Value Containment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.merge.impl.NodeSingleValueContainmentImpl
		 * @see org.eclipse.emf.compare.tests.merge.impl.MergePackageImpl#getNodeSingleValueContainment()
		 * @generated
		 */
		EClass NODE_SINGLE_VALUE_CONTAINMENT = eINSTANCE.getNodeSingleValueContainment();

		/**
		 * The meta object literal for the '<em><b>Single Value Containment</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT = eINSTANCE.getNodeSingleValueContainment_SingleValueContainment();

	}

} //MergePackage
