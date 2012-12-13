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
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * An implementation of EObjectIndex which segregates given EObjects using their type and then delegate to
 * other indexes.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ByTypeIndex implements EObjectIndex {
	/**
	 * All the type specific indexes, created on demand.
	 */
	private Cache<String, EObjectIndex> allIndexes;

	/**
	 * The distance function to use to create the delegates indexes.
	 */
	private DistanceFunction meter;

	/**
	 * Create a new instance using the given {@link DistanceFunction} to instantiate delegate indexes on
	 * demand.
	 * 
	 * @param meter
	 *            the function passed when instantiating delegate indexes.
	 */
	public ByTypeIndex(ProximityEObjectMatcher.DistanceFunction meter) {
		this.meter = meter;
		this.allIndexes = CacheBuilder.newBuilder().build(
				CacheLoader.from(new Function<String, EObjectIndex>() {
					public EObjectIndex apply(String input) {
						return new ProximityIndex(ByTypeIndex.this.meter);
					}
				}));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#getValuesStillThere(org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public Collection<EObject> getValuesStillThere(Side side) {
		LinkedHashSet<EObject> values = new LinkedHashSet<EObject>();
		for (EObjectIndex typeSpecificIndex : allIndexes.asMap().values()) {
			values.addAll(typeSpecificIndex.getValuesStillThere(side));
		}
		return values;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#findClosests(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side, int)
	 */
	public Map<Side, EObject> findClosests(EObject obj, Side side) {
		try {
			EObjectIndex typeSpecificIndex = allIndexes.get(eClassKey(obj));
			return typeSpecificIndex.findClosests(obj, side);
		} catch (ExecutionException e) {
			return Collections.emptyMap();
		}
	}

	/**
	 * Compute a key identifying the EClass of the given EObject.
	 * 
	 * @param obj
	 *            any eObject.
	 * @return a key for its EClass.
	 */
	private String eClassKey(EObject obj) {
		EClass clazz = obj.eClass();
		if (clazz.getEPackage() != null) {
			return clazz.getEPackage().getNsURI() + ":" + clazz.getName(); //$NON-NLS-1$
		}
		return clazz.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#remove(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public void remove(EObject obj, Side side) {
		try {
			EObjectIndex typeSpecificIndex = allIndexes.get(eClassKey(obj));
			typeSpecificIndex.remove(obj, side);
		} catch (ExecutionException e) {
			// We could not compute the indices to remove.
			// They'll remain as 'unmatch' later on.
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#index(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public void index(EObject eObjs, Side side) {
		try {
			EObjectIndex typeSpecificIndex = allIndexes.get(eClassKey(eObjs));
			typeSpecificIndex.index(eObjs, side);
		} catch (ExecutionException e) {
			// Could not index this object.
		}
	}

}
