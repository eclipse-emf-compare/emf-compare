/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: EachoncePackage.java,v 1.1 2010/09/10 15:40:34 cbrun Exp $
 *******************************************************************************/
package eachonce;

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
 * @see eachonce.EachonceFactory
 * @model kind="package"
 * @generated
 */
public interface EachoncePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "eachonce";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eachonce";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "eachonce";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EachoncePackage eINSTANCE = eachonce.impl.EachoncePackageImpl.init();

	/**
	 * The meta object id for the '{@link eachonce.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eachonce.impl.EntityImpl
	 * @see eachonce.impl.EachoncePackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Single Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__SINGLE_ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Multi Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__MULTI_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Single Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__SINGLE_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Multi Reference</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__MULTI_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__CHILDREN = 4;

	/**
	 * The feature id for the '<em><b>Additional Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__ADDITIONAL_ATTRIBUTE = 5;

	/**
	 * The feature id for the '<em><b>Children2</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__CHILDREN2 = 6;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = 7;


	/**
	 * Returns the meta object for class '{@link eachonce.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see eachonce.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the attribute '{@link eachonce.Entity#getSingleAttribute <em>Single Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Single Attribute</em>'.
	 * @see eachonce.Entity#getSingleAttribute()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_SingleAttribute();

	/**
	 * Returns the meta object for the attribute list '{@link eachonce.Entity#getMultiAttribute <em>Multi Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Multi Attribute</em>'.
	 * @see eachonce.Entity#getMultiAttribute()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_MultiAttribute();

	/**
	 * Returns the meta object for the reference '{@link eachonce.Entity#getSingleReference <em>Single Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Single Reference</em>'.
	 * @see eachonce.Entity#getSingleReference()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_SingleReference();

	/**
	 * Returns the meta object for the reference list '{@link eachonce.Entity#getMultiReference <em>Multi Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Multi Reference</em>'.
	 * @see eachonce.Entity#getMultiReference()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_MultiReference();

	/**
	 * Returns the meta object for the containment reference list '{@link eachonce.Entity#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see eachonce.Entity#getChildren()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_Children();

	/**
	 * Returns the meta object for the attribute '{@link eachonce.Entity#getAdditionalAttribute <em>Additional Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Additional Attribute</em>'.
	 * @see eachonce.Entity#getAdditionalAttribute()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_AdditionalAttribute();

	/**
	 * Returns the meta object for the containment reference list '{@link eachonce.Entity#getChildren2 <em>Children2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children2</em>'.
	 * @see eachonce.Entity#getChildren2()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_Children2();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EachonceFactory getEachonceFactory();

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
		 * The meta object literal for the '{@link eachonce.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eachonce.impl.EntityImpl
		 * @see eachonce.impl.EachoncePackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Single Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__SINGLE_ATTRIBUTE = eINSTANCE.getEntity_SingleAttribute();

		/**
		 * The meta object literal for the '<em><b>Multi Attribute</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__MULTI_ATTRIBUTE = eINSTANCE.getEntity_MultiAttribute();

		/**
		 * The meta object literal for the '<em><b>Single Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__SINGLE_REFERENCE = eINSTANCE.getEntity_SingleReference();

		/**
		 * The meta object literal for the '<em><b>Multi Reference</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__MULTI_REFERENCE = eINSTANCE.getEntity_MultiReference();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__CHILDREN = eINSTANCE.getEntity_Children();

		/**
		 * The meta object literal for the '<em><b>Additional Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__ADDITIONAL_ATTRIBUTE = eINSTANCE.getEntity_AdditionalAttribute();

		/**
		 * The meta object literal for the '<em><b>Children2</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__CHILDREN2 = eINSTANCE.getEntity_Children2();

	}

} //EachoncePackage
