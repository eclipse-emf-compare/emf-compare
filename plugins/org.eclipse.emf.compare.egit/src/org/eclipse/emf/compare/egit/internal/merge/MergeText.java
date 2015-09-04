/*******************************************************************************
 * Copyright (C) 2015, Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.merge;

//CHECKSTYLE:OFF
import org.eclipse.osgi.util.NLS;

/**
 * Internationalized messages
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MergeText extends NLS {

	/**
	 * Do not in-line this into the static initializer as the "Find Broken Externalized Strings" tool will not
	 * be able to find the corresponding bundle file.
	 */
	private static final String BUNDLE_NAME = "org.eclipse.emf.compare.egit.core.internal.merge.mergetext"; //$NON-NLS-1$

	public static String RecursiveModelMerger_RefreshError;

	public static String RecursiveModelMerger_AdaptError;

	public static String RecursiveModelMerger_ScopeInitializationInterrupted;

	static {
		initializeMessages(BUNDLE_NAME, MergeText.class);
	}
}
// CHECKSTYLE:ON
