/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * Listens for events sent by
 * {@link org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer ModelContentMergeViewers}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface ICompareEditorPartListener {
	/**
	 * Sent when the viewer changes tabs.
	 * 
	 * @param newIndex
	 *            Index of the newly selected tab.
	 */
	void selectedTabChanged(int newIndex);

	/**
	 * Sent when the selection changes in one of the displayed tabs.
	 * 
	 * @param event
	 *            Object describing the selection changed event.
	 */
	void selectionChanged(SelectionChangedEvent event);

	/**
	 * Notifies that the center part of the viewer needs to be updated.
	 */
	void updateCenter();
}
