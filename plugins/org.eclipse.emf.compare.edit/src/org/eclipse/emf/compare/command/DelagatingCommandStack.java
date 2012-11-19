package org.eclipse.emf.compare.command;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;

public abstract class DelagatingCommandStack implements CommandStack {

	protected abstract CommandStack delegate();

	public void execute(Command command) {
		delegate().execute(command);
	}

	public boolean canUndo() {
		return delegate().canUndo();
	}

	public void undo() {
		delegate().undo();
	}

	public boolean canRedo() {
		return delegate().canRedo();
	}

	public Command getUndoCommand() {
		return delegate().getUndoCommand();
	}

	public Command getRedoCommand() {
		return delegate().getRedoCommand();
	}

	public Command getMostRecentCommand() {
		return delegate().getMostRecentCommand();
	}

	public void redo() {
		delegate().redo();
	}

	public void flush() {
		delegate().flush();
	}

	public void addCommandStackListener(CommandStackListener listener) {
		delegate().addCommandStackListener(listener);
	}

	public void removeCommandStackListener(CommandStackListener listener) {
		delegate().removeCommandStackListener(listener);
	}

}
