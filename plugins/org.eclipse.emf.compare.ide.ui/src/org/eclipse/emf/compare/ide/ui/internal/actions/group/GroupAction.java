/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * This action will allow us to group differences by their kind.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class GroupAction extends Action {
	/** The difference grouper that will be affected by this action. */
	private final DifferenceGrouper differenceGrouper;

	/** The actual instance that will provide groups if this action is used. */
	private final DifferenceGroupProvider provider;

	/**
	 * Instantiates our action given its target grouper.
	 * 
	 * @param differenceGrouper
	 *            The grouper to which we'll provide our DifferenceGroupProvider.
	 */
	public GroupAction(String text, DifferenceGrouper differenceGrouper,
			DifferenceGroupProvider provider) {
		super(text, IAction.AS_RADIO_BUTTON);
		this.differenceGrouper = differenceGrouper;
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		differenceGrouper.setProvider(provider);
	}
}
