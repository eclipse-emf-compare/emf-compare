/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.dmp;

import java.util.LinkedList;

/**
 * Extends {@link diff_match_patch} with a line-based diffing capability.
 * <p>
 * This extension is adopted from the descriptions and comments at
 * https://code.google.com/p/google-diff-match-patch/wiki/LineOrWordDiffs
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class LineBasedDiff extends diff_match_patch {

	/**
	 * Computes differences between {@code text1} and {@code text2} based on lines only (as opposed to
	 * character or word-based diffing).
	 * 
	 * @param text1
	 *            Version 1 of a text.
	 * @param text2
	 *            Version 2 of a text.
	 * @return The list of differences.
	 */
	public LinkedList<Diff> computeLineBasedDiff(String text1, String text2) {
		final LinesToCharsResult lines = diff_linesToChars(text1, text2);
		final LinkedList<Diff> diffs = diff_main(lines.chars1, lines.chars2, false);
		diff_cleanupSemantic(diffs);
		diff_charsToLines(diffs, lines.lineArray);
		return diffs;
	}

}
