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
import static com.google.common.base.Preconditions.checkState;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
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
public class EMFCompare {

	private final IMatchEngine matchEngine;

	private final IDiffEngine diffEngine;

	private final IReqEngine reqEngine;

	private final IEquiEngine equiEngine;

	private final IConflictDetector conflictDetector;

	private final PostProcessorRegistry postProcessorRegistry;

	/**
	 */
	protected EMFCompare(IMatchEngine matchEngine, IDiffEngine diffEngine, IReqEngine reqEngine,
			IEquiEngine equiEngine, IConflictDetector conflictDetector,
			PostProcessorRegistry postProcessorRegistry) {
		this.matchEngine = checkNotNull(matchEngine);
		this.diffEngine = checkNotNull(diffEngine);
		this.reqEngine = checkNotNull(reqEngine);
		this.equiEngine = checkNotNull(equiEngine);
		this.conflictDetector = conflictDetector;
		this.postProcessorRegistry = checkNotNull(postProcessorRegistry);
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
	@Deprecated
	public static EMFCompareConfiguration createDefaultConfiguration() {
		final Monitor monitor = new BasicMonitor();
		return new EMFCompareConfiguration(monitor, null);
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

	public Comparison compare(IComparisonScope scope) {
		return compare(scope, new BasicMonitor());
	}

	/**
	 * Launches the comparison with the current configuration.
	 * 
	 * @return The result of this comparison.
	 */
	public Comparison compare(IComparisonScope scope, final Monitor monitor) {
		checkNotNull(scope);
		checkNotNull(monitor);
		if (scope.getOrigin() != null) {
			checkState(conflictDetector != null,
					"ConflictDetector must not be null to compute a 3-way comparison");
		}

		final Comparison comparison = matchEngine.match(scope, null/* monitor */);

		IPostProcessor postProcessor = postProcessorRegistry.getPostProcessor(scope);

		if (postProcessor != null) {
			postProcessor.postMatch(comparison/* , monitor */);
		}

		diffEngine.diff(comparison/* , monitor */);

		if (postProcessor != null) {
			postProcessor.postDiff(comparison/* , monitor */);
		}

		reqEngine.computeRequirements(comparison/* , monitor */);

		if (postProcessor != null) {
			postProcessor.postRequirements(comparison/* , monitor */);
		}

		equiEngine.computeEquivalences(comparison/* , monitor */);

		if (postProcessor != null) {
			postProcessor.postEquivalences(comparison/* , monitor */);
		}

		if (comparison.isThreeWay()) {
			conflictDetector.detect(comparison/* , monitor */);

			if (postProcessor != null) {
				postProcessor.postConflicts(comparison/* , monitor */);
			}
		}

		return comparison;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		protected IMatchEngine matchEngine;

		protected IReqEngine reqEngine;

		protected IDiffEngine diffEngine;

		protected IEquiEngine equiEngine;

		protected IConflictDetector conflictDetector;

		protected PostProcessorRegistry registry;

		protected Builder() {
		}

		public Builder setMatchEngine(IMatchEngine matchEngine) {
			this.matchEngine = checkNotNull(matchEngine);
			return this;
		}

		public Builder setDiffEngine(IDiffEngine diffEngine) {
			this.diffEngine = checkNotNull(diffEngine);
			return this;
		}

		public Builder setRequirementEngine(IReqEngine reqEngine) {
			this.reqEngine = checkNotNull(reqEngine);
			return this;
		}

		public Builder setEquivalenceEngine(IEquiEngine equiEngine) {
			this.equiEngine = checkNotNull(equiEngine);
			return this;
		}

		public Builder setConflictDetector(IConflictDetector conflictDetector) {
			this.conflictDetector = checkNotNull(conflictDetector);
			return this;
		}

		public Builder setPostProcessorRegistry(PostProcessorRegistry registry) {
			this.registry = checkNotNull(registry);
			return this;
		}

		public EMFCompare build() {
			if (matchEngine == null) {
				matchEngine = DefaultMatchEngine.create(UseIdentifiers.WHEN_AVAILABLE);
			}
			if (diffEngine == null) {
				diffEngine = new DefaultDiffEngine(new DiffBuilder());
			}
			if (reqEngine == null) {
				reqEngine = new DefaultReqEngine();
			}
			if (equiEngine == null) {
				equiEngine = new DefaultEquiEngine();
			}
			if (registry == null) {
				registry = new PostProcessorRegistry();
			}
			if (conflictDetector == null) {
				conflictDetector = new DefaultConflictDetector();
			}
			return new EMFCompare(this.matchEngine, this.diffEngine, this.reqEngine, this.equiEngine,
					this.conflictDetector, this.registry);
		}
	}

}
