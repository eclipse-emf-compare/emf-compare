/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bugs 487595, 510442
 *     Martin Fleck - bug 483798
 *     Philip Langer - bug 527567
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.adapterfactory.context.IContextTester;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider.DelegatingTreeMergeViewerItemContentProvider;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider.MergeViewerItemProviderConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider.TreeContentMergeViewerItemLabelProvider;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProviderConfiguration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeContentMergeViewer extends AbstractTreeContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = TreeContentMergeViewer.class.getName();

	/**
	 * The {@link org.eclipse.emf.common.notify.AdapterFactory} used to create
	 * {@link AdapterFactoryContentProvider} and {@link AdapterFactoryLabelProvider} for ancestor, left and
	 * right {@link org.eclipse.jface.viewers.TreeViewer}.
	 */
	private final ComposedAdapterFactory fAdapterFactory;

	private AtomicBoolean fSyncExpandedState;

	/** The unmirrored content provider of this merge viewer. */
	protected TreeContentMergeViewerContentProvider fContentProvider;

	/** Label provider remembered for swapping sides in the viewer. */
	protected IBaseLabelProvider fLeftLabelProvider, fRightLabelProvider;

	/** Content provider remembered for swapping sides in the viewer. */
	protected IContentProvider fLeftContentProvider, fRightContentProvider;

	/**
	 * Creates a new {@link TreeContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param style
	 *            the style indicator for the parent
	 * @param bundle
	 *            the {@link ResourceBundle} for localization
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public TreeContentMergeViewer(int style, ResourceBundle bundle, Composite parent,
			EMFCompareConfiguration config) {
		super(style, bundle, config);

		Map<Object, Object> context = Maps.newLinkedHashMap();
		context.put(IContextTester.CTX_COMPARISON, config.getComparison());

		fAdapterFactory = new ComposedAdapterFactory(
				EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry(context));
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		fSyncExpandedState = new AtomicBoolean();

		buildControl(parent);
		fContentProvider = new TreeContentMergeViewerContentProvider(config);
		fLeftContentProvider = getLeftMergeViewer().getContentProvider();
		fRightContentProvider = getRightMergeViewer().getContentProvider();
		fLeftLabelProvider = getLeftMergeViewer().getLabelProvider();
		fRightLabelProvider = getRightMergeViewer().getLabelProvider();
		setMirrored(isMirrored());
	}

	protected ComposedAdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	/**
	 * Creates a new {@link TreeContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public TreeContentMergeViewer(Composite parent, EMFCompareConfiguration config) {
		this(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), parent, config);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fAdapterFactory.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractMergeViewer createMergeViewer(final Composite parent, final MergeViewerSide side) {
		final TreeMergeViewer mergeTreeViewer = new TreeMergeViewer(parent, side, this,
				getCompareConfiguration()) {
			@Override
			protected IAction createAction(MergeMode mode, Diff diff) {
				return new MergeAction(getCompareConfiguration(),
						EMFCompareRCPPlugin.getDefault().getMergerRegistry(), mode, null,
						new StructuredSelection(diff));
			}
		};
		final IContentProvider contentProvider = createMergeViewerContentProvider(side);
		mergeTreeViewer.setContentProvider(contentProvider);
		final AdapterFactoryLabelProvider labelProvider = new TreeContentMergeViewerItemLabelProvider(
				getResourceBundle(), getAdapterFactory(), side);
		mergeTreeViewer.setLabelProvider(labelProvider);

		hookListeners(mergeTreeViewer);

		return mergeTreeViewer;
	}

	/**
	 * Creates the {@link IContentProvider} used in the merge viewer.
	 * 
	 * @return the {@link IContentProvider} used in the merge viewer.
	 */
	protected IContentProvider createMergeViewerContentProvider(MergeViewerSide side) {
		final Comparison comparison = getCompareConfiguration().getComparison();

		if (comparison == null) {
			// We were called although there is nothing to show, return a dummy TreeContentProvider
			return new NullTreeContentProvider();
		}

		final IMergeViewerItemProviderConfiguration configuration = createMergeViewerItemProviderConfiguration(
				side);
		return new DelegatingTreeMergeViewerItemContentProvider(comparison, configuration);
	}

	protected IMergeViewerItemProviderConfiguration createMergeViewerItemProviderConfiguration(
			MergeViewerSide side) {
		return new MergeViewerItemProviderConfiguration(getAdapterFactory(), getDifferenceGroupProvider(),
				getDifferenceFilterPredicate(), getCompareConfiguration().getComparison(), side);
	}

	/**
	 * Adds all required listeners to the given {@link TreeMergeViewer}.
	 * 
	 * @param treeMergeViewer
	 *            the {@link TreeMergeViewer}.
	 */
	@Override
	protected void hookListeners(TreeMergeViewer treeMergeViewer) {
		treeMergeViewer.getStructuredViewer().getTree().addListener(SWT.Collapse,
				new ExpandCollapseListener(treeMergeViewer, false));
		treeMergeViewer.getStructuredViewer().getTree().addListener(SWT.Expand,
				new ExpandCollapseListener(treeMergeViewer, true));

		super.hookListeners(treeMergeViewer);
	}

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	protected final class ExpandCollapseListener implements Listener {
		/**
		 * 
		 */
		private final TreeMergeViewer mergeTreeViewer;

		private boolean expanded;

		/**
		 * @param mergeTreeViewer
		 */
		public ExpandCollapseListener(TreeMergeViewer mergeTreeViewer, boolean expanded) {
			this.mergeTreeViewer = mergeTreeViewer;
			this.expanded = expanded;
		}

		public void handleEvent(Event e) {
			Object data = e.item.getData();

			final List<Object> toBeExpanded = newArrayList();
			toBeExpanded.add(data);

			final Object parent;
			if (getLeftMergeViewer() == mergeTreeViewer) {
				parent = ((IMergeViewerItem)data).getLeft();
			} else if (getRightMergeViewer() == mergeTreeViewer) {
				parent = ((IMergeViewerItem)data).getRight();
			} else {
				parent = ((IMergeViewerItem)data).getAncestor();
			}
			Comparison comparison = getCompareConfiguration().getComparison();

			if (parent instanceof NotLoadedFragmentMatch) {
				IMergeViewerItem.Container left = new MergeViewerItem.Container(comparison, null,
						(Match)parent, MergeViewerSide.LEFT, getAdapterFactory());
				IMergeViewerItem.Container right = new MergeViewerItem.Container(comparison, null,
						(Match)parent, MergeViewerSide.RIGHT, getAdapterFactory());
				toBeExpanded.add(left);
				toBeExpanded.add(right);
			} else if (parent instanceof EObject) {
				Match match = comparison.getMatch((EObject)parent);
				if (match != null) {
					// We get all move differencies in order to detect the move of an element with an original
					// position outside the actual match of the diff. We have to do that since move
					// differencies are registered only under one container (left or right depending on the
					// situation)
					for (Diff referenceChange : filter(comparison.getDifferences(),
							and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE)))) {
						Match matchOfValue = comparison
								.getMatch(((ReferenceChange)referenceChange).getValue());
						if (matchOfValue != null) {
							Match leftContainerMatch = getContainerMatch(comparison, matchOfValue.getLeft());
							Match rightContainerMatch = getContainerMatch(comparison,
									matchOfValue.getRight());
							Match originContainerMatch = getContainerMatch(comparison,
									matchOfValue.getOrigin());

							// if one of the container match is equal to the diff match, then the move have is
							// origin or destination in the eContainer of the diff. We have to expend the
							// eContainer of the diff too
							if (leftContainerMatch == match || rightContainerMatch == match
									|| originContainerMatch == match) {
								if (leftContainerMatch != null && leftContainerMatch != match) {
									IMergeViewerItem.Container container = new MergeViewerItem.Container(
											comparison, null, leftContainerMatch, MergeViewerSide.LEFT,
											getAdapterFactory());
									toBeExpanded.add(container);
								}
								if (rightContainerMatch != null && rightContainerMatch != match) {
									IMergeViewerItem.Container container = new MergeViewerItem.Container(
											comparison, null, rightContainerMatch, MergeViewerSide.RIGHT,
											getAdapterFactory());
									toBeExpanded.add(container);
								}
								if (originContainerMatch != null && originContainerMatch != match) {
									IMergeViewerItem.Container container = new MergeViewerItem.Container(
											comparison, null, originContainerMatch, MergeViewerSide.ANCESTOR,
											getAdapterFactory());
									toBeExpanded.add(container);
								}
							}
						}
					}
				}
			}

			try {
				if (fSyncExpandedState.compareAndSet(false, true)) {
					for (Object object : toBeExpanded) {
						getLeftMergeViewer().setExpandedState(object, expanded);
						getRightMergeViewer().setExpandedState(object, expanded);
						getAncestorMergeViewer().setExpandedState(object, expanded);
					}
				}
			} finally {
				getCenterControl().redraw();
				fSyncExpandedState.set(false);
			}
		}
	}

	/**
	 * Return the eContainer match of the given eObject.
	 * 
	 * @param comparison
	 *            The actual comparison
	 * @param value
	 *            The element of which we want the container match
	 * @return the match if found, null otherwise
	 */
	private Match getContainerMatch(Comparison comparison, EObject value) {
		EObject eContainer; // XXX: use itemProvider.getParent().
		if (value != null) {
			eContainer = value.eContainer();
			return comparison.getMatch(eContainer);
		}
		return null;
	}

	/**
	 * Dummy {@link ITreeContentProvider} which does not return anything.
	 */
	private class NullTreeContentProvider implements ITreeContentProvider {

		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}

		public Object[] getChildren(Object parentElement) {
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

	}

	@Override
	protected IContentProvider getUnmirroredContentProvider() {
		return fContentProvider;
	}

	@Override
	protected IContentProvider getMirroredContentProvider() {
		return new MirroredTreeContentMergeViewerContentProvider(getCompareConfiguration(), fContentProvider);
	}

	@Override
	protected void updateMirrored(boolean isMirrored) {
		if (isMirrored) {
			getLeftMergeViewer().setContentProvider(fRightContentProvider);
			getLeftMergeViewer().setLabelProvider(fRightLabelProvider);
			getRightMergeViewer().setContentProvider(fLeftContentProvider);
			getRightMergeViewer().setLabelProvider(fLeftLabelProvider);
		} else {
			getLeftMergeViewer().setContentProvider(fLeftContentProvider);
			getLeftMergeViewer().setLabelProvider(fLeftLabelProvider);
			getRightMergeViewer().setContentProvider(fRightContentProvider);
			getRightMergeViewer().setLabelProvider(fRightLabelProvider);
		}
		super.updateMirrored(isMirrored);
	}
}
