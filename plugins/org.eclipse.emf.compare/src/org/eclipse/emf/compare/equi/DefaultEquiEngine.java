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
package org.eclipse.emf.compare.equi;

import java.util.Set;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * The requirements engine is in charge of actually computing the equivalences between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultEquiEngine implements IEquiEngine {

	/**
	 * Cross referencer which links business model objects to the related differences.
	 */
	private EcoreUtil.CrossReferencer crossReferencerModelObjectsToDiffs;

	/**
	 * Constructor.
	 */
	public DefaultEquiEngine() {
	}

	/**
	 * Constructor with a cross referencer.
	 * 
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	public DefaultEquiEngine(EcoreUtil.CrossReferencer crossReferencer) {
		this.crossReferencerModelObjectsToDiffs = crossReferencer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.equi.IEquiEngine#computeEquivalences(org.eclipse.emf.compare.Comparison)
	 */
	public void computeEquivalences(Comparison comparison) {
		if (crossReferencerModelObjectsToDiffs == null) {
			crossReferencerModelObjectsToDiffs = ReferenceUtil.initializeCrossReferencer(comparison);
		}

		for (Diff difference : comparison.getDifferences()) {
			checkForEquivalences(comparison, difference);
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForEquivalences(Comparison comparison, Diff difference) {
		// If reference change on an opposite reference
		if (difference instanceof ReferenceChange
				&& ((ReferenceChange)difference).getReference().getEOpposite() != null
				&& !((ReferenceChange)difference).getReference().getEOpposite().isContainer()
				&& !((ReferenceChange)difference).getReference().getEOpposite().isDerived()) {

			ReferenceChange diff = (ReferenceChange)difference;

			Equivalence equivalence = diff.getEquivalence();
			if (equivalence == null) {
				// If no equivalence, create one
				equivalence = CompareFactory.eINSTANCE.createEquivalence();
				comparison.getEquivalences().add(equivalence);

				// Add the current difference to the equivalence
				equivalence.getDifferences().add(diff);

				/*
				 * Add the difference where the value is the object containing the current difference, which
				 * is contained by the value of the current difference, where the reference is linked to the
				 * opposite one
				 */
				Set<ReferenceChange> equivalentDifferences = ReferenceUtil.getCrossReferences(
						crossReferencerModelObjectsToDiffs, MatchUtil.getContainer(comparison, diff),
						ComparePackage.eINSTANCE.getReferenceChange_Value(), ReferenceChange.class);
				for (ReferenceChange referenceChange : equivalentDifferences) {
					if (referenceChange.getReference().getEOpposite() != null
							&& referenceChange.getReference().getEOpposite().equals(diff.getReference())
							&& diff.getValue().equals(MatchUtil.getContainer(comparison, referenceChange))) {
						equivalence.getDifferences().add(referenceChange);
						break;
					}
				}

				// Add the change differences on the old references (origin)
				addChangesFromOrigin(comparison, diff, equivalence);
			}
		}
	}

	/**
	 * Add to the given <code>equivalence</code> potential changes based on the same reference as the given
	 * <code>diff</code>.
	 * 
	 * @param comparison
	 *            The comparison which enable to know from which side come the business model objects owning
	 *            the differences.
	 * @param diff
	 *            The current difference.
	 * @param equivalence
	 *            The current equivalence attached to the difference.
	 */
	private void addChangesFromOrigin(Comparison comparison, ReferenceChange diff, Equivalence equivalence) {
		if (!diff.getReference().isMany()) {
			EObject originContainer = MatchUtil.getOriginContainer(comparison, diff);
			Set<ReferenceChange> equivalentDifferences2 = ReferenceUtil.getCrossReferences(
					crossReferencerModelObjectsToDiffs, originContainer, ComparePackage.eINSTANCE
							.getReferenceChange_Value(), ReferenceChange.class);

			for (ReferenceChange referenceChange : equivalentDifferences2) {
				if (MatchUtil.getContainer(comparison, referenceChange).equals(
						originContainer.eGet(diff.getReference()))) {
					equivalence.getDifferences().add(referenceChange);
				}
			}
		}
	}

}
