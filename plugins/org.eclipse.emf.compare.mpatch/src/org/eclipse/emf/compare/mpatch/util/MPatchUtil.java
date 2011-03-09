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
package org.eclipse.emf.compare.mpatch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.ChangeKind;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Helper class for miscellaneous operations related to {@link MPatchModel}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MPatchUtil {

	/**
	 * Create a formatted summary for the given categorized map of changes.<br>
	 * 
	 * @param changeMap
	 *            This is most likely a result of {@link MPatchUtil#collectChanges(List)}.
	 * @return A nicely formatted string summarizing the number of different {@link ChangeKind}s.
	 */
	public static String toSummaryString(Map<ChangeKind, List<IndepChange>> changeMap) {
		String label = "";
		for (final ChangeKind kind : sortChangeKinds(changeMap.keySet())) {
			label += (label.length() == 0 ? "" : ", ") + changeMap.get(kind).size() + " " + kind.name().toLowerCase()
					+ (changeMap.get(kind).size() == 1 ? "" : "s");
		}
		return (label.length() == 0 ? "(empty)" : "(" + label + ")");
	}

	private static List<ChangeKind> sortChangeKinds(Collection<ChangeKind> changeKinds) {
		final List<ChangeKind> result = new ArrayList<ChangeKind>(8);
		if (changeKinds.contains(ChangeKind.GROUP))
			result.add(ChangeKind.GROUP);
		if (changeKinds.contains(ChangeKind.ADDITION))
			result.add(ChangeKind.ADDITION);
		if (changeKinds.contains(ChangeKind.DELETION))
			result.add(ChangeKind.DELETION);
		if (changeKinds.contains(ChangeKind.MOVE))
			result.add(ChangeKind.MOVE);
		if (changeKinds.contains(ChangeKind.CHANGE))
			result.add(ChangeKind.CHANGE);
		if (changeKinds.contains(ChangeKind.UNKNOWN))
			result.add(ChangeKind.UNKNOWN);
		if (changeKinds.size() > result.size())
			throw new UnsupportedOperationException("Unknown change kind detected! Please implement it: "
					+ changeKinds.removeAll(result));
		return result;
	}

	/**
	 * Categorize the given changes and their subchanges.
	 * 
	 * @param changes
	 *            A list of {@link IndepChange}s.
	 * @return A map containing all subchanges categorized by their type.
	 */
	public static Map<ChangeKind, List<IndepChange>> collectChanges(List<IndepChange> changes) {
		final Map<ChangeKind, List<IndepChange>> map = new HashMap<ChangeKind, List<IndepChange>>();
		for (final IndepChange subChange : changes) {
			addChangeToMap(map, subChange);
			if (subChange instanceof ChangeGroup) {
				mergeChangeMaps(map, collectChanges(subChange));
			}
		}
		return map;
	}

	/**
	 * Collect all subchanges and categorize them.
	 * 
	 * @param change
	 *            Any {@link IndepChange}; if it is a {@link ChangeGroup} then all subchanges will be collected and
	 *            categorized.
	 * @return A map containing all subchanges categorized by their type.
	 */
	public static Map<ChangeKind, List<IndepChange>> collectChanges(IndepChange change) {
		if (change instanceof ChangeGroup) {
			return collectChanges(((ChangeGroup) change).getSubChanges());
		} else {
			final Map<ChangeKind, List<IndepChange>> map = new HashMap<ChangeKind, List<IndepChange>>();
			addChangeToMap(map, change);
			return map;
		}
	}

	/**
	 * Merge two categorized change maps. Note that the result is stored in the first map, i.e. the first map is
	 * extended whereas the second map remains unchanged.<br>
	 * <br>
	 * <i>Note: if the current implementation is too slow, optimize it with more clever list merge operations!</i>
	 * 
	 * @param map1
	 *            this map is extended with the second map
	 * @param map2
	 *            the elements in this map are added to the first map
	 */
	private static void mergeChangeMaps(Map<ChangeKind, List<IndepChange>> map1, Map<ChangeKind, List<IndepChange>> map2) {
		for (final List<IndepChange> list : map2.values()) {
			for (final IndepChange change : list) {
				addChangeToMap(map1, change);
			}
		}
	}

	/**
	 * Add a change to a categorized map of changes.
	 */
	public static void addChangeToMap(Map<ChangeKind, List<IndepChange>> map, IndepChange change) {
		List<IndepChange> list = map.get(change.getChangeKind());
		if (list == null) {
			list = new ArrayList<IndepChange>();
			map.put(change.getChangeKind(), list);
		}
		list.add(change);
	}

	/**
	 * Helper method to add an element to a map of set of elements.
	 */
	public static <T, S> void addElementToSetMap(T key, S element, Map<T, Set<S>> map) {
		Set<S> set = map.get(key);
		if (set == null) {
			set = new HashSet<S>();
			map.put(key, set);
		}
		set.add(element);
	}

	/**
	 * Helper method to add an element to a map of list of elements.
	 */
	public static <T, S> void addElementToListMap(T key, S element, Map<T, List<S>> map) {
		List<S> set = map.get(key);
		if (set == null) {
			set = new ArrayList<S>();
			map.put(key, set);
		}
		set.add(element);
	}

	/**
	 * Format a given value, i.e. convert it to a String and shorten it if it is too long.
	 * 
	 * @param obj
	 *            The object to format.
	 * @return A formatted representation of the object.
	 */
	public static String formatValue(Object obj) {
		if (obj == null)
			return "null";
		final String value = obj.toString();
		return shorten(value, 10);
	}

	/**
	 * Format a symbolic reference.
	 * 
	 * @param symref
	 *            A symbolic reference.
	 * @return A formatted representation of the symbolic references.
	 */
	public static String formatSymrefLabel(IElementReference symref) {
		if (symref == null)
			return "<null>";

		String label = symref.getLabel();
		if (label == null || label.length() == 0) {
			label = symref.getUriReference();
			if (label == null || label.length() == 0) {
				label = "<unknown>";
			} else {
				label = label.substring(label.lastIndexOf("#") + 1);
				label = label.substring(label.lastIndexOf("/") + 1);
			}
		}

		if (symref.getType() != null) {
			final String type = "<" + symref.getType().getName() + ">";

			// maybe some label already has a type? (e.g. in UML)
			final String shortLabel = shorten(label, 40);
			if (shortLabel.contains(type) || shortLabel.replaceAll(" ", "").contains(type)) {
				label = shortLabel;
			} else
				label = shorten(label, 30) + " " + type;
		} else
			label = shorten(label, 35);

		return "[" + label + "]";
	}

	/**
	 * Create the text for a symbolic reference. This should only be called by itemproviders of symbolic references!
	 * Callers might extend the string.
	 * 
	 * @param symref
	 *            The symbolic reference.
	 * @return The generic text for a symbolic reference.
	 */
	public static String getTextForSymref(IElementReference symref) {
		final EStructuralFeature containment = symref.eContainingFeature();
		/*
		 * The containment name was too long and too confusing for non-experts.
		 * Use it only if we cannot find a better name!
		 */
//		final String prefix = containment == null ? "<no owner>" : containment.getName();
		final String prefix;
		if (MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT.equals(containment)) {
			prefix = "target";
		} else if (MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT.equals(containment)) {
			prefix = "new parent";
		} else if (MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT.equals(containment)) {
			prefix = "old parent";
		} else if (MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE.equals(containment)) {
			prefix = "existing element";
		} else if (MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE.equals(containment)) {
			prefix = "changed reference";
		} else if (MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE.equals(containment)) {
			prefix = "old reference";
		} else if (MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE.equals(containment)) {
			prefix = "new reference";
		} else if (containment != null && "value".equals(containment.getName())) {
			// this is most likely one of the cross references in model descriptors (whose meta model we cannot access here)
			prefix = "cross reference";
		} else {
			prefix = containment == null ? "<no owner>" : containment.getName();
		}
		final int lower = symref.getLowerBound();
		final int upper = symref.getUpperBound();
		final String bounds = "[" + lower + (lower != upper ? ".." + (upper < 0 ? "*" : upper) : "") + "]";
		return prefix + " " + bounds;
	}

	private static String shorten(String string, int limit) {
		if (string.length() > limit)
			return string.substring(0, limit - 2) + "...";
		else
			return string;
	}

	/**
	 * Collect all cross references in the differences. A cross reference is a reference from one change to a model
	 * element outside that change. The complete list of cross-references is:
	 * <ul>
	 * <li> {@link IModelDescriptor#getAllCrossReferences()}
	 * <li> {@link IndepMoveElementChange#getOldParent()} and {@link IndepMoveElementChange#getNewParent()}
	 * <li> {@link IndepAddRemReferenceChange#getChangedReference()}
	 * <li> {@link IndepUpdateReferenceChange#getOldReference()} and {@link IndepUpdateReferenceChange#getNewReference()}
	 * </ul>
	 * 
	 * @param changes
	 *            A collection of {@link IndepChange}s.
	 * @return A map of cross references pointing to their {@link IndepChange}.
	 */
	public static Map<IElementReference, IndepChange> collectCrossReferences(List<? extends IndepChange> changes) {
		final Map<IElementReference, IndepChange> result = new LinkedHashMap<IElementReference, IndepChange>();

		final Set<EClass> types = new HashSet<EClass>();
		types.add(MPatchPackage.eINSTANCE.getIndepAddRemElementChange());
		types.add(MPatchPackage.eINSTANCE.getIndepMoveElementChange());
		types.add(MPatchPackage.eINSTANCE.getIndepAddRemReferenceChange());
		types.add(MPatchPackage.eINSTANCE.getIndepUpdateReferenceChange());
		final List<EObject> crossReferenceOwners = ExtEcoreUtils.collectTypedElements(changes, types, true);

		for (final EObject obj : crossReferenceOwners) {
			if (obj instanceof IndepAddRemElementChange) {
				final IndepAddRemElementChange change = (IndepAddRemElementChange) obj;
				for (IElementReference ref : change.getSubModel().getAllCrossReferences()) {
					result.put(ref, change);
				}
			} else if (obj instanceof IndepMoveElementChange) {
				final IndepMoveElementChange change = (IndepMoveElementChange) obj;
				result.put(change.getOldParent(), change);
				result.put(change.getNewParent(), change);
			} else if (obj instanceof IndepAddRemReferenceChange) {
				final IndepAddRemReferenceChange change = (IndepAddRemReferenceChange) obj;
				result.put(change.getChangedReference(), change);
			} else if (obj instanceof IndepUpdateReferenceChange) {
				final IndepUpdateReferenceChange change = (IndepUpdateReferenceChange) obj;
				if (change.getNewReference() != null)
					result.put(change.getNewReference(), change);
				if (change.getOldReference() != null)
					result.put(change.getOldReference(), change);
			}
		}

		return result;
	}

	/**
	 * Collect all URIs of elements which are described by model descriptors in the given tree of changes.
	 * 
	 * @param changes
	 *            A list of changes.
	 * @return A mapping from the URI of described model elements to their descriptors.
	 */
	public static Map<String, IModelDescriptor> collectDescriptors(List<? extends IndepChange> changes) {
		final HashMap<String, IModelDescriptor> result = new HashMap<String, IModelDescriptor>();
		final Set<EClass> types = Collections.singleton(MPatchPackage.eINSTANCE.getIModelDescriptor());
		final List<EObject> descriptors = ExtEcoreUtils.collectTypedElements(changes, types, true);
		for (EObject object : descriptors) {
			final IModelDescriptor descriptor = (IModelDescriptor) object;
			result.put(descriptor.getSelfReference().getUriReference(), descriptor);
		}
		return result;
	}

	/**
	 * Get the {@link IndepChange} in which the given {@link IElementReference} is contained.
	 * 
	 * @return The change in which <code>ref</code> is (indirectly) contained or <code>null</code>, if it is not.
	 */
	public static IndepChange getChangeFor(IElementReference ref) {
		EObject parent = ref;
		while (!(parent instanceof IndepChange) && parent != null)
			parent = parent.eContainer();
		return parent instanceof IndepChange ? (IndepChange) parent : null;
	}

	/**
	 * The addition and deletion of elements includes their features, {@link EAttribute}s and {@link EReference}s, to be
	 * concrete. This method determines whether the feature is relevant to be stored, that is, whether it is
	 * serializable. Criteria being checked are whether it is transient, derived, and changeable. Moreover, the generic
	 * type is filtered.
	 * 
	 * @param feature
	 *            A structured feature.
	 * @return Whether it is relevant to be stored for deletions and additions, or not.
	 */
	public static boolean isRelevantFeature(EStructuralFeature feature) {
		// ETypedElement.eGenericType is introduced in EMF since 2.5 and is more or less derived from eType.
		// eGenericType (child!) is in particular set if eType (reference) is set!
		// So I assume it is safe to ignore all generic types in the differences.
		// At least all tests pass so far...
		if (EcorePackage.Literals.ETYPED_ELEMENT__EGENERIC_TYPE.equals(feature)
				|| EcorePackage.Literals.ECLASS__EGENERIC_SUPER_TYPES.equals(feature))
			return false;

		return !feature.isTransient() && !feature.isDerived() && feature.isChangeable();
	}

	/**
	 * Iterate over the given differences and remove all {@link DiffElement}s which are of the type or subtype as
	 * specified in <code>diffTypes</code>. If there are empty {@link DiffGroup}s left, they will be erased as well.
	 * 
	 * @param diff
	 *            Differences.
	 * @param diffTypes
	 *            The types which shall be removed.
	 * @return A list of all removed {@link DiffElement}s.
	 */
	public static List<DiffElement> removeDiffElementOfType(DiffModel diff, Set<EClass> diffTypes) {
		final List<DiffElement> removed = new ArrayList<DiffElement>();

		// don't ever delete diff groups explicitly
		if (diffTypes.contains(DiffPackage.Literals.DIFF_GROUP))
			throw new IllegalArgumentException("DiffGroups are not supported! This would erase all differences!");

		// collect and iterate over all elements to be removed
		final List<EObject> diffElementsToRemove = ExtEcoreUtils.collectTypedElements(diff.getDifferences(), diffTypes,
				true);
		for (EObject obj : diffElementsToRemove) {
			EObject parent = obj.eContainer();

			// remove element and store in result!
			EcoreUtil.remove(obj);
			removed.add((DiffElement) obj);

			// if parent is empty group, remove it!
			while (parent instanceof DiffElement && ((DiffElement) parent).getSubDiffElements().isEmpty()) {
				final EObject newParent = parent.eContainer();
				EcoreUtil.remove(parent);
				parent = newParent;
			}
		}
		return removed;
	}
}
