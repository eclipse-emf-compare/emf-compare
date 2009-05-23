/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl.wizard;

import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.diff.DiffEpatchService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.jface.wizard.Wizard;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EportEpatchWizard extends Wizard {

	private ComparisonSnapshot snapshot;

	public EportEpatchWizard(ComparisonSnapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}

	private SaveEpatchWizardPage savePage = new SaveEpatchWizardPage();

	@Override
	public void addPages() {
		addPage(savePage);
	}

	@Override
	public boolean performFinish() {
		try {
			if (snapshot instanceof ComparisonResourceSnapshot) {
				ComparisonResourceSnapshot crs = (ComparisonResourceSnapshot)snapshot;
				Epatch epatch = DiffEpatchService.createEpatch(crs.getMatch(), crs.getDiff(), savePage
						.getSuggestedPatchName());
				savePage.save(epatch.eResource());
				return true;
			} else if (snapshot instanceof ComparisonResourceSetSnapshot) {
				ComparisonResourceSetSnapshot crss = (ComparisonResourceSetSnapshot)snapshot;
				if (crss.getMatchResourceSet().getMatchModels().size() == 1
						&& crss.getDiffResourceSet().getDiffModels().size() == 1) {
					MatchModel mm = crss.getMatchResourceSet().getMatchModels().get(0);
					DiffModel dm = crss.getDiffResourceSet().getDiffModels().get(0);
					Epatch epatch = DiffEpatchService.createEpatch(mm, dm, savePage.getSuggestedPatchName());
					savePage.save(epatch.eResource());
					return true;
				} else
					throw new RuntimeException(
							"No support for ComparisonResourceSetSnapshots with more/less than one matchmodel and diffmodel");
			} else
				throw new RuntimeException("No support for " + snapshot.getClass().getSimpleName() + " yet");
		} catch (Exception e) {
			e.printStackTrace(); // TODO: output error message
			return false;
		}
	}
}
