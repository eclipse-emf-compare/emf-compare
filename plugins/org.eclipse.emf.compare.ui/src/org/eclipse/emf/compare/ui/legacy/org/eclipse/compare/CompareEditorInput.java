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
package org.eclipse.emf.compare.ui.legacy.org.eclipse.compare;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.IPropertyChangeNotifier;
import org.eclipse.compare.Splitter;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.internal.CompareNavigator;
import org.eclipse.compare.internal.ComparePreferencePage;
import org.eclipse.compare.internal.CompareUIPlugin;
import org.eclipse.compare.internal.ISavable;
import org.eclipse.compare.internal.IgnoreWhiteSpaceAction;
import org.eclipse.compare.internal.ShowPseudoConflicts;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.ISaveablesSource;

/**
 * A compare operation which can present its results in a special editor.
 * Running the compare operation and presenting the results in a compare editor
 * are combined in one class because it allows a client to keep the
 * implementation all in one place while separating it from the innards of a
 * specific UI implementation of compare/merge.
 * <p>
 * A <code>CompareEditorInput</code> defines methods for the following
 * sequence steps:
 * <UL>
 * <LI>running a lengthy compare operation under progress monitor control,
 * <LI>creating a UI for displaying the model and initializing the some widgets
 * with the compare result,
 * <LI>tracking the dirty state of the model in case of merge,
 * <LI>saving the model.
 * </UL>
 * The Compare plug-in's <code>openCompareEditor</code> method takes an
 * <code>ICompareEditorInput</code> and starts sequencing through the above
 * steps. If the compare result is not empty a new compare editor is opened and
 * takes over the sequence until eventually closed.
 * <p>
 * The <code>prepareInput</code> method should contain the code of the compare
 * operation. It is executed under control of a progress monitor and can be
 * canceled. If the result of the compare is not empty, that is if there are
 * differences that needs to be presented, the <code>ICompareEditorInput</code>
 * should hold onto them and return them with the <code>getCompareResult</code>
 * method. If the value returned from <code>getCompareResult</code> is not
 * <code>null</code> a compare editor is opened on the
 * <code>ICompareEditorInput</code> with title and title image initialized by
 * the corresponding methods of the <code>ICompareEditorInput</code>.
 * <p>
 * Creation of the editor's SWT controls is delegated to the
 * <code>createContents</code> method. Here the SWT controls must be created
 * and initialized with the result of the compare operation.
 * <p>
 * If merging is allowed, the modification state of the compared constituents
 * must be tracked and the dirty state returned from method
 * <code>isSaveNeeded</code>. The value <code>true</code> triggers a
 * subsequent call to <code>save</code> where the modified resources can be
 * saved.
 * <p>
 * The most important part of this implementation is the setup of the
 * compare/merge UI. The UI uses a simple browser metaphor to present compare
 * results. The top half of the layout shows the structural compare results
 * (e.g. added, deleted, and changed files), the bottom half the content compare
 * results (e.g. textual differences between two files). A selection in the top
 * pane is fed to the bottom pane. If a content viewer is registered for the
 * type of the selected object, this viewer is installed in the pane. In
 * addition if a structure viewer is registered for the selection type the top
 * pane is split horizontally to make room for another pane and the structure
 * viewer is installed in it. When comparing Java files this second structure
 * viewer would show the structural differences within a Java file, e.g. added,
 * deleted or changed methods and fields.
 * <p>
 * Subclasses provide custom setups, e.g. for a Catchup/Release operation by
 * passing a subclass of <code>CompareConfiguration</code> and by implementing
 * the <code>prepareInput</code> method. If a subclass cannot use the
 * <code>DiffTreeViewer</code> which is installed by default in the top left
 * pane, method <code>createDiffViewer</code> can be overridden.
 * <p>
 * If subclasses of this class implement {@link ISaveablesSource}, the compare
 * editor will pass these models through to the workbench. The editor will still
 * show the dirty indicator if one of these underlying models is dirty. It is
 * the reponsibility of subclasses that implement this interface to call
 * {@link #setDirty(boolean)} when the dirty state of any of the models managed
 * by the sublcass change dirty state.
 * 
 * @see CompareUI
 * @see CompareEditorInput
 */
