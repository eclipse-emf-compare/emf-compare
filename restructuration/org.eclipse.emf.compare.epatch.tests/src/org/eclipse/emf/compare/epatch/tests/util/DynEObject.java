/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.tests.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class DynEObject {
	protected EObject obj;

	public DynEObject(EObject obj) {
		super();
		this.obj = obj;
	}

	public EObject eObj() {
		return obj;
	}

	public DynEObject(Resource res) {
		super();
		this.obj = res.getContents().get(0);
	}

	public EStructuralFeature eGetFeature(String name) {
		EStructuralFeature f = obj.eClass().getEStructuralFeature(name);
		if (f != null)
			return f;
		ArrayList<String> features = new ArrayList<String>();
		for (EStructuralFeature sf : obj.eClass().getEAllStructuralFeatures())
			features.add(sf.getName());
		throw new RuntimeException("Feature '" + name + "' not found. Available features: " + features
				+ " for class " + obj.eClass().getName());
	}

	public static DynEObject eGetResource(ResourceSet rs, String part) {
		for (Resource r : rs.getResources())
			if (r.getURI().toString().contains(part))
				return new DynEObject(r.getContents().get(0));
		throw new RuntimeException("Resource " + part + " not found!");
	};

	public static Resource eLoadResource(ResourceSet rs, Class<?> loader, String model) {
		final String path = loader.getPackage().getName().replace('.', '/');
		// final String file = "/" + path + "/" + model;
		// final URL url = loader.getResource(file);
		// if (url == null)
		// throw new RuntimeException(file + " not found on classpath.");
		// return rs.getResource(URI.createURI(url.toString()), true);
		URI file = URI.createURI(model);
		URL url = loader.getResource("/" + path + "/" + file);
		if (url != null)
			file = URI.createURI(url.toString());
		return rs.getResource(file, true);
	}

	public static ResourceSet eCreateResourceSet(Class<?> loader, String... models) {
		// final String path = loader.getPackage().getName().replace('.', '/');
		final ResourceSet rs = new ResourceSetImpl();
		Map<String, Object> map = rs.getResourceFactoryRegistry().getExtensionToFactoryMap();
		map.put("ecore", new XMIResourceFactoryImpl());
		map.put("xmi", new XMIResourceFactoryImpl());
		for (String m : models)
			eLoadResource(rs, loader, m);
		// URI file = URI.createURI(m);
		// URL url = loader.getResource("/" + path + "/" + file);
		// if (url != null)
		// file = URI.createURI(url.toString());
		// rs.getResource(file, true);
		// }
		return rs;
	}

	public DynEObject eGetObj(String feat, int index) {
		return toDynObj(listGet(obj.eGet(eGetFeature(feat)), index), feat);
	}

	@SuppressWarnings("unchecked")
	public DynEObject eGetObj(String feature, String matchFeature, String matchValue) {
		Object list = eGetVal(feature);
		if (list instanceof List) {
			for (EObject o : (List<EObject>)list) {
				EStructuralFeature f = o.eClass().getEStructuralFeature(matchFeature);
				if (f != null && matchValue.equals(o.eGet(f)))
					return new DynEObject(o);
			}
			return null;
		} else
			throw new RuntimeException("This is not a list: " + list);
	}

	@SuppressWarnings("unchecked")
	private Object listGet(Object list, int index) {
		if (list instanceof List)
			return ((List<Object>)list).get(index);
		throw new RuntimeException("This is not a list: " + list);
	}

	private DynEObject toDynObj(Object o, String feat) {
		if (o instanceof EObject)
			return new DynEObject((EObject)o);
		String t = (o != null) ? o.getClass().getName() : "null";
		throw new RuntimeException("The from " + obj.eClass().getName() + "." + feat
				+ " is not an EObject, but a " + t);

	}

	public DynEObject eGetObj(String feat) {
		return toDynObj(obj.eGet(eGetFeature(feat)), feat);
	}

	public DynEObject eUnset(String feat) {
		obj.eUnset(eGetFeature(feat));
		return this;
	}

	public DynEObject eSet(String feat, Object value) {
		obj.eSet(eGetFeature(feat), value);
		return this;
	}

	public DynEObject eSetNew(String feat, String typeName) {
		DynEObject o = newObj(typeName);
		obj.eSet(eGetFeature(feat), o.obj);
		return o;
	}

	public DynEObject eSetType(String feat, String typeName) {
		EClassifier o = eGetType(typeName);
		obj.eSet(eGetFeature(feat), o);
		return this;
	}

	public Object eGetVal(String feat) {
		return obj.eGet(eGetFeature(feat));
	}

	@SuppressWarnings("unchecked")
	public void eAdd(String feat, Object value) {
		((EList<Object>)obj.eGet(eGetFeature(feat))).add(value);
	}

	@SuppressWarnings("unchecked")
	public void eAdd(String feat, int index, Object value) {
		((EList<Object>)obj.eGet(eGetFeature(feat))).add(index, value);
	}

	@SuppressWarnings("unchecked")
	public void eSet(String feat, int index, Object value) {
		((EList<Object>)obj.eGet(eGetFeature(feat))).set(index, value);
	}

	@SuppressWarnings("unchecked")
	public void eMove(String feat, int newPosition, int oldPosition) {
		((EList<Object>)obj.eGet(eGetFeature(feat))).move(newPosition, oldPosition);
	}

	@SuppressWarnings("unchecked")
	public void eRemove(String feat, int index) {
		((EList<Object>)obj.eGet(eGetFeature(feat))).remove(index);
	}

	@SuppressWarnings("unchecked")
	public void eRemove(String feat, String feature, String matchValue) {
		EList<EObject> list = (EList<EObject>)obj.eGet(eGetFeature(feat));
		for (Iterator<EObject> i = list.iterator(); i.hasNext();) {
			EObject o = i.next();
			EStructuralFeature f = o.eClass().getEStructuralFeature(feature);
			if (f != null && matchValue.equals(o.eGet(f)))
				i.remove();
		}
	}

	public DynEObject eAddNew(String feat, String type) {
		DynEObject o = newObj(type);
		eAdd(feat, o.obj);
		return o;
	}

	public EClassifier eGetType(String typeName) {
		EPackage pkg = obj.eClass().getEPackage();
		EClassifier cls = pkg.getEClassifier(typeName);
		if (cls == null) {
			ArrayList<String> types = new ArrayList<String>();
			for (EClassifier c : pkg.getEClassifiers())
				types.add(c.getName());
			throw new RuntimeException("Type '" + typeName + "' not found in package " + pkg.getName()
					+ ". Available types:" + types + " Package namespace:" + pkg.getNsURI());
		}
		return cls;
	}

	public DynEObject newObj(String typeName) {
		EClassifier cls = eGetType(typeName);
		if (cls instanceof EClass) {
			return new DynEObject(cls.getEPackage().getEFactoryInstance().create((EClass)cls));
		} else
			throw new RuntimeException("Type '" + typeName + " is not an EClass, but an "
					+ cls.eClass().getName());
	}

}
