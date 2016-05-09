/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 466552
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompareMessages;

/**
 * Mergers are used by EMF Compare to merge specific differences from one side to the other. A number of
 * default mergers are provided by EMF Compare, but they can be sub-classed and extended by clients through
 * the extension point "org.eclipse.emf.compare.ide.mergerExtension".
 * <p>
 * Clients are encouraged to subclass AbstractMerger instead of implementing IMerger so that they can benefit
 * from a maximum of the common procedures.
 * </p>
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public interface IMerger {
	/**
	 * Check if the merger is a good candidate to merge the given difference.
	 * 
	 * @param target
	 *            The given target difference.
	 * @return True if it is the good candidate, false otherwise.
	 */
	boolean isMergerFor(Diff target);

	/**
	 * Returns the ranking of this merger.
	 * 
	 * @return The ranking.
	 */
	int getRanking();

	/**
	 * Set the ranking of this merger.
	 * 
	 * @param parseInt
	 *            The ranking.
	 */
	void setRanking(int parseInt);

	/**
	 * Executes the copy from right to left.
	 * 
	 * @param target
	 *            The difference to handle.
	 * @param monitor
	 *            Monitor.
	 */
	void copyRightToLeft(Diff target, Monitor monitor);

	/**
	 * Executes the copy from left to right.
	 * 
	 * @param target
	 *            The difference to handle.
	 * @param monitor
	 *            Monitor.
	 */
	void copyLeftToRight(Diff target, Monitor monitor);

	/**
	 * Set the registry containing this merger.
	 * 
	 * @param registry
	 *            The merger registry.
	 */
	void setRegistry(Registry registry);

	/**
	 * Get the registry.
	 * 
	 * @return The registry.
	 */
	Registry getRegistry();

	/**
	 * This will hold all registered mergers. Mergers can be registered manually in the registry, but they are
	 * usually registered through the "org.eclipse.emf.compare.ide.mergerExtension" extension point.
	 * <p>
	 * An instance of the registry is usually accessed through
	 * "EMFCompareIDEPlugin.getDefault().getMergerRegistry()". However, if you need an instance of the
	 * registry in a standalone environment, you should use "IMerger.RegistryImpl.createStandaloneInstance()"
	 * so that the default registrations are taken care of.
	 * </p>
	 */
	public interface Registry {
		/**
		 * Returns the merger, for the given target, owning the highest ranking.
		 * 
		 * @param target
		 *            The given target difference.
		 * @return The found merger.
		 */
		IMerger getHighestRankingMerger(Diff target);

		/**
		 * Returns the list of the candidate mergers for the given difference. If the given difference is
		 * null, return all known mergers.
		 * 
		 * @param target
		 *            The given difference.
		 * @return The list of the found mergers.
		 */
		Collection<IMerger> getMergers(Diff target);

		/**
		 * Adds a merger to the registry.
		 * 
		 * @param merger
		 *            The merger.
		 * @return The previously registered merger.
		 */
		IMerger add(IMerger merger);

		/**
		 * Removes a merger from the registry, from its class name.
		 * 
		 * @param className
		 *            The class name.
		 * @return The previously registered merger.
		 */
		IMerger remove(String className);

		/**
		 * Clear the registry.
		 */
		void clear();
	}

	/**
	 * Registry that can provide its mergers sorted by rank descending.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 * @since 3.3
	 */
	public interface Registry2 extends Registry {

		/**
		 * Provide the merger with the highest ranking among those that:
		 * <ul>
		 * <li>Are compatible with the given diff;</li>
		 * <li>Match the given non-null criterion.</li>
		 * </ul>
		 * 
		 * @param diff
		 *            The diff
		 * @param criterion
		 *            The criterion
		 * @return The best merger found, should never be null (throw an exception if no merger found)
		 */
		Iterator<IMerger> getMergersByRankDescending(Diff diff, IMergeCriterion criterion);

	}

	/**
	 * A default implementation of an {@link IMerger.Registry}. This is the implementation EMF Compare will
	 * use through its GUI.
	 */
	class RegistryImpl implements Registry2 {
		/**
		 * Map which references the registered mergers per their class name.
		 */
		private final Map<String, IMerger> map;

		/**
		 * Constructor.
		 */
		public RegistryImpl() {
			map = new ConcurrentHashMap<String, IMerger>();
		}

		/**
		 * Returns a registry filled with the default mergers provided by EMF Compare; namely
		 * {@link AttributeChangeMerger}, {@link ReferenceChangeMerger}, {@link FeatureMapChangeMerger},
		 * {@link ResourceAttachmentChangeMerger}, {@link PseudoConflictMerger} and {@link ConflictMerger}.
		 * 
		 * @return A registry filled with the default mergers provided by EMF Compare.
		 */
		public static IMerger.Registry createStandaloneInstance() {
			final IMerger.Registry registry = new RegistryImpl();

			// We need our pseudo-conflict merger and conflict merger to have a higher ranking than
			// default.
			final int defaultRanking = 10;
			final int pseudoConflictRanking = 75;
			final int conflictRanking = 100;

			final IMerger attributeMerger = new AttributeChangeMerger();
			attributeMerger.setRanking(defaultRanking);
			final IMerger referenceMerger = new ReferenceChangeMerger();
			referenceMerger.setRanking(defaultRanking);
			final IMerger additiveReferenceMerger = new AdditiveReferenceChangeMerger();
			additiveReferenceMerger.setRanking(defaultRanking);
			final IMerger featureMapMerger = new FeatureMapChangeMerger();
			featureMapMerger.setRanking(defaultRanking);
			final IMerger resourceAttachmentMerger = new ResourceAttachmentChangeMerger();
			resourceAttachmentMerger.setRanking(defaultRanking);
			final IMerger pseudoConflictMerger = new PseudoConflictMerger();
			pseudoConflictMerger.setRanking(pseudoConflictRanking);
			final IMerger conflictMerger = new ConflictMerger();
			conflictMerger.setRanking(conflictRanking);
			final IMerger additiveConflictMerger = new AdditiveConflictMerger();
			additiveConflictMerger.setRanking(conflictRanking);

			registry.add(attributeMerger);
			registry.add(referenceMerger);
			registry.add(additiveReferenceMerger);
			registry.add(featureMapMerger);
			registry.add(resourceAttachmentMerger);
			registry.add(pseudoConflictMerger);
			registry.add(conflictMerger);
			registry.add(additiveConflictMerger);

			return registry;
		}

		/**
		 * Returns the predicate to check if the current merger is a good candidate to handle the given target
		 * difference.
		 * 
		 * @param target
		 *            The given difference.
		 * @return The predicate.
		 */
		private static Predicate<IMerger> isMergerFor(final Diff target) {
			return new Predicate<IMerger>() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see com.google.common.base.Predicate#apply(java.lang.Object)
				 */
				public boolean apply(IMerger d) {
					return d.isMergerFor(target);
				}
			};
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.merge.IMerger.Registry#add(org.eclipse.emf.compare.merge.IMerger)
		 */
		public IMerger add(IMerger merger) {
			Preconditions.checkNotNull(merger);
			merger.setRegistry(this);
			return map.put(merger.getClass().getName(), merger);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.merge.IMerger.Registry#remove(java.lang.String)
		 */
		public IMerger remove(String className) {
			IMerger previous = map.remove(className);
			if (previous != null) {
				previous.setRegistry(null);
			}
			return previous;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.merge.IMerger.Registry#clear()
		 */
		public void clear() {
			map.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.merge.IMerger.Registry#getHighestRankingMerger(org.eclipse.emf.compare.Diff)
		 */
		public IMerger getHighestRankingMerger(Diff target) {
			Iterator<IMerger> mergers = getMergersByRankDescending(target, null);
			if (mergers.hasNext()) {
				return mergers.next();
			}
			throw new IllegalStateException(EMFCompareMessages.getString("IMerger.MissingMerger", target //$NON-NLS-1$
					.getClass().getSimpleName()));
		}

		/**
		 * Provide the mergers sorted by rank descending.
		 * 
		 * @param diff
		 *            The diff
		 * @param criterion
		 *            The merge criterion, can be <code>null</code>
		 * @return the registered mergers valid for the given diff and criterion, sorted by rank descending.
		 * @since 3.3
		 */
		public Iterator<IMerger> getMergersByRankDescending(Diff diff, final IMergeCriterion criterion) {
			List<IMerger> mergers = new ArrayList<IMerger>(getMergers(diff));
			Collections.sort(mergers, new Comparator<IMerger>() {
				public int compare(IMerger o1, IMerger o2) {
					// Sort from largest to smallest
					return o2.getRanking() - o1.getRanking();
				}
			});
			return Iterators.filter(mergers.iterator(), new Predicate<IMerger>() {
				public boolean apply(IMerger input) {
					if (input instanceof IMergeCriterionAware) {
						if (((IMergeCriterionAware)input).apply(criterion)) {
							return true;
						}
						return false;
					}
					return true;
				}
			});
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.merge.IMerger.Registry#getMergers(org.eclipse.emf.compare.Diff)
		 */
		public Collection<IMerger> getMergers(Diff target) {
			final Predicate<IMerger> mergerFor;
			if (target == null) {
				mergerFor = Predicates.alwaysTrue();
			} else {
				mergerFor = isMergerFor(target);
			}
			Iterable<IMerger> mergers = filter(map.values(), mergerFor);
			List<IMerger> ret = newArrayList(mergers);
			return ret;
		}
	}
}
