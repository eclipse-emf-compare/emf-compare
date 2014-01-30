/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.handler;

import java.util.Iterator;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareInDialog extends AbstractCompareHandler {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		final IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		final ISelection currentSelection = HandlerUtil.getCurrentSelection(event);

		if (currentSelection instanceof IStructuredSelection) {
			Iterator<?> iterator = ((IStructuredSelection)currentSelection).iterator();
			Notifier left = null;
			Notifier right = null;
			Notifier origin = null;

			left = (Notifier)iterator.next();
			if (iterator.hasNext()) {
				right = (Notifier)iterator.next();
			}
			if (iterator.hasNext()) {
				origin = (Notifier)iterator.next();
			}

			if (origin != null) {
				Shell shell = HandlerUtil.getActiveShell(event);
				SelectAncestorDialog dialog = new SelectAncestorDialog(shell, adapterFactory, new Notifier[] {
						left, right, origin });
				if (dialog.open() == Window.CANCEL) {
					return null;
				} else {
					left = dialog.leftNotifier;
					right = dialog.rightNotifier;
					origin = dialog.originNotifier;
				}
			}

			if (left instanceof EObject
					&& right instanceof EObject
					&& (EcoreUtil.isAncestor((EObject)left, (EObject)right) || EcoreUtil.isAncestor(
							(EObject)right, (EObject)left))) {
				MessageDialog.openInformation(activePart.getSite().getShell(), "EMF Compare", //$NON-NLS-1$
						EMFCompareIDEUIMessages.getString("CompareSelfWithAncestor")); //$NON-NLS-1$
			} else {
				final CompareEditorInput input = createCompareEditorInput(activePart, adapterFactory, left,
						right, origin);
				CompareUI.openCompareDialog(input);
			}
		}

		return null; // Reserved for future use, MUST be null.
	}
}
