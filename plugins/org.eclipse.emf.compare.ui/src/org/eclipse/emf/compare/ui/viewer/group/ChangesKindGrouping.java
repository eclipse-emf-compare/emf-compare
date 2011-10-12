/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * A facility to group difference elements per kind of changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class ChangesKindGrouping implements IDifferenceGroupingFacility {
	/** Registered groups. */
	private List<UIDifferenceGroup> mGroups = new ArrayList<UIDifferenceGroup>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#allGroups()
	 */
	public Set<UIDifferenceGroup> allGroups() {
		return new HashSet<IDifferenceGroupingFacility.UIDifferenceGroup>(mGroups);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#addGroups(java.util.Set)
	 */
	public void addGroups(Set<UIDifferenceGroup> groups) {
		mGroups.addAll(groups);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#belongsTo(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public UIDifferenceGroup belongsTo(DiffElement d) {
		final UIDifferenceGroup diffGroup;
		switch (d.getKind()) {
			case ADDITION:
				diffGroup = getFromId("additions"); //$NON-NLS-1$
				break;
			case DELETION:
				diffGroup = getFromId("removes"); //$NON-NLS-1$
				break;
			default:
				diffGroup = getFromId("others"); //$NON-NLS-1$
				break;
		}
		return diffGroup;
	}

	/**
	 * Returns the {@link UIDifferenceGroup} from its id.
	 * 
	 * @param id
	 *            The id of the group.
	 * @return The group.
	 */
	private UIDifferenceGroup getFromId(String id) {
		final Iterator<UIDifferenceGroup> it = mGroups.iterator();
		while (it.hasNext()) {
			final UIDifferenceGroup group = it.next();
			if (group.getId().equals(id)) {
				return group;
			}
		}
		return null;
	}
}
