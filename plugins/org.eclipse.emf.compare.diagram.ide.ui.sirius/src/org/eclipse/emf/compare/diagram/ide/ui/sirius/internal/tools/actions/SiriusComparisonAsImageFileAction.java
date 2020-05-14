/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.DiagramContentMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.DiagramMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.sirius.CompareDiagramIDEUISiriusPlugin;
import org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.dialog.ExportComparisonDialog;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class allows to run the comparison export as image.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class SiriusComparisonAsImageFileAction extends Action {

	/**
	 * The {@link DiagramContentMergeViewer} which contains the left, right and ancestor
	 * {@link DiagramMergeViewer} once initialized.
	 */
	private DiagramContentMergeViewer contentMergeViewer;

	/**
	 * The left {@link DiagramEditPart} to export as image.
	 */
	private DiagramEditPart leftDiagramEditPart;

	/**
	 * The right {@link DiagramEditPart} to export as image.
	 */
	private DiagramEditPart rightDiagramEditPart;

	/**
	 * Constructor.
	 * 
	 * @param mergeViewer
	 *            the {@link DiagramContentMergeViewer}.
	 */
	public SiriusComparisonAsImageFileAction(DiagramContentMergeViewer mergeViewer) {
		super("Export diagram comparison as image",
				CompareDiagramIDEUISiriusPlugin.getImageDescriptor("icons/screenshot.gif")); //$NON-NLS-1$
		contentMergeViewer = mergeViewer;
	}

	@Override
	public void run() {
		setEditParts();
		exportRepresentation();
	}

	/**
	 * Used to initialize leftDiagramEditPart and rightDiagramEditPart once the
	 * {@link DiagramContentMergeViewer} has been set.
	 */
	private void setEditParts() {
		RootEditPart leftRootEP = contentMergeViewer.getLeftMergeViewer().getGraphicalViewer()
				.getRootEditPart();
		RootEditPart rightRootEP = contentMergeViewer.getRightMergeViewer().getGraphicalViewer()
				.getRootEditPart();
		if (leftRootEP instanceof DiagramRootEditPart && rightRootEP instanceof DiagramRootEditPart) {
			DiagramRootEditPart leftRootEditPart = (DiagramRootEditPart)leftRootEP;
			DiagramRootEditPart rightRootEditPart = (DiagramRootEditPart)rightRootEP;
			if (leftRootEditPart.getContents() instanceof DiagramEditPart
					&& rightRootEditPart.getContents() instanceof DiagramEditPart) {
				leftDiagramEditPart = (DiagramEditPart)leftRootEditPart.getContents();
				rightDiagramEditPart = (DiagramEditPart)rightRootEditPart.getContents();
			}
		}
	}

	/**
	 * Display the export path and file format dialog and then export the representations.
	 */
	protected void exportRepresentation() {

		final Shell shell = Display.getCurrent().getActiveShell();
		ExportComparisonDialog dialog = new ExportComparisonDialog(shell);

		if (dialog.open() == Window.OK) {
			final ExportComparisonAction exportAction = new ExportComparisonAction(dialog.getOutputPath(),
					dialog.getImageFormat(), leftDiagramEditPart, rightDiagramEditPart,
					contentMergeViewer.getDecoratorsManager().getAllDecorators());
			final ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);

			try {
				pmd.run(false, false, exportAction);
			} catch (final InvocationTargetException e) {
				Throwable cause = e.getCause();
				if (cause instanceof OutOfMemoryError) {
					MessageDialog.openError(shell, "Not enough memory available to create image files",
							cause.getMessage());
				} else if (cause instanceof SizeTooLargeException) {
					MessageDialog.openError(shell, "Image export impossible", cause.getMessage());
					CompareDiagramIDEUISiriusPlugin.getDefault().log(cause);
				} else {
					MessageDialog.openError(shell, "Error", cause.getMessage());
				}
			} catch (final InterruptedException e) {
				MessageDialog.openInformation(shell, "Error", e.getMessage());
			} finally {
				pmd.close();
			}

		} else {
			dialog.close();
		}
	}
}
