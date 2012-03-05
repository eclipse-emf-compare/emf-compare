/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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

import org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.junit.Test;

public class TestProfile extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/profiles/"; //$NON-NLS-1$

	@Test
	public void applyProfile() throws IOException, InterruptedException {
		testCompare("applyProfile"); //$NON-NLS-1$
	}

	@Test
	public void applyProfile_merge() throws IOException, InterruptedException {
		testMerge("applyProfile"); //$NON-NLS-1$
	}

	@Test
	public void applyStereotype_addition() throws IOException, InterruptedException {
		testCompare("applyStereotype/addition"); //$NON-NLS-1$
	}

	@Test
	public void applyStereotype_removal() throws IOException, InterruptedException {
		testCompare("applyStereotype/removal"); //$NON-NLS-1$
	}

	@Test
	public void applyStereotype_addition_merge() throws IOException, InterruptedException {
		testMerge("applyStereotype/addition"); //$NON-NLS-1$
	}

	@Test
	public void applyStereotype_removal_merge() throws IOException, InterruptedException {
		testMerge("applyStereotype/removal"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_update() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/update"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_3way_localChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/localChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_3way_remoteChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/remoteChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_3way_conflict() throws IOException, InterruptedException {
		testCompare3Way("stereotypeAttribute/update/threeway/conflict"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_changeLeft() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_changeRight() throws IOException, InterruptedException {
		testCompare("stereotypeAttribute/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_update_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/update"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_changeLeft_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeAttribute_changeRight_merge() throws IOException, InterruptedException {
		testMerge("stereotypeAttribute/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_changeLeft() throws IOException, InterruptedException {
		testCompare("stereotypeReference/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_changeRight() throws IOException, InterruptedException {
		testCompare("stereotypeReference/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_orderChange() throws IOException, InterruptedException {
		testCompare("stereotypeReference/orderChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_changeLeft_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_changeRight_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_orderChange_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/orderChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference0() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_0"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference1() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_1"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference2() throws IOException, InterruptedException {
		testCompare("stereotypeReference/updateReference/_2"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_3way_localChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/localChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_3way_remoteChange() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/remoteChange"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_3way_conflict() throws IOException, InterruptedException {
		testCompare3Way("stereotypeReference/updateReference/threeway/conflict"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference0_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_0"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference1_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_1"); //$NON-NLS-1$
	}

	@Test
	public void stereotypeReference_updateReference2_merge() throws IOException, InterruptedException {
		testMerge("stereotypeReference/updateReference/_2"); //$NON-NLS-1$
	}

	@Test
	public void applyProfile_remove() throws IOException, InterruptedException {
		testCompare("applyProfile/remove"); //$NON-NLS-1$
	}

	@Test
	public void applyProfile_remove_merge() throws IOException, InterruptedException {
		testMerge("applyProfile/remove", true, UMLProfileApplicationRemoval.class, 0); //$NON-NLS-1$
		testMerge("applyProfile/remove", false, UMLProfileApplicationRemoval.class, 1); //$NON-NLS-1$
	}

	@Test
	public void applyProfile_addition() throws IOException, InterruptedException {
		testCompare("applyProfile/addition"); //$NON-NLS-1$
	}

	@Test
	public void applyProfile_addition_merge() throws IOException, InterruptedException {
		testMerge("applyProfile/addition", true, UMLProfileApplicationAddition.class, 1); //$NON-NLS-1$
		testMerge("applyProfile/addition", false, UMLProfileApplicationAddition.class, 0); //$NON-NLS-1$
	}

	@Test
	public void applyProfileStereotype_addition() throws IOException, InterruptedException {
		testCompare("applyProfileStereotype/addition"); //$NON-NLS-1$
	}

	@Test
	public void applyProfileStereotype_addition_merge() throws IOException, InterruptedException {
		testMerge("applyProfileStereotype/addition", true, UMLStereotypeApplicationAddition.class, 0); //$NON-NLS-1$
		testMerge("applyProfileStereotype/addition", false, UMLStereotypeApplicationAddition.class, 1); //$NON-NLS-1$
	}

	@Test
	public void applyProfileStereotype_removal() throws IOException, InterruptedException {
		testCompare("applyProfileStereotype/removal"); //$NON-NLS-1$
	}

	@Test
	public void applyProfileStereotype_removal_merge() throws IOException, InterruptedException {
		testMerge("applyProfileStereotype/removal", true, UMLStereotypeApplicationRemoval.class, 1); //$NON-NLS-1$
		testMerge("applyProfileStereotype/removal", false, UMLStereotypeApplicationRemoval.class, 0); //$NON-NLS-1$
	}

	// BEGIN - Tests for Bug 351593

	@Test
	public void addModelWithEmbeddedStereotype() throws IOException, InterruptedException {
		testCompare("changeModelElement/addition/embeddedStereotype"); //$NON-NLS-1$
	}

	@Test
	public void addModelWithEmbeddedStereotype_merge() throws IOException, InterruptedException {
		testMerge("changeModelElement/addition/embeddedStereotype", true, UMLElementChangeLeftTarget.class, 0); //$NON-NLS-1$
		testMerge(
				"changeModelElement/addition/embeddedStereotype", false, UMLElementChangeLeftTarget.class, 0); //$NON-NLS-1$
	}

	@Test
	public void removeModelWithEmbeddedStereotype() throws IOException, InterruptedException {
		testCompare("changeModelElement/remove/embeddedStereotype"); //$NON-NLS-1$
	}

	@Test
	public void removeModelWithEmbeddedStereotype_merge() throws IOException, InterruptedException {
		testMerge("changeModelElement/remove/embeddedStereotype", true, UMLElementChangeRightTarget.class, 0); //$NON-NLS-1$
		testMerge("changeModelElement/remove/embeddedStereotype", false, UMLElementChangeRightTarget.class, 0); //$NON-NLS-1$
	}

	// END - Tests for Bug 351593
	
	// BEGIN - Tests for Bug 361514
	
	@Test
	public void addModelWithStereotype() throws IOException, InterruptedException {
		testCompare("changeModelElement/addition/stereotyped"); //$NON-NLS-1$
	}

	@Test
	public void addModelWithStereotype_merge() throws IOException, InterruptedException {
		testMerge("changeModelElement/addition/stereotyped", true, UMLElementChangeLeftTarget.class, 0); //$NON-NLS-1$
		testMerge("changeModelElement/addition/stereotyped", false, UMLElementChangeLeftTarget.class, 0); //$NON-NLS-1$
	}
	
	@Test
	public void removeModelWithStereotype() throws IOException, InterruptedException {
		testCompare("changeModelElement/remove/stereotyped"); //$NON-NLS-1$
	}

	@Test
	public void removeModelWithStereotype_merge() throws IOException, InterruptedException {
		testMerge("changeModelElement/remove/stereotyped", true, UMLElementChangeRightTarget.class, 0); //$NON-NLS-1$
		testMerge("changeModelElement/remove/stereotyped", false, UMLElementChangeRightTarget.class, 0); //$NON-NLS-1$
	}
	
	// END - Tests for Bug 361514

	@Override
	protected String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}
}
