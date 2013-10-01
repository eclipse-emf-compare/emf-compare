/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages the click on the dropdown menu of the toolbar of the structure merge viewer.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DropDownMergeMenuAction extends Action implements IMenuCreator {

	/** The compare configuration object. */
	private CompareConfiguration configuration;

	/** The menu associated with this action. */
	private Menu fMenu;

	/** The left to right menu item. */
	private Action leftToRightItem;

	/** The right to left menu item. */
	private Action rightToLeftItem;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public DropDownMergeMenuAction(CompareConfiguration configuration) {
		this.configuration = configuration;
		setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$

		leftToRightItem = new DropDownLeftToRightAction(configuration) {
			@Override
			public void run() {
				super.run();
				updateMenu();
			}
		};

		rightToLeftItem = new DropDownRightToLeftAction(configuration) {
			@Override
			public void run() {
				super.run();
				updateMenu();
			}
		};

		setMenuCreator(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		Boolean mergeWay = (Boolean)configuration.getProperty(EMFCompareConstants.MERGE_WAY);
		if (mergeWay == null || mergeWay.booleanValue()) {
			configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(false));
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
		} else {
			configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(true));
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
			fMenu = null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		if (fMenu != null) {
			fMenu.dispose();
		}

		fMenu = new Menu(parent);
		addActionToMenu(fMenu, leftToRightItem);
		addActionToMenu(fMenu, rightToLeftItem);

		return fMenu;
	}

	/**
	 * Add action to the given menu.
	 * 
	 * @param parent
	 *            the given menu.
	 * @param action
	 *            the given action.
	 */
	protected void addActionToMenu(Menu parent, Action action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	/**
	 * Update the icon and tooltip of the dropdown menu.
	 */
	protected void updateMenu() {
		Boolean mergeWay = (Boolean)configuration.getProperty(EMFCompareConstants.MERGE_WAY);
		if (mergeWay == null || mergeWay.booleanValue()) {
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
		} else {
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
		}
	}
}
