/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 495334 
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.framework.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class handle all non git related comparison tests.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public class CompareTestSupport {

	/** The left side resourceSet. */
	private ResourceSet leftRS;

	/** The right side resourceSet. */
	private ResourceSet rightRS;

	/** The ancestor side resourceSet. */
	private ResourceSet ancestorRS;

	/**
	 * Load the resource for the given paths. The paths must be relative to the given class.
	 * 
	 * @param clazz
	 *            The test class
	 * @param left
	 *            The left resource relative path
	 * @param right
	 *            The right resource relative path
	 * @param ancestor
	 *            The ancestor resource relative path
	 * @throws IOException
	 *             If a file cannot be read
	 */
	protected void loadResources(Class<?> clazz, String left, String right, String ancestor)
			throws IOException {
		leftRS = new ResourceSetImpl();
		loadFromClassLoader(clazz, left, leftRS);
		EcoreUtil.resolveAll(leftRS);

		rightRS = new ResourceSetImpl();
		loadFromClassLoader(clazz, right, rightRS);
		EcoreUtil.resolveAll(rightRS);

		if (!("".equals(ancestor))) { //$NON-NLS-1$
			ancestorRS = new ResourceSetImpl();
			loadFromClassLoader(clazz, ancestor, ancestorRS);
			EcoreUtil.resolveAll(ancestorRS);
		}
	}

	/**
	 * Tries and locate a model in the current class' classpath.
	 * 
	 * @param clazz
	 *            The given test class
	 * @param path
	 *            Relative path to the model we seek (relative to the given class).
	 * @param resourceSet
	 *            the resource set in which to load the resource.
	 * @return The loaded resource.
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>path</code> points.
	 */
	private Resource loadFromClassLoader(Class<?> clazz, String path, ResourceSet resourceSet)
			throws IOException {
		final URL fileURL = clazz.getResource(path);
		final URI uri = URI.createURI(fileURL.toString());

		final Resource existing = resourceSet.getResource(uri, false);
		if (existing != null) {
			return existing;
		}

		InputStream stream = null;
		Resource resource = null;
		try {
			resource = resourceSet.createResource(uri);
			stream = fileURL.openStream();
			resource.load(stream, Collections.emptyMap());
		} catch (IOException e) {
			// return null
		} catch (WrappedException e) {
			// return null
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Should have been caught by the outer try
				}
			}
		}

		return resource;
	}

	/**
	 * Launch EMFCompare comparison with the known parameters.
	 * 
	 * @return the comparison
	 */
	public Comparison compare() {
		DefaultComparisonScope scope = new DefaultComparisonScope(leftRS, rightRS, ancestorRS);
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		return comparisonBuilder.build().compare(scope);
	}

	/**
	 * Place for specific tear down treatments to do after the test.
	 */
	protected void tearDown() {
		// TODO is there something to do?
	}

}
