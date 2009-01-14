/*******************************************************************************
 * Copyright (c) 2008, 2009 Dimitrios Kolovos and other.
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
import org.eclipse.emf.compare.diff.service.DiffEngineDescriptor;
import org.eclipse.emf.compare.diff.service.IDiffEngineSelector;
import org.eclipse.emf.compare.match.service.IMatchEngineSelector;
import org.eclipse.emf.compare.match.service.MatchEngineDescriptor;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * When multiple engines are found for the current comparison, this will allow us to show a dialog for the
 * user to select the accurate match or diff engine.
 * 
 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
 * @since 0.9
 */
public class VisualEngineSelector implements IMatchEngineSelector, IDiffEngineSelector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.service.IMatchEngineSelector#selectMatchEngine(java.util.List)
	 */
	public MatchEngineDescriptor selectMatchEngine(final List<MatchEngineDescriptor> engines) {
		final AbstractReturningRunnable runnable = new AbstractReturningRunnable() {
			@Override
			public Object runImpl() {
				final ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getDefault()
						.getActiveShell(), new EngineDescriptorLabelProvider());

				dialog.setMessage(EMFCompareUIMessages.getString("VisualEngineSelector.Dialog.Message")); //$NON-NLS-1$
				dialog.setTitle(EMFCompareUIMessages.getString("VisualEngineSelector.Dialog.Title")); //$NON-NLS-1$
				dialog.setElements(engines.toArray());

				Object result = null;
				dialog.open();

				if (dialog.getReturnCode() == Window.OK) {
					if (dialog.getResult().length > 0) {
						result = dialog.getResult()[0];
					}
				} else {
					throw new EMFCompareException(EMFCompareUIMessages
							.getString("VisualEngineSelector.Dialog.Cancel")); //$NON-NLS-1$
				}

				return result;
			}
		};
		Display.getDefault().syncExec(runnable);

		return (MatchEngineDescriptor)runnable.getResult();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.service.IDiffEngineSelector#selectDiffEngine(java.util.List)
	 */
	public DiffEngineDescriptor selectDiffEngine(final List<DiffEngineDescriptor> engines) {
		final AbstractReturningRunnable runnable = new AbstractReturningRunnable() {
			@Override
			public Object runImpl() {
				final ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getDefault()
						.getActiveShell(), new EngineDescriptorLabelProvider());

				dialog.setMessage(EMFCompareUIMessages.getString("VisualEngineSelector.Dialog.Message")); //$NON-NLS-1$
				dialog.setTitle(EMFCompareUIMessages.getString("VisualEngineSelector.Dialog.Title")); //$NON-NLS-1$
				dialog.setElements(engines.toArray());

				Object result = null;
				dialog.open();

				if (dialog.getReturnCode() == Window.OK) {
					if (dialog.getResult().length > 0) {
						result = dialog.getResult()[0];
					}
				} else {
					throw new EMFCompareException(EMFCompareUIMessages
							.getString("VisualEngineSelector.Dialog.Cancel")); //$NON-NLS-1$
				}

				return result;
			}
		};
		Display.getDefault().syncExec(runnable);

		return (DiffEngineDescriptor)runnable.getResult();
	}

	/**
	 * This implementation of a label provider allows us to visually describe a match or diff engine according
	 * to its descriptor.
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
			Image image = null;
			if (element instanceof MatchEngineDescriptor) {
				final MatchEngineDescriptor descriptor = (MatchEngineDescriptor)element;

				if (descriptor.getIcon().length() > 0) {
					final String contributor = descriptor.getElement().getDeclaringExtension()
							.getNamespaceIdentifier();
					image = AbstractUIPlugin.imageDescriptorFromPlugin(contributor, descriptor.getIcon())
							.createImage();
				}
			} else if (element instanceof DiffEngineDescriptor) {
				final DiffEngineDescriptor descriptor = (DiffEngineDescriptor)element;

				if (descriptor.getIcon().length() > 0) {
					final String contributor = descriptor.getElement().getDeclaringExtension()
							.getNamespaceIdentifier();
					image = AbstractUIPlugin.imageDescriptorFromPlugin(contributor, descriptor.getIcon())
							.createImage();
				}
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
			String text = null;
			if (element instanceof MatchEngineDescriptor) {
				final MatchEngineDescriptor descriptor = (MatchEngineDescriptor)element;

				if (descriptor.getLabel().length() == 0) {
					text = descriptor.getEngineClassName();
				} else {
					text = descriptor.getLabel();
				}
			} else if (element instanceof DiffEngineDescriptor) {
				final DiffEngineDescriptor descriptor = (DiffEngineDescriptor)element;

				if (descriptor.getLabel().length() == 0) {
					text = descriptor.getEngineClassName();
				} else {
					text = descriptor.getLabel();
				}
			}

			return text;
		}
	}
}
