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
package org.eclipse.emf.compare.extension.merge;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;

/**
 * Interface for a merger.
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
	 * The merger registry.
	 */
	interface Registry {

		/**
		 * Returns the merger, for the given target, owning the highest ranking.
		 * 
		 * @param target
		 *            The given target difference.
		 * @return The found merger.
		 */
		IMerger getHighestRankingMerger(Diff target);

		/**
		 * Returns the list of the candidate mergers for the given difference.
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
	 * The implementation of the merger registry.
	 */
	public class RegistryImpl implements Registry {

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
		 * Returns the predicate to check if the current merger is a good candidate to handle the given target
		 * difference.
		 * 
		 * @param target
		 *            The given difference.
		 * @return The predicate.
		 */
		static final Predicate<IMerger> isMergerFor(final Diff target) {
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
		 * @see org.eclipse.emf.compare.extension.merge.IMerger.Registry#add(org.eclipse.emf.compare.extension.merge.IMerger)
		 */
		public IMerger add(IMerger merger) {
			Preconditions.checkNotNull(merger);
			merger.setRegistry(this);
			return map.put(merger.getClass().getName(), merger);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger.Registry#remove(java.lang.String)
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
		 * @see org.eclipse.emf.compare.extension.merge.IMerger.Registry#clear()
		 */
		public void clear() {
			map.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger.Registry#getHighestRankingMerger(org.eclipse.emf.compare.Diff)
		 */
		public IMerger getHighestRankingMerger(Diff target) {
			Iterator<IMerger> mergers = getMergers(target).iterator();

			IMerger ret = null;

			if (mergers.hasNext()) {
				IMerger highestRanking = mergers.next();
				while (mergers.hasNext()) {
					IMerger merger = mergers.next();
					if (merger.getRanking() > highestRanking.getRanking()) {
						highestRanking = merger;
					}
				}
				ret = highestRanking;
			}

			return ret;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger.Registry#getMergers(org.eclipse.emf.compare.Diff)
		 */
		public Collection<IMerger> getMergers(Diff target) {
			Iterable<IMerger> mergers = filter(map.values(), isMergerFor(target));
			List<IMerger> ret = newArrayList();
			for (IMerger merger : mergers) {
				ret.add(merger);
			}
			return ret;
		}

	}

	/**
	 * Implementation of <code>IMerger</code> to factorize the accessors to ranking.
	 */
	public abstract class AbstractMerger implements IMerger {

		/**
		 * The ranking of the merger.
		 */
		private int ranking;

		/**
		 * The merger registry.
		 */
		private Registry registry;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger#getRanking()
		 */
		public int getRanking() {
			return ranking;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger#setRanking(int)
		 */
		public void setRanking(int r) {
			ranking = r;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger#getRegistry()
		 */
		public Registry getRegistry() {
			return registry;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.extension.merge.IMerger#setRegistry(org.eclipse.emf.compare.extension.merge.IMerger.Registry)
		 */
		public void setRegistry(Registry registry) {
			if (this.registry != null && registry != null) {
				throw new IllegalStateException("The registry has to be set only once."); //$NON-NLS-1$
			}
			this.registry = registry;
		}

	}

}
