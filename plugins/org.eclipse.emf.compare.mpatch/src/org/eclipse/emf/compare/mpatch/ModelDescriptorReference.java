/**
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: ModelDescriptorReference.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Descriptor Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This implementation of symbolic references is only used for cross-references referring to added/deleted elements!
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.ModelDescriptorReference#getResolvesTo <em>Resolves To</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getModelDescriptorReference()
 * @model
 * @generated
 */
public interface ModelDescriptorReference extends IElementReference {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Resolves To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resolves To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resolves To</em>' reference.
	 * @see #setResolvesTo(IModelDescriptor)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getModelDescriptorReference_ResolvesTo()
	 * @model required="true"
	 * @generated
	 */
	IModelDescriptor getResolvesTo();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.ModelDescriptorReference#getResolvesTo <em>Resolves To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resolves To</em>' reference.
	 * @see #getResolvesTo()
	 * @generated
	 */
	void setResolvesTo(IModelDescriptor value);

} // ModelDescriptorReference
