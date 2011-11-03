/*******************************************************************************
 * Copyright (c) 2011 Obeo.
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
import java.util.ListIterator;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.common.util.UML2Util;

/**
 * Factory for the difference extensions.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
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
	 * The difference engine.
	 */
	private UML2DiffEngine fEngine;

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            {@link The UML2DiffEngine}
	 */
	protected AbstractDiffExtensionFactory(UML2DiffEngine engine) {
		fEngine = engine;
	}

	/**
	 * Getter for the difference engine.
	 * 
	 * @return The UML2 difference engine.
	 */
	protected final UML2DiffEngine getEngine() {
		return fEngine;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		return (DiffElement)input.eContainer();
	}

	/**
	 * Find or create the difference group to locate a difference element.
	 * 
	 * @param diffModel
	 *            The difference model.
	 * @param elt
	 *            The model object.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The difference group.
	 */
	protected final DiffGroup findOrCreateDiffGroup(DiffModel diffModel, EObject elt,
			EcoreUtil.CrossReferencer crossReferencer) {

		DiffGroup referencingDiffGroup = null;

		final List<EObject> ancestors = ancestors(elt);
		for (EObject ancestor : ancestors) {
			EObject parent = ancestor;
			if (referencingDiffGroup == null) {
				parent = getEngine().getMatched(ancestor);
			}
			referencingDiffGroup = firstReferencingDiffGroup(crossReferencer.get(parent));
			if (referencingDiffGroup != null) {
				break;
			}
		}

		if (referencingDiffGroup == null) {
			if (diffModel.getRightRoots().contains(elt) || diffModel.getLeftRoots().contains(elt)) {
				if (diffModel.getOwnedElements().isEmpty()) {
					referencingDiffGroup = DiffFactory.eINSTANCE.createDiffGroup();
					diffModel.getOwnedElements().add(referencingDiffGroup);
					referencingDiffGroup.setRightParent(elt);
					// register this new group for the current object
					final Setting set = ((InternalEObject)referencingDiffGroup)
							.eSetting(DiffPackage.Literals.DIFF_GROUP__RIGHT_PARENT);
					final Collection<Setting> values = new ArrayList<Setting>();
					values.add(set);
					crossReferencer.put(((DiffModel)elt).getRightRoots().get(0), values);
				} else {
					referencingDiffGroup = (DiffGroup)diffModel.getOwnedElements().get(0);
				}
			} else {
				referencingDiffGroup = (DiffGroup)diffModel.getOwnedElements().get(0);
			}
		}

		final List<EObject> ancestorsAndSelf = new ArrayList<EObject>(ancestors);
		ancestorsAndSelf.add(0, elt);
		referencingDiffGroup = createSubTreeOfDiffGroup(referencingDiffGroup, ancestorsAndSelf,
				crossReferencer);

		return referencingDiffGroup;
	}

	/**
	 * Creates sub tree of DiffGroup; and return the deepest one.
	 * 
	 * @param referencingDiffGroup
	 *            The referencing DiffGroup.
	 * @param ancestors
	 *            The ancestors of the model object.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The DiffGroup.
	 */
	private DiffGroup createSubTreeOfDiffGroup(DiffGroup referencingDiffGroup, List<EObject> ancestors,
			EcoreUtil.CrossReferencer crossReferencer) {
		DiffGroup ret = referencingDiffGroup;

		List<EObject> subList = null;
		if (referencingDiffGroup.getRightParent() != null) {
			// one diff group per ancestor with no referencing diff group
			EObject parent = null;
			final int indexParent = ancestors.indexOf(referencingDiffGroup.getRightParent());
			if (indexParent == -1) {
				parent = getEngine().getMatched(referencingDiffGroup.getRightParent());
			} else {
				parent = referencingDiffGroup.getRightParent();
			}
			subList = ancestors.subList(0, ancestors.indexOf(parent));
		} else {
			subList = ancestors;
		}

		// iterating on reverse order to create the deepest one (index = 0) as the last one.
		final ListIterator<EObject> it = subList.listIterator(subList.size());
		while (it.hasPrevious()) {
			final EObject previous = it.previous();
			final DiffGroup existingDiffGroup = firstReferencingDiffGroup(crossReferencer.get(previous));
			if (existingDiffGroup == null) {
				final DiffGroup newGroup = DiffFactory.eINSTANCE.createDiffGroup();
				ret.getSubDiffElements().add(newGroup);
				newGroup.setRightParent(previous);
				newGroup.setRemote(referencingDiffGroup.isRemote());
				ret = newGroup;

				// register this new group for the current object
				final Setting setting = ((InternalEObject)newGroup)
						.eSetting(DiffPackage.Literals.DIFF_GROUP__RIGHT_PARENT);
				final Collection<Setting> values = new ArrayList<Setting>();
				values.add(setting);
				crossReferencer.put(previous, values);

			} else {
				ret = existingDiffGroup;
			}

		}
		return ret;
	}

	/**
	 * Get the ancestors of the given model object.
	 * 
	 * @param eObject
	 *            The model object
	 * @return The ancestors.
	 */
	private List<EObject> ancestors(EObject eObject) {
		final List<EObject> ret = new ArrayList<EObject>();
		EObject eContainer = eObject.eContainer();
		while (eContainer != null) {
			ret.add(eContainer);
			eContainer = eContainer.eContainer();
		}

		return ret;
	}

	/**
	 * Find the first referencing DiffGroup from a collection of {@link Setting}s.
	 * 
	 * @param settings
	 *            The collection of {@link Setting}s
	 * @return The found DiffGroup or null.
	 */
	private DiffGroup firstReferencingDiffGroup(Collection<Setting> settings) {
		DiffGroup result = null;
		if (settings != null) {
			for (Setting setting : settings) {
				final EObject eObject = setting.getEObject();
				if (setting.getEStructuralFeature() == DiffPackage.Literals.DIFF_GROUP__RIGHT_PARENT
						&& eObject instanceof DiffGroup) {
					result = (DiffGroup)eObject;
					break;
				}
			}
		}
		return result;
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

	// protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature) {
	// return findCrossReferences(lookup, inFeature, alwaysTrue());
	// }

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
	protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
			EcoreUtil.CrossReferencer crossReferencer) {
		return findCrossReferences(lookup, inFeature, alwaysTrue(), crossReferencer);
	}

	// protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
	// UMLPredicate<Setting> p) {
	// Collection<Setting> settings = getCrossReferencer().get(lookup);
	// return findCrossReferences(inFeature, p, settings);
	// }

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
	protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
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
	private List<DiffElement> findCrossReferences(EStructuralFeature inFeature, UMLPredicate<Setting> p,
			Collection<Setting> settings) {
		final List<DiffElement> ret = new ArrayList<DiffElement>();
		if (settings != null) {
			for (Setting setting : settings) {
				final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
				if (eStructuralFeature == inFeature && p.apply(setting)) {
					ret.add((DiffElement)setting.getEObject());
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
	protected final void hideCrossReferences(EObject lookup, EStructuralFeature inFeature,
			AbstractDiffExtension hiddingExtension, UMLPredicate<Setting> p,
			EcoreUtil.CrossReferencer crossReferencer) {
		for (DiffElement diffElement : findCrossReferences(lookup, inFeature, p, crossReferencer)) {
			hiddingExtension.getHideElements().add(diffElement);
			((DiffElement)hiddingExtension).getRequires().add(diffElement);
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
	protected final void hideCrossReferences(EObject lookup, EStructuralFeature inFeature,
			AbstractDiffExtension hiddingExtension, EcoreUtil.CrossReferencer crossReferencer) {
		hideCrossReferences(lookup, inFeature, hiddingExtension, alwaysTrue(), crossReferencer);
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
	public void fillRequiredDifferences(AbstractDiffExtension diff, EcoreUtil.CrossReferencer crossReferencer) {
		// Default behavior
	}

}
