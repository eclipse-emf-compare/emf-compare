/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.internal.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.compare.match.internal.statistic.ResourceSimilarity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This filter will allow us to remove binary identical resources from the lists of resources to be matched.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class BinaryIdenticalResourceFilter implements IResourceFilter {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.filter.IResourceFilter#filter(java.util.List, java.util.List)
	 */
	public void filter(List<Resource> leftResources, List<Resource> rightResources) {
		final List<Doublet<Resource>> matchedResources = new ArrayList<Doublet<Resource>>();

		final List<Resource> leftRemaining = new ArrayList<Resource>(leftResources);
		final List<Resource> rightRemaining = new ArrayList<Resource>(rightResources);

		// Does two passes so as to try to match every resource
		for (int i = 0; i < 2; i++) {
			for (final Resource left : new ArrayList<Resource>(leftRemaining)) {
				final Resource matchedResource = ResourceSimilarity
						.findMatchingResource(left, rightRemaining);
				if (matchedResource != null
						&& ResourceSimilarity.findMatchingResource(matchedResource, leftRemaining) == left) {
					matchedResources.add(new Doublet<Resource>(left, matchedResource));
					leftRemaining.remove(left);
					rightRemaining.remove(matchedResource);
				}
			}
		}

		for (final Doublet<Resource> doublet : matchedResources) {
			// do not filter out resources that contain fragments even when identical as fragments themselves
			// have been removed from the list
			if (!hasFragments(doublet.getFirst()) && !hasFragments(doublet.getSecond())) {
				final byte[] leftContent = getContent(doublet.getFirst());
				final byte[] rightContent = getContent(doublet.getSecond());

				if (Arrays.equals(leftContent, rightContent)) {
					leftResources.remove(doublet.getFirst());
					rightResources.remove(doublet.getSecond());
				}

				doublet.clear();
			}
		}

		leftRemaining.clear();
		rightRemaining.clear();
		matchedResources.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.filter.IResourceFilter#filter(java.util.List, java.util.List,
	 *      java.util.List)
	 */
	public void filter(List<Resource> leftResources, List<Resource> rightResources,
			List<Resource> ancestorResources) {
		final List<Triplet<Resource>> matchedResources = new ArrayList<Triplet<Resource>>();

		final List<Resource> leftRemaining = new ArrayList<Resource>(leftResources);
		final List<Resource> rightRemaining = new ArrayList<Resource>(rightResources);
		final List<Resource> ancestorRemaining = new ArrayList<Resource>(ancestorResources);

		// Does two passes so as to try to match every single resource
		for (int i = 0; i < 2; i++) {
			for (final Resource left : new ArrayList<Resource>(leftRemaining)) {
				final Resource matchedRight = ResourceSimilarity.findMatchingResource(left, rightRemaining);
				final Resource matchedAncestor = ResourceSimilarity.findMatchingResource(left,
						ancestorRemaining);
				if (matchedRight != null
						&& ResourceSimilarity.findMatchingResource(matchedRight, leftRemaining) == left
						&& matchedAncestor != null
						&& ResourceSimilarity.findMatchingResource(matchedAncestor, leftRemaining) == left) {
					matchedResources.add(new Triplet<Resource>(left, matchedRight, matchedAncestor));
					leftRemaining.remove(left);
					rightRemaining.remove(matchedRight);
					ancestorRemaining.remove(matchedAncestor);
				}
			}
		}

		for (final Triplet<Resource> triplet : matchedResources) {
			// do not filter out resources that contain fragments even when identical as fragments themselves
			// have been removed from the list
			if (!hasFragments(triplet.getFirst()) && !hasFragments(triplet.getSecond())
					&& !hasFragments(triplet.getThird())) {
				final byte[] leftContent = getContent(triplet.getFirst());
				final byte[] rightContent = getContent(triplet.getSecond());
				final byte[] ancestorContent = getContent(triplet.getThird());

				if (Arrays.equals(leftContent, ancestorContent)
						&& Arrays.equals(rightContent, ancestorContent)) {
					leftResources.remove(triplet.getFirst());
					rightResources.remove(triplet.getSecond());
					ancestorResources.remove(triplet.getThird());
				}

				triplet.clear();
			}
		}

		leftRemaining.clear();
		rightRemaining.clear();
		ancestorRemaining.clear();
		matchedResources.clear();
	}

	/**
	 * Returns the content of a given resource as a byte array.
	 * 
	 * @param resource
	 *            The resource we seek the content of.
	 * @return The content of <code>resource</code> as a byte array.
	 */
	private byte[] getContent(Resource resource) {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			resource.save(stream, null);
		} catch (final IOException e) {
			EMFComparePlugin.log(e, false);
		}
		return stream.toByteArray();
	}

	/**
	 * This will iterate over the resource's contents and return <code>true</code> if it contains fragments.
	 * 
	 * @param resource
	 *            Resource to iterate over.
	 * @return <code>true</code> if <code>resource</code> has fragments, <code>false</code> otherwise.
	 */
	private boolean hasFragments(Resource resource) {
		final TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			if (iterator.next().eResource() != resource) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This will allow us to hold a doublet of elements.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class Doublet<T> {
		/** Holds the first item composing this Doublet. */
		private T first;

		/** Holds the second item composing this Doublet. */
		private T second;

		/**
		 * Instantiates a doublet given the two elements that are to be wrapped.
		 * 
		 * @param firstElement
		 *            The first of the two elements composing this doublet.
		 * @param secondElement
		 *            The second of the two elements composing this doublet.
		 */
		public Doublet(T firstElement, T secondElement) {
			this.first = firstElement;
			this.second = secondElement;
		}

		/**
		 * Returns the first element of this doublet.
		 * 
		 * @return The first element of this doublet.
		 */
		public T getFirst() {
			return first;
		}

		/**
		 * Returns the second element of this doublet.
		 * 
		 * @return The second element of this doublet.
		 */
		public T getSecond() {
			return second;
		}

		/**
		 * Makes sure we don't keep any reference to any object.
		 */
		public void clear() {
			first = null;
			second = null;
		}
	}

	/**
	 * This will allow us to hold a triplet of elements.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class Triplet<T> extends Doublet<T> {
		/** Holds the thid item composing this Triplet. */
		private T third;

		/**
		 * Instantiates a doublet given the two elements that are to be wrapped.
		 * 
		 * @param firstElement
		 *            The first of the three elements composing this triplet.
		 * @param secondElement
		 *            The second of the three elements composing this triplet.
		 * @param thirdElement
		 *            The third of the three elements composing this triplet.
		 */
		public Triplet(T firstElement, T secondElement, T thirdElement) {
			super(firstElement, secondElement);
			this.third = thirdElement;
		}

		/**
		 * Returns the first element of this triplet.
		 * 
		 * @return The first element of this triplet.
		 */
		@Override
		public T getFirst() {
			return super.getFirst();
		}

		/**
		 * Returns the second element of this triplet.
		 * 
		 * @return The second element of this triplet.
		 */
		@Override
		public T getSecond() {
			return super.getSecond();
		}

		/**
		 * Returns the third element of this triplet.
		 * 
		 * @return The third element of this triplet.
		 */
		public T getThird() {
			return third;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.match.internal.filter.BinaryIdenticalResourceFilter.Doublet#clear()
		 */
		@Override
		public void clear() {
			super.clear();
			third = null;
		}
	}
}
