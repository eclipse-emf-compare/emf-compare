/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link LabelContentViewer} in case of a comparison with only pseudo-conflicts.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class OnlyPseudoConflictsContentViewer extends LabelContentViewer {

	/**
	 * Creates a new viewer and its controls.
	 * 
	 * @param parent
	 *            the parent of the control of this viewer.
	 */
	public OnlyPseudoConflictsContentViewer(Composite parent) {
		super(parent, EMFCompareIDEUIMessages.getString("only.pseudo.conflicts.viewer.title"), //$NON-NLS-1$
				EMFCompareIDEUIMessages.getString("only.pseudo.conflicts.viewer.desc")); //$NON-NLS-1$
	}
}
