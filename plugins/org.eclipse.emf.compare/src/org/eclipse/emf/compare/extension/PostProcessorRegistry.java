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
package org.eclipse.emf.compare.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This will contain all of the EMF Compare extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorRegistry {

	/** List of all the post processors contributed through "org.eclipse.emf.compare.postProcessor". */
	private final List<PostProcessorDescriptor> postProcessors;

	/**
	 * Creates a new extension registry.
	 */
	public PostProcessorRegistry() {
		postProcessors = new CopyOnWriteArrayList<PostProcessorDescriptor>();
	}

	/**
	 * Adds a post processor to the registry.
	 * 
	 * @param postProcessor
	 *            Post Processor that is to be added to this registry.
	 */
	public void addPostProcessor(PostProcessorDescriptor postProcessor) {
		postProcessors.add(postProcessor);
	}

	/**
	 * Removes all extensions from this registry.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void clearRegistry() {
		postProcessors.clear();
	}

	/**
	 * This will return a copy of the registered post processors list.
	 * 
	 * @return A copy of the registered post processors list.
	 */
	public List<PostProcessorDescriptor> getRegisteredPostProcessors() {
		return postProcessors;
	}

	/**
	 * Removes a post processor from this registry.
	 * 
	 * @param postProcessorClassName
	 *            Qualified class name of the post processor that is to be removed from the registry.
	 */
	public void removePostProcessor(String postProcessorClassName) {
		for (PostProcessorDescriptor descriptor : getRegisteredPostProcessors()) {
			if (descriptor.getExtensionClassName().equals(postProcessorClassName)) {
				postProcessors.remove(descriptor);
			}
		}
	}

	/**
	 * Retrieve the post processors from a given <code>scope</code>. The scope provides the set of scanned
	 * namespaces and resource uris. If they match with the regex of a "org.eclipse.emf.compare.postProcessor"
	 * extension point, then the associated post processors are returned.
	 * 
	 * @param scope
	 *            The given scope.
	 * @return The associated post processors if any.
	 */
	public List<IPostProcessor> getPostProcessors(IComparisonScope scope) {
		final List<IPostProcessor> processors = new ArrayList<IPostProcessor>();
		final Iterator<PostProcessorDescriptor> postProcessorIterator = postProcessors.iterator();
		while (postProcessorIterator.hasNext()) {
			final PostProcessorDescriptor descriptor = postProcessorIterator.next();
			if (descriptor.getNsURI() != null && descriptor.getNsURI().trim().length() != 0) {
				final Iterator<String> nsUris = scope.getNsURIs().iterator();
				while (nsUris.hasNext()) {
					if (nsUris.next().matches(descriptor.getNsURI())) {
						processors.add(descriptor.getPostProcessor());
						break;
					}
				}
			}
			// Should probably use two loops here to prioritize NsURI matching
			if (descriptor.getResourceURI() != null && descriptor.getResourceURI().trim().length() != 0) {
				final Iterator<String> resourceUris = scope.getResourceURIs().iterator();
				while (resourceUris.hasNext()) {
					if (resourceUris.next().matches(descriptor.getResourceURI())) {
						processors.add(descriptor.getPostProcessor());
						break;
					}
				}
			}
		}
		return processors;
	}

}
