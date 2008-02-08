/**
 * 
 *  Copyright (c) 2006, 2007, 2008 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: DiffExtensionFactoryImpl.java,v 1.4 2008/02/08 20:40:19 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl;

import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.*;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.AddUMLAssociation;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionFactory;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class DiffExtensionFactoryImpl extends EFactoryImpl implements DiffExtensionFactory {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "\n Copyright (c) 2006, 2007, 2008 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffExtensionFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DiffExtensionPackage getPackage() {
		return DiffExtensionPackage.eINSTANCE;
	}

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DiffExtensionFactory init() {
		try {
			DiffExtensionFactory theDiffExtensionFactory = (DiffExtensionFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/sample/diff_extension/1.0"); 
			if (theDiffExtensionFactory != null) {
				return theDiffExtensionFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DiffExtensionFactoryImpl();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION: return createAddUMLAssociation();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AddUMLAssociation createAddUMLAssociation() {
		AddUMLAssociationImpl addUMLAssociation = new AddUMLAssociationImpl();
		return addUMLAssociation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffExtensionPackage getDiffExtensionPackage() {
		return (DiffExtensionPackage)getEPackage();
	}

} // DiffExtensionFactoryImpl
