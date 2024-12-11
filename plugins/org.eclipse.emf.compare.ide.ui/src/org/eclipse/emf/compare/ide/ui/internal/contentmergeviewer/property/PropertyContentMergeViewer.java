/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import static com.google.common.collect.Iterables.getFirst;

import com.google.common.collect.Iterables;

import java.util.ResourceBundle;

import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.AbstractTreeContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * A specialized {@link ContentMergeViewer} that shows property views for the three side viewers.
 */
class PropertyContentMergeViewer extends AbstractTreeContentMergeViewer {
	/**
	 * The bundle name of the properties file containing all displayed strings needed by the base class.
	 */
	private static final String BUNDLE_NAME = PropertyContentMergeViewer.class.getName();

	/**
	 * The three values of {@link MergeViewerSide} in the order {@link MergeViewerSide#ANCESTOR},
	 * {@link MergeViewerSide#LEFT}, and {@link MergeViewerSide#RIGHT}.
	 */
	static final MergeViewerSide[] MERGE_VIEWER_SIDES = new MergeViewerSide[] {MergeViewerSide.ANCESTOR,
			MergeViewerSide.LEFT, MergeViewerSide.RIGHT };

	/**
	 * The {@link IEMFCompareConfiguration#getBooleanProperty(String) configuration property key} used to
	 * indicate whether {@link IItemPropertyDescriptor#getFilterFlags(Object) advanced properties} should be
	 * shown.
	 * 
	 * @see #createToolItems(ToolBarManager)
	 * @see PropertyItem#createPropertyItem(EMFCompareConfiguration, Object, MergeViewerSide)
	 */
	static String SHOW_ADVANCED_PROPERTIES = "SHOW_ADVANCED_PROPERTIES"; //$NON-NLS-1$

	/**
	 * The {@link IEMFCompareConfiguration#getBooleanProperty(String) configuration property key} used to
	 * indicate whether {@link IItemPropertyDescriptor#getCategory(Object) categories} should be shown.
	 * 
	 * @see #createToolItems(ToolBarManager)
	 * @see PropertyItem#createPropertyItem(EMFCompareConfiguration, Object, MergeViewerSide)
	 */
	static String SHOW_CATEGORIES = "SHOW_CATEGORIES"; //$NON-NLS-1$

	/**
	 * The value used by {@link #updateContent(Object, Object, Object)} as a substitution for the ancestor.
	 * 
	 * @see #buildPropertiesFromSides(Object, Object, Object)
	 */
	private PropertyAccessor ancestorPropertyAccessor;

	/**
	 * The value used by {@link #updateContent(Object, Object, Object)} as a substitution for the left.
	 * 
	 * @see #buildPropertiesFromSides(Object, Object, Object)
	 */
	private PropertyAccessor leftPropertyAccessor;

	/**
	 * The value used by {@link #updateContent(Object, Object, Object)} as a substitution for the right.
	 * 
	 * @see #buildPropertiesFromSides(Object, Object, Object)
	 */
	private PropertyAccessor rightPropertyAccessor;

	/**
	 * A listener attached the tree of {@link #getPropertyMergeViewer(MergeViewerSide) property merge viewer}
	 * that synchronizes the expansion state of the three trees as each individual tree expands and collapses
	 * a tree item.
	 * 
	 * @see #createMergeViewer(Composite, MergeViewerSide)
	 */
	private ITreeViewerListener treeListener = new ITreeViewerListener() {
		public void treeExpanded(TreeExpansionEvent event) {
			update(event, true);
		}

		public void treeCollapsed(TreeExpansionEvent event) {
			update(event, false);
		}

		private void update(TreeExpansionEvent event, boolean expanded) {
			AbstractTreeViewer treeViewer = event.getTreeViewer();
			PropertyItem element = (PropertyItem)event.getElement();
			for (MergeViewerSide side : MERGE_VIEWER_SIDES) {
				PropertyTreeMergeViewer propertyMergeViewer = getPropertyMergeViewer(side);
				if (propertyMergeViewer.getStructuredViewer() != treeViewer) {
					PropertyItem sideElement = element.getSide(side);
					if (sideElement != null) {
						propertyMergeViewer.setExpandedState(sideElement, expanded);
						propertyMergeViewer.columnResizer.resizeColumns();
					}
				}
			}
			redrawCenterControl();
		}
	};

