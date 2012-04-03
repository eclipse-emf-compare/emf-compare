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
package org.eclipse.emf.compare.tests.framework.predicates;

import static com.google.common.base.Predicates.and;

import com.google.common.base.Predicate;

import java.util.Iterator;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * This class will provide a number of Predicates that can be used by EMF Compare tests to check for
 * particular diffs.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFComparePredicates {
	/**
	 * This class does not need to be instantiated.
	 */
	private EMFComparePredicates() {
		// Hides default constructor
	}

	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> changed(final String qualifiedName, final EReference reference,
			final String leftQualifiedName, final String rightQualifiedName) {
		final Predicate<? super Diff> valuesMatch = new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ReferenceChange && ((ReferenceChange)input).getReference() == reference) {
					final Match match = input.getMatch();
					return match(match.getLeft(), leftQualifiedName)
							&& match(match.getRight(), rightQualifiedName);
				}
				return false;
			}
		};
		return and(ofKind(DifferenceKind.CHANGE), onEObject(qualifiedName), valuesMatch);
	}

	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> changed(final String qualifiedName, final EAttribute attribute,
			final String leftValue, final String rightValue) {
		final Predicate<? super Diff> valuesMatch = new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof AttributeChange && ((AttributeChange)input).getAttribute() == attribute) {
					final Match match = input.getMatch();
					// return match(match.getLeft(), leftQualifiedName)
					// && match(match.getRight(), rightQualifiedName);
				}
				return false;
			}
		};
		return and(ofKind(DifferenceKind.CHANGE), onEObject(qualifiedName), valuesMatch);
	}

	/**
	 * This predicate can be used to check that a given Diff represents the addition of {@code eObject} inside
	 * the given {@code container}.
	 * 
	 * @param eObject
	 *            The EObject that we expect to have been added inside a particular container.
	 * @param container
	 *            The expected container of {@code eObject}.
	 * @return The created predicate.
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> addedIn(final EObject eObject, final EObject container) {
		return and(ofKind(DifferenceKind.ADD), onEObject(container), valueIs(eObject));
	}

	/**
	 * This predicate can be used to check that a given Diff represents the deletion of {@code eObject} from
	 * the given {@code container}.
	 * 
	 * @param eObject
	 *            The EObject that we expect to have been removed from a particular container.
	 * @param container
	 *            The expected previous container of {@code eObject}.
	 * @return The created predicate.
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> removedFrom(final EObject eObject, final EObject container) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(container), valueIs(eObject));
	}

	/**
	 * This predicate can be used to check whether a given Diff represents the addition of an EObject matching
	 * the given qualified name. Namely, it will check that that Diff is a ReferenceChange, that one of its
	 * Match sides correspond to the given qualified name's ancestors, and that its value correspond to the
	 * given qualified name's last segment.
	 * <p>
	 * For example, {@code added("extlibrary.BookCategory.Encyclopedia")} will check that an EObject named
	 * "Encyclopedia" has been added under the container "extlibrary.BookCategory". Note that
	 * {@code added("emf.compare.Match")} will <b>not</b> match a difference on the EObject
	 * "org.eclipse.emf.compare.Match". The qualified name must be absolute.
	 * </p>
	 * <p>
	 * Note that to in order for this to work, we expect the EObjects to have a "name" feature returning a
	 * String.
	 * </p>
	 * 
	 * @param qualifiedName
	 *            The qualified name of the EObject we expect to have been added.
	 * @return The created predicate.
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> added(final String qualifiedName) {
		final int parentEndIndex = qualifiedName.lastIndexOf('.');
		if (parentEndIndex >= 0) {
			final String ancestors = qualifiedName.substring(0, parentEndIndex);
			final String objectName = qualifiedName.substring(parentEndIndex + 1);
			return and(ofKind(DifferenceKind.ADD), onEObject(ancestors), valueNameMatches(objectName));
		}
		return and(valueNameMatches(qualifiedName), ofKind(DifferenceKind.ADD));
	}

	/**
	 * This predicate can be used to check whether a given Diff represents the removal of an EObject matching
	 * the given qualified name. Namely, it will check that that Diff is a ReferenceChange, that one of its
	 * Match sides correspond to the given qualified name's ancestors, and that its value correspond to the
	 * given qualified name's last segment.
	 * <p>
	 * For example, {@code removed("extlibrary.BookCategory.Encyclopedia")} will check that an EObject named
	 * "Encyclopedia" has been removed from the container "extlibrary.BookCategory". Note that
	 * {@code removed("emf.compare.Match")} will <b>not</b> match a difference on the EObject
	 * "org.eclipse.emf.compare.Match". The qualified name must be absolute.
	 * </p>
	 * <p>
	 * Note that to in order for this to work, we expect the EObjects to have a "name" feature returning a
	 * String.
	 * </p>
	 * 
	 * @param qualifiedName
	 *            The qualified name of the EObject we expect to have been removed.
	 * @return The created predicate.
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<? super Diff> removed(final String qualifiedName) {
		final int parentEndIndex = qualifiedName.lastIndexOf('.');
		if (parentEndIndex >= 0) {
			final String ancestors = qualifiedName.substring(0, parentEndIndex);
			final String objectName = qualifiedName.substring(parentEndIndex + 1);
			return and(ofKind(DifferenceKind.DELETE), onEObject(ancestors), valueNameMatches(objectName));
		}
		return and(valueNameMatches(qualifiedName), ofKind(DifferenceKind.DELETE));
	}

	/**
	 * This can be used to check that a given Diff correspond to either an {@link AttributeChange} or a
	 * {@link ReferenceChange}, and that the corresponding reference or attribute is {@code feature}.
	 * 
	 * @param feature
	 *            The feature on which we expect a change.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> onFeature(final EStructuralFeature feature) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final EStructuralFeature affectedFeature;
				if (input instanceof AttributeChange) {
					affectedFeature = ((AttributeChange)input).getAttribute();
				} else if (input instanceof ReferenceChange) {
					affectedFeature = ((ReferenceChange)input).getReference();
				} else {
					return false;
				}
				return affectedFeature == feature;
			}
		};
	}

	/**
	 * This can be used to check that a given Diff originates from the given {@code source} side.
	 * 
	 * @param source
	 *            The side from which we expect this diff to originate.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> fromSide(final DifferenceSource source) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getSource() == source;
			}
		};
	}

	/**
	 * This can be used in order to check that a Diff has been detected on the given EObject.
	 * 
	 * @param eObject
	 *            The EObject which we expect the diff to concern.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> onEObject(final EObject eObject) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final Match match = input.getMatch();
				return match.getLeft() == eObject || match.getRight() == eObject
						|| match.getOrigin() == eObject;
			}
		};
	}

	/**
	 * This can be used in order to check whether a Diff has been detected on an EObject matching the given
	 * qualified name.
	 * <p>
	 * For this to work, we expect the EObjects to have a feature named "name" returning a String.
	 * </p>
	 * 
	 * @param qualifiedName
	 *            The qualified name of the EObject we expect that diff to concern.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> onEObject(final String qualifiedName) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final Match match = input.getMatch();
				return match(match.getLeft(), qualifiedName) || match(match.getRight(), qualifiedName)
						|| match(match.getOrigin(), qualifiedName);
			}
		};
	}

	/**
	 * This predicate can be used to check whether a particular diff is of the given {@code kind}. This is
	 * mainly used to differentiate additions from deletions.
	 * 
	 * @param kind
	 *            The kind we expect this diff to have.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> ofKind(final DifferenceKind kind) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getKind() == kind;
			}
		};
	}

	/**
	 * This predicate can be used in order to check that a particular Diff describes either a
	 * {@link ReferenceChange} or {@link AttributeChange} for the given {@code expectedValue}.
	 * <p>
	 * For example, this could be used to check that the given value has indeed been added to a reference or
	 * attribute, though such checks are more easily performed through {@link #addedIn(EObject, EObject)} or
	 * {@link #removedFrom(EObject, EObject)}.
	 * </p>
	 * 
	 * @param expectedValue
	 *            The value which we expect to have changed and detected through a Diff.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> valueIs(final Object expectedValue) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final Object value;
				if (input instanceof ReferenceChange) {
					value = ((ReferenceChange)input).getValue();
				} else if (input instanceof AttributeChange) {
					value = ((AttributeChange)input).getValue();
				} else {
					return false;
				}
				return value == expectedValue;
			}
		};
	}

	/**
	 * This can be used to check whether a given Diff describes either a {@link ReferenceChange} on an EObject
	 * which name is {@code expectedName}.
	 * <p>
	 * For this to work, we expect the EObject to have a feature named "name" returning a String for us to try
	 * and match it.
	 * </p>
	 * 
	 * @param expectedName
	 *            The name of the EObject which we expect as a changed reference value.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> valueNameMatches(final String expectedName) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final EObject value;
				if (input instanceof ReferenceChange) {
					value = ((ReferenceChange)input).getValue();
				} else {
					return false;
				}
				return internalMatch(value, expectedName);
			}
		};
	}

	/**
	 * This will be used to check that a given {@link EObject} corresponds to the given {@code qualifiedName}.
	 * <p>
	 * For example, {@code match("extlibrary.BookCategory.Encyclopedia")} will return {@code true} for an
	 * EObject named "Encyclopedia" under the container "extlibrary.BookCategory". Note, however that
	 * {@code match("emf.compare.Match")} will <b>not</b> match the EObject "org.eclipse.emf.compare.Match".
	 * The qualified name must be absolute.
	 * </p>
	 * <p>
	 * For this to work, we expect the EObject to have a feature named "name" returning a String for us to try
	 * and match it. See also {@link #getNameFeature(EObject)}.
	 * </p>
	 * 
	 * @param eObject
	 *            The EObject which qualified name we are to check.
	 * @param qualifiedName
	 *            The expected, <b>absolute</b> qualified name of the given {@code eObject}.
	 * @return {@code true} if the given {@code eObject} matches the given {@code qualifiedName},
	 *         {@code false} if not, or if we could not determine the "name" feature of that EObject.
	 * @see #getNameFeature(EObject)
	 */
	private static boolean match(EObject eObject, String qualifiedName) {
		if (eObject == null || qualifiedName == null || qualifiedName.length() == 0) {
			return false;
		}
		final String[] names = qualifiedName.split("\\."); //$NON-NLS-1$

		int current = names.length - 1;
		boolean matches = internalMatch(eObject, names[current--]);
		if (matches) {
			EObject container = eObject.eContainer();
			while (matches && container != null && current >= 0) {
				matches = internalMatch(container, names[current--]);
				container = container.eContainer();
			}
			// This qualified name does not match if there was still a container "above"
			// "emf.compare.Match" does not match the EObject "org.eclipse.emf.compare.Match"
			matches = matches && container == null;
		}

		return matches;
	}

	/**
	 * Checks that the given {@code eObject}'s name is equal to {@code name}.
	 * <p>
	 * For this to work, we expect the EObject to have a feature named "name" returning a String for us to try
	 * and match it. See also {@link #getNameFeature(EObject)}.
	 * </p>
	 * 
	 * @param eObject
	 *            the EObject which name we are to check.
	 * @param name
	 *            The expected name of {@code eObject}.
	 * @return {@code true} if the given {@code eObject}'s name is equal to the given {@code name},
	 *         {@code false} if not, or if we could not determine the "name" feature of that EObject.
	 * @see #getNameFeature(EObject)
	 */
	private static boolean internalMatch(EObject eObject, String name) {
		final EStructuralFeature nameFeature = getNameFeature(eObject);
		if (nameFeature != null) {
			final Object featureValue = eObject.eGet(nameFeature);
			if (featureValue instanceof String) {
				return featureValue.equals(name);
			}
		}
		return false;
	}

	/**
	 * Tries and determine the "name" feature of the given EObject. By default, we only consider
	 * {@link ENamedElement#name} or a feature of the given {@code eObject}'s EClass which would be named
	 * "name".
	 * 
	 * @param eObject
	 *            The EObject for which we are trying to determine a name.
	 * @return The name feature of the given EObject if we could find one, {@code null} otherwise.
	 */
	private static EStructuralFeature getNameFeature(EObject eObject) {
		if (eObject instanceof ENamedElement) {
			return EcorePackage.eINSTANCE.getENamedElement_Name();
		}
		EStructuralFeature nameFeature = null;
		final Iterator<EStructuralFeature> features = eObject.eClass().getEAllStructuralFeatures().iterator();
		while (nameFeature == null && features.hasNext()) {
			final EStructuralFeature feature = features.next();
			if ("name".equals(feature.getName())) { //$NON-NLS-1$
				nameFeature = feature;
			}
		}
		return nameFeature;
	}
}
