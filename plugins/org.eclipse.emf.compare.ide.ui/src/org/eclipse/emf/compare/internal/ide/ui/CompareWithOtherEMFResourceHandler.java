/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.ide.ui;

import java.util.Iterator;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.internal.CompareMessages;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Command handler of org.eclipse.emf.compare.internal.ide.ui.openCompareEditor.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareWithOtherEMFResourceHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		IWorkbenchPage workbenchPage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		if (selection.isEmpty()) {
			return null; // Reserved for future use, must be null (see
							// org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
							// documentation)
		}

		if (selection instanceof IStructuredSelection) {
			int size = ((IStructuredSelection)selection).size();
			if (size >= 2 && size <= 3) {
				doExecute((IStructuredSelection)selection, workbenchPage);
			} else {
				throw new ExecutionException("Can not execute handler " + this
						+ " with a number of selections other than 2 or 3.");
			}
		} else {
			throw new ExecutionException("Can not execute handler " + this + " with a selection of type "
					+ selection.getClass().getName());
		}

		return null; // Reserved for future use, must be null (see
		// org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
		// documentation)
	}

	/**
	 * Do execute the comparison by creating the proper {@link EMFCompareEditorInput} and opening the compare
	 * editor.
	 * <p>
	 * If the compare is three-way, it asks to the user which resource to use as the ancestor. Returns quickly
	 * if cancel is hit.
	 * 
	 * @param selection
	 *            the selected element.
	 * @param workbenchPage
	 *            the parent page in which the editor has to be displayed.
	 * @throws ExecutionException
	 *             if any selected element is not an {@link IFile} or if something goes wrong during the
	 *             loading of the resources.
	 */
	private static void doExecute(IStructuredSelection selection, IWorkbenchPage workbenchPage)
			throws ExecutionException {
		Iterator<?> iterator = selection.iterator();
		Object first = iterator.next();
		Object second = iterator.next();
		Object third = null;
		if (selection.size() == 3) {
			third = iterator.next();
		}

		if (first instanceof IFile && second instanceof IFile && (third == null || third instanceof IFile)) {
			Resource leftResource = getResourceFrom((IFile)first);
			Resource rightResource = getResourceFrom((IFile)second);
			Resource ancestordResource = getResourceFrom((IFile)third);

			if (ancestordResource != null) {
				final Shell shell = workbenchPage.getWorkbenchWindow().getShell();
				SelectAncestorDialog dialog = new SelectAncestorDialog(shell, new Resource[] {leftResource,
						rightResource, ancestordResource, });
				int code = dialog.open();
				if (code != Window.OK) {
					return;
				}
				leftResource = dialog.leftResource;
				rightResource = dialog.rightResource;
				ancestordResource = dialog.ancestorResource;
			}

			CompareConfiguration cc = new CompareConfiguration();
			AdapterFactory adapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			EMFCompareEditorInput input = new EMFCompareEditorInput(cc, adapterFactory, leftResource,
					rightResource, ancestordResource);
			CompareUI.openCompareEditorOnPage(input, workbenchPage);
		} else {
			throw new ExecutionException("Selected objects are not all IFile.");
		}
	}

	/**
	 * Return a loaded resource from the given {@link IFile}. If the given argument is null, returns null.
	 * 
	 * @param iFile
	 *            the file to load the resource from.
	 * @return a loaded {@link Resource} or null if the given file is null.
	 * @throws ExecutionException
	 *             is something goes wrong during loading of the resource.
	 */
	private static Resource getResourceFrom(IFile iFile) throws ExecutionException {
		if (iFile == null) {
			return null;
		}

		URI resourceURI = URI.createPlatformResourceURI(iFile.getFullPath().toString(), true);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource;
		try {
			resource = resourceSet.getResource(resourceURI, true);
		} catch (WrappedException e) {
			throw new ExecutionException("Unable to load IFile " + iFile.getFullPath().toString(), e
					.exception());
		}
		return resource;
	}

	/**
	 * A simple message dialog to let the user specify which {@link Resource} is the ancestor.
	 */
	private static class SelectAncestorDialog extends MessageDialog {
		/**
		 * The chosen ancestor.
		 */
		Resource ancestorResource;

		/**
		 * The chosen left Resource.
		 */
		Resource leftResource;

		/**
		 * The chosen right Resource.
		 */
		Resource rightResource;

		/**
		 * The array of resource to choose from.
		 */
		private Resource[] theResources;

		/**
		 * The array of button corresponding to the array of resources.
		 */
		private Button[] buttons;

		/**
		 * The object listening to the change of the selection in the array of button and triggering the
		 * {@link #pickAncestor(int)} method on event catching.
		 */
		private SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selectedButton = (Button)e.widget;
				if (!selectedButton.getSelection()) {
					return;
				}
				for (int i = 0; i < 3; i++) {
					if (selectedButton == buttons[i]) {
						pickAncestor(i);
					}
				}
			}
		};

		/**
		 * Constructor.
		 * 
		 * @param parentShell
		 *            the parent shell to be used to display the dialog.
		 * @param theResources
		 *            the array of resource to choose from.
		 */
		public SelectAncestorDialog(Shell parentShell, Resource[] theResources) {
			super(parentShell, CompareMessages.SelectAncestorDialog_title, null,
					CompareMessages.SelectAncestorDialog_message, MessageDialog.QUESTION, new String[] {
							IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL, }, 0);
			this.theResources = theResources;
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout());
			buttons = new Button[3];
			for (int i = 0; i < 3; i++) {
				buttons[i] = new Button(composite, SWT.RADIO);
				buttons[i].addSelectionListener(selectionListener);
				buttons[i].setText(NLS.bind(CompareMessages.SelectAncestorDialog_option, theResources[i]
						.getURI().toPlatformString(true)));
				buttons[i].setFont(parent.getFont());
				// set initial state
				buttons[i].setSelection(i == 0);
			}
			pickAncestor(0);
			return composite;
		}

		/**
		 * Set the resource fields to the proper value regarding the given index. The left and the right
		 * resources are set as followed:
		 * <ul>
		 * <li>If the ancestor is the first element in the resources array, the left is the second and right
		 * is the <third.</li>
		 * <li>If the ancestor is the second element in the resources array, the left is the first and right
		 * is the third.</li>
		 * <li>If the ancestor is the third element in the resources array, the left is the first, and right
		 * is the second.</li>
		 * </ul>
		 * 
		 * @param i
		 *            the index of the ancestor.
		 */
		private void pickAncestor(int i) {
			ancestorResource = theResources[i];
			if (i == 0) {
				leftResource = theResources[1];
			} else {
				leftResource = theResources[0];
			}

			if (i == 2) {
				rightResource = theResources[1];
			} else {
				rightResource = theResources[2];
			}
		}
	}

}
