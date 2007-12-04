/**
 * 
 *  Copyright (c) 2006, 2007 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: DiffExtensionFactory.java,v 1.2 2007/12/04 13:14:49 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage
 * @generated
 */
public interface DiffExtensionFactory extends EFactory {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String copyright = "\n Copyright (c) 2006, 2007 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	DiffExtensionFactory eINSTANCE = org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Add UML Association</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Add UML Association</em>'.
	 * @generated
	 */
	AddUMLAssociation createAddUMLAssociation();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	DiffExtensionPackage getDiffExtensionPackage();

} // DiffExtensionFactory
