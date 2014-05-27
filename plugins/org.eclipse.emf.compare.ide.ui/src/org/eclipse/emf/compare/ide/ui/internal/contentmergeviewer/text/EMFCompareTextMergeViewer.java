/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.MergeSourceViewer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;
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

	private final DelayedExecutor fDelayedExecutor;

	private final ScheduledExecutorService fExecutorService;

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareTextMergeViewer(Composite parent, EMFCompareConfiguration configuration) {
		super(parent, configuration);
		setContentProvider(new EMFCompareTextMergeViewerContentProvider(configuration));

		fExecutorService = Executors.newSingleThreadScheduledExecutor();
		fDelayedExecutor = new DelayedExecutor(fExecutorService);

		editingDomainChange(null, configuration.getEditingDomain());

		configuration.getEventBus().register(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#flushContent(java.lang.Object,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void flushContent(Object oldInput, IProgressMonitor monitor) {
		try {
			fExecutorService.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			EMFCompareIDEUIPlugin.getDefault().log(e);
		}
		super.flushContent(oldInput, monitor);
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
			oldValue.getCommandStack().removeCommandStackListener(this);
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

		final String leftValueFromModel = getString((IStreamContentAccessor)contentProvider
				.getLeftContent(getInput()));
		final String rightValueFromModel = getString((IStreamContentAccessor)contentProvider
				.getRightContent(getInput()));

		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				String leftValueFromWidget = getContents(true, Charsets.UTF_8.name());
				String rightValueFromWidget = getContents(false, Charsets.UTF_8.name());
				IEqualityHelper equalityHelper = getCompareConfiguration().getComparison()
						.getEqualityHelper();
				if (!equalityHelper.matchingAttributeValues(leftValueFromModel, leftValueFromWidget)
						|| !equalityHelper.matchingAttributeValues(rightValueFromModel, rightValueFromWidget)) {
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

	private void updateModel(final AttributeChange diff, final EAttribute eAttribute,
			final IEqualityHelper equalityHelper, final EObject eObject, final boolean isLeft) {
		final String oldValue = getStringValue(eObject, eAttribute);
		GetContentRunnable runnable = new GetContentRunnable(isLeft);
		Display.getDefault().syncExec(runnable);
		String newValue = (String)runnable.getResult();

		final boolean oldAndNewEquals = equalityHelper.matchingAttributeValues(newValue, oldValue);
		if (eObject != null && !oldAndNewEquals && getCompareConfiguration().isLeftEditable()) {
			// Save the change on left side
			getCompareConfiguration().getEditingDomain().getCommandStack().execute(
					new UpdateModelAndRejectDiffCommand(getCompareConfiguration().getEditingDomain()
							.getChangeRecorder(), eObject, eAttribute, newValue, diff, isLeft));
		}
	}

	private String getStringValue(final EObject eObject, final EAttribute eAttribute) {
		final EDataType eAttributeType = eAttribute.getEAttributeType();
		final Object value;
		if (eObject == null) {
			value = null;
		} else {
			value = ReferenceUtil.safeEGet(eObject, eAttribute);
		}
		return EcoreUtil.convertToString(eAttributeType, value);
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
		attachListeners(getAncestorSourceViewer());
		attachListeners(getLeftSourceViewer());
		attachListeners(getRightSourceViewer());
	}

	protected void attachListeners(final MergeSourceViewer viewer) {
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
				if (event.getDocumentEvent() != null && oldInput instanceof CompareInputAdapter) {
					fDelayedExecutor.schedule(new Runnable() {
						public void run() {
							// When we leave the current input
							if (oldInput instanceof CompareInputAdapter) {
								final AttributeChange diff = (AttributeChange)((CompareInputAdapter)oldInput)
										.getComparisonObject();
								final EAttribute eAttribute = diff.getAttribute();
								final Match match = diff.getMatch();
								final IEqualityHelper equalityHelper = match.getComparison()
										.getEqualityHelper();

								updateModel(diff, eAttribute, equalityHelper, match.getLeft(), true);
								updateModel(diff, eAttribute, equalityHelper, match.getRight(), false);
							}
						}
					});
				}
			}
		});
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
		fExecutorService.shutdown();
		try {
			if (!fExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
				fExecutorService.shutdownNow();
				if (!fExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
					EMFCompareIDEUIPlugin.getDefault().log(IStatus.WARNING,
							"The executor of EMFCompareTextMergeViewer did not shutdown properly."); //$NON-NLS-1$
				}
			}
		} catch (InterruptedException e) {
			EMFCompareIDEUIPlugin.getDefault().log(e);
		}

		getCompareConfiguration().getEventBus().unregister(this);

		editingDomainChange(getCompareConfiguration().getEditingDomain(), null);

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
	private static class UpdateModelAndRejectDiffCommand extends ChangeCommand implements ICompareCopyCommand {

		private boolean isLeft;

		private Diff difference;

		private Object value;

		private EStructuralFeature feature;

		private EObject owner;

		public UpdateModelAndRejectDiffCommand(ChangeRecorder changeRecorder, EObject owner,
				EStructuralFeature feature, Object value, Diff difference, boolean isLeft) {
			super(changeRecorder, ImmutableSet.<Notifier> builder().add(owner).addAll(
					getAffectedDiff(difference)).build());
			this.owner = owner;
			this.feature = feature;
			this.value = value;
			this.difference = difference;
			this.isLeft = isLeft;
		}

		@Override
		public void doExecute() {
			owner.eSet(feature, value);
			for (Diff affectedDiff : getAffectedDiff(difference)) {
				affectedDiff.setState(DifferenceState.DISCARDED);
			}
		}

		private static Set<Diff> getAffectedDiff(Diff diff) {
			EList<Conflict> conflicts = diff.getMatch().getComparison().getConflicts();
			for (Conflict conflict : conflicts) {
				EList<Diff> conflictualDifferences = conflict.getDifferences();
				if (conflictualDifferences.contains(diff)) {
					return ImmutableSet.copyOf(conflictualDifferences);
				}
			}
			return ImmutableSet.of(diff);
		}

		public boolean isLeftToRight() {
			return !isLeft;
		}

	}

}
