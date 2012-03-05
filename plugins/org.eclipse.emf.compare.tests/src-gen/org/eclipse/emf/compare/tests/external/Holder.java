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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.external.Holder#getStringHolder <em>String Holder</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.external.ExternalPackage#getHolder()
 * @model
 * @generated
 */
public interface Holder extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>String Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String Holder</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String Holder</em>' containment reference.
	 * @see #setStringHolder(StringHolder)
	 * @see org.eclipse.emf.compare.tests.external.ExternalPackage#getHolder_StringHolder()
	 * @model containment="true"
	 * @generated
	 */
	StringHolder getStringHolder();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.external.Holder#getStringHolder <em>String Holder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>String Holder</em>' containment reference.
	 * @see #getStringHolder()
	 * @generated
	 */
	void setStringHolder(StringHolder value);

} // Holder
