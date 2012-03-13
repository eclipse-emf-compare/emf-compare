/**
 * Copyright (c) 2012 Obeo.
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
 * A representation of the model object '<em><b>Resource Attachment Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This particular kind of difference describes the fragmentation (or un-fragmentation) of a model element in its own (or back in its parent's) resource.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.ResourceAttachmentChange#getResourceURI <em>Resource URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getResourceAttachmentChange()
 * @model
 * @generated
 */
public interface ResourceAttachmentChange extends Diff {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Resource URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * URI of the resource in which the parent mapping's element is now located.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resource URI</em>' attribute.
	 * @see #setResourceURI(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getResourceAttachmentChange_ResourceURI()
	 * @model required="true"
	 * @generated
	 */
	String getResourceURI();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.ResourceAttachmentChange#getResourceURI <em>Resource URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource URI</em>' attribute.
	 * @see #getResourceURI()
	 * @generated
	 */
	void setResourceURI(String value);

} // ResourceAttachmentChange
