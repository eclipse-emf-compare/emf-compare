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
package org.eclipse.emf.compare.uml2.diff.internal.extension.sequence;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLExecutionSpecificationChangeRightTarget.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLExecutionSpecificationChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	/**
	 * The predicate to hide difference elements.
	 */
	private static final UMLPredicate<Setting> COVERED_BY_PREDICATE = new UMLPredicate<Setting>() {
		public boolean apply(Setting input) {
			return ((ReferenceChangeRightTarget)input.getEObject()).getReference() == UMLPackage.Literals.LIFELINE__COVERED_BY;
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLExecutionSpecificationChangeRightTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof ExecutionSpecification;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		final ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;
		final ExecutionSpecification actionExecutionSpecification = (ExecutionSpecification)changeRightTarget
				.getRightElement();

		final UMLExecutionSpecificationChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLExecutionSpecificationChangeRightTarget();

		hideOccurenceSpecification(actionExecutionSpecification.getStart(), ret, crossReferencer);
		hideOccurenceSpecification(actionExecutionSpecification.getFinish(), ret, crossReferencer);

		hideCrossReferences(actionExecutionSpecification,
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, COVERED_BY_PREDICATE,
				crossReferencer);
		hideCrossReferences(actionExecutionSpecification.getStart(),
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, COVERED_BY_PREDICATE,
				crossReferencer);
		hideCrossReferences(actionExecutionSpecification.getFinish(),
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, COVERED_BY_PREDICATE,
				crossReferencer);

		ret.getHideElements().add(changeRightTarget);
		ret.getRequires().add(changeRightTarget);

		ret.setRemote(changeRightTarget.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}

	/**
	 * Hide occurrence specifications.
	 * 
	 * @param occurrenceSpecification
	 *            {@link OccurrenceSpecification}
	 * @param ret
	 *            {@link UMLExecutionSpecificationChangeRightTarget}
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	private void hideOccurenceSpecification(OccurrenceSpecification occurrenceSpecification,
			UMLExecutionSpecificationChangeRightTarget ret, EcoreUtil.CrossReferencer crossReferencer) {
		hideCrossReferences(occurrenceSpecification,
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, ret, crossReferencer);
	}

}
