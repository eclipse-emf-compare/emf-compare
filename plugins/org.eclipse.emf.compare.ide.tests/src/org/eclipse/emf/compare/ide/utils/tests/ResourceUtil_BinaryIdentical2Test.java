/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"nls", "resource" })
public class ResourceUtil_BinaryIdentical2Test {

	private static final String PATH1 = "src/org/eclipse/emf/compare/ide/utils/tests/data/binaryequalitytestinputdata1";

	private static final String PATH2 = "src/org/eclipse/emf/compare/ide/utils/tests/data/binaryequalitytestinputdata2";

	File file1, file2;

	IStorage storage1_stream, storage1_stream2, storage2_stream;

	@Before
	public void setUp() throws IOException {
		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.tests");
		URL file1Entry = bundle.getEntry(PATH1);
		URL file1URL = FileLocator.resolve(file1Entry);
		String file1Path = file1URL.getPath();
		URL file2Entry = bundle.getEntry(PATH2);
		URL file2URL = FileLocator.resolve(file2Entry);
		String file2Path = file2URL.getPath();

		file1 = new File(file1Path);
		file2 = new File(file2Path);

		storage1_stream = mockStorage(new FileInputStream(file1));
		storage1_stream2 = mockStorage(new FileInputStream(file1));
		storage2_stream = mockStorage(new FileInputStream(file2));
	}

	@Test
	public void testBinaryIdentical_2_sameData() throws IOException {
		assertTrue(ResourceUtil.binaryIdentical(storage1_stream, storage1_stream2));
	}

	@Test
	public void testBinaryIdentical_2_differentData() {
		assertFalse(ResourceUtil.binaryIdentical(storage1_stream, storage2_stream));
	}

	@Test
	public void testBinaryIdentical_2_sameData_differentBuffers() throws IOException {
		IStorage storage1 = mockStorage(buffer(file1, 16384));
		IStorage storage2 = mockStorage(buffer(file1, 8192));

		assertTrue(ResourceUtil.binaryIdentical(storage1, storage2));
	}

	@Test
	public void testBinaryIdentical_2_sameData_sameBuffers() throws IOException {
		IStorage storage1 = mockStorage(buffer(file1, 16384));
		IStorage storage2 = mockStorage(buffer(file1, 16384));

		assertTrue(ResourceUtil.binaryIdentical(storage1, storage2));
	}

	@Test
	public void testBinaryIdentical_2_differentData_sameBuffers() throws IOException {
		IStorage storage1 = mockStorage(buffer(file1, 16384));
		IStorage storage2 = mockStorage(buffer(file2, 16384));

		assertFalse(ResourceUtil.binaryIdentical(storage1, storage2));
	}

	@Test
	public void testBinaryIdentical_2_mixedTypes() throws IOException {
		IStorage storage1Buffered = mockStorage(buffer(file1, 1024));
		IStorage storage2Buffered = mockStorage(buffer(file2, 1024));

		assertTrue(ResourceUtil.binaryIdentical(storage1_stream2, storage1Buffered));
		assertFalse(ResourceUtil.binaryIdentical(storage1_stream2, storage2Buffered));
	}

	@Test
	public void testFalseIfCoreException() throws Exception {
		InputStream input1 = new ByteArrayInputStream(new byte[] {12, 64, });
		IStorage storage2 = mock(IStorage.class);
		when(storage2.getContents()).thenThrow(new CoreException(Status.CANCEL_STATUS));

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), storage2));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testFalseIfIOException() throws IOException {
		InputStream input1 = new ByteArrayInputStream(new byte[] {12, 64, });
		InputStream input2 = mock(InputStream.class);
		when(input2.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	private InputStream buffer(File file, int bufferSize) throws IOException {
		return new BufferedInputStream(new ByteArrayInputStream(Files.toByteArray(file)), bufferSize);
	}

	@SuppressWarnings("boxing")
	private static IStorage mockStorage(InputStream input) {
		try {
			IStorage mockStorage = mock(IStorage.class);
			when(mockStorage.getContents()).thenReturn(input);
			when(mockStorage.isReadOnly()).thenReturn(true);
			return mockStorage;
		} catch (CoreException cEx) {
			// this is merely a checked exception of mockStorage.getContents() and will never happen since we
			// use mockito
			return null;
		}
	}
}
