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
package org.eclipse.emf.compare.internal.adapterfactory;

import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * Specialized {@link AdapterFactory} with additional ranking.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface RankedAdapterFactory extends AdapterFactory {

	/**
	 * Returns the ranking of the adapter factory.
	 * 
	 * @return the ranking of the adapter factory.
	 */
	int getRanking();

	/**
	 * Set the ranking of the adapter factory.
	 * 
	 * @param ranking
	 *            the ranking of the adapter factory.
	 */
	void setRanking(int ranking);
}
