/**
 * 
 */
package org.eclipse.emf.compare.ide.ui.internal.logical.view;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.LogicalModelView.Presentation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.internal.navigator.NavigatorDecoratingLabelProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * LabelProvider for the logical model view.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class LogicalModelViewLabelProvider extends NavigatorDecoratingLabelProvider {

	/** Separator between file and his path. */
	private static final String SEP = " - "; //$NON-NLS-1$

	/** The view associated with this content provider. */
	private LogicalModelView logicalModelView;

	/**
	 * Default constructor.
	 * 
	 * @param logicalModelView
	 *            the view associated with this label provider.
	 */
	LogicalModelViewLabelProvider(LogicalModelView logicalModelView) {
		super(new WorkbenchLabelProvider());
		this.logicalModelView = logicalModelView;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	protected StyledString getStyledText(Object element) {
		if (logicalModelView.getPresentation() == Presentation.LIST) {
			return super.getStyledText(element).append(getProjectPart(element));
		}
		return super.getStyledText(element);
	}

	/**
	 * Compute the relative project path of the given element and returns it as a StyledString.
	 * 
	 * @param element
	 *            the given element.
	 * @return the relative project path of the given element as a StyledString.
	 */
	private StyledString getProjectPart(Object element) {
		StyledString styledString = new StyledString();
		if (element instanceof IResource && !(element instanceof IWorkspaceRoot)) {
			styledString.append(SEP, StyledString.DECORATIONS_STYLER);
			String parentPath = ((IResource)element).getParent().getFullPath().toString();
			styledString.append(parentPath, StyledString.DECORATIONS_STYLER);
		}
		return styledString;
	}
}
