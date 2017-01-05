/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.message.bug507177;

import static org.eclipse.emf.compare.merge.AbstractMerger.SUB_DIFF_AWARE_OPTION;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.junit.runner.RunWith;

/**
 * Tests that equivalences are handled correctly when a message is added to an interaction.
 * 
 * @author <a href="mailto:mfleck@eclipsesource.com">Martin Fleck</a>
 */
@RunWith(RuntimeTestRunner.class)
public class AddMessageSubDiffTest {
	private static final IMerger.Registry MERGER_REGISTRY = EMFCompareRCPPlugin.getDefault()
			.getMergerRegistry();

	private static final Map<IMergeOptionAware, Object> CACHED_OPTIONS = Maps.newHashMap();

	public void enableCascadingFilter() {
		setCascadingFilter(true);
	}

	public void disableCascadingFilter() {
		setCascadingFilter(false);
	}

	public void setCascadingFilter(boolean enabled) {
		for (IMergeOptionAware merger : Iterables.filter(MERGER_REGISTRY.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			Object previousValue = mergeOptions.get(SUB_DIFF_AWARE_OPTION);
			CACHED_OPTIONS.put(merger, previousValue);
			mergeOptions.put(SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enabled));
		}
	}

	public void restoreCascadingFilter() {
		// restore previous values
		for (Entry<IMergeOptionAware, Object> entry : CACHED_OPTIONS.entrySet()) {
			IMergeOptionAware merger = entry.getKey();
			merger.getMergeOptions().put(SUB_DIFF_AWARE_OPTION, entry.getValue());
		}
	}

	/**
	 * Test that equivalent diffs are not merged on both sides when sub-diffs are considered during the merge
	 * process, i.e., the cascading filter is enabled. In this test case, a single message is added.
	 * 
	 * @param comparison
	 */
	@Compare(left = "data/left.uml", right = "data/right.uml")
	public void testNoDuplicateAddition(final Comparison comparison) {
		try {
			enableCascadingFilter();
			final IBatchMerger merger = new BatchMerger(MERGER_REGISTRY);
			merger.copyAllLeftToRight(comparison.getDifferences(), new BasicMonitor());
			// if no exception is thrown during merge, we are happy
		} finally {
			restoreCascadingFilter();
		}
	}

}
