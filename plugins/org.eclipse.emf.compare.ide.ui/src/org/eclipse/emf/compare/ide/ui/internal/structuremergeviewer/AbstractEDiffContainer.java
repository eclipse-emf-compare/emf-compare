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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffContainer extends AbstractEDiffElement implements IDiffContainer {

	/**
	 * @param adapterFactory
	 */
	public AbstractEDiffContainer(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer#hasChildren()
	 */
	public boolean hasChildren() {
		boolean ret = false;
		Adapter treeItemContentProvider = getAdapterFactory().adapt(getTarget(),
				ITreeItemContentProvider.class);
		if (treeItemContentProvider instanceof ITreeItemContentProvider) {
			ret = ((ITreeItemContentProvider)treeItemContentProvider).hasChildren(target);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer#getChildren()
	 */
	public IDiffElement[] getChildren() {
		Iterable<IDiffElement> ret = ImmutableList.of();
		Adapter treeItemContentProvider = getAdapterFactory().adapt(target, ITreeItemContentProvider.class);
		if (treeItemContentProvider instanceof ITreeItemContentProvider) {
			Collection<?> children = ((ITreeItemContentProvider)treeItemContentProvider).getChildren(target);
			ret = adapt(children, getAdapterFactory(), IDiffElement.class);
		}

		return toArray(ret, IDiffElement.class);
	}

	/**
	 * Always throws {@link UnsupportedOperationException}. This {@link AbstractEDiffContainer} is adapted
	 * from a diff EObject and cannot be modified directly.
	 * 
	 * @param child
	 *            the child to add
	 * @throws UnsupportedOperationException
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#add(org.eclipse.compare.structuremergeviewer.IDiffElement)
	 */
	public void add(IDiffElement child) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Always throws {@link UnsupportedOperationException}. This {@link AbstractEDiffContainer} is adapted
	 * from a diff EObject and cannot be modified directly.
	 * 
	 * @param child
	 *            the child to add
	 * @throws UnsupportedOperationException
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#removeToRoot(org.eclipse.compare.structuremergeviewer.IDiffElement)
	 */
	public void removeToRoot(IDiffElement child) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adapts each elements of the the given <code>iterable</code> to the given <code>type</code> by using the
	 * given <code>adapterFactory</code>.
	 * 
	 * @param <T>
	 *            the type of returned elements.
	 * @param iterable
	 *            the iterable to transform.
	 * @param adapterFactory
	 *            the {@link AdapterFactory} used to adapt elements
	 * @param type
	 *            the target type of adapted elements
	 * @return an iterable with element of type <code>type</code>.
	 */
	private static <T> Iterable<T> adapt(Iterable<?> iterable, final AdapterFactory adapterFactory,
			final Class<T> type) {
		Function<Object, Object> adaptFunction = new Function<Object, Object>() {
			public Object apply(Object input) {
				return adapterFactory.adapt(input, type);
			}
		};
		return filter(transform(iterable, adaptFunction), type);
	}
}
