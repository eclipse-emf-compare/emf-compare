/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;

/**
 * Decorator adapter factory for UML stereotyped element.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class UMLProfileItemProviderAdapterFactoryDecorator extends DecoratorAdapterFactory {

	/**
	 * Map holding the {@link IItemProviderDecorator}s that decorate an
	 * {@link org.eclipse.emf.edit.provider.ItemProvider} of a stereotyped element.
	 */
	private Map<Object, IItemProviderDecorator> stereotypeItemProviderDecorators = new HashMap<Object, IItemProviderDecorator>();

	/**
	 * Constructor.
	 */
	public UMLProfileItemProviderAdapterFactoryDecorator() {
		super(new UMLItemProviderAdapterFactory());
	}

	@Override
	public Object adapt(Object target, Object type) {
		if (isStereotypedElement(target)) {
			final Object adapter = decoratedAdapterFactory.adapt(target, type);
			if (adapter instanceof IChangeNotifier) {
				IItemProviderDecorator itemProviderDecorator = stereotypeItemProviderDecorators.get(adapter);
				if (itemProviderDecorator == null) {
					itemProviderDecorator = createStereotypeElementItemProvider();
					stereotypeItemProviderDecorators.put(adapter, itemProviderDecorator);
					itemProviderDecorator.setDecoratedItemProvider((IChangeNotifier)adapter);
				}
				return itemProviderDecorator;
			}
		}
		return super.adapt(target, type);
	}

	/**
	 * Creates a new {@link IItemProviderDecorator} for a stereotyped element.
	 * 
	 * @return {@link IItemProviderDecorator}.
	 */
	protected IItemProviderDecorator createStereotypeElementItemProvider() {
		return new StereotypedElementItemProviderDecorator(this);
	}

	/**
	 * Returns <code>true</code> if the target is an UML {@link Element} with stereotypes.
	 * 
	 * @param target
	 *            Object to test.
	 * @return <code>true</code> if the input is a stereotyped {@link Element}.
	 */
	private boolean isStereotypedElement(Object target) {
		return target instanceof Element && !((Element)target).getAppliedStereotypes().isEmpty();
	}

	@Override
	protected IItemProviderDecorator createItemProviderDecorator(Object target, Object type) {
		return new ForwardingItemProviderAdapterDecorator(this);
	}

	@Override
	public void dispose() {
		for (Object object : stereotypeItemProviderDecorators.values()) {
			if (object instanceof IDisposable) {
				((IDisposable)object).dispose();
			}
		}
		super.dispose();
	}
}
