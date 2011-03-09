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
 * $Id: MPatchFactoryImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.impl;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.ChangeKind;
import org.eclipse.emf.compare.mpatch.ChangeType;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class MPatchFactoryImpl extends EFactoryImpl implements MPatchFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MPatchFactory init() {
		try {
			MPatchFactory theMPatchFactory = (MPatchFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/mpatch/1.0"); 
			if (theMPatchFactory != null) {
				return theMPatchFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MPatchFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchFactoryImpl() {
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
			case MPatchPackage.MPATCH_MODEL: return createMPatchModel();
			case MPatchPackage.CHANGE_GROUP: return createChangeGroup();
			case MPatchPackage.INDEP_ADD_ELEMENT_CHANGE: return createIndepAddElementChange();
			case MPatchPackage.INDEP_REMOVE_ELEMENT_CHANGE: return createIndepRemoveElementChange();
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE: return createIndepMoveElementChange();
			case MPatchPackage.INDEP_ADD_ATTRIBUTE_CHANGE: return createIndepAddAttributeChange();
			case MPatchPackage.INDEP_REMOVE_ATTRIBUTE_CHANGE: return createIndepRemoveAttributeChange();
			case MPatchPackage.INDEP_UPDATE_ATTRIBUTE_CHANGE: return createIndepUpdateAttributeChange();
			case MPatchPackage.INDEP_ADD_REFERENCE_CHANGE: return createIndepAddReferenceChange();
			case MPatchPackage.INDEP_REMOVE_REFERENCE_CHANGE: return createIndepRemoveReferenceChange();
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE: return createIndepUpdateReferenceChange();
			case MPatchPackage.ELEMENT_REFERENCE_TO_EOBJECT_MAP: return (EObject)createElementReferenceToEObjectMap();
			case MPatchPackage.EOBJECT_TO_IMODEL_DESCRIPTOR_MAP: return (EObject)createEObjectToIModelDescriptorMap();
			case MPatchPackage.UNKNOWN_CHANGE: return createUnknownChange();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE: return createModelDescriptorReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case MPatchPackage.CHANGE_TYPE:
				return createChangeTypeFromString(eDataType, initialValue);
			case MPatchPackage.CHANGE_KIND:
				return createChangeKindFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case MPatchPackage.CHANGE_TYPE:
				return convertChangeTypeToString(eDataType, instanceValue);
			case MPatchPackage.CHANGE_KIND:
				return convertChangeKindToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchModel createMPatchModel() {
		MPatchModelImpl mPatchModel = new MPatchModelImpl();
		return mPatchModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeGroup createChangeGroup() {
		ChangeGroupImpl changeGroup = new ChangeGroupImpl();
		return changeGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepAddElementChange createIndepAddElementChange() {
		IndepAddElementChangeImpl indepAddElementChange = new IndepAddElementChangeImpl();
		return indepAddElementChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepRemoveElementChange createIndepRemoveElementChange() {
		IndepRemoveElementChangeImpl indepRemoveElementChange = new IndepRemoveElementChangeImpl();
		return indepRemoveElementChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepMoveElementChange createIndepMoveElementChange() {
		IndepMoveElementChangeImpl indepMoveElementChange = new IndepMoveElementChangeImpl();
		return indepMoveElementChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepAddAttributeChange createIndepAddAttributeChange() {
		IndepAddAttributeChangeImpl indepAddAttributeChange = new IndepAddAttributeChangeImpl();
		return indepAddAttributeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepRemoveAttributeChange createIndepRemoveAttributeChange() {
		IndepRemoveAttributeChangeImpl indepRemoveAttributeChange = new IndepRemoveAttributeChangeImpl();
		return indepRemoveAttributeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepUpdateAttributeChange createIndepUpdateAttributeChange() {
		IndepUpdateAttributeChangeImpl indepUpdateAttributeChange = new IndepUpdateAttributeChangeImpl();
		return indepUpdateAttributeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepAddReferenceChange createIndepAddReferenceChange() {
		IndepAddReferenceChangeImpl indepAddReferenceChange = new IndepAddReferenceChangeImpl();
		return indepAddReferenceChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepRemoveReferenceChange createIndepRemoveReferenceChange() {
		IndepRemoveReferenceChangeImpl indepRemoveReferenceChange = new IndepRemoveReferenceChangeImpl();
		return indepRemoveReferenceChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepUpdateReferenceChange createIndepUpdateReferenceChange() {
		IndepUpdateReferenceChangeImpl indepUpdateReferenceChange = new IndepUpdateReferenceChangeImpl();
		return indepUpdateReferenceChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<IElementReference, EList<EObject>> createElementReferenceToEObjectMap() {
		ElementReferenceToEObjectMapImpl elementReferenceToEObjectMap = new ElementReferenceToEObjectMapImpl();
		return elementReferenceToEObjectMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EObject, IModelDescriptor> createEObjectToIModelDescriptorMap() {
		EObjectToIModelDescriptorMapImpl eObjectToIModelDescriptorMap = new EObjectToIModelDescriptorMapImpl();
		return eObjectToIModelDescriptorMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnknownChange createUnknownChange() {
		UnknownChangeImpl unknownChange = new UnknownChangeImpl();
		return unknownChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelDescriptorReference createModelDescriptorReference() {
		ModelDescriptorReferenceImpl modelDescriptorReference = new ModelDescriptorReferenceImpl();
		return modelDescriptorReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeType createChangeTypeFromString(EDataType eDataType, String initialValue) {
		ChangeType result = ChangeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeKind createChangeKindFromString(EDataType eDataType, String initialValue) {
		ChangeKind result = ChangeKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchPackage getMPatchPackage() {
		return (MPatchPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MPatchPackage getPackage() {
		return MPatchPackage.eINSTANCE;
	}

} //MPatchFactoryImpl
