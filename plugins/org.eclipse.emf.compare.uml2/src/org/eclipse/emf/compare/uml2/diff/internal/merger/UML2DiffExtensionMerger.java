package org.eclipse.emf.compare.uml2.diff.internal.merger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.IMergeListener;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.merge.MergeEvent;
import org.eclipse.emf.compare.diff.merge.service.MergeFactory;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;

public class UML2DiffExtensionMerger extends DefaultMerger {

	private final Set<DiffElement> alreadyMerged = new HashSet<DiffElement>();

	public UML2DiffExtensionMerger() {
		MergeService.addMergeListener(new IMergeListener() {
			public void mergeOperationStart(MergeEvent event) {
			}

			public void mergeOperationEnd(MergeEvent event) {
			}

			public void mergeDiffStart(MergeEvent event) {
			}

			public void mergeDiffEnd(MergeEvent event) {
				alreadyMerged.addAll(event.getDifferences());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */

	@Override
	public void applyInOrigin() {
		final UMLDiffExtension extension = (UMLDiffExtension)this.diff;
		List<DiffElement> hideElements = new ArrayList<DiffElement>(extension.getHideElements());
		for (DiffElement hiddenElement : hideElements) {
			if (!alreadyMerged.contains(hiddenElement)) {
				final IMerger merger = MergeFactory.createMerger(hiddenElement);
				merger.applyInOrigin();
			}
		}
		removeFromContainer(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final UMLDiffExtension extension = (UMLDiffExtension)this.diff;
		List<DiffElement> hideElements = new ArrayList<DiffElement>(extension.getHideElements());
		for (DiffElement hiddenElement : hideElements) {
			if (!alreadyMerged.contains(hiddenElement)) {
				final IMerger merger = MergeFactory.createMerger(hiddenElement);
				merger.undoInTarget();
			}
		}
		removeFromContainer(diff);
	}
}
