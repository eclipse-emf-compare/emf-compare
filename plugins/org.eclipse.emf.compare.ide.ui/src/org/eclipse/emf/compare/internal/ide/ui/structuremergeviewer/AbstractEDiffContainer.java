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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider.MatchNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffContainer extends AbstractEDiffElement implements IDiffContainer {

	/**
	 * 
	 */
	private static final IDiffElement[] EMPTY_ARRAY__DIFF_ELEMENT = new IDiffElement[0];

	private final Predicate<Object> fNeedDisplay = new Predicate<Object>() {
		public boolean apply(Object input) {
			if (input instanceof IDiffContainer) {
				return ((IDiffContainer)input).hasChildren();
			}
			return true;
		}
	};

	/**
	 * @param adapterFactory
	 */
	public AbstractEDiffContainer(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#hasChildren()
	 */
	public boolean hasChildren() {
		boolean ret = false;
		if (target instanceof EObject) {
			Adapter treeItemContentProvider = getRootAdapterFactoryIfComposeable().adapt(target,
					ITreeItemContentProvider.class);
			if (treeItemContentProvider instanceof ITreeItemContentProvider) {
				List<IDiffElement> children = Lists.newArrayList(getChildren());
				Iterable<IDiffElement> notMatchChildren = Iterables.filter(children, Predicates
						.not(Predicates.instanceOf(MatchNode.class)));
				if (!Iterables.isEmpty(notMatchChildren)) {
					ret = true;
				} else {
					ret = hasChildren(notMatchChildren);
				}
			}
		}
		return ret;
	}

	/**
	 * @param notMatchChildren
	 * @return
	 */
	private static boolean hasChildren(Iterable<IDiffElement> notMatchChildren) {
		boolean ret = false;
		for (IDiffElement child : notMatchChildren) {
			if (child instanceof IDiffContainer) {
				if (((IDiffContainer)child).hasChildren()) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.IDiffContainer#getChildren()
	 */
	public IDiffElement[] getChildren() {
		IDiffElement[] ret = EMPTY_ARRAY__DIFF_ELEMENT;
		if (target instanceof EObject) {
			Adapter treeItemContentProvider = getRootAdapterFactoryIfComposeable().adapt(target,
					ITreeItemContentProvider.class);
			if (treeItemContentProvider instanceof ITreeItemContentProvider) {
				Collection<?> children = ((ITreeItemContentProvider)treeItemContentProvider)
						.getChildren(target);
				Iterable<?> childrenToDisplay = Iterables.filter(children, fNeedDisplay);
				ret = Iterables.toArray(adapt(childrenToDisplay, getAdapterFactory(), IDiffElement.class),
						IDiffElement.class);
			}
		}
		return ret;
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

	protected static <T> Iterable<T> adapt(Iterable<?> iterable, final AdapterFactory adapterFactory,
			final Class<T> clazz) {
		Function<Object, Object> adaptFunction = new Function<Object, Object>() {
			public Object apply(Object input) {
				return adapterFactory.adapt(input, clazz);
			}
		};
		return Iterables.filter(Iterables.transform(iterable, adaptFunction), clazz);
	}
}
