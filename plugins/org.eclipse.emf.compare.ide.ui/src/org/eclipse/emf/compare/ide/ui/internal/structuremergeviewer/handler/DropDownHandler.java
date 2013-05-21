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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler;

import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Handler that manages the click on the dropdown menu of the toolbar of the structure merge viewer.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DropDownHandler extends AbstractHandler implements IElementUpdater {

	/** The compare configuration object. */
	private CompareConfiguration configuration;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			Object trigger = event.getTrigger();
			if (trigger instanceof Event) {
				Event eventWidget = (Event)event.getTrigger();
				Widget widget = eventWidget.widget;
				if (widget instanceof ToolItem) {
					ToolItem toolItem = (ToolItem)widget;
					configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
					Boolean mergeWay = (Boolean)configuration.getProperty(EMFCompareConstants.MERGE_WAY);
					if (mergeWay == null || mergeWay.booleanValue()) {
						configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(false));
						toolItem.setImage(EMFCompareIDEUIPlugin
								.getImage("icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
					} else {
						configuration.setProperty(EMFCompareConstants.MERGE_WAY, new Boolean(true));
						toolItem.setImage(EMFCompareIDEUIPlugin
								.getImage("icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
					}
				}
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.commands.IElementUpdater#updateElement(UIElement, Map)
	 */
	public void updateElement(UIElement element, Map parameters) {
		if (configuration == null) {
			Object value = parameters.get("org.eclipse.ui.IWorkbenchWindow"); //$NON-NLS-1$
			if (value instanceof IWorkbenchWindow) {
				IWorkbenchPage pa = ((IWorkbenchWindow)value).getActivePage();
				IEditorPart editor = pa.getActiveEditor();
				if (editor instanceof CompareEditor) {
					IEditorInput editorInput = editor.getEditorInput();
					if (editorInput instanceof CompareEditorInput) {
						configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
					}
				}
			}
		}
		if (configuration != null) {
			Boolean mergeWay = (Boolean)configuration.getProperty(EMFCompareConstants.MERGE_WAY);
			if (mergeWay == null || mergeWay.booleanValue()) {
				element.setIcon(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/left_to_right.gif")); //$NON-NLS-1$
			} else {
				element.setIcon(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/right_to_left.gif")); //$NON-NLS-1$
			}
		}
	}
}
