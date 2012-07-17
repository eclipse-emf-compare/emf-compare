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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.BufferedCanvas;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ComparisonNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Scrollable;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class EMFCompareContentMergeViewer extends ContentMergeViewer implements ISelectionChangedListener {

	/**
	 * 
	 */
	private static final String NORMAL_CURSOR = "fNormalCursor"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String H_SASH_CURSOR = "fHSashCursor"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String HV_SASH_CURSOR = "fHVSashCursor"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String V_SASH_CURSOR = "fVSashCursor"; //$NON-NLS-1$

	protected static final int HORIZONTAL = 1;

	protected static final int VERTICAL = 2;

	protected static final double HSPLIT = 0.5;

	protected static final double VSPLIT = 0.3;

	/** Width of center bar */
	protected static final int CENTER_WIDTH = 34;

	protected final boolean fIsMotif;

	protected final boolean fIsCarbon;

	protected final boolean fIsMac;

	private CompareHandlerService fHandlerService;

	private IMergeViewer<? extends Viewer, ? extends Scrollable> fAncestor;

	private IMergeViewer<? extends Viewer, ? extends Scrollable> fLeft;

	private IMergeViewer<? extends Viewer, ? extends Scrollable> fRight;

	private final DynamicObject fDynamicObject;

	private ActionContributionItem fCopyDiffLeftToRightItem;

	private ActionContributionItem fCopyDiffRightToLeftItem;

	private final Comparison fComparison;

	private final AtomicBoolean syncingSelections = new AtomicBoolean(false);

	/**
	 * @param style
	 * @param bundle
	 * @param cc
	 */
	protected EMFCompareContentMergeViewer(int style, ResourceBundle bundle, CompareConfiguration cc) {
		super(style, bundle, cc);
		fIsMotif = Util.isMotif();
		fIsCarbon = Util.isCarbon();
		fIsMac = Util.isMac();

		fDynamicObject = new DynamicObject(this);

		fComparison = ((ComparisonNode)cc.getProperty("EMF.COMPARE.RESULT")).getTarget();
	}

	/**
	 * @return the fComparison
	 */
	protected final Comparison getComparison() {
		return fComparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		fAncestor = createMergeViewer(composite, MergeViewerSide.ANCESTOR);
		addListenersToViewer(fAncestor);

		fLeft = createMergeViewer(composite, MergeViewerSide.LEFT);
		addListenersToViewer(fLeft);
		fLeft.getControl().getVerticalBar().setVisible(false);

		fRight = createMergeViewer(composite, MergeViewerSide.RIGHT);
		addListenersToViewer(fRight);
		fRight.getControl().getVerticalBar().setVisible(false);
	}

	/**
	 * @param fAncestor2
	 */
	private void addListenersToViewer(
			final IMergeViewer<? extends Viewer, ? extends Scrollable> mergeTreeViewer) {
		Scrollable mergeTreeViewerControl = mergeTreeViewer.getControl();
		mergeTreeViewerControl.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				paint(event, mergeTreeViewer);
			}
		});

		mergeTreeViewerControl.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = (int)(event.gc.getFontMetrics().getHeight() * 1.33);
				if (event.height % 2 == 1) {
					event.height += 1;
				}
			}
		});

		mergeTreeViewer.getViewer().addSelectionChangedListener(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		Action a;
		fHandlerService = CompareHandlerService.createFor(getCompareConfiguration().getContainer(), getLeft()
				.getViewer().getControl().getShell());

		CompareConfiguration cc = getCompareConfiguration();
		if (cc.isRightEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiffLeftToRight();
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffLeftToRight."); //$NON-NLS-1$
			fCopyDiffLeftToRightItem = new ActionContributionItem(a);
			fCopyDiffLeftToRightItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffLeftToRightItem); //$NON-NLS-1$
			fHandlerService.registerAction(a, "org.eclipse.compare.copyLeftToRight"); //$NON-NLS-1$
		}

		if (cc.isLeftEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiffRightToLeft();
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffRightToLeft."); //$NON-NLS-1$
			fCopyDiffRightToLeftItem = new ActionContributionItem(a);
			fCopyDiffRightToLeftItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffRightToLeftItem); //$NON-NLS-1$
			fHandlerService.registerAction(a, "org.eclipse.compare.copyRightToLeft"); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 */
	protected abstract void copyDiffRightToLeft();

	/**
	 * 
	 */
	protected abstract void copyDiffLeftToRight();

	/**
	 * @param e
	 * @param mergeTreeViewer
	 */
	protected abstract void paint(Event e,
			IMergeViewer<? extends Viewer, ? extends Scrollable> mergeTreeViewer);

	/**
	 * Set the currently active Diff and update the toolbars controls and lines. If
	 * <code>revealAndSelect</code> is <code>true</code> the Diff is revealed and selected in both Parts.
	 */
	protected void setCurrentDiff(Object d, boolean revealAndSelect) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeAncestor(int, int, int, int)
	 */
	@Override
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		if (width > 0) {
			getAncestor().getControl().setVisible(true);
			getAncestor().getControl().setBounds(x, y, width, height);
		} else {
			getAncestor().getControl().setVisible(false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeLeftRight(int, int, int,
	 *      int, int, int)
	 */
	@Override
	protected void handleResizeLeftRight(int x, int y, int width1, int centerWidth, int width2, int height) {
		fLeft.getControl().setBounds(x, y, width1, height);
		fRight.getControl().setBounds(x + width1 + centerWidth, y, width2, height);
	}

	protected abstract IMergeViewer<? extends Viewer, ? extends Scrollable> createMergeViewer(
			Composite parent, MergeViewerSide side);

	@Override
	protected final int getCenterWidth() {
		return CENTER_WIDTH;
	}

	protected void setCenterControl(Control center) {
		fDynamicObject.set("fCenter", center);
	}

	protected CLabel getAncestorLabel() {
		return (CLabel)fDynamicObject.get("fAncestorLabel");
	}

	protected CLabel getLeftLabel() {
		return (CLabel)fDynamicObject.get("fLeftLabel");
	}

	protected CLabel getRightLabel() {
		return (CLabel)fDynamicObject.get("fRightLabel");
	}

	protected Cursor getVSashCursor() {
		return (Cursor)fDynamicObject.get(V_SASH_CURSOR);
	}

	protected Cursor getHVSashCursor() {
		return (Cursor)fDynamicObject.get(HV_SASH_CURSOR);
	}

	protected double getHSplit() {
		return fDynamicObject.getDouble("fHSplit");
	}

	protected double getVSplit() {
		return fDynamicObject.getDouble("fVSplit");
	}

	protected void setHSplit(double value) {
		fDynamicObject.setDouble("fHSplit", value);
	}

	protected void getVSplit(double value) {
		fDynamicObject.setDouble("fVSplit", value);
	}

	protected void setVSplit(double value) {
		fDynamicObject.setDouble("fVSplit", value);
	}

	protected Cursor getHSashCursor() {
		return (Cursor)fDynamicObject.get(H_SASH_CURSOR);
	}

	protected Cursor getNormalCursor() {
		return (Cursor)fDynamicObject.get(NORMAL_CURSOR);
	}

	protected void setVSashCursor(Cursor cursor) {
		fDynamicObject.set(V_SASH_CURSOR, cursor);
	}

	protected void setHVSashCursor(Cursor cursor) {
		fDynamicObject.set(HV_SASH_CURSOR, cursor);
	}

	protected void setHSashCursor(Cursor cursor) {
		fDynamicObject.set(H_SASH_CURSOR, cursor);
	}

	protected void setNormalCursor(Cursor cursor) {
		fDynamicObject.set(NORMAL_CURSOR, cursor);
	}

	protected void updateCursor_(Control c, int dir) {
		if (!(c instanceof Sash)) {
			Cursor cursor = null;
			switch (dir) {
				case VERTICAL:
					if (isAncestorVisible()) {
						if (getVSashCursor() == null) {
							setVSashCursor(new Cursor(c.getDisplay(), SWT.CURSOR_SIZENS));
						}
						cursor = getVSashCursor();
					} else {
						if (getNormalCursor() == null) {
							setNormalCursor(new Cursor(c.getDisplay(), SWT.CURSOR_ARROW));
						}
						cursor = getNormalCursor();
					}
					break;
				case HORIZONTAL:
					if (getHSashCursor() == null) {
						setHSashCursor(new Cursor(c.getDisplay(), SWT.CURSOR_SIZEWE));
					}
					cursor = getHSashCursor();
					break;
				case VERTICAL + HORIZONTAL:
					if (isAncestorVisible()) {
						if (getHVSashCursor() == null) {
							setHVSashCursor(new Cursor(c.getDisplay(), SWT.CURSOR_SIZEALL));
						}
						cursor = getHVSashCursor();
					} else {
						if (getHSashCursor() == null) {
							setHSashCursor(new Cursor(c.getDisplay(), SWT.CURSOR_SIZEWE));
						}
						cursor = getHSashCursor();
					}
					break;
			}
			if (cursor != null) {
				c.setCursor(cursor);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getControl()
	 */
	@Override
	public Composite getControl() {
		return (Composite)super.getControl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createCenterControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createCenterControl(Composite parent) {
		final Canvas canvas = new BufferedCanvas(parent, SWT.NONE) {
			@Override
			public void doPaint(GC gc) {
				paintCenter(this, gc);
			}
		};

		new Resizer(canvas, HORIZONTAL);

		if (getNormalCursor() == null) {
			setNormalCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_ARROW));
		}

		return canvas;
	}

	protected abstract void paintCenter(Canvas canvas, GC g);

	protected class Resizer extends MouseAdapter implements MouseMoveListener {

		Control fControl;

		int fX, fY;

		int fWidth1, fWidth2;

		int fHeight1, fHeight2;

		int fDirection;

		boolean fLiveResize;

		boolean fIsDown;

		public Resizer(Control c, int dir) {
			fDirection = dir;
			fControl = c;
			fLiveResize = !(fControl instanceof Sash);
			updateCursor_(c, dir);
			fControl.addMouseListener(this);
			fControl.addMouseMoveListener(this);
			fControl.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					fControl = null;
				}
			});
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if ((fDirection & HORIZONTAL) != 0) {
				setHSplit(-1);
			}
			if ((fDirection & VERTICAL) != 0) {
				setVSplit(VSPLIT);
			}
			getControl().layout(true);
		}

		@Override
		public void mouseDown(MouseEvent e) {
			Composite parent = fControl.getParent();

			Point s = parent.getSize();
			Point as = getAncestorLabel().getSize();
			Point ys = getLeftLabel().getSize();
			Point ms = getRightLabel().getSize();

			fWidth1 = ys.x;
			fWidth2 = ms.x;
			fHeight1 = getLeftLabel().getLocation().y - as.y;
			fHeight2 = s.y - (getLeftLabel().getLocation().y + ys.y);

			fX = e.x;
			fY = e.y;
			fIsDown = true;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			fIsDown = false;
			if (!fLiveResize) {
				resize(e);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {
			if (fIsDown && fLiveResize) {
				resize(e);
			}
		}

		private void resize(MouseEvent e) {
			int dx = e.x - fX;
			int dy = e.y - fY;

			int centerWidth = getCenterControl().getSize().x;

			if (fWidth1 + dx > centerWidth && fWidth2 - dx > centerWidth) {
				fWidth1 += dx;
				fWidth2 -= dx;
				if ((fDirection & HORIZONTAL) != 0) {
					setHSplit((double)fWidth1 / (double)(fWidth1 + fWidth2));
				}
			}
			if (fHeight1 + dy > centerWidth && fHeight2 - dy > centerWidth) {
				fHeight1 += dy;
				fHeight2 -= dy;
				if ((fDirection & VERTICAL) != 0) {
					setVSplit((double)fHeight1 / (double)(fHeight1 + fHeight2));
				}
			}

			getControl().layout(true);
			fControl.getDisplay().update();
		}
	}

	/**
	 * @return the fAncestor
	 */
	protected IMergeViewer<? extends Viewer, ? extends Scrollable> getAncestor() {
		return fAncestor;
	}

	/**
	 * @return the fLeft
	 */
	protected IMergeViewer<? extends Viewer, ? extends Scrollable> getLeft() {
		return fLeft;
	}

	/**
	 * @return the fRight
	 */
	protected IMergeViewer<? extends Viewer, ? extends Scrollable> getRight() {
		return fRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if (syncingSelections.compareAndSet(false, true)) { // prevents stack overflow :)
			try {
				ISelectionProvider selectionProvider = event.getSelectionProvider();
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object firstElement = selection.getFirstElement();
				Match match = getComparison().getMatch((EObject)firstElement);
				EObject right = match.getRight();
				EObject origin = match.getOrigin();
				if (selectionProvider == getLeft().getViewer()) {
					getRight().getViewer().setSelection(
							right == null ? StructuredSelection.EMPTY : new StructuredSelection(right), true);
					getAncestor().getViewer().setSelection(
							origin == null ? StructuredSelection.EMPTY : new StructuredSelection(origin),
							true);
				} else {
					EObject left = match.getLeft();
					if (selectionProvider == getRight().getViewer()) {
						getLeft().getViewer().setSelection(
								left == null ? StructuredSelection.EMPTY : new StructuredSelection(left),
								true);
						getAncestor().getViewer().setSelection(
								origin == null ? StructuredSelection.EMPTY : new StructuredSelection(origin),
								true);
					} else if (selectionProvider == getAncestor().getViewer()) {
						getLeft().getViewer().setSelection(
								left == null ? StructuredSelection.EMPTY : new StructuredSelection(left),
								true);
						getRight().getViewer().setSelection(
								right == null ? StructuredSelection.EMPTY : new StructuredSelection(right),
								true);
					}
				}
			} finally {
				syncingSelections.set(false);
			}
		}
	}
}
