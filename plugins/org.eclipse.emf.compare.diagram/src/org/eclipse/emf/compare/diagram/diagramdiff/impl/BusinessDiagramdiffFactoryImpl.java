/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff.impl;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.ecore.EPackage;

/**
 * Extension of {@link DiagramdiffFactoryImpl} to divert the creation of the extensions.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramdiffFactoryImpl extends DiagramdiffFactoryImpl {

	/**
	 * Constructor.
	 */
	public BusinessDiagramdiffFactoryImpl() {
		super();
	}

	/**
	 * Returns this specific factory.
	 * 
	 * @return The factory.
	 */
	public static DiagramdiffFactory init() {
		final DiagramdiffFactory theDiagramdiffFactory = (DiagramdiffFactory)EPackage.Registry.INSTANCE
				.getEFactory("http://www.eclipse.org/emf/compare/diff/diagram/1.0"); //$NON-NLS-1$
		if (theDiagramdiffFactory != null) {
			return theDiagramdiffFactory;
		}
		return new BusinessDiagramdiffFactoryImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramMoveNode()
	 */
	@Override
	public DiagramMoveNode createDiagramMoveNode() {
		return new BusinessDiagramMoveNodeImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramEdgeChange()
	 */
	@Override
	public DiagramEdgeChange createDiagramEdgeChange() {
		return new BusinessDiagramEdgeChangeImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramHideElement()
	 */
	@Override
	public DiagramHideElement createDiagramHideElement() {
		return new BusinessDiagramHideElementImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramShowElement()
	 */
	@Override
	public DiagramShowElement createDiagramShowElement() {
		return new BusinessDiagramShowElementImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramLabelChange()
	 */
	@Override
	public DiagramLabelChange createDiagramLabelChange() {
		return new BusinessDiagramLabelChangeImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramModelElementChangeLeftTarget()
	 */
	@Override
	public DiagramModelElementChangeLeftTarget createDiagramModelElementChangeLeftTarget() {
		return new BusinessDiagramModelElementChangeLeftTargetImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffFactoryImpl#createDiagramModelElementChangeRightTarget()
	 */
	@Override
	public DiagramModelElementChangeRightTarget createDiagramModelElementChangeRightTarget() {
		return new BusinessDiagramModelElementChangeRightTargetImpl();
	}

}
