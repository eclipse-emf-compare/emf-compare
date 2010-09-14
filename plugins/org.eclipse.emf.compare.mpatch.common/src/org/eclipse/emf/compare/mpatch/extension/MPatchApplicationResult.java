/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.extension;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * The result of the application of a set of {@link IndepChange}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public final class MPatchApplicationResult {

	/**
	 * The result of application.
	 * 
	 * @see MPatchApplicationResult#status
	 */
	public enum ApplicationStatus {
		/** All changes were applied successfully. */
		SUCCESSFUL,
		/** All changes were applied but some cross references could not be restored. */
		REFERENCES,
		/** Some changes could not be applied at all. */
		FAILURE
	}

	/**
	 * The status of application:
	 * <ul>
	 * <li> {@link ApplicationStatus#SUCCESSFUL}: All changes were applied successfully.
	 * <li> {@link ApplicationStatus#REFERENCES}: All changes were applied but some cross references could not be
	 * restored.
	 * <li> {@link ApplicationStatus#FAILURE}: Some changes could not be applied at all.
	 * </ul>
	 */
	public final ApplicationStatus status;

	/** The set of changes that were applied successfully. */
	public final Collection<IndepChange> successful;

	/** The set of changes that were bound to existing elements. */
	public final Collection<IndepChange> bound;
	
	/** The set of changes for which some (or all) cross-references could not be restored. */
	public final Collection<IndepChange> crossReferences;

	/** The set of changes that could not or only partially be applied. */
	public final Collection<IndepChange> failed;

	/**
	 * Constructor for application result.
	 * 
	 * @param status
	 *            The overall status of application.
	 * @param successful
	 *            A collection of successfully applied {@link IndepChange}s.
	 * @param bound
	 *            A collection of {@link IndepChange}s that are already applied and bound to existing model elements.
	 * @param crossReferences
	 *            A collection of applied {@link IndepChange}s for which not all cross references have been restored
	 *            successfully.
	 * @param failed
	 *            A collection of {@link IndepChange} which failed to be applied.
	 */
	public MPatchApplicationResult(ApplicationStatus status, Collection<IndepChange> successful, Collection<IndepChange> bound,
			Collection<IndepChange> crossReferences, Collection<IndepChange> failed) {
		this.status = status;
		this.successful = Collections.unmodifiableCollection(successful);
		this.bound = Collections.unmodifiableCollection(bound);
		this.crossReferences = Collections.unmodifiableCollection(crossReferences);
		this.failed = Collections.unmodifiableCollection(failed);
	}

	/**
	 * Show a dialog with some user friendly version of the application results.
	 * 
	 * @param shell
	 *            The shell.
	 * @param adapterFactory
	 *            An adapter factory for some formatting ;-)
	 */
	public void showDialog(Shell shell, AdapterFactory adapterFactory) {
		final String msg = getMessage(adapterFactory);
		final int messageDialogType;

		// overall result
		if (ApplicationStatus.SUCCESSFUL.equals(status)) {
			messageDialogType = MessageDialog.INFORMATION;
		} else if (ApplicationStatus.REFERENCES.equals(status)) {
			messageDialogType = MessageDialog.WARNING;
		} else if (ApplicationStatus.FAILURE.equals(status)) {
			messageDialogType = MessageDialog.ERROR;
		} else {
			throw new IllegalStateException("Unknown result status!");
		}

		// show the actual dialog
		MessageDialog.open(messageDialogType, shell, MPatchConstants.MPATCH_SHORT_NAME + " Application results", msg,
				SWT.NONE);
	}

	/**
	 * A human readable message about the application result.
	 * 
	 * @param adapterFactory
	 *            An adapter factory for some formatting ;-)
	 * @return The message.
	 */
	public String getMessage(AdapterFactory adapterFactory) {
		final StringBuffer msg = new StringBuffer();
		final AdapterFactoryLabelProvider labels = new AdapterFactoryLabelProvider(adapterFactory);
		final String indent = "    ";

		// overall result
		if (ApplicationStatus.SUCCESSFUL.equals(status)) {
			msg.append(MPatchConstants.MPATCH_SHORT_NAME + " Application was successful:\n");
		} else if (ApplicationStatus.REFERENCES.equals(status)) {
			msg.append(MPatchConstants.MPATCH_SHORT_NAME + " Application was partly successful:\n");
		} else if (ApplicationStatus.FAILURE.equals(status)) {
			msg.append(MPatchConstants.MPATCH_SHORT_NAME + " Application was not successful:\n");
		} else {
			throw new IllegalStateException("Unknown result status!");
		}

		// detailed summary:
		msg.append(successful.size() + " changes were applied successfully.\n");
		if (bound.size() > 0) {
			msg.append(bound.size() + " changes were already applied.\n");
		}
		if (crossReferences.size() > 0) {
			msg.append(crossReferences.size() + " changes with unsufficient cross-reference restoring:\n");
			for (IndepChange change : crossReferences) {
				msg.append(indent + labels.getText(change) + "\n");
			}
		}
		if (failed.size() > 0) {
			msg.append(failed.size() + " change applications failed:\n");
			for (IndepChange change : failed) {
				msg.append(indent + labels.getText(change) + "\n");
			}
		}
		return msg.toString();
	}
}
