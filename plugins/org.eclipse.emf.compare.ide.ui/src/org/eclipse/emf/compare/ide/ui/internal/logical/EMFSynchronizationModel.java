/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ISharedDocumentAdapter;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.RevisionedURIConverter;
import org.eclipse.emf.compare.ide.internal.utils.ResourceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.ui.IEditorInput;

/**
 * This class will act as a logical model for EMF. It will hold the necessary logic to be able to determine
 * which "other" files are to be considered dependencies of a "starting point". For example, when trying to
 * compare a "genmodel" file, we cannot compare the genmodel alone, we need to compare the underlying "ecore"
 * file along with it.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public class EMFSynchronizationModel {
	/** The traversal corresponding to the left side. */
	private ResourceTraversal leftTraversal;

	/** The traversal corresponding to the right side. */
	private ResourceTraversal rightTraversal;

	/** The traversal corresponding to the common ancestor of both other side. */
	private ResourceTraversal originTraversal;

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 */
	private EMFSynchronizationModel(ResourceTraversal leftTraversal, ResourceTraversal rightTraversal,
			ResourceTraversal originTraversal) {
		if (leftTraversal == null) {
			this.leftTraversal = new ResourceTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.leftTraversal = leftTraversal;
		}

		if (rightTraversal == null) {
			this.rightTraversal = new ResourceTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.rightTraversal = rightTraversal;
		}

		if (originTraversal == null) {
			this.originTraversal = new ResourceTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.originTraversal = originTraversal;
		}
	}

	// TODO comment supported ITypedElements
	/**
	 * Creates a synchronization model by resolving the full logical model of the given elements.
	 * 
	 * @param left
	 *            The left resource, starting point of the left logical model we are to resolve.
	 * @param right
	 *            The right resource, starting point of the right logical model we are to resolve.
	 * @param origin
	 *            The origin resource, starting point of the logical model we are to resolve as the origin
	 *            one. Can be <code>null</code>.
	 * @return The created synchronization model.
	 */
	public static EMFSynchronizationModel createSynchronizationModel(ITypedElement left, ITypedElement right,
			ITypedElement origin) {
		/*
		 * We need a way to load these models. If it is a local file, we'll simply resolve the resource set.
		 * For any ITypedElement from which we can find an IFileRevision, we'll resolve the resource set by
		 * using this file revision as a "base" i.e : we won't load any resource which revision is younger.
		 */
		final IFileRevision leftRevision = findFileRevision(left);
		final IFileRevision rightRevision = findFileRevision(right);
		final IFileRevision originRevision = findFileRevision(origin);

		final ResourceTraversal leftTraversal;
		final ResourceTraversal rightTraversal;
		final ResourceTraversal originTraversal;
		if (leftRevision == null) {
			// Load it as a local model
			final IResource leftRes = findResource(left);
			leftTraversal = resolveTraversal(leftRes);
		} else {
			leftTraversal = resolveTraversal(leftRevision);
		}
		if (rightRevision == null) {
			// Load it as a local model
			final IResource rightRes = findResource(right);
			rightTraversal = resolveTraversal(rightRes);
		} else {
			rightTraversal = resolveTraversal(rightRevision);
		}
		if (originRevision == null) {
			// Load it as a local model
			final IResource originRes = findResource(origin);
			originTraversal = resolveTraversal(originRes);
		} else {
			originTraversal = resolveTraversal(originRevision);
		}

		return new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * Creates a synchronization model by resolving the full logical model of the given local resources.
	 * 
	 * @param left
	 *            The left resource, starting point of the left logical model we are to resolve.
	 * @param right
	 *            The right resource, starting point of the right logical model we are to resolve.
	 * @param origin
	 *            The origin resource, starting point of the logical model we are to resolve as the origin
	 *            one. Can be <code>null</code>.
	 * @return The created synchronization model.
	 */
	public static EMFSynchronizationModel createSynchronizationModel(IResource left, IResource right,
			IResource origin) {
		final ResourceTraversal leftTraversal = resolveTraversal(left);
		final ResourceTraversal rightTraversal = resolveTraversal(right);
		final ResourceTraversal originTraversal = resolveTraversal(origin);

		return new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * This can be called in order to prune from all traversals the resource that can be seen as binary
	 * identical. We'll use exact equality between the resources' names to 'match' them together there.
	 */
	public void minimize() {
		final boolean threeWay = !originTraversal.getStorages().isEmpty();
		if (threeWay) {

		} else {
			// Copy the sets to update them as we go.
			final Set<IStorage> leftCopy = Sets.newLinkedHashSet(leftTraversal.getStorages());
			final Set<IStorage> rightCopy = Sets.newLinkedHashSet(rightTraversal.getStorages());
			for (IStorage left : leftCopy) {
				final IPath leftPath = left.getFullPath();
				final Iterator<IStorage> rightIterator = rightCopy.iterator();
				while (rightIterator.hasNext()) {
					final IStorage right = rightIterator.next();
					final IPath rightPath = right.getFullPath();
					if (leftPath.lastSegment().equals(rightPath.lastSegment())) {
						rightIterator.remove();

						if (binaryIdentical(left, right)) {
							leftTraversal.getStorages().remove(left);
							rightTraversal.getStorages().remove(right);
						}
					}
				}
			}
		}
	}

	/**
	 * Checks whether the two given storages point to binary identical data.
	 * 
	 * @param left
	 *            First of the two storages which content we are testing.
	 * @param right
	 *            Second of the two storages which content we are testing.
	 * @return <code>true</code> if {@code left} and {@code right} are binary identical.
	 */
	private static boolean binaryIdentical(IStorage left, IStorage right) {
		Reader leftReader = null;
		Reader rightReader = null;
		try {
			leftReader = new BufferedReader(new InputStreamReader(left.getContents()));
			rightReader = new BufferedReader(new InputStreamReader(right.getContents()));

			final char[] leftBuff = new char[8192];
			final char[] rightBuff = new char[8192];
			boolean identical = true;
			int readLeft = leftReader.read(leftBuff);
			int readRight = rightReader.read(rightBuff);
			while (readLeft > 0 && readRight > 0 && readLeft == readRight && identical) {
				identical = Arrays.equals(leftBuff, rightBuff);
				readLeft = leftReader.read(leftBuff);
				readRight = rightReader.read(rightBuff);
			}
			// One last check in case we've reached the end of one side but not of the other
			return identical && readLeft == readRight && Arrays.equals(leftBuff, rightBuff);
		} catch (CoreException e) {

		} catch (IOException e) {

		} finally {
			if (leftReader != null) {
				Closeables.closeQuietly(leftReader);
			}
			if (rightReader != null) {
				Closeables.closeQuietly(rightReader);
			}
		}
		return false;
	}

	/**
	 * Create the resource set corresponding to the left logical model.
	 * 
	 * @return The resource set corresponding to the left logical model.
	 */
	public ResourceSet getLeftResourceSet() {
		return createResourceSet(leftTraversal);
	}

	/**
	 * Create the resource set corresponding to the right logical model.
	 * 
	 * @return The resource set corresponding to the right logical model.
	 */
	public ResourceSet getRightResourceSet() {
		return createResourceSet(rightTraversal);
	}

	/**
	 * Create the resource set corresponding to the origin logical model.
	 * 
	 * @return The resource set corresponding to the origin logical model.
	 */
	public ResourceSet getOriginResourceSet() {
		return createResourceSet(originTraversal);
	}

	/**
	 * Tries and resolve the resource traversal corresponding to the given starting point.
	 * 
	 * @param start
	 *            The resource that will be considered as the "starting point" of the traversal to resolve.
	 * @return The resource traversal corresponding to the logical model that's been computed from the given
	 *         starting point.
	 */
	private static ResourceTraversal resolveTraversal(IResource start) {
		if (start == null || !(start instanceof IFile)) {
			return new ResourceTraversal(Sets.<IFile> newLinkedHashSet());
		}

		/*
		 * TODO make model resolver extension point use IResource instead of EMF resource ... and use it here.
		 * For now, we'll simply load the resource as an EMF model and resolve it all.
		 */
		final SyncResourceSet resourceSet = new SyncResourceSet();
		ResourceUtil.loadResource((IFile)start, resourceSet);
		resourceSet.resolveAll();

		final Set<IFile> resources = Sets.newLinkedHashSet();
		for (Resource resource : resourceSet.getResources()) {
			resources.add(ResourceUtil.findIResource(resource));
		}
		return new ResourceTraversal(resources);
	}

	/**
	 * Tries and resolve the resource traversal corresponding to the given starting point.
	 * 
	 * @param start
	 *            The revision that will be considered as the "starting point" of the traversal to resolve.
	 * @return The resource traversal corresponding to the logical model that's been computed from the given
	 *         starting point.
	 */
	private static ResourceTraversal resolveTraversal(IFileRevision start) {
		if (start == null) {
			return new ResourceTraversal(Sets.<IFile> newLinkedHashSet());
		}

		// TODO how could we make this extensible?
		ResourceTraversal traversal = new ResourceTraversal(Sets.<IFile> newLinkedHashSet());
		final SyncResourceSet resourceSet = new SyncResourceSet();
		final RevisionedURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				start);
		resourceSet.setURIConverter(converter);
		try {
			ResourceUtil.loadResource(start.getStorage(new NullProgressMonitor()), resourceSet);
			resourceSet.resolveAll();

			final Set<IStorage> storages = Sets.newLinkedHashSet(converter.getLoadedRevisions());
			// There might have been resources loaded through other means
			for (Resource resource : resourceSet.getResources()) {
				final String resourceURI = resource.getURI().toString();
				boolean exists = false;
				final Iterator<IStorage> storageIterator = storages.iterator();
				while (storageIterator.hasNext() && !exists) {
					if (resourceURI.endsWith(storageIterator.next().getFullPath().toString())) {
						exists = true;
					}
				}
				if (!exists) {
					storages.add(ResourceUtil.findIResource(resource));
				}
			}
			traversal = new ResourceTraversal(storages);
		} catch (CoreException e) {
			// FIXME ignore for now
		}
		return traversal;
	}

	/**
	 * Create a resource set corresponding to the given traversal.
	 * 
	 * @param traversal
	 *            The traversal from which we seek to create a new resource set.
	 * @return A new resource set corresponding to the given traversal.
	 */
	private ResourceSet createResourceSet(ResourceTraversal traversal) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		for (IStorage storage : traversal.getStorages()) {
			ResourceUtil.loadResource(storage, resourceSet);
		}
		return resourceSet;
	}

	/**
	 * Try and determine the file revision of the given element.
	 * 
	 * @param element
	 *            The element for which we need an {@link IFileRevision}.
	 * @return The file revision of the given element if we could find one, <code>null</code> otherwise.
	 */
	private static IFileRevision findFileRevision(ITypedElement element) {
		if (element == null) {
			return null;
		}

		// Can we adapt it directly?
		IFileRevision revision = adaptAs(element, IFileRevision.class);
		if (revision == null) {
			// This is quite the workaround ... but we have no direct way to retrieve the IFileRevision.
			final ISharedDocumentAdapter documentAdapter = adaptAs(element, ISharedDocumentAdapter.class);
			if (documentAdapter != null) {
				final IEditorInput input = documentAdapter.getDocumentKey(element);
				if (input != null) {
					revision = adaptAs(input, IFileRevision.class);
				}
			}
		}
		if (revision == null) {
			// Couldn't do it the API way ...
			try {
				final Method method = element.getClass().getMethod("getFileRevision"); //$NON-NLS-1$
				final Object value = method.invoke(element);
				if (value instanceof IFileRevision) {
					revision = (IFileRevision)value;
				}
				// CHECKSTYLE:OFF this would require five "catch" for ignored exceptions...
			} catch (Exception e) {
				// CHECKSTYLE:ON
			}
		}

		return revision;
	}

	/**
	 * Try and determine the resource of the given element.
	 * 
	 * @param element
	 *            The element for which we need an {@link IResource}.
	 * @return The resource corresponding to the given {@code element} if we could find it, <code>null</code>
	 *         otherwise.
	 */
	private static IResource findResource(ITypedElement element) {
		if (element == null) {
			return null;
		}

		// Can we adapt it directly?
		IResource resource = adaptAs(element, IResource.class);
		if (resource == null) {
			// We know about some types ...
			if (element instanceof IResourceProvider) {
				resource = ((IResourceProvider)element).getResource();
			}
		}

		return resource;
	}

	/**
	 * Tries and adapt the given <em>object</em> to an instance of the given class.
	 * 
	 * @param <T>
	 *            Type to which we need to adapt <em>object</em>.
	 * @param object
	 *            The object we need to coerce to a given {@link Class}.
	 * @param clazz
	 *            Class to which we are to adapt <em>object</em>.
	 * @return <em>object</em> cast to type <em>T</em> if possible, <code>null</code> if not.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T adaptAs(Object object, Class<T> clazz) {
		if (object == null) {
			return null;
		}

		T result = null;
		if (clazz.isInstance(object)) {
			result = (T)object;
		} else if (object instanceof IAdaptable) {
			result = (T)((IAdaptable)object).getAdapter(clazz);
		}

		if (result == null) {
			result = (T)Platform.getAdapterManager().getAdapter(object, clazz);
		}

		return result;
	}

	private static class SyncResourceSet extends ResourceSetImpl {
		private Set<URI> demandedURIs = Sets.newLinkedHashSet();

		private Set<URI> loadedURIs = Sets.newLinkedHashSet();

		private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * 2);

		@Override
		public Resource getResource(URI uri, boolean loadOnDemand) {
			// Never load resources from here
			if (!loadedURIs.contains(uri)) {
				final Resource existing = super.getResource(uri, false);
				if (existing == null) {
					demandedURIs.add(uri);
				}
			}
			return null;
		}

		public void resolveAll() {
			for (Resource resource : Sets.newLinkedHashSet(getResources())) {
				loadedURIs.add(resource.getURI());
				resolveAndUnload(resource);
			}

			while (!demandedURIs.isEmpty()) {
				// transform into function
				final Set<URI> newURIs = demandedURIs;
				loadedURIs.addAll(newURIs);
				demandedURIs = Sets.newLinkedHashSet();
				for (URI uri : newURIs) {
					final Resource newResource = super.getResource(uri, true);
					resolveAndUnload(newResource);
				}
			}
		}

		private void resolveAndUnload(Resource resource) {
			final Iterator<EObject> resourceContent = resource.getContents().iterator();
			while (resourceContent.hasNext()) {
				final EObject eObject = resourceContent.next();
				resolveCrossReferences(eObject);
				final TreeIterator<EObject> childContent = eObject.eAllContents();
				while (childContent.hasNext()) {
					final EObject child = childContent.next();
					if (child.eResource() != resource) {
						childContent.prune();
					} else {
						resolveCrossReferences(child);
					}
				}
			}

			// Swap it with a new, unloaded resource with the same URI
			getResources().remove(resource);
			createResource(resource.getURI());
			// We still need to unload what we loaded since some (like UML) cross reference everything...
			/*
			 * However, this unloading is not needed for the comparison or resolving, so we can thread it and
			 * let it die alone in the background
			 */
			unload(resource);
		}

		private void unload(final Resource resource) {
			final Runnable unloader = new Runnable() {
				public void run() {
					resource.unload();
				}
			};
			pool.submit(unloader);
		}

		/**
		 * Resolves the cross references of the given EObject.
		 * 
		 * @param eObject
		 *            The EObject for which we are to resolve the cross references.
		 */
		private void resolveCrossReferences(EObject eObject) {
			final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
			while (objectChildren.hasNext()) {
				// Resolves cross references by simply visiting them.
				objectChildren.next();
			}
		}
	}
}
