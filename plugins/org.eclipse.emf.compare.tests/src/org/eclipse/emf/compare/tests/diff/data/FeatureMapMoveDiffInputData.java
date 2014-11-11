/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class FeatureMapMoveDiffInputData extends AbstractInputData {
	public Resource getFeatureMapMoveLeft() throws IOException {
		return loadFromClassLoader("featuremapmove/left.nodes");
	}

	public Resource getFeatureMapMoveRight() throws IOException {
		return loadFromClassLoader("featuremapmove/right.nodes");
	}
}
