/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.Splitter;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
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

	/** {@link ModelInputSnapshot} result of the underlying comparison. */
	private final ComparisonSnapshot inputSnapshot;

	/**
	 * This constructor takes a {@link ModelInputSnapshot} as input.
	 * 
	 * @param snapshot
	 *            The {@link ModelInputSnapshot} loaded from an emfdiff.
	 */
	public ModelCompareEditorInput(ComparisonSnapshot snapshot) {
		super(new CompareConfiguration());
		inputSnapshot = snapshot;

		inputListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput source) {
				structureMergeViewer.setInput(source);
				contentMergeViewer.setInput(source);
			}
		};
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

		contentMergeViewer = new ModelContentMergeViewer(pane, getCompareConfiguration());
		pane.setContent(contentMergeViewer.getControl());

		contentMergeViewer.setInput(inputSnapshot);

		final int structureWeight = 30;
		final int contentWeight = 70;
		fComposite.setWeights(new int[] {structureWeight, contentWeight });

		return fComposite;
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

		structureMergeViewer.setInput(inputSnapshot);

		return splitter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CompareEditorInput#prepareInput(IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(IProgressMonitor monitor) {
		final ModelCompareInput input;
		if (inputSnapshot instanceof ComparisonResourceSnapshot) {
			final ComparisonResourceSnapshot snap = (ComparisonResourceSnapshot)inputSnapshot;
			resolveAll(snap);
			input = new ModelCompareInput(snap.getMatch(), snap.getDiff());
		} else {
			final ComparisonResourceSetSnapshot snap = (ComparisonResourceSetSnapshot)inputSnapshot;
			resolveAll(snap);
			input = new ModelCompareInput(snap.getMatchResourceSet(), snap.getDiffResourceSet());
		}
		input.addCompareInputChangeListener(inputListener);
		return input;
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
				if (reference == MatchPackage.eINSTANCE.getMatchModel_LeftRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_LeftRoots()) {
					leftReference = leftRS.getEObject(proxyURI, true);
				} else if (reference == MatchPackage.eINSTANCE.getMatchModel_RightRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_RightRoots()) {
					rightReference = rightRS.getEObject(proxyURI, true);
				} else if (reference == MatchPackage.eINSTANCE.getMatchModel_AncestorRoots()
						|| reference == DiffPackage.eINSTANCE.getDiffModel_AncestorRoots()) {
					ancestorReference = ancestorRS.getEObject(proxyURI, true);
				}

				final EObject resolvedReferencedObject;
				if (leftReference != null) {
					resolvedReferencedObject = leftReference;
				} else if (rightReference != null) {
					resolvedReferencedObject = rightReference;
				} else {
					resolvedReferencedObject = ancestorReference;
				}

				if (resolvedReferencedObject != null) {
					if (reference.isMany()) {
						final List<EObject> values = new ArrayList<EObject>();
						values.add(resolvedReferencedObject);
						object.eSet(reference, values);
					} else {
						object.eSet(reference, resolvedReferencedObject);
					}
					// We'll return false : we want our resolved object to be crossReferenced, not the proxy
					result = false;
					add((InternalEObject)object, reference, resolvedReferencedObject);
				} else {
					result = super.crossReference(object, reference, crossReferencedEObject);
				}
			} else {
				result = super.crossReference(object, reference, crossReferencedEObject);
			}
			return result;
		}
	}
}
