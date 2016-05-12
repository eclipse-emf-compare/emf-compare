/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.resourceattachment.stereotype;

import static com.google.common.collect.Iterables.tryFind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.junit.runner.RunWith;

/**
 * This test makes sure that bug 493527 is fixed. The bug reports that when we use the default conflict
 * detector a stereotype application is removed on both sides, a REAL conflict is produced instead of PSEUDO
 * conflicts. The bug does not occur with the scalable conflict detector (MatchBasedConflictDetector).
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@RunWith(RuntimeTestRunner.class)
public class RemoveStereotypeApplicationPseudoConflictTest {

	private static Predicate<? super Conflict> hasConflict(final ConflictKind... kinds) {
		return new Predicate<Conflict>() {
			public boolean apply(Conflict input) {
				return input != null && Arrays.asList(kinds).contains(input.getKind());
			}
		};
	}

	/**
	 * Checks whether no REAL conflicts are created when the same stereotype application is removed on both
	 * sides. In the origin model the UML compare testing profile is used and a stereotype is applied on a
	 * single class. In the left model and the right model the stereotype application is removed but the
	 * profile application remains intact. With no further changes, the left and right model are equal and the
	 * comparison yields the same set of differences when comparing the models to the origin: ReferenceChange
	 * for the stereotype application base class (unset), ResourceAttachment deletion for the applied
	 * stereotype and a StereotypeApplicationChange deletion refined by the previous two differences.
	 * Therefore, any conflicts that are detected between the left and right model should only be PSEUDO
	 * conflicts.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access or find the input data.
	 */
	@Compare(left = "data/left.uml", right = "data/right.uml", ancestor = "data/origin.uml")
	public void testPseudoConflictForStereotypeRemoval(Comparison comparison) throws IOException {
		final EList<Conflict> conflicts = comparison.getConflicts();
		final Optional<Conflict> realConflict = tryFind(conflicts, hasConflict(ConflictKind.REAL));

		assertEquals(2, conflicts.size());
		assertFalse(realConflict.isPresent());
	}
}
