/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Victor Roldan Betancort - [352002] introduce IMatchManager
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.engine;

import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of a Cross Referencer will only cross reference matched and unmatched elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MatchCrossReferencer extends EcoreUtil.CrossReferencer {
	/** Generic Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes our cross referencer given the match model to consider for mappings.
	 * 
	 * @param match
	 *            The match model to cross reference.
	 */
	public MatchCrossReferencer(MatchModel match) {
		super(match);
		crossReference();
	}

	/**
	 * Initializes our cross referencer given the match model to consider for mappings. Used in case of
	 * three-way comparisons.
	 * 
	 * @param matchResourceSet
	 *            The match model to cross reference.
	 */
	public MatchCrossReferencer(MatchResourceSet matchResourceSet) {
		super(matchResourceSet);
		crossReference();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean crossReference(EObject eObject, EReference eReference, EObject crossReferencedEObject) {
		// FIXME shouldn't we test for eObject instanceof Match2Elements?
		// Cross reference this if it is either one of the left, right, or ancestor elements
		boolean crossReference = eReference == MatchPackage.eINSTANCE.getMatch2Elements_LeftElement()
				|| eReference == MatchPackage.eINSTANCE.getMatch2Elements_RightElement()
				|| eReference == MatchPackage.eINSTANCE.getMatch3Elements_OriginElement();

		// Or if it is an unmatched element
		crossReference = crossReference || eReference == MatchPackage.eINSTANCE.getUnmatchElement_Element();

		if (crossReference) {
			super.crossReference(eObject, eReference, crossReferencedEObject);
		}

		return crossReference;
	}
}
