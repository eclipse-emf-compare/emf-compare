/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;

import java.util.Arrays;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

/**
 * Specialized AdapterFactoryContentProvider for the emf compare structure merge viewer.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class EMFCompareStructureMergeViewerContentProvider extends AdapterFactoryContentProvider {

	/**
	 * Constructs the content provider with the appropriate adapter factory.
	 * 
	 * @param adapterFactory
	 *            The adapter factory used to construct the content provider.
	 */
	public EMFCompareStructureMergeViewerContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getParent(Object object)
	 */
	@Override
	public Object getParent(Object element) {
		final Object ret;
		if (element instanceof CompareInputAdapter) {
			ret = super.getParent(((Adapter)element).getTarget());
		} else if (element instanceof ICompareInput) {
			ret = null;
		} else {
			ret = super.getParent(element);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(Object object)
	 */
	@Override
	public final boolean hasChildren(Object element) {
		final boolean ret;
		if (element instanceof CompareInputAdapter) {
			ret = super.hasChildren(((Adapter)element).getTarget());
		} else if (element instanceof ICompareInput) {
			ret = false;
		} else {
			ret = super.hasChildren(element);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(Object object)
	 */
	@Override
	public final Object[] getChildren(Object element) {
		final Object[] children;
		if (element instanceof CompareInputAdapter) {
			children = super.getChildren(((Adapter)element).getTarget());
		} else if (element instanceof ICompareInput) {
			children = new Object[] {};
		} else {
			children = super.getChildren(element);
		}

		Iterable<?> compareInputs = adapt(children, getAdapterFactory(), ICompareInput.class);
		return toArray(compareInputs, Object.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(Object object)
	 */
	@Override
	public Object[] getElements(Object element) {
		return getChildren(element);
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
	 *            the {@link AdapterFactory} used to adapt elements.
	 * @param type
	 *            the target type of adapted elements.
	 * @return an iterable with element of type <code>type</code>.
	 */
	private static Iterable<?> adapt(Iterable<?> iterable, final AdapterFactory adapterFactory,
			final Class<?> type) {
		Function<Object, Object> adaptFunction = new Function<Object, Object>() {
			public Object apply(Object input) {
				Object ret = adapterFactory.adapt(input, type);
				if (ret == null) {
					return input;
				}
				return ret;
			}
		};
		return transform(iterable, adaptFunction);
	}

	/**
	 * Adapts each elements of the the given <code>array</code> to the given <code>type</code> by using the
	 * given <code>adapterFactory</code>.
	 * 
	 * @param <T>
	 *            the type of returned elements.
	 * @param iterable
	 *            the array to transform.
	 * @param adapterFactory
	 *            the {@link AdapterFactory} used to adapt elements.
	 * @param type
	 *            the target type of adapted elements
	 * @return an iterable with element of type <code>type</code>.
	 */
	private static Iterable<?> adapt(Object[] iterable, final AdapterFactory adapterFactory,
			final Class<?> type) {
		return adapt(Arrays.asList(iterable), adapterFactory, type);
	}
}
