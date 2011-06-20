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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLMessageChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLMessageChangeRightTargetFactory(UML2DiffEngine engine, CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)input).getRightElement() instanceof Message;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget changeRightTarget = (ModelElementChangeRightTarget)input;
		final Message message = (Message)changeRightTarget.getRightElement();

		UMLMessageChangeRightTarget ret = UML2DiffFactory.eINSTANCE.createUMLMessageChangeRightTarget();

		MessageEnd receiveEvent = message.getReceiveEvent();
		if (receiveEvent != null) {
			hideEvent(receiveEvent, ret);
		}

		MessageEnd sendEvent = message.getSendEvent();
		if (sendEvent != null) {
			hideEvent(sendEvent, ret);
		}

		ret.getHideElements().add(changeRightTarget);

		ret.setRemote(changeRightTarget.isRemote());
		ret.setLeftParent(changeRightTarget.getLeftParent());
		ret.setRightElement(changeRightTarget.getRightElement());

		return ret;
	}

	private void hideEvent(MessageEnd messageEnd, AbstractDiffExtension hiddingExtension) {
		hideCrossReferences(messageEnd,
				DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, hiddingExtension);
		hideCrossReferences(messageEnd, DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET,
				hiddingExtension, coveredByPredicate);
		if (messageEnd instanceof OccurrenceSpecification) {
			Event event = ((OccurrenceSpecification)messageEnd).getEvent();
			hideCrossReferences(event, DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT,
					hiddingExtension);
		}
	}

	private static final UMLPredicate<Setting> coveredByPredicate = new UMLPredicate<Setting>() {
		public boolean apply(Setting input) {
			return ((ReferenceChange)input.getEObject()).getReference() == UMLPackage.Literals.LIFELINE__COVERED_BY;
		}
	};

}
