package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

public interface IDiagramNodeAccessor {

	EObject getEObject(MergeViewerSide side);

	Diagram getDiagram(MergeViewerSide side);

	Diagram getOwnedDiagram();

	View getOwnedView();

}
