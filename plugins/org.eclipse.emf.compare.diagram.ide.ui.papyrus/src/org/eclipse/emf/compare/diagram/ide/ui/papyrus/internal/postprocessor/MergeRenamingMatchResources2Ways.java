/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor;

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Treatment that merges MatchResource instances that represent implicit renames managed by Papyrus for 2-way
 * comparisons only.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.4
 */
public class MergeRenamingMatchResources2Ways extends AbstractMergeRenamingMatchResources {

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, must no be {@code null} and must NOT be a three-way comparison, otherwise a
	 *            runtime exception is thrown.
	 * @param monitor
	 *            The monitor, must no be {@code null}.
	 */
	public MergeRenamingMatchResources2Ways(Comparison comparison, Monitor monitor) {
		super(comparison, monitor);
	}

	@Override
	protected Resource getBaseResource(MatchResource mr) {
		return mr.getLeft();
	}

	@Override
	protected DiffSide getBaseSide() {
		return DiffSide.SOURCE;
	}

	@Override
	protected void checkComparison() {
		Preconditions.checkState(!comparison.isThreeWay());
	}
}
