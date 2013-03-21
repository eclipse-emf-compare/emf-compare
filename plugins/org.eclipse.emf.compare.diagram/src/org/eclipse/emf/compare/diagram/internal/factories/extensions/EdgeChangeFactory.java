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
package org.eclipse.emf.compare.diagram.internal.factories.extensions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Factory of edge changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class EdgeChangeFactory extends NodeChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return EdgeChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#createExtension()
	 */
	@Override
	public DiagramDiff createExtension() {
		return ExtensionsFactory.eINSTANCE.createEdgeChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#setView(org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public EObject setView(DiagramDiff extension, Diff refiningDiff) {
		EObject view = super.setView(extension, refiningDiff);
		while (view != null && !(view instanceof Edge)) {
			view = view.eContainer();
		}
		extension.setView(view);
		return view;
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		super.setRefiningChanges(extension, extensionKind, refiningDiff);
		if (extensionKind == DifferenceKind.CHANGE) {
			extension.getRefinedBy().addAll(getAllDifferencesForChange(refiningDiff));
		}
	}

	/**
	 * Predicate to check that the given difference is the main unit difference for this macroscopic add or
	 * delete of edge.
	 * 
	 * @return The predicate.
	 */
	public static Predicate<? super Diff> isMainDiffForAddOrDeleteEdge() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return difference instanceof ReferenceChange
						&& (isRelatedToAnAddEdge((ReferenceChange)difference) || isRelatedToADeleteEdge((ReferenceChange)difference));
			}
		};
	}

	/**
	 * Get all differences which are part of a change extension, from the given difference (being part of the
	 * result).
	 * 
	 * @param input
	 *            The given difference.
	 * @return The found differences.
	 */
	@Override
	protected Collection<Diff> getAllDifferencesForChange(Diff input) {
		final Match match = input.getMatch();
		final Comparison comparison = match.getComparison();

		EObject objectContainingDiff = MatchUtil.getContainer(comparison, input);
		Match edgeMatch = getEdgeMatch(comparison, objectContainingDiff);

		Iterable<Diff> diffs = Iterables.filter(edgeMatch.getAllDifferences(), new Predicate<Diff>() {

			public boolean apply(Diff diff) {
				return getRelatedExtensionKind(diff) == DifferenceKind.CHANGE;
			}

		});

		Collection<Diff> result = new ArrayList<Diff>();
		Iterator<Diff> it = diffs.iterator();
		while (it.hasNext()) {
			Diff diff = it.next();
			result.add(diff);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return isRelatedToAnAddEdge(input);
	}

	/**
	 * It checks that the given reference change concerns the add of an edge.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the add of an edge, False otherwise.
	 */
	protected static boolean isRelatedToAnAddEdge(ReferenceChange input) {
		return isContainmentOnSemanticEdge(input) && input.getKind() == DifferenceKind.ADD;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return isRelatedToADeleteEdge(input);
	}

	/**
	 * It checks that the given reference change concerns the delete of an edge.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the delete of an edge, False otherwise.
	 */
	protected static boolean isRelatedToADeleteEdge(ReferenceChange input) {
		return isContainmentOnSemanticEdge(input) && input.getKind() == DifferenceKind.DELETE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return (input.getAttribute().eContainer().equals(NotationPackage.eINSTANCE.getRelativeBendpoints()) || input
				.getAttribute().equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id()))
				&& input.getRefines().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return input.getValue() instanceof IdentityAnchor && input.getReference().isContainment()
				&& input.getRefines().isEmpty() && !isLeadedByAddOrDeleteEdge(input);
	}

	/**
	 * Get the match of the edge containing the given object (<code>subObject</code>).
	 * 
	 * @param comparison
	 *            The comparison
	 * @param subObject
	 *            The object contained in the current edge.
	 * @return The match of the edge.
	 */
	private Match getEdgeMatch(Comparison comparison, EObject subObject) {
		EObject object = subObject;
		while (!(object instanceof Edge) && object != null) {
			object = object.eContainer();
		}
		return comparison.getMatch(object);
	}

	/**
	 * It checks that the given reference change is leaded by an ADD or DELETE of the same object.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it is leaded by an add or delete, False otherwise.
	 */
	private boolean isLeadedByAddOrDeleteEdge(ReferenceChange input) {
		boolean result = false;
		EObject addedOrDeletedObject = input.getValue();
		EObject container = addedOrDeletedObject.eContainer();
		while (!(container instanceof Edge) && container != null) {
			container = container.eContainer();
		}
		if (container instanceof Edge) {
			result = Iterators.any(input.getMatch().getComparison().getDifferences(container).iterator(),
					isMainDiffForAddOrDeleteEdge());
		}
		return result;
	}

	/**
	 * It checks that the given difference is on a containment link to an Edge attached to a semantic object.
	 * 
	 * @param input
	 *            The difference.
	 * @return True if the difference matches with the predicate.
	 */
	private static boolean isContainmentOnSemanticEdge(ReferenceChange input) {
		return input.getReference().isContainment() && input.getValue() instanceof Edge
				&& ReferenceUtil.safeEGet(input.getValue(), NotationPackage.Literals.VIEW__ELEMENT) != null;
	}

}
