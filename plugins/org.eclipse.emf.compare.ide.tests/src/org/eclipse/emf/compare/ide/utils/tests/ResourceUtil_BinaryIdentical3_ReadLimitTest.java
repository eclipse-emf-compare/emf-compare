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

import java.io.IOException;

import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.tests.helper.ByteArrayInputStreamProvider;
import org.eclipse.emf.compare.ide.utils.tests.helper.IStreamProvider;
import org.eclipse.emf.compare.ide.utils.tests.helper.LimitedReadingInputStreamProvider;
import org.junit.Test;

public class ResourceUtil_BinaryIdentical3_ReadLimitTest extends AbstractStorageTest {
	byte[] data_A = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46, 74 };

	byte[] data_B = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46, 0 };

	byte[] data_C = {36, 24, 91, 13, 85, 58, 10, 69, 97, 94, 63, 99, 82, 37, 46 };

	@Test
	public void testSameData_sameLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_sameLength_sameLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_B);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_differentLength_sameLimit_order1() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_C);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_B);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_differentLength_sameLimit_order2() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_B);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_C);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_differentLength_sameLimit_order3() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_C);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_C);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order1() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 7);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 6);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order2() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 6);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 7);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order3() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 6);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 7);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order4() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 6);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 7);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order5() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 7);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 6);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testSameData_differentLimit_order6() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 7);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 6);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_sameLength_differentLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_B);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_A);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 7);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 6);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testDifferentData_differentLength_differentLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(data_A);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(data_C);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(data_B);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 7);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 6);

		assertFalse(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testZeroData_sameLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(new byte[0]);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(new byte[0]);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(new byte[0]);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 8);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 8);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}

	@Test
	public void testZeroData_differentLimit() throws IOException {
		IStreamProvider rawInput1 = new ByteArrayInputStreamProvider(new byte[0]);
		IStreamProvider rawInput2 = new ByteArrayInputStreamProvider(new byte[0]);
		IStreamProvider rawInput3 = new ByteArrayInputStreamProvider(new byte[0]);

		IStreamProvider input1 = new LimitedReadingInputStreamProvider(rawInput1, 8);
		IStreamProvider input2 = new LimitedReadingInputStreamProvider(rawInput2, 7);
		IStreamProvider input3 = new LimitedReadingInputStreamProvider(rawInput3, 6);

		assertTrue(
				ResourceUtil.binaryIdentical(mockStorage(input1), mockStorage(input2), mockStorage(input3)));
	}
}
