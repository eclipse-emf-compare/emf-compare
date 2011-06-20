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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLMessageChangeLeftTargetFactory extends AbstractDiffExtensionFactory {

	public UMLMessageChangeLeftTargetFactory(UML2DiffEngine engine, CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		return input instanceof ModelElementChangeLeftTarget
				&& ((ModelElementChangeLeftTarget)input).getLeftElement() instanceof Message;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeLeftTarget changeLeftTarget = (ModelElementChangeLeftTarget)input;
		final Message message = (Message)changeLeftTarget.getLeftElement();

		UMLMessageChangeLeftTarget ret = UML2DiffFactory.eINSTANCE.createUMLMessageChangeLeftTarget();

		MessageEnd receiveEvent = message.getReceiveEvent();
		if (receiveEvent != null) {
			hideEvent(receiveEvent, ret);
		}

		MessageEnd sendEvent = message.getSendEvent();
		if (sendEvent != null) {
			hideEvent(sendEvent, ret);
		}

		ret.getHideElements().add(changeLeftTarget);

		ret.setRemote(changeLeftTarget.isRemote());
		ret.setRightParent(changeLeftTarget.getRightParent());
		ret.setLeftElement(changeLeftTarget.getLeftElement());

		return ret;
	}

	private void hideEvent(MessageEnd messageEnd, AbstractDiffExtension hiddingExtension) {
		hideCrossReferences(messageEnd, DiffPackage.Literals.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT,
				hiddingExtension);
		hideCrossReferences(messageEnd, DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET,
				hiddingExtension, coveredByPredicate);
		if (messageEnd instanceof OccurrenceSpecification) {
			Event event = ((OccurrenceSpecification)messageEnd).getEvent();
			hideCrossReferences(event, DiffPackage.Literals.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT,
					hiddingExtension);
		}
	}

	private static final UMLPredicate<Setting> coveredByPredicate = new UMLPredicate<Setting>() {
		public boolean apply(Setting input) {
			return ((ReferenceChange)input.getEObject()).getReference() == UMLPackage.Literals.LIFELINE__COVERED_BY;
		}
	};

}
