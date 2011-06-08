package org.eclipse.emf.compare.tests.merge;

import org.eclipse.emf.compare.tests.merge.data.EcoreMergeInputData;

public class UMLHistoryMerge extends MergeTestBase {

	EcoreMergeInputData input = new EcoreMergeInputData();

	public void test171to172() throws Exception {
		mergeAllDiffsLeftToRight(input.getUML171(), input.getUML172());
	}

	public void test172to171() throws Exception {
		mergeAllDiffsRightToLeft(input.getUML171(), input.getUML172());
	}

	public void test172to173() throws Exception {
		mergeAllDiffsLeftToRight(input.getUML172(), input.getUML173());
	}

	public void test173to172() throws Exception {
		mergeAllDiffsRightToLeft(input.getUML172(), input.getUML173());
	}

}
