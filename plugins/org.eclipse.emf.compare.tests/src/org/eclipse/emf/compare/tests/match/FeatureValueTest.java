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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.match.hash.FeatureValue;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

/**
 * Verifies the input-list ownership contract of {@link FeatureValue#of}. Custom hashers can use this factory
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
	/** Changing the input list must not change the values captured by FeatureValue.of. */
	@Test
	public void ofCopiesTheInputValues() {
		List<String> values = new ArrayList<>(Collections.singletonList("original"));
		FeatureValue snapshot = FeatureValue.of(EcorePackage.Literals.ENAMED_ELEMENT__NAME, values);

		values.set(0, "changed");

		FeatureValue expected = new FeatureValue(EcorePackage.Literals.ENAMED_ELEMENT__NAME,
				Collections.singletonList("original"));
		assertEquals(expected, snapshot);
	}
}
