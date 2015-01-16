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

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
	
	public void logicalModel(ITypedElement leftTypedElement, ITypedElement rightTypedElement) {
		ComparisonScopeBuilder.create(null, leftTypedElement, rightTypedElement, null, new NullProgressMonitor());
	}
	
	public Comparison match() {
		return match(UseIdentifiers.ONLY);
	}
	
	public Comparison match(UseIdentifiers useIDs) {
		final IComparisonScope scope = new DefaultComparisonScope(getLeft(), getRight(), getAncestor());
		final IMatchEngine matchEngine = DefaultMatchEngine.create(useIDs);
		comparison = matchEngine.match(scope, new BasicMonitor());
		return comparison;
	}
	
	public Comparison diff() {
		final IDiffProcessor diffBuilder = new DiffBuilder();
		final IDiffEngine diffEngine = new DefaultDiffEngine(diffBuilder);
		diffEngine.diff(comparison,  new BasicMonitor());
		return comparison;
	}
	
	public void req() {
		final IReqEngine reqEngine = new DefaultReqEngine();
		reqEngine.computeRequirements(comparison, new BasicMonitor());
	}
	
	public void equi() {
		final IEquiEngine equiEngine = new DefaultEquiEngine();
		equiEngine.computeEquivalences(comparison, new BasicMonitor());
	}
	
	public void conflict() {
		final IConflictDetector conflictDetector = new DefaultConflictDetector();
		conflictDetector.detect(comparison, new BasicMonitor());
	}
	
	public void compare() {
		final IComparisonScope scope = new DefaultComparisonScope(getLeft(), getRight(), getAncestor());
		EMFCompare.builder().build().compare(scope);
	}

	public void postComparisonGMF() {
		final IPostProcessor postProcessor = new CompareDiagramPostProcessor();
		postProcessor.postComparison(comparison, new BasicMonitor());
	}
	
	public void postMatchUML() {
		final IPostProcessor postProcessor = new UMLPostProcessor();
		postProcessor.postMatch(comparison, new BasicMonitor());
	}
	
	public void postComparisonUML() {
		final IPostProcessor postProcessor = new UMLPostProcessor();
		postProcessor.postComparison(comparison, new BasicMonitor());
	}
	
	
	public void dispose() {
		comparison = null;
		for (ResourceSet rs : resourceSets) {
			EList<Resource> resources = rs.getResources();
			for (Resource resource : resources) {
				TreeIterator<EObject> allContents = EcoreUtil.getAllProperContents(resource, false);
				while (allContents.hasNext()) {
					final EObject next = allContents.next();
					next.eAdapters().clear();
				}
				resource.eAdapters().clear();
			}
			
			rs.getResources().clear();
			rs.eAdapters().clear();
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
			try {
				Closeables.close(stream, true);
			} catch (IOException e) {
				// already swallowed
			}
		}

		return resource;
	}
}