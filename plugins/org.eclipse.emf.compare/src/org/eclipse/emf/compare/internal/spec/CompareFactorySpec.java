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
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.CompareFactoryImpl;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.compare.impl.MatchImpl;

/**
 * Specialized {@link CompareFactoryImpl} that creates specialized model object.
 * <p>
 * This EFactory is registered through the <code>org.eclipse.emf.ecore.factory_override</code> extension
 * point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareFactorySpec extends CompareFactoryImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.CompareFactoryImpl#createComparison()
	 */
	@Override
	public Comparison createComparison() {
		ComparisonImpl comparison = new ComparisonSpec();
		return comparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.CompareFactoryImpl#createMatch()
	 */
	@Override
	public Match createMatch() {
		MatchImpl match = new MatchSpec();
		return match;
	}
}
