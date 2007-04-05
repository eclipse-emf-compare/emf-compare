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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.util.Diagnostician;


/**
 * This contains general support for ecore browsing.
 * @author www.obeo.fr
 *
 */
public class ETools {
	
	/**
	 * Returns a URI for the eObject, 
	 * i.e., either 
	 * the eProxyURI,
	 * the URI of the eResource with the fragment produced by the eResource,
	 * or the URI consisting of just the fragment that would be produced by a default Resource 
	 * with the eObject as its only contents.
	 * @param eObject the object for which to get the URI.
	 * @return the URI for the object.
	 */
	public static String getURI(EObject object){
		if (object.eResource() != null){
			return object.eResource().getURIFragment(object);
		}else{
			// inspired from EMF sources
			StringBuffer result = new StringBuffer("//");
			List uriFragmentPath = new ArrayList();
			for (EObject container = object.eContainer(); container != null; container = object.eContainer()){
				uriFragmentPath.add(((InternalEObject)container).eURIFragmentSegment(object.eContainmentFeature(), object));
				object = container;
			}
			int size = uriFragmentPath.size();
			if (size > 0){
				for (int i = size - 1;; --i){
					result.append((String)uriFragmentPath.get(i));
					if (i == 0){
						break;
					}else{
						result.append('/');
					}
				}
			}
			return result.toString();
		}
	}
	
	/**
	 * It validates the given EMF object. <p>
	 * An error is put in the log if the validation failed. <p>
	 * <li>!blocker || Diagnostic.OK => root</li>
	 * <li>blocker && !Diagnostic.OK => null</li>
	 * <li>root == null => null</li>
	 * @param root is the object to validate
	 * @param blocker indicates if the result must be Diagnostic.OK
	 * @param message is the error message to put in the log
	 * @return the given object or null
	 */
	public static EObject validate(EObject root, boolean blocker, String message){
		if (root != null && !(root.getClass().getName().startsWith("org.eclipse.uml2"))){
			if (Diagnostician.INSTANCE.validate(root).getSeverity() != Diagnostic.OK){
				EMFComparePlugin.getDefault().log(message,false);
				if (blocker){
					return null;
				}
			}
		}
		return root;
	}
	
	
	
	
	
	
	/**
	 * Search all the classifiers recursively in a package.
	 * @param ePackage is the container
	 * @return table of classifiers
	 */
	public static EClassifier[] computeAllClassifiers(EPackage ePackage){
		List classifiers = computeAllClassifiersList(ePackage);
		return (EClassifier[])classifiers.toArray(new EClassifier[]{});
	}
	
	/**
	 * Search all the classifiers recursively in a package.
	 * @param ePackage is the container
	 * @return list of classifiers
	 */
	public static List computeAllClassifiersList(EPackage ePackage){
		return computeAllClassifiersList(ePackage,false);
	}
	
	/**
	 * Search all the classifiers recursively in a package.
	 * @param ePackage is the container
	 * @param classOnly indicates that only the classes are kept
	 * @return list of classifiers
	 */
	public static List computeAllClassifiersList(EPackage ePackage, boolean classOnly){
		List classifiers = new BasicEList();
		if (ePackage != null) computeAllClassifiersList(ePackage,classifiers,classOnly);
		return classifiers;
	}
	private static void computeAllClassifiersList(EPackage ePackage, List all, boolean classOnly){
		Iterator classifiers = ePackage.getEClassifiers().iterator();
		while (classifiers.hasNext()) {
			EClassifier classifier = (EClassifier)classifiers.next();
			if (!classOnly){
				all.add(classifier);
			}else{
				if (classifier instanceof EClass && !((EClass)classifier).isAbstract() && !((EClass)classifier).isInterface()){
					all.add(classifier);
				}
			}
		}
		Iterator packages = ePackage.getESubpackages().iterator();
		while (packages.hasNext()) {
			computeAllClassifiersList((EPackage)packages.next(),all,classOnly);
		}
	}
	
	/**
	 * Search a classifier recursively in a package.
	 * <p>
	 * Remarks :
	 * <li>It never returns classifier java.resources.Folder if name = "File"</li>
	 * <li>It never returns classifier java.resources.Folder if name = "older"</li>
	 * <li>It returns classifier java.resources.Folder for "Folder" or "resources.Folder"</li>
	 * @param ePackage is the container
	 * @param name is the classifier identifier
	 * @return classifier or null if not found
	 */
	public static EClassifier getEClassifier(EPackage ePackage, String name){
		name = name.trim();
		EClassifier get = null;
		Iterator classifiers = ePackage.getEClassifiers().iterator();
		while (get == null && classifiers.hasNext()) {
			EClassifier classifier = (EClassifier)classifiers.next();
			String instanceClassName = "." + getEClassifierPath(classifier);
			String endsWith = "." + name;
			if (instanceClassName.endsWith(endsWith)){
				get = classifier;
			}
		}
		Iterator packages = ePackage.getESubpackages().iterator();
		while (get == null && packages.hasNext()) {
			EClassifier classifier = getEClassifier((EPackage)packages.next(),name);
			if (classifier != null) get = classifier;
		}
		return get;
	}
	
	/**
	 * Returns the feature with this classifier and this name.
	 * @param currentEClassifier is the classifier
	 * @param name is the feature name
	 * @return the feature
	 */
	public static EStructuralFeature getEStructuralFeature(EClassifier currentEClassifier, String name){
		if (currentEClassifier != null && currentEClassifier instanceof EClass){
			return getEStructuralFeature((EClass)currentEClassifier,name);
		}else{
			return null;
		}
	}
	
