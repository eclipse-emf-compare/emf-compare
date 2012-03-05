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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility.UIDifferenceGroup;

/**
 * The difference group descriptor represents a grouping facility contribution trough the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public final class DifferenceGroupingFacilityDescriptor {
	/** Name of the "id" property of the Difference group extension point. */
	public static final String ID_PROPERTY = "id"; //$NON-NLS-1$

	/** Name of the "name" property of the Difference group extension point. */
	public static final String NAME_PROPERTY = "name"; //$NON-NLS-1$

	/** Name of the "class" property of the Difference group extension point. */
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

	/** Name of the "icon" property of the Difference group extension point. */
	public static final String ICON_PROPERTY = "icon"; //$NON-NLS-1$

	/** Name of the "group" property of the Difference group extension point. */
	public static final String GROUP_SEQUENCE = "group"; //$NON-NLS-1$

	/** Value of the "id" attribute of this extension. */
	private String id;

	/** Value of the "name" attribute of this extension. */
	private String name;

	/** Value of the "class" attribute of this extension. */
	private String classname;

	/** The underlying configuration element. */
	private IConfigurationElement element;

	/** Instance of the facility this wrapped by this descriptor. */
	private IDifferenceGroupingFacility extension;

	/** List of the groups contributed by the underlying facility. */
	private Set<UIDifferenceGroup> groups = new HashSet<UIDifferenceGroup>();

	/**
	 * Instantiates a descriptor given its configuration element.
	 * 
	 * @param e
	 *            Configuration element from which to instantiate a descriptor.
	 */
	public DifferenceGroupingFacilityDescriptor(IConfigurationElement e) {
		element = e;
		id = e.getAttribute(ID_PROPERTY);
		name = e.getAttribute(NAME_PROPERTY);
		classname = e.getAttribute(CLASS_PROPERTY);

		for (IConfigurationElement g : e.getChildren(GROUP_SEQUENCE)) {
			addGroup(g);
		}

		checks(this);
	}

	/**
	 * Returns the ID of the filter.
	 * 
	 * @return the ID of the filter
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the classname of the implemented filter.
	 * 
	 * @return the classname of the implemented filter
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * Returns the name of the implemented filter (translatable).
	 * 
	 * @return the name of the implemented filter (translatable)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds the groups described by the given element to the list of contributions.
	 * 
	 * @param e
	 *            Configuration element from which to retrieve a group.
	 */
	void addGroup(IConfigurationElement e) {
		if (!GROUP_SEQUENCE.equals(e.getName())) {
			throw new IllegalArgumentException("Configuration element name must be " + GROUP_SEQUENCE); //$NON-NLS-1$
		}

		final String groupId = e.getAttribute(ID_PROPERTY);
		final String groupName = e.getAttribute(NAME_PROPERTY);
		final String groupIcon = e.getAttribute(ICON_PROPERTY);

		groups.add(new UIDifferenceGroup(groupId, groupName, groupIcon));
	}

	/**
	 * Returns the grouping facility described by this instance.
	 * 
	 * @return The grouping facility described by this instance.
	 */
	public IDifferenceGroupingFacility getExtension() {
		if (extension == null) {
			try {
				extension = (IDifferenceGroupingFacility)element.createExecutableExtension(CLASS_PROPERTY);
				extension.addGroups(groups);
			} catch (CoreException e) {
				EMFCompareUIPlugin.getDefault().getLog()
						.log(new Status(IStatus.ERROR, EMFCompareUIPlugin.PLUGIN_ID, e.getMessage(), e));
			}
		}
		return extension;
	}

	/**
	 * Validates the given descriptor.
	 * 
	 * @param d
	 *            Descriptor of which we need to check the validity.
	 */
	private static void checks(DifferenceGroupingFacilityDescriptor d) {
		if (d.element == null) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages
							.getString("DifferenceFilterDescriptor.nullConfigurationElementException")); //$NON-NLS-1$
		}

		final String missingPropertyKey = "DifferenceFilterDescriptor.missingMandatoryPropertyException"; //$NON-NLS-1$
		if (nullOrEmpty(d.getID())) {
			throw new IllegalArgumentException(
					EMFCompareUIMessages.getString(missingPropertyKey, ID_PROPERTY));
		}

		if (nullOrEmpty(d.getName())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					NAME_PROPERTY));
		}

		if (nullOrEmpty(d.getClassname())) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString(missingPropertyKey,
					CLASS_PROPERTY));
		}
	}

	/**
	 * Checks whether the given String is either <code>null</code> or empty.
	 * 
	 * @param s
	 *            String to consider.
	 * @return <code>true</code> if <em>s</em> is either <code>null</code> or empty, <code>false</code>
	 *         otherwise.
	 */
	private static boolean nullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
