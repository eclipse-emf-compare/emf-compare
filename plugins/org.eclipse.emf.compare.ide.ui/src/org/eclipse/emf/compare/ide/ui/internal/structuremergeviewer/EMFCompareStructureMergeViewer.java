/*******************************************************************************
 * Copyright (c) 2013, 2022 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 467191
 *     Philip Langer - bug 462884, 516576, 521948, 522372, 514079
 *     Stefan Dirix - bugs 473985, 474030
 *     Martin Fleck - bug 497066, 483798, 514767, 514415
 *     Alexandra Buzila - bug 513931
 *     Martin Fleck - bug 580408
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_ASYNC;
import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_SYNC;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Predicate;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.compare.INavigatable;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.adapterfactory.context.IContextTester;
import org.eclipse.emf.compare.command.CommandStackEvent;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.internal.utils.DisposableResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.configuration.ForwardingCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoDifferencesCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoSelectedItemCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoVisibleItemCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.OnlyPseudoConflictsCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.EMFCompareColor;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeInput;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.EmptyComparisonScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressInfoComposite;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressMonitorWrapper;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.FetchListener;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeContainedAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeContainedConflictingAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeContainedNonConflictingAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.RevealConflictingDiffsAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeCompareInputAdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.util.CompareHandlerService;
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
import org.eclipse.emf.compare.internal.merge.MergeDataImpl;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.CachingDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IMergePreviewModeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.SideLabelProvider;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IColorChangeEvent;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.match.MatchOfContainmentReferenceChangeProcessor;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProviderChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.IDiagnosable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.team.internal.ui.mapping.ResourceDiffCompareInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.progress.PendingUpdateAdapter;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * Implementation of {@link AbstractStructuredViewerWrapper}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EMFCompareStructureMergeViewer extends AbstractStructuredViewerWrapper<CTabFolder, WrappableTreeViewer> implements CommandStackListener {

	private final class CompareInputChangedJob extends Job {
		private CompareInputChangedJob(String name) {
			super(name);
		}

		@Override
		public IStatus run(IProgressMonitor monitor) {
			IProgressMonitor wrapper = new JobProgressMonitorWrapper(monitor, progressInfoItem);
			SubMonitor subMonitor = SubMonitor.convert(wrapper, EMFCompareIDEUIMessages
					.getString("EMFCompareStructureMergeViewer.computingModelDifferences"), 100); //$NON-NLS-1$
			try {
				compareInputChanged((ICompareInput)getInput(), subMonitor.newChild(100));
			} catch (final OperationCanceledException e) {
				return Status.CANCEL_STATUS;
			} catch (final Exception e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			} finally {
				subMonitor.setWorkRemaining(0);
			}
			return Status.OK_STATUS;
		}
	}

	private final class TitleBuilderJob extends Job {

		public TitleBuilderJob() {
			super("EMF Compare Title Builder"); //$NON-NLS-1$
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			EMFCompareStructureMergeViewerContentProvider contentProvider = getContentProvider();
			if (contentProvider != null) {
				contentProvider.runWhenReady(IN_UI_ASYNC, new Runnable() {
					public void run() {
						CTabFolder control = getControl();
						if (!control.isDisposed()) {
							final String title = new TitleBuilder(getCompareConfiguration()).toString();
							((CompareViewerSwitchingPane)control.getParent()).setTitleArgument(title);
						}
					}
				});
			}
			return Status.OK_STATUS;
		}
	}

	private final class DiffRelationshipComputerJob extends Job {
		public DiffRelationshipComputerJob() {
			super("EMF Compare Diff Relationship Computer"); //$NON-NLS-1$
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			Comparison comparison = getCompareConfiguration().getComparison();
			if (comparison != null) {
				fDiffRelationshipComputer.invalidate();

				int availableProcessors = Runtime.getRuntime().availableProcessors();
				ThreadFactory computingThreadFactory = new ThreadFactoryBuilder()
						.setNameFormat("EMFCompareDiffRelationshipComputer--%d") //$NON-NLS-1$
						.build();

				ExecutorService pool = Executors.newFixedThreadPool(availableProcessors,
						computingThreadFactory);

				List<Diff> differences = comparison.getDifferences();
				for (final Diff diff : differences) {
					pool.submit(new Runnable() {
						public void run() {
							fDiffRelationshipComputer.computeCache(diff);
						}
					});
				}

				pool.shutdown();

				while (!pool.isTerminated()) {
					if (monitor.isCanceled()) {
						pool.shutdownNow();
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}

			return Status.OK_STATUS;
		}
	}

	/** The width of the tree ruler. */
	private static final int TREE_RULER_WIDTH = 17;

	private static final Function<TreeNode, Diff> TREE_NODE_AS_DIFF = new Function<TreeNode, Diff>() {
		public Diff apply(TreeNode input) {
			if (input.getData() instanceof Diff) {
				return (Diff)input.getData();
			}
			return null;
		}
	};

	/** Preference store holding UI-related settings for this viewer. */
	protected final IPreferenceStore preferenceStore = EMFCompareIDEUIPlugin.getDefault()
			.getPreferenceStore();

	/** The adapter factory. */
	private ComposedAdapterFactory fAdapterFactory;

	/** The diff relationship computer. */
	private CachingDiffRelationshipComputer fDiffRelationshipComputer;

	private final Job diffRelationshipComputer = new DiffRelationshipComputerJob();

	/** The tree ruler associated with this viewer. */
	private EMFCompareDiffTreeRuler treeRuler;

	private final ICompareInputChangeListener fCompareInputChangeListener;

	/** The expand/collapse item listener. */
	private ITreeViewerListener fWrappedTreeListener;

	/** The tree viewer. */

	/** The undo action. */
	private UndoAction undoAction;

	/** The redo action. */
	private RedoAction redoAction;

	/** The compare handler service. */
	private CompareHandlerService fHandlerService;

	/**
	 * When comparing EObjects from a resource, the resource involved doesn't need to be unload by EMF
	 * Compare.
	 */
	private boolean resourceSetShouldBeDisposed;

	private DependencyData dependencyData;

	private ISelectionChangedListener selectionChangeListener;

	private List<ISelectionChangedListener> selectionChangeListeners;

	/** The current selection. */
	protected ISelection currentSelection;

	/** Listener reacting to changes in the {@link #preferenceStore}. */
	protected IPropertyChangeListener preferenceChangeListener;

	private final Listener fEraseItemListener;

	private JobProgressInfoComposite progressInfoItem;

	private Job inputChangedTask;

	private final Job titleBuilderJob = new TitleBuilderJob();

	private CompareToolBar toolBar;

	private Navigatable navigatable;

	private EMFCompareColor fColors;

	/** List of domains we've created ourselves and should thus cleanup ourselves. */
	private final List<IDisposable> disposableDomains = new ArrayList<>();

	private FetchListener toolbarUpdaterContentProviderListener;

	private boolean cascadingDifferencesFilterEnabled;

	private boolean inChange;

	private final ForwardingCompareConfiguration.MirroredPropertyChangeListener mirroredPropertyChangeListener;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the SWT parent control under which to create the viewer's SWT control.
	 * @param config
	 *            a compare configuration the newly created viewer might want to use.
	 */
	public EMFCompareStructureMergeViewer(Composite parent, EMFCompareConfiguration config) {
		super(parent, config);

		updateLayout(true, false);

		StructureMergeViewerFilter structureMergeViewerFilter = getCompareConfiguration()
				.getStructureMergeViewerFilter();
		getViewer().addFilter(structureMergeViewerFilter);

		StructureMergeViewerGrouper structureMergeViewerGrouper = getCompareConfiguration()
				.getStructureMergeViewerGrouper();
		structureMergeViewerGrouper.install(getViewer());

		fCompareInputChangeListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput input) {
				EMFCompareStructureMergeViewer.this.compareInputChanged(input);
			}
		};

		setContentProvider(new EMFCompareStructureMergeViewerContentProvider(
				getCompareConfiguration().getAdapterFactory(), getViewer()));

		navigatable = new Navigatable(getViewer(), getContentProvider());
		getControl().setData(INavigatable.NAVIGATOR_PROPERTY, navigatable);

		toolBar = createToolBar(CompareViewerPane.getToolBarManager(parent));
		getViewer().addSelectionChangedListener(getToolBar());

		createContextMenu();

		selectionChangeListeners = new ArrayList<ISelectionChangedListener>();

		selectionChangeListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChangedEvent(event);
			}
		};
		addSelectionChangedListener(selectionChangeListener);

		preferenceChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				EMFCompareStructureMergeViewer.this.handlePreferenceChangedEvent(event);
			}
		};
		preferenceStore.addPropertyChangeListener(preferenceChangeListener);

		fWrappedTreeListener = new ITreeViewerListener() {
			public void treeExpanded(TreeExpansionEvent event) {
				treeRuler.redraw();
			}

			public void treeCollapsed(TreeExpansionEvent event) {
				treeRuler.redraw();
			}
		};
		getViewer().addTreeListener(fWrappedTreeListener);

		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				handleEraseItemEvent(event);
			}
		};
		getViewer().getControl().addListener(SWT.EraseItem, fEraseItemListener);

		fHandlerService = CompareHandlerService.createFor(getCompareConfiguration().getContainer(),
				getControl().getShell());

		toolbarUpdaterContentProviderListener = new FetchListener() {

			private boolean enabled;

			@Override
			public void startFetching() {
				enabled = getToolBar().isEnabled();
				getToolBar().setEnabled(false);
			}

			@Override
			public void doneFetching() {
				// Only try to enable the toolbar if it was enabled and is not currently enabled.
				if (enabled && !getToolBar().isEnabled()) {
					getToolBar().setEnabled(true);
				}
			}
		};

		getContentProvider().addFetchingListener(toolbarUpdaterContentProviderListener);

		setLabelProvider(
				new DelegatingStyledCellLabelProvider(new EMFCompareStructureMergeViewerLabelProvider(
						getCompareConfiguration().getAdapterFactory(), this)));

		undoAction = new UndoAction(getCompareConfiguration().getEditingDomain());
		redoAction = new RedoAction(getCompareConfiguration().getEditingDomain());

		editingDomainChange(null, getCompareConfiguration().getEditingDomain());

		inputChangedTask.setPriority(Job.LONG);

		config.getEventBus().register(this);

		final boolean enabled = any(config.getStructureMergeViewerFilter().getSelectedDifferenceFilters(),
				instanceOf(CascadingDifferencesFilter.class));
		setCascadingDifferencesFilterEnabled(enabled);

		mirroredPropertyChangeListener = new ForwardingCompareConfiguration.MirroredPropertyChangeListener() {
			@Override
			protected void mirroredPropertyChanged(boolean mirrored) {
				EMFCompareConfiguration compareConfiguration = getCompareConfiguration();
				MergeMode mergePreviewMode = compareConfiguration.getMergePreviewMode();
				if (mergePreviewMode == MergeMode.LEFT_TO_RIGHT
						|| mergePreviewMode == MergeMode.RIGHT_TO_LEFT) {
					compareConfiguration.setMergePreviewMode(mergePreviewMode.inverse());
				}
			}
		};
		getCompareConfiguration().addPropertyChangeListener(mirroredPropertyChangeListener);
	}

	protected CompareToolBar createToolBar(ToolBarManager manager) {
		return new CompareToolBar(manager, getCompareConfiguration().getStructureMergeViewerGrouper(),
				getCompareConfiguration().getStructureMergeViewerFilter(), getCompareConfiguration());
	}

	/**
	 * The tool bar must be init after we know the editable state of left and right input.
	 * 
	 * @see #compareInputChanged(ICompareInput, IProgressMonitor)
	 */
	protected void initToolbar(IProgressMonitor monitor) {
		if (!monitor.isCanceled()) {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					getToolBar().initToolbar(getViewer(), getNavigatable(), fHandlerService);
					getToolBar().setEnabled(false);
				}
			});
		}
	}

	protected void enableToolbar(IProgressMonitor monitor) {
		if (!monitor.isCanceled()) {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					getToolBar().setEnabled(true);
				}
			});
		}
	}

	/**
	 * Returns the toolbar for this Structure merge viewer.
	 * 
	 * @return The {@link CompareToolBar}.
	 */
	protected CompareToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * Returns the {@link Navigatable} for this structure merge viewer.
	 * 
	 * @return The {@link Navigatable}.
	 */
	public Navigatable getNavigatable() {
		return navigatable;
	}

	/**
	 * Allow users to merge diffs through context menu.
	 */
	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EMFCompareStructureMergeViewer.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(getViewer().getControl());
		getViewer().getControl().setMenu(menu);
	}

	/**
	 * Fill the context menu with the appropriate actions (ACCEPT/REJECT or LEFT TO RIGHT/RIGHT TO LEFT
	 * depending on the {@link org.eclipse.emf.compare.internal.merge.MergeMode}, and the write access of
	 * models in input).
	 * 
	 * @param manager
	 *            the context menu to fill.
	 */
	private void fillContextMenu(IMenuManager manager) {
		if (!isMergeableItemSelected()) {
			return;
		}
		boolean leftEditable = getCompareConfiguration().isLeftEditable();
		boolean rightEditable = getCompareConfiguration().isRightEditable();
		final EnumSet<MergeMode> modes;
		if (rightEditable && leftEditable) {
			modes = EnumSet.of(MergeMode.RIGHT_TO_LEFT, MergeMode.LEFT_TO_RIGHT);
		} else {
			modes = EnumSet.of(MergeMode.ACCEPT, MergeMode.REJECT);
		}

		if (rightEditable || leftEditable) {
			IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
			if (isDiffSelected()) {
				addSingleDiffMergeActions(manager, modes, mergerRegistry);
			} else if (isOneMatchOrResourceMatchSelected() || isOneGroupSelected()) {
				addMergeNonConflictingActions(manager, modes, mergerRegistry);
				manager.add(new Separator());
				addMergeConflictingActions(manager, modes, mergerRegistry);

				if (leftEditable && !rightEditable) {
					manager.add(new Separator());
					addMergeAllActions(manager, modes, mergerRegistry);
				}
			}
		}
		if (isOneDiffSelected() && getCompareConfiguration().getComparison().isThreeWay()) {
			final ISelection selection = getSelection();
			if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
				Object element = ((IStructuredSelection)selection).getFirstElement();
				Object selectedData = getDataOfTreeNodeOfAdapter(element);
				if (selectedData instanceof Diff && ((Diff)selectedData).getConflict() != null) {
					manager.add(new Separator());
					manager.add(new RevealConflictingDiffsAction(this, (Diff)selectedData,
							getCompareConfiguration()));
				}
			}
		}
	}

	/**
	 * This will add the merge actions targeting a single selected difference to the given menu manager.
	 * 
	 * @param manager
	 *            The menu manager in which we'll be adding actions.
	 * @param modes
	 *            The current merge modes.
	 * @param mergerRegistry
	 *            The merger registry the new actions need to use.
	 */
	private void addSingleDiffMergeActions(IMenuManager manager, EnumSet<MergeMode> modes,
			IMerger.Registry mergerRegistry) {
		for (MergeMode mode : modes) {
			MergeAction mergeAction = new MergeAction(getCompareConfiguration(), mergerRegistry, mode,
					getNavigatable(), (IStructuredSelection)getSelection());
			manager.add(mergeAction);
		}
	}

	/**
	 * This will add the "merge all non-conflicting" actions to the given menu manager. These actions are
	 * meant to allow a user to merge all non-conflicting diffs under the given selection at once, leaving
	 * only conflicts untouched.
	 * 
	 * @param manager
	 *            The menu manager in which we'll be adding actions.
	 * @param modes
	 *            The current merge modes.
	 * @param mergerRegistry
	 *            The merger registry the new actions need to use.
	 */
	private void addMergeNonConflictingActions(IMenuManager manager, EnumSet<MergeMode> modes,
			IMerger.Registry mergerRegistry) {
		final Predicate<TreeNode> filterPredicate = new Predicate<TreeNode>() {
			public boolean test(TreeNode input) {
				return input != null && JFaceUtil.isFiltered(getViewer(), input, input.getParent());
			}
		};
		for (MergeMode mode : modes) {
			MergeContainedNonConflictingAction mergeAction = new MergeContainedNonConflictingAction(
					getCompareConfiguration(), mergerRegistry, mode, getNavigatable(),
					(IStructuredSelection)getSelection(), filterPredicate);
			manager.add(mergeAction);
		}
	}

	/**
	 * This will add the "merge non-conflicting" actions to the given menu manager. These actions are meant to
	 * allow a user to merge all conflicting diffs under the given selection at once, leaving non-conflicting
	 * diffs untouched.
	 * 
	 * @param manager
	 *            The menu manager in which we'll be adding actions.
	 * @param modes
	 *            The current merge modes.
	 * @param mergerRegistry
	 *            The merger registry the new actions need to use.
	 */
	private void addMergeConflictingActions(IMenuManager manager, EnumSet<MergeMode> modes,
			IMerger.Registry mergerRegistry) {
		final Predicate<TreeNode> filterPredicate = new Predicate<TreeNode>() {
			public boolean test(TreeNode input) {
				return input != null && JFaceUtil.isFiltered(getViewer(), input, input.getParent());
			}
		};
		for (MergeMode mode : modes) {
			MergeContainedConflictingAction mergeAction = new MergeContainedConflictingAction(
					getCompareConfiguration(), mergerRegistry, mode, getNavigatable(),
					(IStructuredSelection)getSelection(), filterPredicate);
			manager.add(mergeAction);
		}
	}

	/**
	 * This will add actions to the given menu manager that will allow a user to merge everything at once in a
	 * given direction, effectively taking the left- or right-side contents as the new left.
	 * 
	 * @param manager
	 *            The menu manager in which we'll be adding actions.
	 * @param modes
	 *            The current merge modes.
	 * @param mergerRegistry
	 *            The merger registry the new actions need to use.
	 */
	private void addMergeAllActions(IMenuManager manager, EnumSet<MergeMode> modes,
			IMerger.Registry mergerRegistry) {
		final Predicate<TreeNode> filterPredicate = new Predicate<TreeNode>() {
			public boolean test(TreeNode input) {
				return input != null && JFaceUtil.isFiltered(getViewer(), input, input.getParent());
			}
		};
		for (MergeMode mode : modes) {
			MergeContainedAction mergeAllAction = new MergeContainedAction(getCompareConfiguration(),
					mergerRegistry, mode, getNavigatable(), (IStructuredSelection)getSelection(),
					filterPredicate);
			manager.add(mergeAllAction);
		}
	}

	/**
	 * Check if the item selected in this viewer is mergeable; that is, if a {@link Diff}, a {@link Match}, or
	 * {@link MatchResource} is selected.
	 * 
	 * @return true if the item selected is mergeable, false otherwise.
	 */
	private boolean isMergeableItemSelected() {
		return isDiffSelected() || isOneMatchOrResourceMatchSelected() || isOneGroupSelected();
	}

	/**
	 * Specifies whether a single {@link Diff} is currently selected in this viewer.
	 * 
	 * @return <code>true</code> if a single instance of a {@link Diff} is selected, <code>false</code>
	 *         otherwise.
	 */
	private boolean isOneDiffSelected() {
		final ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			if (getDataOfTreeNodeOfAdapter(element) instanceof Diff) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Specifies whether anything but {@link Diff} are currently selected in this viewer.
	 * 
	 * @return <code>true</code> if only instances of {@link Diff} are selected, <code>false</code> otherwise.
	 */
	private boolean isDiffSelected() {
		final ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() > 0) {
			for (Object element : ((IStructuredSelection)selection).toList()) {
				if (!(getDataOfTreeNodeOfAdapter(element) instanceof Diff)) {
					return false;
				}
			}
			// all selected objects are differences
			return true;
		}
		return false;
	}

	/**
	 * Specifies whether the a {@link Match} or a {@link MatchResource} is currently selected in this viewer.
	 * 
	 * @return <code>true</code> if an instance of a {@link Match} or a {@link MatchResource} is selected,
	 *         <code>false</code> otherwise.
	 */
	private boolean isOneMatchOrResourceMatchSelected() {
		final ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			if (isMatchOrMatchResource(getDataOfTreeNodeOfAdapter(element))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Specifies whether the given {@code eObject} is a {@link Match} or a {@link MatchResource}.
	 * 
	 * @param eObject
	 *            The EObject to check.
	 * @return <code>true</code> if it is an instance a {@link Match} or a {@link MatchResource},
	 *         <code>false</code> otherwise.
	 */
	private boolean isMatchOrMatchResource(EObject eObject) {
		return eObject instanceof Match || eObject instanceof MatchResource;
	}

	/**
	 * Checks if there is currently a single selected item in the viewer, and that item is a group.
	 * 
	 * @return <code>true</code> if the single selected item in this viewer is a group.
	 */
	private boolean isOneGroupSelected() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			return element instanceof GroupItemProviderAdapter;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractViewerWrapper#preHookCreateControlAndViewer()
	 */
	@Override
	protected void preHookCreateControlAndViewer() {
		fAdapterFactory = initAdapterFactory(getCompareConfiguration().getComparison());
		getCompareConfiguration().setAdapterFactory(fAdapterFactory);

		fDiffRelationshipComputer = new CachingDiffRelationshipComputer(
				EMFCompareRCPPlugin.getDefault().getMergerRegistry());
		getCompareConfiguration().setDiffRelationshipComputer(fDiffRelationshipComputer);

		inputChangedTask = new CompareInputChangedJob(EMFCompareIDEUIMessages
				.getString("EMFCompareStructureMergeViewer.computingModelDifferences")); //$NON-NLS-1$
	}

	/**
	 * Creates a new adapter factory based on the current compare configuration.
	 * 
	 * @return adapter factory
	 */
	protected ComposedAdapterFactory initAdapterFactory(Comparison comparison) {
		Map<Object, Object> context = Maps.newLinkedHashMap();
		context.put(IContextTester.CTX_COMPARISON, comparison);

		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry(context));
		adapterFactory.addAdapterFactory(new TreeItemProviderAdapterFactorySpec(
				getCompareConfiguration().getStructureMergeViewerFilter()));
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		return adapterFactory;
	}

	@Subscribe
	public void colorChanged(
			@SuppressWarnings("unused") /* necessary for @Subscribe */IColorChangeEvent changeColorEvent) {
		internalRedraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ViewerWrapper.createControl(
	 *      Composite, CompareConfiguration)
	 */
	@Override
	protected ControlAndViewer<CTabFolder, WrappableTreeViewer> createControlAndViewer(Composite parent) {
		parent.setLayout(new FillLayout());
		CTabFolder tabFolder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
		tabFolder.setLayout(new FillLayout());

		// Ensures that this viewer will only display the page's tab
		// area if there are more than one page
		//
		tabFolder.addControlListener(new ControlAdapter() {
			boolean guard = false;

			@Override
			public void controlResized(ControlEvent event) {
				if (!guard) {
					guard = true;
					hideTabs();
					guard = false;
				}
			}
		});

		updateProblemIndication(Diagnostic.OK_INSTANCE);

		Composite control = new Composite(tabFolder, SWT.NONE);
		createItem(0, control);
		tabFolder.setSelection(0);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(data);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);

		progressInfoItem = new JobProgressInfoComposite(inputChangedTask, control,
				SWT.SMOOTH | SWT.HORIZONTAL, SWT.NONE);
		progressInfoItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		progressInfoItem.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		final WrappableTreeViewer treeViewer = new WrappableTreeViewer(control,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.viewers.TreeViewer#isExpandable(java.lang.Object)
			 */
			@Override
			public boolean isExpandable(Object element) {
				if (element instanceof PendingUpdateAdapter) {
					// Prevents requesting the content provider if the object is a PendingUpdateAdapter
					return false;
				}
				if (hasFilters()) {
					// workaround for 65762
					return getFilteredChildren(element).length > 0;
				}
				return super.isExpandable(element);
			}
		};
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setUseHashlookup(true);

		treeViewer.getControl().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				fHandlerService.updatePaneActionHandlers(new Runnable() {
					public void run() {
						fHandlerService.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
						fHandlerService.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
					}
				});
			}
		});

		dependencyData = new DependencyData(getCompareConfiguration());

		tabFolder.setData(CompareUI.COMPARE_VIEWER_TITLE,
				EMFCompareIDEUIMessages.getString("EMFCompareStructureMergeViewer.title")); //$NON-NLS-1$

		final ITheme currentTheme = getCurrentTheme();

		boolean leftIsLocal = getCompareConfiguration().getBooleanProperty("LEFT_IS_LOCAL", false); //$NON-NLS-1$
		fColors = new EMFCompareColor(control.getDisplay(), leftIsLocal, currentTheme,
				getCompareConfiguration().getEventBus());

		treeRuler = new EMFCompareDiffTreeRuler(control, SWT.NONE, treeViewer, dependencyData, fColors);
		GridData rulerLayoutData = new GridData(SWT.FILL, SWT.FILL, false, true);
		rulerLayoutData.exclude = true;
		rulerLayoutData.widthHint = TREE_RULER_WIDTH;
		rulerLayoutData.minimumWidth = TREE_RULER_WIDTH;
		treeRuler.setLayoutData(rulerLayoutData);

		return ControlAndViewer.create(tabFolder, treeViewer);
	}

	/**
	 * Returns the tree viewer.
	 * 
	 * @return the tree viewer
	 */
	public TreeViewer getTreeViewer() {
		return getViewer();
	}

	/**
	 * Determines the current used theme.
	 * 
	 * @return The currently used theme if available, {@code null} otherwise.
	 */
	private ITheme getCurrentTheme() {
		if (PlatformUI.isWorkbenchRunning()) {
			final IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
			if (themeManager != null) {
				return themeManager.getCurrentTheme();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getContentProvider()
	 */
	@Override
	public EMFCompareStructureMergeViewerContentProvider getContentProvider() {
		return (EMFCompareStructureMergeViewerContentProvider)super.getContentProvider();
	}

	@Override
	public DelegatingStyledCellLabelProvider getLabelProvider() {
		return (DelegatingStyledCellLabelProvider)super.getLabelProvider();
	}

	private CTabItem createItem(int index, Control control) {
		CTabItem item = new CTabItem((CTabFolder)control.getParent(), SWT.NONE, index);
		item.setControl(control);
		return item;
	}

	@Subscribe
	public void handleEditingDomainChange(ICompareEditingDomainChange event) {
		editingDomainChange(event.getOldValue(), event.getNewValue());
	}

	private void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		if (newValue != oldValue) {
			if (oldValue != null) {
				oldValue.getCommandStack().removeCommandStackListener(this);
			}

			if (newValue != null) {
				newValue.getCommandStack().addCommandStackListener(this);
			}

			undoAction.setEditingDomain(newValue);
			redoAction.setEditingDomain(newValue);
		}
	}

	private void refreshTitle() {
		if (getControl().isDisposed() || !(getControl().getParent() instanceof CompareViewerSwitchingPane)) {
			return;
		}

		if (getCompareConfiguration().getComparison() == null) {
			return;
		}

		// Schedule with a short delay, because refreshTitle is often called multiple times quickly and this
		// way, with a short delay, the job is run only once even in that case.
		titleBuilderJob.schedule(10L);
	}

	static EObject getDataOfTreeNodeOfAdapter(Object object) {
		EObject data = null;
		if (object instanceof Adapter) {
			Notifier target = ((Adapter)object).getTarget();
			if (target instanceof TreeNode) {
				data = ((TreeNode)target).getData();
			}
		}
		return data;
	}

	@Subscribe
	public void mergePreviewModeChange(@SuppressWarnings("unused") IMergePreviewModeChange event) {
		if (!inChange) {
			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					if (getCompareConfiguration().getMergePreviewMode() == null) {
						clearHighlightRelatedChanges();
					} else {
						updateHighlightRelatedChanges(getSelection());
					}
				}
			});
		}
	}

	@Subscribe
	public void handleDifferenceFilterChange(IDifferenceFilterChange event) {
		final boolean enabled = any(event.getSelectedDifferenceFilters(),
				instanceOf(CascadingDifferencesFilter.class));
		setCascadingDifferencesFilterEnabled(enabled);
		if (!inChange) {
			SWTUtil.safeRefresh(this, false, true);
			getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
				public void run() {
					// Begin computing the content tree cache.
					getNavigatable().refresh();

					if (getViewer().getSelection().isEmpty()) {
						selectFirstDiffOrDisplayLabelViewer(getCompareConfiguration().getComparison());
					}
				}
			});
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		// Make sure we clear the navigatable's caches
		getNavigatable().refresh();
		if (getViewer().getSelection().isEmpty()) {
			selectFirstDiffOrDisplayLabelViewer(getCompareConfiguration().getComparison());
		}
	}

	/**
	 * Set the state of the cascading filter.
	 * 
	 * @param enable
	 *            true if the filter is enabled, false otherwise.
	 */
	private void setCascadingDifferencesFilterEnabled(boolean enable) {
		this.cascadingDifferencesFilterEnabled = enable;
		IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		for (IMergeOptionAware merger : Iterables.filter(mergerRegistry.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			mergeOptions.put(AbstractMerger.SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enable));
		}
	}

	/**
	 * Get the state of the cascading filter.
	 * 
	 * @return true if the filter is enabled, false otherwise.
	 */
	private boolean getCascadingDifferencesFilterEnabled() {
		return this.cascadingDifferencesFilterEnabled;
	}

	@Subscribe
	public void handleDifferenceGroupProviderChange(
			@SuppressWarnings("unused") IDifferenceGroupProviderChange event) {
		if (!inChange) {
			SWTUtil.safeRefresh(this, false, true);
			getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
				public void run() {
					// Begin computing the content tree cache.
					getNavigatable().refresh();

					// Expands the tree viewer to the default expansion level
					expandTreeToLevel(getDefaultTreeExpansionLevel(), getTreeExpandTimeout());

					selectFirstDiffOrDisplayLabelViewer(getCompareConfiguration().getComparison());
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(Object, Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		if (oldInput instanceof ICompareInput) {
			ICompareInput old = (ICompareInput)oldInput;
			old.removeCompareInputChangeListener(fCompareInputChangeListener);
			getToolBar().dispose();
		}
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.addCompareInputChangeListener(fCompareInputChangeListener);
			compareInputChanged(ci);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractViewerWrapper#handleDispose(DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		if (fHandlerService != null) {
			fHandlerService.dispose();
		}
		getCompareConfiguration().getEventBus().unregister(this);
		getCompareConfiguration().removePropertyChangeListener(mirroredPropertyChangeListener);
		getViewer().removeTreeListener(fWrappedTreeListener);
		Object input = getInput();
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.removeCompareInputChangeListener(fCompareInputChangeListener);
		}
		removeSelectionChangedListener(selectionChangeListener);
		getViewer().removeSelectionChangedListener(getToolBar());
		getViewer().getTree().removeListener(SWT.EraseItem, fEraseItemListener);
		if (preferenceChangeListener != null) {
			preferenceStore.removePropertyChangeListener(preferenceChangeListener);
		}
		for (IDisposable disposable : disposableDomains) {
			disposable.dispose();
		}
		disposableDomains.clear();
		getCompareConfiguration().getStructureMergeViewerGrouper().uninstall(getViewer());
		compareInputChanged((ICompareInput)null);
		treeRuler.handleDispose();
		fAdapterFactory.dispose();
		diffRelationshipComputer.cancel();
		fDiffRelationshipComputer.invalidate();
		getToolBar().dispose();
		fColors.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		undoAction.update();
		redoAction.update();

		Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
		if (mostRecentCommand instanceof ICompareCopyCommand && shouldSelectAffectedObject(event)) {
			// MUST NOT call a setSelection with a list, o.e.compare does not handle it (cf
			// org.eclipse.compare.CompareEditorInput#getElement(ISelection))
			Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();
			TreeNode unfilteredNode = null;
			if (!affectedObjects.isEmpty()) {
				final Iterator<EObject> affectedIterator = Iterables.filter(affectedObjects, EObject.class)
						.iterator();
				IDifferenceGroupProvider groupProvider = getCompareConfiguration()
						.getStructureMergeViewerGrouper().getProvider();
				while (affectedIterator.hasNext() && unfilteredNode == null) {
					EObject affected = affectedIterator.next();
					Iterable<TreeNode> treeNodes = groupProvider.getTreeNodes(affected);
					for (TreeNode node : treeNodes) {
						if (!JFaceUtil.isFiltered(getViewer(), node, node.getParent())) {
							unfilteredNode = node;
							break;
						}
					}
				}
			}
			if (unfilteredNode != null) {
				final Object adaptedAffectedObject = fAdapterFactory.adapt(unfilteredNode,
						ICompareInput.class);
				// be sure the affected object has been created in the viewer.
				for (TreeNode node : getPath(null, unfilteredNode)) {
					getViewer().expandToLevel(fAdapterFactory.adapt(node, ICompareInput.class), 0);
				}
				// execute synchronously the set selection to be sure the MergeAction#run() will
				// select next diff after.
				SWTUtil.safeSyncExec(new Runnable() {
					public void run() {
						refresh();

						// allows to call CompareToolBar#selectionChanged(SelectionChangedEvent)
						// Check first that we won't end up with an empty selection.
						if (getViewer().doFindItem(adaptedAffectedObject) != null) {
							StructuredSelection selection = new StructuredSelection(adaptedAffectedObject);
							getViewer().setSelection(selection);
						} else {
							getViewer().setSelection(getViewer().getSelection());
						}
					}
				});
				// update content viewers with the new selection
				SWTUtil.safeAsyncExec(new Runnable() {
					public void run() {
						getNavigatable().openSelectedChange();
					}
				});
			} else {
				SWTUtil.safeSyncExec(new Runnable() {

					public void run() {
						refresh();
						getToolBar().selectionChanged(
								new SelectionChangedEvent(getViewer(), getViewer().getSelection()));
					}
				});
			}
		} else {
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					refresh();
					getToolBar().selectionChanged(
							new SelectionChangedEvent(getViewer(), getViewer().getSelection()));
				}
			});

			// FIXME, should recompute the difference, something happened outside of this compare editor
		}

	}

	/**
	 * Returns whether this event should cause the affective object of the most recent command to be selected.
	 * 
	 * @param event
	 *            an event that might be a {@link CommandStackEvent}.
	 * @return whether this event should cause the affective object of the most recent command to be selected.
	 */
	private boolean shouldSelectAffectedObject(EventObject event) {
		if (event instanceof CommandStackEvent) {
			CommandStackEvent commandStackEvent = (CommandStackEvent)event;
			CommandStackEvent.Operation type = commandStackEvent.getOperation();
			return type == CommandStackEvent.Operation.UNDO || type == CommandStackEvent.Operation.REDO;
		} else {
			return true;
		}
	}

	private Iterable<TreeNode> getPath(TreeNode from, TreeNode to) {
		if (to == from) {
			return Collections.emptyList();
		}

		final List<TreeNode> path = new ArrayList<TreeNode>();
		path.add(to);
		TreeNode parent = to.getParent();
		while (parent != null && parent != from) {
			path.add(parent);
			parent = parent.getParent();
		}
		return Lists.reverse(path);
	}

	/**
	 * Triggered by fCompareInputChangeListener and {@link #inputChanged(Object, Object)}.
	 */
	protected void compareInputChanged(ICompareInput input) {
		if (input == null) {
			// When closing, we don't need a progress monitor to handle the input change
			compareInputChanged((ICompareInput)null, new NullProgressMonitor());
			return;
		}
		// The compare configuration is nulled when the viewer is disposed
		if (getCompareConfiguration() != null) {
			updateLayout(true, true);
			inputChangedTask.schedule();
		}
	}

	protected void compareInputChanged(Comparison input, IProgressMonitor monitor) {
		compareInputChanged(null, input, monitor);
	}

	protected void compareInputChanged(ComparisonScopeInput input, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return;
		}
		IComparisonScope comparisonScope = input.getComparisonScope();
		EMFCompareConfiguration compareConfiguration = getCompareConfiguration();

		EMFCompare comparator = compareConfiguration.getEMFComparator();
		compareConfiguration.setLeftEditable(input.isLeftEditable());
		compareConfiguration.setRightEditable(input.isRightEditable());

		if (isHighlightRelatedChanges()) {
			if (input.isLeftEditable() && input.isRightEditable()) {
				compareConfiguration.setMergePreviewMode(MergeMode.RIGHT_TO_LEFT);
			} else {
				compareConfiguration.setMergePreviewMode(MergeMode.ACCEPT);
			}
		} else {
			compareConfiguration.setMergePreviewMode(null);
		}

		if (comparator == null) {
			Builder builder = EMFCompare.builder();
			EMFCompareBuilderConfigurator.createDefault().configure(builder);
			comparator = builder.build();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		final Comparison comparison = comparator.compare(comparisonScope,
				BasicMonitor.toMonitor(subMonitor.newChild(10)));

		// Bug 458802: NPE when synchronizing SMV & CMV if comparison is empty
		hookAdapters(input, comparison);

		if (comparisonScope.getLeft() instanceof NotLoadingResourceSet) {
			((NotLoadingResourceSet)comparisonScope.getLeft()).setAllowResourceLoad(true);
		}
		if (comparisonScope.getRight() instanceof NotLoadingResourceSet) {
			((NotLoadingResourceSet)comparisonScope.getRight()).setAllowResourceLoad(true);
		}
		if (comparisonScope.getOrigin() instanceof NotLoadingResourceSet) {
			((NotLoadingResourceSet)comparisonScope.getOrigin()).setAllowResourceLoad(true);
		}

		compareInputChanged(input.getComparisonScope(), comparison, monitor);
	}

	protected void compareInputChanged(final IComparisonScope scope, final Comparison comparison,
			final IProgressMonitor monitor) {
		if (!getControl().isDisposed() && !monitor.isCanceled()) { // guard against disposal
			final EMFCompareConfiguration config = getCompareConfiguration();

			// setup defaults
			if (config.getEditingDomain() == null) {
				ICompareEditingDomain domain = EMFCompareEditingDomain.create(scope.getLeft(),
						scope.getRight(), scope.getOrigin());
				if (domain instanceof IDisposable) {
					disposableDomains.add((IDisposable)domain);
				}
				config.setEditingDomain(domain);
			}

			ComposedAdapterFactory oldAdapterFactory = fAdapterFactory;
			// re-initialize adapter factory due to new comparison
			fAdapterFactory = initAdapterFactory(comparison);

			// clear cache for new comparison
			if (fDiffRelationshipComputer != null) {
				diffRelationshipComputer.cancel();
			}

			// propagate new adapter factory
			config.setAdapterFactory(fAdapterFactory);
			getContentProvider().setAdapterFactory(fAdapterFactory);
			((EMFCompareStructureMergeViewerLabelProvider)getLabelProvider().getStyledStringProvider())
					.setAdapterFactory(fAdapterFactory);

			final TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
			treeNode.setData(comparison);
			final Object input = fAdapterFactory.adapt(treeNode, ICompareInput.class);

			// this will set to the EMPTY difference group provider, but necessary to avoid NPE while
			// setting input.
			IDifferenceGroupProvider groupProvider = config.getStructureMergeViewerGrouper().getProvider();
			treeNode.eAdapters().add(groupProvider);

			// display problem tabs if any
			if (!monitor.isCanceled()) {
				SWTUtil.safeAsyncExec(new Runnable() {
					public void run() {
						Diagnostic diagnostic = comparison.getDiagnostic();
						if (diagnostic == null) {
							updateProblemIndication(Diagnostic.OK_INSTANCE);
						} else {
							updateProblemIndication(diagnostic);
						}
					}
				});
			}

			// must set the input now in a synchronous mean. It will be used in the #setComparisonAndScope
			// afterwards during the initialization of StructureMergeViewerFilter and
			// StructureMergeViewerGrouper.
			if (!monitor.isCanceled()) {
				SWTUtil.safeSyncExec(new Runnable() {

					public void run() {
						getViewer().setInput(input);
					}
				});
			}

			config.setComparisonAndScope(comparison, scope);

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					if (!getControl().isDisposed()) {
						updateLayout(false, true);
					}
				}
			});

			SWTUtil.safeRefresh(this, false, true);

			getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
				public void run() {
					if (!getControl().isDisposed()) {
						// title is not initialized as the comparison was set in the configuration after
						// the refresh caused by the initialization of the viewer filters and the group
						// providers.
						refreshTitle();

						getViewer().getControl().setFocus();

						// Expands the tree viewer to the default expansion level
						expandTreeToLevel(getDefaultTreeExpansionLevel(), getTreeExpandTimeout());

						// Begin computing the content tree cache.
						getNavigatable().refresh();

						// Selects the first difference once the tree has been filled.
						selectFirstDiffOrDisplayLabelViewer(comparison);
					}
				}
			});

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					fHandlerService.updatePaneActionHandlers(new Runnable() {
						public void run() {
							fHandlerService.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
							fHandlerService.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
						}
					});
				}
			});

			if (fDiffRelationshipComputer != null) {
				diffRelationshipComputer.schedule();
			}

			// Disposing this at the start of this method would meet frequent CME
			if (oldAdapterFactory != null) {
				oldAdapterFactory.dispose();
			}
		}

	}

	protected void compareInputChanged(final ICompareInput input, IProgressMonitor monitor) {
		inChange = true;
		if (input != null && !monitor.isCanceled()) {
			if (input instanceof CompareInputAdapter) {
				resourceSetShouldBeDisposed = false;
				CompareInputAdapter adapter = (CompareInputAdapter)input;
				compareInputChanged((Comparison)adapter.getComparisonObject(), monitor);
				initToolbar(monitor);
			} else if (input instanceof ComparisonScopeInput) {
				resourceSetShouldBeDisposed = false;
				compareInputChanged((ComparisonScopeInput)input, monitor);
				initToolbar(monitor);
			} else if (input instanceof IComparisonProvider) {
				resourceSetShouldBeDisposed = false;
				Comparison comparison = ((IComparisonProvider)input).getComparison(monitor);
				compareInputChanged(((IComparisonProvider)input).getComparisonScope(monitor), comparison,
						monitor);
				hookAdapters(input, comparison);
				initToolbar(monitor);
			} else {
				resourceSetShouldBeDisposed = true;
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

				final ITypedElement left = input.getLeft();
				final ITypedElement right = input.getRight();
				final ITypedElement origin = input.getAncestor();

				final boolean leftEditable;
				final boolean rightEditable;

				EMFCompareConfiguration compareConfiguration = getCompareConfiguration();
				/*
				 * A resource node means that the left ITypedElement is in the workspace, a DiffNode input
				 * means the comparison has been launched from a Replace With action.
				 */
				if (left instanceof ResourceNode && !(input instanceof DiffNode)) {
					ResourceAttributes attributes = ((ResourceNode)left).getResource()
							.getResourceAttributes();
					leftEditable = attributes != null && !attributes.isReadOnly();
				} else {
					leftEditable = compareConfiguration.isLeftEditable();
				}

				if (right instanceof ResourceNode) {
					ResourceAttributes attributes = ((ResourceNode)right).getResource()
							.getResourceAttributes();
					rightEditable = attributes != null && !attributes.isReadOnly();
				} else {
					rightEditable = compareConfiguration.isRightEditable();
				}

				compareConfiguration.setLeftEditable(leftEditable);
				compareConfiguration.setRightEditable(rightEditable);

				if (isHighlightRelatedChanges()) {
					if (leftEditable && rightEditable) {
						compareConfiguration.setMergePreviewMode(MergeMode.RIGHT_TO_LEFT);
					} else {
						compareConfiguration.setMergePreviewMode(MergeMode.ACCEPT);
					}
				} else {
					compareConfiguration.setMergePreviewMode(null);
				}

				final BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK,
						EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[0]);
				IComparisonScope scope = null;

				try {
					scope = ComparisonScopeBuilder.create(compareConfiguration.getContainer(), left, right,
							origin, subMonitor.newChild(85));
				} catch (OperationCanceledException e) {
					scope = new EmptyComparisonScope();
					((BasicDiagnostic)((EmptyComparisonScope)scope).getDiagnostic())
							.merge(new BasicDiagnostic(Diagnostic.CANCEL, EMFCompareIDEUIPlugin.PLUGIN_ID, 0,
									EMFCompareIDEUIMessages
											.getString("EMFCompareStructureMergeViewer.operationCanceled"), //$NON-NLS-1$
									new Object[] {e, }));
				} catch (Exception e) {
					scope = new EmptyComparisonScope();
					((BasicDiagnostic)((EmptyComparisonScope)scope).getDiagnostic())
							.merge(BasicDiagnostic.toDiagnostic(e));
					EMFCompareIDEUIPlugin.getDefault().log(e);
				}

				if (scope instanceof IDiagnosable && ((IDiagnosable)scope).getDiagnostic() != null) {
					diagnostic.merge(((IDiagnosable)scope).getDiagnostic());
				}

				final Builder comparisonBuilder = EMFCompare.builder();

				EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);

				SubMonitor subMonitorChild = SubMonitor.convert(subMonitor.newChild(14), 10);
				final Comparison compareResult = comparisonBuilder.build().compare(scope,
						BasicMonitor.toMonitor(subMonitorChild));

				SubMonitor finalSubMonitorChild = subMonitor.newChild(1);
				finalSubMonitorChild.subTask("Updating view..."); //$NON-NLS-1$

				hookAdapters(input, compareResult);

				if (compareResult.getDiagnostic() != null) {
					diagnostic.merge(compareResult.getDiagnostic());
				}
				// update diagnostic of the comparison with the global one.
				compareResult.setDiagnostic(diagnostic);

				final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
				final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
				final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

				ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(leftResourceSet,
						rightResourceSet, originResourceSet);
				if (editingDomain instanceof IDisposable) {
					disposableDomains.add((IDisposable)editingDomain);
				}
				compareConfiguration.setEditingDomain(editingDomain);

				if (leftResourceSet instanceof NotLoadingResourceSet) {
					((NotLoadingResourceSet)leftResourceSet).setAllowResourceLoad(true);
				}
				if (rightResourceSet instanceof NotLoadingResourceSet) {
					((NotLoadingResourceSet)rightResourceSet).setAllowResourceLoad(true);
				}
				if (originResourceSet instanceof NotLoadingResourceSet) {
					((NotLoadingResourceSet)originResourceSet).setAllowResourceLoad(true);
				}

				IStorage leftStorage = PlatformElementUtil.findFile(left);
				if (leftStorage == null) {
					leftStorage = StreamAccessorStorage.fromTypedElement(left);
				}

				initToolbar(monitor);
				finalSubMonitorChild.worked(1);
				compareInputChanged(scope, compareResult, monitor);
			}
			// Protect compare actions from over-enthusiast users
			enableToolbar(monitor);
		} else {
			compareInputChangedToNull();
		}
		inChange = false;
	}

	/**
	 * Hooks the adapters required for handling UI properly.
	 * 
	 * @param input
	 * @param compareResult
	 */
	protected void hookAdapters(final ICompareInput input, final Comparison compareResult) {
		// It is now possible for users to provide a pre-computed comparison to the compare editor and re-use
		// that comparison across multiple SMV. We need to cleanup previous hooks.
		Iterator<Adapter> adapterIt = compareResult.eAdapters().iterator();
		while (adapterIt.hasNext()) {
			Adapter adapter = adapterIt.next();
			if (adapter instanceof ForwardingCompareInputAdapter) {
				adapterIt.remove();
			} else if (adapter instanceof SideLabelProvider) {
				adapterIt.remove();
			} else if (adapter instanceof MergeDataImpl) {
				adapterIt.remove();
			}
		}

		compareResult.eAdapters().add(new ForwardingCompareInputAdapter(input));
		// Thanks to
		// org.eclipse.team.internal.ui.mapping.ResourceCompareInputChangeNotifier$CompareInputLabelProvider
		// who doesn't check a cast in its getAncestorLabel(), getLeftLabel() and getRightLabel() methods,
		// we can't allow to add side label provider in case of an input of type ResourceDiffCompareInput.
		EMFCompareConfiguration compareConfiguration = getCompareConfiguration();
		if (!(input instanceof ResourceDiffCompareInput)) {
			ICompareInputLabelProvider labelProvider = compareConfiguration.getLabelProvider();
			SideLabelProvider sideLabelProvider = new SideLabelProvider(labelProvider.getAncestorLabel(input),
					labelProvider.getLeftLabel(input), labelProvider.getRightLabel(input),
					labelProvider.getAncestorImage(input), labelProvider.getLeftImage(input),
					labelProvider.getRightImage(input));
			compareResult.eAdapters().add(sideLabelProvider);
		}
		// Bug 501569: The cascading filter does not hide merged cascading diffs
		new MatchOfContainmentReferenceChangeProcessor().execute(compareResult);

		// Add a MergeData to handle status decorations on Diffs
		final MergeDataImpl mergeData = new MergeDataImpl(compareConfiguration.isLeftEditable(),
				compareConfiguration.isRightEditable(), compareConfiguration.isMirrored());
		compareConfiguration.addPropertyChangeListener(
				new ForwardingCompareConfiguration.MirroredPropertyChangeListener() {
					@Override
					protected void mirroredPropertyChanged(boolean mirrored) {
						mergeData.setMirrored(mirrored);
						WrappableTreeViewer viewer = getViewer();
						viewer.refresh();

						// So that tool bar actions can update.
						viewer.fireSelectionChanged(new SelectionChangedEvent(viewer, viewer.getSelection()));
					}
				});
		compareResult.eAdapters().add(mergeData);
	}

	/**
	 * Returns whether the first change should be selected automatically after initialization.
	 * 
	 * @return true if the first change should be selected automatically, false otherwise.
	 * @see #selectFirstDiffOrDisplayLabelViewer(Comparison)
	 */
	protected boolean isSelectFirstChange() {
		return preferenceStore.getBoolean(EMFCompareUIPreferences.EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE);
	}

	/**
	 * Returns the default expansion level for the tree viewer.
	 * 
	 * @return non-negative level, or {@link AbstractTreeViewer#ALL_LEVELS ALL_LEVELS} to expand all levels of
	 *         the tree
	 * @see #expandTreeToLevel(int, long)
	 */
	protected int getDefaultTreeExpansionLevel() {
		return preferenceStore.getInt(EMFCompareUIPreferences.EDITOR_TREE_AUTO_EXPAND_LEVEL);
	}

	/**
	 * Returns the timeout limit for the number of seconds spent expanding the tree viewer.
	 * 
	 * @return the maximum number of seconds to spend on expanding the tree viewer.
	 * @see #expandTreeToLevel(int, long)
	 */
	protected long getTreeExpandTimeout() {
		return preferenceStore.getInt(EMFCompareUIPreferences.EDITOR_TREE_EXPAND_TIMEOUT) * 1000L;
	}

	/**
	 * Expands the {@link #getViewer() tree viewer} to the given level for at most the given number of
	 * milliseconds.
	 * 
	 * @param level
	 *            non-negative level, or {@link AbstractTreeViewer#ALL_LEVELS ALL_LEVELS} to expand all levels
	 *            of the tree
	 * @param timeout
	 *            the maximum number of milliseconds to spend on expanding the tree.
	 * @see TreeViewer#expandToLevel(int)
	 */
	protected void expandTreeToLevel(final int level, final long timeout) {
		if (level != 0) {
			BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
				public void run() {
					// Rather than spend arbitrary amount of time expanding the tree,
					// we'll spend expanding until the configured timeout.
					final long start = System.currentTimeMillis();
					final WrappableTreeViewer viewer = getViewer();
					final Tree tree = viewer.getTree();
					tree.setRedraw(false);
					try {
						int count;
						if (level < 0) {
							count = Integer.MAX_VALUE;
						} else {
							count = level;
						}
						List<TreeItem> items = Lists.newArrayList(tree.getItems());
						while (--count >= 0 && !items.isEmpty()) {
							List<TreeItem> itemsToProcess = items;
							items = Lists.newArrayList();
							for (TreeItem treeItem : itemsToProcess) {
								if (treeItem.getItemCount() > 0) {
									if (!treeItem.getExpanded()) {
										viewer.createChildren(treeItem);
										treeItem.setExpanded(true);
									}
									Collections.addAll(items, treeItem.getItems());
									// if timeout kicks in, we stop expanding
									if (System.currentTimeMillis() - start > timeout) {
										break;
									}
								}
							}
						}
					} finally {
						tree.setRedraw(true);
					}
				}
			});
		}
	}

	/**
	 * Returns whether we highlight changes related to the current selected change.
	 * 
	 * @return true if we highlight related changes, false otherwise.
	 * @see #updateHighlightRelatedChanges(ISelection)
	 */
	protected boolean isHighlightRelatedChanges() {
		return preferenceStore.getBoolean(EMFCompareUIPreferences.EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES);
	}

	/**
	 * Updates the highlighting of related changes for the current selection, if it is
	 * {@link #isHighlightRelatedChanges() enabled}.
	 * 
	 * @param selection
	 *            selection
	 */
	protected void updateHighlightRelatedChanges(ISelection selection) {
		if (getCompareConfiguration().getMergePreviewMode() != null) {
			dependencyData.updateDependencies(selection,
					EMFCompareRCPPlugin.getDefault().getMergerRegistry());
			internalRedraw();
		}
	}

	/**
	 * Clears the highlighting of related changes for the current selection.
	 */
	protected void clearHighlightRelatedChanges() {
		dependencyData.clearDependencies();
		internalRedraw();
	}

	/**
	 * Select the first difference...if there are differences, otherwise, display appropriate content viewer
	 * (no differences or no visible differences)
	 * 
	 * @param comparison
	 *            the comparison used to know if there are differences.
	 */
	protected void selectFirstDiffOrDisplayLabelViewer(final Comparison comparison) {
		if (comparison != null) {
			ICompareInput compareInput = (ICompareInput)EcoreUtil.getAdapter(comparison.eAdapters(),
					ICompareInput.class);
			if (compareInput == null) {
				compareInput = new TreeNodeCompareInput(new TreeCompareInputAdapterFactory());
			}
			List<Diff> differences = comparison.getDifferences();
			if (differences.isEmpty()) {
				getNavigatable().fireOpen(new NoDifferencesCompareInput(compareInput));
			} else if (!getNavigatable().hasChange(INavigatable.FIRST_CHANGE)) {
				if (hasOnlyPseudoConflicts(differences)) {
					getNavigatable().fireOpen(new OnlyPseudoConflictsCompareInput(compareInput));
				} else {
					getNavigatable().fireOpen(new NoVisibleItemCompareInput(compareInput));
				}
			} else if (!isSelectFirstChange()) {
				getNavigatable().fireOpen(new NoSelectedItemCompareInput(compareInput));
				WrappableTreeViewer viewer = getViewer();
				if (viewer.getSelection().isEmpty()) {
					Object[] filteredChildren = viewer.getFilteredChildren(viewer.getInput());
					if (filteredChildren.length != 0) {
						viewer.setSelection(new StructuredSelection(filteredChildren[0]));
					}
				}
			} else {
				getNavigatable().selectChange(INavigatable.FIRST_CHANGE);
			}
		}
	}

	private boolean hasOnlyPseudoConflicts(List<Diff> differences) {
		return Iterators.all(differences.iterator(), EMFComparePredicates.hasConflict(ConflictKind.PSEUDO));
	}

	private void updateLayout(boolean displayProgress, boolean doLayout) {
		((GridData)progressInfoItem.getLayoutData()).exclude = !displayProgress;
		progressInfoItem.setVisible(displayProgress);

		((GridData)getViewer().getControl().getLayoutData()).exclude = displayProgress;
		getViewer().getControl().setVisible(!displayProgress);

		((GridData)treeRuler.getLayoutData()).exclude = displayProgress;
		treeRuler.setVisible(!displayProgress);

		if (doLayout) {
			getControl().layout(true, true);
		}
	}

	protected void compareInputChangedToNull() {
		stopJob(inputChangedTask);
		stopJob(titleBuilderJob);

		if (getCompareConfiguration() != null) {
			if (resourceSetShouldBeDisposed) {
				disposeResourceSets(getCompareConfiguration().getComparison());
			}
			editingDomainChange(getCompareConfiguration().getEditingDomain(), null);
			getCompareConfiguration().disposeComparison();
		}
		getViewer().setInput(null);
	}

	protected void stopJob(Job job) {
		if (!job.cancel()) {
			try {
				job.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}
	}

	protected void disposeResourceSets(Comparison comparison) {
		ResourceSet leftResourceSet = null;
		ResourceSet rightResourceSet = null;
		ResourceSet originResourceSet = null;

		if (comparison != null) {
			Iterator<Match> matchIt = comparison.getMatches().iterator();
			if (comparison.isThreeWay()) {
				while (matchIt.hasNext() && (leftResourceSet == null || rightResourceSet == null
						|| originResourceSet == null)) {
					Match match = matchIt.next();
					if (leftResourceSet == null) {
						leftResourceSet = getResourceSet(match.getLeft());
					}
					if (rightResourceSet == null) {
						rightResourceSet = getResourceSet(match.getRight());
					}
					if (originResourceSet == null) {
						originResourceSet = getResourceSet(match.getOrigin());
					}
				}
			} else {
				while (matchIt.hasNext() && (leftResourceSet == null || rightResourceSet == null)) {
					Match match = matchIt.next();
					if (leftResourceSet == null) {
						leftResourceSet = getResourceSet(match.getLeft());
					}
					if (rightResourceSet == null) {
						rightResourceSet = getResourceSet(match.getRight());
					}
				}
			}
		}
		final ResourceSet finalLeftResourceSet = leftResourceSet;
		final ResourceSet finalRightResourceSet = rightResourceSet;
		final ResourceSet finalOriginResourceSet = originResourceSet;
		new Job("Resource Disposer") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				disposeResourceSet(finalLeftResourceSet);
				disposeResourceSet(finalRightResourceSet);
				disposeResourceSet(finalOriginResourceSet);
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	/**
	 * Disposes the {@link ResourceSet}.
	 * 
	 * @param resourceSet
	 *            that need to be disposed.
	 */
	protected void disposeResourceSet(ResourceSet resourceSet) {
		if (resourceSet instanceof DisposableResourceSet) {
			((DisposableResourceSet)resourceSet).dispose();
		} else {
			unload(resourceSet);
		}
	}

	/**
	 * Handle the erase item event. When select a difference in the structure merge viewer, highlight required
	 * differences with a specific color, and highlight unmergeable differences with another color.
	 * 
	 * @param event
	 *            the erase item event.
	 */
	private void handleEraseItemEvent(Event event) {
		TreeItem item = (TreeItem)event.item;
		EObject dataItem = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(item.getData());
		if (dataItem != null) {
			final Set<Diff> requires = dependencyData.getRequires();
			final Set<Diff> rejectedDiffs = dependencyData.getRejections();
			final GC g = event.gc;
			if (requires.contains(dataItem)) {
				paintItemBackground(g, item, fColors.getRequiredFillColor());
			} else if (rejectedDiffs.contains(dataItem)) {
				paintItemBackground(g, item, fColors.getUnmergeableFillColor());
			}
		}
	}

	/**
	 * Paint the background of the given item with the given color.
	 * 
	 * @param g
	 *            the GC associated to the item.
	 * @param item
	 *            the given item.
	 * @param color
	 *            the given color.
	 */
	private void paintItemBackground(GC g, TreeItem item, Color color) {
		Rectangle itemBounds = item.getBounds();
		Tree tree = item.getParent();
		Rectangle areaBounds = tree.getClientArea();
		g.setClipping(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
		g.setBackground(color);
		g.fillRectangle(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
	}

	/**
	 * Returns a problem indication composite for the given diagnostic. If a problem indication composite
	 * already exists, the existing one is returned. If no composite exists, a new composite is created if the
	 * severity of the provided diagnostic is anything besides OK. If no composite exists and the severity
	 * does not warrant the creation of a new composite, this method returns null.
	 * 
	 * @param diagnostic
	 *            comparison diagnostic
	 * @return the existing or a newly created problem indication composite or null if no indication is
	 *         necessary
	 */
	private ProblemIndicationComposite getProblemIndication(Diagnostic diagnostic) {
		Assert.isNotNull(diagnostic);
		int lastEditorPage = getPageCount() - 1;
		ProblemIndicationComposite problemIndicationComposite = null;
		if (lastEditorPage >= 0 && getItemControl(lastEditorPage) instanceof ProblemIndicationComposite) {
			problemIndicationComposite = ((ProblemIndicationComposite)getItemControl(lastEditorPage));
		} else if (diagnostic.getSeverity() != Diagnostic.OK && !getControl().isDisposed()) {
			problemIndicationComposite = new ProblemIndicationComposite(getControl(), SWT.NONE);
			createItem(++lastEditorPage, problemIndicationComposite);
			getControl().getItem(lastEditorPage)
					.setText(CommonUIPlugin.getPlugin().getString("_UI_Problems_label")); //$NON-NLS-1$
			showTabs();
		}
		return problemIndicationComposite;
	}

	/**
	 * Updates the problem indication for the provided diagnostic. If everything is {@link Diagnostic#OK} and
	 * no problem indication is available, this method does nothing. In any other case, the existing or a
	 * newly created problem indication is updated and automatically revealed if the diagnostics
	 * {@link Diagnostic#getSeverity() severity} is anything besides {@link Diagnostic#OK} and
	 * {@link Diagnostic#WARNING}.
	 * 
	 * @param diagnostic
	 *            comparison diagnostic
	 */
	private void updateProblemIndication(Diagnostic diagnostic) {
		ProblemIndicationComposite problemIndicationComposite = getProblemIndication(diagnostic);
		if (problemIndicationComposite != null) {
			problemIndicationComposite.setDiagnostic(diagnostic);
			if (diagnostic.getSeverity() != Diagnostic.OK && diagnostic.getSeverity() != Diagnostic.WARNING) {
				// reveal problem indication composite (last editor page)
				int lastEditorPage = getPageCount() - 1;
				setActivePage(lastEditorPage);
				updateLayout(false, true);
				getItemControl(lastEditorPage).setFocus();
				return;
			}
		}

		if (getPageCount() > 0) {
			setActivePage(0);
			getItemControl(0).setFocus();
		}
	}

	private void showTabs() {
		if (getPageCount() > 1) {
			getControl().getItem(0).setText(
					EMFCompareIDEUIMessages.getString("EMFCompareStructureMergeViewer.tabItem.0.title")); //$NON-NLS-1$
			getControl().setTabHeight(SWT.DEFAULT);
			Point point = getControl().getSize();
			getControl().setSize(point.x, point.y - 6);
		}
	}

	private void hideTabs() {
		if (getPageCount() <= 1) {
			getControl().getItem(0).setText(""); //$NON-NLS-1$
			getControl().setTabHeight(1);
			Point point = getControl().getSize();
			getControl().setSize(point.x, point.y + 6);
		}
	}

	private void setActivePage(int pageIndex) {
		Assert.isTrue(pageIndex >= 0 && pageIndex < getPageCount());
		getControl().setSelection(pageIndex);
	}

	private int getPageCount() {
		// May not have been created yet, or may have been disposed.
		if (getControl() != null && !getControl().isDisposed()) {
			return getControl().getItemCount();
		}
		return 0;
	}

	private Control getItemControl(int itemIndex) {
		CTabItem item = getControl().getItem(itemIndex);
		if (item != null) {
			return item.getControl();
		}
		return null;
	}

	private static void unload(ResourceSet resourceSet) {
		if (resourceSet != null) {
			for (Resource resource : resourceSet.getResources()) {
				resource.unload();
			}
			resourceSet.getResources().clear();
		}
	}

	private static ResourceSet getResourceSet(EObject eObject) {
		if (eObject != null) {
			Resource eResource = eObject.eResource();
			if (eResource != null) {
				return eResource.getResourceSet();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	@Override
	protected void internalRefresh(Object element) {
		if (element == null) {
			return;
		}

		// Postpones the refresh if the content provider is in pending mode
		getContentProvider().runWhenReady(IN_UI_SYNC, new Runnable() {

			public void run() {
				getViewer().refresh();
			}
		});
		// Updates dependency data when the viewer has been refreshed and the content provider is ready.
		getContentProvider().runWhenReady(IN_UI_SYNC, new Runnable() {
			public void run() {
				updateHighlightRelatedChanges(getSelection());
			}
		});
		// Needs dependency data however do not need to be run in UI thread
		getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {

			public void run() {
				refreshTitle();
			}
		});
	}

	/**
	 * Handles changes to the UI-related preferences in the {@link #preferenceStore}.
	 * 
	 * @param event
	 *            change event for a preference property
	 */
	protected void handlePreferenceChangedEvent(PropertyChangeEvent event) {
		if (event.getProperty() == EMFCompareUIPreferences.EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES) {
			boolean highlightRelatedChanges = Boolean.parseBoolean(event.getNewValue().toString());
			EMFCompareConfiguration compareConfiguration = getCompareConfiguration();
			if (highlightRelatedChanges) {
				if (compareConfiguration.getMergePreviewMode() == null) {
					if (compareConfiguration.isLeftEditable() && compareConfiguration.isRightEditable()) {
						compareConfiguration.setMergePreviewMode(MergeMode.RIGHT_TO_LEFT);
					} else {
						compareConfiguration.setMergePreviewMode(MergeMode.ACCEPT);
					}
				}
			} else {
				compareConfiguration.setMergePreviewMode(null);
			}
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangeListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangeListeners.remove(listener);
	}

	@Override
	protected void fireSelectionChanged(SelectionChangedEvent event) {
		/*
		 * Platform/compare does not support multiple selections in the structure viewer. When the user
		 * selects multiple differences, org.eclipse.compare.CompareEditorInput#getElement(ISelection)) will
		 * return a null selection, causing the CompareViewerSwitchingPane to dispose of the currently shown
		 * ContentMergeViewer. Unfortunately, the dirty state of the CompareEditorInput is _only_ managed
		 * through the CMV: ContentMergeViewer.setLeftDirty will notify the CompareEditorInput that it needs
		 * to update the dirty state... Only if a CMV is currently shown. In short, if a user selects multiple
		 * differences and merges them, the CompareEditorInput will never know that it is dirty and needs a
		 * save.
		 */
		ISelection selection = event.getSelection();
		final SelectionChangedEvent compareEvent;
		if (!selection.isEmpty() && selection instanceof IStructuredSelection
				&& ((IStructuredSelection)selection).size() != 1) {
			ISelection modifiedSelection = new StructuredSelection(
					((IStructuredSelection)selection).getFirstElement());
			compareEvent = new SelectionChangedEvent(event.getSelectionProvider(), modifiedSelection);
		} else {
			compareEvent = event;
		}
		for (ISelectionChangedListener listener : selectionChangeListeners) {
			SafeRunnable.run(new SafeRunnable() {
				@Override
				public void run() {
					if (listener instanceof CompareViewerSwitchingPane) {
						listener.selectionChanged(compareEvent);
					} else {
						listener.selectionChanged(event);
					}
				}
			});
		}
	}

	private void handleSelectionChangedEvent(SelectionChangedEvent event) {
		if (!Objects.equal(currentSelection, event.getSelection())) {
			this.currentSelection = event.getSelection();
			updateHighlightRelatedChanges(event.getSelection());
		}
	}

	/**
	 * We need to call redraw() on the tree and the tree ruler because getControl().redraw() doesn't propagate
	 * the redraw on its sub components under windows platform.
	 */
	private void internalRedraw() {
		Tree tree = getViewer().getTree();
		if (!tree.isDisposed()) {
			tree.redraw();
			if (!treeRuler.isDisposed()) {
				treeRuler.redraw();
			}
		}
	}

	private static class TitleBuilder {

		private final Comparison comparison;

		private final IDifferenceGroupProvider groupProvider;

		private final Predicate<? super EObject> filterPredicate;

		private final Map<Object, Boolean> visited = Maps.newHashMap();

		private int diffsCount;

		private int visibleDiffsCount;

		private int diffsToMergeCount;

		public TitleBuilder(EMFCompareConfiguration configuration) {
			comparison = configuration.getComparison();
			StructureMergeViewerGrouper grouper = configuration.getStructureMergeViewerGrouper();
			groupProvider = grouper == null ? null : grouper.getProvider();
			StructureMergeViewerFilter filter = configuration.getStructureMergeViewerFilter();
			filterPredicate = filter == null ? null
					: EMFComparePredicates.guavaToJava(filter.getAggregatedPredicate());
		}

		void visit(TreeNode node, boolean parentApplies) {
			boolean applies = parentApplies && filterPredicate.test(node);
			EObject data = node.getData();
			if (data instanceof Diff) {
				// If we haven't visited it before...
				Boolean visitedApplies = visited.put(data, Boolean.valueOf(applies));
				if (visitedApplies == null) {
					// Count it.
					++diffsCount;
				}

				// If it's visible...
				if (applies) {
					// If we didn't visit it as visible before...
					if (!Boolean.TRUE.equals(visitedApplies)) {
						// Count it as visible.
						++visibleDiffsCount;

						// And if it's not unresolved, count it has needing to be merged.
						Diff diff = (Diff)data;
						if (diff.getState() == DifferenceState.UNRESOLVED) {
							++diffsToMergeCount;
						}
					}
				} else if (Boolean.TRUE.equals(visitedApplies)) {
					// If it was previously counted as visible, but here it's not visible, replace the state
					// to indicate it was visible and was counted as visible before.
					visited.put(data, Boolean.TRUE);
				}
			}

			for (TreeNode childNode : node.getChildren()) {
				visit(childNode, applies);
			}
		}

		@Override
		public String toString() {
			if (groupProvider == null || filterPredicate == null) {
				return EMFCompareIDEUIMessages.getString("EMFCompareStructureMergeViewer.title"); //$NON-NLS-1$
			}
			for (IDifferenceGroup group : groupProvider.getGroups(comparison)) {
				for (TreeNode node : group.getChildren()) {
					visit(node, true);
				}
			}

			@SuppressWarnings("boxing")
			String titleArgument = EMFCompareIDEUIMessages.getString(
					"EMFCompareStructureMergeViewer.titleDesc", //$NON-NLS-1$
					diffsToMergeCount, visibleDiffsCount, diffsCount - visibleDiffsCount);
			return titleArgument;
		}
	}
}
