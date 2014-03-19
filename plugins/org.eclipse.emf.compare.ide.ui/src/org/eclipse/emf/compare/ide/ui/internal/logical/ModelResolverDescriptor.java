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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;

/**
 * Descriptor of {@link IModelResolver}.
 * <p>
 * The descriptor holds required field such as id or the instance of the {@link IModelResolver}.
 * </p>
 * <p>
 * The descriptor holds optional field such as label or description.
 * </p>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ModelResolverDescriptor implements Comparable<ModelResolverDescriptor> {

	private final String label;

	private final String description;

	private final IModelResolver resolver;

	private final String id;

	/**
	 * Constructor.
	 * 
	 * @param resolver
	 *            Instance of {@link IModelResolver} (not null)
	 * @param id
	 *            Id of the {@link IModelResolver}
	 * @param label
	 *            Optional Human readable label (can be null)
	 * @param description
	 *            Optional Human readable description (can be null)
	 */
	ModelResolverDescriptor(IModelResolver resolver, String id, String label, String description) {
		super();
		Preconditions.checkArgument(resolver != null);
		Preconditions.checkArgument(id != null);
		final String fLabel;
		if (label == null) {
			fLabel = id;
		} else {
			fLabel = label;
		}

		this.label = fLabel;
		this.description = Strings.nullToEmpty(description);
		this.resolver = resolver;
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(ModelResolverDescriptor o) {
		return getModelResolver().getRanking() - o.getModelResolver().getRanking();
	}

	/**
	 * Optional Human readable description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the id of the {@link IModelResolver}.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Optional Human readable label.
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get Instance of {@link IModelResolver}.
	 * 
	 * @return
	 */
	public IModelResolver getModelResolver() {
		return resolver;
	}

}
