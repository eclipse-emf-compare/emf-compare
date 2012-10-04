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
package org.eclipse.emf.compare.utils;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.lang.reflect.Array;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
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
	private final Cache<EObject, URI> uriCache;

	/**
	 * Creates a new EqualityHelper.
	 */
	@Deprecated
	public EqualityHelper() {
		// TODO: use weak keys ? be careful of the use of identity == instead of .equals()
		this(createDefaultCache(CacheBuilder.newBuilder()));
	}

	/**
	 * Creates a new EqualityHelper with the given cache.
	 * 
	 * @param uriCache
	 *            the cache to be used for {@link EcoreUtil#getURI(EObject)} calls.
	 */
	public EqualityHelper(Cache<EObject, URI> uriCache) {
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
		final Object converted1 = internalFindActualObject(object1);
		final Object converted2 = internalFindActualObject(object2);
		if (converted1 == converted2) {
			equal = true;
		} else if (converted1 instanceof EEnumLiteral && converted2 instanceof EEnumLiteral) {
			final EEnumLiteral literal1 = (EEnumLiteral)converted1;
			final EEnumLiteral literal2 = (EEnumLiteral)converted2;
			final String value1 = literal1.getLiteral() + literal1.getValue();
			final String value2 = literal2.getLiteral() + literal2.getValue();
			equal = value1.equals(value2);
		} else if (converted1 instanceof EObject && converted2 instanceof EObject) {
			// [248442] This will handle FeatureMapEntries detection
			equal = matchingEObjects((EObject)converted1, (EObject)converted2);
		} else if (converted1 != null && converted1.getClass().isArray() && converted2 != null
				&& converted2.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			equal = matchingArrays2(converted1, converted2);
		} else if (isNullOrEmptyString(converted1) && isNullOrEmptyString(converted2)) {
			// Special case, consider that the empty String is equal to null (unset attributes)
			equal = true;
		} else {
			equal = converted1 != null && converted1.equals(converted2);
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
	 * Compares two values as arrays, checking that their length and content match each other.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two arrays are to be considered equal, <code>false</code> otherwise.
	 */
	private boolean matchingArrays2(Object object1, Object object2) {
		boolean equal = true;
		final int length1 = Array.getLength(object1);
		if (length1 != Array.getLength(object2)) {
			equal = true;
		} else {
			for (int i = 0; i < length1 && equal; i++) {
				final Object element1 = Array.get(object1, i);
				final Object element2 = Array.get(object2, i);
				equal = matchingValues(getTarget(), element1, element2);
			}
		}
		return equal;
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
	private boolean matchingEObjects(EObject object1, EObject object2) {
		final Match match = getTarget().getMatch(object1);

		final boolean equal;
		// Match could be null if the value is out of the scope
		if (match != null) {
			equal = match.getLeft() == object2 || match.getRight() == object2 || match.getOrigin() == object2;
		} else {
			/*
			 * use a temporary variable as buffer for the "equal" boolean. We know that the following
			 * try/catch block can, and will, only initialize it once ... but the compiler does not.
			 */
			boolean temp = false;
			try {
				final URI uri1 = uriCache.get(object1);
				final URI uri2 = uriCache.get(object2);
				if (uri1.hasFragment() && uri2.hasFragment()) {
					temp = uri1.fragment().equals(uri2.fragment());
				} else {
					temp = uri1.equals(uri2);
				}
			} catch (ExecutionException e) {
				// Could not compute the URIs of these objects, assume not equal
			}
			equal = temp;
		}

		return equal;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.IEqualityHelper#matchingAttributeValues(java.lang.Object,
	 *      java.lang.Object)
	 */
	public boolean matchingAttributeValues(Object object1, Object object2) {
		final boolean equal;
		final Object converted1 = internalFindActualObject(object1);
		final Object converted2 = internalFindActualObject(object2);
		if (converted1 == converted2) {
			equal = true;
		} else if (converted1 instanceof EEnumLiteral && converted2 instanceof EEnumLiteral) {
			final EEnumLiteral literal1 = (EEnumLiteral)converted1;
			final EEnumLiteral literal2 = (EEnumLiteral)converted2;
			final String value1 = literal1.getLiteral() + literal1.getValue();
			final String value2 = literal2.getLiteral() + literal2.getValue();
			equal = value1.equals(value2);
		} else if (converted1 != null && converted1.getClass().isArray() && converted2 != null
				&& converted2.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			equal = matchingArrays(converted1, converted2);
		} else if (isNullOrEmptyString(converted1) && isNullOrEmptyString(converted2)) {
			// Special case, consider that the empty String is equal to null (unset attributes)
			equal = true;
		} else {
			equal = converted1 != null && converted1.equals(converted2);
		}
		return equal;
	}

	/**
	 * Compares two values as arrays, checking that their length and content match each other. Note that this
	 * should only be used when no {@link Comparison} is available.
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
			equal = true;
		} else {
			for (int i = 0; i < length1 && equal; i++) {
				final Object element1 = Array.get(object1, i);
				final Object element2 = Array.get(object2, i);
				equal = matchingAttributeValues(element1, element2);
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
	 * This will convert any Feature map entry to its actual data value. Note that it will have no effect on
	 * an object that is not a {@link FeatureMap.Entry}.
	 * 
	 * @param data
	 *            The data we wish converted.
	 * @return The first value under {@code data} that is not a {@link FeatureMap.Entry}, {@code data} itself
	 *         if it is not a feature map entry.
	 */
	private Object internalFindActualObject(Object data) {
		if (data instanceof FeatureMap.Entry) {
			return internalFindActualObject(((FeatureMap.Entry)data).getValue());
		}
		return data;
	}

	/**
	 * Create a cache as required by EqualityHelper.
	 * 
	 * @param cacheBuilder
	 *            The builder to use to instantiate the cache.
	 * @return the new cache.
	 */
	public static Cache<EObject, URI> createDefaultCache(CacheBuilder<Object, Object> cacheBuilder) {
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
