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

import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.TextFallbackCompareViewerCreator;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.team.internal.ui.mapping.AbstractCompareInput;
import org.eclipse.team.internal.ui.mapping.CompareInputChangeNotifier;
import org.eclipse.team.internal.ui.synchronize.LocalResourceTypedElement;

/**
 * A highly specialized implementation of a compare input that uses {@link TextFallbackCompareInputData text input
 * data} to manage the information about the ancestor, left, and right.
 */
@SuppressWarnings("restriction")
public final class TextFallbackCompareInput extends AbstractCompareInput {

	/**
	 * The text input data representation the underlying sides of the input.
	 */
	private final TextFallbackCompareInputData textInputData;

	/**
	 * The compare input change notifier used by {@link #getChangeNotifier()}.
	 */
	private final CompareInputChangeNotifier compareInputChangeNotifier = new CompareInputChangeNotifier() {
		@Override
		protected IResource[] getResources(ICompareInput input) {
			return getResources();
		}

		/**
		 * Returns an array of resources containing each of{@link #originStorage}, {@link #leftStorage}, and
		 * {@link #rightStorage} that is an {@link IResource}.
		 * 
		 * @return an array of the underlying resources of the sides.
		 * @see
		 */
		public IResource[] getResources() {
			List<IResource> resources = Lists.newArrayList();
			if (textInputData.getOriginStorage() instanceof IResource) {
				resources.add((IResource)textInputData.getOriginStorage());
			}
			if (textInputData.getLeftStorage() instanceof IResource) {
				resources.add((IResource)textInputData.getLeftStorage());
			}
			if (textInputData.getRightStorage() instanceof IResource) {
				resources.add((IResource)textInputData.getRightStorage());
			}
			return resources.toArray(new IResource[resources.size()]);
		}

	};

	/**
	 * Creates an instance of the given kind, using the given text input data, and an indication of whether
	 * this input is for {@link TextFallbackCompareViewerCreator#SHOW_PREVIEW preview mode}.
	 * 
	 * @param kind
	 *            the kind of input.
	 * @param textInputData
	 *            the text input data for this input.
	 * @param showPreview
	 *            whether this is input for preview mode.
	 */
	public TextFallbackCompareInput(int kind, TextFallbackCompareInputData textInputData, boolean showPreview) {
		super(kind, getElement(textInputData.getOriginTypedElement(), textInputData.getOriginResource(), showPreview),
				getElement(textInputData.getLeftTypedElement(), textInputData.getLeftResource(), showPreview),
				getElement(textInputData.getRightTypedElement(), textInputData.getRightResource(), showPreview));
		this.textInputData = textInputData;
	}

	/**
	 * Returns a transformed typed element appropriate for the given resource and the specified preview mode.
	 * 
	 * @param typedElement
	 *            the typed element to transform.
	 * @param resource
	 *            the resource used during the transformation.
	 * @param showPreview
	 *            whether the result should be used for preview mode.
	 * @return a transformed typed element, or the original.
	 */
	private static ITypedElement getElement(ITypedElement typedElement, Resource resource,
			boolean showPreview) {
		// If we want preview mode and we have a local resource typed element, and the resource is an XML
		// resource...
		if (showPreview && typedElement instanceof LocalResourceTypedElement
				&& resource instanceof XMLResource) {
			LocalResourceTypedElement localResourceTypedElement = (LocalResourceTypedElement)typedElement;
			final IResource localResource = localResourceTypedElement.getResource();
			final XMLResource xmlResource = (XMLResource)resource;

			// Create storage that will use the local resource's path information, but fetches contents by
			// serializing the XML resource.
			IEncodedStorage storage = new EncodedStorage(localResource, xmlResource);
			// Created a new storage typed element for the storage.
			return new StorageTypedElement(storage, localResource.getFullPath().toString());
		}
		return typedElement;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation does nothing.
	 * </p>
	 * 
	 * @see AbstractCompareInput#update()
	 */
	@Override
	public void update() {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation always returns {@code false}.
	 * </p>
	 * 
	 * @see AbstractCompareInput#needsUpdate()
	 */
	@Override
	public boolean needsUpdate() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation always returns {@link #compareInputChangeNotifier}.
	 * </p>
	 * 
	 * @see AbstractCompareInput#getChangeNotifier()
	 */
	@Override
	protected CompareInputChangeNotifier getChangeNotifier() {
		return compareInputChangeNotifier;
	}

	public TextFallbackCompareInputData getTextInputData() {
		return textInputData;
	}

	private static final class EncodedStorage implements IEncodedStorage {

		private final IResource localResource;

		private final XMLResource xmlResource;

		private EncodedStorage(IResource localResource, XMLResource xmlResource) {
			this.localResource = localResource;
			this.xmlResource = xmlResource;
		}

		// Don't use getAdapter(Class<T>) for compatibility with Luna
		public Object getAdapter(Class adapter) {
			return null;
		}

		public boolean isReadOnly() {
			return true;
		}

		public String getName() {
			return localResource.getName();
		}

		public IPath getFullPath() {
			return localResource.getFullPath();
		}

		public InputStream getContents() throws CoreException {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				xmlResource.save(byteArrayOutputStream, null);
			} catch (IOException e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			}
			return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		}

		public String getCharset() throws CoreException {
			return xmlResource.getEncoding();
		}
	}
}
