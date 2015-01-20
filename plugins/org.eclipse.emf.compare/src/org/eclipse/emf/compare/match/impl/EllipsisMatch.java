/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.impl;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.spec.MatchSpec;

/**
 * The ellipsis match.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class EllipsisMatch extends MatchSpec {

	/** The child of this ellipsis match. */
	private Match child;

	/**
	 * Constructor.
	 * 
	 * @param child
	 *            the child of this ellipsis match.
	 */
	public EllipsisMatch(Match child) {
		super();
		this.child = child;
	}

	/**
	 * Get child.
	 * 
	 * @return the child
	 */
	public Match getChild() {
		return child;
	}

	/**
	 * Set child.
	 * 
	 * @param child
	 *            the child to set
	 */
	public void setChild(Match child) {
		this.child = child;
	}

	@Override
	public boolean equals(Object obj) {
		return child.equals(child);
	}

	@Override
	public int hashCode() {
		return child.hashCode();
	}
}
