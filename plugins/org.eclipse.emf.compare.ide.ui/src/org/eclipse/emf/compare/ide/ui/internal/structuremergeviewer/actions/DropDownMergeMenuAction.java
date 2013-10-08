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

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener;
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
	private final EMFCompareConfiguration configuration;

	/** The menu associated with this action. */
	private Menu fMenu;

	/** The left to right menu item. */
	private final Action leftToRightItem;

	/** The right to left menu item. */
	private final Action rightToLeftItem;

	private final EMFCompareConfigurationChangeListener changeListener;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public DropDownMergeMenuAction(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
		setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$

		changeListener = new EMFCompareConfigurationChangeListener() {
			@Override
			public void previewMergeModeChange(Boolean oldValue, Boolean newValue) {
				updateMenu(newValue.booleanValue());
			}
		};
		configuration.addChangeListener(changeListener);

		leftToRightItem = new DropDownAction(configuration, true);
		rightToLeftItem = new DropDownAction(configuration, false);
		updateMenu(configuration.getPreviewMergeMode());

		setMenuCreator(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		boolean mergeWay = configuration.getPreviewMergeMode();
		configuration.setPreviewMergeMode(!mergeWay);
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
		configuration.removeChangeListener(changeListener);
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
	protected void updateMenu(boolean mergeWay) {
		if (mergeWay) {
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
			setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.left.to.right.tooltip")); //$NON-NLS-1$
		} else {
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
					"icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
			setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.right.to.left.tooltip")); //$NON-NLS-1$
		}
	}

	private static class DropDownAction extends Action {

		/** The compare configuration object used to get the compare model. */
		private final EMFCompareConfiguration configuration;

		private final boolean mode;

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            The compare configuration object.
		 */
		public DropDownAction(EMFCompareConfiguration configuration, boolean mode) {
			this.configuration = configuration;
			this.mode = mode;
			if (mode) {
				setText(EMFCompareIDEUIMessages.getString("dropdown.left.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
			} else {
				setText(EMFCompareIDEUIMessages.getString("dropdown.right.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			configuration.setPreviewMergeMode(mode);
		}
	}
}
