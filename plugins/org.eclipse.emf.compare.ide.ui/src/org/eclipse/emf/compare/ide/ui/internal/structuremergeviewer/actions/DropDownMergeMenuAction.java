/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.eventbus.Subscribe;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IMergePreviewModeChange;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
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
public class DropDownMergeMenuAction extends Action implements IMenuCreator {

	/** The compare configuration object used to get the compare model. */
	private final IEMFCompareConfiguration configuration;

	/** The menu associated with this action. */
	private Menu fMenu;

	/** The accept menu item. */
	private final List<Action> actions;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public DropDownMergeMenuAction(IEMFCompareConfiguration configuration, EnumSet<MergeMode> previewModes) {
		this.configuration = configuration;
		actions = newArrayList();

		for (MergeMode mergePreviewMode : previewModes) {
			actions.add(new DropDownAction(configuration, mergePreviewMode));
		}
		setTextAndImage(this, configuration.getMergePreviewMode());

		setMenuCreator(this);
		configuration.getEventBus().register(this);
	}

	@Subscribe
	public void mergePreviewModeChange(IMergePreviewModeChange event) {
		setTextAndImage(this, event.getNewValue());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		MergeMode mergeWay = configuration.getMergePreviewMode();
		configuration.setMergePreviewMode(mergeWay.inverse());
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
		configuration.getEventBus().unregister(this);
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
		for (IAction action : actions) {
			addActionToMenu(fMenu, action);
		}
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
	protected void addActionToMenu(Menu parent, IAction action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	static void setTextAndImage(IAction action, MergeMode mergeMode) {
		switch (mergeMode) {
			case LEFT_TO_RIGHT:
				action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
				action.setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.left.to.right.text")); //$NON-NLS-1$
				action.setText(EMFCompareIDEUIMessages.getString("dropdown.left.to.right.text")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
				action.setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.right.to.left.text")); //$NON-NLS-1$
				action.setText(EMFCompareIDEUIMessages.getString("dropdown.right.to.left.text")); //$NON-NLS-1$
				break;
			case ACCEPT:
				action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept.gif")); //$NON-NLS-1$
				action.setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.accept.text")); //$NON-NLS-1$
				action.setText(EMFCompareIDEUIMessages.getString("dropdown.accept.text")); //$NON-NLS-1$
				break;
			case REJECT:
				action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject.gif")); //$NON-NLS-1$
				action.setToolTipText(EMFCompareIDEUIMessages.getString("dropdown.reject.text")); //$NON-NLS-1$
				action.setText(EMFCompareIDEUIMessages.getString("dropdown.reject.text")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	private static class DropDownAction extends Action {

		/** The compare configuration object used to get the compare model. */
		private final IEMFCompareConfiguration configuration;

		private final MergeMode mergeMode;

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            The compare configuration object.
		 */
		public DropDownAction(IEMFCompareConfiguration configuration, MergeMode mergeMode) {
			this.configuration = configuration;
			this.mergeMode = mergeMode;
			setTextAndImage(this, mergeMode);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			configuration.setMergePreviewMode(mergeMode);
		}
	}
}
