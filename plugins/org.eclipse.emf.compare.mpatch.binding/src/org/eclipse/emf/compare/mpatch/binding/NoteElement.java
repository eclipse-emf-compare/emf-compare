/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: NoteElement.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Note Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.NoteElement#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getNoteElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NoteElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Notes</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.Note}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getNoteElement_Notes()
	 * @model
	 * @generated
	 */
	EList<Note> getNotes();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Note</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='\t\tString note = \"\";\r\n\t\tfor (Note aNote : getNotes())\r\n\t\t\tnote += (note.length() > 0 ? \"\\n\" : \"\") + aNote.getNote();\r\n\t\treturn note;'"
	 * @generated
	 */
	String getNote();

} // NoteElement
