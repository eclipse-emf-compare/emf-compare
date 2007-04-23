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
package org.eclipse.emf.compare.ui.legacy.editor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.Splitter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.legacy.ModelCompareInput;
import org.eclipse.emf.compare.ui.legacy.contentmergeviewer.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.legacy.structuremergeviewer.ModelStructureMergeViewer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.MessageFormat;

public class ModelCompareEditorHistoryInput extends ModelCompareEditorInput {

	protected Object[] states;

	private TreeViewer versionTree;

	// private Map<Object, UMLComparisonEngine> statesEngine;
	// FIXME Add history support

	public ModelCompareEditorHistoryInput(final CompareConfiguration cc,
			final Object[] states, final IFile modelFile) {
		super(cc);
		this.merge = false;

		this.states = states;
		this.leftModelFile = modelFile;
		// statesEngine = new HashMap<Object, UMLComparisonEngine>();
	}

	@Override
	protected Object prepareInput(final IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
		this.rightModelFile = this.states[0];

		prepareEngine(monitor);
		final ResourceSet resourceSet = new ResourceSetImpl();
		ModelCompareInput input = null;
//		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI,
//				UMLPackage.eINSTANCE);
//		final Map extensionToFactoryMap = resourceSet.getResourceFactoryRegistry()
//				.getExtensionToFactoryMap();
//		extensionToFactoryMap.put(UMLResource.FILE_EXTENSION,
//				UMLResource.Factory.INSTANCE);
		EObject leftModel;
		try {
			leftModel = load(this.leftModelFile.getContents(), resourceSet);
			final EObject rightModel = load(((IFile) this.rightModelFile).getContents(),
					resourceSet);
			final MatchModel match = new MatchService()
					.doMatch(leftModel, rightModel,monitor);

			final DiffModel diff = new DiffService().doDiff(match);
			input = new ModelCompareInput(match, diff);

			// statesEngine.put(states[0],engine);
			input.addCompareInputChangeListener(this.inputListener);
			input.setLeftStorage(this.leftModelFile);
			input.setRightStorage(this.rightModelFile);
		} catch (final CoreException e) {
			EMFComparePlugin.getDefault().log(e,false);
		}
		return checkInputHasDiffs() ? input : null;
	}

	private EObject load(final InputStream in, final ResourceSet resourceSet) {
		final Resource resource = resourceSet.createResource(URI.createURI("left"));
		try {
			resource.load(in, Collections.EMPTY_MAP);
		} catch (final IOException e) {
			EMFComparePlugin.getDefault().log(e,false);
		}

		final EObject result = (EObject) ((resource.getContents().size() > 0) ? resource
				.getContents().get(0)
				: null);
		return result;
	}

	@Override
	public Control createContents(final Composite parent) {
		final Splitter comp = (Splitter) super.createContents(parent);
		comp.setWeights(new int[] { 30, 70 });
		if (this.versionTree != null) {
			this.versionTree.getTree().setSelection(
					this.versionTree.getTree().getItems()[0].getItems()[0]);
			this.versionTree.getTree().showSelection();
		}
		return comp;
	}

