/*******************************************************************************
 * Copyright (c) 2014, 2018 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 514079
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link LabelContentViewer} in case of a comparison with no differences.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class WaitContentViewer extends LabelContentViewer {

	/**
	 * Creates a new viewer and its controls.
	 * 
	 * @param parent
	 *            the parent of the control of this viewer.
	 */
	public WaitContentViewer(Composite parent, EMFCompareConfiguration configuration) {
		super(parent, EMFCompareIDEUIMessages.getString("wait.viewer.title"), //$NON-NLS-1$
				EMFCompareIDEUIMessages.getString("wait.viewer.desc"), configuration); //$NON-NLS-1$
	}
}
