/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 527567
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.matchs.provider.spec;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Diagram compare merge viewer,
 * on matches.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramCompareItemProviderAdapterFactorySpec extends DecoratorAdapterFactory {

	/**
	 * Constructor calling super {@link #CompareItemProviderAdapterFactory()}.
	 */
	public DiagramCompareItemProviderAdapterFactorySpec() {
		super(new NotationItemProviderAdapterFactory());
	}

	@Override
	protected IItemProviderDecorator createItemProviderDecorator(Object target, Object Type) {
		if (target instanceof Diagram) {
			return new DiagramItemProviderSpec(this);
		} else if (target instanceof View) {
			return new ViewItemProviderSpec(this);
		} else if (target instanceof Style) {
			return new StyleItemProviderSpec(this);
		} else {
			return new BaseItemProviderDecorator(this);
		}
	}

	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		super.setParentAdapterFactory(parentAdapterFactory);
		((ComposeableAdapterFactory)decoratedAdapterFactory).setParentAdapterFactory(parentAdapterFactory);
	}
}
