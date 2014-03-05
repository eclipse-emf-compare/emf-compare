/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import java.util.Collection;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Provide the weight to consider while comparing EObjects by their content.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface WeightProvider {

	/**
	 * Return the weight for the given feature.
	 * 
	 * @param attribute
	 *            any {@link EStructuralFeature}.
	 * @return the weight for the given feature. 0 meaning no effects.
	 */
	int getWeight(EStructuralFeature attribute);

	/**
	 * Return the weight associated with the fact some Object has changed it's container.
	 * 
	 * @param a
	 *            any instance.
	 * @return a weight representing the importance of the change of container to compute matches.
	 */
	int getParentWeight(EObject a);

	/**
	 * Return the weight associated with the fact some Object has changed it's containing reference.
	 * 
	 * @param a
	 *            any instance.
	 * @return a weight representing the importance of the change of containing reference to compute matches.
	 */
	int getContainingFeatureWeight(EObject a);

	/**
	 * Wrapper describing the given weight provider.
	 * 
	 * @since 3.1.0
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	public interface Descriptor {

		/**
		 * Returns the wrapped weight provider.
		 * 
		 * @return the wrapped weight provider
		 */
		WeightProvider getWeightProvider();

		/**
		 * Returns the ranking of this weight provider.
		 * 
		 * @return The ranking.
		 */
		int getRanking();

		/**
		 * Returns the pattern of namespace URI on which this weight provider can be applied.
		 * 
		 * @return The namespace URI pattern.
		 */
		Pattern getNsURI();

		/**
		 * Registry of weight provider descriptors.
		 * 
		 * @since 3.1.0
		 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
		 */
		public interface Registry {
			/**
			 * Adds a weight provider to the registry.
			 * 
			 * @param key
			 *            key with which the specified descriptor is to be associated
			 * @param descriptor
			 *            weight provider that is to be added to this registry.
			 * @return the previous descriptor associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map
			 *         previously associated <tt>null</tt> with <tt>key</tt>, if the implementation supports
			 *         <tt>null</tt> values.)
			 */
			WeightProvider.Descriptor put(String key, WeightProvider.Descriptor descriptor);

			/**
			 * Removes all extensions from this registry.
			 * 
			 * @noreference This method is not intended to be referenced by clients.
			 */
			void clear();

			/**
			 * This will return a copy of the registered weight providers list.
			 * 
			 * @return A copy of the registered weight providers list.
			 */
			Collection<WeightProvider.Descriptor> getDescriptors();

			/**
			 * Removes a weight provider from this registry.
			 * 
			 * @param key
			 *            key of the weight provider descriptor that is to be removed from the registry.
			 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>.
			 */
			WeightProvider.Descriptor remove(String key);

			/**
			 * Retrieve the highest ranking weight provider from a given <code>ePackage</code>.
			 * 
			 * @param ePackage
			 *            The given ePackage.
			 * @return The associated weight provider with the highest ranking.
			 */
			WeightProvider getHighestRankingWeightProvider(EPackage ePackage);

			/**
			 * Retrieve the weight providers from a given <code>ePackage</code>.
			 * 
			 * @param ePackage
			 *            The given ePackage.
			 * @return The associated weight providers if any.
			 */
			Collection<WeightProvider> getWeightProviders(EPackage ePackage);
		}
	}
}
