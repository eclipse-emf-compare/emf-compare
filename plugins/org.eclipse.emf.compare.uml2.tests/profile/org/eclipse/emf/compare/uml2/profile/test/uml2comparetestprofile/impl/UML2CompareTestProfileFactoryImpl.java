/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl;

import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.*;

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
public class UML2CompareTestProfileFactoryImpl extends EFactoryImpl implements UML2CompareTestProfileFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static UML2CompareTestProfileFactory init() {
		try {
			UML2CompareTestProfileFactory theUML2CompareTestProfileFactory = (UML2CompareTestProfileFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile"); 
			if (theUML2CompareTestProfileFactory != null) {
				return theUML2CompareTestProfileFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UML2CompareTestProfileFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2CompareTestProfileFactoryImpl() {
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
			case UML2CompareTestProfilePackage.ACLICHE: return createACliche();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ACliche createACliche() {
		AClicheImpl aCliche = new AClicheImpl();
		return aCliche;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2CompareTestProfilePackage getUML2CompareTestProfilePackage() {
		return (UML2CompareTestProfilePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UML2CompareTestProfilePackage getPackage() {
		return UML2CompareTestProfilePackage.eINSTANCE;
	}

} //UML2CompareTestProfileFactoryImpl
