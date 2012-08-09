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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.text;

import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareTextMergeViewer extends TextMergeViewer {

	private static final String BUNDLE_NAME = EMFCompareTextMergeViewer.class.getName();

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareTextMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		// super.copy(leftToRight);
		Object input = getInput();
		if (input instanceof AttributeChangeNode) {
			AttributeChange attributeChange = ((AttributeChangeNode)input).getTarget();
			if (leftToRight) {
				attributeChange.copyLeftToRight();
			} else {
				attributeChange.copyRightToLeft();
			}
		}
		System.out.println("EMFCompareTextMergeViewer.copy(" + leftToRight + ")");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager tbm) {
	}

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

}
