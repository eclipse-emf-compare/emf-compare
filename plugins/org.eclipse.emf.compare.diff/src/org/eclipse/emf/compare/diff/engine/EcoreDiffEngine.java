/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;

/**
 * This will add ecore-specific behavior to the {@link GenericDiffEngine}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EcoreDiffEngine extends GenericDiffEngine {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#checkMoves(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
	 *      org.eclipse.emf.compare.match.metamodel.Match2Elements)
	 */
	@Override
	protected void checkMoves(DiffGroup root, Match2Elements matchElement) {
		final EObject left = matchElement.getLeftElement();
		final EObject right = matchElement.getRightElement();

		if (!(left instanceof EGenericType || right instanceof EGenericType)) {
			super.checkMoves(root, matchElement);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#checkMoves(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
	 *      org.eclipse.emf.compare.match.metamodel.Match3Element)
	 */
	@Override
	protected void checkMoves(DiffGroup root, Match3Elements matchElement) {
		final EObject leftElement = matchElement.getLeftElement();
		final EObject rightElement = matchElement.getRightElement();
		final EObject originElement = matchElement.getOriginElement();

		if (!(leftElement instanceof EGenericType || rightElement instanceof EGenericType || originElement instanceof EGenericType)) {
			super.checkMoves(root, matchElement);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#processUnmatchedElements(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
	 *      java.util.List)
	 */
	@Override
	protected void processUnmatchedElements(DiffGroup diffRoot, List<UnmatchElement> unmatched) {
		final List<UnmatchElement> filteredUnmatched = new ArrayList<UnmatchElement>(unmatched.size());
		for (final UnmatchElement element : unmatched) {
			if (!(element.getElement() instanceof EGenericType)) {
				filteredUnmatched.add(element);
			}
		}
		super.processUnmatchedElements(diffRoot, filteredUnmatched);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#processUnmatchedElements(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
	 *      java.util.Map)
	 */
	@Override
	protected void processUnmatchedElements(DiffGroup diffRoot, Map<UnmatchElement, Boolean> unmatched) {
		final Map<UnmatchElement, Boolean> filteredUnmatched = new HashMap<UnmatchElement, Boolean>(unmatched
				.size());
		for (final Entry<UnmatchElement, Boolean> element : unmatched.entrySet()) {
			if (!(element.getKey().getElement() instanceof EGenericType)) {
				filteredUnmatched.put(element.getKey(), element.getValue());
			}
		}
		super.processUnmatchedElements(diffRoot, unmatched);
	}
}
