package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import org.eclipse.emf.compare.DifferenceState;

/**
 * Tests the behavior of the {@link DifferenceState} enumeration.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class DifferenceStateTest {
	/**
	 * Tests the behavior of the {@link DifferenceState#get(int)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetInt() {
		int highestValue = -1;
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.get(value.getValue()), value);
			if (value.getValue() > highestValue) {
				highestValue = value.getValue();
			}
		}
		assertNull(DifferenceState.get(++highestValue));
	}

	/**
	 * Tests the behavior of the {@link DifferenceState#get(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetString() {
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.get(value.getLiteral()), value);
		}
		assertNull(DifferenceState.get("ThisIsNotAValueOfTheTestedEnum"));
	}

	/**
	 * Tests the behavior of the {@link DifferenceState#getByName(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetByName() {
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.getByName(value.getName()), value);
		}
		assertNull(DifferenceState.getByName("ThisIsNotTheNameOfAValueFromTheTestedEnum"));
	}
}
