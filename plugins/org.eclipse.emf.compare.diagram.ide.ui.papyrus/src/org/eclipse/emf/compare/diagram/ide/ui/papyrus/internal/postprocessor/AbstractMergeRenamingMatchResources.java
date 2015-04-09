/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MatchResourcePredicates.hasSameTrimmedURI;
import static org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.MatchResourcePredicates.hasSameURI;
import static org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor.PapyrusPostProcessor.FILE_EXTENSIONS;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Map;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.CompareUIPapyrusMessages;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Treatment that merges {@link MatchResource} instances that represent implicit renames managed by Papyrus.
 * This allows to handle the renaming of the di, notation, and sash files when the associated uml file has
 * been renamed and nothing in the content of the di, notation, or sash files allows EMFCompare to match them
 * (which happens systematically when they are empty for example).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.4
 */
public abstract class AbstractMergeRenamingMatchResources {

	/** The comparison. */
	protected final Comparison comparison;

	/** The monitor. */
	protected final Monitor monitor;

	/** The index used to easily retrieve {@link MatchResource} instances. */
	protected final Multimap<URI, MatchResource> index = LinkedHashMultimap.create();

	/** The known renaming matches. */
	protected final Map<URI, MatchResource> renamingMatches = Maps.newLinkedHashMap();

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, must no be {@code null}.
	 * @param monitor
	 *            The monitor, must no be {@code null}.
	 */
	public AbstractMergeRenamingMatchResources(Comparison comparison, Monitor monitor) {
		this.comparison = Preconditions.checkNotNull(comparison);
		checkComparison();
		this.monitor = checkNotNull(monitor);
	}

	/**
	 * Executes this treatment.
	 */
	public final void run() {
		monitor.subTask(CompareUIPapyrusMessages.getString("MergeRenamingResources.TaskLabel")); //$NON-NLS-1$
		checkComparison();
		indexMatchResources();
		for (MatchResource matchResource : renamingMatches.values()) {
			handleRenamingMatchResource(matchResource);
		}
	}

	/**
	 * Checks that the comparison owned by this treatment is valid for this treatment. When called, the owned
	 * comparison cannot be {@code null}. This should send a IllegalStateException if the comparison is not
	 * like expected.
	 */
	protected abstract void checkComparison();

	/**
	 * Provides the "base" resource of the given {@link MatchResource}, "base" begin origin for 3-way
	 * comparisons and left for 2-way comparisons.
	 * 
	 * @param mr
	 *            The {@link MatchResource} for which we want the base resource.
	 * @return The given MatchResource origin resource if the given {@link MatchResource} is part of a 3-way
	 *         comparison, its left resource otherwise.
	 */
	protected abstract Resource getBaseResource(MatchResource mr);

	protected DiffSide getBaseSide() {
		return DiffSide.SOURCE;
	}

	/**
	 * Indexes the MatchResources to facilitate the retrieval of equivalent renames.
	 */
	protected void indexMatchResources() {
		for (MatchResource matchResource : comparison.getMatchedResources()) {
			Resource originResource = getBaseResource(matchResource);
			if (originResource == null) {
				// File didn't exist in ancestor, can have been renamed left or right
				indexPapyrusMatchResource(matchResource, null);
			} else {
				URI originURI = originResource.getURI();
				if (handles(originURI)) {
					index.put(originURI.trimFileExtension(), matchResource);
					indexPapyrusMatchResource(matchResource, originURI);
				}
			}
		}
	}

	/**
	 * Indicates whether a given URI is handled by this treatment.
	 * 
	 * @param uri
	 *            URI to check
	 * @return {@code true} if and only if the given URI ends with ".uml", ".notation" or ".di".
	 */
	protected boolean handles(URI uri) {
		return uri != null && FILE_EXTENSIONS.contains(uri.fileExtension());
	}

