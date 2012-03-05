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
package org.eclipse.emf.compare.ui.viewer.group;

import java.util.Set;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * Interface for the grouping facilities that can be provided through the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public interface IDifferenceGroupingFacility {
	/**
	 * Represents a difference group.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static final class UIDifferenceGroup {
		/** Id of this group. */
		private String id;

		/** Name of this group. */
		private String name;

		/** Icon of this group. */
		private String icon;

		/**
		 * Instantiates a difference group given all necessary information.
		 * 
		 * @param groupId
		 *            Id of this difference group.
		 * @param groupName
		 *            Name of this difference group.
		 * @param groupIcon
		 *            Icon of this difference group.
		 */
		public UIDifferenceGroup(String groupId, String groupName, String groupIcon) {
			this.id = groupId;
			this.name = groupName;
			this.icon = groupIcon;
		}

		/**
		 * Returns the id of this difference group.
		 * 
		 * @return The id of this difference group.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Returns the name of this difference group.
		 * 
		 * @return The name of this difference group.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the icon of this difference group.
		 * 
		 * @return The icon of this difference group.
		 */
		public String getIcon() {
			return icon;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			int iconHash = 0;
			int idHash = 0;
			int nameHash = 0;
			if (icon != null)
				iconHash = icon.hashCode();
			if (id != null)
				idHash = id.hashCode();
			if (name != null)
				nameHash = name.hashCode();

			final int prime = 31;
			int result = 1;
			result = prime * result + iconHash;
			result = prime * result + idHash;
			result = prime * result + nameHash;
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof UIDifferenceGroup)) {
				return false;
			}

			boolean equal = true;
			if (this == obj) {
				equal = true;
			} else {
				final UIDifferenceGroup other = (UIDifferenceGroup)obj;
				if (icon == null) {
					equal = other.icon == null;
				} else {
					equal = icon.equals(other.icon);
				}
				if (equal && id == null) {
					equal = other.id == null;
				} else if (equal) {
					equal = id.equals(other.id);
				}
				if (equal && name == null) {
					equal = other.name == null;
				} else if (equal) {
					equal = name.equals(other.name);
				}
			}
			return equal;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Returns all groups.
	 * 
	 * @return All groups.
	 */
	Set<UIDifferenceGroup> allGroups();

	/**
	 * Add groups.
	 * 
	 * @param groups
	 *            Groups we are to add.
	 */
	void addGroups(Set<UIDifferenceGroup> groups);

	/**
	 * Returns which group the difference element belongs to.
	 * 
	 * @param d
	 *            The difference element.
	 * @return The group
	 */
	UIDifferenceGroup belongsTo(DiffElement d);
}
