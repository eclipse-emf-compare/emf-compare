/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Validator functions for the resolution of an ({@link MPatchModel}) to a target model. The resolution is initially
 * created with {@link MPatchResolver} and stored in {@link ResolvedSymbolicReferences} .
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public final class MPatchValidator {

	/**
	 * Classification of validation result (
	 * {@link MPatchValidator#validateElementState(IndepChange, Map, boolean, boolean)}).<br><br>
	 * 
	 * {@link ValidationResult#SUCCESSFUL} - No problems at all.<br>
	 * {@link ValidationResult#REFERENCE} - Wrong number of resolved references.<br>
	 * {@link ValidationResult#INVALID_STATE} - The state before the change cannot be found.<br>
	 * {@link ValidationResult#UNKNOWN_CHANGE} - The change is unknown.
	 */
	public static enum ValidationResult {
		/** no problems at all. */
		SUCCESSFUL,
		/** wrong number of resolved references. */
		REFERENCE, 
		/** the state before the change cannot be found. */
		INVALID_STATE,
		/** the change is unknown. */
		UNKNOWN_CHANGE,
	}

	/**
	 * A mapping from {@link ValidationResult} to a human-readable String.
	 */
	public static final Map<ValidationResult, String> VALIDATION_RESULTS;

	static {
		final Map<ValidationResult, String> messageMap = new HashMap<ValidationResult, String>();
		messageMap.put(ValidationResult.SUCCESSFUL, "ok");
		messageMap.put(ValidationResult.REFERENCE, "check reference resolutions");
		messageMap.put(ValidationResult.INVALID_STATE, "invalid state in resolved model elements");
		messageMap.put(ValidationResult.UNKNOWN_CHANGE, "unknown change");
		VALIDATION_RESULTS = Collections.unmodifiableMap(messageMap);
	}

	/**
	 * Validate a resolution of symbolic references. It just calls
	 * {@link MPatchValidator#validateResolution(IElementReference, Collection)} for all symbolic references in
	 * {@link ResolvedSymbolicReferences#result()} and returns a collection of changes which did not resolve correctly.
	 * 
	 * Note that {@link UnknownChange}s are not allowed at all!
	 * 
	 * {@link ModelDescriptorReference}s are valid if the depending change is valid.
	 * 
	 * @param mapping
	 *            A mapping of resolved symbolic references to a model; typically created by {@link MPatchResolver} and
	 *            maybe modified by {@link IMPatchResolution}.
	 * @return A list of changes for which not all symbolic references were resolved successfully.
	 */
	public static List<IndepChange> validateResolutions(ResolvedSymbolicReferences mapping) {
		final List<IndepChange> result = new ArrayList<IndepChange>();

		// iterate over all IndepChanges
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {

			// filter unknown changes!
			if (change instanceof UnknownChange) {
				result.add(change);
				continue;
			}

			final Map<IElementReference, List<EObject>> symrefs = mapping.getResolutionByChange().get(change);

			// iterate over all symbolic references of the current change
			for (IElementReference ref : symrefs.keySet()) {
				if (ref instanceof ModelDescriptorReference) {

					// special case: modeldescriptorreferences cannot be checked now - but change with model descriptor
					// must be valid!
					final IndepChange otherChange = MPatchUtil.getChangeFor(ref);
					if (result.contains(otherChange))
						result.add(otherChange);
				} else {

					// for all others: check the resolution!
					if (!validateResolution(ref, symrefs.get(ref))) {
						if (result.contains(change))
							result.add(change);
						break;
					}
				}
			}

			// make sure all changes the current change depends on are also validated!
			if (!mapping.getResolutionByChange().keySet().containsAll(change.getDependsOn())) {
				if (result.contains(change))
					result.add(change);
			}
		}

		return result;
	}

	/**
	 * Check whether the given symbolic reference resolves the correct number of model elements. For most symbolic
	 * references, there must be exactly <i>one</i> element resolved. However, implementations may allow multiple
	 * elements to be resolved, stated by {@link IElementReference#getLowerBound()} and
	 * {@link IElementReference#getUpperBound()}.
	 * 
	 * {@link ModelDescriptorReference}s have a special role because they are internal references. They are valid as
	 * long as the depending change is valid.
	 * 
	 * @param ref
	 *            A symbolic reference.
	 * @param elements
	 *            The resolved elements for which the resolution is checked.
	 * @return <code>true</code>, if the cardinality matches the given elements, <code>false</code> otherwise.
	 */
	public static boolean validateResolution(IElementReference ref, Collection<EObject> elements) {
		if (ref instanceof ModelDescriptorReference)
			return true; // cannot be resolved for the time being
		final int lowerBound = ref.getLowerBound();
		final int upperBound = ref.getUpperBound();
		if (elements == null) {
			return false; // null is obviously not valid!
		} else if (lowerBound < 0) {
			return false; // lower bound must not be less than 0!
		} else if (upperBound == lowerBound) {
			return elements.size() == upperBound; // exact number of elements required
		} else if (upperBound < 0) {
			return lowerBound <= elements.size(); // at least lowerBounds
		} else if (upperBound > lowerBound) {
			return lowerBound <= elements.size() && upperBound >= elements.size(); // between lowerBound and upperBound
		} else {
			return false; // lower bound must not be higher than upper bound in all other cases!
		}
	}

	/**
	 * Check that all changes are resolved to the correct elements which satisfy the precondition of the change. For
	 * example for an attribute change, check that the actual attribute exists having the value before the change.
	 * 
	 * Note that {@link UnknownChange}s are not allowed at all!
	 * 
	 * @param mapping
	 *            The resolved mapping
	 * @param strict
	 *            If strict is <code>true</code>, then the property is checked for all resolved corresponding elements.
	 *            E.g. in case of an attribute change, the value of the state before must exist for all corresponding
	 *            elements. If <code>strict = false</code>, then just one elements must at least fulfill the
	 *            precondition.
	 * 
	 * @return A list of all changes which could not be verified successfully.
	 */
	public static List<IndepChange> validateElementStates(ResolvedSymbolicReferences mapping, boolean strict) {
		final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;
		final List<IndepChange> result = new ArrayList<IndepChange>();
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {

			// filter unknown changes!
			if (change instanceof UnknownChange) {
				result.add(change);
				continue;
			}

			// if invalid, add it to the result
			final ValidationResult state = validateElementState(change, mapping.getResolutionByChange().get(change),
					strict, forward);
			if (!ValidationResult.SUCCESSFUL.equals(state))
				result.add(change);
		}
		return result;
	}

	/**
	 * Check whether the state before the change (with respect to the direction for which the symbolic references were
	 * resolved) can be found in the model.
	 * 
	 * @param change
	 *            The change which is going to be checked.
	 * @param mapping
	 *            The resolved symbolic references for this change.
	 * @param strict
	 *            If strict is <code>true</code>, then the property is checked for all resolved corresponding elements.
	 *            E.g. in case of an attribute change, the value of the state before must exist for all corresponding
	 *            elements. If <code>strict = false</code>, then just one elements must at least fulfill the
	 *            precondition.
	 * @param forward
	 *            Indicates the direction of resolution and thus determines the state which is going to be checked.
	 * @return Please check {@link ValidationResult} for details.
	 */
	public static ValidationResult validateElementState(IndepChange change,
			final Map<IElementReference, List<EObject>> mapping, boolean strict, boolean forward) {

		// unknown change?
		if (change instanceof UnknownChange) {
			return ValidationResult.UNKNOWN_CHANGE;
		}

		// we need at least one corresponding element!
		if (mapping == null || mapping.get(change.getCorrespondingElement()).size() == 0)
			return ValidationResult.REFERENCE;

		// check sure that all symbolic references are resolved correctly before checking state
		for (IElementReference symref : mapping.keySet()) {
			if (!validateResolution(symref, mapping.get(symref)))
				return ValidationResult.REFERENCE;
		}

		if (forward) { // resolve unchanged state
			if (change instanceof IndepAddElementChange) {
				return validateAddElementState((IndepAddElementChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveElementChange) {
				return validateRemoveElementState((IndepRemoveElementChange) change, mapping, strict);
			} else if (change instanceof IndepMoveElementChange) {
				return validateMoveElementState((IndepMoveElementChange) change, mapping, strict, true);
			} else if (change instanceof IndepAddAttributeChange) {
				return validateAddAttributeState((IndepAddAttributeChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveAttributeChange) {
				return validateRemoveAttributeState((IndepRemoveAttributeChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateAttributeChange) {
				return validateUpdateAttributeState((IndepUpdateAttributeChange) change, mapping, strict, true);
			} else if (change instanceof IndepAddReferenceChange) {
				return validateAddReferenceState((IndepAddReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveReferenceChange) {
				return validateRemoveReferenceState((IndepRemoveReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateReferenceChange) {
				return validateUpdateReferenceState((IndepUpdateReferenceChange) change, mapping, strict, true);
			} else
				throw new IllegalArgumentException("Unknown change type: " + change);

		} else { // resolve changed (backward)
			if (change instanceof IndepAddElementChange) {
				return validateRemoveElementState((IndepAddElementChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveElementChange) {
				return validateAddElementState((IndepRemoveElementChange) change, mapping, strict);
			} else if (change instanceof IndepMoveElementChange) {
				return validateMoveElementState((IndepMoveElementChange) change, mapping, strict, false);
			} else if (change instanceof IndepAddAttributeChange) {
				return validateRemoveAttributeState((IndepAddAttributeChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveAttributeChange) {
				return validateAddAttributeState((IndepRemoveAttributeChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateAttributeChange) {
				return validateUpdateAttributeState((IndepUpdateAttributeChange) change, mapping, strict, false);
			} else if (change instanceof IndepAddReferenceChange) {
				return validateRemoveReferenceState((IndepAddReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveReferenceChange) {
				return validateAddReferenceState((IndepRemoveReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateReferenceChange) {
				return validateUpdateReferenceState((IndepUpdateReferenceChange) change, mapping, strict, false);
			} else
				throw new IllegalArgumentException("Unknown change type: " + change);
		}
	}

	/**
	 * Check that the element refers to the object before the change for all (<code>strict = true</code>) or for at
	 * least one (<code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateUpdateReferenceState(IndepUpdateReferenceChange change,
			Map<IElementReference, List<EObject>> map, boolean strict, boolean forward) {

		// we must only have one target element!
		final IElementReference newSymRef = forward ? change.getOldReference() : change.getNewReference();
		final EObject targetElement;
		if (newSymRef != null) {
			final Collection<EObject> targetElements = map.get(newSymRef);
			if (targetElements.size() > 1)
				return ValidationResult.REFERENCE;
			targetElement = targetElements.iterator().next();
		} else
			targetElement = null;

		for (EObject element : map.get(change.getCorrespondingElement())) {
			final Object currentTarget = element.eGet(change.getReference());

			// we need this distinction to prevent nullpointer exceptions in the comparison
			if (targetElement == null) {
				if (strict && currentTarget != null) {
					return ValidationResult.INVALID_STATE; // we found one that does not have the expected ref
				} else if (!strict && currentTarget == null) {
					return ValidationResult.SUCCESSFUL; // we found one with the expected ref, so we are done :)
				}
			} else {
				if (strict && !targetElement.equals(currentTarget)) {
					return ValidationResult.INVALID_STATE; // we found one that does not have the expected ref
				} else if (!strict && targetElement.equals(currentTarget)) {
					return ValidationResult.SUCCESSFUL; // we found one with the expected ref, so we are done :)
				}
			}
		}
		// strict ? (all refs as expected) : (no ref as expected)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Checks that at least one referred element exist for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateRemoveReferenceState(IndepAddRemReferenceChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {

		// check if there are at all elements to remove
		final Collection<EObject> targetElements = map.get(change.getChangedReference());
		if (targetElements.size() == 0)
			return ValidationResult.REFERENCE;

		for (EObject element : map.get(change.getCorrespondingElement())) {

			// which elements do exist here?
			@SuppressWarnings("unchecked")
			final EList<Object> rawList = (EList) element.eGet(change.getReference());
			final List<Object> list = new ArrayList<Object>(rawList);
			list.retainAll(targetElements);

			if (strict && list.isEmpty()) {
				return ValidationResult.INVALID_STATE; // no element exists which can be removed!
			} else if (!strict && !list.isEmpty()) {
				return ValidationResult.SUCCESSFUL; // there are elements to be removed -> were done here :)
			}
		}

		// strict ? (ref(s) can be removed everywhere) : (none elements to be removed at all)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Checks that the referred element(s) do(es) not already exist, if it is a unique reference, for all (
	 * <code>strict = true</code>) or for at least one (<code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateAddReferenceState(IndepAddRemReferenceChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {

		// Note: maybe these elements do not yet exist ;-)
		// So we cannot check anything here :-/
		return ValidationResult.SUCCESSFUL;

		// check if there are at all elements to add
		// final Collection<EObject> targetElements = map.get(change.getChangedReference());
		// if (targetElements == null || targetElements.size() == 0)
		// return false;
		//
		// // for non-unique collections, there is no problem at all!
		// if (!change.getReference().isUnique())
		// return true;
		//
		// for (EObject element : map.get(change.getCorrespondingElement())) {
		//
		// // which elements do already exist?
		// @SuppressWarnings("unchecked")
		// final EList<Object> rawList = (EList) element.eGet(change.getReference());
		// final List<Object> list = new ArrayList<Object>(rawList);
		// list.retainAll(targetElements);
		//
		// if (strict && list.size() == targetElements.size()) {
		// return false; // there are no elements which can be added! return false..
		// } else if (!strict && list.size() < targetElements.size()) {
		// return true; // there are elements that can be added, so we are done here
		// }
		// }
		// return strict; // strict ? (all target elements can be added) : (no element can be added any more)
	}

	/**
	 * Check that the attribute has the value before the change for all (<code>strict = true</code>) or for at least one
	 * (<code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateUpdateAttributeState(IndepUpdateAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict, boolean forward) {
		// get value depending on the direction
		final Object value = forward ? change.getOldValue() : change.getNewValue();
		for (EObject element : map.get(change.getCorrespondingElement())) {
			final Object actualValue = element.eGet(change.getChangedAttribute());

			// we need this distinction to prevent nullpointer exceptions in the comparison
			if (value == null) {
				if (strict && actualValue != null) {
					return ValidationResult.INVALID_STATE; // we found one that does not have the expected value
				} else if (!strict && actualValue == null) {
					return ValidationResult.SUCCESSFUL; // we found one with the expected value, so we are done :)
				}
			} else {
				if (strict && !value.equals(actualValue)) {
					return ValidationResult.INVALID_STATE; // we found one that does not have the expected value
				} else if (!strict && value.equals(actualValue)) {
					return ValidationResult.SUCCESSFUL; // we found one with the expected value, so we are done :)
				}
			}
		}
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Check if the attribute value does exist for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateRemoveAttributeState(IndepAddRemAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {

		for (EObject element : map.get(change.getCorrespondingElement())) {
			@SuppressWarnings("unchecked")
			final List<Object> values = (List) element.eGet(change.getChangedAttribute());
			if (strict && !values.contains(change.getValue())) {
				return ValidationResult.INVALID_STATE; // value does not exist and thus cannot be removed!
			} else if (!strict && values.contains(change.getValue())) {
				return ValidationResult.SUCCESSFUL; // we found a value which can be removed, so we are done
			}
		}
		// strict ? (value is contained somewhere) : (value is not contained anywhere)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Check if the attribute value does not already exists for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateAddAttributeState(IndepAddRemAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {
		// if the values are not unique, then there is no problem at all
		if (!change.getChangedAttribute().isUnique())
			return ValidationResult.SUCCESSFUL;

		for (EObject element : map.get(change.getCorrespondingElement())) {
			@SuppressWarnings("unchecked")
			final List<Object> values = (List) element.eGet(change.getChangedAttribute());
			if (strict && values.contains(change.getValue())) {
				return ValidationResult.INVALID_STATE; // value does already exist and cannot be added any more!
			} else if (!strict && !values.contains(change.getValue())) {
				return ValidationResult.SUCCESSFUL; // value does not exist yet, so it can safely be added here :)
			}
		}
		// strict ? (value is not yet contained anywhere) : (value is already contained everywhere)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Check if the element to move exists in the source element and that it is possible to add it to the target element
	 * for all (<code>strict = true</code>) or for at least one (<code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateMoveElementState(IndepMoveElementChange change,
			Map<IElementReference, List<EObject>> map, boolean strict, boolean forward) {

		// if we have many corresponding elements, check that the container can hold many of them!
		final EReference oldContainment = forward ? change.getOldContainment() : change.getNewContainment();
		final EReference newContainment = forward ? change.getNewContainment() : change.getOldContainment();
		final Collection<EObject> correspondingElements = map.get(change.getCorrespondingElement());
		if (!newContainment.isMany() && correspondingElements.size() > 1)
			return ValidationResult.REFERENCE;

		// get the old and the new parents
		// Note: the new parent might not yet exist! ;-)
		final IElementReference oldParentRef = forward ? change.getOldParent() : change.getNewParent();
		// final IElementReference newParentRef = forward ? change.getNewParent() : change.getOldParent();
		final Collection<EObject> oldParents = map.get(oldParentRef);
		// final Collection<EObject> newParents = map.get(newParentRef);
		if (oldParents.size() != 1/* || newParents.size() != 1 */)
			return ValidationResult.REFERENCE;
		final EObject oldParent = oldParents.iterator().next();
		// final EObject newParent = newParents.iterator().next();

		for (final EObject toMove : correspondingElements) {
			boolean canMove = oldParent.equals(toMove.eContainer())
					&& oldContainment.equals(toMove.eContainmentFeature())
			/* && (newContainment.isMany() || newParent.eGet(newContainment) == null) */;
			if (strict && !canMove) {
				return ValidationResult.INVALID_STATE; // if strict and it cannot move, then we return false
			} else if (!strict && canMove) {
				return ValidationResult.SUCCESSFUL; // if strict and it can move, we are done here
			}
		}
		// strict ? (all can be moved) : (none can be moved)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Check if the element to remove exists in at least one (<code>strict = false</code>) or in all (
	 * <code>strict = true</code>) corresponding elements.
	 */
	protected static ValidationResult validateRemoveElementState(IndepAddRemElementChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {
		for (final EObject parent : map.get(change.getCorrespondingElement())) {
			if (change.getContainment().isMany()) {

				// get resolved list for that symbolic reference and the actual children and calculate intersection
				@SuppressWarnings("unchecked")
				final EList<Object> rawList = (EList) parent.eGet(change.getContainment());
				final List<Object> toDelete = new ArrayList<Object>(rawList);
				toDelete.retainAll(map.get(change.getSubModelReference()));

				if (strict && toDelete.isEmpty()) {
					return ValidationResult.INVALID_STATE; // if strict and the intersection is empty, there is nothing
					// which can be deleted
				} else if (!strict && !toDelete.isEmpty()) {
					return ValidationResult.SUCCESSFUL; // if non-strict and there is something to delete, we are done
					// here
				}
			} else {
				final Object toDelete = parent.eGet(change.getContainment());
				if (strict && (toDelete == null || !map.get(change.getSubModelReference()).contains(toDelete))) {
					return ValidationResult.INVALID_STATE; // if strict, then there is nothing to delete
				} else if (!strict && toDelete != null && map.get(change.getSubModelReference()).contains(toDelete)) {
					return ValidationResult.SUCCESSFUL; // if non-strict and there is something to delete, we are done
					// here
				}
			}
		}
		// strict ? (all exist) : (none exists)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * If containment can hold many elements, this always evaluates to <code>true</code>. If not, then it depends on
	 * <code>strict</code>: if <code>true</code>, then all corresponding elements must be able to add an element; if
	 * <code>false</code>, then at least one corresponding element must be able to add an element.
	 */
	protected static ValidationResult validateAddElementState(IndepAddRemElementChange change,
			Map<IElementReference, List<EObject>> mapping, boolean strict) {
		if (change.getContainment().isMany())
			return ValidationResult.SUCCESSFUL; // element can always be added
		for (final EObject parent : mapping.get(change.getCorrespondingElement())) {
			if (strict && parent.eGet(change.getContainment()) != null) {
				return ValidationResult.INVALID_STATE;
			} else if (!strict && parent.eGet(change.getContainment()) == null) {
				return ValidationResult.SUCCESSFUL;
			}
		}
		// strict ? (all are null) : (none is null)
		return strict ? ValidationResult.SUCCESSFUL : ValidationResult.INVALID_STATE;
	}

	/**
	 * Get all {@link EReference}s which need to be resolved for the particular change.
	 * 
	 * @param eClass
	 *            The eclass of the change, e.g. {@link IndepAddElementChange}, {@link IndepUpdateReferenceChange}, etc.
	 * @param direction
	 *            Return only relevant references; e.g. if <code>direction</code> =
	 *            {@link ResolvedSymbolicReferences#RESOLVE_UNCHANGED}, then for {@link IndepUpdateReferenceChange} only
	 *            {@link IndepUpdateReferenceChange#setOldReference(IElementReference)} will be returned, not
	 *            {@link IndepUpdateReferenceChange#setNewReference(IElementReference)}.
	 * @return A collection of references which are relevant for the given {@link EClass}.
	 */
	public static Collection<EReference> getImportantReferencesFor(EClass eClass, int direction) {
		final Collection<EReference> refs = new ArrayList<EReference>();
		if (equalEClasses(eClass, MPatchPackage.Literals.CHANGE_GROUP)) {
			// we are not interested in groups here!
		} else if (equalEClasses(eClass, MPatchPackage.Literals.UNKNOWN_CHANGE)) {
			// also skip unknown changes!
		} else {

			/*
			 * I decided to resolve all reference! This might slow down the performance a bit, but at least we got as
			 * many references resolved as available.
			 */

			refs.add(MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT);
			if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE)) {
				if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE);
				}
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE)) {
				if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE);
				}
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE)) {
//				if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT);
//				} else if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT);
//				}
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE)) {
				if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE);
				}
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE)) {
				if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE);
				}
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE)) {
//				if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE);
//				} else if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
					refs.add(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE);
//				}
			}
		}
		return refs;
	}

	/**
	 * Check whether two eclasses are equal.
	 * 
	 * @param eClass1 
	 * 			An eclass.
	 * @param eClass2 
	 * 			Another eclass.
	 * @return <code>true</code>, if their classifier IDs equal; <code>false</code> otherwise.
	 */
	protected static boolean equalEClasses(EClass eClass1, EClass eClass2) {
		// return eClass1.getInstanceClassName().equals(eClass2.getInstanceClassName());
		return eClass1.getClassifierID() == eClass2.getClassifierID();
	}

}
