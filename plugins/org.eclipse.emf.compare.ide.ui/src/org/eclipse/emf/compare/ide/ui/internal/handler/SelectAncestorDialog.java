package org.eclipse.emf.compare.ide.ui.internal.handler;

import org.eclipse.compare.internal.CompareMessages;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectAncestorDialog extends MessageDialog {
	private Notifier[] notifiers;

	Notifier originNotifier;

	Notifier leftNotifier;

	Notifier rightNotifier;

	private Button[] buttons;

	private final AdapterFactory adapterFactory;

	public SelectAncestorDialog(Shell parentShell, AdapterFactory adapterFactory, Notifier[] theResources) {
		super(parentShell, CompareMessages.SelectAncestorDialog_title, null,
				CompareMessages.SelectAncestorDialog_message, MessageDialog.QUESTION, new String[] {
						IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.adapterFactory = adapterFactory;
		this.notifiers = theResources;
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		buttons = new Button[3];
		for (int i = 0; i < 3; i++) {
			buttons[i] = new Button(composite, SWT.RADIO);
			buttons[i].addSelectionListener(selectionListener);
			String text = "'" + AbstractCompareHandler.getText(adapterFactory, notifiers[i]) + "'";
			if (notifiers[i] instanceof EObject) {
				text = text + " (" + EcoreUtil.getURI(((EObject)notifiers[i])) + ")";
			}
			buttons[i].setText(text);
			buttons[i].setFont(parent.getFont());
			// set initial state
			buttons[i].setSelection(i == 0);
		}
		pickOrigin(0);
		return composite;
	}

	private void pickOrigin(int i) {
		originNotifier = notifiers[i];
		leftNotifier = notifiers[i == 0 ? 1 : 0];
		rightNotifier = notifiers[i == 2 ? 1 : 2];
	}

	private SelectionListener selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button selectedButton = (Button)e.widget;
			if (!selectedButton.getSelection()) {
				return;
			}
			for (int i = 0; i < 3; i++) {
				if (selectedButton == buttons[i]) {
					pickOrigin(i);
				}
			}
		}
	};
}
