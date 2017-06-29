/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - adds helpers for opaque elements and opaque element changes
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Some utility methods that may be tweaked to allow EMFCompare to scale.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class UMLCompareUtil {

	/** Constructor. */
	private UMLCompareUtil() {
	}

	/**
	 * Retrieves the base element for the specified stereotype application, i.e. the element to which the
	 * stereotype is applied.
	 * <p>
	 * It first calls {@link UMLUtil#getBaseElement(EObject)}. If it returns null, it then tries to find an
	 * {@link EReference} with a name starting with {@link Extension#METACLASS_ROLE_PREFIX}. It <em>does
	 * not</em> verify if the the given {@code stereotypeApplication}'s eClass is defined as a Stereotype
	 * within a Profile because it may lead to load the resource of the Profile.
	 * 
	 * @param stereotypeApplication
	 *            The stereotype application.
	 * @return The base element.
	 */
	public static Element getBaseElement(EObject stereotypeApplication) {
		if (stereotypeApplication == null) {
			return null;
		}

		Element baseElement = UMLUtil.getBaseElement(stereotypeApplication);
		final Iterator<EReference> features = stereotypeApplication.eClass().getEAllReferences().iterator();
		while (features.hasNext() && baseElement == null) {
			final EReference feature = features.next();
			if (feature.getName().startsWith(Extension.METACLASS_ROLE_PREFIX)) {
				final Object value = stereotypeApplication.eGet(feature);
				if (value instanceof Element) {
					baseElement = (Element)value;
				}
			}
		}

		return baseElement;
	}

	/**
	 * From the given EReference, it returns the list of EReference which are superset and non union.
	 * 
	 * @param reference
	 *            The EReference subset from which is requested the non union superset.
	 * @return The list of EReference non union superset.
	 */
	public static Iterable<EReference> getNonUnionSupersetReferences(EReference reference) {
		return Iterables.filter(getSupersetReferences(reference), isNonUnionReference());
	}

	private static Predicate<? super EReference> isNonUnionReference() {
		return new Predicate<EReference>() {
			public boolean apply(EReference input) {
				return input != null
						&& !Iterables.any(input.getEAnnotations(), UMLUtilForCompare.isUnionAnnotation());
			}
		};
	}

	/**
	 * From the given EReference, it returns the list of EReference which are superset.
	 * 
	 * @param reference
	 *            The EReference subset from which is requested the superset.
	 * @return The list of EReference superset.
	 */
	private static Iterable<EReference> getSupersetReferences(EReference reference) {
		EAnnotation subsetsAnnotation = Iterables.find(reference.getEAnnotations(),
				UMLUtilForCompare.isSubsetsAnnotation(), null);
		if (subsetsAnnotation != null) {
			return Iterables.filter(subsetsAnnotation.getReferences(), EReference.class);
		}
		return Collections.emptyList();
	}

	/**
	 * This extends UMLUtil to get the name of the used annotations for subsets and unions.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private static class UMLUtilForCompare extends UMLUtil {
		public static Predicate<? super EAnnotation> isSubsetsAnnotation() {
			return new Predicate<EAnnotation>() {
				public boolean apply(EAnnotation input) {
					return input != null && input.getSource().equals(ANNOTATION__SUBSETS);
				}
			};
		}

		public static Predicate<? super EAnnotation> isUnionAnnotation() {
			return new Predicate<EAnnotation>() {
				public boolean apply(EAnnotation input) {
					return input != null && input.getSource().equals(ANNOTATION__UNION);
				}
			};
		}
	}

	/**
	 * Returns the bodies of the given {@code eObject}, which must be either an OpaqueAction, OpaqueBehavior,
	 * or an OpaqueExpression.
	 * 
	 * @throws IllegalArgumentException
	 *             If {@code eObject} is not a OpaqueAction, OpaqueBehavior, or an OpaqueExpression.
	 * @param eObject
	 *            The OpaqueAction, OpaqueBehavior, or an OpaqueExpression to get the bodies of.
	 * @return The bodies of {@code eObject}.
	 */
	public static List<String> getOpaqueElementBodies(EObject eObject) {
		final List<String> bodies;
		if (eObject instanceof OpaqueAction) {
			bodies = ((OpaqueAction)eObject).getBodies();
		} else if (eObject instanceof OpaqueBehavior) {
			bodies = ((OpaqueBehavior)eObject).getBodies();
		} else if (eObject instanceof OpaqueExpression) {
			bodies = ((OpaqueExpression)eObject).getBodies();
		} else {
			throw new IllegalArgumentException(eObject.eClass().getName()
					+ " has no bodies. Only OpaqueAction, OpaqueBehavior, and OpaqueExpression do."); //$NON-NLS-1$
		}
		return bodies;
	}

	/**
	 * Returns the languages of the given {@code eObject}, which must be either an OpaqueAction,
	 * OpaqueBehavior, or an OpaqueExpression.
	 * 
	 * @throws IllegalArgumentException
	 *             If {@code eObject} is not a OpaqueAction, OpaqueBehavior, or an OpaqueExpression.
	 * @param eObject
	 *            The OpaqueAction, OpaqueBehavior, or an OpaqueExpression to get the languages of.
	 * @return The bodies of {@code eObject}.
	 */
	public static List<String> getOpaqueElementLanguages(EObject eObject) {
		final List<String> languages;
		if (eObject instanceof OpaqueAction) {
			languages = ((OpaqueAction)eObject).getLanguages();
		} else if (eObject instanceof OpaqueBehavior) {
			languages = ((OpaqueBehavior)eObject).getLanguages();
		} else if (eObject instanceof OpaqueExpression) {
			languages = ((OpaqueExpression)eObject).getLanguages();
		} else {
			throw new IllegalArgumentException(eObject.eClass().getName()
					+ " has no languages. Only OpaqueAction, OpaqueBehavior, and OpaqueExpression do."); //$NON-NLS-1$
		}
		return languages;
	}

	/**
	 * Returns the body for the given {@code language} of the given {@code eObject}, which must be either an
	 * OpaqueAction, OpaqueBehavior, or an OpaqueExpression.
	 * 
	 * @throws IllegalArgumentException
	 *             If {@code eObject} is not a OpaqueAction, OpaqueBehavior, or an OpaqueExpression.
	 * @param eObject
	 *            The OpaqueAction, OpaqueBehavior, or an OpaqueExpression to get the body of.
	 * @param language
	 *            The language for which the body is requested.
	 * @return The body for the given {@code language} of the given {@code eObject}.
	 */
	public static String getOpaqueElementBody(EObject eObject, String language) {
		final List<String> languages = getOpaqueElementLanguages(eObject);
		final List<String> bodies = getOpaqueElementBodies(eObject);
		final int index = languages.indexOf(language);
		return bodies.get(index);
	}

	/**
	 * Specifies whether the given {@code diff} is a change of the body attribute of {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a change of the body attribute, <code>false</code> otherwise.
	 */
	public static boolean isChangeOfOpaqueElementBodyAttribute(Diff diff) {
		if (diff instanceof AttributeChange) {
			final EAttribute attribute = ((AttributeChange)diff).getAttribute();
			return UMLPackage.eINSTANCE.getOpaqueAction_Body().equals(attribute)
					|| UMLPackage.eINSTANCE.getOpaqueBehavior_Body().equals(attribute)
					|| UMLPackage.eINSTANCE.getOpaqueExpression_Body().equals(attribute);
		} else {
			return false;
		}
	}

	/**
	 * Specifies whether the given {@code diff} is a change of the language attribute of {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a change of the language attribute, <code>false</code> otherwise.
	 */
	public static boolean isChangeOfOpaqueElementLanguageAttribute(Diff diff) {
		if (diff instanceof AttributeChange) {
			final EAttribute attribute = ((AttributeChange)diff).getAttribute();
			return UMLPackage.eINSTANCE.getOpaqueAction_Language().equals(attribute)
					|| UMLPackage.eINSTANCE.getOpaqueBehavior_Language().equals(attribute)
					|| UMLPackage.eINSTANCE.getOpaqueExpression_Language().equals(attribute);
		} else {
			return false;
		}
	}
}
