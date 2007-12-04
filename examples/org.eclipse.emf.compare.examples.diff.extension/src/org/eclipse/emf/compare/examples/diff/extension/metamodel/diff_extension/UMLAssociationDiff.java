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
 * $Id: UMLAssociationDiff.java,v 1.2 2007/12/04 13:14:48 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>UML Association Diff</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getProperties <em>Properties</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#isIsNavigable <em>Is Navigable</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getContainerPackage <em>Container Package</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage#getUMLAssociationDiff()
 * @model abstract="true"
 * @generated
 */
public interface UMLAssociationDiff extends AbstractDiffExtension {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String copyright = "\n Copyright (c) 2006, 2007 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * Returns the value of the '<em><b>Container Package</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Container Package</em>' reference isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Container Package</em>' reference.
	 * @see #setContainerPackage(EObject)
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage#getUMLAssociationDiff_ContainerPackage()
	 * @model required="true"
	 * @generated
	 */
	EObject getContainerPackage();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' reference list. The list contents are of
	 * type {@link org.eclipse.emf.ecore.EObject}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' reference list isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Properties</em>' reference list.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage#getUMLAssociationDiff_Properties()
	 * @model required="true"
	 * @generated
	 */
	EList<EObject> getProperties();

	/**
	 * Returns the value of the '<em><b>Is Navigable</b></em>' attribute. The default value is
	 * <code>"false"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Navigable</em>' attribute isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Is Navigable</em>' attribute.
	 * @see #setIsNavigable(boolean)
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage#getUMLAssociationDiff_IsNavigable()
	 * @model default="false"
	 * @generated
	 */
	boolean isIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getContainerPackage <em>Container Package</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Container Package</em>' reference.
	 * @see #getContainerPackage()
	 * @generated
	 */
	void setContainerPackage(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#isIsNavigable <em>Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Is Navigable</em>' attribute.
	 * @see #isIsNavigable()
	 * @generated
	 */
	void setIsNavigable(boolean value);

} // UMLAssociationDiff
