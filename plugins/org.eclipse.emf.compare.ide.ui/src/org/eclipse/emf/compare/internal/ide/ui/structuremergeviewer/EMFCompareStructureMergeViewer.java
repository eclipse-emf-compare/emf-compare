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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.internal.CompareMessages;
import org.eclipse.compare.internal.CompareUIPlugin;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
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
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareStructureMergeViewer extends DiffTreeViewer {

	/**
	 * 
	 */
	private static final String EMF_COMPARE_RESULT = "EMF.COMPARE.RESULT";

	private final ICompareInputChangeListener fCompareInputChangeListener;

	private final ComposedAdapterFactory fAdapterFactory;

	private IContentChangeListener fContentChangedListener;

	private final SideInputInfo fAncestorStructure = new SideInputInfo();

	private final SideInputInfo fLeftStructure = new SideInputInfo();

	private final SideInputInfo fRightStructure = new SideInputInfo();

	private final CompareViewerSwitchingPane fParent;

	private Comparison fRoot;

	/**
	 * @param parent
	 */
	public EMFCompareStructureMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
		if (parent instanceof CompareViewerSwitchingPane) {
			fParent = (CompareViewerSwitchingPane)parent;
		} else {
			fParent = null;
		}

		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

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

		AdapterFactoryContentProvider adapterFactoryContentProvider = new StructureMergeViewerContentProvider(
				fAdapterFactory);
		setContentProvider(adapterFactoryContentProvider);

		AdapterFactoryLabelProvider adapterFactoryLabelProvider = new StructureMergeViewerLabelProvider(
				fAdapterFactory, getCompareConfiguration());
		setLabelProvider(adapterFactoryLabelProvider);
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

	@Override
	protected Object getRoot() {
		return fRoot;
	}

	protected void contentChanged(final IContentChangeNotifier changed) {
		// refresh diff
	}

	private IRunnableWithProgress inputChangedTask = new IRunnableWithProgress() {
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask(CompareMessages.StructureDiffViewer_1, 100);
			// TODO: Should we always force
			compareInputChanged((ICompareInput)getInput(), true, new SubProgressMonitor(monitor, 100));
			monitor.done();
		}
	};

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

		Object previousResult = getCompareConfiguration().getProperty(EMF_COMPARE_RESULT);
		if (previousResult instanceof Comparison) {
			fRoot = (Comparison)previousResult;

			getCompareConfiguration().getContainer().runAsynchronously(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					if (Display.getCurrent() != null) {
						refreshAfterDiff("", fRoot);
					} else {
						final String theMessage = "";
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

				fRoot = EMFCompare.compare(leftResourceSet, rightResourceSet, ancestorResourceSet);
				getCompareConfiguration().setProperty(EMF_COMPARE_RESULT, fRoot);

				getCompareConfiguration().getContainer().runAsynchronously(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						if (Display.getCurrent() != null) {
							refreshAfterDiff("", fRoot);
						} else {
							final String theMessage = "";
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

	private void refreshAfterDiff(String message, Comparison root) {
		if (getControl().isDisposed()) {
			return;
		}
		if (fParent != null) {
			fParent.setTitleArgument(message);
		}

		refresh(root);
		// Setting the auto-expand level doesn't do anything for refreshes
		// expandToLevel(3);
	}

	private class SideInputInfo {
		private ITypedElement fInput;

		public boolean setInput(ITypedElement newInput, boolean force) {
			boolean changed = false;
			if (force || newInput != fInput) {
				changed = (newInput != null);
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
	 * @{inheritDoc
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

	@Override
	protected void initialSelection() {
		// expandToLevel(2);
	}

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
		ResourceSet ancestorResourceSet = null;
		if (typedElement instanceof IResourceProvider) {
			IResource ancestorResource = ((IResourceProvider)typedElement).getResource();
			ancestorResourceSet = getResourceSet(ancestorResource, monitor);
		}
		return ancestorResourceSet;
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
}
