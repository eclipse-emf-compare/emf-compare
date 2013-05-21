package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.handler;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

public class AcceptRejectChangePropertyTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IEditorPart) {
			IEditorInput i = ((IEditorPart)receiver).getEditorInput();
			if (i instanceof CompareEditorInput) {
				CompareConfiguration configuration = ((CompareEditorInput)i).getCompareConfiguration();
				if (configuration.isLeftEditable() && !configuration.isRightEditable()) {
					return true;
				} else if (!configuration.isLeftEditable() && configuration.isRightEditable()) {
					return true;
				}
			}
		}
		return false;
	}

}
