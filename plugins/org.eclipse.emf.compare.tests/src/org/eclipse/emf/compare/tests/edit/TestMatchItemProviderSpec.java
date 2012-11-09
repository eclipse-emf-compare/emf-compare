/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.edit;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.compare.provider.spec.MatchItemProviderSpec;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestMatchItemProviderSpec extends AbstractTestCompareItemProviderAdapter {

	private MatchItemProvider itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (MatchItemProviderSpec)compareItemProviderAdapterFactory.createMatchAdapter();
	}

	@Test
	public void testGetChildren_EcoreA1() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackageMatchChildren = itemProvider.getChildren(ePackageMatch);
		assertEquals(9, ePackageMatchChildren.size());
		assertEquals(4, size(filter(ePackageMatchChildren, Diff.class)));
		assertEquals(5, size(filter(ePackageMatchChildren, Match.class)));
	}

	static Match getEcoreA1_EPackageMatch() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		List<Match> matches = comparison.getMatches();
		Match ePackageMatch = matches.get(0);
		return ePackageMatch;
	}

	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name",
				"AudioVisualItem");
		Collection<?> audioVisualItem_MatchChildren = itemProvider.getChildren(audioVisualItem_Match);

		assertEquals(3, audioVisualItem_MatchChildren.size());
		assertEquals(2, size(filter(audioVisualItem_MatchChildren, Diff.class)));
		assertEquals(1, size(filter(audioVisualItem_MatchChildren, Match.class)));
	}

	@Test
	public void testGetChildren_AudioVisualItem_lenght() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name",
				"AudioVisualItem");
		Collection<?> audioVisualItem_MatchChildren = itemProvider.getChildren(audioVisualItem_Match);
		Match audioVisualItem_legnth_Match = getMatchWithFeatureValue(audioVisualItem_MatchChildren, "name",
				"length");
		Collection<?> audioVisualItem_legnth_MatchChildren = itemProvider
				.getChildren(audioVisualItem_legnth_Match);

		assertEquals(2, audioVisualItem_legnth_MatchChildren.size());
		assertEquals(2, size(filter(audioVisualItem_legnth_MatchChildren, Diff.class)));
		assertEquals(0, size(filter(audioVisualItem_legnth_MatchChildren, Match.class)));
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Book");
		Collection<?> book_MatchChildren = itemProvider.getChildren(book_Match);

		assertEquals(3, book_MatchChildren.size());
		assertEquals(3, size(filter(book_MatchChildren, Diff.class)));
		assertEquals(0, size(filter(book_MatchChildren, Match.class)));
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match bookCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "BookCategory");
		Collection<?> bookCategory_MatchChildren = itemProvider.getChildren(bookCategory_Match);

		assertEquals(4, bookCategory_MatchChildren.size());
		assertEquals(4, size(filter(bookCategory_MatchChildren, Diff.class)));
		assertEquals(0, size(filter(bookCategory_MatchChildren, Match.class)));
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match borrowable_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Borrowable");
		Collection<?> borrowable_MatchChildren = itemProvider.getChildren(borrowable_Match);

		assertEquals(1, borrowable_MatchChildren.size());
		assertEquals(1, size(filter(borrowable_MatchChildren, Diff.class)));
		assertEquals(0, size(filter(borrowable_MatchChildren, Match.class)));
	}

	@Test
	public void testGetChildren_Person() throws IOException {
		Match ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = itemProvider.getChildren(ePackageMatch);
		Match person_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Person");
		Collection<?> person_MatchChildren = itemProvider.getChildren(person_Match);

		assertEquals(3, person_MatchChildren.size());
		assertEquals(3, size(filter(person_MatchChildren, Diff.class)));
		assertEquals(0, size(filter(person_MatchChildren, Match.class)));
	}
}
