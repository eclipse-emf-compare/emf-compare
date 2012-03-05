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
package org.eclipse.emf.compare.diagram.diagramdiff.impl;

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil.Side;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extension of {@link DiagramModelElementChangeLeftTargetImpl}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramModelElementChangeLeftTargetImpl extends DiagramModelElementChangeLeftTargetImpl implements BusinessDiagramModelElementChangeLeftTarget {

	/**
	 * The match model.
	 */
	private MatchModel match;

	/**
	 * Constructor.
	 */
	public BusinessDiagramModelElementChangeLeftTargetImpl() {
		super();
	}

	/**
	 * Check if the difference {@link diff} is concerned by the creation of this kind of extension.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if {@link diff} is concerned.
	 */
	public static boolean isConcernedBy(DiffElement diff) {
		return diff instanceof ModelElementChangeLeftTarget
				&& DiffUtil.getProperty(diff, Side.ANY) instanceof View;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl#provideMerger()
	 */
	@Override
	public IMerger provideMerger() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#init(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer,
	 *      org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	public void init(DiffElement origin, CrossReferencer crossReferencer, MatchModel matchModel) {
		if (origin instanceof ModelElementChangeLeftTarget) {

			final ModelElementChangeLeftTarget diffOrigin = (ModelElementChangeLeftTarget)origin;

			getHideElements().add(diffOrigin);
			getRequires().addAll(getHideElements());
			setLeftElement(diffOrigin.getLeftElement());
			setRightParent(diffOrigin.getRightParent());

			setRemote(DiffUtil.isRemote(getHideElements()));

			setSemanticDiff(DiffUtil.getSemanticDiff(diffOrigin, ModelElementChange.class, crossReferencer));

			this.match = matchModel;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl#getText()
	 */
	@Override
	public String getText() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl#getImage()
	 */
	@Override
	public Object getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getElement()
	 */
	public EObject getElement() {
		return getLeftElement();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getRightResource()
	 */
	public Resource getRightResource() {
		if (getRightParent() == null) {
			return match.getRightRoots().get(0).eResource();
		}
		return getRightParent().eResource();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#getLeftResource()
	 */
	public Resource getLeftResource() {
		return getLeftElement().eResource();
	}

}
