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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages a merge of a difference in case of one side of the comparison is not editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class RejectChangeAction extends AbstractAcceptRejectAction {

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public RejectChangeAction(EMFCompareConfiguration configuration) {
		super(configuration);
		setToolTipText(EMFCompareIDEUIMessages.getString("reject.change.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/reject_change.gif")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.RejectChangeAction#isCopyDiffCase(org.eclipse.emf.compare.Diff,
	 *      boolean)
	 */
	@Override
	protected boolean isCopyDiffCase(Diff diff, boolean leftToRight) {
		if (leftToRight) {
			return fromSide(DifferenceSource.RIGHT).apply(diff);
		} else {
			return fromSide(DifferenceSource.LEFT).apply(diff);
		}
	}

}
