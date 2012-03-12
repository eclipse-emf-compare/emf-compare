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
 * $Id: BindingFactory.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage
 * @generated
 */
public interface BindingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BindingFactory eINSTANCE = org.eclipse.emf.compare.mpatch.binding.impl.BindingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>MPatch Model Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MPatch Model Binding</em>'.
	 * @generated
	 */
	MPatchModelBinding createMPatchModelBinding();

	/**
	 * Returns a new object of class '<em>Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Element Change Binding</em>'.
	 * @generated
	 */
	ElementChangeBinding createElementChangeBinding();

	/**
	 * Returns a new object of class '<em>Sub Model Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sub Model Binding</em>'.
	 * @generated
	 */
	SubModelBinding createSubModelBinding();

	/**
	 * Returns a new object of class '<em>Attribute Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Change Binding</em>'.
	 * @generated
	 */
	AttributeChangeBinding createAttributeChangeBinding();

	/**
	 * Returns a new object of class '<em>Add Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Element Change Binding</em>'.
	 * @generated
	 */
	AddElementChangeBinding createAddElementChangeBinding();

	/**
	 * Returns a new object of class '<em>Move Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move Element Change Binding</em>'.
	 * @generated
	 */
	MoveElementChangeBinding createMoveElementChangeBinding();

	/**
	 * Returns a new object of class '<em>Add Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Reference Change Binding</em>'.
	 * @generated
	 */
	AddReferenceChangeBinding createAddReferenceChangeBinding();

	/**
	 * Returns a new object of class '<em>Update Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Reference Change Binding</em>'.
	 * @generated
	 */
	UpdateReferenceChangeBinding createUpdateReferenceChangeBinding();

	/**
	 * Returns a new object of class '<em>Remove Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Element Change Binding</em>'.
	 * @generated
	 */
	RemoveElementChangeBinding createRemoveElementChangeBinding();

	/**
	 * Returns a new object of class '<em>Remove Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Reference Change Binding</em>'.
	 * @generated
	 */
	RemoveReferenceChangeBinding createRemoveReferenceChangeBinding();

	/**
	 * Returns a new object of class '<em>Note</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Note</em>'.
	 * @generated
	 */
	Note createNote();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	BindingPackage getBindingPackage();

	/**
	 * Create a {@link Note} for a given {@link NoteElement}, assuming that it is 
	 * (indirectly) contained in a {@link MPatchModelBinding}.
	 */
	void createNote(NoteElement noteElement, String note);
	
	/**
	 * Returns a new change binding for the given change.
	 * 
	 * @param change
	 *            A change.
	 * @return a new change binding for the given change.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	ChangeBinding createChangeBindingForChange(IndepChange change);

	/**
	 * Returns an element binding for the given symbolic reference and model element.
	 * 
	 * @param elementReference
	 *            A symbolic reference.
	 * @param modelElement
	 *            A model element to which the symbolic reference resolved to.
	 * @return A binding which points to both, the symbolic reference and the resolved model element.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	ElementChangeBinding createElementChangeBinding(IElementReference elementReference, EObject modelElement);

	/**
	 * Return a sub model binding for added elements.
	 * 
	 * @param modelDescriptors
	 *            The model descriptors which are used to create the submodel contained in <b>element</b>.
	 * @param element
	 *            The sub model which was created by <b>modelDescriptor</b>.
	 * @param parentReference
	 *            The symbolic reference which resolved to the <b>parent</b>.
	 * @param parent
	 *            The parent to which <b>element</b> was added to.
	 * @param addedElementToSubModelBindingMap 
	 *            A map from added elements to their respective bindings; used for cross-reference restoring.
	 * @return A sub model binding which contains {@link ElementBinding} for each symbolic reference and 
	 *         {@link SubModelBinding} for each sub model descriptor.
	 */
	SubModelBinding createSubModelBinding(EMap<EObject, IModelDescriptor> modelDescriptors, EObject element, 
			IElementReference parentReference, EObject parent, Map<EObject, SubModelBinding> addedElementToSubModelBindingMap);
	
} //BindingFactory
