/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.match;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;

/**
 * Implementation of {@link IMatchEngine.Factory} for the {@link DefaultMatchEngine} that can be configured.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class DefaultRCPMatchEngineFactory implements IMatchEngine.Factory {

	/** Attribute used to retrieve UseIdentifier attribute from preferences. */
	public static final String USE_IDENTIFIER_ATTR = "UseIdentifier"; //$NON-NLS-1$

	/** Default value of USE_IDENTIFIER_ATTR attribute. */
	public static final UseIdentifiers DEFAULT_USE_IDENTIFIER_ATRIBUTE = UseIdentifiers.WHEN_AVAILABLE;

	/** Rank of the factory. */
	private int rank;

	/**
	 * Constructor.
	 */
	public DefaultRCPMatchEngineFactory() {
		super();
	}

	/**
	 * Parse the input preference to retrieve the value of
	 * {@link DefaultRCPMatchEngineFactory#USE_IDENTIFIER_ATTR}.
	 * 
	 * @return The value of {@link DefaultRCPMatchEngineFactory#USE_IDENTIFIER_ATTR}.
	 */
	public static UseIdentifiers getUseIdentifierValue() {
		UseIdentifiers result;
		String storedPref = Platform.getPreferencesService().getString(EMFCompareRCPPlugin.PLUGIN_ID,
				USE_IDENTIFIER_ATTR, DEFAULT_USE_IDENTIFIER_ATRIBUTE.toString(), null);
		try {
			result = UseIdentifiers.valueOf(storedPref);
		} catch (IllegalArgumentException e) {
			EMFCompareRCPPlugin.getDefault().log(IStatus.ERROR,
					EMFCompareMessages.getString("RCPMatchEngineFactory.INCORECT_USE_IDENTIFIER_ATTRIBUTE")); //$NON-NLS-1$
			result = DEFAULT_USE_IDENTIFIER_ATRIBUTE;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IMatchEngine getMatchEngine() {
		final UseIdentifiers useUdentifier = getUseIdentifierValue();
		return DefaultMatchEngine.create(useUdentifier,
				EMFCompareRCPPlugin.getDefault().getWeightProviderRegistry());
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRanking() {
		return rank;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRanking(int inputRank) {
		this.rank = inputRank;

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
		return true;
	}

}
