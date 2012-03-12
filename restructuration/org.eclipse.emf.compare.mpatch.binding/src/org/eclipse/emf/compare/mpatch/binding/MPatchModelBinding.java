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
 * $Id: MPatchModelBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diff Model Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getChangeBindings <em>Change Bindings</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getModel <em>Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getMPatchModel <em>MPatch Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getMPatchModelBinding()
 * @model
 * @generated
 */
public interface MPatchModelBinding extends NoteElement, NoteContainer {
	/**
	 * Returns the value of the '<em><b>Change Bindings</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Bindings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Bindings</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getMPatchModelBinding_ChangeBindings()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeBinding> getChangeBindings();

	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(EObject)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getMPatchModelBinding_Model()
	 * @model required="true"
	 * @generated
	 */
	EObject getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(EObject value);

	/**
	 * Returns the value of the '<em><b>MPatch Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>MPatch Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>MPatch Model</em>' reference.
	 * @see #setMPatchModel(MPatchModel)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getMPatchModelBinding_MPatchModel()
	 * @model required="true"
	 * @generated
	 */
	MPatchModel getMPatchModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding#getMPatchModel <em>MPatch Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>MPatch Model</em>' reference.
	 * @see #getMPatchModel()
	 * @generated
	 */
	void setMPatchModel(MPatchModel value);

} // MPatchModelBinding
