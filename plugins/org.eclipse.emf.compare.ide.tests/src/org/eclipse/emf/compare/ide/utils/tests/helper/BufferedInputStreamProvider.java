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

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStreamProvider implements IStreamProvider {

	private File file;

	private int bufferSize;

	public BufferedInputStreamProvider(File file, int bufferSize) {
		super();
		this.file = file;
		this.bufferSize = bufferSize;
	}

	public InputStream createInputStream() throws IOException {
		return new BufferedInputStream(new ByteArrayInputStream(Files.toByteArray(file)), bufferSize);
	}
}
