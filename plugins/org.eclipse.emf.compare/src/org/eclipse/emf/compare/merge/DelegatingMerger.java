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
package org.eclipse.emf.compare.merge;

import com.google.common.base.Preconditions;

import java.util.Map;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;

/**
 * Wrapper of mergers that makes sure that a given {@link IMergeCriterion} is passed to the wrapped merger
 * before calling its merge method, and that the former value of criterion used by the wrapped merger is
 * restored afterwards.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.4
 */
public class DelegatingMerger {

	/** The wrapped merger, cannot be <code>null</code>. */
	private final IMerger merger;

	/** The criterion to use, can be <code>null</code>. */
	private final IMergeCriterion criterion;

	/**
	 * Constructor.
	 * 
	 * @param merger
	 *            The merger, cannot be <code>null</code>
	 * @param criterion
	 *            The criterion, can be <code>null</code>
	 */
	public DelegatingMerger(IMerger merger, IMergeCriterion criterion) {
		this.merger = Preconditions.checkNotNull(merger);
		this.criterion = criterion;
	}

	/**
	 * Call copyRightToLeft on the wrapped merger with the right criterion.
	 * 
	 * @param target
	 *            the diff
	 * @param monitor
	 *            the monitor
	 */
	public void copyRightToLeft(Diff target, Monitor monitor) {
		if (merger instanceof IMergeOptionAware) {
			Map<Object, Object> options = ((IMergeOptionAware)merger).getMergeOptions();
			Object oldCriterion = options.get(IMergeCriterion.OPTION_MERGE_CRITERION);
			try {
				options.put(IMergeCriterion.OPTION_MERGE_CRITERION, criterion);
				merger.copyRightToLeft(target, monitor);
			} finally {
				((IMergeOptionAware)merger).getMergeOptions().put(IMergeCriterion.OPTION_MERGE_CRITERION,
						oldCriterion);
			}
		} else {
			merger.copyRightToLeft(target, monitor);
		}
	}

	/**
	 * Call copyLeftToRight on the wrapped merger with the right criterion.
	 * 
	 * @param target
	 *            the diff
	 * @param monitor
	 *            the monitor
	 */
	public void copyLeftToRight(Diff target, Monitor monitor) {
		if (merger instanceof IMergeOptionAware) {
			Map<Object, Object> options = ((IMergeOptionAware)merger).getMergeOptions();
			Object oldCriterion = options.get(IMergeCriterion.OPTION_MERGE_CRITERION);
			try {
				options.put(IMergeCriterion.OPTION_MERGE_CRITERION, criterion);
				merger.copyLeftToRight(target, monitor);
			} finally {
				((IMergeOptionAware)merger).getMergeOptions().put(IMergeCriterion.OPTION_MERGE_CRITERION,
						oldCriterion);
			}
		} else {
			merger.copyLeftToRight(target, monitor);
		}
	}

	public IMerger getMerger() {
		return merger;
	}

	public IMergeCriterion getCriterion() {
		return criterion;
	}

}
