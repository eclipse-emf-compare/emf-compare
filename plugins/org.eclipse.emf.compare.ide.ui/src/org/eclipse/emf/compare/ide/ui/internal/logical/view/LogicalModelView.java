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

import com.google.common.base.Throwables;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressInfoComposite;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressMonitorWrapper;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * A simple view displaying the resources of a logical model.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class LogicalModelView extends CommonNavigator {

	/** Container widget of table and progress bar. */
	private Composite container;

	/** The viewer displaying resources. */
	private CommonViewer viewer;

	/** The content provider of the viewer. */
	private LogicalModelViewContentProvider viewContentProvider;

	/** Display progress bar while computing logical model. */
	private JobProgressInfoComposite progressInfoItem;

	/** Job executed when the selection changed. */
	private SelectionChangedJob selectionChangedTask;

	/** Synchronize the viewer with editors and selection. */
	private IAction synchronizeAction;

	/** Show files in flat list. */
	private Action listPresentationAction;

	/** Show folder structure in full tree. */
	private Action treePresentationAction;

	/** Selection listener. */
	private ListenToSelection listenToSelection;

	/** The service that tracks selection changes. */
	private ISelectionService selectionService;

	/** Keeps track of the last active workbench part. */
	private IWorkbenchPart lastPart;

	/** Keeps track of the last selection. */
	private ISelection lastSelection;

	/** Keeps track of the synchronization button state. */
	private boolean synchroActive;

	/** Default presentation for the viewer. */
	private Presentation presentation = Presentation.LIST;

	/** The preference store of the EMF Compare IDE UI plugin. */
	private final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();

	/** The listener of changes for the preference store of the EMF Compare IDE UI plugin. */
	private IPropertyChangeListener preferenceStoreListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			if (synchroActive && EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE == event.getProperty()
					&& !event.getOldValue().equals(event.getNewValue())) {
				selectionChangedTask.schedule();
			}
		}
	};

	/**
	 * Presentation mode of the viewer.
	 */
	public enum Presentation {
		/** Show files in flat list. */
		LIST,
		/** Show folder structure in full tree. */
		TREE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		super.createPartControl(container);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.navigator.CommonNavigator#createCommonViewerObject(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CommonViewer createCommonViewerObject(Composite aParent) {

		selectionChangedTask = new SelectionChangedJob(EMFCompareIDEUIMessages
				.getString("LogicalModelView.computingLogicalModel")); //$NON-NLS-1$
		selectionChangedTask.setPriority(Job.LONG);

		progressInfoItem = new JobProgressInfoComposite(selectionChangedTask, aParent, SWT.SMOOTH
				| SWT.HORIZONTAL, SWT.NONE);
		progressInfoItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		progressInfoItem.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		viewer = super.createCommonViewerObject(aParent);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewContentProvider = new LogicalModelViewContentProvider(this);
		viewer.setContentProvider(viewContentProvider);
		viewer.setLabelProvider(new LogicalModelViewLabelProvider(this));
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		updateLayout(false, false);

		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		selectionService = activeWorkbenchWindow.getSelectionService();

		makeActions();
		fillToolbar();

		return viewer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (getCommonViewer().getControl().isVisible()) {
			getCommonViewer().getControl().setFocus();
		} else {
			progressInfoItem.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (listenToSelection != null) {
			selectionService.removePostSelectionListener(listenToSelection);
		}
		if (!selectionChangedTask.cancel()) {
			try {
				selectionChangedTask.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Throwables.propagate(e);
			}
		}
		super.dispose();
	}

	/**
	 * Get the presentation of the viewer.
	 * 
	 * @return selected presentation
	 */
	Presentation getPresentation() {
		return presentation;
	}

	/**
	 * Create actions.
	 */
	@SuppressWarnings("static-access")
	private void makeActions() {

		if (listenToSelection == null) {
			listenToSelection = new ListenToSelection();
		}
		selectionService.addPostSelectionListener(listenToSelection);

		String synchronizationLabel = EMFCompareIDEUIMessages
				.getString("LogicalModelView.linkWithEditorAndSelection"); //$NON-NLS-1$
		synchronizeAction = new Action(synchronizationLabel, IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				if (isChecked()) {
					synchroActive = true;
					store.addPropertyChangeListener(preferenceStoreListener);
					listenToSelection.selectionChanged(lastPart, lastSelection);
				} else {
					synchroActive = false;
					store.removePropertyChangeListener(preferenceStoreListener);
				}

			}
		};
		synchronizeAction.setToolTipText(synchronizationLabel);
		synchronizeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_ELCL_SYNCED));

		listPresentationAction = new Action(EMFCompareIDEUIMessages
				.getString("LogicalModelView.listPresentation.title"), IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$
			@Override
			public void run() {
				if (!isChecked()) {
					return;
				}
				presentation = Presentation.LIST;
				treePresentationAction.setChecked(false);
				SWTUtil.safeSyncExec(new Runnable() {
					public void run() {
						getCommonViewer().refresh();
					}
				});
			}
		};
		listPresentationAction.setImageDescriptor(EMFCompareIDEUIPlugin.getDefault().getImageDescriptor(
				"icons/full/eobj16/flatLayout.gif")); //$NON-NLS-1$

		treePresentationAction = new Action(EMFCompareIDEUIMessages
				.getString("LogicalModelView.treePresentation.title"), IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$
			@Override
			public void run() {
				if (!isChecked()) {
					return;
				}
				presentation = Presentation.TREE;
				listPresentationAction.setChecked(false);
				SWTUtil.safeSyncExec(new Runnable() {
					public void run() {
						getCommonViewer().refresh();
					}
				});
			}
		};
		treePresentationAction.setImageDescriptor(EMFCompareIDEUIPlugin.getDefault().getImageDescriptor(
				"icons/full/eobj16/hierarchicalLayout.gif")); //$NON-NLS-1$
	}

	/**
	 * Contribute actions to the toolbar.
	 */
	private void fillToolbar() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(synchronizeAction);
		IMenuManager dropdownMenu = bars.getMenuManager();

		listPresentationAction.setChecked(true);
		dropdownMenu.add(listPresentationAction);
		dropdownMenu.add(treePresentationAction);
	}

	/**
	 * Switch the display between the progress bar and the viewer.
	 * 
	 * @param displayProgress
	 *            true to display the progress bar, false to display the viewer.
	 * @param doLayout
	 *            true to layout the container of the progress bar and the viewer.
	 */
	private void updateLayout(boolean displayProgress, boolean doLayout) {
		((GridData)progressInfoItem.getLayoutData()).exclude = !displayProgress;
		progressInfoItem.setVisible(displayProgress);

		((GridData)viewer.getControl().getLayoutData()).exclude = displayProgress;
		viewer.getControl().setVisible(!displayProgress);

		if (doLayout) {
			container.layout(true, true);
		}
	}

	/**
	 * Listener that reacts on selection changes.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private final class ListenToSelection implements ISelectionListener {

		/**
		 * Notifies this listener that the selection has changed.
		 * 
		 * @param part
		 *            the workbench part containing the selection.
		 * @param selection
		 *            the current selection.
		 */
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (!(part instanceof LogicalModelView)) {
				lastPart = part;
				lastSelection = selection;
			}
			if (synchroActive && selection != null && !selection.isEmpty()
					&& !selection.equals(selectionChangedTask.getSelection())) {
				selectionChangedTask.setPart(part);
				selectionChangedTask.setSelection(selection);
				selectionChangedTask.schedule();
			}
		}
	}

	/**
	 * Job executed on selection changes.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private final class SelectionChangedJob extends Job {

		/**
		 * The workbench part from which the selection occurs.
		 */
		private IWorkbenchPart part;

		/**
		 * The selection.
		 */
		private ISelection selection;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            the job's name.
		 */
		private SelectionChangedJob(String name) {
			super(name);
		}

		/**
		 * Set the workbench part.
		 * 
		 * @param part
		 *            the workbench part.
		 */
		public void setPart(IWorkbenchPart part) {
			this.part = part;
		}

		/**
		 * Get the last selection associated with this job.
		 * 
		 * @return the last selection associated with this job.
		 */
		public ISelection getSelection() {
			return selection;
		}

		/**
		 * Set a new selection to associate with this job.
		 * 
		 * @param selection
		 *            the selection.
		 */
		public void setSelection(ISelection selection) {
			this.selection = selection;
		}

		/**
		 * Compute the logical model from the IFile corresponding to the selection, and then display those
		 * resources in the viewer.
		 * 
		 * @param monitor
		 *            to monitor the whole process.
		 * @return the status of the whole process.
		 */
		@Override
		public IStatus run(IProgressMonitor monitor) {
			IStatus status = Status.OK_STATUS;
			IProgressMonitor wrapper = new JobProgressMonitorWrapper(monitor, progressInfoItem);
			SubMonitor subMonitor = SubMonitor.convert(wrapper, 100);

			final ILogicalModelViewHandler handler = EMFCompareIDEUIPlugin.getDefault()
					.getLogicalModelViewHandlerRegistry().getBestHandlerFor(part, selection);

			if (handler != null) {

				// Display progress bar
				SWTUtil.safeSyncExec(new Runnable() {
					public void run() {
						if (!container.isDisposed()) {
							updateLayout(true, true);
						}
					}
				});

				// Retrieve logical models
				final Collection<SynchronizationModel> logicalModels = handler.getSynchronizationModels(part,
						selection, subMonitor.newChild(50));
				for (SynchronizationModel logicalModel : logicalModels) {
					Diagnostic diagnostic = logicalModel.getDiagnostic();
					if (diagnostic != null && diagnostic.getSeverity() != Diagnostic.OK) {
						if (status != Status.CANCEL_STATUS) {
							SWTUtil.safeSyncExec(new Runnable() {
								public void run() {
									MessageDialog
											.openError(
													LogicalModelView.this.getSite().getShell(),
													EMFCompareIDEUIMessages
															.getString("LogicalModelView.errorDialog.title"), EMFCompareIDEUIMessages //$NON-NLS-1$
															.getString("LogicalModelView.errorDialog.message")); //$NON-NLS-1$
								}
							});
							status = Status.CANCEL_STATUS;
						}
					}
				}

				// Retrieve resources from logical models
				final Collection<IResource> resources;
				if (status == Status.OK_STATUS) {
					resources = LogicalModelViewHandlerUtil.getLogicalModelResources(logicalModels,
							subMonitor.newChild(50));
				} else {
					resources = Collections.emptySet();
				}

				// Display resources in viewer
				if (!monitor.isCanceled()) {
					SWTUtil.safeSyncExec(new Runnable() {
						public void run() {
							updateLayout(false, true);
							viewContentProvider.setLeaves(resources);
							getCommonViewer().refresh();
						}
					});
				} else {
					status = Status.CANCEL_STATUS;
				}
			}

			return status;
		}
	}
}
