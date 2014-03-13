/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison.data.identifier;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class IdentifierMatchInputData extends AbstractInputData {
	public Resource getExtlibraryLeft() throws IOException {
		return loadFromClassLoader("extlibraryLeft.ecore");
	}

	public Resource getExtlibraryRight() throws IOException {
		return loadFromClassLoader("extlibraryRight.ecore");
	}

	public Resource getExtlibraryOrigin() throws IOException {
		return loadFromClassLoader("extlibraryOrigin.ecore");
	}
}
