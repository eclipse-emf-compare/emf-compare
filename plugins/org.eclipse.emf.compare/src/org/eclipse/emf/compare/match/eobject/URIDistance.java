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
import com.google.common.collect.ImmutableList.Builder;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * This class is able to measure similarity between "URI like" strings, basically strings separated by "/".
 * This is mainly intended to be used with EMF's fragments.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class URIDistance implements Function<EObject, String> {

	/**
	 * A computing cache for the locations.
	 */
	private Cache<EObject, String> locationCache;

	/**
	 * Create a new {@link URIDistance}.
	 */
	public URIDistance() {
		locationCache = CacheBuilder.newBuilder().maximumSize(10000).build(CacheLoader.from(this));
	}

	/**
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param a
	 *            First of the two {@link EObject}s to compare.
	 * @param b
	 *            Second of the two {@link EObject}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(EObject a, EObject b) {
		String aPath = locationCache.getUnchecked(a);
		String bPath = locationCache.getUnchecked(b);
		return proximity(aPath, bPath);
	}

	/**
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param aPath
	 *            First of the two {@link String}s to compare.
	 * @param bPath
	 *            Second of the two {@link String}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(String aPath, String bPath) {
		if (aPath.equals(bPath)) {
			return 0;
		}
		return 10;
	}

	/**
	 * Update the builder with location hints for a feature map.
	 * 
	 * @param builder
	 *            the list builder to update.
	 * @param cur
	 *            the current object.
	 * @param container
	 *            the current object container.
	 * @param feat
	 *            the containing feature of the current object.
	 */
	protected void featureMapLocation(Builder<String> builder, EObject cur, EObject container,
			EStructuralFeature feat) {
		FeatureMap featureMap = (FeatureMap)container.eGet(feat, false);
		for (int i = 0, size = featureMap.size(); i < size; ++i) {
			if (featureMap.getValue(i) == cur) {
				EStructuralFeature entryFeature = featureMap.getEStructuralFeature(i);
				if (entryFeature instanceof EReference && ((EReference)entryFeature).isContainment()) {
					builder.add(feat.getName());
					builder.add(Integer.valueOf(i).toString());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public String apply(EObject input) {
		EObject cur = input;
		String result = ""; //$NON-NLS-1$
		EObject container = input.eContainer();
		if (container != null) {
			EStructuralFeature feat = cur.eContainingFeature();
			if (feat instanceof EAttribute) {
				result = featureMapLocation(cur, container, feat);
			} else if (feat != null) {
				if (feat.isMany()) {
					EList<?> eList = (EList<?>)container.eGet(feat, false);
					int index = eList.indexOf(cur);
					result = feat.getName() + Integer.valueOf(index).toString();
				} else {
					result = feat.getName() + "0"; //$NON-NLS-1$
				}
			}
		} else {
			result = "0"; //$NON-NLS-1$
		}

		if (input.eContainer() != null) {
			return result + locationCache.getUnchecked(input.eContainer());
		}
		return result;
	}

	/**
	 * Update the builder with location hints for a feature map.
	 * 
	 * @param cur
	 *            the current object.
	 * @param container
	 *            the current object container.
	 * @param feat
	 *            the containing feature of the current object.
	 * @return a path segment : featureName + position
	 */
	protected String featureMapLocation(EObject cur, EObject container, EStructuralFeature feat) {
		FeatureMap featureMap = (FeatureMap)container.eGet(feat, false);
		for (int i = 0, size = featureMap.size(); i < size; ++i) {
			if (featureMap.getValue(i) == cur) {
				EStructuralFeature entryFeature = featureMap.getEStructuralFeature(i);
				if (entryFeature instanceof EReference && ((EReference)entryFeature).isContainment()) {
					return feat.getName() + Integer.valueOf(i).toString();
				}
			}
		}
		throw new RuntimeException();
	}

}
