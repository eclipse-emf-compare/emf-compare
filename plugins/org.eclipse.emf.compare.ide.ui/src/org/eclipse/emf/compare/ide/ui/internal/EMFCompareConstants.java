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
package org.eclipse.emf.compare.ide.ui.internal;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class EMFCompareConstants {

	private EMFCompareConstants() {
		// prevents instantiation
	}

	public static final int COMPARE_IMAGE_WIDTH = 22;

	public static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	public static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	public static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	public static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	// CompareConfiguration

	public static final String COMPARE_RESULT = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARE.RESULT"; //$NON-NLS-1$

	public static final String COMPARATOR = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARATOR"; //$NON-NLS-1$

	public static final String EDITING_DOMAIN = EMFCompareIDEUIPlugin.PLUGIN_ID + ".EDITING_DOMAIN"; //$NON-NLS-1$

	public static final String SELECTED_FILTERS = EMFCompareIDEUIPlugin.PLUGIN_ID + ".SELECTED_FILTERS"; //$NON-NLS-1$

	public static final String SELECTED_GROUP = EMFCompareIDEUIPlugin.PLUGIN_ID + ".SELECTED_GROUP"; //$NON-NLS-1$

	// ITypedElement#getType()

	public static final String NODE_TYPE__EMF_RESOURCESET = "NODE_TYPE__EMF_RESOURCESET"; //$NON-NLS-1$

	public static final String NODE_TYPE__EMF_RESOURCE = "NODE_TYPE__EMF_RESOURCE"; //$NON-NLS-1$

	public static final String NODE_TYPE__EMF_EOBJECT = "NODE_TYPE__EMF_EOBJECT"; //$NON-NLS-1$

}
