/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link LabelContentViewer} in case of a comparison with no selected item.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class NoSelectedItemContentViewer extends LabelContentViewer {

	/**
	 * Creates a new viewer and its controls.
	 * 
	 * @param parent
	 *            the parent of the control of this viewer.
	 */
	public NoSelectedItemContentViewer(Composite parent) {
		super(parent, EMFCompareIDEUIMessages.getString("no.selected.item.viewer.title"), //$NON-NLS-1$
				EMFCompareIDEUIMessages.getString("no.selected.item.viewer.desc")); //$NON-NLS-1$
	}
}
