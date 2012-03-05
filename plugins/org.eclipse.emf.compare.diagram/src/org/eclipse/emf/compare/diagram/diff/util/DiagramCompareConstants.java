/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.util;


/**
 * Defines constants used throughout EMF Compare's UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface DiagramCompareConstants {
	
	/** Preferences description for the move threshold. */
	String PREFERENCES_DESCRIPTION_MOVE_THRESHOLD = DiagramCompareUIMessages
			.getString("DiagramCompareConstants.preferences.moveThresholdLabel") + ':'; //$NON-NLS-1$
	
	/** Preferences key for the move threshold. */
	String PREFERENCES_KEY_MOVE_THRESHOLD = "emfcompare.diagram.move.threshold"; //$NON-NLS-1$
}
