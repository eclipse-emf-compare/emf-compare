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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
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
	 * This instance might have a delegate matcher. The delegate matcher will be called when no ID is found
	 * and its results are aggregated with the current matcher.
	 */
	private Optional<IEObjectMatcher> delegate;

	/**
	 * This will be used to determine what represents the "identifier" of an EObject. By default, we will use
	 * the following logic, in order (i.e. if condition 1 is fulfilled, we will not try conditions 2 and 3) :
	 * <ol>
	 * <li>If the given eObject is a proxy, it is uniquely identified by its URI fragment.</li>
	 * <li>If the eObject's EClass has an eIdAttribute set, use this attribute's value.</li>
	 * <li>If the eObject is located in an XMI resource and has an XMI ID, use this as its unique identifier.</li>
	 * </ol>
	 */
	private Function<EObject, String> idComputation = new DefaultIDFunction();

	/**
	 * Create an ID based matcher without any delegate.
	 */
	public IdentifierEObjectMatcher() {
		this.delegate = Optional.absent();
	}

	/**
	 * Create an ID based matcher with a delegate which is going to be called when no ID is found for a given
	 * EObject.
	 * 
	 * @param delegateWhenNoID
	 *            the delegate matcher to use when no ID is found.
	 */
	public IdentifierEObjectMatcher(IEObjectMatcher delegateWhenNoID) {
		this.delegate = Optional.of(delegateWhenNoID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.IEObjectMatcher#createMatches(java.util.Iterator,
	 *      java.util.Iterator, java.util.Iterator)
	 */
	public Iterable<Match> createMatches(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects) {
		final List<EObject> leftEObjectsNoID = Lists.newArrayList();
		final List<EObject> rightEObjectsNoID = Lists.newArrayList();
		final List<EObject> originEObjectsNoID = Lists.newArrayList();

		final Set<Match> matches = matchPerId(leftEObjects, rightEObjects, originEObjects, leftEObjectsNoID,
				rightEObjectsNoID, originEObjectsNoID);

		if (delegate.isPresent()) {
			Iterables.addAll(matches, delegate.get().createMatches(leftEObjectsNoID.iterator(),
					rightEObjectsNoID.iterator(), originEObjectsNoID.iterator()));
		} else {
			for (EObject eObject : leftEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setLeft(eObject);
				matches.add(match);

			}
			for (EObject eObject : rightEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setRight(eObject);
				matches.add(match);
			}
			for (EObject eObject : originEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setOrigin(eObject);
				matches.add(match);
			}

		}

		return matches;
	}

	/**
	 * Matches the EObject per ID.
	 * 
	 * @param leftEObjects
	 *            the objects to match (left side).
	 * @param rightEObjects
	 *            the objects to match (right side).
	 * @param originEObjects
	 *            the objects to match (origin side).
	 * @param leftEObjectsNoID
	 *            remaining left objects after matching
	 * @param rightEObjectsNoID
	 *            remaining left objects after matching
	 * @param originEObjectsNoID
	 *            remaining left objects after matching
	 * @return the match built in the process.
	 */
	protected Set<Match> matchPerId(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
			final List<EObject> leftEObjectsNoID, final List<EObject> rightEObjectsNoID,
			final List<EObject> originEObjectsNoID) {
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

			final String identifier = idComputation.apply(left);
			if (identifier != null) {
				final Match match = CompareFactory.eINSTANCE.createMatch();
				match.setLeft(left);

				// Can we find a parent? Assume we're iterating in containment order
				final EObject parentEObject = left.eContainer();
				final Match parent = leftEObjectsToMatch.get(parentEObject);
				if (parent != null) {
					parent.getSubmatches().add(match);
				} else {
					matches.add(match);
				}

				idToMatch.put(identifier, match);
				leftEObjectsToMatch.put(left, match);
			} else {
				leftEObjectsNoID.add(left);
			}
		}

		while (rightEObjects.hasNext()) {
			final EObject right = rightEObjects.next();

			// Do we have an existing match?
			final String identifier = idComputation.apply(right);
			if (identifier != null) {
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
						parent.getSubmatches().add(match);
					} else {
						matches.add(match);
					}

					rightEObjectsToMatch.put(right, match);
					idToMatch.put(identifier, match);
				}
			} else {
				rightEObjectsNoID.add(right);
			}
		}

		while (originEObjects.hasNext()) {
			final EObject origin = originEObjects.next();

			// Do we have an existing match?
			final String identifier = idComputation.apply(origin);
			if (identifier != null) {
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
						parent.getSubmatches().add(match);
					} else {
						matches.add(match);
					}

					idToMatch.put(identifier, match);
					originEObjectsToMatch.put(origin, match);
				}
			} else {
				originEObjectsNoID.add(origin);
			}
		}
		return matches;
	}

	/**
	 * The default function used to retrieve IDs from EObject. You might want to extend or compose with it if
	 * you want to reuse its behavior.
	 */
	public static class DefaultIDFunction implements Function<EObject, String> {
		/**
		 * Return an ID for an EObject, null if not found.
		 * 
		 * @param eObject
		 *            The EObject for which we need an identifier.
		 * @return The identifier for that EObject if we could determine one. <code>null</code> if no
		 *         condition (see description above) is fulfilled for the given eObject.
		 */
		public String apply(EObject eObject) {
			final String identifier;
			if (eObject == null) {
				identifier = null;
			} else if (eObject.eIsProxy()) {
				identifier = ((InternalEObject)eObject).eProxyURI().fragment();
			} else {
				String functionalId = EcoreUtil.getID(eObject);
				if (functionalId != null) {
					identifier = functionalId;
				} else {
					final Resource eObjectResource = eObject.eResource();
					if (eObjectResource instanceof XMIResource) {
						identifier = ((XMIResource)eObjectResource).getID(eObject);
					} else {
						identifier = null;
					}
				}
			}
			return identifier;
		}
	}

	/**
	 * Returns a new Builder to construct an {@link IdentifierEObjectMatcher}.
	 * 
	 * @return the new Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * An utility class to configure an instance of {@link IdentifierEObjectMatcher}.
	 */
	public static class Builder {
		/**
		 * The instance under construction.
		 */
		private IdentifierEObjectMatcher toBe;

		/**
		 * Create the builder.
		 */
		public Builder() {
			this.toBe = new IdentifierEObjectMatcher();
		}

		/**
		 * Specify the function to use to compute the ID of an EObject.
		 * 
		 * @param idFunction
		 *            the function to use to compute the IDs.
		 * @return the current builder to ease chain calls.
		 */
		public Builder idFunction(Function<EObject, String> idFunction) {
			this.toBe.idComputation = idFunction;
			return this;
		}

		/**
		 * Specify the delegate to call when no ID is found.
		 * 
		 * @param delegateWhenNoID
		 *            the matcher to call when no id is found.
		 * @return the current builder to ease chain calls.
		 */
		public Builder delegateWhenNoId(IEObjectMatcher delegateWhenNoID) {
			this.toBe.delegate = Optional.of(delegateWhenNoID);
			return this;
		}

		/**
		 * return the instance under construction.
		 * 
		 * @return the instance under construction.
		 */
		public IdentifierEObjectMatcher build() {
			return this.toBe;
		}

	}
}
