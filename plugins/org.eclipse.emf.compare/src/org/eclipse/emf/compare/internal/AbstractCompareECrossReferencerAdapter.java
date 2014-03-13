/**
 * Copyright (c) 2002-2013 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 */
package org.eclipse.emf.compare.internal;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

// CHECKSTYLE:OFF copy paste from EContentAdapter#handleContainment(Notification)
/**
 * Abstract class that redefines {@link #handleContainment(org.eclipse.emf.common.notify.Notification)} to
 * mimic {@link org.eclipse.emf.ecore.util.EContentAdapter} behavior.
 */
public abstract class AbstractCompareECrossReferencerAdapter extends ECrossReferenceAdapter {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#handleContainment(org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	protected void handleContainment(Notification notification) {
		// Handle eventype the same way as EContentAdapter.
		// This cross referencer adapter should not let client find object if they have been deleted at a
		// given time.
		switch (notification.getEventType()) {
			case Notification.RESOLVE: {
				// We need to be careful that the proxy may be resolved while we are attaching this adapter.
				// We need to avoid attaching the adapter during the resolve
				// and also attaching it again as we walk the eContents() later.
				// Checking here avoids having to check during addAdapter.
				//
				Notifier oldValue = (Notifier)notification.getOldValue();
				if (oldValue.eAdapters().contains(this)) {
					removeAdapter(oldValue);
					Notifier newValue = (Notifier)notification.getNewValue();
					addAdapter(newValue);
				}
				break;
			}
			case Notification.UNSET: {
				Object oldValue = notification.getOldValue();
				if (oldValue != Boolean.TRUE && oldValue != Boolean.FALSE) {
					if (oldValue != null) {
						removeAdapter((Notifier)oldValue);
					}
					Notifier newValue = (Notifier)notification.getNewValue();
					if (newValue != null) {
						addAdapter(newValue);
					}
				}
				break;
			}
			case Notification.SET: {
				Notifier oldValue = (Notifier)notification.getOldValue();
				if (oldValue != null) {
					removeAdapter(oldValue);
				}
				Notifier newValue = (Notifier)notification.getNewValue();
				if (newValue != null) {
					addAdapter(newValue);
				}
				break;
			}
			case Notification.ADD: {
				Notifier newValue = (Notifier)notification.getNewValue();
				if (newValue != null) {
					addAdapter(newValue);
				}
				break;
			}
			case Notification.ADD_MANY: {
				@SuppressWarnings("unchecked")
				Collection<Notifier> newValues = (Collection<Notifier>)notification.getNewValue();
				for (Notifier newValue : newValues) {
					addAdapter(newValue);
				}
				break;
			}
			case Notification.REMOVE: {
				Notifier oldValue = (Notifier)notification.getOldValue();
				if (oldValue != null) {
					removeAdapter(oldValue);
				}
				break;
			}
			case Notification.REMOVE_MANY: {
				@SuppressWarnings("unchecked")
				Collection<Notifier> oldValues = (Collection<Notifier>)notification.getOldValue();
				for (Notifier oldContentValue : oldValues) {
					removeAdapter(oldContentValue);
				}
				break;
			}
			default:
				break;
		}
	}
}
// CHECKSTYLE:ON
