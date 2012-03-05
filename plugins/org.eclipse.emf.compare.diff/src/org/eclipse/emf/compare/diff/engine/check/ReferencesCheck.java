/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Victor Roldan Betancort - [352002] introduce IMatchManager
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine.check;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.engine.IMatchManager;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.engine.IMatchManager2;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.match.internal.statistic.ResourceSimilarity;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This will implement the reference checks : order of reference values, changes between two versions, ...
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 * @since 1.0
 */
public class ReferencesCheck extends AbstractCheck {
	/** We'll use this as a prefix for all of EMF Compare's internal proxies. */
	private static final String PROXY_PREFIX = "emfCompareProxy:/"; //$NON-NLS-1$

	/**
	 * Simply delegates to the super constructor.
	 * 
	 * @param referencer
	 *            CrossReferencer instantiated with the match model or match resource set.
	 * @see {@link AbstractCheck#DefaultCheck(org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)}
	 * @deprecated CrossReferencer mechanism is now hidden behind the {@link IMatchManager} interface.
	 */
	@Deprecated
	public ReferencesCheck(EcoreUtil.CrossReferencer referencer) {
		super(referencer);
	}

	/**
	 * Simply delegates to the super constructor.
	 * 
	 * @see IMatchManager
	 * @param manager
	 *            the IMatchManager instance to determine matches for certain <code>EObject</code>
	 * @since 1.3
	 */
	public ReferencesCheck(IMatchManager manager) {
		super(manager);
	}

	/**
	 * Checks if there's been references updates in the model.<br/>
	 * <p>
	 * A reference is considered updated if its value(s) has been changed (either removal or addition of an
	 * element if the reference is multi-valued or update of a single-valued reference) between the left and
	 * the right model.
	 * </p>
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param mapping
	 *            Contains informations about the left and right model elements we have to compare.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the references' values.
	 */
	public void checkReferencesUpdates(DiffGroup root, Match2Elements mapping) throws FactoryException {
		final EClass eClass = mapping.getLeftElement().eClass();
		final List<EReference> eclassReferences = eClass.getEAllReferences();

		final Iterator<EReference> it = eclassReferences.iterator();
		while (it.hasNext()) {
			final EReference next = it.next();
			if (!shouldBeIgnored(next)) {
				checkReferenceUpdates(root, mapping, next);
			} else if (next.isContainment() && next.isOrdered()) {
				checkContainmentReferenceOrderChange(root, mapping, next);
			}
		}
	}

	/**
	 * Checks if there's been references updates in the model.<br/>
	 * <p>
	 * A reference is considered updated if its value(s) has been changed (either removal or addition of an
	 * element if the reference is multi-valued or update of a single-valued reference) between the left and
	 * the ancestor model, the right and the ancestor or between the left and the right model.
	 * </p>
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param mapping
	 *            Contains informations about the left, right and origin model elements we have to compare.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the references' values.
	 */
	public void checkReferencesUpdates(DiffGroup root, Match3Elements mapping) throws FactoryException {
		// Ignores matchElements when they don't have origin (no updates on these)
		if (mapping.getOriginElement() == null)
			return;
		final EClass eClass = mapping.getOriginElement().eClass();
		final List<EReference> eclassReferences = eClass.getEAllReferences();

		final Iterator<EReference> it = eclassReferences.iterator();
		while (it.hasNext()) {
			final EReference next = it.next();
			if (!shouldBeIgnored(next)) {
				checkReferenceUpdates(root, mapping, next);
			} else if (next.isContainment() && next.isOrdered()) {
				checkContainmentReferenceOrderChange(root, mapping, next);
			}
		}
	}

	/**
	 * This will be called to check for ordering changes on a given containment reference values.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param mapping
	 *            Contains informations about the left and right model elements we have to compare.
	 * @param reference
	 *            {@link EReference} to check for modifications.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the references' values.
	 */
	@SuppressWarnings("unchecked")
	protected void checkContainmentReferenceOrderChange(DiffGroup root, Match2Elements mapping,
			EReference reference) throws FactoryException {
		/*
		 * We'll need to compute the added and removed reference values from the cross referencing of
		 * unmatched elements as they haven't been processed yet.
		 */
		final List<EObject> leftElementReferences = new ArrayList<EObject>(
				(List<EObject>)EFactory.eGetAsList(mapping.getLeftElement(), reference.getName()));
		final List<EObject> rightElementReferences = new ArrayList<EObject>(
				(List<EObject>)EFactory.eGetAsList(mapping.getRightElement(), reference.getName()));
		final List<Integer> removedIndices = new ArrayList<Integer>();
		// Purge "left" list of all reference values that have been added to it
		for (EObject leftValue : new ArrayList<EObject>(leftElementReferences)) {
			if (!getMatchManager().isInScope(leftValue)
					|| getMatchManager().isUnmatched(leftValue)
					|| getMatchManager().getMatchedEObject(leftValue.eContainer()) != getMatchManager()
							.getMatchedEObject(leftValue).eContainer())
				leftElementReferences.remove(leftValue);
		}
		for (EObject rightValue : new ArrayList<EObject>(rightElementReferences)) {
			if (!getMatchManager().isInScope(rightValue)
					|| getMatchManager().isUnmatched(rightValue)
					|| getMatchManager().getMatchedEObject(rightValue.eContainer()) != getMatchManager()
							.getMatchedEObject(rightValue).eContainer()) {
				removedIndices.add(Integer.valueOf(rightElementReferences.indexOf(rightValue)));
			}
		}
		int expectedIndex = 0;
		for (int i = 0; i < leftElementReferences.size(); i++) {
			final EObject matched = getMatchManager().getMatchedEObject(leftElementReferences.get(i));
			for (final Integer removedIndex : new ArrayList<Integer>(removedIndices)) {
				if (expectedIndex == removedIndex.intValue()) {
					expectedIndex += 1;
					removedIndices.remove(removedIndex);
				}
			}
			if (rightElementReferences.indexOf(matched) != expectedIndex++) {
				final ReferenceOrderChange refChange = DiffFactory.eINSTANCE.createReferenceOrderChange();
				refChange.setReference(reference);
				refChange.setLeftElement(mapping.getLeftElement());
				refChange.setRightElement(mapping.getRightElement());

				// The loop will be broken here. Initialize left and right "target" lists for the diff
				final List<EObject> leftTarget = new ArrayList<EObject>();
				for (int j = 0; j < rightElementReferences.size(); j++) {
					final EObject right = rightElementReferences.get(j);
					EObject target = getMatchManager().getMatchedEObject(right);
					if (target == null) {
						target = createProxyFor(right);
					}
					leftTarget.add(target);
				}
				final List<EObject> rightTarget = new ArrayList<EObject>();
				for (int j = 0; j < leftElementReferences.size(); j++) {
					final EObject left = leftElementReferences.get(j);
					EObject target = getMatchManager().getMatchedEObject(left);
					if (target == null) {
						target = createProxyFor(left);
					}
					rightTarget.add(target);
				}

				refChange.getLeftTarget().addAll(leftTarget);
				refChange.getRightTarget().addAll(rightTarget);

				root.getSubDiffElements().add(refChange);
				break;
			}
		}
	}

