/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - add SizeChange
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramConfiguration;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.CoordinatesChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.DiagramChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.EdgeChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.HideFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.ShowFactory;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.SizeChangeFactory;
import org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory;

/**
 * Registry of all {@link IChangeFactory}.
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
	 * Creates and returns all {@link IChangeFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param configuration
	 *            The diagram comparison configuration.
	 * @return an unmodifiable set of all {@link IChangeFactory}.
	 */
	public static Map<Class<? extends Diff>, IChangeFactory> createExtensionFactories(
			CompareDiagramConfiguration configuration) {
		final Map<Class<? extends Diff>, IChangeFactory> dataset = new HashMap<Class<? extends Diff>, IChangeFactory>();

		List<IChangeFactory> factories = new ArrayList<IChangeFactory>();
		factories.add(new HideFactory());
		factories.add(new ShowFactory());
		factories.add(new NodeChangeFactory());
		factories.add(new CoordinatesChangeFactory(configuration));
		factories.add(new SizeChangeFactory());
		factories.add(new EdgeChangeFactory());
		factories.add(new DiagramChangeFactory());

		for (IChangeFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}
