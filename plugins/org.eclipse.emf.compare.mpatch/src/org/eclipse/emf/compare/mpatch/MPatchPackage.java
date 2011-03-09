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
 * $Id: MPatchPackage.java,v 1.2 2010/10/20 09:22:23 pkonemann Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.emf.compare.mpatch.MPatchFactory
 * @model kind="package"
 * @generated
 */
public interface MPatchPackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "mpatch";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/mpatch/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "mpatch";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MPatchPackage eINSTANCE = org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getMPatchModel()
	 * @generated
	 */
	int MPATCH_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL__CHANGES = 0;

	/**
	 * The feature id for the '<em><b>Old Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL__OLD_MODEL = 1;

	/**
	 * The feature id for the '<em><b>New Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL__NEW_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Emfdiff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL__EMFDIFF = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MPATCH_MODEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl <em>Indep Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepChange()
	 * @generated
	 */
	int INDEP_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__CORRESPONDING_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__CHANGE_KIND = 1;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__CHANGE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__DEPENDS_ON = 3;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__DEPENDANTS = 4;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE__RESULTING_ELEMENT = 5;

	/**
	 * The number of structural features of the '<em>Indep Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_CHANGE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.ChangeGroupImpl <em>Change Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.ChangeGroupImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeGroup()
	 * @generated
	 */
	int CHANGE_GROUP = 2;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__CORRESPONDING_ELEMENT = INDEP_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__CHANGE_KIND = INDEP_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__CHANGE_TYPE = INDEP_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__DEPENDS_ON = INDEP_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__DEPENDANTS = INDEP_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__RESULTING_ELEMENT = INDEP_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Sub Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__SUB_CHANGES = INDEP_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Change Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP_FEATURE_COUNT = INDEP_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepElementChangeImpl <em>Indep Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepElementChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepElementChange()
	 * @generated
	 */
	int INDEP_ELEMENT_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__CORRESPONDING_ELEMENT = INDEP_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__CHANGE_KIND = INDEP_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__CHANGE_TYPE = INDEP_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__DEPENDS_ON = INDEP_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__DEPENDANTS = INDEP_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE__RESULTING_ELEMENT = INDEP_CHANGE__RESULTING_ELEMENT;

	/**
	 * The number of structural features of the '<em>Indep Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ELEMENT_CHANGE_FEATURE_COUNT = INDEP_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl <em>Indep Add Rem Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemElementChange()
	 * @generated
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__CORRESPONDING_ELEMENT = INDEP_ELEMENT_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_KIND = INDEP_ELEMENT_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_TYPE = INDEP_ELEMENT_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDS_ON = INDEP_ELEMENT_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDANTS = INDEP_ELEMENT_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__RESULTING_ELEMENT = INDEP_ELEMENT_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Sub Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Sub Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Indep Add Rem Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ELEMENT_CHANGE_FEATURE_COUNT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddElementChangeImpl <em>Indep Add Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddElementChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddElementChange()
	 * @generated
	 */
	int INDEP_ADD_ELEMENT_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_ELEMENT_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__CHANGE_KIND = INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__DEPENDS_ON = INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__DEPENDANTS = INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_ELEMENT_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Sub Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__SUB_MODEL = INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL;

	/**
	 * The feature id for the '<em><b>Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__CONTAINMENT = INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT;

	/**
	 * The feature id for the '<em><b>Sub Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE__SUB_MODEL_REFERENCE = INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE;

	/**
	 * The number of structural features of the '<em>Indep Add Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ELEMENT_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveElementChangeImpl <em>Indep Remove Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveElementChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveElementChange()
	 * @generated
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_ELEMENT_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__CHANGE_KIND = INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_ELEMENT_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__DEPENDS_ON = INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__DEPENDANTS = INDEP_ADD_REM_ELEMENT_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_ELEMENT_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Sub Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__SUB_MODEL = INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL;

	/**
	 * The feature id for the '<em><b>Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__CONTAINMENT = INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT;

	/**
	 * The feature id for the '<em><b>Sub Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE__SUB_MODEL_REFERENCE = INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE;

	/**
	 * The number of structural features of the '<em>Indep Remove Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ELEMENT_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAttributeChangeImpl <em>Indep Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAttributeChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAttributeChange()
	 * @generated
	 */
	int INDEP_ATTRIBUTE_CHANGE = 7;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT = INDEP_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__CHANGE_KIND = INDEP_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__CHANGE_TYPE = INDEP_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__DEPENDS_ON = INDEP_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__DEPENDANTS = INDEP_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__RESULTING_ELEMENT = INDEP_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Changed Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = INDEP_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Indep Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT = INDEP_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemAttributeChangeImpl <em>Indep Add Rem Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemAttributeChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemAttributeChange()
	 * @generated
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE = 8;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_KIND = INDEP_ATTRIBUTE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_TYPE = INDEP_ATTRIBUTE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDS_ON = INDEP_ATTRIBUTE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDANTS = INDEP_ATTRIBUTE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__RESULTING_ELEMENT = INDEP_ATTRIBUTE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Changed Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE__VALUE = INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Indep Add Rem Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_ATTRIBUTE_CHANGE_FEATURE_COUNT = INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl <em>Indep Move Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepMoveElementChange()
	 * @generated
	 */
	int INDEP_MOVE_ELEMENT_CHANGE = 9;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__CORRESPONDING_ELEMENT = INDEP_ELEMENT_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__CHANGE_KIND = INDEP_ELEMENT_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__CHANGE_TYPE = INDEP_ELEMENT_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__DEPENDS_ON = INDEP_ELEMENT_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__DEPENDANTS = INDEP_ELEMENT_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__RESULTING_ELEMENT = INDEP_ELEMENT_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Old Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Old Parent</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>New Parent</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Indep Move Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_MOVE_ELEMENT_CHANGE_FEATURE_COUNT = INDEP_ELEMENT_CHANGE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddAttributeChangeImpl <em>Indep Add Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddAttributeChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddAttributeChange()
	 * @generated
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE = 10;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__CHANGE_KIND = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__DEPENDS_ON = INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__DEPENDANTS = INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_ATTRIBUTE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Changed Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE__VALUE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__VALUE;

	/**
	 * The number of structural features of the '<em>Indep Add Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_ATTRIBUTE_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveAttributeChangeImpl <em>Indep Remove Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveAttributeChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveAttributeChange()
	 * @generated
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE = 11;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__CHANGE_KIND = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__DEPENDS_ON = INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__DEPENDANTS = INDEP_ADD_REM_ATTRIBUTE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_ATTRIBUTE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Changed Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE__VALUE = INDEP_ADD_REM_ATTRIBUTE_CHANGE__VALUE;

	/**
	 * The number of structural features of the '<em>Indep Remove Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_ATTRIBUTE_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateAttributeChangeImpl <em>Indep Update Attribute Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepUpdateAttributeChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepUpdateAttributeChange()
	 * @generated
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE = 12;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ATTRIBUTE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__CHANGE_KIND = INDEP_ATTRIBUTE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__CHANGE_TYPE = INDEP_ATTRIBUTE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__DEPENDS_ON = INDEP_ATTRIBUTE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__DEPENDANTS = INDEP_ATTRIBUTE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__RESULTING_ELEMENT = INDEP_ATTRIBUTE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Changed Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Old Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__OLD_VALUE = INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE__NEW_VALUE = INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Indep Update Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_ATTRIBUTE_CHANGE_FEATURE_COUNT = INDEP_ATTRIBUTE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepReferenceChangeImpl <em>Indep Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepReferenceChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepReferenceChange()
	 * @generated
	 */
	int INDEP_REFERENCE_CHANGE = 13;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__CORRESPONDING_ELEMENT = INDEP_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__CHANGE_KIND = INDEP_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__CHANGE_TYPE = INDEP_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__DEPENDS_ON = INDEP_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__DEPENDANTS = INDEP_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__RESULTING_ELEMENT = INDEP_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE__REFERENCE = INDEP_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Indep Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REFERENCE_CHANGE_FEATURE_COUNT = INDEP_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemReferenceChangeImpl <em>Indep Add Rem Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemReferenceChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemReferenceChange()
	 * @generated
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE = 14;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__CORRESPONDING_ELEMENT = INDEP_REFERENCE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_KIND = INDEP_REFERENCE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_TYPE = INDEP_REFERENCE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDS_ON = INDEP_REFERENCE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDANTS = INDEP_REFERENCE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__RESULTING_ELEMENT = INDEP_REFERENCE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__REFERENCE = INDEP_REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Changed Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE = INDEP_REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Indep Add Rem Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REM_REFERENCE_CHANGE_FEATURE_COUNT = INDEP_REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddReferenceChangeImpl <em>Indep Add Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddReferenceChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddReferenceChange()
	 * @generated
	 */
	int INDEP_ADD_REFERENCE_CHANGE = 15;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_REFERENCE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__CHANGE_KIND = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__DEPENDS_ON = INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__DEPENDANTS = INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_REFERENCE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__REFERENCE = INDEP_ADD_REM_REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Changed Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE__CHANGED_REFERENCE = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE;

	/**
	 * The number of structural features of the '<em>Indep Add Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_ADD_REFERENCE_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveReferenceChangeImpl <em>Indep Remove Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveReferenceChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveReferenceChange()
	 * @generated
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE = 16;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__CORRESPONDING_ELEMENT = INDEP_ADD_REM_REFERENCE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__CHANGE_KIND = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__CHANGE_TYPE = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__DEPENDS_ON = INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__DEPENDANTS = INDEP_ADD_REM_REFERENCE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__RESULTING_ELEMENT = INDEP_ADD_REM_REFERENCE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__REFERENCE = INDEP_ADD_REM_REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Changed Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE__CHANGED_REFERENCE = INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE;

	/**
	 * The number of structural features of the '<em>Indep Remove Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_REMOVE_REFERENCE_CHANGE_FEATURE_COUNT = INDEP_ADD_REM_REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl <em>Indep Update Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepUpdateReferenceChange()
	 * @generated
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE = 17;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__CORRESPONDING_ELEMENT = INDEP_REFERENCE_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__CHANGE_KIND = INDEP_REFERENCE_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__CHANGE_TYPE = INDEP_REFERENCE_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__DEPENDS_ON = INDEP_REFERENCE_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__DEPENDANTS = INDEP_REFERENCE_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__RESULTING_ELEMENT = INDEP_REFERENCE_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__REFERENCE = INDEP_REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Old Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE = INDEP_REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE = INDEP_REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Indep Update Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEP_UPDATE_REFERENCE_CHANGE_FEATURE_COUNT = INDEP_REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.IElementReference <em>IElement Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.IElementReference
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIElementReference()
	 * @generated
	 */
	int IELEMENT_REFERENCE = 18;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Uri Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE__URI_REFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE__UPPER_BOUND = 2;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE__LOWER_BOUND = 3;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE__LABEL = 4;

	/**
	 * The number of structural features of the '<em>IElement Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IELEMENT_REFERENCE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor <em>IModel Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIModelDescriptor()
	 * @generated
	 */
	int IMODEL_DESCRIPTOR = 19;

	/**
	 * The feature id for the '<em><b>Cross References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__CROSS_REFERENCES = 0;

	/**
	 * The feature id for the '<em><b>All Cross References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__ALL_CROSS_REFERENCES = 1;

	/**
	 * The feature id for the '<em><b>Self Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__SELF_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>All Self References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__ALL_SELF_REFERENCES = 3;

	/**
	 * The feature id for the '<em><b>Sub Model Descriptors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS = 4;

	/**
	 * The feature id for the '<em><b>Descriptor Uris</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__DESCRIPTOR_URIS = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR__TYPE = 6;

	/**
	 * The number of structural features of the '<em>IModel Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_DESCRIPTOR_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.ElementReferenceToEObjectMapImpl <em>Element Reference To EObject Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.ElementReferenceToEObjectMapImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getElementReferenceToEObjectMap()
	 * @generated
	 */
	int ELEMENT_REFERENCE_TO_EOBJECT_MAP = 20;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_REFERENCE_TO_EOBJECT_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_REFERENCE_TO_EOBJECT_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Element Reference To EObject Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_REFERENCE_TO_EOBJECT_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.EObjectToIModelDescriptorMapImpl <em>EObject To IModel Descriptor Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.EObjectToIModelDescriptorMapImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getEObjectToIModelDescriptorMap()
	 * @generated
	 */
	int EOBJECT_TO_IMODEL_DESCRIPTOR_MAP = 21;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__KEY = 1;

	/**
	 * The number of structural features of the '<em>EObject To IModel Descriptor Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_IMODEL_DESCRIPTOR_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.UnknownChangeImpl <em>Unknown Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.UnknownChangeImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getUnknownChange()
	 * @generated
	 */
	int UNKNOWN_CHANGE = 22;

	/**
	 * The feature id for the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__CORRESPONDING_ELEMENT = INDEP_CHANGE__CORRESPONDING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Change Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__CHANGE_KIND = INDEP_CHANGE__CHANGE_KIND;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__CHANGE_TYPE = INDEP_CHANGE__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Depends On</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__DEPENDS_ON = INDEP_CHANGE__DEPENDS_ON;

	/**
	 * The feature id for the '<em><b>Dependants</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__DEPENDANTS = INDEP_CHANGE__DEPENDANTS;

	/**
	 * The feature id for the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__RESULTING_ELEMENT = INDEP_CHANGE__RESULTING_ELEMENT;

	/**
	 * The feature id for the '<em><b>Info</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE__INFO = INDEP_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Unknown Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNKNOWN_CHANGE_FEATURE_COUNT = INDEP_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl <em>Model Descriptor Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getModelDescriptorReference()
	 * @generated
	 */
	int MODEL_DESCRIPTOR_REFERENCE = 23;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__TYPE = IELEMENT_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Uri Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE = IELEMENT_REFERENCE__URI_REFERENCE;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND = IELEMENT_REFERENCE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__LOWER_BOUND = IELEMENT_REFERENCE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__LABEL = IELEMENT_REFERENCE__LABEL;

	/**
	 * The feature id for the '<em><b>Resolves To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO = IELEMENT_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Model Descriptor Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_DESCRIPTOR_REFERENCE_FEATURE_COUNT = IELEMENT_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.ChangeType <em>Change Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.ChangeType
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeType()
	 * @generated
	 */
	int CHANGE_TYPE = 24;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.mpatch.ChangeKind <em>Change Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.mpatch.ChangeKind
	 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeKind()
	 * @generated
	 */
	int CHANGE_KIND = 25;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.MPatchModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel
	 * @generated
	 */
	EClass getMPatchModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel#getChanges()
	 * @see #getMPatchModel()
	 * @generated
	 */
	EReference getMPatchModel_Changes();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getOldModel <em>Old Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel#getOldModel()
	 * @see #getMPatchModel()
	 * @generated
	 */
	EAttribute getMPatchModel_OldModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getNewModel <em>New Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel#getNewModel()
	 * @see #getMPatchModel()
	 * @generated
	 */
	EAttribute getMPatchModel_NewModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getEmfdiff <em>Emfdiff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Emfdiff</em>'.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel#getEmfdiff()
	 * @see #getMPatchModel()
	 * @generated
	 */
	EAttribute getMPatchModel_Emfdiff();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepChange <em>Indep Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange
	 * @generated
	 */
	EClass getIndepChange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepChange#getCorrespondingElement <em>Corresponding Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Corresponding Element</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getCorrespondingElement()
	 * @see #getIndepChange()
	 * @generated
	 */
	EReference getIndepChange_CorrespondingElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IndepChange#getChangeKind <em>Change Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Change Kind</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getChangeKind()
	 * @see #getIndepChange()
	 * @generated
	 */
	EAttribute getIndepChange_ChangeKind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IndepChange#getChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Change Type</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getChangeType()
	 * @see #getIndepChange()
	 * @generated
	 */
	EAttribute getIndepChange_ChangeType();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependsOn <em>Depends On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Depends On</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getDependsOn()
	 * @see #getIndepChange()
	 * @generated
	 */
	EReference getIndepChange_DependsOn();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependants <em>Dependants</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Dependants</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getDependants()
	 * @see #getIndepChange()
	 * @generated
	 */
	EReference getIndepChange_Dependants();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepChange#getResultingElement <em>Resulting Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Resulting Element</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getResultingElement()
	 * @see #getIndepChange()
	 * @generated
	 */
	EReference getIndepChange_ResultingElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.ChangeGroup <em>Change Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Group</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ChangeGroup
	 * @generated
	 */
	EClass getChangeGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.mpatch.ChangeGroup#getSubChanges <em>Sub Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Changes</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ChangeGroup#getSubChanges()
	 * @see #getChangeGroup()
	 * @generated
	 */
	EReference getChangeGroup_SubChanges();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepElementChange <em>Indep Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Element Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepElementChange
	 * @generated
	 */
	EClass getIndepElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange <em>Indep Add Rem Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Rem Element Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemElementChange
	 * @generated
	 */
	EClass getIndepAddRemElementChange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModel <em>Sub Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sub Model</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModel()
	 * @see #getIndepAddRemElementChange()
	 * @generated
	 */
	EReference getIndepAddRemElementChange_SubModel();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getContainment <em>Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Containment</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getContainment()
	 * @see #getIndepAddRemElementChange()
	 * @generated
	 */
	EReference getIndepAddRemElementChange_Containment();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModelReference <em>Sub Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sub Model Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModelReference()
	 * @see #getIndepAddRemElementChange()
	 * @generated
	 */
	EReference getIndepAddRemElementChange_SubModelReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddElementChange <em>Indep Add Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Element Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddElementChange
	 * @generated
	 */
	EClass getIndepAddElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveElementChange <em>Indep Remove Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Remove Element Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveElementChange
	 * @generated
	 */
	EClass getIndepRemoveElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAttributeChange <em>Indep Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAttributeChange
	 * @generated
	 */
	EClass getIndepAttributeChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IndepAttributeChange#getChangedAttribute <em>Changed Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Changed Attribute</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAttributeChange#getChangedAttribute()
	 * @see #getIndepAttributeChange()
	 * @generated
	 */
	EReference getIndepAttributeChange_ChangedAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange <em>Indep Add Rem Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Rem Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange
	 * @generated
	 */
	EClass getIndepAddRemAttributeChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange#getValue()
	 * @see #getIndepAddRemAttributeChange()
	 * @generated
	 */
	EAttribute getIndepAddRemAttributeChange_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange <em>Indep Move Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Move Element Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange
	 * @generated
	 */
	EClass getIndepMoveElementChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldContainment <em>Old Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Old Containment</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldContainment()
	 * @see #getIndepMoveElementChange()
	 * @generated
	 */
	EReference getIndepMoveElementChange_OldContainment();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewContainment <em>New Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Containment</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewContainment()
	 * @see #getIndepMoveElementChange()
	 * @generated
	 */
	EReference getIndepMoveElementChange_NewContainment();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldParent <em>Old Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Old Parent</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldParent()
	 * @see #getIndepMoveElementChange()
	 * @generated
	 */
	EReference getIndepMoveElementChange_OldParent();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewParent <em>New Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>New Parent</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewParent()
	 * @see #getIndepMoveElementChange()
	 * @generated
	 */
	EReference getIndepMoveElementChange_NewParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddAttributeChange <em>Indep Add Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddAttributeChange
	 * @generated
	 */
	EClass getIndepAddAttributeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange <em>Indep Remove Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Remove Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange
	 * @generated
	 */
	EClass getIndepRemoveAttributeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange <em>Indep Update Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Update Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange
	 * @generated
	 */
	EClass getIndepUpdateAttributeChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange#getOldValue <em>Old Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Value</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange#getOldValue()
	 * @see #getIndepUpdateAttributeChange()
	 * @generated
	 */
	EAttribute getIndepUpdateAttributeChange_OldValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange#getNewValue <em>New Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Value</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange#getNewValue()
	 * @see #getIndepUpdateAttributeChange()
	 * @generated
	 */
	EAttribute getIndepUpdateAttributeChange_NewValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepReferenceChange <em>Indep Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Reference Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepReferenceChange
	 * @generated
	 */
	EClass getIndepReferenceChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IndepReferenceChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepReferenceChange#getReference()
	 * @see #getIndepReferenceChange()
	 * @generated
	 */
	EReference getIndepReferenceChange_Reference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange <em>Indep Add Rem Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Rem Reference Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange
	 * @generated
	 */
	EClass getIndepAddRemReferenceChange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange#getChangedReference <em>Changed Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Changed Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange#getChangedReference()
	 * @see #getIndepAddRemReferenceChange()
	 * @generated
	 */
	EReference getIndepAddRemReferenceChange_ChangedReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepAddReferenceChange <em>Indep Add Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Add Reference Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddReferenceChange
	 * @generated
	 */
	EClass getIndepAddReferenceChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange <em>Indep Remove Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Remove Reference Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange
	 * @generated
	 */
	EClass getIndepRemoveReferenceChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange <em>Indep Update Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indep Update Reference Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange
	 * @generated
	 */
	EClass getIndepUpdateReferenceChange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getOldReference <em>Old Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Old Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getOldReference()
	 * @see #getIndepUpdateReferenceChange()
	 * @generated
	 */
	EReference getIndepUpdateReferenceChange_OldReference();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getNewReference <em>New Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>New Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getNewReference()
	 * @see #getIndepUpdateReferenceChange()
	 * @generated
	 */
	EReference getIndepUpdateReferenceChange_NewReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IElementReference <em>IElement Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IElement Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference
	 * @generated
	 */
	EClass getIElementReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IElementReference#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference#getType()
	 * @see #getIElementReference()
	 * @generated
	 */
	EReference getIElementReference_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IElementReference#getUriReference <em>Uri Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference#getUriReference()
	 * @see #getIElementReference()
	 * @generated
	 */
	EAttribute getIElementReference_UriReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IElementReference#getUpperBound <em>Upper Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper Bound</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference#getUpperBound()
	 * @see #getIElementReference()
	 * @generated
	 */
	EAttribute getIElementReference_UpperBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IElementReference#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference#getLowerBound()
	 * @see #getIElementReference()
	 * @generated
	 */
	EAttribute getIElementReference_LowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.IElementReference#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference#getLabel()
	 * @see #getIElementReference()
	 * @generated
	 */
	EAttribute getIElementReference_Label();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor <em>IModel Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IModel Descriptor</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor
	 * @generated
	 */
	EClass getIModelDescriptor();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getCrossReferences <em>Cross References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Cross References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getCrossReferences()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_CrossReferences();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllCrossReferences <em>All Cross References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>All Cross References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllCrossReferences()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_AllCrossReferences();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getSelfReference <em>Self Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Self Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getSelfReference()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_SelfReference();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getDescriptorUris <em>Descriptor Uris</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Descriptor Uris</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getDescriptorUris()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EAttribute getIModelDescriptor_DescriptorUris();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllSelfReferences <em>All Self References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>All Self References</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllSelfReferences()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_AllSelfReferences();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getSubModelDescriptors <em>Sub Model Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Sub Model Descriptors</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getSubModelDescriptors()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_SubModelDescriptors();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor#getType()
	 * @see #getIModelDescriptor()
	 * @generated
	 */
	EReference getIModelDescriptor_Type();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Element Reference To EObject Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Reference To EObject Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.compare.mpatch.IElementReference"
	 *        valueType="org.eclipse.emf.ecore.EObject" valueContainment="true" valueMany="true"
	 * @generated
	 */
	EClass getElementReferenceToEObjectMap();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getElementReferenceToEObjectMap()
	 * @generated
	 */
	EReference getElementReferenceToEObjectMap_Key();

	/**
	 * Returns the meta object for the containment reference list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getElementReferenceToEObjectMap()
	 * @generated
	 */
	EReference getElementReferenceToEObjectMap_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EObject To IModel Descriptor Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EObject To IModel Descriptor Map</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.emf.compare.mpatch.IModelDescriptor"
	 *        keyType="org.eclipse.emf.ecore.EObject" keyContainment="true"
	 * @generated
	 */
	EClass getEObjectToIModelDescriptorMap();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToIModelDescriptorMap()
	 * @generated
	 */
	EReference getEObjectToIModelDescriptorMap_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToIModelDescriptorMap()
	 * @generated
	 */
	EReference getEObjectToIModelDescriptorMap_Key();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.UnknownChange <em>Unknown Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unknown Change</em>'.
	 * @see org.eclipse.emf.compare.mpatch.UnknownChange
	 * @generated
	 */
	EClass getUnknownChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.mpatch.UnknownChange#getInfo <em>Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Info</em>'.
	 * @see org.eclipse.emf.compare.mpatch.UnknownChange#getInfo()
	 * @see #getUnknownChange()
	 * @generated
	 */
	EAttribute getUnknownChange_Info();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.mpatch.ModelDescriptorReference <em>Model Descriptor Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Descriptor Reference</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ModelDescriptorReference
	 * @generated
	 */
	EClass getModelDescriptorReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.mpatch.ModelDescriptorReference#getResolvesTo <em>Resolves To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resolves To</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ModelDescriptorReference#getResolvesTo()
	 * @see #getModelDescriptorReference()
	 * @generated
	 */
	EReference getModelDescriptorReference_ResolvesTo();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.mpatch.ChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Type</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ChangeType
	 * @generated
	 */
	EEnum getChangeType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.mpatch.ChangeKind <em>Change Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Kind</em>'.
	 * @see org.eclipse.emf.compare.mpatch.ChangeKind
	 * @generated
	 */
	EEnum getChangeKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MPatchFactory getMPatchFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getMPatchModel()
		 * @generated
		 */
		EClass MPATCH_MODEL = eINSTANCE.getMPatchModel();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MPATCH_MODEL__CHANGES = eINSTANCE.getMPatchModel_Changes();

		/**
		 * The meta object literal for the '<em><b>Old Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MPATCH_MODEL__OLD_MODEL = eINSTANCE.getMPatchModel_OldModel();

		/**
		 * The meta object literal for the '<em><b>New Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MPATCH_MODEL__NEW_MODEL = eINSTANCE.getMPatchModel_NewModel();

		/**
		 * The meta object literal for the '<em><b>Emfdiff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MPATCH_MODEL__EMFDIFF = eINSTANCE.getMPatchModel_Emfdiff();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl <em>Indep Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepChange()
		 * @generated
		 */
		EClass INDEP_CHANGE = eINSTANCE.getIndepChange();

		/**
		 * The meta object literal for the '<em><b>Corresponding Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_CHANGE__CORRESPONDING_ELEMENT = eINSTANCE.getIndepChange_CorrespondingElement();

		/**
		 * The meta object literal for the '<em><b>Change Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEP_CHANGE__CHANGE_KIND = eINSTANCE.getIndepChange_ChangeKind();

		/**
		 * The meta object literal for the '<em><b>Change Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEP_CHANGE__CHANGE_TYPE = eINSTANCE.getIndepChange_ChangeType();

		/**
		 * The meta object literal for the '<em><b>Depends On</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_CHANGE__DEPENDS_ON = eINSTANCE.getIndepChange_DependsOn();

		/**
		 * The meta object literal for the '<em><b>Dependants</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_CHANGE__DEPENDANTS = eINSTANCE.getIndepChange_Dependants();

		/**
		 * The meta object literal for the '<em><b>Resulting Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_CHANGE__RESULTING_ELEMENT = eINSTANCE.getIndepChange_ResultingElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.ChangeGroupImpl <em>Change Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.ChangeGroupImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeGroup()
		 * @generated
		 */
		EClass CHANGE_GROUP = eINSTANCE.getChangeGroup();

		/**
		 * The meta object literal for the '<em><b>Sub Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_GROUP__SUB_CHANGES = eINSTANCE.getChangeGroup_SubChanges();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepElementChangeImpl <em>Indep Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepElementChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepElementChange()
		 * @generated
		 */
		EClass INDEP_ELEMENT_CHANGE = eINSTANCE.getIndepElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl <em>Indep Add Rem Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemElementChange()
		 * @generated
		 */
		EClass INDEP_ADD_REM_ELEMENT_CHANGE = eINSTANCE.getIndepAddRemElementChange();

		/**
		 * The meta object literal for the '<em><b>Sub Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL = eINSTANCE.getIndepAddRemElementChange_SubModel();

		/**
		 * The meta object literal for the '<em><b>Containment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT = eINSTANCE.getIndepAddRemElementChange_Containment();

		/**
		 * The meta object literal for the '<em><b>Sub Model Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE = eINSTANCE.getIndepAddRemElementChange_SubModelReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddElementChangeImpl <em>Indep Add Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddElementChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddElementChange()
		 * @generated
		 */
		EClass INDEP_ADD_ELEMENT_CHANGE = eINSTANCE.getIndepAddElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveElementChangeImpl <em>Indep Remove Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveElementChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveElementChange()
		 * @generated
		 */
		EClass INDEP_REMOVE_ELEMENT_CHANGE = eINSTANCE.getIndepRemoveElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAttributeChangeImpl <em>Indep Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAttributeChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAttributeChange()
		 * @generated
		 */
		EClass INDEP_ATTRIBUTE_CHANGE = eINSTANCE.getIndepAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Changed Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE = eINSTANCE.getIndepAttributeChange_ChangedAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemAttributeChangeImpl <em>Indep Add Rem Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemAttributeChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemAttributeChange()
		 * @generated
		 */
		EClass INDEP_ADD_REM_ATTRIBUTE_CHANGE = eINSTANCE.getIndepAddRemAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEP_ADD_REM_ATTRIBUTE_CHANGE__VALUE = eINSTANCE.getIndepAddRemAttributeChange_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl <em>Indep Move Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepMoveElementChange()
		 * @generated
		 */
		EClass INDEP_MOVE_ELEMENT_CHANGE = eINSTANCE.getIndepMoveElementChange();

		/**
		 * The meta object literal for the '<em><b>Old Containment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT = eINSTANCE.getIndepMoveElementChange_OldContainment();

		/**
		 * The meta object literal for the '<em><b>New Containment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT = eINSTANCE.getIndepMoveElementChange_NewContainment();

		/**
		 * The meta object literal for the '<em><b>Old Parent</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT = eINSTANCE.getIndepMoveElementChange_OldParent();

		/**
		 * The meta object literal for the '<em><b>New Parent</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT = eINSTANCE.getIndepMoveElementChange_NewParent();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddAttributeChangeImpl <em>Indep Add Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddAttributeChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddAttributeChange()
		 * @generated
		 */
		EClass INDEP_ADD_ATTRIBUTE_CHANGE = eINSTANCE.getIndepAddAttributeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveAttributeChangeImpl <em>Indep Remove Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveAttributeChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveAttributeChange()
		 * @generated
		 */
		EClass INDEP_REMOVE_ATTRIBUTE_CHANGE = eINSTANCE.getIndepRemoveAttributeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateAttributeChangeImpl <em>Indep Update Attribute Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepUpdateAttributeChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepUpdateAttributeChange()
		 * @generated
		 */
		EClass INDEP_UPDATE_ATTRIBUTE_CHANGE = eINSTANCE.getIndepUpdateAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Old Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEP_UPDATE_ATTRIBUTE_CHANGE__OLD_VALUE = eINSTANCE.getIndepUpdateAttributeChange_OldValue();

		/**
		 * The meta object literal for the '<em><b>New Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEP_UPDATE_ATTRIBUTE_CHANGE__NEW_VALUE = eINSTANCE.getIndepUpdateAttributeChange_NewValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepReferenceChangeImpl <em>Indep Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepReferenceChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepReferenceChange()
		 * @generated
		 */
		EClass INDEP_REFERENCE_CHANGE = eINSTANCE.getIndepReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_REFERENCE_CHANGE__REFERENCE = eINSTANCE.getIndepReferenceChange_Reference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemReferenceChangeImpl <em>Indep Add Rem Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddRemReferenceChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddRemReferenceChange()
		 * @generated
		 */
		EClass INDEP_ADD_REM_REFERENCE_CHANGE = eINSTANCE.getIndepAddRemReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Changed Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE = eINSTANCE.getIndepAddRemReferenceChange_ChangedReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepAddReferenceChangeImpl <em>Indep Add Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepAddReferenceChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepAddReferenceChange()
		 * @generated
		 */
		EClass INDEP_ADD_REFERENCE_CHANGE = eINSTANCE.getIndepAddReferenceChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepRemoveReferenceChangeImpl <em>Indep Remove Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepRemoveReferenceChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepRemoveReferenceChange()
		 * @generated
		 */
		EClass INDEP_REMOVE_REFERENCE_CHANGE = eINSTANCE.getIndepRemoveReferenceChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl <em>Indep Update Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIndepUpdateReferenceChange()
		 * @generated
		 */
		EClass INDEP_UPDATE_REFERENCE_CHANGE = eINSTANCE.getIndepUpdateReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Old Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE = eINSTANCE.getIndepUpdateReferenceChange_OldReference();

		/**
		 * The meta object literal for the '<em><b>New Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE = eINSTANCE.getIndepUpdateReferenceChange_NewReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.IElementReference <em>IElement Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.IElementReference
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIElementReference()
		 * @generated
		 */
		EClass IELEMENT_REFERENCE = eINSTANCE.getIElementReference();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IELEMENT_REFERENCE__TYPE = eINSTANCE.getIElementReference_Type();

		/**
		 * The meta object literal for the '<em><b>Uri Reference</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IELEMENT_REFERENCE__URI_REFERENCE = eINSTANCE.getIElementReference_UriReference();

		/**
		 * The meta object literal for the '<em><b>Upper Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IELEMENT_REFERENCE__UPPER_BOUND = eINSTANCE.getIElementReference_UpperBound();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IELEMENT_REFERENCE__LOWER_BOUND = eINSTANCE.getIElementReference_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IELEMENT_REFERENCE__LABEL = eINSTANCE.getIElementReference_Label();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor <em>IModel Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getIModelDescriptor()
		 * @generated
		 */
		EClass IMODEL_DESCRIPTOR = eINSTANCE.getIModelDescriptor();

		/**
		 * The meta object literal for the '<em><b>Cross References</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__CROSS_REFERENCES = eINSTANCE.getIModelDescriptor_CrossReferences();

		/**
		 * The meta object literal for the '<em><b>All Cross References</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__ALL_CROSS_REFERENCES = eINSTANCE.getIModelDescriptor_AllCrossReferences();

		/**
		 * The meta object literal for the '<em><b>Self Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__SELF_REFERENCE = eINSTANCE.getIModelDescriptor_SelfReference();

		/**
		 * The meta object literal for the '<em><b>Descriptor Uris</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMODEL_DESCRIPTOR__DESCRIPTOR_URIS = eINSTANCE.getIModelDescriptor_DescriptorUris();

		/**
		 * The meta object literal for the '<em><b>All Self References</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__ALL_SELF_REFERENCES = eINSTANCE.getIModelDescriptor_AllSelfReferences();

		/**
		 * The meta object literal for the '<em><b>Sub Model Descriptors</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS = eINSTANCE.getIModelDescriptor_SubModelDescriptors();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL_DESCRIPTOR__TYPE = eINSTANCE.getIModelDescriptor_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.ElementReferenceToEObjectMapImpl <em>Element Reference To EObject Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.ElementReferenceToEObjectMapImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getElementReferenceToEObjectMap()
		 * @generated
		 */
		EClass ELEMENT_REFERENCE_TO_EOBJECT_MAP = eINSTANCE.getElementReferenceToEObjectMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_REFERENCE_TO_EOBJECT_MAP__KEY = eINSTANCE.getElementReferenceToEObjectMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_REFERENCE_TO_EOBJECT_MAP__VALUE = eINSTANCE.getElementReferenceToEObjectMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.EObjectToIModelDescriptorMapImpl <em>EObject To IModel Descriptor Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.EObjectToIModelDescriptorMapImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getEObjectToIModelDescriptorMap()
		 * @generated
		 */
		EClass EOBJECT_TO_IMODEL_DESCRIPTOR_MAP = eINSTANCE.getEObjectToIModelDescriptorMap();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__VALUE = eINSTANCE.getEObjectToIModelDescriptorMap_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_TO_IMODEL_DESCRIPTOR_MAP__KEY = eINSTANCE.getEObjectToIModelDescriptorMap_Key();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.UnknownChangeImpl <em>Unknown Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.UnknownChangeImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getUnknownChange()
		 * @generated
		 */
		EClass UNKNOWN_CHANGE = eINSTANCE.getUnknownChange();

		/**
		 * The meta object literal for the '<em><b>Info</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNKNOWN_CHANGE__INFO = eINSTANCE.getUnknownChange_Info();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl <em>Model Descriptor Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getModelDescriptorReference()
		 * @generated
		 */
		EClass MODEL_DESCRIPTOR_REFERENCE = eINSTANCE.getModelDescriptorReference();

		/**
		 * The meta object literal for the '<em><b>Resolves To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO = eINSTANCE.getModelDescriptorReference_ResolvesTo();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.ChangeType <em>Change Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.ChangeType
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeType()
		 * @generated
		 */
		EEnum CHANGE_TYPE = eINSTANCE.getChangeType();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.mpatch.ChangeKind <em>Change Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.mpatch.ChangeKind
		 * @see org.eclipse.emf.compare.mpatch.impl.MPatchPackageImpl#getChangeKind()
		 * @generated
		 */
		EEnum CHANGE_KIND = eINSTANCE.getChangeKind();

	}

} //MPatchPackage
