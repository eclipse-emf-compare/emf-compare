/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 521886
 *******************************************************************************/
package org.eclipse.emf.compare.domain.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.impl.ChangeDescriptionImpl;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This custom implementation of a change recorder will avoid copying the internal lists every time they're
 * needed as they can grow to very large sizes along with the size of the compared models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareChangeRecorder extends ChangeRecorder {
	/**
	 * The set used to register notifiers and reduce the size of the "originalTargetObjects" list before using
	 * it in the consolidateChanges() method.
	 */
	private Set<Notifier> setOriginalTargetObjects = new HashSet<Notifier>();

	/**
	 * Overrides the superclass method to ignore the "originalTargetObjets" list since we never resume
	 * recording a paused change description.
	 */
	@SuppressWarnings("nls") // copy/pasted from super
	@Override
	public void setTarget(Notifier target) {
		if (!targetObjects.add(target)) {
			throw new IllegalStateException("The target should not be set more than once");
		}

		if (target instanceof EObject) {
			EObject targetEObject = (EObject)target;
			if (resolveProxies) {
				for (EObject eObject : targetEObject.eContents()) {
					addAdapter(eObject);
				}
			} else {
				Iterator<EObject> contents = ((InternalEList<EObject>)targetEObject.eContents())
						.basicIterator();
				while (contents.hasNext()) {
					// Avoid adding the adapter to unresolved proxies
					// so that the proxies don't look like objects that have become orphans.
					//
					EObject eObject = contents.next();
					if (!eObject.eIsProxy()) {
						addAdapter(eObject);
					}
				}
			}

			handleTarget(targetEObject);
		} else {
			Iterator<?> contents;
			if (target instanceof ResourceSet) {
				contents = ((ResourceSet)target).getResources().iterator();
			} else if (target instanceof Resource) {
				contents = ((Resource)target).getContents().iterator();
			} else {
				contents = null;
			}

			if (contents != null) {
				while (contents.hasNext()) {
					Notifier notifier = (Notifier)contents.next();
					addAdapter(notifier);
				}
			}
		}
	}

	/**
	 * The "setOriginalTargetObjects" Set is used to register target objects instead of the
	 * "originalTargetObjects" list to avoid duplicates, and fill this list with the bare minimum afterwards,
	 * for optimization purposes.
	 */
	@Override
	protected void handleFeature(EStructuralFeature feature, EReference containment,
			Notification notification, EObject eObject) {
		Object oldValue = notification.getOldValue();
		if (oldValue instanceof Notifier) {
			setOriginalTargetObjects.add((Notifier)oldValue);
		}
		super.handleFeature(feature, containment, notification, eObject);
	}

	/**
	 * {@inheritDoc} This avoids growing the "originalTargetObjects" and "setOriginalTargetObjects"
	 * collections indefinitely during the same comparison each time a merge is executed.
	 */
	@Override
	public void beginRecording(ChangeDescription changeDescription, Collection<?> rootObjects) {
		originalTargetObjects.clear();
		setOriginalTargetObjects.clear();
		super.beginRecording(changeDescription, rootObjects);
	}

	/**
	 * {@inheritDoc} See bug 562321. Some Sirius objects that changed during merging may become orphans and
	 * were removed by the org.eclipse.emf.ecore.change.util.ChangeRecorder#consolidateChanges() method. So we
	 * fill the originalTargetObjects list with these orphaned objects to keep them.
	 */
	@Override
	protected void consolidateChanges() {
		for (Notifier target : setOriginalTargetObjects) {
			if (target instanceof EObject) {
				if (isOrphan((EObject)target)) {
					originalTargetObjects.add(target);
				}
			}
		}
		super.consolidateChanges();
	}

	/*
	 * See bug 521886. This avoids the problem that the ChangeDescription acts as a container for removed
	 * objects resulting in UML's CacheAdapter attaching to the change description and degrading performance.
	 * Therefore, we specialize the change description to not attach adapters at all.
	 */
	@Override
	protected ChangeDescription createChangeDescription() {
		return new ChangeDescriptionImpl() {
			@Override
			public EList<Adapter> eAdapters() {
				return new EAdapterList<Adapter>(this) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean add(Adapter object) {
						return false;
					}

					@Override
					public void add(int index, Adapter object) {
					}

					@Override
					public boolean addAll(Collection<? extends Adapter> collection) {
						return false;
					}
				};
			}
		};
	}
}
