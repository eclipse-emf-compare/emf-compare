/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * This is a factory for an ecore metamodel. There is a factory by package. Each
 * factory is used to create instances of classifiers.
 * 
 * @author www.obeo.fr
 * 
 */
public class EFactory {

	/**
	 * Ecore factory
	 */
	protected Object factoryImpl = null;

	/**
	 * The identifier of the factory.
	 */
	protected String id = "";

	/**
	 * The class loader.
	 */
	protected ClassLoader loader;

	/**
	 * @return the identifier of the factory
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Constructor.
	 * 
	 * @param factoryId
	 *            is the identifier of the factory
	 * @param ePackage
	 *            is the package
	 * @param loader
	 *            is the class loader
	 * @throws FactoryException
	 */
	public EFactory(String factoryId, EPackage ePackage, ClassLoader loader) {
		factoryImpl = ePackage.getEFactoryInstance();
		id = factoryId;
		this.loader = loader;
	}

	/**
	 * Constructor.
	 * <p>
	 * Sample : new Factory("java.resources","Resources") creates an instance of
	 * the factory java.resources.ResourcesFactory
	 * 
	 * @param factoryId
	 *            is the identifier of the factory
	 * @param factoryShortName
	 *            is the factory short name
	 * @param loader
	 *            is the class loader
	 * @throws FactoryException
	 */
	public EFactory(String factoryId, String factoryShortName,
			ClassLoader loader) throws FactoryException {
		this.loader = loader;
		init(factoryId, factoryShortName, loader); // throws FactoryException
													// when error
	}

	private void init(String factoryId, String factoryShortName,
			ClassLoader loader) throws FactoryException {
		if (factoryId != null && factoryShortName != null
				&& factoryId.length() > 0 && factoryShortName.length() > 0) {
			// Class name
			String rPackageImplClassName = factoryId + "." + factoryShortName
					+ "Package";
			// Class loader
			try {
				// Class
				Class rPackageImplClass = Class.forName(rPackageImplClassName,
						true, loader);
				// Method
				Field rPackageImplField = rPackageImplClass
						.getField("eINSTANCE");
				Method rPackageImplGetRessourcesFactoryMethod = rPackageImplClass
						.getMethod("get" + factoryShortName + "Factory",
								new Class[] {});
				// Instances
				Object packageImpl = rPackageImplField.get(null);
				factoryImpl = rPackageImplGetRessourcesFactoryMethod.invoke(
						packageImpl, new Object[] {});
				id = factoryId;
			} catch (Exception e) {
				throw new FactoryException("Factory error : " + e.getMessage());
			}
		} else {
			throw new FactoryException("Factory not found : " + factoryId
					+ ".impl." + factoryShortName + "FactoryImpl");
		}
	}

	/**
	 * Returns the factory path.
	 * 
	 * @return the factory path
	 */
	public String getPath() {
		return id;
	}

	/**
	 * Creates an instance of the classifier whose name is given.
	 * <p>
	 * Sample : Creates an instance of java.resources.Folder if name equals
	 * "Folder" or "resources.Folder".
	 * 
	 * @param name
	 *            is the name of the classifier to be created
	 * @return the new EObject
	 * @throws FactoryException
	 */
	public EObject eCreate(String name) throws FactoryException {
		ClassLoader old = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(loader);
		try {
			String createName = "create" + name.substring(0, 1).toUpperCase()
					+ name.substring(1);
			return (EObject) eCall(factoryImpl, createName, null);
		} finally {
			Thread.currentThread().setContextClassLoader(old);
		}
	}

	private static Object eCall(Object object, String name, Object arg)
			throws FactoryException {
		try {
			Method method = object.getClass().getMethod(
					name,
					(arg != null) ? new Class[] { arg.getClass() }
							: new Class[] {});
			return method.invoke(object, (arg != null) ? new Object[] { arg }
					: new Object[] {});
		} catch (Exception e) {
			throw new FactoryException(e.getMessage());
		}
	}

