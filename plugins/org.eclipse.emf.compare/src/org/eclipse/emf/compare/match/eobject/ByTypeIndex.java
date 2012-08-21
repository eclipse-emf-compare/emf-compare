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
import com.google.common.collect.MapMaker;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * An implementation of EObjectIndex which segregates given EObjects using their type and then delegate to
 * other indexes.
 * 
 * @author <a href="mailto:lcedric.brun@obeo.fr">Cedric Brun</a>
 */
class ByTypeIndex implements EObjectIndex {
	/**
	 * All the type specific indexes, created on demand.
	 */
	private Map<EClass, EObjectIndex> allIndexes;

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
		this.allIndexes = new MapMaker().makeComputingMap(new Function<EClass, EObjectIndex>() {

			public EObjectIndex apply(EClass input) {
				return new ProximityIndex(ByTypeIndex.this.meter);
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<EObject> getValuesStillThere(Side side) {
		LinkedHashSet<EObject> values = new LinkedHashSet<EObject>();
		for (EObjectIndex typeSpecificIndex : allIndexes.values()) {
			values.addAll(typeSpecificIndex.getValuesStillThere(side));
		}
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Side, EObject> findClosests(EObject obj, Side side, int maxDistance) {
		EObjectIndex typeSpecificIndex = allIndexes.get(obj.eClass());
		return typeSpecificIndex.findClosests(obj, side, maxDistance);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(EObject obj, Side side) {
		EObjectIndex typeSpecificIndex = allIndexes.get(obj.eClass());
		typeSpecificIndex.remove(obj, side);
	}

	/**
	 * {@inheritDoc}
	 */
	public void index(EObject eObjs, Side side) {
		EObjectIndex typeSpecificIndex = allIndexes.get(eObjs.eClass());
		typeSpecificIndex.index(eObjs, side);
	}

}
