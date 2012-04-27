/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link ITypedElement} that can be used as input of EObjectContentMergeViewer. It is implementing
 * {@link IStreamContentAccessor} to be able to compare XMI serialization of wrapped {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectNode implements ITypedElement, IEObjectAccessor, IStreamContentAccessor {

	/**
	 * The wrapped {@link EObject}.
	 */
	private final EObject fEObject;

	/**
	 * The adapter factory that will be used to get the name and the image of the wrapped EObject.
	 */
	private final AdapterFactory fAdapterFactory;

	/**
	 * Creates a new object wrapping the given <code>eObject</code>.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to get the image from.
	 * @param eObject
	 *            the {@link EObject} to wrap.
	 */
	public EObjectNode(AdapterFactory adapterFactory, EObject eObject) {
		fAdapterFactory = adapterFactory;
		fEObject = eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return fEObject.eClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Adapter adapter = getRootAdapterFactoryIfComposeable().adapt(fEObject, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			Object image = ((IItemLabelProvider)adapter).getImage(fEObject);
			return ExtendedImageRegistry.getInstance().getImage(image);
		}
		return null;
	}

	/**
	 * Returns the {@link #getRootAdapterFactory() root adapter factory} of the given
	 * <code>adapterAdapter</code> if it is a {@link ComposeableAdapterFactory composeable} one.
	 * 
	 * @return either the {@link #getRootAdapterFactory() root adapter factory} of this
	 *         <code>adapterAdapter</code> or <code>adapterAdapter</code>.
	 */
	private AdapterFactory getRootAdapterFactoryIfComposeable() {
		AdapterFactory af = fAdapterFactory;
		// If the adapter factory is composeable, we'll adapt using the root.
		if (fAdapterFactory instanceof ComposeableAdapterFactory) {
			af = ((ComposeableAdapterFactory)fAdapterFactory).getRootAdapterFactory();
		}
		return af;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return "eobject";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IEObjectAccessor#getEObject()
	 */
	public EObject getEObject() {
		return fEObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		XMIResourceImpl r = new XMIResourceImpl(URI.createURI("dummy.xmi")); //$NON-NLS-1$
		final EObject copy = EcoreUtil.copy(fEObject);
		r.getContents().add(copy);
		StringWriter sw = new StringWriter();
		try {
			r.save(sw, Maps.newHashMap());
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID,
					e.getMessage(), e));
		}
		return new ByteArrayInputStream(sw.toString().getBytes());
	}
}
