/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.papyrus.infra.core.resource.sasheditor.DiModel;
import org.eclipse.papyrus.infra.core.resource.sasheditor.SashModel;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

/**
 * Post-processor to modify existing match resources created.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 2.4
 */
public class PapyrusPostProcessor implements IPostProcessor {
	/** List of file extensions managed by this post-processor. */
	public static final List<String> FILE_EXTENSIONS = Arrays.asList(UmlModel.UML_FILE_EXTENSION,
			DiModel.DI_FILE_EXTENSION, NotationModel.NOTATION_FILE_EXTENSION,
			SashModel.SASH_MODEL_FILE_EXTENSION);

	/**
	 * {@inheritDoc} Merges MatchResource instances that correspond to renamed units into a single
	 * MatchResource for 3-way comparisons.
	 * <ul>
	 * <li>For example :</li>
	 * <li>Before refactoring</li>
	 * <li>A.uml <-> B.uml</li>
	 * <li>A.notation <-></li>
	 * <li><-> B.notation</li>
	 * <li>After refactoring</li>
	 * <li>A.uml <-> B.uml</li>
	 * <li>A.notation <-> B.notation</li>
	 * </ul>
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		new AddEquivalencesBetweenPapyrusRenames(comparison, monitor).run();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		// Nothing to do here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
		// Nothing to do here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {
		// Nothing to do here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		// Nothing to do here.
	}
}
