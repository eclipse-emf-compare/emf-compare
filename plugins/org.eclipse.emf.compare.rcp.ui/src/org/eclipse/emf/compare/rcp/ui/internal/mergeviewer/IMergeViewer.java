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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer;

import java.util.Collection;

import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.jface.viewers.IInputSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IMergeViewer extends IInputSelectionProvider {

	/**
	 * {@inheritDoc}
	 */
	MergeViewerSide getSide();

	/**
	 * Returns the primary control associated with this viewer.
	 * 
	 * @return the SWT control which displays this viewer's content
	 */
	Control getControl();

	/**
	 * Refreshes this viewer completely with information freshly obtained from this viewer's model.
	 */
	void refresh();

	/**
	 * Sets or clears the input for this viewer.
	 * 
	 * @param input
	 *            the input of this viewer, or <code>null</code> if none
	 */
	void setInput(Object input);

	/**
	 * Sets a new selection for this viewer and optionally makes it visible.
	 * <p>
	 * Subclasses must implement this method.
	 * </p>
	 * 
	 * @param selection
	 *            the new selection
	 * @param reveal
	 *            <code>true</code> if the selection is to be made visible, and <code>false</code> otherwise
	 */
	void setSelection(ISelection selection, boolean reveal);

	/**
	 * Returns the active group provider.
	 * 
	 * @return the selected group provider.
	 */
	IDifferenceGroupProvider getSelectedGroup();

	/**
	 * Set the active group provider.
	 * 
	 * @param group
	 *            the group provider to set.
	 */
	void setSelectedGroup(IDifferenceGroupProvider group);

	/**
	 * Returns the active filters.
	 * 
	 * @return the selectedFilters.
	 */
	Collection<IDifferenceFilter> getSelectedFilters();

	/**
	 * Set the list of selected filters.
	 * 
	 * @param filters
	 *            the selectedFilters to set
	 */
	void setSelectedFilters(Collection<IDifferenceFilter> filters);

	enum MergeViewerSide {
		LEFT, RIGHT, ANCESTOR;

		public MergeViewerSide opposite() {
			switch (this) {
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;
				case ANCESTOR:
					return ANCESTOR;
				default:
					throw new IllegalStateException(); // happy compiler :)
			}
		}

		public static MergeViewerSide getValueFrom(DifferenceSource source) {
			switch (source) {
				case LEFT:
					return LEFT;
				case RIGHT:
					return RIGHT;
				default:
					throw new IllegalStateException();
			}
		}

		public DifferenceSource convertToDifferenceSource() {
			switch (this) {
				case LEFT:
					return DifferenceSource.LEFT;
				case RIGHT:
					return DifferenceSource.RIGHT;
				default:
					throw new IllegalStateException();
			}
		}
	}
}
