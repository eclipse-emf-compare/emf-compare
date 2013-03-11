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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.ecore.EObject;

/**
 * A filter used by default that filtered out matched elements.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class EmptyMatchedResourcesFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> predicateWhenSelected = new Predicate<EObject>() {
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return predicateWhenSelected;
	}

}
