/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * This menu will display actions that will allow the user to group differences together.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class GroupActionMenu extends Action implements IMenuCreator {
	/** The difference grouper that will be affected by this menu's actions. */
	private final DifferenceGrouper differenceGrouper;

	/** Menu Manager that will contain our menu. */
	private MenuManager menuManager;

	/**
	 * Constructs our grouping menu.
	 * 
	 * @param tree
	 *            The tree viewer which will be used to display our groups.
	 * @param comparison
	 *            The comparison which differences are to be split into groups.
	 */
	public GroupActionMenu(DifferenceGrouper differenceGrouper) {
		super("", IAction.AS_DROP_DOWN_MENU); //$NON-NLS-1$
		this.menuManager = new MenuManager();
		this.differenceGrouper = differenceGrouper;
		setToolTipText("Groups");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/group.gif")); //$NON-NLS-1$
		setMenuCreator(this);
		createActions(menuManager);
	}

	/**
	 * Create the grouping action in the given menu.
	 * 
	 * @param menu
	 */
	public void createActions(MenuManager menu) {
		// TODO make this extensible. we need to allow clients to add new groups to this list.
		final IAction defaultAction = new GroupAction("Default", differenceGrouper, null);
		defaultAction.setChecked(true);
		menu.add(defaultAction);
		menu.add(new GroupAction("By Kind", differenceGrouper, new KindGroupProvider()));
		menu.add(new GroupAction("By Metaclass", differenceGrouper, new MetamodelGroupProvider()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IMenuCreator#dispose()
	 */
	public void dispose() {
		menuManager.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IMenuCreator#getMenu(Control)
	 */
	public Menu getMenu(Control parent) {
		return menuManager.createContextMenu(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IMenuCreator#getMenu(Menu)
	 */
	public Menu getMenu(Menu parent) {
		return menuManager.getMenu();
	}
}
