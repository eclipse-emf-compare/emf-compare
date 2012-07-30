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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
final class DiffInsertionPoint {

	private final Diff fDiff;

	/**
	 * 
	 */
	public DiffInsertionPoint(Diff diff) {
		this.fDiff = diff;
	}

	/**
	 * @return
	 */
	public Diff getDiff() {
		return fDiff;
	}

	public Match getMatch() {
		final Match ret;
		if (fDiff instanceof ReferenceChange) {
			ret = fDiff.getMatch().getComparison().getMatch(((ReferenceChange)fDiff).getValue());
		} else if (fDiff instanceof AttributeChange) {
			ret = fDiff.getMatch();
		} else {
			ret = null;
		}
		return ret;
	}

	/**
	 * Returns an empty String to avoid having to override {@link ILabelProvider#getText(Object)} to do not
	 * display anything for insertion point. {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""; //$NON-NLS-1$
	}

}
