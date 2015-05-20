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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.junit.Test;

@SuppressWarnings({"resource" })
public class ResourceUtil_BinaryIdentical2_ReadLimitTest {
	byte[] data_A = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46, 74 };

	byte[] data_B = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46, 0 };

	byte[] data_C = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46 };

	@Test
	public void testSameData_sameLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_A);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 8);

		assertTrue(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testDifferentData_sameLength_sameLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_B);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 8);

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testDifferentData_differentLength_sameLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_C);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 8);

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testSameData_differentLimit_rightSmaller() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_A);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 7);

		assertTrue(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testSameData_differentLimit_leftSmaller() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_A);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 7);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 8);

		assertTrue(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testDifferentData_sameLength_differentLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_B);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 7);

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testDifferentData_differentLength_differentLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(data_A);
		InputStream rawInput2 = new ByteArrayInputStream(data_C);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 7);

		assertFalse(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testZeroData_sameLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(new byte[0]);
		InputStream rawInput2 = new ByteArrayInputStream(new byte[0]);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 8);

		assertTrue(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
	}

	@Test
	public void testZeroData_differentLimit() throws IOException {
		InputStream rawInput1 = new ByteArrayInputStream(new byte[0]);
		InputStream rawInput2 = new ByteArrayInputStream(new byte[0]);

		InputStream input1 = new LimitedReadingInputStream(rawInput1, 8);
		InputStream input2 = new LimitedReadingInputStream(rawInput2, 7);

		assertTrue(ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2)));
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
