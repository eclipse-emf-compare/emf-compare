/*******************************************************************************
 * Copyright (c) 2011 Obeo.
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

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.ModelCompareInput;

/**
 * Class used to annotate view models ( gmf models ).
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public final class NotationDiffCreator {

	/** The visitor used by this creator. */
	private NotationDiffVisitor visitor;

	/** The input used by this creator. */
	private ModelCompareInput compareInput;

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
			if (!(input instanceof ModelCompareInput)) {
				EMFComparePlugin.log("input must be an instance of ModelCompareInput", true);
				return;
			}
			this.compareInput = (ModelCompareInput)input;
		}
	}

	/**
	 * Annotate models.
	 * @param side Side to annotate.
	 */
	public void addEAnnotations(MatchSide side) {
		final List<DiffElement> diffs = compareInput.getDiffAsList();
		visitor.addEannotations(diffs, side);
	}

	/**
	 * De-annotate models.
	 * @param side Side to annotate.
	 */
	public void removeEAnnotations(MatchSide side) {
		final List<DiffElement> diffs = compareInput.getDiffAsList();
		visitor.removeEAnnotations(diffs, side);
	}
}
