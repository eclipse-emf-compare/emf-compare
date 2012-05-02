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

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.List;

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.MatchNode;
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
			Adapter treeItemContentProvider = getAdapterFactory().adapt(target,
					ITreeItemContentProvider.class);
			if (treeItemContentProvider instanceof ITreeItemContentProvider) {
				List<IDiffElement> children = newArrayList(getChildren());
				Iterable<IDiffElement> notMatchChildren = filter(children, not(instanceOf(MatchNode.class)));
				if (!isEmpty(notMatchChildren)) {
					ret = true;
				} else {
					ret = hasChildren(notMatchChildren);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns true if one of the given <code>elements</code> is a {@link IDiffContainer} and it
	 * {@link IDiffContainer#hasChildren() has children}. Returns false if the given iterable is empty.
	 * 
	 * @param elements
	 *            the elements to test
	 * @return true true if one of the given <code>elements</code> is a {@link IDiffContainer} and it
	 *         {@link IDiffContainer#hasChildren() has children}, false otherwise.
	 */
	private static boolean hasChildren(Iterable<IDiffElement> elements) {
		boolean ret = false;
		for (IDiffElement child : elements) {
			if (child instanceof IDiffContainer && ((IDiffContainer)child).hasChildren()) {
				ret = true;
				break;
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
			Adapter treeItemContentProvider = getAdapterFactory().adapt(target,
					ITreeItemContentProvider.class);
			if (treeItemContentProvider instanceof ITreeItemContentProvider) {
				Collection<?> children = ((ITreeItemContentProvider)treeItemContentProvider)
						.getChildren(target);
				Iterable<?> childrenToDisplay = filter(children, fNeedDisplay);
				ret = toArray(adapt(childrenToDisplay, getAdapterFactory(), IDiffElement.class),
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
