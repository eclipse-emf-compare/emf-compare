package org.eclipse.emf.compare.match.statistic.test.util;

import java.io.File;

public class TestFiles extends EMFCompareTestCase {

	private static final File DATA_DIR = new File(TestUtils
			.getPluginDirectory()
			+ "/data");

	private static final File EXPECTED_DIR = new File(TestUtils
			.getPluginDirectory()
			+ "/data/result/");

	public void testDummy() throws Exception {
		assertTrue(true);
	}

	public void testDirs() throws Exception {
		assertTrue(EXPECTED_DIR.getAbsolutePath() + " doesn't exist",
				EXPECTED_DIR.isDirectory());
		assertTrue(DATA_DIR.getAbsolutePath() + " doesn't exist", EXPECTED_DIR
				.isDirectory());
	}

	public void testFileContents() throws Exception {
		assertFileContentsEqual(pluginFile("/data/data.txt"),
				pluginFile("/data/result/data.txt"));
	}

	public void testDirCompare() throws Exception {
		compareDirs(pluginFile("/data/diff"),
				pluginFile("/data/diff-expected"), "");
	}

}
