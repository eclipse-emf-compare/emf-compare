/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;

/**
 * Specific {@link Adapter} to {@link Match}es that are related to containment {@link ReferenceChange}s (that
 * are placed in their parent {@link Match}).
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class MatchOfContainmentReferenceChangeAdapter extends AdapterImpl {

	/** The {@link ReferenceChange} to associate with the adapted {@link Match}. */
	private ReferenceChange referenceChange;

	/**
	 * Constructor.
	 * 
	 * @param referenceChange
	 *            The {@link ReferenceChange} to associate with the adapted {@link Match}.
	 */
	public MatchOfContainmentReferenceChangeAdapter(ReferenceChange referenceChange) {
		super();
		this.referenceChange = referenceChange;
	}

	/**
	 * {@inheritDoc} .
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		if (type == MatchOfContainmentReferenceChangeAdapter.class) {
			return true;
		}
		return super.isAdapterForType(type);
	}

	/**
	 * Get the {@link ReferenceChange} to associate with the adapted {@link Match}.
	 * 
	 * @return The {@link ReferenceChange} to associate with the adapted {@link Match}.
	 */
	public ReferenceChange getReferenceChange() {
		return referenceChange;
	}
}
