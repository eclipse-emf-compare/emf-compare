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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
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
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
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
	 * @return A list of changes for which not all symbolic references were resolved successfully or the state before or
	 *         after the change could not be found.
	 */
	public static List<IndepChange> validateResolutions(ResolvedSymbolicReferences mapping) {
		final Set<IndepChange> result = new HashSet<IndepChange>();

		// update validation states!
		validateElementStates(mapping, false);

		// iterate over all selected IndepChanges
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {

			// check state first
			final ValidationResult valid = mapping.getValidation().get(change);
			if (!ValidationResult.STATE_BEFORE.equals(valid) && !ValidationResult.STATE_AFTER.equals(valid)) {
				result.add(change);
				continue;
			}

			// make sure all changes the current change depends on are also validated!
			if (!mapping.getResolutionByChange().keySet().containsAll(change.getDependsOn())) {
				result.add(change);
			}
		}

		// iterate over all selected IndepChanges again to find violations of model descriptor references
		for (IndepChange change : mapping.getResolutionByChange().keySet()) {
			if (result.contains(change))
				continue; // already invalid!

			// iterate over all symbolic references of the current change
			final Map<IElementReference, List<EObject>> symrefs = mapping.getResolutionByChange().get(change);
			for (IElementReference ref : symrefs.keySet()) {
				if (ref instanceof ModelDescriptorReference) {

					// special case: modeldescriptorreferences cannot be checked now - but change with model descriptor
					// must be valid!
					final IndepChange otherChange = (IndepChange) ExtEcoreUtils.getContainerOfType(
							((ModelDescriptorReference) ref).getResolvesTo(),
							MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE);
					if (result.contains(otherChange))
						result.add(change);
				}
			}
		}

		return new ArrayList<IndepChange>(result);
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
	 * Check that all changes are resolved to the correct elements which represent either the state before or after the
	 * change. For example for an attribute change, check that the actual attribute exists having the value either
	 * before of after the change.
	 * 
	 * The result is stored in {@link ResolvedSymbolicReferences#getValidation()}.
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
	 */
	static void validateElementStates(ResolvedSymbolicReferences mapping, boolean strict) {
		final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;

		// clear previous validation states
		mapping.getValidation().clear();
		
		// The validation requires the changes to be ordered!
		final List<IndepChange> orderedChanges = orderChanges(mapping.getResolutionByChange().keySet(), !forward);

		for (IndepChange change : orderedChanges) {

			// if invalid, add it to the result
			final ValidationResult state = validateElementState(change, mapping, strict, forward);
			mapping.getValidation().put(change, state);
		}
	}

	/**
	 * Check whether the state before or after the change (with respect to the direction for which the symbolic
	 * references were resolved) can be found in the model.
	 * 
	 * Note that {@link UnknownChange}s are not allowed at all!
	 * 
	 * @param change
	 *            The change which is going to be checked.
	 * @param changeMapping
	 *            The resolved symbolic references for all changes.
	 * @param strict
	 *            If strict is <code>true</code>, then the property is checked for all resolved corresponding elements.
	 *            E.g. in case of an attribute change, the value of the state before must exist for all corresponding
	 *            elements. If <code>strict = false</code>, then just one elements must at least fulfill the
	 *            precondition.
	 * @param forward
	 *            Indicates the direction of resolution and thus determines the state which is going to be checked.
	 * @return Please see {@link ValidationResult} for details.
	 */
	static ValidationResult validateElementState(IndepChange change, final ResolvedSymbolicReferences mapping,
			boolean strict, boolean forward) {
		final Map<IElementReference, List<EObject>> changeMapping = mapping.getResolutionByChange().get(change);

		// unknown change?
		if (change instanceof UnknownChange) {
			return ValidationResult.UNKNOWN_CHANGE;
		}

		// we need at least one corresponding element!
		if (changeMapping == null || changeMapping.get(change.getCorrespondingElement()).size() == 0)
			return ValidationResult.REFERENCE;

		// check sure that all symbolic references are resolved correctly before checking state
		for (IElementReference symref : changeMapping.keySet()) {
			/*
			 * The self reference of model descriptors is an exception here. THe change might even be valid if it is not
			 * resolved!
			 */
			if (symref.eContainer() instanceof IModelDescriptor)
				continue;

			// check all other regularly.
			if (!validateResolution(symref, changeMapping.get(symref)))
				return ValidationResult.REFERENCE;
		}

		if (forward) { // resolve unchanged state
			if (change instanceof IndepAddElementChange) {
				return validateAddElementState((IndepAddElementChange) change, changeMapping, strict);
			} else if (change instanceof IndepRemoveElementChange) {
				return validateRemoveElementState((IndepRemoveElementChange) change, changeMapping, strict);
			} else if (change instanceof IndepMoveElementChange) {
				return validateMoveElementState((IndepMoveElementChange) change, changeMapping, strict, true);
			} else if (change instanceof IndepAddAttributeChange) {
				return validateAddAttributeState((IndepAddAttributeChange) change, changeMapping, strict);
			} else if (change instanceof IndepRemoveAttributeChange) {
				return validateRemoveAttributeState((IndepRemoveAttributeChange) change, changeMapping, strict);
			} else if (change instanceof IndepUpdateAttributeChange) {
				return validateUpdateAttributeState((IndepUpdateAttributeChange) change, changeMapping, strict, true);
			} else if (change instanceof IndepAddReferenceChange) {
				return validateAddReferenceState((IndepAddReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveReferenceChange) {
				return validateRemoveReferenceState((IndepRemoveReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateReferenceChange) {
				return validateUpdateReferenceState((IndepUpdateReferenceChange) change, mapping, strict, true);
			} else
				throw new IllegalArgumentException("Unknown change type: " + change.eClass().getName());

		} else { // resolve changed (backward)
			if (change instanceof IndepAddElementChange) {
				return validateRemoveElementState((IndepAddElementChange) change, changeMapping, strict);
			} else if (change instanceof IndepRemoveElementChange) {
				return validateAddElementState((IndepRemoveElementChange) change, changeMapping, strict);
			} else if (change instanceof IndepMoveElementChange) {
				return validateMoveElementState((IndepMoveElementChange) change, changeMapping, strict, false);
			} else if (change instanceof IndepAddAttributeChange) {
				return validateRemoveAttributeState((IndepAddAttributeChange) change, changeMapping, strict);
			} else if (change instanceof IndepRemoveAttributeChange) {
				return validateAddAttributeState((IndepRemoveAttributeChange) change, changeMapping, strict);
			} else if (change instanceof IndepUpdateAttributeChange) {
				return validateUpdateAttributeState((IndepUpdateAttributeChange) change, changeMapping, strict, false);
			} else if (change instanceof IndepAddReferenceChange) {
				return validateRemoveReferenceState((IndepAddReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepRemoveReferenceChange) {
				return validateAddReferenceState((IndepRemoveReferenceChange) change, mapping, strict);
			} else if (change instanceof IndepUpdateReferenceChange) {
				return validateUpdateReferenceState((IndepUpdateReferenceChange) change, mapping, strict, false);
			} else
				throw new IllegalArgumentException("Unknown change type: " + change.eClass().getName());
		}
	}

	/**
	 * Check that the element refers to the object before or after the change for all (<code>strict = true</code>) or
	 * for at least one (<code>strict = false</code>) corresponding element.
	 * 
	 * <ol>
	 * <li>Determine source and target symbolic references depending on direction.
	 * <li>Get source and target resolutions (might result in {@link ValidationResult#REFERENCE}.
	 * <li>Check for all corresponding elements:<br>
	 * Is the source or the target element set? (might result in {@link ValidationResult#STATE_INVALID}.
	 * <li>Return {@link ValidationResult#STATE_BEFORE} or {@link ValidationResult#STATE_AFTER}, depending on
	 * <code>strict</code>.
	 * </ol>
	 */
	protected static ValidationResult validateUpdateReferenceState(IndepUpdateReferenceChange change,
			ResolvedSymbolicReferences mapping, boolean strict, boolean forward) {

		final Map<IElementReference, List<EObject>> changeMapping = mapping.getResolutionByChange().get(change);

		// get symrefs for source and target
		final IElementReference beforeSymRef = forward ? change.getOldReference() : change.getNewReference();
		final IElementReference afterSymRef = !forward ? change.getOldReference() : change.getNewReference();

		// check symrefs
		final SymRefCheck beforeCheck = new SymRefCheck(beforeSymRef, changeMapping, change.getReference().getEType(),
				1);
		final SymRefCheck afterCheck = new SymRefCheck(afterSymRef, changeMapping, change.getReference().getEType(), 1);
		if (beforeCheck.validationResult != null)
			return beforeCheck.validationResult;
		if (afterCheck.validationResult != null)
			return afterCheck.validationResult;
		final EObject beforeElement = beforeCheck.internal || beforeCheck.symRef == null ? null : beforeCheck.elements
				.get(0);
		final EObject afterElement = afterCheck.internal || afterCheck.symRef == null ? null : afterCheck.elements
				.get(0);

		// collect status of all corresponding elements
		final ResultAccumulator result = new ResultAccumulator();
		for (EObject element : changeMapping.get(change.getCorrespondingElement())) {
			final Object currentElement = element.eGet(change.getReference());

			if (beforeCheck.internal) {
				final IndepChange otherChange = (IndepChange) ExtEcoreUtils.getContainerOfType(
						((ModelDescriptorReference) beforeCheck.symRef).getResolvesTo(),
						MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE);

				// now we need to check whether the transitive resolution equals current element
				if (currentElement == null) {
					// if the actual element is empty, it is only a valid state (after), if the (deletion) change of the
					// model descriptor is also state after.
					if (ValidationResult.STATE_AFTER.equals(mapping.getValidation().get(otherChange))) {
						result.after = true;
					} else {
						result.invalid = true; // there must be an element but it isn't!
					}
				} else {
					// We need to check whether currentElement is described by the model descriptor of this internal
					// reference!
					if (ValidationResult.STATE_BEFORE.equals(mapping.getValidation().get(otherChange))) {
						/*
						 * Strictly spoken, we must make sure that currentElement is the one that will be deleted by the
						 * internal model descriptor. But since the deletion does not include a check whether all
						 * sub-elements are precisely as expected, we cannot ensure that. So lets just assume that the
						 * current element is the correct one... SHouldn't be a big deal since it's just the
						 * validation...
						 */
						result.before = true;
					}
				}
			} else if (currentElement == null && beforeElement == null) {
				result.before = true;
			} else if (currentElement != null && currentElement.equals(beforeElement)) {
				result.before = true;
			}
			if (afterCheck.internal) {
				/*
				 * If afterCheck.internal, then it's only state_after if the change of the internal reference is also
				 * state_after!
				 */
				final IndepChange otherChange = (IndepChange) ExtEcoreUtils.getContainerOfType(
						((ModelDescriptorReference) afterCheck.symRef).getResolvesTo(),
						MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE);
				final ValidationResult otherState = mapping.getValidation().get(otherChange);
				if (ValidationResult.STATE_AFTER.equals(otherState)) {
					result.after |= currentElement != null;
				} else {
					result.invalid = true;
				}
			} else {
				if (currentElement == null && afterElement == null) {
					result.after = true;
				} else if (currentElement != null && currentElement.equals(afterElement)) {
					result.after = true;
				}
			}
		}
		return result.accumulate(strict);
	}

	/**
	 * Checks that at least one referred element exist for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 * 
	 * <ol>
	 * <li>Get symbolic reference.
	 * <li>Get resolution (might result in {@link ValidationResult#REFERENCE}.
	 * <li>Check type (might result in {@link ValidationResult#STATE_INVALID}.
	 * <li>Check for all corresponding elements:<br>
	 * Is the element set? (might result in {@link ValidationResult#STATE_INVALID}.
	 * <li>Return {@link ValidationResult#STATE_BEFORE} or {@link ValidationResult#STATE_AFTER}, depending on
	 * <code>strict</code>.
	 * </ol>
	 */
	protected static ValidationResult validateRemoveReferenceState(IndepAddRemReferenceChange change,
			ResolvedSymbolicReferences mapping, boolean strict) {

		final Map<IElementReference, List<EObject>> changeMapping = mapping.getResolutionByChange().get(change);

		final SymRefCheck symRefCheck = new SymRefCheck(change.getChangedReference(), changeMapping, change
				.getReference().getEType(), change.getChangedReference().getUpperBound());

		// check validation result
		if (symRefCheck.validationResult != null)
			return symRefCheck.validationResult;

		final ResultAccumulator result = new ResultAccumulator();
		for (EObject element : changeMapping.get(change.getCorrespondingElement())) {

			// which elements are currently referenced
			@SuppressWarnings("unchecked")
			final EList<EObject> rawList = (EList<EObject>) element.eGet(change.getReference());
			final List<EObject> list = new ArrayList<EObject>(rawList);

			if (symRefCheck.internal) {
				final IndepChange otherChange = (IndepChange) ExtEcoreUtils.getContainerOfType(
						((ModelDescriptorReference) symRefCheck.symRef).getResolvesTo(),
						MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE);
				if (rawList.isEmpty()) {
					// if the actual list is empty, it is only a valid state (after), if the change of the model
					// descriptor is also state after.
					if (ValidationResult.STATE_AFTER.equals(mapping.getValidation().get(otherChange))) {
						result.after = true;
					} else {
						result.invalid = true; // there must be an element but it isn't!
					}
				} else {
					/*
					 * Like in validateUpdateReferenceState, we must check whether the model descriptor of the internal
					 * reference describes any of the current list of elements. However, this is not easy, imagine the
					 * model descriptor is part of a sub-model! They are (on purpose) not resolved. So we simply assume
					 * that it is correct. Should work because it's just the validation.
					 */
					if (ValidationResult.STATE_BEFORE.equals(mapping.getValidation().get(otherChange))) {
						result.before = true;
					} else {
						result.invalid = true;
					}
				}
			} else { // no internal reference!

				list.retainAll(symRefCheck.elements);

				// there must be elements to remove
				result.before |= !list.isEmpty();
				// there are already elements removed
				result.after |= list.size() < symRefCheck.elements.size();
			}
		}
		return result.accumulate(strict);
	}

	/**
	 * Checks that the referred element(s) do(es) not already exist, if it is a unique reference, for all (
	 * <code>strict = true</code>) or for at least one (<code>strict = false</code>) corresponding element.
	 * 
	 * Almost the same as {@link MPatchValidator#validateRemoveElementState(IndepAddRemElementChange, Map, boolean)},
	 * but the check is reversed.
	 */
	protected static ValidationResult validateAddReferenceState(IndepAddRemReferenceChange change,
			ResolvedSymbolicReferences mapping, boolean strict) {

		final Map<IElementReference, List<EObject>> changeMapping = mapping.getResolutionByChange().get(change);

		final SymRefCheck symRefCheck = new SymRefCheck(change.getChangedReference(), changeMapping, change
				.getReference().getEType(), change.getChangedReference().getUpperBound());

		// check validation result
		if (symRefCheck.validationResult != null)
			return symRefCheck.validationResult;

		final ResultAccumulator result = new ResultAccumulator();
		if (symRefCheck.internal) {
			/*
			 * If it's an internal reference, it can always be added. However, if the internal model descriptor exactly
			 * resolves to one of the elements, we found the state_after ;-)
			 */
			final IndepChange otherChange = (IndepChange) ExtEcoreUtils.getContainerOfType(
					((ModelDescriptorReference) symRefCheck.symRef).getResolvesTo(),
					MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE);
			final ValidationResult otherState = mapping.getValidation().get(otherChange);
			if (ValidationResult.STATE_BEFORE.equals(otherState)) {
				result.before = true;
			} else if (ValidationResult.STATE_INVALID.equals(otherState)) {
				result.invalid = true;
			} else if (ValidationResult.STATE_AFTER.equals(otherState)) {
				// since the addition is checked thoroughly (unlike RemoveReferenceState), the actual element must be
				// resolved!
				final Map<IElementReference, List<EObject>> otherMapping = mapping.getResolutionByChange().get(
						otherChange);
				final IElementReference selfReference = ((IndepAddRemElementChange) otherChange).getSubModelReference();
				final List<EObject> addedElements = otherMapping.get(selfReference); // already added (roots of)
																						// sub-models
				final List<? extends EObject> allAddedElements = ExtEcoreUtils.flattenEObjects(addedElements);
				symRefCheck.elements.addAll(allAddedElements);
			}
		}
		for (EObject element : changeMapping.get(change.getCorrespondingElement())) {
			// which elements are currently referenced
			@SuppressWarnings("unchecked")
			final EList<EObject> rawList = (EList<EObject>) element.eGet(change.getReference());
			final List<EObject> list = new ArrayList<EObject>(rawList);
			list.retainAll(symRefCheck.elements);

			if (!symRefCheck.internal) {
				// there must be elements that are not yet added (does not apply for internal reference)
				result.before |= list.size() < symRefCheck.elements.size();
			}
			// there are already elements added
			result.after |= !list.isEmpty();
		}
		return result.accumulate(strict);
	}

	/**
	 * Check that the attribute has the value before the change for all (<code>strict = true</code>) or for at least one
	 * (<code>strict = false</code>) corresponding element.
	 */
	protected static ValidationResult validateUpdateAttributeState(IndepUpdateAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict, boolean forward) {

		// get value depending on the direction
		final Object oldValue = forward ? change.getOldValue() : change.getNewValue();
		final Object newValue = !forward ? change.getOldValue() : change.getNewValue();

		// collect status of all corresponding elements
		final ResultAccumulator result = new ResultAccumulator();
		for (EObject element : map.get(change.getCorrespondingElement())) {
			final Object currentValue = element.eGet(change.getChangedAttribute());
			if (currentValue == null && oldValue == null) {
				result.before = true;
			} else if (currentValue != null && currentValue.equals(oldValue)) {
				result.before = true;
			} else if (currentValue == null && newValue == null) {
				result.after = true;
			} else if (currentValue != null && currentValue.equals(newValue)) {
				result.after = true;
			} else {
				result.invalid = true;
			}
		}
		return result.accumulate(strict);
	}

	/**
	 * Check if the attribute value does exist for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 * 
	 * This either returns {@link ValidationResult#STATE_BEFORE} or {@link ValidationResult#STATE_AFTER}, depending on
	 * whether the value of interest does or does not exist in the current list.
	 */
	protected static ValidationResult validateRemoveAttributeState(IndepAddRemAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {
		// get value to remove
		final Object value = change.getValue();

		final ResultAccumulator result = new ResultAccumulator();
		for (EObject element : map.get(change.getCorrespondingElement())) {
			// get current value
			@SuppressWarnings("unchecked")
			final EList<Object> currentValues = (EList<Object>) element.eGet(change.getChangedAttribute());

			// the value must not yet exist
			result.before |= currentValues.contains(value);
			// the value does exist
			result.after |= !currentValues.contains(value);
		}
		return result.accumulate(strict);
	}

	/**
	 * Check if the attribute value does not already exists for all (<code>strict = true</code>) or for at least one (
	 * <code>strict = false</code>) corresponding element.
	 * 
	 * Almost the same as {@link MPatchValidator#validateRemoveAttributeState(IndepAddRemAttributeChange, Map, boolean)}
	 * , but the check is reversed.
	 */
	protected static ValidationResult validateAddAttributeState(IndepAddRemAttributeChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {
		// get value to add
		final Object value = change.getValue();

		final ResultAccumulator result = new ResultAccumulator();
		for (EObject element : map.get(change.getCorrespondingElement())) {
			// get current value
			@SuppressWarnings("unchecked")
			final EList<Object> currentValues = (EList<Object>) element.eGet(change.getChangedAttribute());

			// the value does exist
			result.before |= !currentValues.contains(value);
			// the value must not exist
			result.after |= currentValues.contains(value);
		}
		return result.accumulate(strict);
	}

	/**
	 * Check if the element to move exists in the source element and that it is possible to add it to the target element
	 * for all (<code>strict = true</code>) or for at least one (<code>strict = false</code>) corresponding element.
	 * 
	 * <ol>
	 * <li>Determine old and new parents depending on direction.
	 * <li>Check for all corresponding elements:<br>
	 * Is the old or the new parent or neither of them set? (might result in {@link ValidationResult#STATE_INVALID}.
	 * <li>Return {@link ValidationResult#STATE_BEFORE} or {@link ValidationResult#STATE_AFTER}, depending on
	 * <code>strict</code>.
	 * </ol>
	 */
	protected static ValidationResult validateMoveElementState(IndepMoveElementChange change,
			Map<IElementReference, List<EObject>> map, boolean strict, boolean forward) {

		// get the specification of the movement
		final EReference oldContainment = forward ? change.getOldContainment() : change.getNewContainment();
		final EReference newContainment = forward ? change.getNewContainment() : change.getOldContainment();
		final IElementReference oldParentRef = forward ? change.getOldParent() : change.getNewParent();
		final IElementReference newParentRef = !forward ? change.getOldParent() : change.getNewParent();
		if (oldParentRef == null || newParentRef == null || oldContainment == null || newContainment == null)
			throw new IllegalStateException(
					"old and new parent and their containment features must be defined in the change but they are not!");

		// if we have many corresponding elements, check that the container can hold many of them!
		final Collection<EObject> correspondingElements = map.get(change.getCorrespondingElement());
		if (!newContainment.isMany() && correspondingElements.size() > 1)
			return ValidationResult.REFERENCE;

		// get the old and the new parents and check the resolution
		// Note: the new parent might not yet exist (being internal)!
		final SymRefCheck oldParentCheck = new SymRefCheck(oldParentRef, map, oldContainment.getEContainingClass(), 1);
		final SymRefCheck newParentCheck = new SymRefCheck(newParentRef, map, newContainment.getEContainingClass(), 1);
		if (oldParentCheck.validationResult != null)
			return oldParentCheck.validationResult;
		if (newParentCheck.validationResult != null)
			return newParentCheck.validationResult;
		final EObject oldParent = oldParentCheck.internal ? null : oldParentCheck.elements.get(0);
		final EObject newParent = newParentCheck.internal ? null : newParentCheck.elements.get(0);

		final ResultAccumulator result = new ResultAccumulator();
		if (oldParentCheck.internal) {
			// The internal references ensures via change dependency that the before-state is valid! This can occur when
			// we move from a removed sub-model.
			result.before = true;
		} else {
			for (EObject element : map.get(change.getCorrespondingElement())) {
				// get current parent
				final Object currentParent = element.eContainer();
				final EReference currentContainment = element.eContainmentFeature();

				// check state; NB: null values are NOT allowed here!
				if (currentParent == null || currentContainment == null) {
					result.invalid = true; // should never occur!
				} else if (currentParent.equals(oldParent) && currentContainment.equals(oldContainment)) {
					result.before = true;
				} else if (currentParent.equals(newParent) && currentContainment.equals(newContainment)) {
					result.after = true;
				} else { // if newParentCheck.internal, then it's invalid! because we need state_before!
					result.invalid = true;
				}
			}
		}
		return result.accumulate(strict);
	}

	/**
	 * Check if the element to remove exists in at least one (<code>strict = false</code>) or in all (
	 * <code>strict = true</code>) corresponding elements.
	 * 
	 * Get all elements to delete and check whether they are distributed over all parents.
	 * 
	 * Unlike {@link MPatchValidator#validateAddElementState(IndepAddRemElementChange, Map, boolean)}, this does not
	 * check whether they elements to delete are really described by the model descriptors. This allows the user to
	 * delete more elements than only strictly those that are deleted.
	 */
	protected static ValidationResult validateRemoveElementState(IndepAddRemElementChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {

		// get elements to delete
		final SymRefCheck toDeleteCheck = new SymRefCheck(change.getSubModelReference(), map, change.getSubModel()
				.getType(), change.getSubModelReference().getUpperBound());
		if (toDeleteCheck.symRef == null || toDeleteCheck.internal)
			throw new IllegalStateException("Submodel reference must neither be null nor an internal reference!");
		final int elementCountToDelete = toDeleteCheck.elements.size();

		final ResultAccumulator result = new ResultAccumulator();
		if (elementCountToDelete > 0) {
			for (final EObject parent : map.get(change.getCorrespondingElement())) {
				if (change.getContainment().isMany()) {

					// get the actual children and calculate intersection
					@SuppressWarnings("unchecked")
					final EList<EObject> rawList = (EList<EObject>) parent.eGet(change.getContainment());
					final List<EObject> toDelete = new ArrayList<EObject>(rawList);
					toDelete.retainAll(toDeleteCheck.elements);
					toDeleteCheck.elements.removeAll(toDelete);

					// there must be elements to remove
					result.before |= !toDelete.isEmpty();
					result.after |= toDelete.isEmpty();

				} else {
					final Object toDelete = parent.eGet(change.getContainment());

					// is it an element to delete? if so, remove it from toDeleteCheck and update result.before
					result.invalid |= toDelete == null || !toDeleteCheck.elements.contains(toDelete);
					result.before |= toDelete != null && toDeleteCheck.elements.remove(toDelete);
				}
			}
		} else {
			// no elements to delete...
			result.after = true;
		}
		
		// there are already elements removed
		/*
		 * In case the change cannot be applied to all resolved corresponding elements, setting result.after here
		 * yields a wrong result. This setting hasn't been proven to be useful at all, so we deactivate it for the
		 * time being.
		 */
//		result.after |= elementCountToDelete > toDeleteCheck.elements.size();
		return result.accumulate(strict);
	}

	/**
	 * If <code>strict</code> equals <code>true</code>, then all corresponding elements must be able to add an element;
	 * if <code>false</code>, then at least one corresponding element must be able to add an element. With
	 * {@link IModelDescriptor#isDescriptorOf(EObject)} it is also checked whether the element already exists.
	 */
	protected static ValidationResult validateAddElementState(IndepAddRemElementChange change,
			Map<IElementReference, List<EObject>> map, boolean strict) {

		// get elements that might be instances of the added element already
		final SymRefCheck toAddCheck = new SymRefCheck(change.getSubModelReference(), map, change.getSubModel()
				.getType(), change.getSubModelReference().getUpperBound());
		if (toAddCheck.symRef == null || toAddCheck.internal)
			throw new IllegalStateException("Submodel reference must neither be null nor an internal reference!");

		// are they really added elements or does the symbolic reference just match
		for (int i = toAddCheck.elements.size() - 1; i >= 0; i--) {
			final EObject element = toAddCheck.elements.get(i);
			if (change.getSubModel().isDescriptorFor(element, true) == null)
				toAddCheck.elements.remove(i);
		}

		final ResultAccumulator result = new ResultAccumulator();
		for (final EObject parent : map.get(change.getCorrespondingElement())) {
			if (change.getContainment().isMany()) {

				// get the actual children and calculate intersection
				@SuppressWarnings("unchecked")
				final EList<EObject> rawList = (EList<EObject>) parent.eGet(change.getContainment());
				final List<EObject> added = new ArrayList<EObject>(rawList);
				added.retainAll(toAddCheck.elements);
				toAddCheck.elements.removeAll(added);

				// there must be elements to add
				result.before |= added.isEmpty();
				result.after |= !added.isEmpty();

			} else {
				final Object toAdd = parent.eGet(change.getContainment());

				// is it possible to add the element? if so, remove it from toAddCheck and update result.before
				result.invalid |= toAdd != null && !toAddCheck.elements.contains(toAdd);
				result.before |= toAdd == null;
				result.after |= toAddCheck.elements.remove(toAdd);
			}
		}
		// there are already elements removed
		return result.accumulate(strict);
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
	static Collection<EReference> getImportantReferencesFor(EClass eClass, int direction) {
		final Collection<EReference> refs = new ArrayList<EReference>();
		if (equalEClasses(eClass, MPatchPackage.Literals.CHANGE_GROUP)) {
			// we are not interested in groups here!
		} else if (equalEClasses(eClass, MPatchPackage.Literals.UNKNOWN_CHANGE)) {
			// also skip unknown changes!
		} else {

			refs.add(MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT);
			if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE);
				// }
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE);
				// }
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT);
				// } else if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT);
				// }
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE);
				// }
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE);
				// }
			} else if (equalEClasses(eClass, MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE)) {
				// if (direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE);
				// } else if (direction == ResolvedSymbolicReferences.RESOLVE_CHANGED) {
				refs.add(MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE);
				// }
			}
		}
		return refs;
	}

	/**
	 * Check whether two eclasses are equal.
	 * 
	 * @param eClass1
	 *            An eclass.
	 * @param eClass2
	 *            Another eclass.
	 * @return <code>true</code>, if their classifier IDs equal; <code>false</code> otherwise.
	 */
	protected static boolean equalEClasses(EClass eClass1, EClass eClass2) {
		// return eClass1.getInstanceClassName().equals(eClass2.getInstanceClassName());
		return eClass1.getClassifierID() == eClass2.getClassifierID();
	}

	/**
	 * Order the given changes according to their dependencies. So in order to result in a useful order, make sure that
	 * the dependencies are set correctly.
	 * 
	 * The following order will be created: <br>
	 * &lt;all changes which are dependants of deletions&gt; <br>
	 * &lt;all deletions&gt; <br>
	 * &lt;all additions&gt; <br>
	 * &lt;all changes which depend on additions&gt;
	 * 
	 * @param changes
	 *            Changes.
	 * @param forward
	 *            The direction of application.
	 * @return A flat and ordered list of the input.
	 */
	public static List<IndepChange> orderChanges(final Set<IndepChange> changes, boolean forward) {

		// 0. a list containing the resulting order
		final List<IndepChange> list = new ArrayList<IndepChange>(changes.size());

		// 1. iterate over all changes and put all additions and deletions to the list
		for (IndepChange change : changes) {
			if ((change instanceof IndepAddElementChange && forward)
					|| ((change instanceof IndepRemoveElementChange && !forward))) {
				list.add(change); // add additions
			} else if ((change instanceof IndepRemoveElementChange && forward)
					|| ((change instanceof IndepAddElementChange && !forward))) {
				list.add(0, change); // insert deletions at the start
			}
		}

		// 2. evaluate all dependencies:
		// (iterate over a copy to be able to change the original list)
		// -> this might introduce duplicates which need to be removed later!
		for (IndepChange change : new ArrayList<IndepChange>(list)) {
			if ((change instanceof IndepAddElementChange && forward)
					|| ((change instanceof IndepRemoveElementChange && !forward))) {
				list.addAll(change.getDependants());
			} else if ((change instanceof IndepRemoveElementChange && forward)
					|| ((change instanceof IndepAddElementChange && !forward))) {
				list.addAll(0, change.getDependsOn());
			}
		}

		// 3. add all changes which were not considered so far
		final HashSet<IndepChange> tmpDiffs = new HashSet<IndepChange>(changes);
		tmpDiffs.removeAll(list);
		list.addAll(tmpDiffs);
		list.retainAll(changes);

		// 4. remove duplicates introduced in 2.
		if (list.size() > changes.size()) {
			final Set<IndepChange> elements = new HashSet<IndepChange>(changes.size());
			for (int i = list.size() - 1; i >= 0; i--) {
				if (elements.contains(list.get(i)))
					list.remove(i);
				else
					elements.add(list.get(i));
			}
		}

		// 5. double check that we do exactly have the number of changes we require!
		if (list.size() != changes.size() || !changes.containsAll(list)) {
			throw new IllegalStateException(
					"The number of ordered changes does not equal the number of total changes! "
							+ "Please check ordering algorithm!");
		}

		return list;
	}

}
