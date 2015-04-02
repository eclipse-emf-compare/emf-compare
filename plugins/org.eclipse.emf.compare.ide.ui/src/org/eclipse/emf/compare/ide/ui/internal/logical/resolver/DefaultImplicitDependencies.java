/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistry;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * Default implementation of {@link IImplicitDependencies}, which uses the extensions collected by the
 * registry of the <i>modelDependencyProvider</i> extension point.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DefaultImplicitDependencies implements IImplicitDependencies {
	/** The registry of declared extensions. */
	private final ModelDependencyProviderRegistry registry = EMFCompareIDEUIPlugin.getDefault()
			.getModelDependencyProviderRegistry();

	public Set<URI> of(URI uri, URIConverter uriConverter) {
		return Sets.union(Collections.singleton(uri), registry.getDependencies(uri, uriConverter));
	}
}
