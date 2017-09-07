/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 521948
 *******************************************************************************/
package org.eclipse.emf.compare.command.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * This command can be used to copy a number of diffs (or a single one) in a given direction.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public abstract class AbstractCopyCommand extends ChangeCommand implements ICompareCopyCommand {
	/** The list of differences we are to merge. */
	protected final List<? extends Diff> differences;

	/** Direction of the merge operation. */
	protected final boolean leftToRight;

	/** Merger registry. */
	protected final IMerger.Registry mergerRegistry;

	/**
	 * Constructs an instance of this command given the list of differences that it needs to merge.
	 * 
	 * @param changeRecorder
	 *            The change recorder associated to this command.
	 * @param notifiers
	 *            The collection of notifiers that will be notified of this command's execution.
	 * @param differences
	 *            The list of differences that this command should merge.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 */
	public AbstractCopyCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
			List<? extends Diff> differences, boolean leftToRight, IMerger.Registry mergerRegistry) {
		super(changeRecorder, notifiers);
		this.differences = ImmutableList.copyOf(differences);
		this.leftToRight = leftToRight;
		this.mergerRegistry = mergerRegistry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCopyCommand#isLeftToRight()
	 */
	public boolean isLeftToRight() {
		return leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects() {
		return differences;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return super.canExecute() && !differences.isEmpty();
	}

	/**
	 * Returns the state changes to any diffs that this command produced.
	 * 
	 * @return the state changes to any diffs that this command produced.
	 */
	public Multimap<DifferenceState, Diff> getChangedDiffs() {
		return getChangedDiffs(getChangeDescription(), differences);
	}

	/**
	 * Returns the state changes to any relevant diffs modified in the given change description.
	 * 
	 * @param changeDescription
	 *            the change description to process.
	 * @param relevantDiffs
	 *            the diffs for which we can state changes in the map.
	 * @return the state changes to any relevant diffs modified in the given change description.
	 */
	public static Multimap<DifferenceState, Diff> getChangedDiffs(ChangeDescription changeDescription,
			Collection<? extends Diff> relevantDiffs) {
		Multimap<DifferenceState, Diff> ret = LinkedHashMultimap.create();
		if (changeDescription != null) {
			for (Map.Entry<EObject, EList<FeatureChange>> entry : changeDescription.getObjectChanges()) {
				EObject key = entry.getKey();
				if (relevantDiffs.contains(key)) {
					for (FeatureChange featureChange : entry.getValue()) {
						if (featureChange.getFeature() == ComparePackage.Literals.DIFF__STATE) {
							Diff diff = (Diff)key;
							ret.put(diff.getState(), diff);
							break;
						}
					}
				}
			}
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		ChangeDescription changes = getChangeDescription();
		if (changes != null) {
			changes.getObjectsToAttach().clear();
		}

		super.dispose();
	}
}
