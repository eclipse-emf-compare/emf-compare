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

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Manages renamings of papyrus resources for three-way comparisons.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.4
 */
public class MergeRenamingMatchResources3Ways extends AbstractMergeRenamingMatchResources {

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, must not be {@code null} and must be a 3-way comparison, otherwise a runtime
	 *            exception is thrown.
	 * @param monitor
	 *            The monitor, must no be {@code null}.
	 */
	public MergeRenamingMatchResources3Ways(Comparison comparison, Monitor monitor) {
		super(comparison, monitor);
	}

	@Override
	protected Resource getBaseResource(MatchResource mr) {
		return mr.getOrigin();
	}

	@Override
	protected DiffSide getBaseSide() {
		return DiffSide.ORIGIN;
	}

	@Override
	protected void checkComparison() {
		Preconditions.checkState(comparison.isThreeWay());
	}

	@Override
	protected void indexPapyrusMatchResource(MatchResource matchResource, URI baseURI) {
		Resource left = matchResource.getLeft();
		if (left != null && handles(left.getURI()) && !left.getURI().equals(baseURI)) {
			// This MatchResource is a renaming
			index.put(left.getURI().trimFileExtension(), matchResource);
			if (baseURI != null) {
				renamingMatches.put(baseURI.trimFileExtension(), matchResource);
			}
		}
		super.indexPapyrusMatchResource(matchResource, baseURI);
	}

	@Override
	protected void handleRenamingMatchResource(MatchResource matchResource) {
		URI originURI = getBaseResource(matchResource).getURI();
		Resource left = matchResource.getLeft();
		if (left != null && left.getURI() != null && !originURI.equals(left.getURI())) {
			// Renamed on the left
			URI leftTrimmedURI = left.getURI().trimFileExtension();
			for (MatchResource similarMatchResource : findSimilarMatchResources(matchResource)) {
				// We retrieve the file extension of the similar match resource's base resource
				String fileExtension = getBaseResource(similarMatchResource).getURI().fileExtension();
				// And we look for a different MachResource that has the same file extension on the right
				// and the same name as the right resource of the original matchResource
				URI candidateURI = leftTrimmedURI.appendFileExtension(fileExtension);
				for (MatchResource renamedMatchResource : findMatchResources(similarMatchResource,
						candidateURI, true)) {
					// We found the renamed MatchResource
					similarMatchResource.setLeft(renamedMatchResource.getLeft());
					similarMatchResource.setLeftURI(renamedMatchResource.getLeftURI());
					comparison.getMatchedResources().remove(renamedMatchResource);
					break;
				}
			}
		}
		super.handleRenamingMatchResource(matchResource);
	}
}
