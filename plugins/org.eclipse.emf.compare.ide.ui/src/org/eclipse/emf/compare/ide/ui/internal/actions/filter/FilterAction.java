package org.eclipse.emf.compare.ide.ui.internal.actions.filter;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * This class represents the filter action, it acts corresponding to the given filter Type.
 * 
 * @author <a href="mailto:bouanani.meher@obeo.fr">Bouanani Maher</a>
 */
public class FilterAction extends Action {
	/**
	 * The Filter type (ADD, MOVE, REMOVE or CHANGE).
	 */
	private DifferenceKind filterType;

	/**
	 * The Filter that will be applyed by the action.
	 */
	private DifferenceFilter differenceFilter;

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            Will be used as the action's tooltip.
	 * @param filterType
	 *            represents the kind of the choosed filter.
	 * @param differenceFilter
	 *            The filter that this action will need to update.
	 */
	public FilterAction(String text, DifferenceKind filterType, DifferenceFilter differenceFilter) {
		super(text, IAction.AS_CHECK_BOX);
		this.filterType = filterType;
		this.differenceFilter = differenceFilter;
	}

	@Override
	public void run() {

		if (isChecked()) {
			differenceFilter.addFilter(filterType);
		} else {
			differenceFilter.removeFilter(filterType);
		}
	}
}
