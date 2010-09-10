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
 * $Id: MPatchPackageImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.impl;

import java.util.Map;

import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.ChangeKind;
import org.eclipse.emf.compare.mpatch.ChangeType;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepElementChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepReferenceChange;
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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MPatchPackageImpl extends EPackageImpl implements MPatchPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mPatchModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddRemElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepRemoveElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddRemAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepMoveElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepRemoveAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepUpdateAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddRemReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepAddReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepRemoveReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indepUpdateReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iElementReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iModelDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementReferenceToEObjectMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eObjectToIModelDescriptorMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unknownChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelDescriptorReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum changeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum changeKindEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MPatchPackageImpl() {
		super(eNS_URI, MPatchFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MPatchPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MPatchPackage init() {
		if (isInited) return (MPatchPackage)EPackage.Registry.INSTANCE.getEPackage(MPatchPackage.eNS_URI);

		// Obtain or create and register package
		MPatchPackageImpl theMPatchPackage = (MPatchPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MPatchPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MPatchPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theMPatchPackage.createPackageContents();

		// Initialize created meta-data
		theMPatchPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMPatchPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MPatchPackage.eNS_URI, theMPatchPackage);
		return theMPatchPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMPatchModel() {
		return mPatchModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMPatchModel_Changes() {
		return (EReference)mPatchModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMPatchModel_OldModel() {
		return (EAttribute)mPatchModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMPatchModel_NewModel() {
		return (EAttribute)mPatchModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMPatchModel_Emfdiff() {
		return (EAttribute)mPatchModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepChange() {
		return indepChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepChange_CorrespondingElement() {
		return (EReference)indepChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndepChange_ChangeKind() {
		return (EAttribute)indepChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndepChange_ChangeType() {
		return (EAttribute)indepChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepChange_DependsOn() {
		return (EReference)indepChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepChange_Dependants() {
		return (EReference)indepChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeGroup() {
		return changeGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeGroup_SubChanges() {
		return (EReference)changeGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepElementChange() {
		return indepElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddRemElementChange() {
		return indepAddRemElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepAddRemElementChange_SubModel() {
		return (EReference)indepAddRemElementChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepAddRemElementChange_Containment() {
		return (EReference)indepAddRemElementChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepAddRemElementChange_SubModelReference() {
		return (EReference)indepAddRemElementChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddElementChange() {
		return indepAddElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepRemoveElementChange() {
		return indepRemoveElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAttributeChange() {
		return indepAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepAttributeChange_ChangedAttribute() {
		return (EReference)indepAttributeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddRemAttributeChange() {
		return indepAddRemAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndepAddRemAttributeChange_Value() {
		return (EAttribute)indepAddRemAttributeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepMoveElementChange() {
		return indepMoveElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepMoveElementChange_OldContainment() {
		return (EReference)indepMoveElementChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepMoveElementChange_NewContainment() {
		return (EReference)indepMoveElementChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepMoveElementChange_OldParent() {
		return (EReference)indepMoveElementChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepMoveElementChange_NewParent() {
		return (EReference)indepMoveElementChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddAttributeChange() {
		return indepAddAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepRemoveAttributeChange() {
		return indepRemoveAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepUpdateAttributeChange() {
		return indepUpdateAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndepUpdateAttributeChange_OldValue() {
		return (EAttribute)indepUpdateAttributeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndepUpdateAttributeChange_NewValue() {
		return (EAttribute)indepUpdateAttributeChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepReferenceChange() {
		return indepReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepReferenceChange_Reference() {
		return (EReference)indepReferenceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddRemReferenceChange() {
		return indepAddRemReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepAddRemReferenceChange_ChangedReference() {
		return (EReference)indepAddRemReferenceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepAddReferenceChange() {
		return indepAddReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepRemoveReferenceChange() {
		return indepRemoveReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndepUpdateReferenceChange() {
		return indepUpdateReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepUpdateReferenceChange_OldReference() {
		return (EReference)indepUpdateReferenceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndepUpdateReferenceChange_NewReference() {
		return (EReference)indepUpdateReferenceChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIElementReference() {
		return iElementReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIElementReference_Type() {
		return (EReference)iElementReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIElementReference_UriReference() {
		return (EAttribute)iElementReferenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIElementReference_UpperBound() {
		return (EAttribute)iElementReferenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIElementReference_LowerBound() {
		return (EAttribute)iElementReferenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIElementReference_Label() {
		return (EAttribute)iElementReferenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIModelDescriptor() {
		return iModelDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_CrossReferences() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_AllCrossReferences() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_SelfReference() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIModelDescriptor_DescriptorUris() {
		return (EAttribute)iModelDescriptorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_AllSelfReferences() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_SubModelDescriptors() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModelDescriptor_Type() {
		return (EReference)iModelDescriptorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElementReferenceToEObjectMap() {
		return elementReferenceToEObjectMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementReferenceToEObjectMap_Key() {
		return (EReference)elementReferenceToEObjectMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementReferenceToEObjectMap_Value() {
		return (EReference)elementReferenceToEObjectMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEObjectToIModelDescriptorMap() {
		return eObjectToIModelDescriptorMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEObjectToIModelDescriptorMap_Value() {
		return (EReference)eObjectToIModelDescriptorMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEObjectToIModelDescriptorMap_Key() {
		return (EReference)eObjectToIModelDescriptorMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnknownChange() {
		return unknownChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnknownChange_Info() {
		return (EAttribute)unknownChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelDescriptorReference() {
		return modelDescriptorReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelDescriptorReference_ResolvesTo() {
		return (EReference)modelDescriptorReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getChangeType() {
		return changeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getChangeKind() {
		return changeKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchFactory getMPatchFactory() {
		return (MPatchFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		mPatchModelEClass = createEClass(MPATCH_MODEL);
		createEReference(mPatchModelEClass, MPATCH_MODEL__CHANGES);
		createEAttribute(mPatchModelEClass, MPATCH_MODEL__OLD_MODEL);
		createEAttribute(mPatchModelEClass, MPATCH_MODEL__NEW_MODEL);
		createEAttribute(mPatchModelEClass, MPATCH_MODEL__EMFDIFF);

		indepChangeEClass = createEClass(INDEP_CHANGE);
		createEReference(indepChangeEClass, INDEP_CHANGE__CORRESPONDING_ELEMENT);
		createEAttribute(indepChangeEClass, INDEP_CHANGE__CHANGE_KIND);
		createEAttribute(indepChangeEClass, INDEP_CHANGE__CHANGE_TYPE);
		createEReference(indepChangeEClass, INDEP_CHANGE__DEPENDS_ON);
		createEReference(indepChangeEClass, INDEP_CHANGE__DEPENDANTS);

		changeGroupEClass = createEClass(CHANGE_GROUP);
		createEReference(changeGroupEClass, CHANGE_GROUP__SUB_CHANGES);

		indepElementChangeEClass = createEClass(INDEP_ELEMENT_CHANGE);

		indepAddRemElementChangeEClass = createEClass(INDEP_ADD_REM_ELEMENT_CHANGE);
		createEReference(indepAddRemElementChangeEClass, INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL);
		createEReference(indepAddRemElementChangeEClass, INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT);
		createEReference(indepAddRemElementChangeEClass, INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE);

		indepAddElementChangeEClass = createEClass(INDEP_ADD_ELEMENT_CHANGE);

		indepRemoveElementChangeEClass = createEClass(INDEP_REMOVE_ELEMENT_CHANGE);

		indepAttributeChangeEClass = createEClass(INDEP_ATTRIBUTE_CHANGE);
		createEReference(indepAttributeChangeEClass, INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE);

		indepAddRemAttributeChangeEClass = createEClass(INDEP_ADD_REM_ATTRIBUTE_CHANGE);
		createEAttribute(indepAddRemAttributeChangeEClass, INDEP_ADD_REM_ATTRIBUTE_CHANGE__VALUE);

		indepMoveElementChangeEClass = createEClass(INDEP_MOVE_ELEMENT_CHANGE);
		createEReference(indepMoveElementChangeEClass, INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT);
		createEReference(indepMoveElementChangeEClass, INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT);
		createEReference(indepMoveElementChangeEClass, INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT);
		createEReference(indepMoveElementChangeEClass, INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT);

		indepAddAttributeChangeEClass = createEClass(INDEP_ADD_ATTRIBUTE_CHANGE);

		indepRemoveAttributeChangeEClass = createEClass(INDEP_REMOVE_ATTRIBUTE_CHANGE);

		indepUpdateAttributeChangeEClass = createEClass(INDEP_UPDATE_ATTRIBUTE_CHANGE);
		createEAttribute(indepUpdateAttributeChangeEClass, INDEP_UPDATE_ATTRIBUTE_CHANGE__OLD_VALUE);
		createEAttribute(indepUpdateAttributeChangeEClass, INDEP_UPDATE_ATTRIBUTE_CHANGE__NEW_VALUE);

		indepReferenceChangeEClass = createEClass(INDEP_REFERENCE_CHANGE);
		createEReference(indepReferenceChangeEClass, INDEP_REFERENCE_CHANGE__REFERENCE);

		indepAddRemReferenceChangeEClass = createEClass(INDEP_ADD_REM_REFERENCE_CHANGE);
		createEReference(indepAddRemReferenceChangeEClass, INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE);

		indepAddReferenceChangeEClass = createEClass(INDEP_ADD_REFERENCE_CHANGE);

		indepRemoveReferenceChangeEClass = createEClass(INDEP_REMOVE_REFERENCE_CHANGE);

		indepUpdateReferenceChangeEClass = createEClass(INDEP_UPDATE_REFERENCE_CHANGE);
		createEReference(indepUpdateReferenceChangeEClass, INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE);
		createEReference(indepUpdateReferenceChangeEClass, INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE);

		iElementReferenceEClass = createEClass(IELEMENT_REFERENCE);
		createEReference(iElementReferenceEClass, IELEMENT_REFERENCE__TYPE);
		createEAttribute(iElementReferenceEClass, IELEMENT_REFERENCE__URI_REFERENCE);
		createEAttribute(iElementReferenceEClass, IELEMENT_REFERENCE__UPPER_BOUND);
		createEAttribute(iElementReferenceEClass, IELEMENT_REFERENCE__LOWER_BOUND);
		createEAttribute(iElementReferenceEClass, IELEMENT_REFERENCE__LABEL);

		iModelDescriptorEClass = createEClass(IMODEL_DESCRIPTOR);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__CROSS_REFERENCES);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__ALL_CROSS_REFERENCES);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__SELF_REFERENCE);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__ALL_SELF_REFERENCES);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS);
		createEAttribute(iModelDescriptorEClass, IMODEL_DESCRIPTOR__DESCRIPTOR_URIS);
		createEReference(iModelDescriptorEClass, IMODEL_DESCRIPTOR__TYPE);

		elementReferenceToEObjectMapEClass = createEClass(ELEMENT_REFERENCE_TO_EOBJECT_MAP);
		createEReference(elementReferenceToEObjectMapEClass, ELEMENT_REFERENCE_TO_EOBJECT_MAP__KEY);
		createEReference(elementReferenceToEObjectMapEClass, ELEMENT_REFERENCE_TO_EOBJECT_MAP__VALUE);

		eObjectToIModelDescriptorMapEClass = createEClass(EOBJECT_TO_IMODEL_DESCRIPTOR_MAP);
		createEReference(eObjectToIModelDescriptorMapEClass, EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__VALUE);
		createEReference(eObjectToIModelDescriptorMapEClass, EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__KEY);

		unknownChangeEClass = createEClass(UNKNOWN_CHANGE);
		createEAttribute(unknownChangeEClass, UNKNOWN_CHANGE__INFO);

		modelDescriptorReferenceEClass = createEClass(MODEL_DESCRIPTOR_REFERENCE);
		createEReference(modelDescriptorReferenceEClass, MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO);

		// Create enums
		changeTypeEEnum = createEEnum(CHANGE_TYPE);
		changeKindEEnum = createEEnum(CHANGE_KIND);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		changeGroupEClass.getESuperTypes().add(this.getIndepChange());
		indepElementChangeEClass.getESuperTypes().add(this.getIndepChange());
		indepAddRemElementChangeEClass.getESuperTypes().add(this.getIndepElementChange());
		indepAddElementChangeEClass.getESuperTypes().add(this.getIndepAddRemElementChange());
		indepRemoveElementChangeEClass.getESuperTypes().add(this.getIndepAddRemElementChange());
		indepAttributeChangeEClass.getESuperTypes().add(this.getIndepChange());
		indepAddRemAttributeChangeEClass.getESuperTypes().add(this.getIndepAttributeChange());
		indepMoveElementChangeEClass.getESuperTypes().add(this.getIndepElementChange());
		indepAddAttributeChangeEClass.getESuperTypes().add(this.getIndepAddRemAttributeChange());
		indepRemoveAttributeChangeEClass.getESuperTypes().add(this.getIndepAddRemAttributeChange());
		indepUpdateAttributeChangeEClass.getESuperTypes().add(this.getIndepAttributeChange());
		indepReferenceChangeEClass.getESuperTypes().add(this.getIndepChange());
		indepAddRemReferenceChangeEClass.getESuperTypes().add(this.getIndepReferenceChange());
		indepAddReferenceChangeEClass.getESuperTypes().add(this.getIndepAddRemReferenceChange());
		indepRemoveReferenceChangeEClass.getESuperTypes().add(this.getIndepAddRemReferenceChange());
		indepUpdateReferenceChangeEClass.getESuperTypes().add(this.getIndepReferenceChange());
		unknownChangeEClass.getESuperTypes().add(this.getIndepChange());
		modelDescriptorReferenceEClass.getESuperTypes().add(this.getIElementReference());

		// Initialize classes and features; add operations and parameters
		initEClass(mPatchModelEClass, MPatchModel.class, "MPatchModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMPatchModel_Changes(), this.getIndepChange(), null, "changes", null, 0, -1, MPatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMPatchModel_OldModel(), ecorePackage.getEString(), "oldModel", null, 0, 1, MPatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMPatchModel_NewModel(), ecorePackage.getEString(), "newModel", null, 0, 1, MPatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMPatchModel_Emfdiff(), ecorePackage.getEString(), "emfdiff", null, 0, 1, MPatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepChangeEClass, IndepChange.class, "IndepChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepChange_CorrespondingElement(), this.getIElementReference(), null, "correspondingElement", null, 0, 1, IndepChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndepChange_ChangeKind(), this.getChangeKind(), "changeKind", null, 0, 1, IndepChange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndepChange_ChangeType(), this.getChangeType(), "changeType", "", 0, 1, IndepChange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getIndepChange_DependsOn(), this.getIndepChange(), this.getIndepChange_Dependants(), "dependsOn", null, 0, -1, IndepChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepChange_Dependants(), this.getIndepChange(), this.getIndepChange_DependsOn(), "dependants", null, 0, -1, IndepChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeGroupEClass, ChangeGroup.class, "ChangeGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeGroup_SubChanges(), this.getIndepChange(), null, "subChanges", null, 0, -1, ChangeGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepElementChangeEClass, IndepElementChange.class, "IndepElementChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepAddRemElementChangeEClass, IndepAddRemElementChange.class, "IndepAddRemElementChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepAddRemElementChange_SubModel(), this.getIModelDescriptor(), null, "subModel", null, 1, 1, IndepAddRemElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepAddRemElementChange_Containment(), theEcorePackage.getEReference(), null, "containment", null, 1, 1, IndepAddRemElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepAddRemElementChange_SubModelReference(), this.getIElementReference(), null, "subModelReference", null, 1, 1, IndepAddRemElementChange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(indepAddElementChangeEClass, IndepAddElementChange.class, "IndepAddElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepRemoveElementChangeEClass, IndepRemoveElementChange.class, "IndepRemoveElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepAttributeChangeEClass, IndepAttributeChange.class, "IndepAttributeChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepAttributeChange_ChangedAttribute(), theEcorePackage.getEAttribute(), null, "changedAttribute", null, 1, 1, IndepAttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepAddRemAttributeChangeEClass, IndepAddRemAttributeChange.class, "IndepAddRemAttributeChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndepAddRemAttributeChange_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, IndepAddRemAttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepMoveElementChangeEClass, IndepMoveElementChange.class, "IndepMoveElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepMoveElementChange_OldContainment(), theEcorePackage.getEReference(), null, "oldContainment", null, 1, 1, IndepMoveElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepMoveElementChange_NewContainment(), theEcorePackage.getEReference(), null, "newContainment", null, 1, 1, IndepMoveElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepMoveElementChange_OldParent(), this.getIElementReference(), null, "oldParent", null, 1, 1, IndepMoveElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepMoveElementChange_NewParent(), this.getIElementReference(), null, "newParent", null, 1, 1, IndepMoveElementChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepAddAttributeChangeEClass, IndepAddAttributeChange.class, "IndepAddAttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepRemoveAttributeChangeEClass, IndepRemoveAttributeChange.class, "IndepRemoveAttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepUpdateAttributeChangeEClass, IndepUpdateAttributeChange.class, "IndepUpdateAttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndepUpdateAttributeChange_OldValue(), theEcorePackage.getEJavaObject(), "oldValue", null, 0, 1, IndepUpdateAttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndepUpdateAttributeChange_NewValue(), theEcorePackage.getEJavaObject(), "newValue", null, 0, 1, IndepUpdateAttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepReferenceChangeEClass, IndepReferenceChange.class, "IndepReferenceChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepReferenceChange_Reference(), theEcorePackage.getEReference(), null, "reference", null, 1, 1, IndepReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepAddRemReferenceChangeEClass, IndepAddRemReferenceChange.class, "IndepAddRemReferenceChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepAddRemReferenceChange_ChangedReference(), this.getIElementReference(), null, "changedReference", null, 1, 1, IndepAddRemReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indepAddReferenceChangeEClass, IndepAddReferenceChange.class, "IndepAddReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepRemoveReferenceChangeEClass, IndepRemoveReferenceChange.class, "IndepRemoveReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indepUpdateReferenceChangeEClass, IndepUpdateReferenceChange.class, "IndepUpdateReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndepUpdateReferenceChange_OldReference(), this.getIElementReference(), null, "oldReference", null, 0, 1, IndepUpdateReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndepUpdateReferenceChange_NewReference(), this.getIElementReference(), null, "newReference", null, 0, 1, IndepUpdateReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iElementReferenceEClass, IElementReference.class, "IElementReference", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIElementReference_Type(), theEcorePackage.getEClass(), null, "type", null, 1, 1, IElementReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIElementReference_UriReference(), ecorePackage.getEString(), "uriReference", null, 0, 1, IElementReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIElementReference_UpperBound(), ecorePackage.getEInt(), "upperBound", "1", 0, 1, IElementReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIElementReference_LowerBound(), theEcorePackage.getEInt(), "lowerBound", "1", 0, 1, IElementReference.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIElementReference_Label(), theEcorePackage.getEString(), "label", null, 0, 1, IElementReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(iElementReferenceEClass, theEcorePackage.getEObject(), "resolve", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEObject(), "model", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iElementReferenceEClass, ecorePackage.getEBoolean(), "resolvesEqual", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getIElementReference(), "other", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iModelDescriptorEClass, IModelDescriptor.class, "IModelDescriptor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIModelDescriptor_CrossReferences(), this.getIElementReference(), null, "crossReferences", null, 0, -1, IModelDescriptor.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getIModelDescriptor_AllCrossReferences(), this.getIElementReference(), null, "allCrossReferences", null, 0, -1, IModelDescriptor.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getIModelDescriptor_SelfReference(), this.getIElementReference(), null, "selfReference", null, 1, 1, IModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIModelDescriptor_AllSelfReferences(), this.getIElementReference(), null, "allSelfReferences", null, 1, -1, IModelDescriptor.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getIModelDescriptor_SubModelDescriptors(), this.getIModelDescriptor(), null, "subModelDescriptors", null, 0, -1, IModelDescriptor.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getIModelDescriptor_DescriptorUris(), ecorePackage.getEString(), "descriptorUris", null, 0, -1, IModelDescriptor.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getIModelDescriptor_Type(), theEcorePackage.getEClass(), null, "type", null, 1, 1, IModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(iModelDescriptorEClass, this.getIElementReference(), "applyCrossReferences", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "createdObject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getElementReferenceToEObjectMap(), "resolvedCrossReferences", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iModelDescriptorEClass, this.getEObjectToIModelDescriptorMap(), "applyStructure", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "modelElement", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEReference(), "containment", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iModelDescriptorEClass, ecorePackage.getEBoolean(), "describesEqual", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getIModelDescriptor(), "other", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(elementReferenceToEObjectMapEClass, Map.Entry.class, "ElementReferenceToEObjectMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getElementReferenceToEObjectMap_Key(), this.getIElementReference(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementReferenceToEObjectMap_Value(), theEcorePackage.getEObject(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eObjectToIModelDescriptorMapEClass, Map.Entry.class, "EObjectToIModelDescriptorMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEObjectToIModelDescriptorMap_Value(), this.getIModelDescriptor(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEObjectToIModelDescriptorMap_Key(), theEcorePackage.getEObject(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unknownChangeEClass, UnknownChange.class, "UnknownChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnknownChange_Info(), theEcorePackage.getEString(), "info", null, 0, 1, UnknownChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelDescriptorReferenceEClass, ModelDescriptorReference.class, "ModelDescriptorReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getModelDescriptorReference_ResolvesTo(), this.getIModelDescriptor(), null, "resolvesTo", null, 1, 1, ModelDescriptorReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(changeTypeEEnum, ChangeType.class, "ChangeType");
		addEEnumLiteral(changeTypeEEnum, ChangeType.ELEMENT);
		addEEnumLiteral(changeTypeEEnum, ChangeType.ATTRIBUTE);
		addEEnumLiteral(changeTypeEEnum, ChangeType.REFERENCE);
		addEEnumLiteral(changeTypeEEnum, ChangeType.GROUP);
		addEEnumLiteral(changeTypeEEnum, ChangeType.UNKNOWN);

		initEEnum(changeKindEEnum, ChangeKind.class, "ChangeKind");
		addEEnumLiteral(changeKindEEnum, ChangeKind.ADDITION);
		addEEnumLiteral(changeKindEEnum, ChangeKind.DELETION);
		addEEnumLiteral(changeKindEEnum, ChangeKind.CHANGE);
		addEEnumLiteral(changeKindEEnum, ChangeKind.MOVE);
		addEEnumLiteral(changeKindEEnum, ChangeKind.GROUP);
		addEEnumLiteral(changeKindEEnum, ChangeKind.UNKNOWN);

		// Create resource
		createResource(eNS_URI);
	}

} //MPatchPackageImpl
