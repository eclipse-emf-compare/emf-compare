/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.recorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EmfRecorder implements Adapter.Internal {

	public interface RecorderListener {
//		public void addRootObject(Notifier obj);

		public void handleFeature(EStructuralFeature feature,
				EReference containment, Notification notification,
				EObject object);
	}

	protected RecorderListener listener;

	protected boolean recording = false;

	protected boolean resolveProxies;

	protected List<Notifier> rootObjects = new ArrayList<Notifier>();

	protected List<Notifier> targetObjects = new BasicEList.FastCompare<Notifier>();

	public EmfRecorder(RecorderListener listener) {
		super();
		this.listener = listener;
	}

	protected void addAdapter(Notifier notifier, boolean isRoot) {
		// if (notifier != getRecorded()) {
		EList<Adapter> eAdapters = notifier.eAdapters();
		if (!eAdapters.contains(this)) {
			eAdapters.add(this);
		}
		// }
	}

	public void addRootObject(Notifier obj) {
		rootObjects.add(obj);
		if (recording)
			addAdapter(obj, true);
//		listener.addRootObject(obj);
	}

	public void beginRecording() {
		for (Object rootObject : rootObjects) {
			Notifier notifier = (Notifier) rootObject;
			addAdapter(notifier, true);
		}
		recording = true;
	}

	public void dispose() {
		recording = false;

		for (Notifier r : rootObjects.toArray(new Notifier[rootObjects.size()]))
			removeRootObject(r);

		Notifier[] notifiers = targetObjects.toArray(new Notifier[targetObjects
				.size()]);
		targetObjects.clear();
		for (Notifier n : notifiers)
			removeAdapter(n, false);
	}

	public void endRecording() {
		if (recording)
			recording = false;
	}

	public List<Notifier> getRootObjects() {
		return rootObjects;
	}

	public Notifier getTarget() {
		return null;
	}

	protected void handleFeature(EStructuralFeature feature,
			EReference containment, Notification notification, EObject eObject) {

		listener.handleFeature(feature, containment, notification, eObject);

		if (containment == null)
			return;

		switch (notification.getEventType()) {
		case Notification.RESOLVE:
		case Notification.SET:
		case Notification.UNSET:
			Object newValue = notification.getNewValue();
			if (newValue != null && newValue != Boolean.TRUE
					&& newValue != Boolean.FALSE)
				addAdapter((Notifier) newValue, false);
			break;

		case Notification.ADD:
			Notifier newValue1 = (Notifier) notification.getNewValue();
			addAdapter(newValue1, false);
			break;

		case Notification.ADD_MANY:
			@SuppressWarnings("unchecked")
			Collection<Notifier> newValues = (Collection<Notifier>) notification
					.getNewValue();
			for (Notifier newValue2 : newValues) {
				addAdapter(newValue2, false);
			}
			break;

		case Notification.REMOVE:
			// do anything here?
			break;

		case Notification.REMOVE_MANY:
			// do anything here?
			break;

		case Notification.MOVE:
			break;

		}
	}

	protected void handleResource(Resource resource, Notification notification) {
		switch (notification.getFeatureID(Resource.class)) {
		case Resource.RESOURCE__CONTENTS:
			if (!((Resource.Internal) resource).isLoading()) {
				int eventType = notification.getEventType();
				switch (eventType) {
				case Notification.SET:
				case Notification.UNSET:
					Notifier newValue = (Notifier) notification.getNewValue();
					if (newValue != null)
						addAdapter(newValue, false);
					break;
				case Notification.ADD:
					Notifier newValue1 = (Notifier) notification.getNewValue();
					addAdapter(newValue1, false);
					break;
				case Notification.ADD_MANY:
					@SuppressWarnings("unchecked")
					Collection<Notifier> newValues = (Collection<Notifier>) notification
							.getNewValue();
					for (Notifier newValue2 : newValues)
						addAdapter(newValue2, false);
					break;
				case Notification.REMOVE:
					// do anything?
					break;
				case Notification.REMOVE_MANY:
					// do anything?
					break;

				case Notification.MOVE:
					break;
				}
			}
		case Resource.RESOURCE__IS_LOADED:
			for (Notifier content : resource.getContents())
				addAdapter(content, false);
			break;
		}
	}

	protected void handleResourceSet(ResourceSet resourceSet,
			Notification notification) {
		if (notification.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
			switch (notification.getEventType()) {
			case Notification.ADD:
			case Notification.SET:
				// case Notification.REMOVE:
				Resource resource = (Resource) notification.getNewValue();
				addAdapter(resource, false);
				break;
			case Notification.ADD_MANY:
				// case Notification.REMOVE_MANY:
				@SuppressWarnings("unchecked")
				Collection<Resource> resources = (Collection<Resource>) notification
						.getNewValue();
				for (Resource resource1 : resources)
					addAdapter(resource1, false);
			}
		}

	}

	public boolean isAdapterForType(Object type) {
		return false;
	}

	protected boolean isObserved(Notifier not) {
		return not.eAdapters().contains(this);
	}

	public boolean isRecording() {
		return recording;
	}

	public boolean isResolveProxies() {
		return resolveProxies;
	}

	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof EObject) {
			Object feature = notification.getFeature();
			if (feature instanceof EReference) {
				EReference eReference = (EReference) feature;
				handleFeature(eReference,
						eReference.isContainment() ? eReference : null,
						notification, (EObject) notifier);
			} else if (feature != null) {
				handleFeature((EStructuralFeature) feature, null, notification,
						(EObject) notifier);
			}
		} else if (notifier instanceof Resource)
			handleResource((Resource) notifier, notification);
		else if (notifier instanceof ResourceSet)
			handleResourceSet((ResourceSet) notifier, notification);
	}

	protected void removeAdapter(Notifier notifier, boolean isRoot) {
		notifier.eAdapters().remove(this);
	}

	public void removeRootObject(Notifier obj) {
		rootObjects.remove(obj);
		if (recording)
			removeAdapter(obj, true);
	}

	public void setResolveProxies(boolean resolveProxies) {
		this.resolveProxies = resolveProxies;
	}

	public void setTarget(Notifier target) {
		if (!targetObjects.add(target))
			throw new IllegalStateException(
					"The target should not be set more than once");

		Iterator<?> contents = target instanceof EObject ? resolveProxies ? ((EObject) target)
				.eContents().iterator()
				: ((InternalEList<?>) ((EObject) target).eContents())
						.basicIterator()
				: target instanceof ResourceSet ? ((ResourceSet) target)
						.getResources().iterator()
						: target instanceof Resource ? ((Resource) target)
								.getContents().iterator() : null;

		if (contents != null)
			while (contents.hasNext()) {
				Notifier notifier = (Notifier) contents.next();
				addAdapter(notifier, false);
			}
	}

	public void unsetTarget(Notifier oldTarget) {
		targetObjects.remove(oldTarget);
	}

}
