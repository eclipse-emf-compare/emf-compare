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
package org.eclipse.emf.compare.ide.ui.logical;

/** Used by the resolution process to determine the side of the revision to fetch. */
public enum DiffSide {
	/**
	 * Left side. In the case of IThreeWayDiff, this is the "after" state of the local change. It is the after
	 * state of an ITwoWayDiff.
	 */
	LEFT,

	/**
	 * Right side. In the case of IThreeWayDiff, this is the "after" state of the remote change. It is the
	 * before state of an ITwoWayDiff.
	 */
	RIGHT,

	/**
	 * Origin side. In the case of IThreeWayDiff, this is the "brefore" state of either local or remote
	 * change. No meaning for ITwoWayDiffs.
	 */
	ORIGIN;
}
