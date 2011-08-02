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
 * A representation of the model object '<em><b>Node Multiple Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.merge.NodeMultipleContainment#getContainmentRef2 <em>Containment Ref2</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.merge.MergePackage#getNodeMultipleContainment()
 * @model
 * @generated
 */
public interface NodeMultipleContainment extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Containment Ref2</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.tests.merge.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Containment Ref2</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Containment Ref2</em>' containment reference list.
	 * @see org.eclipse.emf.compare.tests.merge.MergePackage#getNodeMultipleContainment_ContainmentRef2()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getContainmentRef2();

} // NodeMultipleContainment
