/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.external;

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
 * @see org.eclipse.emf.compare.tests.external.ExternalFactory
 * @model kind="package"
 * @generated
 */
public interface ExternalPackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "external"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "externalURI"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "compare.tests.external"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ExternalPackage eINSTANCE = org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.external.impl.StringHolderImpl <em>String Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.external.impl.StringHolderImpl
	 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getStringHolder()
	 * @generated
	 */
	int STRING_HOLDER = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_HOLDER__NAME = 0;

	/**
	 * The number of structural features of the '<em>String Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_HOLDER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.external.impl.HolderImpl <em>Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.external.impl.HolderImpl
	 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getHolder()
	 * @generated
	 */
	int HOLDER = 1;

	/**
	 * The feature id for the '<em><b>String Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLDER__STRING_HOLDER = 0;

	/**
	 * The number of structural features of the '<em>Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLDER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.external.impl.NoncontainmentHolderImpl <em>Noncontainment Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.external.impl.NoncontainmentHolderImpl
	 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getNoncontainmentHolder()
	 * @generated
	 */
	int NONCONTAINMENT_HOLDER = 2;

	/**
	 * The feature id for the '<em><b>String Holder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NONCONTAINMENT_HOLDER__STRING_HOLDER = 0;

	/**
	 * The number of structural features of the '<em>Noncontainment Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NONCONTAINMENT_HOLDER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.tests.external.impl.HolderHolderImpl <em>Holder Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.tests.external.impl.HolderHolderImpl
	 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getHolderHolder()
	 * @generated
	 */
	int HOLDER_HOLDER = 3;

	/**
	 * The feature id for the '<em><b>Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLDER_HOLDER__HOLDER = 0;

	/**
	 * The number of structural features of the '<em>Holder Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLDER_HOLDER_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.external.StringHolder <em>String Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.StringHolder
	 * @generated
	 */
	EClass getStringHolder();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.tests.external.StringHolder#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.compare.tests.external.StringHolder#getName()
	 * @see #getStringHolder()
	 * @generated
	 */
	EAttribute getStringHolder_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.external.Holder <em>Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.Holder
	 * @generated
	 */
	EClass getHolder();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.tests.external.Holder#getStringHolder <em>String Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>String Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.Holder#getStringHolder()
	 * @see #getHolder()
	 * @generated
	 */
	EReference getHolder_StringHolder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.external.NoncontainmentHolder <em>Noncontainment Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Noncontainment Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.NoncontainmentHolder
	 * @generated
	 */
	EClass getNoncontainmentHolder();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.tests.external.NoncontainmentHolder#getStringHolder <em>String Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>String Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.NoncontainmentHolder#getStringHolder()
	 * @see #getNoncontainmentHolder()
	 * @generated
	 */
	EReference getNoncontainmentHolder_StringHolder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.tests.external.HolderHolder <em>Holder Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Holder Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.HolderHolder
	 * @generated
	 */
	EClass getHolderHolder();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.tests.external.HolderHolder#getHolder <em>Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Holder</em>'.
	 * @see org.eclipse.emf.compare.tests.external.HolderHolder#getHolder()
	 * @see #getHolderHolder()
	 * @generated
	 */
	EReference getHolderHolder_Holder();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ExternalFactory getExternalFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.external.impl.StringHolderImpl <em>String Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.external.impl.StringHolderImpl
		 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getStringHolder()
		 * @generated
		 */
		EClass STRING_HOLDER = eINSTANCE.getStringHolder();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_HOLDER__NAME = eINSTANCE.getStringHolder_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.external.impl.HolderImpl <em>Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.external.impl.HolderImpl
		 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getHolder()
		 * @generated
		 */
		EClass HOLDER = eINSTANCE.getHolder();

		/**
		 * The meta object literal for the '<em><b>String Holder</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOLDER__STRING_HOLDER = eINSTANCE.getHolder_StringHolder();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.external.impl.NoncontainmentHolderImpl <em>Noncontainment Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.external.impl.NoncontainmentHolderImpl
		 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getNoncontainmentHolder()
		 * @generated
		 */
		EClass NONCONTAINMENT_HOLDER = eINSTANCE.getNoncontainmentHolder();

		/**
		 * The meta object literal for the '<em><b>String Holder</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NONCONTAINMENT_HOLDER__STRING_HOLDER = eINSTANCE.getNoncontainmentHolder_StringHolder();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.tests.external.impl.HolderHolderImpl <em>Holder Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.tests.external.impl.HolderHolderImpl
		 * @see org.eclipse.emf.compare.tests.external.impl.ExternalPackageImpl#getHolderHolder()
		 * @generated
		 */
		EClass HOLDER_HOLDER = eINSTANCE.getHolderHolder();

		/**
		 * The meta object literal for the '<em><b>Holder</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOLDER_HOLDER__HOLDER = eINSTANCE.getHolderHolder_Holder();

	}

} //ExternalPackage
