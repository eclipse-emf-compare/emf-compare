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
 * $Id: IndepAddRemReferenceChange.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indep Add Rem Reference Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange#getChangedReference <em>Changed Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemReferenceChange()
 * @model abstract="true"
 * @generated
 */
public interface IndepAddRemReferenceChange extends IndepReferenceChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Changed Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changed Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Changed Reference</em>' containment reference.
	 * @see #setChangedReference(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemReferenceChange_ChangedReference()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IElementReference getChangedReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange#getChangedReference <em>Changed Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Changed Reference</em>' containment reference.
	 * @see #getChangedReference()
	 * @generated
	 */
	void setChangedReference(IElementReference value);

} // IndepAddRemReferenceChange
