/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl.wizard;

import org.eclipse.emf.compare.epatch.dsl.Activator;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class SaveEpatchWizardPage extends SaveFileWizardPage {

	public SaveEpatchWizardPage() {
		super(SaveEpatchWizardPage.class.getName(), "Save Epatch", Activator
				.getImageDescriptor("icons/etool16/export_epatch_obj.gif"));
	}

}
