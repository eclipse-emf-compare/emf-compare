/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInputLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * An {@link IViewerCreator} that creates a {@link PropertyContentMergeViewer}.
 */
public class PropertyContentMergeViewerCreator implements IViewerCreator {

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation creates a {@link PropertyContentMergeViewer}.
	 * </p>
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(Composite, CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration configuration) {
		final EMFCompareConfiguration emfCompareConfiguration = new EMFCompareConfiguration(configuration);
		emfCompareConfiguration.setLabelProvider(TreeNodeCompareInput.class,
				new TreeNodeCompareInputLabelProvider());
		return new PropertyContentMergeViewer(parent, emfCompareConfiguration);
	}
}