	/**
	 * This will create a proxy for the given EObject. If <code>original</code> is itself a proxy, it will
	 * simply be returned as-is.
	 * 
	 * @param original
	 *            The object we need a proxy for.
	 * @return A new proxy for the given original object.
	 */
	private EObject createProxyFor(EObject original) {
		if (original.eIsProxy()) {
			return original;
		}

		final org.eclipse.emf.ecore.EFactory factory = original.eClass().getEPackage().getEFactoryInstance();
		final EObject proxy = factory.create(original.eClass());
		((InternalEObject)proxy).eSetProxyURI(URI.createURI(PROXY_PREFIX
				+ EcoreUtil.getURI(original).toString()));
		return proxy;
	}

	/**
	 * This will be called to check for changes on a given reference values. Note that we know
	 * <code>reference.isMany()</code> and <code>reference.isOrdered()</code> always return true here for the
	 * generic diff engine and the tests won't be made.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param reference
	 *            {@link EReference} to check for modifications.
	 * @param leftElement
	 *            Element corresponding to the final holder of the given reference.
	 * @param rightElement
	 *            Element corresponding to the initial holder of the given reference.
	 * @param addedValues
	 *            References the values of the left EObject that have been removed in the right one.
	 * @param removedValues
	 *            References the values of the right EObject that have been removed in the left one.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the references' values.
	 * @since 1.3
	 */
	@SuppressWarnings("unchecked")
	protected void checkReferenceOrderChange(DiffGroup root, EReference reference, EObject leftElement,
			EObject rightElement, Set<EObject> addedValues, Set<EObject> removedValues)
			throws FactoryException {
		final List<EObject> leftElementReferences = new ArrayList<EObject>(
				(List<EObject>)EFactory.eGetAsList(leftElement, reference.getName()));
		final List<EObject> rightElementReferences = new ArrayList<EObject>(
				(List<EObject>)EFactory.eGetAsList(rightElement, reference.getName()));
		final List<Integer> removedIndices = new ArrayList<Integer>(removedValues.size());

		final List<EObject> filteredLeft = new ArrayList<EObject>(leftElementReferences);
		// Purge "left" list of all reference values that have been added to it
		for (final EObject added : addedValues) {
			filteredLeft.remove(added);
		}
		for (final EObject removed : removedValues) {
			removedIndices.add(Integer.valueOf(rightElementReferences.indexOf(removed)));
		}

		int expectedIndex = 0;
		for (int i = 0; i < filteredLeft.size(); i++) {
			EObject matched = getMatchManager().getMatchedEObject(filteredLeft.get(i));
			/*
			 * There is a possibiliy that this object is located in another resource. If we are here, we know
			 * it is neither added nor removed. We thus need to assume it is the case, and the object is in
			 * both left and right, same instance in both.
			 */
			if (matched == null) {
				matched = filteredLeft.get(i);
			}
			for (final Integer removedIndex : new ArrayList<Integer>(removedIndices)) {
				if (i == removedIndex.intValue()) {
					expectedIndex += 1;
					removedIndices.remove(removedIndex);
				}
			}
			int actualIndex = rightElementReferences.indexOf(matched);
			if (actualIndex == -1) {
				for (int j = 0; j < rightElementReferences.size() && actualIndex == -1; j++) {
					if (!areDistinct(matched, rightElementReferences.get(j))) {
						actualIndex = j;
					}
				}
			}

			if (actualIndex != expectedIndex++) {
				final ReferenceOrderChange refChange = DiffFactory.eINSTANCE.createReferenceOrderChange();
				refChange.setReference(reference);
				refChange.setLeftElement(leftElement);
				refChange.setRightElement(rightElement);

				// The loop will be broken here. Initialize left and right "target" lists for the diff
				final List<EObject> leftTarget = new ArrayList<EObject>();
				for (int j = 0; j < rightElementReferences.size(); j++) {
					final EObject right = rightElementReferences.get(j);
					EObject target = getMatchManager().getMatchedEObject(right);
					if (target == null) {
						target = createProxyFor(right);
					}
					leftTarget.add(target);
				}
				final List<EObject> rightTarget = new ArrayList<EObject>();
				for (int j = 0; j < leftElementReferences.size(); j++) {
					final EObject left = leftElementReferences.get(j);
					EObject target = getMatchManager().getMatchedEObject(left);
					if (target == null) {
						target = createProxyFor(left);
					}
					rightTarget.add(target);
				}

				refChange.getLeftTarget().addAll(leftTarget);
				refChange.getRightTarget().addAll(rightTarget);

				root.getSubDiffElements().add(refChange);
				break;
			}
		}
	}

