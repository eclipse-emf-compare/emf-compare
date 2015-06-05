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

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This implementation of an {@link XMLDefaultHandler} will forward all calls to its delegate.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ForwardingXMLDefaultHandler extends DefaultHandler implements XMLDefaultHandler {
	/** The delegate to which we'll forward all calls. */
	protected final XMLDefaultHandler delegate;

	/**
	 * Creates our forwarding handler given its delegate.
	 * 
	 * @param delegate
	 *            The delegate instance to forward method calls to.
	 */
	public ForwardingXMLDefaultHandler(XMLDefaultHandler delegate) {
		this.delegate = delegate;
	}

	/**
	 * Returns the delegate instance that methods are forwarded to.
	 * 
	 * @return The delegate instance that methods are forwarded to.
	 */
	protected XMLDefaultHandler delegate() {
		return this.delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		delegate().characters(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endDocument() throws SAXException {
		delegate().endDocument();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		delegate().endElement(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		delegate().endPrefixMapping(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object arg0) {
		return delegate().equals(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(SAXParseException arg0) throws SAXException {
		delegate().error(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		delegate().fatalError(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return delegate().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		delegate().ignorableWhitespace(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notationDecl(String arg0, String arg1, String arg2) throws SAXException {
		delegate().notationDecl(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {
		delegate().processingInstruction(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputSource resolveEntity(String arg0, String arg1) throws IOException, SAXException {
		return delegate().resolveEntity(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDocumentLocator(Locator arg0) {
		delegate().setDocumentLocator(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void skippedEntity(String arg0) throws SAXException {
		delegate().skippedEntity(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startDocument() throws SAXException {
		delegate().startDocument();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
		delegate().startElement(arg0, arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		delegate().startPrefixMapping(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return delegate().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3) throws SAXException {
		delegate().unparsedEntityDecl(arg0, arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		delegate().warning(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
		delegate().comment(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endCDATA() throws SAXException {
		delegate().endCDATA();
	}

	/**
	 * {@inheritDoc}
	 */
	public void endDTD() throws SAXException {
		delegate().endDTD();
	}

	/**
	 * {@inheritDoc}
	 */
	public void endEntity(String arg0) throws SAXException {
		delegate().endEntity(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void startCDATA() throws SAXException {
		delegate().startCDATA();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
		delegate().startDTD(arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEntity(String arg0) throws SAXException {
		delegate().startEntity(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		delegate().reset();
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepare(XMLResource resource, XMLHelper helper, Map<?, ?> options) {
		delegate().prepare(resource, helper, options);
	}
}
