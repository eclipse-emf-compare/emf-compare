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
 * $Id: BindingPackageImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingFactory;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.compare.mpatch.binding.NoteContainer;
import org.eclipse.emf.compare.mpatch.binding.NoteElement;
import org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
public class BindingPackageImpl extends EPackageImpl implements BindingPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass subModelBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addElementChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveElementChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addReferenceChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateReferenceChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass noteElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mPatchModelBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeElementChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeReferenceChangeBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass noteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass noteContainerEClass = null;

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
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private BindingPackageImpl() {
		super(eNS_URI, BindingFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link BindingPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static BindingPackage init() {
		if (isInited) return (BindingPackage)EPackage.Registry.INSTANCE.getEPackage(BindingPackage.eNS_URI);

		// Obtain or create and register package
		BindingPackageImpl theBindingPackage = (BindingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof BindingPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new BindingPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MPatchPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theBindingPackage.createPackageContents();

		// Initialize created meta-data
		theBindingPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theBindingPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(BindingPackage.eNS_URI, theBindingPackage);
		return theBindingPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeBinding() {
		return changeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeBinding_Change() {
		return (EReference)changeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeBinding_CorrespondingElements() {
		return (EReference)changeBindingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElementChangeBinding() {
		return elementChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementChangeBinding_ElementReference() {
		return (EReference)elementChangeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElementBinding() {
		return elementBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementBinding_ModelElement() {
		return (EReference)elementBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElementBinding_Ignore() {
		return (EAttribute)elementBindingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSubModelBinding() {
		return subModelBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubModelBinding_ModelDescriptor() {
		return (EReference)subModelBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubModelBinding_SubModelReferences() {
		return (EReference)subModelBindingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubModelBinding_SelfElement() {
		return (EReference)subModelBindingEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubModelBinding_SelfReference() {
		return (EReference)subModelBindingEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeChangeBinding() {
		return attributeChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddElementChangeBinding() {
		return addElementChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddElementChangeBinding_SubModelReferences() {
		return (EReference)addElementChangeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMoveElementChangeBinding() {
		return moveElementChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveElementChangeBinding_NewParent() {
		return (EReference)moveElementChangeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddReferenceChangeBinding() {
		return addReferenceChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddReferenceChangeBinding_ChangedReference() {
		return (EReference)addReferenceChangeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateReferenceChangeBinding() {
		return updateReferenceChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateReferenceChangeBinding_NewReference() {
		return (EReference)updateReferenceChangeBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNoteElement() {
		return noteElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNoteElement_Notes() {
		return (EReference)noteElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMPatchModelBinding() {
		return mPatchModelBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMPatchModelBinding_ChangeBindings() {
		return (EReference)mPatchModelBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMPatchModelBinding_Model() {
		return (EReference)mPatchModelBindingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMPatchModelBinding_MPatchModel() {
		return (EReference)mPatchModelBindingEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveElementChangeBinding() {
		return removeElementChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveReferenceChangeBinding() {
		return removeReferenceChangeBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNote() {
		return noteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNote_Note() {
		return (EAttribute)noteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNoteContainer() {
		return noteContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNoteContainer_AllNotes() {
		return (EReference)noteContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingFactory getBindingFactory() {
		return (BindingFactory)getEFactoryInstance();
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
		noteElementEClass = createEClass(NOTE_ELEMENT);
		createEReference(noteElementEClass, NOTE_ELEMENT__NOTES);

		mPatchModelBindingEClass = createEClass(MPATCH_MODEL_BINDING);
		createEReference(mPatchModelBindingEClass, MPATCH_MODEL_BINDING__CHANGE_BINDINGS);
		createEReference(mPatchModelBindingEClass, MPATCH_MODEL_BINDING__MODEL);
		createEReference(mPatchModelBindingEClass, MPATCH_MODEL_BINDING__MPATCH_MODEL);

		changeBindingEClass = createEClass(CHANGE_BINDING);
		createEReference(changeBindingEClass, CHANGE_BINDING__CHANGE);
		createEReference(changeBindingEClass, CHANGE_BINDING__CORRESPONDING_ELEMENTS);

		elementBindingEClass = createEClass(ELEMENT_BINDING);
		createEReference(elementBindingEClass, ELEMENT_BINDING__MODEL_ELEMENT);
		createEAttribute(elementBindingEClass, ELEMENT_BINDING__IGNORE);

		elementChangeBindingEClass = createEClass(ELEMENT_CHANGE_BINDING);
		createEReference(elementChangeBindingEClass, ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE);

		subModelBindingEClass = createEClass(SUB_MODEL_BINDING);
		createEReference(subModelBindingEClass, SUB_MODEL_BINDING__MODEL_DESCRIPTOR);
		createEReference(subModelBindingEClass, SUB_MODEL_BINDING__SUB_MODEL_REFERENCES);
		createEReference(subModelBindingEClass, SUB_MODEL_BINDING__SELF_ELEMENT);
		createEReference(subModelBindingEClass, SUB_MODEL_BINDING__SELF_REFERENCE);

		attributeChangeBindingEClass = createEClass(ATTRIBUTE_CHANGE_BINDING);

		addElementChangeBindingEClass = createEClass(ADD_ELEMENT_CHANGE_BINDING);
		createEReference(addElementChangeBindingEClass, ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES);

		moveElementChangeBindingEClass = createEClass(MOVE_ELEMENT_CHANGE_BINDING);
		createEReference(moveElementChangeBindingEClass, MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT);

		addReferenceChangeBindingEClass = createEClass(ADD_REFERENCE_CHANGE_BINDING);
		createEReference(addReferenceChangeBindingEClass, ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE);

		updateReferenceChangeBindingEClass = createEClass(UPDATE_REFERENCE_CHANGE_BINDING);
		createEReference(updateReferenceChangeBindingEClass, UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE);

		removeElementChangeBindingEClass = createEClass(REMOVE_ELEMENT_CHANGE_BINDING);

		removeReferenceChangeBindingEClass = createEClass(REMOVE_REFERENCE_CHANGE_BINDING);

		noteEClass = createEClass(NOTE);
		createEAttribute(noteEClass, NOTE__NOTE);

		noteContainerEClass = createEClass(NOTE_CONTAINER);
		createEReference(noteContainerEClass, NOTE_CONTAINER__ALL_NOTES);
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
		MPatchPackage theMPatchPackage = (MPatchPackage)EPackage.Registry.INSTANCE.getEPackage(MPatchPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		mPatchModelBindingEClass.getESuperTypes().add(this.getNoteElement());
		mPatchModelBindingEClass.getESuperTypes().add(this.getNoteContainer());
		changeBindingEClass.getESuperTypes().add(this.getNoteElement());
		elementBindingEClass.getESuperTypes().add(this.getNoteElement());
		elementChangeBindingEClass.getESuperTypes().add(this.getElementBinding());
		subModelBindingEClass.getESuperTypes().add(this.getElementChangeBinding());
		attributeChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		addElementChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		moveElementChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		addReferenceChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		updateReferenceChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		removeElementChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());
		removeReferenceChangeBindingEClass.getESuperTypes().add(this.getChangeBinding());

		// Initialize classes and features; add operations and parameters
		initEClass(noteElementEClass, NoteElement.class, "NoteElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNoteElement_Notes(), this.getNote(), null, "notes", null, 0, -1, NoteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(noteElementEClass, ecorePackage.getEString(), "getNote", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(mPatchModelBindingEClass, MPatchModelBinding.class, "MPatchModelBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMPatchModelBinding_ChangeBindings(), this.getChangeBinding(), null, "changeBindings", null, 0, -1, MPatchModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMPatchModelBinding_Model(), ecorePackage.getEObject(), null, "model", null, 1, 1, MPatchModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMPatchModelBinding_MPatchModel(), theMPatchPackage.getMPatchModel(), null, "mPatchModel", null, 1, 1, MPatchModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeBindingEClass, ChangeBinding.class, "ChangeBinding", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeBinding_Change(), theMPatchPackage.getIndepChange(), null, "change", null, 1, 1, ChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeBinding_CorrespondingElements(), this.getElementChangeBinding(), null, "correspondingElements", null, 1, -1, ChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementBindingEClass, ElementBinding.class, "ElementBinding", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getElementBinding_ModelElement(), ecorePackage.getEObject(), null, "modelElement", null, 0, 1, ElementBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementBinding_Ignore(), theEcorePackage.getEBoolean(), "ignore", "false", 0, 1, ElementBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementChangeBindingEClass, ElementChangeBinding.class, "ElementChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getElementChangeBinding_ElementReference(), theMPatchPackage.getIElementReference(), null, "elementReference", null, 1, 1, ElementChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(subModelBindingEClass, SubModelBinding.class, "SubModelBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSubModelBinding_ModelDescriptor(), theMPatchPackage.getIModelDescriptor(), null, "modelDescriptor", null, 1, 1, SubModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubModelBinding_SubModelReferences(), this.getElementChangeBinding(), null, "subModelReferences", null, 0, -1, SubModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubModelBinding_SelfElement(), ecorePackage.getEObject(), null, "selfElement", null, 0, 1, SubModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubModelBinding_SelfReference(), theMPatchPackage.getIElementReference(), null, "selfReference", null, 1, 1, SubModelBinding.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(attributeChangeBindingEClass, AttributeChangeBinding.class, "AttributeChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(addElementChangeBindingEClass, AddElementChangeBinding.class, "AddElementChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddElementChangeBinding_SubModelReferences(), this.getSubModelBinding(), null, "subModelReferences", null, 1, -1, AddElementChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(moveElementChangeBindingEClass, MoveElementChangeBinding.class, "MoveElementChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMoveElementChangeBinding_NewParent(), this.getElementChangeBinding(), null, "newParent", null, 1, 1, MoveElementChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(addReferenceChangeBindingEClass, AddReferenceChangeBinding.class, "AddReferenceChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddReferenceChangeBinding_ChangedReference(), this.getElementChangeBinding(), null, "changedReference", null, 1, -1, AddReferenceChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(updateReferenceChangeBindingEClass, UpdateReferenceChangeBinding.class, "UpdateReferenceChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUpdateReferenceChangeBinding_NewReference(), this.getElementChangeBinding(), null, "newReference", null, 0, 1, UpdateReferenceChangeBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(removeElementChangeBindingEClass, RemoveElementChangeBinding.class, "RemoveElementChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(removeReferenceChangeBindingEClass, RemoveReferenceChangeBinding.class, "RemoveReferenceChangeBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(noteEClass, Note.class, "Note", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNote_Note(), ecorePackage.getEString(), "note", null, 0, 1, Note.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(noteContainerEClass, NoteContainer.class, "NoteContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNoteContainer_AllNotes(), this.getNote(), null, "allNotes", null, 0, -1, NoteContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //BindingPackageImpl
