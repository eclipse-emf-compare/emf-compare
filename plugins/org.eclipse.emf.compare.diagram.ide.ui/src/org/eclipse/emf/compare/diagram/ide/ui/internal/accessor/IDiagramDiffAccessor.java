package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IEObjectAccessor;

public interface IDiagramDiffAccessor extends IDiagramNodeAccessor, IEObjectAccessor {

	DiagramDiff getDiff();

}
