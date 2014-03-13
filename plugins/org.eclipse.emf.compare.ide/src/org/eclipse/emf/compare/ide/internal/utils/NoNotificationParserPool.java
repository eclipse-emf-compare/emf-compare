/*******************************************************************************
 * Copyright (c) 2012 Obeo.
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

import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLHandler;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;

/**
 * This implementation of an {@link org.eclipse.emf.ecore.xmi.XMLParserPool} aims at disabling all
 * notifications when the {@link XMLResource#OPTION_DISABLE_NOTIFY option} is set, including the notifications
 * at the very end of parsing.
 * <p>
 * This is one of the steps that allows EMF Compare to bypass UML's CacheAdapter when loading UML models from
 * the logical model.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class NoNotificationParserPool extends XMLParserPoolImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl#getDefaultHandler(org.eclipse.emf.ecore.xmi.XMLResource,
	 *      org.eclipse.emf.ecore.xmi.XMLLoad, org.eclipse.emf.ecore.xmi.XMLHelper, java.util.Map)
	 */
	@Override
	public synchronized XMLDefaultHandler getDefaultHandler(XMLResource resource, XMLLoad xmlLoad,
			XMLHelper helper, Map<?, ?> options) {
		final XMLDefaultHandler handler = super.getDefaultHandler(resource, xmlLoad, helper, options);
		if (handler instanceof XMLHandler) {
			return new NoNotificationXMLHandler((XMLHandler)handler, resource, helper, options);
		}
		return handler;
	}

	/**
	 * The only purpose of this xml handler is to disable the notifications that are sent from the default
	 * implementation of {@link #endDocument()} .
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class NoNotificationXMLHandler extends ForwardingXMLHandler {
		/**
		 * Creates this forwarding handler given its delegate. All other parameters are only used to call the
		 * mandatory super-constructor... but none should be of any use here.
		 * 
		 * @param delegate
		 *            Our delegate XMLHandler.
		 * @param xmlResource
		 *            The resource we'll be loading. Mandatory for the super-constructor, but we'll forward
		 *            all calls to {@code delegate} anyway.
		 * @param helper
		 *            The xml helper to use. Mandatory for the super-constructor, but we'll forward all calls
		 *            to {@code delegate} anyway.
		 * @param options
		 *            The load options that were specified. Mandatory for the super-constructor, but we'll
		 *            forward all calls to {@code delegate} anyway.
		 */
		public NoNotificationXMLHandler(XMLHandler delegate, XMLResource xmlResource, XMLHelper helper,
				Map<?, ?> options) {
			super(delegate, xmlResource, helper, options);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endDocument()
		 */
		@Override
		public void endDocument() {
			// prevent the sending of notifications
			setField("disableNotify", delegate(), Boolean.FALSE); //$NON-NLS-1$
			super.endDocument();
			setField("disableNotify", delegate(), Boolean.TRUE); //$NON-NLS-1$
		}
	}
}
