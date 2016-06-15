/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.e4;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.emf.compare.ide.ui.internal.treecontentmanager.EMFCompareDeferredTreeContentManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.progress.ProgressMessages;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.eclipse.ui.progress.PendingUpdateAdapter;
import org.eclipse.ui.progress.UIJob;

/**
 * Subclass of {@link DeferredTreeContentManager} which works without a workbench.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class E4DeferredTreeContentManager extends DeferredTreeContentManager implements EMFCompareDeferredTreeContentManager {

	/**
	 * The treeviewer whose content is to manage.
	 */
	private final AbstractTreeViewer treeViewer;

	/**
	 * Listener collection.
	 */
	private ListenerList updateCompleteListenerList;

	/**
	 * Constructor.
	 * 
	 * @param treeViewer
	 *            The treeViewer to update.
	 */
	public E4DeferredTreeContentManager(AbstractTreeViewer treeViewer) {
		super(treeViewer);
		this.treeViewer = treeViewer;
	}

	@Override
	protected PendingUpdateAdapter createPendingUpdateAdapter() {
		return new E4PendingUpdateAdapter();
	}

	@Override
	protected void addChildren(final Object parent, final Object[] children, final IProgressMonitor monitor) {
		UIJob updateJob = new UIJob(Display.getDefault(),
				ProgressMessages.DeferredTreeContentManager_AddingChildren) {
			@Override
			public IStatus runInUIThread(IProgressMonitor updateMonitor) {
				// Cancel the job if the tree viewer got closed
				if (treeViewer.getControl().isDisposed() || updateMonitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				treeViewer.add(parent, children);
				return Status.OK_STATUS;
			}
		};
		updateJob.setSystem(true);
		updateJob.schedule();
	}

	@Override
	protected void runClearPlaceholderJob(PendingUpdateAdapter placeholder) {
		final E4PendingUpdateAdapter e4Placeholder = (E4PendingUpdateAdapter)placeholder;
		if (e4Placeholder.isRemoved()) {
			return;
		}
		// Clear the placeholder if it is still there
		UIJob clearJob = new UIJob(Display.getDefault(),
				ProgressMessages.DeferredTreeContentManager_ClearJob) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (!e4Placeholder.isRemoved()) {
					Control control = treeViewer.getControl();
					if (control.isDisposed()) {
						return Status.CANCEL_STATUS;
					}
					treeViewer.remove(e4Placeholder);
					e4Placeholder.setRemoved(true);
				}
				return Status.OK_STATUS;
			}
		};
		clearJob.setSystem(true);

		if (updateCompleteListenerList != null) {
			Object[] listeners = updateCompleteListenerList.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				clearJob.addJobChangeListener((IJobChangeListener)listeners[i]);
			}
		}
		clearJob.schedule();
	}

	// XXX Copied from super class since we need access to updateCompleteListenerList within
	// #runClearPlaceholderJob
	@Override
	public void addUpdateCompleteListener(IJobChangeListener listener) {
		if (listener == null && updateCompleteListenerList != null) {
			Object[] listeners = updateCompleteListenerList.getListeners();
			if (listeners.length == 1) {
				removeUpdateCompleteListener((IJobChangeListener)listeners[0]);
			}
		} else {
			if (updateCompleteListenerList == null) {
				updateCompleteListenerList = new ListenerList();
			}
			updateCompleteListenerList.add(listener);
		}
	}

	// XXX Copied from super class since we need access to updateCompleteListenerList within
	// #runClearPlaceholderJob
	@Override
	public void removeUpdateCompleteListener(IJobChangeListener listener) {
		if (updateCompleteListenerList != null) {
			updateCompleteListenerList.remove(listener);
		}
	}

}
