/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.framework;

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;

/**
 * Provides specific assertions for EMF Compare tests.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class EMFCompareAssert {
	/**
	 * This can be used to check whether all objects of the given list have a corresponding {@link Match} in
	 * the given {@link Comparison}. If one of said EObjects is not matched, we will check whether it is
	 * included in the given <code>scope</code> if it is a {@link FilterComparisonScope}.
	 * 
	 * @param eObjects
	 *            The list of EObjects for which we need corresponding {@link Match}es.
	 * @param comparison
	 *            The {@link Comparison} in which we are to check for Matches.
	 * @param scope
	 *            The scope that has been used to create the given <code>comparison</code>.
	 */
	public static void assertAllMatched(List<EObject> eObjects, Comparison comparison, IComparisonScope scope) {
		final Predicate<? super EObject> scopeFilter;
		if (scope instanceof FilterComparisonScope) {
			scopeFilter = getResourceChildrenFilteringPredicate((FilterComparisonScope)scope);
		} else {
			scopeFilter = Predicates.alwaysTrue();
		}

		final Iterator<EObject> eObjectIterator = eObjects.iterator();
		while (eObjectIterator.hasNext()) {
			final EObject eObject = eObjectIterator.next();
			final Match match = comparison.getMatch(eObject);
			assertTrue(eObject + " has no match", match != null || !scopeFilter.apply(eObject));
		}
	}

	/**
	 * Asserts that the given list of differences contains a ReferenceChange describing the given change for a
	 * single-valued reference. The {@code differences} list will be updated by this call, removing the
	 * corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular reference change.
	 * @param qualifiedName
	 *            Qualified name of the EObject which reference we expect to have changed.
	 * @param referenceName
	 *            Name of the reference which values we expect to have changed. <b>Note</b> that we expect
	 *            this reference to be single-valued.
	 * @param fromQualifiedName
	 *            Qualified name of the original value of this reference (can be either the origin or right
	 *            value).
	 * @param toQualifiedName
	 *            Qualified name of the value to which this reference has been changed (can be either left or
	 *            right in three-way comparisons, only left for two-way).
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#changedReference(String, String, String, String)
	 */
	public static void assertChangedReference(List<Diff> differences, String qualifiedName,
			String referenceName, String fromQualifiedName, String toQualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> changedReferenceOnSide = and(fromSide(side), changedReference(
				qualifiedName, referenceName, fromQualifiedName, toQualifiedName));
		final Diff matchingDiff = removeFirst(differences.iterator(), changedReferenceOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Asserts that the given list of differences contains a ReferenceChange describing the removal of a value
	 * from a multi-valued reference. The {@code differences} list will be updated by this call, removing the
	 * corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular reference change.
	 * @param qualifiedName
	 *            Qualified name of the EObject which reference we expect to have changed.
	 * @param referenceName
	 *            Name of the reference which values we expect to have changed. <b>Note</b> that we expect
	 *            this reference to be multi-valued.
	 * @param removedValueQualifiedName
	 *            Qualified name of the value we expect to have been removed from that reference's list of
	 *            values.
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#removedFromReference(String, String, String)
	 */
	public static void assertRemovedFromReference(List<Diff> differences, String qualifiedName,
			String referenceName, String removedValueQualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> removedFromReferenceOnSide = and(fromSide(side), removedFromReference(
				qualifiedName, referenceName, removedValueQualifiedName));
		final Diff matchingDiff = removeFirst(differences.iterator(), removedFromReferenceOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Asserts that the given list of differences contains a ReferenceChange describing the addition of a
	 * value into a multi-valued reference. The {@code differences} list will be updated by this call,
	 * removing the corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular reference change.
	 * @param qualifiedName
	 *            Qualified name of the EObject which reference we expect to have changed.
	 * @param referenceName
	 *            Name of the reference which values we expect to have changed. <b>Note</b> that we expect
	 *            this reference to be multi-valued.
	 * @param addedValueQualifiedName
	 *            Qualified name of the value we expect to have been added to that reference's list of values.
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#addedToReference(String, String, String)
	 */
	public static void assertAddedToReference(List<Diff> differences, String qualifiedName,
			String referenceName, String addedValueQualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> addedToReferenceOnSide = and(fromSide(side), addedToReference(
				qualifiedName, referenceName, addedValueQualifiedName));
		final Diff matchingDiff = removeFirst(differences.iterator(), addedToReferenceOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Asserts that the given list of differences contains an AttributeChange describing the given change for
	 * a single-valued attribute. The {@code differences} list will be updated by this call, removing the
	 * corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular attribute change.
	 * @param qualifiedName
	 *            Qualified name of the EObject which attribute we expect to have changed.
	 * @param attributeName
	 *            Name of the attribute which values we expect to have changed. <b>Note</b> that we expect
	 *            this attribute to be single-valued.
	 * @param fromValue
	 *            The original value of this attribute. Can be either the origin or right value.
	 * @param toValue
	 *            The value to which this attribute has been changed. Can be either left or right for a
	 *            three-way comparison, only left in two-way.
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#changedAttribute(String, String, Object, Object)
	 */
	public static void assertChangedAttribute(List<Diff> differences, String qualifiedName,
			String attributeName, Object fromValue, Object toValue, DifferenceSource side) {
		final Predicate<? super Diff> changedAttributeOnSide = and(fromSide(side), changedAttribute(
				qualifiedName, attributeName, fromValue, toValue));
		final Diff matchingDiff = removeFirst(differences.iterator(), changedAttributeOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Asserts that the given list of differences contains a ReferenceChange describing the given element
	 * addition. This is only meant for containment changes. The {@code differences} list will be updated by
	 * this call, removing the corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular element addition.
	 * @param qualifiedName
	 *            Qualified name of the EObject we expect to have been added.
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#added(String)
	 */
	public static void assertAdded(List<Diff> differences, String qualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> addedOnSide = and(fromSide(side), added(qualifiedName));
		final Diff matchingDiff = removeFirst(differences.iterator(), addedOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Asserts that the given list of differences contains a ReferenceChange describing the given element
	 * deletion. This is only meant for containment changes. The {@code differences} list will be updated by
	 * this call, removing the corresponding Diff if it could be located.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * so that we can compare it to the given qualified names.
	 * </p>
	 * 
	 * @param differences
	 *            List of differences in which to seek for a particular element deletion.
	 * @param qualifiedName
	 *            Qualified name of the EObject we expect to have been deleted.
	 * @param side
	 *            The side from which we expect this diff to originate.
	 * @see EMFComparePredicates#removed(String)
	 */
	public static void assertRemoved(List<Diff> differences, String qualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> removedOnSide = and(fromSide(side), removed(qualifiedName));
		final Diff matchingDiff = removeFirst(differences.iterator(), removedOnSide);
		assertNotNull(matchingDiff);
	}

	/**
	 * Retrieves the Predicate that is used by the given scope in order to filter out Resource children.
	 * <p>
	 * This uses reflection to access a protected field, and is only meant for testing purposes.
	 * </p>
	 * 
	 * @param scope
	 *            The scope which predicate we need to retrieve.
	 * @return The predicate that was used by the given scope to filter out Resource children.
	 */
	@SuppressWarnings("unchecked")
	private static Predicate<? super EObject> getResourceChildrenFilteringPredicate(
			FilterComparisonScope scope) {
		final String fieldName = "resourceContentFilter"; //$NON-NLS-1$
		try {
			final Field field = FilterComparisonScope.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (Predicate<? super EObject>)field.get(scope);
		} catch (Exception e) {
			fail("Could not retrieve the filtering predicate of " + scope.getClass().getName()); //$NON-NLS-1$
		}
		// Unreachable code
		return null;
	}

	/**
	 * Removes and returns the first element returned by {@code iterator} that satisfies the given predicate.
	 * 
	 * @param iterator
	 *            Iterators over which elements we are to iterate.
	 * @param predicate
	 *            The predicate which needs to be satisified.
	 * @return The first element of {@code iterator} that satisfies {@code predicate}, {@code null} if none.
	 */
	private static <T> T removeFirst(Iterator<T> iterator, Predicate<? super T> predicate) {
		while (iterator.hasNext()) {
			T element = iterator.next();
			if (predicate.apply(element)) {
				iterator.remove();
				return element;
			}
		}
		return null;
	}
}
