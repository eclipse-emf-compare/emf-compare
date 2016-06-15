/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to
 * represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfileFactory
 * @model kind="package" annotation=
 *        "http://www.eclipse.org/uml2/2.0.0/UML originalName='UML2CompareTestProfile'"
 * @generated
 */
public interface UML2CompareTestProfilePackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "uml2comparetestprofile"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "testProfile"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	UML2CompareTestProfilePackage eINSTANCE = org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl
	 * <em>ACliche</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche()
	 * @generated
	 */
	int ACLICHE = 0;

	/**
	 * The feature id for the '<em><b>Single Valued Attribute</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE__SINGLE_VALUED_ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Many Valued Attribute</b></em>' attribute list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE__MANY_VALUED_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Single Valued Reference</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE__SINGLE_VALUED_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Many Valued Reference</b></em>' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE__MANY_VALUED_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Base Class</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE__BASE_CLASS = 4;

	/**
	 * The number of structural features of the '<em>ACliche</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche2Impl
	 * <em>ACliche2</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche2Impl
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche2()
	 * @generated
	 */
	int ACLICHE2 = 1;

	/**
	 * The feature id for the '<em><b>Single Valued Attribute</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2__SINGLE_VALUED_ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Many Valued Attribute</b></em>' attribute list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2__MANY_VALUED_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Single Valued Reference</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2__SINGLE_VALUED_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Many Valued Reference</b></em>' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2__MANY_VALUED_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Base Class</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2__BASE_CLASS = 4;

	/**
	 * The number of structural features of the '<em>ACliche2</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE2_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche3Impl
	 * <em>ACliche3</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche3Impl
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche3()
	 * @generated
	 */
	int ACLICHE3 = 2;

	/**
	 * The feature id for the '<em><b>Base Class</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE3__BASE_CLASS = 0;

	/**
	 * The number of structural features of the '<em>ACliche3</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACLICHE3_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche <em>ACliche</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>ACliche</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche
	 * @generated
	 */
	EClass getACliche();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedAttribute
	 * <em>Single Valued Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Single Valued Attribute</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedAttribute()
	 * @see #getACliche()
	 * @generated
	 */
	EAttribute getACliche_SingleValuedAttribute();

	/**
	 * Returns the meta object for the attribute list '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedAttribute
	 * <em>Many Valued Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Many Valued Attribute</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedAttribute()
	 * @see #getACliche()
	 * @generated
	 */
	EAttribute getACliche_ManyValuedAttribute();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedReference
	 * <em>Single Valued Reference</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Single Valued Reference</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedReference()
	 * @see #getACliche()
	 * @generated
	 */
	EReference getACliche_SingleValuedReference();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedReference
	 * <em>Many Valued Reference</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Many Valued Reference</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedReference()
	 * @see #getACliche()
	 * @generated
	 */
	EReference getACliche_ManyValuedReference();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getBase_Class
	 * <em>Base Class</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Base Class</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getBase_Class()
	 * @see #getACliche()
	 * @generated
	 */
	EReference getACliche_Base_Class();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2 <em>ACliche2</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>ACliche2</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2
	 * @generated
	 */
	EClass getACliche2();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getSingleValuedAttribute
	 * <em>Single Valued Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Single Valued Attribute</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getSingleValuedAttribute()
	 * @see #getACliche2()
	 * @generated
	 */
	EAttribute getACliche2_SingleValuedAttribute();

	/**
	 * Returns the meta object for the attribute list '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getManyValuedAttribute
	 * <em>Many Valued Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Many Valued Attribute</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getManyValuedAttribute()
	 * @see #getACliche2()
	 * @generated
	 */
	EAttribute getACliche2_ManyValuedAttribute();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getSingleValuedReference
	 * <em>Single Valued Reference</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Single Valued Reference</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getSingleValuedReference()
	 * @see #getACliche2()
	 * @generated
	 */
	EReference getACliche2_SingleValuedReference();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getManyValuedReference
	 * <em>Many Valued Reference</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Many Valued Reference</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getManyValuedReference()
	 * @see #getACliche2()
	 * @generated
	 */
	EReference getACliche2_ManyValuedReference();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getBase_Class
	 * <em>Base Class</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Base Class</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2#getBase_Class()
	 * @see #getACliche2()
	 * @generated
	 */
	EReference getACliche2_Base_Class();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3 <em>ACliche3</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>ACliche3</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3
	 * @generated
	 */
	EClass getACliche3();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3#getBase_Class
	 * <em>Base Class</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Base Class</em>'.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3#getBase_Class()
	 * @see #getACliche3()
	 * @generated
	 */
	EReference getACliche3_Base_Class();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UML2CompareTestProfileFactory getUML2CompareTestProfileFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl
		 * <em>ACliche</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche()
		 * @generated
		 */
		EClass ACLICHE = eINSTANCE.getACliche();

		/**
		 * The meta object literal for the '<em><b>Single Valued Attribute</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACLICHE__SINGLE_VALUED_ATTRIBUTE = eINSTANCE.getACliche_SingleValuedAttribute();

		/**
		 * The meta object literal for the '<em><b>Many Valued Attribute</b></em>' attribute list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACLICHE__MANY_VALUED_ATTRIBUTE = eINSTANCE.getACliche_ManyValuedAttribute();

		/**
		 * The meta object literal for the '<em><b>Single Valued Reference</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE__SINGLE_VALUED_REFERENCE = eINSTANCE.getACliche_SingleValuedReference();

		/**
		 * The meta object literal for the '<em><b>Many Valued Reference</b></em>' reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE__MANY_VALUED_REFERENCE = eINSTANCE.getACliche_ManyValuedReference();

		/**
		 * The meta object literal for the '<em><b>Base Class</b></em>' reference feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE__BASE_CLASS = eINSTANCE.getACliche_Base_Class();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche2Impl
		 * <em>ACliche2</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche2Impl
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche2()
		 * @generated
		 */
		EClass ACLICHE2 = eINSTANCE.getACliche2();

		/**
		 * The meta object literal for the '<em><b>Single Valued Attribute</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACLICHE2__SINGLE_VALUED_ATTRIBUTE = eINSTANCE.getACliche2_SingleValuedAttribute();

		/**
		 * The meta object literal for the '<em><b>Many Valued Attribute</b></em>' attribute list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACLICHE2__MANY_VALUED_ATTRIBUTE = eINSTANCE.getACliche2_ManyValuedAttribute();

		/**
		 * The meta object literal for the '<em><b>Single Valued Reference</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE2__SINGLE_VALUED_REFERENCE = eINSTANCE.getACliche2_SingleValuedReference();

		/**
		 * The meta object literal for the '<em><b>Many Valued Reference</b></em>' reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE2__MANY_VALUED_REFERENCE = eINSTANCE.getACliche2_ManyValuedReference();

		/**
		 * The meta object literal for the '<em><b>Base Class</b></em>' reference feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE2__BASE_CLASS = eINSTANCE.getACliche2_Base_Class();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche3Impl
		 * <em>ACliche3</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.ACliche3Impl
		 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.UML2CompareTestProfilePackageImpl#getACliche3()
		 * @generated
		 */
		EClass ACLICHE3 = eINSTANCE.getACliche3();

		/**
		 * The meta object literal for the '<em><b>Base Class</b></em>' reference feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACLICHE3__BASE_CLASS = eINSTANCE.getACliche3_Base_Class();

	}

} // UML2CompareTestProfilePackage
