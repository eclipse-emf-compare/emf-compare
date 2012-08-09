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

import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.toArray;
import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer.adapt;

import com.google.common.collect.Iterables;

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGroup;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGrouper;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ComparisonNode;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class EMFCompareStructureMergeViewerContentProvider implements ITreeContentProvider {

	private final DifferenceGrouper fDifferenceGrouper;

	private final AdapterFactory fAdapterFactory;

	EMFCompareStructureMergeViewerContentProvider(AdapterFactory adapterFactory,
			DifferenceGrouper differenceGrouper) {
		this.fAdapterFactory = adapterFactory;
		this.fDifferenceGrouper = differenceGrouper;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// empty implementation
	}

	public void dispose() {
	}

	public Object getParent(Object element) {
		final Object ret;
		if (element instanceof IDiffElement) {
			ret = ((IDiffElement)element).getParent();
		} else if (element instanceof DifferenceGroup) {
			ret = ((DifferenceGroup)element).getComparison();
		} else {
			ret = null;
		}
		return ret;
	}

	public final boolean hasChildren(Object element) {
		final boolean ret;
		if (element instanceof ComparisonNode) {
			Comparison target = ((ComparisonNode)element).getTarget();
			final Iterable<? extends DifferenceGroup> groups = fDifferenceGrouper.getGroups(target);
			if (isEmpty(groups)) {
				ret = doHasChildren((ComparisonNode)element);
			} else {
				ret = true;
			}
		} else if (element instanceof IDiffContainer) {
			ret = doHasChildren((IDiffContainer)element);
		} else if (element instanceof DifferenceGroup) {
			ret = !isEmpty(((DifferenceGroup)element).getDifferences());
		} else {
			ret = false;
		}
		return ret;
	}

	private boolean doHasChildren(IDiffContainer element) {
		return element.hasChildren();
	}

	public final Object[] getChildren(Object element) {
		final Object[] ret;
		if (element instanceof ComparisonNode) {
			Comparison target = ((ComparisonNode)element).getTarget();
			final Iterable<? extends DifferenceGroup> groups = fDifferenceGrouper.getGroups(target);
			if (!isEmpty(groups)) {
				ret = Iterables.toArray(groups, DifferenceGroup.class);
			} else {
				ret = doGetChildren((IDiffContainer)element);
			}
		} else if (element instanceof IDiffContainer) {
			ret = doGetChildren((IDiffContainer)element);
		} else if (element instanceof DifferenceGroup) {
			Iterable<? extends Diff> differences = ((DifferenceGroup)element).getDifferences();
			Iterable<IDiffElement> diffNodes = adapt(differences, fAdapterFactory, IDiffElement.class);
			ret = toArray(diffNodes, IDiffElement.class);
		} else {
			ret = new Object[0];
		}
		return ret;
	}

	private Object[] doGetChildren(IDiffContainer diffContainer) {
		return diffContainer.getChildren();
	}

	public Object[] getElements(Object element) {
		return getChildren(element);
	}
}
