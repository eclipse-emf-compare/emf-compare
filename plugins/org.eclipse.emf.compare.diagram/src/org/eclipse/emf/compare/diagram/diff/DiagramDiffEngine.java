/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diagram.GMFCompare;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiffExtension;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.compare.diagram.diff.internal.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.diagram.diff.internal.IDiffExtensionFactory;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.provider.IViewLabelProvider;
import org.eclipse.emf.compare.diagram.provider.internal.ViewLabelProviderExtensionRegistry;
import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.engine.IMatchManager;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.engine.check.AttributesCheck;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Diff Engine for Diagram comparison.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class DiagramDiffEngine extends GenericDiffEngine {

	/**
	 * Factories for the creation of the difference extensions.
	 */
	private Set<IDiffExtensionFactory> diagramExtensionFactories;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#doDiffThreeWay(org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	@Override
	protected DiffGroup doDiffThreeWay(MatchModel match) {
		final DiffGroup ret = super.doDiffThreeWay(match);
		postProcess(ret, match);
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#doDiffTwoWay(org.eclipse.emf.compare.match.metamodel.MatchModel)
	 */
	@Override
	protected DiffGroup doDiffTwoWay(MatchModel match) {
		final DiffGroup ret = super.doDiffTwoWay(match);
		postProcess(ret, match);
		return ret;
	}

	/**
	 * Post-processing of the diff model.
	 * 
	 * @param dg
	 *            The DiffGroup to process.
	 * @param matchModel
	 *            The match model.
	 */
	protected void postProcess(DiffGroup dg, MatchModel matchModel) {
		EcoreUtil.CrossReferencer diffModelCrossReferencer = new EcoreUtil.CrossReferencer(dg) {
			private static final long serialVersionUID = -7188045763674814697L;
			{
				crossReference(); // init map
			}
		};
		diagramExtensionFactories = DiffExtensionFactoryRegistry.createExtensionFactories(
				diffModelCrossReferencer, matchModel);
		// CHECKSTYLE:OFF
		for (final TreeIterator<EObject> tit = dg.eAllContents(); tit.hasNext();) {
			// CHECKSTYLE:ON
			final EObject next = tit.next();
			if (next instanceof DiffElement) {
				applyManagedTypes((DiffElement)next, dg);
			}
		}
	}

	/**
	 * Manages the specified difference to instantiate extensions and to complete the diff model.
	 * 
	 * @param element
	 *            The difference to manage.
	 * @param root
	 *            The root of the DiffModel.
	 */
	void applyManagedTypes(DiffElement element, DiffGroup root) {
		for (IDiffExtensionFactory factory : diagramExtensionFactories) {
			if (factory.handles(element, root)) {

				final AbstractDiffExtension ext = factory.create(element);

				if (ext instanceof DiagramDiffExtension) {
					final BusinessDiffExtension extension = (BusinessDiffExtension)ext;

					final DiffElement diffParent = factory.getParentDiff(element);

					if (containsConflictingElement(extension.getHideElements())) {
						final ConflictingDiffElement conflict = DiffFactory.eINSTANCE
								.createConflictingDiffElement();

						final EObject origin = getMatchManager().getMatchedEObject(extension.getElement(),
								MatchSide.ANCESTOR);
						conflict.setOriginElement(origin);
						conflict.setLeftParent(getMatchManager().getMatchedEObject(
								extension.getElement().eContainer(), MatchSide.LEFT));
						conflict.setRightParent(getMatchManager().getMatchedEObject(
								extension.getElement().eContainer(), MatchSide.RIGHT));

						conflict.getSubDiffElements().add(extension);
						diffParent.getSubDiffElements().add(conflict);
					} else {
						diffParent.getSubDiffElements().add(extension);
					}

				}

			}
		}
	}

	/**
	 * Check if the list of differences contains one conflicting element at least or differences with
	 * different values of the remote parameter.
	 * 
	 * @param diffs
	 *            The list of the differences.
	 * @return True if this set is conflicting.
	 */
	private boolean containsConflictingElement(List<DiffElement> diffs) {
		int permutationCounter = 0;
		boolean remote = false;
		boolean result = false;
		for (DiffElement diffElement : diffs) {
			if (diffElement.isConflicting()) {
				result = true;
				break;
			} else if (remote != diffElement.isRemote()) {
				permutationCounter++;
				if (permutationCounter == 2) {
					result = true;
					break;
				}
			}
			remote = diffElement.isRemote();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.GenericDiffEngine#getAttributesChecker()
	 */
	@Override
	protected AttributesCheck getAttributesChecker() {
		return new DiagramAttributesCheck(getMatchManager());
	}

	/**
	 * Class which checks the label of GMF views during the attributes checks.
	 * 
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	private static final class DiagramAttributesCheck extends AttributesCheck {

		/**
		 * Constructor.
		 * 
		 * @param matchManager
		 *            The Match manager
		 */
		public DiagramAttributesCheck(IMatchManager matchManager) {
			super(matchManager);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.engine.check.AttributesCheck#checkAttributesUpdates(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
		 *      org.eclipse.emf.compare.match.metamodel.Match2Elements)
		 */
		@Override
		public void checkAttributesUpdates(DiffGroup root, Match2Elements mapping) throws FactoryException {
			super.checkAttributesUpdates(root, mapping);
			computeLabelChange(root, mapping.getLeftElement(), mapping.getRightElement());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.engine.check.AttributesCheck#checkAttributesUpdates(org.eclipse.emf.compare.diff.metamodel.DiffGroup,
		 *      org.eclipse.emf.compare.match.metamodel.Match3Elements)
		 */
		@Override
		public void checkAttributesUpdates(DiffGroup root, Match3Elements mapping) throws FactoryException {
			super.checkAttributesUpdates(root, mapping);

			final DiffGroup dummyGroup = DiffFactory.eINSTANCE.createDiffGroup();
			final DiagramLabelChange diagramLabelChange = computeLabelChange(dummyGroup,
					mapping.getLeftElement(), mapping.getRightElement());

			// Set the remote attribute and create a ConflictingDiffElement if it is required.
			if (diagramLabelChange instanceof BusinessDiagramLabelChange) {

				final EObject ancestor = mapping.getOriginElement();
				if (ancestor instanceof View) {
					final ITextAwareEditPart ancestorEp = DiffUtil.getTextEditPart((View)ancestor);
					if (ancestorEp != null) {
						final String ancestorLabel = ancestorEp.getEditText();
						final String leftLabel = ((BusinessDiagramLabelChange)diagramLabelChange)
								.getLeftLabel();
						final String rightLabel = ((BusinessDiagramLabelChange)diagramLabelChange)
								.getRightLabel();
						if (ancestorLabel.equals(leftLabel)) {
							diagramLabelChange.setRemote(true);
							root.getSubDiffElements().add(diagramLabelChange);
						} else if (!ancestorLabel.equals(rightLabel)) {
							final ConflictingDiffElement conflict = DiffFactory.eINSTANCE
									.createConflictingDiffElement();

							conflict.setOriginElement(mapping.getOriginElement());
							conflict.setLeftParent(mapping.getLeftElement());
							conflict.setRightParent(mapping.getRightElement());

							conflict.getSubDiffElements().add(diagramLabelChange);
							root.getSubDiffElements().add(conflict);
						} else {
							root.getSubDiffElements().add(diagramLabelChange);
						}
					}

				}

			}

		}

		/**
		 * Create a label change difference extension if needed.
		 * 
		 * @param root
		 *            The root of the DiffModel.
		 * @param leftElement
		 *            The left element.
		 * @param rightElement
		 *            The right element
		 * @return The DiagramLabelChange extension. e.
		 */
		private DiagramLabelChange computeLabelChange(DiffGroup root, EObject leftElement,
				EObject rightElement) {
			DiagramLabelChange diff = null;
			if (leftElement instanceof View) {
				final View view = (View)leftElement;
				final Diagram diagram = view.getDiagram();

				if (diagram != null) {
					final String diagramType = diagram.getType();

					IViewLabelProvider extensionForType = ViewLabelProviderExtensionRegistry.INSTANCE
							.getExtensionForType(diagramType);

					if (extensionForType == null) { // no extension registered for handling label in this
													// diagram,
						// use the default one
						GMFCompare
								.getDefault()
								.getLog()
								.log(new Status(IStatus.INFO, GMFCompare.PLUGIN_ID,
										"No IViewLabelProvider registered for diagram " + diagramType)); //$NON-NLS-1$
						extensionForType = IViewLabelProvider.DEFAULT_INSTANCE;
					}
					if (extensionForType.isManaged(view) && DiffUtil.isVisible(view)
							&& DiffUtil.isVisible((View)rightElement)) {
						final String leftLabel = extensionForType.elementLabel(view);
						final String rightLabel = extensionForType.elementLabel((View)rightElement);
						// CHECKSTYLE:OFF
						if (!leftLabel.equals(rightLabel)) {
							diff = createLabelChange(root, leftElement, rightElement, leftLabel, rightLabel);
						}
						// CHECKSTYLE:ON
					}
				}
			}
			return diff;
		}

		/**
		 * Create the label change difference.
		 * 
		 * @param root
		 *            The difference group on which the difference has to be created.
		 * @param leftElement
		 *            The left element.
		 * @param rightElement
		 *            The right element.
		 * @param leftLabel
		 *            The left label.
		 * @param rightLabel
		 *            The right label.
		 * @return The label change difference.
		 */
		private DiagramLabelChange createLabelChange(final DiffGroup root, final EObject leftElement,
				final EObject rightElement, final String leftLabel, final String rightLabel) {
			final DiagramLabelChange diff = DiagramdiffFactory.eINSTANCE.createDiagramLabelChange();
			diff.setLeftElement(leftElement);
			diff.setRightElement(rightElement);
			root.getSubDiffElements().add(diff);
			if (diff instanceof BusinessDiagramLabelChange) {
				((BusinessDiagramLabelChange)diff).setLeftLabel(leftLabel);
				((BusinessDiagramLabelChange)diff).setRightLabel(rightLabel);
			}
			return diff;
		}

	}

}
