/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.emf.compare.DifferenceSource;
import org.junit.Test;

/**
 * Tests the behavior of the {@link DifferenceSource} enumeration.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class DifferenceSourceTest {
	/**
	 * Tests the behavior of the {@link DifferenceSource#get(int)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetInt() {
		int highestValue = -1;
		for (DifferenceSource value : DifferenceSource.VALUES) {
			assertSame(DifferenceSource.get(value.getValue()), value);
			if (value.getValue() > highestValue) {
				highestValue = value.getValue();
			}
		}
		assertNull(DifferenceSource.get(++highestValue));
	}

	/**
	 * Tests the behavior of the {@link DifferenceSource#get(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetString() {
		for (DifferenceSource value : DifferenceSource.VALUES) {
			assertSame(DifferenceSource.get(value.getLiteral()), value);
		}
		assertNull(DifferenceSource.get("ThisIsNotAValueOfTheTestedEnum"));
	}

	/**
	 * Tests the behavior of the {@link DifferenceSource#getByName(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetByName() {
		for (DifferenceSource value : DifferenceSource.VALUES) {
			assertSame(DifferenceSource.getByName(value.getName()), value);
		}
		assertNull(DifferenceSource.getByName("ThisIsNotTheNameOfAValueFromTheTestedEnum"));
	}
}
