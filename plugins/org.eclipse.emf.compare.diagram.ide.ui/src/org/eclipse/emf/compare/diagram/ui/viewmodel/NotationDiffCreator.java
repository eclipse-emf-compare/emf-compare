/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - rework on generic gmf comparison
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ui.viewmodel;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ComparisonNode;

/**
 * Class used to annotate view models ( gmf models ).
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public final class NotationDiffCreator {

	/** The visitor used by this creator. */
	private NotationDiffVisitor visitor;

	/** The input used by this creator. */
	private ComparisonNode compareInput;

	/**
	 * Constructor.
	 */
	public NotationDiffCreator() {
		visitor = new NotationDiffVisitor();
	}

	/**
	 * Sets the compareInput.
	 * 
	 * @param input
	 *            the compare input. contains the diff models
	 */
	public void setInput(Object input) {
		if (input != null) {
			if (!(input instanceof ComparisonNode)) {
				// FIXME externalize this
				//EMFComparePlugin.log("input must be an instance of ModelCompareInput", true); //$NON-NLS-1$
				return;
			}
			this.compareInput = (ComparisonNode)input;
		}
	}

	/**
	 * Disposes of this creator, nulling out the input reference.
	 */
	public void dispose() {
		visitor = null;
		compareInput = null;
	}

	/**
	 * Annotate models.
	 * 
	 * @param side
	 *            Side to annotate.
	 */
	public void addEAnnotations(DifferenceSource side) {
		final List<Diff> diffs = compareInput.getTarget().getDifferences();
		visitor.addEannotations(diffs, side);
	}

	/**
	 * De-annotate models.
	 * 
	 * @param side
	 *            Side to annotate.
	 */
	public void removeEAnnotations(DifferenceSource side) {
		final List<Diff> diffs = compareInput.getTarget().getDifferences();
		visitor.removeEAnnotations(diffs, side);
	}
}
