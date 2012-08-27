/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.filter;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * These will be the actual actions displayed in the filter menu. Their sole purpose is to provide a Predicate
 * to the structure viewer's filter.
 * <p>
 * Do note that each distinct {@link FilterAction} in the {@link FilterActionMenu filter menu} is considered
 * as an "exclude" filter, and that they are OR'ed together (thus, any element must <b>not</b> meet the
 * selected filters' criteria in order to be displayed).
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FilterAction extends Action {
	/** The Predicate activate through this action. */
	private Predicate<? super EObject> predicate;

	/** The Filter that will be modified by the action. */
	private DifferenceFilter differenceFilter;

	/**
	 * The "default" constructor for this action.
	 * {@link #fromDiffPredicate(String, Predicate, DifferenceFilter)} can be used instead in order to create
	 * an action which filter only aims at Diff elements.
	 * 
	 * @param text
	 *            Will be used as the action's tooltip.
	 * @param predicate
	 *            The predicate that this action will provide to the viewer's filter.
	 * @param differenceFilter
	 *            The filter that this action will need to update.
	 */
	public FilterAction(String text, Predicate<? super EObject> predicate, DifferenceFilter differenceFilter) {
		super(text, IAction.AS_CHECK_BOX);
		this.predicate = predicate;
		this.differenceFilter = differenceFilter;
	}

	/**
	 * Constructs a filtering action that only aims at filtering out Diff elements from the view, ignoring all
	 * other kind of objects.
	 * 
	 * @param text
	 *            The tooltip of the new action.
	 * @param diffPredicate
	 *            The predicate that this action will provide to the viewer's filter.
	 * @param filter
	 *            The filter that this action will need to update.
	 * @return The newly created action.
	 */
	public static FilterAction fromDiffPredicate(String text, final Predicate<? super Diff> diffPredicate,
			DifferenceFilter filter) {
		final Predicate<? super EObject> actualPredicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input instanceof Diff && diffPredicate.apply((Diff)input);
			}
		};
		return new FilterAction(text, actualPredicate, filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (isChecked()) {
			differenceFilter.addPredicate(predicate);
		} else {
			differenceFilter.removePredicate(predicate);
		}
	}
}
