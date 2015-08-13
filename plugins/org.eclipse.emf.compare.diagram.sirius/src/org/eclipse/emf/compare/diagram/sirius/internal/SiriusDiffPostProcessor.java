/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.sirius.internal;

import static com.google.common.collect.Collections2.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

/**
 * A post processor to refine the differences found on a Sirius model.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("restriction")
public class SiriusDiffPostProcessor implements IPostProcessor {
	/**
	 * Create a new predicate to check whether the value of a {@link ReferenceChange} is of a given type.
	 * 
	 * @param clazz
	 *            an EClass.
	 * @return true if the value of the {@link ReferenceChange} is of the given type.
	 */
	private static Predicate<ReferenceChange> valueIsKindOf(final EClass clazz) {
		return new Predicate<ReferenceChange>() {
			public boolean apply(ReferenceChange diff) {
				return clazz.isSuperTypeOf(diff.getValue().eClass());
			}
		};
	}

	private static Function<? super ReferenceChange, EObject> getReferenceChangeValue() {
		return new Function<ReferenceChange, EObject>() {
			public EObject apply(ReferenceChange diff) {
				return diff.getValue();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		/*
		 * Any added/deleted DSemanticDecorator must require the corresponding getTarget() addition/removal
		 */
		Iterable<ReferenceChange> allContainmentRefChanges = Iterables.filter(Iterables.filter(comparison
				.getDifferences(), ReferenceChange.class), CONTAINMENT_REFERENCE_CHANGE);

		Iterable<ReferenceChange> addedOrRemovedSemanticDecorators = Iterables.filter(
				allContainmentRefChanges, valueIsKindOf(ViewpointPackage.eINSTANCE.getDSemanticDecorator()));

		Multimap<EObject, ReferenceChange> diffsByValue = Multimaps.index(allContainmentRefChanges,
				getReferenceChangeValue());

		/*
		 * Any added or removed semantic decorator should have a dependency to the corresponding
		 * addition/removal of semantic element.
		 */
		for (ReferenceChange referenceChange : addedOrRemovedSemanticDecorators) {
			DSemanticDecorator value = (DSemanticDecorator)referenceChange.getValue();
			if (value.getTarget() != null) {
				EObject semanticTarget = value.getTarget();
				addRequiresToDecoratedElement(diffsByValue, referenceChange, semanticTarget);
			}
			/*
			 * A DNode should always have its actualMapping reference set.
			 */
			if (value instanceof DNode) {
				NodeMapping map = ((DNode)value).getActualMapping();
				if (map != null) {
					for (ReferenceChange actualMappingChange : Iterables.filter(comparison
							.getDifferences(map), ReferenceChange.class)) {
						if (actualMappingChange.getReference() == DiagramPackage.eINSTANCE
								.getDNode_ActualMapping()
								&& fromSide(referenceChange.getSource()).apply(actualMappingChange)) {
							referenceChange.getRequires().add(actualMappingChange);
						}
					}
				}
			}
		}
		/*
		 * Any added or removed gmf Node should have a dependency to the corresponding addition/removal of a
		 * DSemantic decorator.
		 */

		Iterable<ReferenceChange> addedOrRemovedGmfView = Iterables.filter(allContainmentRefChanges,
				valueIsKindOf(NotationPackage.eINSTANCE.getView()));

		for (ReferenceChange referenceChange : addedOrRemovedGmfView) {
			View value = (View)referenceChange.getValue();

			/*
			 * beware here, GMF do som trick in getElement() and will return it's container element if the
			 * element is null..
			 */
			if (value.getElement() != null) {
				EObject semanticTarget = value.getElement();
				addRequiresToDecoratedElement(diffsByValue, referenceChange, semanticTarget);
			}
		}
	}

	/**
	 * Add diff requires for every change related to the semantic target.
	 * 
	 * @param diffsByValue
	 *            {@link ReferenceChange} differences indexed by value.
	 * @param referenceChange
	 *            a given {@link ReferenceChange}.
	 * @param semanticTarget
	 *            the semantic target.
	 */
	private void addRequiresToDecoratedElement(Multimap<EObject, ReferenceChange> diffsByValue,
			ReferenceChange referenceChange, EObject semanticTarget) {
		for (Diff valueDiff : diffsByValue.get(semanticTarget)) {
			if (referenceChange.getKind() == DifferenceKind.ADD) {
				if (valueDiff.getKind() == DifferenceKind.ADD
						&& fromSide(referenceChange.getSource()).apply(valueDiff)) {
					referenceChange.getRequires().add(valueDiff);
				}

			} else if (referenceChange.getKind() == DifferenceKind.DELETE) {
				if (valueDiff.getKind() == DifferenceKind.DELETE
						&& fromSide(referenceChange.getSource()).apply(valueDiff)) {
					referenceChange.getRequires().add(valueDiff);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		/*
		 * We re-refine the refinements already setup by
		 * org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor
		 */
		Iterator<DiagramDiff> it = Iterators.filter(comparison.eAllContents(), DiagramDiff.class);
		while (it.hasNext()) {
			DiagramDiff next = it.next();
			Set<Diff> refinesToAdd = Sets.newLinkedHashSet();
			for (Diff refined : next.getRefinedBy()) {
				collectDifferenceRefines(comparison, refinesToAdd, refined.getMatch().getLeft());
				collectDifferenceRefines(comparison, refinesToAdd, refined.getMatch().getRight());
			}
			next.getRefinedBy().addAll(filter(refinesToAdd, fromSide(next.getSource())));
		}
	}

	/**
	 * Collect the differences which have to be added as a refinment of the current DiagramDiff.
	 * 
	 * @param comparison
	 *            the current comparison.
	 * @param refinesToAdd
	 *            the set of differences to add as refinement.
	 * @param changedEObject
	 *            any changed EObject.
	 */
	private void collectDifferenceRefines(Comparison comparison, Set<Diff> refinesToAdd,
			EObject changedEObject) {
		if (changedEObject instanceof View) {
			View gmfView = (View)changedEObject;
			if (gmfView.getElement() instanceof DSemanticDecorator) {
				for (Diff diffOnSemanticDecorator : comparison.getDifferences(gmfView.getElement())) {
					refinesToAdd.add(diffOnSemanticDecorator);
				}
			}
		}
	}

}
