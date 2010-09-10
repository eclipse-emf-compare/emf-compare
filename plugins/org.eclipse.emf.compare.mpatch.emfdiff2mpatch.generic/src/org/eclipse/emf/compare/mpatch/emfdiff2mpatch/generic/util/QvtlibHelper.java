/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * Helper class for the creation of {@link IModelDescriptor}s and {@link IElementReference}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class QvtlibHelper {

	/**
	 * Used for creating human-readable labels for arbitrary EMF models.
	 */
	private static final ComposedAdapterFactory ADAPTER_FACTORY;

	static {
		ADAPTER_FACTORY = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		ADAPTER_FACTORY.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	/**
	 * Create a deep clone from a given java {@link Object} by serializing and deserializing it.
	 * 
	 * @param copyObject
	 *            The object to clone.
	 * @param eDataType
	 *            The data type of the object to clone.
	 * @return A clone of the given object.
	 */
	public static Object clone(Object copyObject, EDataType eDataType) {
		final String string = EcoreUtil.convertToString(eDataType, copyObject);
		final Object clone = EcoreUtil.createFromString(eDataType, string);
		return clone;
	}

	/**
	 * Get the URI (including fragment) of <code>self</code>.
	 * 
	 * @param self
	 *            Any {@link EObject}.
	 * @return A String representation of the {@link URI} for the given {@link EObject}.
	 */
	public static String getUriString(EObject self) {
		return self.eResource().getURI().toString() + "#" + self.eResource().getURIFragment(self);
	}

	/**
	 * Use ecore and reflective adapter factories to compute a label.
	 * 
	 * @param self
	 *            The object for which a label should be created.
	 * @return A label.
	 */
	public static String getLabel(EObject self) {
		final IItemLabelProvider labelProvider = (IItemLabelProvider) ADAPTER_FACTORY.adapt(self,
				IItemLabelProvider.class);
		return labelProvider.getText(self);
	}

}
