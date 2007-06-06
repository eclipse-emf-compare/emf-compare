package org.eclipse.emf.compare.ui.legacy.editor;

import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

/**
 * This editor will be used as the editor class for the extension point 
 * org.eclipse.ui.editors to allow edition of ".emfdiff" files.
 * 
 * @author Laurent Goubet <laurent.goubet@obeo.fr>
 */
public class ModelCompareEditor extends CompareEditor {
	/**
	 * Allows reflective instantiation via Class.newInstance().
	 */
	public ModelCompareEditor() {
		// Default empty instantiation
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) {
			ModelCompareEditorInput mcei = new ModelCompareEditorInput((IFileEditorInput)input);
			setSite(site);
			setInput(mcei);
		}
	}
}
