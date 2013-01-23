package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.action.Action;

/**
 * An undo action is implemented by using the {@link org.eclipse.emf.common.command.CommandStack}.
 */
public class UndoAction extends Action {
	protected ICompareEditingDomain domain;

	public UndoAction(ICompareEditingDomain domain) {
		super(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item", new Object[] {"" })); //$NON-NLS-1$ //$NON-NLS-2$
		setEditingDomain(domain);
	}

	public UndoAction() {
		super(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item", new Object[] {"" })); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void run() {
		domain.getCommandStack().undo();
	}

	public void setEditingDomain(ICompareEditingDomain domain) {
		this.domain = domain;
		if (domain != null) {
			update();
		}
	}

	public void update() {
		setEnabled(domain.getCommandStack().canUndo());

		Command undoCommand = domain.getCommandStack().getUndoCommand();
		if (undoCommand != null && undoCommand.getLabel() != null) {
			setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item", new Object[] {undoCommand //$NON-NLS-1$
					.getLabel() }));
		} else {
			setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item", new Object[] {"" })); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (undoCommand != null && undoCommand.getDescription() != null) {
			setDescription(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item_description", //$NON-NLS-1$
					new Object[] {undoCommand.getDescription() }));
		} else {
			setDescription(EMFEditUIPlugin.INSTANCE.getString("_UI_Undo_menu_item_simple_description")); //$NON-NLS-1$
		}
	}

}
