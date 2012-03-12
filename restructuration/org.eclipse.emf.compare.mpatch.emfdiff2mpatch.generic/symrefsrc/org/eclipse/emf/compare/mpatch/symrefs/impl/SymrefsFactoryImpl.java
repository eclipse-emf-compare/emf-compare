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
 * $Id: SymrefsFactoryImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
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
public class SymrefsFactoryImpl extends EFactoryImpl implements SymrefsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SymrefsFactory init() {
		try {
			SymrefsFactory theSymrefsFactory = (SymrefsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/mpatch/1.0/symrefs"); 
			if (theSymrefsFactory != null) {
				return theSymrefsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SymrefsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymrefsFactoryImpl() {
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
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE: return createExternalElementReference();
			case SymrefsPackage.ID_EMF_REFERENCE: return createIdEmfReference();
			case SymrefsPackage.ELEMENT_SET_REFERENCE: return createElementSetReference();
			case SymrefsPackage.OCL_CONDITION: return createOclCondition();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExternalElementReference createExternalElementReference() {
		ExternalElementReferenceImpl externalElementReference = new ExternalElementReferenceImpl();
		return externalElementReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IdEmfReference createIdEmfReference() {
		IdEmfReferenceImpl idEmfReference = new IdEmfReferenceImpl();
		return idEmfReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementSetReference createElementSetReference() {
		ElementSetReferenceImpl elementSetReference = new ElementSetReferenceImpl();
		return elementSetReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OclCondition createOclCondition() {
		OclConditionImpl oclCondition = new OclConditionImpl();
		return oclCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymrefsPackage getSymrefsPackage() {
		return (SymrefsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SymrefsPackage getPackage() {
		return SymrefsPackage.eINSTANCE;
	}

} //SymrefsFactoryImpl