	/**
	 * Creates an instance contained by the parent composite for the given configuration.
	 * 
	 * @param parent
	 *            composite in which to create this instance.
	 * @param configuration
	 *            the configuration used by this instance.
	 */
	protected PropertyContentMergeViewer(Composite parent, EMFCompareConfiguration configuration) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), configuration);
		buildControl(parent);
	}

	/**
	 * Returns the property merge viewer for the given side.
	 * 
	 * @param side
	 *            the side.
	 * @return the property merge viewer for the given side.
	 */
	protected PropertyTreeMergeViewer getPropertyMergeViewer(MergeViewerSide side) {
		switch (side) {
			case ANCESTOR:
				return (PropertyTreeMergeViewer)getAncestorMergeViewer();
			case LEFT:
				return (PropertyTreeMergeViewer)getLeftMergeViewer();
			case RIGHT:
			default:
				return (PropertyTreeMergeViewer)getRightMergeViewer();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation is specialized to set its own specialized content provider during initialization.
	 * The initialization first happens when the base class' constructor is invoked.
	 * </p>
	 */
	@Override
	public void setContentProvider(IContentProvider contentProvider) {
		// If this is the first time this is called, i.e., by the base class constructor...
		if (getContentProvider() == null) {
			// Wrap the default content provider with our own delegating implementation.
			final IMergeViewerContentProvider mergeViewerContentProvider = (IMergeViewerContentProvider)contentProvider;
			super.setContentProvider(new PropertyContentProvider(mergeViewerContentProvider));
		} else {
			super.setContentProvider(contentProvider);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation creates a {@link PropertyContentMergeViewer}.
	 * </p>
	 */
	@Override
	protected IMergeViewer createMergeViewer(Composite parent, final MergeViewerSide side) {
		PropertyTreeMergeViewer propertyMergeViewer = new PropertyTreeMergeViewer(parent, side, this,
				getCompareConfiguration());
		hookListeners(propertyMergeViewer);
		propertyMergeViewer.getStructuredViewer().addTreeListener(treeListener);
		return propertyMergeViewer;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation creates tool items for showing/hiding advanced properties and for showing/hiding
	 * categories.
	 * </p>
	 * 
	 * @see #SHOW_ADVANCED_PROPERTIES
	 * @see #SHOW_CATEGORIES
	 */
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		super.createToolItems(toolBarManager);

		Action showCategories = new ToggleAction(SHOW_CATEGORIES, true,
				"icons/full/toolb16/show_categories.png", "PropertyContentMergeViewer.hideCategories.tooltip", //$NON-NLS-1$ //$NON-NLS-2$
				"PropertyContentMergeViewer.showCategories.tooltip"); //$NON-NLS-1$
		toolBarManager.add(new ActionContributionItem(showCategories));

		Action showAdvancedProperties = new ToggleAction(SHOW_ADVANCED_PROPERTIES, false,
				"icons/full/toolb16/show_advanced_properties.png", //$NON-NLS-1$
				"PropertyContentMergeViewer.hideAdvancedProperties.tooltip", //$NON-NLS-1$
				"PropertyContentMergeViewer.showAdvancedProperties.tooltip"); //$NON-NLS-1$
		toolBarManager.add(new ActionContributionItem(showAdvancedProperties));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation is specialized to {@link #buildProperties(Object) build the properties} for each of
	 * the three sides of the input.
	 * </p>
	 */
	@Override
	public void setInput(Object input) {
		buildProperties(input);
		super.setInput(input);
	}

	@Override
	protected void handleDispose(DisposeEvent event) {
		ancestorPropertyAccessor = null;
		leftPropertyAccessor = null;
		rightPropertyAccessor = null;
		super.handleDispose(event);
	}

	/**
	 * Builds the properties corresponding to the input.
	 * 
	 * @param input
	 *            the {@link #setInput(Object) input}.
	 */
	private void buildProperties(Object input) {
		if (input instanceof CompareInputAdapter) {
			CompareInputAdapter compareInputAdapter = (CompareInputAdapter)input;
			Notifier target = compareInputAdapter.getTarget();
			if (target instanceof TreeNode) {
				TreeNode treeNode = (TreeNode)target;
				buildPropertiesFromTreeNode(treeNode, true);
				return;
			}
		}
		buildPropertiesFromSides(null, null, null);
	}

	/**
	 * Builds the properties corresponding to the tree node of the input. This is only called if the
	 * {@link #setInput(Object) input} is a {@link CompareInputAdapter} whose
	 * {@link CompareInputAdapter#getTarget() target} is {@link TreeNode}.
	 * 
	 * @param treeNode
	 *            the tree node.
	 * @param visitParent
	 *            whether to visit the parent while visiting this tree node.
	 */
	@SuppressWarnings("restriction")
	private void buildPropertiesFromTreeNode(TreeNode treeNode, boolean visitParent) {
		EObject data = treeNode.getData();

		if (treeNode instanceof DiffNode) {
			// If there is a refined diff, build the properties using that, but of course don't visit the
			// parent while doing so, or we'd end up with a stack overflow.
			TreeNode refinedDiff = getFirst(((DiffNode)treeNode).getRefinedDiffs(), null);
			if (refinedDiff != null) {
				// Then ensures that, for example, when a shape is moved, we see the properties for the object
				// on which the coordinates are changing.
				buildPropertiesFromTreeNode(refinedDiff, false);
				setInitialItemsForProperties(data);
				return;
			}

			if (visitParent) {
				TreeNode parent = treeNode.getParent();
				if (parent != null) {
					buildPropertiesFromTreeNode(parent, true);
					setInitialItemsForProperties(data);
					return;
				}
			}
		}

		buildPropertiesFromEObject(data);
		setInitialItemsForProperties(data);
	}

	/**
	 * Builds the properties corresponding to a tree node's {@link TreeNode#getData() data}.
	 * 
	 * @param eObject
	 *            the data object.
	 * @see #buildPropertiesFromSides(Object, Object, Object)
	 */
	private void buildPropertiesFromEObject(EObject eObject) {
		if (eObject instanceof Match) {
			// If it's a match, get the sides from the match.
			Match match = (Match)eObject;
			EObject effectiveAncestor = match.getOrigin();
			EObject effectiveLeft = match.getLeft();
			EObject effectiveRight = match.getRight();

			// If they're all null, and there is a containing match, populate using that instead.
			if (effectiveAncestor == null && effectiveLeft == null && effectiveRight == null) {
				EObject eContainer = match.eContainer();
				if (eContainer instanceof Match) {
					buildPropertiesFromEObject(eContainer);
				}
			} else {
				// Otherwise build using the tree sides.
				buildPropertiesFromSides(effectiveAncestor, effectiveLeft, effectiveRight);
			}
		} else if (eObject instanceof Diff) {
			// If it's a diff, build using the containing match.
			Diff diff = (Diff)eObject;
			buildPropertiesFromEObject(diff.getMatch());
			setInitialItemsForProperties(diff);
		} else if (eObject instanceof MatchResource) {
			// If it's a match resource, build directly from the three side resources.
			MatchResource matchResource = (MatchResource)eObject;
			buildPropertiesFromSides(matchResource.getOrigin(), matchResource.getLeft(),
					matchResource.getRight());
		} else if (eObject instanceof Equivalence) {
			// If it's an equivalence, use the first diff.
			Equivalence equivalence = (Equivalence)eObject;
			EList<Diff> differences = equivalence.getDifferences();
			Diff first = Iterables.getFirst(differences, null);
			buildPropertiesFromEObject(first);
		} else if (eObject instanceof Conflict) {
			// If it's a conflict, use the first diff.
			Conflict conflict = (Conflict)eObject;
			EList<Diff> differences = conflict.getDifferences();
			Diff first = Iterables.getFirst(differences, null);
			buildPropertiesFromEObject(first);
		}
	}

	/**
	 * Builds the {@link #ancestorPropertyAccessor}, {@link #leftPropertyAccessor}, and
	 * {@link #rightPropertyAccessor} from the given sides.
	 * 
	 * @param origin
	 *            the origin.
	 * @param left
	 *            the left.
	 * @param right
	 *            the right.
	 */
	private void buildPropertiesFromSides(Object origin, Object left, Object right) {
		EMFCompareConfiguration configuration = getCompareConfiguration();
		ancestorPropertyAccessor = new PropertyAccessor(configuration, origin, MergeViewerSide.ANCESTOR);
		leftPropertyAccessor = new PropertyAccessor(configuration, left,
				getEffectiveSide(MergeViewerSide.LEFT));
		rightPropertyAccessor = new PropertyAccessor(configuration, right,
				getEffectiveSide(MergeViewerSide.RIGHT));

		ancestorPropertyAccessor.rootPropertyItem.reconcile(leftPropertyAccessor.rootPropertyItem,
				rightPropertyAccessor.rootPropertyItem);
	}

	/**
	 * Updates the {@link PropertyAccessor#setInitialItem(Diff) initial item} based on the given object. If
	 * the object is a {@link Diff} that is used, otherwise {@code null} is used.
	 * 
	 * @param eObject
	 *            the object.
	 */
	private void setInitialItemsForProperties(EObject eObject) {
		Diff diff = null;
		if (eObject instanceof Diff) {
			diff = (Diff)eObject;
		}
		ancestorPropertyAccessor.setInitialItem(diff);
		leftPropertyAccessor.setInitialItem(diff);
		rightPropertyAccessor.setInitialItem(diff);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation {@link #buildProperties(Object) re-builds the properties}, and attempts to preserve
	 * the selection before calling {@code super.refresh()}.
	 * </p>
	 */
	@Override
	public void refresh() {
		buildProperties(getInput());
		for (MergeViewerSide side : MERGE_VIEWER_SIDES) {
			PropertyTreeMergeViewer propertyMergeViewer = getPropertyMergeViewer(side);
			ISelection selection = propertyMergeViewer.getSelection();
			PropertyItem selectedItem = null;
			if (!selection.isEmpty()) {
				selectedItem = (PropertyItem)((IStructuredSelection)selection).getFirstElement();
			}
			switch (side) {
				case ANCESTOR:
					ancestorPropertyAccessor.setInitialItem(selectedItem);
					break;
				case LEFT:
					leftPropertyAccessor.setInitialItem(selectedItem);
					break;
				case RIGHT:
					rightPropertyAccessor.setInitialItem(selectedItem);
					break;
			}
		}
		super.refresh();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation substitutes {@link #ancestorPropertyAccessor}, {@link #leftPropertyAccessor}, and
	 * {@link #rightPropertyAccessor} in place of {@code ancestor}, {@code left}, and {@code right}. It also
	 * switches the left and right if {@link IEMFCompareConfiguration#isMirrored() mirrored}.
	 * </p>
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		if (getCompareConfiguration().isMirrored()) {
			super.updateContent(ancestorPropertyAccessor, rightPropertyAccessor, leftPropertyAccessor);
		} else {
			super.updateContent(ancestorPropertyAccessor, leftPropertyAccessor, rightPropertyAccessor);
		}
	}

	private final class PropertyContentProvider implements IMergeViewerContentProvider {
		private final IMergeViewerContentProvider mergeViewerContentProvider;

		private PropertyContentProvider(IMergeViewerContentProvider mergeViewerContentProvider) {
			this.mergeViewerContentProvider = mergeViewerContentProvider;
		}

		public void dispose() {
			mergeViewerContentProvider.dispose();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			mergeViewerContentProvider.inputChanged(viewer, oldInput, newInput);
		}

		private String getSideLabel(String baseLabel, MergeViewerSide side) {
			// Augment the base label with the label of the root object
			PropertyTreeMergeViewer propertyMergeViewer = getPropertyMergeViewer(side);
			if (propertyMergeViewer != null && propertyMergeViewer.getRootPropertyItem() != null) {
				String text = propertyMergeViewer.getRootPropertyItem().getText();
				if (!text.isEmpty()) {
					return text + " - " + baseLabel; //$NON-NLS-1$
				}
			}
			return baseLabel;
		}

		private Image getSideImage(Image baseImage, MergeViewerSide side) {
			// Substitute the base image with the image of the root object
			PropertyTreeMergeViewer propertyMergeViewer = getPropertyMergeViewer(side);
			if (propertyMergeViewer != null && propertyMergeViewer.getRootPropertyItem() != null) {
				Object image = propertyMergeViewer.getRootPropertyItem().getImage();
				if (image != null) {
					return ExtendedImageRegistry.INSTANCE.getImage(image);
				}
			}
			return baseImage;
		}

		public String getAncestorLabel(Object input) {
			return getSideLabel(mergeViewerContentProvider.getAncestorLabel(input), MergeViewerSide.ANCESTOR);
		}

		public Image getAncestorImage(Object input) {
			return getSideImage(mergeViewerContentProvider.getAncestorImage(input), MergeViewerSide.ANCESTOR);
		}

		public Object getAncestorContent(Object input) {
			return mergeViewerContentProvider.getAncestorContent(input);
		}

		public boolean showAncestor(Object input) {
			return mergeViewerContentProvider.showAncestor(input);
		}

		public String getLeftLabel(Object input) {
			return getSideLabel(mergeViewerContentProvider.getLeftLabel(input), MergeViewerSide.LEFT);
		}

		public Image getLeftImage(Object input) {
			return getSideImage(mergeViewerContentProvider.getLeftImage(input), MergeViewerSide.LEFT);
		}

		public Object getLeftContent(Object input) {
			return mergeViewerContentProvider.getLeftContent(input);
		}

		public boolean isLeftEditable(Object input) {
			return mergeViewerContentProvider.isLeftEditable(input);
		}

		public void saveLeftContent(Object input, byte[] bytes) {
			mergeViewerContentProvider.saveLeftContent(input, bytes);
		}

		public String getRightLabel(Object input) {
			return getSideLabel(mergeViewerContentProvider.getRightLabel(input), MergeViewerSide.RIGHT);
		}

		public Image getRightImage(Object input) {
			return getSideImage(mergeViewerContentProvider.getRightImage(input), MergeViewerSide.RIGHT);
		}

		public Object getRightContent(Object input) {
			return mergeViewerContentProvider.getRightContent(input);
		}

		public boolean isRightEditable(Object input) {
			return mergeViewerContentProvider.isRightEditable(input);
		}

		public void saveRightContent(Object input, byte[] bytes) {
			mergeViewerContentProvider.saveRightContent(input, bytes);
		}
	}

	private final class ToggleAction extends Action {
		private final String propertyKey;

		private final String checkedTooltip;

		private final String uncheckedTooltip;

		public ToggleAction(String propertyKey, boolean propertyDefaultValue, String imageLocation,
				String checkedTooltipKey, String uncheckedTooltipKey) {
			this.propertyKey = propertyKey;
			checkedTooltip = EMFCompareIDEUIMessages.getString(checkedTooltipKey);
			uncheckedTooltip = EMFCompareIDEUIMessages.getString(uncheckedTooltipKey);
			setChecked(getCompareConfiguration().getBooleanProperty(propertyKey, propertyDefaultValue));
			setImageDescriptor(EMFCompareIDEUIPlugin.getImageDescriptor(imageLocation));
			updateToolTipText();
		}

		@Override
		public void run() {
			getCompareConfiguration().setProperty(propertyKey, Boolean.valueOf(isChecked()));
			refresh();
			updateToolTipText();
		}

		private void updateToolTipText() {
			final String toolTipText;
			if (isChecked()) {
				toolTipText = checkedTooltip;
			} else {
				toolTipText = uncheckedTooltip;
			}
			setToolTipText(toolTipText);
		}
	}

}
