/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl;
import org.eclipse.emf.compare.merge.IMerger;

/**
 * This specialization of the {@link ResourceAttachmentChangeImpl} class allows us to define the derived
 * features and operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceAttachmentChangeSpec extends ResourceAttachmentChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyLeftToRight()
	 */
	@Override
	public void copyLeftToRight() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyLeftToRight(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyRightToLeft()
	 */
	@Override
	public void copyRightToLeft() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyRightToLeft(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}
}
