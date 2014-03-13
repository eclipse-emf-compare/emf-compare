/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import com.google.common.collect.ImmutableSet;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EReference;

/**
 * This implementation of an {@link org.eclipse.emf.ecore.util.ECrossReferenceAdapter} will allow us to only
 * attach ourselves to the Match elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MatchCrossReferencer extends AbstractCompareECrossReferencerAdapter {
	/**
	 * We're only interested in the cross references that come from the Match.left, Match.right and
	 * Match.origin references.
	 */
	private static final ImmutableSet<EReference> INCLUDED_REFERENCES = ImmutableSet.of(
			ComparePackage.Literals.MATCH__LEFT, ComparePackage.Literals.MATCH__RIGHT,
			ComparePackage.Literals.MATCH__ORIGIN);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#addAdapter(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	protected void addAdapter(Notifier notifier) {
		if (notifier instanceof Match) {
			super.addAdapter(notifier);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected boolean isIncluded(EReference eReference) {
		return INCLUDED_REFERENCES.contains(eReference);
	}
}
