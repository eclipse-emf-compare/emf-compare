/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;

public abstract class AbstractTestUITreeNodeItemProviderAdapter extends AbstractTestTreeNodeItemProviderAdapter {

	protected ICompareEditingDomain editingDomain;

	protected IEMFCompareConfiguration createConfiguration(boolean leftEditable, boolean rightEditable) {
		CompareConfiguration cc = new CompareConfiguration();
		cc.setLeftEditable(leftEditable);
		cc.setRightEditable(rightEditable);
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(cc);
		emfCC.setEditingDomain(editingDomain);

		return emfCC;
	}

}
