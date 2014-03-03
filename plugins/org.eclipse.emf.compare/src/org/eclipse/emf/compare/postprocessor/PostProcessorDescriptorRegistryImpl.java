/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.postprocessor;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This will contain all of the EMF Compare extensions.
 * 
 * @param <K>
 *            the type of the key of this registry
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorDescriptorRegistryImpl<K> implements IPostProcessor.Descriptor.Registry<K> {

	/**
	 * Function ordering descriptors by their ranking.
	 */
	private static Function<IPostProcessor.Descriptor, Integer> ordinal = new Function<IPostProcessor.Descriptor, Integer>() {
		public Integer apply(IPostProcessor.Descriptor from) {
			return Integer.valueOf(from.getOrdinal());
		}
	};

	/** List of all the post processors contributed through "org.eclipse.emf.compare.postProcessor". */
	private final Map<K, IPostProcessor.Descriptor> postProcessorFactories;

	/**
	 * Creates a new extension registry.
	 */
	public PostProcessorDescriptorRegistryImpl() {
		postProcessorFactories = new ConcurrentHashMap<K, IPostProcessor.Descriptor>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.Registry#clearRegistry()
	 */
	public void clear() {
		postProcessorFactories.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry#put(java.lang.Object,
	 *      org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor)
	 */
	public Descriptor put(K key, Descriptor postProcessor) {
		return postProcessorFactories.put(key, postProcessor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry#getDescriptors()
	 */
	public List<Descriptor> getDescriptors() {
		return Ordering.natural().onResultOf(ordinal).immutableSortedCopy(postProcessorFactories.values());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry#remove(java.lang.Object)
	 */
	public Descriptor remove(K key) {
		return postProcessorFactories.remove(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry#getPostProcessors(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public List<IPostProcessor> getPostProcessors(IComparisonScope scope) {
		final ImmutableList.Builder<IPostProcessor> processors = ImmutableList.builder();
		for (IPostProcessor.Descriptor factory : getDescriptors()) {
			Pattern nsURIPattern = factory.getNsURI();
			if (nsURIPattern != null) {
				for (String nsURI : scope.getNsURIs()) {
					if (nsURIPattern.matcher(nsURI).matches()) {
						processors.add(factory.getPostProcessor());
						break;
					}
				}
			}
			// Should probably use two loops here to prioritize NsURI matching
			Pattern resourceURIPattern = factory.getResourceURI();
			if (resourceURIPattern != null) {
				for (String resourceURI : scope.getResourceURIs()) {
					if (resourceURIPattern.matcher(resourceURI).matches()) {
						processors.add(factory.getPostProcessor());
						break;
					}
				}
			}
		}
		return processors.build();
	}
}
