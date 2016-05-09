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

/**
 * Criterion for describing a merge operation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.4
 */
public interface IMergeCriterion {

	/**
	 * Constant to use as key for the merge option that represents a merge criterion. This must be used as a
	 * key in the map of optinos available to mergers that implement {@link IMergeOptionAware}, so that
	 * mergers can propagate the criterion to the mergers they may invoke.
	 */
	String OPTION_MERGE_CRITERION = "merge.criterion"; //$NON-NLS-1$
}
