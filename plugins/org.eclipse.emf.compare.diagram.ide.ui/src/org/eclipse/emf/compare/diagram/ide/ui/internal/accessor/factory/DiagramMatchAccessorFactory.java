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
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.factory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramMatchAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AbstractAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;

/**
 * Factory for graphical matches accessors.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramMatchAccessorFactory extends AbstractAccessorFactory {

	/**
	 * Predicate on the namespace URI to create a graphical match accessor and so to be able to open a diagram
	 * comparison on the selection of a graphical match.
	 */
	private static final String NS_URI_PATTERN = "http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation";

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory#isFactoryFor(java.lang.Object)
	 */
	public boolean isFactoryFor(Object target) {
		// CHECKSTYLE:OFF
		return target instanceof Match
				&& (((Match)target).getLeft() != null
						&& ((Match)target).getLeft().eClass().getEPackage().getNsURI()
								.matches(NS_URI_PATTERN) || ((Match)target).getRight() != null
						&& ((Match)target).getRight().eClass().getEPackage().getNsURI().matches(
								NS_URI_PATTERN));
		// CHECKSTYLE:ON
	}

	/**
	 * It creates a graphical accessor from the given selected match and expected side.
	 * 
	 * @param match
	 *            The selected match.
	 * @param side
	 *            The side of the input to create.
	 * @return The accessor.
	 */
	private ITypedElement createAccessor(Match match, MergeViewerSide side) {
		return new DiagramMatchAccessorImpl(match, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory#createLeft(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return createAccessor((Match)target, MergeViewerSide.LEFT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory#createRight(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return createAccessor((Match)target, MergeViewerSide.RIGHT);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory#createAncestor(org.eclipse.emf.common.notify.AdapterFactory,
	 *      java.lang.Object)
	 */
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return createAccessor((Match)target, MergeViewerSide.ANCESTOR);
	}

}
