/*******************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.subscriber;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput;
import org.eclipse.team.ui.synchronize.ModelSynchronizeParticipant;

@SuppressWarnings("restriction")
public class TeamSubscriberProvider implements ISubscriberProvider {

	public Subscriber getSubscriber(ICompareContainer container, ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor) {
		if (container instanceof ModelCompareEditorInput) {
			return getSubscriber((ModelCompareEditorInput)container);
		}
		return null;
	}

	/**
	 * Team left us with absolutely no way to determine whether our supplied input is the result of a
	 * synchronization or not.
	 * <p>
	 * In order to properly resolve the logical model of the resource currently being compared we need to know
	 * what "other" resources were part of its logical model, and we need to know the revisions of these
	 * resources we are to load. All of this has already been computed by Team, but it would not let us know.
	 * This method uses discouraged means to get around this "black box" locking from Team.
	 * </p>
	 * <p>
	 * The basic need here is to retrieve the Subscriber from this point. We have a lot of accessible
	 * variables, the two most important being the CompareConfiguration and ICompareInput... I could find no
	 * way around the privileged access to the private ModelCompareEditorInput.participant field. There does
	 * not seem to be any adapter (or Platform.getAdapterManager().getAdapter(...)) that would allow for this,
	 * so I'm taking the long way 'round.
	 * </p>
	 * 
	 * @return The subscriber used for this comparison if any could be found, <code>null</code> otherwise.
	 */
	private Subscriber getSubscriber(ModelCompareEditorInput input) {
		ModelSynchronizeParticipant participant = getModelSynchronizeParticipant(input);
		return participant != null && participant.getContext() instanceof SubscriberMergeContext
				? ((SubscriberMergeContext)participant.getContext()).getSubscriber()
				: null;
	}

	private ModelSynchronizeParticipant getModelSynchronizeParticipant(ModelCompareEditorInput modelInput) {
		try {
			final Field field = ModelCompareEditorInput.class.getDeclaredField("participant"); //$NON-NLS-1$
			AccessController.doPrivileged((PrivilegedAction<Void>)() -> {
				field.setAccessible(true);
				return null;
			});
			Object participant = field.get(modelInput);
			return participant instanceof ModelSynchronizeParticipant
					? (ModelSynchronizeParticipant)participant
					: null;
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			// Swallow this, this private field was there at least from 3.5 to 4.3
		}
		return null;
	}

}
