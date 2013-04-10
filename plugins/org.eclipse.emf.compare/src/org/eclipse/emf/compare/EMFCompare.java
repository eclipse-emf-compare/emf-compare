/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
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

import java.util.List;

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
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This class serves as the main entry point of a comparison. When all that is wanted is a basic comparison of
 * two or three notifiers, a comparison using all of the default configuration can be launched through
 * <code>EMFCompare.builder().build().compare(EMFCompare.createDefaultScope(left, right, origin))</code>.
 * <p>
 * When in need of a more customized comparison, the API can be used through chained calls. For example, if
 * you need to compare two notifiers ({@code left} and {@code right}) while ignoring their identifiers, with a
 * given progress monitor (call it {@code progress}), you can do so through : <code>
 * EMFCompare.builder().setMatchEngine(DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(EMFCompare.createDefaultScope(left, right), new BasicMonitor())
 * </code>.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompare {

	/** The registry we'll use to create a match engine for this comparison. */
	private final IMatchEngine.Factory.Registry matchEngineFactoryRegistry;

	/** The IDiffEngine to use to compute comparison. */
	private final IDiffEngine diffEngine;

	/** The IReqEngine to use to compute comparison. */
	private final IReqEngine reqEngine;

	/** The IEquiEngine to use to compute comparison. */
	private final IEquiEngine equiEngine;

	/** The IConflictDetector to use to compute comparison. */
	private final IConflictDetector conflictDetector;

	/** The PostProcessorRegistry to use to find an IPostProcessor. */
	private final IPostProcessor.Descriptor.Registry<?> postProcessorDescriptorRegistry;

	/**
	 * Creates a new EMFCompare object able to compare Notifier with the help of given engines.
	 * 
	 * @param matchEngineFactoryRegistry
	 *            {@link IMatchEngine.Factory.Registry} to use to find a match engine factory to compute
	 *            comparison
	 * @param diffEngine
	 *            IDiffEngine to use to compute comparison
	 * @param reqEngine
	 *            IReqEngine to use to compute comparison
	 * @param equiEngine
	 *            IEquiEngine to use to compute comparison
	 * @param conflictDetector
	 *            IConflictDetector to use to compute comparison
	 * @param postProcessorFactoryRegistry
	 *            PostProcessorRegistry to use to find an IPostProcessor
	 */
	protected EMFCompare(IMatchEngine.Factory.Registry matchEngineFactoryRegistry, IDiffEngine diffEngine,
			IReqEngine reqEngine, IEquiEngine equiEngine, IConflictDetector conflictDetector,
			IPostProcessor.Descriptor.Registry<?> postProcessorFactoryRegistry) {
		this.matchEngineFactoryRegistry = checkNotNull(matchEngineFactoryRegistry);
		this.diffEngine = checkNotNull(diffEngine);
		this.reqEngine = checkNotNull(reqEngine);
		this.equiEngine = checkNotNull(equiEngine);
		this.conflictDetector = conflictDetector;
		this.postProcessorDescriptorRegistry = checkNotNull(postProcessorFactoryRegistry);
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
	 * Computes and returns a new Comparison object representation the differences between Notifier in the
	 * give {@code scope}.
	 * 
	 * @param scope
	 *            the scope to compare.
	 * @return the result of the comparison.
	 */
	public Comparison compare(IComparisonScope scope) {
		return compare(scope, new BasicMonitor());
	}

	/**
	 * Launches the comparison with the given scope and reporting progress to the given {@code monitor}.
	 * 
	 * @param scope
	 *            the scope to compare.
	 * @param monitor
	 *            the monitor to report progress to.
	 * @return the result of the comparison.
	 */
	public Comparison compare(IComparisonScope scope, final Monitor monitor) {
		checkNotNull(scope);
		checkNotNull(monitor);

		final Comparison comparison = matchEngineFactoryRegistry.getHighestRankingMatchEngineFactory(scope)
				.getMatchEngine().match(scope, monitor);

		List<IPostProcessor> postProcessors = postProcessorDescriptorRegistry.getPostProcessors(scope);

		for (IPostProcessor iPostProcessor : postProcessors) {
			iPostProcessor.postMatch(comparison, monitor);
		}

		diffEngine.diff(comparison, monitor);

		for (IPostProcessor iPostProcessor : postProcessors) {
			iPostProcessor.postDiff(comparison, monitor);
		}

		reqEngine.computeRequirements(comparison, monitor);

		for (IPostProcessor iPostProcessor : postProcessors) {
			iPostProcessor.postRequirements(comparison, monitor);
		}

		equiEngine.computeEquivalences(comparison, monitor);

		for (IPostProcessor iPostProcessor : postProcessors) {
			iPostProcessor.postEquivalences(comparison, monitor);
		}

		if (comparison.isThreeWay() && conflictDetector != null) {
			conflictDetector.detect(comparison, monitor);

			for (IPostProcessor iPostProcessor : postProcessors) {
				iPostProcessor.postConflicts(comparison, monitor);
			}
		}

		for (IPostProcessor iPostProcessor : postProcessors) {
			iPostProcessor.postComparison(comparison, monitor);
		}

		return comparison;
	}

	/**
	 * Creates a new builder to configure the creation of a new EMFCompare object.
	 * 
	 * @return a new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A Builder pattern to instantiate EMFCompare objects.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class Builder {

		/** The registry we'll use to create a match engine for this comparison. */
		protected IMatchEngine.Factory.Registry matchEngineFactoryRegistry;

		/** The IReqEngine to use to compute comparison. */
		protected IReqEngine reqEngine;

		/** The IDiffEngine to use to compute comparison. */
		protected IDiffEngine diffEngine;

		/** The IEquiEngine to use to compute comparison. */
		protected IEquiEngine equiEngine;

		/** The IConflictDetector to use to compute conflicts. */
		protected IConflictDetector conflictDetector;

		/** The PostProcessorRegistry to use to find an IPostProcessor. */
		protected IPostProcessor.Descriptor.Registry<?> registry;

		/**
		 * Creates a new builder object.
		 */
		protected Builder() {
		}

		/**
		 * Sets the IMatchEngine.Factory.Registry to be used to find a match engine factory to compute
		 * comparison.
		 * 
		 * @param mefr
		 *            the IMatchEngine.Factory.Registry to be used to find a match engine factory to compute
		 *            comparison.
		 * @return this same builder to allow chained call.
		 */
		public Builder setMatchEngineFactoryRegistry(IMatchEngine.Factory.Registry mefr) {
			this.matchEngineFactoryRegistry = checkNotNull(mefr);
			return this;
		}

		/**
		 * Sets the IDiffEngine to be used to compute Diff.
		 * 
		 * @param de
		 *            the IDiffEngine to be used to compute Diff.
		 * @return this same builder to allow chained call.
		 */
		public Builder setDiffEngine(IDiffEngine de) {
			this.diffEngine = checkNotNull(de);
			return this;
		}

		/**
		 * Sets the IReqEngine to be used to compute dependencies between Diff.
		 * 
		 * @param re
		 *            the IReqEngine to be used to compute dependencies between Diff.
		 * @return this same builder to allow chained call.
		 */
		public Builder setRequirementEngine(IReqEngine re) {
			this.reqEngine = checkNotNull(re);
			return this;
		}

		/**
		 * Sets the IEquiEngine to be used to compute equivalences between Diff.
		 * 
		 * @param ee
		 *            the IEquiEngine to be used to compute equivalences between Diff
		 * @return this same builder to allow chained call.
		 */
		public Builder setEquivalenceEngine(IEquiEngine ee) {
			this.equiEngine = checkNotNull(ee);
			return this;
		}

		/**
		 * Sets the IEquiEngine to be used to compute conflicts between Diff.
		 * 
		 * @param cd
		 *            the IEquiEngine to be used to compute conflicts between Diff.
		 * @return this same builder to allow chained call.
		 */
		public Builder setConflictDetector(IConflictDetector cd) {
			this.conflictDetector = checkNotNull(cd);
			return this;
		}

		/**
		 * Sets the PostProcessor to be used to find the post processor of each comparison steps.
		 * 
		 * @param r
		 *            the PostProcessor to be used to find the post processor of each comparison steps.
		 * @return this same builder to allow chained call.
		 */
		public Builder setPostProcessorRegistry(IPostProcessor.Descriptor.Registry<?> r) {
			this.registry = checkNotNull(r);
			return this;
		}

		/**
		 * Instantiates and return an EMFCompare object configured with the previously given engines.
		 * 
		 * @return an EMFCompare object configured with the previously given engines
		 */
		public EMFCompare build() {
			if (matchEngineFactoryRegistry == null) {
				matchEngineFactoryRegistry = MatchEngineFactoryRegistryImpl.createStandaloneInstance();
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
				registry = new PostProcessorDescriptorRegistryImpl<Object>();
			}
			if (conflictDetector == null) {
				conflictDetector = new DefaultConflictDetector();
			}
			return new EMFCompare(this.matchEngineFactoryRegistry, this.diffEngine, this.reqEngine,
					this.equiEngine, this.conflictDetector, this.registry);
		}
	}

}
