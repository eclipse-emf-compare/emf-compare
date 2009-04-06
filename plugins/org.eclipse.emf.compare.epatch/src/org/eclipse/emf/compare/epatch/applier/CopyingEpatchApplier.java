/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.applier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.epatch.Assignment;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.EPackageImport;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.ModelImport;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectCopy;
import org.eclipse.emf.compare.epatch.ObjectNew;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.ResourceImport;
import org.eclipse.emf.compare.epatch.SingleAssignment;
import org.eclipse.emf.compare.epatch.applier.EpatchMapping.EpatchMappingEntry;
import org.eclipse.emf.compare.epatch.util.EpatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class CopyingEpatchApplier {
	public class TriMap implements EpatchMapping {
		private Map<EObject, TriMapEntry> dst = new HashMap<EObject, TriMapEntry>();

		private Map<NamedObject, TriMapEntry> ptc = new HashMap<NamedObject, TriMapEntry>();

		private Map<EObject, TriMapEntry> src = new HashMap<EObject, TriMapEntry>();

		public Set<EpatchMappingEntry> getAllEntries() {
			Set<EpatchMappingEntry> entries = new HashSet<EpatchMappingEntry>(src.values());
			entries.addAll(dst.values());
			entries.addAll(ptc.values());
			return entries;
		}

		public EpatchMappingEntry getByDst(EObject dst) {
			return this.dst.get(dst);
		}

		public EpatchMappingEntry getByPtc(NamedObject ptc) {
			return this.ptc.get(ptc);
		}

		public EpatchMappingEntry getBySrc(EObject src) {
			return this.src.get(src);
		}

		public Map<EObject, EObject> getDstToSrcMap() {
			Map<EObject, EObject> map = new HashMap<EObject, EObject>();
			for (Entry<EObject, TriMapEntry> e : dst.entrySet())
				map.put(e.getKey(), e.getValue().getSrc());
			return map;
		}

		public Map<EObject, EObject> getSrcToDstMap() {
			Map<EObject, EObject> map = new HashMap<EObject, EObject>();
			for (Entry<EObject, TriMapEntry> e : src.entrySet())
				map.put(e.getKey(), e.getValue().getDst());
			return map;
		}

		public void put(EObject src, EObject dst, NamedObject ptc) {
			TriMapEntry e = new TriMapEntry(src, dst, ptc);
			if (src != null)
				this.src.put(src, e);
			if (dst != null)
				this.dst.put(dst, e);
			if (ptc != null)
				this.ptc.put(ptc, e);
		}

		public Map<NamedResource, Resource> getDstResources() {
			return outputResources;
		}

		public Map<ModelImport, Resource> getImportedResources() {
			return imports;
		}

		public Map<NamedResource, Resource> getSrcResources() {
			return inputResources;
		}

		public ApplyStrategy getStrategy() {
			return strategy;
		}
	}

	public class TriMapEntry implements EpatchMappingEntry {
		private EObject dst;

		private NamedObject ptc;

		private EObject src;

		public TriMapEntry(EObject src, EObject dst, NamedObject ptc) {
			super();
			this.src = src;
			this.dst = dst;
			this.ptc = ptc;
		}

		public EObject getDst() {
			return dst;
		}

		public NamedObject getPtc() {
			return ptc;
		}

		public EObject getSrc() {
			return src;
		}

		@Override
		public String toString() {
			StringBuffer b = new StringBuffer();
			b.append("src:");
			b.append(src == null ? "null" : src.eClass().getName() + "@"
					+ Integer.toHexString(src.hashCode()));
			b.append(" dst:");
			b.append(dst == null ? "null" : dst.eClass().getName() + "@"
					+ Integer.toHexString(dst.hashCode()));
			b.append(" ptc:");
			b.append(ptc == null ? "null" : ptc.eClass().getName());
			return b.toString();
		}

	}

	protected EpatchApplyStrategy dir;

	protected ApplyStrategy strategy;

	protected Epatch epatch;

	protected Map<ModelImport, Resource> imports;

	protected Map<NamedResource, Resource> inputResources;

	protected Map<NamedResource, Resource> outputResources;

	protected ResourceSet outputResourceSet;

	protected EpatchMapping triMap;

	public CopyingEpatchApplier(ApplyStrategy strategy, Epatch epatch, Map<ModelImport, Resource> imports,
			Map<NamedResource, Resource> inputResources, ResourceSet outputResourceSet) {
		super();
		this.strategy = strategy;
		this.dir = EpatchApplyStrategy.Util.get(strategy);
		this.epatch = epatch;
		this.imports = imports;
		this.inputResources = inputResources;
		this.outputResourceSet = outputResourceSet;
	}

	public CopyingEpatchApplier(ApplyStrategy strategy, Epatch epatch, ResourceSet inputResourceSet) {
		super();
		this.strategy = strategy;
		this.dir = EpatchApplyStrategy.Util.get(strategy);
		this.epatch = epatch;
		this.imports = matchImports(inputResourceSet);
		this.inputResources = matchResources(inputResourceSet);
		this.outputResourceSet = createOutputResourceSet(inputResourceSet);
	}

	public void apply() {
		createOutputResources();
		createNamedObjectMap();
		// printMaps();
		copyFeatures();
	}

	protected void copyFeatures() {
		for (NamedResource nr : epatch.getResources()) {
			Resource res = outputResources.get(nr);
			EObject src = inputResources.get(nr).getContents().get(0);
			res.getContents().add(getDestObject(src, dir.getOutputRoot(nr), true));
		}
	}

	protected void createNamedObjectMap() {
		triMap = new TriMap();
		for (NamedResource r : epatch.getResources()) {
			if (r.getRightRoot() != null)
				mapObject(r.getRightRoot());
		}
		for (ObjectRef nobj : epatch.getObjects()) {
			EObject eobj = getEObject(dir.getInputResource(nobj), dir.getInputFragment(nobj));
			mapObject(eobj, nobj);
			for (Assignment ass : nobj.getAssignments()) {
				if (ass instanceof SingleAssignment) {
					SingleAssignment sa = (SingleAssignment)ass;
					CreatedObject co = dir.getOutputValue(sa).getNewObject();
					if (co != null)
						mapObject(co);
				} else if (ass instanceof ListAssignment) {
					ListAssignment la = (ListAssignment)ass;
					for (AssignmentValue av : dir.getOutputValues(la)) {
						CreatedObject co = av.getNewObject();
						if (co != null)
							mapObject(co);
					}
				}
			}
		}
	}

	protected void createOutputResources() {
		outputResources = new HashMap<NamedResource, Resource>();
		for (NamedResource res : epatch.getResources())
			if (dir.getOutputURI(res) != null) {
				URI uri = URI.createURI(dir.getOutputURI(res));
				Resource r = outputResourceSet.createResource(uri);
				if (r == null)
					throw new RuntimeException("Failed to create resource for URI " + uri);
				outputResources.put(res, r);
			}
	}

	protected ResourceSet createOutputResourceSet(ResourceSet base) {
		ResourceSet rs = new ResourceSetImpl();
		rs.setPackageRegistry(base.getPackageRegistry());
		rs.setResourceFactoryRegistry(base.getResourceFactoryRegistry());
		return rs;
	}

	@SuppressWarnings("unchecked")
	protected Object getAssignmentValue(EStructuralFeature feat, AssignmentValue val) {
		if (val.getKeyword() != null)
			return null;
		if (val.getNewObject() != null)
			return getDestObject(null, val.getNewObject(), ((EReference)feat).isContainment());
		if (val.getValue() != null) {
			EDataType dt = (EDataType)feat.getEType();
			return dt.getEPackage().getEFactoryInstance().createFromString(dt, val.getValue());
		}
		if (val.getRefObject() != null) {
			EObject eobj = getDestObject(null, val.getRefObject(), ((EReference)feat).isContainment());
			if (val.getRefFeature() != null) {
				EStructuralFeature rf = eobj.eClass().getEStructuralFeature(val.getRefFeature());
				Object obj = eobj.eGet(rf);
				if (rf.isMany())
					return ((EList<Object>)obj).get(val.getRefIndex());
				else
					return obj;
			} else
				return eobj;
		}
		if (val.getImport() != null) {
			EObject e = getImport(val.getImport()).getEObject(val.getImpFrag());
			if (e == null)
				throw new RuntimeException("import not resolved!");
			return e;
		}
		return null;
	}

	protected EObject getDestObject(EObject src, NamedObject ptc, boolean init) {
		EpatchMappingEntry e;
		if (ptc == null) {
			e = triMap.getBySrc(src);
			if (e != null)
				ptc = e.getPtc();
		} else
			e = triMap.getByPtc(ptc);
		if (ptc == null) {
			EObject dst;
			if (e == null) {
				if (isExternal(src))
					return src;
				else
					dst = objectClone(src);
				triMap.put(src, dst, null);
			} else
				dst = e.getDst();
			if (init)
				objectClone(src, dst);
			return dst;
		} else {
			if (init) {
				if (e.getSrc() == null)
					objectCreate(e.getDst(), ptc);
				else
					objectModify(e.getSrc(), e.getDst(), ptc);
			}
			return e.getDst();
		}
	}

	protected EObject getEObject(NamedResource res, String fragment) {
		Resource r = inputResources.get(res);
		EObject o = r.getEObject(fragment);
		if (o != null)
			return o;
		throw new RuntimeException("EObject for " + fragment + " not found in " + r.getURI());
	}

	public Epatch getEpatch() {
		return epatch;
	}

	protected Resource getImport(ModelImport imp) {
		Resource r = imports.get(imp);
		if (r != null)
			return r;
		throw new RuntimeException("No Resource Found for import " + imp);
	}

	public EpatchMapping getMap() {
		return triMap;
	}

	public ResourceSet getOutputResourceSet() {
		return outputResourceSet;
	}

	protected boolean isExternal(EObject obj) {
		for (Resource r : inputResources.values())
			if (r == obj.eResource())
				return false;
		return true;
	}

	protected void mapObject(CreatedObject obj) {
		if (obj instanceof ObjectCopy) {
			ObjectCopy oc = (ObjectCopy)obj;
			EObject src = getEObject(oc.getResource(), oc.getFragment());
			EObject dst = objectClone(src);
			triMap.put(obj, dst, obj);
		} else if (obj instanceof ObjectNew) {
			ObjectNew on = (ObjectNew)obj;
			Resource res = getImport(on.getImport());
			EClass cls = (EClass)res.getEObject(on.getImpFrag());
			EObject dst = cls.getEPackage().getEFactoryInstance().create(cls);
			triMap.put(null, dst, obj);
		} else
			throw new RuntimeException("Unknown CreatObject: " + obj);
		for (Assignment ass : obj.getAssignments()) {
			if (ass instanceof SingleAssignment) {
				SingleAssignment sa = (SingleAssignment)ass;
				CreatedObject co = sa.getLeftValue().getNewObject();
				if (co != null)
					mapObject(co);
			} else if (ass instanceof ListAssignment) {
				ListAssignment la = (ListAssignment)ass;
				for (AssignmentValue av : la.getLeftValues()) {
					CreatedObject co = av.getNewObject();
					if (co != null)
						mapObject(co);
				}
			}
		}

	}

	protected void mapObject(EObject src, ObjectRef obj) {
		triMap.put(src, objectClone(src), obj);
	}

	protected Map<ModelImport, Resource> matchImports(ResourceSet rs) {
		Map<ModelImport, Resource> map = new HashMap<ModelImport, Resource>();
		for (ModelImport imp : epatch.getModelImports())
			if (imp instanceof ModelImport)
				map.put(imp, matchImports(rs, imp));
		return map;
	}

	protected Resource matchImports(ResourceSet rs, ModelImport imp) {
		if (imp instanceof EPackageImport) {
			EPackageImport ei = (EPackageImport)imp;
			EPackage pkg = rs.getPackageRegistry().getEPackage(ei.getNsURI());
			if (pkg != null)
				return pkg.eResource();
			for (Resource r : rs.getResources())
				for (EObject o : r.getContents())
					if (o instanceof EPackage) {
						EPackage p = (EPackage)o;
						if (ei.getNsURI().equals(p.getNsURI()))
							return r;
					}
		} else if (imp instanceof ResourceImport) {
			ResourceImport ri = (ResourceImport)imp;
			Resource res = rs.getResource(URI.createURI(ri.getUri()), true);
			if (res != null)
				return res;
			for (Resource r : rs.getResources())
				if (r.getURI() != null && r.getURI().toString().endsWith(ri.getUri())) {
					return r;
				}
		}
		throw new RuntimeException("No Resource found in ResourceSet for Import :" + imp + " ResourceSet:"
				+ rs);
	}

	protected boolean matchResource(NamedResource res, Resource resources) {
		for (ObjectRef obj : epatch.getObjects()) {
			if (dir.getInputResource(obj) != res)
				continue;
			EObject o = resources.getEObject(dir.getInputFragment(obj));
			if (o == null)
				return false;
			for (Assignment ass : obj.getAssignments()) {
				EStructuralFeature f = o.eClass().getEStructuralFeature(ass.getFeature());
				if (f == null)
					return false;
			}
		}
		return true;
	}

	protected Resource matchResource(NamedResource res, ResourceSet resources) {
		for (Resource r : resources.getResources())
			if (matchResource(res, r))
				return r;
		throw new RuntimeException("No Resource found in ResourceSet for " + dir.getInputURI(res));
	}

	protected Map<NamedResource, Resource> matchResources(ResourceSet input) {
		Map<NamedResource, Resource> map = new HashMap<NamedResource, Resource>();
		for (NamedResource r : epatch.getResources())
			map.put(r, matchResource(r, input));
		return map;
	}

	protected EObject objectClone(EObject obj) {
		// if (isExternal(obj))
		// throw new RuntimeException("Trying to clon external object");
		EClass cls = obj.eClass();
		return cls.getEPackage().getEFactoryInstance().create(cls);
	}

	protected void objectClone(EObject src, EObject dst) {
		for (EStructuralFeature f : src.eClass().getEAllStructuralFeatures()) {
			if (!f.isChangeable() || f.isDerived() || f.isTransient() || !src.eIsSet(f))
				continue;
			objectCloneFeature(src, dst, f);
		}
	}

	@SuppressWarnings("unchecked")
	protected void objectCloneFeature(EObject src, EObject dst, EStructuralFeature f) {
		if (f.isMany()) {
			EList<Object> s = (EList<Object>)src.eGet(f);
			EList<Object> d = (EList<Object>)dst.eGet(f);
			for (Object o : s)
				d.add(objectCopyValue(f, o));
		} else
			dst.eSet(f, objectCopyValue(f, src.eGet(f)));
	}

	protected Object objectCopyValue(EStructuralFeature feature, Object src) {
		if (feature instanceof EReference) {
			EReference r = (EReference)feature;
			return getDestObject((EObject)src, null, r.isContainment());
		} else
			return src;
	}

	@SuppressWarnings("unchecked")
	protected void objectCreate(EObject dst, NamedObject ptc) {
		for (Assignment ass : ptc.getAssignments()) {
			EStructuralFeature f = dst.eClass().getEStructuralFeature(ass.getFeature());
			if (ass instanceof ListAssignment) {
				ListAssignment li = (ListAssignment)ass;
				EList<Object> vals = (EList<Object>)dst.eGet(f);
				for (AssignmentValue av : li.getLeftValues())
					vals.add(getAssignmentValue(f, av));
			} else if (ass instanceof SingleAssignment) {
				SingleAssignment si = (SingleAssignment)ass;
				dst.eSet(f, getAssignmentValue(f, si.getLeftValue()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void objectModify(EObject src, EObject dst, NamedObject ptc) {
		Map<String, Assignment> assmap = new HashMap<String, Assignment>();
		for (Assignment ass : ptc.getAssignments())
			assmap.put(ass.getFeature(), ass);
		for (EStructuralFeature f : src.eClass().getEAllStructuralFeatures()) {
			if (!f.isChangeable() || f.isDerived() || f.isTransient())
				continue;
			Assignment ass = assmap.get(f.getName());
			if (!src.eIsSet(f) && ass == null)
				continue;
			if (ass instanceof ListAssignment) {
				objectModifyMergeLists(f, (EList<Object>)src.eGet(f), (EList<Object>)dst.eGet(f),
						(ListAssignment)ass);
			} else if (ass instanceof SingleAssignment) {
				SingleAssignment si = (SingleAssignment)ass;
				dst.eSet(f, getAssignmentValue(f, dir.getOutputValue(si)));
			} else
				objectCloneFeature(src, dst, f);
		}
	}

	protected void objectModifyMergeLists(EStructuralFeature fest, EList<Object> src, EList<Object> dst,
			ListAssignment ass) {
		ArrayList<Object> items = new ArrayList<Object>(src);

		// backup values that are to be moved
		Map<AssignmentValue, Object> backup = new HashMap<AssignmentValue, Object>();
		for (AssignmentValue a : dir.getOutputValues(ass))
			if (a.getRefObject() == null && a.getKeyword() == null && a.getNewObject() == null
					&& a.getValue() == null && a.getImport() == null)
				backup.put(a, src.get(a.getRefIndex()));

		// remove input values
		ArrayList<AssignmentValue> toRemove = new ArrayList<AssignmentValue>(dir.getInputValues(ass));
		Collections.sort(toRemove, EpatchUtil.ASS_VAL_SORTER_DESC);
		for (AssignmentValue i : toRemove)
			items.remove(i.getIndex());

		// get copied values
		for (int i = 0; i < items.size(); i++)
			items.set(i, objectCopyValue(fest, items.get(i)));

		// add output values
		ArrayList<AssignmentValue> toAdd = new ArrayList<AssignmentValue>(dir.getOutputValues(ass));
		Collections.sort(toAdd, EpatchUtil.ASS_VAL_SORTER_ASC);
		for (AssignmentValue i : toAdd) {
			Object o = backup.get(i);
			if (o == null)
				o = getAssignmentValue(fest, i);
			else
				o = objectCopyValue(fest, o);
			items.add(i.getIndex(), o);
		}

		dst.addAll(items);
	}

	protected void printMaps() {
		System.out.println("inputResources:");
		for (Entry<NamedResource, Resource> e : inputResources.entrySet())
			System.out.println(e.getKey().getName() + " -> " + e.getValue().getURI());

		System.out.println("outputResources:");
		for (Entry<NamedResource, Resource> e : outputResources.entrySet())
			System.out.println(e.getKey().getName() + " -> " + e.getValue().getURI());

		System.out.println("triMap:");
		for (EpatchMappingEntry e : triMap.getAllEntries())
			System.out.println(e);

	}

}
