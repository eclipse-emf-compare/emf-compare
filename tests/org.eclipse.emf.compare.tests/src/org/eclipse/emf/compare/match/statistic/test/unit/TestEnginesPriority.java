package org.eclipse.emf.compare.match.statistic.test.unit;

import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.match.statistic.test.util.EMFCompareTestCase;

public class TestEnginesPriority extends EMFCompareTestCase {

	public void testEngineFileExtension() throws Exception {
		MatchEngine engine = null;
		engine = MatchService.getInstance().getBestMatchEngine("a");
		assertEquals("AEngine", engine.getClass().getSimpleName());
		engine = MatchService.getInstance().getBestMatchEngine("b");
		assertEquals("BEngine", engine.getClass().getSimpleName());

	}

	public void testEnginePriority() throws Exception {
		MatchEngine engine = null;
		engine = MatchService.getInstance().getBestMatchEngine("c");
		assertEquals("CHighEngine", engine.getClass().getSimpleName());

	}

}
