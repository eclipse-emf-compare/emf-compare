/**
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests;

import static org.junit.Assert.assertSame;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.match.eobject.URIDistance;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
@SuppressWarnings("nls")
public class FuzzyTest {

	@Data
	private EObject root;

	@Util
	private MutateUtil util;

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void detectingNoDifferenceOnACopy() {
		Assert.assertNotNull(root);
		EObject backup = EcoreUtil.copy(root);
		Comparison result = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(root, backup, null));
		Assert.assertEquals(0, result.getDifferences().size());
	}

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void copyAllRightToLeft() {
		Assert.assertNotNull(root);
		removeAllDuplicateCrossReferencesFrom(root);
		EObject backup = EcoreUtil.copy(root);

		util.mutate(createConfig());
		EObject mutated = EcoreUtil.copy(root);
		removeAllDuplicateCrossReferencesFrom(root);

		Comparison result = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(root, backup, null));
		int nbDiffs = result.getDifferences().size();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(result.getDifferences(), new BasicMonitor());

		for (Diff delta : result.getDifferences()) {
			assertSame(delta.getState(), DifferenceState.MERGED);
		}

		Comparison valid = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(root, backup, null));
		List<Diff> differences = valid.getDifferences();

		Set<String> urisToDebug = Sets.newLinkedHashSet();
		for (ReferenceChange diff : Iterables.filter(differences, ReferenceChange.class)) {
			if (diff.getMatch().getRight() != null) {
				urisToDebug.add(new URIDistance().apply(diff.getMatch().getRight()).toString());
			}

		}

		if (urisToDebug.size() > 0) {
			/*
			 * restart
			 */
			root = EcoreUtil.copy(mutated);
			result = EMFCompare.builder().build().compare(new DefaultComparisonScope(root, backup, null));
			for (Diff diff : result.getDifferences()) {
				if (diff.getMatch().getRight() != null) {
					String uri = new URIDistance().apply(diff.getMatch().getRight()).toString();
					if (urisToDebug.contains(uri)) {
						final IMerger diffMerger = mergerRegistry.getHighestRankingMerger(diff);
						diffMerger.copyRightToLeft(diff, new BasicMonitor());
					}
				}
			}
		}

		Assert.assertEquals("We still have differences after merging all of them (had " + nbDiffs
				+ " to merge in the beginning)", 0, differences.size());

	}

	/**
	 * Test to check if the {@link FuzzyRunner} is working.
	 */
	@Test
	public void copyAllLeftToRight() {
		Assert.assertNotNull(root);
		removeAllDuplicateCrossReferencesFrom(root);
		EObject backup = EcoreUtil.copy(root);

		util.mutate(createConfig());
		removeAllDuplicateCrossReferencesFrom(root);

		Comparison result = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(root, backup, null));
		int nbDiffs = result.getDifferences().size();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(result.getDifferences(), new BasicMonitor());

		Comparison valid = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(root, backup, null));
		List<Diff> differences = valid.getDifferences();
		Assert.assertEquals("We still have differences after merging all of them (had " + nbDiffs
				+ " to merge in the beginning)", 0, differences.size());
	}

	private ModelMutatorConfiguration createConfig() {
		ModelMutatorConfiguration conf = new ModelMutatorConfiguration(EcorePackage.eINSTANCE, root, 1L);
		conf.setMaxDeleteCount(0);
		return conf;
	}

	private static void removeAllDuplicateCrossReferencesFrom(EObject contentRoot) {
		for (EReference reference : contentRoot.eClass().getEAllReferences()) {
			if (!reference.isContainment() && !reference.isDerived() && reference.isMany()) {
				@SuppressWarnings("unchecked")
				final Iterator<EObject> crossReferences = ((List<EObject>)contentRoot.eGet(reference))
						.iterator();
				final Set<EObject> noDupes = Sets.newHashSet();
				while (crossReferences.hasNext()) {
					if (!noDupes.add(crossReferences.next())) {
						crossReferences.remove();
					}
				}
			}
		}

		final Iterator<EObject> contentIterator = contentRoot.eContents().iterator();
		while (contentIterator.hasNext()) {
			removeAllDuplicateCrossReferencesFrom(contentIterator.next());
		}
	}
}
