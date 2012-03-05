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
package org.eclipse.emf.compare.tests.nodes;

import org.eclipse.emf.compare.tests.external.Holder;
import org.eclipse.emf.compare.tests.external.NoncontainmentHolder;
import org.eclipse.emf.compare.tests.external.StringHolder;

import org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Leaf</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentHolder <em>Noncontainment Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getContainmentHolder <em>Containment Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNumber <em>Number</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getHolder <em>Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNonEMF <em>Non EMF</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentNoncontainmentHolder <em>Noncontainment Noncontainment Holder</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf()
 * @model
 * @generated
 */
public interface Leaf extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Noncontainment Holder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Noncontainment Holder</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Noncontainment Holder</em>' reference.
	 * @see #setNoncontainmentHolder(StringHolder)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_NoncontainmentHolder()
	 * @model
	 * @generated
	 */
	StringHolder getNoncontainmentHolder();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentHolder <em>Noncontainment Holder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Noncontainment Holder</em>' reference.
	 * @see #getNoncontainmentHolder()
	 * @generated
	 */
	void setNoncontainmentHolder(StringHolder value);

	/**
	 * Returns the value of the '<em><b>Containment Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Containment Holder</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Containment Holder</em>' containment reference.
	 * @see #setContainmentHolder(StringHolder)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_ContainmentHolder()
	 * @model containment="true"
	 * @generated
	 */
	StringHolder getContainmentHolder();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getContainmentHolder <em>Containment Holder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Containment Holder</em>' containment reference.
	 * @see #getContainmentHolder()
	 * @generated
	 */
	void setContainmentHolder(StringHolder value);

	/**
	 * Returns the value of the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number</em>' attribute.
	 * @see #setNumber(int)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_Number()
	 * @model
	 * @generated
	 */
	int getNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNumber <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number</em>' attribute.
	 * @see #getNumber()
	 * @generated
	 */
	void setNumber(int value);

	/**
	 * Returns the value of the '<em><b>Holder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Holder</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Holder</em>' containment reference.
	 * @see #setHolder(Holder)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_Holder()
	 * @model containment="true"
	 * @generated
	 */
	Holder getHolder();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getHolder <em>Holder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Holder</em>' containment reference.
	 * @see #getHolder()
	 * @generated
	 */
	void setHolder(Holder value);

	/**
	 * Returns the value of the '<em><b>Non EMF</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Non EMF</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Non EMF</em>' attribute.
	 * @see #setNonEMF(NonEMFStringHolder)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_NonEMF()
	 * @model dataType="org.eclipse.emf.compare.tests.nodes.NonEMFStringHolder"
	 * @generated
	 */
	NonEMFStringHolder getNonEMF();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNonEMF <em>Non EMF</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Non EMF</em>' attribute.
	 * @see #getNonEMF()
	 * @generated
	 */
	void setNonEMF(NonEMFStringHolder value);

	/**
	 * Returns the value of the '<em><b>Noncontainment Noncontainment Holder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Noncontainment Noncontainment Holder</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Noncontainment Noncontainment Holder</em>' reference.
	 * @see #setNoncontainmentNoncontainmentHolder(NoncontainmentHolder)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getLeaf_NoncontainmentNoncontainmentHolder()
	 * @model
	 * @generated
	 */
	NoncontainmentHolder getNoncontainmentNoncontainmentHolder();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.Leaf#getNoncontainmentNoncontainmentHolder <em>Noncontainment Noncontainment Holder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Noncontainment Noncontainment Holder</em>' reference.
	 * @see #getNoncontainmentNoncontainmentHolder()
	 * @generated
	 */
	void setNoncontainmentNoncontainmentHolder(NoncontainmentHolder value);

} // Leaf
