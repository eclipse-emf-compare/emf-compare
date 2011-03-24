package org.eclipse.emf.compare.logical;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.IAdapterFactory;

public class EMFAdapterFactory implements IAdapterFactory {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ResourceMapping.class && adaptableObject instanceof IResource) {
			new EMFResourceMapping((IResource)adaptableObject);
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return new Class[] {ResourceMapping.class,};
	}
}
