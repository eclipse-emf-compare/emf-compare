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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IAccessorFactory {

	boolean isFactoryFor(Object target);

	int getRanking();

	void setRanking(int parseInt);

	ITypedElement createLeft(AdapterFactory adapterFactory, Object target);

	ITypedElement createRight(AdapterFactory adapterFactory, Object target);

	ITypedElement createAncestor(AdapterFactory adapterFactory, Object target);

	interface Registry {

		IAccessorFactory getHighestRankingFactory(Object target);

		Collection<IAccessorFactory> getFactories(Object target);

		IAccessorFactory add(IAccessorFactory factory);

		IAccessorFactory remove(String className);

		void clear();
	}

	public class RegistryImpl implements Registry {

		private final Map<String, IAccessorFactory> map;

		/**
		 * 
		 */
		public RegistryImpl() {
			map = new ConcurrentHashMap<String, IAccessorFactory>();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry#getHighestRankingFactory(java.lang.Object)
		 */
		public IAccessorFactory getHighestRankingFactory(Object target) {
			Iterator<IAccessorFactory> factories = getFactories(target).iterator();

			IAccessorFactory ret = null;

			if (factories.hasNext()) {
				IAccessorFactory highestRanking = factories.next();
				while (factories.hasNext()) {
					IAccessorFactory factory = factories.next();
					if (factory.getRanking() > highestRanking.getRanking()) {
						highestRanking = factory;
					}
				}
				ret = highestRanking;
			}

			return ret;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry#getFactories(java.lang.Object)
		 */
		// safe thanks to the isFactoryFor filter
		public List<IAccessorFactory> getFactories(Object target) {
			Iterable<IAccessorFactory> factories = filter(map.values(), isFactoryFor(target));
			List<IAccessorFactory> ret = newArrayList();
			for (IAccessorFactory factory : factories) {
				ret.add(factory);
			}
			return ret;
		}

		static final Predicate<IAccessorFactory> isFactoryFor(final Object target) {
			return new Predicate<IAccessorFactory>() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see com.google.common.base.Predicate#apply(java.lang.Object)
				 */
				public boolean apply(IAccessorFactory d) {
					return d.isFactoryFor(target);
				}
			};
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry#add(java.lang.Object)
		 */
		public IAccessorFactory add(IAccessorFactory factory) {
			Preconditions.checkNotNull(factory);
			return map.put(factory.getClass().getName(), factory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry#remove(java.lang.Object)
		 */
		public IAccessorFactory remove(String className) {
			return map.remove(className);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry#clear()
		 */
		public void clear() {
			map.clear();
		}
	}

	public abstract class AbstractAccessorFactory implements IAccessorFactory {

		private int ranking;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#getRanking()
		 */
		public int getRanking() {
			return ranking;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#setRanking(int)
		 */
		public void setRanking(int r) {
			ranking = r;
		}

	}

}
