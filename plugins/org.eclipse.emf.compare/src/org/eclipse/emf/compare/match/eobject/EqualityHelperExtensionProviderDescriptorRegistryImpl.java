/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EPackage;

/**
 * This will contain all of the EMF Compare equality helper extension provider descriptors.
 * 
 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
 */
public class EqualityHelperExtensionProviderDescriptorRegistryImpl implements EqualityHelperExtensionProvider.Descriptor.Registry {

	/**
	 * Map of all known {@link EqualityHelperExtensionProvider.Descriptor}s.
	 */
	private final Map<String, EqualityHelperExtensionProvider.Descriptor> equalityHelperExtensionProviderDescriptors;

	/**
	 * Cache (NsURI <-> highest ranking equality helper extension provider) associating each NsURI to his
	 * highest ranking equality helper extension provider.
	 */
	private final Map<String, EqualityHelperExtensionProvider> cache;

	/**
	 * Creates a new extension registry.
	 */
	public EqualityHelperExtensionProviderDescriptorRegistryImpl() {
		equalityHelperExtensionProviderDescriptors = Maps.newHashMap();
		cache = Maps.newHashMap();
	}

	/**
	 * Returns a default registry i.e. an empty one
	 * 
	 * @return A default empty registry
	 */
	public static EqualityHelperExtensionProvider.Descriptor.Registry createStandaloneInstance() {
		final EqualityHelperExtensionProviderDescriptorRegistryImpl registry = new EqualityHelperExtensionProviderDescriptorRegistryImpl();
		return registry;
	}

	/**
	 * {@inheritDoc}
	 */
	public EqualityHelperExtensionProvider.Descriptor put(String key,
			EqualityHelperExtensionProvider.Descriptor equalityHelperExtensionProvider) {
		cache.clear();
		return equalityHelperExtensionProviderDescriptors.put(key, equalityHelperExtensionProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		equalityHelperExtensionProviderDescriptors.clear();
		cache.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public ImmutableList<EqualityHelperExtensionProvider.Descriptor> getDescriptors() {
		return ImmutableList.copyOf(equalityHelperExtensionProviderDescriptors.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public EqualityHelperExtensionProvider.Descriptor remove(String key) {
		cache.clear();
		return equalityHelperExtensionProviderDescriptors.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	public EqualityHelperExtensionProvider getHighestRankingEqualityHelperExtensionProvider(
			EPackage ePackage) {
		EqualityHelperExtensionProvider equalityHelperExtensionProvider = cache.get(ePackage.getNsURI());
		if (equalityHelperExtensionProvider == null) {
			EqualityHelperExtensionProvider.Descriptor highestRankingEqualityHelperExtensionProviderDescriptor = getHighestRankingEqualityHelperExtensionProviderDescriptor(
					ePackage.getNsURI());
			if (highestRankingEqualityHelperExtensionProviderDescriptor != null) {
				equalityHelperExtensionProvider = highestRankingEqualityHelperExtensionProviderDescriptor
						.getEqualityHelperExtensionProvider();
				cache.put(ePackage.getNsURI(), equalityHelperExtensionProvider);
			}
		}
		return equalityHelperExtensionProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImmutableList<EqualityHelperExtensionProvider> getEqualityHelperExtensionProviders(
			EPackage ePackage) {
		final ImmutableList.Builder<EqualityHelperExtensionProvider> equalityHelperExtensionProvidersBuilder = ImmutableList
				.builder();
		for (EqualityHelperExtensionProvider.Descriptor descriptor : getEqualityHelperExtensionProviderDescriptors(
				ePackage)) {
			equalityHelperExtensionProvidersBuilder.add(descriptor.getEqualityHelperExtensionProvider());
		}
		return equalityHelperExtensionProvidersBuilder.build();
	}

	/**
	 * Retrieve the equality helper extension providers descriptors from a given ePackage.
	 * 
	 * @param ePackage
	 *            the given ePackage.
	 * @return the equality helper extension providers descriptors from a given ePackage.
	 */
	private ImmutableList<EqualityHelperExtensionProvider.Descriptor> getEqualityHelperExtensionProviderDescriptors(
			EPackage ePackage) {
		return getEqualityHelperExtensionProviderDescriptors(ePackage.getNsURI());
	}

	/**
	 * Retrieve the equality helper extension providers descriptors from a given nsURI.
	 * 
	 * @param nsURI
	 *            the given nsURI.
	 * @return the equality helper extension providers descriptors from a given nsURI.
	 */
	private ImmutableList<EqualityHelperExtensionProvider.Descriptor> getEqualityHelperExtensionProviderDescriptors(
			String nsURI) {
		final ImmutableList.Builder<EqualityHelperExtensionProvider.Descriptor> equalityHelperExtensionProvidersBuilder = ImmutableList
				.builder();
		for (EqualityHelperExtensionProvider.Descriptor descriptor : getDescriptors()) {
			Pattern nsURIPattern = descriptor.getNsURI();
			if (nsURIPattern != null) {
				if (nsURIPattern.matcher(nsURI).matches()) {
					equalityHelperExtensionProvidersBuilder.add(descriptor);
				}
			}
		}
		return equalityHelperExtensionProvidersBuilder.build();
	}

	/**
	 * Returns the highest ranking equality helper extension provider descriptor associated to the given
	 * nsURI.
	 * 
	 * @param nsURI
	 *            the nsURI for which we want to get the highest ranking equality helper extension provider
	 *            descriptor.
	 * @return the highest ranking equality helper extension provider descriptor associated to the given
	 *         nsURI.
	 */
	private EqualityHelperExtensionProvider.Descriptor getHighestRankingEqualityHelperExtensionProviderDescriptor(
			String nsURI) {
		EqualityHelperExtensionProvider.Descriptor ret = null;
		Iterator<EqualityHelperExtensionProvider.Descriptor> descriptors = getEqualityHelperExtensionProviderDescriptors(
				nsURI).iterator();
		if (descriptors.hasNext()) {
			EqualityHelperExtensionProvider.Descriptor highestRanking = descriptors.next();
			while (descriptors.hasNext()) {
				EqualityHelperExtensionProvider.Descriptor desc = descriptors.next();
				if (desc.getRanking() > highestRanking.getRanking()) {
					highestRanking = desc;
				}
			}
			ret = highestRanking;
		}
		return ret;
	}

}
