/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramConfiguration;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.EdgeChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.HideFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.ShowFactory;

/**
 * Registry of all {@link IDiagramExtensionFactory}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class DiagramExtensionFactoryRegistry {

	/**
	 * Constructor.
	 */
	private DiagramExtensionFactoryRegistry() {
	}

	/**
	 * Creates and returns all {@link IDiagramExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param configuration
	 *            The diagram comparison configuration.
	 * @return an unmodifiable set of all {@link IDiagramExtensionFactory}.
	 */
	public static Map<Class<? extends Diff>, IDiagramExtensionFactory> createExtensionFactories(
			CompareDiagramConfiguration configuration) {
		final Map<Class<? extends Diff>, IDiagramExtensionFactory> dataset = new HashMap<Class<? extends Diff>, IDiagramExtensionFactory>();

		List<IDiagramExtensionFactory> factories = new ArrayList<IDiagramExtensionFactory>();
		factories.add(new HideFactory());
		factories.add(new ShowFactory());
		factories.add(new NodeChangeFactory(configuration));
		factories.add(new EdgeChangeFactory());

		for (IDiagramExtensionFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}
