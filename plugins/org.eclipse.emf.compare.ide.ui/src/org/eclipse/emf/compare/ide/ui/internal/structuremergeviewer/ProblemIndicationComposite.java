/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.emf.common.ui.DiagnosticComposite;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ProblemIndicationComposite extends Composite {

	public static class TextProvider extends DiagnosticComposite.TextProvider {
		/**
		 * Returns the message to be displayed next to the icon, at the top of the editor.
		 * 
		 * @return a not null String
		 */
		public String getMessage(Diagnostic rootDiagnostic) {
			if (rootDiagnostic.getSeverity() == Diagnostic.OK) {
				return EMFCompareIDEUIMessages.getString("_UI_NoProblems_message"); //$NON-NLS-1$
			} else if (rootDiagnostic.getSeverity() == Diagnostic.CANCEL) {
				return EMFCompareIDEUIMessages.getString("_UI_Cancel_message"); //$NON-NLS-1$				
			} else {
				return EMFCompareIDEUIMessages.getString("_UI_DefaultProblem_message"); //$NON-NLS-1$
			}
		}
	}

	private Diagnostic diagnostic;

	private Composite detailsComposite;

	private Text messageText;

	private Label imageLabel;

	private TextProvider textProvider = new TextProvider();

	private DiagnosticComposite diagnosticComposite;

	/**
	 * @param parent
	 * @param style
	 */
	public ProblemIndicationComposite(Composite parent, int style) {
		super(parent, style);
		createControl(this);
	}

	protected void createControl(Composite parent) {
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			int spacing = 8;
			int margins = 8;
			layout.marginBottom = margins;
			layout.marginTop = margins;
			layout.marginLeft = margins;
			layout.marginRight = margins;
			layout.horizontalSpacing = spacing;
			layout.verticalSpacing = spacing;
			parent.setLayout(layout);
		}

		imageLabel = new Label(parent, SWT.NONE);

		messageText = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.NO_FOCUS);
		messageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		messageText.setBackground(messageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		detailsComposite = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
		data.horizontalSpan = 2;
		detailsComposite.setLayoutData(data);
		{
			GridLayout layout = new GridLayout();
			int margin = -5;
			int spacing = 3;
			layout.marginTop = margin;
			layout.marginLeft = margin;
			layout.marginRight = margin;
			layout.marginBottom = margin;
			layout.horizontalSpacing = spacing;
			layout.verticalSpacing = spacing;
			detailsComposite.setLayout(layout);
		}
		updateDetails();

		refresh();
		layout(true);
	}

	protected void refresh() {
		if (diagnostic != null && messageText != null) {
			Image image = getImage();
			if (image != null) {
				image.setBackground(imageLabel.getBackground());
				imageLabel.setImage(image);
				imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER
						| GridData.VERTICAL_ALIGN_BEGINNING));
			}

			messageText.setText(getMessage());

			if (diagnosticComposite != null && diagnosticComposite.getDiagnostic() != diagnostic) {
				diagnosticComposite.setDiagnostic(diagnostic);
			}
		}
	}

	protected void updateDetails() {
		if (diagnosticComposite == null) {
			diagnosticComposite = new DiagnosticComposite(detailsComposite, SWT.NONE);
			diagnosticComposite.setSeverityMask(DiagnosticComposite.ERROR_WARNING_MASK);
			diagnosticComposite.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL));
			diagnosticComposite.setTextProvider(textProvider);
			diagnosticComposite.initialize(getDiagnostic());
			detailsComposite.layout(true);
		} else {
			diagnosticComposite.setVisible(true);
		}
	}

	protected Image getImage() {
		Display display = Display.getCurrent();
		switch (diagnostic.getSeverity()) {
			case Diagnostic.ERROR:
				return display.getSystemImage(SWT.ICON_ERROR);
			case Diagnostic.WARNING:
			case Diagnostic.CANCEL:
				return display.getSystemImage(SWT.ICON_WARNING);
			default:
				return display.getSystemImage(SWT.ICON_INFORMATION);
		}
	}

	protected String getMessage() {
		return textProvider.getMessage(getDiagnostic());
	}

	/**
	 * @return
	 */
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

	/**
	 * @param diagnostic
	 */
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
		refresh();
	}

}
