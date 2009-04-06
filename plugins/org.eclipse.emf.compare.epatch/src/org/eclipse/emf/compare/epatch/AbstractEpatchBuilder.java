/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.epatch.util.EpatchUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public abstract class AbstractEpatchBuilder {
	protected static class NamingTool<T> {
		private String def;

		private Map<String, List<T>> nameMap = new HashMap<String, List<T>>();

		private Map<T, String> objMap = new HashMap<T, String>();

		public NamingTool(String def) {
			super();
			this.def = def;
		}

		public void fixRemainingConflicts() {
			List<List<T>> c = getConflicts();
			while (c.size() > 0) {
				for (List<T> cl : c) {
					for (int i = 0; i < cl.size(); i++)
						put(objMap.get(cl.get(i)) + i, cl.get(i));
				}
				c = getConflicts();
			}
		}

		public List<List<T>> getConflicts() {
			List<List<T>> r = new ArrayList<List<T>>();
			for (List<T> l : nameMap.values())
				if (l.size() > 1)
					r.add(new ArrayList<T>(l));
			return r;
		}

		public Map<T, String> getMap() {
			return objMap;
		}

		public String getName(T obj) {
			return objMap.get(obj);
		}

		public void put(String name, T obj) {
			if (name == null || "".equals(name))
				name = def;
			String oldName = objMap.get(obj);
			if (oldName != null)
				nameMap.get(oldName).remove(obj);
			List<T> lst = nameMap.get(name);
			if (lst == null)
				nameMap.put(name, lst = new ArrayList<T>());
			lst.add(obj);
			objMap.put(obj, name);
		}
	}

	protected static EpatchFactory fc = EpatchFactory.eINSTANCE;

	protected static String NULL = "null";

	protected Epatch epatch;

	protected Map<URI, ModelImport> importMap = new HashMap<URI, ModelImport>();

	protected Map<EObject, NamedObject> objMap = new HashMap<EObject, NamedObject>();

	protected Epatch createEpatch(String name) {
		Epatch patch = fc.createEpatch();
		patch.setName(name);
		Resource r = new ResourceImpl(URI.createURI(name + ".epatch"));
		r.getContents().add(patch);
		new ResourceSetImpl().getResources().add(r);
		return patch;
	}

	protected ObjectNew createObjectNew(EObject obj) {
		ObjectNew o = fc.createObjectNew();
		objMap.put(obj, o);
		o.setImpFrag(getFragment(obj.eClass()));
		o.setImport(getImportRef(obj.eClass()));
		for (EStructuralFeature f : obj.eClass().getEAllStructuralFeatures()) {
			if (f.isTransient() || !obj.eIsSet(f))
				continue;
			if (f.isMany()) {
				ListAssignment ass = getListAssignment(o, f);
				for (Object v : (List<?>)obj.eGet(f))
					ass.getLeftValues().add(getAssignmentValue(f, v));
			} else {
				SingleAssignment ass = getSingleAssignment(o, f);
				ass.setLeftValue(getAssignmentValue(f, obj.eGet(f)));
			}
		}
		return o;
	}

	protected void generateNames() {
		Set<NamedObject> needName = new HashSet<NamedObject>();
		for (TreeIterator<EObject> i = epatch.eAllContents(); i.hasNext();) {
			EObject o = i.next();
			if (o instanceof AssignmentValue) {
				AssignmentValue la = (AssignmentValue)o;
				if (la.getRefObject() != null)
					needName.add(la.getRefObject());
			}
		}
		NamingTool<EObject> nt = new NamingTool<EObject>("obj");
		for (Entry<EObject, NamedObject> e : objMap.entrySet())
			if (needName.contains(e.getValue()))
				nt.put(getNameForObj(e.getKey()), e.getKey());
		for (List<EObject> c : nt.getConflicts())
			for (EObject o : c)
				if (o.eContainer() != null)
					nt.put(getNameForObj(o.eContainer()) + nt.getName(o), o);
		nt.fixRemainingConflicts();
		for (Entry<EObject, String> e : nt.getMap().entrySet())
			objMap.get(e.getKey()).setName(e.getValue());
	}

	protected AssignmentValue getAssignmentValue(EStructuralFeature feat, Object value) {
		if (value == null)
			return getAssignmentValueNull();
		else if (feat instanceof EAttribute)
			return getAssignmentValueDataType((EAttribute)feat, value);
		else if (feat instanceof EReference)
			return getAssignmentValueEObject((EReference)feat, (EObject)value);
		throw new RuntimeException("UnknownfFeature type:" + feat);
	}

	protected AssignmentValue getAssignmentValueDataType(EAttribute attr, Object value) {
		AssignmentValue ass = fc.createAssignmentValue();
		EFactory f = attr.getEType().getEPackage().getEFactoryInstance();
		String strVal = f.convertToString(attr.getEAttributeType(), value);
		ass.setValue(strVal);
		return ass;
	}

	protected abstract AssignmentValue getAssignmentValueEObject(EReference ref, EObject eobj);

	protected AssignmentValue getAssignmentValueNull() {
		AssignmentValue ass = fc.createAssignmentValue();
		ass.setKeyword(NULL);
		return ass;
	}

	protected String getFragment(EObject obj) {
		if (obj.eIsProxy())
			return ((InternalEObject)obj).eProxyURI().fragment();
		return obj.eResource().getURIFragment(obj);
	}

	protected String getImportName(String base) {
		base = base.replaceAll("[^a-zA-Z]", " ").trim().replace(' ', '_');
		base = base.length() == 0 ? "obj" : base;
		boolean found;
		int counter = -1;
		String name;
		do {
			name = counter++ == -1 ? base : base + counter;
			found = false;
			for (ModelImport i : epatch.getModelImports())
				if (i instanceof ModelImport && i.getName().equals(name)) {
					found = true;
					break;
				}
		} while (found);
		return name;
	}

	protected ListAssignment getListAssignment(NamedObject obj, EStructuralFeature feat) {
		for (Assignment a : obj.getAssignments())
			if (a instanceof ListAssignment && a.getFeature().equals(feat.getName()))
				return (ListAssignment)a;

		ListAssignment a = fc.createListAssignment();
		a.setFeature(feat.getName());
		obj.getAssignments().add(a);
		return a;
	}

	protected AssignmentValue getListAssignmentValue(EStructuralFeature feat, Object value, int index) {
		AssignmentValue ass = getAssignmentValue(feat, value);
		ass.setIndex(index);
		return ass;
	}

	protected String getNameForObj(EObject obj) {
		EStructuralFeature f = obj.eClass().getEStructuralFeature("name");
		if (f instanceof EAttribute && obj.eIsSet(f))
			return obj.eGet(f) + "";
		return null;
	}

	protected ModelImport getImportRef(EObject obj) {
		URI uri = obj.eIsProxy() ? ((InternalEObject)obj).eProxyURI().trimFragment() : obj.eResource()
				.getURI();
		ModelImport imp = importMap.get(uri);
		if (imp != null)
			return imp;
		if (obj.eIsProxy()) {
			ResourceImport ri = fc.createResourceImport();
			ri.setName(getImportName(uri.lastSegment()));
			ri.setUri(uri.toString());
			imp = ri;
		} else {
			EObject p = obj.eResource().getContents().get(0);
			if (p instanceof EPackage
					&& ("file".equals(uri.scheme()) || ((EPackage)p).getNsURI().equals(uri.toString()))) { // FIXME
				// :
				// find a
				// better
				// criteria
				// for this
				// decision
				EPackageImport ei = fc.createEPackageImport();
				EPackage ep = (EPackage)p;
				ei.setName(getImportName(ep.getNsPrefix()));
				ei.setNsURI(ep.getNsURI());
				imp = ei;
			} else {
				ResourceImport ri = fc.createResourceImport();
				ri.setName(getImportName(uri.lastSegment()));
				ri.setUri(obj.eResource().getURI().toString());
				imp = ri;
			}
		}
		importMap.put(uri, imp);
		epatch.getModelImports().add(imp);
		return imp;
	}

	protected SingleAssignment getSingleAssignment(NamedObject obj, EStructuralFeature feat) {
		for (Assignment a : obj.getAssignments())
			if (a instanceof SingleAssignment && a.getFeature().equals(feat.getName()))
				return (SingleAssignment)a;

		SingleAssignment a = fc.createSingleAssignment();
		a.setFeature(feat.getName());
		obj.getAssignments().add(a);
		return a;
	}

	protected boolean ignoreFeature(EStructuralFeature feat) {
		return feat == EcorePackage.eINSTANCE.getEClass_EGenericSuperTypes()
				|| feat == EcorePackage.eINSTANCE.getETypedElement_EGenericType();
	}

	protected void sortAssignmentValue(AssignmentValue av) {
		if (av != null && av.getNewObject() != null)
			sortNamedObject(av.getNewObject());
	}

	protected void sortLists() {
		ECollections.sort(epatch.getObjects(), EpatchUtil.NAMED_OBJECT_SORTER);
		for (ObjectRef r : epatch.getObjects())
			sortNamedObject(r);
	}

	protected void sortNamedObject(NamedObject obj) {
		ECollections.sort(obj.getAssignments(), EpatchUtil.ASS_SORTER);
		for (Assignment a : obj.getAssignments()) {
			if (a instanceof ListAssignment) {
				ListAssignment la = (ListAssignment)a;
				ECollections.sort(la.getLeftValues(), EpatchUtil.ASS_VAL_SORTER_DESC);
				ECollections.sort(la.getRightValues(), EpatchUtil.ASS_VAL_SORTER_ASC);
				for (AssignmentValue av : la.getLeftValues())
					sortAssignmentValue(av);
				for (AssignmentValue av : la.getRightValues())
					sortAssignmentValue(av);
			} else {
				SingleAssignment sa = (SingleAssignment)a;
				sortAssignmentValue(sa.getLeftValue());
				sortAssignmentValue(sa.getRightValue());
			}
		}
	}
}
