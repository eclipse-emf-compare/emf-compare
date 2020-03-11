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

import org.eclipse.emf.compare.Diff;

/**
 * Interface for the management of decorators.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IDecoratorManager {

	/**
	 * It hides the revealed decorators.
	 */
	void hideAll();

	/**
	 * From a given difference, it hides the related decorators.
	 * 
	 * @param difference
	 *            The difference.
	 */
	void hideDecorators(Diff difference);

	/**
	 * From a given difference, it reveals the related decorators.
	 * 
	 * @param difference
	 *            The difference.
	 */
	void revealDecorators(Diff difference);

	/**
	 * From a given difference, it removes the related decorators from cash.
	 * 
	 * @param difference
	 *            The difference.
	 */
	void removeDecorators(Diff difference);

	/**
	 * It removes all the displayed decorators from cache.
	 */
	void removeAll();

}
