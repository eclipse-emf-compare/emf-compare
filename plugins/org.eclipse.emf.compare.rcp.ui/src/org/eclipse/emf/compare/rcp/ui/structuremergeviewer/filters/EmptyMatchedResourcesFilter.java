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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;

/**
 * A filter used by default that filtered out matched elements.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class EmptyMatchedResourcesFilter implements IDifferenceFilter {

	/** A human-readable label for this filter. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the filter. */
	private boolean activeByDefault;

	/** The Predicate activate through this action. */
	private Predicate<? super EObject> predicate;

	/**
	 * Constructs the filter with the appropriate predicate.
	 */
	public EmptyMatchedResourcesFilter() {
		super();
		setPredicate();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#isEnabled(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getPredicate()
	 */
	public Predicate<? super EObject> getPredicate() {
		return predicate;
	}

	/**
	 * Set the predicate that will be activate through this filter.
	 */
	private void setPredicate() {
		final Predicate<? super EObject> actualPredicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				if (input instanceof MatchResource) {
					EList<Diff> differences = ((MatchResource)input).getComparison().getDifferences();
					Iterable<ResourceAttachmentChange> resourceAttachmentchanges = filter(differences,
							ResourceAttachmentChange.class);
					if (!isEmpty(resourceAttachmentchanges)) {
						for (ResourceAttachmentChange rac : resourceAttachmentchanges) {
							final String diffResourceURI = rac.getResourceURI();
							if (!diffResourceURI.equals(((MatchResource)input).getLeftURI())
									&& !diffResourceURI.equals(((MatchResource)input).getRightURI())
									&& !diffResourceURI.equals(((MatchResource)input).getOriginURI())) {
								return true;
							}
						}
					} else {
						return true;
					}
				}
				return false;
			}
		};
		predicate = actualPredicate;
	}

}
