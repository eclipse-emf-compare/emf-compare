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
 * $Id: SymrefsPackage.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;

import org.eclipse.emf.compare.mpatch.MPatchPackage;
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
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory
 * @model kind="package"
 * @generated
 */
public interface SymrefsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "symrefs";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/mpatch/1.0/symrefs";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "symrefs";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SymrefsPackage eINSTANCE = org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl <em>External Element Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getExternalElementReference()
	 * @generated
	 */
	int EXTERNAL_ELEMENT_REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE__TYPE = MPatchPackage.IELEMENT_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Uri Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE = MPatchPackage.IELEMENT_REFERENCE__URI_REFERENCE;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND = MPatchPackage.IELEMENT_REFERENCE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE__LOWER_BOUND = MPatchPackage.IELEMENT_REFERENCE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE__LABEL = MPatchPackage.IELEMENT_REFERENCE__LABEL;

	/**
	 * The number of structural features of the '<em>External Element Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_ELEMENT_REFERENCE_FEATURE_COUNT = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl <em>Id Emf Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getIdEmfReference()
	 * @generated
	 */
	int ID_EMF_REFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__TYPE = MPatchPackage.IELEMENT_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Uri Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__URI_REFERENCE = MPatchPackage.IELEMENT_REFERENCE__URI_REFERENCE;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__UPPER_BOUND = MPatchPackage.IELEMENT_REFERENCE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__LOWER_BOUND = MPatchPackage.IELEMENT_REFERENCE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__LABEL = MPatchPackage.IELEMENT_REFERENCE__LABEL;

	/**
	 * The feature id for the '<em><b>Id Attribute Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Id Emf Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ID_EMF_REFERENCE_FEATURE_COUNT = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl <em>Element Set Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getElementSetReference()
	 * @generated
	 */
	int ELEMENT_SET_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__TYPE = MPatchPackage.IELEMENT_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Uri Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__URI_REFERENCE = MPatchPackage.IELEMENT_REFERENCE__URI_REFERENCE;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__UPPER_BOUND = MPatchPackage.IELEMENT_REFERENCE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__LOWER_BOUND = MPatchPackage.IELEMENT_REFERENCE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__LABEL = MPatchPackage.IELEMENT_REFERENCE__LABEL;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__CONDITIONS = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Context</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE__CONTEXT = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Element Set Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_SET_REFERENCE_FEATURE_COUNT = MPatchPackage.IELEMENT_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition <em>Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.symrefs.Condition
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getCondition()
	 * @generated
	 */
	int CONDITION = 3;

	/**
	 * The feature id for the '<em><b>Element Reference</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION__ELEMENT_REFERENCE = 0;

	/**
	 * The number of structural features of the '<em>Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl <em>Ocl Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl
	 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getOclCondition()
	 * @generated
	 */
	int OCL_CONDITION = 4;

	/**
	 * The feature id for the '<em><b>Element Reference</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_CONDITION__ELEMENT_REFERENCE = CONDITION__ELEMENT_REFERENCE;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_CONDITION__EXPRESSION = CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Check Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_CONDITION__CHECK_TYPE = CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Ocl Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_CONDITION_FEATURE_COUNT = CONDITION_FEATURE_COUNT + 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference <em>External Element Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>External Element Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference
	 * @generated
	 */
	EClass getExternalElementReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference <em>Id Emf Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Id Emf Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference
	 * @generated
	 */
	EClass getIdEmfReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference#getIdAttributeValue <em>Id Attribute Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id Attribute Value</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference#getIdAttributeValue()
	 * @see #getIdEmfReference()
	 * @generated
	 */
	EAttribute getIdEmfReference_IdAttributeValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference <em>Element Set Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Set Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference
	 * @generated
	 */
	EClass getElementSetReference();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Context</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getContext()
	 * @see #getElementSetReference()
	 * @generated
	 */
	EReference getElementSetReference_Context();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conditions</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getConditions()
	 * @see #getElementSetReference()
	 * @generated
	 */
	EReference getElementSetReference_Conditions();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Condition</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.Condition
	 * @generated
	 */
	EClass getCondition();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference <em>Element Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Element Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference()
	 * @see #getCondition()
	 * @generated
	 */
	EReference getCondition_ElementReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition <em>Ocl Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ocl Condition</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.OclCondition
	 * @generated
	 */
	EClass getOclCondition();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.OclCondition#getExpression()
	 * @see #getOclCondition()
	 * @generated
	 */
	EAttribute getOclCondition_Expression();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#isCheckType <em>Check Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Check Type</em>'.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.OclCondition#isCheckType()
	 * @see #getOclCondition()
	 * @generated
	 */
	EAttribute getOclCondition_CheckType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SymrefsFactory getSymrefsFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl <em>External Element Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getExternalElementReference()
		 * @generated
		 */
		EClass EXTERNAL_ELEMENT_REFERENCE = eINSTANCE.getExternalElementReference();
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl <em>Id Emf Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getIdEmfReference()
		 * @generated
		 */
		EClass ID_EMF_REFERENCE = eINSTANCE.getIdEmfReference();
		/**
		 * The meta object literal for the '<em><b>Id Attribute Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE = eINSTANCE.getIdEmfReference_IdAttributeValue();
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl <em>Element Set Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getElementSetReference()
		 * @generated
		 */
		EClass ELEMENT_SET_REFERENCE = eINSTANCE.getElementSetReference();
		/**
		 * The meta object literal for the '<em><b>Context</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_SET_REFERENCE__CONTEXT = eINSTANCE.getElementSetReference_Context();
		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_SET_REFERENCE__CONDITIONS = eINSTANCE.getElementSetReference_Conditions();
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition <em>Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.symrefs.Condition
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getCondition()
		 * @generated
		 */
		EClass CONDITION = eINSTANCE.getCondition();
		/**
		 * The meta object literal for the '<em><b>Element Reference</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITION__ELEMENT_REFERENCE = eINSTANCE.getCondition_ElementReference();
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl <em>Ocl Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl
		 * @see org.eclipse.emf.compare.mpatch.symrefs.impl.SymrefsPackageImpl#getOclCondition()
		 * @generated
		 */
		EClass OCL_CONDITION = eINSTANCE.getOclCondition();
		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OCL_CONDITION__EXPRESSION = eINSTANCE.getOclCondition_Expression();
		/**
		 * The meta object literal for the '<em><b>Check Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OCL_CONDITION__CHECK_TYPE = eINSTANCE.getOclCondition_CheckType();

	}

} //SymrefsPackage
