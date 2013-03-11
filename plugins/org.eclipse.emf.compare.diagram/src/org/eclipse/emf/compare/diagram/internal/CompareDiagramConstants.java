/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal;

/**
 * Defines constants used throughout EMF Compare's UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface CompareDiagramConstants {

	/** Preferences description for the move threshold. */
	String PREFERENCES_DESCRIPTION_MOVE_THRESHOLD = CompareDiagramUIMessages
			.getString("DiagramCompareConstants.preferences.moveThresholdLabel") + ':'; //$NON-NLS-1$

	/** Preferences key for the move threshold. */
	String PREFERENCES_KEY_MOVE_THRESHOLD = "emfcompare.diagram.move.threshold"; //$NON-NLS-1$
}
