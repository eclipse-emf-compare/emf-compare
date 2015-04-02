/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Utility class that provides a few static methods useful for resolving.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ResolutionUtil {

	/**
	 * Private constructor (utility pattern).
	 */
	private ResolutionUtil() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Tells this resolver how much of the dependency graph should be created at once. Note that this value
	 * may change during a resolution, which sole "visible" effect would be to prevent resolution of further
	 * outgoing references if the new value is "SELF".
	 * 
	 * @return The current resolution scope.
	 */
	public static CrossReferenceResolutionScope getResolutionScope() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		if (store.getBoolean(EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE)) {
			return CrossReferenceResolutionScope.SELF;
		}
		final String stringValue = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		return CrossReferenceResolutionScope.valueOf(stringValue);
	}

	/**
	 * Checks if the current thread is interrupted or if the given monitor has been canceled.
	 * 
	 * @param monitor
	 *            the monitor to check
	 * @return true if the current thread has been canceled, false otherwise.
	 */
	public static boolean isInterruptedOrCanceled(IProgressMonitor monitor) {
		return Thread.currentThread().isInterrupted() || monitor.isCanceled();
	}

	/**
	 * Returns the IFile located at the given URI.
	 * 
	 * @param uri
	 *            URI we need the file for.
	 * @return The IFile located at the given URI.
	 */
	public static IFile getFileAt(URI uri) {
		final StringBuilder path = new StringBuilder();
		List<String> segments = uri.segmentsList();
		if (uri.isPlatformResource()) {
			segments = segments.subList(1, segments.size());
		}
		for (String segment : segments) {
			path.append(URI.decode(segment)).append('/');
		}
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path.toString()));
	}
}
