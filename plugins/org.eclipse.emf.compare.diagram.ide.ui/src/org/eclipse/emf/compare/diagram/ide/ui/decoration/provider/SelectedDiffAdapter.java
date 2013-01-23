package org.eclipse.emf.compare.diagram.ide.ui.decoration.provider;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.diagram.DiagramDiff;

public class SelectedDiffAdapter implements Adapter {

	private DiagramDiff diff;

	public SelectedDiffAdapter(DiagramDiff diff) {
		this.diff = diff;
	}

	public void notifyChanged(Notification notification) {
		// TODO Auto-generated method stub

	}

	public Notifier getTarget() {
		return diff;
	}

	public void setTarget(Notifier newTarget) {
		if (newTarget instanceof DiagramDiff) {
			this.diff = (DiagramDiff)newTarget;
		}
	}

	public boolean isAdapterForType(Object type) {
		return type == DiagramDiff.class;
	}

}
