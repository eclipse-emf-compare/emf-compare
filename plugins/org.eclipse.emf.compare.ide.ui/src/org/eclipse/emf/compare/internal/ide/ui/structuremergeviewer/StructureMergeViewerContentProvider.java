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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider.CompareNodeAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
final class StructureMergeViewerContentProvider extends AdapterFactoryContentProvider {

	private final AdapterFactory fCompareNodeAdapterFactory;

	Predicate<Object> needDisplay = new Predicate<Object>() {
		public boolean apply(Object input) {
			if (input instanceof Match) {
				return hasChildren(input);
			}
			return true;
		}
	};

	/**
	 * @param adapterFactory
	 * @param emfCompareStructureMergeViewer
	 */
	StructureMergeViewerContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		fCompareNodeAdapterFactory = new CompareNodeAdapterFactory();
	}

	@Override
	public Object[] getChildren(Object object) {
		Object targetIfNotifier = getTargetIfNotifier(object);
		List<Object> children = Lists.newArrayList(super.getChildren(targetIfNotifier));
		Object[] adaptedChildren = adapt(Iterables.toArray(Iterables.filter(children, needDisplay),
				Object.class), fCompareNodeAdapterFactory, IDiffElement.class);
		return adaptedChildren;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object object) {
		Object targetIfNotifier = getTargetIfNotifier(object);
		Object[] elements = super.getElements(targetIfNotifier);
		Object[] adaptedElements = adapt(elements, fCompareNodeAdapterFactory, IDiffElement.class);
		return adaptedElements;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		Object targetIfNotifier = getTargetIfNotifier(object);
		Iterable<Object> notMatchChildren = Iterables.filter(Lists.newArrayList(super
				.getChildren(targetIfNotifier)), Predicates.not(Predicates.instanceOf(Match.class)));
		if (!Iterables.isEmpty(notMatchChildren)) {
			return true;
		}

		for (Object child : notMatchChildren) {
			if (hasChildren(child)) {
				return true;
			}
		}

		return false;
	}

	private static Object[] adapt(Object[] children, AdapterFactory adapterFactory, Class<?> clazz) {
		Object[] ret = new Object[children.length];
		for (int i = 0; i < ret.length; i++) {
			Object child = children[i];
			ret[i] = adapterFactory.adapt(child, clazz);
		}

		return ret;
	}

	private static Object getTargetIfNotifier(Object object) {
		if (object instanceof Adapter) {
			return ((Adapter)object).getTarget();
		}
		return object;
	}
}
