/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - extracted into own class
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

/**
 * The content provider for {@link IMergeViewerItem}s
 */
public class TreeContentMergeViewerItemContentProvider extends AdapterFactoryContentProvider {

	private IDifferenceGroupProvider differenceGroupProvider;

	private Predicate<? super EObject> differenceFilterPredicate;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the {@link AdapterFactory}
	 * @param differenceGroupProvider
	 *            the {@link IDifferenceGroupProvider}
	 * @param differenceFilterPredicate
	 *            the {@link Predicate}
	 */
	public TreeContentMergeViewerItemContentProvider(AdapterFactory adapterFactory,
			IDifferenceGroupProvider differenceGroupProvider,
			Predicate<? super EObject> differenceFilterPredicate) {
		super(adapterFactory);
		this.differenceGroupProvider = differenceGroupProvider;
		this.differenceFilterPredicate = differenceFilterPredicate;
	}

	@Override
	public Object[] getElements(Object object) {
		if (object instanceof ICompareAccessor) {
			return ((ICompareAccessor)object).getItems().toArray();
		}
		return super.getElements(object);
	}

	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof IMergeViewerItem.Container) {
			IMergeViewerItem[] children = ((IMergeViewerItem.Container)object)
					.getChildren(differenceGroupProvider, differenceFilterPredicate);

			return children;
		}
		return super.getChildren(object);
	}

	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof IMergeViewerItem.Container) {
			return ((IMergeViewerItem.Container)object).hasChildren(differenceGroupProvider,
					differenceFilterPredicate);
		}
		return super.hasChildren(object);
	}

	@Override
	public Object getParent(Object object) {
		if (object instanceof IMergeViewerItem.Container) {
			return ((IMergeViewerItem.Container)object).getParent();
		}
		return super.getParent(object);
	}
}