	/**
	 * Returns the feature with this class and this name.
	 * @param currentEClass is the class
	 * @param name is the feature name
	 * @return the feature
	 */
	public static EStructuralFeature getEStructuralFeature(EClass currentEClass, String name){
		name = name.trim();
		if (currentEClass != null){
			return currentEClass.getEStructuralFeature(name);
		}else{
			return null;
		}
	}
	
	/**
	 * Returns a factory name for a classifier. There is a factory by package.
	 * The factory is used to create instances of classifiers.
	 * <p> Sample : "ResourcesFactory" is the name of the factory
	 * 			 java.resources.ResourcesFactory
	 * @param eClassifier is the classifier
	 * @return the factory name
	 */
	public static String getEClassifierFactoryName(EClassifier eClassifier){
		return getEClassifierFactoryShortName(eClassifier) + "Factory";
	}
	
	/**
	 * Returns a factory short name for a classifier. There is a factory by package.
	 * The factory is used to create instances of classifiers.
	 * <p> Sample : "Resources" is the short name of the factory
	 * 			 java.resources.ResourcesFactory
	 * @param eClassifier is the classifier
	 * @return the factory short name
	 */
	public static String getEClassifierFactoryShortName(EClassifier eClassifier){
		if (eClassifier != null){
			EPackage p = eClassifier.getEPackage();
			String name = p.getName();
			if (name != null && name.length() > 0){
				return name.substring(0,1).toUpperCase() + name.substring(1);
			}
		}
		return "";
	}
	
	/**
	 * Returns the full path of the classifier.
	 * <p> Sample : "java.resources.JavaFile"
	 * 			is the full path for the classifier java.resources.JavaFile
	 * @param eClassifier is the classifier
	 * @return full path of the classifier
	 */
	public static String getEClassifierPath(EClassifier eClassifier){
		if (eClassifier != null){
			String instanceClassName = eClassifier.getInstanceClassName();
			String name = eClassifier.getName();
			if (eClassifier.getEPackage() != null){
				EPackage container = eClassifier.getEPackage();
				if (container != null && instanceClassName != null && instanceClassName.endsWith(container.getName() + "." + name)){
					return instanceClassName;
				}
				while (container != null){
					name = container.getName() + "." + name;
					container = container.getESuperPackage();
				}
			}
			return name;
		}else{
			return null;
		}
	}
	
	/**
	 * Returns the short path of the classifier.
	 * <p> Sample : "resources.JavaFile" is the short path
	 * 			for the classifier java.resources.JavaFile
	 * @param eClassifier is the classifier
	 * @return short path of the classifier
	 */
	public static String getEClassifierShortPath(EClassifier eClassifier){
		String name = eClassifier.getName();
		if (eClassifier.getEPackage() != null) name = eClassifier.getEPackage().getName() + "." + name;
		return name;
	}
	
	/**
	 * Creates a package and his children in an ecore model.
	 * The children are separated by '.'
	 * <p> Sample : "a.b.c" is a package full path,<p>
	 * 		"a", "b", and "c" packages are created, the root package "a" is returned.
	 * @param path is the package full path
	 * @return the root package
	 */
	public static EPackage createPackageHierarchy(String path) {
		EPackage ePackage = null;
		if (path != null && path.length() > 0){
			EcorePackage p = EcorePackageImpl.init();
			EcoreFactory factory = p.getEcoreFactory();
			EPackage parent = null;
			StringTokenizer st = new StringTokenizer(path,".");
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				EPackage child = factory.createEPackage();
				child.setName(name);
				if (parent != null)
					parent.getESubpackages().add(child);
				else
					ePackage = child;
				parent = child;
			}
		}
		return ePackage;
	}
	
	/**
	 * Get a package in a parent package.
	 * <p> Sample : "a.b" is a parent package and "c.d" is a relative path,<p>
	 * 		"a.b.c.d" package is returned.
	 * @param parent is the parent package
	 * @param path is the relative path of the required package
	 * @return the required package, null if not found
	 */
	public static EPackage getEPackage(EPackage parent, String path) {
		EPackage ePackage = parent;
		if (path != null && path.length() > 0){
			StringTokenizer st = new StringTokenizer(path,".");
			while (st.hasMoreTokens()) {
				String name = st.nextToken();
				boolean found = false;
				Iterator subPackages = ePackage.getESubpackages().iterator();
				while (!found && subPackages.hasNext()){
					EPackage subPackage = (EPackage)subPackages.next();
					if (subPackage.getName().equals(name)){
						found = true;
						ePackage = subPackage;
					}
				}
				if (!found) return null;
			}
		}
		return ePackage;
	}
	
	/**
	 * Indicates if an instance of the classifier is an instance of the type.
	 * @param classifier is the classifier
	 * @param type is the type
	 * @return true if an instance of the classifier is an instance of the type
	 */
	public static boolean ofType(EClassifier classifier, String type){
		if (classifier instanceof EClass){
			return ofType((EClass)classifier,type);
		}else{
			return ofClass(classifier,type);
		}
	}
	
	/**
	 * Indicates if an instance of the class is an instance of the type.
	 * @param eClass is the class
	 * @param type is the type
	 * @return true if an instance of the class is an instance of the type
	 */
	public static boolean ofType(EClass eClass, String type){
		if (ofClass(eClass,type)) return true;
		Iterator superTypes = eClass.getESuperTypes().iterator();
		while (superTypes.hasNext()) {
			EClassifier superType = (EClassifier) superTypes.next();
			if (ofType(superType,type)) return true;
		}
		return false;
	}
	
	/**
	 * Indicates if the type corresponds to the name of the classifier.
	 * @param classifier is the classifier
	 * @param type is the type
	 * @return true if the type corresponds to the name of the classifier
	 */
	public static boolean ofClass(EClassifier classifier, String type) {
		String path = ETools.getEClassifierPath(classifier);
		return ("." + path).endsWith("." + type);
	}
	
	
}
