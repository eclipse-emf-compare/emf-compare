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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputStreamProvider implements IStreamProvider {

	private byte[] array;

	public ByteArrayInputStreamProvider(byte[] array) {
		super();
		this.array = array;
	}

	public InputStream createInputStream() throws IOException {
		return new ByteArrayInputStream(array);
	}
}
