/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.common.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * One image descriptor is overlayed on another image descriptor to generate a new image. Fixed image size: 16 pixel.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor {

	/** Static image size. */
	private static final Point IMAGE_SIZE = new Point(16, 16);

	/** Original image. */
	private final Image originalImage;

	/** Overlay image descriptor. */
	private final ImageDescriptor overlayDescriptor;

	/**
	 * Create an image descriptor which overlays a new image on another image.
	 * 
	 * @param originalImage
	 *            The original image.
	 * @param overlayDescriptor
	 *            Image descriptor of the overlay image.
	 */
	public OverlayImageDescriptor(Image originalImage, ImageDescriptor overlayDescriptor) {
		this.originalImage = originalImage;
		this.overlayDescriptor = overlayDescriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void drawCompositeImage(int width, int height) {
		final ImageData backgroundData = originalImage.getImageData();
		final ImageData overlayData = overlayDescriptor.getImageData();
		if (backgroundData != null)
			drawImage(backgroundData, 0, 0);
		if (overlayData != null)
			drawImage(overlayData, 0, IMAGE_SIZE.y - overlayData.height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getSize() {
		return IMAGE_SIZE;
	}

}