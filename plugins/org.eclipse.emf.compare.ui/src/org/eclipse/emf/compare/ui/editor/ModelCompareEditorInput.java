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
package org.eclipse.emf.compare.ui.editor;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.Splitter;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This will be used as input for the {@link CompareEditor} used for the edition of emfdiff files.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelCompareEditorInput extends CompareEditorInput {
	/**
	 * Content merge viewer of this {@link CompareViewerPane}. It represents the bottom splitted part of the
	 * view.
	 */
	protected ModelContentMergeViewer contentMergeViewer;

	/**
	 * Structure merge viewer of this {@link CompareViewerPane}. It represents the top {@link TreeViewer} of
	 * the view.
	 */
	protected ModelStructureMergeViewer structureMergeViewer;

	/** {@link DiffModel} result of the underlying comparison. */
	private final DiffModel diff;

	/**
	 * This listener will be in charge of updating the {@link ModelContentMergeViewer} and
	 * {@link ModelStructureMergeViewer}'s input.
	 */
	private final ICompareInputChangeListener inputListener;

	/** {@link ModelInputSnapshot} result of the underlying comparison. */
	private final ModelInputSnapshot inputSnapshot;

	/** {@link MatchModel} result of the underlying comparison. */
	private final MatchModel match;

	/**
	 * This constructor takes a {@link ModelInputSnapshot} as input.
	 * 
	 * @param snapshot
	 *            The {@link ModelInputSnapshot} loaded from an emfdiff.
	 */
	public ModelCompareEditorInput(ModelInputSnapshot snapshot) {
		super(new CompareConfiguration());
		diff = snapshot.getDiff();
		match = snapshot.getMatch();
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
		fComposite.setWeights(new int[] {structureWeight, contentWeight});

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
		final ModelCompareInput input = new ModelCompareInput(match, diff);
		input.addCompareInputChangeListener(inputListener);
		return input;
	}
}
