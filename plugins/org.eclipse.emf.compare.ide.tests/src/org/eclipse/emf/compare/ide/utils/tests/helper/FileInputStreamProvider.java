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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileInputStreamProvider implements IStreamProvider {

	private File file;

	public FileInputStreamProvider(File file) {
		this.file = file;
	}

	public InputStream createInputStream() throws IOException {
		return new FileInputStream(file);
	}

}
