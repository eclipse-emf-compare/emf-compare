/**
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Multi Value EEnum Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeMultiValueEEnumAttribute#getMultiValueEEnumAttribute <em>Multi Value EEnum Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeMultiValueEEnumAttribute()
 * @model
 * @generated
 */
public interface NodeMultiValueEEnumAttribute extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Multi Value EEnum Attribute</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.nodes.NodeEnum}.
	 * The literals are from the enumeration {@link org.eclipse.emf.compare.tests.nodes.NodeEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Value EEnum Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Value EEnum Attribute</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodeEnum
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeMultiValueEEnumAttribute_MultiValueEEnumAttribute()
	 * @model
	 * @generated
	 */
	EList<NodeEnum> getMultiValueEEnumAttribute();

} // NodeMultiValueEEnumAttribute
