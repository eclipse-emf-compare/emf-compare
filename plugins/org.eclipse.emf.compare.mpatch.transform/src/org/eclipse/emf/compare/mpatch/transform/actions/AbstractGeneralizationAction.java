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
package org.eclipse.emf.compare.mpatch.transform.actions;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.actions.AbstractCompareAction;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.transform.TransformActivator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * An abstract action on {@link MPatchModel} files (extension: {@link MPatchConstants#FILE_EXTENSION_MPATCH}) for
 * Generalization of {@link MPatchModel}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public abstract class AbstractGeneralizationAction extends AbstractCompareAction {

	private static final String ACTION_NAME = "Generalization";

	private static final String inputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String outputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String jobTitle = ACTION_NAME + " of " + MPatchConstants.MPATCH_SHORT_NAME + "...";

	public AbstractGeneralizationAction() {
		super(inputFileExtension, outputFileExtension, jobTitle);
	}

	/**
	 * Subclasses should return their transformation here.
	 * 
	 * @return The actual transformation for generalizing an {@link MPatchModel}.
	 */
	protected abstract IMPatchTransformation getTransformation();

	/**
	 * Performing the actual transformation and return appropriate status.
	 * 
	 * @return Appropriate status.
	 */
	@Override
	protected final Status runAction(Resource input, Resource output, IProgressMonitor monitor) {
		String message = "";
		int code = Status.OK;
		Exception exception = null;

		// get the mpatch
		final EObject content = input.getContents().get(0);
		if (content instanceof MPatchModel) {
			final MPatchModel mpatch = (MPatchModel) content;

			try {
				// perform the transformation
				final int modifications = getTransformation().transform(mpatch);
				if (modifications > 0) {

					// save the output
					output.getContents().add(mpatch);
					try {
						output.save(null);

						// return some information
						message = ACTION_NAME + " successfully finished: " + modifications + " modifications.";
					} catch (final IOException e) {
						code = Status.ERROR;
						message = "Could not save generalized " + MPatchConstants.MPATCH_SHORT_NAME + "!";
						exception = e;
					}
				} else {
					message = ACTION_NAME + " finished but did not change anything.";
					// code = Status.WARNING;
				}
			} catch (final Exception e) {
				message = "An exception occured during transformation: " + ACTION_NAME;
				code = Status.ERROR;
				exception = e;
			}

		} else {
			code = Status.ERROR;
			message = "Could not find " + MPatchConstants.MPATCH_LONG_NAME + " in:\n\n" + content;
		}

		return new Status(code, TransformActivator.PLUGIN_ID, message, exception);
	}
}
