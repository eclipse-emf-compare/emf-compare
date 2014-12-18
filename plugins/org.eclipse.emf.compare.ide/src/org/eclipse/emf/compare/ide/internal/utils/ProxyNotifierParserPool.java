/*******************************************************************************
 * Copyright (c) 2014 Obeo.
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

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.DanglingHREFException;
import org.eclipse.emf.ecore.xmi.NameInfo;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;

/**
 * This implementation of an XML parser pool will notify a list of {@link IProxyCreationListener proxy
 * creation listeners} of each and every proxy it sees while loading an XML file as an EMF model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ProxyNotifierParserPool extends XMLParserPoolImpl {
	/** Only set containment reference values, ignore the rest. */
	protected final boolean containmentOnly;

	/** The list of parties interested by our proxies. */
	private ListenerList proxyListeners;

	/**
	 * Default constructor.
	 * 
	 * @param containmentOnly
	 *            only set containment reference values. The model will be mostly empty except for its
	 *            containment tree.
	 */
	public ProxyNotifierParserPool(boolean containmentOnly) {
		super(true);
		this.proxyListeners = new ListenerList();
		this.containmentOnly = containmentOnly;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized XMLDefaultHandler getDefaultHandler(XMLResource resource, XMLLoad xmlLoad,
			XMLHelper helper, Map<?, ?> options) {
		final ProxyNotifierXMLHelper wrapper = new ProxyNotifierXMLHelper(helper, containmentOnly);
		for (Object listener : proxyListeners.getListeners()) {
			wrapper.addProxyListener((IProxyCreationListener)listener);
		}
		final XMLDefaultHandler handler = createDefaultHandler(resource, xmlLoad, wrapper, options);
		handler.prepare(resource, wrapper, options);
		return handler;
	}

	/**
	 * Create the default (unwrapped) XMLDefaultHandler. This is merely a call to <code>super</code> but can
	 * be sub-classed.
	 * 
	 * @param resource
	 *            The resource to load.
	 * @param xmlLoad
	 *            The XML load to pass on tho the handler.
	 * @param helper
	 *            The XML helper to pass on tho the handler.
	 * @param options
	 *            The load options for this resource.
	 * @return The created XMLDefaultHandler.
	 * @see #getDefaultHandler(XMLResource, XMLLoad, XMLHelper, Map)
	 */
	protected XMLDefaultHandler createDefaultHandler(XMLResource resource, XMLLoad xmlLoad, XMLHelper helper,
			Map<?, ?> options) {
		return super.getDefaultHandler(resource, xmlLoad, helper, options);
	}

	/**
	 * Add a proxy creation listener to this parser pool's list.
	 * 
	 * @param listener
	 *            The listener to add to this pool's list.
	 */
	public void addProxyListener(IProxyCreationListener listener) {
		proxyListeners.add(listener);
	}

	/**
	 * Remove a proxy creation listener from this parser pool's list.
	 * 
	 * @param listener
	 *            The listener to remove from this pool's list.
	 */
	public void removeProxyListener(IProxyCreationListener listener) {
		proxyListeners.remove(listener);
	}

	/**
	 * Describes the contract for a proxy creation listener as can be notifier from this pool's created
	 * parsers.
	 */
	public interface IProxyCreationListener {
		/**
		 * This will be called when a proxy is created from one of the parser pool's parsers.
		 * 
		 * @param source
		 *            The resource in which a proxy has been created towards another.
		 * @param eObject
		 *            The EObject on which some feature is going to be set with a proxy value.
		 * @param eStructuralFeature
		 *            The structural feature which value will contain a proxy.
		 * @param proxy
		 *            The actual proxy created for this eObject's feature.
		 * @param position
		 *            Position at which the proxy is going to be inserted. This will be set to <code>-1</code>
		 *            when the proxy is added at the end of the <code>eStructuralFeature</code>'s values list
		 *            (for multi-valued features) or if said feature is single-valued.
		 */
		void proxyCreated(Resource source, EObject eObject, EStructuralFeature eStructuralFeature,
				EObject proxy, int position);
	}

	/**
	 * An XMLHelper wrapper that's capable of notifying {@link IProxyCreationListener listeners}s about proxy
	 * creations.
	 */
	private static class ProxyNotifierXMLHelper extends ForwardingXMLHelper {
		/** The list of parties interested by our proxy creations. */
		private final ListenerList proxyListeners;

		/** Only set containment reference values, ignore the rest. */
		private final boolean containmentOnly;

		/**
		 * Constructs a wrapper given its delegate XMLHelper.
		 * 
		 * @param delegate
		 *            The delegate XMLHelper.
		 * @param containmentOnly
		 *            Only set containment reference values.
		 */
		public ProxyNotifierXMLHelper(XMLHelper delegate, boolean containmentOnly) {
			super(delegate);
			this.proxyListeners = new ListenerList();
			this.containmentOnly = containmentOnly;
		}

		/** {@inheritDoc} */
		@Override
		public void setValue(EObject eObject, EStructuralFeature eStructuralFeature, Object value,
				int position) {
			if (!containmentOnly
					|| (eStructuralFeature instanceof EReference && ((EReference)eStructuralFeature)
							.isContainment())) {
				super.setValue(eObject, eStructuralFeature, value, position);
			}
			if (value instanceof EObject && ((EObject)value).eIsProxy()) {
				for (Object listener : proxyListeners.getListeners()) {
					((IProxyCreationListener)listener).proxyCreated(getResource(), eObject,
							eStructuralFeature, (EObject)value, position);
				}
			}
		}

		/**
		 * Add a proxy creation listener to this parser pool's list.
		 * 
		 * @param listener
		 *            The listener to add to this pool's list.
		 */
		public void addProxyListener(IProxyCreationListener listener) {
			proxyListeners.add(listener);
		}
	}

	/** A delegating XMLHelper. */
	private static class ForwardingXMLHelper implements XMLHelper {
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
		public void setValue(EObject eObject, EStructuralFeature eStructuralFeature, Object value,
				int position) {
			delegate.setValue(eObject, eStructuralFeature, value, position);
		}

		/** {@inheritDoc} */
		public EStructuralFeature getFeature(EClass eClass, String namespaceURI, String name) {
			return delegate.getFeature(eClass, namespaceURI, name);
		}

		/** {@inheritDoc} */
		public EStructuralFeature getFeature(EClass eClass, String namespaceURI, String name,
				boolean isElement) {
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
}
