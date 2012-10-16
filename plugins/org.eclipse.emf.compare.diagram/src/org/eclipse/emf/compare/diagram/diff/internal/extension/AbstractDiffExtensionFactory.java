/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diff.internal.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.ecore.EObject;

/**
 * Factory for the difference extensions.
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	private CompareSwitch<DifferenceKind> differenceKindCompareSwitch = new CompareSwitch<DifferenceKind>() {

		@Override
		public DifferenceKind caseAttributeChange(AttributeChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseAttributeChange(object);
			}
		}

		@Override
		public DifferenceKind caseReferenceChange(ReferenceChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseReferenceChange(object);
			}
		}

		@Override
		public DifferenceKind caseResourceAttachmentChange(ResourceAttachmentChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseResourceAttachmentChange(object);
			}
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return getRelatedExtensionKind(input) != null && !isDiffOnAddOrDelete(input);
	}

	public Match getParentMatch(Diff input) {
		return input.getMatch();
	}

	protected boolean isDiffOnAddOrDelete(Diff input) {
		final Match match = input.getMatch();
		final EObject container = match.eContainer();
		if (container instanceof Match) {
			final Iterator<Diff> diffs = ((Match)container).getAllDifferences().iterator();
			while (diffs.hasNext()) {
				final Diff diff = (Diff)diffs.next();
				if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()
						&& match.getComparison().getMatch(((ReferenceChange)diff).getValue()) == match) {
					return true;
				}
			}
		}
		return false;
	}

	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		return differenceKindCompareSwitch.doSwitch(input);
	}

	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionAdd(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(ResourceAttachmentChange input) {
		return false;
	}

	public Class<? extends Diff> getExtensionKind() {
		return Diff.class;
	}

	protected List<Diff> getAllContainedDifferences(ReferenceChange input) {
		final List<Diff> result = new ArrayList<Diff>();
		final Iterator<Diff> diffs = input.getMatch().getComparison().getMatch(input.getValue())
				.getAllDifferences().iterator();
		while (diffs.hasNext()) {
			Diff diff = (Diff)diffs.next();
			result.add(diff);
		}
		return result;
	}

	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		// TODO Auto-generated method stub

	}

}
