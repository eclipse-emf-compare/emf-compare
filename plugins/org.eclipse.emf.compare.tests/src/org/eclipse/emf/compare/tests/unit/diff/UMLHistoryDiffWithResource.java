package org.eclipse.emf.compare.tests.unit.diff;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class UMLHistoryDiffWithResource extends UMLHistoryDiff {

	@Override
	protected void doTest(EObject ancestor, EObject left) throws IOException, InterruptedException {
		Resource leftR = left.eResource();
		Resource ancestorR = ancestor.eResource();

		Resource rightR = new XMIResourceImpl(leftR.getURI());
		rightR.getContents().add(EcoreUtil.copy(left));

		MatchModel match = MatchService.doResourceMatch(leftR, rightR, ancestorR, Collections.EMPTY_MAP);
		DiffModel diff = DiffService.doDiff(match, true);

		List<DiffElement> deltas = diff.getDifferences();
		for (DiffElement delta : deltas) {
			assertFalse(
					"There should be no conflict as left and right did the same change :" + delta.toString(),
					delta.isConflicting());
		}
	}

}
