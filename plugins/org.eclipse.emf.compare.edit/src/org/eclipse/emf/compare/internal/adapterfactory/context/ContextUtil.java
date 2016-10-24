/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.adapterfactory.context;

import static com.google.common.base.Predicates.isNull;
import static com.google.common.collect.Iterables.all;

import java.util.Map;

import org.eclipse.emf.compare.adapterfactory.context.IContextTester;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * Utility class for evaluating context testers in adapter factory descriptors.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.3
 */
public final class ContextUtil {

	/**
	 * Private constructor for utility class.
	 */
	private ContextUtil() {
	}

	/**
	 * Returns whether the adapter factory should be applied in the given context.
	 * 
	 * @param adapterFactoryDescriptor
	 *            the {@link ComposedAdapterFactory.Descriptor} containing the adapter factory and possibly a
	 *            context tester
	 * @param context
	 *            the context
	 * @return {@code false} if the descriptor's context tester indicates no responsibility for the given
	 *         {@code comparison}, {@code true} otherwise.
	 * @see IContextTester#apply(Map)
	 */
	public static boolean apply(ComposedAdapterFactory.Descriptor adapterFactoryDescriptor,
			Map<Object, Object> context) {
		if (!(adapterFactoryDescriptor instanceof RankedAdapterFactoryDescriptor)) {
			return true;
		}
		RankedAdapterFactoryDescriptor rankedDescriptor = (RankedAdapterFactoryDescriptor)adapterFactoryDescriptor;
		// if we have a context tester, the context must not be null
		return rankedDescriptor.getContextTester() == null
				|| (!isNullContext(context) && rankedDescriptor.getContextTester().apply(context));
	}

	/**
	 * Evaluates whether the given context is a null context, i.e., the context itself is null, it has no
	 * entries or all its values are null.
	 * 
	 * @param context
	 *            the context
	 * @return {@code true} if the context is null, empty or all its values are null, {@code false} otherwise.
	 */
	public static boolean isNullContext(Map<Object, Object> context) {
		return context == null || context.isEmpty() || all(context.values(), isNull());
	}
}
