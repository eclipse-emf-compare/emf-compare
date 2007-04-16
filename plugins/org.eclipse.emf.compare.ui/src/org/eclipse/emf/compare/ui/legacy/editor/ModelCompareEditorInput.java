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
package org.eclipse.emf.compare.ui.legacy.editor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.IPropertyChangeNotifier;
import org.eclipse.compare.Splitter;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.legacy.DiffUtils;
import org.eclipse.emf.compare.ui.legacy.ModelCompareInput;
import org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ModelCompareContentProvider;
import org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.legacy.image.ImageUtils;
import org.eclipse.emf.compare.ui.legacy.org.eclipse.compare.CompareEditorInput;
import org.eclipse.emf.compare.ui.legacy.structuremergeviewer.ModelStructureMergeViewer;
import org.eclipse.emf.compare.ui.legacy.wizard.SaveDeltaWizard;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelCompareEditorInput extends CompareEditorInput {

	protected ModelContentMergeViewer umlContentViewer;

	protected ModelStructureMergeViewer umlDiffViewer;

	protected boolean merge;

	protected IFile leftModelFile;

	protected DiffModel diff;

	protected Object rightModelFile;

	protected Object ancestorModelFile;

	protected Object fInput;

	protected ModelCompareEditorInput(final CompareConfiguration cc) {
		super(cc);

		this.inputListener = new ICompareInputChangeListener() {

			public void compareInputChanged(final ICompareInput source) {
				ModelCompareEditorInput.this.umlDiffViewer.setInput(source);
				ModelCompareEditorInput.this.umlContentViewer.setInput(source);
			}
		};
	}

	/**
	 * @param configuration
	 * @param b
	 * @param object
	 * @param stream
	 */
	public ModelCompareEditorInput(final CompareConfiguration configuration,
			final IFile modelFile, final Object comparedModelFile,
			final Object ancestorModelFile, final boolean merge) {
		this(configuration);
		DiffUtils.initializeCompareConfiguration(configuration, modelFile,
				comparedModelFile, ancestorModelFile);
		configuration.setLeftEditable(true);
		configuration.setRightEditable(true);
		this.leftModelFile = modelFile;

		this.rightModelFile = comparedModelFile;

		this.ancestorModelFile = ancestorModelFile;

		this.merge = merge;

	}

	private InterruptedException prepareInputInterruptedException;

	private InvocationTargetException prepareInputInvocationTargetException;

	private ActionContributionItem noDiffFilterItem;

	private ActionContributionItem pseudoConflictFilterItem;

	protected ICompareInputChangeListener inputListener;

	// private EObject load(final InputStream in, final ResourceSet resourceSet)
	// {
	//
	// final Resource resource = resourceSet.createResource(URI
	// .createURI("left"));
	// // XMIResourceImpl resource = new XMIResourceImpl();
	// try {
	// resource.load(in, Collections.EMPTY_MAP);
	// } catch (final IOException e) {
	// EMFComparePlugin.getDefault().log(e, false);
	// }
	//
	// final EObject result = (EObject) ((resource.getContents().size() > 0) ?
	// resource
	// .getContents().get(0)
	// : null);
	// return result;
	// }

	public EObject load(final IFile file, final ResourceSet resourceSet) {
		EObject result = null;
		final URI modelURI = URI.createURI(file.getFullPath().toString());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		final Resource modelResource = resourceSet.getResource(modelURI, true);
		result = (EObject) ((modelResource.getContents().size() > 0) ? modelResource
				.getContents().get(0)
				: null);
		return result;
	}

	/**
	 * @see org.eclipse.compare.CompareEditorInput#prepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(final IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
		prepareEngine(monitor);

		final ResourceSet resourceSet = new ResourceSetImpl();

		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI,
				UMLPackage.eINSTANCE);
		final Map extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		EObject leftModel;
		
			leftModel = load(this.leftModelFile, resourceSet);
			final EObject rightModel = load(((IFile) this.rightModelFile)
					, resourceSet);
			final MatchModel match = new MatchService().doMatch(leftModel,
					rightModel, monitor);
			diff = new DiffService().doDiff(match);
			final ModelCompareInput input = new ModelCompareInput(match, diff);
			input.addCompareInputChangeListener(this.inputListener);
			input.setLeftStorage(this.leftModelFile);
			input.setRightStorage(this.rightModelFile);
			return checkInputHasDiffs() ? input : null;

	}

	private void save(final EObject root, final String path) throws IOException {
		final URI modelURI = URI.createURI(path);
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		final Resource newModelResource = resourceSet.createResource(modelURI);
		newModelResource.getContents().add(root);
		final Map options = new HashMap();
		options.put(XMLResource.OPTION_ENCODING, System
				.getProperty("file.encoding"));
		newModelResource.save(options);

	}

	protected void prepareEngine(final IProgressMonitor monitor)
			throws InterruptedException, InvocationTargetException {
		this.prepareInputInterruptedException = null;
		this.prepareInputInvocationTargetException = null;

		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			public void run() {
				try {
					final InputStream ancestorModelStream = getInputContents(
							ModelCompareEditorInput.this.ancestorModelFile,
							monitor);

					final InputStream comparedStream = getInputContents(
							ModelCompareEditorInput.this.rightModelFile,
							monitor);

					if ((ancestorModelStream == null)
							&& (ModelCompareEditorInput.this.ancestorModelFile != null)) {
						throw new InterruptedException(
								"Ancestor element does not exist");
					}
					if (comparedStream == null) {
						throw new InterruptedException(
								"Compared element does not exist");
					}

					// UMLComparisonEngine engine = new
					// UMLComparisonEngine(modelFile.getContents(),
					// comparedStream, ancestorModelStream, merge);
					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell());

					// dialog.run(true, true, engine);
					// setEngine(engine);

				} catch (final CoreException e) {
					EMFComparePlugin.getDefault().log(e, false);
				} catch (final InterruptedException e) {
					ModelCompareEditorInput.this.prepareInputInterruptedException = e;
				}
			}

		});

		if (this.prepareInputInterruptedException != null) {
			EMFComparePlugin.getDefault().log(
					this.prepareInputInterruptedException, false);
			if (this.prepareInputInterruptedException.getMessage() != null) {
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

					public void run() {
						EMFComparePlugin
								.getDefault()
								.log(
										ModelCompareEditorInput.this.prepareInputInterruptedException
												.getMessage(), false);
					}
				});
			}
			throw this.prepareInputInterruptedException;
		}
		if (this.prepareInputInvocationTargetException != null) {
			EMFComparePlugin.getDefault().log(
					this.prepareInputInterruptedException, false);
			throw this.prepareInputInvocationTargetException;
		}

		// if (engine.getCircularReferenceErrors().size() != 0)
		// {
		// for (CircularReferenceError error :
		// engine.getCircularReferenceErrors())
		// {
		// //
		// LoggerFactory.getLogger(UMLCompareEditorInput.class).debug(error.getElt()
		// + " " + error.getSecondElt());
		// }
		// PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
		//
		// public void run()
		// {
		// MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Circular
		// reference detected",engine.getCircularReferenceErrors().toString());
		// }});
		//
		// throw new InterruptedException();
		// }

	}

	protected boolean checkInputHasDiffs() {
		// boolean result = false;
		// TreeIterator treeIter = engine.getDelta().treeIterator();
		// while (treeIter.hasNext())
		// {
		// if (((Match2Elements) treeIter.next()).getKind() !=
		// DiffConstants.NO_CHANGE)
		// return true;
		// }
		// return result;
		// FIXMECBR : check input has diff
		return true;
	}

	protected void navigateToDelta(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).getFirstElement() instanceof Match2Elements) {
				final Match2Elements delta = (Match2Elements) ((IStructuredSelection) selection)
						.getFirstElement();
				this.umlContentViewer.navigateToDelta(delta);
			}
			if (((IStructuredSelection) selection).getFirstElement() instanceof DiffElement) {
				final DiffElement diff = (DiffElement) ((IStructuredSelection) selection)
						.getFirstElement();
				this.umlContentViewer.navigateToDiff(diff);
			}
		}

	}

	protected void navigateFromModelsTree(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).getFirstElement() instanceof Match2Elements) {
				final Match2Elements delta = (Match2Elements) ((IStructuredSelection) selection)
						.getFirstElement();
				this.umlContentViewer.navigateToDelta(delta);
				this.umlDiffViewer.navigateToDelta(delta);
			}
			if (((IStructuredSelection) selection).getFirstElement() instanceof DiffElement) {
				final DiffElement diff = (DiffElement) ((IStructuredSelection) selection)
						.getFirstElement();
				this.umlContentViewer.navigateToDiff(diff);
				this.umlDiffViewer.navigateToDiff(diff);
			}
			if (((IStructuredSelection) selection).getFirstElement() instanceof EAttribute) {
				final EAttribute att = (EAttribute) ((IStructuredSelection) selection)
						.getFirstElement();
				this.umlContentViewer.navigateToProperty(att);
			}
		}
	}

	/**
	 * @see org.eclipse.compare.AbstractCompareEditorInput#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(final Composite parent) {
		final Splitter fComposite = new Splitter(parent, SWT.VERTICAL);

		createOutlineContents(fComposite, SWT.HORIZONTAL);

		final CompareViewerPane pane = new CompareViewerPane(fComposite,
				SWT.NONE);

		this.umlContentViewer = new ModelContentMergeViewer(pane,
				getCompareConfiguration());
		this.umlContentViewer
				.setContentProvider(new ModelCompareContentProvider(
						getCompareConfiguration()));
		pane.setContent(this.umlContentViewer.getControl());
		this.umlContentViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(
							final SelectionChangedEvent event) {
						navigateFromModelsTree(event.getSelection());

					}
				});

		final IPropertyChangeNotifier dsp = this.umlContentViewer;
		dsp.addPropertyChangeListener(this.fDirtyStateListener);

		final Control c = this.umlContentViewer.getControl();
		c.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				dsp
						.removePropertyChangeListener(ModelCompareEditorInput.this.fDirtyStateListener);
			}
		});

		fComposite.setWeights(new int[] { 30, 70 });

		this.umlContentViewer.setInput(this.fInput); // TODOCBR Content
		// provider check

		final ToolBarManager manager = CompareViewerPane
				.getToolBarManager(this.umlDiffViewer.getTree().getParent());
		if (manager != null) {
			manager.removeAll();

			// define groups
			manager.add(new Separator("filter")); //$NON-NLS-1$
			// add actions
			final Action a = new Action() {
				public void run() {
					final SaveDeltaWizard wizard = new SaveDeltaWizard();
					IStructuredSelection selection = new StructuredSelection(
							leftModelFile);
					wizard.init(PlatformUI.getWorkbench(), selection,
							ModelCompareEditorInput.this);
					final WizardDialog dialog = new WizardDialog(PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getShell(), wizard);
					dialog.open();
				}
			};
			a.setImageDescriptor(ImageUtils.SAVE_DIFF.getImageDescriptor());
			a.setToolTipText("Save diff model...");
			// a.setChecked(false);
			this.noDiffFilterItem = new ActionContributionItem(a);
			manager.appendToGroup("filter", this.noDiffFilterItem); //$NON-NLS-1$

			// add actions
			/*
			 * final Action b = new Action() { public void run() {
			 * ModelCompareEditorInput.this.umlDiffViewer.setPseudoConflictFilterEnabled(!this
			 * .isChecked());
			 * ModelCompareEditorInput.this.umlContentViewer.setShowPseudoConflicts(this.isChecked()); } };
			 * b.setImageDescriptor(ImageUtils.SHOW_PSEUDO_CONFLICTS
			 * .getImageDescriptor()); b.setToolTipText("Show
			 * pseudo-conflicts"); b.setChecked(false);
			 * this.pseudoConflictFilterItem = new ActionContributionItem(b);
			 * manager.appendToGroup("filter", this.pseudoConflictFilterItem);
			 */
			manager.update(true);
		}
		return fComposite;
	}

	public Control createOutlineContents(final Composite parent,
			final int direction) {
		final Splitter h = new Splitter(parent, direction);

		final CompareViewerPane pane = new CompareViewerPane(h, SWT.NONE);

		this.umlDiffViewer = new ModelStructureMergeViewer(pane,
				getCompareConfiguration());
		pane.setContent(this.umlDiffViewer.getTree());
		this.umlDiffViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(
							final SelectionChangedEvent event) {
						navigateToDelta(event.getSelection());
					}
				});
		this.umlDiffViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(final DoubleClickEvent event) {
				ModelCompareEditorInput.this.umlContentViewer
						.setSelectedPage(ModelContentMergeViewer.PROPERTIES_TAB);
				navigateToDelta(event.getSelection());
			}
		});

		final List factories = new ArrayList();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new DiffItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				factories);
		this.umlDiffViewer
				.setContentProvider(new AdapterFactoryContentProvider(
						adapterFactory));
		this.umlDiffViewer.setInput(getCompareResult());

		return h;
	}

	/**
	 * @see org.eclipse.compare.AbstractCompareEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageUtils.EDITOR_ICON.getImageDescriptor();
	}

	/**
	 * @see org.eclipse.compare.AbstractCompareEditorInput#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the compare result computed by the most recent call to the
	 * <code>run</code> method. Returns <code>null</code> if no differences
	 * were found.
	 * 
	 * @return the compare result prepared in method <code>prepareInput</code>
	 *         or <code>null</code> if there were no differences
	 */
	public Object getCompareResult() {
		return ((ModelCompareInput) this.fInput).getDiff();
	}

	/**
	 * Runs the compare operation and stores the compare result.
	 * 
	 * @param monitor
	 *            the progress monitor to use to display progress and receive
	 *            requests for cancelation
	 * @exception InvocationTargetException
	 *                if the <code>prepareInput</code> method must propagate a
	 *                checked exception, it should wrap it inside an
	 *                <code>InvocationTargetException</code>; runtime
	 *                exceptions are automatically wrapped in an
	 *                <code>InvocationTargetException</code> by the calling
	 *                context
	 * @exception InterruptedException
	 *                if the operation detects a request to cancel, using
	 *                <code>IProgressMonitor.isCanceled()</code>, it should
	 *                exit by throwing <code>InterruptedException</code>
	 */
	public void run(final IProgressMonitor monitor)
			throws InterruptedException, InvocationTargetException {
		this.fInput = prepareInput(monitor);
	}

	private InputStream getInputContents(final Object file,
			final IProgressMonitor monitor) throws CoreException,
			InterruptedException {
		if (file == null) {
			return null;
		}
		if (file instanceof IFile) {
			return ((IFile) file).getContents(true);
		}
		if (file instanceof IFileRevision) {
			return ((IFileRevision) file).getStorage(monitor).getContents();
		}
		if (file instanceof IFileState) {
			return ((IFileState) file).getContents();
		}
		if (file instanceof IResourceVariant) {
			try {
				return ((IResourceVariant) file).getStorage(monitor)
						.getContents();
			} catch (final CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
				throw new InterruptedException(
						"Error while trying to cache remote files contents");
			}
		}

		throw new IllegalStateException("unknown type");
	}

	public void saveChanges(final IProgressMonitor pm) throws CoreException {

		this.umlContentViewer.save(pm);
	}

	public DiffModel getDiff() {
		return diff;
	}
}
