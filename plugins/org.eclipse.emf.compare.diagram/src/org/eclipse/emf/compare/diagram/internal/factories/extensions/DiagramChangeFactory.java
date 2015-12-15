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
import com.google.common.collect.Collections2;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.diagram.internal.factories.AbstractDiagramChangeFactory;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Factory of diagram changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramChangeFactory extends AbstractDiagramChangeFactory {

	/**
	 * Constructor.
	 */
	public DiagramChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return DiagramChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public DiagramDiff createExtension() {
		return ExtensionsFactory.eINSTANCE.createDiagramChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#setRefiningChanges(org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		// Macroscopic change on a diagram is refined by the unit main change and all unit children related
		// changes.
		if (refiningDiff.getSource() == extension.getSource()) {
			extension.getRefinedBy().add(refiningDiff);
			extension.getRefinedBy().addAll(
					Collections2.filter(getAllContainedDifferences(refiningDiff), EMFComparePredicates
							.fromSide(extension.getSource())));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		// nothing
	}

	/**
	 * Predicate to check that the given difference is the main unit difference for this macroscopic add or
	 * delete of diagram.
	 * 
	 * @return The predicate.
	 */
	public static Predicate<Diff> isMainDiffForAddOrDeleteDiagram() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return difference instanceof ResourceAttachmentChange
						&& (isRelatedToAnAddDiagram((ResourceAttachmentChange)difference) || isRelatedToADeleteDiagram((ResourceAttachmentChange)difference));
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ResourceAttachmentChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return isRelatedToAnAddDiagram(input);
	}

	/**
	 * It checks that the given resource attachment change concerns the add of a diagram.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the add of a node, False otherwise.
	 */
	protected static boolean isRelatedToAnAddDiagram(ResourceAttachmentChange input) {
		return isContainmentOnSemanticDiagram(input) && input.getKind() == DifferenceKind.ADD;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ResourceAttachmentChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return isRelatedToADeleteDiagram(input);
	}

	/**
	 * It checks that the given resource attachment change concerns the delete of a diagram.
	 * 
	 * @param input
	 *            The reference change.
	 * @return True if it concerns the delete of a node, False otherwise.
	 */
	protected static boolean isRelatedToADeleteDiagram(ResourceAttachmentChange input) {
		return isContainmentOnSemanticDiagram(input) && input.getKind() == DifferenceKind.DELETE;
	}

	/**
	 * It checks that the given difference is on a containment link to a Diagram attached to a semantic
	 * object.
	 * 
	 * @param input
	 *            The difference.
	 * @return True if the difference matches with the predicate.
	 */
	private static boolean isContainmentOnSemanticDiagram(ResourceAttachmentChange input) {
		EObject value = MatchUtil.getContainer(input.getMatch().getComparison(), input);
		return value instanceof Diagram
				&& ReferenceUtil.safeEGet(value, NotationPackage.Literals.VIEW__ELEMENT) != null;
	}

}
