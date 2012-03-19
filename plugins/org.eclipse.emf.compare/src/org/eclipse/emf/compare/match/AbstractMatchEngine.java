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
package org.eclipse.emf.compare.match;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.match.resource.ResourceMapping;
import org.eclipse.emf.compare.match.resource.ResourceMatcher;
import org.eclipse.emf.compare.scope.AbstractComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This class defines the general contract of a match engine.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMatchEngine {
	/** Root of this comparison, should only be accessed or instantiated through {@link #getComparison()}. */
	private Comparison comparison;

	/** The comparison scope that will be used by this engine. Should be accessed through {@link #getScope()}. */
	private final AbstractComparisonScope scope;

	/**
	 * Initializes a match engine given the comparison scope it should use.
	 * 
	 * @param scope
	 *            The comparison scope that should be used by this engine.
	 */
	public AbstractMatchEngine(AbstractComparisonScope scope) {
		this.scope = scope;
	}

	/**
	 * This is the entry point of a Comparison (Match-Diff process). It will use the given scope in order to
	 * determine which objects it should iterate over.
	 * 
	 * @return An initialized {@link Comparison} model with all matches determined.
	 */
	public Comparison match() {
		final Notifier left = getScope().getLeft();
		final Notifier right = getScope().getRight();
		final Notifier origin = getScope().getOrigin();

		// FIXME side-effect coding
		match(left, right, origin);

		return getComparison();
	}

	/**
	 * This will only be called on unknown Notifier types; and will throw an
	 * {@link UnsupportedOperationException}. Subclasses should handle the necessary dispatching.
	 * 
	 * @param left
	 *            The left Notifier.
	 * @param right
	 *            The right Notifier.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. Can be <code>null</code>.
	 */
	protected void match(Notifier left, Notifier right, Notifier origin) {
		// Unknown notifier type
		throw new UnsupportedOperationException();
	}

	/**
	 * This will be used to match the given {@link ResourceSet}s. This default implementation will query the
	 * comparison scope for these resource sets children, then delegate to a
	 * {@link #createResourceMatcher(Iterable, Iterable, Iterable) ResourceMatcher} to determine the resource
	 * mappings.
	 * 
	 * @param left
	 *            The left {@link ResourceSet}.
	 * @param right
	 *            The common {@link ResourceSet}.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. Can be <code>null</code>.
	 */
	protected void match(ResourceSet left, ResourceSet right, ResourceSet origin) {
		final Iterable<Notifier> leftChildren = getScope().getChildren(left);
		final Iterable<Notifier> rightChildren = getScope().getChildren(right);
		final Iterable<Notifier> originChildren;
		if (origin != null) {
			originChildren = getScope().getChildren(origin);
		} else {
			originChildren = Lists.newArrayList();
		}

		final Iterable<Resource> leftResources = Iterables.filter(leftChildren, Resource.class);
		final Iterable<Resource> rightResources = Iterables.filter(rightChildren, Resource.class);
		final Iterable<Resource> originResources = Iterables.filter(originChildren, Resource.class);

		final ResourceMatcher matcher = createResourceMatcher(leftResources, rightResources, originResources);
		final Iterable<ResourceMapping> mappings = matcher.createMappings();

		for (ResourceMapping mapping : mappings) {
			final MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
			if (mapping.getLeft().getURI() != null) {
				matchResource.setLeftURI(mapping.getLeft().getURI().toString());
			}
			if (mapping.getRight().getURI() != null) {
				matchResource.setRightURI(mapping.getRight().getURI().toString());
			}
			if (mapping.getOrigin() != null && mapping.getOrigin().getURI() != null) {
				matchResource.setOriginURI(mapping.getOrigin().getURI().toString());
			}
			getComparison().getMatchedResources().add(matchResource);

			match(mapping.getLeft(), mapping.getRight(), mapping.getOrigin());
		}
	}

	/**
	 * This will only query the scope for the given Resources' children, then iterate over these children to
	 * match them together.
	 * 
	 * @param left
	 *            The left {@link Resource}.
	 * @param right
	 *            The right {@link Resource}.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. can be <code>null</code>.
	 */
	protected void match(Resource left, Resource right, Resource origin) {
		// CODEME
	}

	/**
	 * This will be used to create the resource matcher that will be used by this match engine.
	 * 
	 * @param leftResources
	 *            The list of all resources from the left side.
	 * @param rightResources
	 *            The list of all resources from the right side.
	 * @param originResources
	 *            The list of all resources from the origin side.
	 * @return A {@link ResourceMatcher} that can be used to retrieve the {@link MatchResource} corresponding
	 *         to the given input.
	 */
	protected ResourceMatcher createResourceMatcher(Iterable<Resource> leftResources,
			Iterable<Resource> rightResources, Iterable<Resource> originResources) {
		return new ResourceMatcher(leftResources, rightResources, originResources);
	}

	/**
	 * Returns the root of this Comparison. This default implementation will instantiate a Comparison model
	 * through the factory if needed.
	 * 
	 * @return The root of this Comparison.
	 */
	protected Comparison getComparison() {
		if (comparison == null) {
			comparison = CompareFactory.eINSTANCE.createComparison();
		}
		return comparison;
	}

	/**
	 * Returns the comparison scope associated with this engine.
	 * 
	 * @return The comparison scope associated with this engine.
	 */
	protected AbstractComparisonScope getScope() {
		return scope;
	}
}
