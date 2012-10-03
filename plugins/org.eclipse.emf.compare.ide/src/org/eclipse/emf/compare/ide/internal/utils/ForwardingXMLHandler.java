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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This implementation of an {@link XMLHandler} will forward all calls to its delegate.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ForwardingXMLHandler extends XMLHandler {
	/**
	 * The cache in which we'll store the methods accessed through reflection. Note that this is most likely
	 * never used.
	 */
	private static final Map<String, Method> METHOD_CACHE = Maps.newHashMap();

	/**
	 * The cache in which we'll store the fields we access through reflection. Note that this is only used by
	 * a single field in this implementation.
	 */
	private static final Cache<String, Field> FIELD_CACHE = CacheBuilder.newBuilder().build(
			new CacheLoader<String, Field>() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see com.google.common.cache.CacheLoader#load(java.lang.Object)
				 */
				@Override
				public Field load(String key) throws Exception {
					final Field field = findField(key);
					// Make it accessible right off the bat
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							field.setAccessible(true);
							return null;
						}
					});
					return field;
				}
			});

	/** The delegate to which we'll forward all calls. */
	protected XMLHandler delegate;

	/**
	 * Creates this forwarding handler given its delegate. All other parameters are only used to call the
	 * mandatory super-constructor... but none should be of any use here.
	 * 
	 * @param delegate
	 *            Our delegate XMLHandler.
	 * @param xmlResource
	 *            The resource we'll be loading. Mandatory for the super-constructor, but we'll forward all
	 *            calls to {@code delegate} anyway.
	 * @param helper
	 *            The xml helper to use. Mandatory for the super-constructor, but we'll forward all calls to
	 *            {@code delegate} anyway.
	 * @param options
	 *            The load options that were specified. Mandatory for the super-constructor, but we'll forward
	 *            all calls to {@code delegate} anyway.
	 */
	public ForwardingXMLHandler(XMLHandler delegate, XMLResource xmlResource, XMLHelper helper,
			Map<?, ?> options) {
		super(xmlResource, helper, options);
		this.delegate = delegate;
	}

	/**
	 * Returns the delegate instance that methods are forwarded to.
	 * 
	 * @return The delegate instance that methods are forwarded to.
	 */
	protected XMLHandler delegate() {
		return this.delegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endDocument()
	 */
	@Override
	public void endDocument() {
		delegate().endDocument();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) {
		delegate().characters(ch, start, length);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#comment(char[], int, int)
	 */
	@Override
	public void comment(char[] ch, int start, int length) {
		delegate().comment(ch, start, length);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endCDATA()
	 */
	@Override
	public void endCDATA() {
		delegate().endCDATA();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endDTD()
	 */
	@Override
	public void endDTD() {
		delegate().endDTD();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endElement(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name) {
		delegate().endElement(uri, localName, name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endEntity(java.lang.String)
	 */
	@Override
	public void endEntity(String name) {
		delegate().endEntity(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	public void endPrefixMapping(String prefix) {
		delegate().endPrefixMapping(prefix);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return delegate().equals(obj);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException e) throws SAXException {
		delegate().error(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#error(org.eclipse.emf.ecore.xmi.XMIException)
	 */
	@Override
	public void error(XMIException e) {
		delegate().error(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		delegate().fatalError(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#fatalError(org.eclipse.emf.ecore.xmi.XMIException)
	 */
	@Override
	public void fatalError(XMIException e) {
		delegate().fatalError(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return delegate().hashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		delegate().ignorableWhitespace(ch, start, length);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#notationDecl(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void notationDecl(String name, String publicId, String systemId) throws SAXException {
		delegate().notationDecl(name, publicId, systemId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#prepare(org.eclipse.emf.ecore.xmi.XMLResource,
	 *      org.eclipse.emf.ecore.xmi.XMLHelper, java.util.Map)
	 */
	@Override
	public void prepare(XMLResource resource, XMLHelper xmlHelper, Map<?, ?> options) {
		delegate().prepare(resource, xmlHelper, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#processingInstruction(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data) {
		delegate().processingInstruction(target, data);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#reset()
	 */
	@Override
	public void reset() {
		delegate().reset();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#resolveEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
		return delegate().resolveEntity(publicId, systemId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		delegate().setDocumentLocator(locator);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#skippedEntity(java.lang.String)
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		delegate().skippedEntity(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startCDATA()
	 */
	@Override
	public void startCDATA() {
		delegate().startCDATA();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startDocument()
	 */
	@Override
	public void startDocument() {
		delegate().startDocument();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startDTD(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void startDTD(String name, String publicId, String systemId) {
		delegate().startDTD(name, publicId, systemId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void startElement(String uri, String localName, String name) {
		delegate().startElement(uri, localName, name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		delegate().startElement(uri, localName, qName, attributes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startEntity(java.lang.String)
	 */
	@Override
	public void startEntity(String name) {
		delegate().startEntity(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri) {
		delegate().startPrefixMapping(prefix, uri);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return delegate().toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#unparsedEntityDecl(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
			throws SAXException {
		delegate().unparsedEntityDecl(name, publicId, systemId, notationName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException e) throws SAXException {
		delegate().warning(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#warning(org.eclipse.emf.ecore.xmi.XMIException)
	 */
	@Override
	public void warning(XMIException e) {
		delegate().warning(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#getXSIType()
	 */
	@Override
	protected String getXSIType() {
		final String key = "getXSIType1"; //$NON-NLS-1$
		return (String)reflectiveCall(key, delegate(), "getXSIType"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#handleObjectAttribs(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void handleObjectAttribs(EObject obj) {
		final String key = "handleObjectAttribs1"; //$NON-NLS-1$
		reflectiveCall(key, delegate(), "handleObjectAttribs", obj); //$NON-NLS-1$
	}

	/**
	 * Calls a given method through reflection after setting it
	 * {@link java.lang.reflect.AccessibleObject#setAccessible(boolean) accessible}.
	 * 
	 * @param key
	 *            The key of the bucket in which the Method object is stored within {@link #METHOD_CACHE}.
	 * @param target
	 *            Target upon which we should invoke this method.
	 * @param methodName
	 *            Name of the method we are to call.
	 * @param params
	 *            Parameters of the invocation.
	 * @return Result of the invocation.
	 */
	protected static Object reflectiveCall(String key, Object target, String methodName, Object... params) {
		Method method = METHOD_CACHE.get(key);
		if (method == null) {
			Class<?>[] paramTypes = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++) {
				paramTypes[i] = params[i].getClass();
			}
			final Method temp = findMethod(methodName, paramTypes);
			if (temp != null) {
				// We'll make it accessible right now
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						temp.setAccessible(true);
						return null;
					}
				});
				METHOD_CACHE.put(key, temp);
			}
			method = temp;
		}

		try {
			if (method != null) {
				return method.invoke(target, params);
			} else {
				throw new RuntimeException(new NoSuchMethodException("Could not find method " + methodName //$NON-NLS-1$
						+ " on " + XMLHandler.class.getName())); //$NON-NLS-1$
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the given field through reflection after setting it
	 * {@link java.lang.reflect.AccessibleObject#setAccessible(boolean) accessible}.
	 * 
	 * @param fieldName
	 *            Name of the field we should set.
	 * @param target
	 *            The actual object which field we are to set.
	 * @param value
	 *            Value to which this field should be set.
	 */
	protected static void setField(String fieldName, Object target, Object value) {
		try {
			final Field field = FIELD_CACHE.get(fieldName);
			if (field != null) {
				field.set(target, value);
			} else {
				throw new RuntimeException(new NoSuchFieldException("Could not find field " + fieldName //$NON-NLS-1$
						+ " on " + XMLHandler.class.getName())); //$NON-NLS-1$
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Finds a field of the given name on {@link XMLHandler}.
	 * 
	 * @param fieldName
	 *            Name of the sought field.
	 * @return The field we were seeking if we could locate it.
	 */
	private static Field findField(String fieldName) {
		final Field[] fields = XMLHandler.class.getDeclaredFields();
		Field result = null;
		for (int i = 0; i < fields.length && result == null; i++) {
			final Field candidate = fields[i];
			if (fieldName.equals(candidate.getName())) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * Finds a method of the given name on {@link XMLHandler}.
	 * 
	 * @param methodName
	 *            Name of the sought method.
	 * @param paramTypes
	 *            Types of the parameters of the method we seek.
	 * @return The method we were seeking if we could locate it.
	 */
	private static Method findMethod(String methodName, Class<?>... paramTypes) {
		final Method[] methods = XMLHandler.class.getDeclaredMethods();
		Method result = null;
		for (int i = 0; i < methods.length && result == null; i++) {
			final Method candidate = methods[i];
			if (methodName.equals(candidate.getName())
					&& equalArrays(paramTypes, candidate.getParameterTypes())) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * Checks whether the two given arrays are equal. Contrarily to
	 * {@link java.util.Arrays#equals(Object[], Object[])}, this will consider an empty array to be equal to
	 * <code>null</code>.
	 * 
	 * @param a1
	 *            First of the two arrays to compare.
	 * @param a2
	 *            Second of the two arrays to compare.
	 * @return <code>true</code> if the two given arrays are equal, <code>false</code> otherwise.
	 */
	private static boolean equalArrays(Object[] a1, Object[] a2) {
		boolean equal = false;
		if (a1 == null) {
			equal = a2 == null || a2.length == 0;
		} else if (a2 == null) {
			equal = a1.length == 0;
		} else if (a1.length != a2.length) {
			equal = false;
		} else {
			equal = true;
			for (int i = 0; i < a1.length && equal; i++) {
				if (a1[i] != a2[i]) {
					equal = false;
				}
			}
		}

		return equal;
	}
}