	/**
	 * Sets the value of the given feature of the object to the new value.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name of the value to set
	 * @param arg
	 *            is the new value
	 * @throws FactoryException
	 */
	public static void eSet(EObject object, String name, Object arg)
			throws FactoryException {
		EStructuralFeature feature = eStructuralFeature(object, name);
		if (feature != null && feature.getEType() instanceof EEnum
				&& arg instanceof String) {
			try {
				Class c = Class.forName(ETools.getEClassifierPath(feature
						.getEType()));
				Method m = c.getMethod("get", new Class[] { String.class }); //$NON-NLS-1$
				arg = m.invoke(c, new Object[] { arg });
				object.eSet(feature, arg);
			} catch (Exception e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		} else {
			object.eSet(feature, arg);
		}
	}

	/**
	 * Sets the value of the given feature of the object to the new value.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name of the value to set
	 * @param arg
	 *            is the new value
	 * @param loader
	 *            is the specific classloader use to set the value
	 * @throws FactoryException
	 */
	public static void eSet(EObject object, String name, Object arg,
			ClassLoader loader) throws FactoryException {
		EStructuralFeature feature = eStructuralFeature(object, name);
		if (feature != null && feature.getEType() instanceof EEnum
				&& arg instanceof String) {
			try {
				Class c = loader.loadClass(ETools.getEClassifierPath(feature
						.getEType()));
				Method m = c.getMethod("get", new Class[] { String.class }); //$NON-NLS-1$
				arg = m.invoke(c, new Object[] { arg });
				object.eSet(feature, arg);
			} catch (Exception e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		} else {
			object.eSet(feature, arg);
		}
	}

	/**
	 * Adds the new value of the given feature of the object. If the structural
	 * feature isn't a list, it behaves like eSet.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name of the new value
	 * @param arg
	 *            is the new value
	 * @throws FactoryException
	 */
	public static void eAdd(EObject object, String name, Object arg)
			throws FactoryException {
		Object list = object.eGet(eStructuralFeature(object, name));
		if (list != null && list instanceof List) {
			if (arg != null) {
				((List) list).add(arg);
			}
		} else {
			eSet(object, name, arg);
		}
	}

	/**
	 * Removes the value of the given feature of the object. If the structural
	 * feature isn't a list, it behaves like eSet(object,name,null).
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name of the value
	 * @param arg
	 *            is the value to remove, null is allowed
	 * @throws FactoryException
	 */
	public static void eRemove(EObject object, String name, Object arg)
			throws FactoryException {
		Object list = object.eGet(eStructuralFeature(object, name));
		if (list != null && list instanceof List) {
			if (arg != null) {
				((List) list).remove(arg);
			}
		} else {
			eSet(object, name, null);
		}
	}

	/**
	 * Gets the value of the given feature of the object.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name, or a method defined on EObject like
	 *            'eClass', 'eResource', 'eContainer', 'eContainingFeature',
	 *            'eContainmentFeature', 'eContents', 'eAllContents',
	 *            'eCrossReferences'
	 * @return the value of the given feature of the object
	 * @throws FactoryException
	 */
	public static Object eGet(EObject object, String name)
			throws FactoryException {
		Object result;
		try {
			EStructuralFeature feature = eStructuralFeature(object, name);
			result = object.eGet(feature);
		} catch (FactoryException eGet) {
			try {
				result = eCall(object, name, null);
			} catch (FactoryException eCall) {
				throw eGet;
			}

		} catch (NullPointerException eCall) {
			return null;
		}
		if (result != null && result instanceof Enumerator) {
			return ((Enumerator) result).getName();
		} else if (result != null && result instanceof EDataTypeUniqueEList) {
			List list = new ArrayList();
			Iterator enums = ((EDataTypeUniqueEList) result).iterator();
			while (enums.hasNext()) {
				Object next = enums.next();
				if (next instanceof Enumerator) {
					list.add(((Enumerator) next).getName());
				} else {
					list.add(next);
				}
			}
			return list;
		} else {
			return result;
		}
	}

	/**
	 * Ecore factory.
	 */
	public static EcoreFactory ECORE = EcorePackageImpl.init()
			.getEcoreFactory();

	/**
	 * Gets the structural feature of the given feature name of the object.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the structural feature
	 * @throws FactoryException
	 */
	public static EStructuralFeature eStructuralFeature(EObject object,
			String name) throws FactoryException {
		EStructuralFeature structuralFeature = object.eClass()
				.getEStructuralFeature(name);
		if (structuralFeature != null) {
			return structuralFeature;
		} else {
			throw new FactoryException("The link '" + name
					+ "' doesn't exist in the class '"
					+ object.eClass().getName() + "'");
		}
	}

	/**
	 * Gets the value of the given feature of the object, as an EObject.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the value or null if it isn't an EObject
	 * @throws FactoryException
	 */
	public static EObject eGetAsEObject(EObject object, String name)
			throws FactoryException {
		Object eGet = eGet(object, name);
		if (eGet != null && eGet instanceof EObject)
			return (EObject) eGet;
		else
			return null;
	}

	/**
	 * Gets the value of the given feature of the object, as a String.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the value or null if it isn't a String
	 * @throws FactoryException
	 */
	public static String eGetAsString(EObject object, String name)
			throws FactoryException {
		Object eGet = eGet(object, name);
		if (eGet != null)
			return eGet.toString();
		else
			return null;
	}

	/**
	 * Gets the value of the given feature of the object, as a Boolean.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the value or null if it isn't a Boolean
	 * @throws FactoryException
	 */
	public static Boolean eGetAsBoolean(EObject object, String name)
			throws FactoryException {
		Object eGet = eGet(object, name);
		if (eGet != null && eGet instanceof Boolean)
			return (Boolean) eGet;
		else
			return null;
	}

	/**
	 * Gets the value of the given feature of the object, as an Integer.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the value or null if it isn't an Integer
	 * @throws FactoryException
	 */
	public static Integer eGetAsInteger(EObject object, String name)
			throws FactoryException {
		Object eGet = eGet(object, name);
		if (eGet != null && eGet instanceof Integer)
			return (Integer) eGet;
		else
			return null;
	}

	/**
	 * Gets the value of the given feature of the object, as a List.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return the value, or a new List with a single element if it isn't a
	 *         List, or null if it doesn't exist
	 * @throws FactoryException
	 */
	public static List eGetAsList(EObject object, String name)
			throws FactoryException {
		Object eGet = eGet(object, name);
		if (eGet != null) {
			if (eGet instanceof List) {
				return (List) eGet;
			} else {
				List list = new BasicEList(1);
				list.add(eGet);
				return list;
			}
		} else {
			return null;
		}
	}

	/**
	 * Indicates if the object is instance of the class whose name is given.
	 * <p>
	 * Samples :
	 * <p>
	 * An instance of java.resources.Folder return true if name equals "Folder"
	 * or "resources.Folder".
	 * <p>
	 * An instance of java.resources.Folder return true if name equals "File"
	 * and Folder inherits File.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the class name
	 * @return true if the object is instance of the class whose name is given
	 */
	public static boolean eInstanceOf(EObject object, String name) {
		if (object == null)
			return (name == null);
		return eInstanceOf(object.eClass(), name);
	}

	private static boolean eInstanceOf(EClass eClass, String name) {
		if (name.indexOf(".") == -1 && name.equals(eClass.getName())) {
			return true;
		} else {
			String instanceClassName = "." + eClass.getInstanceClassName();
			String endsWith = "." + name;
			if (instanceClassName.endsWith(endsWith)) {
				return true;
			} else {
				Iterator superTypes = eClass.getESuperTypes().iterator();
				while (superTypes.hasNext()) {
					EClass eSuperClass = (EClass) superTypes.next();
					if (eInstanceOf(eSuperClass, name))
						return true;
				}
				return false;
			}
		}
	}

	/**
	 * Indicates if the feature name given is valid for the object.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return true if the feature is defined, false if not
	 */
	public static boolean eValid(EObject object, String name) {
		try {
			eGet(object, name);
			return true;
		} catch (FactoryException e) {
			return false;
		}
	}

	/**
	 * Indicates if the object has a value for the feature name.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @return if the feature is a list, return feature.size() > 0 else return
	 *         feature != null
	 */
	public static boolean eExist(EObject object, String name) {
		try {
			Object eGet = eGet(object, name);
			if (eGet != null) {
				if (eGet instanceof List) {
					return ((List) eGet).size() > 0;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} catch (FactoryException e) {
			return false;
		}
	}

	/**
	 * Indicates if the object contains the given value for the feature name.
	 * 
	 * @param object
	 *            is the object
	 * @param name
	 *            is the feature name
	 * @param arg
	 *            is the value to find, null is allowed
	 * @return true if the object contains the given value for the feature name
	 */
	public static boolean eExist(EObject object, String name, Object arg) {
		try {
			Object eGet = eGet(object, name);
			if (eGet != null && eGet instanceof List) {
				return ((List) eGet).contains(arg);
			} else {
				return (eGet == arg);
			}
		} catch (FactoryException e) {
			return false;
		}
	}

}