	/**
	 * Indexes one MatchResource that could be a papyrus-related MatchResource (i.e. *.uml, *.di or
	 * *.notation).
	 * 
	 * @param matchResource
	 *            The MatchResource to index.
	 * @param baseURI
	 *            The URI of the base resource, which can be null.
	 */
	protected void indexPapyrusMatchResource(MatchResource matchResource, URI baseURI) {
		Resource right = matchResource.getRight();
		if (right != null && handles(right.getURI()) && !right.getURI().equals(baseURI)) {
			index.put(right.getURI().trimFileExtension(), matchResource);
			if (baseURI != null) {
				renamingMatches.put(baseURI.trimFileExtension(), matchResource);
			}
		}
	}

	/**
	 * Handle one MatchResource that is known to be a renaming of a Papyrus resource.
	 * 
	 * @param matchResource
	 *            Match resource that is currently known to represent a rename, from which we want to derive
	 *            associated renames (if a.uml has been renamed to b.uml, then a.di should also have been
	 *            renamed to b.di)
	 */
	protected void handleRenamingMatchResource(MatchResource matchResource) {
		URI originURI = getBaseResource(matchResource).getURI();
		Resource right = matchResource.getRight();
		if (right != null && right.getURI() != null && !originURI.equals(right.getURI())) {
			// Renamed on the right
			// Let's go over MatchResource that have the same origin URI (except file extension)
			// These will be the MatchResource that we keep and to which we add the relevant information
			// for sides where there have been renames
			URI rightTrimmedURI = right.getURI().trimFileExtension();
			for (MatchResource similarMatchResource : findSimilarMatchResources(matchResource)) {
				// We retrieve the file extension of the similar match resource's base resource
				String fileExtension = getBaseResource(similarMatchResource).getURI().fileExtension();
				// And we look for a different MachResource that has the same file extension on the right
				// and the same name as the right resource of the original matchResource
				URI candidateURI = rightTrimmedURI.appendFileExtension(fileExtension);
				for (MatchResource renamedMatchResource : findMatchResources(similarMatchResource,
						candidateURI, false)) {
					// We found the renamed MatchResource
					similarMatchResource.setRight(renamedMatchResource.getRight());
					similarMatchResource.setRightURI(renamedMatchResource.getRightURI());
					comparison.getMatchedResources().remove(renamedMatchResource);
					break;
				}
			}
		}
	}

	/**
	 * Finds {@link MatchResource}s that are potentially to be renamed like he given Matchresource. Those will
	 * be that have the same URI on the origin side, except the file extension.
	 * 
	 * @param matchResource
	 *            The MatchResource
	 * @return An iterable over the {@link MatchResource}s that have the same URI on the base side, excluding
	 *         the given matchresource.
	 */
	protected Iterable<MatchResource> findSimilarMatchResources(MatchResource matchResource) {
		URI originURI = getBaseResource(matchResource).getURI();
		URI trimmedURI = originURI.trimFileExtension();
		return filter(index.get(trimmedURI), and(not(equalTo(matchResource)), hasSameTrimmedURI(
				matchResource, getBaseSide())));
	}

	/**
	 * Finds the {@link MatchResource}s that have the given URI on the given side, excluding the given
	 * candidate.
	 * 
	 * @param candidate
	 *            Candidate to exclude
	 * @param expectedURI
	 *            Researched URI
	 * @param left
	 *            true for left side, false for right
	 * @return An iterable over the candidates, never null, possibly empty, and that should contain one value
	 *         at most.
	 */
	protected Iterable<MatchResource> findMatchResources(MatchResource candidate, URI expectedURI,
			boolean left) {
		if (left) {
			return filter(index.get(expectedURI.trimFileExtension()), and(not(equalTo(candidate)),
					hasSameURI(expectedURI, DiffSide.SOURCE)));
		}
		return filter(index.get(expectedURI.trimFileExtension()), and(not(equalTo(candidate)), hasSameURI(
				expectedURI, DiffSide.REMOTE)));
	}
}
