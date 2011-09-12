/*******************************************************************************
 * Copyright (c) 2011 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLExecutionSpecificationChangeLeftTargetFactory extends AbstractDiffExtensionFactory {

	public UMLExecutionSpecificationChangeLeftTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeLeftTarget
				&& ((ModelElementChangeLeftTarget)input).getLeftElement() instanceof ExecutionSpecification;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeLeftTarget changeLeftTarget = (ModelElementChangeLeftTarget)input;
		final ExecutionSpecification actionExecutionSpecification = (ExecutionSpecification)changeLeftTarget
				.getLeftElement();

		UMLExecutionSpecificationChangeLeftTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLExecutionSpecificationChangeLeftTarget();

		hideOccurenceSpecification(actionExecutionSpecification.getStart(), ret, crossReferencer);
		hideOccurenceSpecification(actionExecutionSpecification.getFinish(), ret, crossReferencer);

		hideCrossReferences(actionExecutionSpecification,
				DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET, ret, coveredByPredicate,
				crossReferencer);
		hideCrossReferences(actionExecutionSpecification.getStart(),
				DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET, ret, coveredByPredicate,
				crossReferencer);
		hideCrossReferences(actionExecutionSpecification.getFinish(),
				DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET, ret, coveredByPredicate,
				crossReferencer);

		ret.getHideElements().add(changeLeftTarget);
		ret.getRequires().add(changeLeftTarget);

		ret.setRemote(changeLeftTarget.isRemote());
		ret.setRightParent(changeLeftTarget.getRightParent());
		ret.setLeftElement(changeLeftTarget.getLeftElement());

		return ret;
	}

	private static final UMLPredicate<Setting> coveredByPredicate = new UMLPredicate<Setting>() {
		public boolean apply(Setting input) {
			return ((ReferenceChangeLeftTarget)input.getEObject()).getReference() == UMLPackage.Literals.LIFELINE__COVERED_BY;
		}
	};

	private void hideOccurenceSpecification(OccurrenceSpecification occurrenceSpecification,
			UMLExecutionSpecificationChangeLeftTarget ret, EcoreUtil.CrossReferencer crossReferencer) {
		hideCrossReferences(occurrenceSpecification,
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT, ret, crossReferencer);
		hideCrossReferences(occurrenceSpecification.getEvent(),
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT, ret, crossReferencer);
	}
}
