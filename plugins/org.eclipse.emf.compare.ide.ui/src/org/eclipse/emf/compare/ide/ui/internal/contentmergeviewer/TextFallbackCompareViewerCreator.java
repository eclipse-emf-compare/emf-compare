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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TextFallbackCompareViewerCreator implements IViewerCreator {

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class TextFallbackMergeViewer extends TextMergeViewer {
		private Object originalInput;

		/**
		 * @param parent
		 * @param configuration
		 */
		private TextFallbackMergeViewer(Composite parent, CompareConfiguration configuration) {
			super(parent, configuration);
		}

		@Override
		public void setInput(Object input) {
			originalInput = input;
			if (input instanceof CompareInputAdapter) {
				Notifier target = ((CompareInputAdapter)input).getTarget();
				EObject data = ((TreeNode)target).getData();

				Comparison comparison = ComparisonUtil.getComparison(data);
				if (comparison != null) {
					ICompareInput compareInput = (ICompareInput)EcoreUtil.getAdapter(comparison.eAdapters(),
							ICompareInput.class);
					if (compareInput instanceof ForwardingCompareInput) {
						super.setInput(((ForwardingCompareInput)compareInput).delegate());
					} else {
						EMFCompareIDEUIPlugin.getDefault().log(IStatus.ERROR,
								"Comparison cannot be adapted to ICompareInput.");
					}
				} else {
					EMFCompareIDEUIPlugin.getDefault().log(IStatus.ERROR,
							"Cannot find a comparison from input " + input);
				}
			} else if (input instanceof ForwardingCompareInput) {
				super.setInput(((ForwardingCompareInput)input).delegate());
			} else {
				super.setInput(input);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ContentViewer#getInput()
		 */
		@Override
		public Object getInput() {
			return originalInput;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
		 */
		@Override
		protected void handleDispose(DisposeEvent event) {
			super.handleDispose(event);
			originalInput = null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getTitle()
		 */
		@Override
		public String getTitle() {
			return EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.title"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new TextFallbackMergeViewer(parent, config);
	}

}
