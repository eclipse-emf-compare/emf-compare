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
package org.eclipse.emf.compare.ide;

import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.req.IReqEngine;

/**
 * Specialized EMFCompare class to handle IDE specific behavior.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareIDE extends EMFCompare {

	/**
	 * Creates a new EMFCompareIDE object able to compare Notifier with the help of given engines.
	 * 
	 * @param matchEngine
	 *            IMatchEngine to use to compute comparison
	 * @param diffEngine
	 *            IDiffEngine to use to compute comparison
	 * @param reqEngine
	 *            IReqEngine to use to compute comparison
	 * @param equiEngine
	 *            IEquiEngine to use to compute comparison
	 * @param conflictDetector
	 *            IConflictDetector to use to compute comparison
	 * @param postProcessorRegistry
	 *            PostProcessorRegistry to use to find an IPostProcessor
	 */
	protected EMFCompareIDE(IMatchEngine matchEngine, IDiffEngine diffEngine, IReqEngine reqEngine,
			IEquiEngine equiEngine, IConflictDetector conflictDetector,
			PostProcessorRegistry postProcessorRegistry) {
		super(matchEngine, diffEngine, reqEngine, equiEngine, conflictDetector, postProcessorRegistry);
	}

	/**
	 * Creates a new builder to configure the creation of a new EMFCompare object.
	 * 
	 * @return a new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A Builder pattern to instantiate EMFCompareIDE objects.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class Builder extends EMFCompare.Builder {

		/**
		 * Creates a new builder object.
		 */
		protected Builder() {
			this.registry = EMFCompareIDEPlugin.getDefault().getPostProcessorRegistry();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.EMFCompare.Builder#setPostProcessorRegistry(org.eclipse.emf.compare.extension.PostProcessorRegistry)
		 */
		@Override
		public org.eclipse.emf.compare.EMFCompare.Builder setPostProcessorRegistry(
				PostProcessorRegistry registry) {
			throw new UnsupportedOperationException(
					"Can not set a post processor registry in the IDE context");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.EMFCompare.Builder#build()
		 */
		@Override
		public EMFCompare build() {
			return super.build();
		}
	}
}
