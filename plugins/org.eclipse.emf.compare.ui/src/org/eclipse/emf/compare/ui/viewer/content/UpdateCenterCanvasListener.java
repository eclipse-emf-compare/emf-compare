/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content;

import org.eclipse.emf.compare.ui.viewer.OrderingListener;

/**
 * Define a listener of ordering changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class UpdateCenterCanvasListener implements OrderingListener {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.OrderingListener#notifyChanged(int, java.lang.Object)
	 */
	public void notifyChanged(int event, Object descriptor) {
		final ParameterizedContentMergeViewer viewer = ParameterizedContentMergeViewer.getInstance();
		if (viewer != null) {
			viewer.update();
		}
	}
}
