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

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil.Side;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extension of {@link DiagramHideElementImpl}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramHideElementImpl extends DiagramHideElementImpl implements BusinessDiagramShowHideElement {

	/**
	 * The hidden difference.
	 */
	private UpdateAttribute updateAttributeVisibleDiff;

	/**
	 * The left view.
	 */
	private View leftView;

	/**
	 * The right view.
	 */
	private View rightView;

	/**
	 * Constructor.
	 */
	public BusinessDiagramHideElementImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#init(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer,
	 *      org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	public void init(DiffElement origin, CrossReferencer crossReferencer, MatchModel match) {
		if (origin instanceof UpdateAttribute) {
			initHiddenDiffElements((UpdateAttribute)origin);
			getRequires().addAll(getHideElements());
			initLeftElement((UpdateAttribute)origin);
			initRightElement((UpdateAttribute)origin);
			setRemote(DiffUtil.isRemote(getHideElements()));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement#getUpdateAttributeVisible()
	 */
	public UpdateAttribute getUpdateAttributeVisible() {
		return updateAttributeVisibleDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement#getLeftView()
	 */
	public View getLeftView() {
		return leftView;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement#getRightView()
	 */
	public View getRightView() {
		return rightView;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl#getText()
	 */
	@Override
	public String getText() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl#getImage()
	 */
	@Override
	public Object getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl#provideMerger()
	 */
	@Override
	public IMerger provideMerger() {
		return null;
	}

	/**
	 * Set the hidden difference.
	 * 
	 * @param diff
	 *            The difference.
	 */
	protected void initHiddenDiffElements(UpdateAttribute diff) {
		updateAttributeVisibleDiff = diff;
		getHideElements().add(diff);
	}

	/**
	 * Set the left view.
	 * 
	 * @param diff
	 *            The difference.
	 */
	protected void initLeftElement(UpdateAttribute diff) {
		final EObject obj = diff.getLeftElement();
		if (obj instanceof View) {
			leftView = (View)obj;
			setLeftElement(diff);
		}
	}

	/**
	 * Set the right view.
	 * 
	 * @param diff
	 *            The difference.
	 */
	protected void initRightElement(UpdateAttribute diff) {
		final EObject obj = diff.getRightElement();
		if (obj instanceof View) {
			rightView = (View)obj;
			setRightElement(diff);
		}
	}

	/**
	 * Check if the difference {@link diff} is concerned by the creation of this kind of extension.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if {@link diff} is concerned.
	 */
	public static boolean isConcernedBy(DiffElement diff) {
		return diff instanceof UpdateAttribute && isHideElementComparison((UpdateAttribute)diff);
	}

	/**
	 * Check if the difference {@link diff} is a hide element comparison.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if it concerns the change of an anchor comparison.
	 */
	private static boolean isHideElementComparison(UpdateAttribute diff) {
		if (diff.getAttribute().equals(NotationPackage.eINSTANCE.getView_Visible())) {
			final View leftView = DiffUtil.getElement(diff, Side.LEFT, View.class);
			final boolean cond1 = leftView != null && !leftView.isVisible() && !diff.isRemote();
			final boolean cond2 = leftView != null && leftView.isVisible() && diff.isRemote();
			return cond1 || cond2;
		}
		return false;
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
		return getRightElement().eResource();
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
