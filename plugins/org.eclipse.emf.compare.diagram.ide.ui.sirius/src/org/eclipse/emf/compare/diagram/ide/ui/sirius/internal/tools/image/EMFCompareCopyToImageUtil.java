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
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageExporter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.sirius.common.tools.api.util.FileUtil;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.part.DiagramEditPartService;

/**
 * This class is used to manage the creation of images from DiagramEditPart, using the Sirius API.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class EMFCompareCopyToImageUtil extends DiagramEditPartService {

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
	 * Constructor.
	 * 
	 * @param leftDEP
	 *            The left {@link DiagramEditPart} to be copied.
	 * @param rightDEP
	 *            The right {@link DiagramEditPart} to be copied.
	 * @param abstractDecorators
	 *            The {@link AbstractDecorator}s of the ContentMergeViewer.
	 */
	public EMFCompareCopyToImageUtil(DiagramEditPart leftDEP, DiagramEditPart rightDEP,
			Collection<AbstractDecorator> abstractDecorators) {
		leftDiagramEditPart = leftDEP;
		rightDiagramEditPart = rightDEP;
		decorators = abstractDecorators;
	}

	/**
	 * Get the left {@link DiagramEditPart} to be copied.
	 * 
	 * @return the left {@link DiagramEditPart} to be copied.
	 */
	public DiagramEditPart getLeftDiagramEditPart() {
		return leftDiagramEditPart;
	}

	/**
	 * Get the right {@link DiagramEditPart} to be copied.
	 * 
	 * @return the right {@link DiagramEditPart} to be copied.
	 */
	public DiagramEditPart getRightDiagramEditPart() {
		return rightDiagramEditPart;
	}

	/**
	 * Get the {@link AbstractDecorator}s of the ContentMergeViewer.
	 * 
	 * @return the {@link AbstractDecorator}s of the ContentMergeViewer.
	 */
	public Collection<AbstractDecorator> getDecorators() {
		return decorators;
	}

	/**
	 * Only overridden to use {@link EMFCompareDiagramImageGenerator} instead of
	 * org.eclipse.sirius.diagram.ui.tools.internal.render.SiriusDiagramImageGenerator.
	 */
	@Override
	protected DiagramGenerator getDiagramGenerator(DiagramEditPart diagramEP, ImageFileFormat format) {
		return new EMFCompareDiagramImageGenerator(diagramEP, decorators, getSide(diagramEP));
	}

	/**
	 * Used to find out which side of the MergeViewer the diagram belongs to.
	 * 
	 * @param diagramEP
	 *            the {@link DiagramEditPart} for which we are looking for the {@link MergeViewerSide}.
	 * @return the corresponding {@link MergeViewerSide}.
	 */
	protected MergeViewerSide getSide(DiagramEditPart diagramEP) {
		if (diagramEP.equals(leftDiagramEditPart)) {
			return MergeViewerSide.LEFT;
		} else {
			return MergeViewerSide.RIGHT;
		}
	}

	/**
	 * Executes the creation of all images. One image is created for the LeftDiagramEditPart, and another one
	 * for the RightDiagramEditPart, then the two images are joined together to return the image resulting
	 * from the diagram comparison.
	 * 
	 * @param destination
	 *            the destination folder chosen in the dialog.
	 * @param format
	 *            the image format chosen in the dialog.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             if the export of the image to a file has failed.
	 */
	public void copyAllToImage(IPath destination, ImageFileFormat format, IProgressMonitor monitor)
			throws CoreException {
		IPath leftPath = getValidPath(destination, leftDiagramEditPart, format);
		copyToImage(leftDiagramEditPart, leftPath, format, monitor);
		IPath rightPath = getValidPath(destination, rightDiagramEditPart, format);
		copyToImage(rightDiagramEditPart, rightPath, format, monitor);
		try {
			appendImages(destination, leftPath, rightPath, format, monitor);
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to recover the files of the two generated images, and create a new one with the two joined images.
	 * 
	 * @param destination
	 *            the destination folder for the resulting image.
	 * @param leftPath
	 *            the path to the left image file.
	 * @param rightPath
	 *            the path to the right image file
	 * @param format
	 *            the image format.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             if the export of the image to a file has failed.
	 * @throws IOException
	 *             if the images on the left or right could not be read.
	 */
	protected void appendImages(IPath destination, IPath leftPath, IPath rightPath, ImageFileFormat format,
			IProgressMonitor monitor) throws CoreException, IOException {
		IPath fileName = getValidPath(destination, null, format);

		String exportFormat = ImageExporter.PNG_FILE;
		java.awt.Image leftImage = ImageIO.read(leftPath.toFile());
		java.awt.Image rightImage = ImageIO.read(rightPath.toFile());
		if (leftImage instanceof BufferedImage && rightImage instanceof BufferedImage) {
			java.awt.Image finalImage = joinImage((BufferedImage)leftImage, (BufferedImage)rightImage);
			monitor.worked(1);
			ImageExporter.exportToFile(fileName, (BufferedImage)finalImage, exportFormat, monitor,
					format.getQuality());

		}
		List<IPath> filesToDelete = new ArrayList<IPath>();
		filesToDelete.add(rightPath);
		filesToDelete.add(leftPath);
		deleteFiles(filesToDelete);
	}

	/**
	 * Used to delete files.
	 * 
	 * @param filesToDelete
	 *            the list of files to delete.
	 */
	protected void deleteFiles(List<IPath> filesToDelete) {
		for (IPath path : filesToDelete) {
			path.toFile().delete();
		}
	}

	/**
	 * Joins the two images and adds a separator.
	 * 
	 * @param leftImage
	 *            the image on the left.
	 * @param rightImage
	 *            the image on the right.
	 * @return the new generated image.
	 */
	protected Image joinImage(BufferedImage leftImage, BufferedImage rightImage) {

		int separator = (leftImage.getWidth() + rightImage.getWidth()) / 100;
		int wid = leftImage.getWidth() + rightImage.getWidth() + separator;
		int height = Math.max(leftImage.getHeight(), rightImage.getHeight());

		BufferedImage newImage = new BufferedImage(wid, height, leftImage.getType());

		Graphics2D g2 = newImage.createGraphics();

		Color oldColor = g2.getColor();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, wid + separator, height);
		g2.setPaint(Color.BLACK);
		g2.fillRect(leftImage.getWidth(), 0, separator, height);
		g2.setColor(oldColor);

		g2.drawImage(leftImage, 0, 0, null);
		g2.drawImage(rightImage, leftImage.getWidth() + separator, 0, null);

		g2.dispose();
		return newImage;
	}

	/**
	 * Get the {@link IPath} of the file to write. If the file already exists, it appends a number to its
	 * name.
	 * 
	 * @param destination
	 *            the folder where to write the file.
	 * @param diagramEP
	 *            the {@link DiagramEditPart} used to retrieve the name of the diagram.
	 * @param format
	 *            the file format.
	 * @return the path of the file to write.
	 */
	protected IPath getValidPath(IPath destination, DiagramEditPart diagramEP, ImageFileFormat format) {
		String filename = null;
		String extension = format.getName().toLowerCase();
		if (diagramEP != null) {
			Diagram diagram = (Diagram)diagramEP.getModel();
			DDiagram ddiagram = (DDiagram)diagram.getElement();
			filename = ddiagram.getDescription().getName();
			if (diagramEP == getLeftDiagramEditPart()) {
				filename = "left_" + filename; //$NON-NLS-1$
			} else if (diagramEP == getRightDiagramEditPart()) {
				filename = "right_" + filename; //$NON-NLS-1$
			}
		} else {
			filename = "Comparison_Image"; //$NON-NLS-1$
		}
		IPath filePath;
		final StringBuffer file = new StringBuffer(filename).append('.').append(extension);

		final String filenameWithExtension = validFilename(file.toString());
		if (destination.append(filenameWithExtension).toFile().exists()) {
			int version = 1;

			do {
				final String newFileName = validFilename(new StringBuffer(filename).append('_')
						.append(String.valueOf(version)).append('.').append(extension).toString());
				filePath = destination.append(newFileName);
				version++;
			} while (filePath.toFile().exists());
		} else {
			filePath = destination.append(filenameWithExtension);
		}
		return filePath;
	}

	/**
	 * Check if the filename is valid or not.
	 * 
	 * @param filename
	 *            the filename to check.
	 * @return a valid filename
	 */
	protected String validFilename(String filename) {
		final FileUtil util = new FileUtil(filename);
		if (util.isValid()) {
			return filename;
		} else {
			return util.getValidFilename();
		}
	}
}
