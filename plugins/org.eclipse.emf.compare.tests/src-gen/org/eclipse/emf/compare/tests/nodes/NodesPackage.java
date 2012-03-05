/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.emf.compare.tests.nodes.NodesFactory
 * @model kind="package"
 * @generated
 */
public interface NodesPackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "nodes"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "namespaceURI"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "compare.tests"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NodesPackage eINSTANCE = org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.nodes.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodeImpl
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__UUID = 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.nodes.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.nodes.impl.GroupImpl
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__UUID = NODE__UUID;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__CHILDREN = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl <em>Leaf</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.nodes.impl.LeafImpl
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getLeaf()
	 * @generated
	 */
	int LEAF = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__UUID = NODE__UUID;

	/**
	 * The feature id for the '<em><b>Noncontainment Holder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__NONCONTAINMENT_HOLDER = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Containment Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__CONTAINMENT_HOLDER = NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__NUMBER = NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__HOLDER = NODE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Non EMF</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__NON_EMF = NODE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Noncontainment Noncontainment Holder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER = NODE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Leaf</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF_FEATURE_COUNT = NODE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '<em>UUID</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.UUID
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getUUID()
	 * @generated
	 */
	int UUID = 3;

	/**
	 * The meta object id for the '<em>Non EMF String Holder</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder
	 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getNonEMFStringHolder()
	 * @generated
	 */
	int NON_EMF_STRING_HOLDER = 4;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.nodes.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.tests.nodes.Group#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Group#getChildren()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.nodes.Leaf <em>Leaf</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Leaf</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf
	 * @generated
	 */
	EClass getLeaf();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentHolder <em>Noncontainment Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Noncontainment Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentHolder()
	 * @see #getLeaf()
	 * @generated
	 */
	EReference getLeaf_NoncontainmentHolder();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getContainmentHolder <em>Containment Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Containment Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getContainmentHolder()
	 * @see #getLeaf()
	 * @generated
	 */
	EReference getLeaf_ContainmentHolder();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getNumber()
	 * @see #getLeaf()
	 * @generated
	 */
	EAttribute getLeaf_Number();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getHolder <em>Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getHolder()
	 * @see #getLeaf()
	 * @generated
	 */
	EReference getLeaf_Holder();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNonEMF <em>Non EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Non EMF</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getNonEMF()
	 * @see #getLeaf()
	 * @generated
	 */
	EAttribute getLeaf_NonEMF();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentNoncontainmentHolder <em>Noncontainment Noncontainment Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Noncontainment Noncontainment Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentNoncontainmentHolder()
	 * @see #getLeaf()
	 * @generated
	 */
	EReference getLeaf_NoncontainmentNoncontainmentHolder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.nodes.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.nodes.Node#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Node#getName()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.nodes.Node#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see org.eclipse.emf.compare.tests.nodes.Node#getUuid()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Uuid();

	/**
	 * Returns the meta object for data type '{@link java.util.UUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>UUID</em>'.
	 * @see java.util.UUID
	 * @model instanceClass="java.util.UUID"
	 * @generated
	 */
	EDataType getUUID();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder <em>Non EMF String Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Non EMF String Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder
	 * @model instanceClass="org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder"
	 * @generated
	 */
	EDataType getNonEMFStringHolder();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	NodesFactory getNodesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.nodes.impl.GroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.nodes.impl.GroupImpl
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getGroup()
		 * @generated
		 */
		EClass GROUP = eINSTANCE.getGroup();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP__CHILDREN = eINSTANCE.getGroup_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl <em>Leaf</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.nodes.impl.LeafImpl
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getLeaf()
		 * @generated
		 */
		EClass LEAF = eINSTANCE.getLeaf();

		/**
		 * The meta object literal for the '<em><b>Noncontainment Holder</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEAF__NONCONTAINMENT_HOLDER = eINSTANCE.getLeaf_NoncontainmentHolder();

		/**
		 * The meta object literal for the '<em><b>Containment Holder</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEAF__CONTAINMENT_HOLDER = eINSTANCE.getLeaf_ContainmentHolder();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LEAF__NUMBER = eINSTANCE.getLeaf_Number();

		/**
		 * The meta object literal for the '<em><b>Holder</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEAF__HOLDER = eINSTANCE.getLeaf_Holder();

		/**
		 * The meta object literal for the '<em><b>Non EMF</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LEAF__NON_EMF = eINSTANCE.getLeaf_NonEMF();

		/**
		 * The meta object literal for the '<em><b>Noncontainment Noncontainment Holder</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER = eINSTANCE.getLeaf_NoncontainmentNoncontainmentHolder();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.nodes.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodeImpl
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getNode()
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
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__UUID = eINSTANCE.getNode_Uuid();

		/**
		 * The meta object literal for the '<em>UUID</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.UUID
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getUUID()
		 * @generated
		 */
		EDataType UUID = eINSTANCE.getUUID();

		/**
		 * The meta object literal for the '<em>Non EMF String Holder</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder
		 * @see org.eclipse.emf.compare.tests.nodes.impl.NodesPackageImpl#getNonEMFStringHolder()
		 * @generated
		 */
		EDataType NON_EMF_STRING_HOLDER = eINSTANCE.getNonEMFStringHolder();

	}

} //NodesPackage
