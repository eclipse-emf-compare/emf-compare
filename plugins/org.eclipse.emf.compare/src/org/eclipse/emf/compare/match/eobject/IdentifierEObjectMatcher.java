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
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This implementation of an {@link IEObjectMatcher} will create {@link Match}es based on the input EObjects
 * identifiers (either XMI:ID or attribute ID) alone.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class IdentifierEObjectMatcher implements IEObjectMatcher {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.IEObjectMatcher#createMatches(java.util.Iterator,
	 *      java.util.Iterator, java.util.Iterator)
	 */
	public Iterable<Match> createMatches(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects) {
		final Set<Match> matches = Sets.newLinkedHashSet();

		// This lookup map will be used by iterations on right and origin to find the match in which they
		// should add themselves
		final Map<String, Match> idToMatch = Maps.newHashMap();

		// We will try and mimic the structure of the input model.
		// These map do not need to be ordered, we only need fast lookup.
		final Map<EObject, Match> leftEObjectsToMatch = Maps.newHashMap();
		final Map<EObject, Match> rightEObjectsToMatch = Maps.newHashMap();
		final Map<EObject, Match> originEObjectsToMatch = Maps.newHashMap();

		// We'll only iterate once on each of the three sides, building the matches as we go
		while (leftEObjects.hasNext()) {
			final EObject left = leftEObjects.next();

			final Match match = CompareFactory.eINSTANCE.createMatch();
			match.setLeft(left);

			// Can we find a parent? Assume we're iterating in containment order
			final EObject parentEObject = left.eContainer();
			final Match parent = leftEObjectsToMatch.get(parentEObject);
			if (parent != null) {
				parent.getSubMatches().add(match);
			} else {
				matches.add(match);
			}

			final String identifier = getID(left);
			if (identifier != null) {
				idToMatch.put(identifier, match);
			}
			leftEObjectsToMatch.put(left, match);
		}

		while (rightEObjects.hasNext()) {
			final EObject right = rightEObjects.next();

			// Do we have an existing match?
			final String identifier = getID(right);
			Match match = idToMatch.get(identifier);
			if (match != null) {
				match.setRight(right);

				rightEObjectsToMatch.put(right, match);
			} else {
				// Otherwise, create and place it.
				match = CompareFactory.eINSTANCE.createMatch();
				match.setRight(right);

				// Can we find a parent?
				final EObject parentEObject = right.eContainer();
				final Match parent = rightEObjectsToMatch.get(parentEObject);
				if (parent != null) {
					parent.getSubMatches().add(match);
				} else {
					matches.add(match);
				}

				if (identifier != null) {
					idToMatch.put(identifier, match);
				}
				rightEObjectsToMatch.put(right, match);
			}
		}

		while (originEObjects.hasNext()) {
			final EObject origin = originEObjects.next();

			// Do we have an existing match?
			final String identifier = getID(origin);
			Match match = idToMatch.get(identifier);
			if (match != null) {
				match.setOrigin(origin);

				originEObjectsToMatch.put(origin, match);
			} else {
				// Otherwise, create and place it.
				match = CompareFactory.eINSTANCE.createMatch();
				match.setOrigin(origin);

				// Can we find a parent?
				final EObject parentEObject = origin.eContainer();
				final Match parent = originEObjectsToMatch.get(parentEObject);
				if (parent != null) {
					parent.getSubMatches().add(match);
				} else {
					matches.add(match);
				}

				if (identifier != null) {
					idToMatch.put(identifier, match);
				}
				originEObjectsToMatch.put(origin, match);
			}
		}

		return matches;
	}

	/**
	 * This will be used to retrieve either the eIdAttribute's value for the given EObject or its XMI:ID. If
	 * none are set, we'll return <code>null</code>.
	 * <p>
	 * Note that if the eIdAttribute of this EObject is set, we will not try and find an XMI:ID.
	 * </p>
	 * 
	 * @param eObject
	 *            The EObject for which we need an identifier.
	 * @return The identifier for that EObject, be it the value of this EObject's eIdAttribute (if it is set)
	 *         or an XMI:ID. Could be <code>null</code> if none are set.
	 */
	protected String getID(EObject eObject) {
		String identifier = EcoreUtil.getID(eObject);
		if (identifier == null) {
			final Resource eObjectResource = eObject.eResource();
			if (eObjectResource instanceof XMIResource) {
				identifier = ((XMIResource)eObjectResource).getID(eObject);
			}
		}
		return identifier;
	}
}
