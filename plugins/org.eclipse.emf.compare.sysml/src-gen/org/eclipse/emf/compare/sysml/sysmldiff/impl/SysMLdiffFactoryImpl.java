/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff.impl;

import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class SysMLdiffFactoryImpl extends EFactoryImpl implements SysMLdiffFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static SysMLdiffFactory init() {
		try {
			SysMLdiffFactory theSysMLdiffFactory = (SysMLdiffFactory)EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/emf/compare/diff/SysML/1.0"); //$NON-NLS-1$
			if (theSysMLdiffFactory != null) {
				return theSysMLdiffFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SysMLdiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET:
				return createSysMLStereotypePropertyChangeLeftTarget();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET:
				return createSysMLStereotypePropertyChangeRightTarget();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET:
				return createSysMLStereotypeReferenceChangeLeftTarget();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET:
				return createSysMLStereotypeReferenceChangeRightTarget();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE:
				return createSysMLStereotypeReferenceOrderChange();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE:
				return createSysMLStereotypeUpdateAttribute();
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_UPDATE_REFERENCE:
				return createSysMLStereotypeUpdateReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() //$NON-NLS-1$
						+ "' is not a valid classifier"); //$NON-NLS-1$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypePropertyChangeLeftTarget createSysMLStereotypePropertyChangeLeftTarget() {
		SysMLStereotypePropertyChangeLeftTargetImpl sysMLStereotypePropertyChangeLeftTarget = new SysMLStereotypePropertyChangeLeftTargetImpl();
		return sysMLStereotypePropertyChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypePropertyChangeRightTarget createSysMLStereotypePropertyChangeRightTarget() {
		SysMLStereotypePropertyChangeRightTargetImpl sysMLStereotypePropertyChangeRightTarget = new SysMLStereotypePropertyChangeRightTargetImpl();
		return sysMLStereotypePropertyChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypeReferenceChangeLeftTarget createSysMLStereotypeReferenceChangeLeftTarget() {
		SysMLStereotypeReferenceChangeLeftTargetImpl sysMLStereotypeReferenceChangeLeftTarget = new SysMLStereotypeReferenceChangeLeftTargetImpl();
		return sysMLStereotypeReferenceChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypeReferenceChangeRightTarget createSysMLStereotypeReferenceChangeRightTarget() {
		SysMLStereotypeReferenceChangeRightTargetImpl sysMLStereotypeReferenceChangeRightTarget = new SysMLStereotypeReferenceChangeRightTargetImpl();
		return sysMLStereotypeReferenceChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypeReferenceOrderChange createSysMLStereotypeReferenceOrderChange() {
		SysMLStereotypeReferenceOrderChangeImpl sysMLStereotypeReferenceOrderChange = new SysMLStereotypeReferenceOrderChangeImpl();
		return sysMLStereotypeReferenceOrderChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypeUpdateAttribute createSysMLStereotypeUpdateAttribute() {
		SysMLStereotypeUpdateAttributeImpl sysMLStereotypeUpdateAttribute = new SysMLStereotypeUpdateAttributeImpl();
		return sysMLStereotypeUpdateAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLStereotypeUpdateReference createSysMLStereotypeUpdateReference() {
		SysMLStereotypeUpdateReferenceImpl sysMLStereotypeUpdateReference = new SysMLStereotypeUpdateReferenceImpl();
		return sysMLStereotypeUpdateReference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffPackage getSysMLdiffPackage() {
		return (SysMLdiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SysMLdiffPackage getPackage() {
		return SysMLdiffPackage.eINSTANCE;
	}

} // SysMLdiffFactoryImpl
