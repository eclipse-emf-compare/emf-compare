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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.epatch.AbstractEpatchBuilder;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.EpatchPackage;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.ModelImport;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectCopy;
import org.eclipse.emf.compare.epatch.ObjectNew;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.SingleAssignment;
import org.eclipse.emf.compare.epatch.recorder.EmfRecorder.RecorderListener;
import org.eclipse.emf.compare.epatch.util.EpatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchRecorder extends AbstractEpatchBuilder implements RecorderListener {
	protected Map<EObject, String> fragMap = new HashMap<EObject, String>();

	protected EmfRecorder recorder = new EmfRecorder(this);

	protected Map<Resource, NamedResource> resMap = new HashMap<Resource, NamedResource>();

	public EpatchRecorder(Notifier notifier, String name) {
		this(name);
		addRootObject(notifier);
		recorder.beginRecording();
	}

	public EpatchRecorder(String name) {
		super();
		epatch = createEpatch(name);
	}

	protected void addListAddAssignments(ListAssignment ass, Collection<AssignmentValue> addValues) {
		boolean c = isCreate((NamedObject)ass.eContainer());
		EList<AssignmentValue> list = c ? ass.getLeftValues() : ass.getRightValues();
		for (AssignmentValue newV : addValues)
			if (c)
				newV.setIndex(0);
			else
				for (AssignmentValue oldV : list)
					if (oldV.getIndex() >= newV.getIndex())
						oldV.setIndex(oldV.getIndex() + 1);
		list.addAll(addValues);
		if (!c)
			ECollections.sort(list, EpatchUtil.ASS_VAL_SORTER_ASC);
	}

	protected void addListRemoveAssignments(ListAssignment ass, Collection<AssignmentValue> values) {
		for (AssignmentValue newV : values) {
			int oldindex;
			for (AssignmentValue oldV : ass.getRightValues())
				if (oldV.getIndex() > newV.getIndex())
					oldV.setIndex(oldV.getIndex() - 1);
			List<AssignmentValue> vals = new ArrayList<AssignmentValue>(ass.getLeftValues());
			do {
				oldindex = newV.getIndex();
				for (Iterator<AssignmentValue> i = vals.iterator(); i.hasNext();)
					if (i.next().getIndex() <= newV.getIndex()) {
						newV.setIndex(newV.getIndex() + 1);
						i.remove();
					}
			} while (oldindex != newV.getIndex());
		}
		ass.getLeftValues().addAll(values);
		ECollections.sort(ass.getLeftValues(), EpatchUtil.ASS_VAL_SORTER_DESC);
	}

	public void addRootObject(Notifier obj) {
		recorder.addRootObject(obj);
		if (obj instanceof ResourceSet)
			for (Resource r : ((ResourceSet)obj).getResources())
				for (EObject o : r.getContents())
					addToFragMap(o);
		else if (obj instanceof Resource)
			for (EObject o : ((Resource)obj).getContents())
				addToFragMap(o);
		else if (obj instanceof EObject)
			addToFragMap((EObject)obj);
		else
			throw new RuntimeException("Unknown observable type " + obj.getClass());
	}

	protected void addToFragMap(EObject obj) {
		fragMap.put(obj, getFragment(obj));
		for (TreeIterator<EObject> i = obj.eAllContents(); i.hasNext();) {
			EObject o = i.next();
			fragMap.put(o, getFragment(o));
		}
	}

	protected void consolidateChanges() {
		consolidateResources();
		consolidateObjectRefs();
		sortLists();
		generateNames();
	}

	protected void consolidateObjectRefs() {
		for (Entry<EObject, NamedObject> e : objMap.entrySet()) {
			if (e.getValue() instanceof ObjectRef) {
				ObjectRef or = (ObjectRef)e.getValue();
				EObject eo = e.getKey();
				NamedResource nres = getResource(eo.eResource());
				String nfrag = eo.eResource().getURIFragment(eo);
				if (nres != or.getLeftRes() || !nfrag.equals(or.getLeftFrag())) {
					or.setRightRes(nres);
					or.setRightFrag(nfrag);
				}
			}
		}
		ECollections.sort(epatch.getObjects(), EpatchUtil.NAMED_OBJECT_SORTER);
	}

	protected void consolidateResources() {
		for (NamedResource r : epatch.getResources()) {
			if (r.getLeftUri() == null && r.getLeftRoot() == null)
				r.setLeftUri(newURI(r.getRightUri()));
			if (r.getRightUri() == null && r.getRightRoot() == null)
				r.setRightUri(newURI(r.getLeftUri()));
		}
	}

	public Epatch endRecording() {
		recorder.endRecording();
		consolidateChanges();
		return getRecorded();
	}

	protected AssignmentValue getAssignmentMoveValue(EStructuralFeature feat, int index, int refIndex) {
		AssignmentValue ass = fc.createAssignmentValue();
		ass.setIndex(index);
		ass.setRefIndex(refIndex);
		return ass;
	}

	@Override
	protected AssignmentValue getAssignmentValueEObject(EReference ref, EObject eobj) {
		AssignmentValue ass = fc.createAssignmentValue();
		// if (eobj.eIsProxy())
		// eobj = EcoreUtil.resolve(eobj, ref);
		if (ref.isContainment()) {
			if (isRemovedValue(eobj))
				ass.setRefObject(getObjectReadded(eobj));
			else
				ass.setNewObject(getObjectNew(eobj));
		} else if (recorder.isObserved(eobj)) {
			ass.setRefObject(getObjectRef(eobj));
		} else {
			ass.setImport(getImportRef(eobj));
			ass.setImpFrag(getFragment(eobj));
		}
		return ass;
	}

	protected ObjectNew getObjectNew(EObject obj) {
		ObjectNew o = (ObjectNew)objMap.get(obj);
		return o == null ? createObjectNew(obj) : o;
	}

	protected NamedObject getObjectReadded(EObject eobj) {
		NamedObject o = objMap.get(eobj);
		if (o instanceof ObjectRef)
			return o;
		else if (o instanceof ObjectNew) {
			objMap.remove(eobj);
			NamedObject r = getObjectRef(eobj);
			if (o.eContainer() instanceof AssignmentValue) {
				AssignmentValue oldAss = (AssignmentValue)o.eContainer();
				oldAss.eUnset(o.eContainmentFeature());
				oldAss.setRefObject(r);
			}
			removeIfNotNeededAnymore(((ObjectNew)o).getImport());
			removeUnneededObjectRefs();
			removeUnneededResources();
			return r;
		}
		throw new IllegalStateException();
	}

	protected NamedObject getObjectRef(EObject obj) {
		NamedObject o = objMap.get(obj);
		if (o == null) {
			ObjectRef r = fc.createObjectRef();
			r.setLeftRes(getResource(obj.eResource()));
			r.setLeftFrag(fragMap.get(obj));
			epatch.getObjects().add(r);
			objMap.put(obj, r);
			o = r;
		}
		return o;
	}

	protected Epatch getRecorded() {
		return epatch;
	}

	protected NamedResource getResource(Resource res) {
		NamedResource r = resMap.get(res);
		if (r == null) {
			r = fc.createNamedResource();
			r.setLeftUri(res.getURI().lastSegment());
			r.setName("res" + resMap.size());
			epatch.getResources().add(r);
			resMap.put(res, r);
		}
		return r;
	}

	public void handleFeature(EStructuralFeature feature, EReference containment, Notification notification,
			EObject object) {

		if (notification.isTouch() || feature.isTransient())
			return;

		if (ignoreFeature(feature))
			return;

		if (object.eResource() == null)
			throw new IllegalArgumentException("EObject has no resource");

		Object oldv = notification.getOldValue();
		Object newv = notification.getNewValue();
		int pos = notification.getPosition();
		NamedObject obj = getObjectRef(object);
		if (feature.isMany()) {
			ListAssignment a = getListAssignment(obj, feature);
			switch (notification.getEventType()) {
				case Notification.RESOLVE:
					break;
				case Notification.SET:
					addListRemoveAssignments(a, Collections.singleton(getListAssignmentValue(feature, oldv,
							pos)));
					addListAddAssignments(a, Collections
							.singleton(getListAssignmentValue(feature, newv, pos)));
					break;
				case Notification.UNSET:
					throw new RuntimeException("unhandeled Notification.UNSET for multi-features");
				case Notification.ADD:
					addListAddAssignments(a, Collections
							.singleton(getListAssignmentValue(feature, newv, pos)));
					break;
				case Notification.ADD_MANY:
					// TODO: implement
					throw new RuntimeException("unhandeled Notification.ADD_MANY for multi-features");
				case Notification.REMOVE:
					addListRemoveAssignments(a, Collections.singleton(getListAssignmentValue(feature, oldv,
							pos)));
					break;
				case Notification.REMOVE_MANY:
					// TODO: implement
					throw new RuntimeException("unhandeled Notification.REMOVE_MANY for multi-features");
				case Notification.MOVE:
					int oldPos = (Integer)oldv;
					addListRemoveAssignments(a, Collections.singleton(getAssignmentMoveValue(feature, oldPos,
							pos)));
					addListAddAssignments(a, Collections.singleton(getAssignmentMoveValue(feature, pos,
							oldPos)));
			}
		} else {
			SingleAssignment a = getSingleAssignment(obj, feature);
			switch (notification.getEventType()) {
				case Notification.RESOLVE:
					break;
				case Notification.SET:
					if (isCreate(obj))
						a.setLeftValue(getAssignmentValue(feature, newv));
					else {
						if (a.getLeftValue() == null)
							a.setLeftValue(getAssignmentValue(feature, oldv));
						a.setRightValue(getAssignmentValue(feature, newv));
					}
					break;
				case Notification.UNSET:
					if (isCreate(obj))
						a.setLeftValue(getAssignmentValue(feature, null));
					else {
						a.setLeftValue(getAssignmentValue(feature, oldv));
						a.setRightValue(getAssignmentValue(feature, null));
					}
					break;
				case Notification.ADD:
					throw new RuntimeException("unhandeled Notification.ADD for single-features");
				case Notification.ADD_MANY:
					throw new RuntimeException("unhandeled Notification.ADD_MANY for single-features");
				case Notification.REMOVE:
					throw new RuntimeException("unhandeled Notification.REMOVE for single-features");
				case Notification.REMOVE_MANY:
					throw new RuntimeException("unhandeled Notification.REMOVE_MANY for single-features");
				case Notification.MOVE:
					throw new RuntimeException("unhandeled Notification.MOVE for single-features");
			}
		}

	}

	protected boolean isCreate(NamedObject obj) {
		return obj instanceof CreatedObject;
	}

	protected boolean isRemovedValue(EObject obj) {
		NamedObject o = objMap.get(obj);
		if (o == null)
			return false;
		EReference r = o.eContainer().eContainmentFeature();
		EpatchPackage p = EpatchPackage.eINSTANCE;
		return r == p.getSingleAssignment_LeftValue() || r == p.getListAssignment_LeftValues();
	}

	protected String newURI(String uri) {
		int p = uri.lastIndexOf('.');
		if (p < 0)
			return uri + "1";
		else
			return uri.substring(0, p) + "1" + uri.substring(p);
	}

	protected void removeIfNotNeededAnymore(ModelImport imp) {
		for (TreeIterator<EObject> i = epatch.eAllContents(); i.hasNext();) {
			EObject o = i.next();
			if (o instanceof ObjectNew && ((ObjectNew)o).getImport() == imp)
				return;
			if (o instanceof AssignmentValue && ((AssignmentValue)o).getImport() == imp)
				return;
		}
		epatch.getModelImports().remove(imp);
	}

	protected void removeUnneededObjectRefs() {
		Set<NamedObject> refs = new HashSet<NamedObject>();
		for (TreeIterator<EObject> i = epatch.eAllContents(); i.hasNext();) {
			EObject o = i.next();
			if (o instanceof AssignmentValue) {
				NamedObject n = ((AssignmentValue)o).getRefObject();
				if (n != null)
					refs.add(n);
			}
		}
		for (Iterator<ObjectRef> i = epatch.getObjects().iterator(); i.hasNext();) {
			ObjectRef o = i.next();
			if (o.getAssignments().size() == 0 && !refs.contains(o))
				i.remove();
		}
	}

	protected void removeUnneededResources() {
		Set<NamedResource> refs = new HashSet<NamedResource>();
		for (TreeIterator<EObject> i = epatch.eAllContents(); i.hasNext();) {
			EObject o = i.next();
			if (o instanceof ObjectRef) {
				ObjectRef r = (ObjectRef)o;
				if (r.getLeftRes() != null)
					refs.add(r.getLeftRes());
				if (r.getRightRes() != null)
					refs.add(r.getRightRes());
			} else if (o instanceof ObjectCopy)
				refs.add(((ObjectCopy)o).getResource());
		}
		for (Iterator<NamedResource> i = epatch.getResources().iterator(); i.hasNext();) {
			NamedResource o = i.next();
			if (o.getLeftRoot() == null && o.getRightRoot() == null && !refs.contains(o))
				i.remove();
		}
	}
}
