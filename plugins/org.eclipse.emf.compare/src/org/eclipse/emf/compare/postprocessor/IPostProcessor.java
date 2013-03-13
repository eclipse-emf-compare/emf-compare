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
package org.eclipse.emf.compare.postprocessor;

import com.google.common.collect.ImmutableList;

import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * Implementations of this interface can be used in order to tell EMF Compare how to make post treatments at
 * each step of the comparison.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IPostProcessor {

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the match step,
	 * from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the match step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postMatch(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the difference
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the difference step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postDiff(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the requirements
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the requirements step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postRequirements(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the equivalences
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the equivalences step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postEquivalences(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the conflicts step,
	 * from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the conflicts step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postConflicts(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after a comparison, from
	 * a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the all steps.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postComparison(Comparison comparison, Monitor monitor);

	/**
	 * Wrapper describing the given post processor.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	interface Descriptor {
		/**
		 * Returns the wrapped post processor.
		 * 
		 * @return the wrapped post processor
		 */
		IPostProcessor getPostProcessor();

		/**
		 * Returns the ordinal of this post processor.
		 * 
		 * @return The ordinal.
		 */
		int getOrdinal();

		/**
		 * Set the ordinal of this post processor.
		 * 
		 * @param parseInt
		 *            The ordinal.
		 */
		void setOrdinal(int parseInt);

		/**
		 * Returns the pattern of namespace URI on which this post processor can be applied.
		 * 
		 * @return The namespace URI pattern.
		 */
		Pattern getNsURI();

		/**
		 * Returns the pattern of resource URI on which this post processor can be applied.
		 * 
		 * @return The resource URI.
		 */
		Pattern getResourceURI();

		/**
		 * Returns the class name of the instance that will be returned by {@link #getPostProcessor()}.
		 * 
		 * @return the class name of the instance that will be returned by {@link #getPostProcessor()}.
		 */
		String getInstanceClassName();

		/**
		 * Registry of post processor.
		 * 
		 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
		 */
		public interface Registry<K> {

			/**
			 * Adds a post processor to the registry.
			 * 
			 * @param key
			 *            key with which the specified descriptor is to be associated
			 * @param descriptor
			 *            Post Processor that is to be added to this registry.
			 * @return the previous descriptor associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map
			 *         previously associated <tt>null</tt> with <tt>key</tt>, if the implementation supports
			 *         <tt>null</tt> values.)
			 */
			IPostProcessor.Descriptor put(K key, IPostProcessor.Descriptor descriptor);

			/**
			 * Removes all extensions from this registry.
			 * 
			 * @noreference This method is not intended to be referenced by clients.
			 */
			void clear();

			/**
			 * This will return a copy of the registered post processors list.
			 * 
			 * @return A copy of the registered post processors list.
			 */
			ImmutableList<IPostProcessor.Descriptor> getDescriptors();

			/**
			 * Removes a post processor from this registry.
			 * 
			 * @param key
			 *            key of the post processor descriptor that is to be removed from the registry.
			 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>.
			 */
			IPostProcessor.Descriptor remove(K key);

			/**
			 * Retrieve the post processors from a given <code>scope</code>. The scope provides the set of
			 * scanned namespaces and resource uris. If they match with the regex of some post processors,
			 * then they are returned.
			 * 
			 * @param scope
			 *            The given scope.
			 * @return The associated post processors if any.
			 */
			ImmutableList<IPostProcessor> getPostProcessors(IComparisonScope scope);
		}
	}
}
