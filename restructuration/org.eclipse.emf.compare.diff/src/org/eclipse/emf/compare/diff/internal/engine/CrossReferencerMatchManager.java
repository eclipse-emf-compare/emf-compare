/*******************************************************************************
 * Copyright (c) 2011 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Victor Roldan Betancort - [352002] initial API and implementation     
 *     Obeo
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.engine;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.compare.diff.engine.IMatchManager;
import org.eclipse.emf.compare.diff.engine.IMatchManager2;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * An IMatchManager that uses a CrossReferencer to determine matches for an <code>EObject</code>.
 * 
 * @author Victor Roldan Betancort
 * @see IMatchManager
 */
public class CrossReferencerMatchManager implements IMatchManager, IMatchManager2 {
	/**
	 * If we're currently doing a resourceSet differencing, this will have been initialized with the whole
	 * MatchResourceSet.
	 */
	private EcoreUtil.CrossReferencer crossReferencer;

	/**
	 * Instantiates this match manager given the CrossReferencer from which to retrieve the mappings.
	 * 
	 * @param referencer
	 *            The CrossReferencer containing the mappings.
	 */
	public CrossReferencerMatchManager(EcoreUtil.CrossReferencer referencer) {
		this.crossReferencer = referencer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager#getMatchedEObject(org.eclipse.emf.ecore.EObject)
	 */
	public EObject getMatchedEObject(EObject from) {
		EObject matchedEObject = null;
		if (crossReferencer != null && from != null) {
			final Collection<EStructuralFeature.Setting> settings = crossReferencer.get(from);
			if (settings == null) {
				return null;
			}
			for (final org.eclipse.emf.ecore.EStructuralFeature.Setting setting : crossReferencer.get(from)) {
				if (setting.getEObject() instanceof Match2Elements) {
					if (setting.getEStructuralFeature().getFeatureID() == MatchPackage.MATCH2_ELEMENTS__LEFT_ELEMENT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getRightElement();
					} else if (setting.getEStructuralFeature().getFeatureID() == MatchPackage.MATCH2_ELEMENTS__RIGHT_ELEMENT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getLeftElement();
					}
				}
			}
		}
		return matchedEObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager#getMatchedEObject(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide)
	 */
	public EObject getMatchedEObject(EObject from, MatchSide side) {
		EObject matchedEObject = null;
		if (crossReferencer != null && from != null) {
			final Collection<EStructuralFeature.Setting> settings = crossReferencer.get(from);
			if (settings == null) {
				return null;
			}
			for (final org.eclipse.emf.ecore.EStructuralFeature.Setting setting : settings) {
				if (setting.getEObject() instanceof Match2Elements) {
					if (side == MatchSide.LEFT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getLeftElement();
					} else if (side == MatchSide.RIGHT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getRightElement();
					} else if (setting.getEObject() instanceof Match3Elements) {
						matchedEObject = ((Match3Elements)setting.getEObject()).getOriginElement();
					}
				}
			}
		}
		return matchedEObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager#isInScope(org.eclipse.emf.ecore.EObject)
	 */
	public boolean isInScope(EObject eObj) {
		if (crossReferencer != null && eObj != null && crossReferencer.get(eObj) != null) {
			for (final org.eclipse.emf.ecore.EStructuralFeature.Setting setting : crossReferencer.get(eObj)) {
				if (setting.getEObject() instanceof Match2Elements
						|| setting.getEObject() instanceof UnmatchElement) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager2#isRemoteUnmatched(org.eclipse.emf.ecore.EObject)
	 */
	public boolean isRemoteUnmatched(EObject element) {
		if (crossReferencer != null && crossReferencer.get(element) != null) {
			final Iterator<EStructuralFeature.Setting> it = crossReferencer.get(element).iterator();
			if (it.hasNext()) {
				final EObject next = it.next().getEObject();
				if (next instanceof UnmatchElement && ((UnmatchElement)next).isRemote()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager#isUnmatched(org.eclipse.emf.ecore.EObject)
	 */
	public boolean isUnmatched(EObject element) {
		if (crossReferencer != null && crossReferencer.get(element) != null) {
			final Iterator<EStructuralFeature.Setting> it = crossReferencer.get(element).iterator();
			if (it.hasNext() && it.next().getEObject() instanceof UnmatchElement) {
				return true;
			}
		}
		return false;
	}
}
