/*******************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.subscriber;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.subscriber.SubscriberProviderDescriptor;
import org.eclipse.team.core.subscribers.Subscriber;

/**
 * The registry managing the registered subscriber provider extension point information.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.4.3
 */
public class SubscriberProviderRegistry {

	/** Keeps track of the extensions providing subscriber providers. */
	private final Map<String, SubscriberProviderDescriptor> registeredDescriptors;

	/**
	 * Constructs and initialized this registry.
	 */
	public SubscriberProviderRegistry() {
		registeredDescriptors = new LinkedHashMap<>();
	}

	/**
	 * Adds the given {@link SubscriberProviderDescriptor} to this registry, using the given {@code className}
	 * as the identifier.
	 * 
	 * @param className
	 *            The identifier for the given {@link SubscriberProviderDescriptor}.
	 * @param descriptor
	 *            The {@link SubscriberProviderDescriptor} which is to be added to this registry.
	 */
	public void addProvider(String className, SubscriberProviderDescriptor descriptor) {
		registeredDescriptors.put(className, descriptor);
	}

	/**
	 * Removes the {@link SubscriberProviderDescriptor} and its managed {@link ISubscriberProvider} identified
	 * by the given {@code className} from this registry.
	 * 
	 * @param className
	 *            Identifier of the provider we are to remove from this registry.
	 * @return The removed {@link SubscriberProviderDescriptor}, if any.
	 */
	public SubscriberProviderDescriptor removeProvider(String className) {
		return registeredDescriptors.remove(className);
	}

	/** Clears out all registered providers from this registry. */
	public void clear() {
		registeredDescriptors.clear();
	}

	/**
	 * Returns the subscriber that provides the synchronization between local resources and remote resources
	 * based on the given comparison input.
	 * 
	 * @param container
	 *            The compare container input.
	 * @param left
	 *            Left of the compared elements.
	 * @param right
	 *            Right of the compared elements.
	 * @param origin
	 *            Common ancestor of the <code>left</code> and <code>right</code> compared elements.
	 * @param monitor
	 *            Monitor to report progress on.
	 * @return The subscriber used for the comparison of the container or <code>null</code> if no subscriber
	 *         could be determined.
	 */
	public Subscriber getSubscriber(ICompareContainer container, ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor) {
		List<SubscriberProviderDescriptor> rankedDescriptors = registeredDescriptors.values().stream()
				.sorted(Comparator.comparingInt(SubscriberProviderDescriptor::getRanking).reversed())
				.collect(Collectors.toList());
		for (SubscriberProviderDescriptor descriptor : rankedDescriptors) {
			ISubscriberProvider provider = descriptor.getSubscriberProvider();
			if (provider != null) {
				Subscriber subscriber = provider.getSubscriber(container, left, right, origin, monitor);
				if (subscriber != null) {
					return subscriber;
				}
			}
		}
		return null;
	}
}
