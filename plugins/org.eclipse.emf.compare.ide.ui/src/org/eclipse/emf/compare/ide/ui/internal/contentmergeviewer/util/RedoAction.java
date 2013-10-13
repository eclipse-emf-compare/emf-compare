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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.action.Action;

/**
 * An redo action is implemented by using the {@link org.eclipse.emf.common.command.CommandStack}.
 */
public class RedoAction extends Action {
	protected ICompareEditingDomain domain;

	public RedoAction(ICompareEditingDomain domain) {
		super(EMFEditUIPlugin.INSTANCE.getString("_UI_Redo_menu_item", new Object[] {"" })); //$NON-NLS-1$ //$NON-NLS-2$
		setEditingDomain(domain);
	}

	@Override
	public void run() {
		if (domain != null) {
			domain.getCommandStack().redo();
		} else {
			/*
			 * FIXME : Domain may be null since we never unregister the actions from the handler service. It
			 * is set to null from #setEditingDomain(ICompareEditingDomain) when we switch to another content
			 * viewer. In such cases, we "may" reach this run() before the new viewer is fully displayed and
			 * its action initialized with a proper editing domain.
			 */
		}
	}

	public void setEditingDomain(ICompareEditingDomain domain) {
		this.domain = domain;
		if (domain != null) {
			update();
		}
	}

	public void update() {
		if (domain == null) {
			return;
		}

		setEnabled(domain.getCommandStack().canRedo());

		Command redoCommand = domain.getCommandStack().getRedoCommand();
		if (redoCommand != null && redoCommand.getLabel() != null) {
			setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Redo_menu_item", //$NON-NLS-1$
					new Object[] {EMFCompareIDEUIMessages.getString("redo.menu.item.text") })); //$NON-NLS-1$
		} else {
			setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Redo_menu_item", new Object[] {"" })); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (redoCommand != null && redoCommand.getDescription() != null) {
			setDescription(EMFEditUIPlugin.INSTANCE.getString("_UI_Redo_menu_item_description", //$NON-NLS-1$
					new Object[] {redoCommand.getDescription() }));
		} else {
			setDescription(EMFEditUIPlugin.INSTANCE.getString("_UI_Redo_menu_item_simple_description")); //$NON-NLS-1$
		}
	}
}
