/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;

/**
 * Decorator manager to create, hide or reveal all decorator figures related to graphical changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DecoratorsManager implements IDecoratorManager {
	/** Phantoms manager. */
	private IDecoratorManager fPhantomManager;

	/** Markers manager. */
	private IDecoratorManager fMarkerManager;

	/**
	 * Constructor.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration of the viewer.
	 * @param left
	 *            The left area of the viewer.
	 * @param right
	 *            The right area of the viewer.
	 * @param ancestor
	 *            The ancestor area of the viewer.
	 * @param color
	 *            The color of the difference.
	 */
	public DecoratorsManager(EMFCompareConfiguration compareConfiguration, DiagramMergeViewer left,
			DiagramMergeViewer right, DiagramMergeViewer ancestor, ICompareColor color) {
		fPhantomManager = new PhantomManager(compareConfiguration, left, right, ancestor, color);
		fMarkerManager = new MarkerManager(compareConfiguration, left, right, ancestor, color);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#hideDecorators(org.eclipse.emf.compare.Diff)
	 */
	public void hideDecorators(Diff difference) {
		fMarkerManager.hideDecorators(difference);
		fPhantomManager.hideDecorators(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#revealDecorators(org.eclipse.emf.compare.Diff)
	 */
	public void revealDecorators(Diff difference) {
		fMarkerManager.revealDecorators(difference);
		fPhantomManager.revealDecorators(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#hideAll()
	 */
	public void hideAll() {
		fMarkerManager.hideAll();
		fPhantomManager.hideAll();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#removeDecorators(org.eclipse.emf.compare.Diff)
	 */
	public void removeDecorators(Diff difference) {
		fMarkerManager.removeDecorators(difference);
		fPhantomManager.removeDecorators(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#removeAll()
	 */
	public void removeAll() {
		fMarkerManager.removeAll();
		fPhantomManager.removeAll();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#getAllDecorators()
	 */
	public Collection<AbstractDecorator> getAllDecorators() {
		Collection<AbstractDecorator> decorators = new ArrayList<AbstractDecorator>();
		decorators.addAll(fMarkerManager.getAllDecorators());
		decorators.addAll(fPhantomManager.getAllDecorators());
		return decorators;
	}

}
