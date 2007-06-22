/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffPackage.java,v 1.1 2007/06/22 12:59:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

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
 * @see org.eclipse.emf.compare.diff.metamodel.DiffFactory
 * @model kind="package"
 * @generated
 */
public interface DiffPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diff";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "diff";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DiffPackage eINSTANCE = org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
	 * @generated
	 */
	int DIFF_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__RIGHT = 0;

	/**
	 * The feature id for the '<em><b>Owned Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__OWNED_ELEMENTS = 1;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__LEFT = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffElement()
	 * @generated
	 */
	int DIFF_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__SUB_DIFF_ELEMENTS = 0;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
	 * @generated
	 */
	int DIFF_GROUP = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__LEFT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Subchanges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__SUBCHANGES = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChange()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__ATTRIBUTE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChange()
	 * @generated
	 */
	int REFERENCE_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFERENCE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The number of structural features of the '<em>Model Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl <em>Add Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddModelElement()
	 * @generated
	 */
	int ADD_MODEL_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__LEFT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Add Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl <em>Remove Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveModelElement()
	 * @generated
	 */
	int REMOVE_MODEL_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__RIGHT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Remove Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateModelElement()
	 * @generated
	 */
	int UPDATE_MODEL_ELEMENT = 8;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Update Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getMoveModelElement()
	 * @generated
	 */
	int MOVE_MODEL_ELEMENT = 9;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_ELEMENT = UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_ELEMENT = UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_PARENT = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_PARENT = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Move Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT_FEATURE_COUNT = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl <em>Add Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddAttribute()
	 * @generated
	 */
	int ADD_ATTRIBUTE = 10;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Add Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl <em>Remove Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveAttribute()
	 * @generated
	 */
	int REMOVE_ATTRIBUTE = 11;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Remove Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
	 * @generated
	 */
	int UPDATE_ATTRIBUTE = 12;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Update Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl <em>Add Reference Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddReferenceValue()
	 * @generated
	 */
	int ADD_REFERENCE_VALUE = 13;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Added Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Added Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Add Reference Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl <em>Remove Reference Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveReferenceValue()
	 * @generated
	 */
	int REMOVE_REFERENCE_VALUE = 14;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Removed Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Removed Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Remove Reference Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
	 * @generated
	 */
	int UPDATE_REFERENCE = 15;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Update Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl <em>Model Input Snapshot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelInputSnapshot()
	 * @generated
	 */
	int MODEL_INPUT_SNAPSHOT = 16;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__DATE = 0;

	/**
	 * The feature id for the '<em><b>Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__DIFF = 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__MATCH = 2;

	/**
	 * The number of structural features of the '<em>Model Input Snapshot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT_FEATURE_COUNT = 3;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * @generated
	 */
	EClass getDiffModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight()
	 * @see #getDiffModel()
	 * @generated
	 */
	EAttribute getDiffModel_Right();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements <em>Owned Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_OwnedElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft()
	 * @see #getDiffModel()
	 * @generated
	 */
	EAttribute getDiffModel_Left();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	EClass getDiffElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements <em>Sub Diff Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Diff Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements()
	 * @see #getDiffElement()
	 * @generated
	 */
	EReference getDiffElement_SubDiffElements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * @generated
	 */
	EClass getDiffGroup();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EReference getDiffGroup_LeftParent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Subchanges</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EAttribute getDiffGroup_Subchanges();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	EClass getAttributeChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_Attribute();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	EClass getReferenceChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_Reference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_RightElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_LeftElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	EClass getModelElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddModelElement <em>Add Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddModelElement
	 * @generated
	 */
	EClass getAddModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AddModelElement#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddModelElement#getLeftParent()
	 * @see #getAddModelElement()
	 * @generated
	 */
	EReference getAddModelElement_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AddModelElement#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddModelElement#getRightElement()
	 * @see #getAddModelElement()
	 * @generated
	 */
	EReference getAddModelElement_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveModelElement <em>Remove Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveModelElement
	 * @generated
	 */
	EClass getRemoveModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.RemoveModelElement#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveModelElement#getRightParent()
	 * @see #getRemoveModelElement()
	 * @generated
	 */
	EReference getRemoveModelElement_RightParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.RemoveModelElement#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveModelElement#getLeftElement()
	 * @see #getRemoveModelElement()
	 * @generated
	 */
	EReference getRemoveModelElement_LeftElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	EClass getUpdateModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_RightElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_LeftElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement <em>Move Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Move Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement
	 * @generated
	 */
	EClass getMoveModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftParent()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightParent()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_RightParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddAttribute <em>Add Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddAttribute
	 * @generated
	 */
	EClass getAddAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveAttribute <em>Remove Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveAttribute
	 * @generated
	 */
	EClass getRemoveAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	EClass getUpdateAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue <em>Add Reference Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddReferenceValue
	 * @generated
	 */
	EClass getAddReferenceValue();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getLeftAddedTarget <em>Left Added Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Left Added Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getLeftAddedTarget()
	 * @see #getAddReferenceValue()
	 * @generated
	 */
	EReference getAddReferenceValue_LeftAddedTarget();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getRightAddedTarget <em>Right Added Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Right Added Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getRightAddedTarget()
	 * @see #getAddReferenceValue()
	 * @generated
	 */
	EReference getAddReferenceValue_RightAddedTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue <em>Remove Reference Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue
	 * @generated
	 */
	EClass getRemoveReferenceValue();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getLeftRemovedTarget <em>Left Removed Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Left Removed Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getLeftRemovedTarget()
	 * @see #getRemoveReferenceValue()
	 * @generated
	 */
	EReference getRemoveReferenceValue_LeftRemovedTarget();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getRightRemovedTarget <em>Right Removed Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Right Removed Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getRightRemovedTarget()
	 * @see #getRemoveReferenceValue()
	 * @generated
	 */
	EReference getRemoveReferenceValue_RightRemovedTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	EClass getUpdateReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot <em>Model Input Snapshot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Input Snapshot</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot
	 * @generated
	 */
	EClass getModelInputSnapshot();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EAttribute getModelInputSnapshot_Date();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff <em>Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Diff</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EReference getModelInputSnapshot_Diff();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch <em>Match</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Match</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EReference getModelInputSnapshot_Match();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DiffFactory getDiffFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
		 * @generated
		 */
		EClass DIFF_MODEL = eINSTANCE.getDiffModel();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_MODEL__RIGHT = eINSTANCE.getDiffModel_Right();

		/**
		 * The meta object literal for the '<em><b>Owned Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_MODEL__OWNED_ELEMENTS = eINSTANCE.getDiffModel_OwnedElements();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_MODEL__LEFT = eINSTANCE.getDiffModel_Left();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffElement()
		 * @generated
		 */
		EClass DIFF_ELEMENT = eINSTANCE.getDiffElement();

		/**
		 * The meta object literal for the '<em><b>Sub Diff Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_ELEMENT__SUB_DIFF_ELEMENTS = eINSTANCE.getDiffElement_SubDiffElements();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
		 * @generated
		 */
		EClass DIFF_GROUP = eINSTANCE.getDiffGroup();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_GROUP__LEFT_PARENT = eINSTANCE.getDiffGroup_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Subchanges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_GROUP__SUBCHANGES = eINSTANCE.getDiffGroup_Subchanges();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChange()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE = eINSTANCE.getAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__ATTRIBUTE = eINSTANCE.getAttributeChange_Attribute();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__LEFT_ELEMENT = eINSTANCE.getAttributeChange_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__RIGHT_ELEMENT = eINSTANCE.getAttributeChange_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChange()
		 * @generated
		 */
		EClass REFERENCE_CHANGE = eINSTANCE.getReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE__REFERENCE = eINSTANCE.getReferenceChange_Reference();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE__RIGHT_ELEMENT = eINSTANCE.getReferenceChange_RightElement();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE__LEFT_ELEMENT = eINSTANCE.getReferenceChange_LeftElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
		 * @generated
		 */
		EClass MODEL_ELEMENT_CHANGE = eINSTANCE.getModelElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl <em>Add Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddModelElement()
		 * @generated
		 */
		EClass ADD_MODEL_ELEMENT = eINSTANCE.getAddModelElement();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_MODEL_ELEMENT__LEFT_PARENT = eINSTANCE.getAddModelElement_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_MODEL_ELEMENT__RIGHT_ELEMENT = eINSTANCE.getAddModelElement_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl <em>Remove Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveModelElement()
		 * @generated
		 */
		EClass REMOVE_MODEL_ELEMENT = eINSTANCE.getRemoveModelElement();

		/**
		 * The meta object literal for the '<em><b>Right Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REMOVE_MODEL_ELEMENT__RIGHT_PARENT = eINSTANCE.getRemoveModelElement_RightParent();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REMOVE_MODEL_ELEMENT__LEFT_ELEMENT = eINSTANCE.getRemoveModelElement_LeftElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateModelElement()
		 * @generated
		 */
		EClass UPDATE_MODEL_ELEMENT = eINSTANCE.getUpdateModelElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT = eINSTANCE.getUpdateModelElement_RightElement();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UPDATE_MODEL_ELEMENT__LEFT_ELEMENT = eINSTANCE.getUpdateModelElement_LeftElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getMoveModelElement()
		 * @generated
		 */
		EClass MOVE_MODEL_ELEMENT = eINSTANCE.getMoveModelElement();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MOVE_MODEL_ELEMENT__LEFT_PARENT = eINSTANCE.getMoveModelElement_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Right Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MOVE_MODEL_ELEMENT__RIGHT_PARENT = eINSTANCE.getMoveModelElement_RightParent();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl <em>Add Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddAttribute()
		 * @generated
		 */
		EClass ADD_ATTRIBUTE = eINSTANCE.getAddAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl <em>Remove Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveAttribute()
		 * @generated
		 */
		EClass REMOVE_ATTRIBUTE = eINSTANCE.getRemoveAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
		 * @generated
		 */
		EClass UPDATE_ATTRIBUTE = eINSTANCE.getUpdateAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl <em>Add Reference Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddReferenceValue()
		 * @generated
		 */
		EClass ADD_REFERENCE_VALUE = eINSTANCE.getAddReferenceValue();

		/**
		 * The meta object literal for the '<em><b>Left Added Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET = eINSTANCE.getAddReferenceValue_LeftAddedTarget();

		/**
		 * The meta object literal for the '<em><b>Right Added Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET = eINSTANCE
				.getAddReferenceValue_RightAddedTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl <em>Remove Reference Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveReferenceValue()
		 * @generated
		 */
		EClass REMOVE_REFERENCE_VALUE = eINSTANCE.getRemoveReferenceValue();

		/**
		 * The meta object literal for the '<em><b>Left Removed Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET = eINSTANCE
				.getRemoveReferenceValue_LeftRemovedTarget();

		/**
		 * The meta object literal for the '<em><b>Right Removed Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET = eINSTANCE
				.getRemoveReferenceValue_RightRemovedTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
		 * @generated
		 */
		EClass UPDATE_REFERENCE = eINSTANCE.getUpdateReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl <em>Model Input Snapshot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelInputSnapshot()
		 * @generated
		 */
		EClass MODEL_INPUT_SNAPSHOT = eINSTANCE.getModelInputSnapshot();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_INPUT_SNAPSHOT__DATE = eINSTANCE.getModelInputSnapshot_Date();

		/**
		 * The meta object literal for the '<em><b>Diff</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_INPUT_SNAPSHOT__DIFF = eINSTANCE.getModelInputSnapshot_Diff();

		/**
		 * The meta object literal for the '<em><b>Match</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_INPUT_SNAPSHOT__MATCH = eINSTANCE.getModelInputSnapshot_Match();

	}

} //DiffPackage
