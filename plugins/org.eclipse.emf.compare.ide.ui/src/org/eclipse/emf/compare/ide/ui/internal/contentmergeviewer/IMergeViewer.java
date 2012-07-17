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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Scrollable;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IMergeViewer<V extends Viewer, C extends Scrollable> {

	C getControl();

	V getViewer();

	int getLineHeight();

	int getViewportHeight();

	int getVerticalScrollOffset();

	public static enum MergeViewerSide {
		LEFT, RIGHT, ANCESTOR,
	}

	MergeViewerSide getSide();

	/**
	 * @param b
	 */
	void setEnabled(boolean b);
}
