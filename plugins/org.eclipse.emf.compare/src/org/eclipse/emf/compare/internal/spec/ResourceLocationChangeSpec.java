/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import com.google.common.base.Objects;

import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ResourceLocationChangeImpl;

/**
 * This specialization of the {@link ResourceLocationChangeImpl} class allows us to define the derived
 * features and operations implementations.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class ResourceLocationChangeSpec extends ResourceLocationChangeImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(this)
					.add("baseLocation", baseLocation)
					.add("changedLocation", changedLocation)
					.add("parentMatchResource", eContainer().toString())
					.add("kind", getKind())
					.add("source", getSource())
					.add("state", getState()).toString();
		// @formatter:on
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#basicGetMatch()
	 */
	@Override
	public Match basicGetMatch() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#setMatch(org.eclipse.emf.compare.Match)
	 */
	@Override
	public void setMatch(Match newMatch) {
		// Nothing to do here.
	}
}
