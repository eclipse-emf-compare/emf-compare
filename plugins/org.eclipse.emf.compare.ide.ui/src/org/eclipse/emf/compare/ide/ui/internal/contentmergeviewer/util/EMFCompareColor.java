/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.CompareColorImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareColor extends CompareColorImpl {

	private final IPropertyChangeListener fCompareConfigurationChangeListener;

	private final CompareConfiguration fCompareConfiguration;

	public EMFCompareColor(ContentMergeViewer contentMergeViewer, IPreferenceStore preferenceStore,
			EMFCompareConfiguration compareConfiguration) {
		super(contentMergeViewer.getControl().getDisplay(), compareConfiguration.getBooleanProperty(
				"LEFT_IS_LOCAL", false), preferenceStore); //$NON-NLS-1$

		this.fCompareConfigurationChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				handlePropertyChangeEvent(event);
			}
		};

		fCompareConfiguration = compareConfiguration;
		fCompareConfiguration.addPropertyChangeListener(fCompareConfigurationChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor#dispose()
	 */
	@Override
	public void dispose() {
		fCompareConfiguration.removePropertyChangeListener(fCompareConfigurationChangeListener);
		super.dispose();
	}
}
