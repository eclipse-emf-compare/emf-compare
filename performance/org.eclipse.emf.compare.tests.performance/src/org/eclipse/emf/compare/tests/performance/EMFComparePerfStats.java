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
package org.eclipse.emf.compare.tests.performance;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFComparePerfStats {

	private MemoryMXBean fMemoryBean;

	private MemoryUsage beforeMatchHeapMemoryUsage;

	private MemoryUsage afterMatchHeapMemoryUsage;

	private MemoryUsage beforeDiffHeapMemoryUsage;

	private MemoryUsage afterDiffHeapMemoryUsage;

	private final Stopwatch elapsedTimeInMatch;

	private final Stopwatch elapsedTimeInDiff;

	EMFComparePerfStats() {
		fMemoryBean = ManagementFactory.getMemoryMXBean();
		elapsedTimeInMatch = new Stopwatch();
		elapsedTimeInDiff = new Stopwatch();
	}

	void beforeMatch() {
		beforeMatchHeapMemoryUsage = fMemoryBean.getHeapMemoryUsage();
		elapsedTimeInMatch.start();
	}

	void afterMatch() {
		elapsedTimeInMatch.stop();
		afterMatchHeapMemoryUsage = fMemoryBean.getHeapMemoryUsage();
	}

	void beforeDiff() {
		beforeDiffHeapMemoryUsage = fMemoryBean.getHeapMemoryUsage();
		elapsedTimeInDiff.start();
	}

	void afterDiff() {
		elapsedTimeInDiff.stop();
		afterDiffHeapMemoryUsage = fMemoryBean.getHeapMemoryUsage();
	}

	public long elapsedTimeInMatch(TimeUnit unit) {
		return elapsedTimeInMatch.elapsedTime(unit);
	}

	public long usedHeapBeforeMatch(SizeUnit unit) {
		return unit.convert(beforeMatchHeapMemoryUsage.getUsed());
	}

	public long usedHeapAfterMatch(SizeUnit unit) {
		return unit.convert(afterMatchHeapMemoryUsage.getUsed());
	}

	public long elapsedTimeInDiff(TimeUnit unit) {
		return elapsedTimeInDiff.elapsedTime(unit);
	}

	public long usedHeapBeforeDiff(SizeUnit unit) {
		return unit.convert(beforeDiffHeapMemoryUsage.getUsed());
	}

	public long usedHeapAfterDiff(SizeUnit unit) {
		return unit.convert(afterDiffHeapMemoryUsage.getUsed());
	}

	public EMFComparePerfStats minus(EMFComparePerfStats stats) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("Match\n")
				.append("\tElapsed time: " + elapsedTimeInMatch(TimeUnit.MILLISECONDS) + " msec.\n")
				.append("\tUsed heap before: " + usedHeapBeforeMatch(SizeUnit.MEBI) + " MiB\n")
				.append("\tUsed heap after : " + usedHeapAfterMatch(SizeUnit.MEBI) + " MiB\n")
				.append("Diff\n")
				.append("\tElapsed time: " + elapsedTimeInDiff(TimeUnit.MILLISECONDS) + " msec.\n")
				.append("\tUsed heap before: " + usedHeapBeforeDiff(SizeUnit.MEBI) + " MiB\n")
				.append("\tUsed heap after : " + usedHeapAfterDiff(SizeUnit.MEBI) + " MiB\n");
		return sb.toString();
	}
}
