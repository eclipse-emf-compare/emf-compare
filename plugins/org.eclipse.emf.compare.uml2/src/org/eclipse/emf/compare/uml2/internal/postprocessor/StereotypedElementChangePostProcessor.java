/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLStereotypedElementChangeFactory;

/**
 * Post processor that creates the {@link org.eclipse.emf.compare.uml2.internal.StereotypedElementChange} in
 * the comparison model.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypedElementChangePostProcessor implements IPostProcessor {

	/**
	 * {@link UMLStereotypedElementChangeFactory}.
	 */
	private UMLStereotypedElementChangeFactory factory = new UMLStereotypedElementChangeFactory();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(Comparison , Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(Comparison , Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(Comparison, Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(Comparison, Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(Comparison, Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(Comparison, Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		for (Diff difference : comparison.getDifferences()) {
			if (factory.handles(difference)) {
				Diff stereotypedElementChange = factory.create(difference);
				if (!stereotypedElementChange.getRefinedBy().isEmpty()) {
					final Match match = factory.getParentMatch(difference);
					// FIXME: why the match may be null ? (see AddAssociation2Test.testMergeLtRA30UseCase)
					if (match != null) {
						match.getDifferences().add(stereotypedElementChange);
					}
				}
			}
		}
	}

}
