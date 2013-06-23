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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestReferenceChangeItemProviderSpec extends AbstractTestCompareItemProviderAdapter {

	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", //$NON-NLS-1$
				"AudioVisualItem"); //$NON-NLS-1$
		Collection<?> audioVisualItem_MatchChildren = adaptAsITreeItemContentProvider(audioVisualItem_Match)
				.getChildren(audioVisualItem_Match);

		ReferenceChange titleReferenceChange = getReferenceChangeWithFeatureValue(
				audioVisualItem_MatchChildren, "name", "title"); //$NON-NLS-1$ //$NON-NLS-2$

		Collection<?> titleReferenceChange_Children = adaptAsITreeItemContentProvider(titleReferenceChange)
				.getChildren(titleReferenceChange);

		assertEquals(0, titleReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> book_MatchChildren = adaptAsITreeItemContentProvider(book_Match)
				.getChildren(book_Match);

		ReferenceChange subtitleReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildren,
				"name", "subtitle"); //$NON-NLS-1$ //$NON-NLS-2$

		Collection<?> subtitleReferenceChange_Children = adaptAsITreeItemContentProvider(
				subtitleReferenceChange).getChildren(subtitleReferenceChange);

		assertEquals(0, subtitleReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match borrowableCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", //$NON-NLS-1$
				"Borrowable"); //$NON-NLS-1$
		Collection<?> borrowable_MatchChildren = adaptAsITreeItemContentProvider(borrowableCategory_Match)
				.getChildren(borrowableCategory_Match);

		assertEquals(3, borrowable_MatchChildren.size());
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match bookCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "BookCategory"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> bookCategory_MatchChildren = adaptAsITreeItemContentProvider(bookCategory_Match)
				.getChildren(bookCategory_Match);

		ReferenceChange dictionaryReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Dictionary"); //$NON-NLS-1$ //$NON-NLS-2$
		ReferenceChange encyclopediaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Encyclopedia"); //$NON-NLS-1$ //$NON-NLS-2$
		ReferenceChange mangaReferenceChange = getReferenceChangeWithFeatureValue(bookCategory_MatchChildren,
				"name", "Manga"); //$NON-NLS-1$ //$NON-NLS-2$
		ReferenceChange manhwaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Manhwa"); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(adaptAsITreeItemContentProvider(dictionaryReferenceChange).getChildren(
				dictionaryReferenceChange).isEmpty());
		assertTrue(adaptAsITreeItemContentProvider(encyclopediaReferenceChange).getChildren(
				encyclopediaReferenceChange).isEmpty());
		assertTrue(adaptAsITreeItemContentProvider(mangaReferenceChange).getChildren(mangaReferenceChange)
				.isEmpty());
		assertTrue(adaptAsITreeItemContentProvider(manhwaReferenceChange).getChildren(manhwaReferenceChange)
				.isEmpty());
	}

	@Test
	public void testGetChildren_Magazine1() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);

		Collection<?> magazineChildren = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildren, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName()) //$NON-NLS-1$
					&& "CirculatingItem".equals(eClass.getESuperTypes().get(0).getName())) { //$NON-NLS-1$
				magazineChildren = adaptAsITreeItemContentProvider(referenceChange).getChildren(
						referenceChange);
				assertEquals(0, magazineChildren.size());
				break;
			}
		}
	}

	@Test
	public void testGetChildren_Magazine2() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);

		Collection<?> magazineChildren = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildren, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName()) //$NON-NLS-1$
					&& "Periodical".equals(eClass.getESuperTypes().get(0).getName())) { //$NON-NLS-1$
				magazineChildren = adaptAsITreeItemContentProvider(referenceChange).getChildren(
						referenceChange);
				assertEquals(0, magazineChildren.size());
				break;
			}
		}
	}

	@Test
	public void testGetChildren_Periodical() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		ReferenceChange periodical_ReferenceChange = getReferenceChangeWithFeatureValue(
				ePackage_MatchChildren, "name", "Periodical"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> periodical_ReferenceChangeChildren = adaptAsITreeItemContentProvider(
				periodical_ReferenceChange).getChildren(periodical_ReferenceChange);

		assertEquals(0, periodical_ReferenceChangeChildren.size());
	}

	@Test
	public void testGetChildren_Person() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match person_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Person"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> person_MatchChildren = adaptAsITreeItemContentProvider(person_Match).getChildren(
				person_Match);

		assertEquals(6, person_MatchChildren.size());

		ReferenceChange issuesPerYearChange = getReferenceChangeWithFeatureValue(person_MatchChildren,
				"name", "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> firstNameChildren = adaptAsITreeItemContentProvider(issuesPerYearChange).getChildren(
				issuesPerYearChange);
		assertEquals(0, firstNameChildren.size());
	}

	@Test
	public void testGetChildren_TitledItem() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreeItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);

		ReferenceChange titledItem_ReferenceChange = getReferenceChangeWithFeatureValue(
				ePackage_MatchChildren, "name", "TitledItem"); //$NON-NLS-1$ //$NON-NLS-2$
		Collection<?> titledItem_ReferenceChangeChildren = adaptAsITreeItemContentProvider(
				titledItem_ReferenceChange).getChildren(titledItem_ReferenceChange);
		assertEquals(0, titledItem_ReferenceChangeChildren.size());
	}
}
