/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tobias Ortmayr - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests.helper;

import java.io.IOException;
import java.io.InputStream;

/**
 * An interface to create new input streams.
 * 
 * @author Tobias Ortmayr
 */
public interface IStreamProvider {
	/**
	 * Creates a new instance of an input stream.
	 * 
	 * @return newly created input stream.
	 * @throws IOException
	 *             if the input stream cannot be created.
	 */
	InputStream createInputStream() throws IOException;
}
