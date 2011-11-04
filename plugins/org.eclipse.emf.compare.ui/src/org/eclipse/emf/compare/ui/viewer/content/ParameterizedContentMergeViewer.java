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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.OrderingUtils;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ParameterizedContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.diff.ParameterizedContentMergeDiffTab;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterRegistry;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Define a ModelContentMergeViewer parameterized by ordering extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedContentMergeViewer extends ModelContentMergeViewer {

	/**
	 * Instance of this viewer.
	 * 
	 * @deprecated
	 */
	@Deprecated
	private static ParameterizedContentMergeViewer instance;

	/**
	 * Merge tab folder to manage.
	 * 
	 * @since 1.3
	 */
	protected List<ParameterizedContentMergeTabFolder> folders;

	/**
	 * Selected filters.
	 * 
	 * @since 1.3
	 */
	protected List<IDifferenceFilter> selectedFilters = new ArrayList<IDifferenceFilter>();

	/**
	 * Listener to react on ordering changes.
	 */
	private IPropertyChangeListener orderingSelectionListener;

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

		// deprecated:
		instance = this;

		final String preferenceValue = EMFCompareUIPlugin.getDefault().getPreferenceStore()
				.getString(EMFComparePreferenceConstants.PREFERENCES_KEY_DEFAULT_FILTERS);
		selectedFilters = DifferenceFilterRegistry.INSTANCE.getFilters(preferenceValue);
		orderingSelectionListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFCompareConstants.PROPERTY_STRUCTURE_FILTERS)) {
					selectedFilters = (List<IDifferenceFilter>)event.getNewValue();
					for (ParameterizedContentMergeTabFolder folder : folders) {
						final IModelContentMergeViewerTab tab = folder.getTreePart();
						if (tab instanceof ParameterizedContentMergeDiffTab) {
							((ParameterizedContentMergeDiffTab)tab).setSelectedFilters(selectedFilters);
						}
					}
					update();
				}
			}
		};
		configuration.addPropertyChangeListener(orderingSelectionListener);
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
			return super.hasLineBeDrawn(diff) && !OrderingUtils.isHidden(diff, selectedFilters);
		}

	}

	/**
	 * Returns the instance of the viewer.
	 * 
	 * @deprecated
	 * @return The instance of the viewer.
	 */
	@Deprecated
	public static ParameterizedContentMergeViewer getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		configuration.removePropertyChangeListener(orderingSelectionListener);

		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#createModelContentMergeTabFolder(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */

	@Override
	protected ModelContentMergeTabFolder createModelContentMergeTabFolder(Composite composite, int side) {
		final ParameterizedContentMergeTabFolder folder = new ParameterizedContentMergeTabFolder(this,
				composite, side);
		if (folders == null) {
			folders = new ArrayList<ParameterizedContentMergeTabFolder>();
		}
		folders.add(folder);
		return folder;
	}

}
