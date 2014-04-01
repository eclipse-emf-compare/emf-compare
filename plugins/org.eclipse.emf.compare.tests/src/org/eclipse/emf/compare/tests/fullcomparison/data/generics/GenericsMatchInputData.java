/**
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison.data.generics;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the generics matching.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("nls")
public class GenericsMatchInputData extends AbstractInputData {
	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.ecore");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.ecore");
	}
}
