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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator.ValidationResult;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A helper class for resolving all symbolic references of the given differences for the given model. The result is an
 * instance of {@link ResolvedSymbolicReferences} which contains a raw symbolic reference resolution and a resolution
 * grouped per {@link IndepChange}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MPatchResolver {

	/** A constant defining the resolution of elements in the unchanged model of an mpatch. */
	public static final int RESOLVE_UNCHANGED = ResolvedSymbolicReferences.RESOLVE_UNCHANGED;

	/** A constant defining the resolution of elements in the changed model of an mpatch. */
	public static final int RESOLVE_CHANGED = ResolvedSymbolicReferences.RESOLVE_CHANGED;

	/**
	 * This resolves all symbolic references and wraps it in a result object of type {@link ResolvedSymbolicReferences}
	 * . It usually makes use of {@link MPatchResolver#resolveSymbolicReferences(MPatchModel, Resource)}.<br>
	 * <br>
	 * Since the diff is not directed, the symbolic references can be resolved either for the target (right) or the
	 * source (left) model. This is given in the parameter <code>direction</code>.<br>
	 * <br>
	 * <b>Please note: Quite often a symbolic reference is resolved to too many model elements! That can
	 * 
	 * @param mpatch
	 *            An mpatch.
	 * @param model
	 *            An emf model for which the symbolic references should be resolved. <b>Note that its meta model should
	 *            rather be the same as the one of the originating models the diff was created from!</b>
	 * @param direction
	 *            The direction for symbolic reference resolution. Either {@link MPatchResolver#RESOLVE_UNCHANGED}
	 *            (resolves the elements <i>before</i> the change) or {@link MPatchResolver#RESOLVE_CHANGED} (resolves
	 *            the elements <i>after</i> the change).
	 * @return A wrapper element for the result, see {@link ResolvedSymbolicReferences} for details.
	 */
	public static ResolvedSymbolicReferences resolveSymbolicReferences(final MPatchModel mpatch, final EObject model,
			final int direction) {
		// 0. prepare result
		final Map<IElementReference, List<EObject>> rawResult = new HashMap<IElementReference, List<EObject>>();
		final Map<IndepChange, Map<IElementReference, List<EObject>>> resolution = new LinkedHashMap<IndepChange, Map<IElementReference, List<EObject>>>();
		final Map<IElementReference, List<IElementReference>> equalRefs = new LinkedHashMap<IElementReference, List<IElementReference>>();
		final ResolvedSymbolicReferences result = new ResolvedSymbolicReferences(mpatch, model, direction, resolution,
				rawResult, equalRefs);

		// 1. resolve all symbolic references
		resolveRawSymbolicReferences(result);

		// 2. prepare and fill result
		for (final IndepChange change : mpatch.getChanges()) {
			analyzeChange(change, rawResult, direction, resolution);
		}

		// 3. remove invalid states!
		final boolean forward = direction == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;
		for (IndepChange change : result.getResolutionByChange().keySet()) {
			checkStateResolution(change, result.getResolutionByChange().get(change), false, forward);
		}
		
		// 4. return wrapper object
		return result;
	}

	/**
	 * Resolve symbolic references <b>in a raw form</b>. This means that all symbolic references (instances of
	 * {@link IElementReference}) are collected from the input diff ({@link MPatchModel}) and resolved for the given
	 * resource. The collection is then returned in a map.<br>
	 * <br>
	 * <b>Note: If you need symbolic reference resolution depending on the direction, you should rather use
	 * {@link IDiffApplier#resolveSymbolicReferences(MPatchModel, Resource, int)}, because it makes use of this
	 * method.</b>
	 * 
	 * A mapping from symbolic references to all resolved model elements is stored in
	 * {@link ResolvedSymbolicReferences#getRawResolution()}.
	 * 
	 * @param result
	 *            The data object containing references to an mpatch and a model for which the symbolic references
	 *            should be resolved. <b>Note that its meta model should rather be the same as the one of the
	 *            originating models the diff was created from!</b>
	 */
	protected static void resolveRawSymbolicReferences(final ResolvedSymbolicReferences result) {
		// 1. collect all symbolic references
		final List<EObject> references = ExtEcoreUtils.collectTypedElements(result.getMPatchModel().getChanges(),
				Collections.singleton(MPatchPackage.Literals.IELEMENT_REFERENCE), true);

		// 2. iterate over all symbolic references and try to resolve them
		for (final EObject obj : references) {
			final IElementReference ref = (IElementReference) obj;

			// 3. check if there is an equally resolving symref
			List<EObject> resolution = null;
			final Map<IElementReference, List<IElementReference>> equallyResolvingRefs = result
					.getEquallyResolvingReferences();
			for (IElementReference ref2 : equallyResolvingRefs.keySet()) {
				if (ref.resolvesEqual(ref2)) {

					// if so, add current ref to existing collection
					MPatchUtil.addElementToListMap(ref2, ref, equallyResolvingRefs);

					// add ref as new key
					equallyResolvingRefs.put(ref, equallyResolvingRefs.get(ref2));

					// clone existing resolution
					resolution = new ArrayList<EObject>(result.getRawResolution().get(ref2));
					break;
				}
			}

			// resolve and create new collection only if not resolved before!
			if (resolution == null) {
				resolution = ref.resolve(result.getModel()); // the actual reference resolution!
				MPatchUtil.addElementToListMap(ref, ref, equallyResolvingRefs); // new collection
			}

			// add resolution to result
			result.getRawResolution().put(ref, resolution);
		}
	}

	/**
	 * Convert raw result into a more convenient format.
	 */
	protected static void analyzeChange(final IndepChange change,
			final Map<IElementReference, List<EObject>> rawResult, final int direction,
			final Map<IndepChange, Map<IElementReference, List<EObject>>> accumulator) {
		if (change instanceof ChangeGroup) {

			// recursive call for groups
			final ChangeGroup group = (ChangeGroup) change;
			for (final IndepChange subChange : group.getSubChanges()) {
				analyzeChange(subChange, rawResult, direction, accumulator);
			}
			// TODO: we skip corresponding elements for groups here! maybe we need that later?!

		} else if (!(change instanceof UnknownChange)) {

			// fill map only for the relevant referencing, depending on the direction
			final Map<IElementReference, List<EObject>> map = new HashMap<IElementReference, List<EObject>>();

			// regular symrefs
			final Collection<EReference> importantRefs = MPatchValidator.getImportantReferencesFor(change.eClass(),
					direction);
			for (final EReference ref : importantRefs) {
				final IElementReference symRef = (IElementReference) change.eGet(ref);
				if (symRef != null)
					map.put(symRef, rawResult.get(symRef));
			}

			// cross reference symrefs
			final Map<IElementReference, IndepChange> crossRefs = MPatchUtil.collectCrossReferences(Collections
					.singletonList(change));
			for (final IElementReference symRef : crossRefs.keySet()) {
				if (symRef != null && !map.containsKey(symRef))
					map.put(symRef, rawResult.get(symRef));
			}

			// store that to the accumulator
			accumulator.put(change, map);
		}
	}

	/**
	 * Check the state of the given change. If it is only partially valid, remove all invalid corresponding elements. If
	 * it cannot be validated at all, return false.
	 * 
	 * @param change
	 *            The change to check.
	 * @param resolution
	 *            The resolution for this particular change.
	 * @param reduce
	 *            If <code>true</code>, then the resolutions might be reduced to the maximum number of allowed elements
	 *            as defined by the upper bound. However, it might remove good matches, too!
	 * @param forward
	 *            The direction of resolution.
	 * @return <code>true</code>, if the state is valid or could be fixed. <code>false</code> if the state could not be
	 *         fixed.
	 */
	public static boolean checkStateResolution(IndepChange change, Map<IElementReference, List<EObject>> resolution,
			boolean reduce, boolean forward) {

		final ValidationResult allValidStates = MPatchValidator.validateElementState(change, resolution, true, forward);

		switch (allValidStates) {
		case SUCCESSFUL:
			// cool, this one is fine!
			return true;

		case UNKNOWN_CHANGE:
			// Oups.. where does that one come from?! anyway.. let's kick it out!
			return false;

		case INVALID_STATE:
			// ok, this one needs further investigation. maybe we can validate it by removing some corresponding
			// elements! Lets check that:
			if (resolution.get(change.getCorrespondingElement()).size() <= 1)
				return false; // we cannot do much if there is just one element with an invalid state!
			final ValidationResult anyValidState = MPatchValidator.validateElementState(change, resolution, false,
					forward);
			if (ValidationResult.SUCCESSFUL.equals(anyValidState)) {
				return filterValidStates(change, resolution, change.getCorrespondingElement().getUpperBound(), reduce, forward);
			}
			return false; // if there is not even one valid state (strict was false!), we cannot do much...

		case REFERENCE:
			// Some reference was not resolved successfully. We cannot do much here if it's not the corresponding
			// element that failed. So let's check that!
			final IElementReference symref = change.getCorrespondingElement();
			final List<EObject> elements = resolution.get(symref);
			if (elements == null || elements.isEmpty())
				return false; // no resolution - not valid!
			final boolean valid = MPatchValidator.validateResolution(symref, elements);
			if (valid)
				return false; // nope.. we cannot solve invalid cross references...
			if (symref.getUpperBound() < elements.size() && elements.size() > 1) {
				// ok, here we can do something! Let's just remove those whose states are invalid!
				return filterValidStates(change, resolution, symref.getUpperBound(), reduce, forward);
			}
			return false; // I don't know how we could resolve the references now... kick it out!

		default:
			throw new RuntimeException("Unknown status value received: " + allValidStates);
		}
	}

	/**
	 * Filter only those resolved corresponding elements whose state is valid.
	 * 
	 * In case there are too many elements resolved, i.e. the number of resolved elements is greater than
	 * <code>maxElements</code>, then only the first <code>#maxElements</code> are used!!! <b>This is just a heuristic
	 * and might remove wanted and return unwanted elements!!! So the user should ALWAYS check the resolution
	 * manually!!!</b>
	 * 
	 * @param change
	 *            The change under investigation.
	 * @param resolution
	 *            The resolution of this change.
	 * @param maxElements
	 *            The maximum number of resolved corresponding elements allowed.
	 * @param reduce
	 *            If <code>true</code>, then the resolutions might be reduced to the maximum number of allowed elements
	 *            as defined by <code>maxElements</code>. However, it might remove good matches, too!
	 * @param forward
	 *            The direction of resolution.
	 * @return <code>true</code> if the resolution could be fixed for the given conditions, <code>false</code>
	 *         otherwise.
	 */
	private static boolean filterValidStates(IndepChange change, Map<IElementReference, List<EObject>> resolution,
			int maxElements, boolean reduce, boolean forward) {
		// unfortunately, we don't have individual state testing opportunities, so we do that for each resolved
		// corresponding element individually!
		final List<EObject> resolvedElements = resolution.get(change.getCorrespondingElement());
		final List<EObject> allResolvedElements = new ArrayList<EObject>(resolvedElements);
		final List<EObject> validElements = new ArrayList<EObject>(resolvedElements.size());
		for (EObject element : allResolvedElements) {
			resolvedElements.clear(); // let's clear it temporarily to test the current element
			resolvedElements.add(element);
			final ValidationResult state = MPatchValidator.validateElementState(change, resolution, true, forward);
			if (ValidationResult.SUCCESSFUL.equals(state))
				validElements.add(element);
		}
		resolvedElements.clear();

		// now we filtered all valid elements!
		// if there are too many, just get as much we need...
		final int count = reduce ? Math.min(maxElements, validElements.size()) : validElements.size();
		for (int i = 0; i < count; i++) {
			resolvedElements.add(validElements.get(i));
		}

		return !resolvedElements.isEmpty();
	}
}
