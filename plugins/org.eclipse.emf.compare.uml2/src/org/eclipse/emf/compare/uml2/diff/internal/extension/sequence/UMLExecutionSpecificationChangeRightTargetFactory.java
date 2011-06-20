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

public class UMLExecutionSpecificationChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLExecutionSpecificationChangeRightTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof ExecutionSpecification;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;
		final ExecutionSpecification actionExecutionSpecification = (ExecutionSpecification)changeRightTarget
				.getRightElement();

		UMLExecutionSpecificationChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLExecutionSpecificationChangeRightTarget();

		hideOccurenceSpecification(actionExecutionSpecification.getStart(), ret);
		hideOccurenceSpecification(actionExecutionSpecification.getFinish(), ret);

		hideCrossReferences(actionExecutionSpecification,
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, coveredByPredicate);
		hideCrossReferences(actionExecutionSpecification.getStart(),
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, coveredByPredicate);
		hideCrossReferences(actionExecutionSpecification.getFinish(),
				DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, ret, coveredByPredicate);

		ret.getHideElements().add(changeRightTarget);

		ret.setRemote(changeRightTarget.isRemote());
		ret.setRightElement(changeRightTarget.getRightElement());
		ret.setLeftParent(changeRightTarget.getLeftParent());

		return ret;
	}

	private static final UMLPredicate<Setting> coveredByPredicate = new UMLPredicate<Setting>() {
		public boolean apply(Setting input) {
			return ((ReferenceChangeRightTarget)input.getEObject()).getReference() == UMLPackage.Literals.LIFELINE__COVERED_BY;
		}
	};

	private void hideOccurenceSpecification(OccurrenceSpecification occurrenceSpecification,
			UMLExecutionSpecificationChangeRightTarget ret) {
		hideCrossReferences(occurrenceSpecification,
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, ret);
		hideCrossReferences(occurrenceSpecification.getEvent(),
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, ret);
	}

}
