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

import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.subscribers.Subscriber;

/**
 * Contract for clients of the org.eclipse.emf.ecompare.ide.ui.subscriberProvider extension point.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.4.3
 */
public interface ISubscriberProvider {
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
	Subscriber getSubscriber(ICompareContainer container, ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor);
}
