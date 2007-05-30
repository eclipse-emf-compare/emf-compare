package org.eclipse.emf.compare.diff.generic.merge.impl;

import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * The merge factory build a merger from any kind of delta
 * 
 * @author Cedric Brun  cedric.brun@obeo.fr 
 * 
 */
public class DefaultMergeFactory extends MergeFactory {
	public AbstractMerger createMerger(DiffElement element) {
		if (element instanceof AddModelElement)
			return new AddModelElementMerger(element);

		if (element instanceof RemoveModelElement)
			return new RemoveModelElementMerger(element);

		if (element instanceof UpdateAttribute)
			return new UpdateAttributeMerger(element);

		if (element instanceof AddReferenceValue)
			return new AddReferenceValueMerger(element);

		if (element instanceof RemoveReferenceValue)
			return new RemoveReferenceValueMerger(element);

		return new AbstractMerger(element);
	}

}
