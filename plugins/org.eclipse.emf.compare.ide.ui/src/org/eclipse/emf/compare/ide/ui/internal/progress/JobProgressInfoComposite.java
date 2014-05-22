/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.progress;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.internal.progress.ProgressMessages;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class JobProgressInfoComposite extends Composite {

	private ProgressBar progressBar;

	private Label jobImageLabel;

	private Label jobNameLabel;

	private Label taskNameLabel;

	private ToolBar actionBar;

	private ToolItem actionButton;

	private final Job job;

	/**
	 * @param parent
	 * @param style
	 */
	public JobProgressInfoComposite(Job job, Composite parent, int progressBarStyle, int style) {
		super(parent, style);
		this.job = job;

		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginTop = 2;
		gridLayout.marginBottom = 2;
		gridLayout.marginLeft = 2;
		gridLayout.marginRight = 2;
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		setLayout(gridLayout);

		Image jobImage = EMFCompareIDEUIPlugin.getImage("icons/full/eobj16/task.gif"); //$NON-NLS-1$
		jobNameLabel = new Label(this, SWT.NONE);
		int horizontalIndent = jobImage.getBounds().width + 2;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalIndent = horizontalIndent;
		gridData.horizontalSpan = 3;
		jobNameLabel.setLayoutData(gridData);
		jobNameLabel.setText(job.getName());

		jobImageLabel = new Label(this, SWT.NONE);
		jobImageLabel.setImage(jobImage);
		jobImageLabel.setLayoutData(new GridData());

		progressBar = new ProgressBar(this, SWT.HORIZONTAL | progressBarStyle);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		progressBar.setLayoutData(gridData);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);

		actionBar = new ToolBar(this, SWT.FLAT);
		actionBar.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
		actionBar.setLayoutData(new GridData());

		// set cursor to overwrite any busy cursor we might have

		actionButton = new ToolItem(actionBar, SWT.NONE);
		actionButton.setToolTipText(ProgressMessages.NewProgressView_CancelJobToolTip);
		actionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				actionButton.setEnabled(false);
				JobProgressInfoComposite.this.job.cancel();
			}
		});
		actionButton.setImage(EMFCompareIDEUIPlugin.getImage("icons/full/eobj16/stop.gif")); //$NON-NLS-1$
		actionButton.setDisabledImage(EMFCompareIDEUIPlugin.getImage("icons/full/dobj16/stop.gif")); //$NON-NLS-1$

		taskNameLabel = new Label(this, SWT.NONE);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalIndent = horizontalIndent;
		gridData.horizontalSpan = 3;
		taskNameLabel.setLayoutData(gridData);
	}

	void init() {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				if (!taskNameLabel.isDisposed() && !progressBar.isDisposed()) {
					actionButton.setEnabled(true);
					progressBar.setSelection(0);
				}
			}
		});
	}

	/**
	 * @param taskNameLabel
	 *            the taskNameLabel to set
	 */
	public void setTaskName(final String taskName) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				if (!taskNameLabel.isDisposed() && !JobProgressInfoComposite.this.isDisposed()) {
					taskNameLabel.setText(taskName);
					layout();
				}
			}
		});
	}

	public void setPercentDone(final int percent) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				if (!progressBar.isDisposed()) {
					progressBar.setSelection(percent);
				}
			}
		});
	}
}
