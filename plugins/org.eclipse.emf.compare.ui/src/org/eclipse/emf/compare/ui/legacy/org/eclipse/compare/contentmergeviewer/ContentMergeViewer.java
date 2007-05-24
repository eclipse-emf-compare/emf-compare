/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.ui.legacy.org.eclipse.compare.contentmergeviewer;

import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.IPropertyChangeNotifier;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.ChangePropertyAction;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.internal.ComparePreferencePage;
import org.eclipse.compare.internal.MergeViewerAction;
import org.eclipse.compare.internal.MergeViewerContentProvider;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.compare.internal.ViewerSwitchingCancelled;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * An abstract compare and merge viewer with two side-by-side content areas and
 * an optional content area for the ancestor. The implementation makes no
 * assumptions about the content type.
 * <p>
 * <code>ContentMergeViewer</code>
 * <ul>
 * <li>implements the overall layout and defines hooks so that subclasses can
 * easily provide an implementation for a specific content type,
 * <li>implements the UI for making the areas resizable,
 * <li>has an action for controlling whether the ancestor area is visible or
 * not,
 * <li>has actions for copying one side of the input to the other side,
 * <li>tracks the dirty state of the left and right sides and send out
 * notification on state changes.
 * </ul>
 * A <code>ContentMergeViewer</code> accesses its model by means of a content
 * provider which must implement the <code>IMergeViewerContentProvider</code>
 * interface.
 * </p>
 * <p>
 * Clients may wish to use the standard concrete subclass
 * <code>TextMergeViewer</code>, or define their own subclass.
 * 
 * @see IMergeViewerContentProvider
 * @see TextMergeViewer
 */
