/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.contentmergeviewer;

import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public interface ICompareEditorPartListener {

	public void selectedTabChanged(int newIndex);

	/**
	 * @param event
	 */
	public void selectionChanged(SelectionChangedEvent event);

	/**
	 * 
	 */
	public void updateCenter();
}
