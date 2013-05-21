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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler.util;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.DiffMergeDataAdapter;
import org.eclipse.emf.compare.internal.merge.IDiffMergeData;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Util class that provides utilities methods for RCP UI handlers.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public final class EMFCompareUIHandlerUtil {

	/**
	 * Utility classes don't need a default constructor.
	 */
	private EMFCompareUIHandlerUtil() {
		// Hides default constructor.
	}

	/**
	 * Checks the state of the cascading differences filter.
	 * 
	 * @param configuration
	 *            the compare configuration object.
	 * @return true, if the cascading differences filter is active, false otherwise.
	 */
	public static boolean isCascadingDifferencesFilterActive(CompareConfiguration configuration) {
		Object property = configuration.getProperty(EMFCompareConstants.SELECTED_FILTERS);
		final Collection<IDifferenceFilter> selectedFilters;
		if (property != null) {
			selectedFilters = (Collection<IDifferenceFilter>)property;
			for (IDifferenceFilter iDifferenceFilter : selectedFilters) {
				if (iDifferenceFilter instanceof CascadingDifferencesFilter) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Copy the given diff.
	 * 
	 * @param diffToCopy
	 *            the given diff to copy.
	 * @param leftToRight
	 *            the way of merge.
	 * @param configuration
	 *            the compare configuration object.
	 */
	public static void copyDiff(Diff diffToCopy, boolean leftToRight, CompareConfiguration configuration) {
		if (diffToCopy != null) {
			List<Diff> diffsToCopy = new ArrayList<Diff>();
			diffsToCopy.add(diffToCopy);
			for (Diff require : DiffUtil.getRequires(diffToCopy, leftToRight, diffToCopy.getSource())) {
				EMFCompareUIHandlerUtil.setMergeDataForDiff(require, leftToRight, configuration
						.isLeftEditable(), configuration.isRightEditable());
			}
			if (EMFCompareUIHandlerUtil.isCascadingDifferencesFilterActive(configuration)) {
				addAll(diffsToCopy, org.eclipse.emf.compare.utils.DiffUtil.getSubDiffs(leftToRight).apply(
						diffToCopy));
			}
			for (Diff diff : diffsToCopy) {
				EMFCompareUIHandlerUtil.setMergeDataForDiff(diff, leftToRight,
						configuration.isLeftEditable(), configuration.isRightEditable());
			}
			ICompareEditingDomain editingDomain = (ICompareEditingDomain)configuration
					.getProperty(EMFCompareConstants.EDITING_DOMAIN);
			Command copyCommand = editingDomain.createCopyCommand(diffsToCopy, leftToRight,
					EMFCompareRCPPlugin.getDefault().getMergerRegistry());
			editingDomain.getCommandStack().execute(copyCommand);
		}
	}

	/**
	 * Copy all non-conflicting changes.
	 * 
	 * @param leftToRight
	 *            the way of merge.
	 * @param configuration
	 *            the compare configuration object.
	 */
	public static void copyAllDiffs(final boolean leftToRight, CompareConfiguration configuration) {
		final List<Diff> differences;
		Comparison comparison = (Comparison)configuration.getProperty(EMFCompareConstants.COMPARE_RESULT);
		if (comparison.isThreeWay()) {
			differences = ImmutableList.copyOf(filter(comparison.getDifferences(), new Predicate<Diff>() {
				public boolean apply(Diff diff) {
					final boolean unresolved = diff.getState() == DifferenceState.UNRESOLVED;
					final boolean nonConflictual = diff.getConflict() == null;
					final boolean fromLeftToRight = leftToRight && diff.getSource() == DifferenceSource.LEFT;
					final boolean fromRightToLeft = !leftToRight
							&& diff.getSource() == DifferenceSource.RIGHT;
					return unresolved && nonConflictual && (fromLeftToRight || fromRightToLeft);
				}
			}));
		} else {
			differences = ImmutableList.copyOf(filter(comparison.getDifferences(), EMFComparePredicates
					.hasState(DifferenceState.UNRESOLVED)));
		}

		if (differences.size() > 0) {
			for (Diff diff : differences) {
				EMFCompareUIHandlerUtil.setMergeDataForDiff(diff, leftToRight,
						configuration.isLeftEditable(), configuration.isRightEditable());
			}
			ICompareEditingDomain editingDomain = (ICompareEditingDomain)configuration
					.getProperty(EMFCompareConstants.EDITING_DOMAIN);
			final Command copyCommand = editingDomain.createCopyCommand(differences, leftToRight,
					EMFCompareRCPPlugin.getDefault().getMergerRegistry());

			editingDomain.getCommandStack().execute(copyCommand);
		}
	}

	/**
	 * Called by the framework to navigate to the next (or previous) difference. This will open the content
	 * viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 * @param configuration
	 *            the compare configuration object.
	 */
	public static void navigate(boolean next, CompareConfiguration configuration) {
		final ICompareNavigator navigator = configuration.getContainer().getNavigator();
		if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
			navigator.selectChange(next);
		}
	}

	/**
	 * Set the merge way for the given diff. After a merge, it allows to know the way of the merge.
	 * 
	 * @param diff
	 *            the given diff.
	 * @param leftToRight
	 *            the way of the merge.
	 * @param leftEditable
	 *            the left side of the difference is editable.
	 * @param rightEditable
	 *            the right side of the difference is editable.
	 */
	public static void setMergeDataForDiff(Diff diff, boolean leftToRight, boolean leftEditable,
			boolean rightEditable) {
		Adapter adapter = EcoreUtil.getExistingAdapter(diff, IDiffMergeData.class);
		if (adapter != null) {
			((IDiffMergeData)adapter).setMergedTo(leftToRight);
			((IDiffMergeData)adapter).setLeftEditable(leftEditable);
			((IDiffMergeData)adapter).setRightEditable(rightEditable);
		} else {
			diff.eAdapters().add(new DiffMergeDataAdapter(leftToRight, leftEditable, rightEditable));
		}
	}
}
