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
package org.eclipse.emf.compare.req;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * The requirements engine is in charge of actually computing the requirements between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities. TODO to test on XSD models for FeatureMaps
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultReqEngine implements IReqEngine {

	/**
	 * Cross referencer which links business model objects to the related differences.
	 */
	private EcoreUtil.CrossReferencer crossReferencerModelObjectsToDiffs;

	/**
	 * Constructor.
	 */
	public DefaultReqEngine() {
	}

	/**
	 * Constructor with a cross referencer.
	 * 
	 * @param crossReferencerModelObjectsToDiffs
	 *            The cross referencer.
	 */
	public DefaultReqEngine(EcoreUtil.CrossReferencer crossReferencerModelObjectsToDiffs) {
		this.crossReferencerModelObjectsToDiffs = crossReferencerModelObjectsToDiffs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.IDiffEngine#computeRequirements(org.eclipse.emf.compare.Comparison)
	 */
	public void computeRequirements(Comparison comparison) {
		if (crossReferencerModelObjectsToDiffs == null) {
			crossReferencerModelObjectsToDiffs = ReferenceUtil.initializeCrossReferencer(comparison);
		}

		for (Diff difference : comparison.getDifferences()) {
			checkForRequiredDifferences(comparison, difference);
		}
	}

	/**
	 * Checks the potential required differences from the given <code>difference</code>. If the given
	 * <code>difference</code> is an ADD/CHANGE or DELETE of a business model object and this object
	 * references other business model objects, respectively added or deleted too, then the related
	 * differences are required by the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForRequiredDifferences(Comparison comparison, Diff difference) {
		if (isConcernedByRequirements(difference)) {

			// Look for all the other business model objects linked to the value object of the difference.
			Set<EObject> referencedObjects = getReferencedObjects((ReferenceChange)difference);

			// For each of them, look for existing other equivalent differences
			for (EObject modelObj : referencedObjects) {
				Set<Diff> requiredDifferences = ReferenceUtil.getCrossReferences(
						crossReferencerModelObjectsToDiffs, modelObj, ComparePackage.eINSTANCE
								.getReferenceChange_Value(), Diff.class);
				if (requiredDifferences != null) {
					for (Diff diff : requiredDifferences) {
						if (isConcernedByRequirements(diff)
								&& ((ReferenceChange)diff).getReference().isContainment()
								&& !diff.equals(difference)) {
							difference.getRequires().add(diff);
						}
					}
				}
			}

		}
	}

	/**
	 * Get the business model objects linked to the value object of the given difference.
	 * 
	 * @param difference
	 *            The reference change.
	 * @return A set of linked objects.
	 */
	private static Set<EObject> getReferencedObjects(ReferenceChange difference) {
		Set<EObject> referencedObjects = new HashSet<EObject>();

		// Retrieve the business model object concerned by this difference.
		EObject concernedObject = difference.getValue();

		if (difference.getReference().isContainment()) {
			// Look for all outgoing references.
			/*
			 * TODO: to study if this call is necessary or can be replaced by eCrossReferences().
			 * referencedObjects.addAll(ReferenceUtil.getReferencedEObjects(concernedObject, true));
			 */
			referencedObjects.addAll(concernedObject.eCrossReferences());

			// Add of the parent model object.
			if (concernedObject.eContainer() != null) {
				referencedObjects.add(concernedObject.eContainer());
			}
		} else {
			List<EObject> refContainers = getModelObjects(difference.getMatch());
			EReference concernedRef = difference.getReference();
			if (concernedRef.isMany()) {
				referencedObjects.add(concernedObject); // We have to retrieve containment differences on it.
			} else {
				// Add of the objects from ALL sides (necessity to get old value too in a CHANGE) -> maybe to
				// fix (2 change differences instead of 1 ?)
				for (EObject refContainer : refContainers) {
					Object obj = refContainer.eGet(concernedRef);
					referencedObjects.add((EObject)obj);
				}
			}

			// Add of the container of the reference.
			referencedObjects.addAll(refContainers);
		}

		return referencedObjects;
	}

	/**
	 * Check if the given <code>difference</code> is a candidate to compute requirements.
	 * 
	 * @param difference
	 *            The given difference.
	 * @return True if the given difference is a good candidate, False else.
	 */
	private static boolean isConcernedByRequirements(Diff difference) {
		return difference instanceof ReferenceChange
				&& (difference.getKind().equals(DifferenceKind.ADD)
						|| difference.getKind().equals(DifferenceKind.CHANGE) || difference.getKind().equals(
						DifferenceKind.DELETE));
	}

	/**
	 * Return all the business model objects related to the given <code>match</code>.
	 * 
	 * @param match
	 *            The given match.
	 * @return The list of the related business model objects.
	 */
	private static List<EObject> getModelObjects(Match match) {
		List<EObject> result = new ArrayList<EObject>();
		if (match.getLeft() != null) {
			result.add(match.getLeft());
		}
		if (match.getRight() != null) {
			result.add(match.getRight());
		}
		if (match.getOrigin() != null) {
			result.add(match.getOrigin());
		}
		return result;
	}

}
