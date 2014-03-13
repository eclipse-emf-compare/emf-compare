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
package org.eclipse.emf.compare.utils;

import com.google.common.collect.Iterables;

import java.io.PrintStream;
import java.util.Arrays;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

// TODO do we need to externalize this? For now, suppressing the NLS warnings.
/**
 * This class exposes methods to serialize a "human-readable" form of the comparison model onto a given
 * stream.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public final class EMFComparePrettyPrinter {
	/** This is the max length of the columns we display for the Match. */
	private static final int COLUMN_LENGTH = 40;

	/**
	 * Hides default constructor.
	 */
	private EMFComparePrettyPrinter() {
		// No need to construct an instance of this.
	}

	/**
	 * Prints the whole comparison on the given stream (might be {@code stream}).
	 * 
	 * @param comparison
	 *            The comparison we are to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print this comparison model.
	 */
	public static void printComparison(Comparison comparison, PrintStream stream) {
		for (MatchResource res : comparison.getMatchedResources()) {
			stream.println("Matched resources :");
			stream.println("Left = " + res.getLeftURI());
			stream.println("Right = " + res.getRightURI());
			stream.println("origin = " + res.getOriginURI());
		}
		stream.println();

		printMatch(comparison, stream);

		stream.println();

		printDifferences(comparison, stream);
	}

	/**
	 * Prints all the Match elements contained by the given {@code comparison}. Each Match will be displayed
	 * on its own line.
	 * <p>
	 * For example, if the left model has two packages "package1" and "package2", but the right has "package1"
	 * and "package3", what we will display here depends on the Match : if "left.package1" is matched with
	 * "right.package1", but "package2" and "package3" did not match, this will print <code><pre>
	 * | package1 | package1 |
	 * | package2 |          |
	 * |          | package3 |
	 * </pre></code> On the contrary, if "package2" and "package3" did match, we will display <code><pre>
	 * | package1 | package1 |
	 * | package2 | package3 |
	 * </pre></code>
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison which Matched elements we are to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print the matched elements of this comparison.
	 */
	public static void printMatch(Comparison comparison, PrintStream stream) {
		final String separator = "+----------------------------------------+----------------------------------------+----------------------------------------+";
		final String leftLabel = "Left";
		final String rightLabel = "Right";
		final String originLabel = "Origin";
		stream.println(separator);
		stream.println('|' + formatHeader(leftLabel) + '|' + formatHeader(rightLabel) + '|'
				+ formatHeader(originLabel) + '|');
		stream.println(separator);
		for (Match match : comparison.getMatches()) {
			printMatch(match, stream);
		}
		stream.println(separator);
	}

	/**
	 * Prints all differences detected for the given {@code comparison} on the given {@code stream}.
	 * 
	 * @param comparison
	 *            The comparison which differences we are to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print these differences.
	 */
	public static void printDifferences(Comparison comparison, PrintStream stream) {
		final Iterable<ReferenceChange> refChanges = Iterables.filter(comparison.getDifferences(),
				ReferenceChange.class);

		stream.println("REFERENCE CHANGES");
		for (Diff diff : refChanges) {
			printDiff(diff, stream);
		}
		stream.println();

		stream.println("ATTRIBUTE CHANGES");
		final Iterable<AttributeChange> attChanges = Iterables.filter(comparison.getDifferences(),
				AttributeChange.class);
		for (Diff diff : attChanges) {
			printDiff(diff, stream);
		}
		stream.println();

		stream.println("CONFLICTS");
		for (Conflict conflict : comparison.getConflicts()) {
			printConflict(conflict, stream);
		}
	}

	/**
	 * Prints the given {@link Conflict} on the given {@code stream}.
	 * 
	 * @param conflict
	 *            The conflict we need to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print this conflict.
	 */
	private static void printConflict(Conflict conflict, PrintStream stream) {
		stream.println(conflict.getKind() + " conflict:");
		final Iterable<ReferenceChange> refChanges = Iterables.filter(conflict.getDifferences(),
				ReferenceChange.class);
		for (Diff diff : refChanges) {
			stream.print("\t");
			printDiff(diff, stream);
		}
		final Iterable<AttributeChange> attChanges = Iterables.filter(conflict.getDifferences(),
				AttributeChange.class);
		for (Diff diff : attChanges) {
			stream.print("\t");
			printDiff(diff, stream);
		}

	}

	/**
	 * Prints the given {@link Diff difference} on the given {@code stream}.
	 * 
	 * @param diff
	 *            The difference we are to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print this difference.
	 */
	private static void printDiff(Diff diff, PrintStream stream) {
		if (diff instanceof ReferenceChange) {
			final ReferenceChange refChange = (ReferenceChange)diff;
			final String valueName;
			if (refChange.getValue() instanceof ENamedElement) {
				valueName = ((ENamedElement)refChange.getValue()).getName();
			} else {
				valueName = refChange.getValue().toString();
			}
			String change = "";
			if (diff.getSource() == DifferenceSource.RIGHT) {
				change = "remotely ";
			}
			if (diff.getKind() == DifferenceKind.ADD) {
				change += "added to";
			} else if (diff.getKind() == DifferenceKind.DELETE) {
				change += "deleted from";
			} else if (diff.getKind() == DifferenceKind.CHANGE) {
				change += "changed from";
			} else {
				change += "moved from";
			}
			final String objectName;
			if (refChange.getMatch().getLeft() instanceof ENamedElement) {
				objectName = ((ENamedElement)refChange.getMatch().getLeft()).getName();
			} else if (refChange.getMatch().getRight() instanceof ENamedElement) {
				objectName = ((ENamedElement)refChange.getMatch().getRight()).getName();
			} else if (refChange.getMatch().getOrigin() instanceof ENamedElement) {
				objectName = ((ENamedElement)refChange.getMatch().getOrigin()).getName();
			} else {
				objectName = "";
			}
			stream.println("value " + valueName + " has been " + change + " reference "
					+ refChange.getReference().getName() + " of object " + objectName);
		} else if (diff instanceof AttributeChange) {
			final AttributeChange attChange = (AttributeChange)diff;
			String valueName = "null";
			if (attChange.getValue() != null) {
				valueName = attChange.getValue().toString();
			}
			String change = "";
			if (diff.getSource() == DifferenceSource.RIGHT) {
				change = "remotely ";
			}
			if (diff.getKind() == DifferenceKind.ADD) {
				change += "added to";
			} else if (diff.getKind() == DifferenceKind.DELETE) {
				change += "deleted from";
			} else if (diff.getKind() == DifferenceKind.CHANGE) {
				change += "changed from";
			} else {
				change += "moved from";
			}
			final String objectName;
			if (attChange.getMatch().getLeft() instanceof ENamedElement) {
				objectName = ((ENamedElement)attChange.getMatch().getLeft()).getName();
			} else if (attChange.getMatch().getRight() instanceof ENamedElement) {
				objectName = ((ENamedElement)attChange.getMatch().getRight()).getName();
			} else if (attChange.getMatch().getOrigin() instanceof ENamedElement) {
				objectName = ((ENamedElement)attChange.getMatch().getOrigin()).getName();
			} else {
				objectName = "";
			}
			stream.println("value " + valueName + " has been " + change + " attribute "
					+ attChange.getAttribute().getName() + " of object " + objectName);
		}
	}

	/**
	 * Prints the given {@link Match} on the given {@code stream}.
	 * 
	 * @param match
	 *            The match we are to print on {@code stream}.
	 * @param stream
	 *            The {@link PrintStream} on which we should print this difference.
	 * @see #printMatch(Comparison, PrintStream) A description on how we format the match.
	 */
	private static void printMatch(Match match, PrintStream stream) {
		String leftName = null;
		String rightName = null;
		String originName = null;

		final EObject left = match.getLeft();
		final EObject right = match.getRight();
		final EObject origin = match.getOrigin();

		// Ignore this match if it is not a Named element
		if (isNullOrNamedElement(left) && isNullOrNamedElement(right) && isNullOrNamedElement(origin)) {
			if (left != null && ((ENamedElement)left).getName() != null) {
				leftName = formatName((ENamedElement)left);
			} else {
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

			if (right != null && ((ENamedElement)right).getName() != null) {
				rightName = formatName((ENamedElement)right);
			} else {
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

			if (origin != null && ((ENamedElement)origin).getName() != null) {
				originName = formatName((ENamedElement)origin);
			} else {
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

			stream.println('|' + leftName + '|' + rightName + '|' + originName + '|');
		}

		for (Match submatch : match.getSubmatches()) {
			printMatch(submatch, stream);
		}
	}

	/**
	 * Formats the given header so that it spans {@value #COLUMN_LENGTH} characters, centered between white
	 * spaces.
	 * 
	 * @param header
	 *            The header we are to format.
	 * @return The formatted header.
	 */
	private static String formatHeader(String header) {
		int padding = (COLUMN_LENGTH - header.length()) / 2;
		char[] charsBefore = new char[padding];
		for (int i = 0; i < charsBefore.length; i++) {
			charsBefore[i] = ' ';
		}
		if ((header.length() & 1) == 1) {
			padding++;
		}
		final char[] charsAfter = new char[padding];
		for (int i = 0; i < charsAfter.length; i++) {
			charsAfter[i] = ' ';
		}
		return String.valueOf(charsBefore) + header + String.valueOf(charsAfter);
	}

	/**
	 * Formats the named of the given element by adding spaces before and after it so that it spans
	 * {@value #COLUMN_LENGTH} characters at most.
	 * 
	 * @param element
	 *            The element which name should be formatted.
	 * @return the formatted element's name.
	 */
	private static String formatName(ENamedElement element) {
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

		int missingChars = COLUMN_LENGTH - name.length() - charsBefore.length;
		final char[] spacesAfter = new char[Math.max(0, missingChars)];
		Arrays.fill(spacesAfter, ' ');

		return String.valueOf(charsBefore) + name + String.valueOf(spacesAfter);
	}

	/**
	 * Returns an "empty line" which will only show pipes for previous levels.
	 * 
	 * @param level
	 *            The level of nesting that we should make visible through pipes on this line.
	 * @return A line that displays only pipes for a tree's {@code level}, and only that.
	 */
	private static String getEmptyLine(int level) {
		char[] charsBefore = new char[1 + (level * 2)];
		charsBefore[0] = ' ';
		for (int i = 1; i < charsBefore.length; i = i + 2) {
			charsBefore[i] = '|';
			charsBefore[i + 1] = ' ';
		}

		int missingChars = COLUMN_LENGTH - charsBefore.length;
		final char[] spacesAfter = new char[Math.max(0, missingChars)];
		Arrays.fill(spacesAfter, ' ');

		return String.valueOf(charsBefore) + String.valueOf(spacesAfter);
	}

	/**
	 * Returns <code>true</code> if the given {@code object} is either <code>null</code> or an instance of
	 * {@link ENamedElement}.
	 * 
	 * @param object
	 *            The object we'll test here.
	 * @return <code>true</code> if the given {@code object} is either <code>null</code> or an instance of
	 *         {@link ENamedElement}.
	 */
	private static boolean isNullOrNamedElement(final Object object) {
		return object == null || object instanceof ENamedElement;
	}
}
