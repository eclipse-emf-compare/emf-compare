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
package org.eclipse.emf.compare.ide.ui.internal.logical.view.registry;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.ILogicalModelViewHandler;

/**
 * Descriptor of an {@link ILogicalModelViewHandler}.
 * <p>
 * This acts as a DTO holding the information about the registered handlers.
 * </p>
 * <p>
 * Handlers are stateful, so once a descriptor has been used to create its underlying instance, the handler
 * remains in memory.
 * </p>
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class LogicalModelViewHandlerDescriptor {

	/** Underlying {@link IConfigurationElement} describing this handler. */
	private final IConfigurationElement configurationElement;

	/**
	 * Name of the configuration property that can be used to retrieve the qualified class name of this
	 * handler.
	 */
	private final String handlerClassPropertyName;

	/** Ranking of this handler. */
	private final int ranking;

	/**
	 * Qualified class name of the handler (value of the {@link #handlerClassPropertyName} from this
	 * {@link #configurationElement}. Will be used as identifier.
	 */
	private final String className;

	/** Keeps track of the underlying handler. */
	private ILogicalModelViewHandler handler;

	/** Don't log the same error multiple times. */
	private boolean logOnce;

	/**
	 * Default constructor.
	 * 
	 * @param configurationElement
	 *            Configuration element that served to populate this descriptor.
	 * @param handlerClassPropertyName
	 *            Name of the <code>configurationElement</code>'s property that can be used to retrieve the
	 *            handler's qualified class name.
	 * @param ranking
	 *            Ranking of this handler. High-priority handlers take precedence over low-priority ones.
	 */
	LogicalModelViewHandlerDescriptor(IConfigurationElement configurationElement,
			String handlerClassPropertyName, int ranking) {
		this.configurationElement = checkNotNull(configurationElement);
		this.handlerClassPropertyName = checkNotNull(handlerClassPropertyName);
		this.className = checkNotNull(configurationElement.getAttribute(handlerClassPropertyName));
		this.ranking = ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.view.ILogicalModelViewHandler#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * Get the qualified class name of the described handler.
	 * 
	 * @return the qualified class name of the described handler.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Create an instance of this handler.
	 * 
	 * @return a new instance of this handler
	 */
	ILogicalModelViewHandler getHandler() {
		if (handler == null) {
			try {
				handler = (ILogicalModelViewHandler)configurationElement
						.createExecutableExtension(handlerClassPropertyName);
			} catch (CoreException e) {
				// Shouldn't happen since the registry listener should have checked that.
				// log anyway.
				if (!logOnce) {
					logOnce = true;
					final String message = EMFCompareIDEUIMessages.getString(
							"LogicalModelViewHandlerRegistry.invalidHandler", handlerClassPropertyName); //$NON-NLS-1$
					final IStatus status = new Status(IStatus.ERROR,
							configurationElement.getDeclaringExtension().getContributor().getName(), message,
							e);
					EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
				}
			}
		}
		return handler;
	}

}
