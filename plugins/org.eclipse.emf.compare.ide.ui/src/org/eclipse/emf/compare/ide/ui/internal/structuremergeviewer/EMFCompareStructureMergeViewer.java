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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.internal.CompareUIPlugin;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.actions.filter.DifferenceFilter;
import org.eclipse.emf.compare.ide.ui.internal.actions.filter.FilterActionMenu;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGrouper;
import org.eclipse.emf.compare.ide.ui.internal.actions.group.GroupActionMenu;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.CompareConfigurationExtension;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareStructureMergeViewer extends DiffTreeViewer {

	private final ICompareInputChangeListener fCompareInputChangeListener;

	private final ComposedAdapterFactory fAdapterFactory;

	private IContentChangeListener fContentChangedListener;

	private final SideInputInfo fAncestorStructure = new SideInputInfo();

	private final SideInputInfo fLeftStructure = new SideInputInfo();

	private final SideInputInfo fRightStructure = new SideInputInfo();

	private final CompareViewerSwitchingPane fParent;

	private Object fRoot;

	/**
	 * The difference filter that will be applied to the structure viewer. Note that this will be initialized
	 * from {@link #createToolItems(ToolBarManager)} since that method is called from the super-constructor
	 * and we cannot init ourselves beforehand.
	 */
	private DifferenceFilter differenceFilter;

	/**
	 * This will be used by our adapter factory in order to group together the differences located under the
	 * Comparison. Note that this will be initialized from {@link #createToolItems(ToolBarManager)} since that
	 * method is called from the super-constructor and we cannot init ourselves beforehand.
	 */
	private DifferenceGrouper differenceGrouper;

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareStructureMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);

		differenceFilter.install(this);
		differenceGrouper.install(this);

		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		boolean leftIsLocal = CompareConfigurationExtension.getBoolean(configuration, "LEFT_IS_LOCAL", false);
		setLabelProvider(new EMFCompareStructureMergeViewerLabelProvider(fAdapterFactory, leftIsLocal));
		setContentProvider(new EMFCompareStructureMergeViewerContentProvider(fAdapterFactory,
				differenceGrouper));

		if (parent instanceof CompareViewerSwitchingPane) {
			fParent = (CompareViewerSwitchingPane)parent;
		} else {
			fParent = null;
		}

		fContentChangedListener = new IContentChangeListener() {
			public void contentChanged(IContentChangeNotifier changed) {
				EMFCompareStructureMergeViewer.this.contentChanged(changed);
			}
		};

		fCompareInputChangeListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput input) {
				EMFCompareStructureMergeViewer.this.compareInputChanged(input, true);
			}
		};

		// Wrap the defined comparer in our own.
		setComparer(new DiffNodeComparer(super.getComparer()));
	}

	void compareInputChanged(ICompareInput input, boolean force) {
		if (input == null) {
			// When closing, we don't need a progress monitor to handle the input change
			compareInputChanged(input, force, null);
			return;
		}
		CompareConfiguration cc = getCompareConfiguration();
		// The compare configuration is nulled when the viewer is disposed
		if (cc != null) {
			BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
				public void run() {
					try {
						inputChangedTask.run(new NullProgressMonitor());
					} catch (InvocationTargetException e) {
						CompareUIPlugin.log(e.getTargetException());
					} catch (InterruptedException e) {
						// Ignore
					}
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRoot()
	 */
	@Override
	protected Object getRoot() {
		return fRoot;
	}

	protected void contentChanged(final IContentChangeNotifier changed) {
		// refresh diff
	}

	private IRunnableWithProgress inputChangedTask = new IRunnableWithProgress() {
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Computing Structure Differences", 100);
			// TODO: Should we always force
			compareInputChanged((ICompareInput)getInput(), true, new SubProgressMonitor(monitor, 100));
			monitor.done();
		}
	};

	private ToolBarManager fToolbarManager;

	void compareInputChanged(ICompareInput input, boolean force, IProgressMonitor monitor) {
		ITypedElement t = null;

		if (input != null) {
			t = input.getAncestor();
		}
		fAncestorStructure.setInput(t, force);

		if (input != null) {
			t = input.getLeft();
		}
		fLeftStructure.setInput(t, force);

		if (input != null) {
			t = input.getRight();
		}
		fRightStructure.setInput(t, force);

		Object previousResult = getCompareConfiguration().getProperty(EMFCompareConstants.COMPARE_RESULT);
		if (previousResult instanceof Comparison) {
			fRoot = (Comparison)previousResult;

			getCompareConfiguration().getContainer().runAsynchronously(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					if (Display.getCurrent() != null) {
						refreshAfterDiff("my message", fRoot);
					} else {
						final String theMessage = "my message";
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								refreshAfterDiff(theMessage, fRoot);
							}
						});
					}
				}
			});
		} else {
			if (input != null) {
				ResourceSet leftResourceSet = getResourceSetFrom(input.getLeft(), monitor);
				ResourceSet rightResourceSet = getResourceSetFrom(input.getRight(), monitor);
				ResourceSet ancestorResourceSet = getResourceSetFrom(input.getAncestor(), monitor);

				// TODO: run with a progress monitor.
				EMFCompareConfiguration emfCompareConfiguration = EMFCompareConfiguration.builder().build();
				Comparison compareResult = EMFCompare.compare(leftResourceSet, rightResourceSet,
						ancestorResourceSet, emfCompareConfiguration);
				compareResult.eAdapters().add(new EContentAdapter() {
					/**
					 * {@inheritDoc}
					 * 
					 * @see org.eclipse.emf.ecore.util.EContentAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
					 */
					@Override
					public void notifyChanged(Notification notification) {
						int eventType = notification.getEventType();
						Object feature = notification.getFeature();
						Object oldValue = notification.getOldValue();
						Object newValue = notification.getNewValue();
						if (eventType == Notification.SET
								&& feature == ComparePackage.Literals.DIFF__STATE
								&& oldValue == DifferenceState.UNRESOLVED
								&& (newValue == DifferenceState.MERGED || newValue == DifferenceState.DISCARDED)) {
							refresh();
						}
					}
				});
				fRoot = fAdapterFactory.adapt(compareResult, IDiffElement.class);
				getCompareConfiguration().setProperty(EMFCompareConstants.COMPARE_RESULT, fRoot);

				getCompareConfiguration().getContainer().runAsynchronously(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						if (Display.getCurrent() != null) {
							refreshAfterDiff("my message", fRoot);
						} else {
							final String theMessage = "my message";
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									refreshAfterDiff(theMessage, fRoot);
								}
							});
						}
					}
				});
			}
		}
	}

	private void refreshAfterDiff(String message, Object root) {
		if (getControl().isDisposed()) {
			return;
		}

		if (fParent != null) {
			fParent.setTitleArgument(message);
		}

		refresh(root);
	}

	private class SideInputInfo {
		private ITypedElement fInput;

		public boolean setInput(ITypedElement newInput, boolean force) {
			boolean changed = false;
			if (force || newInput != fInput) {
				changed = newInput != null;
				if (fInput instanceof IContentChangeNotifier && fContentChangedListener != null) {
					((IContentChangeNotifier)fInput).removeContentChangeListener(fContentChangedListener);
				}
				fInput = newInput;
				if (fInput instanceof IContentChangeNotifier && fContentChangedListener != null) {
					((IContentChangeNotifier)fInput).addContentChangeListener(fContentChangedListener);
				}
			}
			return changed;
		}
	}

	/**
	 * Recreates the comparable structures for the input sides.
	 * 
	 * @param input
	 *            this viewer's new input
	 */
	protected void compareInputChanged(ICompareInput input) {
		compareInputChanged(input, false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setComparer(org.eclipse.jface.viewers.IElementComparer)
	 */
	@Override
	public void setComparer(IElementComparer comparer) {
		// Wrap this new comparer in our own
		super.setComparer(new DiffNodeComparer(comparer));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolbarManager) {
		fToolbarManager = toolbarManager;
		super.createToolItems(toolbarManager);

		// Initialized here since this is called from the super-constructor
		if (differenceFilter == null) {
			differenceFilter = new DifferenceFilter();
		}
		if (differenceGrouper == null) {
			differenceGrouper = new DifferenceGrouper();
		}

		fToolbarManager.add(new GroupActionMenu(differenceGrouper));
		fToolbarManager.add(new FilterActionMenu(differenceFilter));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#inputChanged(java.lang.Object,
	 *      java.lang.Object)
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
			compareInputChanged(ci);
			if (input != oldInput) {
				initialSelection();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		Object input = getInput();
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.removeCompareInputChangeListener(fCompareInputChangeListener);
		}
		compareInputChanged(null);
		fContentChangedListener = null;
		super.handleDispose(event);
	}

	private static ResourceSet getResourceSetFrom(ITypedElement typedElement, IProgressMonitor monitor) {
		ResourceSet resourceSet = null;
		if (typedElement instanceof IResourceProvider) {
			IResource resource = ((IResourceProvider)typedElement).getResource();
			resourceSet = getResourceSet(resource, monitor);
		}
		return resourceSet;
	}

	private static ResourceSet getResourceSet(IResource resource, IProgressMonitor monitor) {
		if (resource == null) {
			return null;
		}

		IModelProviderDescriptor[] descriptors = ModelProvider.getModelProviderDescriptors();
		for (int i = 0; i < descriptors.length; i++) {
			IModelProviderDescriptor descriptor = descriptors[i];
			try {
				IResource[] resources = descriptor.getMatchingResources(new IResource[] {resource });
				if (resources.length > 0) {
					ModelProvider modelProvider = descriptor.getModelProvider();
					// TODO MBA: see if a context is necessary
					ResourceMapping[] mappings = modelProvider.getMappings(resource, null, monitor);
					// FIXME: no need for returned value?
					modelProvider.getTraversals(mappings, null, monitor);
					for (ResourceMapping resourceMapping : mappings) {
						if (resourceMapping.getModelObject() instanceof Resource) {
							return ((Resource)resourceMapping.getModelObject()).getResourceSet();
						}
					}
				}
			} catch (CoreException e) {
				EMFCompareIDEUIPlugin.getDefault().getLog().log(
						new Status(IStatus.ERROR, "", e.getMessage(), e));
			}
		}
		return null;
	}

	/**
	 * We'll use this in order to compare our diff nodes through their target's {@link Object#equals(Object)}
	 * instead of the nodes' own equals (which only resorts to instance equality).
	 * <p>
	 * Note that this will fall back to the default behavior for anything that is not an
	 * {@link AbstractEDiffElement}.
	 * </p>
	 * <p>
	 * This class most likely breaks the implicit contract of equals() since we are comparing
	 * AbstractEDiffElement through two different means : if we have a target, use it... otherwise fall back
	 * to instance equality. Both equals() and hashCode() follow this same rule.
	 * </p>
	 */
	private class DiffNodeComparer implements IElementComparer {
		/** Our delegate comparer. May be {@code null}. */
		private IElementComparer delegate;

		/**
		 * Constructs this comparer given the previous one that was installed on this viewer.
		 * 
		 * @param delegate
		 *            The comparer to which we should delegate our default behavior. May be {@code null}.
		 */
		public DiffNodeComparer(IElementComparer delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.IElementComparer#equals(java.lang.Object, java.lang.Object)
		 */
		public boolean equals(Object a, Object b) {
			final boolean equal;
			if (a instanceof AbstractEDiffElement && b instanceof AbstractEDiffElement) {
				final Notifier targetA = ((AbstractEDiffElement)a).getTarget();
				if (targetA == null) {
					// Fall back to default behavior
					equal = a.equals(b);
				} else {
					equal = targetA.equals(((AbstractEDiffElement)b).getTarget());
				}
			} else if (delegate != null) {
				equal = delegate.equals(a, b);
			} else if (a != null) {
				equal = a.equals(b);
			} else {
				equal = a == b;
			}
			return equal;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.IElementComparer#hashCode(java.lang.Object)
		 */
		public int hashCode(Object element) {
			final int hashCode;
			if (element instanceof AbstractEDiffElement) {
				final Notifier target = ((AbstractEDiffElement)element).getTarget();
				if (target == null) {
					// Fall back to default behavior
					hashCode = element.hashCode();
				} else {
					hashCode = target.hashCode();
				}
			} else if (delegate != null) {
				hashCode = delegate.hashCode(element);
			} else if (element != null) {
				hashCode = element.hashCode();
			} else {
				hashCode = 0;
			}
			return hashCode;
		}
	}
}
