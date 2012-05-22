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
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EReference;
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
		if (difference instanceof ReferenceChange) {
			ReferenceChange diff = (ReferenceChange)difference;
			if (diff.getReference().getEOpposite() != null) {
				Equivalence equivalence = diff.getEquivalence();
				if (equivalence == null) {
					equivalence = CompareFactory.eINSTANCE.createEquivalence();
					comparison.getEquivalences().add(equivalence);
					equivalence.getDifferences().add(difference);
					// Find the opposite difference to reference it from the equivalence:
					EReference oppositeReference = diff.getReference().getEOpposite();
					Set<Diff> equivalentDifferences = ReferenceUtil.getCrossReferences(
							crossReferencerModelObjectsToDiffs, oppositeReference, ComparePackage.eINSTANCE
									.getReferenceChange_Reference(), Diff.class);
					for (Diff eqDiff : equivalentDifferences) {
						equivalence.getDifferences().add(eqDiff);
					}
				}
			}
		}
	}

}
