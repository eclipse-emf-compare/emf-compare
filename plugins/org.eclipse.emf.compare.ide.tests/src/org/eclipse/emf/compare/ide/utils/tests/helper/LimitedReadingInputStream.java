/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests.helper;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.compare.ide.utils.ResourceUtil;

/**
 * This class wraps an {@link InputStream} with the purpose of limiting the maximum size a
 * {@link #read(byte[])} or {@link #read(byte[], int, int)} call will return. This is used to test
 * functionality of {@link ResourceUtil} and simulating a stream with a limited read buffer size.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class LimitedReadingInputStream extends InputStream {
	private final InputStream base;

	private final int limit;

	public LimitedReadingInputStream(InputStream base, int limit) {
		this.base = base;
		this.limit = limit;
	}

	@Override
	public int read() throws IOException {
		return base.read();
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		return read(buffer, 0, buffer.length);
	}

	@Override
	public int read(byte[] buffer, int off, int len) throws IOException {
		return super.read(buffer, off, Math.min(len, limit));
	}
}