	/**
	 * This will be called to check for changes on a given reference values. Note that we know
	 * <code>reference.isMany()</code> and <code>reference.isOrdered()</code> always return true here for the
	 * generic diff engine and the tests won't be made.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param reference
	 *            {@link EReference} to check for modifications.
	 * @param leftElement
	 *            Element corresponding to the final holder of the given reference.
	 * @param rightElement
	 *            Element corresponding to the initial holder of the given reference.
	 * @param addedReferences
	 *            Contains the created differences for added reference values.
	 * @param removedReferences
	 *            Contains the created differences for removed reference values.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the references' values.
	 * @deprecated Use {@link #checkReferenceOrderChange(DiffGroup, EReference, EObject, EObject, Set, Set)}
	 */
	@Deprecated
	protected void checkReferenceOrderChange(DiffGroup root, EReference reference, EObject leftElement,
			EObject rightElement, List<ReferenceChangeLeftTarget> addedReferences,
			List<ReferenceChangeRightTarget> removedReferences) throws FactoryException {
		final Set<EObject> addedValues = new HashSet<EObject>();
		final Set<EObject> removedValues = new HashSet<EObject>();

		for (ReferenceChangeLeftTarget added : addedReferences) {
			addedValues.add(added.getLeftTarget());
		}
		for (ReferenceChangeRightTarget removed : removedReferences) {
			removedValues.add(removed.getRightTarget());
		}

		checkReferenceOrderChange(root, reference, leftElement, rightElement, addedValues, removedValues);
	}

	/**
	 * This will check that the values of the given reference from the objects contained by mapping has been
	 * modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param mapping
	 *            Contains informations about the left and right model elements we have to compare.
	 * @param reference
	 *            The reference we need to check for differences.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 */
	protected void checkReferenceUpdates(DiffGroup root, Match2Elements mapping, EReference reference)
			throws FactoryException {
		createNonConflictingReferencesUpdate(root, reference, mapping.getLeftElement(),
				mapping.getRightElement());
	}

