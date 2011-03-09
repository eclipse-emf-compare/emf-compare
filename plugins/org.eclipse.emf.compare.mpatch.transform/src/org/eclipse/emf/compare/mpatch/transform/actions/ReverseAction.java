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
import org.eclipse.emf.compare.mpatch.transform.TransformActivator;
import org.eclipse.emf.compare.mpatch.transform.impl.ReverseMPatch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This implementation reverses the direction of a given MPatch. By default, an MPatch P created from a Model A which is
 * the unchanged version of a Model B can be used to reproduce Model B out of Model A, and not vice versa! So P is
 * directed and cannot be used to create Model A out of Model B.
 * 
 * This transformation reverses P and creates P_r such that P_r applied to Model B yields Model A.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ReverseAction extends AbstractCompareAction {

	private static final String ACTION_NAME = "Reverse";

	private static final String INPUT_FILE_EXTENSION = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String OUTPUT_FILE_EXTENSION = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String JOB_TITLE = ACTION_NAME + " " + MPatchConstants.MPATCH_SHORT_NAME + "...";

	/**
	 * Default constructor.
	 */
	public ReverseAction() {
		super(INPUT_FILE_EXTENSION, OUTPUT_FILE_EXTENSION, JOB_TITLE);
	}

	/**
	 * Performing the actual transformation and return appropriate status.
	 * 
	 * @return Appropriate status.
	 */
	@Override
	protected Status runAction(Resource input, Resource output, IProgressMonitor monitor) {
		String message = "";
		int code = Status.OK;
		Exception exception = null;

		// get the mpatch
		final EObject content = input.getContents().get(0);
		if (content instanceof MPatchModel) {
			final MPatchModel mpatch = (MPatchModel)content;

			// perform the restructuring
			int reversed = 0;
			try {
				reversed = ReverseMPatch.reverse(mpatch);
				
				// save the output
				if (reversed > 0) {
					output.getContents().add(mpatch);
					try {
						output.save(null);
						
						// successful!
						message = MPatchConstants.MPATCH_LONG_NAME + " successfully reversed: " + reversed + " changes affected.";
					} catch (final IOException e) {
						code = Status.ERROR;
						message = "Could not save reversed " + MPatchConstants.MPATCH_SHORT_NAME + "!";
						exception = e;
					}
				} else {
					message = ACTION_NAME + " finished: nothing was changed.";
				}
				
			} catch (final Exception e) {
				message = "An exception occured during " + ACTION_NAME;
				exception = e;
				code = Status.ERROR;
			}

		} else {
			code = Status.ERROR;
			message = "Could not find " + MPatchConstants.MPATCH_LONG_NAME + " in:\n\n" + content;
		}
		
		return new Status(code, TransformActivator.PLUGIN_ID, message, exception);
	}
}
