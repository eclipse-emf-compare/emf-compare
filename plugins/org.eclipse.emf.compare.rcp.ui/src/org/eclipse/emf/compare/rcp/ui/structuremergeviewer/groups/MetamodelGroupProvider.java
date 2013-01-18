/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups;

import static com.google.common.base.Predicates.alwaysTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * This implementation of a {@link IDifferenceGroupProvider} will be used to group the differences by their
 * metamodel element : all diffs that apply to a Class, all diffs that apply on a reference...
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class MetamodelGroupProvider implements IDifferenceGroupProvider {

	/** A human-readable label for this group provider. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the group provider. */
	private boolean activeByDefault;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Iterable<? extends DifferenceGroup> getGroups(Comparison comparison) {
		final List<Diff> diffs = comparison.getDifferences();

		final Map<EClass, List<Diff>> diffByEClass = Maps.newLinkedHashMap();
		for (Diff candidate : diffs) {
			final EClass target;
			if (candidate instanceof ReferenceChange) {
				if (((ReferenceChange)candidate).getReference().isContainment()) {
					final EObject parentMatch = candidate.getMatch().eContainer();
					if (parentMatch instanceof Match) {
						target = findEClass((Match)parentMatch);
					} else {
						target = findEClass(candidate.getMatch());
					}
				} else {
					target = findEClass(candidate.getMatch());
				}
			} else if (candidate instanceof AttributeChange) {
				target = findEClass(candidate.getMatch());
			} else {
				// Ignore this possibility for now.
				continue;
			}

			List<Diff> diffsForEClass = diffByEClass.get(target);
			if (diffsForEClass == null) {
				diffsForEClass = Lists.newArrayList();
				diffByEClass.put(target, diffsForEClass);
			}
			diffsForEClass.add(candidate);
		}

		final List<DifferenceGroup> groups = Lists.newArrayList();
		for (Map.Entry<EClass, List<Diff>> entry : diffByEClass.entrySet()) {
			groups.add(new DefaultDifferenceGroup(comparison, entry.getValue(), alwaysTrue(), entry.getKey()
					.getName()));
		}

		return groups;
	}

	/**
	 * Returns the appropriate {@link EClass} associated with the given {@link Match}.
	 * 
	 * @param match
	 *            The given {@link Match}.
	 * @return the appropriate {@link EClass} associated with the given {@link Match}.
	 */
	private EClass findEClass(Match match) {
		final EClass eClass;
		if (match.getOrigin() != null) {
			eClass = match.getOrigin().eClass();
		} else if (match.getRight() != null) {
			eClass = match.getRight().eClass();
		} else {
			/*
			 * All three sides null means that something went awry. Might as well throw the exception from
			 * here.
			 */
			eClass = match.getLeft().eClass();
		}
		return eClass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}
}
