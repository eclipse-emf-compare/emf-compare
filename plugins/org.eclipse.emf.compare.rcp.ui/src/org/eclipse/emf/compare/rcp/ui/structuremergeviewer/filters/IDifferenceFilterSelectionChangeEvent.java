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
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters;

/**
 * The {@link IDifferenceFilterSelectionChangeEvent} is type of event that will be posted through an event bus. When
 * clicking on a filter through the EMF Compare UI in order to activate or deactivate it, an event of type
 * {@link IDifferenceFilterSelectionChangeEvent} will be posted through an event bus.
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
	 * The types of actions which can be associated with the events.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	enum Action {
		/** Corresponding to an activation of an {@link IDifferenceFilter}. */
		ADD,
		/** Corresponding to a deactivation of an {@link IDifferenceFilter}. */
		REMOVE,
	}

	/**
	 * A default implementation of the {@link IDifferenceFilterSelectionChangeEvent}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	class DefaultFilterSelectionChangeEvent implements IDifferenceFilterSelectionChangeEvent {

		/** The {@link IDifferenceFilter} associated with this event. */
		private final IDifferenceFilter filter;

		/** The {@link Action} associated with this event. */
		private final Action action;

		/**
		 * Constructs the event.
		 * 
		 * @param filter
		 *            The {@link IDifferenceFilter} that will be associated with this event.
		 * @param action
		 *            The {@link Action} that will be associated with this event.
		 */
		public DefaultFilterSelectionChangeEvent(IDifferenceFilter filter, Action action) {
			this.filter = filter;
			this.action = action;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent#getFilter()
		 */
		public IDifferenceFilter getFilter() {
			return filter;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterSelectionChangeEvent#getAction()
		 */
		public Action getAction() {
			return action;
		}

	}
}
