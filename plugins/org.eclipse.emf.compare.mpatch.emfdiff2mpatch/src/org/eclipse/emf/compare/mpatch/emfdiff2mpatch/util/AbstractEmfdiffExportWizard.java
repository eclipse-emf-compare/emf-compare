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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.dialogs.DiagnosticDialog;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.Emfdiff2mpatchActivator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.wizards.EmfdiffExportWizardTransformationPage;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * An abstraction of the wizard that transforms an emfdiff to an {@link MPatchModel}. The reason for having
 * such an abstract wizard is that the most common functionality can be reused in other, maybe more
 * specialized scenarios, too. The default wizard contains a page for selecting the default symbolic reference
 * and model descriptor creators and for selecting the order of additional transformations.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public abstract class AbstractEmfdiffExportWizard extends Wizard implements INewWizard {

	/** Result of the comparison this wizard is meant to export. */
	private ComparisonSnapshot input = null;

	/** References the page displayed by this wizard. */
	private EmfdiffExportWizardTransformationPage transformationPage;

	/** The resulting mpatch. */
	private MPatchModel mPatch;

	/**
	 * Initializes this wizard given the active workbench and the snapshot that will be exported.
	 * 
	 * @param workbench
	 *            Active workbench.
	 * @param inputSnapshot
	 *            The {@link ModelInputSnapshot} that is to be exported by this wizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Save as " + MPatchConstants.MPATCH_LONG_NAME);
		setNeedsProgressMonitor(true);

		for (Object obj : selection.toArray()) {
			if (obj instanceof ComparisonSnapshot) {
				ComparisonSnapshot inputSnapshot = (ComparisonSnapshot)obj;

				// ensures no modification will be made to the input
				// input.add((ComparisonSnapshot)EcoreUtil.copy(inputSnapshot));
				input = inputSnapshot; // we do not modify the input, so it is safe to use it directly
			}
		}

		// we cannot do anything if input is empty!
		if (input == null) {
			final String message = "Cannot transform an emfdiff to " + MPatchConstants.MPATCH_SHORT_NAME
					+ " if there is no emfdiff given!";
			MessageDialog.openError(getShell(), "No emfdiff given!", message);
			throw new RuntimeException(message);
		}

		initializeDefaultPageImageDescriptor();
	}

	/**
	 * Subclasses are expected to set the image for the wizard via
	 * {@link Wizard#setDefaultPageImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)}.
	 */
	protected abstract void initializeDefaultPageImageDescriptor();

	/**
	 * This performs the transformation and validates the result afterwards.
	 * 
	 * @return In case the transformation was successful (performFinish returns <code>true</code>), the
	 *         differences are accessible via getDiff().
	 */
	@Override
	public boolean performFinish() {
		final boolean[] returnValue = new boolean[] {true}; // easy way to store return value
		mPatch = null;
		final StringBuffer details = new StringBuffer();
		final List<IMPatchTransformation> transformations = transformationPage.getTransformations();

		// use a progress monitor to create mpatch!
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {

					monitor.beginTask("Creating...", 2 + 4 + (transformations.size() * 2) + 2 + 2);
					monitor.worked(2); // PROGRESS MONITOR

					try {
						mPatch = TransformationLauncher.transform(input, details,
								transformationPage.getSymbolicReferenceCreator(),
								transformationPage.getModelDescriptorCreator());
					} catch (final Exception e) {
						Emfdiff2mpatchActivator.getDefault().logError(
								"Could not export " + MPatchConstants.MPATCH_LONG_NAME + "!\n"
										+ details.toString(), e);
						MessageDialog.openError(getShell(), "Transformation failed",
								"The Transformation failed! Please see the error log for details.\nError message: "
										+ e.getMessage());
						returnValue[0] = false;
					}
					monitor.worked(4); // PROGRESS MONITOR

					if (returnValue[0]) { // only if emfdiff2mpatch transformation succeeded
						// perform post-transformations
						final StringBuffer result = new StringBuffer();
						for (IMPatchTransformation transformation : transformations) {
							try {
								final int transformationResult = transformation.transform(mPatch);
								result.append(transformation.getLabel() + ": " + transformationResult + "\n");

								// if (!validateDiffWithMessage(mPatch, "Invalid mPatch by: " +
								// transformation.getLabel()))
								// return false;

								monitor.worked(2); // PROGRESS MONITOR
							} catch (Exception e) {
								Emfdiff2mpatchActivator.getDefault().logError(
										"Error in Transformation: " + transformation.getLabel(), e);
								result.append(transformation.getLabel() + ": " + e.getMessage() + "\n");
							}
						}

						if (!validateMPatchWithMessage(mPatch, MPatchConstants.MPATCH_SHORT_NAME
								+ " cannot be validated successfully")) {
							result.append("\nWarning: Validation of " + MPatchConstants.MPATCH_SHORT_NAME
									+ " failed!\n");
						}
						monitor.worked(2); // PROGRESS MONITOR

						// ask user if the results are ok, otherwise abort
						if (result.length() > 0) {
							result.insert(0, "Transformation results:\n\n");
							result.append("\nContinue saving the resulting "
									+ MPatchConstants.MPATCH_SHORT_NAME + "?\n"
									+ "('No' returns to the wizard)");
							if (!MessageDialog.openQuestion(getShell(), MPatchConstants.MPATCH_SHORT_NAME
									+ " creation results", result.toString())) {
								returnValue[0] = false;
							}
						}
					}
					monitor.done(); // PROGRESS MONITOR
				}
			});
		} catch (InvocationTargetException e) {
			// Ignore
		} catch (InterruptedException e) {
			// Ignore
		}

		return returnValue[0];
	}

	/**
	 * The MPatch after successful creation.
	 * 
	 * @return The mpatch after their successful creation (in performFinish()).
	 */
	protected final MPatchModel getMPatch() {
		return mPatch;
	}

	/**
	 * Validate the given MPatch and show a dialog to the user in case of problems.
	 * 
	 * @param mpatch
	 *            The MPatch to check.
	 * @param title
	 *            The title of the dialog.
	 * @return Whether there was a problem or not.
	 */
	private boolean validateMPatchWithMessage(final MPatchModel mpatch, final String title) {
		final Diagnostic diagnostic = Diagnostician.INSTANCE.validate(mpatch);
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING) {
			DiagnosticDialog.openProblem(getShell(), title, title, diagnostic);
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		transformationPage = new EmfdiffExportWizardTransformationPage(MPatchConstants.MPATCH_SHORT_NAME
				+ " Configuration");
		addPage(transformationPage);
	}
}
