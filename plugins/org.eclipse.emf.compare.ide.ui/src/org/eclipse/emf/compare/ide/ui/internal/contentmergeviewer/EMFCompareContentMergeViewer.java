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

import java.util.EventObject;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.EMFCompareColor;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.ComparisonNode;
import org.eclipse.emf.compare.ide.ui.internal.util.EMFCompareEditingDomain;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColorProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IStructuralFeatureAccessor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class EMFCompareContentMergeViewer extends ContentMergeViewer implements ISelectionChangedListener, ICompareColorProvider {

	private static final String HANDLER_SERVICE = "fHandlerService";

	protected static final int HORIZONTAL = 1;

	protected static final int VERTICAL = 2;

	protected static final double HSPLIT = 0.5;

	protected static final double VSPLIT = 0.3;

	/**
	 * Width of center bar
	 */
	protected static final int CENTER_WIDTH = 34;

	private MergeViewer fAncestor;

	private MergeViewer fLeft;

	private MergeViewer fRight;

	private ActionContributionItem fCopyDiffLeftToRightItem;

	private ActionContributionItem fCopyDiffRightToLeftItem;

	private final Comparison fComparison;

	private final AtomicBoolean fSyncingSelections = new AtomicBoolean(false);

	private EMFCompareColor fColors;

	private final EMFCompareEditingDomain fEditingDomain;

	private final DynamicObject fDynamicObject;

	/**
	 * @param style
	 * @param bundle
	 * @param cc
	 */
	protected EMFCompareContentMergeViewer(int style, ResourceBundle bundle, CompareConfiguration cc) {
		super(style, bundle, cc);
		fDynamicObject = new DynamicObject(this);

		fComparison = ((ComparisonNode)cc.getProperty(EMFCompareConstants.COMPARE_RESULT)).getTarget();

		fEditingDomain = (EMFCompareEditingDomain)getCompareConfiguration().getProperty(
				EMFCompareConstants.EDITING_DOMAIN);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColorProvider#getCompareColor()
	 */
	public ICompareColor getCompareColor() {
		return fColors;
	}

	/**
	 * @return the fEditingDomain
	 */
	protected final EMFCompareEditingDomain getEditingDomain() {
		return fEditingDomain;
	}

	/**
	 * @return the fComparison
	 */
	protected final Comparison getComparison() {
		return fComparison;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overriden to enhance visibility.
	 * </p>
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#isThreeWay()
	 */
	@Override
	public boolean isThreeWay() {
		// enhances visibility
		return super.isThreeWay();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		fAncestor.setInput(ancestor);
		fLeft.setInput(left);
		fRight.setInput(right);
		// must update selection after the three viewers input has been set
		// to avoid some NPE/AssertionError (they are calling each other on selectionChanged event to
		// synchronize their selection)

		IMergeViewerItem leftInitialItem = null;
		if (left instanceof IStructuralFeatureAccessor) {
			leftInitialItem = ((IStructuralFeatureAccessor)left).getInitialItem();
		}

		ISelection leftSelection = createSelectionOrEmpty(leftInitialItem);
		fLeft.setSelection(leftSelection); // others will synchronize on this one :)

		getCenterControl().redraw();
	}

	private ISelection createSelectionOrEmpty(final Object o) {
		final ISelection selection;
		if (o != null) {
			selection = new StructuredSelection(o);
		} else {
			selection = StructuredSelection.EMPTY;
		}
		return selection;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		fAncestor = createMergeViewer(composite, MergeViewerSide.ANCESTOR);
		fAncestor.addSelectionChangedListener(this);

		fLeft = createMergeViewer(composite, MergeViewerSide.LEFT);
		fLeft.addSelectionChangedListener(this);

		fRight = createMergeViewer(composite, MergeViewerSide.RIGHT);
		fRight.addSelectionChangedListener(this);

		fColors = new EMFCompareColor(this, null, getCompareConfiguration());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		Action a;

		CompareConfiguration cc = getCompareConfiguration();
		if (cc.isRightEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiff(true);
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffLeftToRight."); //$NON-NLS-1$
			fCopyDiffLeftToRightItem = new ActionContributionItem(a);
			fCopyDiffLeftToRightItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffLeftToRightItem); //$NON-NLS-1$
			getHandlerService().registerAction(a, "org.eclipse.compare.copyLeftToRight"); //$NON-NLS-1$
		}

		if (cc.isLeftEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiff(false);
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffRightToLeft."); //$NON-NLS-1$
			fCopyDiffRightToLeftItem = new ActionContributionItem(a);
			fCopyDiffRightToLeftItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffRightToLeftItem); //$NON-NLS-1$
			getHandlerService().registerAction(a, "org.eclipse.compare.copyRightToLeft"); //$NON-NLS-1$
		}

		final UndoAction undoAction = new UndoAction(fEditingDomain);
		final RedoAction redoAction = new RedoAction(fEditingDomain);

		fEditingDomain.getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				undoAction.update();
				redoAction.update();
				refresh();
			}
		});

		getHandlerService().setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		getHandlerService().setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		EList<Diff> differences = getComparison().getDifferences();

		final Command copyCommand = getEditingDomain().createCopyAllNonConflictingCommand(differences,
				leftToRight);

		getEditingDomain().getCommandStack().execute(copyCommand);

		if (leftToRight) {
			setRightDirty(true);
		} else {
			setLeftDirty(true);
		}
		refresh();
	}

	/**
	 * 
	 */
	protected abstract void copyDiff(boolean leftToRight);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeAncestor(int, int, int, int)
	 */
	@Override
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		if (width > 0) {
			getAncestorMergeViewer().getControl().setVisible(true);
			getAncestorMergeViewer().getControl().setBounds(x, y, width, height);
		} else {
			getAncestorMergeViewer().getControl().setVisible(false);
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

	protected abstract MergeViewer createMergeViewer(Composite parent, MergeViewerSide side);

	@Override
	protected final int getCenterWidth() {
		return CENTER_WIDTH;
	}

	protected final CompareHandlerService getHandlerService() {
		return (CompareHandlerService)fDynamicObject.get(HANDLER_SERVICE);
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
		final Control ret = super.createCenterControl(parent);

		final PaintListener paintListener = new PaintListener() {
			public void paintControl(PaintEvent e) {
				paintCenter(e.gc);
			}
		};
		ret.addPaintListener(paintListener);

		ret.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				ret.removePaintListener(paintListener);
			}
		});

		new Resizer(ret);

		return ret;
	}

	private class Resizer extends MouseAdapter implements MouseMoveListener {

		Control fControl;

		boolean fIsDown;

		public Resizer(Control c) {
			fControl = c;
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
			getControl().layout(true);
		}

		@Override
		public void mouseDown(MouseEvent e) {
			fIsDown = true;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			fIsDown = false;
			resize();
		}

		public void mouseMove(MouseEvent e) {
			if (fIsDown) {
				resize();
			}
		}

		private void resize() {
			getControl().layout(true);
			fControl.getDisplay().update();
		}
	}

	protected abstract void paintCenter(GC g);

	/**
	 * @return the fAncestor
	 */
	protected MergeViewer getAncestorMergeViewer() {
		return fAncestor;
	}

	/**
	 * @return the fLeft
	 */
	protected MergeViewer getLeftMergeViewer() {
		return fLeft;
	}

	/**
	 * @return the fRight
	 */
	protected MergeViewer getRightMergeViewer() {
		return fRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if (fSyncingSelections.compareAndSet(false, true)) { // prevents stack overflow :)
			try {
				ISelectionProvider selectionProvider = event.getSelectionProvider();
				ISelection selection = event.getSelection();
				synchronizeSelection(selectionProvider, selection);
			} finally {
				fSyncingSelections.set(false);
			}
		}
	}

	private void synchronizeSelection(final ISelectionProvider selectionProvider, final ISelection selection) {
		if (selectionProvider == fLeft) {
			fRight.setSelection(selection);
			fAncestor.setSelection(selection);
		} else if (selectionProvider == fRight) {
			fLeft.setSelection(selection);
			fAncestor.setSelection(selection);
		} else { // selectionProvider == fAncestor
			fLeft.setSelection(selection);
			fRight.setSelection(selection);
		}
	}
}
