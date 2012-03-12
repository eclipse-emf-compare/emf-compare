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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.generic.GenericApplyActivator;
import org.eclipse.emf.compare.mpatch.apply.generic.impl.MPatchDependencies;
import org.eclipse.emf.compare.mpatch.common.actions.AbstractCompareAction;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * An action on {@link MPatchModel} files (extension: {@link MPatchConstants#FILE_EXTENSION_MPATCH}) that
 * calculates and adds dependencies between the {@link IndepChange}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class DependencyAction extends AbstractCompareAction {

	private static final String ACTION_NAME = "Dependency Graph";

	private static final String inputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;

	private static final String outputFileExtension = MPatchConstants.FILE_EXTENSION_MPATCH;

	private static final String jobTitle = "Creating " + ACTION_NAME + " for "
			+ MPatchConstants.MPATCH_SHORT_NAME + "...";

	public DependencyAction() {
		super(inputFileExtension, outputFileExtension, jobTitle);
	}

	@Override
	protected Status runAction(Resource input, Resource output, IProgressMonitor monitor) {
		String message = "";
		int code = IStatus.OK;
		Exception exception = null;

		// get the mpatch
		final EObject content = input.getContents().get(0);
		if (content instanceof MPatchModel) {
			final MPatchModel mpatch = (MPatchModel)content;

			// introduce dependency graph
			int deps;
			try {
				deps = MPatchDependencies.calculateDependencies(mpatch);

				// save the output
				if (deps > 0) {
					output.getContents().add(mpatch);
					try {
						output.save(null);

						// notify the user about result
						message = ACTION_NAME + " successfully created with " + deps + " dependencies.";
					} catch (final IOException e) {
						exception = e;
						message = "Could not save diff with " + ACTION_NAME + "!";
						code = IStatus.ERROR;
					}
				} else {
					message = "No dependencies created/required for " + MPatchConstants.MPATCH_LONG_NAME
							+ ".";
				}
			} catch (final Exception e) {
				exception = e;
				message = "An exception occured during " + ACTION_NAME + " creation";
				code = IStatus.ERROR;
			}
		} else {
			code = IStatus.ERROR;
			message = "Could not find " + MPatchConstants.MPATCH_LONG_NAME + " in:\n\n" + content;
		}

		return new Status(code, GenericApplyActivator.PLUGIN_ID, message, exception);
	}
}
