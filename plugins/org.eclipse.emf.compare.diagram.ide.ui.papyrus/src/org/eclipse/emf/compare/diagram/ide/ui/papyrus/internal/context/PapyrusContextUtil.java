/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *     Philip Langer - check for DI files in IComparisonScope2.getAllInvolvedResourceURIs
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.context;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.any;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.IComparisonScope2;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.core.resource.sasheditor.DiModel;

/**
 * Helper class for determining the context of a comparison.
 * 
 * @author Stefan Dirix
 */
public final class PapyrusContextUtil {

	/** Predicate specifying whether the given URI string ends with the Papyrus DI file extension. */
	private static final Predicate<String> ENDS_WITH_PAPYRUS_EXTENSION = new Predicate<String>() {
		public boolean apply(String input) {
			if (input != null) {
				return input.endsWith(DiModel.DI_FILE_EXTENSION);
			}
			return false;
		}
	};

	/** Transforms an URI into the platform string representation. */
	private static final Function<URI, String> URI_TO_STRING = new Function<URI, String>() {
		public String apply(URI input) {
			String uriString = null;
			if (input != null) {
				uriString = input.toPlatformString(true);
				if (uriString == null) {
					uriString = input.toString();
				}
			}
			return uriString;
		}
	};

	/**
	 * Hidden constructor for this utility class.
	 */
	private PapyrusContextUtil() {
		// defeat instantiation
	}

	/**
	 * Determines whether the comparison concerns Papyrus models.
	 * 
	 * @param comparison
	 *            the {@link Comparison} to check.
	 * @return {@code true} if the comparison concerns Papyrus models, {@code false} otherwise.
	 */
	public static boolean isPapyrusContext(Comparison comparison) {
		final IComparisonScope2 comparisonScope = (IComparisonScope2)EcoreUtil.getAdapter(comparison
				.eAdapters(), IComparisonScope2.class);
		if (comparisonScope != null) {
			return containsPapyrusURI(transform(comparisonScope.getAllInvolvedResourceURIs(), URI_TO_STRING));
		}
		// Fallback if the scope is not available. This way of determining the Papyrus context is not as
		// accurate since the MatchResources are already minimized.
		return containsPapyrusURI(comparison.getMatchedResources());
	}

	/**
	 * Specifies whether the given matched resources contain a Papyrus resource.
	 * 
	 * @param matchedResources
	 *            the list of matched resources to check.
	 * @return <code>true</code> if the matched resources contain a Papyrus resources, <code>false</code>
	 *         otherwise.
	 */
	private static boolean containsPapyrusURI(List<MatchResource> matchedResources) {
		final Builder<String> setBuilder = ImmutableSet.builder();
		for (MatchResource res : matchedResources) {
			for (String uri : ImmutableSet.of(res.getLeftURI(), res.getRightURI(), res.getOriginURI())) {
				if (uri != null) {
					setBuilder.add(uri);
				}
			}
		}
		return containsPapyrusURI(setBuilder.build());
	}

	/**
	 * Specifies whether the given URI strings contain a Papyrus resource.
	 * 
	 * @param uris
	 *            the set of uris to check.
	 * @return <code>true</code> if uris contain a Papyrus resources, <code>false</code> otherwise.
	 */
	private static boolean containsPapyrusURI(Collection<String> uris) {
		return any(uris, ENDS_WITH_PAPYRUS_EXTENSION);
	}
}
