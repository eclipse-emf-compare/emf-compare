/*******************************************************************************
 * Copyright (c) 2018 Obeo.
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

import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * Compute specific matching rules. This is used by the EqualityHelper.
 * 
 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
 */
public interface EqualityHelperExtensionProvider {

	/**
	 * Enumeration used to return the result of a specific matching.
	 * 
	 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
	 */
	enum SpecificMatch {
		/** This means that this specific extension provider doesn't know how to handle the given objects. */
		UNKNOWN,

		/** If these objects have been determined to be a match by this extension. */
		MATCH,

		/** If these objects have been determined to <i>not</i> match by this extension. */
		UNMATCH
	}

	/**
	 * Test whether two objects match.
	 * 
	 * @param object1
	 *            First of the two compared objects.
	 * @param object2
	 *            Second of the two compared objects.
	 * @param equalityHelper
	 *            Calling equality helper
	 * @return MATCH if the objects match, UNMATCH if they do not match, UNKNOWN if the provider does not know
	 *         how to handle these objects
	 */
	SpecificMatch matchingEObjects(EObject object1, EObject object2, IEqualityHelper equalityHelper);

	/**
	 * Wrapper describing the given equality helper extension provider.
	 * 
	 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
	 */
	public interface Descriptor {

		/**
		 * Returns the wrapped equality helper extension provider.
		 * 
		 * @return the wrapped equality helper extension provider
		 */
		EqualityHelperExtensionProvider getEqualityHelperExtensionProvider();

		/**
		 * Returns the ranking of this equality helper extension provider.
		 * 
		 * @return The ranking.
		 */
		int getRanking();

		/**
		 * Returns the pattern of namespace URI on which this equality helper extension provider can be
		 * applied.
		 * 
		 * @return The namespace URI pattern.
		 */
		Pattern getNsURI();

		/**
		 * Registry of equality helper extension provider descriptors.
		 * 
		 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
		 */
		public interface Registry {
			/**
			 * Adds a equality helper extension provider to the registry.
			 * 
			 * @param key
			 *            key with which the specified descriptor is to be associated
			 * @param descriptor
			 *            equality helper extension provider that is to be added to this registry.
			 * @return the previous descriptor associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map
			 *         previously associated <tt>null</tt> with <tt>key</tt>, if the implementation supports
			 *         <tt>null</tt> values.)
			 */
			EqualityHelperExtensionProvider.Descriptor put(String key,
					EqualityHelperExtensionProvider.Descriptor descriptor);

			/**
			 * Removes all extensions from this registry.
			 * 
			 * @noreference This method is not intended to be referenced by clients.
			 */
			void clear();

			/**
			 * This will return a copy of the registered equality helper extension providers list.
			 * 
			 * @return A copy of the registered equality helper extension providers list.
			 */
			Collection<EqualityHelperExtensionProvider.Descriptor> getDescriptors();

			/**
			 * Removes a equality helper extension provider from this registry.
			 * 
			 * @param key
			 *            key of the equality helper extension provider descriptor that is to be removed from
			 *            the registry.
			 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no
			 *         mapping for <tt>key</tt>.
			 */
			EqualityHelperExtensionProvider.Descriptor remove(String key);

			/**
			 * Retrieve the highest ranking equality helper extension provider from a given
			 * <code>ePackage</code>.
			 * 
			 * @param ePackage
			 *            The given ePackage.
			 * @return The associated equality helper extension provider with the highest ranking.
			 */
			EqualityHelperExtensionProvider getHighestRankingEqualityHelperExtensionProvider(
					EPackage ePackage);

			/**
			 * Retrieve the equality helper extension providers from a given <code>ePackage</code>.
			 * 
			 * @param ePackage
			 *            The given ePackage.
			 * @return The associated equality helper extension providers if any.
			 */
			Collection<EqualityHelperExtensionProvider> getEqualityHelperExtensionProviders(
					EPackage ePackage);
		}
	}
}