public abstract class CompareEditorInput extends
		org.eclipse.compare.CompareEditorInput implements IEditorInput,
		IPropertyChangeNotifier, IRunnableWithProgress {

	private static final boolean DEBUG = false;

	/**
	 * The name of the "dirty" property (value <code>"DIRTY_STATE"</code>).
	 */
	public static final String DIRTY_STATE = "DIRTY_STATE"; //$NON-NLS-1$

	private static final String COMPARE_EDITOR_IMAGE_NAME = "eview16/compare_view.gif"; //$NON-NLS-1$

	private static Image fgTitleImage;

	private Splitter fComposite;

	private CompareConfiguration fCompareConfiguration;

	private CompareViewerSwitchingPane fStructureInputPane;

	private CompareViewerSwitchingPane fStructurePane1;

	private CompareViewerSwitchingPane fStructurePane2;

	private CompareViewerSwitchingPane fContentInputPane;

	private CompareViewerSwitchingPane fFocusPane;

	private String fMessage;

	private Object fInput;

	private String fTitle;

	private final ListenerList fListenerList = new ListenerList();

	private CompareNavigator fNavigator;

	private boolean fDirty = false;

	private final ArrayList fDirtyViewers = new ArrayList();

	protected IPropertyChangeListener fDirtyStateListener;

	private IgnoreWhiteSpaceAction fIgnoreWhitespace;

	private ShowPseudoConflicts fShowPseudoConflicts;

	boolean fStructureCompareOnSingleClick = true;

	boolean fUseOutlineView = false;

	/**
	 * Creates a <code>CompareEditorInput</code> which is initialized with the
	 * given compare configuration. The compare configuration is passed to
	 * subsequently created viewers.
	 * 
	 * @param configuration
	 *            the compare configuration
	 */
	public CompareEditorInput(final CompareConfiguration configuration) {
		super(configuration);
		this.fCompareConfiguration = configuration;
		Assert.isNotNull(configuration);

		final Object object = this.fCompareConfiguration
				.getProperty(CompareConfiguration.USE_OUTLINE_VIEW);
		if (object instanceof Boolean) {
			this.fUseOutlineView = ((Boolean) object).booleanValue();
		}

		final ResourceBundle bundle = CompareUI.getResourceBundle();
		this.fIgnoreWhitespace = new IgnoreWhiteSpaceAction(bundle, configuration);
		this.fShowPseudoConflicts = new ShowPseudoConflicts(bundle, configuration);

		this.fDirtyStateListener = new IPropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent e) {
				final String propertyName = e.getProperty();
				if (CompareEditorInput.DIRTY_STATE.equals(propertyName)) {
					boolean changed = false;
					final Object newValue = e.getNewValue();
					if (newValue instanceof Boolean) {
						changed = ((Boolean) newValue).booleanValue();
					}
					setDirty(e.getSource(), changed);
				}
			}
		};

		final IPreferenceStore ps = configuration.getPreferenceStore();
		if (ps != null) {
			this.fStructureCompareOnSingleClick = ps
					.getBoolean(ComparePreferencePage.OPEN_STRUCTURE_COMPARE);
		}
	}

	private boolean structureCompareOnSingleClick() {
		return this.fStructureCompareOnSingleClick;
	}

	/*
	 * (non Javadoc) see IAdaptable.getAdapter
	 */
	public Object getAdapter(final Class adapter) {
		if (ICompareNavigator.class.equals(adapter)
				|| CompareNavigator.class.equals(adapter)) {
			if (this.fNavigator == null) {
				this.fNavigator = new CompareNavigator(
						new CompareViewerSwitchingPane[] { this.fStructureInputPane,
								this.fStructurePane1, this.fStructurePane2,
								this.fContentInputPane });
			}
			return this.fNavigator;
		}
		if (IFile.class.equals(adapter)) {
			final IProgressMonitor pm = new NullProgressMonitor();
			// flush changes in any dirty viewer
			try {
				flushViewer(this.fStructureInputPane, pm);
				flushViewer(this.fStructurePane1, pm);
				flushViewer(this.fStructurePane2, pm);
				flushViewer(this.fContentInputPane, pm);
			} catch (final CoreException e) {
				CompareUIPlugin.log(e);
			}
			final IFile[] files = (IFile[]) getAdapter(IFile[].class);
			if ((files != null) && (files.length > 0)) {
				return files[0]; // can only return one: limitation on
			}
									// IDE.saveAllEditors; see #64617
			return null;
		}
		return null;
	}

	/*
	 * (non Javadoc) see IEditorInput.getImageDescriptor
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * (non Javadoc) see IEditorInput.getToolTipText
	 */
	public String getToolTipText() {
		return getTitle();
	}

	/*
	 * (non Javadoc) see IEditorInput.getName
	 */
	public String getName() {
		return getTitle();
	}

	/**
	 * Returns <code>null</code> since this editor cannot be persisted.
	 * 
	 * @return <code>null</code> because this editor cannot be persisted
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * Returns <code>false</code> to indicate that this input should not
	 * appear in the "File Most Recently Used" menu.
	 * 
	 * @return <code>false</code>
	 */
	public boolean exists() {
		return false;
	}

	/*
	 * FIXME!
	 */
	protected void setMessage(final String message) {
		this.fMessage = message;
	}

	/*
	 * FIXME!
	 */
	public String getMessage() {
		return this.fMessage;
	}

	/**
	 * Returns the title which will be used in the compare editor's title bar.
	 * It can be set with <code>setTitle</code>.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		if (this.fTitle == null) {
			return Utilities.getString("CompareEditorInput.defaultTitle"); //$NON-NLS-1$
		}
		return this.fTitle;
	}

	/**
	 * Sets the title which will be used when presenting the compare result.
	 * This method must be called before the editor is opened.
	 * 
	 * @param title
	 *            the title to use for the CompareEditor
	 */
	public void setTitle(final String title) {
		this.fTitle = title;
	}

	/**
	 * Returns the title image which will be used in the compare editor's title
	 * bar. Returns the title image which will be used when presenting the
	 * compare result. This implementation returns a generic compare icon.
	 * Subclasses can override.
	 * 
	 * @return the title image, or <code>null</code> if none
	 */
	public Image getTitleImage() {
		if (fgTitleImage == null) {
			fgTitleImage = CompareUIPlugin.getImageDescriptor(
					COMPARE_EDITOR_IMAGE_NAME).createImage();
			CompareUI.disposeOnShutdown(fgTitleImage);
		}
		return fgTitleImage;
	}

	/**
	 * Returns the configuration object for the viewers within the compare
	 * editor. Returns the configuration which was passed to the constructor.
	 * 
	 * @return the compare configuration
	 */
	public CompareConfiguration getCompareConfiguration() {
		return this.fCompareConfiguration;
	}

	/**
	 * Adds standard actions to the given <code>ToolBarManager</code>.
	 * <p>
	 * Subclasses may override to add their own actions.
	 * </p>
	 * 
	 * @param toolBarManager
	 *            the <code>ToolBarManager</code> to which to contribute
	 */
	public void contributeToToolBar(final ToolBarManager toolBarManager) {

		toolBarManager.add(new Separator());
		toolBarManager.add(this.fIgnoreWhitespace);
		toolBarManager.add(this.fShowPseudoConflicts);
	}

	/**
	 * Runs the compare operation and stores the compare result.
	 * 
	 * @param monitor
	 *            the progress monitor to use to display progress and receive
	 *            requests for cancelation
	 * @exception InvocationTargetException
	 *                if the <code>prepareInput</code> method must propagate a
	 *                checked exception, it should wrap it inside an
	 *                <code>InvocationTargetException</code>; runtime
	 *                exceptions are automatically wrapped in an
	 *                <code>InvocationTargetException</code> by the calling
	 *                context
	 * @exception InterruptedException
	 *                if the operation detects a request to cancel, using
	 *                <code>IProgressMonitor.isCanceled()</code>, it should
	 *                exit by throwing <code>InterruptedException</code>
	 */
	public void run(final IProgressMonitor monitor) throws InterruptedException,
			InvocationTargetException {
		this.fInput = prepareInput(monitor);
	}

	/**
	 * Runs the compare operation and returns the compare result. If
	 * <code>null</code> is returned no differences were found and no compare
	 * editor needs to be opened. Progress should be reported to the given
	 * progress monitor. A request to cancel the operation should be honored and
	 * acknowledged by throwing <code>InterruptedException</code>.
	 * <p>
	 * Note: this method is typically called in a modal context thread which
	 * doesn't have a Display assigned. Implementors of this method shouldn't
	 * therefore allocated any SWT resources in this method.
	 * </p>
	 * 
	 * @param monitor
	 *            the progress monitor to use to display progress and receive
	 *            requests for cancelation
	 * @return the result of the compare operation, or <code>null</code> if
	 *         there are no differences
	 * @exception InvocationTargetException
	 *                if the <code>prepareInput</code> method must propagate a
	 *                checked exception, it should wrap it inside an
	 *                <code>InvocationTargetException</code>; runtime
	 *                exceptions are automatically wrapped in an
	 *                <code>InvocationTargetException</code> by the calling
	 *                context
	 * @exception InterruptedException
	 *                if the operation detects a request to cancel, using
	 *                <code>IProgressMonitor.isCanceled()</code>, it should
	 *                exit by throwing <code>InterruptedException</code>
	 */
	protected abstract Object prepareInput(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException;

	/**
	 * Returns the compare result computed by the most recent call to the
	 * <code>run</code> method. Returns <code>null</code> if no differences
	 * were found.
	 * 
	 * @return the compare result prepared in method <code>prepareInput</code>
	 *         or <code>null</code> if there were no differences
	 */
	public Object getCompareResult() {
		return this.fInput;
	}

	/**
	 * Create the SWT controls that are used to display the result of the
	 * compare operation. Creates the SWT Controls and sets up the wiring
	 * between the individual panes. This implementation creates all four panes
	 * but makes only the necessary ones visible. Finally it feeds the compare
	 * result into the top left structure viewer and the content viewer.
	 * <p>
	 * Subclasses may override if they need to change the layout or wiring
	 * between panes.
	 * 
	 * @param parent
	 *            the parent control under which the control must be created
	 * @return the SWT control hierarchy for the compare editor
	 */
	public Control createContents(final Composite parent) {

		this.fComposite = new Splitter(parent, SWT.VERTICAL);
		this.fComposite.setData(this);

		Control outline = null;
		if (!this.fUseOutlineView) {
			outline = createOutlineContents(this.fComposite, SWT.HORIZONTAL);
		}

		this.fContentInputPane = new CompareViewerSwitchingPane(this.fComposite,
				SWT.BORDER | SWT.FLAT) {
			protected Viewer getViewer(final Viewer oldViewer, final Object input) {
				if (input instanceof ICompareInput) {
					return findContentViewer(oldViewer, (ICompareInput) input,
							this);
				}
				return null;
			}
		};
		if (this.fFocusPane == null) {
			this.fFocusPane = this.fContentInputPane;
		}
		if (outline != null) {
			this.fComposite.setVisible(outline, false);
		}
		this.fComposite.setVisible(this.fContentInputPane, true);

		if (this.fStructureInputPane != null) {
			this.fComposite.setWeights(new int[] { 30, 70 });
		}

		this.fComposite.layout();

		if ((this.fStructureInputPane != null) && (this.fInput instanceof ICompareInput)) {
			this.fStructureInputPane.setInput(this.fInput);
			final ISelection sel = this.fStructureInputPane.getSelection();
			if ((sel == null) || sel.isEmpty()) {
				feed1(sel); // we only feed downstream viewers if the top left
							// pane is empty
			}
		}

		this.fComposite.setData("Nav", //$NON-NLS-1$
				new CompareViewerSwitchingPane[] { this.fStructureInputPane,
						this.fStructurePane1, this.fStructurePane2, this.fContentInputPane });

		return this.fComposite;
	}

	/**
	 * @param parent
	 *            the parent control under which the control must be created
	 * @param direction
	 *            the layout direction of the contents, either </code>SWT.HORIZONTAL<code>
	 *            or </code>SWT.VERTICAL<code>
	 * @return the SWT control hierarchy for the outline part of the compare
	 *         editor
	 * @since 3.0
	 */
	public Control createOutlineContents(final Composite parent, final int direction) {
		final Splitter h = new Splitter(parent, direction);

		this.fStructureInputPane = new CompareViewerSwitchingPane(h, SWT.BORDER
				| SWT.FLAT, true) {
			protected Viewer getViewer(final Viewer oldViewer, final Object input) {
				if (input instanceof DiffNode) {
					final DiffNode dn = (DiffNode) input;
					if (dn.hasChildren()) {
						return createDiffViewer(this);
					}
				}
				if (input instanceof ICompareInput) {
					return findStructureViewer(oldViewer,
							(ICompareInput) input, this);
				}
				return null;
			}
		};
		this.fFocusPane = this.fStructureInputPane;

		this.fStructurePane1 = new CompareViewerSwitchingPane(h, SWT.BORDER
				| SWT.FLAT, true) {
			protected Viewer getViewer(final Viewer oldViewer, final Object input) {
				if (input instanceof ICompareInput) {
					return findStructureViewer(oldViewer,
							(ICompareInput) input, this);
				}
				return null;
			}
		};
		h.setVisible(this.fStructurePane1, false);

		this.fStructurePane2 = new CompareViewerSwitchingPane(h, SWT.BORDER
				| SWT.FLAT, true) {
			protected Viewer getViewer(final Viewer oldViewer, final Object input) {
				if (input instanceof ICompareInput) {
					return findStructureViewer(oldViewer,
							(ICompareInput) input, this);
				}
				return null;
			}
		};
		h.setVisible(this.fStructurePane2, false);

		// setup the wiring for top left pane
		this.fStructureInputPane.addOpenListener(new IOpenListener() {
			public void open(final OpenEvent oe) {
				feed1(oe.getSelection());
			}
		});
		this.fStructureInputPane
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(final SelectionChangedEvent e) {
						final ISelection s = e.getSelection();
						if ((s == null) || s.isEmpty()) {
							feed1(s);
						}
					}
				});
		this.fStructureInputPane.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				feedDefault1(event.getSelection());
			}
		});

		this.fStructurePane1
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(final SelectionChangedEvent e) {
						feed2(e.getSelection());
					}
				});

		this.fStructurePane2
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(final SelectionChangedEvent e) {
						feed3(e.getSelection());
					}
				});

		if (this.fUseOutlineView) {
			if (this.fInput instanceof ICompareInput) {
				this.fStructureInputPane.setInput(this.fInput);
				final ISelection sel = this.fStructureInputPane.getSelection();
				if ((sel == null) || sel.isEmpty()) {
					feed1(sel); // we only feed downstream viewers if the top
								// left pane is empty
				}
			}

			this.fComposite
					.setData("Nav", //$NON-NLS-1$
							new CompareViewerSwitchingPane[] {
									this.fStructureInputPane, this.fStructurePane1,
									this.fStructurePane2, this.fContentInputPane });
		}

		return h;
	}

	private void feed1(final ISelection selection) {
		BusyIndicator.showWhile(this.fComposite.getDisplay(), new Runnable() {
			public void run() {
				if ((selection == null) || selection.isEmpty()) {
					final Object input = CompareEditorInput.this.fStructureInputPane.getInput();
					CompareEditorInput.this.fContentInputPane.setInput(input);
					CompareEditorInput.this.fStructurePane2.setInput(null); // clear downstream pane
					CompareEditorInput.this.fStructurePane1.setInput(null);
				} else {
					final Object input = getElement(selection);
					CompareEditorInput.this.fContentInputPane.setInput(input);
					if (structureCompareOnSingleClick()) {
						CompareEditorInput.this.fStructurePane1.setInput(input);
					}
					CompareEditorInput.this.fStructurePane2.setInput(null); // clear downstream pane
					if (CompareEditorInput.this.fStructurePane1.getInput() != input) {
						CompareEditorInput.this.fStructurePane1.setInput(null);
					}
				}
			}
		});
	}

	private void feedDefault1(final ISelection selection) {
		BusyIndicator.showWhile(this.fComposite.getDisplay(), new Runnable() {
			public void run() {
				if (!selection.isEmpty()) {
					CompareEditorInput.this.fStructurePane1.setInput(getElement(selection));
				}
			}
		});
	}

	private void feed2(final ISelection selection) {
		BusyIndicator.showWhile(this.fComposite.getDisplay(), new Runnable() {
			public void run() {
				if (selection.isEmpty()) {
					final Object input = CompareEditorInput.this.fStructurePane1.getInput();
					CompareEditorInput.this.fContentInputPane.setInput(input);
					CompareEditorInput.this.fStructurePane2.setInput(null);
				} else {
					final Object input = getElement(selection);
					CompareEditorInput.this.fContentInputPane.setInput(input);
					CompareEditorInput.this.fStructurePane2.setInput(input);
				}
			}
		});
	}

	private void feed3(final ISelection selection) {
		BusyIndicator.showWhile(this.fComposite.getDisplay(), new Runnable() {
			public void run() {
				if (selection.isEmpty()) {
					CompareEditorInput.this.fContentInputPane.setInput(CompareEditorInput.this.fStructurePane2.getInput());
				} else {
					CompareEditorInput.this.fContentInputPane.setInput(getElement(selection));
				}
			}
		});

	}

	/**
	 * Returns the first element of the given selection if the selection is a
	 * <code>IStructuredSelection</code> with exactly one element. Returns
	 * <code>null</code> otherwise.
	 * 
	 * @param selection
	 *            the selection
	 * @return the first element of the selection, or <code>null</code>
	 */
	private static Object getElement(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				return ss.getFirstElement();
			}
		}
		return null;
	}

	/**
	 * Asks this input to take focus within its container (editor).
	 * <p>
	 * Clients should not call this method but they may override if they
	 * implement a different layout with different visual components. Clients
	 * are free to call the inherited method.
	 * </p>
	 */
	public void setFocus() {
		if (this.fFocusPane != null) {
			final Viewer v = this.fFocusPane.getViewer();
			if (v != null) {
				final Control c = v.getControl();
				if (c != null) {
					c.setFocus();
				}
			}
		} else if (this.fComposite != null) {
			this.fComposite.setFocus();
		}
	}

	/**
	 * Factory method for creating a differences viewer for the top left pane.
	 * It is called from <code>createContents</code> and returns a
	 * <code>DiffTreeViewer</code>.
	 * <p>
	 * Subclasses may override if they need a different viewer.
	 * </p>
	 * 
	 * @param parent
	 *            the SWT parent control under which to create the viewer's SWT
	 *            controls
	 * @return a compare viewer for the top left pane
	 */
	public Viewer createDiffViewer(final Composite parent) {
		return new DiffTreeViewer(parent, this.fCompareConfiguration);
	}

	/**
	 * Implements the dynamic viewer switching for structure viewers. The method
	 * must return a compare viewer based on the old (or current) viewer and a
	 * new input object. If the old viewer is suitable for showing the new input
	 * the old viewer can be returned. Otherwise a new viewer must be created
	 * under the given parent composite or <code>null</code> can be returned
	 * to indicate that no viewer could be found.
	 * <p>
	 * This implementation forwards the request to
	 * <code>CompareUI.findStructureViewer</code>.
	 * <p>
	 * Subclasses may override to implement a different strategy.
	 * </p>
	 * 
	 * @param oldViewer
	 *            a new viewer is only created if this old viewer cannot show
	 *            the given input
	 * @param input
	 *            the input object for which to find a structure viewer
	 * @param parent
	 *            the SWT parent composite under which the new viewer is created
	 * @return a compare viewer which is suitable for the given input object or
	 *         <code>null</code>
	 */
	public Viewer findStructureViewer(final Viewer oldViewer, final ICompareInput input,
			final Composite parent) {
		return CompareUI.findStructureViewer(oldViewer, input, parent,
				this.fCompareConfiguration);
	}

	/**
	 * Implements the dynamic viewer switching for content viewers. The method
	 * must return a compare viewer based on the old (or current) viewer and a
	 * new input object. If the old viewer is suitable for showing the new input
	 * the old viewer can be returned. Otherwise a new viewer must be created
	 * under the given parent composite or <code>null</code> can be returned
	 * to indicate that no viewer could be found.
	 * <p>
	 * This implementation forwards the request to
	 * <code>CompareUI.findContentViewer</code>.
	 * <p>
	 * Subclasses may override to implement a different strategy.
	 * </p>
	 * 
	 * @param oldViewer
	 *            a new viewer is only created if this old viewer cannot show
	 *            the given input
	 * @param input
	 *            the input object for which to find a structure viewer
	 * @param parent
	 *            the SWT parent composite under which the new viewer is created
	 * @return a compare viewer which is suitable for the given input object or
	 *         <code>null</code>
	 */
	public Viewer findContentViewer(final Viewer oldViewer, final ICompareInput input,
			final Composite parent) {

		final Viewer newViewer = CompareUI.findContentViewer(oldViewer, input,
				parent, this.fCompareConfiguration);

		final boolean isNewViewer = newViewer != oldViewer;
		if (DEBUG) {
			System.out
					.println("CompareEditorInput.findContentViewer: " + isNewViewer); //$NON-NLS-1$
		}

		if (isNewViewer && (newViewer instanceof IPropertyChangeNotifier)) {
			final IPropertyChangeNotifier dsp = (IPropertyChangeNotifier) newViewer;
			dsp.addPropertyChangeListener(this.fDirtyStateListener);

			final Control c = newViewer.getControl();
			c.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(final DisposeEvent e) {
					dsp.removePropertyChangeListener(CompareEditorInput.this.fDirtyStateListener);
				}
			});
		}

		return newViewer;
	}

	/**
	 * Returns <code>true</code> if there are unsaved changes. The value
	 * returned is the value of the <code>DIRTY_STATE</code> property of this
	 * input object.
	 * 
	 * Returns <code>true</code> if this input has unsaved changes, that is if
	 * <code>setDirty(true)</code> has been called. Subclasses don't have to
	 * override if the functionality provided by <code>setDirty</code> is
	 * sufficient.
	 * 
	 * @return <code>true</code> if there are changes that need to be saved
	 */
	public boolean isSaveNeeded() {
		return this.fDirty || (this.fDirtyViewers.size() > 0);
	}

	/**
	 * Sets the dirty state of this input to the given value and sends out a
	 * <code>PropertyChangeEvent</code> if the new value differs from the old
	 * value.
	 * 
	 * @param dirty
	 *            the dirty state for this compare input
	 */
	public void setDirty(final boolean dirty) {

		boolean confirmSave = true;
		final Object o = this.fCompareConfiguration
				.getProperty(CompareEditor.CONFIRM_SAVE_PROPERTY);
		if (o instanceof Boolean) {
			confirmSave = ((Boolean) o).booleanValue();
		}

		if (!confirmSave) {
			this.fDirty = dirty;
			if (!this.fDirty) {
				this.fDirtyViewers.clear();
			}
		}
	}

	private void setDirty(final Object source, final boolean dirty) {
		Assert.isNotNull(source);
		final boolean oldDirty = this.fDirtyViewers.size() > 0;
		if (dirty) {
			this.fDirtyViewers.add(source);
		} else {
			this.fDirtyViewers.remove(source);
		}
		final boolean newDirty = this.fDirty || (this.fDirtyViewers.size() > 0);
		if (DEBUG) {
			System.out
					.println("setDirty(" + source + ", " + dirty + "): " + newDirty); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (oldDirty != newDirty) {
			Utilities.firePropertyChange(this.fListenerList, this, DIRTY_STATE,
					new Boolean(oldDirty), new Boolean(newDirty));
		}
	}

	/*
	 * (non Javadoc) see IPropertyChangeNotifier.addListener
	 */
	public void addPropertyChangeListener(final IPropertyChangeListener listener) {
		if (listener != null) {
			this.fListenerList.add(listener);
		}
	}

	/*
	 * (non Javadoc) see IPropertyChangeNotifier.removeListener
	 */
	public void removePropertyChangeListener(final IPropertyChangeListener listener) {
		if (listener != null) {
			this.fListenerList.remove(listener);
		}
	}

	/**
	 * Save any unsaved changes. Empty implementation. Subclasses must override
	 * to save any changes.
	 * 
	 * @param pm
	 *            an <code>IProgressMonitor</code> that the implementation of
	 *            save may use to show progress
	 * @deprecated Override method saveChanges instead.
	 */
	public void save(final IProgressMonitor pm) {
		// empty default implementation
	}

	/**
	 * Save any unsaved changes. Subclasses must override to save any changes.
	 * This implementation tries to flush changes in all viewers by calling
	 * <code>ISavable.save</code> on them.
	 * 
	 * @param pm
	 *            an <code>IProgressMonitor</code> that the implementation of
	 *            save may use to show progress
	 * @throws CoreException
	 * @since 2.0
	 */
	public void saveChanges(final IProgressMonitor pm) throws CoreException {

		// flush changes in any dirty viewer
		flushViewer(this.fStructureInputPane, pm);
		flushViewer(this.fStructurePane1, pm);
		flushViewer(this.fStructurePane2, pm);
		flushViewer(this.fContentInputPane, pm);

		save(pm);
	}

	private static void flushViewer(final CompareViewerSwitchingPane pane,
			final IProgressMonitor pm) throws CoreException {
		if (pane != null) {
			final Viewer v = pane.getViewer();
			if (v instanceof ISavable) {
				((ISavable) v).save(pm);
			}
		}
	}
}
