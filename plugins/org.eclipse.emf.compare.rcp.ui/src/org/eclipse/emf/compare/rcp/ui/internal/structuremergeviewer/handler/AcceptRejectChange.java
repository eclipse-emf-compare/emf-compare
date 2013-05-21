package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.handler;

import static com.google.common.collect.Iterables.addAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeDataAdapter;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AcceptRejectChange extends AbstractHandler {

	/** The compare configuration object used to get the compare model. */
	private CompareConfiguration configuration;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			configuration = ((CompareEditorInput)editorInput).getCompareConfiguration();
			Object diffNode = ((CompareEditorInput)editorInput).getSelectedEdition();
			if (diffNode instanceof Adapter) {
				Notifier diff = ((Adapter)diffNode).getTarget();
				if (diff instanceof Diff) {
					boolean leftReadOnly = !configuration.isLeftEditable() && configuration.isRightEditable();
					boolean rightReadOnly = configuration.isLeftEditable()
							&& !configuration.isRightEditable();
					if (rightReadOnly) {
						if (isCopyDiffCase((Diff)diff, leftReadOnly)) {
							copyDiff((Diff)diff, leftReadOnly);
						} else {
							changeState((Diff)diff, rightReadOnly);
						}
						// Select next diff
						navigate(true);
					}
				}
			}
		}
		return null;
	}

	protected abstract boolean isCopyDiffCase(Diff diff, boolean leftToRight);

	private void changeState(Diff diffToChangeState, boolean leftToRight) {
		if (diffToChangeState != null) {
			ICompareEditingDomain compareEditingDomain = (ICompareEditingDomain)configuration
					.getProperty(EMFCompareConstants.EDITING_DOMAIN);
			Command changeStateCommand = new AcceptRejectChangeCommand(compareEditingDomain
					.getChangeRecorder(), diffToChangeState, leftToRight);
			compareEditingDomain.getCommandStack().execute(changeStateCommand);
		}
	}

	private void copyDiff(Diff diffToCopy, boolean leftToRight) {
		if (diffToCopy != null) {
			List<Diff> diffsToCopy = new ArrayList<Diff>();
			diffsToCopy.add(diffToCopy);
			if (isSubDiffFilterActive()) {
				addAll(diffsToCopy, DiffUtil.getSubDiffs(leftToRight).apply(diffToCopy));
			}
			ICompareEditingDomain editingDomain = (ICompareEditingDomain)configuration
					.getProperty(EMFCompareConstants.EDITING_DOMAIN);
			Command copyCommand = editingDomain.createCopyCommand(diffsToCopy, leftToRight,
					EMFCompareRCPPlugin.getDefault().getMergerRegistry());

			editingDomain.getCommandStack().execute(copyCommand);
			// refresh();
		}
	}

	protected boolean isSubDiffFilterActive() {
		Object property = configuration.getProperty(EMFCompareConstants.SELECTED_FILTERS);
		final Collection<IDifferenceFilter> selectedFilters;
		if (property == null) {
			return false;
		} else {
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
	 * Called by the framework to navigate to the next (or previous) difference. This will open the content
	 * viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 */
	protected void navigate(boolean next) {
		// final Control control = getControl();
		// if (control != null && !control.isDisposed()) {
		final ICompareNavigator navigator = configuration.getContainer().getNavigator();
		if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
			navigator.selectChange(next);
		}
		// }
	}

	public class AcceptRejectChangeCommand extends ChangeCommand implements ICompareCopyCommand {

		private Diff difference;

		private boolean leftToRight;

		public AcceptRejectChangeCommand(ChangeRecorder changeRecorder, Diff difference, boolean leftToRight) {
			super(changeRecorder, difference);
			this.difference = difference;
			this.leftToRight = leftToRight;
		}

		@Override
		public void doExecute() {
			Adapter adapter = EcoreUtil.getExistingAdapter(difference, IMergeData.class);
			if (adapter != null) {
				if (leftToRight) {
					((IMergeData)adapter).setMergedToRight();
				} else {
					((IMergeData)adapter).setMergedToLeft();
				}
			} else {
				difference.eAdapters().add(new MergeDataAdapter(leftToRight));
			}
			difference.setState(DifferenceState.MERGED);

		}

		public boolean isLeftToRight() {
			return leftToRight;
		}

	}

}
