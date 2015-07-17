/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;

/**
 * Factory for the tooltip managers.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public final class TooltipFactory {

	/**
	 * The AddTooltipManager instance.
	 */
	private static AddTooltipManager addTooltipManager;

	/**
	 * The ChangeTooltipManager instance.
	 */
	private static ChangeTooltipManager changeTooltipManager;

	/**
	 * The DeleteTooltipManager instance.
	 */
	private static DeleteTooltipManager deleteTooltipManager;

	/**
	 * The MoveTooltipManager instance.
	 */
	private static MoveTooltipManager moveTooltipManager;

	/**
	 * The constructor.
	 */
	private TooltipFactory() {
		// Prevent instanciation.
	}

	/**
	 * Create a new instance of the right type or return the existing one for the given diff.
	 * 
	 * @param adapterFactory
	 *            The given adapter factory
	 * @param diff
	 *            The diff which which the tooltip will be computed
	 * @return an instance of the good AbstractTooltipManager
	 */
	public static AbstractTooltipManager getTooltipManager(AdapterFactory adapterFactory, Diff diff) {
		AbstractTooltipManager manager = null;

		switch (diff.getKind()) {
			case ADD:
				if (addTooltipManager == null) {
					addTooltipManager = new AddTooltipManager(adapterFactory);
				}
				manager = addTooltipManager;
				break;
			case CHANGE:
				if (changeTooltipManager == null) {
					changeTooltipManager = new ChangeTooltipManager(adapterFactory);
				}
				manager = changeTooltipManager;
				break;
			case DELETE:
				if (deleteTooltipManager == null) {
					deleteTooltipManager = new DeleteTooltipManager(adapterFactory);
				}
				manager = deleteTooltipManager;
				break;
			case MOVE:
				if (moveTooltipManager == null) {
					moveTooltipManager = new MoveTooltipManager(adapterFactory);
				}
				manager = moveTooltipManager;
				break;
			default:
				// Do nothing
		}

		return manager;
	}

}
