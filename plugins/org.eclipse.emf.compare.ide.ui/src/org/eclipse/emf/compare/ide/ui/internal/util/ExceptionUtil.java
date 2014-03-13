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

import com.google.common.base.Throwables;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This provides access to commonly used functions which need to handle exceptions.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class ExceptionUtil {

	/**
	 * Log exception and close editor if asked.
	 * 
	 * @param e
	 *            the exception to log.
	 * @param configuration
	 *            the compare configuration object.
	 * @param closeEditor
	 *            true if you want to close editor.
	 */
	public static void handleException(final Exception e, final CompareConfiguration configuration,
			final boolean closeEditor) {
		if (closeEditor) {
			final IWorkbenchPart workbenchPart = configuration.getContainer().getWorkbenchPart();
			if (workbenchPart != null) {
				SWTUtil.safeAsyncExec(new Runnable() {
					public void run() {
						workbenchPart.getSite().getPage().closeEditor((IEditorPart)workbenchPart, false);
					}
				});
			}
		}
		EMFCompareIDEUIPlugin.getDefault().log(e);
		Throwables.propagate(e);
	}
}
