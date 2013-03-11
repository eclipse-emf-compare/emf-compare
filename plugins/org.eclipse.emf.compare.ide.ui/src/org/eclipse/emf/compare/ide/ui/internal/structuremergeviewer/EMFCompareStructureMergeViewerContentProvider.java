/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ComparisonNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Specialized AdapterFactoryContentProvider for the emf compare structure merge viewer.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class EMFCompareStructureMergeViewerContentProvider extends AdapterFactoryContentProvider {

	/** The viewer grouper associated with this content provider. */
	private final StructureMergeViewerGrouper fViewerGrouper;

	/** The viewer filter associated with this content provider. */
	private final StructureMergeViewerFilter fViewerFilter;

	/**
	 * Constructs the content provider with the appropriate adapter factory.
	 * 
	 * @param adapterFactory
	 *            The adapter factory used to construct the content provider.
	 * @param structureMergeViewerGrouper
	 *            The viewer grouper associated with this content provider.
	 * @param structureMergeViewerFilter
	 *            The viewer filter associated with this content provider.
	 */
	public EMFCompareStructureMergeViewerContentProvider(AdapterFactory adapterFactory,
			StructureMergeViewerGrouper structureMergeViewerGrouper,
			StructureMergeViewerFilter structureMergeViewerFilter) {
		super(adapterFactory);
		this.fViewerGrouper = structureMergeViewerGrouper;
		this.fViewerFilter = structureMergeViewerFilter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getParent(Object object)
	 */
	@Override
	public Object getParent(Object element) {
		Object ret;
		if (element instanceof Adapter) {
			ret = getAdapterFactory().adapt(super.getParent(((Adapter)element).getTarget()),
					ICompareInput.class);
		} else if (element instanceof IDifferenceGroup) {
			ret = ((IDifferenceGroup)element).getComparison();
		} else {
			ret = null;
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
		if (element instanceof ComparisonNode) {
			Comparison target = ((ComparisonNode)element).getTarget();
			final Iterable<? extends IDifferenceGroup> groups = fViewerGrouper.getGroups(target);
			if (isEmpty(groups)) {
				ret = super.hasChildren(((Adapter)element).getTarget());
			} else {
				ret = true;
			}
		} else if (element instanceof IDifferenceGroup) {
			ret = !isEmpty(((IDifferenceGroup)element).getDifferences());
		} else if (element instanceof Adapter) {
			ret = super.hasChildren(((Adapter)element).getTarget());
		} else {
			ret = false;
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
		final Object[] ret;
		if (element instanceof ComparisonNode) {
			Comparison target = ((ComparisonNode)element).getTarget();
			final Iterable<? extends IDifferenceGroup> groups = fViewerGrouper.getGroups(target);
			if (!isEmpty(groups)) {
				ret = Iterables.toArray(groups, IDifferenceGroup.class);
			} else {
				Iterable<ICompareInput> compareInputs = adapt(super.getChildren(((Adapter)element)
						.getTarget()), getAdapterFactory(), ICompareInput.class);
				ret = toArray(compareInputs, ICompareInput.class);
			}
		} else if (element instanceof IDifferenceGroup) {
			final Comparison target = ((IDifferenceGroup)element).getComparison();
			Iterable<ICompareInput> compareInputs = adapt(super.getChildren(target), getAdapterFactory(),
					ICompareInput.class);
			Iterable<FilteredEDiffNode> filteredCompareInputs = filteredEDiffNodes(filter(compareInputs,
					AbstractEDiffNode.class), (IDifferenceGroup)element);
			ret = toArray(filteredCompareInputs, FilteredEDiffNode.class);
		} else if (element instanceof Adapter) {
			final Iterable<Object> children;
			if (element instanceof FilteredEDiffNode) {
				children = filteredElements(super.getChildren(((Adapter)element).getTarget()),
						((FilteredEDiffNode)element).getDifferenceGroup());
			} else {
				children = Lists.newArrayList(super.getChildren(((Adapter)element).getTarget()));
			}
			Iterable<ICompareInput> compareInputs = adapt(children, getAdapterFactory(), ICompareInput.class);
			if (element instanceof FilteredEDiffNode) {
				Iterable<FilteredEDiffNode> filteredCompareInputs = filteredEDiffNodes(filter(compareInputs,
						AbstractEDiffNode.class), ((FilteredEDiffNode)element).getDifferenceGroup());
				ret = toArray(filteredCompareInputs, FilteredEDiffNode.class);
			} else {
				ret = toArray(compareInputs, ICompareInput.class);
			}
		} else {
			ret = new Object[0];
		}
		return ret;
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
	static <T> Iterable<T> adapt(Iterable<?> iterable, final AdapterFactory adapterFactory,
			final Class<T> type) {
		Function<Object, Object> adaptFunction = new Function<Object, Object>() {
			public Object apply(Object input) {
				return adapterFactory.adapt(input, type);
			}
		};
		return filter(transform(iterable, adaptFunction), type);
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
	static <T> Iterable<T> adapt(Object[] iterable, final AdapterFactory adapterFactory, final Class<T> type) {
		return adapt(Lists.newArrayList(iterable), adapterFactory, type);
	}

	/**
	 * Filter out each element of the given <code>array</code> that should not be contained under the given
	 * {@link IDifferenceGroup}.
	 * 
	 * @param unfiltered
	 *            the array to filter.
	 * @param diffGroup
	 *            the given {@link IDifferenceGroup}.
	 * @return an iterable of elements that should be contained under the given {@link IDifferenceGroup}.
	 */
	private Iterable<Object> filteredElements(Object[] unfiltered, final IDifferenceGroup diffGroup) {
		final Predicate<? super Object> isPartOfTree = new Predicate<Object>() {
			public boolean apply(Object input) {
				return isPartOfGroup(input, diffGroup);
			}
		};
		return filter(Lists.newArrayList(unfiltered), isPartOfTree);
	}

	/**
	 * Returns whether this object should be contained under the given {@link IDifferenceGroup}.
	 * 
	 * @param object
	 *            the object to filter.
	 * @param diffGroup
	 *            the given {@link IDifferenceGroup}.
	 * @return true if the object should be contained under the given {@link IDifferenceGroup}, false
	 *         otherwise.
	 */
	private boolean isPartOfGroup(Object object, final IDifferenceGroup diffGroup) {
		final Predicate<? super EObject> isPartOfTree = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return Iterables.contains(filter(diffGroup.getDifferences(), not(or(fViewerFilter
						.getPredicates()))), input);
			}
		};
		boolean ret = false;
		if (object instanceof Match) {
			ret = Iterables.any(((Match)object).getAllDifferences(), isPartOfTree);
		} else if (object instanceof Diff) {
			if (!isPartOfTree.apply((Diff)object)) {
				if (object instanceof ReferenceChange
						&& ((ReferenceChange)object).getReference().isContainment()) {
					ret = Iterables.any(((Diff)object).getMatch().getComparison().getMatch(
							((ReferenceChange)object).getValue()).getAllDifferences(), isPartOfTree);
				}
			} else {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Creates an iterable of {@link FilteredEDiffNode} from an iterable of {@link AbstractEDiffNode} and a
	 * {@link IDifferenceGroup}.
	 * 
	 * @param iterable
	 *            the iterable of {@link AbstractEDiffNode} to transform.
	 * @param diffGroup
	 *            the {@link IDifferenceGroup} to associate with the FilteredEDiffNodes.
	 * @return an iterable of {@link FilteredEDiffNode}.
	 */
	private Iterable<FilteredEDiffNode> filteredEDiffNodes(Iterable<AbstractEDiffNode> iterable,
			final IDifferenceGroup diffGroup) {
		Function<AbstractEDiffNode, FilteredEDiffNode> adaptFunction = new Function<AbstractEDiffNode, FilteredEDiffNode>() {
			public FilteredEDiffNode apply(AbstractEDiffNode input) {
				return new FilteredEDiffNode(input, diffGroup);
			}
		};
		return transform(iterable, adaptFunction);
	}

	/**
	 * AbstractEDiffNodes that know the IDifferenceGroup in which they are. This class wraps an
	 * AbstractEDiffNode and it delegates its interfaces to corresponding AbstractEDiffNode implemented
	 * interfaces.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private class FilteredEDiffNode implements ICompareInput, Adapter {

		/** The AbstractEDiffNode wrapped. */
		private AbstractEDiffNode fDelegate;

		/** The IDifferenceGroup associated with the node. */
		private IDifferenceGroup iDifferenceGroup;

		/**
		 * This constructs an instance that wraps this {@link AbstractEDiffNode} contained in the given
		 * {@link IDifferenceGroup}.
		 * 
		 * @param delegate
		 *            the given AbstractEDiffNode.
		 * @param diffGroup
		 *            the given IDifferenceGroup.
		 */
		public FilteredEDiffNode(AbstractEDiffNode delegate, IDifferenceGroup diffGroup) {
			fDelegate = delegate;
			iDifferenceGroup = diffGroup;
		}

		/**
		 * Returns the backing delegate instance that methods are forwarded to.
		 * 
		 * @return the wrapped AbstractEDiffNode.
		 */
		private AbstractEDiffNode delegate() {
			return fDelegate;
		}

		/**
		 * Returns the {@link IDifferenceGroup} associated with the wrapped {@link AbstractEDiffNode}.
		 * 
		 * @return the difference group.
		 */
		public IDifferenceGroup getDifferenceGroup() {
			return iDifferenceGroup;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getName()
		 */
		public String getName() {
			return delegate().getName();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getImage()
		 */
		public Image getImage() {
			return delegate().getImage();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getKind()
		 */
		public int getKind() {
			return delegate().getKind();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
		 */
		public ITypedElement getAncestor() {
			return delegate().getAncestor();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
		 */
		public ITypedElement getLeft() {
			return delegate().getLeft();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
		 */
		public ITypedElement getRight() {
			return delegate().getRight();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#addCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
		 */
		public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
			delegate().addCompareInputChangeListener(listener);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#removeCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
		 */
		public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
			delegate().removeCompareInputChangeListener(listener);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
		 */
		public void copy(boolean leftToRight) {
			delegate().copy(leftToRight);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
		 */
		public void notifyChanged(Notification notification) {
			delegate().notifyChanged(notification);

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
		 */
		public Notifier getTarget() {
			return delegate().getTarget();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
		 */
		public void setTarget(Notifier newTarget) {
			delegate().setTarget(newTarget);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(Object)
		 */
		public boolean isAdapterForType(Object type) {
			return delegate().isAdapterForType(type);
		}
	}
}
