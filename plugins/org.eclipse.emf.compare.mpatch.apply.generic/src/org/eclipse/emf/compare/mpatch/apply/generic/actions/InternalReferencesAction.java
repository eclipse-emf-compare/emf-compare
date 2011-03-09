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
package org.eclipse.emf.compare.mpatch.apply.generic.actions;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.apply.generic.GenericApplyActivator;
import org.eclipse.emf.compare.mpatch.apply.generic.impl.GenericMPatchApplier;
import org.eclipse.emf.compare.mpatch.apply.generic.impl.InternalReferences;
import org.eclipse.emf.compare.mpatch.common.actions.AbstractCompareAction;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * An action on {@link MPatchModel} files (extension: {@link MPatchConstants#FILE_EXTENSION_MPATCH}) that replaces symbolic
 * references within the {@link MPatchModel} with {@link ModelDescriptorReference}s.
 * 
 * This is required for the {@link GenericMPatchApplier} to properly refine resolved references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class InternalReferencesAction extends AbstractCompareAction {

	private static final String ACTION_NAME = "Internal " + MPatchConstants.SYMBOLIC_REFERENCES_NAME;

	private static final String inputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String outputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String jobTitle = "Creating " + ACTION_NAME + " for " + MPatchConstants.MPATCH_SHORT_NAME + "...";

	public InternalReferencesAction() {
		super(inputFileExtension, outputFileExtension, jobTitle);
	}

	@Override
	protected Status runAction(Resource input, Resource output, IProgressMonitor monitor) {
		String message = "";
		int code = Status.OK;
		Exception exception = null;

		// get the mpatch
		final EObject content = input.getContents().get(0);
		if (content instanceof MPatchModel) {
			final MPatchModel mpatch = (MPatchModel) content;

			// create internal references
			int refs;
			try {
				refs = InternalReferences.createInternalReferences(mpatch);

				// save the output
				if (refs > 0) {
					output.getContents().add(mpatch);
					try {
						output.save(null);

						// notify the user about result
						message = ACTION_NAME + " successful: " + refs + " internal references created.";
					} catch (final IOException e) {
						code = Status.ERROR;
						exception = e;
						message = "Could not save " + MPatchConstants.MPATCH_SHORT_NAME + " with " + ACTION_NAME + "!";
					}
				} else {
					message = "No " + ACTION_NAME + " created.";
				}
			} catch (final Exception e) {
				code = Status.ERROR;
				exception = e;
				message = "An exception occured during " + ACTION_NAME + " creation";
			}
		} else {
			code = Status.ERROR;
			message = "Could not find " + MPatchConstants.MPATCH_LONG_NAME + " in:\n\n" + content;
		}

		return new Status(code, GenericApplyActivator.PLUGIN_ID, message, exception);
	}
}
