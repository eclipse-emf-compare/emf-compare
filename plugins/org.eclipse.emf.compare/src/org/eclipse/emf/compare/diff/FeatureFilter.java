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
package org.eclipse.emf.compare.diff;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * {@link FeatureFilter}s will be used by the default implemention of a diff engine in order to determine
 * which features it is to check for differences. Any feature that is not returned by this filter will be
 * ignored by the diff engine.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FeatureFilter {
	/**
	 * The diff engine expects this to return the set of references that need to be checked for differences
	 * for the given {@link Match} element.
	 * <p>
	 * This default implementation assumes that all three sides of the match are conform to the same
	 * metamodel, and simply returns one of the side's {@link EClass#getEAllReferences()
	 * side.eClass().getEAllReferences()}, ignoring only the derived and container.
	 * </p>
	 * 
	 * @param match
	 *            The match for which we are trying to compute differences.
	 * @return The set of references that are to be checked by the diff engine. May be an empty iterator, in
	 *         which case no difference will be detected on any of this <code>match</code>'s references.
	 */
	public Iterator<EReference> getReferencesToCheck(final Match match) {
		final EClass clazz;
		if (match.getLeft() != null) {
			clazz = match.getLeft().eClass();
		} else if (match.getRight() != null) {
			clazz = match.getRight().eClass();
		} else {
			clazz = match.getOrigin().eClass();
		}
		return Iterators.filter(clazz.getEAllReferences().iterator(), new Predicate<EReference>() {
			public boolean apply(EReference input) {
				return !isIgnoredReference(match, input);
			}
		});
	}

	/**
	 * The diff engine expects this to return the set of attributes that need to be checked for differences
	 * for the given {@link Match} element.
	 * <p>
	 * This default implementation assumes that all three sides of the match are conform to the same
	 * metamodel, and simply returns one of the side's {@link EClass#getEAllAttributes()
	 * side.eClass().getEAllAttributes()}, ignoring only the derived.
	 * </p>
	 * 
	 * @param match
	 *            The match for which we are trying to compute differences.
	 * @return The set of attributes that are to be checked by the diff engine. May be an empty iterator, in
	 *         which case no difference will be detected on any of this <code>match</code>'s attributes.
	 */
	public Iterator<EAttribute> getAttributesToCheck(Match match) {
		final EClass clazz;
		if (match.getLeft() != null) {
			clazz = match.getLeft().eClass();
		} else if (match.getRight() != null) {
			clazz = match.getRight().eClass();
		} else {
			clazz = match.getOrigin().eClass();
		}
		return Iterators.filter(clazz.getEAllAttributes().iterator(), new Predicate<EAttribute>() {
			public boolean apply(EAttribute input) {
				return !isIgnoredAttribute(input);
			}
		});
	}

	/**
	 * Tells the diff engine whether the given feature should be checked for changed in the ordering or not.
	 * This default implementation considers that any "ordered" or "containment" feature should be checked for
	 * changes.
	 * 
	 * @param feature
	 *            The feature we are currently checking.
	 * @return <code>true</code> if the diff engine should consider the ordering of this feature,
	 *         <code>false</code> otherwise.
	 */
	public boolean checkForOrderingChanges(EStructuralFeature feature) {
		if (feature.isMany()) {
			return feature.isOrdered() || feature instanceof EReference
					&& ((EReference)feature).isContainment();
		}
		return false;
	}

	/**
	 * Checks whether the given reference is set on at least one of the three sides of the given match.
	 * 
	 * @param reference
	 *            The reference we need to be set.
	 * @param match
	 *            The match for which values we need to check the given reference.
	 * @return {@code true} if the given reference is set on at least one of the three sides of the given
	 *         match.
	 */
	protected boolean referenceIsSet(EReference reference, Match match) {
		if (match.getLeft() != null && match.getLeft().eIsSet(reference)) {
			return true;
		}
		return (match.getRight() != null && match.getRight().eIsSet(reference))
				|| (match.getOrigin() != null && match.getOrigin().eIsSet(reference));
	}

	/**
	 * This will be used by {@link #getReferencesToCheck(Match)} in order to determine whether a given
	 * reference should be ignored.
	 * 
	 * @param match
	 *            The match from which was taken that particular reference.
	 * @param reference
	 *            The candidate that might be ignored.
	 * @return {@code true} if that reference should be ignored by the comparison engine.
	 */
	protected boolean isIgnoredReference(Match match, EReference reference) {
		if (reference != null) {
			// ignore the derived, container or transient
			if (!reference.isDerived() && !reference.isContainer() && !reference.isTransient()) {
				/*
				 * EGenericTypes are usually "mutually derived" references that are handled through specific
				 * means in ecore (eGenericSuperTypes and eSuperTypes, EGenericType and eType...). As these
				 * aren't even shown to the user, we wish to avoid detection of changes on them.
				 */
				// Otherwise if this reference is not set on any side, no use checking it
				return reference.getEType() == EcorePackage.eINSTANCE.getEGenericType()
						|| !referenceIsSet(reference, match);
			}
		}
		return true;
	}

	/**
	 * This will be used by {@link #getAttributesToCheck(Match)} in order to determine whether a given
	 * attribute should be ignored.
	 * 
	 * @param attribute
	 *            The candidate that might be ignored.
	 * @return {@code true} if that attribute should be ignored by the comparison engine.
	 */
	protected boolean isIgnoredAttribute(EAttribute attribute) {
		return attribute == null || attribute.isDerived() || attribute.isTransient();
	}
}
