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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent.Action;
import org.eclipse.emf.ecore.EObject;

/**
 * A default implementation of the {@link IDifferenceFilterSelectionChangeEvent}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DifferenceFilterSelectionChangeEventImpl implements IDifferenceFilterSelectionChangeEvent {

	/** The {@link IDifferenceFilter} associated with this event. */
	private final IDifferenceFilter filter;

	/** The {@link Action} associated with this event. */
	private final Action action;

	private final Predicate<? super EObject> predicate;

	/**
	 * Constructs the event.
	 * 
	 * @param filter
	 *            The {@link IDifferenceFilter} that will be associated with this event.
	 * @param action
	 *            The {@link Action} that will be associated with this event.
	 */
	public DifferenceFilterSelectionChangeEventImpl(IDifferenceFilter filter,
			Predicate<? super EObject> predicate, Action action) {
		this.filter = filter;
		this.predicate = predicate;
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent#getFilter()
	 */
	public IDifferenceFilter getFilter() {
		return filter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent#getAggregatedPredicate()
	 */
	public Predicate<? super EObject> getAggregatedPredicate() {
		return predicate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent#getAction()
	 */
	public Action getAction() {
		return action;
	}

}