	/**
	 * This will check that the values of the given reference from the objects contained by mapping has been
	 * modified.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement} to create.
	 * @param mapping
	 *            Contains informations about the left and right model elements we have to compare.
	 * @param reference
	 *            The reference we need to check for differences.
	 * @throws FactoryException
	 *             Thrown if one of the checks fails.
	 */
	protected void checkReferenceUpdates(DiffGroup root, Match3Elements mapping, EReference reference)
			throws FactoryException {
		final String referenceName = reference.getName();
		final List<Object> leftReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getLeftElement(), referenceName));
		final List<Object> rightReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getRightElement(), referenceName));
		final List<Object> ancestorReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getOriginElement(), referenceName));

		// Checks if there're conflicts
		if (isConflictual(reference, leftReferences, rightReferences, ancestorReferences)) {
			createConflictingReferenceUpdate(root, reference, mapping);
			return;
		}
		// We know there aren't any conflicting changes
		final List<EObject> remoteDeletedReferences = new ArrayList<EObject>();
		final List<EObject> remoteAddedReferences = new ArrayList<EObject>();
		final List<EObject> deletedReferences = new ArrayList<EObject>();
		final List<EObject> addedReferences = new ArrayList<EObject>();

		populateThreeWayReferencesChanges(mapping, reference, addedReferences, deletedReferences,
				remoteAddedReferences, remoteDeletedReferences);
		createRemoteReferencesUpdate(root, reference, mapping, remoteAddedReferences, remoteDeletedReferences);

		if (!reference.isMany()) {
			EObject addedValue = null;
			EObject deletedValue = null;
			if (addedReferences.size() > 0) {
				addedValue = addedReferences.get(0);
			}
			if (deletedReferences.size() > 0) {
				deletedValue = deletedReferences.get(0);
			}

			if (areDistinct(addedValue, deletedValue)) {
				root.getSubDiffElements().add(
						createUpdatedReferenceOperation(mapping.getLeftElement(), mapping.getRightElement(),
								reference, addedValue, deletedValue));
			}
		} else {
			final List<EObject> addedReferencesCopy = new ArrayList<EObject>(addedReferences);
			final List<EObject> deletedReferencesCopy = new ArrayList<EObject>(deletedReferences);

			for (EObject addedReference : addedReferencesCopy) {
				deletedReferences.remove(addedReference);
			}
			for (EObject deletedReference : deletedReferencesCopy) {
				addedReferences.remove(deletedReference);
			}

			final List<ReferenceChangeLeftTarget> addedReferencesDiffs = new ArrayList<ReferenceChangeLeftTarget>(
					addedReferences.size());
			final List<ReferenceChangeRightTarget> removedReferencesDiffs = new ArrayList<ReferenceChangeRightTarget>(
					deletedReferences.size());
			// REFERENCES ADD
			if (addedReferences.size() > 0) {
				addedReferencesDiffs.addAll(createNewReferencesOperation(root, mapping.getLeftElement(),
						mapping.getRightElement(), reference, addedReferences));
			}
			// REFERENCES DEL
			if (deletedReferences.size() > 0) {
				removedReferencesDiffs.addAll(createRemovedReferencesOperation(root,
						mapping.getLeftElement(), mapping.getRightElement(), reference, deletedReferences));
			}
			// Check for references order changes
			if (reference.isOrdered()) {
				// Consider both "added" and "remotelyDeleted" here
				final Set<EObject> addedValues = new HashSet<EObject>(remoteDeletedReferences);
				for (final ReferenceChangeLeftTarget added : addedReferencesDiffs) {
					addedValues.add(added);
				}
				// Similarly, consider both "deleted" and "remoteAdded"
				final Set<EObject> removedValues = new HashSet<EObject>(remoteAddedReferences);
				for (final ReferenceChangeRightTarget removed : removedReferencesDiffs) {
					removedValues.add(removed);
				}

				checkReferenceOrderChange(root, reference, mapping.getLeftElement(),
						mapping.getRightElement(), addedValues, removedValues);
			}
		}
	}

	/**
	 * Checks whether the two given values are distinct.
	 * 
	 * @param addedValue
	 *            The value that's been added to a reference.
	 * @param deletedValue
	 *            The value that's been removed from a reference.
	 * @return <code>true</code> if there is a diff between the two values, <code>false</code> otherwise.
	 */
	private boolean areDistinct(EObject addedValue, EObject deletedValue) {
		final double similarReferenceURIThreshold = 0.8d;
		boolean createDiff = false;

		// One of the two value is null, reference has been unset
		if (addedValue == deletedValue) {
			createDiff = false;
		} else if ((addedValue == null || deletedValue == null) && addedValue != deletedValue) {
			createDiff = true;
		} else if (getMatchManager().isUnmatched(addedValue) && getMatchManager().isUnmatched(deletedValue)) {
			createDiff = true;
		} else if (addedValue != null && deletedValue != null) {
			final EObject matchAdded = getMatchManager().getMatchedEObject(addedValue);
			if (matchAdded != null && matchAdded != deletedValue) {
				createDiff = true;
			} else if (getMatchManager().getMatchedEObject(deletedValue) != null) {
				// the deleted object has a match. At this point it can only be distinct from the added
				// value since this added value itself has no match.
				createDiff = true;
			} else {
				final URI addedURI = EcoreUtil.getURI(addedValue);
				final URI deletedURI = EcoreUtil.getURI(deletedValue);
				if (addedValue.eIsProxy() && deletedValue.eIsProxy()) {
					// Strict equality
					createDiff = !addedURI.equals(deletedURI);
				} else if (addedValue.eIsProxy() || deletedValue.eIsProxy()) {
					// Only one of them is a proxy, thus different since one of the two cannot be resolved
					createDiff = true;
				} else if (ResourceSimilarity.computeURISimilarity(addedURI, deletedURI) < similarReferenceURIThreshold) {
					createDiff = true;
				}
			}
		}

		return createDiff;
	}

	/**
	 * Determines if we should ignore a reference for diff detection.
	 * <p>
	 * Default is to ignore references marked either
	 * <ul>
	 * <li>Containment</li>
	 * <li>Container</li>
	 * <li>Transient</li>
	 * <li>Derived</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Clients should override this if they wish to ignore other references.
	 * </p>
	 * 
	 * @param reference
	 *            Reference to determine whether it should be ignored.
	 * @return <code>True</code> if reference has to be ignored, <code>False</code> otherwise.
	 */
	protected boolean shouldBeIgnored(EReference reference) {
		boolean ignore = reference.isContainment();
		ignore = ignore || reference.isDerived();
		ignore = ignore || reference.isTransient();
		ignore = ignore || reference.isContainer();
		ignore = ignore || reference.eContainer() == EcorePackage.eINSTANCE.getEGenericType();
		return ignore;
	}

	/**
	 * This will create and populate a {@link List} with all the references from the
	 * <code>leftReferences</code> {@link List} that cannot be matched in the <code>rightReferences</code>
	 * {@link List}.
	 * 
	 * @param leftReferences
	 *            List of the left element reference values.
	 * @param rightReferences
	 *            List of the right element reference values.
	 * @return {@link List} of all the references that have been added in the left (local) element since the
	 *         right (distant) element.
	 */
	private List<EObject> computeAddedReferences(List<EObject> leftReferences, List<EObject> rightReferences) {
		final List<EObject> deletedReferences = new ArrayList<EObject>();
		final List<EObject> addedReferences = new ArrayList<EObject>();
		final List<EObject> deletedProxies = new ArrayList<EObject>();

		if (leftReferences != null) {
			addedReferences.addAll(leftReferences);
		}
		if (rightReferences != null) {
			for (EObject obj : rightReferences) {
				if (obj.eIsProxy()) {
					deletedProxies.add(obj);
				} else {
					deletedReferences.add(obj);
				}
			}
		}
		final List<EObject> matchedOldReferences = getMatchedReferences(deletedReferences);

		// If the same object is both in left and right, it is neither added nor removed.
		addedReferences.removeAll(deletedReferences);
		// "Added" references are the references from the left element that
		// have no matching "right" counterpart
		addedReferences.removeAll(matchedOldReferences);

		// Double check for proxies
		final Iterator<EObject> addedCandidates = addedReferences.iterator();
		while (addedCandidates.hasNext() && !deletedProxies.isEmpty()) {
			final EObject added = addedCandidates.next();
			if (added.eIsProxy()) {
				final URI addedURI = ((InternalEObject)added).eProxyURI();
				boolean hasMatch = false;
				final Iterator<EObject> candidateMatches = deletedProxies.iterator();
				while (!hasMatch && candidateMatches.hasNext()) {
					final EObject candidate = candidateMatches.next();
					if (addedURI.equals(((InternalEObject)candidate).eProxyURI())) {
						hasMatch = true;
						candidateMatches.remove();
					}
				}
				if (hasMatch) {
					addedCandidates.remove();
				}
			}
		}

		return addedReferences;
	}

	/**
	 * This will create and populate a {@link List} with all the references from the
	 * <code>rightReferences</code> {@link List} that cannot be matched in the <code>leftReferences</code>
	 * {@link List}.
	 * 
	 * @param leftReferences
	 *            List of the left element reference values.
	 * @param rightReferences
	 *            List of the right element reference values.
	 * @return {@link List} of all the references that have been deleted from the left (local) element since
	 *         the right (distant) element.
	 */
	private List<EObject> computeDeletedReferences(List<EObject> leftReferences, List<EObject> rightReferences) {
		final List<EObject> deletedReferences = new ArrayList<EObject>();
		final List<EObject> addedReferences = new ArrayList<EObject>();
		final List<EObject> addedProxies = new ArrayList<EObject>();

		if (leftReferences != null) {
			for (EObject obj : leftReferences) {
				if (obj.eIsProxy()) {
					addedProxies.add(obj);
				} else {
					addedReferences.add(obj);
				}
			}
		}
		if (rightReferences != null) {
			deletedReferences.addAll(rightReferences);
		}
		final List<EObject> matchedNewReferences = getMatchedReferences(addedReferences);

		// If the same object is both in left and right, it is neither added nor removed.
		deletedReferences.removeAll(addedReferences);
		// "deleted" references are the references from the right element that
		// have no counterpart in the left element
		deletedReferences.removeAll(matchedNewReferences);

		// Double check for proxies
		final Iterator<EObject> deletedCandidates = deletedReferences.iterator();
		while (deletedCandidates.hasNext() && !addedProxies.isEmpty()) {
			final EObject deleted = deletedCandidates.next();
			if (deleted.eIsProxy()) {
				final URI deletedURI = ((InternalEObject)deleted).eProxyURI();
				boolean hasMatch = false;
				final Iterator<EObject> candidateMatches = addedProxies.iterator();
				while (!hasMatch && candidateMatches.hasNext()) {
					final EObject candidate = candidateMatches.next();
					if (deletedURI.equals(((InternalEObject)candidate).eProxyURI())) {
						hasMatch = true;
						candidateMatches.remove();
					}
				}
				if (hasMatch) {
					deletedCandidates.remove();
				}
			}
		}

		return deletedReferences;
	}

	/**
	 * This will create the {@link ConflictingDiffGroup} and its children for a conflictual ReferenceChange.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement} to create.
	 * @param reference
	 *            Target {@link EReference} of the modification.
	 * @param mapping
	 *            Contains informations about the left, right and origin element where the given reference has
	 *            changed.
	 * @throws FactoryException
	 *             Thrown if we cannot create the underlying ReferenceChanges.
	 */
	private void createConflictingReferenceUpdate(DiffGroup root, EReference reference, Match3Elements mapping)
			throws FactoryException {
		// We'll use this diffGroup to make use of #createNonConflictingAttributeChange(DiffGroup, EAttribute,
		// EObject, EObject)
		final DiffGroup dummyGroup = DiffFactory.eINSTANCE.createDiffGroup();
		createNonConflictingReferencesUpdate(dummyGroup, reference, mapping.getLeftElement(),
				mapping.getRightElement());

		if (dummyGroup.getSubDiffElements().size() > 0) {
			final ConflictingDiffElement conflictingDiff = DiffFactory.eINSTANCE
					.createConflictingDiffElement();
			conflictingDiff.setLeftParent(mapping.getLeftElement());
			conflictingDiff.setRightParent(mapping.getRightElement());
			conflictingDiff.setOriginElement(mapping.getOriginElement());
			for (final DiffElement subDiff : new ArrayList<DiffElement>(dummyGroup.getSubDiffElements())) {
				conflictingDiff.getSubDiffElements().add(subDiff);
			}
			root.getSubDiffElements().add(conflictingDiff);
		}
	}

	/**
	 * Creates the {@link DiffGroup} corresponding to a reference's value addition under the given
	 * {@link DiffGroup}.<br/>
	 * The parameters include the list of added references which can be computed using
	 * {@link #computeAddedReferences(List, List)}.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement}s to create.
	 * @param left
	 *            Left element of the reference change.
	 * @param right
	 *            Right element of the reference change.
	 * @param reference
	 *            {@link EReference} target of the operation.
	 * @param addedReferences
	 *            {@link List} of reference values that have been added in the <code>right</code> element
	 *            since the <code>left</code> element.
	 * @return The list of created differences, an empty list if none.
	 */
	private List<ReferenceChangeLeftTarget> createNewReferencesOperation(DiffGroup root, EObject left,
			EObject right, EReference reference, List<EObject> addedReferences) {
		final List<ReferenceChangeLeftTarget> result = new ArrayList<ReferenceChangeLeftTarget>();
		final Iterator<EObject> addedReferenceIterator = addedReferences.iterator();
		while (addedReferenceIterator.hasNext()) {
			final EObject eobj = addedReferenceIterator.next();
			final ReferenceChangeLeftTarget addOperation = DiffFactory.eINSTANCE
					.createReferenceChangeLeftTarget();
			addOperation.setRightElement(right);
			addOperation.setLeftElement(left);
			addOperation.setReference(reference);
			addOperation.setLeftTarget(eobj);
			if (getMatchManager().getMatchedEObject(eobj) != null) {
				addOperation.setRightTarget(getMatchManager().getMatchedEObject(eobj));
			}
			root.getSubDiffElements().add(addOperation);
			result.add(addOperation);
		}
		return result;
	}

	/**
	 * This will check the given <code>reference</code> for modification between <code>leftElement</code> and
	 * <code>rightElement</code> and create the corresponding {@link DiffElement}s under the given
	 * {@link DiffGroup}.
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param reference
	 *            {@link EReference} to check for modifications.
	 * @param leftElement
	 *            Element corresponding to the final value for the given reference.
	 * @param rightElement
	 *            Element corresponding to the initial value for the given reference.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch <code>reference</code>'s values for either the left or the right
	 *             element.
	 */
	private void createNonConflictingReferencesUpdate(DiffGroup root, EReference reference,
			EObject leftElement, EObject rightElement) throws FactoryException {
		final List<Object> leftElementObjReferences = convertFeatureMapList(EFactory.eGetAsList(leftElement,
				reference.getName()));
		final List<Object> rightElementObjReferences = convertFeatureMapList(EFactory.eGetAsList(
				rightElement, reference.getName()));

		// All values should be EObjects
		final List<EObject> leftElementReferences = new ArrayList<EObject>();
		final List<EObject> rightElementReferences = new ArrayList<EObject>();
		for (Object left : leftElementObjReferences) {
			leftElementReferences.add((EObject)left);
		}
		for (Object right : rightElementObjReferences) {
			rightElementReferences.add((EObject)right);
		}

		final List<EObject> deletedReferences = computeDeletedReferences(leftElementReferences,
				rightElementReferences);
		final List<EObject> addedReferences = computeAddedReferences(leftElementReferences,
				rightElementReferences);

		// REFERENCES UPDATES
		if (!reference.isMany()) {
			EObject addedValue = null;
			EObject deletedValue = null;

			if (addedReferences.size() > 0) {
				addedValue = addedReferences.get(0);
			}
			if (deletedReferences.size() > 0) {
				deletedValue = deletedReferences.get(0);
			}

			if (getMatchManager().isUnmatched(addedValue) || getMatchManager().isUnmatched(deletedValue)
					|| areDistinct(addedValue, deletedValue)) {
				root.getSubDiffElements().add(
						createUpdatedReferenceOperation(leftElement, rightElement, reference, addedValue,
								deletedValue));
			}
		} else {
			// check that added references are not in deleted reference
			final Iterator<EObject> addedIterator = addedReferences.iterator();
			while (addedIterator.hasNext()) {
				final EObject added = addedIterator.next();
				final Iterator<EObject> deletedIterator = deletedReferences.iterator();
				while (deletedIterator.hasNext()) {
					final EObject deleted = deletedIterator.next();
					if (!areDistinct(added, deleted)) {
						addedIterator.remove();
						deletedIterator.remove();
					}
				}
			}

			final List<ReferenceChangeLeftTarget> addedReferencesDiffs = new ArrayList<ReferenceChangeLeftTarget>(
					addedReferences.size());
			final List<ReferenceChangeRightTarget> removedReferencesDiffs = new ArrayList<ReferenceChangeRightTarget>(
					deletedReferences.size());
			// REFERENCES ADD
			if (addedReferences.size() > 0) {
				addedReferencesDiffs.addAll(createNewReferencesOperation(root, leftElement, rightElement,
						reference, addedReferences));
			}
			// REFERENCES DEL
			if (deletedReferences.size() > 0) {
				removedReferencesDiffs.addAll(createRemovedReferencesOperation(root, leftElement,
						rightElement, reference, deletedReferences));
			}
			// Check for references order changes
			if (reference.isOrdered()) {
				checkReferenceOrderChange(root, reference, leftElement, rightElement, addedReferencesDiffs,
						removedReferencesDiffs);
			}
		}
	}

	/**
	 * This will check for remote ReferenceChange operations and create the corresponding {@link DiffElement}
	 * s.<br/>
	 * <p>
	 * A reference is considered &quot;remotely changed&quot; if its values differ between the right (latest
	 * from HEAD) and origin (common ancestor) model, but its values haven't changed between the left (working
	 * copy) and the origin model.
	 * </p>
	 * 
	 * @param root
	 *            {@link DiffGroup Root} of the {@link DiffElement}s to create.
	 * @param reference
	 *            {@link EReference} to check for ReferenceChanges.
	 * @param mapping
	 *            Contains informations about the left, right and original model elements.
	 * @param remotelyAdded
	 *            {@link List} of reference values that have been added in the left model since the origin.
	 * @param remotelyDeleted
	 *            {@link List} of reference values that have been removed from the left model since the
	 *            origin.
	 */
	private void createRemoteReferencesUpdate(DiffGroup root, EReference reference, Match3Elements mapping,
			List<EObject> remotelyAdded, List<EObject> remotelyDeleted) {
		if (!reference.isMany() && (remotelyAdded.size() > 0 || remotelyDeleted.size() > 0)) {
			EObject remoteAdded = null;
			if (remotelyAdded.size() > 0) {
				remoteAdded = remotelyAdded.get(0);
			}
			EObject remoteDeleted = null;
			if (remotelyDeleted.size() > 0) {
				remoteDeleted = remotelyDeleted.get(0);
			}

			final UpdateReference operation = DiffFactory.eINSTANCE.createUpdateReference();
			operation.setRemote(true);
			operation.setLeftElement(mapping.getLeftElement());
			operation.setRightElement(mapping.getRightElement());
			operation.setReference(reference);

			EObject leftTarget = getMatchManager().getMatchedEObject(remoteAdded);
			EObject rightTarget = getMatchManager().getMatchedEObject(remoteDeleted);
			// checks if target are defined remotely
			if (leftTarget == null && remoteAdded != null) {
				leftTarget = remoteAdded;
			}
			if (rightTarget == null && remoteDeleted != null) {
				rightTarget = remoteDeleted;
			}

			operation.setLeftTarget(leftTarget);
			operation.setRightTarget(rightTarget);

			root.getSubDiffElements().add(operation);
		} else if (reference.isMany()) {
			final Iterator<EObject> addedReferenceIterator = remotelyAdded.iterator();
			while (addedReferenceIterator.hasNext()) {
				final EObject eobj = addedReferenceIterator.next();
				final ReferenceChangeRightTarget addOperation = DiffFactory.eINSTANCE
						.createReferenceChangeRightTarget();
				addOperation.setRemote(true);
				addOperation.setRightElement(mapping.getRightElement());
				addOperation.setLeftElement(mapping.getLeftElement());
				addOperation.setReference(reference);
				addOperation.setRightTarget(eobj);
				if (getMatchManager().getMatchedEObject(eobj) != null) {
					addOperation.setLeftTarget(getMatchManager().getMatchedEObject(eobj));
				}
				root.getSubDiffElements().add(addOperation);
			}
			final Iterator<EObject> deletedReferenceIterator = remotelyDeleted.iterator();
			while (deletedReferenceIterator.hasNext()) {
				final EObject eobj = deletedReferenceIterator.next();
				final ReferenceChangeLeftTarget delOperation = DiffFactory.eINSTANCE
						.createReferenceChangeLeftTarget();
				delOperation.setRemote(true);
				delOperation.setRightElement(mapping.getRightElement());
				delOperation.setLeftElement(mapping.getLeftElement());
				delOperation.setReference(reference);
				delOperation.setLeftTarget(eobj);
				if (getMatchManager().getMatchedEObject(eobj) != null) {
					delOperation.setRightTarget(getMatchManager().getMatchedEObject(eobj));
				}
				root.getSubDiffElements().add(delOperation);
			}
		}
	}

	/**
	 * Creates the {@link DiffGroup} corresponding to a reference's value removal under the given
	 * {@link DiffGroup}.<br/>
	 * The parameters include the list of removed references which can be computed using
	 * {@link #computeDeletedReferences(List, List)}.
	 * 
	 * @param root
	 *            {@link DiffGroup root} of the {@link DiffElement}s to create.
	 * @param left
	 *            Left element of the reference change.
	 * @param right
	 *            Right element of the reference change.
	 * @param reference
	 *            {@link EReference} target of the operation.
	 * @param deletedReferences
	 *            {@link List} of reference values that have been removed in the <code>right</code> element
	 *            since the <code>left</code> element.
	 * @return The list of created differences, an empty list if none.
	 */
	private List<ReferenceChangeRightTarget> createRemovedReferencesOperation(DiffGroup root, EObject left,
			EObject right, EReference reference, List<EObject> deletedReferences) {
		final List<ReferenceChangeRightTarget> result = new ArrayList<ReferenceChangeRightTarget>();
		final Iterator<EObject> deletedReferenceIterator = deletedReferences.iterator();
		while (deletedReferenceIterator.hasNext()) {
			final EObject eobj = deletedReferenceIterator.next();
			final ReferenceChangeRightTarget delOperation = DiffFactory.eINSTANCE
					.createReferenceChangeRightTarget();
			delOperation.setRightElement(right);
			delOperation.setLeftElement(left);
			delOperation.setReference(reference);
			delOperation.setRightTarget(eobj);
			if (getMatchManager().getMatchedEObject(eobj) != null) {
				delOperation.setLeftTarget(getMatchManager().getMatchedEObject(eobj));
			}
			root.getSubDiffElements().add(delOperation);
			result.add(delOperation);
		}
		return result;
	}

	/**
	 * Creates the {@link DiffElement} corresponding to an unique reference's value update.
	 * 
	 * @param left
	 *            Left element of the reference change.
	 * @param right
	 *            Right element of the reference change.
	 * @param reference
	 *            {@link EReference} target of the operation.
	 * @param addedValue
	 *            Value which has been added for the reference.
	 * @param deletedValue
	 *            Value that has been deleted from the reference.
	 * @return The {@link DiffElement} corresponding to an unique reference's value update
	 */
	private UpdateReference createUpdatedReferenceOperation(EObject left, EObject right,
			EReference reference, EObject addedValue, EObject deletedValue) {
		final UpdateReference operation = DiffFactory.eINSTANCE.createUpdateReference();
		operation.setLeftElement(left);
		operation.setRightElement(right);
		operation.setReference(reference);

		EObject leftTarget = getMatchManager().getMatchedEObject(deletedValue);
		EObject rightTarget = getMatchManager().getMatchedEObject(addedValue);
		// checks if target are defined remotely
		if (leftTarget == null && deletedValue != null) {
			leftTarget = deletedValue;
		}
		if (rightTarget == null && addedValue != null) {
			rightTarget = addedValue;
		}

		operation.setLeftTarget(leftTarget);
		operation.setRightTarget(rightTarget);

		return operation;
	}

	/**
	 * Returns the list of references from the given list that can be matched on either right or left
	 * {@link EObject}s.
	 * 
	 * @param references
	 *            {@link List} of the references to match.
	 * @return The list of references from the given list that can be matched on either right or left
	 *         {@link EObject}s.
	 */
	private List<EObject> getMatchedReferences(List<EObject> references) {
		final List<EObject> matchedReferences = new ArrayList<EObject>();
		final Iterator<EObject> refIterator = references.iterator();
		while (refIterator.hasNext()) {
			final Object currentReference = refIterator.next();
			if (currentReference != null) {
				final EObject currentMapped = getMatchManager().getMatchedEObject((EObject)currentReference);
				if (currentMapped != null) {
					matchedReferences.add(currentMapped);
				}
			}
		}
		return matchedReferences;
	}

	/**
	 * Checks if the values of a given reference have been changed both on the right (latest from head) and
	 * left (working copy) to distinct values since the origin.
	 * 
	 * @param reference
	 *            Reference we're checking for conflictual changes.
	 * @param leftReferences
	 *            {@link List} of values from the left (working copy) model for <code>reference</code>.
	 * @param rightReferences
	 *            {@link List} of values from the right (latest from head) model for <code>reference</code>.
	 * @param ancestorReferences
	 *            {@link List} of values from the origin (common ancestor) model for <code>reference</code>.
	 * @return <code>True</code> if there's been a conflictual change for the given {@link EReference},
	 *         <code>False</code> otherwise.
	 */
	private boolean isConflictual(EReference reference, List<?> leftReferences, List<?> rightReferences,
			List<?> ancestorReferences) {
		boolean isConflictual = false;
		// There CAN be a conflict ONLY if the reference is unique
		if (!reference.isMany()) {
			// If both left and right number of values have changed since origin...
			if (leftReferences.size() != ancestorReferences.size()
					&& rightReferences.size() != ancestorReferences.size()) {
				// ... There is a conflict if the value hasn't been erased AND
				// the left value is different than the right one
				if (leftReferences.size() > 0
						&& !leftReferences.get(0).equals(
								getMatchManager().getMatchedEObject((EObject)rightReferences.get(0)))) {
					isConflictual = true;
				}
				// If the number of values hasn't changed since the origin, there
				// cannot be a conflict if there are no values
			} else if (leftReferences.size() > 0 && rightReferences.size() > 0) {
				// There's a conflict if the values are distinct
				if (!leftReferences.get(0).equals(
						getMatchManager().getMatchedEObject((EObject)ancestorReferences.get(0),
								MatchSide.LEFT))
						&& !rightReferences.get(0).equals(
								getMatchManager().getMatchedEObject((EObject)ancestorReferences.get(0),
										MatchSide.RIGHT))
						&& !rightReferences.get(0).equals(
								getMatchManager().getMatchedEObject((EObject)leftReferences.get(0)))) {
					isConflictual = true;
				}
			}
		}
		return isConflictual;
	}

	/**
	 * Checks a given {@link EReference reference} for changes related to a given <code>mapping</code> and
	 * populates the given {@link List}s with the reference values belonging to them.
	 * 
	 * @param mapping
	 *            Contains informations about the left, right and origin elements.<br/>
	 *            <ul>
	 *            <li>&quot;Added&quot; values are the values that have been added in the left element since
	 *            the origin and that haven't been added in the right element.</li>
	 *            <li>&quot;Deleted&quot; values are the values that have been removed from the left element
	 *            since the origin but are still present in the right element.</li>
	 *            <li>&quot;Remotely added&quot; values are the values that have been added in the right
	 *            element since the origin but haven't been added in the left element.</li>
	 *            <li>&quot;Remotely deleted&quot; values are the values that have been removed from the right
	 *            element since the origin but are still present in the left element.</li>
	 *            </ul>
	 * @param reference
	 *            {@link EReference} we're checking for changes.
	 * @param addedReferences
	 *            {@link List} that will be populated with the values that have been added in the left element
	 *            since the origin.
	 * @param deletedReferences
	 *            {@link List} that will be populated with the values that have been removed from the left
	 *            element since the origin.
	 * @param remoteAddedReferences
	 *            {@link List} that will be populated with the values that have been added in the right
	 *            element since the origin.
	 * @param remoteDeletedReferences
	 *            {@link List} that will be populated with the values that have been removed from the right
	 *            element since the origin.
	 * @throws FactoryException
	 *             Thrown if we cannot fetch the reference's values in either the left, right or origin
	 *             element.
	 */
	private void populateThreeWayReferencesChanges(Match3Elements mapping, EReference reference,
			List<EObject> addedReferences, List<EObject> deletedReferences,
			List<EObject> remoteAddedReferences, List<EObject> remoteDeletedReferences)
			throws FactoryException {
		final List<Object> leftReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getLeftElement(), reference.getName()));
		final List<Object> rightReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getRightElement(), reference.getName()));
		final List<Object> ancestorReferences = convertFeatureMapList(EFactory.eGetAsList(
				mapping.getOriginElement(), reference.getName()));
		// populates remotely added and locally deleted lists
		final List<Object> leftCopy = new ArrayList<Object>(leftReferences);
		List<Object> ancestorCopy = new ArrayList<Object>(ancestorReferences);
		for (final Object right : rightReferences) {
			EObject leftMatched = null;
			EObject ancestorMatched = null;
			boolean hasLeftMatch = false;
			boolean hasAncestorMatch = false;
			if (right instanceof EObject && !((EObject)right).eIsProxy()) {
				ancestorMatched = getMatchManager().getMatchedEObject((EObject)right, MatchSide.ANCESTOR);
				leftMatched = getMatchManager().getMatchedEObject((EObject)right);
				hasLeftMatch = leftMatched != null && leftCopy.contains(leftMatched);
				hasAncestorMatch = ancestorMatched != null && ancestorCopy.contains(ancestorMatched);
			} else if (right instanceof EObject && ((EObject)right).eIsProxy()) {
				final Iterator<Object> ancestorIterator = ancestorCopy.iterator();
				while (ancestorMatched == null && ancestorIterator.hasNext()) {
					final Object ancestor = ancestorIterator.next();
					if (!areDistinct((EObject)right, (EObject)ancestor)) {
						ancestorMatched = (EObject)ancestor;
					}
				}
				final Iterator<Object> leftIterator = leftCopy.iterator();
				while (leftMatched == null && leftIterator.hasNext()) {
					final Object left = leftIterator.next();
					if (!areDistinct((EObject)right, (EObject)left)) {
						leftMatched = (EObject)left;
					}
				}
				hasLeftMatch = leftMatched != null;
				hasAncestorMatch = ancestorMatched != null;
			}
			// Take remote unmatched into account
			hasAncestorMatch = hasAncestorMatch || getMatchManager() instanceof IMatchManager2
					&& ((IMatchManager2)getMatchManager()).isRemoteUnmatched((EObject)right);
			if (!hasLeftMatch && !hasAncestorMatch) {
				remoteAddedReferences.add((EObject)right);
			} else if (!hasLeftMatch) {
				deletedReferences.add((EObject)right);
			}
			if (leftMatched != null) {
				leftCopy.remove(leftMatched);
			}
			if (ancestorMatched != null) {
				ancestorCopy.remove(ancestorMatched);
			}
		}
		// populates remotely deleted and locally added lists
		final List<Object> rightCopy = new ArrayList<Object>(rightReferences);
		ancestorCopy = new ArrayList<Object>(ancestorReferences);
		for (final Object left : leftReferences) {
			EObject rightMatched = null;
			EObject ancestorMatched = null;
			boolean hasRightMatch = false;
			boolean hasAncestorMatch = false;
			if (left instanceof EObject && !((EObject)left).eIsProxy()) {
				ancestorMatched = getMatchManager().getMatchedEObject((EObject)left, MatchSide.ANCESTOR);
				rightMatched = getMatchManager().getMatchedEObject((EObject)left);
				hasRightMatch = rightMatched != null && rightCopy.contains(rightMatched);
				hasAncestorMatch = ancestorMatched != null && ancestorCopy.contains(ancestorMatched);
			} else if (left instanceof EObject && ((EObject)left).eIsProxy()) {
				final Iterator<Object> ancestorIterator = ancestorCopy.iterator();
				while (ancestorMatched == null && ancestorIterator.hasNext()) {
					final Object ancestor = ancestorIterator.next();
					if (!areDistinct((EObject)left, (EObject)ancestor)) {
						ancestorMatched = (EObject)ancestor;
					}
				}
				final Iterator<Object> rightIterator = rightCopy.iterator();
				while (rightMatched == null && rightIterator.hasNext()) {
					final Object right = rightIterator.next();
					if (!areDistinct((EObject)left, (EObject)right)) {
						rightMatched = (EObject)right;
					}
				}
				hasRightMatch = rightMatched != null;
				hasAncestorMatch = ancestorMatched != null;
			}
			// Take remote unmatched into account
			hasAncestorMatch = hasAncestorMatch || getMatchManager() instanceof IMatchManager2
					&& ((IMatchManager2)getMatchManager()).isRemoteUnmatched((EObject)left);
			if (!hasRightMatch && !hasAncestorMatch) {
				addedReferences.add((EObject)left);
			} else if (!hasRightMatch) {
				remoteDeletedReferences.add((EObject)left);
			}
			if (rightMatched != null) {
				leftCopy.remove(rightMatched);
			}
			if (ancestorMatched != null) {
				ancestorCopy.remove(ancestorMatched);
			}
		}
	}
}
