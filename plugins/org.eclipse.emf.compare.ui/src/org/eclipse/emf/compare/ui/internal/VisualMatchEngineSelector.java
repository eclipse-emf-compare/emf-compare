/*******************************************************************************
 * Copyright (c) 2008 Dimitrios Kolovos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation.
 *     Obeo
 *******************************************************************************/
package org.eclipse.emf.compare.ui.internal;

import java.util.List;

import org.eclipse.emf.compare.EMFCompareException;
import org.eclipse.emf.compare.match.service.EngineDescriptor;
import org.eclipse.emf.compare.match.service.IMatchEngineSelector;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * When multiple match engines are found for the current comparison, this will allow us to show a dialog for
 * the user to select the accurate match engine.
 * 
 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
 * @since 0.9
 */
public class VisualMatchEngineSelector implements IMatchEngineSelector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.service.IMatchEngineSelector#selectMatchEngine(java.util.List)
	 */
	public EngineDescriptor selectMatchEngine(final List<EngineDescriptor> engines) {
		final AbstractReturningRunnable runnable = new AbstractReturningRunnable() {
			@Override
			public Object runImpl() {
				final ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getDefault()
						.getActiveShell(), new EngineDescriptorLabelProvider());

				dialog.setMessage(EMFCompareUIMessages.getString("VisualMatchEngineSelector.Dialog.Message")); //$NON-NLS-1$
				dialog.setTitle(EMFCompareUIMessages.getString("VisualMatchEngineSelector.Dialog.Title")); //$NON-NLS-1$
				dialog.setElements(engines.toArray());

				Object result = null;
				dialog.open();

				if (dialog.getReturnCode() == Window.OK) {
					if (dialog.getResult().length > 0) {
						result = dialog.getResult()[0];
					}
				} else {
					throw new EMFCompareException(EMFCompareUIMessages
							.getString("VisualMatchEngineSelector.Dialog.Cancel")); //$NON-NLS-1$
				}

				return result;
			}
		};
		Display.getDefault().syncExec(runnable);

		return (EngineDescriptor)runnable.getResult();
	}

	/**
	 * This implementation of a label provider allows us to visually describe a match engine according to its
	 * {@link EngineDescriptor descriptor}.
	 * 
	 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
	 * @since 0.9
	 */
	protected class EngineDescriptorLabelProvider extends LabelProvider {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {

			final EngineDescriptor descriptor = (EngineDescriptor)element;

			Image image = null;

			if (descriptor.getIcon().length() > 0) {
				final String contributor = descriptor.getElement().getDeclaringExtension()
						.getNamespaceIdentifier();
				image = AbstractUIPlugin.imageDescriptorFromPlugin(contributor, descriptor.getIcon())
						.createImage();
			}

			return image;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			final EngineDescriptor descriptor = (EngineDescriptor)element;

			String text = null;

			if (descriptor.getLabel().length() == 0) {
				text = descriptor.getEngineClassName();
			} else {
				text = descriptor.getLabel();
			}

			return text;
		}
	}
}
