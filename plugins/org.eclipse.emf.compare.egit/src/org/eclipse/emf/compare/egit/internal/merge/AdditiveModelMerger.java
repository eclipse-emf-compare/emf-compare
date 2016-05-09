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
package org.eclipse.emf.compare.egit.internal.merge;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.IAdditiveResourceMappingMerger;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.team.core.mapping.IResourceMappingMerger;

/**
 * This extends the recursive model merger in order to specialize it for additive merge. The goal of this
 * merger is to allow users to split a model in several architectural releases focused on a feature and
 * stripped out of everything else and then merge this models.
 * <p>
 * The additive model merger extends the behavior of the recursive model merger. It only customize the
 * resource mapping merger used.
 * </p>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class AdditiveModelMerger extends RecursiveModelMerger {

	/**
	 * Default constructor.
	 * 
	 * @param db
	 *            The repository
	 * @param inCore
	 *            a parameter
	 */
	public AdditiveModelMerger(Repository db, boolean inCore) {
		super(db, inCore);
	}

	@Override
	protected IResourceMappingMerger getResourceMappingMerger(Set<IResource> logicalModel)
			throws CoreException {
		return LogicalModels.findAdapter(logicalModel, IAdditiveResourceMappingMerger.class);
	}
}
