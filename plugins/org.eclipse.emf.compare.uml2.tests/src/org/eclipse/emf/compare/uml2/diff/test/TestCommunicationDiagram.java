package org.eclipse.emf.compare.uml2.diff.test;

import java.io.IOException;

import org.junit.Test;

public class TestCommunicationDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/communication/";

	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}
	
	@Test
	public void actionExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("message/changeLeftTarget");
	}
	
	@Test
	public void actionExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("message/changeRightTarget");
	}
	
	@Test
	public void actionExecutionSpecification_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("message/changeLeftTarget");
	}
	
	@Test
	public void actionExecutionSpecification_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("message/changeRightTarget");
	}
}
