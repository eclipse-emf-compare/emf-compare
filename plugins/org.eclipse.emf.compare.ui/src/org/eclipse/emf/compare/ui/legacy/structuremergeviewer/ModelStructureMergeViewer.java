/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.structuremergeviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.MatchElement;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelStructureMergeViewer extends TreeViewer {

	private final NoDiffFilter noDiffFilter = new NoDiffFilter();

	private class NoDiffFilter extends ViewerFilter {

		/**
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(final Viewer viewer, final Object parentElement,
				final Object element) {
			if (element instanceof MatchElement) {
				// return ((MatchElement) element).hasDiffs();
				// FIXMECBR
			}
			return true;
		}

	}

	private final PseudoConflictFilter pseudoConflictFilter = new PseudoConflictFilter();

	private class PseudoConflictFilter extends ViewerFilter {

		/**
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(final Viewer viewer, final Object parentElement,
				final Object element) {
			// if (element instanceof Delta)
			// {
			// return ((Delta) element).getKind() <=
			// DiffConstants.PSEUDO_CONFLICT;
			// FIXMECBR
			// }
			return true;
		}

	}

	private static final class ProjectAdapterFactoryProvider {

		private static ComposedAdapterFactory adapterFactory;

		public final static List<AdapterFactory> createFactoryList() {
			final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
			// this is my provider generated in the .Edit plugin for me. Replace
			// with yours.
			factories.add(new DiffItemProviderAdapterFactory());
			// these are EMF generic providers
			factories.add(new ResourceItemProviderAdapterFactory());
			factories.add(new ReflectiveItemProviderAdapterFactory());
			// ... add other provider adapter factories for your model as needed
			// if you don't know what to add, look at the creation of the
			// adapter factory
			// in your generated editor
			return factories;
		}

		public final static ComposedAdapterFactory getAdapterFactory() {
			if (adapterFactory == null) {
				adapterFactory = new ComposedAdapterFactory(createFactoryList());
			}
			return adapterFactory;
		}
	}

	private class DiffLabelProvider extends AdapterFactoryLabelProvider {

		public DiffLabelProvider() {
			super(ProjectAdapterFactoryProvider.getAdapterFactory());
		}

		/**
		 * Returns the platform icon for a file. You can replace with your own
		 * icon If not a IFile, then passes to the regular EMF.Edit providers
		 */
		public Image getImage(final Object object) {
			if (object instanceof IFile) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FILE);
			}
			return super.getImage(object);
		}

		public String getText(final Object object) {
			if (object instanceof IFile) {
				return ((IFile) object).getName();
			}
			return super.getText(object);
		}
	}

	public ModelStructureMergeViewer(final Composite parent,
			final CompareConfiguration config) {
		super(parent);
		setLabelProvider(new DiffLabelProvider());
		setUseHashlookup(true);
		addFilter(this.noDiffFilter);
		addFilter(this.pseudoConflictFilter);

	}

	/**
	 * @param delta
	 */
	public void navigateToDelta(final Match2Elements delta) {
		boolean isPresent = true;
		for (final ViewerFilter filter : getFilters()) {
			isPresent = isPresent
					&& filter.select(this, delta.eContainer(), delta);
		}
		if (!isPresent) {
			return;
		}
		final Object result = findItem(delta);
		TreeItem resultItem = null;
		if (result instanceof Tree) // root
		{
			resultItem = getTree().getItem(0);
		} else {
			if (result instanceof TreeItem) {
				resultItem = (TreeItem) result;
			} else {
				// shouldn't be there
				resultItem = null;
				assert (false);
			}
		}

		if (resultItem == null) // we expand the tree to recuperate the element.
		{
			expandAll();

			resultItem = (TreeItem) findItem(delta);
		}

		collapseToLevel(resultItem, 0);
		getTree().setSelection(resultItem);
		getTree().showSelection();

	}

	/**
	 * @param diff
	 */
	public void navigateToDiff(final DiffElement diff) {
		final Object result = findItem(diff);
		TreeItem resultItem = null;
		if (result instanceof Tree) // root
		{
			resultItem = getTree().getItem(0);
		} else {
			if (result instanceof TreeItem) {
				resultItem = (TreeItem) result;
			} else {
				// shouldn't be there
				resultItem = null;
				assert (false);
			}
		}

		if (resultItem == null) // we expand the tree to recuperate the element.
		{
			expandAll();

			resultItem = (TreeItem) findItem(diff);
		}

		collapseToLevel(resultItem, 0);
		getTree().setSelection(resultItem);
		getTree().showSelection();

	}

	public void setPseudoConflictFilterEnabled(final boolean b) {
		if (b) {
			this.addFilter(this.pseudoConflictFilter);
		} else {
			this.removeFilter(this.pseudoConflictFilter);
		}
	}

	public void setNoDiffFilterEnabled(final boolean b) {
		if (b) {
			this.addFilter(this.noDiffFilter);
		} else {
			this.removeFilter(this.noDiffFilter);
		}
	}
}
