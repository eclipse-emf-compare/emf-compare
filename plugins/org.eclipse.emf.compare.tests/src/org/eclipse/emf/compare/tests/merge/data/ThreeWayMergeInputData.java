/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class ThreeWayMergeInputData extends AbstractInputData {
	public Resource getMoveToDifferentContainmentFeatureOrigin() throws IOException {
		return loadFromClassLoader("threeway/movedifferentcontainmentfeature/origin.nodes");
	}

	public Resource getMoveToDifferentContainmentFeatureMove() throws IOException {
		return loadFromClassLoader("threeway/movedifferentcontainmentfeature/side-of-move.nodes");
	}

	public Resource getMoveToDifferentContainmentFeatureUnchanged() throws IOException {
		return loadFromClassLoader("threeway/movedifferentcontainmentfeature/unchanged.nodes");
	}
}
