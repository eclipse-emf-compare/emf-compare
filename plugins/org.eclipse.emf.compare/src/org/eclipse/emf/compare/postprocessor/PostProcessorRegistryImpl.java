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

import com.google.common.collect.ImmutableList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This will contain all of the EMF Compare extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorRegistryImpl implements IPostProcessor.Registry {

	/** List of all the post processors contributed through "org.eclipse.emf.compare.postProcessor". */
	private final Map<String, IPostProcessor> postProcessors;

	/**
	 * Creates a new extension registry.
	 */
	public PostProcessorRegistryImpl() {
		postProcessors = new ConcurrentHashMap<String, IPostProcessor>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.Registry#addPostProcessor(org.eclipse.emf.compare.extension.PostProcessorDescriptor)
	 */
	public IPostProcessor addPostProcessor(IPostProcessor postProcessor) {
		if (postProcessor instanceof IPostProcessor.Descriptor) {
			return postProcessors.put(((IPostProcessor.Descriptor)postProcessor).getPostProcessor()
					.getClass().getName(), postProcessor);
		} else {
			return postProcessors.put(postProcessor.getClass().getName(), postProcessor);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.Registry#clearRegistry()
	 */
	public void clear() {
		postProcessors.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.Registry#getRegisteredPostProcessors()
	 */
	public ImmutableList<IPostProcessor> getPostProcessors() {
		return ImmutableList.copyOf(postProcessors.values());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 * @see org.eclipse.emf.compare.extension.Registry#removePostProcessor(java.lang.String)
	 */
	public IPostProcessor removePostProcessor(String postProcessorClassName) {
		return postProcessors.remove(postProcessorClassName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.extension.Registry#getPostProcessors(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public ImmutableList<IPostProcessor> getPostProcessors(IComparisonScope scope) {
		final ImmutableList.Builder<IPostProcessor> processors = ImmutableList.builder();
		for (IPostProcessor postProcessor : getPostProcessors()) {
			Pattern nsURIPattern = postProcessor.getNsURI();
			if (nsURIPattern != null) {
				for (String nsURI : scope.getNsURIs()) {
					if (nsURIPattern.matcher(nsURI).matches()) {
						processors.add(postProcessor);
						break;
					}
				}
			}
			// Should probably use two loops here to prioritize NsURI matching
			Pattern resourceURIPattern = postProcessor.getResourceURI();
			if (resourceURIPattern != null) {
				for (String resourceURI : scope.getResourceURIs()) {
					if (resourceURIPattern.matcher(resourceURI).matches()) {
						processors.add(postProcessor);
						break;
					}
				}
			}
		}
		return processors.build();
	}
}
