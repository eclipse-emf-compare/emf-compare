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
import java.util.List;

/**
 * This will contain all of the EMF Compare extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class EMFCompareExtensionRegistry {

	/** List of all the post processors contributed through "org.eclipse.emf.compare.postProcessor". */
	private static final List<PostProcessorDescriptor> POST_PROCESSORS = new ArrayList<PostProcessorDescriptor>();

	/** This class should not be instantiated. */
	protected EMFCompareExtensionRegistry() {
	}

	/**
	 * Adds a post processor to the registry.
	 * 
	 * @param postProcessor
	 *            Post Processor that is to be added to this registry.
	 */
	public static void addPostProcessor(PostProcessorDescriptor postProcessor) {
		POST_PROCESSORS.add(postProcessor);
	}

	/**
	 * Removes all extensions from this registry.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static void clearRegistry() {
		POST_PROCESSORS.clear();
	}

	/**
	 * This will return a copy of the registered post processors list.
	 * 
	 * @return A copy of the registered post processors list.
	 */
	public static List<PostProcessorDescriptor> getRegisteredPostProcessors() {
		return new ArrayList<PostProcessorDescriptor>(POST_PROCESSORS);
	}

	/**
	 * Removes a post processor from this registry.
	 * 
	 * @param postProcessorClassName
	 *            Qualified class name of the post processor that is to be removed from the registry.
	 */
	public static void removePostProcessor(String postProcessorClassName) {
		for (PostProcessorDescriptor descriptor : getRegisteredPostProcessors()) {
			if (descriptor.getExtensionClassName().equals(postProcessorClassName)) {
				POST_PROCESSORS.remove(descriptor);
			}
		}
	}

}
