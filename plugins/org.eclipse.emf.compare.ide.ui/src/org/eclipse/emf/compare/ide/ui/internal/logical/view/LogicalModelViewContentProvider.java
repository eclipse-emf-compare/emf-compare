/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.view;

import com.google.common.collect.Sets;

import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.LogicalModelView.Presentation;
import org.eclipse.ui.model.WorkbenchContentProvider;

/**
 * ContentProvider for the logical model view.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class LogicalModelViewContentProvider extends WorkbenchContentProvider {

	/** All models to display. */
	private Collection<IResource> leaves = Sets.newLinkedHashSet();

	/** The view associated with this content provider. */
	private LogicalModelView logicalModelView;

	/**
	 * Default constructor.
	 * 
	 * @param logicalModelView
	 *            the view associated with this content provider.
	 */
	LogicalModelViewContentProvider(final LogicalModelView logicalModelView) {
		this.logicalModelView = logicalModelView;
	}

	/**
	 * The models to display in the viewer.
	 * 
	 * @param leaves
	 *            the models to display in the viewer.
	 */
	public void setLeaves(Collection<IResource> leaves) {
		this.leaves.clear();
		this.leaves.addAll(leaves);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (logicalModelView.getPresentation() == Presentation.LIST) {
			return false;
		}
		return !leaves.contains(element) && getChildren(element).length > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object element) {
		Object[] children = new Object[0];
		if (logicalModelView.getPresentation() == Presentation.LIST) {
			if (!leaves.contains(element)) {
				children = leaves.toArray();
			}
		} else if (logicalModelView.getPresentation() == Presentation.TREE) {
			if (element instanceof IContainer) {
				if (isParentOfALeaf((IContainer)element)) {
					Object[] tmp = super.getChildren(element);
					children = getChildren(tmp);
				}
			} else if (element instanceof Object[]) {
				Collection<Object> tmp = Sets.newLinkedHashSet();
				Object[] list = (Object[])element;
				for (Object object : list) {
					if (object instanceof IContainer && isParentOfALeaf((IContainer)object)) {
						tmp.add(object);
					} else if (leaves.contains(object)) {
						tmp.add(object);
					}
				}
				children = tmp.toArray();
			} else {
				children = super.getChildren(element);
			}
		}
		return children;
	}

	/**
	 * Check if the given container is a parent of a model to display.
	 * 
	 * @param container
	 *            the given container.
	 * @return true if the given container is a parent of a model to display, false otherwise.
	 */
	private boolean isParentOfALeaf(IContainer container) {
		for (IResource leaf : leaves) {
			if (container.getFullPath().isPrefixOf(leaf.getFullPath())) {
				return true;
			}
		}
		return false;
	}
}
