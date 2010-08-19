/*******************************************************************************
 * Copyright (c) 2010 Gerhardt Informatics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerhardt Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Base class for implementing MergeService test cases. The tests consist of comparing/diffing two models and
 * merging all changes from the left model into the right model. Subclasses are expected to created these
 * models in their setUp() method. They are also expected to provide a model that has to match the result of
 * the merging. Merging is always done so that the leftModel remains unchanged and the right model is compared
 * to the expected model. (When doing rightToLeft merging, the left and right models are swapped). Subclasses
 * do not need to implement test methods, they will inherit two such methods from this class.
 * 
 * @author Csaba Koncz
 */
public abstract class MergeTestBase extends TestCase {

	/**
	 * The leftModel of merging in leftToRight merge mode. This model is never changed during the merge.
	 */
	protected EObject leftModel;

	/**
	 * This model is the target of the merge model.
	 */
	protected EObject rightModel;

	/**
	 * This is the expected outcome of the merge. Used for asserting test failure/success.
	 */
	protected EObject expectedModel;

	@Override
	public void setUp() throws Exception {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	}

	/**
	 * Perform the merging of the models prepared by in setUp().
	 * 
	 * @param isLeftToRight
	 *            indicates the direction of the merging.
	 * @throws Exception
	 */
	protected void doPerformTest(boolean isLeftToRight) throws Exception {

		EObject testLeftModel = isLeftToRight ? leftModel : rightModel;
		EObject testRightModel = isLeftToRight ? rightModel : leftModel;

		Map<String, Object> options = Collections.emptyMap();

		MatchModel match = MatchService.doMatch(testLeftModel, testRightModel, options);
		DiffModel diff = DiffService.doDiff(match);

		EList<DiffElement> differences = diff.getDifferences();

		preMergeHook(isLeftToRight);
		MergeService.merge(differences, isLeftToRight);
		postMergeHook(isLeftToRight);

		boolean mergeOK = EcoreUtil.equals(expectedModel, rightModel);

		if (false == mergeOK) {

			System.err.println("Expected :\n" + ModelUtils.serialize(expectedModel));
			System.err.println("Actual   :\n" + ModelUtils.serialize(rightModel));

			fail(" Merge (leftToRight=" + isLeftToRight + ")failed ");
		}
	}

	/**
	 * Subclasses can use this method to perform tasks after the merging is done.
	 * 
	 * @param isLeftToRight
	 */
	protected void postMergeHook(boolean isLeftToRight) {
		// nothing to do by default
	}

	/**
	 * Subclasses can use this method to perform tasks before the merging is done.
	 * 
	 * @param isLeftToRight
	 */
	protected void preMergeHook(boolean isLeftToRight) {
		// nothing to do by default
	}

	public void testLeftToRight() throws Exception {
		doPerformTest(true);
	}

	public void testRightToLeft() throws Exception {
		doPerformTest(false);
	}
}
