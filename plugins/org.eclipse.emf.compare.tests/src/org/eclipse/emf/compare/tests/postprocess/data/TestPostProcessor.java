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
package org.eclipse.emf.compare.tests.postprocess.data;

import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.extension.IPostProcessor;

/**
 * This provides a post processor to add a copy of the first scanned match at the same level of the
 * comparison.
 * 
 * @author <a href="cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TestPostProcessor implements IPostProcessor {

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison)
	 */
	public void postMatch(Comparison comparison) {
		final List<Match> matches = comparison.getMatches();
		final Match lastMatch = matches.get(matches.size() - 1);
		final Match copyLastMatch = CompareFactory.eINSTANCE.createMatch();
		copyLastMatch.setLeft(lastMatch.getLeft());
		copyLastMatch.setRight(lastMatch.getRight());
		copyLastMatch.setOrigin(lastMatch.getOrigin());
		matches.add(copyLastMatch);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison)
	 */
	public void postDiff(Comparison comparison) {

	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison)
	 */
	public void postRequirements(Comparison comparison) {

	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison)
	 */
	public void postEquivalences(Comparison comparison) {

	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.extension.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison)
	 */
	public void postConflicts(Comparison comparison) {

	}

}
