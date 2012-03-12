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
 * $Id: IndepUpdateReferenceChange.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indep Update Reference Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getOldReference <em>Old Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getNewReference <em>New Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepUpdateReferenceChange()
 * @model
 * @generated
 */
public interface IndepUpdateReferenceChange extends IndepReferenceChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Old Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Reference</em>' containment reference.
	 * @see #setOldReference(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepUpdateReferenceChange_OldReference()
	 * @model containment="true"
	 * @generated
	 */
	IElementReference getOldReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getOldReference <em>Old Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Reference</em>' containment reference.
	 * @see #getOldReference()
	 * @generated
	 */
	void setOldReference(IElementReference value);

	/**
	 * Returns the value of the '<em><b>New Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Reference</em>' containment reference.
	 * @see #setNewReference(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepUpdateReferenceChange_NewReference()
	 * @model containment="true"
	 * @generated
	 */
	IElementReference getNewReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange#getNewReference <em>New Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Reference</em>' containment reference.
	 * @see #getNewReference()
	 * @generated
	 */
	void setNewReference(IElementReference value);

} // IndepUpdateReferenceChange
