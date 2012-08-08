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
package org.eclipse.emf.compare.uml2.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareAdapterFactorySpec extends UMLCompareItemProviderAdapterFactory {

	private Adapter fAssociationChangeItemProvider;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createAssociationChangeAdapter()
	 */
	@Override
	public Adapter createAssociationChangeAdapter() {
		if (fAssociationChangeItemProvider == null) {
			fAssociationChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createAssociationChangeAdapter());
		}
		return fAssociationChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createDependencyChangeAdapter()
	 */
	@Override
	public Adapter createDependencyChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createDependencyChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createInterfaceRealizationChangeAdapter()
	 */
	@Override
	public Adapter createInterfaceRealizationChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createInterfaceRealizationChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createSubstitutionChangeAdapter()
	 */
	@Override
	public Adapter createSubstitutionChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createSubstitutionChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createExtendChangeAdapter()
	 */
	@Override
	public Adapter createExtendChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createExtendChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createGeneralizationSetChangeAdapter()
	 */
	@Override
	public Adapter createGeneralizationSetChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createGeneralizationSetChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createExecutionSpecificationChangeAdapter()
	 */
	@Override
	public Adapter createExecutionSpecificationChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createExecutionSpecificationChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createIntervalConstraintChangeAdapter()
	 */
	@Override
	public Adapter createIntervalConstraintChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createIntervalConstraintChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createMessageChangeAdapter()
	 */
	@Override
	public Adapter createMessageChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createMessageChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypePropertyChangeAdapter()
	 */
	@Override
	public Adapter createStereotypePropertyChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createStereotypePropertyChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypeApplicationChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeApplicationChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createStereotypeApplicationChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypeReferenceChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createStereotypeReferenceChangeAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createProfileApplicationChangeAdapter()
	 */
	@Override
	public Adapter createProfileApplicationChangeAdapter() {
		// TODO Auto-generated method stub
		return super.createProfileApplicationChangeAdapter();
	}

}
