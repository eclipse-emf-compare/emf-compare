/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: DescriptorFactoryImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor.impl;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorFactory;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage;
import org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DescriptorFactoryImpl extends EFactoryImpl implements DescriptorFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DescriptorFactory init() {
		try {
			DescriptorFactory theDescriptorFactory = (DescriptorFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/mpatch/1.0/descriptor"); 
			if (theDescriptorFactory != null) {
				return theDescriptorFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DescriptorFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescriptorFactoryImpl() {
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
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR: return createEMFModelDescriptor();
			case DescriptorPackage.EATTRIBUTE_TO_OBJECT_MAP: return (EObject)createEAttributeToObjectMap();
			case DescriptorPackage.EREFERENCE_TO_DESCRIPTOR_MAP: return (EObject)createEReferenceToDescriptorMap();
			case DescriptorPackage.EREFERENCE_TO_ELEMENT_REFERENCE_MAP: return (EObject)createEReferenceToElementReferenceMap();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMFModelDescriptor createEMFModelDescriptor() {
		EMFModelDescriptorImpl emfModelDescriptor = new EMFModelDescriptorImpl();
		return emfModelDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EAttribute, EList<Object>> createEAttributeToObjectMap() {
		EAttributeToObjectMapImpl eAttributeToObjectMap = new EAttributeToObjectMapImpl();
		return eAttributeToObjectMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EReference, EList<EMFModelDescriptor>> createEReferenceToDescriptorMap() {
		EReferenceToDescriptorMapImpl eReferenceToDescriptorMap = new EReferenceToDescriptorMapImpl();
		return eReferenceToDescriptorMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EReference, EList<IElementReference>> createEReferenceToElementReferenceMap() {
		EReferenceToElementReferenceMapImpl eReferenceToElementReferenceMap = new EReferenceToElementReferenceMapImpl();
		return eReferenceToElementReferenceMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescriptorPackage getDescriptorPackage() {
		return (DescriptorPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DescriptorPackage getPackage() {
		return DescriptorPackage.eINSTANCE;
	}

} //DescriptorFactoryImpl
