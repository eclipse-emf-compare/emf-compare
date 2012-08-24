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

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IInputSelectionProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IMergeViewer<C extends Composite> extends IInputSelectionProvider {

	C getControl();

	void setInput(Object input);

	MergeViewerSide getSide();

	void setContentProvider(IContentProvider contentProvider);

	void setLabelProvider(ILabelProvider labelProvider);

	public static enum MergeViewerSide {
		LEFT, RIGHT, ANCESTOR;

		public MergeViewerSide opposite() {
			switch (this) {
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;
				case ANCESTOR:
					return ANCESTOR;
				default:
					throw new IllegalStateException(); // happy compiler :)
			}
		}
	}

}
