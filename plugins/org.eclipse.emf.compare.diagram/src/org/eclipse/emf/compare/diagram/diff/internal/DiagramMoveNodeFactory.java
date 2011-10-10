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
package org.eclipse.emf.compare.diagram.diff.internal;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.compare.diagram.diagramdiff.impl.BusinessDiagramMoveNodeImpl;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;

/**
 * The factory for the DiagramMoveNode extensions.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class DiagramMoveNodeFactory extends AbstractDiffExtensionFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.compare.diff.metamodel.DiffGroup)
	 */
	public boolean handles(DiffElement input, DiffGroup root) {
		return BusinessDiagramMoveNodeImpl.isConcernedBy(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.AbstractDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	@Override
	public DiffElement getParentDiff(DiffElement input) {
		return (DiffElement)input.eContainer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.AbstractDiffExtensionFactory#init()
	 */
	@Override
	protected AbstractDiffExtension init() {
		return DiagramdiffFactory.eINSTANCE.createDiagramMoveNode();
	}

}
