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
 * $Id: DescriptorFactory.java,v 1.1 2010/09/10 15:32:56 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage
 * @generated
 */
public interface DescriptorFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DescriptorFactory eINSTANCE = org.eclipse.emf.compare.mpatch.descriptor.impl.DescriptorFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EMF Model Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EMF Model Descriptor</em>'.
	 * @generated
	 */
	EMFModelDescriptor createEMFModelDescriptor();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DescriptorPackage getDescriptorPackage();

} //DescriptorFactory
