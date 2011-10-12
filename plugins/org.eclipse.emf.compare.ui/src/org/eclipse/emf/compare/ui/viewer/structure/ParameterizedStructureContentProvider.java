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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.ui.util.OrderingUtils;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility.UIDifferenceGroup;
import org.eclipse.emf.ecore.EObject;

/**
 * A content provider to manage the display of the difference elements in relation to the selected group and
 * filters.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedStructureContentProvider extends ModelStructureContentProvider {

	/**
	 * The checked group.
	 * 
	 * @deprecated
	 */
	@Deprecated
	private static IDifferenceGroupingFacility mSelectedGroupFacility;

	/**
	 * The checked filters.
	 * 
	 * @deprecated
	 */
	@Deprecated
	private static List<IDifferenceFilter> mSelectedFilters;

	/** The checked group. */
	private IDifferenceGroupingFacility selectedGroupFacility;

	/** The checked filters. */
	private List<IDifferenceFilter> selectedFilters;

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
	 * @param pSelectedGroupFacility
	 *            The grouping facility that is to be used to group the children returned by this content
	 *            provider.
	 * @param pSelectedFilters
	 *            The filters that are to be applied to the children returned by this content provider.
	 */
	public ParameterizedStructureContentProvider(CompareConfiguration compareConfiguration,
			IDifferenceGroupingFacility pSelectedGroupFacility, List<IDifferenceFilter> pSelectedFilters) {
		this(compareConfiguration);
		selectedGroupFacility = pSelectedGroupFacility;
		selectedFilters = pSelectedFilters;
		// deprecated:
		mSelectedGroupFacility = pSelectedGroupFacility;
		mSelectedFilters = pSelectedFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (selectedGroupFacility == null) {
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
		return result;
	}

	/**
	 * Almost the same as {@link ModelStructureContentProvider#getChildren(Object)} but call local
	 * {@link #shouldBeHidden(EObject)} to take selected filters into account.
	 * 
	 * @param parentElement
	 *            The element from which to retrieve all non-hidden children.
	 * @return The list of non-hidden children of the given element.
	 */
	private Object[] getChildrenAsInSuper(Object parentElement) {
		Object[] children = null;
		if (parentElement instanceof EObject) {
			final Collection<EObject> childrenList = new ArrayList<EObject>();
			for (final EObject child : ((EObject)parentElement).eContents()) {
				if (!shouldBeHidden(child)) {
					childrenList.add(child);
				}
			}
			children = childrenList.toArray();
		}
		return children;
	}

	/**
	 * Specific should be hidden method taking into account the selected filters.
	 * <p>
	 * Almost the same as
	 * {@link org.eclipse.emf.compare.diff.metamodel.util.DiffAdapterFactory#shouldBeHidden(EObject)} but call
	 * {@link #isHidden(DiffElement)}.
	 * </p>
	 * 
	 * @param element
	 *            The element of which we need to check the "hidden" state.
	 * @return <code>true</code> if the given element should be hidden, <code>false</code> otherwise.
	 */
	private boolean shouldBeHidden(EObject element) {
		boolean result = false;
		if (element instanceof DiffElement) {
			final DiffElement diff = (DiffElement)element;
			final Iterator<AbstractDiffExtension> it = diff.getIsHiddenBy().iterator();
			while (it.hasNext()) {
				final AbstractDiffExtension extension = it.next();
				if (!extension.isIsCollapsed()) {
					result = true;
				}
			}
			result = result || isHiddenInMyContext((DiffElement)element);
		}
		if (element instanceof DiffGroup) {
			final DiffGroup group = (DiffGroup)element;
			if (filteredSubchanges(group) == 0) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Like {@link DiffGroup#getSubDiffElements()} but take into account if sub-elements are filtered by
	 * selected filters.
	 * 
	 * @param group
	 *            The diff group for which we need to count the subchanges that are neither hidden nor
	 *            filtered.
	 * @return The count of changes that are neither hidden nor filtered.
	 */
	private int filteredSubchanges(DiffGroup group) {
		final Iterator<DiffElement> it = group.getSubDiffElements().iterator();
		int result = 0;
		while (it.hasNext()) {
			final DiffElement eObj = it.next();
			if (!shouldBeHidden(eObj)) {
				if (eObj instanceof DiffGroup) {
					result += filteredSubchanges((DiffGroup)eObj);
				} else {
					result += 1;
				}
			}
		}
		return result;
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
		if (parentElement instanceof UIDifferenceGroup && selectedGroupFacility != null) {
			final List<DiffElement> diffs = new ArrayList<DiffElement>();

			final Object[] differences = super.getElements(null);
			for (int i = 0; i < differences.length; i++) {
				final Object object = differences[i];
				if (object instanceof DiffModel) {
					final DiffModel diffModel = (DiffModel)object;
					final EList<DiffElement> allDifferences = diffModel.getDifferences();
					for (DiffElement diffElement : allDifferences) {
						if (parentElement.equals(selectedGroupFacility.belongsTo(diffElement))
								&& !shouldBeHidden(diffElement)) {
							diffs.add(diffElement);
						}
					}
				} else if (object instanceof DiffElement) {
					final DiffElement diffElement = (DiffElement)object;
					if (parentElement.equals(selectedGroupFacility.belongsTo(diffElement))
							&& !shouldBeHidden(diffElement)) {
						diffs.add(diffElement);
					}
				}
			}
			return diffs.toArray();
		}
		return getChildrenAsInSuper(parentElement);
	}

	/**
	 * Filter the elements in relation to the checked filters.
	 * 
	 * @param elements
	 *            The elements.
	 * @return The filtered elements.
	 */
	@Deprecated
	protected Object[] filterElements(Object[] elements) {
		// TODO Get the right filters in relation to the menu checked
		final List<Object> lResult = Arrays.asList(elements);
		final List<Object> filteredResult = new ArrayList<Object>();
		filteredResult.addAll(lResult);

		final Iterator<Object> itResult = lResult.iterator();
		while (itResult.hasNext()) {
			final Object obj = itResult.next();
			if (obj instanceof DiffElement && !(obj instanceof DiffGroup)
					&& isHiddenInMyContext((DiffElement)obj)) {
				filteredResult.remove(obj);
			}
		}

		return filteredResult.toArray();
	}

	/**
	 * Checks if the element is hidden by a filter at least.
	 * 
	 * @deprecated Use ParameterizedStructureContentProvider#isHiddenInMyContext(DiffElement) instead of it.
	 * @param element
	 *            The element.
	 * @return true if it is hidden, false otherwise.
	 */
	@Deprecated
	public static boolean isHidden(DiffElement element) {
		return OrderingUtils.isHidden(element, mSelectedFilters);
	}

	/**
	 * Checks if the element is hidden by a filter at least.
	 * 
	 * @param element
	 *            The element.
	 * @return true if it is hidden, false otherwise.
	 * @since 1.3
	 */
	public boolean isHiddenInMyContext(DiffElement element) {
		return OrderingUtils.isHidden(element, selectedFilters);
	}

	/**
	 * Returns the groups which own difference elements.
	 * 
	 * @return The groups.
	 */
	protected List<UIDifferenceGroup> getGroupsWithChildren() {
		final List<UIDifferenceGroup> result = new ArrayList<IDifferenceGroupingFacility.UIDifferenceGroup>();

		if (selectedGroupFacility != null) {

			initGroups();

			final Iterator<UIDifferenceGroup> groups = selectedGroupFacility.allGroups().iterator();
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
	 * Initialize the UI groups.
	 */
	private void initGroups() {
		final Object[] elements = super.getElements(null);
		for (int i = 0; i < elements.length; i++) {
			final Object d = elements[i];
			initChildrenGroups(d);

			if (d instanceof DiffElement) {
				selectedGroupFacility.belongsTo((DiffElement)d);
			}
		}
	}

	/**
	 * Initialize the UI group for the given element and its own children.
	 * 
	 * @param inputElement
	 *            The element for which to create UI groups.
	 */
	private void initChildrenGroups(Object inputElement) {
		final Object[] elements = getChildrenAsInSuper(inputElement);
		for (int i = 0; i < elements.length; i++) {
			final Object d = elements[i];
			initChildrenGroups(d);

			if (d instanceof DiffElement) {
				selectedGroupFacility.belongsTo((DiffElement)d);
			}
		}
	}

	/**
	 * Setter of selectedGroupFacility.
	 * 
	 * @param groupingFacility
	 *            The facility that is to be used to group this content provider children.
	 * @since 1.3
	 */
	public void setSelectedGroup(IDifferenceGroupingFacility groupingFacility) {
		selectedGroupFacility = groupingFacility;
	}

	/**
	 * Setter of mSelectedGroupFacility.
	 * 
	 * @deprecated Use ParameterizedStructureContentProvider#setSelectedGroup(IDifferenceGroupingFacility)
	 *             instead of it.
	 * @param groupingFacility
	 *            The facility that is to be used to group this content provider children.
	 */
	@Deprecated
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
		if (selectedFilters == null) {
			selectedFilters = new ArrayList<IDifferenceFilter>();
		}
		selectedFilters.add(filter);
	}

	/**
	 * Set the selected filters.
	 * 
	 * @param filters
	 *            Selected filters.
	 * @since 1.3
	 */
	public void setSelectedFilters(List<IDifferenceFilter> filters) {
		selectedFilters = filters;
	}

	/**
	 * Removes a filter from the list of applied difference filters.
	 * 
	 * @param filter
	 *            Filter that is to be removed from the list of applied difference filters.
	 */
	public void removeSelectedFilter(IDifferenceFilter filter) {
		if (selectedFilters != null) {
			selectedFilters.remove(filter);
		}
	}
}
