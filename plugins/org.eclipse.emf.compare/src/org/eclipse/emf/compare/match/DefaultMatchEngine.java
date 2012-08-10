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
import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompareConfiguration;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.resource.IResourceMatcher;
import org.eclipse.emf.compare.match.resource.StrategyResourceMatcher;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * The Match engine orchestrates the matching process : it takes an {@link IComparisonScope scope} as input,
 * iterates over its {@link IComparisonScope#getLeft() left}, {@link IComparisonScope#getRight() right} and
 * {@link IComparisonScope#getOrigin() origin} roots and delegates to {@link IResourceMatcher}s and
 * {@link IEObjectMatcher}s in order to create the result {@link Comparison} model for this scope.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultMatchEngine implements IMatchEngine {
	/** Root of this comparison, should only be accessed or instantiated through {@link #getComparison()}. */
	private Comparison comparison;

	/** The comparison scope that will be used by this engine. Should be accessed through {@link #getScope()}. */
	private IComparisonScope comparisonScope;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine#match(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public Comparison match(IComparisonScope scope, EMFCompareConfiguration configuration) {
		this.comparisonScope = scope;
		associate(getComparison(), configuration);

		final Notifier left = getScope().getLeft();
		final Notifier right = getScope().getRight();
		final Notifier origin = getScope().getOrigin();

		getComparison().setThreeWay(origin != null);

		// FIXME side-effect coding
		if (left instanceof ResourceSet || right instanceof ResourceSet) {
			match((ResourceSet)left, (ResourceSet)right, (ResourceSet)origin);
		} else if (left instanceof Resource || right instanceof Resource) {
			// Our "roots" are Resources. Consider them matched
			final MatchResource match = CompareFactory.eINSTANCE.createMatchResource();

			match.setLeft((Resource)left);
			match.setRight((Resource)right);
			match.setOrigin((Resource)origin);

			if (left != null && ((Resource)left).getURI() != null) {
				match.setLeftURI(((Resource)left).getURI().toString());
			}
			if (right != null && ((Resource)right).getURI() != null) {
				match.setRightURI(((Resource)right).getURI().toString());
			}
			if (origin != null && ((Resource)origin).getURI() != null) {
				match.setOriginURI(((Resource)origin).getURI().toString());
			}

			getComparison().getMatchedResources().add(match);
			match((Resource)left, (Resource)right, (Resource)origin);
		} else if (left instanceof EObject || right instanceof EObject) {
			match((EObject)left, (EObject)right, (EObject)origin);
		} else {
			// FIXME
		}

		return getComparison();
	}

	/**
	 * This will be used to match the given {@link ResourceSet}s. This default implementation will query the
	 * comparison scope for these resource sets children, then delegate to an {@link IResourceMatcher} to
	 * determine the resource mappings.
	 * 
	 * @param left
	 *            The left {@link ResourceSet}.
	 * @param right
	 *            The common {@link ResourceSet}.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. Can be <code>null</code>.
	 */
	protected void match(ResourceSet left, ResourceSet right, ResourceSet origin) {
		final Iterator<? extends Resource> leftChildren = getScope().getCoveredResources(left);
		final Iterator<? extends Resource> rightChildren = getScope().getCoveredResources(right);
		final Iterator<? extends Resource> originChildren;
		if (origin != null) {
			originChildren = getScope().getCoveredResources(origin);
		} else {
			originChildren = Iterators.emptyIterator();
		}

		final IResourceMatcher matcher = getResourceMatcher();
		final Iterable<MatchResource> mappings = matcher.createMappings(leftChildren, rightChildren,
				originChildren);

		for (MatchResource mapping : mappings) {
			getComparison().getMatchedResources().add(mapping);

			match(mapping.getLeft(), mapping.getRight(), mapping.getOrigin());
		}
	}

	/**
	 * This will only query the scope for the given Resources' children, then delegate to an
	 * {@link IEObjectMatcher} to determine the EObject matches.
	 * <p>
	 * We expect at least two of the given resources not to be <code>null</code>.
	 * </p>
	 * 
	 * @param left
	 *            The left {@link Resource}. Can be <code>null</code>.
	 * @param right
	 *            The right {@link Resource}. Can be <code>null</code>.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. Can be <code>null</code>.
	 */
	protected void match(Resource left, Resource right, Resource origin) {
		// We need at least two resources to match them
		if (atLeastTwo(left == null, right == null, origin == null)) {
			/*
			 * TODO But if we have only one resource, which is then unmatched, should we not still do
			 * something with it?
			 */
			return;
		}

		final Iterator<? extends EObject> leftEObjects;
		if (left != null) {
			leftEObjects = getScope().getCoveredEObjects(left);
		} else {
			leftEObjects = Iterators.emptyIterator();
		}
		final Iterator<? extends EObject> rightEObjects;
		if (right != null) {
			rightEObjects = getScope().getCoveredEObjects(right);
		} else {
			rightEObjects = Iterators.emptyIterator();
		}
		final Iterator<? extends EObject> originEObjects;
		if (origin != null) {
			originEObjects = getScope().getCoveredEObjects(origin);
		} else {
			originEObjects = Iterators.emptyIterator();
		}

		final IEObjectMatcher matcher = createEObjectMatcher();
		final Iterable<Match> matches = matcher.createMatches(leftEObjects, rightEObjects, originEObjects);

		Iterables.addAll(getComparison().getMatches(), matches);
	}

	/**
	 * This will query the scope for the given {@link EObject}s' children, then delegate to an
	 * {@link IEObjectMatcher} to compute the {@link Match}es.
	 * <p>
	 * We expect at least the <code>left</code> and <code>right</code> EObjects not to be <code>null</code>.
	 * </p>
	 * 
	 * @param left
	 *            The left {@link EObject}.
	 * @param right
	 *            The right {@link EObject}.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>.
	 */
	protected void match(EObject left, EObject right, EObject origin) {
		if (left == null || right == null) {
			// FIXME IAE or NPE?
			throw new IllegalArgumentException();
		}

		final Iterator<? extends EObject> leftEObjects = Iterators.concat(Iterators.singletonIterator(left),
				getScope().getChildren(left));
		final Iterator<? extends EObject> rightEObjects = Iterators.concat(
				Iterators.singletonIterator(right), getScope().getChildren(right));
		final Iterator<? extends EObject> originEObjects;
		if (origin != null) {
			originEObjects = Iterators.concat(Iterators.singletonIterator(origin), getScope().getChildren(
					origin));
		} else {
			originEObjects = Iterators.emptyIterator();
		}

		final IEObjectMatcher matcher = createEObjectMatcher();
		final Iterable<Match> matches = matcher.createMatches(leftEObjects, rightEObjects, originEObjects);

		Iterables.addAll(getComparison().getMatches(), matches);
	}

	/**
	 * This will be used to create the resource matcher that will be used by this match engine.
	 * 
	 * @return An {@link IResourceMatcher} that can be used to retrieve the {@link MatchResource}s for this
	 *         comparison.
	 */
	protected IResourceMatcher getResourceMatcher() {
		return new StrategyResourceMatcher();
	}

	/**
	 * This will be used to create the EObject matcher that will be used by this match engine.
	 * <p>
	 * This default implementation uses an {@link IdentifierEObjectMatcher} to match EObjects through their ID
	 * only.
	 * </p>
	 * 
	 * @return An {@link IEObjectMatcher} that can be used to retrieve the {@link Match}es for this
	 *         comparison.
	 */
	protected IEObjectMatcher createEObjectMatcher() {
		return new IdentifierEObjectMatcher();
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
	protected IComparisonScope getScope() {
		return comparisonScope;
	}

	/**
	 * This will check that at least two of the three given booleans are <code>true</code>.
	 * 
	 * @param condition1
	 *            First of the three booleans.
	 * @param condition2
	 *            Second of the three booleans.
	 * @param condition3
	 *            Third of the three booleans.
	 * @return <code>true</code> if at least two of the three given booleans are <code>true</code>,
	 *         <code>false</code> otherwise.
	 */
	protected static boolean atLeastTwo(boolean condition1, boolean condition2, boolean condition3) {
		// CHECKSTYLE:OFF This expression is alone in its method, and documented.
		return condition1 && (condition2 || condition3) || (condition2 && condition3);
		// CHECKSTYLE:ON
	}

	/**
	 * Removes any already existing {@link EMFCompareConfiguration} adapter on the given
	 * <code>comparison</code> object. Then it associates the given <code>configuration</code> with the
	 * <code>comparison</code>.
	 * 
	 * @param comparison
	 *            the comparison receiver of the Adapter
	 * @param configuration
	 *            the Adapter to associtate to the comparison.
	 */
	private static void associate(Comparison comparison, EMFCompareConfiguration configuration) {
		Iterator<Adapter> eAdapters = comparison.eAdapters().iterator();
		while (eAdapters.hasNext()) {
			Adapter eAdapter = eAdapters.next();
			if (eAdapter.isAdapterForType(EMFCompareConfiguration.class)) {
				eAdapters.remove();
				if (eAdapter instanceof Adapter.Internal) {
					((Adapter.Internal)eAdapter).unsetTarget(comparison);
				}
			}
		}
		comparison.eAdapters().add(configuration);
		configuration.setTarget(comparison);
	}
}
