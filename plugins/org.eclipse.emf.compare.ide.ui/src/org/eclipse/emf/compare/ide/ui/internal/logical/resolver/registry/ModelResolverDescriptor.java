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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;

/**
 * Descriptor of an {@link IModelResolver}.
 * <p>
 * This acts as a DTO holding the information about the registered resolvers.
 * </p>
 * <p>
 * Note that model resolvers are only partially lazy : we won't start the plugins providing model resolvers as
 * soon as we discover them, but we will do so as soon as EMF Compare is first called (we need an instance to
 * check whether the resolver {@link IModelResolver#canResolve(org.eclipse.core.resources.IStorage) can
 * resolve} the target model).
 * </p>
 * <p>
 * Model resolvers are stateful, so once a descriptor has been used to create its underlying instance, the
 * resolver remains in memory.
 * </p>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ModelResolverDescriptor {
	/** Underlying {@link IConfigurationElement} describing this resolver. */
	private final IConfigurationElement configurationElement;

	/**
	 * Name of the configuration property that can be used to retrieve the qualified class name of this
	 * resolver.
	 */
	private final String resolverClassPropertyName;

	/** Ranking of this resolver. */
	private final int ranking;

	/** Human-readable label for this resolver. */
	private final String label;

	/** Human-readable description for this resolver. */
	private final String description;

	/**
	 * Qualified class name of the resolver (value of the {@link #resolverClassPropertyName} from this
	 * {@link #configurationElement}. Will be used as identifier.
	 */
	private final String className;

	/** Keeps track of the underlying resolver. */
	private IModelResolver resolver;

	/** Don't log the same error multiple times. */
	private boolean logOnce;

	/**
	 * Default constructor.
	 * 
	 * @param configurationElement
	 *            Configuration element that served to populate this descriptor.
	 * @param resolverClassPropertyName
	 *            Name of the <code>configurationElement</code>'s property that can be used to retrieve the
	 *            resolver's qualified class name.
	 * @param ranking
	 *            Ranking of this resolver. High-priority resolvers take precedence over low-priority ones.
	 * @param label
	 *            Human-readable label for this resolver. The class name will be used if <code>null</code>.
	 * @param description
	 *            Human-readable description for this resolver. An empty string will be used if
	 *            <code>null</code>.
	 */
	ModelResolverDescriptor(IConfigurationElement configurationElement, String resolverClassPropertyName,
			int ranking, String label, String description) {
		this.configurationElement = checkNotNull(configurationElement);
		this.resolverClassPropertyName = checkNotNull(resolverClassPropertyName);
		this.className = checkNotNull(configurationElement.getAttribute(resolverClassPropertyName));
		this.ranking = ranking;
		if (label == null) {
			this.label = this.className;
		} else {
			this.label = label;
		}
		this.description = Strings.nullToEmpty(description);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * @return Human-readable label for this resolver.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return Human-readable description for this resolver.
	 */
	public String getDescription() {
		return description;
	}

	/** @return the qualified class name of the described resolver. */
	public String getClassName() {
		return className;
	}

	/**
	 * Create an instance of this resolver.
	 * 
	 * @return a new instance of this resolver
	 */
	IModelResolver getModelResolver() {
		if (resolver == null) {
			try {
				resolver = (IModelResolver)configurationElement
						.createExecutableExtension(resolverClassPropertyName);
				resolver.initialize();
			} catch (CoreException e) {
				// Shouldn't happen since the registry listener should have checked that.
				// log anyway.
				if (!logOnce) {
					logOnce = true;
					final String message = EMFCompareIDEUIMessages.getString(
							"ModelResolverRegistry.invalidResolver", label); //$NON-NLS-1$
					final IStatus status = new Status(IStatus.ERROR, configurationElement
							.getDeclaringExtension().getContributor().getName(), message, e);
					EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
				}
			}
		}
		return resolver;
	}

	void dispose() {
		if (resolver != null) {
			resolver.dispose();
		}
	}
}
