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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeInput;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.SynchronizationCompareAdapter;
import org.eclipse.ui.IMemento;

/**
 * Allows us to provide comparison support for the EMF model provider.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFSynchronizationCompareAdapter extends SynchronizationCompareAdapter {
	/** {@inheritDoc} */
	@Override
	public boolean hasCompareInput(ISynchronizationContext context, Object object) {
		return object instanceof SynchronizationModel;
	}

	/** {@inheritDoc} */
	@Override
	public ICompareInput asCompareInput(ISynchronizationContext context, Object o) {
		final IComparisonScope scope = ComparisonScopeBuilder.create((SynchronizationModel)o,
				new NullProgressMonitor());
		final ComparisonScopeInput input = new ComparisonScopeInput(scope,
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		input.setRightEditable(false);
		return input;
	}

	public void save(ResourceMapping[] mappings, IMemento memento) {
		// We do not support restoring from memento
	}

	public ResourceMapping[] restore(IMemento memento) {
		// We do not support restoring from memento
		// Note that even though other implementations return "null", parts of the Team framework will fail if
		// we do. See org.eclipse.team.ui.synchronize.ModelSynchronizeParticipant.loadMappings(IMemento) for
		// an example of such a failure.
		return new ResourceMapping[0];
	}
}