public abstract class ContentMergeViewer extends ContentViewer implements
IPropertyChangeNotifier {

	class SaveAction extends MergeViewerAction {

		SaveAction(final boolean left) {
			super(true, false, false);
			Utilities.initAction(this, getResourceBundle(), "action.save."); //$NON-NLS-1$
		}

		public void run() {
			saveContent(getInput());
		}
	}

	/**
	 * Property names.
	 */
	private static final String ANCESTOR_ENABLED = ComparePreferencePage.INITIALLY_SHOW_ANCESTOR_PANE;

	/* package */static final int HORIZONTAL = 1;

	/* package */static final int VERTICAL = 2;

	static final double HSPLIT = 0.5;

	static final double VSPLIT = 0.3;

	private class ContentMergeViewerLayout extends Layout {

		public Point computeSize(final Composite c, final int w, final int h, final boolean force) {
			return new Point(100, 100);
		}

		public void layout(final Composite composite, final boolean force) {

			// determine some derived sizes
			final int headerHeight = ContentMergeViewer.this.fLeftLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT,
					true).y;
			final Rectangle r = composite.getClientArea();

			final int centerWidth = 34;
			final int width1 = (int) ((r.width - centerWidth) * ContentMergeViewer.this.fHSplit);
			final int width2 = r.width - width1 - centerWidth;

			int height1 = 0;
			int height2 = 0;
			if (ContentMergeViewer.this.fAncestorEnabled && ContentMergeViewer.this.fShowAncestor) {
				height1 = (int) ((r.height - (2 * headerHeight)) * ContentMergeViewer.this.fVSplit);
				height2 = r.height - (2 * headerHeight) - height1;
			} else {
				height1 = 0;
				height2 = r.height - headerHeight;
			}

			int y = 0;

			if (ContentMergeViewer.this.fAncestorEnabled && ContentMergeViewer.this.fShowAncestor) {
				ContentMergeViewer.this.fAncestorLabel.setBounds(0, y, r.width, headerHeight);
				ContentMergeViewer.this.fAncestorLabel.setVisible(true);
				y += headerHeight;
				handleResizeAncestor(0, y, r.width, height1);
				y += height1;
			} else {
				ContentMergeViewer.this.fAncestorLabel.setVisible(false);
				handleResizeAncestor(0, 0, 0, 0);
			}

			ContentMergeViewer.this.fLeftLabel.getSize(); // without this resizing would not always
									// work

			if (centerWidth > 3) {
				ContentMergeViewer.this.fLeftLabel.setBounds(0, y, width1 + 1, headerHeight);
				ContentMergeViewer.this.fDirectionLabel.setVisible(true);
				ContentMergeViewer.this.fDirectionLabel.setBounds(width1 + 1, y, centerWidth - 1,
						headerHeight);
				ContentMergeViewer.this.fRightLabel.setBounds(width1 + centerWidth, y, width2,
						headerHeight);
			} else {
				ContentMergeViewer.this.fLeftLabel.setBounds(0, y, width1, headerHeight);
				ContentMergeViewer.this.fDirectionLabel.setVisible(false);
				ContentMergeViewer.this.fRightLabel
						.setBounds(width1, y, r.width - width1, headerHeight);
			}

			y += headerHeight;

			if ((ContentMergeViewer.this.fCenter != null) && !ContentMergeViewer.this.fCenter.isDisposed()) {
				ContentMergeViewer.this.fCenter.setBounds(width1, y, centerWidth, height2);
			}

			handleResizeLeftRight(0, y, width1, centerWidth, width2, height2);
		}
	}

	class Resizer extends MouseAdapter implements MouseMoveListener {

		Control fControl;

		int fX, fY;

		int fWidth1, fWidth2;

		int fHeight1, fHeight2;

		int fDirection;

		boolean fLiveResize;

		boolean fIsDown;

		public Resizer(final Control c, final int dir) {
			this.fDirection = dir;
			this.fControl = c;
			this.fLiveResize = !(this.fControl instanceof Sash);
			updateCursor(c, dir);
			this.fControl.addMouseListener(this);
			this.fControl.addMouseMoveListener(this);
			this.fControl.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(final DisposeEvent e) {
					Resizer.this.fControl = null;
				}
			});
		}

		public void mouseDoubleClick(final MouseEvent e) {
			if ((this.fDirection & HORIZONTAL) != 0) {
				ContentMergeViewer.this.fHSplit = HSPLIT;
			}
			if ((this.fDirection & VERTICAL) != 0) {
				ContentMergeViewer.this.fVSplit = VSPLIT;
			}
			ContentMergeViewer.this.fComposite.layout(true);
		}

		public void mouseDown(final MouseEvent e) {
			final Composite parent = this.fControl.getParent();

			final Point s = parent.getSize();
			final Point as = ContentMergeViewer.this.fAncestorLabel.getSize();
			final Point ys = ContentMergeViewer.this.fLeftLabel.getSize();
			final Point ms = ContentMergeViewer.this.fRightLabel.getSize();

			this.fWidth1 = ys.x;
			this.fWidth2 = ms.x;
			this.fHeight1 = ContentMergeViewer.this.fLeftLabel.getLocation().y - as.y;
			this.fHeight2 = s.y - (ContentMergeViewer.this.fLeftLabel.getLocation().y + ys.y);

			this.fX = e.x;
			this.fY = e.y;
			this.fIsDown = true;
		}

		public void mouseUp(final MouseEvent e) {
			this.fIsDown = false;
			if (!this.fLiveResize) {
				resize(e);
			}
		}

		public void mouseMove(final MouseEvent e) {
			if (this.fIsDown && this.fLiveResize) {
				resize(e);
			}
		}

		private void resize(final MouseEvent e) {
			final int dx = e.x - this.fX;
			final int dy = e.y - this.fY;

			final int centerWidth = ContentMergeViewer.this.fCenter.getSize().x;

			if ((this.fWidth1 + dx > centerWidth) && (this.fWidth2 - dx > centerWidth)) {
				this.fWidth1 += dx;
				this.fWidth2 -= dx;
				if ((this.fDirection & HORIZONTAL) != 0) {
					ContentMergeViewer.this.fHSplit = (double) this.fWidth1 / (double) (this.fWidth1 + this.fWidth2);
				}
			}
			if ((this.fHeight1 + dy > centerWidth) && (this.fHeight2 - dy > centerWidth)) {
				this.fHeight1 += dy;
				this.fHeight2 -= dy;
				if ((this.fDirection & VERTICAL) != 0) {
					ContentMergeViewer.this.fVSplit = (double) this.fHeight1
							/ (double) (this.fHeight1 + this.fHeight2);
				}
			}

			ContentMergeViewer.this.fComposite.layout(true);
			this.fControl.getDisplay().update();
		}
	}

	/** Style bits for top level composite */
	private int fStyles;

	private ResourceBundle fBundle;

	private CompareConfiguration fCompareConfiguration;

	private IPropertyChangeListener fPropertyChangeListener;

	private ICompareInputChangeListener fCompareInputChangeListener;

	private ListenerList fListenerList;

	boolean fConfirmSave = true;

	private double fHSplit = HSPLIT; // width ratio of left and right panes

	private double fVSplit = VSPLIT; // height ratio of ancestor and bottom
										// panes

	private boolean fAncestorEnabled = true; // show ancestor in case of
												// conflicts

	/* package */boolean fShowAncestor = false; // if current input has
												// conflicts

	private boolean fIsThreeWay = false;

	private ActionContributionItem fAncestorItem;

	private Action fCopyLeftToRightAction; // copy from left to right

	private Action fCopyRightToLeftAction; // copy from right to left

	private Action fCopyCurrentLeftToRightAction; // copy from left to right

	private Action fCopyCurrentRightToLeftAction;

	protected MergeViewerAction fLeftSaveAction;

	protected MergeViewerAction fRightSaveAction;

	private IKeyBindingService fKeyBindingService;

	// SWT widgets
	/* package */protected Composite fComposite;

	private CLabel fAncestorLabel;

	private CLabel fLeftLabel;

	private CLabel fRightLabel;

	/* package */CLabel fDirectionLabel;

	/* package */Control fCenter;

	// ---- SWT resources to be disposed
	private Image fRightArrow;

	private Image fLeftArrow;

	private Image fBothArrow;

	Cursor fNormalCursor;

	private Cursor fHSashCursor;

	private Cursor fVSashCursor;

	private Cursor fHVSashCursor;

	// ---- end

	/**
	 * Creates a new content merge viewer and initializes with a resource bundle
	 * and a configuration.
	 * 
	 * @param style
	 *            SWT style bits
	 * @param bundle
	 *            the resource bundle
	 * @param cc
	 *            the configuration object
	 */
	protected ContentMergeViewer(final int style, final ResourceBundle bundle,
			final CompareConfiguration cc) {

		this.fStyles = style & ~(SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT); // remove
																	// BIDI
																	// direction
																	// bits
		this.fBundle = bundle;

		this.fAncestorEnabled = Utilities.getBoolean(cc, ANCESTOR_ENABLED,
				this.fAncestorEnabled);
		this.fConfirmSave = Utilities.getBoolean(cc,
				CompareEditor.CONFIRM_SAVE_PROPERTY, this.fConfirmSave);

		setContentProvider(new MergeViewerContentProvider(cc));

		this.fCompareInputChangeListener = new ICompareInputChangeListener() {
			public void compareInputChanged(final ICompareInput input) {
				ContentMergeViewer.this.internalRefresh(input);
			}
		};

		this.fCompareConfiguration = cc;
		if (this.fCompareConfiguration != null) {
			this.fPropertyChangeListener = new IPropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent event) {
					ContentMergeViewer.this.propertyChange(event);
				}
			};
			this.fCompareConfiguration
					.addPropertyChangeListener(this.fPropertyChangeListener);
		}

		this.fLeftSaveAction = new SaveAction(true);
		this.fLeftSaveAction.setEnabled(false);
		this.fRightSaveAction = new SaveAction(false);
		this.fRightSaveAction.setEnabled(false);
	}

	// ---- hooks ---------------------

	/**
	 * Returns the viewer's name.
	 * 
	 * @return the viewer's name
	 */
	public String getTitle() {
		return Utilities.getString(getResourceBundle(), "title"); //$NON-NLS-1$
	}

	/**
	 * Creates the SWT controls for the ancestor, left, and right content areas
	 * of this compare viewer. Implementations typically hold onto the controls
	 * so that they can be initialized with the input objects in method
	 * <code>updateContent</code>.
	 * 
	 * @param composite
	 *            the container for the three areas
	 */
	abstract protected void createControls(Composite composite);

	/**
	 * Lays out the ancestor area of the compare viewer. It is called whenever
	 * the viewer is resized or when the sashes between the areas are moved to
	 * adjust the size of the areas.
	 * 
	 * @param x
	 *            the horizontal position of the ancestor area within its
	 *            container
	 * @param y
	 *            the vertical position of the ancestor area within its
	 *            container
	 * @param width
	 *            the width of the ancestor area
	 * @param height
	 *            the height of the ancestor area
	 */
	abstract protected void handleResizeAncestor(int x, int y, int width,
			int height);

	/**
	 * Lays out the left and right areas of the compare viewer. It is called
	 * whenever the viewer is resized or when the sashes between the areas are
	 * moved to adjust the size of the areas.
	 * 
	 * @param x
	 *            the horizontal position of the left area within its container
	 * @param y
	 *            the vertical position of the left and right area within its
	 *            container
	 * @param leftWidth
	 *            the width of the left area
	 * @param centerWidth
	 *            the width of the gap between the left and right areas
	 * @param rightWidth
	 *            the width of the right area
	 * @param height
	 *            the height of the left and right areas
	 */
	abstract protected void handleResizeLeftRight(int x, int y, int leftWidth,
			int centerWidth, int rightWidth, int height);

	/**
	 * Contributes items to the given <code>ToolBarManager</code>. It is
	 * called when this viewer is installed in its container and if the
	 * container has a <code>ToolBarManager</code>. The
	 * <code>ContentMergeViewer</code> implementation of this method does
	 * nothing. Subclasses may reimplement.
	 * 
	 * @param toolBarManager
	 *            the toolbar manager to contribute to
	 */
	protected void createToolItems(final ToolBarManager toolBarManager) {
		// empty implementation
	}

	/**
	 * Initializes the controls of the three content areas with the given input
	 * objects.
	 * 
	 * @param ancestor
	 *            the input for the ancestor area
	 * @param left
	 *            the input for the left area
	 * @param right
	 *            the input for the right area
	 */
	abstract protected void updateContent(Object ancestor, Object left,
			Object right);

	/**
	 * Copies the content of one side to the other side. Called from the
	 * (internal) actions for copying the sides of the viewer's input object.
	 * 
	 * @param leftToRight
	 *            if <code>true</code>, the left side is copied to the right
	 *            side; if <code>false</code>, the right side is copied to
	 *            the left side
	 */
	abstract protected void copy(boolean leftToRight);

	abstract protected void mergeLeftToRight();

	abstract protected void mergeRightToLeft();

	/**
	 * Returns the byte contents of the left or right side. If the viewer has no
	 * editable content <code>null</code> can be returned.
	 * 
	 * @param left
	 *            if <code>true</code>, the byte contents of the left area is
	 *            returned; if <code>false</code>, the byte contents of the
	 *            right area
	 * @return the content as an array of bytes, or <code>null</code>
	 */
	abstract protected byte[] getContents(boolean left);

	// ----------------------------

	/**
	 * Returns the resource bundle of this viewer.
	 * 
	 * @return the resource bundle
	 */
	protected ResourceBundle getResourceBundle() {
		return this.fBundle;
	}

	/**
	 * Returns the compare configuration of this viewer, or <code>null</code>
	 * if this viewer does not yet have a configuration.
	 * 
	 * @return the compare configuration, or <code>null</code> if none
	 */
	protected CompareConfiguration getCompareConfiguration() {
		return this.fCompareConfiguration;
	}

	/**
	 * The <code>ContentMergeViewer</code> implementation of this
	 * <code>ContentViewer</code> method checks to ensure that the content
	 * provider is an <code>IMergeViewerContentProvider</code>.
	 * 
	 * @param contentProvider
	 *            the content provider to set. Must implement
	 *            IMergeViewerContentProvider.
	 */
	public void setContentProvider(final IContentProvider contentProvider) {
		Assert.isTrue(contentProvider instanceof IMergeViewerContentProvider);
		super.setContentProvider(contentProvider);
	}

	/* package */IMergeViewerContentProvider getMergeContentProvider() {
		return (IMergeViewerContentProvider) getContentProvider();
	}

	/**
	 * The <code>ContentMergeViewer</code> implementation of this
	 * <code>Viewer</code> method returns the empty selection. Subclasses may
	 * override.
	 * 
	 * @return empty selection.
	 */
	public ISelection getSelection() {
		return new ISelection() {
			public boolean isEmpty() {
				return true;
			}
		};
	}

	/*
	 * The <code>ContentMergeViewer</code> implementation of this <code>Viewer</code>
	 * method does nothing. Subclasses may reimplement.
	 */
	public void setSelection(final ISelection selection, final boolean reveal) {
		// empty implementation
	}

	/* package */void propertyChange(final PropertyChangeEvent event) {

		final String key = event.getProperty();

		if (key.equals(ANCESTOR_ENABLED)) {
			this.fAncestorEnabled = Utilities.getBoolean(getCompareConfiguration(),
					ANCESTOR_ENABLED, this.fAncestorEnabled);
			this.fComposite.layout(true);

			updateCursor(this.fLeftLabel, VERTICAL);
			updateCursor(this.fDirectionLabel, HORIZONTAL | VERTICAL);
			updateCursor(this.fRightLabel, VERTICAL);

			return;
		}
	}

	void updateCursor(final Control c, final int dir) {
		if (!(c instanceof Sash)) {
			Cursor cursor = null;
			switch (dir) {
			case VERTICAL:
				if (this.fAncestorEnabled) {
					if (this.fVSashCursor == null) {
						this.fVSashCursor = new Cursor(c.getDisplay(),
								SWT.CURSOR_SIZENS);
					}
					cursor = this.fVSashCursor;
				} else {
					if (this.fNormalCursor == null) {
						this.fNormalCursor = new Cursor(c.getDisplay(),
								SWT.CURSOR_ARROW);
					}
					cursor = this.fNormalCursor;
				}
				break;
			case HORIZONTAL:
				if (this.fHSashCursor == null) {
					this.fHSashCursor = new Cursor(c.getDisplay(), SWT.CURSOR_SIZEWE);
				}
				cursor = this.fHSashCursor;
				break;
			case VERTICAL + HORIZONTAL:
				if (this.fAncestorEnabled) {
					if (this.fHVSashCursor == null) {
						this.fHVSashCursor = new Cursor(c.getDisplay(),
								SWT.CURSOR_SIZEALL);
					}
					cursor = this.fHVSashCursor;
				} else {
					if (this.fHSashCursor == null) {
						this.fHSashCursor = new Cursor(c.getDisplay(),
								SWT.CURSOR_SIZEWE);
					}
					cursor = this.fHSashCursor;
				}
				break;
			}
			if (cursor != null) {
				c.setCursor(cursor);
			}
		}
	}

	void setAncestorVisibility(final boolean visible, final boolean enabled) {
		if (this.fAncestorItem != null) {
			final Action action = (Action) this.fAncestorItem.getAction();
			if (action != null) {
				action.setChecked(visible);
				action.setEnabled(enabled);
			}
		}
		if (this.fCompareConfiguration != null) {
			this.fCompareConfiguration.setProperty(ANCESTOR_ENABLED, new Boolean(
					visible));
		}
	}

	// ---- input

	/* package */boolean isThreeWay() {
		return this.fIsThreeWay;
	}

	/**
	 * Internal hook method called when the input to this viewer is initially
	 * set or subsequently changed.
	 * <p>
	 * The <code>ContentMergeViewer</code> implementation of this
	 * <code>Viewer</code> method tries to save the old input by calling
	 * <code>doSave(...)</code> and then calls
	 * <code>internalRefresh(...)</code>.
	 * 
	 * @param input
	 *            the new input of this viewer, or <code>null</code> if there
	 *            is no new input
	 * @param oldInput
	 *            the old input element, or <code>null</code> if there was
	 *            previously no input
	 */
	protected final void inputChanged(final Object input, final Object oldInput) {

		if (input != oldInput) {
			if (oldInput instanceof ICompareInput) {
				((ICompareInput) oldInput)
						.removeCompareInputChangeListener(this.fCompareInputChangeListener);
			}
		}

		final boolean success = doSave(input, oldInput);

		if (input != oldInput) {
			if (input instanceof ICompareInput) {
				((ICompareInput) input)
						.addCompareInputChangeListener(this.fCompareInputChangeListener);
			}
		}

		if (success) {
			setLeftDirty(false);
			setRightDirty(false);
		}

		if (input != oldInput) {
			internalRefresh(input);
		}
	}

	/**
	 * This method is called from the <code>Viewer</code> method
	 * <code>inputChanged</code> to save any unsaved changes of the old input.
	 * <p>
	 * The <code>ContentMergeViewer</code> implementation of this method calls
	 * <code>saveContent(...)</code>. If confirmation has been turned on with
	 * <code>setConfirmSave(true)</code>, a confirmation alert is posted
	 * before saving.
	 * </p>
	 * Clients can override this method and are free to decide whether they want
	 * to call the inherited method.
	 * 
	 * @param newInput
	 *            the new input of this viewer, or <code>null</code> if there
	 *            is no new input
	 * @param oldInput
	 *            the old input element, or <code>null</code> if there was
	 *            previously no input
	 * @return <code>true</code> if saving was successful, or if the user
	 *         didn't want to save (by pressing 'NO' in the confirmation
	 *         dialog).
	 * @since 2.0
	 */
	protected boolean doSave(final Object newInput, final Object oldInput) {

		// before setting the new input we have to save the old
		if (this.fLeftSaveAction.isEnabled() || this.fRightSaveAction.isEnabled()) {

			// post alert
			if (this.fConfirmSave) {
				final Shell shell = this.fComposite.getShell();

				final MessageDialog dialog = new MessageDialog(shell, Utilities
						.getString(getResourceBundle(), "saveDialog.title"), //$NON-NLS-1$
						null, // accept the default window icon
						Utilities.getString(getResourceBundle(),
								"saveDialog.message"), //$NON-NLS-1$
						MessageDialog.QUESTION, new String[] {
								IDialogConstants.YES_LABEL,
								IDialogConstants.NO_LABEL, }, 0); // default
																	// button
																	// index

				switch (dialog.open()) { // open returns index of pressed
											// button
				case 0:
					saveContent(oldInput);
					break;
				case 1:
					setLeftDirty(false);
					setRightDirty(false);
					break;
				case 2:
					throw new ViewerSwitchingCancelled();
				}
			} else {
				saveContent(oldInput);
			}
			return true;
		}
		return false;
	}

	/**
	 * Controls whether <code>doSave(Object, Object)</code> asks for
	 * confirmation before saving the old input with
	 * <code>saveContent(Object)</code>.
	 * 
	 * @param enable
	 *            a value of <code>true</code> enables confirmation
	 * @since 2.0
	 */
	public void setConfirmSave(final boolean enable) {
		this.fConfirmSave = enable;
	}

	/*
	 * (non Javadoc) see Viewer.refresh
	 */
	public void refresh() {
		internalRefresh(getInput());
	}

	private void internalRefresh(final Object input) {

		final IMergeViewerContentProvider content = getMergeContentProvider();
		if (content != null) {
			final Object ancestor = content.getAncestorContent(input);
			if (input instanceof ICompareInput) {
				this.fIsThreeWay = (((ICompareInput) input).getKind() & Differencer.DIRECTION_MASK) != 0;
			} else {
				this.fIsThreeWay = ancestor != null;
			}

			if (this.fAncestorItem != null) {
				this.fAncestorItem.setVisible(this.fIsThreeWay);
			}

			final boolean oldFlag = this.fShowAncestor;
			this.fShowAncestor = this.fIsThreeWay && content.showAncestor(input);

			if (this.fAncestorEnabled && (oldFlag != this.fShowAncestor)) {
				this.fComposite.layout(true);
			}

			final ToolBarManager tbm = CompareViewerPane.getToolBarManager(this.fComposite
					.getParent());
			if (tbm != null) {
				updateToolItems();
				tbm.update(true);
				tbm.getControl().getParent().layout(true);
			}

			updateHeader();

			final Object left = content.getLeftContent(input);
			final Object right = content.getRightContent(input);
			updateContent(ancestor, left, right);
		}
	}

	// ---- layout & SWT control creation

	/**
	 * Builds the SWT controls for the three areas of a compare/merge viewer.
	 * <p>
	 * Calls the hooks <code>createControls</code> and
	 * <code>createToolItems</code> to let subclasses build the specific
	 * content areas and to add items to an enclosing toolbar.
	 * <p>
	 * This method must only be called in the constructor of subclasses.
	 * 
	 * @param parent
	 *            the parent control
	 * @return the new control
	 */
	protected final Control buildControl(final Composite parent) {

		this.fComposite = new Composite(parent, this.fStyles | SWT.LEFT_TO_RIGHT) { // we
																			// force
																			// a
																			// specific
																			// direction
			public boolean setFocus() {
				return internalSetFocus();
			}
		};
		this.fComposite.setData(CompareUI.COMPARE_VIEWER_TITLE, getTitle());

		hookControl(this.fComposite); // hook help & dispose listener

		this.fComposite.setLayout(new ContentMergeViewerLayout());

		final int style = SWT.SHADOW_OUT;
		this.fAncestorLabel = new CLabel(this.fComposite, style);

		this.fLeftLabel = new CLabel(this.fComposite, style);
		new Resizer(this.fLeftLabel, VERTICAL);

		this.fDirectionLabel = new CLabel(this.fComposite, style);
		this.fDirectionLabel.setAlignment(SWT.CENTER);
		new Resizer(this.fDirectionLabel, HORIZONTAL | VERTICAL);

		this.fRightLabel = new CLabel(this.fComposite, style);
		new Resizer(this.fRightLabel, VERTICAL);

		if ((this.fCenter == null) || this.fCenter.isDisposed()) {
			this.fCenter = createCenter(this.fComposite);
		}

		createControls(this.fComposite);

		final IWorkbenchPartSite ps = Utilities.findSite(this.fComposite);
		this.fKeyBindingService = ps != null ? ps.getKeyBindingService() : null;

		final ToolBarManager tbm = CompareViewerPane.getToolBarManager(parent);
		if (tbm != null) {
			tbm.removeAll();

			// define groups
			tbm.add(new Separator("modes")); //$NON-NLS-1$
			tbm.add(new Separator("merge")); //$NON-NLS-1$
			tbm.add(new Separator("navigation")); //$NON-NLS-1$

			final CompareConfiguration cc = getCompareConfiguration();

			if (cc.isRightEditable()) {
				this.fCopyLeftToRightAction = new Action() {
					public void run() {
						copy(true);
					}
				};
				this.fCopyCurrentLeftToRightAction = new Action() {
					public void run() {
						mergeLeftToRight();
					}
				};
				Utilities.initAction(this.fCopyLeftToRightAction,
						getResourceBundle(), "action.CopyLeftToRight."); //$NON-NLS-1$
				tbm.appendToGroup("merge", this.fCopyLeftToRightAction); //$NON-NLS-1$
				/*
				Utilities.registerAction(this.fKeyBindingService,
						this.fCopyLeftToRightAction,
						"org.eclipse.compare.copyAllLeftToRight"); //$NON-NLS-1$
						Removed to build against Eclipse 3.3
						*/

			}

			if (cc.isLeftEditable()) {
				this.fCopyRightToLeftAction = new Action() {
					public void run() {
						copy(false);
					}
				};
				this.fCopyCurrentRightToLeftAction = new Action() {
					public void run() {
						mergeRightToLeft();
					}
				};
				Utilities.initAction(this.fCopyRightToLeftAction,
						getResourceBundle(), "action.CopyRightToLeft."); //$NON-NLS-1$
				tbm.appendToGroup("merge", this.fCopyRightToLeftAction); //$NON-NLS-1$
				/*
				  
				 Utilities.registerAction(this.fKeyBindingService,
						this.fCopyRightToLeftAction,
						"org.eclipse.compare.copyAllRightToLeft"); //$NON-NLS-1$
						Removed to build against Eclipse 3.3
						*/

			}

			if (cc.isRightEditable()) {
				Utilities.initAction(this.fCopyCurrentLeftToRightAction,
						getResourceBundle(), "action.CopyCurrentLeftToRight."); //$NON-NLS-1$
				tbm.appendToGroup("merge", this.fCopyCurrentLeftToRightAction); //$NON-NLS-1$
			}
			if (cc.isLeftEditable()) {
				Utilities.initAction(this.fCopyCurrentRightToLeftAction,
						getResourceBundle(), "action.CopyCurrentRightToLeft."); //$NON-NLS-1$
				tbm.appendToGroup("merge", this.fCopyCurrentRightToLeftAction); //$NON-NLS-1$
			}
			final Action a = new ChangePropertyAction(this.fBundle, this.fCompareConfiguration,
					"action.EnableAncestor.", ANCESTOR_ENABLED); //$NON-NLS-1$
			a.setChecked(this.fAncestorEnabled);
			this.fAncestorItem = new ActionContributionItem(a);
			this.fAncestorItem.setVisible(false);
			tbm.appendToGroup("modes", this.fAncestorItem); //$NON-NLS-1$

			createToolItems(tbm);
			updateToolItems();

			tbm.update(true);
		}

		return this.fComposite;
	}

	/* package */boolean internalSetFocus() {
		return false;
	}

	protected int getCenterWidth() {
		return 3;
	}

	/* package */boolean getAncestorEnabled() {
		return this.fAncestorEnabled;
	}

	protected Control createCenter(final Composite parent) {
		final Sash sash = new Sash(parent, SWT.VERTICAL);
		new Resizer(sash, HORIZONTAL);
		return sash;
	}

	protected Control getCenter() {
		return this.fCenter;
	}

	/*
	 * @see Viewer.getControl()
	 */
	public Control getControl() {
		return this.fComposite;
	}

	/*
	 * Called on the viewer disposal. Unregisters from the compare
	 * configuration. Clients may extend if they have to do additional cleanup.
	 */
	protected void handleDispose(final DisposeEvent event) {

		if (this.fKeyBindingService != null) {
			if (this.fCopyLeftToRightAction != null) {
				this.fKeyBindingService.unregisterAction(this.fCopyLeftToRightAction);
			}
			if (this.fCopyRightToLeftAction != null) {
				this.fKeyBindingService.unregisterAction(this.fCopyRightToLeftAction);
			}
			if (this.fCopyCurrentRightToLeftAction != null) {
				this.fKeyBindingService.unregisterAction(this.fCopyRightToLeftAction);
			}
			if (this.fCopyCurrentLeftToRightAction != null) {
				this.fKeyBindingService.unregisterAction(this.fCopyRightToLeftAction);
			}
			this.fKeyBindingService = null;
		}

		final Object input = getInput();
		if (input instanceof ICompareInput) {
			((ICompareInput) input)
					.removeCompareInputChangeListener(this.fCompareInputChangeListener);
		}

		if ((this.fCompareConfiguration != null) && (this.fPropertyChangeListener != null)) {
			this.fCompareConfiguration
					.removePropertyChangeListener(this.fPropertyChangeListener);
			this.fPropertyChangeListener = null;
		}

		this.fAncestorLabel = null;
		this.fLeftLabel = null;
		this.fDirectionLabel = null;
		this.fRightLabel = null;
		this.fCenter = null;

		if (this.fRightArrow != null) {
			this.fRightArrow.dispose();
			this.fRightArrow = null;
		}
		if (this.fLeftArrow != null) {
			this.fLeftArrow.dispose();
			this.fLeftArrow = null;
		}
		if (this.fBothArrow != null) {
			this.fBothArrow.dispose();
			this.fBothArrow = null;
		}

		if (this.fNormalCursor != null) {
			this.fNormalCursor.dispose();
			this.fNormalCursor = null;
		}
		if (this.fHSashCursor != null) {
			this.fHSashCursor.dispose();
			this.fHSashCursor = null;
		}
		if (this.fVSashCursor != null) {
			this.fVSashCursor.dispose();
			this.fVSashCursor = null;
		}
		if (this.fHVSashCursor != null) {
			this.fHVSashCursor.dispose();
			this.fHVSashCursor = null;
		}

		super.handleDispose(event);
	}

	/**
	 * Updates the enabled state of the toolbar items.
	 * <p>
	 * This method is called whenever the state of the items needs updating.
	 * <p>
	 * Subclasses may extend this method, although this is generally not
	 * required.
	 */
	protected void updateToolItems() {

		final IMergeViewerContentProvider content = getMergeContentProvider();

		final Object input = getInput();

		if (this.fCopyLeftToRightAction != null) {
			final boolean enable = content.isRightEditable(input);
			// if (enable && input instanceof ICompareInput) {
			// ITypedElement e= ((ICompareInput) input).getLeft();
			// if (e == null)
			// enable= false;
			// }
			this.fCopyLeftToRightAction.setEnabled(enable);
		}

		if (this.fCopyRightToLeftAction != null) {
			final boolean enable = content.isLeftEditable(input);
			// if (enable && input instanceof ICompareInput) {
			// ITypedElement e= ((ICompareInput) input).getRight();
			// if (e == null)
			// enable= false;
			// }
			this.fCopyRightToLeftAction.setEnabled(enable);
		}
		if (this.fCopyCurrentRightToLeftAction != null) {
			final boolean enable = content.isLeftEditable(input);
			this.fCopyCurrentRightToLeftAction.setEnabled(enable);
		}
		if (this.fCopyCurrentLeftToRightAction != null) {
			final boolean enable = content.isLeftEditable(input);
			this.fCopyCurrentLeftToRightAction.setEnabled(enable);
		}
	}

	/**
	 * Updates the headers of the three areas by querying the content provider
	 * for a name and image for the three sides of the input object.
	 * <p>
	 * This method is called whenever the header must be updated.
	 * <p>
	 * Subclasses may extend this method, although this is generally not
	 * required.
	 */
	protected void updateHeader() {

		final IMergeViewerContentProvider content = getMergeContentProvider();
		final Object input = getInput();

		if (this.fAncestorLabel != null) {
			this.fAncestorLabel.setImage(content.getAncestorImage(input));
			this.fAncestorLabel.setText(content.getAncestorLabel(input));
		}
		if (this.fLeftLabel != null) {
			this.fLeftLabel.setImage(content.getLeftImage(input));
			this.fLeftLabel.setText(content.getLeftLabel(input));
		}
		if (this.fRightLabel != null) {
			this.fRightLabel.setImage(content.getRightImage(input));
			this.fRightLabel.setText(content.getRightLabel(input));
		}
	}

	// private Image loadImage(String name) {
	// ImageDescriptor id=
	// ImageDescriptor.createFromFile(ContentMergeViewer.class, name);
	// if (id != null)
	// return id.createImage();
	// return null;
	// }

	/*
	 * Calculates the height of the header.
	 */
	/* package */int getHeaderHeight() {
		int headerHeight = this.fLeftLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).y;
		headerHeight = Math.max(headerHeight, this.fDirectionLabel.computeSize(
				SWT.DEFAULT, SWT.DEFAULT, true).y);
		return headerHeight;
	}

	// ---- merge direction

	/*
	 * Returns true if both sides are editable.
	 */
	/* package */boolean canToggleMergeDirection() {
		final IMergeViewerContentProvider content = getMergeContentProvider();
		final Object input = getInput();
		return content.isLeftEditable(input) && content.isRightEditable(input);
	}

	// ---- dirty state & saving state

	/*
	 * (non Javadoc) see IPropertyChangeNotifier.addPropertyChangeListener
	 */
	public void addPropertyChangeListener(final IPropertyChangeListener listener) {
		if (this.fListenerList == null) {
			this.fListenerList = new ListenerList();
		}
		this.fListenerList.add(listener);
	}

	/*
	 * (non Javadoc) see IPropertyChangeNotifier.removePropertyChangeListener
	 */
	public void removePropertyChangeListener(final IPropertyChangeListener listener) {
		if (this.fListenerList != null) {
			this.fListenerList.remove(listener);
			if (this.fListenerList.isEmpty()) {
				this.fListenerList = null;
			}
		}
	}

	/* package */void fireDirtyState(final boolean state) {
		Utilities.firePropertyChange(this.fListenerList, this,
				CompareEditorInput.DIRTY_STATE, null, new Boolean(state));
	}

	/**
	 * Sets the dirty state of the left side of this viewer. If the new value
	 * differs from the old all registered listener are notified with a
	 * <code>PropertyChangeEvent</code> with the property name
	 * <code>CompareEditorInput.DIRTY_STATE</code>.
	 * 
	 * @param dirty
	 *            the state of the left side dirty flag
	 */
	protected void setLeftDirty(final boolean dirty) {
		if (this.fLeftSaveAction.isEnabled() != dirty) {
			this.fLeftSaveAction.setEnabled(dirty);
			fireDirtyState(dirty);
		}
	}

	/**
	 * Sets the dirty state of the right side of this viewer. If the new value
	 * differs from the old all registered listener are notified with a
	 * <code>PropertyChangeEvent</code> with the property name
	 * <code>CompareEditorInput.DIRTY_STATE</code>.
	 * 
	 * @param dirty
	 *            the state of the right side dirty flag
	 */
	protected void setRightDirty(final boolean dirty) {
		if (this.fRightSaveAction.isEnabled() != dirty) {
			this.fRightSaveAction.setEnabled(dirty);
			fireDirtyState(dirty);
		}
	}

	/*
	 * Save the viewers's content. Note: this method is for internal use only.
	 * Clients should not call this method.
	 * 
	 * @since 2.0
	 */
	public void save(final IProgressMonitor pm) throws CoreException {
		saveContent(getInput());
	}

	/*
	 * Save modified content back to input elements via the content provider.
	 */
	/* package */void saveContent(final Object oldInput) {

		// write back modified contents
		final IMergeViewerContentProvider content = (IMergeViewerContentProvider) getContentProvider();

		final boolean leftEmpty = content.getLeftContent(oldInput) == null;
		final boolean rightEmpty = content.getRightContent(oldInput) == null;

		if (this.fCompareConfiguration.isLeftEditable()
				&& this.fLeftSaveAction.isEnabled()) {
			byte[] bytes = getContents(true);
			if (leftEmpty && (bytes != null) && (bytes.length == 0)) {
				bytes = null;
			}
			setLeftDirty(false);
			content.saveLeftContent(oldInput, bytes);
		}

		if (this.fCompareConfiguration.isRightEditable()
				&& this.fRightSaveAction.isEnabled()) {
			byte[] bytes = getContents(false);
			if (rightEmpty && (bytes != null) && (bytes.length == 0)) {
				bytes = null;
			}
			setRightDirty(false);
			content.saveRightContent(oldInput, bytes);
		}
	}
}
