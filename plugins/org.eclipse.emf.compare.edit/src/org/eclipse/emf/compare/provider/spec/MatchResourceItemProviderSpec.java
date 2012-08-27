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
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.provider.MatchResourceItemProvider;

/**
 * Specialized {@link MatchResourceItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchResourceItemProviderSpec extends MatchResourceItemProvider {

	/**
	 * Constructor calling super {@link #MatchResourceItemProviderSpec(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchResourceItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchResourceItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final MatchResource matchResource = (MatchResource)object;
		final String leftURI = matchResource.getLeftURI();
		final String rightURI = matchResource.getRightURI();

		final String commonBase = getCommonBase(leftURI, rightURI);

		String text = leftURI.substring(commonBase.length()) + " <-> "
				+ rightURI.substring(commonBase.length());
		// TODO is that really useful info?
		// if (matchResource.eContainer() instanceof Comparison
		// && ((Comparison)matchResource.eContainer()).isThreeWay()) {
		// final String originURI = matchResource.getOriginURI();
		// text += " (" + originURI + ")";
		// }
		return text;
	}

	/**
	 * Returns the longest common starting substring of the two given strings.
	 * 
	 * @param left
	 *            First of the two strings for which we need the common starting substring.
	 * @param right
	 *            Second of the two strings for which we need the common starting substring.
	 * @return The longest common starting substring of the two given strings.
	 */
	public String getCommonBase(String left, String right) {
		if (left == null || right == null) {
			return ""; //$NON-NLS-1$
		}

		final char[] leftChars = left.toCharArray();
		final char[] rightChars = right.toCharArray();

		final StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < Math.min(leftChars.length, rightChars.length); i++) {
			if (leftChars[i] == rightChars[i]) {
				buffer.append(leftChars[i]);
			} else {
				break;
			}
		}

		return buffer.toString();
	}
}
