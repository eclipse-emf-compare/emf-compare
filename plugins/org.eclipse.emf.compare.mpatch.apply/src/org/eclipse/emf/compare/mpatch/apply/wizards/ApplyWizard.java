/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.ApplyActivator;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchApplication;
import org.eclipse.emf.compare.mpatch.extension.MPatchApplicationResult;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.provider.MPatchItemProviderAdapterFactory;
import org.eclipse.emf.compare.ui.editor.ModelCompareEditorInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;

/**
 * A wizard which takes an MPatch (instance of {@link MPatchModel}) and an emf model as input, resolves all
 * symbolic references of the diff, and creates a new emfdiff (instance of {@link ComparisonSnapshot}) and a
 * new model such that the emfdiff can be used to transfer the mpatch to the target model.<br>
 * <br>
 * <i>Example:</i>
 * <ol>
 * <li>A file <code>diff.mpatch</code> contains an mpatch for models of type 'mymodel'.
 * <li>An example model <code>test.mymodel</code> contains an instance of that type of models.
 * <li>The wizard takes both files as input and tries to resolve all symbolic references from the
 * <code>diff.mpatch</code> in <code>test.mymodel</code>.
 * <li>Then it asks the user to store the results in an EMF Compare based diff, e.g. <code>diff.emfdiff</code>.
 * <li>Afterwards it asks the user to store the resulting model in a new file, e.g. <code>test2.mymodel</code>.
 * <li>In the end, it opens the EMF Compare GUI to revise the application of the diff.
 * </ol>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ApplyWizard extends Wizard implements INewWizard {

	private static final String WIZARD_ICON = "icons/mpatch.gif";

	/**
	 * Defines whether the intermediate emfdiff and the temporary new model should be saved. If set to
	 * <code>false</code>, then these artifacts are only handled in-memory.
	 */
	boolean saveIntermediateFiles;

	/**
	 * Defines whether the binding between the differences and the model, to which the differences were
	 * applied, should be saved into a file.
	 */
	boolean saveBinding;

	/** Singleton diff applier. */
	protected IMPatchApplication mPatchApplication;

	/** We need that for opening the EMF Compare GUI in the end. */
	protected IWorkbench workbench;

	/** The initial selected MPatch when the wizard was launched (might be <code>null</code>). */
	protected IFile mPatchFile;

	/** The initial selected model when the wizard was launched (might be <code>null</code>). */
	protected URI modelURI;

	/** The result of the page asking the user for the diff. */
	protected MPatchModel mpatch;

	/** The result of the page asking the user for the model to which the diff should be applied. */
	protected Resource modelResource;

	/** The model element in modelResource to which the diff should be applied. */
	private EObject modelTarget;

	/** The result of the page performing the symbolic reference resolution. */
	protected ResolvedSymbolicReferences resolvedElements;

	/** The result of the page asking the user for the file to which the emfdiff should be saved. */
	protected IFile emfdiffFile;

	/** The result of the page asking the user for the file to which the new model should be saved. */
	protected IFile newModelFile;

	/**
	 * The result of the page asking the user for the file for the binding between the differences and the
	 * model.
	 */
	private IFile bindingFile;

	private ApplyWizardResolvePage symbolicReferenceResolutionPage;

	private ApplyWizardNewModelPage storeModelPage;

	private ApplyWizardSummaryPage summaryPage;

	private ApplyWizardSelectMPatchPage selectMPatchPage;

	private ApplyWizardSelectModelPage selectModelPage;

	private ApplyWizardNewEmfdiffPage storeDiffPage;

	private ApplyWizardSaveBindingPage storeBindingPage;

	private ComposedAdapterFactory adapterFactory;

	/** @see {@link ApplyWizard} */
	public ApplyWizard() {
		// load a diff applier extension
		mPatchApplication = ExtensionManager.getSelectedApplication();
	}

	@Override
	public void addPages() {
		// get mpatch file from default selection
		IStructuredSelection selection = null;
		if (mPatchFile != null) {
			selection = new StructuredSelection(mPatchFile);
		}

		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new MPatchItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// now lets create the pages
		selectMPatchPage = new ApplyWizardSelectMPatchPage("Select " + MPatchConstants.MPATCH_LONG_NAME,
				mPatchFile, adapterFactory);
		selectModelPage = new ApplyWizardSelectModelPage("Select Model", modelURI, adapterFactory);
		symbolicReferenceResolutionPage = new ApplyWizardResolvePage("Resolve "
				+ MPatchConstants.SYMBOLIC_REFERENCES_NAME, adapterFactory);
		storeModelPage = new ApplyWizardNewModelPage("Store New Model", selection);
		storeDiffPage = new ApplyWizardNewEmfdiffPage("Store Emfdiff", selection);
		storeBindingPage = new ApplyWizardSaveBindingPage("Store Binding", selection);
		summaryPage = new ApplyWizardSummaryPage("Summary");
		addPage(selectMPatchPage);
		addPage(selectModelPage);
		addPage(symbolicReferenceResolutionPage);
		addPage(storeModelPage);
		addPage(storeDiffPage);
		addPage(storeBindingPage);
		addPage(summaryPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page.equals(symbolicReferenceResolutionPage)) {
			saveIntermediateFiles = symbolicReferenceResolutionPage.storeIntermediateModels();
			saveBinding = symbolicReferenceResolutionPage.storeBinding();
			if (saveIntermediateFiles) {
				return storeModelPage;
			} else if (saveBinding) {
				return storeBindingPage;
			} else {
				return summaryPage;
			}
		} else if (page.equals(storeDiffPage)) {
			if (saveBinding) {
				return storeBindingPage;
			} else {
				return summaryPage;
			}
		} else {
			return super.getNextPage(page);
		}
	}

	@Override
	public boolean canFinish() {
		for (IWizardPage page : getPages()) {
			if (!saveIntermediateFiles && (page.equals(storeModelPage) || page.equals(storeDiffPage))) {
				continue;
			}
			if (!saveBinding && page.equals(storeBindingPage)) {
				continue;
			}
			if (!page.isPageComplete()) {
				return false;
			}
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// check the initial selection for a valid input file
		for (Object obj : selection.toArray()) {
			if (obj instanceof IFile) {
				final IFile file = (IFile)obj;
				if (MPatchConstants.FILE_EXTENSION_MPATCH.equals(file.getFileExtension())
						&& mPatchFile == null) {
					mPatchFile = file;
				} else if (modelURI == null) {
					modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				}
			}
		}
		this.workbench = workbench;

		// set some wizard attributes
		setWindowTitle("Apply " + MPatchConstants.MPATCH_LONG_NAME);
		final URL imageURL = ApplyActivator.getDefault().getBundle().getEntry(ApplyWizard.WIZARD_ICON);
		setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(imageURL));

		// if a model is open, get its uri
		if (modelURI == null) {
			modelURI = CommonUtils.getCurrentEditorFileInputUri();
		}

		// maybe the MPatch is open?! then erase the default model uri again...
		if (modelURI != null && mPatchFile != null) {
			final URI uri = URI.createPlatformResourceURI(mPatchFile.getFullPath().toString(), true);
			if (modelURI.equals(uri)) {
				modelURI = null;
			}
		}
	}

	/**
	 * At last, new {@link Resource}s for the new model and the emfdiff are created and the respective objects
	 * are saved. If that was successful, the EMF Compare editor is opened.<br>
	 * <br>
	 * In case the files cannot be saved, an error message will be shown to the user.
	 */
	@Override
	public boolean performFinish() {
		final boolean reviewDiffApplication = summaryPage.reviewDiffApplication();
		final EObject model = modelResource.getContents().get(0);
		final boolean[] result = new boolean[] {true}; // easy way to store return value

		try {
			/*
			 * Use a blocking progress monitor!
			 */
			getContainer().run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {

					monitor.beginTask("Applying MPatch...", 10);
					monitor.worked(2); // PROGRESS MONITOR

					try {
						if (!saveIntermediateFiles && !reviewDiffApplication) {
							MPatchApplicationResult result = mPatchApplication.applyMPatch(resolvedElements,
									saveBinding);
							result.showDialog(getShell(), adapterFactory);
							modelResource.save(null);
						} else {

							final EObject copyModel = EcoreUtil.copy(model); // create a copy of the model

							// initialize resources (which are required for EMF Compare to work
							final Resource copyModelResource;
							final Resource emfdiffResource;
							if (saveIntermediateFiles) {
								copyModelResource = new XMIResourceImpl(URI.createPlatformResourceURI(
										newModelFile.getFullPath().toString(), true));
								emfdiffResource = new XMIResourceImpl(URI.createPlatformResourceURI(
										emfdiffFile.getFullPath().toString(), true));
							} else {
								copyModelResource = new ResourceImpl(modelResource.getURI());
								emfdiffResource = new ResourceImpl();
							}
							copyModelResource.getContents().add(copyModel); // new model must be contained in
																			// a resource
																			// beforehand
							monitor.worked(2); // PROGRESS MONITOR

							// apply differences!
							MPatchApplicationResult result = mPatchApplication.applyMPatch(resolvedElements,
									saveBinding);
							monitor.worked(2); // PROGRESS MONITOR
							result.showDialog(getShell(), adapterFactory);
							modelResource.save(null);
							final boolean useIds = false; // in case of id-based models, new ids were added!
							final ComparisonSnapshot emfdiff = CommonUtils.createEmfdiff(model, copyModel,
									useIds);

							// save resources, if necessary
							if (saveIntermediateFiles) {
								copyModelResource.save(null);
								emfdiffResource.getContents().add(emfdiff);
								emfdiffResource.save(null);
								if (reviewDiffApplication) {
									IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(),
											emfdiffFile);
								}
							} else if (reviewDiffApplication) {
								CompareUI.openCompareEditor(new ModelCompareEditorInput(emfdiff));
							}
							monitor.worked(2); // PROGRESS MONITOR
						}

						// if storeBinding is true, save the binding!
						if (saveBinding) {
							try {
								Resource bindingResource = new XMIResourceImpl(URI.createPlatformResourceURI(
										bindingFile.getFullPath().toString(), true));
								bindingResource.getContents().add(resolvedElements.getMPatchModelBinding());
								bindingResource.save(null);
							} catch (IOException e) {
								ApplyActivator.getDefault().logError("An error occurred saving the binding.",
										e);
								MessageDialog.openError(getShell(), "Could not save binding",
										"An error occurred saving the binding.\nPlease check error log for details.\n\n"
												+ e.getMessage());
							}
						}
						monitor.done(); // PROGRESS MONITOR

					} catch (final Exception e) {
						ApplyActivator.getDefault().logError(
								"An error occured while saving the selected files", e);
						MessageDialog.openError(getShell(), "An error occured",
								"An error occured while applying differences:\n" + e.getMessage());
						result[0] = false;
					}
				}
			});

		} catch (InvocationTargetException e) {
		} catch (InterruptedException e) {
		}

		return result[0];
	}

	// //////////////////// Some getters and setters for commonly used data in the wizard

	/** The result of the page asking the user for the mpatch. */
	MPatchModel getMPatch() {
		return mpatch;
	}

	/** The result of the page asking the user for the mpatch. */
	void setMPatch(MPatchModel mpatch) {
		this.mpatch = mpatch;
	}

	/** The result of the page asking the user for the model to which the diff should be applied. */
	Resource getModelResource() {
		return modelResource;
	}

	/** The result of the page asking the user for the model to which the diff should be applied. */
	void setModelResource(Resource modelResource) {
		this.modelResource = modelResource;
	}

	// /** A copy of the input model in <code>modelResource</code> which in the end contains all applied
	// differences. */
	// void setNewModel(EObject newModel) {
	// this.newModel = newModel;
	// }

	/** The result of the page performing the symbolic reference resolution. */
	ResolvedSymbolicReferences getResolvedElements() {
		return resolvedElements;
	}

	/** The result of the page performing the symbolic reference resolution. */
	void setResolvedElements(ResolvedSymbolicReferences resolvedElements) {
		this.resolvedElements = resolvedElements;
	}

	/** The result of the page asking the user for the file to which the emfdiff should be saved. */
	void setEmfdiff(IFile emfdiffFile) {
		this.emfdiffFile = emfdiffFile;
	}

	/** The result of the page asking the user for the file to which the emfdiff should be saved. */
	IFile getEmfdiffFile() {
		return emfdiffFile;
	}

	/** The result of the page asking the user for the file to which the new model should be saved. */
	void setNewModelFile(IFile newModelFile) {
		this.newModelFile = newModelFile;
	}

	/** The result of the page asking the user for the file to which the new model should be saved. */
	IFile getNewModelFile() {
		return newModelFile;
	}

	/** The result of the page asking the user for the file to which the binding should be saved. */
	void setBindingFile(IFile file) {
		this.bindingFile = file;
	}

	/** The result of the page asking the user for the file to which the binding should be saved. */
	IFile getBindingFile() {
		return bindingFile;
	}

	/**
	 * The specific model element in <code>modelResource</code> from the page asking the user for the target
	 * model.
	 */
	void setModelTarget(EObject modelTarget) {
		this.modelTarget = modelTarget;
	}

	/**
	 * The specific model element in <code>modelResource</code> from the page asking the user for the target
	 * model.
	 */
	EObject getModelTarget() {
		return modelTarget;
	}
}
