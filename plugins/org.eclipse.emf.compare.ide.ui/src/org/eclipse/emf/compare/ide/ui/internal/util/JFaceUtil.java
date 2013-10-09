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
package org.eclipse.emf.compare.ide.ui.internal.util;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class JFaceUtil {

	/**
	 * All element filter tests must go through this method.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param object
	 *            the object to filter
	 * @param parent
	 *            the parent
	 * @return true if the element is filtered
	 */
	public static boolean isFiltered(StructuredViewer viewer, Object object, Object parent) {
		for (ViewerFilter filter : viewer.getFilters()) {
			if (!filter.select(viewer, parent, object)) {
				return true;
			}
		}
		return false;
	}
}
