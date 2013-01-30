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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IMergeViewerItem extends Adapter {

	Diff getDiff();

	Object getLeft();

	Object getRight();

	Object getAncestor();

	Object getSideValue(MergeViewerSide side);

	MergeViewerSide getSide();

	boolean isInsertionPoint();

	/**
	 * Returns the parent of this element. If the object is the root of a hierarchy <code>null</code> is
	 * returned.
	 * 
	 * @return the parent of this element, or <code>null</code> if the element has no parent
	 */
	Container getParent();

	public static final Predicate<IMergeViewerItem> IS_INSERTION_POINT = new Predicate<IMergeViewerItem>() {
		public boolean apply(IMergeViewerItem item) {
			return item.isInsertionPoint();
		}
	};

	interface Container extends IMergeViewerItem {
		/**
		 * Returns whether this container has at least one child. In some cases this methods avoids having to
		 * call the potential more costly <code>getChildren</code> method.
		 * 
		 * @return <code>true</code> if this container has at least one child
		 */
		boolean hasChildren();

		/**
		 * Returns the children of this container. If this container has no children an empty array is
		 * returned (not <code>null</code>).
		 * 
		 * @return the children of this container as an array
		 */
		IMergeViewerItem[] getChildren();
	}
}
