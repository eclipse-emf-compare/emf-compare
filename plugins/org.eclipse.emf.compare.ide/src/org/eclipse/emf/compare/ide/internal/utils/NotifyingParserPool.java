/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This implementation of an XML parser pool will notify a list of {@link INamespaceDeclarationListener
 * namespace declaration listeners} of all namespaces declared in the parsed resource (xsi:schemalocation),
 * then a list of {@link IProxyCreationListener proxy creation listeners} of each and every proxy it sees
 * while loading an XML file as an EMF model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class NotifyingParserPool extends XMLParserPoolImpl {
	/** Only set containment reference values, ignore the rest. */
	protected final boolean containmentOnly;

	/** The list of parties interested by our proxies. */
	private ListenerList proxyListeners;

	/** The list of parties interested in the declaration of namespaces. */
	private ListenerList namespaceDeclarationListeners;

	/**
	 * Default constructor.
	 * 
	 * @param containmentOnly
	 *            only set containment reference values. The model will be mostly empty except for its
	 *            containment tree.
	 */
	public NotifyingParserPool(boolean containmentOnly) {
		super(true);
		this.proxyListeners = new ListenerList();
		this.namespaceDeclarationListeners = new ListenerList();
		this.containmentOnly = containmentOnly;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized XMLDefaultHandler getDefaultHandler(XMLResource resource, XMLLoad xmlLoad,
			XMLHelper helper, Map<?, ?> options) {
		final NotifyingXMLHelper wrapper = new NotifyingXMLHelper(helper, containmentOnly);
		for (Object listener : proxyListeners.getListeners()) {
			wrapper.addProxyListener((IProxyCreationListener)listener);
		}
		final XMLDefaultHandler handler = createDefaultHandler(resource, xmlLoad, wrapper, options);
		final NamespaceDeclarationNotifyingXMLDefaultHandler handlerWrapper = new NamespaceDeclarationNotifyingXMLDefaultHandler(
				handler);
		for (Object listener : namespaceDeclarationListeners.getListeners()) {
			handlerWrapper.addNamespaceDeclarationListener((INamespaceDeclarationListener)listener);
		}
		handlerWrapper.prepare(resource, wrapper, options);
		return handlerWrapper;
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
	 * Add a namespace declaration listener to this parser pool's list.
	 * 
	 * @param listener
	 *            The listener to add to this pool's list.
	 */
	public void addNamespaceDeclarationListener(INamespaceDeclarationListener listener) {
		namespaceDeclarationListeners.add(listener);
	}

	/**
	 * Remove a namespace declaration listener from this parser pool's list.
	 * 
	 * @param listener
	 *            The listener to remove from this pool's list.
	 */
	public void removeNamespaceDeclarationListener(INamespaceDeclarationListener listener) {
		namespaceDeclarationListeners.remove(listener);
	}

	/**
	 * An XMLDefaultHandler that will notify interested {@link INamespaceDeclarationListener listeners} of its
	 * namespace declarations.
	 */
	private static class NamespaceDeclarationNotifyingXMLDefaultHandler extends ForwardingXMLDefaultHandler {
		/** The list of parties interested in the declaration of namespaces. */
		private ListenerList namespaceDeclarationListeners;

		/** <code>true</code> only when we're parsing the very first element. */
		private boolean isRoot;

		/**
		 * Constructs a wrapper given its delegate.
		 * 
		 * @param delegate
		 *            The delegate handler.
		 */
		public NamespaceDeclarationNotifyingXMLDefaultHandler(XMLDefaultHandler delegate) {
			super(delegate);
			this.namespaceDeclarationListeners = new ListenerList();
		}

		@Override
		public void startDocument() throws SAXException {
			isRoot = true;
			super.startDocument();
		}

		@Override
		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			if (isRoot) {
				String xsiSchemaLocation = arg3.getValue(ExtendedMetaData.XSI_URI,
						XMLResource.SCHEMA_LOCATION);
				if (xsiSchemaLocation != null) {
					declareSchemaLocation(xsiSchemaLocation);
				}
				isRoot = false;
			}
			super.startElement(arg0, arg1, arg2, arg3);
		}

		/**
		 * Add a namespace declaration listener to this helper's list.
		 * 
		 * @param listener
		 *            The listener to add to this helper's list.
		 */
		public void addNamespaceDeclarationListener(INamespaceDeclarationListener listener) {
			namespaceDeclarationListeners.add(listener);
		}

		/**
		 * We've read the headers of the resource to load. Notify our {@link #namespaceDeclarationListeners
		 * listeners} about the schema locations that can be found therein.
		 * 
		 * @param xsiSchemaLocation
		 *            The String of xsi:schemalocation declarations in the file.
		 */
		private void declareSchemaLocation(String xsiSchemaLocation) {
			StringTokenizer stringTokenizer = new StringTokenizer(xsiSchemaLocation, " "); //$NON-NLS-1$
			while (stringTokenizer.hasMoreTokens()) {
				String key = stringTokenizer.nextToken();
				if (stringTokenizer.hasMoreTokens()) {
					String value = stringTokenizer.nextToken();
					URI uri = URI.createURI(value);
					for (Object listener : namespaceDeclarationListeners.getListeners()) {
						((INamespaceDeclarationListener)listener).schemaLocationDeclared(key, uri);
					}
				}
			}
		}
	}

	/**
	 * An XMLHelper wrapper that's capable of notifying {@link IProxyCreationListener listeners}s about proxy
	 * creations.
	 */
	private static class NotifyingXMLHelper extends ForwardingXMLHelper {
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
		public NotifyingXMLHelper(XMLHelper delegate, boolean containmentOnly) {
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
		 * Add a proxy creation listener to this helper's list.
		 * 
		 * @param listener
		 *            The listener to add to this helper's list.
		 */
		public void addProxyListener(IProxyCreationListener listener) {
			proxyListeners.add(listener);
		}
	}
}
