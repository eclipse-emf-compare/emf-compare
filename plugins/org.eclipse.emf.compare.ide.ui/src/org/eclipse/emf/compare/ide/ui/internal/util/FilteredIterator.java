/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Andreas Mayer - initial implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.util;

import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;

import org.eclipse.emf.common.util.TreeIterator;

/**
 * A wrapper for a {@link TreeIterator} that only returns nodes that satisfy a predicate and skips all others
 * and their descendants.
 * 
 * @param <E>
 *            the type of elements returned by this iterator
 */
public class FilteredIterator<E> extends AbstractIterator<E> {
	private TreeIterator<E> delegate;

	private Predicate<? super E> predicate;

	/**
	 * Constructs a new iterator.
	 * 
	 * @param delegate
	 *            a tree iterator
	 * @param predicate
	 *            the predicate to satisfy; all nodes (including their descendants) for which the predicate
	 *            yields {@code false} are skipped
	 */
	public FilteredIterator(TreeIterator<E> delegate, Predicate<? super E> predicate) {
		this.delegate = delegate;
		this.predicate = predicate;
	}

	@Override
	protected E computeNext() {
		while (delegate.hasNext()) {
			E next = delegate.next();
			if (predicate.apply(next)) {
				return next;
			}

			delegate.prune();
		}

		endOfData();
		return null;
	}
}
