/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.CompareUIPlugin;
import org.eclipse.compare.internal.MergeViewerContentProvider;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * A manager class for handling the switching of the mirrored mode for each specialized
 * {@link ContentMergeViewer} and {@link TextMergeViewer} in EMF Compare,
 */
public class MirrorManager {
	/**
	 * The compare configuration used by the merge viewer.
	 */
	private final EMFCompareConfiguration configuration;

	/**
	 * The first content provider that's set while this manager is managing the content providers. We keep
	 * track of this because org.eclipse.compare.contentmergeviewer.ContentMergeViewer.updateContentProvider()
	 * replace our content provider with a default one.
	 */
	private IContentProvider managedContentProvider;

	/**
	 * The mirror of the first content provider that's set while this manager is managing the content
	 * providers.
	 */
	private IContentProvider mirroredManagedContentProvider;

	/**
	 * Creates an instance for this confirmation.
	 * 
	 * @param configuration
	 *            the EMF compare configuration.
	 */
	public MirrorManager(EMFCompareConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the appropriate content provider based on the current state of
	 * {@link EMFCompareConfiguration#isMirrored()}.
	 * <p>
	 * This method should be used in overrides of
	 * {@link ContentMergeViewer#setContentProvider(IContentProvider)} to automatically set the correctly
	 * mirrored content provider.
	 * </p>
	 * 
	 * @param contentProvider
	 *            the current content provider.
	 * @return the appropriate content provider based on the current mirroring state.
	 */
	public IContentProvider getContentProvider(IContentProvider contentProvider) {
		Assert.isTrue(contentProvider instanceof IMergeViewerContentProvider);
		if (managedContentProvider == null) {
			managedContentProvider = contentProvider;
		}

		if (configuration.isMirrored()) {
			if (mirroredManagedContentProvider == null) {
				mirroredManagedContentProvider = new MirroredContentProvider(configuration,
						(IMergeViewerContentProvider)managedContentProvider);
			}
			return mirroredManagedContentProvider;
		} else {
			return managedContentProvider;
		}
	}

	/**
	 * Called from a derived {@link ContentMergeViewer#handlePropertyChangeEvent(PropertyChangeEvent)} method
	 * to intercept the preference change for swapping, i.e., mirroring the sides.
	 * <p>
	 * If this method returns true, the caller should not call
	 * <code>super.handlePropertyChangeEvent(event)</code> but rather should do the processing themselves to
	 * avoid calls to {@link ContentMergeViewer#updateContentProvider()} which will set the bogus default
	 * content provider. The method sets the MIRRORED property of the configuration as is normally done when
	 * calling super.
	 * </p>
	 * 
	 * @param event
	 *            the event to handle.
	 * @return true, if the property change is the property for the preference switching of sides, false
	 *         otherwise.
	 */
	public boolean handlePropertyChangeEvent(PropertyChangeEvent event) {
		// For backward compatibility, instead of using ComparePreferencePage.SWAPPED,
		// we compute the value.
		if (event.getProperty().equals(CompareUIPlugin.PLUGIN_ID + ".Swapped")) { //$NON-NLS-1$
			configuration.setProperty(EMFCompareConfiguration.MIRRORED, event.getNewValue());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * A merge viewer content provider that mirrors all the directional methods. It's implemented by extending
	 * MergeViewerContentProvider because {@link TextMergeViewer#isUsingDefaultContentProvider()} tests for
	 * that. If that's false, the viewer created by
	 * {@link TextFallbackCompareViewerCreator#createViewer(Composite, CompareConfiguration)} will not be able
	 * to use shared documents while mirrored.
	 */
	private static class MirroredContentProvider extends MergeViewerContentProvider {
		private final IMergeViewerContentProvider delegate;

		public MirroredContentProvider(EMFCompareConfiguration configuration,
				IMergeViewerContentProvider delegate) {
			super(configuration);
			this.delegate = delegate;
		}

		@Override
		public boolean showAncestor(Object input) {
			return delegate.showAncestor(input);
		}

		@Override
		public void saveRightContent(Object input, byte[] bytes) {
			delegate.saveLeftContent(input, bytes);
		}

		@Override
		public void saveLeftContent(Object input, byte[] bytes) {
			delegate.saveRightContent(input, bytes);
		}

		@Override
		public boolean isRightEditable(Object input) {
			return delegate.isLeftEditable(input);
		}

		@Override
		public boolean isLeftEditable(Object input) {
			return delegate.isRightEditable(input);
		}

		@Override
		public String getRightLabel(Object input) {
			return delegate.getLeftLabel(input);
		}

		@Override
		public Image getRightImage(Object input) {
			return delegate.getLeftImage(input);
		}

		@Override
		public Object getRightContent(Object input) {
			return delegate.getLeftContent(input);
		}

		@Override
		public String getLeftLabel(Object input) {
			return delegate.getRightLabel(input);
		}

		@Override
		public Image getLeftImage(Object input) {
			return delegate.getRightImage(input);
		}

		@Override
		public Object getLeftContent(Object input) {
			return delegate.getRightContent(input);
		}

		@Override
		public String getAncestorLabel(Object input) {
			return delegate.getAncestorLabel(input);
		}

		@Override
		public Image getAncestorImage(Object input) {
			return delegate.getAncestorImage(input);
		}

		@Override
		public Object getAncestorContent(Object input) {
			return delegate.getAncestorContent(input);
		}

		@Override
		public void setLeftError(String errorMessage) {
			super.setRightError(errorMessage);
		}

		@Override
		public void setRightError(String errorMessage) {
			super.setLeftError(errorMessage);
		}

		@Override
		public void dispose() {
			super.dispose();
			delegate.dispose();
		}
	}
}
