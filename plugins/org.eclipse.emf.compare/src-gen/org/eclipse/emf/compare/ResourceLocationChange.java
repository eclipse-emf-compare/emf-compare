/**
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Location Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This particular kind of difference describes the change of a resource's location.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.ResourceLocationChange#getBaseLocation <em>Base Location</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.ResourceLocationChange#getChangedLocation <em>Changed Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getResourceLocationChange()
 * @model
 * @generated
 * @since 3.2
 */
public interface ResourceLocationChange extends Diff {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Base Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Cannot be null. Represents the URI of the left resource of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Base Location</em>' attribute.
	 * @see #setBaseLocation(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getResourceLocationChange_BaseLocation()
	 * @model required="true"
	 * @generated
	 */
	String getBaseLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.ResourceLocationChange#getBaseLocation <em>Base Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Location</em>' attribute.
	 * @see #getBaseLocation()
	 * @generated
	 */
	void setBaseLocation(String value);

	/**
	 * Returns the value of the '<em><b>Changed Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Cannot be null. Represents the URI of the right resource of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Changed Location</em>' attribute.
	 * @see #setChangedLocation(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getResourceLocationChange_ChangedLocation()
	 * @model required="true"
	 * @generated
	 */
	String getChangedLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.ResourceLocationChange#getChangedLocation <em>Changed Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Changed Location</em>' attribute.
	 * @see #getChangedLocation()
	 * @generated
	 */
	void setChangedLocation(String value);

} // ResourceLocationChange
