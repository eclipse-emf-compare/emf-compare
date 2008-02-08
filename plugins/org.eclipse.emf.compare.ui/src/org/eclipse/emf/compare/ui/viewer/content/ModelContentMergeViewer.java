/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.HistoryItem;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompareException;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.metamodel.util.DiffAdapterFactory;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.api.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.AbstractCompareAction;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.ICompareEditorPartListener;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.TypedElementWrapper;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewer.content.part.AbstractCenterPart;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabItem;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.ui.PlatformUI;

/**
 * Compare and merge viewer with two side-by-side content areas and an optional content area for the ancestor.
 * getKind
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class ModelContentMergeViewer extends ContentMergeViewer {
	/** Name of the bundle resources property file. */
	public static final String BUNDLE_NAME = "org.eclipse.emf.compare.ui.viewer.content.ModelMergeViewerResources"; //$NON-NLS-1$

	/** Width to affect to the center area. */
	public static final int CENTER_WIDTH = 34;

	/** Keeps references to the colors to use when drawing differences markers. */
	/* package */static Map<String, RGB> colors = new EMFCompareMap<String, RGB>();

	/**
	 * Indicates that the diff markers should be drawn. This allows defining a threshold to avoid too long
	 * drawing times.
	 */
	private static boolean drawDiffMarkers;

	/** Keeps track of the currently selected tab for this viewer part. */
	protected int activeTabIndex;

	/** Ancestor part of the three possible parts of this content viewer. */
	protected ModelContentMergeTabFolder ancestorPart;

	/**
	 * Ancestor model used for the comparison if it takes place here instead of in the structure viewer's
	 * content provider.
	 */
	protected Resource ancestorResource;

	/** Keeps track of the current diff Selection. */
	protected final List<DiffElement> currentSelection = new ArrayList<DiffElement>();

	/** Indicates that this is a three way comparison. */
	protected boolean isThreeWay;

	/** Left of the three possible parts of this content viewer. */
	protected ModelContentMergeTabFolder leftPart;

	/**
	 * Left model used for the comparison if it takes place here instead of in the structure viewer's content
	 * provider.
	 */
	protected Resource leftResource;

	/** Right of the three possible parts of this content viewer. */
	protected ModelContentMergeTabFolder rightPart;

	/**
	 * Right model used for the comparison if it takes place here instead of in the structure viewer's content
	 * provider.
	 */
	protected Resource rightResource;

	/**
	 * this is the "center" part of the content merge viewer where we handle all the drawing operations.
	 */
	private AbstractCenterPart canvas;

	/**
	 * {@link CompareConfiguration} controls various aspect of the GUI elements. This will keep track of the
	 * one used to created this compare editor.
	 */
	private final CompareConfiguration configuration;

	/**
	 * This is the action we instantiate to handle the {@link DiffElement}s merge from the left model to the
	 * right model.
	 */
	private Action copyDiffLeftToRight;

	/**
	 * This is the action we instantiate to handle the {@link DiffElement}s merge from the right model to the
	 * left model.
	 */
	private Action copyDiffRightToLeft;

	/**
	 * Used for history comparisons, this will keep track of modification time of the last {@link HistoryItem}
	 * we compared.
	 */
	private long lastHistoryItemDate;

	/**
	 * Indicates that the left model has been modified since opening. Will allow us to prompt the user to save
	 * this model.
	 */
	private boolean leftDirty;

	/**
	 * This will listen for changes made on this plug-in's {@link PreferenceStore} to update the GUI colors as
	 * needed.
	 */
	private final IPropertyChangeListener preferenceListener;

	/**
	 * Indicates that the right model has been modified since opening. Will allow us to prompt the user to
	 * save this model.
	 */
	private boolean rightDirty;

	/**
	 * This will listen for changes of the {@link CompareConfiguration} concerning the structure's input and
	 * selection.
	 */
	private final IPropertyChangeListener structureSelectionListener;

	/**
	 * Creates a new model content merge viewer and intializes it.
	 * 
	 * @param parent
	 *            Parent composite for this viewer.
	 * @param config
	 *            The configuration object.
	 */
	public ModelContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		configuration = config;
		buildControl(parent);
		updatePreferences();
		setContentProvider(new ModelContentMergeContentProvider(config));

		// disables diff copy from either side
		switchCopyState(false);

		structureSelectionListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFCompareConstants.PROPERTY_STRUCTURE_SELECTION)) {
					final List<?> elements = (List<?>)event.getNewValue();
					// We'll remove all diffgroups without subDiffs from the selection
					final List<DiffElement> selectedDiffs = new ArrayList<DiffElement>();
					for (int i = 0; i < elements.size(); i++) {
						if (elements.get(i) instanceof DiffElement
								&& !(elements.get(i) instanceof DiffGroup && ((DiffGroup)elements.get(i))
										.getSubDiffElements().size() == 0))
							selectedDiffs.add((DiffElement)elements.get(i));
					}
					setSelection(selectedDiffs);
				}
			}
		};
		configuration.addPropertyChangeListener(structureSelectionListener);

		preferenceListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().matches(".*(color|differences)")) { //$NON-NLS-1$
					updatePreferences();
				}
			}
		};
		EMFCompareUIPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceListener);
	}

	/**
	 * Returns the color identified by the given key in {@link #colors}.
	 * 
	 * @param key
	 *            Key of the color to return.
	 * @return The color identified by the given key in the map.
	 */
	public static final RGB getColor(String key) {
		return colors.get(key);
	}

	/**
	 * Returns <code>True</code> if the trees and center have to draw markers over the differences.
	 * 
	 * @return <code>True</code> if the trees and center have to draw markers over the differences,
	 *         <code>False</code> otherwise.
	 */
	public static boolean shouldDrawDiffMarkers() {
		return drawDiffMarkers;
	}

	/**
	 * Returns the center {@link Canvas} appearing between the vewer parts.
	 * 
	 * @return The center {@link Canvas}.
	 */
	public Canvas getCenterPart() {
		if (canvas == null && !getControl().isDisposed())
			canvas = new AbstractCenterPart((Composite)getControl()) {
				@Override
				public void doPaint(GC gc) {
					if (!ModelContentMergeViewer.shouldDrawDiffMarkers() || getInput() == null)
						return;
					final List<DiffElement> diffList = new ArrayList<DiffElement>(((ModelCompareInput)getInput()).getDiffAsList());
					final List<ModelContentMergeTabItem> leftVisible = leftPart.getVisibleElements();
					final List<ModelContentMergeTabItem> rightVisible = rightPart.getVisibleElements();
					diffList.removeAll(currentSelection);
					final List<DiffElement> visibleDiffs = retainVisibleDiffs(diffList, leftVisible,
							rightVisible);
					// we don't clear selection when the last diff is merged so this could happen
					if (currentSelection.size() > 0 && currentSelection.get(0).eContainer() != null)
						visibleDiffs.addAll(currentSelection);
					diffList.clear();
					for (final DiffElement diff : visibleDiffs) {
						if (!(diff instanceof DiffGroup)) {
							final ModelContentMergeTabItem leftUIItem = leftPart.getUIItem(diff);
							final ModelContentMergeTabItem rightUIItem = rightPart.getUIItem(diff);
							if (leftUIItem != null && rightUIItem != null)
								drawLine(gc, leftUIItem, rightUIItem);
						}
					}
				}
			};
		if (canvas != null)
			canvas.moveAbove(null);
		return canvas;
	}

	/**
	 * Returns the compare configuration of this viewer.
	 * 
	 * @return The compare configuration of this viewer.
	 */
	public CompareConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInput(Object input) {
		// We won't compare again if the given input is the same as the last.
		boolean changed = false;
		if (input instanceof ICompareInput && ((ICompareInput)input).getRight() instanceof HistoryItem) {
			changed = lastHistoryItemDate != ((HistoryItem)((ICompareInput)input).getRight())
					.getModificationDate();
			if (changed)
				lastHistoryItemDate = ((HistoryItem)((ICompareInput)input).getRight()).getModificationDate();
		}
		if (configuration.getProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT) != null && !changed) {
			final ModelInputSnapshot snapshot = (ModelInputSnapshot)configuration
					.getProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT);
			super.setInput(new ModelCompareInput(snapshot.getMatch(), snapshot.getDiff()));
		} else if (input instanceof ModelInputSnapshot) {
			final ModelInputSnapshot snapshot = (ModelInputSnapshot)input;
			super.setInput(new ModelCompareInput(snapshot.getMatch(), snapshot.getDiff()));
		} else if (input instanceof ICompareInput) {
			final ITypedElement left = ((ICompareInput)input).getLeft();
			final ITypedElement right = ((ICompareInput)input).getRight();
			final ITypedElement ancestor = ((ICompareInput)input).getAncestor();

			if (ancestor != null)
				isThreeWay = true;

			prepareComparison(left, right, ancestor);
			doCompare();
		}
	}

	/**
	 * Sets the parts' tree selection given the {@link DiffElement} to select.
	 * 
	 * @param diff
	 *            {@link DiffElement} backing the current selection.
	 */
	public void setSelection(DiffElement diff) {
		final List<DiffElement> diffs = new ArrayList<DiffElement>();
		diffs.add(diff);
		setSelection(diffs);
	}

	/**
	 * Sets the parts' tree selection given the list of {@link DiffElement}s to select.
	 * 
	 * @param diffs
	 *            {@link DiffElement} backing the current selection.
	 */
	public void setSelection(List<DiffElement> diffs) {
		currentSelection.clear();
		if (diffs.size() > 0) {
			currentSelection.addAll(diffs);
			if (leftPart != null)
				leftPart.navigateToDiff(diffs);
			if (rightPart != null)
				rightPart.navigateToDiff(diffs);
			if (isThreeWay && diffs.get(0).eContainer() instanceof ConflictingDiffElement)
				ancestorPart.navigateToDiff(diffs.get(0));
			switchCopyState(true);
		}
	}

	/**
	 * Redraws this viewer.
	 */
	public void update() {
		if (isThreeWay)
			ancestorPart.layout();
		rightPart.layout();
		leftPart.layout();
		updateCenter();
		updateToolItems();
	}

	/**
	 * Redraws the center Control.
	 */
	public void updateCenter() {
		if (getCenterPart() != null)
			getCenterPart().redraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		((ModelCompareInput)getInput()).copy(leftToRight);
		final ModelInputSnapshot snap = DiffFactory.eINSTANCE.createModelInputSnapshot();
		snap.setDiff(((ModelCompareInput)getInput()).getDiff());
		snap.setMatch(((ModelCompareInput)getInput()).getMatch());
		configuration.setProperty(EMFCompareConstants.PROPERTY_CONTENT_INPUT_CHANGED, snap);
		leftDirty = leftDirty || leftToRight;
		rightDirty = rightDirty || !leftToRight;
		setRightDirty(leftDirty);
		setLeftDirty(rightDirty);
		update();
	}

	/**
	 * Copies a single {@link DiffElement} or a {@link DiffGroup} in the given direction, then updates the
	 * toolbar items states as well as the dirty state of both the left and the right models.
	 * 
	 * @param diff
	 *            {@link DiffElement Element} to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 * @see ModelCompareInput#copy(DiffElement, boolean)
	 */
	protected void copy(DiffElement diff, boolean leftToRight) {
		if (diff != null) {
			((ModelCompareInput)getInput()).copy(diff, leftToRight);
			final ModelInputSnapshot snap = DiffFactory.eINSTANCE.createModelInputSnapshot();
			snap.setDiff(((ModelCompareInput)getInput()).getDiff());
			snap.setMatch(((ModelCompareInput)getInput()).getMatch());
			configuration.setProperty(EMFCompareConstants.PROPERTY_CONTENT_INPUT_CHANGED, snap);
			leftDirty = leftDirty || (leftToRight && configuration.isLeftEditable());
			rightDirty = rightDirty || (!leftToRight && configuration.isRightEditable());
			setRightDirty(leftDirty);
			setLeftDirty(rightDirty);
			update();
		}
	}

	/**
	 * Copies a list of {@link DiffElement}s or {@link DiffGroup}s in the given direction, then updates the
	 * toolbar items states as well as the dirty state of both the left and the right models.
	 * 
	 * @param diffs
	 *            {@link DiffElement Element}s to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 * @see ModelCompareInput#copy(List, boolean)
	 */
	protected void copy(List<DiffElement> diffs, boolean leftToRight) {
		if (diffs.size() > 0) {
			((ModelCompareInput)getInput()).copy(diffs, leftToRight);
			final ModelInputSnapshot snap = DiffFactory.eINSTANCE.createModelInputSnapshot();
			snap.setDiff(((ModelCompareInput)getInput()).getDiff());
			snap.setMatch(((ModelCompareInput)getInput()).getMatch());
			configuration.setProperty(EMFCompareConstants.PROPERTY_CONTENT_INPUT_CHANGED, snap);
			leftDirty = leftDirty || (leftToRight && configuration.isLeftEditable());
			rightDirty = rightDirty || (!leftToRight && configuration.isRightEditable());
			setRightDirty(leftDirty);
			setLeftDirty(rightDirty);
			update();
		}
	}

	/**
	 * Undoes the changes implied by the currently selected {@link DiffElement diff}.
	 */
	protected void copyDiffLeftToRight() {
		if (currentSelection != null)
			copy(currentSelection, true);
		currentSelection.clear();
		switchCopyState(false);
	}

	/**
	 * Applies the changes implied by the currently selected {@link DiffElement diff}.
	 */
	protected void copyDiffRightToLeft() {
		if (currentSelection != null)
			copy(currentSelection, false);
		currentSelection.clear();
		switchCopyState(false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#createControls(Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		leftPart = new ModelContentMergeTabFolder(this, composite, EMFCompareConstants.LEFT);
		rightPart = new ModelContentMergeTabFolder(this, composite, EMFCompareConstants.RIGHT);
		ancestorPart = new ModelContentMergeTabFolder(this, composite, EMFCompareConstants.ANCESTOR);

		final EditorPartListener partListener = new EditorPartListener(leftPart, rightPart, ancestorPart);
		leftPart.addCompareEditorPartListener(partListener);
		rightPart.addCompareEditorPartListener(partListener);
		ancestorPart.addCompareEditorPartListener(partListener);

		createPropertiesSyncHandlers(leftPart, rightPart, ancestorPart);
		createTreeSyncHandlers(leftPart, rightPart, ancestorPart);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#createToolItems(ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager tbm) {
		// COPY DIFF LEFT TO RIGHT
		if (getCompareConfiguration().isRightEditable()) {
			copyDiffLeftToRight = new AbstractCompareAction(ResourceBundle.getBundle(BUNDLE_NAME),
					"action.CopyDiffLeftToRight.") { //$NON-NLS-1$
				@Override
				public void run() {
					copyDiffLeftToRight();
				}
			};
			final ActionContributionItem copyLeftToRightContribution = new ActionContributionItem(
					copyDiffLeftToRight);
			copyLeftToRightContribution.setVisible(true);
			tbm.appendToGroup("merge", copyLeftToRightContribution); //$NON-NLS-1$
		}
		// COPY DIFF RIGHT TO LEFT
		if (getCompareConfiguration().isLeftEditable()) {
			copyDiffRightToLeft = new AbstractCompareAction(ResourceBundle.getBundle(BUNDLE_NAME),
					"action.CopyDiffRightToLeft.") { //$NON-NLS-1$
				@Override
				public void run() {
					copyDiffRightToLeft();
				}
			};
			final ActionContributionItem copyRightToLeftContribution = new ActionContributionItem(
					copyDiffRightToLeft);
			copyRightToLeftContribution.setVisible(true);
			tbm.appendToGroup("merge", copyRightToLeftContribution); //$NON-NLS-1$
		}
		// NEXT DIFF
		final Action nextDiff = new AbstractCompareAction(ResourceBundle.getBundle(BUNDLE_NAME),
				"action.NextDiff.") { //$NON-NLS-1$
			@Override
			public void run() {
				navigate(true);
			}
		};
		final ActionContributionItem nextDiffContribution = new ActionContributionItem(nextDiff);
		nextDiffContribution.setVisible(true);
		tbm.appendToGroup("navigation", nextDiffContribution); //$NON-NLS-1$
		// PREVIOUS DIFF
		final Action previousDiff = new AbstractCompareAction(ResourceBundle.getBundle(BUNDLE_NAME),
				"action.PrevDiff.") { //$NON-NLS-1$
			@Override
			public void run() {
				navigate(false);
			}
		};
		final ActionContributionItem previousDiffContribution = new ActionContributionItem(previousDiff);
		previousDiffContribution.setVisible(true);
		tbm.appendToGroup("navigation", previousDiffContribution); //$NON-NLS-1$
	}

	/**
	 * Handles the comparison (either two or three ways) of the models.
	 */
	protected void doCompare() {
		try {
			final Date start = Calendar.getInstance().getTime();
			final ModelInputSnapshot snapshot = DiffFactory.eINSTANCE.createModelInputSnapshot();

			if (!isThreeWay) {
				PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InterruptedException {
						final Map<String, Object> options = new EMFCompareMap<String, Object>();
						options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);
						final MatchModel match = MatchService.doResourceMatch(leftResource, rightResource,
								options);
						final DiffModel diff = DiffService.doDiff(match, isThreeWay);

						snapshot.setDate(Calendar.getInstance().getTime());
						snapshot.setDiff(diff);
						snapshot.setMatch(match);
					}
				});
			} else {
				PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InterruptedException {
						try {
							final Map<String, Object> options = new EMFCompareMap<String, Object>();
							options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);
							final MatchModel match = MatchService.doResourceMatch(leftResource,
									rightResource, ancestorResource, options);
							final DiffModel diff = DiffService.doDiff(match, isThreeWay);

							snapshot.setDate(Calendar.getInstance().getTime());
							snapshot.setDiff(diff);
							snapshot.setMatch(match);
						} finally {
							monitor.done();
						}
					}
				});
			}
			final Date end = Calendar.getInstance().getTime();
			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_TIME, end.getTime()
					- start.getTime());

			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT, snapshot);
			super.setInput(new ModelCompareInput(snapshot.getMatch(), snapshot.getDiff()));
		} catch (InterruptedException e) {
			throw new EMFCompareException(e);
		} catch (InvocationTargetException e) {
			EMFComparePlugin.log(e, true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	protected void fireSelectionChanged(final SelectionChangedEvent event) {
		super.fireSelectionChanged(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		byte[] contents = null;

		EObject root = ((TypedElementWrapper)((IMergeViewerContentProvider)getContentProvider())
				.getLeftContent(getInput())).getObject();
		if (!left)
			root = ((TypedElementWrapper)((IMergeViewerContentProvider)getContentProvider())
					.getRightContent(getInput())).getObject();

		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			root.eResource().save(stream, null);
			contents = stream.toByteArray();
		} catch (IOException e) {
			EMFComparePlugin.log(e, false);
		}

		return contents;
	}

	/**
	 * This will minimize the list of differences to the visible differences. Differences are considered
	 * "visible" if {@link DiffAdapterFactory#shouldBeHidden(EObject)} returns false on it.
	 * 
	 * @return {@link List} of the visible differences for this comparison.
	 */
	protected List<DiffElement> getVisibleDiffs() {
		final List<DiffElement> diffs = ((ModelCompareInput)getInput()).getDiffAsList();
		final List<DiffElement> visibleDiffs = new ArrayList<DiffElement>(diffs.size());

		for (int i = 0; i < diffs.size(); i++) {
			if (!DiffAdapterFactory.shouldBeHidden(diffs.get(i)))
				visibleDiffs.add(diffs.get(i));
		}

		return visibleDiffs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		super.handleDispose(event);
		configuration.removePropertyChangeListener(structureSelectionListener);
		EMFCompareUIPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(preferenceListener);
		leftPart = null;
		rightPart = null;
		ancestorPart = null;
		canvas = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#handleResizeAncestor(int, int, int, int)
	 */
	@Override
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		ancestorPart.setBounds(x, y, width, height);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#handleResizeLeftRight(int, int, int, int)
	 */
	@Override
	protected void handleResizeLeftRight(int x, int y, int leftWidth, int centerWidth, int rightWidth,
			int height) {
		if (getCenterPart() != null)
			getCenterPart().setBounds(leftWidth - (CENTER_WIDTH / 2), y, CENTER_WIDTH, height);
		leftPart.setBounds(x, y, leftWidth - (CENTER_WIDTH / 2), height);
		rightPart.setBounds(x + leftWidth + (CENTER_WIDTH / 2), y, rightWidth - (CENTER_WIDTH / 2), height);
		update();
	}

	/**
	 * Selects the next or previous {@link DiffElement} as compared to the currently selected one.
	 * 
	 * @param down
	 *            <code>True</code> if we seek the next {@link DiffElement}, <code>False</code> for the
	 *            previous.
	 */
	protected void navigate(boolean down) {
		final List<DiffElement> diffs = getVisibleDiffs();
		if (diffs.size() != 0) {
			final DiffElement theDiff;
			if (currentSelection.size() > 0 && !(currentSelection.get(0) instanceof DiffGroup))
				theDiff = currentSelection.get(0);
			else if (diffs.size() == 1)
				theDiff = diffs.get(0);
			else if (down)
				theDiff = diffs.get(diffs.size() - 1);
			else
				theDiff = diffs.get(1);
			for (int i = 0; i < diffs.size(); i++) {
				if (diffs.get(i).equals(theDiff) && down) {
					DiffElement next = diffs.get(0);
					if (diffs.size() > i + 1) {
						next = diffs.get(i + 1);
					}
					if (next != null && !DiffAdapterFactory.shouldBeHidden(next)) {
						setSelection(next);
						break;
					}
				} else if (diffs.get(i).equals(theDiff) && !down) {
					DiffElement previous = diffs.get(diffs.size() - 1);
					if (i > 0) {
						previous = diffs.get(i - 1);
					}
					if (previous != null && !DiffAdapterFactory.shouldBeHidden(previous)) {
						setSelection(previous);
						break;
					}
				}
			}
		}
	}

	/**
	 * Handles all the loading operations for the three models we need.
	 * 
	 * @param left
	 *            Left resource (either local or remote) to load.
	 * @param right
	 *            Right resource (either local or remote) to load.
	 * @param ancestor
	 *            Ancestor resource (either local or remote) to load.
	 */
	protected void prepareComparison(ITypedElement left, ITypedElement right, ITypedElement ancestor) {
		try {
			final ResourceSet modelResourceSet = new ResourceSetImpl();
			if (left instanceof ResourceNode && right instanceof ResourceNode) {
				leftResource = ModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
						modelResourceSet).eResource();
				rightResource = ModelUtils.load(((ResourceNode)right).getResource().getFullPath(),
						modelResourceSet).eResource();
				if (isThreeWay)
					ancestorResource = ModelUtils.load(((ResourceNode)ancestor).getResource().getFullPath(),
							modelResourceSet).eResource();
			} else if (left instanceof ResourceNode && right instanceof IStreamContentAccessor) {
				// this is the case of SVN/CVS comparison, we invert left
				// (remote) and right (local).
				if (((ResourceNode)left).getResource().isAccessible())
					rightResource = ModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
							modelResourceSet).eResource();
				else
					rightResource = ModelUtils.createResource(URI.createPlatformResourceURI(
							((ResourceNode)left).getResource().getFullPath().toOSString(), true));
				leftResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right.getName(), modelResourceSet).eResource();
				configuration.setRightLabel(EMFCompareUIMessages.getString("comparison.label.localResource")); //$NON-NLS-1$)
				configuration.setLeftLabel(EMFCompareUIMessages.getString("comparison.label.remoteResource")); //$NON-NLS-1$
				configuration.setLeftEditable(false);
				configuration.setProperty(EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE, true);
				if (isThreeWay)
					ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
							ancestor.getName(), modelResourceSet).eResource();
			}
		} catch (IOException e) {
			throw new EMFCompareException(e);
		} catch (CoreException e) {
			throw new EMFCompareException(e);
		}
	}

	/**
	 * This will enable or disable the toolbar's copy actions according to the given <code>boolean</code>.
	 * The "copy diff left to right" action will be enabled if <code>enable</code> is <code>True</code>,
	 * but the "copy diff right to left" action will only be activated if <code>enable</code> is
	 * <code>True</code> AND the left model isn't a remote model.
	 * 
	 * @param enabled
	 *            <code>True</code> if we seek to enable the actions, <code>False</code> otherwise.
	 */
	protected void switchCopyState(boolean enabled) {
		boolean leftIsRemote = false;
		if (configuration.getProperty(EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE) != null)
			leftIsRemote = Boolean.parseBoolean(configuration.getProperty(
					EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE).toString());
		if (copyDiffLeftToRight != null)
			copyDiffLeftToRight.setEnabled(enabled);
		if (copyDiffRightToLeft != null)
			copyDiffRightToLeft.setEnabled(!leftIsRemote && enabled);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ContentMergeViewer#updateContent(Object, Object, Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		Object ancestorObject = ancestor;
		Object leftObject = left;
		Object rightObject = right;
		if (ancestorObject instanceof TypedElementWrapper)
			ancestorObject = ((TypedElementWrapper)ancestorObject).getObject();
		if (leftObject instanceof TypedElementWrapper)
			leftObject = ((TypedElementWrapper)leftObject).getObject();
		if (rightObject instanceof TypedElementWrapper)
			rightObject = ((TypedElementWrapper)rightObject).getObject();

		if (ancestorObject != null)
			ancestorPart.setInput(ancestorObject);
		if (leftObject != null)
			leftPart.setInput(leftObject);
		if (rightObject != null)
			rightPart.setInput(rightObject);
		update();
	}

	/**
	 * Updates the values of all the variables using preferences values.
	 */
	protected void updatePreferences() {
		final IPreferenceStore comparePreferences = EMFCompareUIPlugin.getDefault().getPreferenceStore();
		updateColors(comparePreferences);
		updateDrawDiffMarkers(comparePreferences);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateToolItems()
	 */
	@Override
	protected void updateToolItems() {
		super.updateToolItems();
		CompareViewerPane.getToolBarManager(getControl().getParent()).update(true);
	}

	/**
	 * Takes care of the creation of the synchronization handlers for the properties tab of our viewer parts.
	 * 
	 * @param parts
	 *            The other parts to synchronize with.
	 */
	private void createPropertiesSyncHandlers(ModelContentMergeTabFolder... parts) {
		if (parts.length < 2)
			throw new IllegalArgumentException(EMFCompareUIMessages
					.getString("ModelContentMergeViewer.illegalSync")); //$NON-NLS-1$

		// horizontal synchronization
		handleHSync(leftPart.getPropertyPart(), rightPart.getPropertyPart(), ancestorPart.getPropertyPart());
		handleHSync(ancestorPart.getPropertyPart(), rightPart.getPropertyPart(), leftPart.getPropertyPart());
		handleHSync(rightPart.getPropertyPart(), leftPart.getPropertyPart(), ancestorPart.getPropertyPart());
		// Vertical synchronization
		handleVSync(leftPart.getPropertyPart(), rightPart.getPropertyPart(), ancestorPart.getPropertyPart());
		handleVSync(rightPart.getPropertyPart(), leftPart.getPropertyPart(), ancestorPart.getPropertyPart());
		handleVSync(ancestorPart.getPropertyPart(), rightPart.getPropertyPart(), leftPart.getPropertyPart());
	}

	/**
	 * Takes care of the creation of the synchronization handlers for the tree tab of our viewer parts.
	 * 
	 * @param parts
	 *            The other parts to synchronize with.
	 */
	private void createTreeSyncHandlers(ModelContentMergeTabFolder... parts) {
		if (parts.length < 2)
			throw new IllegalArgumentException(EMFCompareUIMessages
					.getString("ModelContentMergeViewer.illegalSync")); //$NON-NLS-1$

		handleHSync(leftPart.getTreePart(), rightPart.getTreePart(), ancestorPart.getTreePart());
		handleHSync(rightPart.getTreePart(), leftPart.getTreePart(), ancestorPart.getTreePart());
		handleHSync(ancestorPart.getTreePart(), rightPart.getTreePart(), leftPart.getTreePart());
	}

	/**
	 * Allows synchronization of the properties viewports horizontal scrolling.
	 * 
	 * @param parts
	 *            The other parts to synchronize with.
	 */
	private void handleHSync(IModelContentMergeViewerTab... parts) {
		// inspired from TreeMergeViewer#hsynchViewport
		final Scrollable scroll1 = (Scrollable)parts[0].getControl();
		final Scrollable scroll2 = (Scrollable)parts[1].getControl();
		final Scrollable scroll3;
		if (parts.length > 2)
			scroll3 = (Scrollable)parts[2].getControl();
		else
			scroll3 = null;
		final ScrollBar scrollBar1 = scroll1.getHorizontalBar();

		scrollBar1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int max = scrollBar1.getMaximum() - scrollBar1.getThumb();
				double v = 0.0;
				if (max > 0)
					v = (double)scrollBar1.getSelection() / (double)max;
				if (scroll2.isVisible()) {
					final ScrollBar scrollBar2 = scroll2.getHorizontalBar();
					scrollBar2.setSelection((int)((scrollBar2.getMaximum() - scrollBar2.getThumb()) * v));
				}
				if (scroll3 != null && scroll3.isVisible()) {
					final ScrollBar scrollBar3 = scroll3.getHorizontalBar();
					scrollBar3.setSelection((int)((scrollBar3.getMaximum() - scrollBar3.getThumb()) * v));
				}
				if (SWT.getPlatform().equals("carbon") && getControl() != null //$NON-NLS-1$
						&& !getControl().isDisposed()) {
					getControl().getDisplay().update();
				}
			}
		});
	}

	/**
	 * Allows synchronization of the viewports vertical scrolling.
	 * 
	 * @param parts
	 *            The other parts to synchronize with.
	 */
	private void handleVSync(IModelContentMergeViewerTab... parts) {
		// inspired from TreeMergeViewer#hsynchViewport
		final Scrollable table1 = (Scrollable)parts[0].getControl();
		final Scrollable table2 = (Scrollable)parts[1].getControl();
		final Scrollable table3;
		if (parts.length > 2)
			table3 = (Scrollable)parts[2].getControl();
		else
			table3 = null;
		final ScrollBar scrollBar1 = table1.getVerticalBar();

		scrollBar1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int max = scrollBar1.getMaximum() - scrollBar1.getThumb();
				double v = 0.0;
				if (max > 0)
					v = (double)scrollBar1.getSelection() / (double)max;
				if (table2.isVisible()) {
					final ScrollBar scrollBar2 = table2.getVerticalBar();
					scrollBar2.setSelection((int)((scrollBar2.getMaximum() - scrollBar2.getThumb()) * v));
				}
				if (table3 != null && table3.isVisible()) {
					final ScrollBar scrollBar3 = table3.getVerticalBar();
					scrollBar3.setSelection((int)((scrollBar3.getMaximum() - scrollBar3.getThumb()) * v));
				}
				if (SWT.getPlatform().equals("carbon") && getControl() != null //$NON-NLS-1$
						&& !getControl().isDisposed()) {
					getControl().getDisplay().update();
				}
			}
		});
	}

	/**
	 * Updates the value of the colors as they are changed on the preference page.
	 * 
	 * @param comparePreferences
	 *            Preference store where to retrieve our values.
	 */
	private void updateColors(IPreferenceStore comparePreferences) {
		final RGB highlightColor = PreferenceConverter.getColor(comparePreferences,
				EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR);
		final RGB changedColor = PreferenceConverter.getColor(comparePreferences,
				EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR);
		final RGB conflictingColor = PreferenceConverter.getColor(comparePreferences,
				EMFCompareConstants.PREFERENCES_KEY_CONFLICTING_COLOR);
		final RGB addedColor = PreferenceConverter.getColor(comparePreferences,
				EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR);
		final RGB removedColor = PreferenceConverter.getColor(comparePreferences,
				EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR);
		colors.put(EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR, highlightColor);
		colors.put(EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR, changedColor);
		colors.put(EMFCompareConstants.PREFERENCES_KEY_CONFLICTING_COLOR, conflictingColor);
		colors.put(EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR, addedColor);
		colors.put(EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR, removedColor);
	}

	/**
	 * Updates the value of the boolean indicating that we should ignore diff markers as it is changed on the
	 * preference page.
	 * 
	 * @param comparePreferences
	 *            Preference store where to retrieve our values.
	 */
	private void updateDrawDiffMarkers(IPreferenceStore comparePreferences) {
		drawDiffMarkers = comparePreferences.getBoolean(EMFCompareConstants.PREFERENCES_KEY_DRAW_DIFFERENCES);
	}

	/**
	 * Basic implementation of an {@link ICompareEditorPartListener}.
	 */
	private class EditorPartListener implements ICompareEditorPartListener {
		/** Viewer parts this listener is registered for. */
		private final ModelContentMergeTabFolder[] viewerParts;

		/**
		 * Instantiate this {@link EditorPartListener} given the left, right and ancestor viewer parts.
		 * 
		 * @param parts
		 *            The viewer parts.
		 */
		public EditorPartListener(ModelContentMergeTabFolder... parts) {
			viewerParts = parts;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see ICompareEditorPartListener#selectedTabChanged()
		 */
		public void selectedTabChanged(int newIndex) {
			for (int i = 0; i < viewerParts.length; i++) {
				viewerParts[i].setSelectedTab(newIndex);
			}
			updateCenter();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see ICompareEditorPartListener#selectionChanged()
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged(event);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see ICompareEditorPartListener#updateCenter()
		 */
		public void updateCenter() {
			ModelContentMergeViewer.this.updateCenter();
		}
	}
}
