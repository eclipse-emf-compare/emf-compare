/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer;

/**
 * Defines a listener on any ordering changes.
 * 
 * @deprecated Use org.eclipse.jface.util.IPropertyChangeListener instead of it, on
 *             org.eclipse.compare.CompareConfiguration.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
@Deprecated
public interface OrderingListener {
	/**
	 * Event id for the application of an additional filter.
	 */
	int ADD_FILTER_EVENT = 0;

	/**
	 * Event id for the remove of an applied filter.
	 */
	int REMOVE_FILTER_EVENT = 1;

	/**
	 * Event id for the change of application of a grouping kind.
	 */
	int CHANGE_GROUP_EVENT = 2;

	/**
	 * This method is launched when an event is fired.
	 * 
	 * @param event
	 *            The kind of event.
	 * @param descriptor
	 *            The descriptor of the kind of ordering action
	 *            (org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor or
	 *            org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityDescriptor)
	 */
	void notifyChanged(int event, Object descriptor);
}
