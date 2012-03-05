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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDiff;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A facility to group difference elements per kind of changes.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public class EClassGrouping implements IDifferenceGroupingFacility {
	/** This will be used as the default difference group. */
	private static final UIDifferenceGroup UI_DIFFERENCE_GROUP_DEFAULT = new UIDifferenceGroup(
			"default.other", "Others", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** Registered groups. */
	private Map<Class<?>, UIDifferenceGroup> mGroups = new HashMap<Class<?>, UIDifferenceGroup>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#allGroups()
	 */
	public Set<UIDifferenceGroup> allGroups() {
		return new HashSet<UIDifferenceGroup>(mGroups.values());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#addGroups(java.util.Set)
	 */
	public void addGroups(Set<UIDifferenceGroup> groups) {
		// do nothing, this implementation dynamically creates its groups
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility#belongsTo(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public UIDifferenceGroup belongsTo(DiffElement d) {
		UIDifferenceGroup group = UI_DIFFERENCE_GROUP_DEFAULT;
		if (d instanceof AttributeChange) {
			final AttributeChange attributeChange = (AttributeChange)d;
			final EObject leftElement = attributeChange.getLeftElement();
			if (leftElement == null) {
				final EObject rightElement = attributeChange.getRightElement();
				if (rightElement != null) {
					group = getFromEClass(rightElement.eClass().getInstanceClass());
				}
			} else {
				group = getFromEClass(leftElement.eClass().getInstanceClass());
			}
		} else if (d instanceof ModelElementChangeLeftTarget) {
			final ModelElementChangeLeftTarget attributeChange = (ModelElementChangeLeftTarget)d;
			final EObject leftElement = attributeChange.getLeftElement();
			if (leftElement == null) {
				final EObject rightParent = attributeChange.getRightParent();
				if (rightParent != null) {
					group = getFromEClass(rightParent.eClass().getInstanceClass());
				}
			} else {
				group = getFromEClass(leftElement.eClass().getInstanceClass());
			}
		} else if (d instanceof ModelElementChangeRightTarget) {
			final ModelElementChangeRightTarget attributeChange = (ModelElementChangeRightTarget)d;
			final EObject rightElement = attributeChange.getRightElement();
			if (rightElement == null) {
				final EObject leftParent = attributeChange.getLeftParent();
				if (leftParent != null) {
					group = getFromEClass(leftParent.eClass().getInstanceClass());
				}
			} else {
				group = getFromEClass(rightElement.eClass().getInstanceClass());
			}
		} else if (d instanceof UpdateModelElement) {
			final UpdateModelElement updateModelElement = (UpdateModelElement)d;
			final EObject leftElement = updateModelElement.getLeftElement();
			if (leftElement == null) {
				final EObject rightElement = updateModelElement.getRightElement();
				if (rightElement != null) {
					group = getFromEClass(rightElement.eClass().getInstanceClass());
				}
			} else {
				group = getFromEClass(leftElement.eClass().getInstanceClass());
			}
		} else if (d instanceof ReferenceChange) {
			final ReferenceChange referenceChange = (ReferenceChange)d;
			final EObject leftElement = referenceChange.getLeftElement();
			if (leftElement == null) {
				final EObject rightElement = referenceChange.getRightElement();
				if (rightElement != null) {
					group = getFromEClass(rightElement.eClass().getInstanceClass());
				}
			} else {
				group = getFromEClass(leftElement.eClass().getInstanceClass());
			}
		} else if (d instanceof ResourceDiff) {
			group = getFromEClass(Resource.class);
		}
		return group;
	}

	/**
	 * Returns the {@link UIDifferenceGroup} corresponding to the given eClass.
	 * 
	 * @param eClass
	 *            The EClass we need a difference group for.
	 * @return The {@link UIDifferenceGroup} corresponding to the given eClass.
	 */
	private UIDifferenceGroup getFromEClass(Class<?> eClass) {
		UIDifferenceGroup ret;
		if (mGroups.containsKey(eClass)) {
			ret = mGroups.get(eClass);
		} else {
			ret = new UIDifferenceGroup(eClass.getName(), eClass.getSimpleName(), ""); //$NON-NLS-1$
			mGroups.put(eClass, ret);
		}
		return ret;
	}
}
