/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.tests;

import java.io.IOException;

import org.junit.Test;

/**
 * All test linked to SysML profile.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 */
public class TestSysMLProfile extends AbstractSysMLCompareTest {

	/** DIAGRAM_KIND_PATH. */
	private static final String DIAGRAM_KIND_PATH = "/profile/sysml/";

	@Override
	protected String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

	/**
	 * Test of addition of an attribute.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypePropertyChangeLeftTarget0() throws IOException, InterruptedException {
		testCompare("property/attribute/changeLeftTarget"); //$NON-NLS-1$
	}

	/**
	 * Test of removal of an attribute.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypePropertyChangeRigthTarget0() throws IOException, InterruptedException {
		testCompare("property/attribute/changeRightTarget"); //$NON-NLS-1$
	}

	/**
	 * Test of update of an attribute.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeAttributeUpdate() throws IOException, InterruptedException {
		testCompare("property/attribute/update"); //$NON-NLS-1$
	}

	/**
	 * Test of update of a reference.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeUpdateReference() throws IOException, InterruptedException {
		testCompare("property/reference/update"); //$NON-NLS-1$
	}

	/**
	 * Test of update of a reference to reference null.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeUpdateReference01() throws IOException, InterruptedException {
		testCompare("property/reference/update01"); //$NON-NLS-1$
	}

	/**
	 * Test of addition of a reference.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeReferenceChangeLeftTarget() throws IOException, InterruptedException {
		testCompare("property/reference/changeLeftTarget"); //$NON-NLS-1$
	}

	/**
	 * Test removal of a reference.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeReferenceChangeRightTarget() throws IOException, InterruptedException {
		testCompare("property/reference/changeRightTarget"); //$NON-NLS-1$
	}

	/**
	 * Test reference change order.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void sysMLStereotypeReferenceChangeOrder() throws IOException, InterruptedException {
		testCompare("property/reference/changeOrder"); //$NON-NLS-1$
	}
}
