/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.diff.internal.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;

/**
 * Registry of all {@link IDiffExtensionFactory}.
 */
public final class DiffExtensionFactoryRegistry {

	/**
	 * Constructor.
	 */
	private DiffExtensionFactoryRegistry() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates and returns all {@link IDiffExtensionFactory} available in this plugin. The returned Set in
	 * unmodifiable.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Map<Class<? extends Diff>, IDiffExtensionFactory> createExtensionFactories() {
		final Map<Class<? extends Diff>, IDiffExtensionFactory> dataset = new HashMap<Class<? extends Diff>, IDiffExtensionFactory>();

		List<IDiffExtensionFactory> factories = new ArrayList<IDiffExtensionFactory>();

		for (IDiffExtensionFactory iDiffExtensionFactory : factories) {
			dataset.put(iDiffExtensionFactory.getExtensionKind(), iDiffExtensionFactory);
		}

		return Collections.unmodifiableMap(dataset);
	}

}