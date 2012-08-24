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
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.MatchedObject;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.AbstractBufferedCanvas;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemFontProvider;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
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

		final Command copyCommand;
		if (leftToRight) {
			copyCommand = getEditingDomain().createCopyAllNonConflictingLeftToRightCommand(differences);
		} else {
			copyCommand = getEditingDomain().createCopyAllNonConflictingRightToLeftCommand(differences);
		}

		getEditingDomain().getCommandStack().execute(copyCommand);

		if (leftToRight) {
			setRightDirty(true);
		} else {
			setLeftDirty(true);
		}
		refresh();
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
		Diff diffToCopy = getDiffToCopy(getRightMergeViewer());
		if (diffToCopy != null) {
			Command copyCommand = getEditingDomain().createCopyRightToLeftCommand(diffToCopy);
			getEditingDomain().getCommandStack().execute(copyCommand);

			setLeftDirty(true);
			refresh();
		}
	}

	private Diff getDiffToCopy(IMergeViewer<? extends Scrollable> mergeViewer) {
		Diff diffToCopy = null;
		ISelection selection = mergeViewer.getSelection();
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			Object firstElement = ((IStructuredSelection)selection).getFirstElement();
			if (firstElement instanceof IMergeViewerItem) {
				diffToCopy = ((IMergeViewerItem)firstElement).getDiff();
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
		Diff diffToCopy = getDiffToCopy(getLeftMergeViewer());
		if (diffToCopy != null) {
			Command copyCommand = getEditingDomain().createCopyLeftToRightCommand(diffToCopy);
			getEditingDomain().getCommandStack().execute(copyCommand);

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
	protected IMergeViewer<? extends Composite> createMergeViewer(Composite parent, final MergeViewerSide side) {
		TableMergeViewer ret = new TableMergeViewer(parent, this, side);
		ret.setContentProvider(new ArrayContentProvider());
		ret.setLabelProvider(new AdapterFactoryLabelProvider.FontAndColorProvider(fAdapterFactory, ret
				.getStructuredViewer()) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
			 */
			@Override
			public Font getFont(Object object) {
				Font font = super.getFont(object);
				if (object instanceof EObject && ((EObject)object).eIsProxy()) {
					return getFontFromObject(IItemFontProvider.ITALIC_FONT);
				}
				return font;
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnText(java.lang.Object,
			 *      int)
			 */
			@Override
			public String getColumnText(Object object, int columnIndex) {
				if (object instanceof MatchedObject) {
					return super.getColumnText(((MatchedObject)object).getSideValue(side), columnIndex);
				}
				return super.getColumnText(object, columnIndex);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnImage(java.lang.Object,
			 *      int)
			 */
			@Override
			public Image getColumnImage(Object object, int columnIndex) {
				if (object instanceof MatchedObject) {
					return super.getColumnImage(((MatchedObject)object).getSideValue(side), columnIndex);
				}
				return super.getColumnImage(object, columnIndex);
			}
		});
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

		Rectangle leftClientArea = leftMergeViewer.getControl().getClientArea();
		Rectangle rightClientArea = rightMergeViewer.getControl().getClientArea();

		TableItem[] leftItems = leftMergeViewer.getControl().getItems();
		TableItem[] rightItems = rightMergeViewer.getControl().getItems();

		TableItem[] selection = leftMergeViewer.getControl().getSelection();

		for (TableItem leftItem : leftItems) {
			final boolean selected;
			if (selection.length > 0) {
				selected = leftItem == selection[0];
			} else {
				selected = false;
			}
			final Diff leftDiff = ((IMergeViewerItem)leftItem.getData()).getDiff();

			if (leftDiff != null && leftDiff.getState() == DifferenceState.UNRESOLVED) {
				TableItem rightItem = findRightTableItemFromLeftDiff(rightItems, leftDiff);

				if (rightItem != null) {
					Color strokeColor = getColors().getStrokeColor(leftDiff, isThreeWay(), false, selected);
					g.setForeground(strokeColor);
					drawCenterLine(g, leftClientArea, rightClientArea, leftItem, rightItem);
				}
			}
		}
	}

	private void drawCenterLine(GC g, Rectangle leftClientArea, Rectangle rightClientArea,
			TableItem leftItem, TableItem rightItem) {
		Canvas canvas = (Canvas)getCenterControl();
		Point from = new Point(0, 0);
		Point to = new Point(0, 0);

		Rectangle leftBounds = leftItem.getBounds();
		Rectangle rightBounds = rightItem.getBounds();

		from.y = leftBounds.y + (leftBounds.height / 2) - leftClientArea.y + 1;

		to.x = canvas.getBounds().width;
		to.y = rightBounds.y + (rightBounds.height / 2) - rightClientArea.y + 1;

		int[] points = getCenterCurvePoints(from, to);
		for (int i = 1; i < points.length; i++) {
			g.drawLine(from.x + i - 1, points[i - 1], i, points[i]);
		}
	}

	private TableItem findRightTableItemFromLeftDiff(TableItem[] rightItems, Diff leftDiff) {
		TableItem ret = null;
		for (int i = 0; i < rightItems.length && ret == null; i++) {
			TableItem rightItem = rightItems[i];
			final Diff rightDiff = ((IMergeViewerItem)rightItem.getData()).getDiff();
			if (leftDiff == rightDiff) {
				ret = rightItem;
			}
		}
		return ret;
	}

	private int[] getCenterCurvePoints(Point from, Point to) {
		int startx = from.x;
		int starty = from.y;
		int endx = to.x;
		int endy = to.y;
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
