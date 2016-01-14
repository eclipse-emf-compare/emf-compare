/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.conflict;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.ecore.EReference;

/**
 * Factory for ConflictSearch classes.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ConflictSearchFactory extends CompareSwitch<AbstractConflictSearch<? extends Diff>> {

	/**
	 * Index of reference changes by their target.
	 */
	private final ComparisonIndex index;

	/**
	 * The monitor that searches instantiated by this factory must use.
	 */
	private final Monitor monitor;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param monitor
	 *            The progress monitor
	 */
	public ConflictSearchFactory(Comparison comparison, Monitor monitor) {
		checkNotNull(comparison);
		this.monitor = checkNotNull(monitor);
		this.index = ComparisonIndex.index(comparison, monitor);
	}

	@Override
	public AbstractConflictSearch<AttributeChange> caseAttributeChange(AttributeChange diff) {
		switch (diff.getKind()) {
			case ADD:
				return new AttributeChangeConflictSearch.Add(diff, index, monitor);
			case CHANGE:
				return new AttributeChangeConflictSearch.Change(diff, index, monitor);
			case DELETE:
				return new AttributeChangeConflictSearch.Delete(diff, index, monitor);
			case MOVE:
				return new AttributeChangeConflictSearch.Move(diff, index, monitor);
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public AbstractConflictSearch<? extends Diff> caseFeatureMapChange(FeatureMapChange diff) {
		switch (diff.getKind()) {
			case ADD:
				return new FeatureMapChangeConflictSearch.Add(diff, index, monitor);
			case CHANGE:
				return new FeatureMapChangeConflictSearch.Change(diff, index, monitor);
			case DELETE:
				return new FeatureMapChangeConflictSearch.Delete(diff, index, monitor);
			case MOVE:
				return new FeatureMapChangeConflictSearch.Move(diff, index, monitor);
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public AbstractConflictSearch<? extends Diff> caseReferenceChange(ReferenceChange diff) {
		EReference ref = diff.getReference();
		checkNotNull(ref);
		if (ref.isContainment()) {
			return createContainmentSearch(diff);
		} else {
			return createNonContaimentSearch(diff);
		}
	}

	/**
	 * Create a Conflict Search for containment reference change.
	 * 
	 * @param diff
	 *            The containment {@link ReferenceChange}
	 * @return A conflict search for the given diff.
	 */
	private AbstractConflictSearch<? extends Diff> createContainmentSearch(ReferenceChange diff) {
		switch (diff.getKind()) {
			case ADD:
				return new ContainmentRefChangeConflictSearch.Add(diff, index, monitor);
			case CHANGE:
				return new ContainmentRefChangeConflictSearch.Change(diff, index, monitor);
			case DELETE:
				return new ContainmentRefChangeConflictSearch.Delete(diff, index, monitor);
			case MOVE:
				return new ContainmentRefChangeConflictSearch.Move(diff, index, monitor);
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Create a Conflict Search for non-containment reference change.
	 * 
	 * @param diff
	 *            The non-containment {@link ReferenceChange}
	 * @return A conflict search for the given diff.
	 */
	private AbstractConflictSearch<? extends Diff> createNonContaimentSearch(ReferenceChange diff) {
		switch (diff.getKind()) {
			case ADD:
				return new NonContainmentRefChangeConflictSearch.Add(diff, index, monitor);
			case CHANGE:
				return new NonContainmentRefChangeConflictSearch.Change(diff, index, monitor);
			case DELETE:
				return new NonContainmentRefChangeConflictSearch.Delete(diff, index, monitor);
			case MOVE:
				return new NonContainmentRefChangeConflictSearch.Move(diff, index, monitor);
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public AbstractConflictSearch<? extends Diff> caseResourceAttachmentChange(ResourceAttachmentChange diff) {
		switch (diff.getKind()) {
			case ADD:
				return new ResourceAttachmentChangeConflictSearch.Add(diff, index, monitor);
			case CHANGE:
				return new ResourceAttachmentChangeConflictSearch.Change(diff, index, monitor);
			case DELETE:
				return new ResourceAttachmentChangeConflictSearch.Delete(diff, index, monitor);
			case MOVE:
				return new ResourceAttachmentChangeConflictSearch.Move(diff, index, monitor);
			default:
				throw new IllegalArgumentException();
		}
	}

}
