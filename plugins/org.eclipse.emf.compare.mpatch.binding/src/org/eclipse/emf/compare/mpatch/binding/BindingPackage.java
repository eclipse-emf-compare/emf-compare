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
 * $Id: BindingPackage.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

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
 * @see org.eclipse.emf.compare.mpatch.binding.BindingFactory
 * @model kind="package"
 * @generated
 */
public interface BindingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "binding";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/mpatch/1.0/binding";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "binding";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BindingPackage eINSTANCE = org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.NoteElement <em>Note Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteElement
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNoteElement()
	 * @generated
	 */
	int NOTE_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_ELEMENT__NOTES = 0;

	/**
	 * The number of structural features of the '<em>Note Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl <em>MPatch Model Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getMPatchModelBinding()
	 * @generated
	 */
	int MPATCH_MODEL_BINDING = 1;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING__NOTES = NOTE_ELEMENT__NOTES;

	/**
	 * The feature id for the '<em><b>All Notes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING__ALL_NOTES = NOTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Change Bindings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING__CHANGE_BINDINGS = NOTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING__MODEL = NOTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>MPatch Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING__MPATCH_MODEL = NOTE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>MPatch Model Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_BINDING_FEATURE_COUNT = NOTE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl <em>Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getChangeBinding()
	 * @generated
	 */
	int CHANGE_BINDING = 2;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_BINDING__NOTES = NOTE_ELEMENT__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_BINDING__CHANGE = NOTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_BINDING__CORRESPONDING_ELEMENTS = NOTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_BINDING_FEATURE_COUNT = NOTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding <em>Element Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getElementBinding()
	 * @generated
	 */
	int ELEMENT_BINDING = 3;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_BINDING__NOTES = NOTE_ELEMENT__NOTES;

	/**
	 * The feature id for the '<em><b>Model Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_BINDING__MODEL_ELEMENT = NOTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ignore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_BINDING__IGNORE = NOTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Element Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_BINDING_FEATURE_COUNT = NOTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl <em>Element Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getElementChangeBinding()
	 * @generated
	 */
	int ELEMENT_CHANGE_BINDING = 4;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_CHANGE_BINDING__NOTES = ELEMENT_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Model Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_CHANGE_BINDING__MODEL_ELEMENT = ELEMENT_BINDING__MODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Ignore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_CHANGE_BINDING__IGNORE = ELEMENT_BINDING__IGNORE;

	/**
	 * The feature id for the '<em><b>Element Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE = ELEMENT_BINDING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Element Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_CHANGE_BINDING_FEATURE_COUNT = ELEMENT_BINDING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl <em>Sub Model Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getSubModelBinding()
	 * @generated
	 */
	int SUB_MODEL_BINDING = 5;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__NOTES = ELEMENT_CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Model Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__MODEL_ELEMENT = ELEMENT_CHANGE_BINDING__MODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Ignore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__IGNORE = ELEMENT_CHANGE_BINDING__IGNORE;

	/**
	 * The feature id for the '<em><b>Element Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__ELEMENT_REFERENCE = ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE;

	/**
	 * The feature id for the '<em><b>Model Descriptor</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__MODEL_DESCRIPTOR = ELEMENT_CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Model References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__SUB_MODEL_REFERENCES = ELEMENT_CHANGE_BINDING_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Self Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__SELF_ELEMENT = ELEMENT_CHANGE_BINDING_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Self Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING__SELF_REFERENCE = ELEMENT_CHANGE_BINDING_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Sub Model Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_MODEL_BINDING_FEATURE_COUNT = ELEMENT_CHANGE_BINDING_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AttributeChangeBindingImpl <em>Attribute Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.AttributeChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAttributeChangeBinding()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE_BINDING = 6;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The number of structural features of the '<em>Attribute Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AddElementChangeBindingImpl <em>Add Element Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.AddElementChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAddElementChangeBinding()
	 * @generated
	 */
	int ADD_ELEMENT_CHANGE_BINDING = 7;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ELEMENT_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ELEMENT_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ELEMENT_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Sub Model References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Add Element Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ELEMENT_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.MoveElementChangeBindingImpl <em>Move Element Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.MoveElementChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getMoveElementChangeBinding()
	 * @generated
	 */
	int MOVE_ELEMENT_CHANGE_BINDING = 8;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_ELEMENT_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_ELEMENT_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_ELEMENT_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The feature id for the '<em><b>New Parent</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Move Element Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_ELEMENT_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AddReferenceChangeBindingImpl <em>Add Reference Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.AddReferenceChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAddReferenceChangeBinding()
	 * @generated
	 */
	int ADD_REFERENCE_CHANGE_BINDING = 9;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Changed Reference</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Add Reference Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.UpdateReferenceChangeBindingImpl <em>Update Reference Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.UpdateReferenceChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getUpdateReferenceChangeBinding()
	 * @generated
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING = 10;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The feature id for the '<em><b>New Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Update Reference Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.RemoveElementChangeBindingImpl <em>Remove Element Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.RemoveElementChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getRemoveElementChangeBinding()
	 * @generated
	 */
	int REMOVE_ELEMENT_CHANGE_BINDING = 11;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ELEMENT_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ELEMENT_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ELEMENT_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The number of structural features of the '<em>Remove Element Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ELEMENT_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.RemoveReferenceChangeBindingImpl <em>Remove Reference Change Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.RemoveReferenceChangeBindingImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getRemoveReferenceChangeBinding()
	 * @generated
	 */
	int REMOVE_REFERENCE_CHANGE_BINDING = 12;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_CHANGE_BINDING__NOTES = CHANGE_BINDING__NOTES;

	/**
	 * The feature id for the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_CHANGE_BINDING__CHANGE = CHANGE_BINDING__CHANGE;

	/**
	 * The feature id for the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_CHANGE_BINDING__CORRESPONDING_ELEMENTS = CHANGE_BINDING__CORRESPONDING_ELEMENTS;

	/**
	 * The number of structural features of the '<em>Remove Reference Change Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_CHANGE_BINDING_FEATURE_COUNT = CHANGE_BINDING_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.NoteImpl <em>Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.NoteImpl
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNote()
	 * @generated
	 */
	int NOTE = 13;

	/**
	 * The feature id for the '<em><b>Note</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__NOTE = 0;

	/**
	 * The number of structural features of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_FEATURE_COUNT = 1;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer <em>Note Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteContainer
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNoteContainer()
	 * @generated
	 */
	int NOTE_CONTAINER = 14;

	/**
	 * The feature id for the '<em><b>All Notes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_CONTAINER__ALL_NOTES = 0;

	/**
	 * The number of structural features of the '<em>Note Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_CONTAINER_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding <em>Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ChangeBinding
	 * @generated
	 */
	EClass getChangeBinding();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getChange <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getChange()
	 * @see #getChangeBinding()
	 * @generated
	 */
	EReference getChangeBinding_Change();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getCorrespondingElements <em>Corresponding Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Corresponding Elements</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getCorrespondingElements()
	 * @see #getChangeBinding()
	 * @generated
	 */
	EReference getChangeBinding_CorrespondingElements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding <em>Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding
	 * @generated
	 */
	EClass getElementChangeBinding();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding#getElementReference <em>Element Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding#getElementReference()
	 * @see #getElementChangeBinding()
	 * @generated
	 */
	EReference getElementChangeBinding_ElementReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding <em>Element Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding
	 * @generated
	 */
	EClass getElementBinding();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding#getModelElement <em>Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model Element</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding#getModelElement()
	 * @see #getElementBinding()
	 * @generated
	 */
	EReference getElementBinding_ModelElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding#isIgnore <em>Ignore</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ignore</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding#isIgnore()
	 * @see #getElementBinding()
	 * @generated
	 */
	EAttribute getElementBinding_Ignore();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding <em>Sub Model Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sub Model Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding
	 * @generated
	 */
	EClass getSubModelBinding();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getModelDescriptor <em>Model Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model Descriptor</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getModelDescriptor()
	 * @see #getSubModelBinding()
	 * @generated
	 */
	EReference getSubModelBinding_ModelDescriptor();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSubModelReferences <em>Sub Model References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Model References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSubModelReferences()
	 * @see #getSubModelBinding()
	 * @generated
	 */
	EReference getSubModelBinding_SubModelReferences();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfElement <em>Self Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Self Element</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfElement()
	 * @see #getSubModelBinding()
	 * @generated
	 */
	EReference getSubModelBinding_SelfElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfReference <em>Self Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Self Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfReference()
	 * @see #getSubModelBinding()
	 * @generated
	 */
	EReference getSubModelBinding_SelfReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding <em>Attribute Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding
	 * @generated
	 */
	EClass getAttributeChangeBinding();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding <em>Add Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Element Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding
	 * @generated
	 */
	EClass getAddElementChangeBinding();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding#getSubModelReferences <em>Sub Model References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Model References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding#getSubModelReferences()
	 * @see #getAddElementChangeBinding()
	 * @generated
	 */
	EReference getAddElementChangeBinding_SubModelReferences();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding <em>Move Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Move Element Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding
	 * @generated
	 */
	EClass getMoveElementChangeBinding();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding#getNewParent <em>New Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>New Parent</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding#getNewParent()
	 * @see #getMoveElementChangeBinding()
	 * @generated
	 */
	EReference getMoveElementChangeBinding_NewParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding <em>Add Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Reference Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding
	 * @generated
	 */
	EClass getAddReferenceChangeBinding();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding#getChangedReference <em>Changed Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changed Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding#getChangedReference()
	 * @see #getAddReferenceChangeBinding()
	 * @generated
	 */
	EReference getAddReferenceChangeBinding_ChangedReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding <em>Update Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Reference Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding
	 * @generated
	 */
	EClass getUpdateReferenceChangeBinding();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding#getNewReference <em>New Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>New Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding#getNewReference()
	 * @see #getUpdateReferenceChangeBinding()
	 * @generated
	 */
	EReference getUpdateReferenceChangeBinding_NewReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.NoteElement <em>Note Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note Element</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteElement
	 * @generated
	 */
	EClass getNoteElement();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.binding.NoteElement#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Notes</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteElement#getNotes()
	 * @see #getNoteElement()
	 * @generated
	 */
	EReference getNoteElement_Notes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding <em>MPatch Model Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MPatch Model Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding
	 * @generated
	 */
	EClass getMPatchModelBinding();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getChangeBindings <em>Change Bindings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Bindings</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getChangeBindings()
	 * @see #getMPatchModelBinding()
	 * @generated
	 */
	EReference getMPatchModelBinding_ChangeBindings();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getModel()
	 * @see #getMPatchModelBinding()
	 * @generated
	 */
	EReference getMPatchModelBinding_Model();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getMPatchModel <em>MPatch Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>MPatch Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getMPatchModel()
	 * @see #getMPatchModelBinding()
	 * @generated
	 */
	EReference getMPatchModelBinding_MPatchModel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding <em>Remove Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove Element Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding
	 * @generated
	 */
	EClass getRemoveElementChangeBinding();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding <em>Remove Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove Reference Change Binding</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding
	 * @generated
	 */
	EClass getRemoveReferenceChangeBinding();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.Note
	 * @generated
	 */
	EClass getNote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.binding.Note#getNote <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Note</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.Note#getNote()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Note();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer <em>Note Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note Container</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteContainer
	 * @generated
	 */
	EClass getNoteContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer#getAllNotes <em>All Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>All Notes</em>'.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteContainer#getAllNotes()
	 * @see #getNoteContainer()
	 * @generated
	 */
	EReference getNoteContainer_AllNotes();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BindingFactory getBindingFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl <em>Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getChangeBinding()
		 * @generated
		 */
		EClass CHANGE_BINDING = eINSTANCE.getChangeBinding();

		/**
		 * The meta object literal for the '<em><b>Change</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_BINDING__CHANGE = eINSTANCE.getChangeBinding_Change();

		/**
		 * The meta object literal for the '<em><b>Corresponding Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_BINDING__CORRESPONDING_ELEMENTS = eINSTANCE.getChangeBinding_CorrespondingElements();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl <em>Element Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getElementChangeBinding()
		 * @generated
		 */
		EClass ELEMENT_CHANGE_BINDING = eINSTANCE.getElementChangeBinding();

		/**
		 * The meta object literal for the '<em><b>Element Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE = eINSTANCE.getElementChangeBinding_ElementReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding <em>Element Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getElementBinding()
		 * @generated
		 */
		EClass ELEMENT_BINDING = eINSTANCE.getElementBinding();

		/**
		 * The meta object literal for the '<em><b>Model Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_BINDING__MODEL_ELEMENT = eINSTANCE.getElementBinding_ModelElement();

		/**
		 * The meta object literal for the '<em><b>Ignore</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT_BINDING__IGNORE = eINSTANCE.getElementBinding_Ignore();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl <em>Sub Model Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getSubModelBinding()
		 * @generated
		 */
		EClass SUB_MODEL_BINDING = eINSTANCE.getSubModelBinding();

		/**
		 * The meta object literal for the '<em><b>Model Descriptor</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_MODEL_BINDING__MODEL_DESCRIPTOR = eINSTANCE.getSubModelBinding_ModelDescriptor();

		/**
		 * The meta object literal for the '<em><b>Sub Model References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_MODEL_BINDING__SUB_MODEL_REFERENCES = eINSTANCE.getSubModelBinding_SubModelReferences();

		/**
		 * The meta object literal for the '<em><b>Self Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_MODEL_BINDING__SELF_ELEMENT = eINSTANCE.getSubModelBinding_SelfElement();

		/**
		 * The meta object literal for the '<em><b>Self Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_MODEL_BINDING__SELF_REFERENCE = eINSTANCE.getSubModelBinding_SelfReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AttributeChangeBindingImpl <em>Attribute Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.AttributeChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAttributeChangeBinding()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE_BINDING = eINSTANCE.getAttributeChangeBinding();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AddElementChangeBindingImpl <em>Add Element Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.AddElementChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAddElementChangeBinding()
		 * @generated
		 */
		EClass ADD_ELEMENT_CHANGE_BINDING = eINSTANCE.getAddElementChangeBinding();

		/**
		 * The meta object literal for the '<em><b>Sub Model References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES = eINSTANCE.getAddElementChangeBinding_SubModelReferences();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.MoveElementChangeBindingImpl <em>Move Element Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.MoveElementChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getMoveElementChangeBinding()
		 * @generated
		 */
		EClass MOVE_ELEMENT_CHANGE_BINDING = eINSTANCE.getMoveElementChangeBinding();

		/**
		 * The meta object literal for the '<em><b>New Parent</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT = eINSTANCE.getMoveElementChangeBinding_NewParent();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.AddReferenceChangeBindingImpl <em>Add Reference Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.AddReferenceChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getAddReferenceChangeBinding()
		 * @generated
		 */
		EClass ADD_REFERENCE_CHANGE_BINDING = eINSTANCE.getAddReferenceChangeBinding();

		/**
		 * The meta object literal for the '<em><b>Changed Reference</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE = eINSTANCE.getAddReferenceChangeBinding_ChangedReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.UpdateReferenceChangeBindingImpl <em>Update Reference Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.UpdateReferenceChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getUpdateReferenceChangeBinding()
		 * @generated
		 */
		EClass UPDATE_REFERENCE_CHANGE_BINDING = eINSTANCE.getUpdateReferenceChangeBinding();

		/**
		 * The meta object literal for the '<em><b>New Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE = eINSTANCE.getUpdateReferenceChangeBinding_NewReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.NoteElement <em>Note Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.NoteElement
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNoteElement()
		 * @generated
		 */
		EClass NOTE_ELEMENT = eINSTANCE.getNoteElement();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOTE_ELEMENT__NOTES = eINSTANCE.getNoteElement_Notes();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl <em>MPatch Model Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getMPatchModelBinding()
		 * @generated
		 */
		EClass MPATCH_MODEL_BINDING = eINSTANCE.getMPatchModelBinding();

		/**
		 * The meta object literal for the '<em><b>Change Bindings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MPATCH_MODEL_BINDING__CHANGE_BINDINGS = eINSTANCE.getMPatchModelBinding_ChangeBindings();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MPATCH_MODEL_BINDING__MODEL = eINSTANCE.getMPatchModelBinding_Model();

		/**
		 * The meta object literal for the '<em><b>MPatch Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MPATCH_MODEL_BINDING__MPATCH_MODEL = eINSTANCE.getMPatchModelBinding_MPatchModel();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.RemoveElementChangeBindingImpl <em>Remove Element Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.RemoveElementChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getRemoveElementChangeBinding()
		 * @generated
		 */
		EClass REMOVE_ELEMENT_CHANGE_BINDING = eINSTANCE.getRemoveElementChangeBinding();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.RemoveReferenceChangeBindingImpl <em>Remove Reference Change Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.RemoveReferenceChangeBindingImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getRemoveReferenceChangeBinding()
		 * @generated
		 */
		EClass REMOVE_REFERENCE_CHANGE_BINDING = eINSTANCE.getRemoveReferenceChangeBinding();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.impl.NoteImpl <em>Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.NoteImpl
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNote()
		 * @generated
		 */
		EClass NOTE = eINSTANCE.getNote();

		/**
		 * The meta object literal for the '<em><b>Note</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__NOTE = eINSTANCE.getNote_Note();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer <em>Note Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.binding.NoteContainer
		 * @see org.eclipse.emf.compare.mpatch.binding.impl.BindingPackageImpl#getNoteContainer()
		 * @generated
		 */
		EClass NOTE_CONTAINER = eINSTANCE.getNoteContainer();

		/**
		 * The meta object literal for the '<em><b>All Notes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOTE_CONTAINER__ALL_NOTES = eINSTANCE.getNoteContainer_AllNotes();

	}

} //BindingPackage
