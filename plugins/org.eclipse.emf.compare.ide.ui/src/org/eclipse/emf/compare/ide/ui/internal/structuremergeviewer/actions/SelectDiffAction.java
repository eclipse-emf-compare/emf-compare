/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Tobias Ortmayr - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages the selection of a difference. The change flag determines which type of difference
 * should be selected.
 * 
 * @author Tobias Ortmayr <tortmayr.ext@eclipsessource.com>
 */
public class SelectDiffAction extends Action {

	private final INavigatable navigatable;

	private final int changeFlag;

	private IHandlerActivation activatedHandler;

	public SelectDiffAction(INavigatable navigatable, int changeFlag) {
		this.navigatable = navigatable;
		this.changeFlag = changeFlag;
		initToolTipAndImage();
		activateActionHandler();
	}

	protected void initToolTipAndImage() {
		switch (changeFlag) {
			case INavigatable.PREVIOUS_CHANGE:
				setToolTipText(EMFCompareIDEUIMessages.getString("previous.diff.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/prev_diff.gif")); //$NON-NLS-1$
				break;
			case INavigatable.NEXT_CHANGE:
				setToolTipText(EMFCompareIDEUIMessages.getString("next.diff.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/next_diff.gif")); //$NON-NLS-1$
				break;

			case Navigatable.PREVIOUS_UNRESOLVED_CHANGE:
				setToolTipText(EMFCompareIDEUIMessages.getString("previous.unresolved.diff.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/prev_unresolved_diff.png")); //$NON-NLS-1$
				break;

			case Navigatable.NEXT_UNRESOLVED_CHANGE:
				setToolTipText(EMFCompareIDEUIMessages.getString("next.unresolved.diff.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/next_unresolved_diff.png")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * Activates the action handler for the respective action.
	 */
	protected void activateActionHandler() {
		switch (changeFlag) {
			case INavigatable.PREVIOUS_CHANGE:
				break; // no keybinding and handler defined
			case INavigatable.NEXT_CHANGE:
				break; // no keybinding and handler defined
			case Navigatable.PREVIOUS_UNRESOLVED_CHANGE:
				activateActionHandler("org.eclipse.emf.compare.ide.ui.selectPreviousUnresolvedDiff");//$NON-NLS-1$
				break;
			case Navigatable.NEXT_UNRESOLVED_CHANGE:
				activateActionHandler("org.eclipse.emf.compare.ide.ui.selectNextUnresolvedDiff");//$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * Creates and activates a handler for this action and the given command id. This enables the usage of
	 * keybindings for the given command id.
	 * 
	 * @param actionDefinitionID
	 *            action definition id and command id
	 */
	private void activateActionHandler(String actionDefinitionID) {
		if (PlatformUI.isWorkbenchRunning()) {
			setActionDefinitionId(actionDefinitionID);
			final IHandlerService handlerService = getHandlerService();
			if (handlerService != null) {
				activatedHandler = handlerService.activateHandler(actionDefinitionID,
						new ActionHandler(this));
			}
		}
	}

	@SuppressWarnings("cast")
	protected IHandlerService getHandlerService() {
		// explicit cast necessary for Luna
		return (IHandlerService)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart().getSite().getService(IHandlerService.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		navigatable.selectChange(changeFlag);
	}

	public void dispose() {
		final IHandlerService handlerService = getHandlerService();
		if (handlerService != null && activatedHandler != null) {
			handlerService.deactivateHandler(activatedHandler);
		}
	}
}
