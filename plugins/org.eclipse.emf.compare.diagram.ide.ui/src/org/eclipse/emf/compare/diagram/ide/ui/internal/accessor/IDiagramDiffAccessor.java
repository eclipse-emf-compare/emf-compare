package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

public interface IDiagramDiffAccessor extends IEObjectAccessor {

	EObject getEObject(MergeViewerSide side);

	Diagram getDiagram();

	Diagram getDiagram(MergeViewerSide side);

	Comparison getComparison();

	DiagramDiff getDiff();

	Diagram getOwnedDiagram();

	View getOwnedView();

}
