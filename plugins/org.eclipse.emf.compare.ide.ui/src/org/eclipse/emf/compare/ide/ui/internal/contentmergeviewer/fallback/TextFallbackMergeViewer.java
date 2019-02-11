/*******************************************************************************
 * Copyright (c) 2017, 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.fallback;

import com.google.common.eventbus.Subscribe;

import java.util.EventObject;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ISharedDocumentAdapter;
import org.eclipse.compare.SharedDocumentAdapter;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.SharedDocumentAdapterWrapper;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.MirrorManager;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ForwardingCompareInput;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * A highly specialized implementation of a text merge viewer.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
@SuppressWarnings("restriction")
public class TextFallbackMergeViewer extends TextMergeViewer {

	static final String SHOW_PREVIEW = "SHOW_PREVIEW"; //$NON-NLS-1$

	/**
	 * We subvert the base class from seeing or notifying listeners. That's because editing the text makes the
	 * text dirty, but the EMF compare's editor should always reflect the dirty state of the command stack.
	 * 
	 * @see #addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 * @see #removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 * @see #updateDirtyState(ICompareCommandStack)
	 */
	@SuppressWarnings("rawtypes")
	private final ListenerList listenerList = new ListenerList();

	/**
	 * The original input passed to {@link #setInput(Object)}.
	 * 
	 * @see #getInput()
	 */
	private Object originalInput;

	/**
	 * The effective input computed during {@link #setInput(Object)} by
	 * {@link #getAdaptedCompareInput(CompareInputAdapter)}.
	 */
	private Object effectiveInput;

	/**
	 * The ancestor viewer in which to {@link #select(SourceViewer, EObject, Resource) select} objects.
	 * 
	 * @see #createSourceViewer(Composite, int)
	 */
	private SourceViewer ancesorViewer;

	/**
	 * The left viewer in which to {@link #select(SourceViewer, EObject, Resource) select} objects.
	 * 
	 * @see #createSourceViewer(Composite, int)
	 */
	private SourceViewer leftViewer;

	/**
	 * The right viewer in which to {@link #select(SourceViewer, EObject, Resource) select} objects.
	 * 
	 * @see #createSourceViewer(Composite, int)
	 */
	private SourceViewer rightViewer;

	/**
	 * The mirror manager used to control the {@link #setContentProvider(IContentProvider) content provider}
	 * and when {@link #handlePropertyChangeEvent(PropertyChangeEvent) handling mirror state changes}.
	 */
	private MirrorManager mirrorManager;

	/**
	 * Controls whether {@link #setContentProvider(IContentProvider) content provider changes} are respected
	 * or ignored.
	 * 
	 * @see #TextFallbackCompareViewerCreator(Composite, EMFCompareConfiguration)
	 * @see #handlePropertyChangeEvent(PropertyChangeEvent)
	 */
	private boolean ignoreContentProvideChanges = true;

	/**
	 * The item added by {@link #createToolItems(ToolBarManager)} and updated by {@link #updateToolItems()}.
	 * It's used to provide the ability to preview the contents of a resource as if it were saved.
	 */
	private ActionContributionItem previewItem;

	/**
	 * The command stack using during {@link #updateDirtyState(ICompareCommandStack) dirty state updates}.
	 */
	private ICompareCommandStack commandStackForNotification;

	/**
	 * A command stack listener that listens to the
	 * {@link #editingDomainChange(ICompareEditingDomain, ICompareEditingDomain) editing domain's command
	 * stack}. It {@link #updateDirtyState(ICompareCommandStack) updates the dirty state} and
	 * {@link #updateTitleImage() updates the title image}.
	 */
	private final CommandStackListener commandStackListener = new CommandStackListener() {
		public void commandStackChanged(EventObject event) {
			Object commandStack = event.getSource();
			if (commandStack instanceof ICompareCommandStack) {
				ICompareCommandStack compareCommandStack = (ICompareCommandStack)commandStack;
				updateDirtyState(compareCommandStack);
				updateTitleImage();
			}
			setInput(getOriginalInput());
		}
	};

	/**
	 * Creates an instance under the given parent using the given configuration.
	 * 
	 * @param parent
	 *            the parent composite under which to create this viewer.
	 * @param configuration
	 *            the EMF compare configuration used by this viewer.
	 */
	public TextFallbackMergeViewer(Composite parent, EMFCompareConfiguration configuration) {
		super(parent, configuration);

		// Register with the event bus.
		configuration.getEventBus().register(this);

		// Hook up the command stack listener to the editing domain's command stack.
		editingDomainChange(null, getCompareConfiguration().getEditingDomain());

		// Create a mirror manager to help manage the mirror state.
		mirrorManager = new MirrorManager(configuration);

		// Set our content provider, ensuring that it's not ignored during the update.
		ignoreContentProvideChanges = false;
		setContentProvider(new TextFallbackMergeViewerContentProvider(this));
		ignoreContentProvideChanges = true;
	}

	/**
	 * Listens to editing domain changes on the {@link EMFCompareConfiguration#getEventBus() event bus}.
	 * 
	 * @param event
	 *            the editing domain change event.
	 */
	@Subscribe
	public void handleEditingDomainChange(ICompareEditingDomainChange event) {
		editingDomainChange(event.getOldValue(), event.getNewValue());
	}

	/**
	 * Manages the {{@link #commandStackListener command stack listener} by removing it from the old editing
	 * domain's {@link ICompareEditingDomain#getCommandStack() command stack} and adding it to the new editing
	 * domain's command stack.
	 * 
	 * @param oldValue
	 *            the previous editing domain.
	 * @param newValue
	 *            the new editing domain.
	 */
	private void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		if (newValue != oldValue) {
			if (oldValue != null) {
				oldValue.getCommandStack().removeCommandStackListener(commandStackListener);
			}
			if (newValue != null) {
				newValue.getCommandStack().addCommandStackListener(commandStackListener);
				updateDirtyState(newValue.getCommandStack());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation subverts calls to {@code super} so that nothing can actually listen to underlying
	 * state changes to the dirty state of this viewer's source viewers. It manages its own
	 * {@link #listenerList listeners} and {@link #updateDirtyState(ICompareCommandStack) informs listeners of
	 * the dirty state} based on changes to {@link #commandStackListener command stack state}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#removePropertyChangeListener(IPropertyChangeListener)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation subverts calls to {@code super} so that nothing can actually listen to underlying
	 * state changes to the dirty state of this viewer's source viewers. It manages its own
	 * {@link #listenerList listeners}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#removePropertyChangeListener(IPropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.remove(listener);
	}

	/**
	 * {@link Utilities#firePropertyChange(ListenerList, Object, String, Object, Object) Fires} a
	 * {@link CompareEditorInput#DIRTY_STATE dirty state} event to the {@link #listenerList listeners} of this
	 * viewer. It ensures that calls to the {link {@link #isLeftDirty()} and {@link #isRightDirty()} return
	 * the state of the {@link #commandStackForNotification command stack}.
	 * 
	 * @param commandStack
	 *            the command stack whose state should be used to update dirtiness.
	 */
	@SuppressWarnings("unchecked")
	private void updateDirtyState(ICompareCommandStack commandStack) {
		// Don't change the dirty state of the part itself, because that's managed by actual changes to
		// the source viewers.
		this.commandStackForNotification = commandStack;
		Utilities.firePropertyChange(listenerList, this, CompareEditorInput.DIRTY_STATE, null,
				Boolean.valueOf(commandStack.isLeftSaveNeeded() || commandStack.isRightSaveNeeded()));
		this.commandStackForNotification = null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns the {@link ICompareCommandStack#isLeftSaveNeeded() state} of the
	 * {@link #commandStackForNotification command stack} when the command stack is
	 * {@link #updateDirtyState(ICompareCommandStack) updating the dirty state}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#isLeftDirty()
	 */
	@Override
	protected boolean isLeftDirty() {
		if (commandStackForNotification == null) {
			return super.isLeftDirty();
		} else {
			return commandStackForNotification.isLeftSaveNeeded();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation {@link #updateHeader() updates the header} because the label
	 * {@link TextFallbackCompareInputLabelProvider#getLabel(Resource, IStorage, boolean) includes a dirty
	 * state indication}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#setLeftDirty(boolean)
	 */
	@Override
	protected void setLeftDirty(boolean dirty) {
		super.setLeftDirty(dirty);
		updateHeader();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns the {@link ICompareCommandStack#isRightSaveNeeded() state} of the
	 * {@link #commandStackForNotification command stack} when the command stack is
	 * {@link #updateDirtyState(ICompareCommandStack) updating the dirty state}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#isRightDirty()
	 */
	@Override
	protected boolean isRightDirty() {
		if (commandStackForNotification == null) {
			return super.isRightDirty();
		} else {
			return commandStackForNotification.isRightSaveNeeded();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation {@link #updateHeader() updates the header} because the label
	 * {@link TextFallbackCompareInputLabelProvider#getLabel(Resource, IStorage, boolean) includes a dirty
	 * state indication}.
	 * </p>
	 * 
	 * @see ContentMergeViewer#setRightDirty(boolean)
	 */
	@Override
	protected void setRightDirty(boolean dirty) {
		super.setRightDirty(dirty);
		updateHeader();
	}

	/**
	 * Updates the title image on the {@link CompareViewerSwitchingPane compare viewer switching pane}. This
	 * is needed when the {@link #commandStackListener command stack changes state} and when
	 * {@link #handlePropertyChangeEvent(PropertyChangeEvent) handling mirror direction changes}. The image
	 * often includes a directional indicator or a {@link Diff#getState() difference resolution state} that
	 * changes.
	 */
	private void updateTitleImage() {
		if (getInput() instanceof ICompareInput) {
			Composite parent = getControl().getParent();
			if (parent instanceof CompareViewerSwitchingPane) {
				CompareViewerSwitchingPane switchingPane = (CompareViewerSwitchingPane)parent;
				switchingPane.setImage(((ICompareInput)getInput()).getImage());
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation also creates a {@link #previewItem preview action} that's useful for showing the
	 * saved contents of resource in their current state of modification.
	 * </p>
	 * 
	 * @see TextMergeViewer#createToolItems(ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager tbm) {
		super.createToolItems(tbm);

		Action previewAction = new Action() {
			{
				setChecked(getCompareConfiguration().getBooleanProperty(SHOW_PREVIEW, true));
				setImageDescriptor(
						EMFCompareIDEUIPlugin.getImageDescriptor("icons/full/toolb16/show_preview.gif")); //$NON-NLS-1$
				updateToolTipText();
			}

			@Override
			public void run() {
				getCompareConfiguration().setProperty(SHOW_PREVIEW, Boolean.valueOf(isChecked()));
				setInput(getOriginalInput());
				updateToolTipText();
			}

			private void updateToolTipText() {
				if (isChecked()) {
					setToolTipText(
							EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.hidePreviewLabel")); //$NON-NLS-1$
				} else {
					setToolTipText(
							EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.showPreviewLabel")); //$NON-NLS-1$
				}
			}
		};

		tbm.appendToGroup("modes", previewAction); //$NON-NLS-1$
		previewItem = new ActionContributionItem(previewAction);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation also updates the {@link #previewItem preview action}.
	 * </p>
	 * 
	 * @see TextMergeViewer#updateToolItems()
	 */
	@Override
	protected void updateToolItems() {
		previewItem.setVisible(getEffectiveInput() instanceof TextFallbackCompareInput);
		super.updateToolItems();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation assumes the configuration must be an {@link EMFCompareConfiguration}, returning it
	 * as such.
	 * </p>
	 * 
	 * @see ContentMergeViewer#getCompareConfiguration()
	 */
	@Override
	protected EMFCompareConfiguration getCompareConfiguration() {
		return (EMFCompareConfiguration)super.getCompareConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This specialized implementation {@link #ignoreContentProvideChanges ignores} content provider changes
	 * except when explicitly set in the
	 * {@link #TextFallbackCompareViewerCreator(Composite, EMFCompareConfiguration) constructor} and when this
	 * implementation is {@link #handlePropertyChangeEvent(PropertyChangeEvent) handling mirror changes}.
	 * </p>
	 * 
	 * @see TextMergeViewer#setContentProvider(IContentProvider)
	 */
	@Override
	public void setContentProvider(IContentProvider contentProvider) {
		if (!ignoreContentProvideChanges) {
			if (mirrorManager != null) {
				super.setContentProvider(mirrorManager.getContentProvider(contentProvider));
			} else {
				super.setContentProvider(contentProvider);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation is specialized to {@link #getAdaptedCompareInput(CompareInputAdapter) adapt} the
	 * input and to {@link #select(SourceViewer, EObject, Resource) select} the objects of the input.
	 * </p>
	 * 
	 * @see TextMergeViewer#setInput(Object)
	 */
	@Override
	public void setInput(Object input) {
		Control control = getControl();
		try {
			// Disable painting to reduce flicker during the update process.
			control.setRedraw(false);

			// We set this to null while calling the super methods.
			// This ensures that getInput() will return the actual old input during the input switching
			// process until the super call has actually set the field and is returning the input we've
			// just set.
			// If we don't do this, any edits will not be saved when the user is prompted whether to save
			// the changes or discard them because that relies on the actual old input.
			setOriginalInput(null);
			if (input instanceof CompareInputAdapter) {
				setEffectiveInput(getAdaptedCompareInput((CompareInputAdapter)input));
			} else if (input instanceof ForwardingCompareInput) {
				setEffectiveInput(((ForwardingCompareInput)input).delegate());
			} else {
				setEffectiveInput(input);
			}
			super.setInput(getEffectiveInput());

			// From this point forward, getInput() will return the original input.
			// This ensures that when we switch to a different view that disposes this view,
			// the input for that new view is this original input.
			setOriginalInput(input);

			// If we have a text compare input...
			if (getEffectiveInput() instanceof TextFallbackCompareInput) {
				TextFallbackCompareInput textCompareInput = (TextFallbackCompareInput)getEffectiveInput();
				TextFallbackCompareInputData textInputData = textCompareInput.getTextInputData();
				// Select the objects on each of the sides.
				select(leftViewer, textInputData.getLeft(), textInputData.getLeftResource());
				select(rightViewer, textInputData.getRight(), textInputData.getRightResource());
				select(ancesorViewer, textInputData.getOrigin(), textInputData.getOriginResource());
			}
		} finally {
			control.setRedraw(true);
		}
	}

	/**
	 * Adapts the real input passed to {@link #setInput(Object)} to an input more appropriate for this viewer.
	 * 
	 * @param input
	 *            a compare input adapter.
	 * @return the input adapted to the appropriate input to use for this viewer.
	 * @see TextFallbackCompareInputData
	 * @see TextFallbackCompareInput
	 */
	private ICompareInput getAdaptedCompareInput(CompareInputAdapter input) {
		ICompareInput adaptedCompareInput;
		Notifier target = input.getTarget();
		// If this is an adapter for a tree node....
		if (target instanceof TreeNode) {
			// Get the tree node's data and determine its comparison; we generally expect that to never be
			// null.
			TreeNode treeNode = (TreeNode)target;
			EObject data = treeNode.getData();
			Comparison comparison = ComparisonUtil.getComparison(data);
			if (comparison != null) {
				// Adapt the input and if it's a forwarding compare input, unwrap the delegate.
				ICompareInput compareInput = (ICompareInput)EcoreUtil.getAdapter(comparison.eAdapters(),
						ICompareInput.class);
				if (compareInput instanceof ForwardingCompareInput) {
					adaptedCompareInput = ((ForwardingCompareInput)compareInput).delegate();
				} else {
					adaptedCompareInput = compareInput;
				}
				// Compute the most appropriate text input data from the tree node's data.
				// Only use it as the adapted compare input if it succeeds to compute at least one typed
				// element for ones of the sides.
				TextFallbackCompareInputData textInputData = new TextFallbackCompareInputData(data);
				if (textInputData.hasTypedElement()) {
					adaptedCompareInput = new TextFallbackCompareInput(adaptedCompareInput.getKind(),
							textInputData, getCompareConfiguration().getBooleanProperty(SHOW_PREVIEW, true));
				}
			} else {
				adaptedCompareInput = null;
				EMFCompareIDEUIPlugin.getDefault().log(IStatus.ERROR,
						"Cannot find a comparison from input " + input); //$NON-NLS-1$
			}
		} else {
			adaptedCompareInput = null;
		}
		return adaptedCompareInput;
	}

	/**
	 * Selects the the source viewer's text line closest to the given object of the given resource. If that
	 * line contains a difference, that difference is also selected.
	 * 
	 * @param sourceViewer
	 *            the source viewer on which to operate.
	 * @param eObject
	 *            the object to be selected.
	 * @param resource
	 *            the resource containing the object.
	 */
	private void select(SourceViewer sourceViewer, EObject eObject, Resource resource) {
		IDocument document = sourceViewer.getDocument();
		int offset = getOffset(eObject, resource, document.get());
		if (offset != -1) {
			sourceViewer.setSelectedRange(offset, 0);
			sourceViewer.setSelection(sourceViewer.getSelection(), true);

			// The viewer listens for key events for keyboard navigation updates to the selected
			// difference and this approach updates that selection based on the current selected range.
			Event event = new Event();
			StyledText textWidget = sourceViewer.getTextWidget();
			textWidget.notifyListeners(SWT.KeyDown, event);
			textWidget.notifyListeners(SWT.KeyUp, event);
		}
	}

	/**
	 * Returns the offset of the start of the line containing then object of the given resource serialized for
	 * the given text.
	 * 
	 * @param eObject
	 *            the object to be selected.
	 * @param resource
	 *            the resource containing the object.
	 * @param text
	 *            the text of the serialized resource.
	 * @return the offset of the start of the line containing then object of the given resource serialized for
	 *         the given text, or {@code -1} if the object can't be located.
	 */
	private int getOffset(EObject eObject, Resource resource, String text) {
		int offset = -1;
		if (resource instanceof XMLResource && eObject != null) {
			// This only works if there is an object and the resource is an XML Resource that has an
			// extrinsic ID for the object.
			XMLResource xmlResource = (XMLResource)resource;
			String id = xmlResource.getID(eObject);
			if (id != null) {
				// Look for the expected form of the ID in the serialization.
				offset = text.indexOf("xmi:id=\"" + id + '"'); //$NON-NLS-1$
				if (offset != -1) {
					// If we find it, iterate back to the start of the line.
					while (offset > 0) {
						char c = text.charAt(offset - 1);
						if (c == '\n' || c == '\r') {
							break;
						}
						--offset;
					}
				}
			}
		}
		return offset;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation simply delegates to {@code super} but it is used to record the
	 * {@link #ancesorViewer}, {@link #leftViewer}, and {@link #rightViewer} which are needed in
	 * {@link #setInput(Object)} to {@link #select(SourceViewer, EObject, Resource) select} objects.
	 * </p>
	 * 
	 * @see TextMergeViewer#createSourceViewer(Composite, int)
	 */
	@Override
	protected SourceViewer createSourceViewer(Composite parent, int textOrientation) {
		SourceViewer sourceViewer = super.createSourceViewer(parent, textOrientation);
		if (ancesorViewer == null) {
			ancesorViewer = sourceViewer;
		} else if (leftViewer == null) {
			leftViewer = sourceViewer;
		} else {
			rightViewer = sourceViewer;
		}
		return sourceViewer;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This class' specialized {@link #setInput(Object) setInput} method transforms the real input, so it's
	 * important that we return the {@link #originalInput} when that isn't {@code null}.
	 * </p>
	 * 
	 * @see #setInput(Object)
	 * @see org.eclipse.jface.viewers.ContentViewer#getInput()
	 */
	@Override
	public Object getInput() {
		if (getOriginalInput() == null) {
			return super.getInput();
		} else {
			return getOriginalInput();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		super.handleDispose(event);

		// Disconnect from the editing domain.
		EMFCompareConfiguration configuration = getCompareConfiguration();
		editingDomainChange(configuration.getEditingDomain(), null);

		// Disconnect from the event bus.
		configuration.getEventBus().unregister(this);

		// Stop using this configuration.
		configuration.disposeListeners();

		// Clean up the inputs.
		setOriginalInput(null);
		setEffectiveInput(null);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation uses the {@link #mirrorManager mirror manager} to subvert {@code super's} handling
	 * of mirror state changes so that it can handle the state change in a better way than does the base
	 * class.
	 * </p>
	 * 
	 * @see TextMergeViewer#handlePropertyChangeEvent(PropertyChangeEvent)
	 */
	@Override
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (mirrorManager != null && mirrorManager.handlePropertyChangeEvent(event)) {
			// Connect to both left and right document providers so the documents aren't released during
			// the switching process.
			Object oldInput = getInput();
			IMergeViewerContentProvider oldContentProvider = (IMergeViewerContentProvider)getContentProvider();
			Runnable leftDocumentConnection = connectDocumentProvider(
					oldContentProvider.getLeftContent(oldInput));
			Runnable rightDocumentConnection = connectDocumentProvider(
					oldContentProvider.getRightContent(oldInput));
			try {
				ignoreContentProvideChanges = false;
				setContentProvider(mirrorManager.getContentProvider(oldContentProvider));

				// Update the title image because it may include a directional overlay that has changed.
				updateTitleImage();
			} finally {
				ignoreContentProvideChanges = true;
				// Disconnect them again.
				if (leftDocumentConnection != null) {
					leftDocumentConnection.run();
				}
				if (rightDocumentConnection != null) {
					rightDocumentConnection.run();
				}
			}

			// Update the tool items, many of which include directional indication.
			updateToolItems();
		} else {
			super.handlePropertyChangeEvent(event);
		}
	}

	/**
	 * Opens a connection to the document provider and returns a runnable that will disconnect it.
	 * 
	 * @param element
	 *            the left or right element.
	 * @return a runnable to disconnect the connection, or null, if no connection was established.
	 */
	private Runnable connectDocumentProvider(Object element) {
		if (element != null) {
			final ISharedDocumentAdapter sharedDocumentationAdapter = SharedDocumentAdapterWrapper
					.getAdapter(element);
			if (sharedDocumentationAdapter != null) {
				final IEditorInput documentKey = sharedDocumentationAdapter.getDocumentKey(element);
				if (documentKey != null) {
					final IDocumentProvider documentProvider = SharedDocumentAdapter
							.getDocumentProvider(documentKey);
					if (documentProvider != null) {
						try {
							sharedDocumentationAdapter.connect(documentProvider, documentKey);
							return new Runnable() {
								public void run() {
									sharedDocumentationAdapter.disconnect(documentProvider, documentKey);
								}
							};
						} catch (CoreException e) {
							EMFCompareIDEUIPlugin.getDefault().log(e);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getTitle()
	 */
	@Override
	public String getTitle() {
		return EMFCompareIDEUIMessages.getString("TextFallbackCompareViewer.title"); //$NON-NLS-1$
	}

	public Object getOriginalInput() {
		return originalInput;
	}

	private void setOriginalInput(Object originalInput) {
		this.originalInput = originalInput;
	}

	public Object getEffectiveInput() {
		return effectiveInput;
	}

	private void setEffectiveInput(Object effectiveInput) {
		this.effectiveInput = effectiveInput;
	}
}
