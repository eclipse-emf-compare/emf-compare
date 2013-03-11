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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory;



import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IAccessorFactory {

	boolean isFactoryFor(Object target);

	int getRanking();

	void setRanking(int parseInt);

	ITypedElement createLeft(AdapterFactory adapterFactory, Object target);

	ITypedElement createRight(AdapterFactory adapterFactory, Object target);

	ITypedElement createAncestor(AdapterFactory adapterFactory, Object target);

	interface Registry {

		IAccessorFactory getHighestRankingFactory(Object target);

		Collection<IAccessorFactory> getFactories(Object target);

		IAccessorFactory add(IAccessorFactory factory);

		IAccessorFactory remove(String className);

		void clear();
	}

}
