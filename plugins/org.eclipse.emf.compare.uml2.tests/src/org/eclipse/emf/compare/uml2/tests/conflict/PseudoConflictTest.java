/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.conflict;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.PseudoConflictMerger;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the handling of pseudo conflicts within UML models.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class PseudoConflictTest extends AbstractUMLTest {

	/**
	 * Test input data.
	 */
	private ConflictInputData input = new ConflictInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	/**
	 * Tests if EMF Compare uses the {@link PseudoConflictMerger} when merging pseudo conflicts of UML models.
	 * 
	 * @throws IOException
	 *             When an error is thrown while reading the models.
	 */
	@Test
	public void testPseudoConflictMergerPriority() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();
		final Resource origin = input.getA1Origin();

		final Comparison comparison = compare(left, right, origin);
		final EList<Diff> differences = comparison.getDifferences();

		Iterator<Diff> conflicts = Iterators.filter(differences.iterator(), hasConflict(ConflictKind.PSEUDO));

		final Registry registry = getMergerRegistry();

		while (conflicts.hasNext()) {
			Diff conflict = conflicts.next();
			IMerger merger = registry.getHighestRankingMerger(conflict);

			assertThat(merger, instanceOf(PseudoConflictMerger.class));
		}
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}
}
