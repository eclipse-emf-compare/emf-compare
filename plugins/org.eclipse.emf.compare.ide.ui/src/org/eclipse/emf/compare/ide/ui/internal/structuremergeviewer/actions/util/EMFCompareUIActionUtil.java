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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.util;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;

/**
 * Util class that provides utilities methods for IDE UI actions.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public final class EMFCompareUIActionUtil {

	/**
	 * Utility classes don't need a default constructor.
	 */
	private EMFCompareUIActionUtil() {
		// Hides default constructor.
	}

	/**
	 * Called by the framework to navigate to the next (or previous) difference. This will open the content
	 * viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 * @param configuration
	 *            the compare configuration object.
	 */
	public static void navigate(boolean next, CompareConfiguration configuration) {
		final ICompareNavigator navigator = configuration.getContainer().getNavigator();
		if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
			navigator.selectChange(next);
		}
	}
}
