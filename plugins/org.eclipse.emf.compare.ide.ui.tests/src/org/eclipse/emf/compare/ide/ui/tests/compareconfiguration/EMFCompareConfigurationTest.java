/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.compareconfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.junit.Test;

@SuppressWarnings({"restriction", "nls" })
public class EMFCompareConfigurationTest {

	@Test
	public void testEMFCompareConfigurationDefaultValues() {
		CompareConfiguration cc = new CompareConfiguration();
		cc.setLeftEditable(true);
		cc.setRightEditable(true);
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(cc);
		// From now, the following properties are not null:
		// PREVIEW_MERGE_MODE, SMV_FILTERS, SMV_GROUP_PROVIDERS, EVENT_BUS.
		Object preview_mode = emfCC.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".PREVIEW_MERGE_MODE");
		assertNotNull(preview_mode);
		Object smv_filters = emfCC.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".SMV_FILTERS");
		assertNotNull(smv_filters);
		Object smv_group_providers = emfCC.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID
				+ ".SMV_GROUP_PROVIDERS");
		assertNotNull(smv_group_providers);
		Object event_bus = emfCC.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".EVENT_BUS");
		assertNotNull(event_bus);
		// Simulate a change of viewer, so a new instantiation of EMFCompareConfiguration.
		EMFCompareConfiguration emfCC2 = new EMFCompareConfiguration(cc);
		Object preview_mode2 = emfCC2.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".PREVIEW_MERGE_MODE");
		assertEquals(preview_mode, preview_mode2);
		Object smv_filters2 = emfCC2.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".SMV_FILTERS");
		assertEquals(smv_filters, smv_filters2);
		Object smv_group_providers2 = emfCC2.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID
				+ ".SMV_GROUP_PROVIDERS");
		assertEquals(smv_group_providers, smv_group_providers2);
		Object event_bus2 = emfCC2.getProperty(EMFCompareIDEUIPlugin.PLUGIN_ID + ".EVENT_BUS");
		assertEquals(event_bus, event_bus2);
	}
}
