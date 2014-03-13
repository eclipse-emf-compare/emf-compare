/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import org.eclipse.emf.common.notify.Notifier;

/**
 * This represents a "use case" as EMF Compare understands it, i.e : two or three notifiers which are to be
 * compared and merged.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class NotifierTuple {
	/** "Left" notifier of this comparison. */
	private Notifier left;

	/** "Right" notifier of this comparison. */
	private Notifier right;

	/** common ancestor of {@link #left} and {@link #right}. */
	private Notifier origin;

	/**
	 * Constructs a tuple given its content.
	 * 
	 * @param left
	 *            The left Notifier.
	 * @param right
	 *            The right Notifier.
	 * @param origin
	 *            The Notifier that is to be considered as the common ancestor of <code>left</code> and
	 *            <code>right</code>.
	 */
	public NotifierTuple(Notifier left, Notifier right, Notifier origin) {
		this.left = left;
		this.right = right;
		this.origin = origin;
	}

	/**
	 * Returns the left Notifier of this comparison.
	 * 
	 * @return The left Notifier of this comparison.
	 */
	public Notifier getLeft() {
		return left;
	}

	/**
	 * Returns the right Notifier of this comparison.
	 * 
	 * @return The right Notifier of this comparison.
	 */
	public Notifier getRight() {
		return right;
	}

	/**
	 * Returns the origin Notifier of this comparison.
	 * 
	 * @return The origin Notifier of this comparison.
	 */
	public Notifier getOrigin() {
		return origin;
	}
}
