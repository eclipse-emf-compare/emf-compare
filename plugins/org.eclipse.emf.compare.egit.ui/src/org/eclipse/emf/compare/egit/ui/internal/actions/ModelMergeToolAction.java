/******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Goubet <laurent.goubet@obeo.fr> - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.ui.internal.actions;

import org.eclipse.egit.ui.internal.actions.ActionCommands;
import org.eclipse.egit.ui.internal.actions.RepositoryAction;

/**
 * An action to use the MergeTool with EMFCompare.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public class ModelMergeToolAction extends RepositoryAction {

	/**
	 * Default constructor.
	 */
	public ModelMergeToolAction() {
		super(ActionCommands.MERGE_TOOL_ACTION, new ModelMergeToolActionHandler());
	}

}
