/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IEObjectAccessor;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Specialized {@link ContentMergeViewer} that uses {@link TreeViewer} to display left, right and ancestor
 * {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectContentMergeViewer extends EMFCompareContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = "org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EObjectContentMergeViewer"; //$NON-NLS-1$

	/**
	 * The {@link AdapterFactory} used to create {@link AdapterFactoryContentProvider} and
	 * {@link AdapterFactoryLabelProvider} for ancestor, left and right {@link TreeViewer}.
	 */
	private final AdapterFactory fAdapterFactory;

	private EMFCompareColor fColors;

	/**
	 * Creates a new {@link EObjectContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link EObjectMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public EObjectContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		buildControl(parent);
		setContentProvider(new EObjectMergeViewerContentProvider(config));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		doUpdateContent(ancestor, getAncestor().getViewer());
		doUpdateContent(left, getLeft().getViewer());
		doUpdateContent(right, getRight().getViewer());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestor()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<TreeViewer, Tree> getAncestor() {
		return (IMergeViewer<TreeViewer, Tree>)super.getAncestor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeft()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<TreeViewer, Tree> getLeft() {
		return (IMergeViewer<TreeViewer, Tree>)super.getLeft();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRight()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<TreeViewer, Tree> getRight() {
		return (IMergeViewer<TreeViewer, Tree>)super.getRight();
	}

	private static void doUpdateContent(Object object, TreeViewer viewer) {
		if (object instanceof IEObjectAccessor) {
			EObject eObject = ((IEObjectAccessor)object).getEObject();
			final Object viewerInput = doGetInput(eObject);
			viewer.setInput(viewerInput);
			Object selection = viewerInput;
			if (eObject != null) {
				if (eObject.eContainer() == viewerInput) {
					selection = eObject;
				} else if (eObject.eContainer() == null) {
					selection = eObject;
				}
			}
			viewer.setSelection(new StructuredSelection(selection), true);
			viewer.expandToLevel(selection, 1);
		} else {
			viewer.setInput(null);
		}
	}

	/**
	 * Returns either the {@link EObject#eContainer() container} of the given <code>eObject</code> if it is
	 * not null or its {@link EObject#eResource() containing resource} if it is not null.
	 * 
	 * @param eObject
	 *            the object to get the input from.
	 * @return either the {@link EObject#eContainer()} of the given <code>eObject</code> if it is not null or
	 *         its {@link EObject#eResource() containing resource} if it is not null.
	 */
	private static Object doGetInput(EObject eObject) {
		Object input = null;
		if (eObject != null) {
			if (eObject.eContainer() != null) {
				input = eObject.eContainer();
			} else {
				input = eObject.eResource();
			}
		}
		return input;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffLeftToRight()
	 */
	@Override
	protected void copyDiffLeftToRight() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffRightToLeft()
	 */
	@Override
	protected void copyDiffRightToLeft() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		super.createControls(composite);
		fColors = new EMFCompareColor(this, null, getCompareConfiguration());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected IMergeViewer<TreeViewer, Tree> createMergeViewer(final Composite parent, MergeViewerSide side) {
		final MergeTreeViewer mergeTreeViewer = new MergeTreeViewer(parent, side,
				new AdapterFactoryContentProvider(fAdapterFactory), new AdapterFactoryLabelProvider(
						fAdapterFactory));
		return mergeTreeViewer;
	}

	@Override
	protected void paint(Event event, IMergeViewer<? extends Viewer, ? extends Scrollable> mergeTreeViewer) {
		TreeItem treeItem = (TreeItem)event.item;

		if (!getComparison().getDifferences().isEmpty()) {
			EObject data = (EObject)treeItem.getData();
			EObject dataEContainer = data.eContainer();
			EList<Diff> differences = getComparison().getDifferences(dataEContainer);
			for (Diff diff : differences) {
				if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()) {
					ReferenceChange referenceChange = (ReferenceChange)diff;
					EObject value = referenceChange.getValue();
					if (value == data) {
						paintTreeItem(event, treeItem, diff);
					}
				}
			}

			// this code aims at painting line above or under a treeitem where an element will be merged
			// EList<Diff> dataDifferences = getComparison().getDifferences(data);
			// for (Diff diff : dataDifferences) {
			// if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment())
			// {
			// ReferenceChange referenceChange = (ReferenceChange)diff;
			// EObject value = referenceChange.getValue();
			// Match matchOfValueContainer = getComparison().getMatch(value.eContainer());
			// if (matchOfValueContainer != null) {
			// EObject leftMatchedEObject = matchOfValueContainer.getLeft();
			// EObject rightMatchedEObject = matchOfValueContainer.getRight();
			// switch (mergeTreeViewer.getSide()) {
			// case LEFT:
			// if (rightMatchedEObject != null
			// && rightMatchedEObject.eContents().contains(value)) {
			// paintTreeItem2(event, treeItem, diff);
			// }
			// break;
			// case RIGHT:
			// if (leftMatchedEObject != null
			// && leftMatchedEObject.eContents().contains(value)) {
			// paintTreeItem2(event, treeItem, diff);
			// }
			// break;
			// }
			// }
			// }
			// }
		}
	}

	private int findInsertionIndex(Match valueMatch, ReferenceChange referenceChange, boolean rightToLeft) {
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = referenceChange.getMatch().getLeft();
		} else {
			expectedContainer = referenceChange.getMatch().getRight();
		}

		final EObject expectedValue = EcoreUtil.create(referenceChange.getValue().eClass());

		final List<?> sourceList;
		if (referenceChange.getValue() == valueMatch.getOrigin()) {
			sourceList = (List<?>)referenceChange.getMatch().getOrigin().eGet(referenceChange.getReference());
		} else if (rightToLeft) {
			sourceList = (List<?>)referenceChange.getMatch().getRight().eGet(referenceChange.getReference());
		} else {
			sourceList = (List<?>)referenceChange.getMatch().getLeft().eGet(referenceChange.getReference());
		}
		final List<?> targetList = (List<?>)expectedContainer.eGet(referenceChange.getReference());

		final Iterable<?> ignoredElements;
		if (getComparison().isThreeWay() && referenceChange.getValue() != valueMatch.getOrigin()) {
			ignoredElements = computeIgnoredElements(targetList, referenceChange);
		} else {
			ignoredElements = null;
		}

		return DiffUtil.findInsertionIndex(getComparison(), new EqualityHelper(),
				(Iterable<Object>)ignoredElements, (List<Object>)sourceList, (List<Object>)targetList,
				expectedValue);
	}

	private static Iterable<?> computeIgnoredElements(Iterable<?> candidates,
			final ReferenceChange referenceChange) {
		return Iterables.filter(candidates, new Predicate<Object>() {
			public boolean apply(final Object element) {
				final Match match = referenceChange.getMatch();
				final Iterable<ReferenceChange> filteredCandidates = Iterables.filter(match.getDifferences(),
						ReferenceChange.class);

				return Iterables.any(filteredCandidates, new Predicate<ReferenceChange>() {
					public boolean apply(ReferenceChange input) {
						return input.getState() == DifferenceState.UNRESOLVED
								&& input.getReference() == referenceChange.getReference()
								&& input.getValue() == element;
					}
				});
			}
		});
	}

	private void paintTreeItem(Event event, TreeItem treeItem, Diff diff) {
		event.detail &= ~SWT.HOT;

		GC g = event.gc;
		Rectangle treeBounds = treeItem.getParent().getBounds();
		Rectangle itemBounds = treeItem.getBounds();

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = 2;
		fill.y = itemBounds.y + 2;
		fill.width = treeBounds.width - 6;
		fill.height = itemBounds.height - 3;

		if (diff.getState() == DifferenceState.DISCARDED || diff.getState() == DifferenceState.MERGED) {
			return;
		}
		boolean selected = ((event.detail & SWT.SELECTED) != 0);

		g.setForeground(fColors.getStrokeColor(diff, isThreeWay(), false, selected));
		g.setBackground(fColors.getFillColor(diff, isThreeWay(), false, selected));
		g.fillRectangle(fill);
		g.drawRectangle(fill);

		if (selected) {
			g.setForeground(event.display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			event.detail &= ~SWT.SELECTED;
		}
	}

	private void paintTreeItem2(Event event, TreeItem treeItem, Diff diff) {
		event.detail &= ~SWT.HOT;

		GC g = event.gc;
		Rectangle treeBounds = treeItem.getParent().getBounds();
		Rectangle itemBounds = treeItem.getBounds();

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = 2;
		fill.y = itemBounds.y + itemBounds.height - 3;
		fill.width = treeBounds.width - 6;
		fill.height = 3;

		if (diff.getState() == DifferenceState.DISCARDED || diff.getState() == DifferenceState.MERGED) {
			return;
		}

		// g.setForeground(event.display.getSystemColor(SWT.COLOR_BLUE));
		g.setBackground(event.display.getSystemColor(SWT.COLOR_RED));
		g.fillRectangle(fill);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(Canvas canvas, GC g) {
		Display display = canvas.getDisplay();

		Point size = canvas.getSize();
		int x = 0;
		int w = size.x;

		g.setBackground(canvas.getBackground());
		g.fillRectangle(x + 1, 0, w - 2, size.y);

		if (!fIsMotif) {
			// draw thin line between center ruler and both texts
			g.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
			g.fillRectangle(0, 0, 1, size.y);
			g.fillRectangle(w - 1, 0, 1, size.y);
		}
	}

}
