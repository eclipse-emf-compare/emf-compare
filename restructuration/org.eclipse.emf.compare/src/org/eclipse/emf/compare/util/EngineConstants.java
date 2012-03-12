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
package org.eclipse.emf.compare.util;

/**
 * Defines constants for use with all engine classes.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface EngineConstants {
	/** Integer (value 4) representing high priority. */
	int PRIORITY_HIGH = 4;

	/** Integer (value 5) representing the highest priority. */
	int PRIORITY_HIGHEST = 5;

	/** Integer (value 2) representing low priority. */
	int PRIORITY_LOW = 2;

	/** Integer (value 1) representing the lowest priority. */
	int PRIORITY_LOWEST = 1;

	/** Integer (value 3) representing normal priority. */
	int PRIORITY_NORMAL = 3;
}
