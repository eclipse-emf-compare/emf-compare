package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

public interface IDiagramNodeAccessor {

	Comparison getComparison();

	EObject getEObject(MergeViewerSide side);

	Diagram getDiagram(MergeViewerSide side);

	Diagram getOwnedDiagram();

	View getOwnedView();

	MergeViewerSide getOriginSide();

	MergeViewerSide getSide();

	List<Diff> getAllDiffs();

}
