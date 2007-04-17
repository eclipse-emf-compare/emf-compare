/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.contentmergeviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.internal.BufferedCanvas;
import org.eclipse.compare.internal.MergeSourceViewer;
import org.eclipse.compare.internal.MergeViewerAction;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffGroup;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.UpdateModelElement;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.MatchElement;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.UnMatchElement;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.merge.api.MergeFactory;
import org.eclipse.emf.compare.merge.service.MergeService;
import org.eclipse.emf.compare.ui.EMFContentProvider;
import org.eclipse.emf.compare.ui.legacy.DiffConstants;
import org.eclipse.emf.compare.ui.legacy.ModelCompareInput;
import org.eclipse.emf.compare.ui.legacy.contentmergeviewer.properties.ModelContentMergePropertiesPart;
import org.eclipse.emf.compare.ui.legacy.contentmergeviewer.tree.ModelContentMergeTreePart;
import org.eclipse.emf.compare.ui.legacy.org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class ModelContentMergeViewer extends ContentMergeViewer {
	public static final String UNDO_ID = "undo"; //$NON-NLS-1$

	public static final String REDO_ID = "redo"; //$NON-NLS-1$

	public static final String CUT_ID = "cut"; //$NON-NLS-1$

	public static final String COPY_ID = "copy"; //$NON-NLS-1$

	public static final String PASTE_ID = "paste"; //$NON-NLS-1$

	public static final String DELETE_ID = "delete"; //$NON-NLS-1$

	public static final String SELECT_ALL_ID = "selectAll"; //$NON-NLS-1$

	public static final String SAVE_ID = "save"; //$NON-NLS-1$

	/**
	 * Size for the resolver
	 */
	private static final String BUNDLE_NAME = "org.eclipse.emf.compare.ui.legacy.contentmergeviewer.UMLMergeResources";

	private int selectedTab = TREE_TAB;

	private static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	private static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	private static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	private static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	public static final int TREE_TAB = 0;

	public static final int PROPERTIES_TAB = 1;

	// Colors
	private RGB fBackground;

	private RGB SELECTED_INCOMING;

	private RGB INCOMING;

	private RGB INCOMING_FILL;

	private RGB SELECTED_CONFLICT;

	private RGB CONFLICT;

	private RGB CONFLICT_FILL;

	private RGB SELECTED_OUTGOING;

	private RGB OUTGOING;

	private RGB OUTGOING_FILL;

	private RGB RESOLVED;

	private boolean fIsMotif;

	private boolean fIsCarbon;

	private final boolean isThreeWay = false;

	private double[] fBasicCenterCurve;

	private boolean showOnlyDiffs = true;

	private final int fPts[] = new int[8]; // scratch area for polygon drawing

	private HashMap<RGB, Color> fColors;

	private MatchElement currentMatch;

	private boolean leftIsLocal;

	private ModelContentMergeViewerPart leftPart;

	private ModelContentMergeViewerPart ancestorPart;

	private ModelContentMergeViewerPart rightPart;

	private DiffElement currentDiff;

	private boolean showPseudoConflicts = false;

	/**
	 * @param parent
	 */
	public ModelContentMergeViewer(final Composite parent, final CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);

		this.fIsMotif = "motif".equals(SWT.getPlatform()); //$NON-NLS-1$
		this.fIsCarbon = "carbon".equals(SWT.getPlatform()); //$NON-NLS-1$
		this.leftIsLocal = (!(config == null) && (config
				.getProperty("LEFT_IS_LOCAL") != null)) ? ((Boolean) config.getProperty("LEFT_IS_LOCAL")).booleanValue() : false; // FIXME

		buildControl(parent);

	}

	public String getTitle() { // FIXME overriding this method is not enough to
		// make it work allright.
		return "Visualization of structural differences";
	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(final boolean leftToRight) {
		setRightDirty(false);
		setLeftDirty(false);
		((ModelCompareInput) getInput()).copy(leftToRight);

		if (leftToRight) {
			setRightDirty(true);
		} else {
			setLeftDirty(true);
		}

	}

	private boolean shouldShow(final Match2Elements match) {
		final DiffElement diff = ((ModelCompareInput) getInput())
				.findDiffFromMatch(match);
		return diff != null;
	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(final Composite composite) {
		this.leftPart = new ModelContentMergeViewerPart(composite,
				DiffConstants.LEFT);

		this.leftPart.addAction(MergeSourceViewer.SAVE_ID, this.fLeftSaveAction);
		// pane.setContent(leftPart.tabFolder);
		this.ancestorPart = new ModelContentMergeViewerPart(composite,
				DiffConstants.ANCESTOR);
		// pane2.setContent(ancestorPart.tabFolder);
		this.rightPart = new ModelContentMergeViewerPart(composite,
				DiffConstants.RIGHT);
		// pane3.setContent(rightPart.tabFolder);

		this.leftPart.hsynchViewport(this.rightPart, this.ancestorPart); // manage the
		// horizontal bar
		this.rightPart.hsynchViewport(this.leftPart, this.ancestorPart);
		this.ancestorPart.hsynchViewport(this.leftPart, this.rightPart);

		this.leftPart.addCompareEditorPartListener(new ICompareEditorPartListener() {

			public void selectedTabChanged(final int newIndex) {
				ModelContentMergeViewer.this.selectedTab = newIndex;
				ModelContentMergeViewer.this.rightPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
				ModelContentMergeViewer.this.ancestorPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
				updateCenter();

			}

			public void selectionChanged(final SelectionChangedEvent event) {
				fireSelectionChanged(event);
			}

			public void updateCenter() {
				ModelContentMergeViewer.this.updateCenter();
			}

		});
		this.rightPart
				.addCompareEditorPartListener(new ICompareEditorPartListener() {

					public void selectedTabChanged(final int newIndex) {
						ModelContentMergeViewer.this.selectedTab = newIndex;
						ModelContentMergeViewer.this.leftPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
						ModelContentMergeViewer.this.ancestorPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
						updateCenter();

					}

					public void selectionChanged(final SelectionChangedEvent event) {
						fireSelectionChanged(event);

					}

					public void updateCenter() {
						ModelContentMergeViewer.this.updateCenter();

					}

				});
		this.ancestorPart
				.addCompareEditorPartListener(new ICompareEditorPartListener() {

					public void selectedTabChanged(final int newIndex) {
						ModelContentMergeViewer.this.selectedTab = newIndex;
						ModelContentMergeViewer.this.rightPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
						ModelContentMergeViewer.this.leftPart.setSelectedTab(ModelContentMergeViewer.this.selectedTab);
						updateCenter();
					}

					public void selectionChanged(final SelectionChangedEvent event) {
						fireSelectionChanged(event);

					}

					public void updateCenter() {
						ModelContentMergeViewer.this.updateCenter();

					}
				});
		updateColors(composite.getDisplay());
	}

	private void updateCenter() {
		getCenter().redraw();
	}

	public void update() {
		this.ancestorPart.layout();
		this.rightPart.layout();
		this.leftPart.layout();
		updateCenter();
	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(final boolean left) {
		final EObject root = (EObject) (left ? ((IMergeViewerContentProvider) getContentProvider())
				.getLeftContent(getInput())
				: ((IMergeViewerContentProvider) getContentProvider())
						.getRightContent(getInput()));
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			root.eResource().save(stream, null);
			return stream.toByteArray();
		} catch (final IOException e) {
			EMFComparePlugin.getDefault().log(e, false);
		}
		return null;
	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#handleResizeAncestor(int,
	 *      int, int, int)
	 */
	@Override
	protected void handleResizeAncestor(final int x, final int y, final int width, final int height) {
		this.ancestorPart.setBounds(x, y, width, height);

	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#handleResizeLeftRight(int,
	 *      int, int, int, int, int)
	 */
	@Override
	protected void handleResizeLeftRight(final int x, final int y, final int leftWidth,
			final int centerWidth, final int rightWidth, final int height) {
		this.leftPart.setBounds(x, y, leftWidth, height);
		this.rightPart.setBounds(x + leftWidth + centerWidth, y, rightWidth, height);
	}

	/**
	 * @see org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(final Object ancestor, final Object left, final Object right) {
		if (getInput() == null) {
			return;
		}
		// TODOCBR handle 3way diff
		// isThreeWay = ((MatchElement) ((UMLCompareInput)
		// getInput()).getDelta()).getAncestorElt() != null;
		// if (isThreeWay)
		// {
		// ancestorPart.setInput(((UMLCompareInput) getInput()).getDelta());
		// }
		if (left != null) {
			this.leftPart.setInput(left);
		}
		// leftPart
		// .setInput(((Match2Elements) ((ModelCompareInput) getInput())
		// .getDelta().getMatchedElements().get(0)));
		if (right != null) {
			this.rightPart.setInput(right);
		}
		// rightPart
		// .setInput(((Match2Elements) ((ModelCompareInput) getInput())
		// .getDelta().getMatchedElements().get(0))
		// );
		this.currentMatch = ((MatchElement) ((ModelCompareInput) getInput())
				.getDelta().getMatchedElements().get(0));
		updateCenter();
	}

	/* package */class PropertiesPartContentProvider implements
			IStructuredContentProvider {

		public PropertiesPartContentProvider() {

		}

		private int side;

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// Nothing to do...
		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (newInput == null) {
				return;
			}
			final ModelContentMergePropertiesPart table = (ModelContentMergePropertiesPart) viewer;
			table.getTable().clearAll();
			this.side = table.getSide();
		}

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(final Object inputElement) {
			if (inputElement == null) {
				return null;
			}

			if (inputElement instanceof Match2Elements) {

				final Match2Elements delta = (Match2Elements) inputElement;
				EObject input = null;
				switch (this.side) {
				case DiffConstants.LEFT:
					input = delta.getLeftElement();
					break;
				case DiffConstants.RIGHT:
					input = delta.getRightElement();
					break;
				case DiffConstants.ANCESTOR:
					// input = delta.getAncestorElt();
					// TODOCBR 3way diff
					break;
				}
				if (input == null) {
					return new Object[0];
				}
				final List<Object> elements = new ArrayList<Object>();
				for (final Object attObject : input.eClass().getEAllAttributes()) {
					final EAttribute att = (EAttribute) attObject;
					final List row = new ArrayList();
					row.add(att);
					row.add(input.eGet(att));

					elements.add(row);
				}

				final Object[] rows = elements.toArray();
				Arrays.sort(rows, new Comparator<Object>() {

					public int compare(final Object o1, final Object o2) {

						final String name1 = ((EAttribute) ((List) o1).get(0))
								.getName();
						final String name2 = ((EAttribute) ((List) o2).get(0))
								.getName();
						if (name1.equals(name2)) {
							return 0;
						}
						for (int i = 0; i < Math.min(name1.length(), name2
								.length()); i++) {
							if (name1.codePointAt(i) > name2.codePointAt(i)) {
								return 1;
							}
							if (name1.codePointAt(i) < name2.codePointAt(i)) {
								return -1;
							}
						}
						if (name1.length() > name2.length()) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				return rows;
			}

			return null;
		}

	}

	/* package */class TreePartContentProvider implements ITreeContentProvider {
		private int side;

		private EMFContentProvider genericContentProvider;

		public TreePartContentProvider(final int side) {
			this.side = side;
			this.genericContentProvider = new EMFContentProvider();
		}

		private static final String NULL_ROOT = "(null)";

		private Match2Elements root;

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(final Object parentElement) {
			if (NULL_ROOT.equals(parentElement)) {
				return null;
			}
			if (parentElement instanceof Match2Elements) {
				if (((Match2Elements) parentElement).getSubMatchElements()
						.isEmpty()) {
					final Match2Elements delta = ((Match2Elements) parentElement);

					switch (this.side) {
					case DiffConstants.LEFT:
						return delta.getLeftElement().eContents().toArray();
					case DiffConstants.RIGHT:
						return delta.getRightElement().eContents().toArray();
						// case DiffConstants.ANCESTOR :
						// return delta.getAncestorElt().eContents().toArray();
						// TODOCBR handle 3way diff
					default:
						throw new IllegalStateException("Invalid side value");
					}

				}
				return ((Match2Elements) parentElement).getSubMatchElements()
						.toArray();
			}
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(final Object element) {
			if (NULL_ROOT.equals(element)) {
				return null;
			}
			if (element instanceof Match2Elements) {
				return ((Match2Elements) element).eContainer();
			}
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(final Object element) {
			if (element instanceof Match2Elements) {
				boolean hasChildren = false;
				switch (this.side) {
				case DiffConstants.LEFT:
					hasChildren = !((Match2Elements) element).getLeftElement()
							.eContents().isEmpty();
					break;
				case DiffConstants.RIGHT:
					hasChildren = !((Match2Elements) element).getRightElement()
							.eContents().isEmpty();
					break;
				case DiffConstants.ANCESTOR:
					// hasChildren = !((Match2Elements)
					// element).getAncestorElt().eContents().isEmpty();
					// TODOCBR handle 3way diff
					break;
				default:
					throw new IllegalStateException("Invalid side value");
				}
				if (NULL_ROOT.equals(element)) {
					return false;
				}
				return (((Match2Elements) element).getSubMatchElements().size() > 0)
						|| hasChildren;
			}
			return false;
		}

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(final Object inputElement) {
			if (inputElement == null) {
				return null;
			}
			if ((this.root != null) && this.root.equals(inputElement)) {
				return getChildren(inputElement);
			}
			this.root = (Match2Elements) inputElement;

			final Object[] obj = new Object[1];
			obj[0] = this.root;
			return obj;
		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			this.root = null;
			((TreeViewer) viewer).getTree().clearAll(true);

		}
	}

	/*
	 * Creates the central Canvas. Called from ContentMergeViewer.
	 */
	protected Control createCenter(final Composite parent) {
		final Canvas canvas = new BufferedCanvas(parent, SWT.NONE) {
			public void doPaint(GC gc) {
				paintCenter(this, gc);
			}
		};

		return canvas;

	}

	private void paintCenter(final Canvas canvas, final GC g) {
		if (getInput() == null) {
			return;
		}
		final Display display = canvas.getDisplay();

		final int visibleHeight = this.leftPart.getVisibleHeight();

		final Point size = canvas.getSize();
		final int x = 0;
		final int w = size.x;

		switch (this.selectedTab) {
		case TREE_TAB:

			g.setBackground(canvas.getBackground());
			g.fillRectangle(x + 1, 0, w - 2, size.y);

			if (!this.fIsMotif) {
				// draw thin line between center ruler and both texts
				g.setBackground(display
						.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				g.fillRectangle(0, 0, 1, size.y);
				g.fillRectangle(w - 1, 0, 1, size.y);
			}

			final List<Object> lVisibleElts = this.leftPart.getVisibleElements();
			final List<Object> rVisibleElts = this.rightPart.getVisibleElements();

			TreeItem lTreePrevious = null;
			TreeItem rTreePrevious = null;

			final Match2Elements rootMatch = (Match2Elements) ((ModelCompareInput) getInput())
					.getDelta().getMatchedElements().get(0);
			final TreeIterator iter = rootMatch.eAllContents();

			while (iter.hasNext()) {
				final Match2Elements match = (Match2Elements) iter.next();

				if (shouldShow(match)) {
					if (!((match).getSubMatchElements().size() > 0)) // ||(!showPseudoConflicts
					// &&
					// (delta.getKind()
					// >=
					// DiffConstants.PSEUDO_CONFLICT)))
					// FIXMECBR
					{
						lTreePrevious = (TreeItem) this.leftPart.find(match
								.getLeftElement());
						rTreePrevious = (TreeItem) this.rightPart.find(match
								.getRightElement());
						continue;
					}

					final TreeItem leftItem = (TreeItem) this.leftPart.find(match
							.getLeftElement());
					final TreeItem rightItem = (TreeItem) this.rightPart.find(match
							.getRightElement());
					if ((!lVisibleElts.contains(leftItem))
							&& (!rVisibleElts.contains(rightItem))) {
						continue;
					}
					if ((leftItem == null) && (rightItem == null)) {
						continue;
					}
					int ly = 0;
					int lh = 0;
					if (leftItem == null) {
						if (lTreePrevious == null) {
							ly = rightItem.getBounds().height;
							lh = 0;
						} else {

							ly = lTreePrevious.getBounds().y
									+ lTreePrevious.getBounds().height;
							lh = 0;
							if (ly == 1) // the previous item was not visible
							{
								final TreeItem parent = (TreeItem) this.leftPart
										.getVisibleParentElement(lTreePrevious);

								ly = parent.getBounds().y
										+ parent.getBounds().height;
								lh = 0;
							}
						}
					} else {
						if (lVisibleElts.contains(leftItem)) {
							int heightFactor = 1;
							if (leftItem.getExpanded()) {
								// case in which children elements were added to
								// new
								// elements
							}
							ly = leftItem.getBounds().y;
							lh = leftItem.getBounds().height * heightFactor;
							lTreePrevious = leftItem;
						} else {
							final TreeItem parent = (TreeItem) this.leftPart
									.getVisibleParentElement(leftItem);

							ly = parent.getBounds().y
									+ parent.getBounds().height;

						}
					}

					int ry = 0;
					int rh = 0;
					if (rightItem == null) {
						if (rTreePrevious == null) {
							ry = leftItem.getBounds().height;
							rh = 0;
						} else {
							ry = rTreePrevious.getBounds().y
									+ rTreePrevious.getBounds().height;
							rh = 0;
							if (ry == 1) // the previous item was not visible
							{
								final TreeItem parent = (TreeItem) this.rightPart
										.getVisibleParentElement(rTreePrevious);

								ry = parent.getBounds().y
										+ parent.getBounds().height;

							}
						}
					} else {
						if (rVisibleElts.contains(rightItem)) {
							int heightFactor = 1;
							if (rightItem.getExpanded()) {
								// case in which children elements were added to
								// new
								// elements
							}
							ry = rightItem.getBounds().y;
							rh = rightItem.getBounds().height * heightFactor;
							rTreePrevious = rightItem;
						} else {
							final TreeItem parent = (TreeItem) this.rightPart
									.getVisibleParentElement(rightItem);

							ry = parent.getBounds().y
									+ parent.getBounds().height;
							rh = 0;
						}
					}
					if (Math.max(ly + lh, ry + rh) < 0) {
						continue;
					}
					if (Math.min(ly, ry) >= visibleHeight) {
						break;
					}

					this.fPts[0] = x;
					this.fPts[1] = ly;
					this.fPts[2] = w;
					this.fPts[3] = ry;
					this.fPts[6] = x;
					this.fPts[7] = ly + lh;
					this.fPts[4] = w;
					this.fPts[5] = ry + rh;

					final int w2 = 3;
					final int yshift = 4; // due to encapsulation in TabFolder

					final Color fillColor = getColor(display, getFillColor(match));
					final Color strokeColor = getColor(display, getStrokeColor(match));

					g.setBackground(fillColor);

					g.fillRectangle(-1, ly - 1 + yshift, w2, lh); // left
					g.fillRectangle(w - w2, ry - 1 + yshift, w2, rh); // right

					g.setLineWidth(1);
					g.setForeground(strokeColor);
					g.setLineStyle(SWT.LINE_DASH);

					g.drawRoundRectangle(-1, ly - 1 + yshift, w2, lh, 10, 10); // left
					g.drawRoundRectangle(w - w2, ry - 1 + yshift, w2, rh, 10,
							10); // right

					final int[] points = getCenterCurvePoints(w2, ly + yshift + lh
							/ 2, w - w2, ry + yshift + rh / 2);
					for (int i = 1; i < points.length; i++) {
						g.setLineStyle(SWT.LINE_DASH);
						g
								.drawLine(w2 + i - 1, points[i - 1], w2 + i,
										points[i]);
					}
				}
			}
			break;
		case PROPERTIES_TAB:
			// if (currentDelta == null)
			// return;
			// g.setBackground(canvas.getBackground());
			// g.fillRectangle(x+1, 0, w-2, size.y);
			//
			// if (!fIsMotif) {
			// // draw thin line between center ruler and both texts
			// g.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
			// g.fillRectangle(0, 0, 1, size.y);
			// g.fillRectangle(w-1, 0, 1, size.y);
			// }
			//
			// TableItem lPrevious = null;
			// TableItem rPrevious = null;
			// for ( diff : currentDelta.getSubMatchElements())
			// {
			// if (diff.getAttribute() == null)
			// continue;
			//
			// if (!showPseudoConflicts && (diff.getKind() >=
			// DiffConstants.PSEUDO_CONFLICT))
			// {
			// lPrevious = (TableItem) leftPart.find(diff);
			// rPrevious = (TableItem) rightPart.find(diff);
			// }
			// int ly = 0;
			// int lh = 0;
			// TableItem lItem = null;
			//
			// lItem = (TableItem) leftPart.find(diff);
			// if (lItem == null)
			// {
			// if (lPrevious == null)
			// {
			// ly = 1;
			// lh = 0;
			// }
			// else
			// {
			// ly = lPrevious.getBounds().y + lPrevious.getBounds().height;
			// lh= 0;
			// }
			// }
			// else
			// {
			// ly= lItem.getBounds().y;
			// lh= lItem.getBounds().height;
			// }
			//
			// int ry = 0;
			// int rh = 0;
			// TableItem rItem = null;
			//
			// rItem = (TableItem) rightPart.find(diff);
			// if (rItem == null)
			// {
			// if (rPrevious == null)
			// {
			// ry = 1;
			// rh = 0;
			// }
			// else
			// {
			// ry = rPrevious.getBounds().y + rPrevious.getBounds().height;
			// rh= 0;
			// }
			// }
			// else
			// {
			// ry= rItem.getBounds().y;
			// rh= rItem.getBounds().height;
			// }
			//
			// lPrevious = lItem;
			// rPrevious = rItem;
			// if (Math.max(ly+lh, ry+rh) < 0)
			// continue;
			// if (Math.min(ly, ry) >= visibleHeight)
			// break;
			//
			// fPts[0]= x; fPts[1]= ly; fPts[2]= w; fPts[3]= ry;
			// fPts[6]= x; fPts[7]= ly+lh; fPts[4]= w; fPts[5]= ry+rh;
			//
			// int w2 = 3;
			// int yshift = 4; // due to encapsulation in TabFolder
			// yshift += leftPart.properties.getTable().getHeaderHeight(); //
			// header height
			// Color fillColor= getColor(display, getFillColor(diff));
			// Color strokeColor= getColor(display, getStrokeColor(diff));
			//
			// g.setBackground(fillColor);
			//
			// g.fillRectangle(-1, ly - 1 + yshift, w2, lh); // left
			// g.fillRectangle(w-w2, ry - 1 + yshift, w2, rh); // right
			//
			// g.setLineWidth(1);
			// g.setForeground(strokeColor);
			// g.setLineStyle(SWT.LINE_DASH);
			//
			// g.drawRoundRectangle(-1, ly - 1 + yshift, w2, lh,10,10); // left
			// g.drawRoundRectangle(w-w2, ry - 1 + yshift, w2, rh,10,10); //
			// right
			//
			// int[] points= getCenterCurvePoints(w2, ly + yshift +lh/2, w-w2,
			// ry+ yshift + rh/2);
			// for (int i= 1; i < points.length; i++)
			// {
			// g.setLineStyle(SWT.LINE_DASH);
			// g.drawLine(w2+i-1, points[i-1], w2+i, points[i]);
			// }
			//
			// }
			// FIXMECBR handle property tabs
			break;
		default:
			throw new IllegalStateException("Invalid value for selected tab");
		}
	}

	private int heightFactor(int heightFactor, final TreeItem parentItem) {
		for (final TreeItem item : parentItem.getItems()) {
			heightFactor++;
			if (item.getExpanded()) {

				// heightFactor = heightFactor(heightFactor, item);
			}
		}
		return heightFactor;
	}

	private int[] getCenterCurvePoints(final int startx, final int starty, final int endx,
			final int endy) {
		if (this.fBasicCenterCurve == null) {
			buildBaseCenterCurve(endx - startx);
		}
		double height = endy - starty;
		height = height / 2;
		final int width = endx - startx;
		final int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int) (-height * this.fBasicCenterCurve[i] + height + starty);
		}
		return points;
	}

	private void buildBaseCenterCurve(final int w) {
		final double width = w;
		this.fBasicCenterCurve = new double[getCenterWidth()];
		for (int i = 0; i < getCenterWidth(); i++) {
			final double r = i / width;
			this.fBasicCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}

	/**
	 * @param selectedDelta
	 */
	public void navigateToDelta(final Match2Elements selectedDelta) {
		this.leftPart.navigateToDelta(selectedDelta);
		this.rightPart.navigateToDelta(selectedDelta);
		if (this.isThreeWay) {
			this.ancestorPart.navigateToDelta(selectedDelta);
		}
		this.currentMatch = selectedDelta;
		updateCenter();
	}

	/** Width of center bar */
	private static final int CENTER_WIDTH = 34;

	protected int getCenterWidth() {
		return CENTER_WIDTH;
	}

	private void updateColors(Display display) {

		if (display == null) {
			display = this.fComposite.getDisplay();
		}

		Color color = null;
		if (this.fBackground != null) {
			color = getColor(display, this.fBackground);
		}

		if (this.ancestorPart != null) {
			this.ancestorPart.setBackground(color);
		}
		if (this.leftPart != null) {
			this.leftPart.setBackground(color);
		}
		if (this.rightPart != null) {
			this.rightPart.setBackground(color);
		}

		final ColorRegistry registry = JFaceResources.getColorRegistry();

		final RGB bg = getBackground(display);
		this.SELECTED_INCOMING = registry.getRGB(INCOMING_COLOR);
		if (this.SELECTED_INCOMING == null) {
			this.SELECTED_INCOMING = new RGB(0, 0, 255); // BLUE
		}
		this.INCOMING = interpolate(this.SELECTED_INCOMING, bg, 0.6);
		this.INCOMING_FILL = interpolate(this.SELECTED_INCOMING, bg, 0.97);

		this.SELECTED_OUTGOING = registry.getRGB(OUTGOING_COLOR);
		if (this.SELECTED_OUTGOING == null) {
			this.SELECTED_OUTGOING = new RGB(0, 0, 0); // BLACK
		}
		this.OUTGOING = interpolate(this.SELECTED_OUTGOING, bg, 0.6);
		this.OUTGOING_FILL = interpolate(this.SELECTED_OUTGOING, bg, 0.97);

		this.SELECTED_CONFLICT = registry.getRGB(CONFLICTING_COLOR);
		if (this.SELECTED_CONFLICT == null) {
			this.SELECTED_CONFLICT = new RGB(255, 0, 0); // RED
		}
		this.CONFLICT = interpolate(this.SELECTED_CONFLICT, bg, 0.6);
		this.CONFLICT_FILL = interpolate(this.SELECTED_CONFLICT, bg, 0.97);

		this.RESOLVED = registry.getRGB(RESOLVED_COLOR);
		if (this.RESOLVED == null) {
			this.RESOLVED = new RGB(0, 255, 0); // GREEN
		}

		getCenter().redraw();

	}

	static RGB interpolate(final RGB fg, final RGB bg, final double scale) {
		if ((fg != null) && (bg != null)) {
			return new RGB((int) ((1.0 - scale) * fg.red + scale * bg.red),
					(int) ((1.0 - scale) * fg.green + scale * bg.green),
					(int) ((1.0 - scale) * fg.blue + scale * bg.blue));
		}
		if (fg != null) {
			return fg;
		}
		if (bg != null) {
			return bg;
		}
		return new RGB(128, 128, 128); // a gray
	}

	public void setSelectedPage(final int index) {
		if (this.selectedTab == index) {
			return;
		}
		this.selectedTab = index;
		this.leftPart.setSelectedTab(index);
		this.rightPart.setSelectedTab(index);
		if (this.isThreeWay) {
			this.ancestorPart.setSelectedTab(index);
		}
	}

	private RGB getBackground(Display display) {

		if (display == null) {
			display = this.fComposite.getDisplay();
		}
		return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
	}

	private class ModelContentMergeViewerPart {
		private Map<String, Action> fActions;

		public void addAction(final String actionId, final Action action) {
			this.fActions.put(actionId, action);
		}

		public Action getAction(final String actionId) {
			Action action = this.fActions.get(actionId);
			if (action == null) {
				action = createAction(actionId);
				if (action == null) {
					return null;
				}

				/*
				 * if (action.isContentDependent()) addTextListener(this); if
				 * (action.isSelectionDependent())
				 * addSelectionChangedListener(this);
				 */// TODO adapt
				Utilities.initAction(action, getResourceBundle(),
						"action." + actionId + "."); //$NON-NLS-1$ //$NON-NLS-2$
				this.fActions.put(actionId, action);
			}
			if (((MergeViewerAction) action).isEditableDependent()
					&& !isEditable()) {
				return null;
			}
			return action;
		}

		private boolean isEditable() {
			switch (this.side) {
			case DiffConstants.LEFT:
				return ((IMergeViewerContentProvider) getContentProvider())
						.isLeftEditable(getInput());
			case DiffConstants.RIGHT:
				return ((IMergeViewerContentProvider) getContentProvider())
						.isRightEditable(getInput());
			case DiffConstants.ANCESTOR:
				return false;
			default:
				throw new IllegalStateException("bad value for side");
			}
		}

		protected MergeViewerAction createAction(final String actionId) {
			/*
			 * if (UNDO_ID.equals(actionId)) return new
			 * TextOperationAction(UNDO, true, false, true); if
			 * (REDO_ID.equals(actionId)) return new TextOperationAction(REDO,
			 * true, false, true); if (CUT_ID.equals(actionId)) return new
			 * TextOperationAction(CUT, true, true, false); if
			 * (COPY_ID.equals(actionId)) return new TextOperationAction(COPY,
			 * false, true, false); if (PASTE_ID.equals(actionId)) return new
			 * TextOperationAction(PASTE, true, false, false); if
			 * (DELETE_ID.equals(actionId)) return new
			 * TextOperationAction(DELETE, true, false, false); if
			 * (SELECT_ALL_ID.equals(actionId)) return new
			 * TextOperationAction(SELECT_ALL, false, false, false);
			 */// TODO
			// adapt
			return null;
		}

		public ModelContentMergeViewerPart(final Composite composite, final int side) {
			this.side = side;
			this.fActions = new HashMap<String, Action>();
			createContents(composite);
			this.addCompareEditorPartListener(new ICompareEditorPartListener() {

				public void selectedTabChanged(final int newIndex) {
					// added to prevent strange resize reaction
					resizeBounds();

				}

				public void selectionChanged(final SelectionChangedEvent event) {
					// null implementation
				}

				public void updateCenter() {
					// null implementation

				}
			});
		}

		private void resizeBounds() {
			switch (this.selectedTab) {
			case TREE_TAB:
				this.tree.getTree().setBounds(this.tabFolder.getClientArea());
				break;
			case PROPERTIES_TAB:
				this.properties.getTable().setBounds(this.tabFolder.getClientArea());
				break;
			}
		}

		/**
		 * @param leftItem
		 * @return
		 */
		public Widget getVisibleParentElement(final Widget item) {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tree.getVisibleParentElement((TreeItem) item);
			case PROPERTIES_TAB:
				return null;
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @param delta
		 * @return
		 */
		public Widget find(final Object object) {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tree.find(object);
			case PROPERTIES_TAB:
				if (object instanceof DiffElement) {
					return this.properties.find((DiffElement) object);
				}
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @return
		 */
		public int getVisibleHeight() {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tabFolder.getClientArea().height
						- this.tabFolder.getTabHeight();
			case PROPERTIES_TAB:
				return this.properties.getTable().getClientArea().height
						- this.properties.getTable().getHeaderHeight();
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @return
		 */
		public List<Object> getVisibleElements() {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tree.getVisibleElements();
			case PROPERTIES_TAB:
				return null;
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @return
		 */
		public int getMaxHeight() {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tree.getTree().getVerticalBar().getMaximum();
			case PROPERTIES_TAB:
				return 0; // TODO
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @return
		 */
		public int getLimitedSelection() {
			switch (this.selectedTab) {
			case TREE_TAB:
				return this.tree.getTree().getVerticalBar().getSelection();
			case PROPERTIES_TAB:
				return 0;
			default:
				throw new IllegalStateException(
						"Invalid value for tab selection");
			}
		}

		/**
		 * @param rightPart
		 * @param ancestorPart
		 */
		public void hsynchViewport(final ModelContentMergeViewerPart part1,
				final ModelContentMergeViewerPart part2) {
			final Tree st1 = this.tree.getTree();
			final Tree st2 = part1.tree.getTree();
			final Tree st3 = part2.tree.getTree();
			final ScrollBar sb1 = st1.getHorizontalBar();
			sb1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					final int max = sb1.getMaximum() - sb1.getThumb();
					double v = 0.0;
					if (max > 0) {
						v = (float) sb1.getSelection() / (float) max;
					}
					if (st2.isVisible()) {
						final ScrollBar sb2 = st2.getHorizontalBar();
						final int newPosition = (int) ((sb2.getMaximum() - sb2
								.getThumb()) * v);
						sb2.setSelection(newPosition < 0 ? 0 : newPosition);
					}
					if (st3.isVisible()) {
						final ScrollBar sb3 = st3.getHorizontalBar();
						final int newPosition = (int) ((sb3.getMaximum() - sb3
								.getThumb()) * v);
						sb3.setSelection(newPosition < 0 ? 0 : newPosition);
					}
					if ("carbon".equals(SWT.getPlatform())
							&& (ModelContentMergeViewer.this.fComposite != null) && !ModelContentMergeViewer.this.fComposite.isDisposed()) {
						ModelContentMergeViewer.this.fComposite.getDisplay().update();
					}
				}
			});
			final Table tb1 = this.properties.getTable();
			final Table tb2 = part1.properties.getTable();
			final Table tb3 = part2.properties.getTable();
			final ScrollBar tsb1 = tb1.getHorizontalBar();
			tsb1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					final int max = tsb1.getMaximum() - tsb1.getThumb();
					double v = 0.0;
					if (max > 0) {
						v = (float) tsb1.getSelection() / (float) max;
					}
					if (tb2.isVisible()) {
						final ScrollBar sb2 = tb2.getHorizontalBar();
						final int newPosition = (int) ((sb2.getMaximum() - sb2
								.getThumb()) * v);
						sb2.setSelection(newPosition < 0 ? 0 : newPosition);
					}
					if (tb3.isVisible()) {
						final ScrollBar sb3 = tb3.getHorizontalBar();
						final int newPosition = (int) ((sb3.getMaximum() - sb3
								.getThumb()) * v);
						sb3.setSelection(newPosition < 0 ? 0 : newPosition);
					}
					if ("carbon".equals(SWT.getPlatform())
							&& (ModelContentMergeViewer.this.fComposite != null) && !ModelContentMergeViewer.this.fComposite.isDisposed()) {
						ModelContentMergeViewer.this.fComposite.getDisplay().update();
					}
				}
			});

		}

		/**
		 * @param input
		 */
		public void setInput(final Object input) {
			switch (this.selectedTab) {
			case TREE_TAB:
				this.tree.setInput(((EObject) input).eResource());
				break;
			case PROPERTIES_TAB:
				this.properties.setInput(input);
				break;
			default:
				throw new IllegalStateException("Unknow tab selection index");
			}

		}

		/**
		 * @param i
		 * @param j
		 * @param k
		 * @param l
		 * @return
		 */
		public Rectangle computeTrim(final int i, final int j, final int k, final int l) {
			return this.tabFolder.computeTrim(i, j, k, l);
		}

		/**
		 * @param x
		 * @param y
		 * @param leftWidth
		 * @param height
		 */
		public void setBounds(final int x, final int y, final int leftWidth, final int height) {
			this.tabFolder.setBounds(x, y, leftWidth, height);
			resizeBounds();
		}

		/**
		 * @param color
		 */
		public void setBackground(final Color color) {
			this.tree.getTree().setBackground(color);
			this.properties.getTable().setBackground(color);
			// TODO add diagram
		}

		/**
		 * @param index
		 */
		public void setSelectedTab(final int index) {
			this.selectedTab = index;
			this.tabFolder.setSelection(this.selectedTab);
			resizeBounds();
		}

		/**
		 * @param selectedDelta
		 */
		public void navigateToDelta(final Match2Elements selectedDelta) {
			switch (this.selectedTab) {
			case TREE_TAB:
				this.tree.showItem(selectedDelta);
				this.properties.setInput(selectedDelta);
				break;
			case PROPERTIES_TAB:
				this.properties.setInput(selectedDelta);
				break;
			}

		}

		private int selectedTab = TREE_TAB;

		private CTabFolder tabFolder;

		private ModelContentMergeTreePart tree;

		private ModelContentMergePropertiesPart properties;

		private int side;

		public Control createContents(final Composite composite) {
			this.tabFolder = new CTabFolder(composite, SWT.BOTTOM);
			final CTabItem treeTab = new CTabItem(this.tabFolder, SWT.NONE);
			treeTab.setText("Tree");

			final CTabItem propertiesTab = new CTabItem(this.tabFolder, SWT.NONE);
			propertiesTab.setText("Properties");


			Composite panel = new Composite(this.tabFolder, SWT.NONE);
			panel.setLayout(new GridLayout());
			panel.setLayoutData(new GridData(GridData.FILL_BOTH));
			panel.setFont(composite.getFont());
			this.tree = createTree(panel, this.side);
			treeTab.setControl(panel);

			panel = new Composite(this.tabFolder, SWT.NONE);
			panel.setLayout(new GridLayout());
			panel.setLayoutData(new GridData(GridData.FILL_BOTH));
			panel.setFont(composite.getFont());
			this.properties = createProperties(panel, this.side);
			propertiesTab.setControl(panel);
			// listener
			this.tabFolder.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(final SelectionEvent e) {
					if (e.item.equals(treeTab)) {
						ModelContentMergeViewerPart.this.selectedTab = TREE_TAB;
					} else {
						if (e.item.equals(propertiesTab)) {
							ModelContentMergeViewerPart.this.selectedTab = PROPERTIES_TAB;
						} 
					}
					fireSelectedtabChanged();
				}

				public void widgetSelected(final SelectionEvent e) {
					if (e.item.equals(treeTab)) {
						ModelContentMergeViewerPart.this.selectedTab = TREE_TAB;
					} else {
						if (e.item.equals(propertiesTab)) {
							ModelContentMergeViewerPart.this.selectedTab = PROPERTIES_TAB;
						} 
					}
					fireSelectedtabChanged();
				}

			});

			this.tabFolder.setSelection(treeTab);

			return this.tabFolder;
		}

		/**
		 * @param bounds
		 */
		public void setBounds(final Rectangle bounds) {
			setBounds(bounds.x, bounds.y, bounds.width, bounds.height);

		}

		private ModelContentMergeTreePart createTree(final Composite composite,
				final int side) {
			final ModelContentMergeTreePart tree = new ModelContentMergeTreePart(
					composite, side);

			// tree.setContentProvider(new TreePartContentProvider(side));
			// TODOCBR check content provider
			tree.setContentProvider(new EMFContentProvider());

			tree.getTree().getVerticalBar().addSelectionListener(
					new SelectionListener() {

						public void widgetDefaultSelected(final SelectionEvent e) {
							fireUpdateCenter();

						}

						public void widgetSelected(final SelectionEvent e) {
							fireUpdateCenter();

						}
					});

			tree.getTree().addTreeListener(new TreeListener() {

				public void treeCollapsed(final TreeEvent e) {
					((TreeItem) e.item).setExpanded(false);
					e.doit = false;
					fireUpdateCenter();

				}

				public void treeExpanded(final TreeEvent e) {
					((TreeItem) e.item).setExpanded(true);

					e.doit = false;
					fireUpdateCenter();
				}

			});
			tree.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(final SelectionChangedEvent event) {
					fireSelectionChanged(event);
				}
			});

			tree.getTree().addPaintListener(new PaintListener() {
				public void paintControl(final PaintEvent e) {
					paint(e);
				}
			});

			return tree;
		}

		private ModelContentMergePropertiesPart createProperties(
				final Composite composite, final int side) {
			final ModelContentMergePropertiesPart prop = new ModelContentMergePropertiesPart(
					composite, SWT.NONE, side);
			prop.setContentProvider(new PropertiesPartContentProvider()); // TODOCBR
			// check
			// propertiespartcontentprovider
			// prop.setContentProvider(new EMFContentProvider());
			// prop.getTable().setLayout(new GridLayout());
			// prop.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
			prop.getTable().getVerticalBar().addSelectionListener(
					new SelectionListener() {

						public void widgetDefaultSelected(final SelectionEvent e) {
							updateCenter();

						}

						public void widgetSelected(final SelectionEvent e) {
							updateCenter();

						}
					});
			prop.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(final SelectionChangedEvent event) {
					fireSelectionChanged(event);
				}
			});
			prop.getTable().addPaintListener(new PaintListener() {
				public void paintControl(final PaintEvent e) {
					paint(e);
				}
			});
			return prop;
		}

		private void paint(final PaintEvent event) {

			switch (this.selectedTab) {
			case TREE_TAB:
				paintTree(event);
				break;
			case PROPERTIES_TAB:
				paintProperties(event);
				break;
			default:
				throw new IllegalStateException("Invalid index for tab");
			}
		}

		private void layout() {
			switch (this.selectedTab) {
			case TREE_TAB:
				this.tree.getTree().redraw();
				break;
			case PROPERTIES_TAB:
				this.properties.getTable().redraw();
				break;
			default:
				throw new IllegalStateException("Invalid index for tab");
			}
		}

		/**
		 * @param event
		 */
		private void paintProperties(final PaintEvent event) {
			final Control canvas = (Control) event.widget;
			final Display display = canvas.getDisplay();

			final int w = canvas.getSize().x;
			if (this.properties.getInput() instanceof Match2Elements) {
				final Match2Elements delta = ((Match2Elements) this.properties.getInput());
				if ((delta.getSubMatchElements().isEmpty())) {
					return;
				}
				// for each attribute we will draw a line
				final Iterator it = delta.getLeftElement().eClass()
						.getEAllAttributes().iterator();
				while (it.hasNext()) {
					final EAttribute diff = (EAttribute) it.next();

					// // if ((diff.getAttribute() == null)
					// // || ((!showPseudoConflicts) && (diff.getKind() >=
					// DiffConstants.PSEUDO_CONFLICT)))
					// // continue;
					// //TODOCBR paint properties
					//
					// int y = 0;
					// int h = 0;
					// TableItem item = null;
					//
					// item = (TableItem) properties.find(diff); //TODOCBR
					// replace null by getAttribute
					// if (item == null) {
					// if (previous == null) {
					// y = 1;
					// h = 0;
					// } else {
					// y = previous.getBounds().y
					// + previous.getBounds().height;
					// h = 0;
					// }
					// } else {
					// y = item.getBounds().y;
					// h = item.getBounds().height;
					// }
					//
					// previous = item;
					// if (y + h < event.y)
					// continue;
					// if (y > maxh)
					// break;
					//
					// g.setLineWidth(1);
					// g.setLineStyle(SWT.LINE_DASH);
					// // g.setBackground(getColor(display,
					// getFillColor(diff)));
					// //
					// // g.setForeground(getColor(canvas.getDisplay(),
					// // getStrokeColor(diff)));
					// // TODOCBR handle colors
					//
					// g.fillRectangle(0, y - 1, w, 1);
					// g.fillRectangle(0, y + h - 1, w, 1);
					// g.drawRoundRectangle(0, y - 1, w, 1, 5, 5);
					// g.drawRoundRectangle(0, y + h - 1, w, 1, 5, 5);

				}
				// TODOCBR handle property drawing
			} else {
				// TODOCBR handle unmatched elements
			}
		}

		/**
		 * 
		 */
		private void paintTree(final PaintEvent event) {
			final Control canvas = (Control) event.widget;
			final GC g = event.gc;
			final Display display = canvas.getDisplay();
			final int w = canvas.getSize().x;
			final int maxh = event.y + event.height; // visibleHeight
			final List<Object> vItems = this.tree.getVisibleElements();
			// FIXMECBR implement paintTree
			final Match2Elements rootMatch = (Match2Elements) ((ModelCompareInput) getInput())
					.getDelta().getMatchedElements().get(0);
			final TreeIterator iter = rootMatch.eAllContents();
			Match2Elements previous = null;
			Match2Elements match = null;
			while (iter.hasNext()) {
				previous = match;
				match = (Match2Elements) iter.next();
				if (shouldShow(match)) {
					//
					final boolean elementIsNull = false;
					// if
					// ((!delta.hasDiffs())||((!showPseudoConflicts)&&(delta.getKind()
					// >= DiffConstants.PSEUDO_CONFLICT)))
					// continue;
					// switch (side) {
					// case DiffConstants.LEFT:
					// elementIsNull = match.getLeftElement() == null;
					// break;
					// case DiffConstants.RIGHT:
					// elementIsNull = match.getRightElement() == null;
					// break;
					// // case DiffConstants.ANCESTOR :
					// // elementIsNull = delta.getAncestorElt() == null;
					// // break;
					// default:
					// throw new IllegalStateException("Invalid side value");
					// }
					// FIXMECBR
					int y = 0;
					int h = 0;
					TreeItem item = null;
					if (elementIsNull) {
						// // will draw a line starting from below the previous
						// delta.
						// // In case no previous delta is available, means root
						// was
						// added.
						// // will then add line starting on the x = 1 line
						//
						if (previous == null) {
							y = 1;
							h = 0;
						} else {
							item = (TreeItem) this.tree.find(previous);
							if (item == null) {
								// hasn't
								// been
								// initialized (must be seen at least once)
								continue;
							}

							if (vItems.contains(item)) {

								y = item.getBounds().y
										+ item.getBounds().height;
								h = 0;
							} else {
								final TreeItem parent = this.tree
										.getVisibleParentElement(item);
								if (parent.getParent() == null) {
									// visible element
									// is the root, there is no point showing
									// the
									// lines.
									continue;
								}
								y = parent.getBounds().y
										+ parent.getBounds().height;
								h = 0;
							}
						}
					} else {
						switch (this.side) {
						case DiffConstants.LEFT:
							item = (TreeItem) this.tree.find(match.getLeftElement());
							break;
						case DiffConstants.RIGHT:
							item = (TreeItem) this.tree
									.find(match.getRightElement());
							break;
						case DiffConstants.ANCESTOR:
							// elementIsNull = delta.getAncestorElt() == null;
							// break;
							// TODOCBR handle 3way diff
						}

						if (item == null) {
							// been
							// initialized (must be seen at least once)
							continue;
						}

						if (vItems.contains(item)) {
							int heightFactor = 1;
							if (item.getExpanded()) {
								// case in which children elements were added to
								// new
								// elements
							}
							y = item.getBounds().y;
							h = item.getBounds().height * heightFactor;
						} else {
							final TreeItem parent = this.tree
									.getVisibleParentElement(item);
							if (parent == null) {
								continue;
							}
							y = parent.getBounds().y
									+ parent.getBounds().height;
							h = 0;
						}
					}

					if (y + h < event.y) {
						continue;
					}
					if (y > maxh) {
						break;
					}

					g.setLineWidth(1);
					g.setLineStyle(SWT.LINE_DASH);
					g.setBackground(getColor(display, getFillColor(match)));
					g.setForeground(getColor(canvas.getDisplay(),
							getStrokeColor(match)));

					if (!vItems.contains(item)) {
						// element is not visible
						if (this.tree.getTree().getItems()[0].getExpanded()) {
							// root is expanded
							g.drawLine(0, y + h - 1, w, y + h - 1);
						}
					} else {
						g.fillRectangle(0, y - 1, w, 1);
						g.fillRectangle(0, y + h - 1, w, 1);
						g.drawRoundRectangle(0, y - 1, w, 1, 5, 5);
						g.drawRoundRectangle(0, y + h - 1, w, 1, 5, 5);
					}
				}
			}
		}

		private final ListenerList listeners = new ListenerList();

		public void addCompareEditorPartListener(
				final ICompareEditorPartListener listener) {
			this.listeners.add(listener);
		}

		public void removePartListener(final ICompareEditorPartListener listener) {
			this.listeners.remove(listener);
		}

		protected void fireSelectedtabChanged() {
			for (final Object listener : this.listeners.getListeners()) {
				if (listener instanceof ICompareEditorPartListener) {
					((ICompareEditorPartListener) listener)
							.selectedTabChanged(this.selectedTab);
				}
			}
		}

		protected void fireSelectionChanged(final SelectionChangedEvent event) {
			for (final Object listener : this.listeners.getListeners()) {
				if (listener instanceof ICompareEditorPartListener) {
					((ICompareEditorPartListener) listener)
							.selectionChanged(event);
				}
			}
		}

		protected void fireUpdateCenter() {
			for (final Object listener : this.listeners.getListeners()) {
				if (listener instanceof ICompareEditorPartListener) {
					((ICompareEditorPartListener) listener).updateCenter();
				}
			}
		}

		private EObject findElementFromDiff(final DiffElement diff) {
			if (diff instanceof AddModelElement) {
				switch (this.side) {
				case DiffConstants.LEFT:
					return ((AddModelElement) diff).getLeftParent();
				case DiffConstants.RIGHT:
					return ((AddModelElement) diff).getRightElement();
				case DiffConstants.ANCESTOR:
					// elementIsNull = delta.getAncestorElt() == null;
					// break;
					// TODOCBR handle 3way diff
				default:
					throw new IllegalStateException("Invalid side value");
				}
			}
			if (diff instanceof RemoveModelElement) {
				switch (this.side) {
				case DiffConstants.LEFT:
					return ((RemoveModelElement) diff).getLeftElement();
				case DiffConstants.RIGHT:
					return ((RemoveModelElement) diff).getRightParent();
				case DiffConstants.ANCESTOR:
					// elementIsNull = delta.getAncestorElt() == null;
					// break;
					// TODOCBR handle 3way diff
				default:
					throw new IllegalStateException("Invalid side value");
				}
			}
			if (diff instanceof AttributeChange) {
				switch (this.side) {
				case DiffConstants.LEFT:
					return ((AttributeChange) diff).getLeftElement();
				case DiffConstants.RIGHT:
					return ((AttributeChange) diff).getRightElement();
				case DiffConstants.ANCESTOR:
					// elementIsNull = delta.getAncestorElt() == null;
					// break;
					// TODOCBR handle 3way diff
				default:
					throw new IllegalStateException("Invalid side value");
				}
			}
			if (diff instanceof UpdateModelElement) {
				switch (this.side) {
				case DiffConstants.LEFT:
					return ((UpdateModelElement) diff).getLeftElement();
				case DiffConstants.RIGHT:
					return ((UpdateModelElement) diff).getRightElement();
				case DiffConstants.ANCESTOR:
					// elementIsNull = delta.getAncestorElt() == null;
					// break;
					// TODOCBR handle 3way diff
				default:
					throw new IllegalStateException("Invalid side value");
				}
			}
			if (diff instanceof DiffGroup) {
				switch (this.side) {
				case DiffConstants.LEFT:
					return ((DiffGroup) diff).getLeftParent();
				case DiffConstants.RIGHT:
					break;
				case DiffConstants.ANCESTOR:
					// elementIsNull = delta.getAncestorElt() == null;
					// break;
					// TODOCBR handle 3way diff
				default:
					throw new IllegalStateException("Invalid side value");
				}
			}
			return null;
		}

		/**
		 * @param diff
		 */
		public void navigateToDiff(final DiffElement diff) {
			switch (this.selectedTab) {
			case TREE_TAB:
				// FIXMECBR handle navigation to diff
				final EObject target = findElementFromDiff(diff);
				this.tree.showItem(target);
				// if (findMatchFromDiff((target))!= null)
				// properties.setInput(findMatchFromDiff((target)));
				break;
			case PROPERTIES_TAB:
				if (has2Elements(diff)) {
					final EObject targt = findElementFromDiff(diff);
					this.properties.setInput(findMatchFromDiff((targt)));
					this.properties.showItem(diff);
				}
				break;
			}

		}

		private boolean has2Elements(final DiffElement diff) {
			if (diff instanceof AttributeChange) {
				return true;
			}
			if (diff instanceof UpdateModelElement) {
				return true;
			}
			return false;
		}

		private Object findMatchFromDiff(final EObject element) {
			// TODOCBR handle cache
			final MatchModel match = ((ModelCompareInput) getInput()).getDelta();
			final TreeIterator it = match.eAllContents();
			while (it.hasNext()) {
				final Object obj = it.next();
				if (obj instanceof Match2Elements) {
					final Match2Elements match_elem = (Match2Elements) obj;
					if ((match_elem.getLeftElement() == element)
							|| (match_elem.getRightElement() == element)) {
						return match_elem;
					}

				}
				if (obj instanceof UnMatchElement) {
					final UnMatchElement match_elem = (UnMatchElement) obj;
					if (match_elem.getElement() == element) {
						return match_elem;
					}
				}
			}
			return null;
		}

		/**
		 * @param att
		 */
		public void navigateToProperty(final EAttribute att) {
			this.properties.showItem(att);

		}
	}

	private Color getColor(final Display display, final RGB rgb) {
		if (rgb == null) {
			return null;
		}
		if (this.fColors == null) {
			this.fColors = new HashMap<RGB, Color>(20);
		}
		Color c = this.fColors.get(rgb);
		if (c == null) {
			c = new Color(display, rgb);
			this.fColors.put(rgb, c);
		}
		return c;
	}

	/**
	 * @param diff
	 * @return
	 */
	private RGB getFillColor(final Match2Elements diff) {
		final boolean selected = (this.currentDiff != null) && this.currentDiff.equals(diff);

		final RGB selected_fill = getBackground(null);
		return this.INCOMING_FILL;
		// TODOCBR handle colors
		// if (diff.getKind() >= DiffConstants.RESOLVED)
		// return RESOLVED;
		// if (isThreeWay) {
		// switch (diff.getKind() & DiffConstants.DIRECTION_MASK) {
		// case DiffConstants.RIGHT:
		// if (leftIsLocal)
		// return selected ? selected_fill : INCOMING_FILL;
		// return selected ? selected_fill : OUTGOING_FILL;
		// case DiffConstants.CONFLICTING:
		// return selected ? selected_fill : CONFLICT_FILL;
		// case DiffConstants.LEFT:
		// if (leftIsLocal)
		// return selected ? selected_fill : OUTGOING_FILL;
		// return selected ? selected_fill : INCOMING_FILL;
		// }
		// return RESOLVED;
		// }
		// return selected ? selected_fill : OUTGOING_FILL;
	}

	/**
	 * @param diff
	 * @return
	 */
	private RGB getStrokeColor(final Match2Elements diff) {
		final boolean selected = (this.currentDiff != null) && this.currentMatch.equals(diff);

		// if (diff.getKind() >= DiffConstants.RESOLVED)
		// return RESOLVED;
		// if (isThreeWay) {
		// switch (diff.getKind() & DiffConstants.DIRECTION_MASK) {
		// case DiffConstants.RIGHT:
		// if (leftIsLocal)
		// return selected ? SELECTED_INCOMING : INCOMING;
		// return selected ? SELECTED_OUTGOING : OUTGOING;
		// case DiffConstants.LEFT:
		// if (leftIsLocal)
		// return selected ? SELECTED_OUTGOING : OUTGOING;
		// return selected ? SELECTED_INCOMING : INCOMING;
		// case DiffConstants.CONFLICTING:
		// return selected ? SELECTED_CONFLICT : CONFLICT;
		// }
		// return null;
		// }
		// TODOCBR handle colors
		return this.OUTGOING;
		// return selected ? SELECTED_OUTGOING : OUTGOING;
	}

	/**
	 * @param diff
	 */
	public void navigateToDiff(final DiffElement diff) {
		this.leftPart.navigateToDiff(diff);
		this.rightPart.navigateToDiff(diff);
		// if (isThreeWay)
		// {
		// ancestorPart.navigateToDiff(diff);
		// }
		// TODOCBR handle 3way diff
		// currentDelta = (MatchElement)diff.eContainer();
		//
		this.currentDiff = diff;
		updateCenter();

	}

	/**
	 * @param att
	 */
	public void navigateToProperty(final EAttribute att) {
		this.leftPart.navigateToProperty(att);
		this.rightPart.navigateToProperty(att);
		if (this.isThreeWay) {
			this.ancestorPart.navigateToProperty(att);
		}
	}

	public void setShowOnlyDiffs(final boolean value) {
		if (value != this.showOnlyDiffs) {
			this.showOnlyDiffs = value;
			update();
		}
	}

	public void setShowPseudoConflicts(final boolean value) {
		if (value != this.showPseudoConflicts) {
			this.showPseudoConflicts = value;
			update();
		}
	}

	MergeFactory mergeFactory = new MergeService().getBestFactory();

	@Override
	protected void mergeLeftToRight() {
		if (this.currentDiff != null) {
			final AbstractMerger merger = this.mergeFactory.createMerger(this.currentDiff);
			if (merger.canUndoInTarget()) {
				merger.undoInTarget();
				setRightDirty(true);
			}
		}
	}

	@Override
	protected void mergeRightToLeft() {
		if (this.currentDiff != null) {
			final AbstractMerger merger = this.mergeFactory.createMerger(this.currentDiff);
			if (merger.canApplyInOrigin()) {
				merger.applyInOrigin();
				setLeftDirty(true);
			}
		}
	}

}
