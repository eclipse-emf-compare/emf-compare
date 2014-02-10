/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package data.models;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.performance.EMFComparePerformanceActivator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import com.google.common.io.Closeables;

public abstract class Data {
	private List<ResourceSet> resourceSets = newArrayList();
	private Comparison comparison;
	private final Notifier left;
	private final Notifier right;
	private final Notifier ancestor;
	
	public abstract Notifier loadLeft();
	public abstract Notifier loadRight();
	public abstract Notifier loadAncestor();
	
	/**
	 * 
	 */
	protected Data() {
		left = loadLeft();
		right = loadRight();
		ancestor = loadAncestor();
	}
	
	/**
	 * @return the left
	 */
	public Notifier getLeft() {
		return left;
	}
	
	/**
	 * @return the right
	 */
	public Notifier getRight() {
		return right;
	}
	
	/**
	 * @return the ancestor
	 */
	public Notifier getAncestor() {
		return ancestor;
	}
	
	protected ResourceSet createResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		resourceSets.add(resourceSet);
		return resourceSet;
	}
	
	public Comparison match(IEObjectMatcher matcher) {
		final IComparisonScope scope = new DefaultComparisonScope(getLeft(), loadRight(), loadAncestor());
		final DefaultMatchEngine matchEngine = new DefaultMatchEngine(matcher, new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		comparison = matchEngine.match(scope, new BasicMonitor());
		return comparison;
	}

	public Comparison diff() {
		final IDiffProcessor diffBuilder = new DiffBuilder();
		final IDiffEngine diffEngine = new DefaultDiffEngine(diffBuilder);
		diffEngine.diff(comparison,  new BasicMonitor());
		return comparison;
	}
	
	public void dispose() {
		comparison = null;
		for (ResourceSet rs : resourceSets) {
			EList<Resource> resources = rs.getResources();
			for (Resource resource : resources) {
				resource.unload();
			}				
		}
		resourceSets = null;
	}

	/**
	 * Tries and locate a model in the current class' classpath.
	 * 
	 * @param string
	 *            Relative path to the model we seek (relative to this class).
	 * @param resourceSet
	 *            the resource set in which to load the resource.
	 * @return The loaded resource.
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>path</code> points.
	 */
	// Suppressing the warning until bug 376938 is fixed
	@SuppressWarnings("resource")
	protected Resource loadFromClassLoader(String path, ResourceSet resourceSet) {
		final URL fileURL = getClass().getResource(path);
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
			Closeables.closeQuietly(stream);
		}

		return resource;
	}
	
}