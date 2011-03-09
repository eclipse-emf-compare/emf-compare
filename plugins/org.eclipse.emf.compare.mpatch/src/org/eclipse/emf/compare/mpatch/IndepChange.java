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
 * $Id: IndepChange.java,v 1.2 2010/10/20 09:22:23 pkonemann Exp $
 */
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indep Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getCorrespondingElement <em>Corresponding Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getChangeKind <em>Change Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getChangeType <em>Change Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependsOn <em>Depends On</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependants <em>Dependants</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepChange#getResultingElement <em>Resulting Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange()
 * @model abstract="true"
 * @generated
 */
public interface IndepChange extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Corresponding Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The corresponding element is the element that is actually changed. <br>
	 * In case of an attribute- or reference-change, it is the owner of the attribute / reference.<br>
	 * In case of an add- or remove-element-change, it is the parent of the added / removed element.<br>
	 * In case of a move-element-change, it is the moved element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Corresponding Element</em>' containment reference.
	 * @see #setCorrespondingElement(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_CorrespondingElement()
	 * @model containment="true"
	 * @generated
	 */
	IElementReference getCorrespondingElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepChange#getCorrespondingElement <em>Corresponding Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Corresponding Element</em>' containment reference.
	 * @see #getCorrespondingElement()
	 * @generated
	 */
	void setCorrespondingElement(IElementReference value);

	/**
	 * Returns the value of the '<em><b>Change Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.compare.mpatch.ChangeKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derived from the concrete type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Change Kind</em>' attribute.
	 * @see org.eclipse.emf.compare.mpatch.ChangeKind
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_ChangeKind()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	ChangeKind getChangeKind();

	/**
	 * Returns the value of the '<em><b>Change Type</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * The literals are from the enumeration {@link org.eclipse.emf.compare.mpatch.ChangeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derived from the concrete type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Change Type</em>' attribute.
	 * @see org.eclipse.emf.compare.mpatch.ChangeType
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_ChangeType()
	 * @model default="" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	ChangeType getChangeType();

	/**
	 * Returns the value of the '<em><b>Depends On</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IndepChange}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependants <em>Dependants</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Depends On</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Depends On</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_DependsOn()
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getDependants
	 * @model opposite="dependants"
	 * @generated
	 */
	EList<IndepChange> getDependsOn();

	/**
	 * Returns the value of the '<em><b>Dependants</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IndepChange}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.mpatch.IndepChange#getDependsOn <em>Depends On</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependants</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependants</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_Dependants()
	 * @see org.eclipse.emf.compare.mpatch.IndepChange#getDependsOn
	 * @model opposite="dependsOn"
	 * @generated
	 */
	EList<IndepChange> getDependants();

	/**
	 * Returns the value of the '<em><b>Resulting Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The corresponding element is the element that is actually changed. <br>
	 * In case of an attribute- or reference-change, it is the owner of the attribute / reference.<br>
	 * In case of an add- or remove-element-change, it is the parent of the added / removed element.<br>
	 * In case of a move-element-change, it is the moved element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resulting Element</em>' containment reference.
	 * @see #setResultingElement(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepChange_ResultingElement()
	 * @model containment="true"
	 * @generated
	 */
	IElementReference getResultingElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepChange#getResultingElement <em>Resulting Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resulting Element</em>' containment reference.
	 * @see #getResultingElement()
	 * @generated
	 */
	void setResultingElement(IElementReference value);

} // IndepChange
