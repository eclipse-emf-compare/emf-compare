/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Define the menu to launch any execution to filter or group difference elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public abstract class AbstractOrderingMenu extends Action implements IMenuCreator {
	/** The menu manager. */
	protected MenuManager menuMgr = new MenuManager();

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of the menu.
	 */
	public AbstractOrderingMenu(String name) {
		super(name, SWT.DROP_DOWN);
		setMenuCreator(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		if (menuMgr.getMenu() == null) {
			menuMgr.createContextMenu(parent);
			doGetMenu();
		}
		return menuMgr.getMenu();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		return menuMgr.getMenu();
	}

	/**
	 * Build the menu in relation to the extensions.
	 */
	protected abstract void doGetMenu();

	/**
	 * Add the contribution of any action.
	 * 
	 * @param action
	 *            The action to add.
	 */
	protected void addContribution(IAction action) {
		final ActionContributionItem contribution = new ActionContributionItem(action);
		menuMgr.add(contribution);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		menuMgr.removeAll();
	}
}
