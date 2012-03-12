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
 * $Id: DescriptorPackage.java,v 1.1 2010/09/10 15:32:56 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor;

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
 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorFactory
 * @model kind="package"
 * @generated
 */
public interface DescriptorPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "descriptor";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/mpatch/1.0/descriptor";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "descriptor";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DescriptorPackage eINSTANCE = org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl <em>EMF Model Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEMFModelDescriptor()
	 * @generated
	 */
	int EMF_MODEL_DESCRIPTOR = 0;

	/**
	 * The feature id for the '<em><b>Cross References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__CROSS_REFERENCES = MPatchPackage.IMODEL_DESCRIPTOR__CROSS_REFERENCES;

	/**
	 * The feature id for the '<em><b>All Cross References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__ALL_CROSS_REFERENCES = MPatchPackage.IMODEL_DESCRIPTOR__ALL_CROSS_REFERENCES;

	/**
	 * The feature id for the '<em><b>Self Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__SELF_REFERENCE = MPatchPackage.IMODEL_DESCRIPTOR__SELF_REFERENCE;

	/**
	 * The feature id for the '<em><b>All Self References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__ALL_SELF_REFERENCES = MPatchPackage.IMODEL_DESCRIPTOR__ALL_SELF_REFERENCES;

	/**
	 * The feature id for the '<em><b>Sub Model Descriptors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS = MPatchPackage.IMODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS;

	/**
	 * The feature id for the '<em><b>Descriptor Uris</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URIS = MPatchPackage.IMODEL_DESCRIPTOR__DESCRIPTOR_URIS;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__TYPE = MPatchPackage.IMODEL_DESCRIPTOR__TYPE;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__ATTRIBUTES = MPatchPackage.IMODEL_DESCRIPTOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Descriptors</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS = MPatchPackage.IMODEL_DESCRIPTOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>References</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__REFERENCES = MPatchPackage.IMODEL_DESCRIPTOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Descriptor Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI = MPatchPackage.IMODEL_DESCRIPTOR_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>EMF Model Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_MODEL_DESCRIPTOR_FEATURE_COUNT = MPatchPackage.IMODEL_DESCRIPTOR_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EAttributeToObjectMapImpl <em>EAttribute To Object Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EAttributeToObjectMapImpl
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEAttributeToObjectMap()
	 * @generated
	 */
	int EATTRIBUTE_TO_OBJECT_MAP = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_TO_OBJECT_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_TO_OBJECT_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EAttribute To Object Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_TO_OBJECT_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToDescriptorMapImpl <em>EReference To Descriptor Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToDescriptorMapImpl
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEReferenceToDescriptorMap()
	 * @generated
	 */
	int EREFERENCE_TO_DESCRIPTOR_MAP = 2;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_DESCRIPTOR_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_DESCRIPTOR_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EReference To Descriptor Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_DESCRIPTOR_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToElementReferenceMapImpl <em>EReference To Element Reference Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToElementReferenceMapImpl
	 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEReferenceToElementReferenceMap()
	 * @generated
	 */
	int EREFERENCE_TO_ELEMENT_REFERENCE_MAP = 3;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_ELEMENT_REFERENCE_MAP__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_ELEMENT_REFERENCE_MAP__KEY = 1;

	/**
	 * The number of structural features of the '<em>EReference To Element Reference Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_TO_ELEMENT_REFERENCE_MAP_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor <em>EMF Model Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMF Model Descriptor</em>'.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor
	 * @generated
	 */
	EClass getEMFModelDescriptor();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Attributes</em>'.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getAttributes()
	 * @see #getEMFModelDescriptor()
	 * @generated
	 */
	EReference getEMFModelDescriptor_Attributes();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getSubDescriptors <em>Sub Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Sub Descriptors</em>'.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getSubDescriptors()
	 * @see #getEMFModelDescriptor()
	 * @generated
	 */
	EReference getEMFModelDescriptor_SubDescriptors();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getReferences()
	 * @see #getEMFModelDescriptor()
	 * @generated
	 */
	EReference getEMFModelDescriptor_References();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getDescriptorUri <em>Descriptor Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Descriptor Uri</em>'.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getDescriptorUri()
	 * @see #getEMFModelDescriptor()
	 * @generated
	 */
	EAttribute getEMFModelDescriptor_DescriptorUri();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EAttribute To Object Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EAttribute To Object Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.ecore.EAttribute"
	 *        valueDataType="org.eclipse.emf.ecore.EJavaObject" valueMany="true"
	 * @generated
	 */
	EClass getEAttributeToObjectMap();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEAttributeToObjectMap()
	 * @generated
	 */
	EReference getEAttributeToObjectMap_Key();

	/**
	 * Returns the meta object for the attribute list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEAttributeToObjectMap()
	 * @generated
	 */
	EAttribute getEAttributeToObjectMap_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EReference To Descriptor Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EReference To Descriptor Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.ecore.EReference"
	 *        valueType="org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor" valueContainment="true" valueMany="true"
	 * @generated
	 */
	EClass getEReferenceToDescriptorMap();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEReferenceToDescriptorMap()
	 * @generated
	 */
	EReference getEReferenceToDescriptorMap_Key();

	/**
	 * Returns the meta object for the containment reference list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEReferenceToDescriptorMap()
	 * @generated
	 */
	EReference getEReferenceToDescriptorMap_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EReference To Element Reference Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EReference To Element Reference Map</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.emf.compare.mpatch.IElementReference" valueContainment="true" valueMany="true"
	 *        keyType="org.eclipse.emf.ecore.EReference"
	 * @generated
	 */
	EClass getEReferenceToElementReferenceMap();

	/**
	 * Returns the meta object for the containment reference list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEReferenceToElementReferenceMap()
	 * @generated
	 */
	EReference getEReferenceToElementReferenceMap_Value();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEReferenceToElementReferenceMap()
	 * @generated
	 */
	EReference getEReferenceToElementReferenceMap_Key();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DescriptorFactory getDescriptorFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl <em>EMF Model Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEMFModelDescriptor()
		 * @generated
		 */
		EClass EMF_MODEL_DESCRIPTOR = eINSTANCE.getEMFModelDescriptor();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMF_MODEL_DESCRIPTOR__ATTRIBUTES = eINSTANCE.getEMFModelDescriptor_Attributes();

		/**
		 * The meta object literal for the '<em><b>Sub Descriptors</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS = eINSTANCE.getEMFModelDescriptor_SubDescriptors();

		/**
		 * The meta object literal for the '<em><b>References</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMF_MODEL_DESCRIPTOR__REFERENCES = eINSTANCE.getEMFModelDescriptor_References();

		/**
		 * The meta object literal for the '<em><b>Descriptor Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI = eINSTANCE.getEMFModelDescriptor_DescriptorUri();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EAttributeToObjectMapImpl <em>EAttribute To Object Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EAttributeToObjectMapImpl
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEAttributeToObjectMap()
		 * @generated
		 */
		EClass EATTRIBUTE_TO_OBJECT_MAP = eINSTANCE.getEAttributeToObjectMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EATTRIBUTE_TO_OBJECT_MAP__KEY = eINSTANCE.getEAttributeToObjectMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EATTRIBUTE_TO_OBJECT_MAP__VALUE = eINSTANCE.getEAttributeToObjectMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToDescriptorMapImpl <em>EReference To Descriptor Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToDescriptorMapImpl
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEReferenceToDescriptorMap()
		 * @generated
		 */
		EClass EREFERENCE_TO_DESCRIPTOR_MAP = eINSTANCE.getEReferenceToDescriptorMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_TO_DESCRIPTOR_MAP__KEY = eINSTANCE.getEReferenceToDescriptorMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_TO_DESCRIPTOR_MAP__VALUE = eINSTANCE.getEReferenceToDescriptorMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToElementReferenceMapImpl <em>EReference To Element Reference Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.EReferenceToElementReferenceMapImpl
		 * @see org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorPackageImpl#getEReferenceToElementReferenceMap()
		 * @generated
		 */
		EClass EREFERENCE_TO_ELEMENT_REFERENCE_MAP = eINSTANCE.getEReferenceToElementReferenceMap();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_TO_ELEMENT_REFERENCE_MAP__VALUE = eINSTANCE.getEReferenceToElementReferenceMap_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_TO_ELEMENT_REFERENCE_MAP__KEY = eINSTANCE.getEReferenceToElementReferenceMap_Key();

	}

} //DescriptorPackage
