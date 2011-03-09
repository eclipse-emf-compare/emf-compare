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
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.actions.AbstractCompareAction;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.transform.TransformActivator;
import org.eclipse.emf.compare.mpatch.transform.impl.DefaultMPatchGrouping;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This implementation introduces {@link ChangeGroup} in an unstructured {@link MPatchModel}, that is, it must not
 * already contain {@link ChangeGroup}s.
 * 
 * It calls the grouping algorithm defined in {@link DefaultMPatchGrouping}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class GroupingAction extends AbstractCompareAction {

	private static final String ACTION_NAME = "Grouping";

	private static final String INPUT_FILE_EXTENSION = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String OUTPUT_FILE_EXTENSION = MPatchConstants.FILE_EXTENSION_MPATCH;
	private static final String JOB_TITLE = ACTION_NAME + " " + MPatchConstants.MPATCH_SHORT_NAME + "...";

	/**
	 * Default constructor.
	 */
	public GroupingAction() {
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
			int groups = 0;
			try {
				groups = DefaultMPatchGrouping.group(mpatch);
				
				// save the output
				if (groups > 0) {
					output.getContents().add(mpatch);
					try {
						output.save(null);
						
						// successful!
						message = "Grouping successfully finished: " + groups + " groups created.";
					} catch (final IOException e) {
						code = Status.ERROR;
						message = "Could not save grouped " + MPatchConstants.MPATCH_SHORT_NAME + "!";
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
