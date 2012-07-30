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

import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
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
		doUpdateContent(ancestor, getAncestorMergeViewer());
		doUpdateContent(left, getLeftMergeViewer());
		doUpdateContent(right, getRightMergeViewer());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<Tree> getAncestorMergeViewer() {
		return (IMergeViewer<Tree>)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<Tree> getLeftMergeViewer() {
		return (IMergeViewer<Tree>)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected IMergeViewer<Tree> getRightMergeViewer() {
		return (IMergeViewer<Tree>)super.getRightMergeViewer();
	}

	private static void doUpdateContent(Object object, IMergeViewer<? extends Composite> viewer) {
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
			viewer.setSelection(new StructuredSelection(selection));
			// viewer.expandToLevel(selection, 1);
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
		System.out.println("EObjectContentMergeViewer.copy()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffLeftToRight()
	 */
	@Override
	protected void copyDiffLeftToRight() {
		IStructuredSelection selection = (IStructuredSelection)getLeftMergeViewer().getSelection();
		Object firstElement = selection.getFirstElement();
		EList<Diff> differences = getComparison().getDifferences((EObject)firstElement);
		for (Diff diff : differences) {
			if (diff.getSource() == DifferenceSource.LEFT) {
				diff.copyLeftToRight();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffRightToLeft()
	 */
	@Override
	protected void copyDiffRightToLeft() {
		IStructuredSelection selection = (IStructuredSelection)getRightMergeViewer().getSelection();
		Object firstElement = selection.getFirstElement();
		EList<Diff> differences = getComparison().getDifferences((EObject)firstElement);
		for (Diff diff : differences) {
			if (diff.getSource() == DifferenceSource.RIGHT) {
				diff.copyRightToLeft();
			}
		}
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected IMergeViewer<Tree> createMergeViewer(final Composite parent, MergeViewerSide side) {
		final MergeTreeViewer mergeTreeViewer = new MergeTreeViewer(parent, side,
				new AdapterFactoryContentProvider(fAdapterFactory), new AdapterFactoryLabelProvider(
						fAdapterFactory));
		return mergeTreeViewer;
	}

	protected void paint(Event event, IMergeViewer<? extends Scrollable> mergeTreeViewer) {
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
		}
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

		g.setForeground(getColors().getStrokeColor(diff, isThreeWay(), false, selected));
		g.setBackground(getColors().getFillColor(diff, isThreeWay(), false, selected));
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
