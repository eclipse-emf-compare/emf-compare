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
package org.eclipse.emf.compare.internal;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This implementation of an {@link ECrossReferenceAdapter} will allow us to only attach ourselves to the
 * Match elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MatchCrossReferencer extends ECrossReferenceAdapter {
	/**
	 * We're only interested in the cross references that come from the Match.left, Match.right and
	 * Match.origin references.
	 */
	private final Set<EReference> includedReferences;

	/**
	 * Default constructor.
	 */
	public MatchCrossReferencer() {
		final ComparePackage pack = ComparePackage.eINSTANCE;
		includedReferences = Sets.newHashSet(pack.getMatch_Left(), pack.getMatch_Right(), pack
				.getMatch_Origin());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#getInverseReferences(org.eclipse.emf.ecore.EObject,
	 *      boolean)
	 */
	@Override
	public Collection<Setting> getInverseReferences(EObject eObject, boolean resolve) {
		Collection<EStructuralFeature.Setting> nonNavigableInverseReferences = inverseCrossReferencer
				.get(eObject);
		if (nonNavigableInverseReferences == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(nonNavigableInverseReferences);
	}

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
		if (super.isIncluded(eReference)) {
			return includedReferences.contains(eReference);
		}
		return false;
	}
}
