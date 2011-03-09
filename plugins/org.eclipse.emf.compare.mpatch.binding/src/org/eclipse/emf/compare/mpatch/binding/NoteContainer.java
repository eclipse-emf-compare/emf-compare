/**
 *  Copyright (c) 2010, 2011 Technical University of Denmark.
 *  All rights reserved. This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html 
 *  
 *  Contributors:
 *     Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: NoteContainer.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Note Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer#getAllNotes <em>All Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getNoteContainer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NoteContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>All Notes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.Note}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All Notes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>All Notes</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getNoteContainer_AllNotes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Note> getAllNotes();

} // NoteContainer
