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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class MergeTableViewer implements IMergeViewer<Table> {

	private final TableViewer fViewer;

	private final MergeViewerSide fSide;

	private IManyStructuralFeatureAccessor<?> fManyFeatureAccessor;

	private final EMFCompareContentMergeViewer fContentMergeViewer;

	private ImmutableMap<Match, DiffInsertionPoint> fInsertionPoints;

	private final ListenerList fSelectionChangedListeners;

	MergeTableViewer(Composite parent, EMFCompareContentMergeViewer contentMergeViewer, MergeViewerSide side) {
		fViewer = new TableViewer(parent);
		fContentMergeViewer = contentMergeViewer;
		fSide = side;

		fInsertionPoints = ImmutableMap.of();
		fSelectionChangedListeners = new ListenerList();

		fViewer.getTable().addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				MergeTableViewer.this.handleEraseItemEvent(event);
			}
		});

		fViewer.getTable().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = (int)(event.gc.getFontMetrics().getHeight() * 1.33d);
				if (event.height % 2 == 1) {
					event.height += 1;
				}
			}
		});

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getControl()
	 */
	public Table getControl() {
		return fViewer.getTable();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getViewer()
	 */
	public TableViewer getViewer() {
		return fViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getSide()
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setInput(java.lang.Object)
	 */
	public void setInput(Object object) {
		if (object instanceof IManyStructuralFeatureAccessor<?>) {
			fManyFeatureAccessor = (IManyStructuralFeatureAccessor<?>)object;
			final List<Object> values = newArrayList(fManyFeatureAccessor.getValues());
			addInsertionPoints(values);
			fViewer.setInput(values);
		} else {
			fManyFeatureAccessor = null;
			fViewer.setInput(null);
		}
	}

	public void setSelection(Object selection) {
		if (selection instanceof IManyStructuralFeatureAccessor<?>) {
			setSelection((IManyStructuralFeatureAccessor<?>)selection);
		} else if (selection instanceof Match) {
			setSelection((Match)selection);
		} else if (selection instanceof ISelection) {
			setSelection((ISelection)selection);
		} else if (selection instanceof List<?>) {
			setSelection(new StructuredSelection((List<?>)selection));
		} else {
			setSelection(new StructuredSelection(selection));
		}
	}

	public void setSelection(IManyStructuralFeatureAccessor<?> selection) {
		if (selection != null) {
			final Object value = selection.getValue();
			if (((Collection<?>)fViewer.getInput()).contains(value)) {
				setSelection(new StructuredSelection(value));
			} else {
				setSelection(StructuredSelection.EMPTY);
			}
		} else {
			setSelection(StructuredSelection.EMPTY);
		}
	}

	private void addInsertionPoints(final List<Object> values) {
		ImmutableMap.Builder<Match, DiffInsertionPoint> insertionsPoints = ImmutableMap.builder();
		for (ReferenceChange diff : filter(fManyFeatureAccessor.getDiffFromTheOtherSide().reverse(),
				ReferenceChange.class)) {
			boolean rightToLeft = fSide == MergeViewerSide.LEFT;

			Match match = diff.getMatch();
			int insertionIndex = DiffUtil.findInsertionIndex(match.getComparison(), new EqualityHelper(),
					diff, rightToLeft);
			Match matchOfDiffValue = match.getComparison().getMatch(diff.getValue());
			DiffInsertionPoint insertionPoint = new DiffInsertionPoint(diff);

			values.add(insertionIndex, insertionPoint);
			insertionsPoints.put(matchOfDiffValue, insertionPoint);
		}
		fInsertionPoints = insertionsPoints.build();
	}

	public void setSelection(Match match) {
		final EObject eObject;
		switch (fSide) {
			case ANCESTOR:
				eObject = match.getOrigin();
				break;
			case LEFT:
				eObject = match.getLeft();
				break;
			case RIGHT:
				eObject = match.getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		final DiffInsertionPoint insertionPoint = fInsertionPoints.get(match);
		ISelection selection = createSelectionForFirstNonNull(eObject, insertionPoint);
		fViewer.setSelection(selection);
	}

	private ISelection createSelectionForFirstNonNull(final Object first, final Object second) {
		final ISelection selection;
		if (first != null) {
			selection = new StructuredSelection(first);
		} else if (second != null) {
			selection = new StructuredSelection(second);
		} else {
			selection = StructuredSelection.EMPTY;
		}
		return selection;
	}

	private void handleEraseItemEvent(Event event) {
		TableItem tableItem = (TableItem)event.item;
		Object data = tableItem.getData();

		boolean specialPaint = false;
		if (data instanceof DiffInsertionPoint) {
			DiffInsertionPoint insertionPoint = (DiffInsertionPoint)data;
			paintItemInsertionPoint(event, tableItem, insertionPoint.getDiff());
			specialPaint = true;
		}
		if (fManyFeatureAccessor != null) {
			for (Diff diff : fManyFeatureAccessor.getDiffFromThisSide()) {
				if (diff instanceof ReferenceChange) {
					if (fManyFeatureAccessor.getValue(((ReferenceChange)diff)) == data) {
						paintItemDiffBox(event, tableItem, diff);
						specialPaint = true;
					}
				}
			}
			if (fSide == MergeViewerSide.ANCESTOR) {
				for (Diff diff : fManyFeatureAccessor.getDiffFromAncestor()) {
					if (diff instanceof ReferenceChange) {
						if (fManyFeatureAccessor.getValue(((ReferenceChange)diff)) == data) {
							paintItemDiffBox(event, tableItem, diff);
							specialPaint = true;
						}
					}
				}
			}
		}

		if (!specialPaint) {
			paintItem(event, tableItem);
		}
	}

	/**
	 * @param event
	 * @param tableItem
	 */
	private void paintItem(Event event, TableItem tableItem) {
		event.detail &= ~SWT.HOT;

		if (isSelected(event)) {
			Rectangle fill = getDrawingBounds(tableItem);
			drawSelectionBox(event, fill);
		}
	}

	/**
	 * @param event
	 * @param treeItem
	 * @param diff
	 */
	private void paintItemInsertionPoint(Event event, TableItem tableItem, Diff diff) {
		event.detail &= ~SWT.HOT;

		if (diff.getState() == DifferenceState.DISCARDED || diff.getState() == DifferenceState.MERGED) {
			return;
		}

		GC g = event.gc;
		Color oldBackground = g.getBackground();
		Color oldForeground = g.getForeground();

		Rectangle fill = getDrawingBounds(tableItem);
		fill.y += 6;
		fill.height -= 12;
		setDiffColorsToGC(g, diff, isSelected(event));
		g.fillRectangle(fill);
		g.drawRectangle(fill);

		if (isSelected(event)) {
			g.setForeground(event.display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			g.setBackground(event.display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			event.detail &= ~SWT.SELECTED;
		} else {
			g.setBackground(oldBackground);
			g.setForeground(oldForeground);
		}
	}

	private void paintItemDiffBox(Event event, TableItem tableItem, Diff diff) {
		event.detail &= ~SWT.HOT;

		if (diff.getState() == DifferenceState.DISCARDED || diff.getState() == DifferenceState.MERGED) {
			return;
		}

		GC g = event.gc;
		Color oldBackground = g.getBackground();
		Color oldForeground = g.getForeground();

		Rectangle fill = getDrawingBounds(tableItem);
		setDiffColorsToGC(g, diff, isSelected(event));
		g.fillRectangle(fill);
		g.drawRectangle(fill);

		if (isSelected(event)) {
			g.setForeground(event.display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			g.setBackground(event.display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			event.detail &= ~SWT.SELECTED;
		} else {
			g.setBackground(oldBackground);
			g.setForeground(oldForeground);
		}
	}

	private static boolean isSelected(Event event) {
		return (event.detail & SWT.SELECTED) != 0;
	}

	private static void drawSelectionBox(Event event, Rectangle fill) {
		Display display = event.display;
		GC g = event.gc;
		g.setBackground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
		g.setForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		g.fillRectangle(fill);
		event.detail &= ~SWT.SELECTED;
	}

	private static Rectangle getDrawingBounds(TableItem tableItem) {
		Rectangle tableBounds = tableItem.getParent().getBounds();
		Rectangle itemBounds = tableItem.getBounds();

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = 2;
		fill.y = itemBounds.y + 2;
		fill.width = tableBounds.width - 6;
		fill.height = itemBounds.height - 3;
		return fill;
	}

	private void setDiffColorsToGC(GC g, Diff diff, boolean selected) {
		g.setForeground(fContentMergeViewer.getColors().getStrokeColor(diff,
				fContentMergeViewer.isThreeWay(), false, selected));
		g.setBackground(fContentMergeViewer.getColors().getFillColor(diff, fContentMergeViewer.isThreeWay(),
				false, selected));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return fViewer.getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		Control control = getControl();
		if (control == null || control.isDisposed()) {
			return;
		}
		fViewer.setSelection(selection, true);
		ISelection sel = getSelection();
		fireSelectionChanged(new SelectionChangedEvent(this, sel));
	}

	/**
	 * @param selectionChangedEvent
	 */
	private void fireSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = fSelectionChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener)listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IInputProvider#getInput()
	 */
	public Object getInput() {
		return fViewer.getInput();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	public void setContentProvider(IContentProvider contentProvider) {
		fViewer.setContentProvider(contentProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setLabelProvider(org.eclipse.jface.viewers.ILabelProvider)
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		fViewer.setLabelProvider(labelProvider);
	}

}
