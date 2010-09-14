package org.eclipse.emf.compare.mpatch.common.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorInput;

/**
 * Internally used to locate GMF-based diagram editor input.
 * 
 * Please note that this class is optional! It might not be loaded if GMF is not loaded!
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class CommonGmfUtils {

	/**
	 * Try to get the editor input from a GMF diagram editor.
	 * 
	 * @param editorInput
	 *            The editor input.
	 * @return The {@link URI} of the model to which the diagram editor belongs or <code>null</code> if none is found.
	 */
	static URI getUriFromEditorInput(IEditorInput editorInput) {
		if (editorInput instanceof IDiagramEditorInput) {
			final IDiagramEditorInput diagramEditorInput = (IDiagramEditorInput) editorInput;
			final Diagram diagram = diagramEditorInput.getDiagram();

			// is the element set and is that our model?
			if (diagram.getElement() != null) {
				final Resource resource = diagram.getElement().eResource();
				if (resource != null && resource.getURI() != null)
					return resource.getURI();
			}

			// RSA, for instance, stores diagrams in annotations.
			if (diagram.eContainer() != null && diagram.eContainer() instanceof EAnnotation) {
				final Resource resource = diagram.eResource();
				if (resource != null && resource.getURI() != null)
					return resource.getURI();
			}
		}
		return null;
	}

}
