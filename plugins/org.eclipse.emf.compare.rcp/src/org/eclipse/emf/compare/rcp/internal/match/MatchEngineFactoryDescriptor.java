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
package org.eclipse.emf.compare.rcp.internal.match;

import com.google.common.base.Throwables;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.rcp.internal.extension.impl.LazyItemDescriptor;

/**
 * Descriptor for {@link IMatchEngine.Factory}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class MatchEngineFactoryDescriptor extends LazyItemDescriptor<IMatchEngine.Factory> {

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link LazyItemDescriptor#getLabel()}
	 * @param description
	 *            {@link LazyItemDescriptor#getDescription()}
	 * @param rank
	 *            {@link LazyItemDescriptor#getRank()}
	 * @param config
	 *            {@link IConfigurationElement} element used to create this item.
	 * @param id
	 *            {@link LazyItemDescriptor#getID()}
	 */
	public MatchEngineFactoryDescriptor(String label, String description, int rank,
			IConfigurationElement config, String id) {
		super(label, description, rank, config, id);
	}

	/**
	 * {@inheritDoc} Set the rank of the factory and set configuration if needed.
	 */
	@Override
	public Factory getItem() {
		Factory factory = null;
		try {
			factory = (Factory)getConfig()
					.createExecutableExtension(MatchEngineFactoryRegistryListener.ATT_CLASS);
			factory.setRanking(getRank());
		} catch (CoreException e) {
			Throwables.propagate(e);
		}
		return factory;
	}

}
