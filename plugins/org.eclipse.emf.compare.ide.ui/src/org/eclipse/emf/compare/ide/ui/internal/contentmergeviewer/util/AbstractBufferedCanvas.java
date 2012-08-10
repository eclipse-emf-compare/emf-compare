/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * We want to avoid flickering as much as possible for our draw operations on the center part, yet we can't
 * use double buffering to draw on it. We will then draw on a {@link Canvas} moved above that center part.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractBufferedCanvas extends Canvas {
	/** Buffer used by this {@link Canvas} to smoothly paint its content. */
	protected Image buffer;

	/**
	 * Default constructor, instantiates the canvas given its parent.
	 * 
	 * @param parent
	 *            Parent of the canvas.
	 * @param flags
	 *            SWT flags for this canvas.
	 */
	public AbstractBufferedCanvas(Composite parent, int flags) {
		super(parent, flags | SWT.NO_BACKGROUND);

		final PaintListener paintListener = new PaintListener() {
			public void paintControl(PaintEvent event) {
				doubleBufferedPaint(event.gc);
			}
		};
		addPaintListener(paintListener);

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (buffer != null) {
					buffer.dispose();
					buffer = null;
				}
				removePaintListener(paintListener);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (buffer != null) {
			buffer.dispose();
		}
	}

	public void repaint() {
		if (!isDisposed()) {
			GC gc = new GC(this);
			doubleBufferedPaint(gc);
			gc.dispose();
		}
	}

	/**
	 * Paints this component using double-buffering.
	 * 
	 * @param dest
	 *            Destination {@link GC graphics}.
	 */
	void doubleBufferedPaint(GC dest) {
		Point size = getSize();

		if (size.x <= 1 || size.y <= 1) {
			return;
		}

		if (buffer != null) {
			final Rectangle bufferBounds = buffer.getBounds();
			if (bufferBounds.width != size.x || bufferBounds.height != size.y) {
				buffer.dispose();
				buffer = null;
			}
		}
		if (buffer == null) {
			buffer = new Image(getDisplay(), size.x, size.y);
		}

		final GC gc = new GC(buffer);
		try {
			gc.setBackground(getBackground());
			gc.fillRectangle(0, 0, size.x, size.y);
			doPaint(gc);
		} finally {
			gc.dispose();
		}

		dest.drawImage(buffer, 0, 0);
	}

	/**
	 * Clients should implement this method for the actual drawing.
	 * 
	 * @param gc
	 *            {@link GC} used for the painting.
	 */
	abstract public void doPaint(GC gc);
}
