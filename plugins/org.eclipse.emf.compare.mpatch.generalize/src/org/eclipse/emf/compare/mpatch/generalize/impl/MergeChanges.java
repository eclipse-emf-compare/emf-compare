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
package org.eclipse.emf.compare.mpatch.generalize.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;

/**
 * This transformation analyzes all changes in the given MPatch and merges similar changes. This extends their scope and
 * makes them applicable to more models.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MergeChanges implements IMPatchTransformation {

	/** Label. */
	public static final String LABEL = "Merge Similar Changes";

	/** Description for this transformation. */
	private static final String DESCRIPTION = "This transformation looks for similar changes and merges them into generalized changes. "
			+ "This is an optional transformation and might change the result of "
			+ MPatchConstants.MPATCH_SHORT_NAME
			+ " application!\n\n"
			+ "If the "
			+ MPatchConstants.MPATCH_SHORT_NAME
			+ " contains multiple changes of the same type describing the same change but for several model elements, "
			+ "this transformation will merge them into a single, generalized change. "
			+ "The cardinality of a generalized change will be set to [1..n], with n being the number of changes it was created from. "
			+ "If the result should be applicable to another number of element, perform 'Unbound Symbolic References' afterwards.\n"
			+ "Condition-based " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + " are required!";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return LABEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPriority() {
		return 30;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOptional() {
		return true;
	}

	/**
	 * See: {@value #DESCRIPTION}.
	 */
	@Override
	public int transform(MPatchModel mpatch) {
		return mergeChanges(mpatch);
	}

	/**
	 * This method first analyzes all given changes in the MPatch in order to find mergable changes. Then it creates
	 * generalized changes for all finds, updates dependencies and cross-references, and finally removes all changes
	 * that were replaced by a generalized change.
	 * 
	 * @param mpatch
	 *            An MPatch.
	 * @return The number of generalized changes that were created.
	 */
	public static int mergeChanges(MPatchModel mpatch) {

		// 1. sort changes by type: we can only merge changes of the same type
		final Collection<List<IndepChange>> typedChanges = getTypedChanges(mpatch);

		// 3. compare changes to find for sure sets for generalization
		final List<List<IndepChange>> filteredChanges = filterTypedChanges(typedChanges);

		// 4. create generalized changes
		final Map<IndepChange, List<IndepChange>> generalizedChanges = generalizeChanges(filteredChanges);

		// 5. delete old changes and add generalized changes (respect and transfer dependencies!!!)
		final int successfulReplacements = replaceChanges(mpatch, generalizedChanges);

		// 6. return number of created generalized changes
		return successfulReplacements;
	}

	/**
	 * Replace all changes there were generalized with the generalized changes given in the map.
	 * 
	 * @param mpatch
	 *            The MPatch containing the changes.
	 * @param generalizedChanges
	 *            The mapping from all newly created generalized changes their sets of non-generalized changed from
	 *            which they were created from.
	 * @return The number of successfully added generalized changes.
	 */
	private static int replaceChanges(MPatchModel mpatch, Map<IndepChange, List<IndepChange>> generalizedChanges) {
		final ChangeGroup group = MPatchFactory.eINSTANCE.createChangeGroup();
		final Set<ChangeGroup> oldGroups = new HashSet<ChangeGroup>();
		int counter = 0;

		for (IndepChange generalizedChange : generalizedChanges.keySet()) {
			final List<IndepChange> changes = generalizedChanges.get(generalizedChange);

			// 1. find the best place for the new generalized change
			addGeneralizedChange(mpatch, generalizedChange, changes, group);

			// 2. update dependencies
			updateDependencies(generalizedChange, changes, mpatch);

			// 3. remove all other changes
			deleteElements(changes, oldGroups);

			counter++;
		}

		// if there are changes in that group, we have to add it!
		if (!group.getSubChanges().isEmpty())
			mpatch.getChanges().add(group);
		
		// clean up empty groups
		for (ChangeGroup changeGroup : oldGroups) {
			if (changeGroup.getSubChanges().isEmpty())
				EcoreUtil.delete(changeGroup);
		}

		return counter;
	}

	/**
	 * Update the dependencies of the given generalized change by calculating the union of all dependencies in the list
	 * changes.
	 * 
	 * @param generalizedChange
	 *            The generalized change without any dependencies.
	 * @param changes
	 *            The non-generalized change containing dependency relations.
	 * @param mpatch
	 *            The root container.
	 */
	private static void updateDependencies(IndepChange generalizedChange, List<IndepChange> changes, MPatchModel mpatch) {
		for (IndepChange change : changes) {

			// dependencies are just unions
			generalizedChange.getDependants().addAll(change.getDependants());
			generalizedChange.getDependsOn().addAll(change.getDependsOn());

			// not necessary because we make a recursive delete later..
			// change.getDependants().clear();
			// change.getDependsOn().clear();

			/*
			 * Now we got the problem of cross references _to_ 'change', e.g. references from other
			 * ModelDescriptorReferences. We have to detect them and replace them with references to
			 * 'generalizedChange'. This should be done in a generic way, maybe there will be other cases of cross
			 * references in the future!
			 * 
			 * Cross reference _from_ change are not a problem because we copied them :-)
			 */
			final List<? extends EObject> flattenedChange = ExtEcoreUtils
					.flattenEObjects(Collections.singleton(change));
			final Map<EObject, Collection<Setting>> crossReferences = UsageCrossReferencer.findAll(flattenedChange,
					mpatch);
			Map<EObject, EObject> match = null;
			for (EObject oldTarget : crossReferences.keySet()) {
				for (Setting setting : crossReferences.get(oldTarget)) {
					final EStructuralFeature feature = setting.getEStructuralFeature();
					final EObject owner = setting.getEObject();

					// check whether this cross reference is relevant
					if (feature == null || feature.isDerived() || !feature.isChangeable())
						continue;
					if (MPatchPackage.Literals.INDEP_CHANGE__DEPENDANTS.equals(feature)
							|| MPatchPackage.Literals.INDEP_CHANGE__DEPENDS_ON.equals(feature))
						continue;

					// some debug info
					// System.out.println("owner: " + owner);
					// System.out.println("feature: " + feature);
					// System.out.println("old target: " + oldTarget);

					// copy cross reference "owner.feature = target"
					if (match == null)
						match = CommonUtils.getMatchingObjects(change, generalizedChange);
					final EObject newTarget = match.get(oldTarget);
					if (newTarget == null)
						throw new IllegalStateException(
								"We should ALWAYS get a match here since generalized is a COPY of change!!!");
					updateReference(owner, feature, oldTarget, newTarget);
				}
			}
		}
	}

	/**
	 * This basically 'moves' a reference from <code>oldTarget</code> to <code>newTarget</code>.
	 * 
	 * @param owner
	 *            The owner of the reference.
	 * @param feature
	 *            The feature that will be moved.
	 * @param oldTarget
	 *            The old target of the reference.
	 * @param newTarget
	 *            The new target of the reference.
	 */
	private static void updateReference(EObject owner, EStructuralFeature feature, EObject oldTarget, EObject newTarget) {
		// unset oldOwner.feature = target
		EcoreUtil.remove(owner, feature, oldTarget);

		// set newOwner.feature = target
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final List<EObject> list = (List<EObject>) owner.eGet(feature);
			list.add(newTarget);

			// validate
			if (!list.contains(newTarget))
				throw new RuntimeException("Could not move reference to merged change! Feature: " + feature.getName()
						+ ", owner: " + owner);
		} else {
			owner.eSet(feature, newTarget);
		}

	}

	/**
	 * Delete the given list of elements. Also delete all references to other elements (recursively)!
	 * 
	 * @param elements
	 *            A list of elements that should be deleted.
	 * @param A set of {@link ChangeGroup}s in which the deleted elements were contained.
	 */
	private static void deleteElements(List<? extends EObject> elements, Set<ChangeGroup> groups) {
		for (EObject element : elements) {
			if (element.eContainer() != null && element.eContainer() instanceof ChangeGroup)
				groups.add((ChangeGroup) element.eContainer());
			/*
			 * this is a real delete, i.e. all cross-references are removed recursively!
			 */
			EcoreUtil.delete(element, true);
		}
	}

	/**
	 * This adds a generalized change in the MPatch. If all changes from which it was created are located in the same
	 * container, then the generalized change will be created in it. Otherwise it will be put in the given
	 * <code>group</code>.
	 * 
	 * @param mpatch
	 *            The root MPatch model.
	 * @param generalizedChange
	 *            The generalization that is about to be added.
	 * @param changes
	 *            The list of changes that the generalized change replaces.
	 * @param group
	 *            A group for all generalized changes that do not have a unique place to be added.
	 */
	private static void addGeneralizedChange(MPatchModel mpatch, IndepChange generalizedChange,
			List<IndepChange> changes, ChangeGroup group) {

		// if all changes have the same parent, find it!
		EObject parent = null;
		for (IndepChange indepChange : changes) {
			if (indepChange.eContainer() == null)
				throw new IllegalStateException("Change is not contained anywhere! " + indepChange);
			if (parent == null) {
				parent = indepChange.eContainer();
			} else if (!parent.equals(indepChange.eContainer())) {
				parent = null;
				break;
			}
		}

		if (parent instanceof ChangeGroup) {

			// if we got one common parent, add our change to it!
			final ChangeGroup commonGroup = (ChangeGroup) parent;
			commonGroup.getSubChanges().add(generalizedChange);
		} else if (mpatch.equals(parent)) {

			// maybe it is the mpatch model itself (probably no groups..)
			mpatch.getChanges().add(generalizedChange);
		} else {

			// else add it to the given group
			group.getSubChanges().add(generalizedChange);
		}
	}

	/**
	 * From the given collection of typed changes, filter out all generalizable changes!
	 * 
	 * @param typedChanges
	 *            A collection of lists that contain changes of the same type!
	 * @return A list of lists, each containing a set of changes that are generalizable.
	 */
	private static List<List<IndepChange>> filterTypedChanges(Collection<List<IndepChange>> typedChanges) {
		final List<List<IndepChange>> filteredChanges = new ArrayList<List<IndepChange>>();
		for (List<IndepChange> list : typedChanges) {

			final List<List<IndepChange>> filteredList = filterChanges(list);
			if (filteredList != null) {
				// eliminate null lists, empty lists, and single-valued lists
				for (int i = filteredList.size() - 1; i >= 0; i--)
					if (filteredList.get(i) == null || filteredList.get(i).size() <= 1)
						filteredList.remove(i);
				if (!filteredList.isEmpty())
					filteredChanges.addAll(filteredList);
			}
		}
		return filteredChanges;
	}

	/**
	 * From the given list of changes (all having the same type!), check which of them are generalizable. Group them
	 * together and return these groups.
	 * 
	 * @param list
	 *            A list of changes of the same type.
	 * @return A collection of lists, each containing a set of generalizable changes. Note that all changes are
	 *         returned! That is, all changes that are not generalizable are returned in singleton lists.
	 */
	public static List<List<IndepChange>> filterChanges(List<IndepChange> list) {
		final ArrayList<List<IndepChange>> result = new ArrayList<List<IndepChange>>();
		outer: for (final IndepChange change : list) {
			for (List<IndepChange> filtered : result) {
				final IndepChange otherChange = filtered.get(0); // it is sufficient to compare it to one element only
				if (MergeChangesCompare.generalizable(change, otherChange)) {
					filtered.add(change);
					continue outer;
				}
			}

			// not found: create new group
			final ArrayList<IndepChange> group = new ArrayList<IndepChange>();
			group.add(change);
			result.add(group);
		}
		return result;
	}

	/**
	 * Simply collect all {@link IndepChange}s of the given MPatch and sort them by their type (EClass).
	 * 
	 * @param mpatch
	 *            An MPatch.
	 * @return A collection of changes sorted by their type.
	 */
	private static Collection<List<IndepChange>> getTypedChanges(MPatchModel mpatch) {
		final Map<EClass, List<IndepChange>> typedChanges = new LinkedHashMap<EClass, List<IndepChange>>();
		collectedTypedChanges(mpatch.getChanges(), typedChanges);
		return typedChanges.values();
	}

	/**
	 * A helper method for collecting changes of the same type.
	 * 
	 * @param changes
	 *            A list of changes.
	 * @param typedChanges
	 *            An accumulator for sorting changes by their type.
	 */
	private static void collectedTypedChanges(EList<IndepChange> changes, Map<EClass, List<IndepChange>> typedChanges) {
		for (IndepChange change : changes) {
			if (change instanceof ChangeGroup) {
				collectedTypedChanges(((ChangeGroup) change).getSubChanges(), typedChanges);
			} else {
				MPatchUtil.addElementToListMap(change.eClass(), change, typedChanges);
			}
		}
	}

	/**
	 * This performs the generalization of changes by delegating the call to {@link MergeChangesGeneralizer}.
	 * 
	 * @param filteredChanges
	 *            A collection of generalizable changes.
	 * @return A map from generalized changes to their original changes.
	 */
	private static Map<IndepChange, List<IndepChange>> generalizeChanges(List<List<IndepChange>> filteredChanges) {
		final Map<IndepChange, List<IndepChange>> generalizedChanges = new LinkedHashMap<IndepChange, List<IndepChange>>();
		for (List<IndepChange> changes : filteredChanges) {
			final IndepChange generalizedChange = MergeChangesGeneralizer.generalizeChanges(changes);
			if (generalizedChange != null)
				generalizedChanges.put(generalizedChange, changes);
		}
		return generalizedChanges;
	}
}
