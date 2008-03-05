/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompareException;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.metamodel.util.DiffAdapterFactory;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.api.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;

/**
 * Structure viewer used by the
 * {@link org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer ModelStructureMergeViewer}.
 * Assumes that its input is a {@link DiffModel}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ModelStructureContentProvider implements ITreeContentProvider {
	/** Resource of the ancestor model used in this comparison. */
	protected Resource ancestorResource;

	/** Indicates that this is a three way comparison. */
	protected boolean isThreeWay;

	/** Resource of the left model used in this comparison. */
	protected Resource leftResource;

	/** Resource of the right model used in this comparison. */
	protected Resource rightResource;

	/** Keeps track of the comparison result. */
	/* package */ModelInputSnapshot snapshot;

	/**
	 * {@link CompareConfiguration} controls various aspect of the GUI elements. This will keep track of the
	 * one used to created this compare editor.
	 */
	private final CompareConfiguration configuration;

	/**
	 * {@link DiffModel} result of the underlying comparison. This contains the data for this content
	 * provider.
	 */
	private DiffModel diffInput;

	/**
	 * Instantiates a content provider given the {@link CompareConfiguration} to use.
	 * 
	 * @param compareConfiguration
	 *            {@link CompareConfiguration} used for this comparison.
	 */
	public ModelStructureContentProvider(CompareConfiguration compareConfiguration) {
		configuration = compareConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing needs to be disposed of here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentElement) {
		Object[] children = null;
		if (parentElement instanceof EObject) {
			final Collection<EObject> childrenList = new ArrayList<EObject>();
			for (EObject child : ((EObject)parentElement).eContents()) {
				if (!DiffAdapterFactory.shouldBeHidden(child))
					childrenList.add(child);
			}
			children = childrenList.toArray();
		}
		return children;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		Object[] elements = null;
		if (inputElement instanceof DiffModel) {
			elements = ((DiffModel)inputElement).getOwnedElements().toArray();
		} else {
			try {
				elements = diffInput.getOwnedElements().toArray();
			} catch (NullPointerException e) {
				throw new EMFCompareException(EMFCompareUIMessages
						.getString("ModelStructureContentProvider.inputException")); //$NON-NLS-1$
			}
		}
		return elements;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		Object parent = null;
		if (element instanceof EObject) {
			parent = ((EObject)element).eContainer();
		}
		return parent;
	}

	/**
	 * Returns this content provider's input.
	 * 
	 * @return This content provider's input.
	 */
	public ModelInputSnapshot getSnapshot() {
		return snapshot;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {
		boolean hasChildren = false;
		if (element instanceof EObject) {
			hasChildren = !((EObject)element).eContents().isEmpty();
		}
		return hasChildren;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		((TreeViewer)viewer).getTree().clearAll(true);
		if (newInput instanceof ModelInputSnapshot) {
			snapshot = (ModelInputSnapshot)newInput;
			diffInput = snapshot.getDiff();
		} else if (configuration.getProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT) != null) {
			snapshot = (ModelInputSnapshot)configuration
					.getProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT);
			diffInput = snapshot.getDiff();
		} else if (newInput instanceof ICompareInput) {
			final ITypedElement left = ((ICompareInput)newInput).getLeft();
			final ITypedElement right = ((ICompareInput)newInput).getRight();
			final ITypedElement ancestor = ((ICompareInput)newInput).getAncestor();

			if (ancestor != null)
				isThreeWay = true;

			prepareComparison(left, right, ancestor);
			doCompare();
		}
	}

	/**
	 * Handles the comparison (either two or three ways) of the models.
	 */
	protected void doCompare() {
		try {
			final Date start = Calendar.getInstance().getTime();
			snapshot = DiffFactory.eINSTANCE.createModelInputSnapshot();

			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InterruptedException {
					final Map<String, Object> options = new EMFCompareMap<String, Object>();
					options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);
					final MatchModel match;
					if (!isThreeWay)
						match = MatchService.doResourceMatch(leftResource, rightResource, options);
					else
						match = MatchService.doResourceMatch(leftResource, rightResource, ancestorResource,
								options);
					final DiffModel diff = DiffService.doDiff(match, isThreeWay);

					snapshot.setDate(Calendar.getInstance().getTime());
					snapshot.setDiff(diff);
					snapshot.setMatch(match);
				}
			});

			final Date end = Calendar.getInstance().getTime();
			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_TIME, end.getTime()
					- start.getTime());

			diffInput = snapshot.getDiff();
		} catch (InterruptedException e) {
			throw new EMFCompareException(e.getMessage());
		} catch (InvocationTargetException e) {
			EMFComparePlugin.log(e, true);
		}
	}

	/**
	 * Handles all the loading operations for the three models we need.
	 * 
	 * @param left
	 *            Left resource (either local or remote) to load.
	 * @param right
	 *            Right resource (either local or remote) to load.
	 * @param ancestor
	 *            Ancestor resource (either local or remote) to load.
	 */
	protected void prepareComparison(ITypedElement left, ITypedElement right, ITypedElement ancestor) {
		try {
			final ResourceSet modelResourceSet = new ResourceSetImpl();
			if (left instanceof ResourceNode && right instanceof ResourceNode) {
				leftResource = ModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
						modelResourceSet).eResource();
				rightResource = ModelUtils.load(((ResourceNode)right).getResource().getFullPath(),
						modelResourceSet).eResource();
				if (isThreeWay)
					ancestorResource = ModelUtils.load(((ResourceNode)ancestor).getResource().getFullPath(),
							modelResourceSet).eResource();
			} else if (left instanceof ResourceNode && right instanceof IStreamContentAccessor) {
				// this is the case of SVN/CVS comparison, we invert left and
				// right.
				if (((ResourceNode)left).getResource().isAccessible())
					rightResource = ModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
							modelResourceSet).eResource();
				else
					rightResource = ModelUtils.createResource(URI.createPlatformResourceURI(
							((ResourceNode)left).getResource().getFullPath().toOSString(), true));
				leftResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right.getName(), modelResourceSet).eResource();
				configuration.setRightLabel(EMFCompareUIMessages.getString("comparison.label.localResource")); //$NON-NLS-1$
				configuration.setLeftLabel(EMFCompareUIMessages.getString("comparison.label.remoteResource")); //$NON-NLS-1$
				configuration.setLeftEditable(false);
				configuration.setProperty(EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE, true);
				if (isThreeWay)
					ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
							ancestor.getName(), modelResourceSet).eResource();
			} else if (left instanceof IStreamContentAccessor && right instanceof IStreamContentAccessor) {
				// This can happen with some SVN plug-ins
				rightResource = ModelUtils.load(((IStreamContentAccessor)left).getContents(), left.getName(),
						modelResourceSet).eResource();
				leftResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right.getName(), modelResourceSet).eResource();
				configuration.setRightLabel(EMFCompareUIMessages.getString("comparison.label.localResource")); //$NON-NLS-1$
				configuration.setLeftLabel(EMFCompareUIMessages.getString("comparison.label.remoteResource")); //$NON-NLS-1$
				configuration.setLeftEditable(false);
				configuration.setProperty(EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE, true);
				if (isThreeWay)
					ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
							ancestor.getName(), modelResourceSet).eResource();
			}
		} catch (IOException e) {
			throw new EMFCompareException(e);
		} catch (CoreException e) {
			throw new EMFCompareException(e);
		}
	}
}
