/*******************************************************************************
 * Copyright (c) 2013, 2020 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - adaptation for refactoring regarding SizeChange, bug 514079
 *     Simon Delisle - bug 511047
 *     Camille Letavernier - bug 529882
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;

import com.google.common.collect.Iterators;

import java.util.EventObject;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.impl.CopyCommand;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramDiffAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.TreeContentMergeViewerContentProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.PlatformUI;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
@SuppressWarnings("restriction")
public class DiagramContentMergeViewer extends EMFCompareContentMergeViewer implements ZoomListener {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = DiagramContentMergeViewer.class.getName();

	/** The phantom manager to use in the context of this viewer. */
	private DecoratorsManager fDecoratorsManager;

	/** The current "opened" difference. */
	private Diff fCurrentSelectedDiff;

	/** Flag to store whether this viewer synchronizes the zoom level of all diagrams. */
	private boolean isSynchronizingZoom;

	/** The contribution item to set the zoom level. */
	private ZoomComboContributionItem zoomItem;

	/**
	 * Creates a new {@link DiagramContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link EMFCompareConfiguration}
	 */
	public DiagramContentMergeViewer(Composite parent, EMFCompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		buildControl(parent);
		setContentProvider(new TreeContentMergeViewerContentProvider(config));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public DiagramMergeViewer getAncestorMergeViewer() {
		return (DiagramMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public DiagramMergeViewer getLeftMergeViewer() {
		return (DiagramMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public DiagramMergeViewer getRightMergeViewer() {
		return (DiagramMergeViewer)super.getRightMergeViewer();
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
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected IMergeViewer createMergeViewer(Composite parent, MergeViewerSide side) {
		final DiagramMergeViewer diagramMergeViewer = new DiagramMergeViewer(parent, side,
				getCompareConfiguration());
		return diagramMergeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(GC g) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {

		fDecoratorsManager = new DecoratorsManager(getCompareConfiguration(), getLeftMergeViewer(),
				getRightMergeViewer(), getAncestorMergeViewer(), getCompareColor());

		// Delete decorators at each selection of a difference (force the computation)
		fDecoratorsManager.hideAll();
		fDecoratorsManager.removeAll();

		super.updateContent(ancestor, left, right);

		getLeftMergeViewer().getGraphicalViewer().flush();
		getRightMergeViewer().getGraphicalViewer().flush();
		getAncestorMergeViewer().getGraphicalViewer().flush();

		if (left instanceof IDiagramNodeAccessor) {
			// Compute and display the decorators related to the selected difference (if not merged and
			// different from the current one)
			if (left instanceof IDiagramDiffAccessor) {
				IDiagramDiffAccessor input = (IDiagramDiffAccessor)left;

				Diff diff = input.getDiff(); // equivalent to getInput().getTarget()

				if (!isInTerminalState(diff) && diff != fCurrentSelectedDiff) {
					fDecoratorsManager.revealDecorators(diff);
				}

				fCurrentSelectedDiff = diff;
			} else {
				fCurrentSelectedDiff = null;
			}
		}

		updateToolItems();

		if (left != null && right != null) {
			addZoomListener(getAncestorMergeViewer(), this);
			addZoomListener(getLeftMergeViewer(), this);
			addZoomListener(getRightMergeViewer(), this);
		}
		if (left != null && zoomItem != null) {
			zoomItem.setZoomManager(getZoomManager(getLeftMergeViewer()));
		}
	}

	/**
	 * Adds the specified <code>zoomListener</code> to the zoom manager of the specified <code>viewer</code>.
	 * 
	 * @param viewer
	 *            The viewer to which the zoom listener should be added.
	 * @param zoomListener
	 *            The zoom listener to be added.
	 */
	private void addZoomListener(DiagramMergeViewer viewer, ZoomListener zoomListener) {
		final ZoomManager zoomManager = getZoomManager(viewer);
		if (zoomManager != null) {
			zoomManager.addZoomListener(zoomListener);
		}
	}

	/**
	 * Sets the specified <code>zoom</code> level to the zoom manager of the specified <code>viewer</code>.
	 * 
	 * @param viewer
	 *            The viewer in which the zoom level should be set.
	 * @param zoom
	 *            The zoom level to set.
	 */
	private void setZoom(DiagramMergeViewer viewer, double zoom) {
		final ZoomManager zoomManager = getZoomManager(viewer);
		if (zoomManager != null) {
			zoomManager.setZoom(zoom);
		}
	}

	/**
	 * Obtains the zoom manager from the specified diagram merge <code>viewer</code>.
	 * 
	 * @param viewer
	 *            The viewer to get the zoom manager from.
	 * @return The zoom manager or <code>null</code> if it couldn't be obtained.
	 */
	private ZoomManager getZoomManager(DiagramMergeViewer viewer) {
		final RootEditPart rootEditPart = viewer.getGraphicalViewer().getRootEditPart();
		if (rootEditPart instanceof DiagramRootEditPart) {
			return ((DiagramRootEditPart)rootEditPart).getZoomManager();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		super.createToolItems(toolBarManager);
		IPartService partService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService();
		zoomItem = new ZoomComboContributionItem(partService);
		toolBarManager.insert(0, zoomItem);
	}

	/**
	 * {@inheritDoc} When the zoom on any of the sides changes, the same zoom level is applied to other sides.
	 * 
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	@Override
	public void zoomChanged(double zoom) {
		if (isSynchronizingZoom) {
			return;
		}
		isSynchronizingZoom = true;
		try {
			setZoom(getAncestorMergeViewer(), zoom);
			setZoom(getLeftMergeViewer(), zoom);
			setZoom(getRightMergeViewer(), zoom);
		} finally {
			isSynchronizingZoom = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);

		// Delete decorators at each change of the input models (after merging or CTRL-Z, CTRL-Y)
		Object source = event.getSource();
		if (source instanceof CommandStack && fDecoratorsManager != null) {
			Command command = ((CommandStack)source).getMostRecentCommand();
			if (command instanceof CopyCommand) {
				Iterator<DiagramDiff> diffs = Iterators.filter(command.getAffectedObjects().iterator(),
						DiagramDiff.class);
				if (diffs.hasNext()) {
					// force the computation for the next decorator reveal.
					fDecoratorsManager.hideAll();
					fDecoratorsManager.removeAll();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getDiffFrom(org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer)
	 */
	@Override
	protected Diff getDiffFrom(IMergeViewer viewer) {
		return fCurrentSelectedDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite composite) {
		super.createControls(composite);
		getAncestorMergeViewer().removeSelectionChangedListener(this);
		getLeftMergeViewer().removeSelectionChangedListener(this);
		getRightMergeViewer().removeSelectionChangedListener(this);
	}

}
