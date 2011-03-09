/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

/**
 * Specific content provider for symbolic reference resolution. The expected input is an {@link MPatchModel} and it
 * shows {@link ChangeGroup}s, {@link IndepChange}s, and all symbolic references which must be resolved for the
 * application of the {@link MPatchModel}. This includes corresponding elements as well as cross-references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ReferenceResolutionContentProvider extends AdapterFactoryContentProvider {

	/**
	 * Mapping all changes to their relevant references for symbolic reference resolution if the state before the change
	 * is found.
	 */
	private final static Map<EClass, List<EReference>> RELEVANT_REFERENCES_BEFORE = new HashMap<EClass, List<EReference>>();

	/**
	 * Mapping all changes to their relevant references for symbolic reference resolution if the state after the change
	 * is found.
	 */
	private final static Map<EClass, List<EReference>> RELEVANT_REFERENCES_AFTER = new HashMap<EClass, List<EReference>>();

	private final static EReference[] ADD_REM_ELEMENT_LIST = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			/*MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE,*/ };
	private final static EReference[] MOVE_ELEMENT_LIST_BEFORE = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT, };
	private final static EReference[] MOVE_ELEMENT_LIST_AFTER = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT, };
	private final static EReference[] ATTRIBUTE_LIST = new EReference[] { MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT, };
	private final static EReference[] ADD_REM_REFERENCE_LIST = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE, };
	private final static EReference[] UPDATE_REFERENCE_LIST_BEFORE = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE, };
	private final static EReference[] UPDATE_REFERENCE_LIST_AFTER = new EReference[] {
			MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT,
			MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE, };
	static {
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE,
				Arrays.asList(ADD_REM_ELEMENT_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE,
				Arrays.asList(ADD_REM_ELEMENT_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE,
				Arrays.asList(MOVE_ELEMENT_LIST_BEFORE));
		RELEVANT_REFERENCES_BEFORE
				.put(MPatchPackage.Literals.INDEP_ADD_ATTRIBUTE_CHANGE, Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_REMOVE_ATTRIBUTE_CHANGE,
				Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_UPDATE_ATTRIBUTE_CHANGE,
				Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE,
				Arrays.asList(ADD_REM_REFERENCE_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE,
				Arrays.asList(ADD_REM_REFERENCE_LIST));
		RELEVANT_REFERENCES_BEFORE.put(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE,
				Arrays.asList(UPDATE_REFERENCE_LIST_BEFORE));

		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE,
				Arrays.asList(ADD_REM_ELEMENT_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE,
				Arrays.asList(ADD_REM_ELEMENT_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE,
				Arrays.asList(MOVE_ELEMENT_LIST_AFTER));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_ADD_ATTRIBUTE_CHANGE, Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_REMOVE_ATTRIBUTE_CHANGE,
				Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_UPDATE_ATTRIBUTE_CHANGE,
				Arrays.asList(ATTRIBUTE_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE,
				Arrays.asList(ADD_REM_REFERENCE_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE,
				Arrays.asList(ADD_REM_REFERENCE_LIST));
		RELEVANT_REFERENCES_AFTER.put(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE,
				Arrays.asList(UPDATE_REFERENCE_LIST_AFTER));
	}

	/**
	 * Mapping.
	 */
	private final ResolvedSymbolicReferences mapping;

	/**
	 * Default constructor.
	 * 
	 * @see ReferenceResolutionContentProvider
	 */
	public ReferenceResolutionContentProvider(ResolvedSymbolicReferences mapping, AdapterFactory adapterFactory) {
		super(adapterFactory);
		this.mapping = mapping;
	}

	@Override
	public Object[] getChildren(Object object) {
		final Object[] result;

		if (object instanceof MPatchModel) {
			result = super.getChildren(object); // root element

		} else if (object instanceof ChangeGroup) {
			result = super.getChildren(object); // groups may only contain other changes

		} else if (object instanceof UnknownChange) {
			result = new Object[0]; // unknown changes are ... unknown ;-)

		} else if (object instanceof IndepChange) {
			final IndepChange change = (IndepChange) object;
			result = getReferencesForChange(change);

		} else {
			result = new Object[0]; // We don't support anything else...
		}

		// check for null objects!
		for (Object object2 : result) {
			if (object2 == null)
				throw new IllegalStateException("A child is null for object: " + object.toString() + " - Children: "
						+ Arrays.asList(result));
		}
		return result;
	}

	/**
	 * Return a context-sensitive collection of all relevant symbolic references, depending on the state of the change.
	 */
	private Object[] getReferencesForChange(IndepChange change) {
		final List<Object> resultList = new ArrayList<Object>();
		final ValidationResult state = mapping.getValidation().get(change);
		final List<EReference> relevantRefs;

		// if state is after the change, we need to show that. otherwise (also if change is invalid), show references
		// for STATE_BEFORE.
		if (ValidationResult.STATE_AFTER.equals(state))
			relevantRefs = RELEVANT_REFERENCES_AFTER.get(change.eClass());
		else
			relevantRefs = RELEVANT_REFERENCES_BEFORE.get(change.eClass());

		if (relevantRefs != null) {

			for (EReference eRef : relevantRefs) {
				final Object ref = change.eGet(eRef);
				// filter null references and internal references
				if (ref != null && !(ref instanceof ModelDescriptorReference))
					resultList.add(ref);
			}

			/*
			 * Special case: if the change is already applied and bound to existing model elements, we don't need any
			 * children!
			 */
			final boolean skipCrossRefs = change instanceof IndepAddRemElementChange
					&& ValidationResult.STATE_AFTER.equals(state);

			if (!skipCrossRefs) {
				// also: cross references which are not direct children of the change
				final Map<IElementReference, IndepChange> crossRefs = MPatchUtil.collectCrossReferences(Collections
						.singletonList(change));
				for (IElementReference symRef : crossRefs.keySet()) {
					// skip cross references which we already have and internal references
					if (!change.equals(symRef.eContainer()) && !resultList.contains(symRef)
							&& !(symRef instanceof ModelDescriptorReference))
						resultList.add(symRef);
				}
			}
		}
		return resultList.toArray();
	}

	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof IElementReference) {
			return false;
		} else
			return super.hasChildren(object);
	}

}