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

import org.eclipse.emf.compare.DifferenceKind;
import org.junit.Test;

/**
 * Tests the behavior of the {@link DifferenceKind} enumeration.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class DifferenceKindTest {
	/**
	 * Tests the behavior of the {@link DifferenceKind#get(int)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetInt() {
		int highestValue = -1;
		for (DifferenceKind value : DifferenceKind.VALUES) {
			assertSame(DifferenceKind.get(value.getValue()), value);
			if (value.getValue() > highestValue) {
				highestValue = value.getValue();
			}
		}
		assertNull(DifferenceKind.get(++highestValue));
	}

	/**
	 * Tests the behavior of the {@link DifferenceKind#get(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetString() {
		for (DifferenceKind value : DifferenceKind.VALUES) {
			assertSame(DifferenceKind.get(value.getLiteral()), value);
		}
		assertNull(DifferenceKind.get("ThisIsNotAValueOfTheTestedEnum"));
	}

	/**
	 * Tests the behavior of the {@link DifferenceKind#getByName(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetByName() {
		for (DifferenceKind value : DifferenceKind.VALUES) {
			assertSame(DifferenceKind.getByName(value.getName()), value);
		}
		assertNull(DifferenceKind.getByName("ThisIsNotTheNameOfAValueFromTheTestedEnum"));
	}
}
