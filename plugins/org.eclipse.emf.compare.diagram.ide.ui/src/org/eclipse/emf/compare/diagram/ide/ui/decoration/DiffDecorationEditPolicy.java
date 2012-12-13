package org.eclipse.emf.compare.diagram.ide.ui.decoration;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;

public class DiffDecorationEditPolicy extends DecorationEditPolicy {

	@Override
	public void refresh() {
		decorators = null;
		super.refresh();
	}

}
