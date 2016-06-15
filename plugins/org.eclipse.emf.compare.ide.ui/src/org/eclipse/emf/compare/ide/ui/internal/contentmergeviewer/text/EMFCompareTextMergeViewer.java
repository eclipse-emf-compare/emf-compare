/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - Bug 457117
 *     Philip Langer - integrated model update strategy (bug 457839)
 *     Michael Borkowski - Bug 462863
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.text;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.EventObject;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.MergeSourceViewer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.ide.ui.mergeresolution.MergeResolutionManager;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategy;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareTextMergeViewer extends TextMergeViewer implements CommandStackListener {

	private static final String BUNDLE_NAME = EMFCompareTextMergeViewer.class.getName();

	private DynamicObject fDynamicObject;

	private UndoAction fUndoAction;

	private RedoAction fRedoAction;

	private MergeResolutionManager mergeResolutionManager;

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareTextMergeViewer(Composite parent, EMFCompareConfiguration configuration) {
		super(parent, configuration);
		setContentProvider(new EMFCompareTextMergeViewerContentProvider(configuration));

		editingDomainChange(null, configuration.getEditingDomain());

		configuration.getEventBus().register(this);

		mergeResolutionManager = new MergeResolutionManager(
				EMFCompareIDEUIPlugin.getDefault().getMergeResolutionListenerRegistry());
	}

	/**
	 * @param oldValue
	 * @param newValue
	 */
	@Subscribe
	public void editingDomainChange(ICompareEditingDomainChange event) {
		ICompareEditingDomain oldValue = event.getOldValue();
		ICompareEditingDomain newValue = event.getNewValue();
		editingDomainChange(oldValue, newValue);
	}

	public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		if (oldValue != null) {
			ICompareCommandStack commandStack = oldValue.getCommandStack();
			commandStack.removeCommandStackListener(this);
		}
		if (newValue != oldValue) {
			if (newValue != null) {
				ICompareCommandStack commandStack = newValue.getCommandStack();
				commandStack.addCommandStackListener(this);
				setLeftDirty(commandStack.isLeftSaveNeeded());
				setRightDirty(commandStack.isRightSaveNeeded());
			}
			if (fUndoAction != null) {
				fUndoAction.setEditingDomain(newValue);
			}
			if (fRedoAction != null) {
				fRedoAction.setEditingDomain(newValue);
			}
		}
	}

	public void commandStackChanged(EventObject event) {
		if (fUndoAction != null) {
			fUndoAction.update();
		}
		if (fRedoAction != null) {
			fRedoAction.update();
		}
		if (getCompareConfiguration().getEditingDomain() != null) {
			ICompareCommandStack commandStack = getCompareConfiguration().getEditingDomain()
					.getCommandStack();
			setLeftDirty(commandStack.isLeftSaveNeeded());
			setRightDirty(commandStack.isRightSaveNeeded());
		}

		IMergeViewerContentProvider contentProvider = (IMergeViewerContentProvider)getContentProvider();

		final String leftValueFromModel = getString(
				(IStreamContentAccessor)contentProvider.getLeftContent(getInput()));
		final String rightValueFromModel = getString(
				(IStreamContentAccessor)contentProvider.getRightContent(getInput()));

		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				String leftValueFromWidget = getContents(true, Charsets.UTF_8.name());
				String rightValueFromWidget = getContents(false, Charsets.UTF_8.name());
				IEqualityHelper equalityHelper = getCompareConfiguration().getComparison()
						.getEqualityHelper();
				if (!equalityHelper.matchingAttributeValues(leftValueFromModel, leftValueFromWidget)
						|| !equalityHelper.matchingAttributeValues(rightValueFromModel,
								rightValueFromWidget)) {
					// only refresh if values are different to avoid select-all of the text.
					refresh();
				}
			}
		});
	}

	// closed by Closeables
	private String getString(IStreamContentAccessor contentAccessor) {
		String ret = null;
		InputStream content = null;
		if (contentAccessor != null) {
			try {
				content = contentAccessor.getContents();
				ret = new String(ByteStreams.toByteArray(content), Charsets.UTF_8.name());
			} catch (CoreException e) {
			} catch (IOException e) {
			} finally {
				Closeables.closeQuietly(content);
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getCompareConfiguration()
	 */
	@Override
	protected EMFCompareConfiguration getCompareConfiguration() {
		return (EMFCompareConfiguration)super.getCompareConfiguration();
	}

	/**
	 * Inhibits this method to avoid asking to save on each input change!!
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#doSave(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected boolean doSave(Object newInput, Object oldInput) {
		return false;
	}

	protected String getContents(boolean isLeft, String charsetName) {
		try {
			return new String(getContents(isLeft), charsetName);
		} catch (UnsupportedEncodingException e) {
			// UTF_8 is a standard charset guaranteed to be supported by all Java platform
			// implementations
		}
		return null; // can not happen.
	}

	/**
	 * @return the fDynamicObject
	 */
	public DynamicObject getDynamicObject() {
		if (fDynamicObject == null) {
			this.fDynamicObject = new DynamicObject(this);
		}
		return fDynamicObject;
	}

	@SuppressWarnings("restriction")
	protected final MergeSourceViewer getAncestorSourceViewer() {
		return (MergeSourceViewer)getDynamicObject().get("fAncestor"); //$NON-NLS-1$
	}

	@SuppressWarnings("restriction")
	protected final MergeSourceViewer getLeftSourceViewer() {
		return (MergeSourceViewer)getDynamicObject().get("fLeft"); //$NON-NLS-1$
	}

	@SuppressWarnings("restriction")
	protected final MergeSourceViewer getRightSourceViewer() {
		return (MergeSourceViewer)getDynamicObject().get("fRight"); //$NON-NLS-1$
	}

	@SuppressWarnings("restriction")
	protected final CompareHandlerService getHandlerService() {
		return (CompareHandlerService)getDynamicObject().get("fHandlerService"); //$NON-NLS-1$
	}

	protected final void setHandlerService(@SuppressWarnings("restriction") CompareHandlerService service) {
		getDynamicObject().set("fHandlerService", service); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		super.createControls(composite);
		attachListeners(getAncestorSourceViewer(), MergeViewerSide.ANCESTOR);
		attachListeners(getLeftSourceViewer(), MergeViewerSide.LEFT);
		attachListeners(getRightSourceViewer(), MergeViewerSide.RIGHT);
	}

	protected void attachListeners(MergeSourceViewer viewer, final MergeViewerSide side) {
		// Nothing to do on the ancestor pane, which should not be edited
		if (viewer != null && (side == MergeViewerSide.LEFT || side == MergeViewerSide.RIGHT)) {
			final StyledText textWidget = viewer.getSourceViewer().getTextWidget();
			textWidget.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					getHandlerService().setGlobalActionHandler(ActionFactory.UNDO.getId(), null);
					getHandlerService().setGlobalActionHandler(ActionFactory.REDO.getId(), null);
				}

				public void focusGained(FocusEvent e) {
					getHandlerService().setGlobalActionHandler(ActionFactory.UNDO.getId(), fUndoAction);
					getHandlerService().setGlobalActionHandler(ActionFactory.REDO.getId(), fRedoAction);
				}
			});
			viewer.getSourceViewer().addTextListener(new ITextListener() {
				public void textChanged(TextEvent event) {
					final Object oldInput = getInput();
					if (event.getDocumentEvent() != null && isCompareInputAdapterHoldingDiff(oldInput)) {
						final CompareInputAdapter inputAdapter = (CompareInputAdapter)oldInput;
						final IModelUpdateStrategy modelUpdateStrategy = inputAdapter
								.getModelUpdateStrategy();
						final Diff diff = (Diff)inputAdapter.getComparisonObject();
						updateModel(diff, modelUpdateStrategy, MergeViewerSide.LEFT);
						updateModel(diff, modelUpdateStrategy, MergeViewerSide.RIGHT);
					}
				}
			});
		}
	}

	/**
	 * Specifies whether the given {@code input} is a {@link CompareInputAdapter} that holds a {@link Diff}.
	 * 
	 * @param input
	 *            Object to check.
	 * @return <code>true</code> if {@code input} is a {@link CompareInputAdapter} holding a a {@link Diff}.
	 */
	private boolean isCompareInputAdapterHoldingDiff(Object input) {
		return input instanceof CompareInputAdapter
				&& ((CompareInputAdapter)input).getComparisonObject() != null
				&& ((CompareInputAdapter)input).getComparisonObject() instanceof Diff;
	}

	/**
	 * Updates the underlying model with the given {@code modelUpdateStrategy} on the given {@code side} in
	 * the context of the given {@code diff}.
	 * 
	 * @param diff
	 *            The context of the model update.
	 * @param modelUpdateStrategy
	 *            The model update strategy to be used.
	 * @param side
	 *            The side on which to perform the udpate.
	 */
	private void updateModel(Diff diff, IModelUpdateStrategy modelUpdateStrategy, MergeViewerSide side) {
		if (isEditable(side) && modelUpdateStrategy.canUpdate(diff, side)) {
			final String newValue = getCurrentValueFromViewer(side);
			final Command updateCmd = modelUpdateStrategy.getModelUpdateCommand(diff, newValue, side);
			final RejectAffectedDiffCommand rejectDiffsCmd = new RejectAffectedDiffCommand(diff);
			final CompoundCommand compoundCmd = createCompoundCompareCommand(side, updateCmd, rejectDiffsCmd);
			getCompareConfiguration().getEditingDomain().getCommandStack().execute(compoundCmd);
		}
	}

	/**
	 * Specifies whether the content merge viewers on the given {@code side} are editable.
	 * 
	 * @param side
	 *            The side to check.
	 * @return <code>true</code> if the content merge viewer is editable on {@code side}, <code>false</code>
	 *         otherwise.
	 */
	private boolean isEditable(MergeViewerSide side) {
		final boolean isLeft = MergeViewerSide.LEFT.equals(side);
		return getCompareConfiguration().isEditable(isLeft);
	}

	/**
	 * Returns the current value from the viewer on the given {@code side}.
	 * 
	 * @param side
	 *            The side to get the value for.
	 * @return The content of the viewer on the given {@code side}.
	 */
	private String getCurrentValueFromViewer(MergeViewerSide side) {
		final boolean isLeft = MergeViewerSide.LEFT.equals(side);
		final GetContentRunnable runnable = new GetContentRunnable(isLeft);
		Display.getDefault().syncExec(runnable);
		return (String)runnable.getResult();
	}

	/**
	 * Creates a compound compare command for the given {@code side} and the given {@code commands}.
	 * 
	 * @param side
	 *            The side on which the compare command is to be executed.
	 * @param commands
	 *            The commands to be enclosed in the compound command.
	 * @return The compound compare command.
	 */
	private CompoundCompareCommand createCompoundCompareCommand(MergeViewerSide side, Command... commands) {
		final CompoundCompareCommand compoundCommand = new CompoundCompareCommand(side);
		for (Command command : commands) {
			compoundCommand.append(command);
		}
		return compoundCommand;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@SuppressWarnings("restriction")
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		super.createToolItems(toolBarManager);

		fRedoAction = new RedoAction(getCompareConfiguration().getEditingDomain());
		fUndoAction = new UndoAction(getCompareConfiguration().getEditingDomain());

		getHandlerService().setGlobalActionHandler(ActionFactory.UNDO.getId(), fUndoAction);
		getHandlerService().setGlobalActionHandler(ActionFactory.REDO.getId(), fRedoAction);
	}

	/**
	 * Called by the framework when the last (or first) diff of the current content viewer has been reached.
	 * This will open the content viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 */
	protected void endOfContentReached(boolean next) {
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			final ICompareNavigator navigator = getCompareConfiguration().getContainer().getNavigator();
			if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
				navigator.selectChange(next);
			}
		}
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
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			final ICompareNavigator navigator = getCompareConfiguration().getContainer().getNavigator();
			if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
				navigator.selectChange(next);
			}
		}
	}

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getCompareConfiguration().getEventBus().unregister(this);

		editingDomainChange(getCompareConfiguration().getEditingDomain(), null);

		fRedoAction = null;
		fUndoAction = null;

		super.handleDispose(event);
	}

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class GetContentRunnable implements Runnable {
		/**
		 * 
		 */
		private final boolean isLeft;

		private String result;

		/**
		 * @param isLeft
		 */
		private GetContentRunnable(boolean isLeft) {
			this.isLeft = isLeft;
		}

		public void run() {
			result = getContents(isLeft, Charsets.UTF_8.name());
		}

		public Object getResult() {
			return result;
		}
	}

	/**
	 * Command to directly modify the semantic model and reject the related difference.
	 * 
	 * @author cnotot
	 */
	private static class RejectAffectedDiffCommand extends ChangeCommand {

		private Diff difference;

		public RejectAffectedDiffCommand(Diff difference) {
			super(ImmutableSet.<Notifier> builder().addAll(getAffectedDiff(difference)).build());
			this.difference = difference;
		}

		@Override
		public void doExecute() {
			for (Diff affectedDiff : getAffectedDiff(difference)) {
				affectedDiff.setState(DifferenceState.DISCARDED);
			}
		}

		private static Set<Diff> getAffectedDiff(Diff diff) {
			EList<Conflict> conflicts = ComparisonUtil.getComparison(diff).getConflicts();
			for (Conflict conflict : conflicts) {
				EList<Diff> conflictualDifferences = conflict.getDifferences();
				if (conflictualDifferences.contains(diff)) {
					return ImmutableSet.copyOf(conflictualDifferences);
				}
			}
			return ImmutableSet.of(diff);
		}
	}

	/**
	 * A compound command that also implements the {@link ICompareCopyCommand} to be executable in the context
	 * of a comparison.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private static class CompoundCompareCommand extends CompoundCommand implements ICompareCopyCommand {

		/** The side on which this command executes. */
		private MergeViewerSide side;

		/**
		 * Creates a new compound command for the given {@code side}.
		 * 
		 * @param side
		 *            The side on which this command executes.
		 */
		public CompoundCompareCommand(MergeViewerSide side) {
			this.side = side;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isLeftToRight() {
			return !MergeViewerSide.LEFT.equals(side);
		}
	}

	@Override
	protected void flushContent(Object oldInput, IProgressMonitor monitor) {
		super.flushContent(oldInput, monitor);
		mergeResolutionManager.handleFlush(oldInput);
	}
}
