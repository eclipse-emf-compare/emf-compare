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

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange;
import org.eclipse.emf.compare.diagram.merge.DiagramLabelChangeMerger;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * Extension of {@link DiagramLabelChangeImpl}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramLabelChangeImpl extends DiagramLabelChangeImpl implements BusinessDiagramLabelChange {

	/**
	 * Left label.
	 */
	private String leftLabel = ""; //$NON-NLS-1$

	/**
	 * Right label.
	 */
	private String rightLabel = ""; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public BusinessDiagramLabelChangeImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl#getText()
	 */
	@Override
	public String getText() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl#getImage()
	 */
	@Override
	public Object getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl#provideMerger()
	 */
	@Override
	public IMerger provideMerger() {
		return new DiagramLabelChangeMerger();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension#init(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer,
	 *      org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	public void init(DiffElement origin, CrossReferencer crossReferencer, MatchModel match) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange#setLeftLabel(java.lang.String)
	 */
	public void setLeftLabel(String label) {
		leftLabel = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange#setRightLabel(java.lang.String)
	 */
	public void setRightLabel(String label) {
		rightLabel = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange#getLeftLabel()
	 */
	public String getLeftLabel() {
		return leftLabel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange#getRightLabel()
	 */
	public String getRightLabel() {
		return rightLabel;
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
