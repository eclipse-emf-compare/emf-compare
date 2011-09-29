/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.test;

import java.io.IOException;

import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.junit.Test;

public class TestProfile extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/profiles/";

	@Test
	public void applyProfile() throws IOException, InterruptedException {
		testCompare("applyProfile");
	}

	@Test
	public void applyProfile_merge() throws IOException, InterruptedException {
		testMerge("applyProfile");
	}

	@Test
	public void applyStereotype_addition() throws IOException, InterruptedException {
		testCompare("applyStereotype/addition");
	}

	@Test
	public void applyStereotype_removal() throws IOException, InterruptedException {
		testCompare("applyStereotype/removal");
	}

	@Test
	public void applyStereotype_addition_merge() throws IOException, InterruptedException {
		testMerge("applyStereotype/addition");
	}

	@Test
	public void applyStereotype_removal_merge() throws IOException, InterruptedException {
		testMerge("applyStereotype/removal");
	}

	@Test
	public void stereotypeAttribute_update() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/update");
	}
	
	@Test
	public void stereotypeAttribute_3way_localChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/localChange");
	}
	
	@Test
	public void stereotypeAttribute_3way_remoteChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/remoteChange");
	}
	
	@Test
	public void stereotypeAttribute_3way_conflict() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/conflict");
	}

	@Test
	public void stereotypeAttribute_changeLeft() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/changeLeftTarget");
	}

	@Test
	public void stereotypeAttribute_changeRight() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/changeRightTarget");
	}

	@Test
	public void stereotypeAttribute_update_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/update");
	}

	@Test
	public void stereotypeAttribute_changeLeft_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/changeLeftTarget");
	}

	@Test
	public void stereotypeAttribute_changeRight_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/changeRightTarget");
	}

	@Test
	public void stereotypeReference_changeLeft() throws IOException, InterruptedException {
		testCompare("stereotypeReference/changeLeftTarget");
	}

	@Test
	public void stereotypeReference_changeRight() throws IOException, InterruptedException {
		testCompare("stereotypeReference/changeRightTarget");
	}

	@Test
	public void stereotypeReference_orderChange() throws IOException, InterruptedException {
		testCompare("stereotypeReference/orderChange");
	}

	@Test
	public void stereotypeReference_changeLeft_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/changeLeftTarget");
	}

	@Test
	public void stereotypeReference_changeRight_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/changeRightTarget");
	}

	@Test
	public void stereotypeReference_orderChange_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/orderChange");
	}

	@Test
	public void stereotypeReference_updateReference0() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_0");
	}

	@Test
	public void stereotypeReference_updateReference1() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_1");
	}

	@Test
	public void stereotypeReference_updateReference2() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_2");
	}
	
	@Test
	public void stereotypeReference_3way_localChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/localChange");
	}
	
	@Test
	public void stereotypeReference_3way_remoteChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/remoteChange");
	}
	
	@Test
	public void stereotypeReference_3way_conflict() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/conflict");
	}

	@Test
	public void stereotypeReference_updateReference0_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_0");
	}

	@Test
	public void stereotypeReference_updateReference1_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_1");
	}

	@Test
	public void stereotypeReference_updateReference2_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_2");
	}
	
	@Test
	public void applyProfile_remove() throws IOException, InterruptedException {
		testCompare("applyProfile/remove");
	}
	
	@Test
	public void applyProfile_remove_merge() throws IOException, InterruptedException {		
		testMerge("applyProfile/remove", true, UMLProfileApplicationRemoval.class, 0);
		testMerge("applyProfile/remove", false, UMLProfileApplicationRemoval.class, 1);
	}
	
	@Test
	public void applyProfile_addition() throws IOException, InterruptedException {
		testCompare("applyProfile/addition");
	}
	
	@Test
	public void applyProfile_addition_merge() throws IOException, InterruptedException {		
		testMerge("applyProfile/addition", true, UMLProfileApplicationAddition.class, 1);
		testMerge("applyProfile/addition", false, UMLProfileApplicationAddition.class, 0);
	}
	
	@Test
	public void applyProfileStereotype_addition() throws IOException, InterruptedException {
		testCompare("applyProfileStereotype/addition");
	}

	@Test
	public void applyProfileStereotype_addition_merge() throws IOException, InterruptedException {		
		testMerge("applyProfileStereotype/addition", true, UMLStereotypeApplicationAddition.class, 0);
		testMerge("applyProfileStereotype/addition", false, UMLStereotypeApplicationAddition.class, 1);
	}
	
	@Test
	public void applyProfileStereotype_removal() throws IOException, InterruptedException {
		testCompare("applyProfileStereotype/removal");
	}

	@Test
	public void applyProfileStereotype_removal_merge() throws IOException, InterruptedException {		
		testMerge("applyProfileStereotype/removal", true, UMLStereotypeApplicationRemoval.class, 1);
		testMerge("applyProfileStereotype/removal", false, UMLStereotypeApplicationRemoval.class, 0);
	}
	
	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}
}
