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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.action.Action;

/**
 * Abstract Action that manages a merge of a all non-conflicting difference in case of both sides of the
 * comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public abstract class AbstractMergeAllAction extends Action {

	/** The compare configuration object used to get the compare model. */
	private EMFCompareConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public AbstractMergeAllAction(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		copyAllDiffs();
	}

	/**
	 * Get the compare configuration object.
	 * 
	 * @return the configuration
	 */
	public EMFCompareConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Copy all non-conflicting differences.
	 */
	protected abstract void copyAllDiffs();
}
