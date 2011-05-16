/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ParameterizedContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * Define a ModelContentMergeViewer parameterized by ordering extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedContentMergeViewer extends ModelContentMergeViewer {
	/** Instance of this viewer. */
	private static ParameterizedContentMergeViewer instance;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent Composite.
	 * @param config
	 *            the Compare configuration.
	 */
	public ParameterizedContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(parent, config);
		instance = this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#getCenterCanvas()
	 */
	@Override
	protected CenterCanvas getCenterCanvas() {
		return new ParameterizedCenterCanvas((Composite)getControl());
	}

	/**
	 * A center canvas parameterized by ordering extensions. The visible differences take into account the
	 * active filters.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	public class ParameterizedCenterCanvas extends CenterCanvas {

		/**
		 * Constructor.
		 * 
		 * @param parent
		 *            The parent Composite.
		 */
		public ParameterizedCenterCanvas(Composite parent) {
			super(parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer.CenterCanvas#hasLineBeDrawn(org.eclipse.emf.compare.diff.metamodel.DiffElement)
		 */
		@Override
		protected boolean hasLineBeDrawn(DiffElement diff) {
			return super.hasLineBeDrawn(diff) && !ParameterizedStructureContentProvider.isHidden(diff);
		}

	}

	/**
	 * Returns the instance of the viewer.
	 * 
	 * @return The instance of the viewer.
	 */
	public static ParameterizedContentMergeViewer getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#createModelContentMergeTabFolder(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	@Override
	protected ModelContentMergeTabFolder createModelContentMergeTabFolder(Composite composite, int side) {
		return new ParameterizedContentMergeTabFolder(this, composite, side);
	}
}
