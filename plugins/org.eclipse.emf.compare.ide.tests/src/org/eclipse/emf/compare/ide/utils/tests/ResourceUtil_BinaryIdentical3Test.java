/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *     Tobias Ortmayr - bug 507157
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.tests.helper.BufferedInputStreamProvider;
import org.eclipse.emf.compare.ide.utils.tests.helper.ByteArrayInputStreamProvider;
import org.eclipse.emf.compare.ide.utils.tests.helper.FileInputStreamProvider;
import org.eclipse.emf.compare.ide.utils.tests.helper.IStreamProvider;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"nls", "resource" })
public class ResourceUtil_BinaryIdentical3Test extends AbstractStorageTest {

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

		storage1_stream = mockStorage(new FileInputStreamProvider(file1));
		storage1_stream2 = mockStorage(new FileInputStreamProvider(file1));
		storage2_stream = mockStorage(new FileInputStreamProvider(file2));
	}

	@Test
	public void testBinaryIdentical_2_sameData() throws IOException {
		final IStorage storage1_stream3 = mockStorage(new FileInputStreamProvider(file1));
		assertTrue(ResourceUtil.binaryIdentical(storage1_stream, storage1_stream2, storage1_stream3));
	}

	@Test
	public void testBinaryIdentical_2_differentData() {
		assertFalse(ResourceUtil.binaryIdentical(storage1_stream, storage2_stream, storage1_stream));
	}

	@Test
	public void testBinaryIdentical_2_differentData_reordered() {
		assertFalse(ResourceUtil.binaryIdentical(storage1_stream, storage1_stream2, storage2_stream));
	}

	@Test
	public void testBinaryIdentical_2_sameData_differentBuffers() throws IOException {
		IStorage storage1 = mockStorage(new BufferedInputStreamProvider(file1, 16384));
		IStorage storage2 = mockStorage(new BufferedInputStreamProvider(file1, 8192));
		IStorage storage3 = mockStorage(new BufferedInputStreamProvider(file1, 1021));

		assertTrue(ResourceUtil.binaryIdentical(storage1, storage2, storage3));
	}

	@Test
	public void testBinaryIdentical_2_sameData_sameBuffers() throws IOException {
		IStorage storage1 = mockStorage(new BufferedInputStreamProvider(file1, 16384));
		IStorage storage2 = mockStorage(new BufferedInputStreamProvider(file1, 16384));
		IStorage storage3 = mockStorage(new BufferedInputStreamProvider(file1, 16384));

		assertTrue(ResourceUtil.binaryIdentical(storage1, storage2, storage3));
	}

	@Test
	public void testBinaryIdentical_2_differentData_sameBuffers() throws IOException {
		IStorage storage1 = mockStorage(new BufferedInputStreamProvider(file1, 16384));
		IStorage storage2 = mockStorage(new BufferedInputStreamProvider(file2, 16384));
		IStorage storage3 = mockStorage(new BufferedInputStreamProvider(file2, 16384));

		assertFalse(ResourceUtil.binaryIdentical(storage1, storage2, storage3));
	}

	@Test
	public void testBinaryIdentical_2_mixedTypes() throws IOException {
		IStorage storage1Buffered = mockStorage(new BufferedInputStreamProvider(file1, 1024));
		IStorage storage1Buffered2 = mockStorage(new BufferedInputStreamProvider(file1, 1024));
		IStorage storage2Buffered = mockStorage(new BufferedInputStreamProvider(file2, 1024));

		assertTrue(ResourceUtil.binaryIdentical(storage1Buffered2, storage1_stream, storage1Buffered));
		assertFalse(ResourceUtil.binaryIdentical(storage1_stream, storage2Buffered, storage1Buffered));
	}

	@Test
	public void testFalseIfCoreException() throws Exception {
		IStreamProvider input1 = new ByteArrayInputStreamProvider(new byte[] {12, 64, });
		IStreamProvider input3 = new ByteArrayInputStreamProvider(new byte[] {12, 64, });
		IStorage storage2 = mock(IStorage.class);
		when(storage2.getContents()).thenThrow(new CoreException(Status.CANCEL_STATUS));

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), storage2, mockStorage(input3)));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testFalseIfIOException() throws IOException {
		IStreamProvider input1 = new ByteArrayInputStreamProvider(new byte[] {12, 64, });
		IStreamProvider input3 = new ByteArrayInputStreamProvider(new byte[] {12, 64, });
		InputStream input2 = mock(InputStream.class);
		when(input2.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());
		IStorage mockStorage = mock(IStorage.class);
		try {
			when(mockStorage.getContents()).thenReturn(input2);
			when(mockStorage.isReadOnly()).thenReturn(true);
		} catch (CoreException e) {
			// this is merely a checked exception of mockStorage.getContents() and will never happen since we
			// use mockito
			e.printStackTrace();
		}
		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage, mockStorage(input3)));
	}
}
