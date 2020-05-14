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
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator;
import org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.image.EMFCompareCopyToImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * Export action for diagram comparison.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class ExportComparisonAction extends WorkspaceModifyOperation {

	/**
	 * The diagram to be copied from the left side of the comparison in the ContentMergeViewer.
	 */
	private DiagramEditPart leftDiagramEditPart;

	/**
	 * The diagram to be copied from the right side of the comparison in the ContentMergeViewer.
	 */
	private DiagramEditPart rightDiagramEditPart;

	/**
	 * Decorators representing Phantoms and Markers in the ContentMergeViewer.
	 */
	private Collection<AbstractDecorator> decorators;

	/**
	 * The destination folder chosen in the dialog.
	 */
	private IPath outputPath;

	/**
	 * The image format chosen in the dialog.
	 */
	private ImageFileFormat imageFormat;

	/**
	 * Constructor used to export leftDiagramEditPart and rightDiagramEditPart as image.
	 * 
	 * @param path
	 *            the folder in which store the images, result of the export.
	 * @param format
	 *            the format of the image, result of the export.
	 * @param leftDEP
	 *            The left {@link DiagramEditPart} to be copied.
	 * @param rightDEP
	 *            The right {@link DiagramEditPart} to be copied.
	 * @param abstractDecorators
	 *            The {@link AbstractDecorator}s of the ContentMergeViewer.
	 */
	public ExportComparisonAction(IPath path, ImageFileFormat format, DiagramEditPart leftDEP,
			DiagramEditPart rightDEP, Collection<AbstractDecorator> abstractDecorators) {
		outputPath = path;
		imageFormat = format;
		leftDiagramEditPart = leftDEP;
		rightDiagramEditPart = rightDEP;
		decorators = abstractDecorators;
	}

	/**
	 * Overridden to do the export work. {@inheritDoc}
	 */
	@Override
	protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			createImageFiles(monitor);
		} catch (final OutOfMemoryError | CoreException e) {
			// Catching OOM as this could be thrown if the image to export requires too large a buffer
			throw new InvocationTargetException(e, e.getLocalizedMessage());
		} finally {
			monitor.done();
			if (monitor.isCanceled()) {
				throw new InterruptedException("The operation was cancelled");
			}
		}
	}

	/**
	 * Create the image files.
	 *
	 * @param monitor
	 *            the progress monitor
	 * @throws CoreException
	 *             if one or several images creation failed. The Exception contains a Status with the causes
	 *             of this Exception.
	 */
	protected void createImageFiles(final IProgressMonitor monitor) throws CoreException {
		EMFCompareCopyToImageUtil tool = new EMFCompareCopyToImageUtil(leftDiagramEditPart,
				rightDiagramEditPart, decorators);
		tool.copyAllToImage(outputPath, imageFormat, monitor);
	}
}
