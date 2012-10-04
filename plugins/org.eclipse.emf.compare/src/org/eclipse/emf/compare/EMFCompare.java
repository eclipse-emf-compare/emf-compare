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
package org.eclipse.emf.compare;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.UseIdentifiers;

/**
 * This class serves as the main entry point of a comparison. When all that is wanted is a basic comparison of
 * two or three notifiers, a comparison using all of the default configuration can be launched through
 * <code>EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right, origin)).compare()</code>.
 * <p>
 * When in need of a more customized comparison, the API can be used through chained calls. For example, if
 * you need to compare two notifiers ({@code left} and {@code right}) while ignoring their identifiers, with a
 * given progress monitor (call it {@code progress}), you can do so through : <code>
 * EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right)).matchById(UseIdentifiers.NEVER).setMonitor(progress).compare()
 * </code>.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFCompare {
	/**
	 * The matcher that should be used by the {@link org.eclipse.emf.compare.match.IMatchEngine match engine}
	 * to pair elements together.
	 * <p>
	 * Note that setting this overrides any configuration made through {@link #useIds}.
	 * </p>
	 */
	private IEObjectMatcher eObjectMatcher;

	/** This can be overriden to tweak the way EMF Compare sees two Objects as "equal". */
	private EqualityHelper equalityHelper;

	/** The progress monitor that should be used to display information about the comparison to the user. */
	private Monitor progressMonitor;

	/**
	 * Defines the scope of the comparison. Objects not present in this scope will be ignored by the whole
	 * process.
	 */
	private final IComparisonScope scope;

	/**
	 * This can be used to configure the ID handling of EMF Compare.
	 * <p>
	 * Do note that this configuration will be ignored if {@link #eObjectMatcher} is set.
	 * </p>
	 */
	private UseIdentifiers useIDs = UseIdentifiers.WHEN_AVAILABLE;

	/**
	 * Instantiation is only permitted through {@link #newComparator(IComparisonScope)}.
	 * 
	 * @param scope
	 *            The comparison scope to be used by EMF Compare.
	 */
	private EMFCompare(IComparisonScope scope) {
		checkNotNull(scope);
		this.scope = scope;
	}

	/**
	 * Creates a default EMF Compare Configuration.
	 * <p>
	 * This will use a basic monitor doing nothing to report progress, and the default implementation of an
	 * {@link EqualityHelper}.
	 * </p>
	 * 
	 * @return The default EMF Compare Configuration.
	 */
	public static EMFCompareConfiguration createDefaultConfiguration() {
		final Monitor monitor = new BasicMonitor();
		final EqualityHelper equalityHelper = new EqualityHelper();
		return new EMFCompareConfiguration(monitor, equalityHelper);
	}

	/**
	 * Creates a default comparison scope given its left and right notifiers.
	 * <p>
	 * The default comparison scope covers all proper content of the given notifiers, i.e any element
	 * contained directly under that notifier.
	 * </p>
	 * 
	 * @param left
	 *            The left notifier of this scope.
	 * @param right
	 *            The right notifier of this scope.
	 * @return The newly created scope, as used as default by EMF Compare.
	 * @see DefaultComparisonScope
	 */
	public static IComparisonScope createDefaultScope(Notifier left, Notifier right) {
		return new DefaultComparisonScope(left, right, null);
	}

	/**
	 * Creates the default comparison scope given its left and right notifiers, along with the common ancestor
	 * of both.
	 * <p>
	 * The default comparison scope covers all proper content of the given notifiers, i.e any element
	 * contained directly under that notifier.
	 * </p>
	 * 
	 * @param left
	 *            The left notifier of this scope.
	 * @param right
	 *            The right notifier of this scope.
	 * @param origin
	 *            The common ancestor of {@code left} and {@code right}.
	 * @return The newly created scope, as used as default by EMF Compare.
	 * @see DefaultComparisonScope
	 */
	public static IComparisonScope createDefaultScope(Notifier left, Notifier right, Notifier origin) {
		return new DefaultComparisonScope(left, right, origin);
	}

	/**
	 * Returns a new comparator to allow customization of the comparison process.
	 * 
	 * @param scope
	 *            Defines the scope of the comparison. Any object <b>not</b> present in this scope will be
	 *            ignored by the whole comparison process.
	 * @return A comparator that can be used as-is or further customized to compare the given {@code scope}.
	 */
	public static EMFCompare newComparator(IComparisonScope scope) {
		return new EMFCompare(scope);
	}

	/**
	 * Launches the comparison with the given configuration.
	 * 
	 * @param scope
	 *            The scope on which a comparison is to be performed.
	 * @param configuration
	 *            The configuration object from which compare engines will be configured.
	 * @param matcher
	 *            The EObject matcher that will be in charge of pairing EObjects together for this comparison.
	 * @return The result of this comparison.
	 */
	private static Comparison compare(IComparisonScope scope, EMFCompareConfiguration configuration,
			IEObjectMatcher matcher) {
		// TODO allow extension of the default match engine
		final IMatchEngine matchEngine = new DefaultMatchEngine(matcher);
		Comparison comparison = matchEngine.match(scope, configuration);

		final List<IPostProcessor> postProcessors = getPostProcessors(scope);
		for (IPostProcessor postProcessor : postProcessors) {
			postProcessor.postMatch(comparison);
		}

		final IDiffProcessor diffBuilder = new DiffBuilder();

		// TODO allow extension of the default diff engine
		final IDiffEngine diffEngine = new DefaultDiffEngine(diffBuilder);
		diffEngine.diff(comparison);

		for (IPostProcessor postProcessor : postProcessors) {
			postProcessor.postDiff(comparison);
		}

		final IReqEngine reqEngine = new DefaultReqEngine();
		reqEngine.computeRequirements(comparison);

		for (IPostProcessor postProcessor : postProcessors) {
			postProcessor.postRequirements(comparison);
		}

		final IEquiEngine equiEngine = new DefaultEquiEngine();
		equiEngine.computeEquivalences(comparison);

		for (IPostProcessor postProcessor : postProcessors) {
			postProcessor.postEquivalences(comparison);
		}

		if (comparison.isThreeWay()) {
			final IConflictDetector conflictDetector = new DefaultConflictDetector();
			conflictDetector.detect(comparison);

			for (IPostProcessor postProcessor : postProcessors) {
				postProcessor.postConflicts(comparison);
			}
		}

		return comparison;
	}

	/**
	 * Retrieve the post processors from a given <code>scope</code>. The scope provides the set of scanned
	 * namespaces and resource uris. If one of them matches with the regex of a
	 * "org.eclipse.emf.compare.postProcessor" extension point, then the associated post processor is
	 * returned.
	 * 
	 * @param scope
	 *            The given scope.
	 * @return The associated post processors if any.
	 */
	private static List<IPostProcessor> getPostProcessors(IComparisonScope scope) {
		final List<IPostProcessor> postProcessors = new ArrayList<IPostProcessor>();
		final Iterator<PostProcessorDescriptor> postProcessorIterator = EMFCompareExtensionRegistry
				.getRegisteredPostProcessors().iterator();
		while (postProcessorIterator.hasNext()) {
			final PostProcessorDescriptor descriptor = postProcessorIterator.next();
			if (descriptor.getNsURI() != null && descriptor.getNsURI().trim().length() != 0) {
				final Iterator<String> nsUris = scope.getNsURIs().iterator();
				while (nsUris.hasNext()) {
					if (nsUris.next().matches(descriptor.getNsURI())) {
						postProcessors.add(descriptor.getPostProcessor());
						break;
					}
				}
			}
			// Should probably use two loops here to prioritize NsURI matching
			if (descriptor.getResourceURI() != null && descriptor.getResourceURI().trim().length() != 0) {
				final Iterator<String> resourceUris = scope.getResourceURIs().iterator();
				while (resourceUris.hasNext()) {
					if (resourceUris.next().matches(descriptor.getResourceURI())) {
						postProcessors.add(descriptor.getPostProcessor());
						break;
					}
				}
			}
		}
		return postProcessors;
	}

	/**
	 * Launches the comparison with the current configuration.
	 * 
	 * @return The result of this comparison.
	 */
	public Comparison compare() {
		final Monitor monitor;
		if (progressMonitor != null) {
			monitor = progressMonitor;
		} else {
			monitor = new BasicMonitor();
		}

		final EqualityHelper helper;
		if (equalityHelper != null) {
			helper = equalityHelper;
		} else {
			helper = new EqualityHelper();
		}
		final EMFCompareConfiguration configuration = new EMFCompareConfiguration(monitor, helper);

		final IEObjectMatcher matcher = createMatcher(helper);

		return compare(scope, configuration, matcher);
	}

	/**
	 * Tells EMF Compare whether it should use identifiers when matching EObjects.
	 * <p>
	 * <b>Note</b> that this setting will be ignored if a custom matcher
	 * {@link #setEObjectMatcher(IEObjectMatcher) has been set}.
	 * </p>
	 * 
	 * @param matchByID
	 *            Whether we should use identifiers, and how.
	 * @return This same comparator to allow chained calls.
	 */
	public EMFCompare matchByID(UseIdentifiers matchByID) {
		if (matchByID != null) {
			this.useIDs = matchByID;
		}
		return this;
	}

	/**
	 * This can be used to provide a custom EObject matcher to EMF Compare whenever the default configurations
	 * (see {@link #matchByID(UseIdentifiers)}) are not sufficient.
	 * <p>
	 * <b>Note</b> that using this will supersede any setting made through {@link #matchByID(UseIdentifiers)}.
	 * </p>
	 * 
	 * @param matcher
	 *            The matcher EMF Compare should use to pair EObjects together.
	 * @return This same comparator to allow chained calls.
	 */
	public EMFCompare setEObjectMatcher(IEObjectMatcher matcher) {
		if (matcher != null) {
			this.eObjectMatcher = matcher;
		}
		return this;
	}

	/**
	 * If you need to tweak the function EMF Compare uses to determine whether two Objects are "equal", this
	 * can be used to provide it a new {@link EqualityHelper}.
	 * <p>
	 * Note that the equality helper will be used throughout the whole comparison process, from matching to
	 * conflict detection.
	 * </p>
	 * 
	 * @param helper
	 *            The equality helper that should be used by the comparison process.
	 * @return This same comparator to allow chained calls.
	 * @see EqualityHelper
	 */
	public EMFCompare setEqualityHelper(EqualityHelper helper) {
		if (helper != null) {
			this.equalityHelper = helper;
		}
		return this;
	}

	/**
	 * Changes the progress monitor that should be used by compare to display progress information about the
	 * comparison to the user.
	 * 
	 * @param monitor
	 *            The monitor that should be used
	 * @return This same builder to allow chained calls.
	 */
	public EMFCompare setMonitor(Monitor monitor) {
		if (monitor != null) {
			this.progressMonitor = monitor;
		}
		return this;
	}

	/**
	 * Creates the {@link IEObjectMatcher EObject matcher} that should be used by the comparison engine
	 * according to the values of {@link #eObjectMatcher} or {@link #useIDs}.
	 * 
	 * @param helper
	 *            The equality helper that should be used by the matcher if using proximity algorithms.
	 * @return The newly created matcher.
	 */
	private IEObjectMatcher createMatcher(EqualityHelper helper) {
		if (eObjectMatcher != null) {
			return eObjectMatcher;
		}

		final IEObjectMatcher matcher;
		switch (useIDs) {
			case NEVER:
				matcher = ProximityEObjectMatcher.builder(EditionDistance.builder(helper).build()).build();
				break;
			case ONLY:
				matcher = new IdentifierEObjectMatcher();
				break;
			case WHEN_AVAILABLE:
				// fall through to default
			default:
				// Use an ID matcher, delegating to proximity when no ID is available
				final IEObjectMatcher contentMatcher = ProximityEObjectMatcher.builder(
						EditionDistance.builder(helper).build()).build();
				matcher = new IdentifierEObjectMatcher(contentMatcher);
				break;

		}
		return matcher;
	}
}
