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
package org.eclipse.emf.compare.ui.viewer.group;

import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewer.AbstractOrderingAction;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.jface.action.IAction;

/**
 * Action to group difference elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class GroupingAction extends AbstractOrderingAction {
	/** Descriptor for groups. */
	private IDifferenceGroupingFacility relatedGroup;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of this action.
	 * @param viewer
	 *            Viewer on which this action is acting.
	 */
	public GroupingAction(String name, ParameterizedStructureMergeViewer viewer) {
		super(name, IAction.AS_RADIO_BUTTON, viewer);
	}

	/**
	 * Constructor.
	 * 
	 * @param desc
	 *            Descriptor of the underlying grouping facility.
	 * @param viewer
	 *            Viewer on which this action is acting.
	 */
	public GroupingAction(DifferenceGroupingFacilityDescriptor desc, ParameterizedStructureMergeViewer viewer) {
		super(desc.getName(), IAction.AS_RADIO_BUTTON, viewer);
		relatedGroup = desc.getExtension();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.AbstractOrderingAction#doRun(org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider)
	 */
	@Override
	protected void doRun(ParameterizedStructureContentProvider provider) {
		if (isChecked()) {
			mViewer.getCompareConfiguration().setProperty(EMFCompareConstants.PROPERTY_STRUCTURE_GROUP,
					relatedGroup);
		} else {
			mViewer.getCompareConfiguration().setProperty(EMFCompareConstants.PROPERTY_STRUCTURE_GROUP, null);
		}
	}
}
