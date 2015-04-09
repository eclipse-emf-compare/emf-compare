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
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Utility class with predicates applying to {@link MatchResource}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.4
 */
public final class MatchResourcePredicates {

	/** Constructor, private to prevent instantiation. */
	private MatchResourcePredicates() {
		// Not instantiable.
	}

	/**
	 * Provides a predicate that matches {@link MatchResource}s that have the given URI on the given side.
	 * 
	 * @param uri
	 *            The expected URI
	 * @param side
	 *            The side
	 * @return a predicate, never null.
	 */
	public static Predicate<MatchResource> hasSameURI(URI uri, DiffSide side) {
		return new HasSameURI(uri, side);
	}

	/**
	 * Provides a predicate that matches {@link MatchResource}s that have the same trimmed URI (i.e. without
	 * file extension) as the given MatchResource, on the given side.
	 * 
	 * @param mr
	 *            The MatchResource
	 * @param side
	 *            The side
	 * @return a predicate, never null.
	 */
	public static Predicate<MatchResource> hasSameTrimmedURI(MatchResource mr, DiffSide side) {
		return new HasSameTrimmedURI(mr, side);
	}

	/**
	 * Predicate that matches {@link MatchResource}s that have non-null origin and left or right resource URI.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 * @since 2.4
	 */
	private static class HasSameURI implements Predicate<MatchResource> {

		/**
		 * The side of the resource to check.
		 */
		protected final DiffSide side;

		/**
		 * The match resource to which we want to have the same URI on the given side.
		 */
		protected final URI uri;

		/**
		 * Constructor.
		 * 
		 * @param uri
		 *            The URI that we want to find on the given side.
		 * @param side
		 *            side where we want non-null resource and URI.
		 */
		public HasSameURI(URI uri, DiffSide side) {
			this.side = checkNotNull(side);
			this.uri = checkNotNull(uri);
		}

		/**
		 * Apply implementation.
		 * 
		 * @param input
		 *            The MatchResource to check.
		 * @return true if the input has the 2 expected resources with non-null URIs.
		 */
		public boolean apply(MatchResource input) {
			boolean result;
			switch (side) {
				case ORIGIN:
					result = input.getOrigin() != null && uri.equals(input.getOrigin().getURI());
					break;
				case SOURCE:
					result = input.getLeft() != null && uri.equals(input.getLeft().getURI());
					break;
				case REMOTE:
					result = input.getRight() != null && uri.equals(input.getRight().getURI());
					break;
				default:
					throw new IllegalStateException();
			}
			return result;
		}
	}

	/**
	 * Predicate that matches {@link MatchResource}s that have non-null origin and left or right resource URI.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 * @since 2.4
	 */
	private static final class HasSameTrimmedURI extends HasSameURI {

		/**
		 * Constructor.
		 * 
		 * @param match
		 *            The match resource to which we want to have the same URI on the given side.
		 * @param side
		 *            side where we want non-null resource and URI.
		 */
		public HasSameTrimmedURI(MatchResource match, DiffSide side) {
			super(getTrimmedURI(match, side), side);
		}

		/**
		 * Only called in constructor.
		 * 
		 * @param match
		 *            The matchResource the URI of which we want to match, except file extension
		 * @param aSide
		 *            the side for which we want the URI
		 * @return The trimmed URI.
		 */
		private static URI getTrimmedURI(MatchResource match, DiffSide aSide) {
			final URI result;
			switch (aSide) {
				case ORIGIN:
					if (!hasURI(match.getOrigin())) {
						throw new IllegalArgumentException();
					}
					result = match.getOrigin().getURI().trimFileExtension();
					break;
				case SOURCE:
					if (!hasURI(match.getLeft())) {
						throw new IllegalArgumentException();
					}
					result = match.getLeft().getURI().trimFileExtension();
					break;
				case REMOTE:
					if (!hasURI(match.getRight())) {
						throw new IllegalArgumentException();
					}
					result = match.getRight().getURI().trimFileExtension();
					break;
				default:
					throw new IllegalStateException();
			}
			return result;
		}

		/**
		 * Apply implementation.
		 * 
		 * @param input
		 *            The MatchResource to check.
		 * @return true if the input has the 2 expected resources with non-null URIs.
		 */
		@Override
		public boolean apply(MatchResource input) {
			boolean result;
			switch (side) {
				case ORIGIN:
					result = hasURI(input.getOrigin())
							&& uri.trimFileExtension().equals(input.getOrigin().getURI().trimFileExtension());
					break;
				case SOURCE:
					result = hasURI(input.getLeft())
							&& uri.trimFileExtension().equals(input.getLeft().getURI().trimFileExtension());
					break;
				case REMOTE:
					result = hasURI(input.getRight())
							&& uri.trimFileExtension().equals(input.getRight().getURI().trimFileExtension());
					break;
				default:
					throw new IllegalStateException();
			}
			return result;
		}

		/**
		 * Indicates whether the given resource is non-null and has a non-null URI.
		 * 
		 * @param r
		 *            The resource
		 * @return true if the given resource is non-null and has a URI.
		 */
		private static boolean hasURI(Resource r) {
			return r != null && r.getURI() != null;
		}
	}
}