	public Control createOutlineContents(final Composite parent, final int direction) {
		final Splitter h = new Splitter(parent, direction);

		createVersionViewer(h);

		final CompareViewerPane pane = new CompareViewerPane(h, SWT.NONE);

		this.umlDiffViewer = new ModelStructureMergeViewer(pane,
				getCompareConfiguration());
		pane.setContent(this.umlDiffViewer.getTree());
		this.umlDiffViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(final SelectionChangedEvent event) {
						navigateToDelta(event.getSelection());
					}
				});
		this.umlDiffViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(final DoubleClickEvent event) {
				ModelCompareEditorHistoryInput.this.umlContentViewer
						.setSelectedPage(ModelContentMergeViewer.PROPERTIES_TAB);
				navigateToDelta(event.getSelection());
			}
		});

		final List factories = new ArrayList();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new DiffItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				factories);
		this.umlDiffViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		this.umlDiffViewer.setInput(getCompareResult());

		h.setWeights(new int[] { 30, 70 });

		return h;
	}

	/**
	 * @param h
	 * 
	 */
	protected void createVersionViewer(final Composite parent) {
		this.versionTree = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER | SWT.SINGLE);
		this.versionTree.setUseHashlookup(true);
		this.versionTree.setContentProvider(new VersionTreeContentProvider());
		this.versionTree.setLabelProvider(new VersionTreeLabelProvider());
		this.versionTree.setInput(this.states);
		this.versionTree
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(final SelectionChangedEvent event) {
						updateSelectedVersion(event.getSelection());

					}

				});

	}

	private class VersionTreeContentProvider implements ITreeContentProvider {
		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(final Object parentElement) {
			if (parentElement instanceof Date) {
				final List<Object> result = new ArrayList<Object>();
				for (final Object historyFragment : ModelCompareEditorHistoryInput.this.states) {
					final long ldate = ((IFileState) historyFragment)
							.getModificationTime();
					final long value = ldate - ((Date) parentElement).getTime()
							+ ((Date) parentElement).getTime() % (86400 * 1000);
					if ((value > 0) && (value < 86400 * 1000)) {
						result.add(historyFragment);
					}
				}
				return result.toArray();
			}
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(final Object element) {
			if (element instanceof IFileState) {
				final long ldate = ((IFileState) element).getModificationTime();
				final Date date = new Date(ldate);
				return date;
			}
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(final Object element) {
			if (element instanceof Date) {
				for (final Object historyFragment : ModelCompareEditorHistoryInput.this.states) {
					final long ldate = ((IFileState) historyFragment)
							.getModificationTime();

					final Date date = new Date(ldate);
					if (date.equals(element)) {
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof IFileState[]) {
				final Map<Object, Object> result = new HashMap<Object, Object>();
				for (final IFileState historyFragment : ((IFileState[]) inputElement)) {
					final long ldate = (historyFragment)
							.getModificationTime();
					final long day = dayNumber(ldate);
					final Date date = new Date(ldate);
					String df = DateFormat.getDateInstance().format(date);

					final long today = dayNumber(System.currentTimeMillis());

					String formatKey;
					if (day == today) {
						formatKey = "Today ({0})"; //$NON-NLS-1$
					} else if (day == today - 1) {
						formatKey = "Yesterday ({0})"; //$NON-NLS-1$
					} else {
						formatKey = "{0}"; //$NON-NLS-1$
					}

					df = MessageFormat.format(formatKey, new String[] { df });

					result.put(df, date);
				}
				return result.values().toArray();

			}
			return null;

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
		}

	}

	private class VersionTreeLabelProvider implements ILabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(final Object element) {
			// TODO add image support
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(final Object element) {
			if (element instanceof Date) {
				final Date date = (Date) element;
				String df = DateFormat.getDateInstance().format(date);
				final long day = dayNumber(date.getTime());
				final long today = dayNumber(System.currentTimeMillis());

				String formatKey;
				if (day == today) {
					formatKey = "Today ({0})"; //$NON-NLS-1$
				} else if (day == today - 1) {
					formatKey = "Yesterday ({0})"; //$NON-NLS-1$
				} else {
					formatKey = "{0}"; //$NON-NLS-1$
				}

				df = MessageFormat.format(formatKey, new String[] { df });
				return df;
			}
			if (element instanceof IFileState) {
				final IFileState state = (IFileState) element;
				final long ltime = state.getModificationTime();
				final String time = DateFormat.getTimeInstance().format(ltime);
				return time;
			}
			return element.toString();
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			// TODO Auto-generated method stub

		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
		 *      java.lang.String)
		 */
		public boolean isLabelProperty(final Object element, final String property) {
			// TODO Auto-generated method stub
			return false;
		}

		private final ListenerList labelProviderListeners = new ListenerList();

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(final ILabelProviderListener listener) {
			this.labelProviderListeners.add(listener);

		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(final ILabelProviderListener listener) {
			this.labelProviderListeners.remove(listener);

		}

	}

	/*
	 * Returns the number of s since Jan 1st, 1970. The given date is converted
	 * to GMT and daylight saving is taken into account too.
	 */
	private long dayNumber(final long date) {
		final int ONE_DAY_MS = 24 * 60 * 60 * 1000; // one day in milli seconds

		final Calendar calendar = Calendar.getInstance();
		final long localTimeOffset = calendar.get(Calendar.ZONE_OFFSET)
				+ calendar.get(Calendar.DST_OFFSET);

		return (date + localTimeOffset) / ONE_DAY_MS;
	}

	/**
	 * @return
	 */
	public Object getCurrentState() {
		return this.rightModelFile;
	}

	protected void updateSelectedVersion(final Object data) {
		if (data == null) {
			return;
		}
		if (data instanceof IStructuredSelection) {
			final Object first = ((IStructuredSelection) data)
					.getFirstElement();

			//
			// if (statesEngine.get((first)) != null)
			// {
			// setEngine(statesEngine.get((first)));
			// }
			// else
			// {
			// try
			// {
			// comparedModelFile = first;
			// prepareEngine(new NullProgressMonitor());
			// } catch(InterruptedException e)
			// {
			// return;
			// } catch (InvocationTargetException e) {
			// EMFComparePlugin.getDefault().log(e,false);
			// return;
			// }
			// statesEngine.put(first,engine);
			// comparedModelFile = first;
			// }

		}

	}
}
