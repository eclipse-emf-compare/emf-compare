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
package org.eclipse.emf.compare.rcp.ui.tests.contentmergeviewer.accessor.match;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.MatchAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.MatchAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.tests.contentmergeviewer.accessor.match.data.MatchAccessorInputData;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * Tests for {@link MatchAccessor}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MatchAccessorTest {

	private final static MatchAccessorInputData inputData = new MatchAccessorInputData();

	private final static ComposedAdapterFactory fAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

	private static Comparison comparison;

	@BeforeClass
	public static void beforeClass() throws IOException {
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		final Resource leftResource = inputData.getLeft();
		final Resource rightResource = inputData.getRight();
		final Resource originResource = inputData.getOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource,
				originResource);
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		comparison = comparisonBuilder.build().compare(scope);
	}

	@Test
	public void testEClassifiersAdd() {
		final List<Diff> differences = comparison.getDifferences();

		final Predicate<? super Diff> item = addedToReference("extlibrary", "eClassifiers",
				"extlibrary.Item");
		final Diff itemDiff = Iterators.find(differences.iterator(), item);
		final EObject itemValue = (EObject)MergeViewerUtil.getDiffValue(itemDiff);
		final Match itemMatch = comparison.getMatch(itemValue);

		final MatchAccessorFactory factory = new MatchAccessorFactory();
		final ITypedElement leftTypedElement = factory.createLeft(fAdapterFactory, itemMatch);
		final ITypedElement rightTypedElement = factory.createRight(fAdapterFactory, itemMatch);
		final ITypedElement originTypedElement = factory.createAncestor(fAdapterFactory, itemMatch);

		if (leftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)leftTypedElement).getDiff();
			assertEquals(diff, itemDiff);
		}
		if (rightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)rightTypedElement).getDiff();
			assertEquals(diff, itemDiff);
		}
		if (originTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)originTypedElement).getDiff();
			assertEquals(diff, itemDiff);
		}
	}

	@Test
	public void testEStructuralFeaturesDelete() {
		final List<Diff> differences = comparison.getDifferences();

		final Predicate<? super Diff> title = removedFromReference("extlibrary.Book", "eStructuralFeatures",
				"extlibrary.Book.title");
		final Diff titleDiff = Iterators.find(differences.iterator(), title);
		final EObject titleValue = (EObject)MergeViewerUtil.getDiffValue(titleDiff);
		final Match titleMatch = comparison.getMatch(titleValue);

		final MatchAccessorFactory factory = new MatchAccessorFactory();
		final ITypedElement leftTypedElement = factory.createLeft(fAdapterFactory, titleMatch);
		final ITypedElement rightTypedElement = factory.createRight(fAdapterFactory, titleMatch);
		final ITypedElement originTypedElement = factory.createAncestor(fAdapterFactory, titleMatch);

		if (leftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)leftTypedElement).getDiff();
			assertEquals(diff, titleDiff);
		}
		if (rightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)rightTypedElement).getDiff();
			assertEquals(diff, titleDiff);
		}
		if (originTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)originTypedElement).getDiff();
			assertEquals(diff, titleDiff);
		}
	}

	@Test
	public void testELiteralsMultipleDelete() {
		final List<Diff> differences = comparison.getDifferences();
		final MatchAccessorFactory factory = new MatchAccessorFactory();

		final Predicate<? super Diff> encyclopedia = removedFromReference("extlibrary.BookCategory",
				"eLiterals", "extlibrary.BookCategory.Encyclopedia");
		final Diff encyclopediaDiff = Iterators.find(differences.iterator(), encyclopedia);
		final EObject encyclopediaValue = (EObject)MergeViewerUtil.getDiffValue(encyclopediaDiff);
		final Match encyclopediaMatch = comparison.getMatch(encyclopediaValue);

		final ITypedElement encyclopediaLeftTypedElement = factory.createLeft(fAdapterFactory,
				encyclopediaMatch);
		final ITypedElement encyclopediaRightTypedElement = factory.createRight(fAdapterFactory,
				encyclopediaMatch);
		final ITypedElement encyclopediaOriginTypedElement = factory.createAncestor(fAdapterFactory,
				encyclopediaMatch);

		if (encyclopediaLeftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)encyclopediaLeftTypedElement).getDiff();
			assertEquals(diff, encyclopediaDiff);
		}
		if (encyclopediaRightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)encyclopediaRightTypedElement).getDiff();
			assertEquals(diff, encyclopediaDiff);
		}
		if (encyclopediaOriginTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)encyclopediaOriginTypedElement).getDiff();
			assertEquals(diff, encyclopediaDiff);
		}

		final Predicate<? super Diff> dictionary = removedFromReference("extlibrary.BookCategory",
				"eLiterals", "extlibrary.BookCategory.Dictionary");
		final Diff dictionaryDiff = Iterators.find(differences.iterator(), dictionary);
		final EObject dictionaryValue = (EObject)MergeViewerUtil.getDiffValue(dictionaryDiff);
		final Match dictionaryMatch = comparison.getMatch(dictionaryValue);

		final ITypedElement dictionaryLeftTypedElement = factory.createLeft(fAdapterFactory, dictionaryMatch);
		final ITypedElement dictionaryRightTypedElement = factory.createRight(fAdapterFactory,
				dictionaryMatch);
		final ITypedElement dictionaryOriginTypedElement = factory.createAncestor(fAdapterFactory,
				dictionaryMatch);

		if (dictionaryLeftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)dictionaryLeftTypedElement).getDiff();
			assertEquals(diff, dictionaryDiff);
		}
		if (dictionaryRightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)dictionaryRightTypedElement).getDiff();
			assertEquals(diff, dictionaryDiff);
		}
		if (dictionaryOriginTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)dictionaryOriginTypedElement).getDiff();
			assertEquals(diff, dictionaryDiff);
		}
	}

	@Test
	public void testEStructuralFeaturesMultipleAdd() {
		final List<Diff> differences = comparison.getDifferences();
		final MatchAccessorFactory factory = new MatchAccessorFactory();

		final Predicate<? super Diff> borrowed = addedToReference("extlibrary.Borrower",
				"eStructuralFeatures", "extlibrary.Borrower.borrowed");
		final Diff borrowedDiff = Iterators.find(differences.iterator(), borrowed);
		final EObject borrowedValue = (EObject)MergeViewerUtil.getDiffValue(borrowedDiff);
		final Match borrowedMatch = comparison.getMatch(borrowedValue);

		final ITypedElement borrowedLeftTypedElement = factory.createLeft(fAdapterFactory, borrowedMatch);
		final ITypedElement borrowedRightTypedElement = factory.createRight(fAdapterFactory, borrowedMatch);
		final ITypedElement borrowedOriginTypedElement = factory.createAncestor(fAdapterFactory,
				borrowedMatch);

		if (borrowedLeftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)borrowedLeftTypedElement).getDiff();
			assertEquals(diff, borrowedDiff);
		}
		if (borrowedRightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)borrowedRightTypedElement).getDiff();
			assertEquals(diff, borrowedDiff);
		}
		if (borrowedOriginTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)borrowedOriginTypedElement).getDiff();
			assertEquals(diff, borrowedDiff);
		}

		final Predicate<? super Diff> containmentBorrowed = addedToReference("extlibrary.Borrower",
				"eStructuralFeatures", "extlibrary.Borrower.containmentBorrowed");
		final Diff containmentBorrowedDiff = Iterators.find(differences.iterator(), containmentBorrowed);
		final EObject containmentBorrowedValue = (EObject)MergeViewerUtil
				.getDiffValue(containmentBorrowedDiff);
		final Match containmentBorrowedMatch = comparison.getMatch(containmentBorrowedValue);

		final ITypedElement containmentBorrowedLeftTypedElement = factory.createLeft(fAdapterFactory,
				containmentBorrowedMatch);
		final ITypedElement containmentBorrowedRightTypedElement = factory.createRight(fAdapterFactory,
				containmentBorrowedMatch);
		final ITypedElement containmentBorrowedOriginTypedElement = factory.createAncestor(fAdapterFactory,
				containmentBorrowedMatch);

		if (containmentBorrowedLeftTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)containmentBorrowedLeftTypedElement).getDiff();
			assertEquals(diff, containmentBorrowedDiff);
		}
		if (containmentBorrowedRightTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)containmentBorrowedRightTypedElement).getDiff();
			assertEquals(diff, containmentBorrowedDiff);
		}
		if (containmentBorrowedOriginTypedElement instanceof MergeViewerItem) {
			Diff diff = ((MergeViewerItem)containmentBorrowedOriginTypedElement).getDiff();
			assertEquals(diff, containmentBorrowedDiff);
		}
	}
}
