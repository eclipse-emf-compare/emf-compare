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
import org.eclipse.compare.internal.MergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TextFallbackCompareViewerCreator implements IViewerCreator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new TextFallbackMergeViewer(parent, new EMFCompareConfiguration(config));
	}

	private static ICompareInput getAdaptedCompareInput(CompareInputAdapter input) {
		final ICompareInput adaptedCompareInput;
		Notifier target = input.getTarget();
		if (target instanceof TreeNode) {
			TreeNode treeNode = (TreeNode)target;
			EObject data = treeNode.getData();
			Comparison comparison = ComparisonUtil.getComparison(data);
			if (comparison != null) {
				ICompareInput compareInput = (ICompareInput)EcoreUtil.getAdapter(comparison.eAdapters(),
						ICompareInput.class);
				if (compareInput instanceof ForwardingCompareInput) {
					adaptedCompareInput = ((ForwardingCompareInput)compareInput).delegate();
				} else {
					adaptedCompareInput = compareInput;
				}
			} else {
				adaptedCompareInput = null;
				EMFCompareIDEUIPlugin.getDefault().log(IStatus.ERROR,
						"Cannot find a comparison from input " + input); //$NON-NLS-1$
			}
		} else {
			adaptedCompareInput = null;
		}
		return adaptedCompareInput;
	}

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
			setContentProvider(new TextFallbackMergeViewerContentProvider(configuration));
		}
	
		@Override
		public void setInput(Object input) {
			originalInput = input;
			if (input instanceof CompareInputAdapter) {
				super.setInput(getAdaptedCompareInput((CompareInputAdapter)input));
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
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class TextFallbackMergeViewerContentProvider extends MergeViewerContentProvider {
		/**
		 * @param cc
		 */
		private TextFallbackMergeViewerContentProvider(CompareConfiguration cc) {
			super(cc);
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isLeftEditable(java.lang.Object)
		 */
		@Override
		public boolean isLeftEditable(Object element) {
			final boolean leftEditable;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					leftEditable = super.isLeftEditable(adaptedCompareInput);
				} else {
					leftEditable = super.isLeftEditable(element);
				}
			} else {
				leftEditable = super.isLeftEditable(element);
			}
			return leftEditable;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isRightEditable(java.lang.Object)
		 */
		@Override
		public boolean isRightEditable(Object element) {
			final boolean rightEditable;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					rightEditable = super.isRightEditable(adaptedCompareInput);
				} else {
					rightEditable = super.isRightEditable(element);
				}
			} else {
				rightEditable = super.isRightEditable(element);
			}
			return rightEditable;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveLeftContent(java.lang.Object,
		 *      byte[])
		 */
		@Override
		public void saveLeftContent(Object element, byte[] bytes) {
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					super.saveLeftContent(adaptedCompareInput, bytes);
				} else {
					super.saveLeftContent(element, bytes);
				}
			} else {
				super.saveLeftContent(element, bytes);
			}
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveRightContent(java.lang.Object,
		 *      byte[])
		 */
		@Override
		public void saveRightContent(Object element, byte[] bytes) {
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					super.saveRightContent(adaptedCompareInput, bytes);
				} else {
					super.saveRightContent(element, bytes);
				}
			} else {
				super.saveRightContent(element, bytes);
			}
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorLabel(java.lang.Object)
		 */
		@Override
		public String getAncestorLabel(Object element) {
			final String ancestorLabel;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					ancestorLabel = super.getAncestorLabel(adaptedCompareInput);
				} else {
					ancestorLabel = super.getAncestorLabel(element);
				}
			} else {
				ancestorLabel = super.getAncestorLabel(element);
			}
			return ancestorLabel;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorImage(java.lang.Object)
		 */
		@Override
		public Image getAncestorImage(Object element) {
			final Image ancestorImage;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					ancestorImage = super.getAncestorImage(adaptedCompareInput);
				} else {
					ancestorImage = super.getAncestorImage(element);
				}
			} else {
				ancestorImage = super.getAncestorImage(element);
			}
			return ancestorImage;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getAncestorContent(java.lang.Object)
		 */
		@Override
		public Object getAncestorContent(Object element) {
			final Object ancestorContent;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					ancestorContent = super.getAncestorContent(adaptedCompareInput);
				} else {
					ancestorContent = super.getAncestorContent(element);
				}
			} else {
				ancestorContent = super.getAncestorContent(element);
			}
			return ancestorContent;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftLabel(java.lang.Object)
		 */
		@Override
		public String getLeftLabel(Object element) {
			final String leftLabel;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					leftLabel = super.getLeftLabel(adaptedCompareInput);
				} else {
					leftLabel = super.getLeftLabel(element);
				}
			} else {
				leftLabel = super.getLeftLabel(element);
			}
			return leftLabel;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftImage(java.lang.Object)
		 */
		@Override
		public Image getLeftImage(Object element) {
			final Image leftImage;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					leftImage = super.getLeftImage(adaptedCompareInput);
				} else {
					leftImage = super.getLeftImage(element);
				}
			} else {
				leftImage = super.getLeftImage(element);
			}
			return leftImage;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getLeftContent(java.lang.Object)
		 */
		@Override
		public Object getLeftContent(Object element) {
			final Object leftContent;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					leftContent = super.getLeftContent(adaptedCompareInput);
				} else {
					leftContent = super.getLeftContent(element);
				}
			} else {
				leftContent = super.getLeftContent(element);
			}
			return leftContent;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightLabel(java.lang.Object)
		 */
		@Override
		public String getRightLabel(Object element) {
			final String rightLabel;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					rightLabel = super.getRightLabel(adaptedCompareInput);
				} else {
					rightLabel = super.getRightLabel(element);
				}
			} else {
				rightLabel = super.getRightLabel(element);
			}
			return rightLabel;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightImage(java.lang.Object)
		 */
		@Override
		public Image getRightImage(Object element) {
			final Image rightImage;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					rightImage = super.getRightImage(adaptedCompareInput);
				} else {
					rightImage = super.getRightImage(element);
				}
			} else {
				rightImage = super.getRightImage(element);
			}
			return rightImage;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#getRightContent(java.lang.Object)
		 */
		@Override
		public Object getRightContent(Object element) {
			final Object rightContent;
			if (element instanceof CompareInputAdapter) {
				ICompareInput adaptedCompareInput = getAdaptedCompareInput((CompareInputAdapter)element);
				if (adaptedCompareInput != null) {
					rightContent = super.getRightContent(adaptedCompareInput);
				} else {
					rightContent = super.getRightContent(element);
				}
			} else {
				rightContent = super.getRightContent(element);
			}
			return rightContent;
		}
	}

}
