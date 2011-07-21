/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.editor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.Splitter;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.internal.ModelComparator;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This will be used as input for the CompareEditor used for the edition of emfdiff files.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelCompareEditorInput extends CompareEditorInput {
	/**
	 * Content merge viewer of this {@link org.eclipse.compare.CompareViewerPane}. It represents the bottom
	 * splitted part of the view.
	 */
	protected ModelContentMergeViewer contentMergeViewer;

	/** {@link ComparisonSnapshot} result of the underlying comparison. */
	protected final ComparisonSnapshot inputSnapshot;

	/** This is the input that will be used throughout. */
	protected ModelCompareInput preparedInput;

	/**
	 * Structure merge viewer of this {@link org.eclipse.compare.CompareViewerPane}. It represents the top
	 * TreeViewer of the view.
	 */
	protected ModelStructureMergeViewer structureMergeViewer;

	/**
	 * This listener will be in charge of updating the {@link ModelContentMergeViewer} and
	 * {@link ModelStructureMergeViewer}'s input.
	 */
	private final ICompareInputChangeListener inputListener;

	/**
	 * This constructor takes a {@link ComparisonSnapshot} as input.
	 * 
	 * @param snapshot
	 *            The {@link ComparisonSnapshot} loaded from an emfdiff.
	 */
	public ModelCompareEditorInput(ComparisonSnapshot snapshot) {
		super(new CompareConfiguration());
		inputSnapshot = snapshot;

		inputListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput source) {
				structureMergeViewer.setInput(source);
				contentMergeViewer.setInput(source);
				setDirty(true);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#saveChanges(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void saveChanges(IProgressMonitor monitor) {
		if (preparedInput.getLeftResource() != null) {
			final ResourceSet rs = preparedInput.getLeftResource().getResourceSet();
			if (rs == null)
				safeSave(preparedInput.getLeftResource());
			else
				for (Resource res : rs.getResources()) {
					safeSave(res);
				}
		}
		if (preparedInput.getRightResource() != null) {
			final ResourceSet rs = preparedInput.getRightResource().getResourceSet();
			if (rs == null)
				safeSave(preparedInput.getRightResource());
			else
				for (Resource res : rs.getResources()) {
					safeSave(res);
				}
		}
	}

	/**
	 * Saves the given resource while catching and logging potential exceptiions.
	 * 
	 * @param res
	 *            The resource that is to be saved.
	 */
	private void safeSave(Resource res) {
		try {
			res.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			EMFComparePlugin.log(e.getMessage(), false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CompareEditorInput#createContents(Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		final Splitter fComposite = new Splitter(parent, SWT.VERTICAL);

		createOutlineContents(fComposite, SWT.HORIZONTAL);

		final CompareViewerPane pane = new CompareViewerPane(fComposite, SWT.NONE);
		final CompareConfiguration compareConfig = getCompareConfiguration();

		// Delegates the merge viewer creation so that clients could override it
		contentMergeViewer = createMergeViewer(pane, compareConfig);
		pane.setContent(contentMergeViewer.getControl());

		contentMergeViewer.setInput(preparedInput);

		final int structureWeight = 30;
		final int contentWeight = 70;
		fComposite.setWeights(new int[] {structureWeight, contentWeight });

		return fComposite;
	}

	/**
	 * Creates and returns the {@link ModelContentMergeViewer merge viewer} constituting the content of this
	 * compare editor input. Clients may override this method in order to create their own merge viewer.
	 * 
	 * @param pane
	 *            The compare viewer pane to use as a parent composite for the viewer to create.
	 * @param config
	 *            The {@link CompareConfiguration compare configuration} to consider.
	 * @return The {@link ModelContentMergeViewer merge viewer} constituting the content of this compare
	 *         editor input.
	 * @since 1.1
	 */
	protected ModelContentMergeViewer createMergeViewer(CompareViewerPane pane, CompareConfiguration config) {
		return new ModelContentMergeViewer(pane, config);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CompareEditorInput#createOutlineContents(Composite, int)
	 */
	@Override
	public Control createOutlineContents(Composite parent, int direction) {
		final Splitter splitter = new Splitter(parent, direction);

		final CompareViewerPane pane = new CompareViewerPane(splitter, SWT.NONE);

		structureMergeViewer = new ModelStructureMergeViewer(pane, getCompareConfiguration());
		pane.setContent(structureMergeViewer.getTree());

		structureMergeViewer.setInput(preparedInput);

		return splitter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CompareEditorInput#prepareInput(IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(IProgressMonitor monitor) {
		resolveAll(inputSnapshot);
		preparedInput = createModelCompareInput(inputSnapshot);
		final ModelComparator comparator = ModelComparator.getComparator(getCompareConfiguration(),
				preparedInput);
		comparator.setComparisonResult(inputSnapshot);
		preparedInput.addCompareInputChangeListener(inputListener);
		return preparedInput;
	}

	/**
	 * Creates the ModelCompareInput for this editor input.
	 * 
	 * @param snap
	 *            Snapshot of the current comparison.
	 * @return The prepared ModelCompareInput for this editor input.
	 */
	protected ModelCompareInput createModelCompareInput(final ComparisonSnapshot snap) {
		if (snap instanceof ComparisonResourceSetSnapshot) {
			return new ModelCompareInput(((ComparisonResourceSetSnapshot)snap).getMatchResourceSet(),
					((ComparisonResourceSetSnapshot)snap).getDiffResourceSet());
		}
		return new ModelCompareInput(((ComparisonResourceSnapshot)snap).getMatch(),
				((ComparisonResourceSnapshot)snap).getDiff());
	}

	/**
	 * This will resolve all proxies of the given snapshot, dispatching references to two (three) distinct
	 * ResourceSets as needed for left, right (and ancestor) references.
	 * 
	 * @param snapshot
	 *            Snapshot which links are to be resolved.
	 */
	private void resolveAll(ComparisonSnapshot snapshot) {
		final ResourceSet left = new ResourceSetImpl();
		final ResourceSet right = new ResourceSetImpl();
		final ResourceSet ancestor = new ResourceSetImpl();

		// The cross referencer resolves all proxies by visiting them
		new DispatchingCrossReferencer(snapshot, left, right, ancestor);
	}

	/**
	 * This will allow us to resolve links towards the left, right and ancestor models in distinctive
	 * ResourceSets.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	final class DispatchingCrossReferencer extends CrossReferencer {
		/** Generated serial version UID. */
		private static final long serialVersionUID = -1062288900965446469L;

		/** ResourceSet where left references will be resolved. */
		private final ResourceSet leftRS;

		/** ResourceSet where right references will be resolved. */
		private final ResourceSet rightRS;

		/** ResourceSet where ancestor references will be resolved. */
		private final ResourceSet ancestorRS;

		/**
		 * Instantiates this cross referencer given the ResourceSets in which links are to be resolved.
		 * 
		 * @param snapshot
		 *            Snapshot containing the diff and match information.
		 * @param left
		 *            ResourceSet where left references are to be resolved.
		 * @param right
		 *            ResourceSet where right references are to be resolved.
		 * @param ancestor
		 *            ResourceSet where ancestor references are to be resolved.
		 */
		public DispatchingCrossReferencer(EObject snapshot, ResourceSet left, ResourceSet right,
				ResourceSet ancestor) {
			super(snapshot);
			leftRS = left;
			rightRS = right;
			ancestorRS = ancestor;
			crossReference();
		}

		/**
		 * We'll handle reference resolving ourselves in order to dispatch to distinct resourceSets.
		 * 
		 * @return <code>false</code> so as to deactivate proxy resolving.
		 */
		@Override
		protected boolean resolve() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer#crossReference(org.eclipse.emf.ecore.EObject,
		 *      org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject)
		 */
		@Override
		protected boolean crossReference(EObject object, EReference reference, EObject crossReferencedEObject) {
			boolean result;
			if (crossReferencedEObject.eIsProxy()) {
				final URI proxyURI = ((InternalEObject)crossReferencedEObject).eProxyURI();
				EObject leftReference = leftRS.getEObject(proxyURI, false);
				EObject rightReference = rightRS.getEObject(proxyURI, false);
				EObject ancestorReference = ancestorRS.getEObject(proxyURI, false);

				boolean resolved = false;
				if (reference == MatchPackage.eINSTANCE.getMatchModel_LeftRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_LeftRoots()) {
					leftReference = leftRS.getEObject(proxyURI, true);
					if (leftReference != null) {
						resolved = true;
						crossReferenceResolvedObject(object, reference, crossReferencedEObject, leftReference);
					}
				} else if (reference == MatchPackage.eINSTANCE.getMatchModel_RightRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_RightRoots()) {
					rightReference = rightRS.getEObject(proxyURI, true);
					if (rightReference != null) {
						resolved = true;
						crossReferenceResolvedObject(object, reference, crossReferencedEObject,
								rightReference);
					}
				} else if (reference == MatchPackage.eINSTANCE.getMatchModel_AncestorRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_AncestorRoots()) {
					ancestorReference = ancestorRS.getEObject(proxyURI, true);
					if (ancestorReference != null) {
						resolved = true;
						crossReferenceResolvedObject(object, reference, crossReferencedEObject,
								ancestorReference);
					}
				}

				if (resolved) {
					// We'll return false : our resolved object to be crossReferenced, the proxy must not be
					result = false;
				} else {
					result = super.crossReference(object, reference, crossReferencedEObject);
				}
			} else {
				result = super.crossReference(object, reference, crossReferencedEObject);
			}
			return result;
		}

		/**
		 * Cross references the given resolved object in this CrossReferencer's map.
		 * 
		 * @param object
		 *            an object in the cross referencer's content tree.
		 * @param reference
		 *            a reference from the object.
		 * @param proxy
		 *            The proxy object (will be removed from the list of referenced objects).
		 * @param resolvedReferencedObject
		 *            the resolved target of the specified reference.
		 */
		private void crossReferenceResolvedObject(EObject object, EReference reference, final EObject proxy,
				final EObject resolvedReferencedObject) {
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				final List<Object> oldValues = (List<Object>)object.eGet(reference);
				oldValues.remove(proxy);
				oldValues.add(resolvedReferencedObject);
			} else {
				object.eSet(reference, resolvedReferencedObject);
			}
			add((InternalEObject)object, reference, resolvedReferencedObject);
		}
	}
}
