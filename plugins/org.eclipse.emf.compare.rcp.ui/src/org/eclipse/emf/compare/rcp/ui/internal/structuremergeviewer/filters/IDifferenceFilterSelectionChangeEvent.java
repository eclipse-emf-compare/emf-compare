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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters;

import com.google.common.base.Predicate;

import org.eclipse.emf.ecore.EObject;

/**
 * The {@link IDifferenceFilterSelectionChangeEvent} is type of event that will be posted through an event
 * bus. When clicking on a filter through the EMF Compare UI in order to activate or deactivate it, an event
 * of type {@link IDifferenceFilterSelectionChangeEvent} will be posted through an event bus.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public interface IDifferenceFilterSelectionChangeEvent {

	/**
	 * The {@link IDifferenceFilter} associated with this event.
	 * 
	 * @return The {@link IDifferenceFilter} associated with this event.
	 */
	IDifferenceFilter getFilter();

	/**
	 * The {@link Action} associated with this event.
	 * 
	 * @return The {@link Action} associated with this event.
	 */
	Action getAction();

	/**
	 * Returns the viewer predicate.
	 * 
	 * @return the viewer predicate.
	 */
	Predicate<? super EObject> getAggregatedPredicate();

	/**
	 * The types of actions which can be associated with the events.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	enum Action {
		/** Corresponding to a selection of an {@link IDifferenceFilter}. */
		SELECTED,
		/** Corresponding to a deselection of an {@link IDifferenceFilter}. */
		DESELECTED,
	}
}
