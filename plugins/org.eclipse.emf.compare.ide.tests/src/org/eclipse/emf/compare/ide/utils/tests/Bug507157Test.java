/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Tobias Ortmayr - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ide.utils.tests.helper.FileInputStreamProvider;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"nls" })
public class Bug507157Test extends AbstractStorageTest {
	private static final String PATH = "src/org/eclipse/emf/compare/ide/utils/tests/data/bug507157";

	private File file;

	private IStorage storage;

	@Before
	public void setUp() throws IOException {
		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.tests");
		URL file1Entry = bundle.getEntry(PATH);
		URL file1URL = FileLocator.resolve(file1Entry);
		String file1Path = file1URL.getPath();

		file = new File(file1Path);
		storage = mockStorage(new FileInputStreamProvider(file));
	}

	/**
	 * Tests that the mocked storage creates new input streams on each call.
	 * 
	 * @throws CoreException
	 *             if the contents of this storage could not be accessed.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Test
	public void testNewInstances() throws CoreException, IOException {
		InputStream firstCall = null;
		InputStream secondCall = null;
		try {
			firstCall = storage.getContents();
			secondCall = storage.getContents();
			assertNotSame(firstCall, secondCall);
		} finally {
			if (firstCall != null) {
				firstCall.close();
			}
			if (secondCall != null) {
				secondCall.close();
			}
		}
	}

	/**
	 * Tests that the mocked storage is readable multiple times without throwing an exception.
	 * 
	 * @throws IOException
	 *             if the stream cannot be read.
	 * @throws CoreException
	 *             if the contents of this storage could not be accessed.
	 */
	@Test
	public void testMultipleRead() throws CoreException, IOException {
		String firstRead = read(storage.getContents());
		String secondRead = read(storage.getContents());
		assertTrue(firstRead.equals(secondRead));
	}

	private String read(InputStream stream) throws IOException {
		int val;
		String returnStr = "";
		InputStreamReader reader = new InputStreamReader(stream);
		while ((val = reader.read()) != -1) {
			returnStr += val;
		}
		stream.close();
		return returnStr;
	}
}
