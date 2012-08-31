/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.mergeviewer;

import org.eclipse.emf.compare.Diff;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class InsertionPoint extends BasicMergeViewerItemImpl {

	/**
	 * 
	 */
	public InsertionPoint(Diff diff, Object left, Object right, Object ancestor) {
		super(diff, left, right, ancestor);
	}

	/**
	 * Returns an empty String to avoid having to override
	 * {@link org.eclipse.jface.viewers.ILabelProvider#getText(Object)} to do not display anything for
	 * insertion point. {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""; //$NON-NLS-1$
	}

}
