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
package org.eclipse.emf.compare.ui.viewer.structure;

import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility;
import org.eclipse.swt.widgets.Composite;

/**
 * The viewer to display the tree of differences.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 * @since 1.3
 */
public class StructureViewer extends ParameterizedStructureMergeViewer {

	/**
	 * The filters to apply.
	 */
	protected List<IDifferenceFilter> differenceFilters;

	/**
	 * The groups to use.
	 */
	protected IDifferenceGroupingFacility differenceGroupingFacility;

	/**
	 * Constructor of the viewer.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param compareConfiguration
	 *            The compare configuration.
	 */
	public StructureViewer(Composite parent, CompareConfiguration compareConfiguration) {
		super(parent, compareConfiguration);
	}

	/**
	 * Constructor of the viewer.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @param filters
	 *            The list of filters to apply.
	 * @param groupingFacility
	 *            The grouping to apply.
	 */
	public StructureViewer(Composite parent, CompareConfiguration compareConfiguration,
			List<IDifferenceFilter> filters, IDifferenceGroupingFacility groupingFacility) {
		super(parent, compareConfiguration);
		setDifferenceFilters(filters);
		setDifferenceGroupingFacility(groupingFacility);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer#createToolItems()
	 */
	@Override
	protected void createToolItems() {
		// To do nothing.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer#updateToolItems()
	 */
	@Override
	protected void updateToolItems() {
		// To do nothing.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer#buildContentProvider(org.eclipse.compare.CompareConfiguration)
	 */
	@Override
	protected ParameterizedStructureContentProvider buildContentProvider(
			CompareConfiguration compareConfiguration) {
		return new ParameterizedStructureContentProvider(compareConfiguration, differenceGroupingFacility,
				differenceFilters);
	}

	@Override
	protected ModelStructureContentProvider createContentProvider(CompareConfiguration compareConfiguration) {
		return buildContentProvider(compareConfiguration);
	}

	/**
	 * Sets the value of filters to filters.
	 * 
	 * @param filters
	 *            The filters to set.
	 */
	public void setDifferenceFilters(List<IDifferenceFilter> filters) {
		if (differenceFilters == null || !differenceFilters.equals(filters)) {
			differenceFilters = filters;
			((ParameterizedStructureContentProvider)getContentProvider()).setSelectedFilters(filters);
			refresh();
		}
	}

	/**
	 * Sets the value of groupingFacility to groupingFacility.
	 * 
	 * @param groupingFacility
	 *            The groupingFacility to set.
	 */
	public void setDifferenceGroupingFacility(IDifferenceGroupingFacility groupingFacility) {
		if (differenceGroupingFacility == null || !differenceGroupingFacility.equals(groupingFacility)) {
			differenceGroupingFacility = groupingFacility;
			((ParameterizedStructureContentProvider)getContentProvider()).setSelectedGroup(groupingFacility);
			refresh();
		}
	}

	/**
	 * Returns the differenceFilters.
	 * 
	 * @return The differenceFilters.
	 */
	public List<IDifferenceFilter> getDifferenceFilters() {
		return differenceFilters;
	}

	/**
	 * Returns the differenceGroupingFacility.
	 * 
	 * @return The differenceGroupingFacility.
	 */
	public IDifferenceGroupingFacility getDifferenceGroupingFacility() {
		return differenceGroupingFacility;
	}

}
