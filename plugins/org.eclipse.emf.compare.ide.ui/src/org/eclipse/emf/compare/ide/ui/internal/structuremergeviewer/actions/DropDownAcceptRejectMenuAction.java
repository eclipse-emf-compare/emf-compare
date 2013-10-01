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
 * Action that manages the dropdown menu that allows to show the consequences of an accept or a reject.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class DropDownAcceptRejectMenuAction extends Action implements IMenuCreator {

	/** The compare configuration object used to get the compare model. */
	private CompareConfiguration configuration;

	/** The menu associated with this action. */
	private Menu fMenu;

	/** The accept menu item. */
	private Action acceptItem;

	/** The reject menu item. */
	private Action rejectItem;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public DropDownAcceptRejectMenuAction(final CompareConfiguration configuration) {
		this.configuration = configuration;
		setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/accept.gif")); //$NON-NLS-1$

		acceptItem = new DropDownAcceptAction(configuration) {
			@Override
			public void run() {
				super.run();
				updateMenu();
			}
		};

		rejectItem = new DropDownRejectAction(configuration) {
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
		boolean rightEditableOnly = !configuration.isLeftEditable() && configuration.isRightEditable();
		boolean leftEditableOnly = configuration.isLeftEditable() && !configuration.isRightEditable();
		if (mergeWay == null || mergeWay.booleanValue()) {
			configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(false));
			if (leftEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.reject.tooltip")); //$NON-NLS-1$
			} else if (rightEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.tooltip")); //$NON-NLS-1$
			}
		} else {
			configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(true));
			if (leftEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.tooltip")); //$NON-NLS-1$
			} else if (rightEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.reject.tooltip")); //$NON-NLS-1$
			}
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
		addActionToMenu(fMenu, acceptItem);
		addActionToMenu(fMenu, rejectItem);

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
		boolean rightEditableOnly = !configuration.isLeftEditable() && configuration.isRightEditable();
		boolean leftEditableOnly = configuration.isLeftEditable() && !configuration.isRightEditable();
		if (mergeWay == null || mergeWay.booleanValue()) {
			if (leftEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.tooltip")); //$NON-NLS-1$
			} else if (rightEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.reject.tooltip")); //$NON-NLS-1$
			}
		} else {
			if (leftEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.reject.tooltip")); //$NON-NLS-1$
			} else if (rightEditableOnly) {
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept.gif")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.tooltip")); //$NON-NLS-1$
			}
		}
	}
}
