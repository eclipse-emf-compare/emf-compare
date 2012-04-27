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
package org.eclipse.emf.compare.ide.tests.mock;

import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.core.mapping.provider.ResourceDiffTree;
import org.eclipse.team.core.mapping.provider.SynchronizationContext;
import org.eclipse.team.core.mapping.provider.SynchronizationScopeManager;

/**
 * Mocks the behavior of a synchronization context in order to test the behavior of our extensions without
 * relying on the platform UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MockSynchronizationContext extends SynchronizationContext {
	/**
	 * Delegates to the super constructor.
	 * 
	 * @param mappings
	 *            The ResourceMappings for which we need a synchronization context.
	 * @param context
	 *            The resource mapping context.
	 * @param type
	 *            The type of synchronization ({@link ISynchronizationContext#TWO_WAY} or
	 *            {@link ISynchronizationContext#THREE_WAY}).
	 */
	public MockSynchronizationContext(ResourceMapping[] mappings, ResourceMappingContext context, int type) {
		super(new SynchronizationScopeManager("dummy", mappings, context, false), type, //$NON-NLS-1$
				new ResourceDiffTree());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.core.mapping.ISynchronizationContext#refresh(org.eclipse.core.resources.mapping.ResourceTraversal[],
	 *      int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void refresh(ResourceTraversal[] traversals, int flags, IProgressMonitor monitor)
			throws CoreException {
		// ignore
	}
}
