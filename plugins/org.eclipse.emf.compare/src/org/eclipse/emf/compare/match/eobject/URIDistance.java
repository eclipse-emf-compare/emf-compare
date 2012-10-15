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
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

import java.util.List;

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
public class URIDistance implements Function<EObject, List<String>> {

	/**
	 * A computing cache for the locations.
	 */
	private Cache<EObject, List<String>> locationCache = CacheBuilder.newBuilder().maximumSize(1000).build(
			CacheLoader.from(this));

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public List<String> apply(EObject input) {
		if (input == null) {
			return null;
		}
		EObject cur = input;
		EObject container = input.eContainer();
		Builder<String> builder = ImmutableList.builder();
		if (container != null) {
			builder.addAll(locationCache.getUnchecked(container));
			EStructuralFeature feat = cur.eContainingFeature();
			if (feat instanceof EAttribute) {
				featureMapLocation(builder, cur, container, feat);
			} else if (feat != null) {
				if (feat.isMany()) {
					EList<?> eList = (EList<?>)container.eGet(feat, false);
					int index = eList.indexOf(cur);
					builder.add(feat.getName());
					builder.add(Integer.valueOf(index).toString());
				} else {
					builder.add(feat.getName());
					builder.add("0"); //$NON-NLS-1$
				}
			}
		} else {
			builder.add("0"); //$NON-NLS-1$
		}

		return builder.build();
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
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param str1
	 *            First of the two {@link String}s to compare.
	 * @param str2
	 *            Second of the two {@link String}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(String str1, String str2) {
		Splitter splitter = Splitter.on('/').trimResults().omitEmptyStrings();
		List<String> fragments1 = Lists.newArrayList(splitter.split(str1));
		List<String> fragments2 = Lists.newArrayList(splitter.split(str2));
		return proximity(fragments1, fragments2);
	}

	/**
	 * Return a metric result URI similarities. It compares 2 list of fragments and return an int representing
	 * the level of similarity. 0 - they are exactly the same to 10 - they are completely different.
	 * "adding a fragment", "removing a fragment".
	 * 
	 * @param fragments1
	 *            First list of fragments to compare.
	 * @param fragments2
	 *            Second list of fragments to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(List<String> fragments1, List<String> fragments2) {
		if (fragments1.size() == 0 && fragments2.size() == 0) {
			return 0;
		}
		int frag2Size = fragments2.size();
		int commonPart = 0;
		for (int i = 0; i < fragments1.size(); i++) {
			String f1 = fragments1.get(i);
			if (i < frag2Size && f1.equals(fragments2.get(i))) {
				commonPart++;
			} else {
				break;
			}
		}

		int totalFrag = Math.max(fragments2.size(), fragments1.size());
		double similarity = commonPart * 10d / totalFrag;
		return 10 - (int)similarity;
	}

	/**
	 * Return a metric result location similarities. A location might be seen as an URI except it does not
	 * depend on the referencing scheme of EObjects related to a given resource (with intrinsic IDs, with
	 * eKeys..). It the location of 2 EObjects and return an int representing the level of similarity. 0 -
	 * they are exactly the same to 10 - they are completely different. "adding a fragment",
	 * "removing a fragment".
	 * 
	 * @param a
	 *            First of the two {@link EObject}s to compare.
	 * @param b
	 *            Second of the two {@link EObject}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(EObject a, EObject b) {
		return proximity(apply(a), apply(b));
	}

}
