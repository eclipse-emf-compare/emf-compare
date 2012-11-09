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
import static com.google.common.collect.Iterables.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestReferenceChangeItemProviderSpec extends AbstractTestCompareItemProviderAdapter {

	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name",
				"AudioVisualItem");
		Collection<?> audioVisualItem_MatchChildren = adaptAsITreItemContentProvider(audioVisualItem_Match)
				.getChildren(audioVisualItem_Match);

		ReferenceChange titleReferenceChange = getReferenceChangeWithFeatureValue(
				audioVisualItem_MatchChildren, "name", "title");

		Collection<?> titleReferenceChange_Children = adaptAsITreItemContentProvider(titleReferenceChange)
				.getChildren(titleReferenceChange);

		assertEquals(1, titleReferenceChange_Children.size());
		Object child = get(titleReferenceChange_Children, 0);
		assertTrue(child instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)child).getReference());

		ReferenceChange titledItemReferenceChange = getReferenceChangeWithFeatureValue(
				audioVisualItem_MatchChildren, "name", "TitledItem");
		Collection<?> titledItemReferenceChange_Children = adaptAsITreItemContentProvider(
				titledItemReferenceChange).getChildren(titledItemReferenceChange);
		assertEquals(0, titledItemReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "Book");
		Collection<?> book_MatchChildren = adaptAsITreItemContentProvider(book_Match).getChildren(book_Match);

		ReferenceChange subtitleReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildren,
				"name", "subtitle");

		Collection<?> subtitleReferenceChange_Children = adaptAsITreItemContentProvider(
				subtitleReferenceChange).getChildren(subtitleReferenceChange);

		assertEquals(1, subtitleReferenceChange_Children.size());
		Notifier child = (Notifier)get(subtitleReferenceChange_Children, 0);
		assertTrue(child instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)child).getReference());
		assertTrue(adaptAsITreItemContentProvider(child).getChildren(child).isEmpty());

		ReferenceChange titleReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildren, "name",
				"title");
		Collection<?> titleReferenceChange_Children = adaptAsITreItemContentProvider(titleReferenceChange)
				.getChildren(titleReferenceChange);
		assertEquals(1, titleReferenceChange_Children.size());
		child = (Notifier)get(titleReferenceChange_Children, 0);
		assertTrue(child instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)child).getReference());
		assertTrue(adaptAsITreItemContentProvider(child).getChildren(child).isEmpty());

		ReferenceChange titledItemReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildren,
				"name", "TitledItem");
		Collection<?> titledItemReferenceChange_Children = adaptAsITreItemContentProvider(
				titledItemReferenceChange).getChildren(titledItemReferenceChange);
		assertEquals(0, titledItemReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match borrowableCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name",
				"Borrowable");
		Collection<?> borrowable_MatchChildren = adaptAsITreItemContentProvider(borrowableCategory_Match)
				.getChildren(borrowableCategory_Match);

		assertEquals(1, borrowable_MatchChildren.size());
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);
		Match bookCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildren, "name", "BookCategory");
		Collection<?> bookCategory_MatchChildren = adaptAsITreItemContentProvider(bookCategory_Match)
				.getChildren(bookCategory_Match);

		ReferenceChange dictionaryReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Dictionary");
		ReferenceChange encyclopediaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Encyclopedia");
		ReferenceChange mangaReferenceChange = getReferenceChangeWithFeatureValue(bookCategory_MatchChildren,
				"name", "Manga");
		ReferenceChange manhwaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildren, "name", "Manhwa");

		assertTrue(adaptAsITreItemContentProvider(dictionaryReferenceChange).getChildren(
				dictionaryReferenceChange).isEmpty());
		assertTrue(adaptAsITreItemContentProvider(encyclopediaReferenceChange).getChildren(
				encyclopediaReferenceChange).isEmpty());
		assertTrue(adaptAsITreItemContentProvider(mangaReferenceChange).getChildren(mangaReferenceChange)
				.isEmpty());
		assertTrue(adaptAsITreItemContentProvider(manhwaReferenceChange).getChildren(manhwaReferenceChange)
				.isEmpty());
	}

	@Test
	public void testGetChildren_Magazine1() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);

		Collection<?> magazineChildren = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildren, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName())
					&& "CirculatingItem".equals(eClass.getESuperTypes().get(0).getName())) {
				magazineChildren = adaptAsITreItemContentProvider(referenceChange).getChildren(
						referenceChange);
				assertEquals(3, magazineChildren.size());
				break;
			}
		}
		ReferenceChange magazineSuperTypeChange = getReferenceChangeWithFeatureValue(magazineChildren,
				"name", "CirculatingItem");
		assertTrue(adaptAsITreItemContentProvider(magazineSuperTypeChange).getChildren(
				magazineSuperTypeChange).isEmpty());

		ReferenceChange magazineSFChange1 = getReferenceChangeWithFeatureValue(magazineChildren, "name",
				"pages");
		assertEquals(1, adaptAsITreItemContentProvider(magazineSFChange1).getChildren(magazineSFChange1)
				.size());

		ReferenceChange magazineSFChange2 = getReferenceChangeWithFeatureValue(magazineChildren, "name",
				"title");
		assertEquals(1, adaptAsITreItemContentProvider(magazineSFChange2).getChildren(magazineSFChange2)
				.size());
	}

	@Test
	public void testGetChildren_Magazine2() throws IOException {
		Match ePackageMatch = TestMatchItemProviderSpec.getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = adaptAsITreItemContentProvider(ePackageMatch).getChildren(
				ePackageMatch);

		Collection<?> magazineChildren = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildren, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName())
					&& "Periodical".equals(eClass.getESuperTypes().get(0).getName())) {
				magazineChildren = adaptAsITreItemContentProvider(referenceChange).getChildren(
						referenceChange);
				assertEquals(1, magazineChildren.size());
				break;
			}
		}
		ReferenceChange magazineSuperTypeChange = getReferenceChangeWithFeatureValue(magazineChildren,
				"name", "Periodical");
		assertTrue(adaptAsITreItemContentProvider(magazineSuperTypeChange).getChildren(
				magazineSuperTypeChange).isEmpty());
	}

	// still to add: Periodical, Person, TitleItem
}
