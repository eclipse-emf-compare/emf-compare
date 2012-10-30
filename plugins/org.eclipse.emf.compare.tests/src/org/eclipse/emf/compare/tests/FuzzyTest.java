/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Data;
import org.eclipse.emf.emfstore.fuzzy.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Util;
import org.eclipse.emf.emfstore.fuzzy.FuzzyRunner;
import org.eclipse.emf.emfstore.fuzzy.emf.EMFDataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.MutateUtil;
import org.eclipse.emf.emfstore.modelmutator.api.ModelMutatorConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A brute force test using fuzzy testing.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@RunWith(FuzzyRunner.class)
@DataProvider(EMFDataProvider.class)
public class FuzzyTest {

	@Data
	private EObject root;

	@Util
	private MutateUtil util;

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void detectingNoDifferenceOnACopy() {
		Assert.assertNotNull(root);
		EObject backup = EcoreUtil.copy(root);
		Comparison result = EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(root, backup));
		Assert.assertEquals(0, result.getDifferences().size());
	}

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void copyAllRightToLeft() {
		Assert.assertNotNull(root);
		EObject backup = EcoreUtil.copy(root);
		ModelMutatorConfiguration conf = new ModelMutatorConfiguration(EcorePackage.eINSTANCE, root, 1L);
		conf.setMaxDeleteCount(20);

		util.mutate(conf);

		Comparison result = EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(root, backup));
		int nbDiffs = result.getDifferences().size();

		for (Diff delta : result.getDifferences()) {
			delta.copyRightToLeft();
		}

		Comparison valid = EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(root, backup));
		Assert.assertEquals("We still have differences after merging all of them (had " + nbDiffs
				+ " to merge in the beginning)", 0, valid.getDifferences().size());

	}

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void copyAllLeftToRight() {
		Assert.assertNotNull(root);
		EObject backup = EcoreUtil.copy(root);
		ModelMutatorConfiguration conf = new ModelMutatorConfiguration(EcorePackage.eINSTANCE, root, 1L);
		conf.setMaxDeleteCount(20);

		util.mutate(conf);

		Comparison result = EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(root, backup));
		int nbDiffs = result.getDifferences().size();
		for (Diff delta : result.getDifferences()) {
			delta.copyLeftToRight();
		}

		Comparison valid = EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(root, backup));
		Assert.assertEquals("We still have differences after merging all of them (had " + nbDiffs
				+ " to merge in the beginning)", 0, valid.getDifferences().size());
	}
}
