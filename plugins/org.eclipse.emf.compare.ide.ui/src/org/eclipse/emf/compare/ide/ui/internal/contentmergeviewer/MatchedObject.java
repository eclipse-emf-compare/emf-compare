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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchedObject extends BasicMergeViewerItemImpl {

	/**
	 * @param left
	 * @param right
	 * @param ancestor
	 */
	public MatchedObject(Diff diff, Object left, Object right, Object ancestor) {
		super(diff, left, right, ancestor);
	}

	public MatchedObject(Object left, Object right, Object ancestor) {
		this(null, left, right, ancestor);
	}

	public MatchedObject(Diff diff, Match match) {
		this(diff, match.getLeft(), match.getRight(), match.getOrigin());
	}

	public MatchedObject(Match match) {
		this(null, match.getLeft(), match.getRight(), match.getOrigin());
	}

}
