/*******************************************************************************
 * Copyright (c) 2017, 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback;

import static org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback.TextFallbackMergeViewer.SHOW_PREVIEW;

import java.util.Optional;

import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.compare.ide.internal.utils.StoragePathAdapter;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * A specialized implementation of a compare input label provider.
 */
public final class TextFallbackCompareInputLabelProvider implements ICompareInputLabelProvider {

	/**
	 * The configuration used to provide labels.
	 * 
	 * @see #isDirty(boolean)
	 * @see #getLabel(Resource, IStorage, boolean)
	 */
	private final EMFCompareConfiguration configuration;

	/**
	 * The item delegator used by {@link #getImage(Resource)} to provide resource images.
	 */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/**
	 * The viewer for which labels are being provided so that {@link #isDirty(boolean) a dirty} indication is
	 * supported.
	 */
	private final TextFallbackMergeViewer textFallbackMergeViewer;

	/**
	 * Creates an instance for the given viewer and configuration.
	 * 
	 * @param textFallbackMergeViewer
	 *            the viewer for which labels are being provided
	 * @param configuration
	 *            the configuration used to provide labels.
	 */
	public TextFallbackCompareInputLabelProvider(TextFallbackMergeViewer textFallbackMergeViewer,
			EMFCompareConfiguration configuration) {
		this.textFallbackMergeViewer = textFallbackMergeViewer;
		this.configuration = configuration;
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(configuration.getAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This is never called and always return {@code null}.
	 * <p>
	 * 
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This is never called and always return {@code null}.
	 * <p>
	 * 
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		return null;
	}

	/**
	 * Returns an appropriate label for the given resource, storage, and dirty state.
	 * 
	 * @param resource
	 *            the resource for the label.
	 * @param storage
	 *            the storage for the label, if the resource is {@code null}.
	 * @param dirty
	 *            the dirty state of the resource/storage.
	 * @return an appropriate label for the given resource, storage, and dirty state.
	 */
	private String getLabel(Resource resource, IStorage storage, boolean dirty) {
		Optional<String> label = Optional.ofNullable(null);
		if (resource != null) {
			label = resource.eAdapters().stream().filter(StoragePathAdapter.class::isInstance)
					.map(adapter -> getLabel(storage, dirty, (StoragePathAdapter)adapter)).findFirst();
		}
		return label.orElse(""); //$NON-NLS-1$
	}

	private String getLabel(IStorage storage, boolean dirty, StoragePathAdapter storagePathAdapter) {
		String label = storagePathAdapter.getStoragePath();
		if (storage instanceof IFile) {
			if (configuration.getBooleanProperty(SHOW_PREVIEW, true)) {
				label = EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.preview.title", label); //$NON-NLS-1$
			} else {
				label = EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.local.title", label); //$NON-NLS-1$
				if (dirty) {
					label = EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.dirty.title", label); //$NON-NLS-1$
				}
			}
		}
		return label;
	}

	private boolean isDirty(boolean left) {
		if (left != configuration.isMirrored()) {
			return textFallbackMergeViewer.isLeftDirty();
		} else {
			return textFallbackMergeViewer.isRightDirty();
		}
	}

	/**
	 * Returns an image for the resource using the {@link #itemDelegator item delegator}.
	 * 
	 * @param resource
	 *            the resource for which we want an image.
	 * @return the image for the resource.
	 */
	private Image getImage(Resource resource) {
		if (resource != null) {
			Object image = itemDelegator.getImage(resource);
			return ExtendedImageRegistry.INSTANCE.getImage(image);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation has no state so ignores listeners.
	 * </p>
	 * 
	 * @see IBaseLabelProvider#addListener(ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation has no state so does nothing.
	 * </p>
	 * 
	 * @see IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This is never called and always returns {@code false}.
	 * </p>
	 * 
	 * @see IBaseLabelProvider#isLabelProperty(Object, String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation has no state so ignores listeners.
	 * </p>
	 * 
	 * @see IBaseLabelProvider#removeListener(ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getLabel(Resource, IStorage, boolean)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getAncestorLabel(Object)
	 */
	public String getAncestorLabel(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			TextFallbackCompareInputData textInputData = ((TextFallbackCompareInput)input).getTextInputData();
			return getLabel(textInputData.getOriginResource(), textInputData.getOriginStorage(), false);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getImage(Resource)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getAncestorImage(Object)
	 */
	public Image getAncestorImage(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			TextFallbackCompareInputData textInputData = ((TextFallbackCompareInput)input).getTextInputData();
			return getImage(textInputData.getOriginResource());
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getLabel(Resource, IStorage, boolean)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getLeftLabel(Object)
	 */
	public String getLeftLabel(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			TextFallbackCompareInputData textInputData = ((TextFallbackCompareInput)input).getTextInputData();
			return getLabel(textInputData.getLeftResource(), textInputData.getLeftStorage(), isDirty(true));
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getImage(Resource)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getLeftImage(Object)
	 */
	public Image getLeftImage(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			TextFallbackCompareInputData textInputData = ((TextFallbackCompareInput)input).getTextInputData();
			return getImage(textInputData.getLeftResource());
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getLabel(Resource, IStorage, boolean)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getRightLabel(Object)
	 */
	public String getRightLabel(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			TextFallbackCompareInputData textInputData = ((TextFallbackCompareInput)input).getTextInputData();
			return getLabel(textInputData.getRightResource(), textInputData.getRightStorage(),
					isDirty(false));
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls {@link #getImage(Resource)}.
	 * </p>
	 * 
	 * @see ICompareInputLabelProvider#getRightImage(Object)
	 */
	public Image getRightImage(Object input) {
		if (input instanceof TextFallbackCompareInput) {
			return getImage(((TextFallbackCompareInput)input).getTextInputData().getRightResource());
		} else {
			return null;
		}
	}
}
