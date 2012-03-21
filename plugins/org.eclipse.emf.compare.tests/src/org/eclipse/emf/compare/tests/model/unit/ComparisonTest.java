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
package org.eclipse.emf.compare.tests.model.unit;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.tests.model.mock.MockCompareModel;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

/**
 * Tests the behavior of the methods from the {@link Comparison} class that were not generated.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class ComparisonTest {
	@Test
	public void createComparison() throws IOException {
		MockCompareModel mock = new MockCompareModel();
		Comparison comparison = mock.createComparisonModel();

		for (MatchResource res : comparison.getMatchedResources()) {
			System.out.println("Matched resources :");
			System.out.println("Left = " + res.getLeftURI());
			System.out.println("Right = " + res.getRightURI());
			System.out.println("origin = " + res.getOriginURI());
		}
		System.out.println();
		System.out
				.println("|                  Left                  |                 Right                  |                 Origin                 |");
		System.out
				.println("|----------------------------------------|----------------------------------------|----------------------------------------|");
		for (Match match : comparison.getMatches()) {
			printMatch(match);
		}
	}

	private void printMatch(Match match) {
		String leftName = null;
		String rightName = null;
		String originName = null;

		final EObject left = match.getLeft();
		final EObject right = match.getRight();
		final EObject origin = match.getOrigin();

		// Ignore this match if it is not a Named element
		if ((left == null || left instanceof ENamedElement)
				&& (right == null || right instanceof ENamedElement)
				&& (origin == null || origin instanceof ENamedElement)) {
			if (left != null) {
				leftName = formatName((ENamedElement)left);
			}

			if (right != null) {
				rightName = formatName((ENamedElement)right);
			}

			if (origin != null) {
				originName = formatName((ENamedElement)origin);
			}

			if (leftName == null) {
				int level = 0;
				EObject currentMatch = match;
				while (currentMatch instanceof Match && ((Match)currentMatch).getLeft() == null) {
					currentMatch = currentMatch.eContainer();
				}
				while (currentMatch instanceof Match && ((Match)currentMatch).getLeft() != null) {
					level++;
					currentMatch = currentMatch.eContainer();
				}
				leftName = getEmptyLine(level);
			}
			if (rightName == null) {
				int level = 0;
				EObject currentMatch = match;
				while (currentMatch instanceof Match && ((Match)currentMatch).getRight() == null) {
					currentMatch = currentMatch.eContainer();
				}
				while (currentMatch instanceof Match && ((Match)currentMatch).getRight() != null) {
					level++;
					currentMatch = currentMatch.eContainer();
				}
				rightName = getEmptyLine(level);
			}
			if (originName == null) {
				int level = 0;
				EObject currentMatch = match;
				while (currentMatch instanceof Match && ((Match)currentMatch).getOrigin() == null) {
					currentMatch = currentMatch.eContainer();
				}
				while (currentMatch instanceof Match && ((Match)currentMatch).getOrigin() != null) {
					level++;
					currentMatch = currentMatch.eContainer();
				}
				originName = getEmptyLine(level);
			}

			System.out.println('|' + leftName + '|' + rightName + '|' + originName + '|');
		}

		for (Match submatch : match.getSubMatches()) {
			printMatch(submatch);
		}
	}

	/**
	 * Formats the named of the given element by adding spaces before and after it so that it spans 40
	 * characters at most.
	 */
	private static String formatName(ENamedElement element) {
		final int lineLength = 40;
		String name = element.getName();
		int level = 0;
		EObject current = element;
		while (current.eContainer() != null) {
			level++;
			current = current.eContainer();
		}

		char[] charsBefore = new char[1 + (level * 2)];
		charsBefore[0] = ' ';
		if (level > 0) {
			for (int i = 1; i < charsBefore.length - 2; i = i + 2) {
				charsBefore[i] = '|';
				charsBefore[i + 1] = ' ';
			}
			charsBefore[charsBefore.length - 2] = '|';
			charsBefore[charsBefore.length - 1] = '-';
		}

		int missingChars = lineLength - name.length() - charsBefore.length;
		final char[] spacesAfter = new char[Math.max(0, missingChars)];

		return String.valueOf(charsBefore) + name + String.valueOf(spacesAfter);
	}

	/**
	 * Returns an "empty line" which will only show pipes for previous levels.
	 */
	private static String getEmptyLine(int level) {
		final int lineLength = 40;

		char[] charsBefore = new char[1 + (level * 2)];
		charsBefore[0] = ' ';
		for (int i = 1; i < charsBefore.length; i = i + 2) {
			charsBefore[i] = '|';
			charsBefore[i + 1] = ' ';
		}

		int missingChars = lineLength - charsBefore.length;
		final char[] spacesAfter = new char[Math.max(0, missingChars)];

		return String.valueOf(charsBefore) + String.valueOf(spacesAfter);
	}
}
