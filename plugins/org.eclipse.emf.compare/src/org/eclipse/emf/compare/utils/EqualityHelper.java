/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 473730 (ignore URI type descriptions)
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * EMF Compare needs its own rules for "equality", which are based on similarity instead of strict equality.
 * These will be used throughout the process.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EqualityHelper extends AdapterImpl implements IEqualityHelper {
	/** A cache keeping track of the URIs for EObjects. */
	private final LoadingCache<EObject, URI> uriCache;

	/**
	 * {@link #matchingEObjects(EObject, EObject)} is called _a lot_ of successive times with the same first
	 * parameter... we use this as a poor man's cache.
	 */
	private WeakReference<EObject> lastMatchedEObject;

	/** See #lastMatchedEObject. */
	private WeakReference<Match> lastMatch;

	/**
	 * Creates a new EqualityHelper.
	 * 
	 * @deprecated use the EqualityHelper(Cache) constructor instead.
	 */
	@Deprecated
	public EqualityHelper() {
		this(createDefaultCache(CacheBuilder.newBuilder()
				.maximumSize(DefaultMatchEngine.DEFAULT_EOBJECT_URI_CACHE_MAX_SIZE)));
	}

	/**
	 * Creates a new EqualityHelper with the given cache.
	 * 
	 * @param uriCache
	 *            the cache to be used for {@link EcoreUtil#getURI(EObject)} calls.
	 */
	public EqualityHelper(LoadingCache<EObject, URI> uriCache) {
		this.uriCache = uriCache;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Comparison getTarget() {
		return (Comparison)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type == IEqualityHelper.class;
	}

	/**
	 * Check that the two given values are "equal", considering the specifics of EMF.
	 * 
	 * @param comparison
	 *            Provides us with the Match necessary for EObject comparison.
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if both objects are to be considered equal, <code>false</code> otherwise.
	 * @see #matchingValues(Object, Object)
	 */
	@Deprecated
	public boolean matchingValues(Comparison comparison, Object object1, Object object2) {
		return matchingValues(object1, object2);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.IEqualityHelper#matchingValues(java.lang.Object, java.lang.Object)
	 */
	public boolean matchingValues(Object object1, Object object2) {
		final boolean equal;
		if (object1 == object2) {
			equal = true;
		} else if (object1 instanceof EObject && object2 instanceof EObject) {
			// [248442] This will handle FeatureMapEntries detection
			equal = matchingEObjects((EObject)object1, (EObject)object2);
		} else if (isNullOrEmptyString(object1) && isNullOrEmptyString(object2)) {
			// Special case, consider that the empty String is equal to null (unset attributes)
			equal = true;
		} else if (object1 instanceof String || object1 instanceof Integer || object1 instanceof Boolean) {
			// primitives and String are much more common than arrays... and isArray() is expensive.
			equal = object1.equals(object2);
		} else if (object1 != null && object1.getClass().isArray() && object2 != null
				&& object2.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			equal = matchingArrays(object1, object2);
		} else if (object1 instanceof FeatureMap.Entry && object2 instanceof FeatureMap.Entry) {
			EStructuralFeature key1 = ((FeatureMap.Entry)object1).getEStructuralFeature();
			EStructuralFeature key2 = ((FeatureMap.Entry)object2).getEStructuralFeature();
			Object value1 = ((FeatureMap.Entry)object1).getValue();
			Object value2 = ((FeatureMap.Entry)object2).getValue();

			equal = key1.equals(key2) && matchingValues(value1, value2);
		} else {
			equal = object1 != null && object1.equals(object2);
		}
		return equal;
	}

	/**
	 * Returns {@code true} if the given {@code object} is {@code null} or the empty String.
	 * 
	 * @param object
	 *            The object we need to test.
	 * @return {@code true} if the given {@code object} is {@code null} or the empty String.
	 */
	private boolean isNullOrEmptyString(Object object) {
		return object == null || object instanceof String && ((String)object).length() == 0;
	}

	/**
	 * Compares two values as EObjects, using their Match if it can be found, comparing through their URIs
	 * otherwise.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two EObjects are to be considered equal, <code>false</code>
	 *         otherwise.
	 */
	protected boolean matchingEObjects(EObject object1, EObject object2) {
		final Match match = getMatch(object1);

		final boolean equal;
		// Match could be null if the value is out of the scope
		if (match != null) {
			equal = match.getLeft() == object2 || match.getRight() == object2 || match.getOrigin() == object2;
			// use getTarget().getMatch() to avoid invalidating the cache here
		} else if (getTarget().getMatch(object2) != null) {
			equal = false;
		} else if (object1.eClass() != object2.eClass()) {
			equal = false;
		} else {
			equal = matchingURIs(object1, object2);
		}

		return equal;
	}

	/**
	 * Retrieves the match of the given EObject. This will cache the latest access so as to avoid a hashmap
	 * lookup in the comparison's inverse cross referencer. This is most useful when computing the LCS of two
	 * sequences, where we call {@link #matchingEObjects(EObject, EObject)} over and over with the same first
	 * object.
	 * 
	 * @param o
	 *            The object for which we need the associated Match.
	 * @return Match of this EObject if any, <code>null</code> otherwise.
	 */
	protected Match getMatch(EObject o) {
		final Match match;
		if (lastMatchedEObject != null && lastMatchedEObject.get() == o) {
			Match temp = lastMatch.get();
			if (temp != null) {
				match = temp;
			} else {
				match = getTarget().getMatch(o);
				lastMatch = new WeakReference<Match>(match);
			}
		} else {
			match = getTarget().getMatch(o);
			lastMatchedEObject = new WeakReference<EObject>(o);
			lastMatch = new WeakReference<Match>(match);
		}
		return match;
	}

	/**
	 * Compare the URIs (of similar concept) of EObjects.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two EObjects have the same URIs, <code>false</code> otherwise.
	 */
	protected boolean matchingURIs(EObject object1, EObject object2) {
		// An object that is uncontained and is not a proxy has no URI. bypass them.
		if (!object1.eIsProxy() && isUncontained(object1) || !object2.eIsProxy() && isUncontained(object2)) {
			return false;
		}

		final boolean equal;
		final URI uri1 = uriCache.getUnchecked(object1);
		final URI uri2 = uriCache.getUnchecked(object2);
		if (uri1.hasFragment() && uri2.hasFragment()) {
			final String uri1Fragment = removeURIAttachment(uri1.fragment());
			final String uri2Fragment = removeURIAttachment(uri2.fragment());
			equal = uri1Fragment.equals(uri2Fragment);
		} else {
			equal = uri1.equals(uri2);
		}
		return equal;
	}

	/**
	 * To some {@link URI}s a human friendly description is attached describing the type the {@link URI} is
	 * pointing to. The description is marked by a "?" at the beginning and end. This method returns the
	 * fragment without the attached type description.
	 * 
	 * @param fragment
	 *            The {@link URI} fragment to check for a type description attachment
	 * @return The fragment of the {@link URI} stripped from type description if it has one, otherwise the
	 *         original fragment is returned.
	 */
	private String removeURIAttachment(String fragment) {
		// check if fragment contains at least two question marks
		final int questionMark1 = fragment.indexOf('?');
		final boolean hasTwoQuestionMarks = questionMark1 != -1
				&& fragment.indexOf('?', questionMark1 + 1) != -1;
		if (hasTwoQuestionMarks) {
			return fragment.substring(0, questionMark1);
		}
		return fragment;
	}

	/**
	 * Checks whether the given object is contained anywhere.
	 * 
	 * @param object
	 *            The object whose container we are to check.
	 * @return <code>true</code> if the object has no reachable container, <code>false</code> otherwise.
	 */
	private boolean isUncontained(EObject object) {
		return object.eContainer() == null && object.eResource() == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.IEqualityHelper#matchingAttributeValues(java.lang.Object,
	 *      java.lang.Object)
	 */

	public boolean matchingAttributeValues(Object object1, Object object2) {
		// The default equality helper handles attributes and references the same.
		return matchingValues(object1, object2);
	}

	/**
	 * Compares two values as arrays, checking that their length and content match each other.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two arrays are to be considered equal, <code>false</code> otherwise.
	 */
	private boolean matchingArrays(Object object1, Object object2) {
		boolean equal = true;
		final int length1 = Array.getLength(object1);
		if (length1 != Array.getLength(object2)) {
			equal = false;
		} else {
			for (int i = 0; i < length1 && equal; i++) {
				final Object element1 = Array.get(object1, i);
				final Object element2 = Array.get(object2, i);
				equal = matchingValues(element1, element2);
			}
		}
		return equal;
	}

	/**
	 * The EqualityHelper often needs to get an EObject uri. As such it has an internal cache that clients
	 * might leverage through this method.
	 * 
	 * @param object
	 *            any EObject.
	 * @return the URI of the given EObject, or {@code null} if we somehow could not compute it.
	 */
	@Deprecated
	public URI getURI(EObject object) {
		try {
			return uriCache.get(object);
		} catch (ExecutionException e) {
			return null;
		}
	}

	/**
	 * Returns the cache used by this object.
	 * 
	 * @return the cache used by this object.
	 */
	@Deprecated
	public Cache<EObject, URI> getCache() {
		return uriCache;
	}

	/**
	 * Create a cache as required by EqualityHelper.
	 * 
	 * @param cacheBuilder
	 *            The builder to use to instantiate the cache.
	 * @return the new cache.
	 */
	public static LoadingCache<EObject, URI> createDefaultCache(CacheBuilder<Object, Object> cacheBuilder) {
		return cacheBuilder.build(CacheLoader.from(new URICacheFunction()));
	}

	/**
	 * This is the function that will be used by our {@link #uriCache} to compute its values.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class URICacheFunction implements Function<EObject, URI> {
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		public URI apply(EObject input) {
			if (input == null) {
				return null;
			}
			return EcoreUtil.getURI(input);
		}
	}
}
