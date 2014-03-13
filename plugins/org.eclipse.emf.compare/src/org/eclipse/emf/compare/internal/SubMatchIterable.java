/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import java.util.Iterator;

import org.eclipse.emf.compare.Match;

/**
 * A custom Iterable that will iterate over the Match->submatch tree of a given Match.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class SubMatchIterable implements Iterable<Match> {
	/** The root of the Match->subMatch tree for which this iterable has been constructed. */
	private final Match match;

	/**
	 * Constructs an iterable given the root of its tree.
	 * 
	 * @param match
	 *            Starting match of the tree we'll iterate over.
	 */
	public SubMatchIterable(Match match) {
		this.match = match;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Match> iterator() {
		return new SubMatchIterator(match);
	}
}
