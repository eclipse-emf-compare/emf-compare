package org.eclipse.emf.compare.uml2.diff;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.common.util.SubsetSupersetEObjectEList;

public class UMLDiffExtensionPostProcessor implements IPostProcessor {
	/** UML2 extensions factories. */
	private Set<IDiffExtensionFactory> uml2ExtensionFactories;

	/**
	 * UML has notions of "subset" and "superset" features, which can be multiply derived but can also hold
	 * values of their own.
	 * <p>
	 * For example, Association#memberEnds is a superset of Association#ownedEnds which in turn is a superset
	 * of Association#navigableOwnedEnds.
	 * </p>
	 * <p>
	 * If we add a value to "ownedEnds", it will automatically be added to "memberEnds" but not to
	 * "navigableOwnedEnds". EMF Compare will detect two differences : 'value has been added to ownedEnds' and
	 * 'value has been added to memberEnds'. We need to ignore the diff on memberEnds, but not the one on
	 * ownedEnds (otherwise, and since these are not simple equivalences, the value would be added twice to
	 * memberEnds, and once in ownedEnds).
	 * </p>
	 * <p>
	 * Likewise, if we add a value to navigableOwnedEnds, we must still ignore the diff on memberEnds, but
	 * this time and for that value we also need to ignore the diff on ownedEnds, lest we end up post-merge
	 * with three identical values in memberEnds, two in ownedEnds and one in navigableOwnedEnds.
	 * </p>
	 * <p>
	 * Since there is no API way to determine which references are subsets of another, we will have to resort
	 * to casting the reference's value in {@link SubsetSupersetEObjectEList} and reflexively access its
	 * "subsetFeatureIDs" protected field.
	 * </p>
	 */
	private static final Field SUBSET_FEATURES_FIELD = getSubsetField();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// Not needed here.
	}

	private void removeDuplicateDiffs(Comparison comparison) {
		final Set<Diff> removeMe = Sets.newLinkedHashSet();
		for (Diff input : comparison.getDifferences()) {
			if (!(input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isMany())) {
				// Nothing to see here
				continue;
			}

			final EObject matchSide = getSourceSide(input);
			final Object value = ReferenceUtil.safeEGet(matchSide, ((ReferenceChange)input).getReference());
			// The only diffs that can be duplicated are those that can have subsets
			if (value instanceof SubsetSupersetEObjectEList<?>) {
				final int[] subsetsFeatures = getSubsetFeatures((SubsetSupersetEObjectEList<?>)value);

				// If we have subsets and this is a MOVE, ignore the Diff altogether
				if (subsetsFeatures.length > 0 && input.getKind() == DifferenceKind.MOVE) {
					removeMe.add(input);
				} else {
					// Check each subset : if it contains the same value as the input then it is a duplicate
					final Diff duplicate = findDuplicatedDiffOnLowestSubset(input, matchSide, subsetsFeatures);
					if (duplicate != null) {
						// We have a duplicate on a subset. This diff will be removed from the comparison
						// model, but before that we need to copy all of its requires and refines
						removeMe.add(input);
						copyRequirements(input, duplicate);
					}
				}
			}
		}

		for (Diff sentenced : removeMe) {
			EcoreUtil.delete(sentenced);
		}
	}

	private void copyRequirements(Diff source, Diff target) {
		for (Diff requiredBy : source.getRequiredBy()) {
			if (requiredBy != target) {
				target.getRequiredBy().add(requiredBy);
			}
		}
		for (Diff requires : source.getRequires()) {
			if (requires != target) {
				target.getRequires().add(requires);
			}
		}
		if (source.getEquivalence() != null) {
			source.getEquivalence().getDifferences().add(target);
		}
	}

	private Diff findDuplicatedDiffOnLowestSubset(Diff input, EObject matchSide, int[] subsetsFeatures) {
		final EClass clazz = matchSide.eClass();
		final Object diffValue = ((ReferenceChange)input).getValue();
		final int[] actualIDs = convertFeatureIDs(clazz, subsetsFeatures);

		Diff lowestDuplicate = null;
		final Iterator<ReferenceChange> siblings = Iterables.filter(input.getMatch().getDifferences(),
				ReferenceChange.class).iterator();
		while (siblings.hasNext() && lowestDuplicate == null) {
			final ReferenceChange sibling = siblings.next();
			final int refID = sibling.getReference().getFeatureID();
			final Object siblingValue = sibling.getValue();

			if (sibling.getKind() == input.getKind() && Ints.contains(actualIDs, refID)
					&& siblingValue == diffValue) {
				// This is a duplicate... but it may be a duplicate itself.
				// We have to go down to the lowest level.
				final Object subset = matchSide.eGet(clazz.getEStructuralFeature(refID), false);
				if (subset instanceof SubsetSupersetEObjectEList<?>) {
					final int[] lowerSubsets = getSubsetFeatures((SubsetSupersetEObjectEList<?>)subset);
					final Diff lowerDuplicate = findDuplicatedDiffOnLowestSubset(sibling, matchSide,
							lowerSubsets);
					if (lowerDuplicate != null) {
						lowestDuplicate = lowerDuplicate;
					} else {
						lowestDuplicate = sibling;
					}
				} else {
					lowestDuplicate = sibling;
				}
			}
		}

		return lowestDuplicate;
	}

	private int[] convertFeatureIDs(EClass clazz, int[] ids) {
		int[] result = new int[ids.length];
		for (int i = 0; i < ids.length; i++) {
			result[i] = clazz.getEStructuralFeature(ids[i]).getFeatureID();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
		removeDuplicateDiffs(comparison);

		final Map<Class<? extends Diff>, IDiffExtensionFactory> mapUml2ExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories();
		uml2ExtensionFactories = new HashSet<IDiffExtensionFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff umlDiff : comparison.getDifferences()) {
			if (umlDiff instanceof UMLDiff) {
				final Class<?> classDiffElement = umlDiff.eClass().getInstanceClass();
				final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, (UMLDiff)umlDiff);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postComparison(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 * @param diffModelCrossReferencer
	 *            The cross referencer.
	 */
	private void applyManagedTypes(Diff element) {
		for (IDiffExtensionFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				final Diff extension = factory.create(element);
				final Match match = factory.getParentMatch(element);
				match.getDifferences().add(extension);
			}
		}
	}

	/**
	 * Retrieves the value of the <i>subsetFeatureIDs</i> field of the given list. Note that this will never
	 * return <code>null</code>, but an empty array instead.
	 * 
	 * @param list
	 *            The list for which we need the subset feature IDs.
	 * @return The IDs of the subsets from which the given lists derives its values.
	 * @see #SUBSET_FEATURES_FIELD
	 */
	private static int[] getSubsetFeatures(SubsetSupersetEObjectEList<?> list) {
		Object value = null;
		try {
			value = SUBSET_FEATURES_FIELD.get(list);
		} catch (IllegalArgumentException e) {
			// Ignore, cannot happen
		} catch (IllegalAccessException e) {
			// Ignore, cannot happen
		}
		if (value instanceof int[]) {
			return (int[])value;
		}
		return new int[0];
	}

	/**
	 * This will allow us to retrieve the "subsetFeatureIDs" protected field of the
	 * {@link SubsetSupersetEObjectEList} class and set it accessible for further reflexive access. More on
	 * this on the {@link #SUBSET_FEATURES_FIELD field}'s description.
	 * 
	 * @return The {@link SubsetSupersetEObjectEList#subsetFeatureIDs} field, after having made it accessible.
	 * @see #SUBSET_FEATURES_FIELD
	 */
	private static Field getSubsetField() {
		try {
			final Field subsetIDs = SubsetSupersetEObjectEList.class.getDeclaredField("subsetFeatureIDs"); //$NON-NLS-1$
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					subsetIDs.setAccessible(true);
					return null;
				}
			});
			return subsetIDs;
		} catch (SecurityException e) {
			// Ignore, cannot happen
		} catch (NoSuchFieldException e) {
			// Ignore, cannot happen
		}
		return null;
	}

	/**
	 * Retrieves the EObject holding the reference on which we detected a difference.
	 * 
	 * @param input
	 *            The difference for which we need a "holder" object.
	 * @return The EObject holding the reference on which we detected a difference.
	 */
	private static EObject getSourceSide(Diff input) {
		// Note that we know this diff is not a "CHANGE" as its reference is multi-valued
		final Match match = input.getMatch();
		final EObject matchSide;
		if (input.getKind() == DifferenceKind.DELETE) {
			if (match.getOrigin() != null) {
				matchSide = match.getOrigin();
			} else if (input.getSource() == DifferenceSource.LEFT) {
				matchSide = match.getRight();
			} else {
				// Should never happen
				matchSide = match.getLeft();
			}
		} else if (input.getSource() == DifferenceSource.LEFT) {
			matchSide = match.getLeft();
		} else {
			matchSide = match.getRight();
		}
		return matchSide;
	}
}
