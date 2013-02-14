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
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.uml2.provider.StereotypeApplicationChangeItemProvider;
import org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareAdapterFactorySpec extends UMLCompareItemProviderAdapterFactory {

	ForwardingUMLDiffItemProvider fAssociationChangeItemProvider;

	ForwardingUMLDiffItemProvider fDependencyChangeItemProvider;

	ForwardingUMLDiffItemProvider fExecutionSpecificationChangeItemProvider;

	ForwardingUMLDiffItemProvider fExtendChangeItemProvider;

	ForwardingUMLDiffItemProvider fGeneralizationSetChangeItemProvider;

	ForwardingUMLDiffItemProvider fIncludeChangeItemProvider;

	ForwardingUMLDiffItemProvider fInterfaceRealizationChangeItemProvider;

	ForwardingUMLDiffItemProvider fIntervalConstraintChangeItemProvider;

	ForwardingUMLDiffItemProvider fMessageChangeItemProvider;

	ForwardingUMLDiffItemProvider fProfileApplicationChangeItemProvider;

	StereotypeApplicationChangeItemProvider fStereotypeApplicationChangeItemProvider;

	ForwardingUMLDiffItemProvider fStereotypePropertyChangeItemProvider;

	ForwardingUMLDiffItemProvider fStereotypeReferenceChangeItemProvider;

	ForwardingUMLDiffItemProvider fSubstitutionChangeItemProvider;

	public UMLCompareAdapterFactorySpec() {
		super();
		supportedTypes.add(IItemStyledLabelProvider.class);
	}

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
		if (fDependencyChangeItemProvider == null) {
			fDependencyChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createDependencyChangeAdapter());
		}
		return fDependencyChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createInterfaceRealizationChangeAdapter()
	 */
	@Override
	public Adapter createInterfaceRealizationChangeAdapter() {
		if (fInterfaceRealizationChangeItemProvider == null) {
			fInterfaceRealizationChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createInterfaceRealizationChangeAdapter());
		}
		return fInterfaceRealizationChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createSubstitutionChangeAdapter()
	 */
	@Override
	public Adapter createSubstitutionChangeAdapter() {
		if (fSubstitutionChangeItemProvider == null) {
			fSubstitutionChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createSubstitutionChangeAdapter());
		}
		return fSubstitutionChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createExtendChangeAdapter()
	 */
	@Override
	public Adapter createExtendChangeAdapter() {
		if (fExtendChangeItemProvider == null) {
			fExtendChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createExtendChangeAdapter());
		}
		return fExtendChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createGeneralizationSetChangeAdapter()
	 */
	@Override
	public Adapter createGeneralizationSetChangeAdapter() {
		if (fGeneralizationSetChangeItemProvider == null) {
			fGeneralizationSetChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createGeneralizationSetChangeAdapter());
		}
		return fGeneralizationSetChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createExecutionSpecificationChangeAdapter()
	 */
	@Override
	public Adapter createExecutionSpecificationChangeAdapter() {
		if (fExecutionSpecificationChangeItemProvider == null) {
			fExecutionSpecificationChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createExecutionSpecificationChangeAdapter());
		}
		return fExecutionSpecificationChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createIntervalConstraintChangeAdapter()
	 */
	@Override
	public Adapter createIntervalConstraintChangeAdapter() {
		if (fIntervalConstraintChangeItemProvider == null) {
			fIntervalConstraintChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createIntervalConstraintChangeAdapter());
		}
		return fIntervalConstraintChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createMessageChangeAdapter()
	 */
	@Override
	public Adapter createMessageChangeAdapter() {
		if (fMessageChangeItemProvider == null) {
			fMessageChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createMessageChangeAdapter());
		}
		return fMessageChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypePropertyChangeAdapter()
	 */
	@Override
	public Adapter createStereotypePropertyChangeAdapter() {
		if (fStereotypePropertyChangeItemProvider == null) {
			fStereotypePropertyChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createStereotypePropertyChangeAdapter());
		}
		return fStereotypePropertyChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypeApplicationChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeApplicationChangeAdapter() {
		if (fStereotypeApplicationChangeItemProvider == null) {
			fStereotypeApplicationChangeItemProvider = new StereotypeApplicationChangeItemProviderSpec(this);
		}
		return fStereotypeApplicationChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createStereotypeReferenceChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		if (fStereotypeReferenceChangeItemProvider == null) {
			fStereotypeReferenceChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createStereotypeReferenceChangeAdapter());
		}
		return fStereotypeReferenceChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createProfileApplicationChangeAdapter()
	 */
	@Override
	public Adapter createProfileApplicationChangeAdapter() {
		if (fProfileApplicationChangeItemProvider == null) {
			fProfileApplicationChangeItemProvider = new ForwardingUMLDiffItemProvider(
					(ItemProviderAdapter)super.createProfileApplicationChangeAdapter());
		}
		return fProfileApplicationChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.provider.UMLCompareItemProviderAdapterFactory#createIncludeChangeAdapter()
	 */
	@Override
	public Adapter createIncludeChangeAdapter() {
		if (fIncludeChangeItemProvider == null) {
			fIncludeChangeItemProvider = new ForwardingUMLDiffItemProvider((ItemProviderAdapter)super
					.createIncludeChangeAdapter());
		}
		return fIncludeChangeItemProvider;
	}

}
