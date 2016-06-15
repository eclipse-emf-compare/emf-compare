/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Collections2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.tests.merge.data.bug485266.Bug485266InputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class assert that EMFCompare correctly handle conflicts between the suppression of an element
 * (packageA) on a side and the move of an other element (which is a child of packageA) on the other side.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@RunWith(Parameterized.class)
public class Bug485266_MoveDeleteConflict_Test extends AbstractMergerTest {

	/**
	 * Create the list of parameters for the parametrized test. The parameters for each test is an object:
	 * 
	 * <pre>
	 * Object[] {
	 * 			originResource, 
	 * 			leftResource, 
	 * 			rightResource, 
	 * 			boolean for the merge direction,
	 * 			the resource we expect after the merge}
	 * </pre>
	 * 
	 * @return the list of parameters for the tests
	 * @throws IOException
	 */
	@SuppressWarnings({"boxing" })
	@Parameters
	public static Collection<Object[]> params() throws IOException {
		Bug485266InputData inputData = new Bug485266InputData();

		/**
		 * <pre>
		 * The ancestor:
		 * - root
		 *   - packageA
		 *     - nodeB
		 * 
		 * The left model:
		 * - root
		 * 
		 * The right model:
		 * - root
		 *   - packageA
		 *   - nodeB
		 *   
		 * The correct result on the left side after a merge of the move of nodeB from right to left:
		 * - root
		 *   - nodeB
		 * </pre>
		 */
		Object[] test1RightToLeft = new Object[] {inputData.getData1AncestorResource(),
				inputData.getData1LeftResource(), inputData.getData1RightResource(), true,
				inputData.getData1ResultResource() };
		Object[] test1LeftToRight = new Object[] {inputData.getData1AncestorResource(),
				inputData.getData1RightResource(), inputData.getData1LeftResource(), false,
				inputData.getData1ResultResource() };

		/**
		 * <pre>
		 * The ancestor:
		 * - root
		 *   - packageA
		 *     - packageB
		 *       - packageC
		 *         - packageD
		 *           - packageE
		 * 
		 * The left model:
		 * - root
		 * 
		 * The right model:
		 * - root
		 *   - packageA
		 *   - packageB
		 *     - packageC
		 *   - packageD
		 *     - packageE
		 *   
		 * The correct result on the left side after a merge of the move of packageB and packageD from right to left:
		 * - root
		 *   - packageB
		 *   - packageD
		 * </pre>
		 */
		Object[] test2RightToLeft = new Object[] {inputData.getData2AncestorResource(),
				inputData.getData2LeftResource(), inputData.getData2RightResource(), true,
				inputData.getData2ResultResource() };
		Object[] test2LeftToRight = new Object[] {inputData.getData2AncestorResource(),
				inputData.getData2RightResource(), inputData.getData2LeftResource(), false,
				inputData.getData2ResultResource() };

		/**
		 * More complex models (supertypes, renaming)
		 */
		Object[] test3RightToLeft = new Object[] {inputData.getData3AncestorResource(),
				inputData.getData3LeftResource(), inputData.getData3RightResource(), true,
				inputData.getData3ResultResource() };
		Object[] test3LeftToRight = new Object[] {inputData.getData3AncestorResource(),
				inputData.getData3RightResource(), inputData.getData3LeftResource(), false,
				inputData.getData3ResultResource() };

		/**
		 * <pre>
		 * The ancestor:
		 * - root
		 *   - packageA
		 *     - packageB
		 *       - packageC
		 * 
		 * The left model:
		 * - root
		 * 
		 * The right model:
		 * - root
		 *   - packageC
		 *     - packageB
		 *   
		 * The correct result on the left side after a merge of the move of packageB and packageD from right to left:
		 * - root
		 *   - packageC
		 *     - packageB
		 * </pre>
		 * 
		 * The test is actually commented due to a bug in comparison process.
		 */
		Object[] test4RightToLeft = new Object[] {inputData.getData4AncestorResource(),
				inputData.getData4LeftResource(), inputData.getData4RightResource(), true,
				inputData.getData4ResultResource() };
		Object[] test4LeftToRight = new Object[] {inputData.getData4AncestorResource(),
				inputData.getData4RightResource(), inputData.getData4LeftResource(), false,
				inputData.getData4ResultResource() };

		/**
		 * <pre>
		 * The ancestor:
		 * - root
		 *   - packageA
		 *   - packageB
		 *   - packageC
		 * 
		 * The left model:
		 * - root
		 * 
		 * The right model:
		 * - root
		 *   - packageB
		 *     - packageA
		 *   - packageD
		 *   
		 * The correct result on the left side after a merge of the move of packageB and packageD from right to left:
		 * - root
		 *   - packageB
		 *     - packageA
		 * </pre>
		 */
		Object[] test5RightToLeft = new Object[] {inputData.getData5AncestorResource(),
				inputData.getData5LeftResource(), inputData.getData5RightResource(), true,
				inputData.getData5ResultResource() };
		Object[] test5LeftToRight = new Object[] {inputData.getData5AncestorResource(),
				inputData.getData5RightResource(), inputData.getData5LeftResource(), false,
				inputData.getData5ResultResource() };

		// test4 is not used for the moment since their is a bug in the comparison process with this models
		return Arrays.asList(test1RightToLeft, test1LeftToRight, test2RightToLeft, test2LeftToRight,
				test3RightToLeft, test3LeftToRight, test5RightToLeft, test5LeftToRight);
	}

	public Bug485266_MoveDeleteConflict_Test(final Resource origin, final Resource left, final Resource right,
			final boolean rightToLeft, final Resource expected) {
		super(origin, left, right, rightToLeft, expected, IMerger.RegistryImpl.createStandaloneInstance());
	}

	@Override
	protected List<Diff> getDiffsToMerge() {
		List<Diff> diffsToMerge = new ArrayList<Diff>();

		final EList<Conflict> conflicts = comparison.getConflicts();
		for (Conflict conflict : conflicts) {
			final EList<Diff> differences = conflict.getDifferences();

			final Collection<Diff> moveDiffs = Collections2.filter(differences, ofKind(MOVE));
			assertEquals(1, moveDiffs.size());

			final Diff move = moveDiffs.iterator().next();
			diffsToMerge.add(move);
		}
		return diffsToMerge;
	}
}
