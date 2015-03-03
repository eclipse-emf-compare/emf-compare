/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - Bug 450360
 *     Philip Langer - Bug 460778
 *     Stefan Dirix - Bugs 461011 and 461291
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

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
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

	/** A diagnostic to be used for reporting on the matches. */
	private BasicDiagnostic diagnostic;

	/**
	 * Creates an ID based matcher without any delegate.
	 */
	public IdentifierEObjectMatcher() {
		this(null, new DefaultIDFunction());
	}

	/**
	 * Creates an ID based matcher with the given delegate when no ID can be found.
	 * 
	 * @param delegateWhenNoID
	 *            the matcher to delegate to when no ID is found.
	 */
	public IdentifierEObjectMatcher(IEObjectMatcher delegateWhenNoID) {
		this(delegateWhenNoID, new DefaultIDFunction());
	}

	/**
	 * Creates an ID based matcher computing the ID with the given function.
	 * 
	 * @param idComputation
	 *            the function used to compute the ID.
	 */
	public IdentifierEObjectMatcher(Function<EObject, String> idComputation) {
		this(null, idComputation);
	}

	/**
	 * Create an ID based matcher with a delegate which is going to be called when no ID is found for a given
	 * EObject. It is computing the ID with the given function
	 * 
	 * @param delegateWhenNoID
	 *            the delegate matcher to use when no ID is found.
	 * @param idComputation
	 *            the function used to compute the ID.
	 */
	public IdentifierEObjectMatcher(IEObjectMatcher delegateWhenNoID, Function<EObject, String> idComputation) {
		this.delegate = Optional.fromNullable(delegateWhenNoID);
		this.idComputation = idComputation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createMatches(Comparison comparison, Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
			Monitor monitor) {
		if (monitor.isCanceled()) {
			throw new ComparisonCanceledException();
		}
		final List<EObject> leftEObjectsNoID = Lists.newArrayList();
		final List<EObject> rightEObjectsNoID = Lists.newArrayList();
		final List<EObject> originEObjectsNoID = Lists.newArrayList();

		diagnostic = new BasicDiagnostic(Diagnostic.OK, "org.eclipse.emf.common", 0, //$NON-NLS-1$
				org.eclipse.emf.common.CommonPlugin.INSTANCE.getString("_UI_OK_diagnostic_0"), null); //$NON-NLS-1$

		// TODO Change API to pass the monitor to matchPerId()
		final Set<Match> matches = matchPerId(leftEObjects, rightEObjects, originEObjects, leftEObjectsNoID,
				rightEObjectsNoID, originEObjectsNoID);

		addDiagnostic(comparison);

		Iterables.addAll(comparison.getMatches(), matches);

		if (!leftEObjectsNoID.isEmpty() || !rightEObjectsNoID.isEmpty() || !originEObjectsNoID.isEmpty()) {
			if (delegate.isPresent()) {
				doDelegation(comparison, leftEObjectsNoID, rightEObjectsNoID, originEObjectsNoID, monitor);
			} else {
				for (EObject eObject : leftEObjectsNoID) {
					if (monitor.isCanceled()) {
						throw new ComparisonCanceledException();
					}
					Match match = CompareFactory.eINSTANCE.createMatch();
					match.setLeft(eObject);
					matches.add(match);
				}
				for (EObject eObject : rightEObjectsNoID) {
					if (monitor.isCanceled()) {
						throw new ComparisonCanceledException();
					}
					Match match = CompareFactory.eINSTANCE.createMatch();
					match.setRight(eObject);
					matches.add(match);
				}
				for (EObject eObject : originEObjectsNoID) {
					if (monitor.isCanceled()) {
						throw new ComparisonCanceledException();
					}
					Match match = CompareFactory.eINSTANCE.createMatch();
					match.setOrigin(eObject);
					matches.add(match);
				}
			}
		}
	}

	/**
	 * Execute matching process for the delegated IEObjectMatcher.
	 * 
	 * @param comparison
	 *            the comparison object that contains the matches
	 * @param monitor
	 *            the monitor instance to track the matching progress
	 * @param leftEObjectsNoID
	 *            remaining left objects after matching
	 * @param rightEObjectsNoID
	 *            remaining right objects after matching
	 * @param originEObjectsNoID
	 *            remaining origin objects after matching
	 */
	protected void doDelegation(Comparison comparison, final List<EObject> leftEObjectsNoID,
			final List<EObject> rightEObjectsNoID, final List<EObject> originEObjectsNoID, Monitor monitor) {
		delegate.get().createMatches(comparison, leftEObjectsNoID.iterator(), rightEObjectsNoID.iterator(),
				originEObjectsNoID.iterator(), monitor);
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
	 *            remaining right objects after matching
	 * @param originEObjectsNoID
	 *            remaining origin objects after matching
	 * @return the match built in the process.
	 */
	protected Set<Match> matchPerId(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
			final List<EObject> leftEObjectsNoID, final List<EObject> rightEObjectsNoID,
			final List<EObject> originEObjectsNoID) {
		final Set<Match> matches = Sets.newLinkedHashSet();
		// This lookup map will be used by iterations on right and origin to find the match in which they
		// should add themselves

		SwitchMap<String, Match> idProxyMap = new SwitchMap<String, Match>();

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
				final EObject parentEObject = getParentEObject(left);
				final Match parent = leftEObjectsToMatch.get(parentEObject);
				if (parent != null) {
					((InternalEList<Match>)parent.getSubmatches()).addUnique(match);
				} else {
					matches.add(match);
				}

				final boolean isAlreadyContained = idProxyMap.put(left.eIsProxy(), identifier, match);
				if (isAlreadyContained) {
					reportDuplicateID(Side.LEFT, left);
				}
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
				Match match = idProxyMap.get(right.eIsProxy(), identifier);
				if (match != null) {
					if (match.getRight() != null) {
						reportDuplicateID(Side.RIGHT, right);
					}
					match.setRight(right);
					rightEObjectsToMatch.put(right, match);
				} else {
					// Otherwise, create and place it.
					match = CompareFactory.eINSTANCE.createMatch();
					match.setRight(right);

					// Can we find a parent?
					final EObject parentEObject = getParentEObject(right);
					final Match parent = rightEObjectsToMatch.get(parentEObject);
					if (parent != null) {
						((InternalEList<Match>)parent.getSubmatches()).addUnique(match);
					} else {
						matches.add(match);
					}

					rightEObjectsToMatch.put(right, match);
					idProxyMap.put(right.eIsProxy(), identifier, match);
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
				Match match = idProxyMap.get(origin.eIsProxy(), identifier);
				if (match != null) {
					if (match.getOrigin() != null) {
						reportDuplicateID(Side.ORIGIN, origin);
					}
					match.setOrigin(origin);

					originEObjectsToMatch.put(origin, match);
				} else {
					// Otherwise, create and place it.
					match = CompareFactory.eINSTANCE.createMatch();
					match.setOrigin(origin);

					// Can we find a parent?
					final EObject parentEObject = getParentEObject(origin);
					final Match parent = originEObjectsToMatch.get(parentEObject);
					if (parent != null) {
						((InternalEList<Match>)parent.getSubmatches()).addUnique(match);
					} else {
						matches.add(match);
					}

					idProxyMap.put(origin.eIsProxy(), identifier, match);
					originEObjectsToMatch.put(origin, match);
				}
			} else {
				originEObjectsNoID.add(origin);
			}
		}
		return matches;
	}

	/**
	 * This method is used to determine the parent objects during matching. The default implementation of this
	 * method returns the eContainer of the given {@code eObject}. Can be overwritten by clients to still
	 * allow proper matching when using a more complex architecture.
	 * 
	 * @param eObject
	 *            The {@link EObject} for which the parent object is to determine.
	 * @return The parent of the given {@code eObject}.
	 * @since 3.2
	 */
	protected EObject getParentEObject(EObject eObject) {
		return eObject.eContainer();
	}

	/**
	 * Adds a warning diagnostic to the comparison for the duplicate ID.
	 * 
	 * @param side
	 *            the side where the duplicate ID was found
	 * @param eObject
	 *            the element with the duplicate ID
	 */
	private void reportDuplicateID(Side side, EObject eObject) {
		final String duplicateID = idComputation.apply(eObject);
		final String sideName = side.name().toLowerCase();
		final String uriString = getUriString(eObject);
		final String message;
		if (uriString != null) {
			message = EMFCompareMessages.getString("IdentifierEObjectMatcher.duplicateIdWithResource", //$NON-NLS-1$
					duplicateID, sideName, uriString);
		} else {
			message = EMFCompareMessages.getString("IdentifierEObjectMatcher.duplicateId", //$NON-NLS-1$
					duplicateID, sideName);
		}
		diagnostic
				.add(new BasicDiagnostic(Diagnostic.WARNING, EMFCompare.DIAGNOSTIC_SOURCE, 0, message, null));
	}

	/**
	 * Returns a String representation of the URI of the given {@code eObject}'s resource.
	 * <p>
	 * If the {@code eObject}'s resource or its URI is <code>null</code>, this method returns
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param eObject
	 *            The {@link EObject} for which's resource the string representation of its URI is determined.
	 * @return A String representation of the given {@code eObject}'s resource URI.
	 */
	private String getUriString(EObject eObject) {
		String uriString = null;
		final Resource resource = eObject.eResource();
		if (resource != null && resource.getURI() != null) {
			final URI uri = resource.getURI();
			if (uri.isPlatform()) {
				uriString = uri.toPlatformString(true);
			} else {
				uriString = uri.toString();
			}
		}
		return uriString;
	}

	/**
	 * Adds the diagnostic to the comparison.
	 * 
	 * @param comparison
	 *            the comparison
	 */
	private void addDiagnostic(Comparison comparison) {
		if (comparison.getDiagnostic() == null) {
			comparison.setDiagnostic(diagnostic);
		} else {
			((BasicDiagnostic)comparison.getDiagnostic()).merge(diagnostic);
		}
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
	 * Helper class to manage two different maps within one class based on a switch boolean.
	 *
	 * @param <K>
	 *            The class used as key in the internal maps.
	 * @param <V>
	 *            The class used as value in the internal maps.
	 */
	private class SwitchMap<K, V> {

		/**
		 * Map used when the switch boolean is true.
		 */
		final Map<K, V> trueMap = Maps.newHashMap();

		/**
		 * Map used when the switch boolean is false.
		 */
		final Map<K, V> falseMap = Maps.newHashMap();

		/**
		 * Puts the key-value pair in the map corresponding to the switch.
		 *
		 * @param switcher
		 *            The boolean variable defining which map is to be used.
		 * @param key
		 *            The key which is to be put into a map.
		 * @param value
		 *            The value which is to be put into a map.
		 * @return {@code true} if the key was already contained in the chosen map, {@code false} otherwise.
		 */
		public boolean put(boolean switcher, K key, V value) {
			final Map<K, V> selectedMap = getMap(switcher);
			final boolean isContained = selectedMap.containsKey(key);
			selectedMap.put(key, value);
			return isContained;
		}

		/**
		 * Returns the value mapped to key.
		 *
		 * @param switcher
		 *            The boolean variable defining which map is to be used.
		 * @param key
		 *            The key for which the value is looked up.
		 * @return The value {@link V} if it exists, {@code null} otherwise.
		 */
		public V get(boolean switcher, K key) {
			final Map<K, V> selectedMap = getMap(switcher);
			return selectedMap.get(key);
		}

		/**
		 * Selects the map based on the given boolean.
		 *
		 * @param switcher
		 *            Defined which map is to be used.
		 * @return {@link #trueMap} if {@code switcher} is true, {@link #falseMap} otherwise.
		 */
		private Map<K, V> getMap(boolean switcher) {
			if (switcher) {
				return falseMap;
			} else {
				return trueMap;
			}
		}
	}
}
