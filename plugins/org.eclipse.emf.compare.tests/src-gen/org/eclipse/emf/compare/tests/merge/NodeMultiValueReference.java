/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Multi Value Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.merge.NodeMultiValueReference#getMultiValuedReference <em>Multi Valued Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.merge.MergePackage#getNodeMultiValueReference()
 * @model
 * @generated
 */
public interface NodeMultiValueReference extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Multi Valued Reference</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.merge.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Valued Reference</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Valued Reference</em>' reference list.
	 * @see org.eclipse.emf.compare.tests.merge.MergePackage#getNodeMultiValueReference_MultiValuedReference()
	 * @model
	 * @generated
	 */
	EList<Node> getMultiValuedReference();

} // NodeMultiValueReference
