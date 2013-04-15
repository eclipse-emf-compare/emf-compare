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
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.sequence;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.MessageChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;

/**
 * Factory for message changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLMessageChangeFactory extends AbstractUMLChangeFactory {

	/**
	 * Discriminants getter for the message change.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class MessageDiscriminantsGetter extends DiscriminantsGetter {
		/**
		 * {@inheritDoc}<br>
		 * Discriminants are the message and its message ends.
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseMessage(org.eclipse.uml2.uml.Message)
		 */
		@Override
		public Set<EObject> caseMessage(Message object) {
			Set<EObject> result = new HashSet<EObject>();
			result.add(object);
			if (object.getSendEvent() != null) {
				result.add(object.getSendEvent());
			}
			if (object.getReceiveEvent() != null) {
				result.add(object.getReceiveEvent());
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseMessageOccurrenceSpecification(org.eclipse.uml2.uml.MessageOccurrenceSpecification)
		 */
		@Override
		public Set<EObject> caseMessageOccurrenceSpecification(MessageOccurrenceSpecification object) {
			Set<EObject> result = new HashSet<EObject>();
			Message message = object.getMessage();
			if (message != null) {
				result.addAll(caseMessage(message));
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseLifeline(org.eclipse.uml2.uml.Lifeline)
		 */
		@Override
		public Set<EObject> caseLifeline(Lifeline object) {
			Set<EObject> result = new HashSet<EObject>();
			for (InteractionFragment fragment : object.getCoveredBys()) {
				result.addAll(doSwitch(fragment));
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return MessageChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createMessageChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		return Iterables.find(getDiscriminants(input), instanceOf(Message.class), null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new MessageDiscriminantsGetter();
	}

}
