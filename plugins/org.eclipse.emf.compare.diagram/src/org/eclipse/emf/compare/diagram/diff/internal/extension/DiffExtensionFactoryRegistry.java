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
package org.eclipse.emf.compare.diagram.diff.internal.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.diff.DiagramComparisonConfiguration;
import org.eclipse.emf.compare.diagram.diff.internal.extension.factories.EdgeChangeFactory;
import org.eclipse.emf.compare.diagram.diff.internal.extension.factories.HideFactory;
import org.eclipse.emf.compare.diagram.diff.internal.extension.factories.NodeChangeFactory;
import org.eclipse.emf.compare.diagram.diff.internal.extension.factories.ShowFactory;

/**
 * Registry of all {@link IDiffExtensionFactory}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class DiffExtensionFactoryRegistry {

	/**
	 * Constructor.
	 */
	private DiffExtensionFactoryRegistry() {
	}

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param configuration
	 *            The diagram comparison configuration.
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Map<Class<? extends Diff>, IDiffExtensionFactory> createExtensionFactories(
			DiagramComparisonConfiguration configuration) {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> dataset = new HashMap<Class<? extends Diff>, IDiffExtensionFactory>();

		List<IDiffExtensionFactory> factories = new ArrayList<IDiffExtensionFactory>();
		factories.add(new HideFactory());
		factories.add(new ShowFactory());
		factories.add(new NodeChangeFactory(configuration));
		factories.add(new EdgeChangeFactory());

		for (IDiffExtensionFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}
