/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - public CallbackType visibility for testing
 *     Stefan Dirix - bug 473985
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.treecontentmanager.EMFCompareDeferredTreeContentManager;
import org.eclipse.emf.compare.ide.ui.internal.treecontentmanager.EMFCompareDeferredTreeContentManagerUtil;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider2;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IViewerNotification;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.NotifyChangedToViewerRefresh;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;
import org.eclipse.ui.progress.PendingUpdateAdapter;

/**
 * Specialized AdapterFactoryContentProvider for the emf compare structure merge viewer.
 * <p>
 * <i>This class is not intended to be used outside of its package. It has been set to public for testing
 * purpose only.</i>
 * </p>
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareStructureMergeViewerContentProvider extends AdapterFactoryContentProvider implements IJobChangeListener {

	/**
	 * Class to listen the state of the content provider.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	static class FetchListener {

		/**
		 * This method is called when the content provider starts fetching new elements in an external Job.
		 */
		public void startFetching() {

		}

		/**
		 * This method is called when the content provider has finished fetching elements and has removed the
		 * {@link PendingUpdateAdapter} from the tree.
		 */
		public void doneFetching() {

		}
	}

	/**
	 * Callback holder used to defer the run of a callback in a specific thread.
	 * 
	 * @see {@link #callback}
	 * @see EMFCompareStructureMergeViewerContentProvider#runWhenReady(CallbackType, Runnable)
	 */
	private static class CallbackHolder {

		private final Runnable callback;

		private final CallbackType callbackType;

		public CallbackHolder(Runnable callback, CallbackType callbackType) {
			super();
			this.callback = callback;
			this.callbackType = callbackType;
		}

		private Runnable getCallback() {
			return callback;
		}

		private CallbackType getType() {
			return callbackType;
		}

	}

	/**
	 * {@link IDeferredWorkbenchAdapter} using this content provider to fetch children.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class EMFCompareStructureMergeViewerContentProviderDeferredAdapter implements IDeferredWorkbenchAdapter {

		private final EMFCompareStructureMergeViewerContentProvider contentProvider;

		public EMFCompareStructureMergeViewerContentProviderDeferredAdapter(
				EMFCompareStructureMergeViewerContentProvider contentProvider) {
			super();
			this.contentProvider = contentProvider;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link IDeferredWorkbenchAdapter#getChildren(Object)}
		 */
		public Object[] getChildren(Object o) {
			// Not used
			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link IDeferredWorkbenchAdapter#getImageDescriptor(Object)}
		 */
		public ImageDescriptor getImageDescriptor(Object object) {
			// Not used
			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link IDeferredWorkbenchAdapter#getLabel(Object)}
		 */
		public String getLabel(Object o) {
			return EMFCompareIDEUIMessages.getString(
					"EMFCompareStructureMergeViewerContentProvider.deferredWorkbenchAdapter.label"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link IDeferredWorkbenchAdapter#getParent(Object)}
		 */
		public Object getParent(Object o) {
			// Not used
			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter
		 *      {@link IDeferredWorkbenchAdapter#fetchDeferredChildren(Object object, IElementCollector collector, IProgressMonitor monitor)}
		 */
		public void fetchDeferredChildren(Object object, IElementCollector collector,
				IProgressMonitor monitor) {
			if (!monitor.isCanceled()) {
				if (object instanceof CompareInputAdapter) {
					Notifier target = ((Adapter)object).getTarget();
					Object[] children = contentProvider.getChildren(target);
					collector.add(children, monitor);
				}
			}

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link IDeferredWorkbenchAdapter#isContainer()}
		 */
		public boolean isContainer() {
			// Not used
			return true;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see {IDeferredWorkbenchAdapter{@link #getRule(Object)}
		 */
		public ISchedulingRule getRule(Object object) {
			// Not used
			return null;
		}

	}

	/** {@link DeferredTreeContentManager} use to fetch groups in a external {@link Job}. */
	private final EMFCompareDeferredTreeContentManager contentManagerAdapter;

	/** Holds true if this content provider is currently fetching children. */
	private boolean isFetchingGroup;

	/** Will protect R/W of {@link #pending} and {@link #isFetchingGroup}. */
	private final ReentrantLock lock;

	/** Object listening the status of this object. */
	private final List<FetchListener> listeners;

	/** List of current callbacks. Callbacks are only run once. */
	private List<CallbackHolder> callbacks;

	/** Pending object displayed in the tree. */
	private Object[] pending;

	/**
	 * Constructs the content provider with the appropriate adapter factory.
	 * 
	 * @param adapterFactory
	 *            The adapter factory used to construct the content provider.
	 */
	public EMFCompareStructureMergeViewerContentProvider(AdapterFactory adapterFactory,
			AbstractTreeViewer viewer) {
		super(adapterFactory);
		contentManagerAdapter = EMFCompareDeferredTreeContentManagerUtil
				.createEMFDeferredTreeContentManager(viewer);
		contentManagerAdapter.addUpdateCompleteListener(this);
		lock = new ReentrantLock();
		listeners = new CopyOnWriteArrayList<EMFCompareStructureMergeViewerContentProvider.FetchListener>();
		callbacks = new CopyOnWriteArrayList<EMFCompareStructureMergeViewerContentProvider.CallbackHolder>();

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getParent(Object object)
	 */
	@Override
	public Object getParent(Object element) {
		final Object ret;
		if (element instanceof CompareInputAdapter) {
			Object parentNode = super.getParent(((Adapter)element).getTarget());
			if (parentNode instanceof TreeNode) {
				final Optional<Adapter> cia = Iterators.tryFind(((TreeNode)parentNode).eAdapters().iterator(),
						instanceOf(CompareInputAdapter.class));
				if (cia.isPresent()) {
					ret = cia.get();
				} else {
					ret = parentNode;
				}
			} else {
				ret = parentNode;
			}
		} else if (element instanceof ICompareInput) {
			ret = null;
		} else {
			ret = super.getParent(element);
		}
		return ret;
	}

	/**
	 * Enum used for better readability of the method
	 * {@link EMFCompareStructureMergeViewerContentProvider#runWhenReady(CallbackType, Runnable)}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	// public for testing
	public static enum CallbackType {
		/** Run the runnable in the UI thread synchronously. */
		IN_UI_SYNC,
		/** Run the runnable in the UI thread asynchronously. */
		IN_UI_ASYNC,
		/** Run the runnable in the current thread. */
		IN_CURRENT_THREAD
	}

	/**
	 * Run the given runnable in the specified thread when then content provider is ready. It can be run
	 * directly if the content provider is not fecthing or during a callback when the content provider is done
	 * fetching.
	 * 
	 * @param type
	 *            of thread to run the {@link Runnable} inside.
	 * @param runnable
	 *            to run
	 */
	public void runWhenReady(CallbackType type, final Runnable runnable) {
		// Prevents adding a callback if another thread set this content provider as not fetching.
		lock.lock();
		try {
			if (isFetchingGroup) {
				callbacks.add(new CallbackHolder(runnable, type));
			} else {
				run(runnable, type);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Runs a callback in the related thread.
	 * 
	 * @param callback
	 *            to run.
	 * @param type
	 *            of thread.
	 */
	private void run(Runnable callback, CallbackType type) {
		switch (type) {
			case IN_UI_SYNC:
				SWTUtil.safeSyncExec(callback);
				break;
			case IN_UI_ASYNC:
				SWTUtil.safeAsyncExec(callback);
				break;
			default:
				callback.run();
				break;
		}
	}

	/**
	 * Adds a listener to this content provider.
	 * 
	 * @param listener
	 *            to add
	 * @return
	 */
	public boolean addFetchingListener(FetchListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Removes a listener to this content provider.
	 * 
	 * @param listener
	 *            to remove
	 * @return
	 */
	public boolean removeFetchingListener(FetchListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(Object object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		final boolean ret;
		if (element instanceof CompareInputAdapter) {
			ret = super.hasChildren(((Adapter)element).getTarget());
		} else if (element instanceof ICompareInput) {
			ret = false;
		} else {
			ret = super.hasChildren(element);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object element) {
		Object[] children;
		if (element instanceof CompareInputAdapter) {
			children = getCompareInputAdapterChildren((CompareInputAdapter)element);
		} else if (element instanceof ICompareInput) {
			children = new Object[] {};
		} else {
			children = super.getChildren(element);
		}

		// Avoid NPE.
		if (children == null) {
			children = new Object[] {};
		}
		// Only adapt if the children are not PendingUpdateAdapters
		Object[] result = new Object[children.length];
		for (int i = 0; i < result.length; i++) {
			Object child = children[i];
			if (child instanceof PendingUpdateAdapter) {
				result[i] = child;
			} else {
				result[i] = adapterFactory.adapt(child, ICompareInput.class);
				if (result[i] == null) {
					result[i] = child;
				}
			}
		}
		return result;
	}

	/**
	 * Returns a {@link PendingUpdateAdapter} while a Job is fetching the children for this object or the
	 * children if they have already been fetched.
	 * <p>
	 * When the job is finished it will autamically replace the {@link PendingUpdateAdapter} by the fetched
	 * children. The fetched children will be stored under the TreeItem holding the input object or at the
	 * root of the tree if the input object match the input of the tree viewer.
	 * </p>
	 * 
	 * @param compareInputAdapter
	 * @return
	 */
	private Object[] getCompareInputAdapterChildren(CompareInputAdapter compareInputAdapter) {
		Notifier target = compareInputAdapter.getTarget();
		if (target instanceof TreeNode) {
			TreeNode treeNode = (TreeNode)target;
			if (treeNode.getData() instanceof Comparison) {
				IDifferenceGroupProvider2 groupProvider2 = getGroupProvider2(treeNode);
				// Handles the first initialisation of the groups.
				lock.lock();
				try {
					if (groupProvider2 != null && !groupProvider2.groupsAreBuilt()) {
						return deferReturnChildren(compareInputAdapter);
					}
				} finally {
					lock.unlock();
				}
			}
		}
		return super.getChildren(compareInputAdapter.getTarget());
	}

	private Object[] deferReturnChildren(CompareInputAdapter compareInputAdapter) {
		if (!isFetchingGroup) {
			isFetchingGroup = true;
			/*
			 * Notifies listeners that the content provider starts fetching here and not in
			 * EMFCompareStructureMergeViewerContentProvider#aboutToRun() since it is only notified on the
			 * "clear pending updater" job events and not on the "fetching children" job events.
			 * @see org.eclipse.ui.progress.DeferredTreeContentManager.runClearPlaceholderJob(
			 * PendingUpdateAdapter)
			 */
			for (FetchListener callback : listeners) {
				callback.startFetching();
			}
			compareInputAdapter.setDeferredAdapter(
					new EMFCompareStructureMergeViewerContentProviderDeferredAdapter(this));
			pending = contentManagerAdapter.getChildren(compareInputAdapter);
		}
		return pending;
	}

	private IDifferenceGroupProvider2 getGroupProvider2(TreeNode treeNode) {
		IDifferenceGroupProvider2 result = null;
		Optional<Adapter> searchResult = Iterables.tryFind(treeNode.eAdapters(),
				Predicates.instanceOf(IDifferenceGroupProvider2.class));
		if (searchResult.isPresent()) {
			return (IDifferenceGroupProvider2)searchResult.get();
		}
		return result;
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
	 * {@inheritDoc}
	 * 
	 * @see IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		contentManagerAdapter.removeUpdateCompleteListener(this);
		listeners.clear();
	}

	/**
	 * This implementation specializes the EMF implementation to ensure that if more than 30 notifications
	 * arrive, the viewer is simply refreshed rather than processing each update separately. This is
	 * especially important for when undo is invoked in editor and there are many things to be undone.
	 * <p/>
	 * {@inheritDoc}
	 * 
	 * @see IContentProvider#dispose()
	 */
	@Override
	public void notifyChanged(Notification notification) {
		if (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed()
				&& viewer.getInput() != null) {
			if (notification instanceof IViewerNotification) {
				if (viewerRefresh == null) {
					viewerRefresh = new EMFCompareViewerRefresh(viewer);
				}

				if (viewerRefresh.addNotification((IViewerNotification)notification)) {
					viewer.getControl().getDisplay().asyncExec(viewerRefresh);
				}
			} else {
				NotifyChangedToViewerRefresh.handleNotifyChanged(viewer, notification.getNotifier(),
						notification.getEventType(), notification.getFeature(), notification.getOldValue(),
						notification.getNewValue(), notification.getPosition());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener{@link #aboutToRun(IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent event) {
		/*
		 * Nothing to do here since it has already been done in
		 * EMFCompareStructureMergeViewerContentProvider#getCompareInputAdapterChildren(CompareInputAdapter
		 * compareInputAdapter)
		 */
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener#awake(IJobChangeEvent)
	 */
	public void awake(IJobChangeEvent event) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener#done(IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		if (event.getResult().isOK()) {
			// Prevents running callbacks while another thread add a new callback or another thread launch a
			// fetching job.
			lock.lock();
			try {
				if (isFetchingGroup) {
					isFetchingGroup = false;
					pending = null;
					for (FetchListener listener : listeners) {
						listener.doneFetching();
						// If the listener starts to fetch again then stop notifying listeners and wait for
						// the content provider to be ready before re-starting.
						if (isFetchingGroup) {
							return;
						}
					}

					final Iterator<CallbackHolder> callbacksIterator = callbacks.iterator();

					while (callbacksIterator.hasNext()) {
						CallbackHolder callbackHolder = callbacksIterator.next();
						run(callbackHolder.getCallback(), callbackHolder.getType());
						// If the callback has started to fetch again the stop running callbacks and wait for
						// the content provider to be ready.
						if (isFetchingGroup) {
							List<CallbackHolder> remainingCallBack = Lists.newArrayList(callbacksIterator);
							callbacks = new CopyOnWriteArrayList<EMFCompareStructureMergeViewerContentProvider.CallbackHolder>(
									remainingCallBack);
							return;
						}
					}

					callbacks.clear();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener#running(IJobChangeEvent)
	 */
	public void running(IJobChangeEvent event) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener#scheduled(IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent event) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IJobChangeListener#sleeping(IJobChangeEvent)
	 */
	public void sleeping(IJobChangeEvent event) {
		// Nothing to do
	}

	/**
	 * This custom implementation of a ViewerRefresh intends to suppress the thousands of
	 * NoSuchMethodExceptions thrown by the regular implementation when it calls
	 * {@link AdapterFactoryEditingDomain#isStale(Object)}.
	 */
	private static class EMFCompareViewerRefresh extends ViewerRefresh {
		int count = 0;

		final Viewer targetViewer;

		public EMFCompareViewerRefresh(Viewer viewer) {
			super(viewer);
			targetViewer = viewer;
		}

		/**
		 * Copy/paste of the super implementation except for the
		 * {@link AdapterFactoryEditingDomain#isStale(Object)} calls which are replaced with calls to
		 * {@link #isStale(Object)}.
		 */
		@Override
		protected void refresh(IViewerNotification notification) {
			Object element = notification.getElement();

			if (targetViewer instanceof StructuredViewer) {
				StructuredViewer structuredViewer = (StructuredViewer)targetViewer;

				ISelection selection = structuredViewer.getSelection();
				boolean isStaleSelection = isStale(selection);
				if (isStaleSelection) {
					targetViewer.setSelection(StructuredSelection.EMPTY);
				}

				AbstractTreeViewer treeViewer = structuredViewer instanceof AbstractTreeViewer
						? (AbstractTreeViewer)structuredViewer
						: null;
				List<Object> expandedElements = treeViewer == null ? Collections.emptyList()
						: Arrays.asList(treeViewer.getExpandedElements());
				boolean isStaleExpandedElements = isStale(expandedElements);

				if (element != null) {
					if (notification.isContentRefresh()) {
						structuredViewer.refresh(element, notification.isLabelUpdate());
					} else if (notification.isLabelUpdate()) {
						structuredViewer.update(element, null);
					}
				} else {
					structuredViewer.refresh(notification.isLabelUpdate());
				}

				if (isStaleSelection || isStaleExpandedElements) {
					Object object = structuredViewer.getInput();
					EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(object);
					if (editingDomain == null) {
						for (Object child : ((IStructuredContentProvider)structuredViewer
								.getContentProvider()).getElements(object)) {
							editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(child);
							if (editingDomain != null) {
								break;
							}
						}
					}
					if (editingDomain instanceof AdapterFactoryEditingDomain) {
						AdapterFactoryEditingDomain adapterFactoryEditingDomain = (AdapterFactoryEditingDomain)editingDomain;
						if (treeViewer != null && isStaleExpandedElements) {
							treeViewer.setExpandedElements(
									adapterFactoryEditingDomain.resolve(expandedElements).toArray());
						}
						if (isStaleSelection) {
							structuredViewer
									.setSelection(
											new StructuredSelection(adapterFactoryEditingDomain
													.resolve(((IStructuredSelection)selection).toList())),
											true);
						}
					}
				}
			} else {
				targetViewer.refresh();
			}
		}

		/**
		 * Copy/paste of {@link AdapterFactoryEditingDomain#isStale(Object)} but prevents exceptions from
		 * being thrown.
		 */
		private boolean isStale(Object object) {
			if (object instanceof IWrapperItemProvider) {
				IWrapperItemProvider wrapper = (IWrapperItemProvider)object;
				return isStale(wrapper.getValue()) || isStale(wrapper.getOwner());
			} else if (object instanceof Collection<?>) {
				for (Object item : (Collection<?>)object) {
					if (isStale(item)) {
						return true;
					}
				}
				return false;
			} else if (object instanceof Object[]) {
				for (Object item : (Object[])object) {
					if (isStale(item)) {
						return true;
					}
				}
				return false;
			} else if (object instanceof EObject) {
				EObject eObject = (EObject)object;
				return eObject.eIsProxy() && eObject.eAdapters().isEmpty();
			} else if (object instanceof FeatureMap.Entry) {
				return isStale(((FeatureMap.Entry)object).getValue());
			} else if (object == null) {
				return false;
			} else if (object instanceof IStructuredSelection) {
				return isStale(((IStructuredSelection)object).toArray());
			} else {
				return false;
			}
		}

		@Override
		public synchronized boolean addNotification(IViewerNotification notification) {
			if (super.addNotification(notification)) {
				count = 0;
				return true;
			}

			// When there are more than 30 notifications, it's probably cheaper to simply
			// refresh the overall view.
			if (count > 30) {
				super.addNotification(new ViewerNotification(notification, null, true, true));
			}

			++count;
			return false;
		}
	}

}
