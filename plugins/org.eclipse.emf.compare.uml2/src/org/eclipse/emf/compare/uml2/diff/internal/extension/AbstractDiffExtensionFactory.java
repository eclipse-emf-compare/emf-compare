/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.common.util.UML2Util;

/**
 * Factory for the difference extensions.
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	/**
	 * The always checked predicate.
	 */
	private static final UMLPredicate<?> ALWAYS_TRUE = new UMLPredicate<Object>() {
		public boolean apply(Object input) {
			return true;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Match getParentMatch(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		return input.getMatch();
	}

	/**
	 * Get the inverted references of the given model object, through the specified feature.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @return The inverted references.
	 */
	protected static final List<EObject> getInverseReferences(EObject lookup, EStructuralFeature inFeature) {
		return getInverseReferences(lookup, inFeature, alwaysTrue());
	}

	/**
	 * Get the inverted references of the given model object, through the specified feature, with a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param predicate
	 *            The predicate.
	 * @return The inverted references.
	 */
	protected static final List<EObject> getInverseReferences(EObject lookup, EStructuralFeature inFeature,
			UMLPredicate<Setting> predicate) {
		final List<EObject> ret = new ArrayList<EObject>();
		for (Setting setting : UML2Util.getInverseReferences(lookup)) {
			if (setting.getEStructuralFeature() == inFeature && predicate.apply(setting)) {
				ret.add(setting.getEObject());
			}
		}
		return ret;
	}

	/**
	 * Get the non navigable inverted references of the given model object, through the specified feature.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @return The inverted references.
	 */
	protected static final List<EObject> getNonNavigableInverseReferences(EObject lookup,
			EStructuralFeature inFeature) {
		return getNonNavigableInverseReferences(lookup, inFeature, alwaysTrue());
	}

	/**
	 * Get the non navigable inverted references of the given model object, through the specified feature,
	 * with a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param predicate
	 *            The predicate.
	 * @return The inverted references.
	 */
	protected static final List<EObject> getNonNavigableInverseReferences(EObject lookup,
			EStructuralFeature inFeature, UMLPredicate<Setting> predicate) {
		final List<EObject> ret = new ArrayList<EObject>();
		for (Setting setting : UML2Util.getNonNavigableInverseReferences(lookup)) {
			if (setting.getEStructuralFeature() == inFeature && predicate.apply(setting)) {
				ret.add(setting.getEObject());
			}
		}
		return ret;
	}

	/**
	 * Find the cross references of the given model object, through the specified feature, with a cross
	 * referencer.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The cross references.
	 */
	protected final List<Diff> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
			EcoreUtil.CrossReferencer crossReferencer) {
		return findCrossReferences(lookup, inFeature, alwaysTrue(), crossReferencer);
	}

	/**
	 * Find the cross references of the given model object, through the specified feature, with a cross
	 * referencer and a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param p
	 *            The predicate.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The cross references.
	 */
	protected final List<Diff> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
			UMLPredicate<Setting> p, EcoreUtil.CrossReferencer crossReferencer) {
		final Collection<Setting> settings = crossReferencer.get(lookup);
		return findCrossReferences(inFeature, p, settings);
	}

	/**
	 * Find the cross references from a collection of {@link Setting}s, through the specified feature and a
	 * predicate.
	 * 
	 * @param inFeature
	 *            The feature.
	 * @param p
	 *            the predicate.
	 * @param settings
	 *            The collection of {@link Setting}s.
	 * @return The cross references.
	 */
	private List<Diff> findCrossReferences(EStructuralFeature inFeature, UMLPredicate<Setting> p,
			Collection<Setting> settings) {
		final List<Diff> ret = new ArrayList<Diff>();
		if (settings != null) {
			for (Setting setting : settings) {
				final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
				if (eStructuralFeature == inFeature && p.apply(setting)) {
					ret.add((Diff)setting.getEObject());
				}
			}
		}
		return ret;
	}

	/**
	 * Hide the difference elements from the given extension, from the specified model object, the feature and
	 * cross referencer, with a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param hiddingExtension
	 *            The extension
	 * @param p
	 *            The predicate
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	protected final void beRefinedByCrossReferences(EObject lookup, EStructuralFeature inFeature,
			Diff refinedExtension, UMLPredicate<Setting> p, EcoreUtil.CrossReferencer crossReferencer) {
		for (Diff diffElement : findCrossReferences(lookup, inFeature, p, crossReferencer)) {
			refinedExtension.getRefinedBy().add(diffElement);
		}
	}

	/**
	 * Hide the difference elements from the given extension, from the specified model object, the feature and
	 * cross referencer.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param hiddingExtension
	 *            The extension.
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	protected final void beRefinedByCrossReferences(EObject lookup, EStructuralFeature inFeature,
			Diff refinedExtension, EcoreUtil.CrossReferencer crossReferencer) {
		beRefinedByCrossReferences(lookup, inFeature, refinedExtension, alwaysTrue(), crossReferencer);
	}

	protected void registerUMLExtension(EcoreUtil.CrossReferencer crossReferencer, final Diff umlExtension,
			final EStructuralFeature umlExtensionFeature, final EObject umlExtensionValue) {
		Collection<Setting> settings = crossReferencer.get(umlExtensionValue);
		if (settings == null) {
			settings = new ArrayList<Setting>();
			crossReferencer.put(umlExtensionValue, settings);
		}
		settings.add(((InternalEObject)umlExtension).eSetting(umlExtensionFeature));
	}

	protected EObject eContainer(EObject obj, Class<? extends EObject> clazz) {
		if (clazz.isInstance(obj)) {
			return obj;
		} else if (obj != null) {
			return eContainer(obj.eContainer(), clazz);
		} else {
			return null;
		}
	}

	/**
	 * UML Predicate.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	protected interface UMLPredicate<T> {
		/**
		 * Apply the predicate.
		 * 
		 * @param input
		 *            The input type.
		 * @return True if the predicate is checked.
		 */
		boolean apply(T input);
	}

	/**
	 * Returns an always checked predicate.
	 * 
	 * @return The predicate.
	 */
	@SuppressWarnings("unchecked")
	private static UMLPredicate<Setting> alwaysTrue() {
		return (UMLPredicate<Setting>)ALWAYS_TRUE; // predicate works for all T
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public void fillRequiredDifferences(UMLExtension extension, EcoreUtil.CrossReferencer crossReferencer) {
		// Default behavior
	}

}
