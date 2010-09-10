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
 * $Id: IdEmfReference.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;

import org.eclipse.emf.compare.mpatch.IElementReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Id Emf Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference#getIdAttributeValue <em>Id Attribute Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getIdEmfReference()
 * @model
 * @generated
 */
public interface IdEmfReference extends IElementReference {
	/**
	 * Returns the value of the '<em><b>Id Attribute Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id Attribute Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id Attribute Value</em>' attribute.
	 * @see #setIdAttributeValue(String)
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getIdEmfReference_IdAttributeValue()
	 * @model
	 * @generated
	 */
	String getIdAttributeValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference#getIdAttributeValue <em>Id Attribute Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id Attribute Value</em>' attribute.
	 * @see #getIdAttributeValue()
	 * @generated
	 */
	void setIdAttributeValue(String value);

} // IdEmfReference
