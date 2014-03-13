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
package org.eclipse.emf.compare.match;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.utils.IEqualityHelper;

/**
 * A default implementation of {@link IComparisonFactory} that creates a new {@link Comparison} through the
 * {@link CompareFactory#eINSTANCE default CompareFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DefaultComparisonFactory implements IComparisonFactory {

	/** The factory used to instantiate IEqualityHelper to associate with Comparison. */
	private final IEqualityHelperFactory equalityHelperFactory;

	/**
	 * Creates a new DefaultComparisonFactory.
	 * 
	 * @param equalityHelperFactory
	 *            The factory used to instantiate IEqualityHelper to associate with Comparison.
	 */
	public DefaultComparisonFactory(IEqualityHelperFactory equalityHelperFactory) {
		this.equalityHelperFactory = checkNotNull(equalityHelperFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IComparisonFactory#createComparison()
	 */
	public Comparison createComparison() {
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();

		IEqualityHelper equalityHelper = equalityHelperFactory.createEqualityHelper();

		comparison.eAdapters().add(equalityHelper);
		equalityHelper.setTarget(comparison);

		return comparison;
	}

}
