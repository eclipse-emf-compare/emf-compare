/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Registry of all {@link IDiffExtensionFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
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
	 * @param crossReferencer
	 *            The DiffModel cross referencer.
	 * @param match
	 *            The match model.
	 * @return an unmodifiable set of all {@link IDiffExtensionFactory}.
	 */
	public static Set<IDiffExtensionFactory> createExtensionFactories(
			EcoreUtil.CrossReferencer crossReferencer, MatchModel match) {
		final Set<IDiffExtensionFactory> dataset = new HashSet<IDiffExtensionFactory>();
		dataset.add(new DiagramMoveNodeFactory());
		dataset.add(new DiagramEdgeLayoutChangeFactory(crossReferencer));
		dataset.add(new DiagramHideElementFactory());
		dataset.add(new DiagramShowElementFactory());
		dataset.add(new DiagramModelElementChangeLeftTargetFactory(crossReferencer, match));
		dataset.add(new DiagramModelElementChangeRightTargetFactory(crossReferencer, match));
		return Collections.unmodifiableSet(dataset);
	}

}
