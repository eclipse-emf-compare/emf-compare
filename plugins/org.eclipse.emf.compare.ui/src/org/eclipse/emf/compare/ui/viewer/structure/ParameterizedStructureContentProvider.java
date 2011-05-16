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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility.UIDifferenceGroup;

/**
 * A content provider to manage the display of the difference elements in relation to the selected group and
 * filters.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedStructureContentProvider extends ModelStructureContentProvider {
	/** The checked group. */
	private static IDifferenceGroupingFacility mSelectedGroupFacility;

	/** The checked filters. */
	private static List<IDifferenceFilter> mSelectedFilters;

	/**
	 * Constructor.
	 * 
	 * @param compareConfiguration
	 *            {@link CompareConfiguration} used for this comparison.
	 */
	public ParameterizedStructureContentProvider(CompareConfiguration compareConfiguration) {
		super(compareConfiguration);
	}

	/**
	 * Constructor.
	 * 
	 * @param compareConfiguration
	 *            {@link CompareConfiguration} used for this comparison.
	 * @param selectedGroupFacility
	 *            The grouping facility that is to be used to group the children returned by this content
	 *            provider.
	 * @param selectedFilters
	 *            The filters that are to be applied to the children returned by this content provider.
	 */
	public ParameterizedStructureContentProvider(CompareConfiguration compareConfiguration,
			IDifferenceGroupingFacility selectedGroupFacility, List<IDifferenceFilter> selectedFilters) {
		this(compareConfiguration);
		mSelectedGroupFacility = selectedGroupFacility;
		mSelectedFilters = selectedFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (mSelectedGroupFacility == null) {
			return super.getElements(inputElement);
		}
		return getGroupsWithChildren().toArray();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		final Object[] result = groupElements(parentElement);
		return filterElements(result);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return super.hasChildren(element) || element instanceof UIDifferenceGroup;
	}

	/**
	 * Group the elements under the parent element, in relation to the checked group.
	 * 
	 * @param parentElement
	 *            The parent element.
	 * @return The grouped elements.
	 */
	protected Object[] groupElements(Object parentElement) {
		if (parentElement instanceof UIDifferenceGroup && mSelectedGroupFacility != null) {
			final List<DiffElement> diffs = new ArrayList<DiffElement>();

			if (input instanceof DiffModel) {
				final Iterator<DiffElement> differences = ((DiffModel)input).getDifferences().iterator();
				while (differences.hasNext()) {
					final DiffElement diff = differences.next();
					if (parentElement.equals(mSelectedGroupFacility.belongsTo(diff))) {
						diffs.add(diff);
					}
				}
			}
			return diffs.toArray();
		}
		return super.getChildren(parentElement);
	}

	/**
	 * Filter the elements in relation to the checked filters.
	 * 
	 * @param elements
	 *            The elements.
	 * @return The filtered elements.
	 */
	protected Object[] filterElements(Object[] elements) {
		// TODO Get the right filters in relation to the menu checked
		final List<Object> lResult = Arrays.asList(elements);
		final List<Object> filteredResult = new ArrayList<Object>();
		filteredResult.addAll(lResult);

		final Iterator<Object> itResult = lResult.iterator();
		while (itResult.hasNext()) {
			final Object obj = itResult.next();
			if (obj instanceof DiffElement && isHidden((DiffElement)obj)) {
				filteredResult.remove(obj);
			}
		}

		return filteredResult.toArray();
	}

	/**
	 * Checks if the element is hidden by a filter at least.
	 * 
	 * @param element
	 *            The element.
	 * @return true if it is hidden, false otherwise.
	 */
	public static boolean isHidden(DiffElement element) {
		if (mSelectedFilters != null) {
			final Iterator<IDifferenceFilter> it = mSelectedFilters.iterator();
			while (it.hasNext()) {
				final IDifferenceFilter diffFilter = it.next();
				if (diffFilter.hides(element))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns the groups which own difference elements.
	 * 
	 * @return The groups.
	 */
	protected List<UIDifferenceGroup> getGroupsWithChildren() {
		final List<UIDifferenceGroup> result = new ArrayList<IDifferenceGroupingFacility.UIDifferenceGroup>();

		if (mSelectedGroupFacility != null) {
			final Iterator<UIDifferenceGroup> groups = mSelectedGroupFacility.allGroups().iterator();
			while (groups.hasNext()) {
				final UIDifferenceGroup group = groups.next();
				if (getChildren(group).length > 0) {
					result.add(group);
				}
			}
		}

		return result;
	}

	/**
	 * Setter of mSelectedGroupFacility.
	 * 
	 * @param groupingFacility
	 *            The facility that is to be used to group this content provider children.
	 */
	public static void setSelectedGroupFacility(IDifferenceGroupingFacility groupingFacility) {
		mSelectedGroupFacility = groupingFacility;
	}

	/**
	 * Adds a filter to the list of applied difference filters.
	 * 
	 * @param filter
	 *            Filter that is to be added to the list of applied difference filters.
	 */
	public void addSelectedFilter(IDifferenceFilter filter) {
		if (mSelectedFilters == null) {
			mSelectedFilters = new ArrayList<IDifferenceFilter>();
		}
		mSelectedFilters.add(filter);
	}

	/**
	 * Removes a filter from the list of applied difference filters.
	 * 
	 * @param filter
	 *            Filter that is to be removed from the list of applied difference filters.
	 */
	public void removeSelectedFilter(IDifferenceFilter filter) {
		if (mSelectedFilters != null) {
			mSelectedFilters.remove(filter);
		}
	}
}
