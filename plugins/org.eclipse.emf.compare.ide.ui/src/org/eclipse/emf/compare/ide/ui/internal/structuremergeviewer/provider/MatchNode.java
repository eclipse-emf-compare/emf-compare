/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.EObjectNode;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;
import org.eclipse.emf.ecore.EObject;

/**
 * Specific AbstractEDiffNode for {@link Match} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchNode extends AbstractEDiffNode {

	/**
	 * 
	 */
	private static final Predicate<Diff> CONFLICTUAL_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict() != null;
		}
	};

	private static final Predicate<Diff> PSEUDO_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict().getKind() == ConflictKind.REAL;
		}
	};

	private static final Predicate<Diff> LEFT_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getSource() == DifferenceSource.LEFT;
		}
	};

	private static final Predicate<Diff> RIGHT_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getSource() == DifferenceSource.RIGHT;
		}
	};

	/**
	 * Creates a node with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public MatchNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Match getTarget() {
		return (Match)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getType()
	 */
	@Override
	public String getType() {
		return "eobject";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getAncestor()
	 */
	@Override
	public ITypedElement getAncestor() {
		EObject o = getTarget().getOrigin();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getLeft()
	 */
	@Override
	public ITypedElement getLeft() {
		EObject o = getTarget().getLeft();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getRight()
	 */
	@Override
	public ITypedElement getRight() {
		EObject o = getTarget().getRight();
		if (o != null) {
			return new EObjectNode(getAdapterFactory(), o);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getKind()
	 */
	@Override
	public int getKind() {
		int ret = super.getKind();

		final EObject ancestor = getTarget().getOrigin();
		final EObject left = getTarget().getLeft();
		final EObject right = getTarget().getRight();

		final Iterable<Diff> differences = getTarget().getAllDifferences();

		if (getTarget().getComparison().isThreeWay()) {
			Iterable<Diff> conflictualDiffs = filter(differences, CONFLICTUAL_DIFF);

			if (ancestor == null) {
				if (left == null) {
					if (right == null) {
						Assert.isTrue(false);
						// shouldn't happen
					} else {
						ret = Differencer.RIGHT | Differencer.ADDITION;
					}
				} else {
					if (right == null) {
						ret = Differencer.LEFT | Differencer.ADDITION;
					} else {
						if (!isEmpty(conflictualDiffs)) {
							ret = Differencer.CONFLICTING | Differencer.ADDITION;
							if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
								ret |= Differencer.PSEUDO_CONFLICT;
							}
						}
					}
				}
			} else {
				if (left == null) {
					if (right == null) {
						ret = Differencer.CONFLICTING | Differencer.DELETION | Differencer.PSEUDO_CONFLICT;
					} else {
						if (isEmpty(conflictualDiffs)) {
							ret = Differencer.LEFT | Differencer.DELETION;
						} else {
							if (!isEmpty(conflictualDiffs)) {
								ret = Differencer.CONFLICTING | Differencer.DELETION;
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									ret |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					}
				} else {
					if (right == null) {
						if (isEmpty(conflictualDiffs)) {
							ret = Differencer.RIGHT | Differencer.DELETION;
						} else {
							if (!isEmpty(conflictualDiffs)) {
								ret = Differencer.CONFLICTING | Differencer.CHANGE;
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									ret |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					} else {
						boolean ay = isEmpty(filter(differences, LEFT_DIFF));
						boolean am = isEmpty(filter(differences, RIGHT_DIFF));

						if (isEmpty(differences)) {
							// empty
						} else if (ay && !am) {
							ret = Differencer.RIGHT | Differencer.CHANGE;
						} else if (!ay && am) {
							ret = Differencer.LEFT | Differencer.CHANGE;
						} else {
							if (!isEmpty(conflictualDiffs)) {
								ret = Differencer.CONFLICTING | Differencer.CHANGE;
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									ret |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					}
				}
			}
		} else { // two way compare ignores ancestor
			if (left == null) {
				if (right == null) {
					Assert.isTrue(false);
					// shouldn't happen
				} else {
					ret = Differencer.ADDITION;
				}
			} else {
				if (right == null) {
					ret = Differencer.DELETION;
				} else {
					if (!isEmpty(differences)) {
						ret = Differencer.CHANGE;
					}
				}
			}
		}
		return ret;
	}
}
