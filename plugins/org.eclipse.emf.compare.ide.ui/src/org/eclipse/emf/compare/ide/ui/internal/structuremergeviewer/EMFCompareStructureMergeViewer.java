/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.collect.Iterables.getFirst;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.INavigatable;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.ui.dialogs.DiagnosticDialog;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeInput;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.internal.util.ExceptionUtil;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IMergePreviewModeChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProviderChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.scope.IComparisonScope;
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
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipant;
import org.eclipse.team.ui.synchronize.ModelSynchronizeParticipant;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Implementation of {@link AbstractViewerWrapper}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EMFCompareStructureMergeViewer extends AbstractStructuredViewerWrapper<Composite, TreeViewer> implements CommandStackListener {

	/** The width of the tree ruler. */
	private static final int TREE_RULER_WIDTH = 17;

	/** The adapter factory. */
	private ComposedAdapterFactory fAdapterFactory;

	/** The tree ruler associated with this viewer. */
	private EMFCompareDiffTreeRuler treeRuler;

	private ICompareInputChangeListener fCompareInputChangeListener;

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
	private boolean resourcesShouldBeUnload;

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

		config.getEventBus().register(this);

		fHandlerService = CompareHandlerService.createFor(getCompareConfiguration().getContainer(),
				getControl().getShell());

		setLabelProvider(new DelegatingStyledCellLabelProvider(
				new EMFCompareStructureMergeViewerLabelProvider(
						getCompareConfiguration().getAdapterFactory(), this)));
		setContentProvider(new EMFCompareStructureMergeViewerContentProvider(getCompareConfiguration()
				.getAdapterFactory()));

		inputChangedTask.setPriority(Job.LONG);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractViewerWrapper#preHookCreateControlAndViewer()
	 */
	@Override
	protected void preHookCreateControlAndViewer() {
		super.preHookCreateControlAndViewer();
		fAdapterFactory = new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault()
				.getAdapterFactoryRegistry());

		fAdapterFactory.addAdapterFactory(new TreeItemProviderAdapterFactorySpec());
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());

		getCompareConfiguration().setAdapterFactory(fAdapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see 
	 *      org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ViewerWrapper.createControl(Composite,
	 *      CompareConfiguration)
	 */
	@Override
	protected ControlAndViewer<Composite, TreeViewer> createControlAndViewer(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayout(layout);
		control.setLayoutData(data);
		final TreeViewer treeViewer = new EMFCompareDiffTreeViewer(control, fAdapterFactory,
				getCompareConfiguration());
		INavigatable nav = new Navigatable(fAdapterFactory, treeViewer);
		control.setData(INavigatable.NAVIGATOR_PROPERTY, nav);
		control.setData(CompareUI.COMPARE_VIEWER_TITLE, "Model differences");
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true);
		layoutData.widthHint = TREE_RULER_WIDTH;
		layoutData.minimumWidth = TREE_RULER_WIDTH;
		treeRuler = new EMFCompareDiffTreeRuler(control, SWT.NONE, layoutData.widthHint, treeViewer,
				getCompareConfiguration());
		treeRuler.setLayoutData(layoutData);

		fCompareInputChangeListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput input) {
				EMFCompareStructureMergeViewer.this.compareInputChanged(input);
			}
		};

		fWrappedTreeListener = new ITreeViewerListener() {
			public void treeExpanded(TreeExpansionEvent event) {
				treeRuler.redraw();
			}

			public void treeCollapsed(TreeExpansionEvent event) {
				treeRuler.redraw();
			}
		};
		treeViewer.addTreeListener(fWrappedTreeListener);

		fHandlerService = CompareHandlerService.createFor(getCompareConfiguration().getContainer(),
				treeViewer.getControl().getShell());

		EventBus eventBus = new EventBus();
		eventBus.register(this);

		StructureMergeViewerFilter structureMergeViewerFilter = getCompareConfiguration()
				.getStructureMergeViewerFilter();
		structureMergeViewerFilter.install(treeViewer);

		StructureMergeViewerGrouper structureMergeViewerGrouper = getCompareConfiguration()
				.getStructureMergeViewerGrouper();
		structureMergeViewerGrouper.install(treeViewer);

		toolBar = new CompareToolBar(structureMergeViewerGrouper, structureMergeViewerFilter,
				getCompareConfiguration());
		treeViewer.addSelectionChangedListener(toolBar);
		toolBar.initToolbar(CompareViewerPane.getToolBarManager(parent), treeViewer);

		return ControlAndViewer.create(control, treeViewer);
	}

	@Subscribe
	public void handleGroupProviderSelectionChange(IDifferenceGroupProviderChange event) {
		differenceGroupProvider = event.getDifferenceGroupProvider();

		Adapter root = (Adapter)getViewer().getInput();
		if (root != null) {
			TreeNode target = (TreeNode)root.getTarget();
			registerDifferenceGroupProvider(target, differenceGroupProvider);
		}

		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				((EMFCompareDiffTreeViewer)getViewer()).createChildrenSilently(getViewer().getTree());
				treeRuler.computeConsequences();
				treeRuler.redraw();
			}
		});
	}

	/**
	 * @return the differenceGroupProvider
	 */
	public IDifferenceGroupProvider getSelectedDifferenceGroupProvider() {
		if (differenceGroupProvider == null) {
			differenceGroupProvider = getCompareConfiguration().getStructureMergeViewerGrouper()
					.getProvider();
		}
		return differenceGroupProvider;
	}

	protected void registerDifferenceGroupProvider(TreeNode treeNode,
			IDifferenceGroupProvider differenceGroupProvider) {
		List<Adapter> eAdapters = treeNode.eAdapters();
		IDifferenceGroupProvider oldDifferenceGroupProvider = (IDifferenceGroupProvider)EcoreUtil.getAdapter(
				eAdapters, IDifferenceGroupProvider.class);
		if (oldDifferenceGroupProvider != null) {
			eAdapters.remove(oldDifferenceGroupProvider);
		}
		eAdapters.add(differenceGroupProvider);

		treeRuler.computeConsequences();
		treeRuler.redraw();
	}

	@Subscribe
	public void mergePreviewModeChange(IMergePreviewModeChange event) {
		treeRuler.computeConsequences();
		treeRuler.redraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireSelectionChanged(SelectionChangedEvent)
	 */
	@Override
	protected void fireSelectionChanged(SelectionChangedEvent event) {
		super.fireSelectionChanged(event);
		treeRuler.selectionChanged(event);
		treeRuler.redraw();
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
		}
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.addCompareInputChangeListener(fCompareInputChangeListener);

			// Hack to display a message in the tree viewer while the differences are being computed.
			TreeItem item = new TreeItem(getViewer().getTree(), SWT.NONE);
			item.setText("Computing model differences...");

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
		getViewer().removeTreeListener(fWrappedTreeListener);
		Object input = getInput();
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.removeCompareInputChangeListener(fCompareInputChangeListener);
		}
		getViewer().removeSelectionChangedListener(toolBar);
		compareInputChanged((ICompareInput)null);
		treeRuler.handleDispose();
		fAdapterFactory.dispose();
		toolBar.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		if (undoAction != null) {
			undoAction.update();
		}
		if (redoAction != null) {
			redoAction.update();
		}

		Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
		if (mostRecentCommand instanceof ICompareCopyCommand) {
			Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					treeRuler.computeConsequences();
					((EMFCompareDiffTreeViewer)getViewer()).createChildrenSilently(getViewer().getTree());
					treeRuler.redraw();
				}
			});
			if (!affectedObjects.isEmpty()) {
				// MUST NOT call a setSelection with a list, o.e.compare does not handle it (cf
				// org.eclipse.compare.CompareEditorInput#getElement(ISelection))
				Object first = getFirst(affectedObjects, null);
				if (first instanceof EObject) {
					IDifferenceGroupProvider groupProvider = getSelectedDifferenceGroupProvider();
					Iterable<TreeNode> treeNodes = groupProvider.getTreeNodes((EObject)first);
					TreeNode treeNode = getFirst(treeNodes, null);
					if (treeNode != null) {
						final Object adaptedAffectedObject = fAdapterFactory.adapt(treeNode,
								ICompareInput.class);
						SWTUtil.safeAsyncExec(new Runnable() {
							public void run() {
								refresh();
								setSelection(new StructuredSelection(adaptedAffectedObject), true);
							}
						});
					}
				}
			}
		} else {
			// FIXME, should recompute the difference, something happened outside of this compare editor
		}

	}

	private Job inputChangedTask = new Job("Compute Model Differences") {
		@Override
		public IStatus run(IProgressMonitor monitor) {
			SubMonitor subMonitor = SubMonitor.convert(monitor, "Computing Model Differences", 100);
			compareInputChanged((ICompareInput)getInput(), subMonitor.newChild(100));
			return Status.OK_STATUS;
		}
	};

	private CompareToolBar toolBar;

	private IDifferenceGroupProvider differenceGroupProvider;

	/**
	 * Triggered by fCompareInputChangeListener and {@link #inputChanged(Object, Object)}.
	 */
	void compareInputChanged(ICompareInput input) {
		if (input == null) {
			// When closing, we don't need a progress monitor to handle the input change
			compareInputChanged((ICompareInput)null, new NullProgressMonitor());
			return;
		}
		// The compare configuration is nulled when the viewer is disposed
		if (getCompareConfiguration() != null) {
			inputChangedTask.schedule();
		}
	}

	void compareInputChanged(CompareInputAdapter input, IProgressMonitor monitor) {
		ICompareEditingDomain editingDomain = getCompareConfiguration().getEditingDomain();
		editingDomain.getCommandStack().addCommandStackListener(this);

		compareInputChanged(null, (Comparison)input.getComparisonObject());
	}

	void compareInputChanged(ComparisonScopeInput input, IProgressMonitor monitor) {
		ICompareEditingDomain editingDomain = getCompareConfiguration().getEditingDomain();
		editingDomain.getCommandStack().addCommandStackListener(this);

		EMFCompare comparator = getCompareConfiguration().getEMFComparator();

		IComparisonScope comparisonScope = input.getComparisonScope();
		Comparison comparison = comparator.compare(comparisonScope, BasicMonitor.toMonitor(monitor));

		reportErrors(comparison);

		compareInputChanged(input.getComparisonScope(), comparison);
	}

	void compareInputChanged(final IComparisonScope scope, final Comparison comparison) {
		if (!getControl().isDisposed()) { // guard against disposal
			final TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
			treeNode.setData(comparison);

			getCompareConfiguration().setComparisonAndScope(comparison, scope);
			registerDifferenceGroupProvider(treeNode, getSelectedDifferenceGroupProvider());

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					// Mandatory for the EMFCompareDiffTreeRuler, all TreeItems must have been created
					((EMFCompareDiffTreeViewer)getViewer()).refreshAfterDiff(getViewer().getInput());
					((EMFCompareDiffTreeViewer)getViewer()).createChildrenSilently(getViewer().getTree());
					((EMFCompareDiffTreeViewer)getViewer()).initialSelection();
				}
			});

			ICompareEditingDomain editingDomain = getCompareConfiguration().getEditingDomain();

			undoAction = new UndoAction(editingDomain);
			redoAction = new RedoAction(editingDomain);

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
		}
	}

	void compareInputChanged(ICompareInput input, IProgressMonitor monitor) {
		if (input != null) {
			if (input instanceof CompareInputAdapter) {
				resourcesShouldBeUnload = false;
				compareInputChanged((CompareInputAdapter)input, monitor);
			} else if (input instanceof ComparisonScopeInput) {
				resourcesShouldBeUnload = false;
				compareInputChanged((ComparisonScopeInput)input, monitor);
			} else {
				resourcesShouldBeUnload = true;
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

				final ITypedElement left = input.getLeft();
				final ITypedElement right = input.getRight();
				final ITypedElement origin = input.getAncestor();

				IComparisonScope scope = null;
				try {
					scope = buildComparisonScope(left, right, origin, subMonitor.newChild(85));
				} catch (Exception e) {
					ExceptionUtil.handleException(e, getCompareConfiguration(), true);
					return;
				}
				final Comparison compareResult = EMFCompare
						.builder()
						.setMatchEngineFactoryRegistry(
								EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry())
						.setPostProcessorRegistry(EMFCompareRCPPlugin.getDefault().getPostProcessorRegistry())
						.build().compare(scope, BasicMonitor.toMonitor(subMonitor.newChild(15)));

				reportErrors(compareResult);

				final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
				final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
				final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

				if (getCompareConfiguration() != null) {
					ICompareEditingDomain editingDomain = getCompareConfiguration().getEditingDomain();
					if (editingDomain != null) {
						editingDomain.getCommandStack().removeCommandStackListener(this);
						if (editingDomain instanceof IDisposable) {
							((IDisposable)editingDomain).dispose();
						}
					}

					editingDomain = EMFCompareEditingDomain.create(leftResourceSet, rightResourceSet,
							originResourceSet);
					editingDomain.getCommandStack().addCommandStackListener(this);
					getCompareConfiguration().setEditingDomain(editingDomain);
				}

				compareInputChanged(scope, compareResult);
			}
		} else {
			ResourceSet leftResourceSet = null;
			ResourceSet rightResourceSet = null;
			ResourceSet originResourceSet = null;

			if (getCompareConfiguration().getComparison() != null) {
				Comparison comparison = getCompareConfiguration().getComparison();
				Iterator<Match> matchIt = comparison.getMatches().iterator();
				if (comparison.isThreeWay()) {
					while (matchIt.hasNext()
							&& (leftResourceSet == null || rightResourceSet == null || originResourceSet == null)) {
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

			ICompareEditingDomain editingDomain = getCompareConfiguration().getEditingDomain();
			if (editingDomain != null) {
				editingDomain.getCommandStack().removeCommandStackListener(this);
				getCompareConfiguration().setEditingDomain(null);
				if (editingDomain instanceof IDisposable) {
					((IDisposable)editingDomain).dispose();
				}
				editingDomain = null;
			}

			if (resourcesShouldBeUnload) {
				unload(leftResourceSet);
				unload(rightResourceSet);
				unload(originResourceSet);
			}

			if (getCompareConfiguration() != null) {
				getCompareConfiguration().dispose();
			}
			getViewer().setInput(null);
		}
	}

	private void reportErrors(final Comparison comparison) {
		if (ComparisonUtil.containsErrors(comparison)) {
			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					DiagnosticDialog.open(getControl().getShell(), "Comparison report", //$NON-NLS-1$
							"Some issues were detected.", comparison.getDiagnostic()); //$NON-NLS-1$
				}
			});
		}
	}

	/**
	 * Constructs the comparison scope corresponding to the given typed elements.
	 * 
	 * @param left
	 *            Left of the compared elements.
	 * @param right
	 *            Right of the compared elements.
	 * @param origin
	 *            Common ancestor of the <code>left</code> and <code>right</code> compared elements.
	 * @param monitor
	 *            Monitor to report progress on.
	 * @return The created comparison scope.
	 */
	private IComparisonScope buildComparisonScope(ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor) {
		IStorageProviderAccessor storageAccessor = null;
		if (getSubscriber() != null) {
			storageAccessor = new SubscriberStorageAccessor(getSubscriber());
		}
		IStorage leftStorage = PlatformElementUtil.findFile(left);
		if (leftStorage == null) {
			leftStorage = StreamAccessorStorage.fromTypedElement(left);
		}
		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), storageAccessor);
		return scopeBuilder.build(left, right, origin, monitor);
	}

	/**
	 * Team left us with absolutely no way to determine whether our supplied input is the result of a
	 * synchronization or not.
	 * <p>
	 * In order to properly resolve the logical model of the resource currently being compared we need to know
	 * what "other" resources were part of its logical model, and we need to know the revisions of these
	 * resources we are to load. All of this has already been computed by Team, but it would not let us know.
	 * This method uses discouraged means to get around this "black box" locking from Team.
	 * </p>
	 * <p>
	 * The basic need here is to retrieve the Subscriber from this point. We have a lot of accessible
	 * variables, the two most important being the CompareConfiguration and ICompareInput... I could find no
	 * way around the privileged access to the private ModelCompareEditorInput.participant field. There does
	 * not seem to be any adapter (or Platform.getAdapterManager().getAdapter(...)) that would allow for this,
	 * so I'm taking the long way 'round.
	 * </p>
	 * 
	 * @return The subscriber used for this comparison if any could be found, <code>null</code> otherwise.
	 */
	@SuppressWarnings("restriction")
	private Subscriber getSubscriber() {
		if (getCompareConfiguration().getContainer() instanceof ModelCompareEditorInput) {
			final ModelCompareEditorInput modelInput = (ModelCompareEditorInput)getCompareConfiguration()
					.getContainer();
			ISynchronizeParticipant participant = null;
			try {
				final Field field = ModelCompareEditorInput.class.getDeclaredField("participant"); //$NON-NLS-1$
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						field.setAccessible(true);
						return null;
					}
				});
				participant = (ISynchronizeParticipant)field.get(modelInput);
			} catch (NoSuchFieldException e) {
				// Swallow this, this private field was there at least from 3.5 to 4.3
			} catch (IllegalArgumentException e) {
				// Cannot happen
			} catch (IllegalAccessException e) {
				// "Should" not happen, but ignore it anyway
			}
			if (participant instanceof ModelSynchronizeParticipant
					&& ((ModelSynchronizeParticipant)participant).getContext() instanceof SubscriberMergeContext) {
				return ((SubscriberMergeContext)((ModelSynchronizeParticipant)participant).getContext())
						.getSubscriber();
			}
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
		getViewer().refresh();

	}
}
