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
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * The implementation of the transformation introduces dependencies between the {@link IndepChange}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MPatchDependencies implements IMPatchTransformation {

	/**
	 * The label for this transformation.
	 */
	public static final String LABEL = "Dependency Graph";

	/** Description for this transformation. */
	private static final String DESCRIPTION = "This transformation builds a dependency graph between all changes in the "
			+ MPatchConstants.MPATCH_LONG_NAME
			+ ". This is required for the default "
			+ MPatchConstants.MPATCH_SHORT_NAME
			+ " application engine to work properly!\n\n"
			+ "A dependency is defined by the following two rules:\n"
			+ "1. a change A depends on a change B, if B describes the addition of a sub-model "
			+ "and A contains a reference to any element in that sub-model;\n"
			+ "2. a change A depends further on a change B, if A describes the deletion of a sub-model "
			+ "and B contains references to any element in that sub-model.";

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
		return 20;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOptional() {
		return false;
	}
	
	/**
	 * See {@link MPatchDependencies#calculateDependencies(MPatchModel)}.
	 */
	@Override
	public int transform(MPatchModel mpatch) {
		return calculateDependencies(mpatch);
	}

	/**
	 * This computes the dependency graph between changes.<br>
	 * The order of difference application is important because cross-references make changes dependent on each other;
	 * Thus, the order of difference application can directly be derived from a dependency graph which is
	 * straightforward to compute from the following two rules:
	 * <ol>
	 * <li>a change \emph{A} depends on a change \emph{B}, if \emph{B} describes the addition of a sub-model and
	 * \emph{A} contains a symbolic reference to any element in that sub-model;
	 * <li>a change \emph{A} depends further on a change \emph{B}, if \emph{A} describes the deletion of a sub-model and
	 * \emph{B} contains symbolic references to any element in that sub-model.
	 * </ol>
	 * 
	 * @param mpatch
	 *            MPatch which does not yet have dependencies set.
	 * @return The number of dependencies set.
	 */
	public static int calculateDependencies(MPatchModel mpatch) {

		// 1. get flat list of all changes
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final List<IndepChange> changes = (List) ExtEcoreUtils.collectTypedElements(mpatch.getChanges(), Collections
				.singleton(MPatchPackage.Literals.INDEP_CHANGE), true);

		// 2. analyze changes and build dependency graph
		final Map<IndepChange, Set<IndepChange>> dependencyMap = analyzeChanges(changes);

		// 3. set dependencies between changes
		return addDependencies(dependencyMap);
	}

	/**
	 * This analyzes the given changes and collects them according to common symbolic references.<br>
	 * First, {@link DefaultDiffGrouping#fillMaps(Set)} is used to calculate all relations between the changes and
	 * symbolic references. Second, all changes having any symbolic reference in common, are grouped together.
	 * 
	 * @param changes
	 *            A set of unordered and ungrouped changes.
	 * @return A set of {@link ChangeGroup}s, such that all containing {@link IndepChange}s do not interfere with others
	 *         from a different group.
	 */
	protected static Map<IndepChange, Set<IndepChange>> analyzeChanges(List<IndepChange> changes) {
		/*
		 * initialize intermediate data structure for dependency graph-triple:
		 * 
		 * 1. change -> ref* (set of all uris it uses) 2. ref -> change* (set of all changes in which the reference is
		 * used) 3. ref -> change (contains all references which lead to dependencies)
		 */
		final T3<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>, Map<String, IndepChange>> maps = fillMaps(changes);

		// initialize result data structure for groups and dependencies
		final Queue<IndepChange> queue = new LinkedList<IndepChange>();
		final Set<IndepChange> processed = new HashSet<IndepChange>();
		final Map<IndepChange, Set<IndepChange>> dependencyMap = new HashMap<IndepChange, Set<IndepChange>>();

		// iterate over all changes
		for (final IndepChange change : maps.s.keySet()) {
			if (!processed.contains(change)) {

				final Set<IndepChange> group = new HashSet<IndepChange>();

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

						// if current symbolic reference is a dependency, update the dependencyMap
						if (maps.u.get(ref) != null && !maps.u.get(ref).equals(current)) {
							MPatchUtil.addElementToSetMap(current, maps.u.get(ref), dependencyMap);
						}
					}
				}
				processed.addAll(group);
			}
		}

		return dependencyMap;
	}

	/**
	 * Look for all symbolic references ({@link IElementReference}) in the given set of {@link IndepChange}s.<br>
	 * <ol>
	 * <li>The first part of the result ({@link T3#s}) maps each {@link IndepChange} to all symbolic references it uses.
	 * <li>The second part of the result ({@link T3#t}) maps each symbolic reference (as a String representation
	 * determined by {@link DefaultDiffGrouping#getSymbolicReferenceRepresentative(IElementReference)}) to all
	 * {@link IndepChange}s in which they are used.
	 * <li>The third part of the result ({@link T3#u}) contains all symbolic references which lead to dependencies, i.e.
	 * they contain symbolic references to elements which are part of a {@link IModelDescriptor}.
	 * </ol>
	 * <br>
	 * So this data structure contains a <b>dependency graph</b>.
	 * 
	 * @param changes
	 *            A set of {@link IndepChange}s.
	 * @return A dependency graph between {@link IndepChange}s and symbolic references.
	 */
	// SuppressWarning because of the cast to List<EObject>
	@SuppressWarnings("unchecked")
	protected static T3<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>, Map<String, IndepChange>> fillMaps(
			List<IndepChange> changes) {
		final Map<IndepChange, Set<String>> changeToRefMap = new HashMap<IndepChange, Set<String>>();
		final Map<String, Set<IndepChange>> refToChangeMap = new HashMap<String, Set<IndepChange>>();
		final Map<String, IndepChange> dependencyMap = new HashMap<String, IndepChange>();

		// iterate over all changes and create set of string representatives for its symbolic references
		for (final IndepChange change : changes) {
			final Set<String> references = new HashSet<String>();

			// all symbolic references are contained somewhere according to our meta model
			for (final EReference containment : change.eClass().getEAllContainments()) {

				// TODO: we can maybe optimize by iterating only over IModelDescriptors and IElementReferences

				// there might be single references or many references: collect them before processing them
				final List<EObject> children = new ArrayList<EObject>();
				if (containment.isMany()) {
					children.addAll((List<? extends EObject>) change.eGet(containment));
				} else {
					children.add((EObject) change.eGet(containment));
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
							MPatchUtil.addElementToSetMap(getSymbolicReferenceRepresentative(ref), change,
									refToChangeMap);

							// collect symbolic references of descriptors for strong dependencies
							for (IElementReference descriptorSelfReference : descriptor.getAllSelfReferences()) {
								dependencyMap.put(getSymbolicReferenceRepresentative(descriptorSelfReference), change);
							}
							// for (final String descriptorUri : descriptor.getDescriptorUris()) {
							// dependencyMap.put(descriptorUri, change);
							// }
						}
					} else if (eObject instanceof IElementReference) {

						// collect references for the current change
						references.add(getSymbolicReferenceRepresentative((IElementReference) eObject));
						MPatchUtil.addElementToSetMap(getSymbolicReferenceRepresentative((IElementReference) eObject),
								change, refToChangeMap);
					}
				}
			}
			changeToRefMap.put(change, references);
		}
		return new T3<Map<IndepChange, Set<String>>, Map<String, Set<IndepChange>>, Map<String, IndepChange>>(
				changeToRefMap, refToChangeMap, dependencyMap);
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
	 * Set dependencies between the changes.
	 * 
	 * @param dependencyMap
	 *            The dependency map containing all dependencies.
	 * @return The number of dependencies set.
	 */
	protected static int addDependencies(Map<IndepChange, Set<IndepChange>> dependencyMap) {
		int count = 0;
		for (final IndepChange change : dependencyMap.keySet()) {
			for (IndepChange dep : dependencyMap.get(change)) {
				switch (dep.getChangeKind()) {
				case ADDITION:
					change.getDependsOn().add(dep);
					count++;
					break;
				case DELETION:
					change.getDependants().add(dep);
					count++;
					break;
				default:
					throw new IllegalArgumentException(
							"DependencyMap contains a change as value which neither is an addition nor a deletion!");
				}
			}
		}
		return count;
	}

	/** 
	 * Simple immutable triple wrapper class. 
	 *
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 */
	private static class T3<S, T, U> {
		public final S s;
		public final T t;
		public final U u;

		public T3(S s, T t, U u) {
			this.s = s;
			this.t = t;
			this.u = u;
		}
	}
}
