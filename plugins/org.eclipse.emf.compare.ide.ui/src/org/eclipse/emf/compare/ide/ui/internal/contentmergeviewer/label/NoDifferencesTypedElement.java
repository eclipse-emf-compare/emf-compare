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

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.swt.graphics.Image;

/**
 * A specific {@link ITypedElement} to use with
 * {@link org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoDifferencesCompareInput}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class NoDifferencesTypedElement implements ITypedElement {

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return EMFCompareIDEUIMessages.getString("no.differences.viewer.title"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return "org.eclipse.emf.compare.rcp.ui.eNoDiff"; //$NON-NLS-1$
	}

}
