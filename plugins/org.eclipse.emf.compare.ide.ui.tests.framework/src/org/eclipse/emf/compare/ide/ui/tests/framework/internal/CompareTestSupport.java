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
 *     Martin Fleck - extension for bug 495259
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.framework.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.hook.IResourceSetHook;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistry;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
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

	/** Resource set hooks considered for the left, right, and ancestor side. */
	private List<IResourceSetHook> resourceSetHooks;

	/** The right side resource. */
	private Resource rightResource;

	/** The left side resource. */
	private Resource leftResource;

	/** The ancestor side resource. */
	private Resource ancestorResource;

	/**
	 * The provided resource set hooks are used when loading the left, right, and ancestor side and disposed
	 * when this support object is teared down after the test method has been called.
	 * 
	 * @param resourceSetHooks
	 *            resource set hooks
	 */
	public CompareTestSupport(final Class<?>[] resourceSetHooks) {
		this.resourceSetHooks = new ArrayList<IResourceSetHook>(collectResourceSetHooks(resourceSetHooks));
	}

	/**
	 * Returns a filtered collection of resource set hooks from the
	 * {@link EMFCompareIDEPlugin#getResourceSetHookRegistry() resource set hook registry} based on the given
	 * set of classes. Any hook that conforms to or is a subclass of any of the provided classes is accepted.
	 * 
	 * @param resourceSetHookClasses
	 *            classes of resource set hooks to be returned
	 * @return collection of resource set hooks conforming to the given classes
	 */
	protected Collection<IResourceSetHook> collectResourceSetHooks(final Class<?>[] resourceSetHookClasses) {
		final ResourceSetHookRegistry hookRegistry = EMFCompareIDEPlugin.getDefault()
				.getResourceSetHookRegistry();

		return Collections2.filter(hookRegistry.getResourceSetHooks(), new Predicate<IResourceSetHook>() {
			public boolean apply(IResourceSetHook hook) {
				for (final Class<?> hookClass : resourceSetHookClasses) {
					// hook is class or subclass of provided classes
					if (hookClass.isAssignableFrom(hook.getClass())) {
						return true;
					}
				}
				return false;
			}
		});
	}

	/**
	 * Removes resources from the resource set identified via the pathmap URI, e.g., UML and Ecore metamodel
	 * or UML Primitive Types library. As these resources do not change, we do not need them in the resource
	 * set. Removal is necessary since they are automatically added by
	 * {@link EcoreUtil#resolveAll(ResourceSet)}.
	 * 
	 * @param set
	 *            set to be cleaned from pathmap resources
	 */
	protected void removePathmapResources(ResourceSet set) {
		for (final ListIterator<Resource> it = set.getResources().listIterator(); it.hasNext();) {
			final Resource resource = it.next();
			if (resource.getURI().toString().startsWith("pathmap://")) { //$NON-NLS-1$
				it.remove();
			}
		}
	}

	/**
	 * Load the resource for the given paths. The paths must be relative to the given class. Any provided
	 * resource set hooks will be considered during the loading process.
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
		leftResource = loadFromClassLoader(clazz, left, leftRS);
		EcoreUtil.resolveAll(leftRS);
		removePathmapResources(leftRS);

		rightRS = new ResourceSetImpl();
		rightResource = loadFromClassLoader(clazz, right, rightRS);
		EcoreUtil.resolveAll(rightRS);
		removePathmapResources(rightRS);

		if (!("".equals(ancestor))) { //$NON-NLS-1$
			ancestorRS = new ResourceSetImpl();
			ancestorResource = loadFromClassLoader(clazz, ancestor, ancestorRS);
			EcoreUtil.resolveAll(ancestorRS);
			removePathmapResources(ancestorRS);
		}
	}

	/**
	 * Returns a collection of resource set hooks that should be used for the resources provided by the given
	 * URIs. If no hooks match, an empty collection is returned.
	 * 
	 * @param uris
	 *            resource URIs
	 * @return collection of matching resource set hooks
	 */
	private Collection<IResourceSetHook> getMatchingHooks(final Collection<URI> uris) {
		return Collections2.filter(resourceSetHooks, new Predicate<IResourceSetHook>() {
			public boolean apply(IResourceSetHook input) {
				return input.isHookFor(uris);
			}
		});
	}

	/**
	 * Returns a collection of resource set hooks that should be used for the given resources. If no hooks
	 * match, an empty collection is returned.
	 * 
	 * @param resources
	 *            resources
	 * @return collection of matching resource set hooks
	 */
	private Collection<IResourceSetHook> getMatchingHooks(final List<Resource> resources) {
		final Collection<URI> uris = Collections2.transform(resources, new Function<Resource, URI>() {
			public URI apply(Resource resource) {
				return resource.getURI();
			}
		});
		return getMatchingHooks(uris);
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

		final List<URI> urisToLoad = Collections.singletonList(uri);
		for (final IResourceSetHook hook : getMatchingHooks(urisToLoad)) {
			hook.preLoadingHook(resourceSet, urisToLoad);
		}

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
		} catch (final IOException e) {
			// return null
		} catch (final WrappedException e) {
			// return null
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException e) {
					// Should have been caught by the outer try
				}
			}
		}
		for (final IResourceSetHook hook : getMatchingHooks(urisToLoad)) {
			hook.postLoadingHook(resourceSet, urisToLoad);
		}
		return resource;
	}

	/**
	 * Launch EMFCompare comparison with the known parameters.
	 * 
	 * @return the comparison
	 * @see EMFCompare#compare(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public Comparison compare() {
		final DefaultComparisonScope scope = new DefaultComparisonScope(leftRS, rightRS, ancestorRS);
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		return comparisonBuilder.build().compare(scope);
	}

	/**
	 * Place for specific tear down treatments to do after the test.
	 */
	protected void tearDown() {
		// call matching resource set hooks on all resource sets
		onDispose(leftRS);
		onDispose(rightRS);
		onDispose(ancestorRS);
	}

	/**
	 * Calls the matching resource set hooks' {@link IResourceSetHook#onDispose(Iterable) onDispose} method
	 * for the given resource set.
	 * 
	 * @param resourceSet
	 *            resource set to be disposed
	 */
	protected void onDispose(ResourceSet resourceSet) {
		if (resourceSet != null) {
			for (final IResourceSetHook hook : getMatchingHooks(resourceSet.getResources())) {
				hook.onDispose(resourceSet.getResources());
			}
		}
	}

	/**
	 * Returns the provided and loaded {@link Compare#left() left resource}. This resource should not be null.
	 * 
	 * @return loaded left resource
	 */
	public Resource getLeftResource() {
		return leftResource;
	}

	/**
	 * Returns the provided and loaded {@link Compare#right() right resource}. This resource should not be
	 * null.
	 * 
	 * @return loaded right resource
	 */
	public Resource getRightResource() {
		return rightResource;
	}

	/**
	 * Returns the provided and loaded {@link Compare#ancestor() ancestor resource}. If no ancestor resource
	 * was given, null is returned.
	 * 
	 * @return loaded ancestor resource or null
	 */
	public Resource getAncestorResource() {
		return ancestorResource;
	}

	/**
	 * Returns the list of resource set hooks matching the provided {@link Compare#resourceSetHooks() resource
	 * set hook classes}.
	 * 
	 * @return list of resource set hooks
	 */
	public List<IResourceSetHook> getResourceSetHooks() {
		return resourceSetHooks;
	}

	/**
	 * Returns true if the comparison is 3-way, i.e., an ancestor resource is present.
	 * 
	 * @return true if comparison is 3-way, false otherwise
	 */
	public boolean isThreeWay() {
		return ancestorResource != null;
	}
}
