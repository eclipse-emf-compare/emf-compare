/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 508526
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback.TextFallbackCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback.TextFallbackCompareInputLabelProvider;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback.TextFallbackMergeViewer;
import org.eclipse.jface.viewers.Viewer;
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
		final EMFCompareConfiguration emfConfig = new EMFCompareConfiguration(config);
		TextFallbackMergeViewer textFallbackMergeViewer = new TextFallbackMergeViewer(parent, emfConfig);
		emfConfig.setLabelProvider(TextFallbackCompareInput.class,
				new TextFallbackCompareInputLabelProvider(textFallbackMergeViewer, emfConfig));
		return textFallbackMergeViewer;
	}

}
