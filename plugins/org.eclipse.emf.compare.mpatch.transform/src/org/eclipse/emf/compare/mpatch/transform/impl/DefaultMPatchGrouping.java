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
package org.eclipse.emf.compare.mpatch.transform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * The default grouping strategy for an {@link MPatchModel}.<br>
 * Please see {@link DefaultMPatchGrouping#group(MPatchModel)} for details.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class DefaultMPatchGrouping implements IMPatchTransformation {

	/** The label. */
	public static final String LABEL = "Intuitive Grouping";

	/** Description for this transformation. */
	private static final String DESCRIPTION = "This transformation introduces groups in the "
			+ MPatchConstants.MPATCH_LONG_NAME
			+ " for structuring the changes. This is an optional transformation and makes the "
			+ MPatchConstants.MPATCH_LONG_NAME + " easier to read.\n\n"
			+ "The groups do not have any functional aspect but are introduced for convenience only.\n"
			+ "The grouping strategy is a heuristic algorithm which works as follows.\n"
			+ "Assumption: each change in the " + MPatchConstants.MPATCH_LONG_NAME
			+ " as well as all model elements (defined by their URI) are nodes in a graph, and all "
			+ MPatchConstants.SYMBOLIC_REFERENCES_NAME + " are (undirected) arcs.\n"
			+ "Then the changes of each *connected component* are put into one group.";

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
		return 40;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOptional() {
		return true;
	}

	/**
	 * See {@link DefaultMPatchGrouping#group(MPatchModel)}.
	 */
	@Override
	public int transform(MPatchModel mpatch) {
		return group(mpatch);
	}

	/**
	 * This is a default implementation of the mpatch grouping.
	 * 
	 * Note that it makes use of the uri attribute of symbolic references to identify symbolic references which point to
	 * the same elements. Subclasses may change this behavior.
	 * 
	 * @param mpatch
	 *            The ungrouped mpatch.
	 * @return <code>0</code>, if nothing was changed; the number of groups created, otherwise.
	 * @throws IllegalArgumentException
	 *             If the given mpatch is either empty or they already contains groups.
	 */
	public static int group(MPatchModel mpatch) throws IllegalArgumentException {

		// 1. read model and check whether it is flat
		final Set<IndepChange> changes = checkModel(mpatch);

		// 2. analyze changes and build groups
		final Set<Set<IndepChange>> groups = analyzeChanges(changes);

		// 3. create groups and move changes
		return regroup(mpatch, groups);
	}

	/**
	 * This checks that the diff does not already contain groups. Furthermore, it returns all changes that should be
	 * restructured.
	 * 
	 * @param mpatch
	 *            The input mpatch.
	 * @return A set of changes which are matter of the grouping.
	 * @throws IllegalArgumentException
	 *             If the input is empty or the diff already contains {@link ChangeGroup}s.
	 */
	protected static Set<IndepChange> checkModel(MPatchModel mpatch) throws IllegalArgumentException {
		// initialize result set
		final Set<IndepChange> changes = new HashSet<IndepChange>();
		for (final IndepChange change : mpatch.getChanges()) {
			if (change instanceof ChangeGroup) {
				throw new IllegalArgumentException("Input expected not to contain groups, but it already does:\n"
						+ change);
			}
			changes.add(change);
		}

		// make sure we have at least one change
		if (changes.size() == 0) {
			throw new IllegalArgumentException("Input is empty!");
		}
		return changes;
	}

	/**
	 * This analyzes the given changes and collects them according to common symbolic references.<br>
	 * First, {@link DefaultMPatchGrouping#fillMaps(Set)} is used to calculate all relations between the changes and
	 * symbolic references. Second, all changes having any symbolic reference in common, are grouped together.
	 * 
	 * @param changes
	 *            A set of unordered and ungrouped changes.
	 * @return A set of groups, such that all containing {@link IndepChange}s do not interfere with others from a
	 *         different group.
	 */
	protected static Set<Set<IndepChange>> analyzeChanges(Set<IndepChange> changes) {
		/*
		 * initialize intermediate data structure:
		 * 
		 * 1. change -> ref* (set of all uris it uses) 2. ref -> change* (set of all changes in which the reference is
		 * used)
		 */
		final T2<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>> maps = fillMaps(changes);

		// initialize result data structure for groups
		final Set<Set<IndepChange>> groups = new HashSet<Set<IndepChange>>();
		final Queue<IndepChange> queue = new LinkedList<IndepChange>();
		final Set<IndepChange> processed = new HashSet<IndepChange>();

		// iterate over all changes
		for (final IndepChange change : maps.s.keySet()) {
			if (!processed.contains(change)) {

				final Set<IndepChange> group = new HashSet<IndepChange>();
				groups.add(group);

				// create group for the current change
				queue.add(change); // at this point, we add an element to an empty queue
				while (!queue.isEmpty()) {
					final IndepChange current = queue.remove();
					group.add(current);

					// iterate over all symbolic references and get new changes which also refer to them
					for (final String ref : maps.s.get(current)) {
						for (final IndepChange refChange : maps.t.get(ref)) {
							if (!group.contains(refChange) && !queue.contains(refChange)) {
								queue.add(refChange);
							}
						}
					}
				}
				processed.addAll(group);
			}
		}

		return groups;
	}

	/**
	 * Look for all symbolic references ({@link IElementReference}) in the given set of {@link IndepChange}s.<br>
	 * <ol>
	 * <li>The first part of the result ({@link T2#s}) maps each {@link IndepChange} to all symbolic references it uses.
	 * <li>The second part of the result ({@link T2#t}) maps each symbolic reference (as a String representation
	 * determined by {@link DefaultMPatchGrouping#getSymbolicReferenceRepresentative(IElementReference)}) to all
	 * {@link IndepChange}s in which they are used.
	 * </ol>
	 * <br>
	 * 
	 * @param changes
	 *            A set of {@link IndepChange}s.
	 * @return A relation between {@link IndepChange}s and symbolic references.
	 */
	// SuppressWarning because of the cast to List<EObject>
	@SuppressWarnings("unchecked")
	protected static T2<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>> fillMaps(Set<IndepChange> changes) {
		final Map<IndepChange, Set<String>> changeToRefMap = new HashMap<IndepChange, Set<String>>();
		final Map<String, Set<IndepChange>> refToChangeMap = new HashMap<String, Set<IndepChange>>();

		// iterate over all changes and create set of string representatives for its symbolic references
		for (final IndepChange change : changes) {

			// special treatment for unknown changes: create extra group for them
			if (change instanceof UnknownChange) {
				addElementToSetMap("<unknown>", change, refToChangeMap);
				addElementToSetMap(change, "<unknown>", changeToRefMap);
				continue;
			}

			final Set<String> references = new HashSet<String>();

			// all symbolic references are contained somewhere according to our meta model
			for (final EReference containment : change.eClass().getEAllContainments()) {

				// TODO: we can maybe optimize by iterating only over IModelDescriptors and IElementReferences

				// there might be single references or many references: collect them before processing them
				final List<EObject> children = new ArrayList<EObject>();
				if (containment.isMany()) {
					children.addAll((List<? extends EObject>)change.eGet(containment));
				} else {
					children.add((EObject)change.eGet(containment));
				}

				// now lets create string representatives for all symbolic references
				for (final EObject eObject : children) {
					if (eObject instanceof IModelDescriptor) {
						final IModelDescriptor descriptor = (IModelDescriptor) eObject;

						// consider all requiredReferences as well as all selfReferences
						final Collection<IElementReference> descriptorReferences = new ArrayList<IElementReference>();
						descriptorReferences.addAll(descriptor.getAllCrossReferences());
						descriptorReferences.addAll(descriptor.getAllSelfReferences());
						for (final IElementReference ref : descriptorReferences) {

							// collect references for the current change
							references.add(getSymbolicReferenceRepresentative(ref));
							addElementToSetMap(getSymbolicReferenceRepresentative(ref), change, refToChangeMap);

						}
					} else if (eObject instanceof IElementReference) {

						// collect references for the current change
						references.add(getSymbolicReferenceRepresentative((IElementReference)eObject));
						addElementToSetMap(getSymbolicReferenceRepresentative((IElementReference)eObject), change,
								refToChangeMap);
					}
				}
			}
			changeToRefMap.put(change, references);
		}
		return new T2<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>>(changeToRefMap, refToChangeMap);
	}

	/** Helper method to add an element to a map of set of elements. */
	protected static <T, S> void addElementToSetMap(T key, S element, Map<T, Set<S>> map) {
		Set<S> set = map.get(key);
		if (set == null) {
			set = new HashSet<S>();
			map.put(key, set);
		}
		set.add(element);
	}

	/**
	 * The default implementation uses {@link IElementReference#getUriReference()} to identify common target elements of
	 * symbolic references. Subclasses may refine this behavior.
	 * 
	 * @param reference
	 *            A symbolic reference.
	 * @return A {@link String} representative for the target element of this particular {@link IElementReference}.
	 */
	protected static String getSymbolicReferenceRepresentative(IElementReference reference) {
		final String uriReference = reference.getUriReference();
		if (uriReference.indexOf("#") > 0)
			return uriReference.substring(uriReference.indexOf("#"));
		else
			return uriReference;
	}

	/**
	 * This changes the input {@link MPatchModel} according to the given set of changes. To this end, it creates
	 * {@link ChangeGroup}s and moves all changes accordingly.
	 * 
	 * @param mpatch
	 *            The input mpatch which will be restructured.
	 * @param changeGroups
	 *            A set of changes which will be used for the restructuring.
	 * @return The number of groups created in <code>model</code>.
	 */
	protected static int regroup(final MPatchModel mpatch, final Set<Set<IndepChange>> changeGroups) {
		// create new changegroups, add them to the model, and move all changes accordingly
		for (final Set<IndepChange> group : changeGroups) {
			final ChangeGroup changeGroup = MPatchFactory.eINSTANCE.createChangeGroup();
			mpatch.getChanges().add(changeGroup);
			changeGroup.getSubChanges().addAll(group);
		}
		return changeGroups.size(); // returns 0, if nothing was changed
	}

	/** 
	 * Simple immutable triple wrapper class. 
	 *
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private static class T2<S, T> {
		public final S s;
		public final T t;

		public T2(S s, T t) {
			this.s = s;
			this.t = t;
		}
	}
}
