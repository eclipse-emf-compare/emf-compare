/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge;

/**
 * An abstract implementation of a listener to receive merge events. All methods in this class are empty,
 * clients should override the method corresponding to whichever event they are interested in handling
 * notifications for.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMergeListener implements IMergeListener {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.IMergeListener#mergeDiffEnd(org.eclipse.emf.compare.diff.merge.MergeEvent)
	 */
	public void mergeDiffEnd(MergeEvent event) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.IMergeListener#mergeDiffStart(org.eclipse.emf.compare.diff.merge.MergeEvent)
	 */
	public void mergeDiffStart(MergeEvent event) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.IMergeListener#mergeOperationEnd(org.eclipse.emf.compare.diff.merge.MergeEvent)
	 */
	public void mergeOperationEnd(MergeEvent event) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.IMergeListener#mergeOperationStart(org.eclipse.emf.compare.diff.merge.MergeEvent)
	 */
	public void mergeOperationStart(MergeEvent event) {
		// empty implementation
	}
}
