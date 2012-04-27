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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer;

import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffElement extends AdapterImpl implements IDiffElement {

	private final AdapterFactory fAdapterFactory;

	/**
	 * @param adapterFactory
	 */
	public AbstractEDiffElement(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == fAdapterFactory;
	}

	/**
	 * @return
	 */
	protected final AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	/**
	 * Returns the {@link #getRootAdapterFactory() root adapter factory} of the given
	 * <code>adapterAdapter</code> if it is a {@link ComposeableAdapterFactory composeable} one.
	 * 
	 * @return either the {@link #getRootAdapterFactory() root adapter factory} of this
	 *         <code>adapterAdapter</code> or <code>adapterAdapter</code>.
	 */
	protected final AdapterFactory getRootAdapterFactoryIfComposeable() {
		AdapterFactory af = getAdapterFactory();
		// If the adapter factory is composeable, we'll adapt using the root.
		if (fAdapterFactory instanceof ComposeableAdapterFactory) {
			af = ((ComposeableAdapterFactory)fAdapterFactory).getRootAdapterFactory();
		}
		return af;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		String ret = null;
		Adapter adapter = getRootAdapterFactoryIfComposeable().adapt(target, IItemLabelProvider.class);
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
		Adapter adapter = getRootAdapterFactoryIfComposeable().adapt(target, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			ret = getImageFromObject(((IItemLabelProvider)adapter).getImage(target));
		}
		return ret;
	}

	/**
	 * @param object
	 * @return
	 */
	protected Image getImageFromObject(Object object) {
		return ExtendedImageRegistry.getInstance().getImage(object);
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
			Adapter treeItemContentProvider = getRootAdapterFactoryIfComposeable().adapt(target,
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
