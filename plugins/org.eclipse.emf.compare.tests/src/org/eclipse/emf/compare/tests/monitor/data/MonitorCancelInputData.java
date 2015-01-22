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
package org.eclipse.emf.compare.tests.monitor.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class MonitorCancelInputData extends AbstractInputData {
	public Resource getLeft() throws IOException {
		return loadFromClassLoader("extlibraryLeft.ecore");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("extlibraryRight.ecore");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("extlibraryOrigin.ecore");
	}
}
