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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.MessageChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLGeneralizationSetChangeLeftTarget.
 */
public class UMLMessageChangeFactory extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLDiff> getExtensionKind() {
		return MessageChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createMessageChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (container instanceof Message) {
				result = container;
			} else if (container instanceof MessageOccurrenceSpecification) {
				result = ((MessageOccurrenceSpecification)container).getMessage();
			}
		}
		return result;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof Message) {
			final MessageEnd recvEvent = ((Message)discriminant).getReceiveEvent();
			final MessageEnd sendEvent = ((Message)discriminant).getSendEvent();
			if (recvEvent instanceof InteractionFragment) {
				result.add(recvEvent);
				result.addAll(((InteractionFragment)recvEvent).getCovereds());
			}
			if (sendEvent instanceof InteractionFragment) {
				result.add(sendEvent);
				result.addAll(((InteractionFragment)sendEvent).getCovereds());
			}
		}
		return result;
	}

	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.MESSAGE);
		result.add(UMLPackage.Literals.MESSAGE_OCCURRENCE_SPECIFICATION);
		return result;
	}

	@Override
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		if (input instanceof ReferenceChange) {
			if (isRelatedToAnExtensionChange((ReferenceChange)input)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionAdd((ReferenceChange)input)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete((ReferenceChange)input)) {
				return DifferenceKind.DELETE;
			}
		}
		return null;
	}

	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return (input.getReference().equals(UMLPackage.Literals.MESSAGE__RECEIVE_EVENT)
				|| input.getReference().equals(UMLPackage.Literals.MESSAGE__SEND_EVENT) || input
				.getReference().equals(UMLPackage.Literals.INTERACTION_FRAGMENT__COVERED))
				&& getManagedConcreteDiscriminantKind().contains(
						MatchUtil.getContainer(input.getMatch().getComparison(), input).eClass());
	}

	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return (input.getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof Message
				&& (((Message)input.getValue()).getReceiveEvent() != null || ((Message)input.getValue())
						.getSendEvent() != null) && getManagedConcreteDiscriminantKind().contains(
				input.getValue().eClass()));
	}

	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof Message
				&& getManagedConcreteDiscriminantKind().contains(input.getValue().eClass());
	}

}
