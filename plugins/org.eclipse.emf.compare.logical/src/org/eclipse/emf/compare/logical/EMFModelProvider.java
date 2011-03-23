package org.eclipse.emf.compare.logical;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class EMFModelProvider extends ModelProvider {
	public static final String PROVIDER_ID = "org.eclipse.emf.compare.model.provider"; //$NON-NLS-1$

	public EMFModelProvider() {
		System.out.println();
	}

	@Override
	public ResourceMapping[] getMappings(IResource resource, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		return super.getMappings(resource, context, monitor);
	}

	@Override
	public ResourceMapping[] getMappings(IResource[] resources, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		return super.getMappings(resources, context, monitor);
	}

	@Override
	public ResourceMapping[] getMappings(ResourceTraversal[] traversals, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		return super.getMappings(traversals, context, monitor);
	}

	@Override
	public ResourceTraversal[] getTraversals(ResourceMapping[] mappings, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		return super.getTraversals(mappings, context, monitor);
	}
}
