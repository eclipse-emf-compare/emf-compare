/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This resource bundle wraps the resource bundle of an EMF Compare content merge viewer to default to the
 * resource bundle of the Eclipse Compare TextMergeViewer for any non-defined keys. The TextMergeViewer
 * already defines the label, tooltip and image for the following actions:
 * <ul>
 * <li>CopyLeftToRight</li>
 * <li>CopyRightToLeft</li>
 * <li>CopyDiffLeftToRight</li>
 * <li>CopyDiffRightToLeft</li>
 * <li>NextDiff</li>
 * <li>PrevDiff</li>
 * <li>NextChange</li>
 * <li>PrevChange</li>
 * <li>EnableAncestor</li>
 * <li>IgnoreAncestor</li>
 * <li>SwitchLeftAndRight (added in Oxygen)</li>
 * </ul>
 * Specifically, this class avoids the problem of re-defining the images which may change between versions,
 * e.g., the change from gif to png done in Oxygen.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class EMFCompareContentMergeViewerResourceBundle extends ResourceBundle {
	private static final String FALLBACK_BUNDLE_NAME = "org.eclipse.compare.contentmergeviewer.TextMergeViewerResources"; //$NON-NLS-1$

	protected ResourceBundle mergeViewerResources;

	protected ResourceBundle fallbackMergeViewerResources;

	public EMFCompareContentMergeViewerResourceBundle(ResourceBundle bundle) {
		this.mergeViewerResources = bundle;
		this.fallbackMergeViewerResources = ResourceBundle.getBundle(FALLBACK_BUNDLE_NAME);
	}

	@Override
	protected Object handleGetObject(String key) {
		Object object = null;
		try {
			object = mergeViewerResources.getObject(key);
		} catch (MissingResourceException e) {
			// ignore missing resources, try fallback
		}
		if (object == null && fallbackMergeViewerResources != null) {
			object = fallbackMergeViewerResources.getObject(key);
		}
		return object;
	}

	@Override
	public Enumeration<String> getKeys() {
		HashSet<String> bundleKeys = Sets.newHashSet(Collections.list(mergeViewerResources.getKeys()));
		if (fallbackMergeViewerResources != null) {
			bundleKeys.addAll(Collections.list(fallbackMergeViewerResources.getKeys()));
		}
		return Collections.enumeration(bundleKeys);
	}

}
