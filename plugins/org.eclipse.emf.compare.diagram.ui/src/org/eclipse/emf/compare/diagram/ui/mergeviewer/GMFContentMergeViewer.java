/*******************************************************************************
 * Copyright (c) 2009, 2011 Tobias Jaehnel and Others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Tobias Jaehnel - Bug#241385
 *   Obeo - rework on generic gmf comparison
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ui.mergeviewer;

import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.emf.compare.diagram.ui.viewmodel.NotationDiffCreator;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.TypedElementWrapper;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeContentProvider;
import org.eclipse.emf.compare.ui.viewer.content.ParameterizedContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * The content viewer for gmf comparison. Displays two diagrams, with diff annotations.
 * 
 * @author <a href="mailto:tjaehnel@gmail.com">Tobias Jaehnel</a>
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class GMFContentMergeViewer extends ParameterizedContentMergeViewer {

	/** the modelcreator used to annotate models. */
	private final NotationDiffCreator gmfModelCreator = new NotationDiffCreator();

	/** The transactionnal Editing Domain used to annotate/deannotate left diagrams. */
	private TransactionalEditingDomain leftTED;

	/** The transactionnal Editing Domain used to annotate/deannotate right diagrams. */
	private TransactionalEditingDomain rightTED;

	/** The transactionnal Editing Domain used to display ancestor diagrams. */
	private TransactionalEditingDomain ancestorTED;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent Composite.
	 * @param config
	 *            the Compare configuration.
	 */
	protected GMFContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(parent, config);
	}

	@Override
	public String getTitle() {
		return "Visualization of graphical differences";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ParameterizedContentMergeViewer#createModelContentMergeTabFolder(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	@Override
	protected ModelContentMergeTabFolder createModelContentMergeTabFolder(Composite composite, int side) {
		return new GMFContentMergeTabFolder(this, composite, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		Object ancestorObject = ancestor;
		Object leftObject = left;
		Object rightObject = right;

		if (ancestorObject instanceof TypedElementWrapper) {
			if (((TypedElementWrapper)ancestorObject).getObject() == null) {
				ancestorObject = null;
			} else {
				ancestorObject = getInputObject((TypedElementWrapper)ancestorObject);
			}
		}
		if (leftObject instanceof TypedElementWrapper) {
			leftObject = getInputObject((TypedElementWrapper)leftObject);
		}
		if (rightObject instanceof TypedElementWrapper) {
			rightObject = getInputObject((TypedElementWrapper)rightObject);
		}

		if (ancestorObject != null) {
			ancestorTED = EditingDomainUtils.getOrCreateEditingDomain(ancestorObject);
		}
		if (leftObject != null) {
			leftTED = EditingDomainUtils.getOrCreateEditingDomain(leftObject);
			annotateSide(MatchSide.LEFT);
		}

		if (rightObject != null) {
			rightTED = EditingDomainUtils.getOrCreateEditingDomain(rightObject);
			annotateSide(MatchSide.RIGHT);
		}

		super.updateContent(ancestor, left, right);
	}

	/**
	 * Execute a RecordingCommand to annotate the {@link #getInput() input} of this viewer. Only the given
	 * <code>side</code> is annotated.
	 * 
	 * @param side The side to annotate.
	 */
	private void annotateSide(final MatchSide side) {
		TransactionalEditingDomain ted = null;
		if (side == MatchSide.LEFT) {
			ted = leftTED;
		} else {
			ted = rightTED;
		}

		// annotate models
		final RecordingCommand command = new RecordingCommand(ted) {

			protected void doExecute() {
				if (gmfModelCreator != null) {
					gmfModelCreator.setInput(getInput());
					gmfModelCreator.addEAnnotations(side);
				}
			}
		};
		ted.getCommandStack().execute(command);
	}

	/**
	 * Execute a RecordingCommand to annotate the {@link #getInput() input} of this viewer. Only the given
	 * <code>side</code> is annotated.
	 * 
	 * @param side The side to annotate.
	 */
	private void unnannotateSide(final MatchSide side) {
		TransactionalEditingDomain ted = null;
		if (side == MatchSide.LEFT) {
			ted = leftTED;
		} else {
			ted = rightTED;
		}

		// annotate models
		final RecordingCommand command = new RecordingCommand(ted) {

			protected void doExecute() {
				if (gmfModelCreator != null) {
					gmfModelCreator.setInput(getInput());
					gmfModelCreator.removeEAnnotations(side);
				}
			}
		};
		ted.getCommandStack().execute(command);
	}

	/**
	 * Returns the notationDiffCreator used to annotate the gmf models.
	 * 
	 * @return the notationDiffCreator used to annotate the gmf models.
	 */
	public NotationDiffCreator getModelCreator() {
		return gmfModelCreator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#copy(java.util.List, boolean)
	 */
	@Override
	protected void copy(final List<DiffElement> diffs, final boolean leftToRight) {
		unnannotateSide(MatchSide.LEFT);
		unnannotateSide(MatchSide.RIGHT);
		super.copy(diffs, leftToRight);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(final boolean leftToRight) {
		unnannotateSide(MatchSide.LEFT);
		unnannotateSide(MatchSide.RIGHT);
		super.copy(leftToRight);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		// needed when switching from diagram to text
		if (leftTED != null)
			leftTED.dispose();
		if (rightTED != null)
			rightTED.dispose();
		// needed if not using 3 way comparison
		if (ancestorTED != null)
			ancestorTED.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer#createMergeViewerContentProvider()
	 */
	@Override
	protected IMergeViewerContentProvider createMergeViewerContentProvider() {
		// CHECKSTYLE:OFF
		return new ModelContentMergeContentProvider(configuration) {
			// CHECKSTYLE:ON
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeContentProvider#saveLeftContent(java.lang.Object,
			 *      byte[])
			 */
			@Override
			public void saveLeftContent(Object element, byte[] bytes) {
				unnannotateSide(MatchSide.LEFT);
				unnannotateSide(MatchSide.RIGHT);
				super.saveLeftContent(element, bytes);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeContentProvider#saveRightContent(java.lang.Object,
			 *      byte[])
			 */
			@Override
			public void saveRightContent(Object element, byte[] bytes) {
				unnannotateSide(MatchSide.LEFT);
				unnannotateSide(MatchSide.RIGHT);
				super.saveRightContent(element, bytes);
			}
		};
	}
}
