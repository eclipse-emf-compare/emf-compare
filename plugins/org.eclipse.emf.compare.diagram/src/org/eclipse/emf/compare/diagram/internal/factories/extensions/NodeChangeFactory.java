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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.diagram.internal.factories.AbstractDiagramChangeFactory;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory of node changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class NodeChangeFactory extends AbstractDiagramChangeFactory {

	/**
	 * Constructor.
	 */
	public NodeChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return NodeChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public DiagramDiff createExtension() {
		return ExtensionsFactory.eINSTANCE.createNodeChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#setRefiningChanges(org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		// Macroscopic change on a node is refined by the unit main change and unit children related changes.
		extension.getRefinedBy().add(refiningDiff);
		// if (extensionKind != DifferenceKind.MOVE) {
		extension.getRefinedBy().addAll(getAllContainedDifferences(refiningDiff));
		// }
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		super.fillRequiredDifferences(comparison, extension);
		fillRequiredDifferencesForMove(comparison, extension);
	}

	/**
	 * MOVE case: The MOVE of a Node requires the MOVE of the semantic object.
	 * 
	 * @see NodeChangeFactory#fillRequiredDifferences(Comparison, Diff).
	 * @param comparison
	 *            the comparison.
	 * @param extension
	 *            the node change.
	 */
	private void fillRequiredDifferencesForMove(Comparison comparison, Diff extension) {
		Set<Diff> requiredExtensions = new HashSet<Diff>();
		Set<Diff> requiringExtensions = new HashSet<Diff>();
		final Predicate<Diff> moveReference = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.MOVE));
		Collection<Diff> refiningMoves = Collections2.filter(extension.getRefinedBy(), moveReference);
		for (Diff diff : refiningMoves) {
			EObject target = ((ReferenceChange)diff).getValue();
			if (target instanceof View) {
				EObject semanticTarget = ((View)target).getElement();
				Collection<Diff> requiredDiffs = Collections2.filter(comparison
						.getDifferences(semanticTarget), moveReference);
				requiredExtensions.addAll(requiredDiffs);
				// The graphical object and the semantic one are linked, they change their container both
				// (difference case of ADD/DELETE)
				requiringExtensions.addAll(requiredDiffs);
			}
		}
		extension.getRequires().addAll(requiredExtensions);
		extension.getRequiredBy().addAll(requiringExtensions);
	}

	/**
	 * Predicate to check that the given difference is the main unit difference for this macroscopic add or
	 * delete of node.
	 * 
	 * @return The predicate.
	 */
	public static Predicate<Diff> isMainDiffForAddOrDeleteNode() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return difference instanceof ReferenceChange
						&& (isRelatedToAnAddNode((ReferenceChange)difference) || isRelatedToADeleteNode((ReferenceChange)difference));
			}
		};
	}

	/**
	 * Predicate to check that the given difference is the main unit difference for this macroscopic move of
	 * node.
	 * 
	 * @return The predicate.
	 */
	public static Predicate<Diff> isMainDiffForMoveNode() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return difference instanceof ReferenceChange
						&& isRelatedToAMoveNode((ReferenceChange)difference);
			}
		};
	}

	/**
	 * Get all the changes for the object containing them, from one of them: the given one.
	 * 
	 * @param input
	 *            one of the changes to get.
	 * @return The list of the related changes.
	 */
	protected Collection<Diff> getAllDifferencesForChange(Diff input) {
		return input.getMatch().getDifferences();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return isExclusive() && isRelatedToAnAddNode(input);
	}

	/**
	 * It checks that the given reference change concerns the add of a node.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the add of a node, False otherwise.
	 */
	protected static boolean isRelatedToAnAddNode(ReferenceChange input) {
		return isContainmentOnSemanticNode(input) && input.getKind() == DifferenceKind.ADD;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return isExclusive() && isRelatedToADeleteNode(input);
	}

	/**
	 * It checks that the given reference change concerns the delete of a node.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the delete of a node, False otherwise.
	 */
	protected static boolean isRelatedToADeleteNode(ReferenceChange input) {
		return isContainmentOnSemanticNode(input) && input.getKind() == DifferenceKind.DELETE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return isExclusive() && isRelatedToAMoveNode(input);
	}

	/**
	 * It checks that the given reference change concerns the move of a node.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the move of a node, False otherwise.
	 */
	protected static boolean isRelatedToAMoveNode(ReferenceChange input) {
		return isContainmentOnSemanticNode(input) && input.getKind() == DifferenceKind.MOVE;
	}

	/**
	 * It checks that the predicate applies on only this factory and not on potential children factories.
	 * 
	 * @return true if the predicate applies only on this factory.
	 */
	protected boolean isExclusive() {
		return getExtensionKind() == NodeChange.class;
	}

	/**
	 * It checks that the given difference is on a containment link to a Node attached to a semantic object.
	 * 
	 * @param input
	 *            The difference.
	 * @return True if the difference matches with the predicate.
	 */
	private static boolean isContainmentOnSemanticNode(ReferenceChange input) {
		return input.getReference().isContainment() && input.getValue() instanceof Node
				&& ReferenceUtil.safeEGet(input.getValue(), NotationPackage.Literals.VIEW__ELEMENT) != null;
	}

}
