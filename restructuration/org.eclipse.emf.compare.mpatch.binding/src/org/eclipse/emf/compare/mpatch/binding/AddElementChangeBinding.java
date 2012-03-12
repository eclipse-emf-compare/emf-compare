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
 * $Id: AddElementChangeBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Add Element Change Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding#getSubModelReferences <em>Sub Model References</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getAddElementChangeBinding()
 * @model
 * @generated
 */
public interface AddElementChangeBinding extends ChangeBinding {
	/**
	 * Returns the value of the '<em><b>Sub Model References</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model References</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getAddElementChangeBinding_SubModelReferences()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<SubModelBinding> getSubModelReferences();

} // AddElementChangeBinding
