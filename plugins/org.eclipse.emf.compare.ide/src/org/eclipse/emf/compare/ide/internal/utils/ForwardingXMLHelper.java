/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.DanglingHREFException;
import org.eclipse.emf.ecore.xmi.NameInfo;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;

/**
 * This implementation of an {@link XMLHelper} will forward all calls to its delegate.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ForwardingXMLHelper implements XMLHelper {
	/** The actual helper we'll delegate all calls to. */
	private final XMLHelper delegate;

	/**
	 * Default constructor.
	 * 
	 * @param delegate
	 *            The actual helper we'll delegate all calls to.
	 */
	public ForwardingXMLHelper(XMLHelper delegate) {
		this.delegate = delegate;
	}

	/** {@inheritDoc} */
	public void setOptions(Map<?, ?> options) {
		delegate.setOptions(options);
	}

	/** {@inheritDoc} */
	public void setNoNamespacePackage(EPackage pkg) {
		delegate.setNoNamespacePackage(pkg);
	}

	/** {@inheritDoc} */
	public EPackage getNoNamespacePackage() {
		return delegate.getNoNamespacePackage();
	}

	/** {@inheritDoc} */
	public void setAnySimpleType(EClass type) {
		delegate.setAnySimpleType(type);
	}

	/** {@inheritDoc} */
	public void setXMLMap(XMLMap map) {
		delegate.setXMLMap(map);
	}

	/** {@inheritDoc} */
	public XMLMap getXMLMap() {
		return delegate.getXMLMap();
	}

	/** {@inheritDoc} */
	public void setExtendedMetaData(ExtendedMetaData extendedMetaData) {
		delegate.setExtendedMetaData(extendedMetaData);
	}

	/** {@inheritDoc} */
	public ExtendedMetaData getExtendedMetaData() {
		return delegate.getExtendedMetaData();
	}

	/** {@inheritDoc} */
	public XMLResource getResource() {
		return delegate.getResource();
	}

	/** {@inheritDoc} */
	public Object getValue(EObject eObject, EStructuralFeature eStructuralFeature) {
		return delegate.getValue(eObject, eStructuralFeature);
	}

	/** {@inheritDoc} */
	public String getName(ENamedElement eNamedElement) {
		return delegate.getName(eNamedElement);
	}

	/** {@inheritDoc} */
	public String getQName(EClass eClass) {
		return delegate.getQName(eClass);
	}

	/** {@inheritDoc} */
	public void populateNameInfo(NameInfo nameInfo, EClass eClass) {
		delegate.populateNameInfo(nameInfo, eClass);
	}

	/** {@inheritDoc} */
	public String getQName(EDataType eDataType) {
		return delegate.getQName(eDataType);
	}

	/** {@inheritDoc} */
	public void populateNameInfo(NameInfo nameInfo, EDataType eDataType) {
		delegate.populateNameInfo(nameInfo, eDataType);
	}

	/** {@inheritDoc} */
	public String getQName(EStructuralFeature feature) {
		return delegate.getQName(feature);
	}

	/** {@inheritDoc} */
	public void populateNameInfo(NameInfo nameInfo, EStructuralFeature feature) {
		delegate.populateNameInfo(nameInfo, feature);
	}

	/** {@inheritDoc} */
	public String getPrefix(String namespaceURI) {
		return delegate.getPrefix(namespaceURI);
	}

	/** {@inheritDoc} */
	public String getPrefix(EPackage ePackage) {
		return delegate.getPrefix(ePackage);
	}

	/** {@inheritDoc} */
	public String getNamespaceURI(String prefix) {
		return delegate.getNamespaceURI(prefix);
	}

	/** {@inheritDoc} */
	public List<String> getPrefixes(EPackage ePackage) {
		return delegate.getPrefixes(ePackage);
	}

	/** {@inheritDoc} */
	public String getID(EObject eObject) {
		return delegate.getID(eObject);
	}

	/** {@inheritDoc} */
	public String getIDREF(EObject eObject) {
		return delegate.getIDREF(eObject);
	}

	/** {@inheritDoc} */
	public String getHREF(EObject eObject) {
		return delegate.getHREF(eObject);
	}

	/** {@inheritDoc} */
	public URI deresolve(URI uri) {
		return delegate.deresolve(uri);
	}

	/** {@inheritDoc} */
	public EPackage[] packages() {
		return delegate.packages();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated
	 */
	@Deprecated
	public EObject createObject(EFactory eFactory, String name) {
		return delegate.createObject(eFactory, name);
	}

	/** {@inheritDoc} */
	public EObject createObject(EFactory eFactory, EClassifier type) {
		return delegate.createObject(eFactory, type);
	}

	/** {@inheritDoc} */
	public EClassifier getType(EFactory eFactory, String typeName) {
		return delegate.getType(eFactory, typeName);
	}

	/** {@inheritDoc} */
	public void setValue(EObject eObject, EStructuralFeature eStructuralFeature, Object value, int position) {
		delegate.setValue(eObject, eStructuralFeature, value, position);
	}

	/** {@inheritDoc} */
	public EStructuralFeature getFeature(EClass eClass, String namespaceURI, String name) {
		return delegate.getFeature(eClass, namespaceURI, name);
	}

	/** {@inheritDoc} */
	public EStructuralFeature getFeature(EClass eClass, String namespaceURI, String name, boolean isElement) {
		return delegate.getFeature(eClass, namespaceURI, name, isElement);
	}

	/** {@inheritDoc} */
	public int getFeatureKind(EStructuralFeature feature) {
		return delegate.getFeatureKind(feature);
	}

	/** {@inheritDoc} */
	public String getXMLEncoding(String javaEncoding) {
		return delegate.getXMLEncoding(javaEncoding);
	}

	/** {@inheritDoc} */
	public String getJavaEncoding(String xmlEncoding) {
		return delegate.getJavaEncoding(xmlEncoding);
	}

	/** {@inheritDoc} */
	public List<XMIException> setManyReference(ManyReference reference, String location) {
		return delegate.setManyReference(reference, location);
	}

	/** {@inheritDoc} */
	public void setCheckForDuplicates(boolean checkForDuplicates) {
		delegate.setCheckForDuplicates(checkForDuplicates);
	}

	/** {@inheritDoc} */
	public void setProcessDanglingHREF(String value) {
		delegate.setProcessDanglingHREF(value);
	}

	/** {@inheritDoc} */
	public DanglingHREFException getDanglingHREFException() {
		return delegate.getDanglingHREFException();
	}

	/** {@inheritDoc} */
	public URI resolve(URI relative, URI base) {
		return delegate.resolve(relative, base);
	}

	/** {@inheritDoc} */
	public void addPrefix(String prefix, String uri) {
		delegate.addPrefix(prefix, uri);
	}

	/** {@inheritDoc} */
	public Map<String, String> getAnyContentPrefixToURIMapping() {
		return delegate.getAnyContentPrefixToURIMapping();
	}

	/** {@inheritDoc} */
	public void recordPrefixToURIMapping() {
		delegate.recordPrefixToURIMapping();
	}

	/** {@inheritDoc} */
	public String getURI(String prefix) {
		return delegate.getURI(prefix);
	}

	/** {@inheritDoc} */
	public void pushContext() {
		delegate.pushContext();
	}

	/** {@inheritDoc} */
	public void popContext() {
		delegate.popContext();
	}

	/** {@inheritDoc} */
	public void popContext(Map<String, EFactory> prefixesToFactories) {
		delegate.popContext(prefixesToFactories);
	}

	/** {@inheritDoc} */
	public String convertToString(EFactory factory, EDataType dataType, Object data) {
		return delegate.convertToString(factory, dataType, data);
	}

	/** {@inheritDoc} */
	public EMap<String, String> getPrefixToNamespaceMap() {
		return delegate.getPrefixToNamespaceMap();
	}

	/** {@inheritDoc} */
	public void setPrefixToNamespaceMap(EMap<String, String> prefixToNamespaceMap) {
		delegate.setPrefixToNamespaceMap(prefixToNamespaceMap);
	}

	/** {@inheritDoc} */
	public void setMustHavePrefix(boolean mustHavePrefix) {
		delegate.setMustHavePrefix(mustHavePrefix);
	}
}
