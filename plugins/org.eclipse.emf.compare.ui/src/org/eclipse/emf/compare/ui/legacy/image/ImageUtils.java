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
package org.eclipse.emf.compare.ui.legacy.image;

import org.eclipse.emf.compare.ui.legacy.CompareEngineUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;

public enum ImageUtils {
	SHOW_PSEUDO_CONFLICTS("showPseudoConflicts"), SAVE_DIFF("save"), EDITOR_ICON(
			"editorIcon"), DIFFERENCES_ICON("differencesIcon");
	private final String path;

	ImageUtils(final String path) {
		this.path = path;
	}

	private static final String prefix = "icons/full/";

	private static final String suffix = ".gif";

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromURL(CompareEngineUIPlugin.getDefault()
				.getBundle().getResource(prefix + this.path + suffix));
	}
}
