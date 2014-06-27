/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.provider.UMLCompareItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.util.UMLCompareSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;

/**
 * Decorator for factory {@link UMLCompareItemProviderAdapterFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareItemProviderDecoratorAdapterFactory extends DecoratorAdapterFactory {

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 */
	private final UMLCompareSwitch<IItemProviderDecorator> modelSwitch = new UMLCompareSwitch<IItemProviderDecorator>() {
		@Override
		public IItemProviderDecorator caseStereotypeApplicationChange(StereotypeApplicationChange object) {
			return createStereotypeApplicationChangeItemProviderDecorator();
		}

		@Override
		public IItemProviderDecorator caseStereotypeAttributeChange(
				org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange object) {
			return createStereotypeAttributeChangeItemProviderDecorator();
		}

		@Override
		public IItemProviderDecorator caseStereotypeReferenceChange(
				org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange object) {
			return createStereotyeReferenceChangeItemProviderDecorator();
		}

		@Override
		public IItemProviderDecorator caseUMLDiff(UMLDiff object) {
			return createUMLDiffItemProviderDecorator();
		}

	};

	/**
	 * Creates a new instance.
	 */
	public UMLCompareItemProviderDecoratorAdapterFactory() {
		super(new UMLCompareItemProviderAdapterFactory());
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange
	 * <em>Stereotype Reference Change</em>}'.
	 * 
	 * @return the new adapter.
	 */
	protected IItemProviderDecorator createStereotyeReferenceChangeItemProviderDecorator() {
		return new StereotypeReferenceChangeItemProviderDecorator(this);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange
	 * <em>Stereotype Attribute Change</em>}'.
	 * 
	 * @return the new adapter.
	 */
	protected IItemProviderDecorator createStereotypeAttributeChangeItemProviderDecorator() {
		return new StereotypeAttributeChangeItemProviderDecorator(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.DecoratorAdapterFactory#createItemProviderDecorator(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected IItemProviderDecorator createItemProviderDecorator(Object target, Object type) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange
	 * <em>Stereotype Application Change</em>}'.
	 * 
	 * @return the new adapter.
	 */
	protected IItemProviderDecorator createStereotypeApplicationChangeItemProviderDecorator() {
		return new StereotypeApplicationChangeItemProviderDecorator(this);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff
	 * <em>UML Diff</em>}'.
	 * 
	 * @return the new adapter.
	 */
	protected IItemProviderDecorator createUMLDiffItemProviderDecorator() {
		return new UMLDiffItemProviderDecorator(this);
	}
}
