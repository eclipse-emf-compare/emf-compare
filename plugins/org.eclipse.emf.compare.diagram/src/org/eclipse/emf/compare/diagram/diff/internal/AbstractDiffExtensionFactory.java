/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.internal;

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Factory to create diagram difference extensions.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	/**
	 * The DiffModel cross referencer.
	 */
	protected EcoreUtil.CrossReferencer fCrossReferencer;

	/**
	 * The match model.
	 */
	private MatchModel match;

	/**
	 * Enables use of the default constructor.
	 */
	public AbstractDiffExtensionFactory() {
		// Do nothing
	}

	/**
	 * Constructor.
	 * 
	 * @param crossReferencer
	 *            the DiffModel cross referencer.
	 * @param matchModel
	 *            The match model.
	 */
	public AbstractDiffExtensionFactory(EcoreUtil.CrossReferencer crossReferencer, MatchModel matchModel) {
		fCrossReferencer = crossReferencer;
		this.match = matchModel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.IDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public DiffElement getParentDiff(DiffElement input) {
		return (DiffElement)input.eContainer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public AbstractDiffExtension create(DiffElement input) {

		final AbstractDiffExtension diffExtension = init();

		if (diffExtension instanceof BusinessDiffExtension) {

			final BusinessDiffExtension bDiffExtension = (BusinessDiffExtension)diffExtension;

			bDiffExtension.init(input, getCrossReferencer(), getMatchModel());

		}

		return diffExtension;
	}

	/**
	 * Creates the related difference extension.
	 * 
	 * @return The related difference extension.
	 */
	protected abstract AbstractDiffExtension init();

	/**
	 * Get the cross referencer.
	 * 
	 * @return The cross referencer.
	 */
	protected final EcoreUtil.CrossReferencer getCrossReferencer() {
		return fCrossReferencer;
	}

	/**
	 * Get the match model.
	 * 
	 * @return The match model.
	 */
	protected MatchModel getMatchModel() {
		return match;
	}

}
