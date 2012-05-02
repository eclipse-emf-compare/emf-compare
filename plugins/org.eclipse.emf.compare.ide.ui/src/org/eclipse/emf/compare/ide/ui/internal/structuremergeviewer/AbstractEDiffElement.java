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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * An EMF {@link Adapter} implementing the {@link IDiffElement} interface.
 * <p>
 * It is delegating {@link #getImage()} and {@link #getName()} to an adapter retrieved from an
 * {@link AdapterFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffElement extends AdapterImpl implements IDiffElement {

	/**
	 * The {@link AdapterFactory} used to implement {@link #getName()} and {@link #getImage()}.
	 */
	private final AdapterFactory fAdapterFactory;

	/**
	 * Simple constructor storing the given {@link AdapterFactory}.
	 * 
	 * @param adapterFactory
	 *            the factory.
	 */
	public AbstractEDiffElement(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == fAdapterFactory;
	}

	/**
	 * Final accessor to the {@link AdapterFactory} for sub classses.
	 * 
	 * @return the wrapped {@link AdapterFactory}.
	 */
	protected final AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		String ret = null;
		Adapter adapter = getAdapterFactory().adapt(target, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			ret = ((IItemLabelProvider)adapter).getText(target);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Image ret = null;
		Adapter adapter = getAdapterFactory().adapt(target, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			Object imageObject = ((IItemLabelProvider)adapter).getImage(target);
			ret = ExtendedImageRegistry.getInstance().getImage(imageObject);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#getKind()
	 */
	public int getKind() {
		return Differencer.NO_CHANGE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#getParent()
	 */
	public IDiffContainer getParent() {
		IDiffContainer ret = null;
		if (target instanceof EObject) {
			Adapter treeItemContentProvider = getAdapterFactory().adapt(target,
					ITreeItemContentProvider.class);
			if (treeItemContentProvider instanceof ITreeItemContentProvider) {
				ret = getParentAndAdaptAsIDiffContainer((ITreeItemContentProvider)treeItemContentProvider);
			}
		}
		return ret;
	}

	/**
	 * @param treeItemContentProvider
	 * @return
	 */
	private IDiffContainer getParentAndAdaptAsIDiffContainer(ITreeItemContentProvider treeItemContentProvider) {
		IDiffContainer ret = null;
		Object parent = treeItemContentProvider.getParent(target);
		if (parent instanceof EObject) {
			Object diffContainer = getAdapterFactory().adapt(parent, IDiffContainer.class);
			if (diffContainer instanceof IDiffContainer) {
				ret = (IDiffContainer)diffContainer;
			}
		}
		return ret;
	}

	/**
	 * Always throws {@link UnsupportedOperationException}. This {@link AbstractEDiffElement} is adapted from
	 * a diff EObject and cannot be modified directly.
	 * 
	 * @param parent
	 *            the parent to set
	 * @see org.eclipse.compare.structuremergeviewer.IDiffElement#setParent(org.eclipse.compare.structuremergeviewer.IDiffContainer)
	 * @throws UnsupportedOperationException
	 */
	public void setParent(IDiffContainer parent) {
		throw new UnsupportedOperationException();
	}

}
