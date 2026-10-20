/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.match.hash.FeatureValue;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

/**
 * Verifies the constructor and factory input-list ownership contracts. Custom hashers can use either API
 * to retain a snapshot even when they later modify the list passed to it.
 * <p>
 * The test replaces an entry in the caller's list after constructing the snapshot, then compares the result
 * with independently constructed content containing the original string. Retaining the caller's list would
 * make that assertion fail.
 * </p>
 * <p>
 * This checks the outer-list copy directly on the API that owns it. The elements are immutable strings;
 * copying or normalizing mutable model values is covered by {@code ContentHasherTest}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class FeatureValueTest {
	/** Original immutable element retained by a snapshot. */
	private static final String ORIGINAL = "original";

	/** Changing the input list must not change the values captured by FeatureValue.of. */
	@Test
	public void ofCopiesTheInputValues() {
		List<String> values = new ArrayList<>(Collections.singletonList(ORIGINAL));
		FeatureValue snapshot = FeatureValue.of(EcorePackage.Literals.ENAMED_ELEMENT__NAME, values);

		values.set(0, "changed");

		FeatureValue expected = new FeatureValue(EcorePackage.Literals.ENAMED_ELEMENT__NAME,
				Collections.singletonList(ORIGINAL));
		assertEquals(expected, snapshot);
	}

	/** A constructor snapshot must remain a valid hash key after the caller replaces or appends values. */
	@Test
	public void constructorCopiesValuesBeforeCachingTheirHash() {
		List<Object> values = new ArrayList<>(Collections.singletonList(ORIGINAL));
		FeatureValue snapshot = new FeatureValue(EcorePackage.Literals.ENAMED_ELEMENT__NAME, values);
		FeatureValue expected = FeatureValue.of(EcorePackage.Literals.ENAMED_ELEMENT__NAME,
				Collections.singletonList(ORIGINAL));
		Set<FeatureValue> keys = new HashSet<>();
		keys.add(snapshot);

		values.set(0, "changed");
		values.add("extra");

		assertEquals(expected, snapshot);
		assertEquals(expected.hashCode(), snapshot.hashCode());
		assertTrue(keys.contains(expected));
	}

}
