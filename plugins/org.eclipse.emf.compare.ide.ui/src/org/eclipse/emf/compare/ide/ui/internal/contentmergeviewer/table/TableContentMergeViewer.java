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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.table;

import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.AbstractBufferedCanvas;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DiffInsertionPoint;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TableContentMergeViewer extends EMFCompareContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = TableContentMergeViewer.class.getName();

	private final AdapterFactory fAdapterFactory;

	private double[] fBasicCenterCurve;

	/**
	 * Call the super constructor.
	 * 
	 * @see TableContentMergeViewer
	 */
	protected TableContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		buildControl(parent);
		setContentProvider(new TableContentMergeViewerContentProvider(config));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		EList<Diff> differences = getComparison().getDifferences();
		boolean merged = false;
		for (Diff diff : differences) {
			if (leftToRight && diff.getSource() == DifferenceSource.LEFT
					&& diff.getState() == DifferenceState.UNRESOLVED) {
				if (diff.getConflict() == null || diff.getConflict().getKind() == ConflictKind.PSEUDO) {
					diff.copyLeftToRight();
					merged = true;
				}
			} else if (!leftToRight && diff.getSource() == DifferenceSource.RIGHT
					&& diff.getState() == DifferenceState.UNRESOLVED) {
				if (diff.getConflict() == null || diff.getConflict().getKind() == ConflictKind.PSEUDO) {
					diff.copyRightToLeft();
					merged = true;
				}
			}
		}
		if (merged) {
			if (leftToRight) {
				setRightDirty(true);
			} else {
				setLeftDirty(true);
			}
			refresh();
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
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffRightToLeft()
	 */
	@Override
	protected void copyDiffRightToLeft() {
		Diff diffToCopy = getDiffToCopy(getRightMergeViewer(), MergeViewerSide.RIGHT);
		if (diffToCopy != null) {
			diffToCopy.copyRightToLeft();
			setLeftDirty(true);
			refresh();
		}
	}

	private Diff getDiffToCopy(IMergeViewer<? extends Scrollable> mergeViewer, MergeViewerSide side) {
		Diff diffToCopy = null;
		ISelection selection = mergeViewer.getSelection();
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			Object firstElement = ((IStructuredSelection)selection).getFirstElement();
			if (firstElement instanceof DiffInsertionPoint) {
				diffToCopy = ((DiffInsertionPoint)firstElement).getDiff();
			} else {
				Object mergeViewerInput = mergeViewer.getInput();
				if (mergeViewerInput instanceof IStructuralFeatureAccessor) {
					diffToCopy = ((IStructuralFeatureAccessor)mergeViewerInput).getDiff(firstElement, side);
				}
			}

		}
		return diffToCopy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiffLeftToRight()
	 */
	@Override
	protected void copyDiffLeftToRight() {
		Diff diffToCopy = getDiffToCopy(getLeftMergeViewer(), MergeViewerSide.LEFT);
		if (diffToCopy != null) {
			diffToCopy.copyLeftToRight();
			setRightDirty(true);
			refresh();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected IMergeViewer<? extends Composite> createMergeViewer(Composite parent, MergeViewerSide side) {
		TableMergeViewer ret = new TableMergeViewer(parent, this, side);
		ret.setContentProvider(new ArrayContentProvider());
		ret.setLabelProvider(new AdapterFactoryLabelProvider(fAdapterFactory));
		ret.getControl().getVerticalBar().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (getCenterControl() instanceof AbstractBufferedCanvas) {
					((AbstractBufferedCanvas)getCenterControl()).repaint();
				}
			}
		});
		ret.getControl().addGestureListener(new GestureListener() {
			public void gesture(GestureEvent e) {
				if (e.detail == SWT.GESTURE_PAN) {
					if (getCenterControl() instanceof AbstractBufferedCanvas) {
						((AbstractBufferedCanvas)getCenterControl()).repaint();
					}
				}
			}
		});

		ret.getControl().addMouseWheelListener(new MouseWheelListener() {
			public void mouseScrolled(MouseEvent e) {
				if (getCenterControl() instanceof AbstractBufferedCanvas) {
					((AbstractBufferedCanvas)getCenterControl()).repaint();
				}
			}
		});
		ret.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (getCenterControl() instanceof AbstractBufferedCanvas) {
					((AbstractBufferedCanvas)getCenterControl()).repaint();
				}
			}
		});
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(Canvas canvas, GC g) {
		TableMergeViewer leftMergeViewer = (TableMergeViewer)getLeftMergeViewer();
		TableMergeViewer rightMergeViewer = (TableMergeViewer)getRightMergeViewer();

		Table leftTable = leftMergeViewer.getControl();
		Table rightTable = rightMergeViewer.getControl();

		TableItem[] leftItems = leftTable.getItems();
		TableItem[] rightItems = rightTable.getItems();

		TableItem[] selection = leftTable.getSelection();

		for (TableItem leftItem : leftItems) {
			final boolean selected;
			if (selection.length > 0) {
				selected = leftItem == selection[0];
			} else {
				selected = false;
			}
			Object leftData = leftItem.getData();

			final Diff leftDiff;
			if (leftData instanceof DiffInsertionPoint) {
				leftDiff = ((DiffInsertionPoint)leftData).getDiff();
			} else {
				leftDiff = ((IStructuralFeatureAccessor)leftMergeViewer.getInput()).getDiff(leftData,
						MergeViewerSide.LEFT);
			}

			if (leftDiff != null) {
				for (TableItem rightItem : rightItems) {
					Object rightData = rightItem.getData();
					final Diff rightDiff;
					if (rightData instanceof DiffInsertionPoint) {
						rightDiff = ((DiffInsertionPoint)rightData).getDiff();
					} else {
						rightDiff = ((IStructuralFeatureAccessor)rightMergeViewer.getInput()).getDiff(
								rightData, MergeViewerSide.RIGHT);
					}

					if (leftDiff == rightDiff) {
						Point from = new Point(0, 0);
						Point to = new Point(0, 0);

						Rectangle leftClientArea = leftMergeViewer.getControl().getClientArea();
						Rectangle rightClientArea = rightMergeViewer.getControl().getClientArea();

						Rectangle leftBounds = leftItem.getBounds();
						Rectangle rightBounds = rightItem.getBounds();

						from.y = leftBounds.y + (leftBounds.height / 2) - leftClientArea.y + 1;

						to.x = canvas.getBounds().width;
						to.y = rightBounds.y + (rightBounds.height / 2) - rightClientArea.y + 1;

						g.setForeground(getColors().getStrokeColor(leftDiff, isThreeWay(), false, selected));

						int[] points = getCenterCurvePoints(from.x, from.y, to.x, to.y);
						for (int i = 1; i < points.length; i++) {
							g.drawLine(from.x + i - 1, points[i - 1], i, points[i]);
						}

						break;
					}
				}
			}
		}
	}

	private int[] getCenterCurvePoints(int startx, int starty, int endx, int endy) {
		if (fBasicCenterCurve == null) {
			buildBaseCenterCurve(endx - startx);
		}
		double height = endy - starty;
		height = height / 2;
		int width = endx - startx;
		int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int)(-height * fBasicCenterCurve[i] + height + starty);
		}
		return points;
	}

	private void buildBaseCenterCurve(int w) {
		double width = w;
		fBasicCenterCurve = new double[getCenterWidth()];
		for (int i = 0; i < getCenterWidth(); i++) {
			double r = i / width;
			fBasicCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}
}
